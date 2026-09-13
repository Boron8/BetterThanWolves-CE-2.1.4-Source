package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.MCPatcherUtils;
import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.tile.IconAPI;
import com.prupe.mcpatcher.mal.util.InputHandler;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.imageio.ImageIO;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.TextureClock;
import net.minecraft.src.TextureCompass;
import net.minecraft.src.TextureStitched;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;

@Environment(EnvType.CLIENT)
public class FancyDial {
   private static final MCLogger logger = MCLogger.getLogger("Custom Animations", "Animation");
   private static final double ANGLE_UNSET = Double.MAX_VALUE;
   private static final int NUM_SCRATCH_TEXTURES = 3;
   private static final boolean fboSupported = GLContext.getCapabilities().GL_EXT_framebuffer_object;
   private static final boolean gl13Supported = GLContext.getCapabilities().OpenGL13;
   private static final boolean enableCompass = Config.getBoolean("Extended HD", "fancyCompass", true);
   private static final boolean enableClock = Config.getBoolean("Extended HD", "fancyClock", true);
   private static final boolean useGL13 = gl13Supported && Config.getBoolean("Extended HD", "useGL13", true);
   private static final boolean useScratchTexture = Config.getBoolean("Extended HD", "useScratchTexture", true);
   private static final int glAttributes;
   private static boolean initialized;
   private static boolean active;
   private static final int drawList = GL11.glGenLists(1);
   private static final Map<TextureStitched, FakeResourceLocation> setupInfo = new IdentityHashMap<>();
   private static final Map<TextureStitched, FancyDial> instances = new IdentityHashMap<>();
   private static int warnCount;
   private final TextureStitched icon;
   private final PropertiesFile properties;
   private final String name;
   private final int originX;
   private final int originY;
   private final int width;
   private final int height;
   private final ByteBuffer scratchBuffer;
   private final FancyDial.FBO[] scratchFBO = new FancyDial.FBO[3];
   private FancyDial.FBO itemsFBO;
   private int scratchIndex;
   private Map<Double, ByteBuffer> itemFrames = new TreeMap<>();
   private int outputFrames;
   private boolean ok;
   private double lastAngle = Double.MAX_VALUE;
   private boolean lastItemFrameRenderer;
   private final List<FancyDial.Layer> layers = new ArrayList<>();
   private InputHandler keyboard;
   private static final float STEP = 0.01F;
   private float scaleXDelta;
   private float scaleYDelta;
   private float offsetXDelta;
   private float offsetYDelta;

   public static void setup(TextureStitched icon) {
      if (fboSupported) {
         String name = IconAPI.getIconName(icon).replaceFirst("^minecraft:items/", "");
         if (icon instanceof TextureClock && name.equals("compass")) {
            name = "clock";
         }

         if ("compass".equals(name)) {
            if (!enableCompass) {
               return;
            }
         } else {
            if (!"clock".equals(name)) {
               logger.warning("ignoring custom animation for %s not compass or clock", name);
               return;
            }

            if (!enableClock) {
               return;
            }
         }

         FakeResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation("/misc/" + name + ".properties");
         if (TexturePackAPI.hasResource(resource)) {
            logger.fine("found custom %s (%s)", name, resource);
            setupInfo.put(icon, resource);
            active = true;
         }
      }
   }

   public static boolean update(TextureStitched icon, boolean itemFrameRenderer) {
      if (!initialized) {
         logger.finer("deferring %s update until initialization finishes", IconAPI.getIconName(icon));
         return false;
      } else if (!active) {
         return false;
      } else {
         int oldFB = GL11.glGetInteger(36006);
         if (oldFB != 0 && warnCount < 10) {
            logger.finer("rendering %s while non-default framebuffer %d is active", IconAPI.getIconName(icon), oldFB);
            warnCount++;
         }

         int oldTexture = GL11.glGetInteger(32873);

         boolean var5;
         try {
            FancyDial instance = getInstance(icon);
            var5 = instance != null && instance.render(itemFrameRenderer);
         } finally {
            EXTFramebufferObject.glBindFramebufferEXT(36160, oldFB);
            GLAPI.glBindTexture(oldTexture);
         }

         return var5;
      }
   }

   static void clearAll() {
      logger.finer("FancyDial.clearAll");
      if (initialized) {
         active = false;
         setupInfo.clear();
      }

      for (FancyDial instance : instances.values()) {
         if (instance != null) {
            instance.finish();
         }
      }

      instances.clear();
      initialized = true;
   }

   private static FancyDial getInstance(TextureStitched icon) {
      if (instances.containsKey(icon)) {
         return instances.get(icon);
      } else {
         FakeResourceLocation resource = setupInfo.remove(icon);
         instances.put(icon, null);
         if (resource == null) {
            return null;
         } else {
            PropertiesFile properties = PropertiesFile.get(logger, resource);
            if (properties == null) {
               return null;
            } else {
               try {
                  FancyDial instance = new FancyDial(icon, properties);
                  if (instance.ok) {
                     instances.put(icon, instance);
                     return instance;
                  }

                  instance.finish();
               } catch (Throwable var4) {
                  var4.printStackTrace();
               }

               return null;
            }
         }
      }
   }

   private FancyDial(TextureStitched icon, PropertiesFile properties) {
      this.icon = icon;
      this.properties = properties;
      this.name = IconAPI.getIconName(icon);
      this.originX = IconAPI.getIconOriginX(icon);
      this.originY = IconAPI.getIconOriginY(icon);
      this.width = IconAPI.getIconWidth(icon);
      this.height = IconAPI.getIconHeight(icon);
      this.scratchBuffer = ByteBuffer.allocateDirect(4 * this.width * this.height);
      int itemsTexture = TexturePackAPI.getTextureIfLoaded(TexturePackAPI.ITEMS_PNG);
      if (itemsTexture < 0) {
         logger.severe("could not get items texture");
      } else {
         this.itemsFBO = new FancyDial.FBO(itemsTexture, this.originX, this.originY, this.width, this.height);
         if (useScratchTexture) {
            logger.fine("rendering %s to %dx%d scratch texture", this.name, this.width, this.height);

            for (int i = 0; i < this.scratchFBO.length; i++) {
               this.scratchFBO[i] = new FancyDial.FBO(this.width, this.height);
            }
         } else {
            logger.fine("rendering %s directly to atlas", this.name);
         }

         boolean debug = false;
         int i = 0;

         while (true) {
            FancyDial.Layer layer = this.newLayer(properties, "." + i);
            if (layer == null) {
               if (i > 0) {
                  this.keyboard = new InputHandler(this.name, debug);
                  if (this.layers.size() < 2) {
                     logger.error("custom %s needs at least two layers defined", this.name);
                     return;
                  }

                  this.outputFrames = properties.getInt("outputFrames", 0);
                  i = GL11.glGetError();
                  if (i != 0) {
                     logger.severe("%s during %s setup", GLU.gluErrorString(i), this.name);
                     return;
                  }

                  this.ok = true;
                  return;
               }
            } else {
               this.layers.add(layer);
               debug |= layer.debug;
               logger.fine("  new %s", layer);
            }

            i++;
         }
      }
   }

   private boolean render(boolean itemFrameRenderer) {
      if (!this.ok) {
         return false;
      } else {
         if (!itemFrameRenderer) {
            boolean changed = true;
            if (!this.keyboard.isEnabled()) {
               changed = false;
            } else if (this.keyboard.isKeyPressed(80)) {
               this.scaleYDelta -= 0.01F;
            } else if (this.keyboard.isKeyPressed(72)) {
               this.scaleYDelta += 0.01F;
            } else if (this.keyboard.isKeyPressed(75)) {
               this.scaleXDelta -= 0.01F;
            } else if (this.keyboard.isKeyPressed(77)) {
               this.scaleXDelta += 0.01F;
            } else if (this.keyboard.isKeyPressed(208)) {
               this.offsetYDelta += 0.01F;
            } else if (this.keyboard.isKeyPressed(200)) {
               this.offsetYDelta -= 0.01F;
            } else if (this.keyboard.isKeyPressed(203)) {
               this.offsetXDelta -= 0.01F;
            } else if (this.keyboard.isKeyPressed(205)) {
               this.offsetXDelta += 0.01F;
            } else if (this.keyboard.isKeyPressed(55)) {
               this.scaleXDelta = this.scaleYDelta = this.offsetXDelta = this.offsetYDelta = 0.0F;
            } else {
               changed = false;
            }

            if (changed) {
               logger.info("");
               logger.info("scaleX  %+f", this.scaleXDelta);
               logger.info("scaleY  %+f", this.scaleYDelta);
               logger.info("offsetX %+f", this.offsetXDelta);
               logger.info("offsetY %+f", this.offsetYDelta);
               this.lastAngle = Double.MAX_VALUE;
            }

            if (this.outputFrames > 0) {
               this.writeCustomImage();
               this.outputFrames = 0;
            }
         }

         double angle = getAngle(this.icon);
         if (!useScratchTexture) {
            if (angle != this.lastAngle) {
               this.renderToItems(angle);
               this.lastAngle = angle;
            }
         } else if (itemFrameRenderer) {
            ByteBuffer buffer = this.itemFrames.get(angle);
            if (buffer == null) {
               logger.fine("rendering %s at angle %f for item frame", this.name, angle);
               buffer = ByteBuffer.allocateDirect(this.width * this.height * 4);
               this.renderToItems(angle);
               this.itemsFBO.read(buffer);
               this.itemFrames.put(angle, buffer);
            } else {
               this.itemsFBO.write(buffer);
            }

            this.lastItemFrameRenderer = true;
         } else if (this.lastAngle == Double.MAX_VALUE) {
            for (FancyDial.FBO fbo : this.scratchFBO) {
               this.renderToFB(angle, fbo);
            }

            this.scratchFBO[0].read(this.scratchBuffer);
            this.itemsFBO.write(this.scratchBuffer);
            this.lastAngle = angle;
            this.scratchIndex = 0;
         } else if (this.lastItemFrameRenderer || angle != this.lastAngle) {
            int nextIndex = (this.scratchIndex + 1) % 3;
            if (angle != this.lastAngle) {
               this.renderToFB(angle, this.scratchFBO[nextIndex]);
               this.scratchFBO[this.scratchIndex].read(this.scratchBuffer);
            }

            this.itemsFBO.write(this.scratchBuffer);
            this.lastAngle = angle;
            this.scratchIndex = nextIndex;
            this.lastItemFrameRenderer = false;
         }

         int glError = GL11.glGetError();
         if (glError != 0) {
            logger.severe("%s during %s update", GLU.gluErrorString(glError), this.name);
            this.ok = false;
         }

         return this.ok;
      }
   }

   private void writeCustomImage() {
      try {
         BufferedImage image = new BufferedImage(this.width, this.outputFrames * this.height, 2);
         IntBuffer intBuffer = this.scratchBuffer.asIntBuffer();
         int[] argb = new int[this.width * this.height];
         File path = MCPatcherUtils.getGamePath("custom_" + this.name + ".png");
         logger.info("generating %d %s frames", this.outputFrames, this.name);

         for (int i = 0; i < this.outputFrames; i++) {
            this.renderToItems(i * (360.0 / this.outputFrames));
            this.itemsFBO.read(this.scratchBuffer);
            ((Buffer)intBuffer).position(0);

            for (int j = 0; j < argb.length; j++) {
               switch ('\u80e1') {
                  case '\u80e1':
                     int bgra = intBuffer.get(j);
                     argb[j] = bgra << 24 | (bgra & 0xFF00) << 8 | (bgra & 0xFF0000) >> 8 | bgra >>> 24;
                     break;
                  default:
                     if (i == 0 && j == 0) {
                        logger.warning("unhandled texture format %d, color channels may be incorrect", 32993);
                     }
                  case '\u1908':
                     argb[j] = Integer.rotateRight(intBuffer.get(j), 8);
               }
            }

            image.setRGB(0, i * this.height, this.width, this.height, argb, 0, this.width);
         }

         ImageIO.write(image, "png", path);
         logger.info("wrote %dx%d %s", image.getWidth(), image.getHeight(), path.getPath());
      } catch (Throwable var8) {
         var8.printStackTrace();
      }
   }

   private void renderToItems(double angle) {
      this.renderToFB(angle, this.itemsFBO);
   }

   private void renderToFB(double angle, FancyDial.FBO fbo) {
      if (fbo != null) {
         fbo.bind();
         this.renderImpl(angle);
         fbo.unbind();
      }
   }

   private void renderImpl(double angle) {
      for (FancyDial.Layer layer : this.layers) {
         layer.blendMethod.applyBlending();
         GL11.glPushMatrix();
         TexturePackAPI.bindTexture(layer.textureName);
         float offsetX = layer.offsetX;
         float offsetY = layer.offsetY;
         float scaleX = layer.scaleX;
         float scaleY = layer.scaleY;
         if (layer.debug) {
            offsetX += this.offsetXDelta;
            offsetY += this.offsetYDelta;
            scaleX += this.scaleXDelta;
            scaleY += this.scaleYDelta;
         }

         GL11.glTranslatef(offsetX, offsetY, 0.0F);
         GL11.glScalef(scaleX, scaleY, 1.0F);
         float layerAngle = (float)(angle * layer.rotationMultiplier + layer.rotationOffset);
         GL11.glRotatef(layerAngle, 0.0F, 0.0F, 1.0F);
         GL11.glCallList(drawList);
         GL11.glPopMatrix();
      }
   }

   private static void drawBox() {
      GL11.glBegin(7);
      GL11.glTexCoord2f(0.0F, 0.0F);
      GL11.glVertex3f(-1.0F, -1.0F, 0.0F);
      GL11.glTexCoord2f(1.0F, 0.0F);
      GL11.glVertex3f(1.0F, -1.0F, 0.0F);
      GL11.glTexCoord2f(1.0F, 1.0F);
      GL11.glVertex3f(1.0F, 1.0F, 0.0F);
      GL11.glTexCoord2f(0.0F, 1.0F);
      GL11.glVertex3f(-1.0F, 1.0F, 0.0F);
      GL11.glEnd();
   }

   private void finish() {
      for (int i = 0; i < this.scratchFBO.length; i++) {
         if (this.scratchFBO[i] != null) {
            this.scratchFBO[i].delete();
            this.scratchFBO[i] = null;
         }
      }

      if (this.itemsFBO != null) {
         this.itemsFBO.delete();
         this.itemsFBO = null;
      }

      this.itemFrames.clear();
      this.layers.clear();
      this.ok = false;
   }

   @Override
   public String toString() {
      return String.format("FancyDial{%s, %dx%d @ %d,%d}", this.name, this.width, this.height, this.originX, this.originY);
   }

   @Override
   protected void finalize() throws Throwable {
      this.finish();
      super.finalize();
   }

   private static double getAngle(Icon icon) {
      if (icon instanceof TextureCompass) {
         return ((TextureCompass)icon).currentAngle * 180.0 / Math.PI;
      } else {
         return icon instanceof TextureClock ? ((TextureClock)icon).field_94239_h * 360.0 : 0.0;
      }
   }

   FancyDial.Layer newLayer(PropertiesFile properties, String suffix) {
      FakeResourceLocation textureResource = properties.getResourceLocation("source" + suffix, "");
      if (textureResource == null) {
         return null;
      } else if (!TexturePackAPI.hasResource(textureResource)) {
         properties.error("could not read %s", textureResource);
         return null;
      } else {
         float scaleX = properties.getFloat("scaleX" + suffix, 1.0F);
         float scaleY = properties.getFloat("scaleY" + suffix, 1.0F);
         float offsetX = properties.getFloat("offsetX" + suffix, 0.0F);
         float offsetY = properties.getFloat("offsetY" + suffix, 0.0F);
         float angleMultiplier = properties.getFloat("rotationSpeed" + suffix, 0.0F);
         float angleOffset = properties.getFloat("rotationOffset" + suffix, 0.0F);
         String blend = properties.getString("blend" + suffix, "alpha");
         BlendMethod blendMethod = BlendMethod.parse(blend);
         if (blendMethod == null) {
            properties.error("unknown blend method %s", blend);
            return null;
         } else {
            boolean debug = properties.getBoolean("debug" + suffix, false);
            return new FancyDial.Layer(textureResource, scaleX, scaleY, offsetX, offsetY, angleMultiplier, angleOffset, blendMethod, debug);
         }
      }
   }

   static {
      logger.config("fbo: supported=%s", fboSupported);
      logger.config("GL13: supported=%s, enabled=%s", gl13Supported, useGL13);
      int bits = 527702;
      if (useGL13) {
         bits |= 536870912;
      }

      glAttributes = bits;
      GL11.glNewList(drawList, 4864);
      drawBox();
      GL11.glEndList();
   }

   @Environment(EnvType.CLIENT)
   private static class FBO {
      private final int texture;
      private final boolean ownTexture;
      private final int x0;
      private final int y0;
      private final int width;
      private final int height;
      private final int frameBuffer;
      private boolean lightmapEnabled;
      private boolean deleted;

      FBO(int width, int height) {
         this(blankTexture(width, height), true, 0, 0, width, height);
      }

      FBO(int texture, int x0, int y0, int width, int height) {
         this(texture, false, x0, y0, width, height);
      }

      private FBO(int texture, boolean ownTexture, int x0, int y0, int width, int height) {
         this.texture = texture;
         this.ownTexture = ownTexture;
         this.x0 = x0;
         this.y0 = y0;
         this.width = width;
         this.height = height;
         this.frameBuffer = EXTFramebufferObject.glGenFramebuffersEXT();
         if (this.frameBuffer < 0) {
            throw new RuntimeException("could not get framebuffer object");
         } else {
            GLAPI.glBindTexture(texture);
            EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
            EXTFramebufferObject.glFramebufferTexture2DEXT(36160, 36064, 3553, texture, 0);
         }
      }

      void bind() {
         EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
         GL11.glPushAttrib(FancyDial.glAttributes);
         GL11.glViewport(this.x0, this.y0, this.width, this.height);
         GL11.glEnable(3089);
         GL11.glScissor(this.x0, this.y0, this.width, this.height);
         this.lightmapEnabled = false;
         if (FancyDial.gl13Supported) {
            GL13.glActiveTexture(33985);
            this.lightmapEnabled = GL11.glIsEnabled(3553);
            if (this.lightmapEnabled) {
               GL11.glDisable(3553);
            }

            GL13.glActiveTexture(33984);
         }

         GL11.glEnable(3553);
         GL11.glDisable(2929);
         GLAPI.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(2896);
         GL11.glEnable(3008);
         GLAPI.glAlphaFunc(516, 0.01F);
         if (FancyDial.useGL13) {
            GL11.glDisable(32925);
         }

         GLAPI.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
         GL11.glClear(16384);
         GL11.glMatrixMode(5889);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glOrtho(-1.0, 1.0, -1.0, 1.0, -1.0, 1.0);
         GL11.glMatrixMode(5888);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
      }

      void unbind() {
         GL11.glPopAttrib();
         GL11.glMatrixMode(5889);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
         GL11.glPopMatrix();
         if (this.lightmapEnabled) {
            GL13.glActiveTexture(33985);
            GL11.glEnable(3553);
            GL13.glActiveTexture(33984);
         }

         GL11.glEnable(3042);
         GLAPI.glBlendFunc(770, 771);
         EXTFramebufferObject.glBindFramebufferEXT(36160, 0);
      }

      void read(ByteBuffer buffer) {
         EXTFramebufferObject.glBindFramebufferEXT(36160, this.frameBuffer);
         ((Buffer)buffer).position(0);
         GL11.glReadPixels(this.x0, this.y0, this.width, this.height, 32993, 33639, buffer);
      }

      void write(ByteBuffer buffer) {
         GLAPI.glBindTexture(this.texture);
         ((Buffer)buffer).position(0);
         GL11.glTexSubImage2D(3553, 0, this.x0, this.y0, this.width, this.height, 32993, 33639, buffer);
      }

      void delete() {
         if (!this.deleted) {
            this.deleted = true;
            if (this.ownTexture) {
               GL11.glDeleteTextures(this.texture);
            }

            EXTFramebufferObject.glDeleteFramebuffersEXT(this.frameBuffer);
         }
      }

      @Override
      protected void finalize() throws Throwable {
         this.delete();
         super.finalize();
      }

      private static int blankTexture(int width, int height) {
         int texture = GL11.glGenTextures();
         MipmapHelper.setupTexture(texture, width, height, "scratch");
         return texture;
      }
   }

   @Environment(EnvType.CLIENT)
   private class Layer {
      final FakeResourceLocation textureName;
      final float scaleX;
      final float scaleY;
      final float offsetX;
      final float offsetY;
      final float rotationMultiplier;
      final float rotationOffset;
      final BlendMethod blendMethod;
      final boolean debug;

      Layer(
         FakeResourceLocation textureName,
         float scaleX,
         float scaleY,
         float offsetX,
         float offsetY,
         float rotationMultiplier,
         float rotationOffset,
         BlendMethod blendMethod,
         boolean debug
      ) {
         this.textureName = textureName;
         this.scaleX = scaleX;
         this.scaleY = scaleY;
         this.offsetX = offsetX;
         this.offsetY = offsetY;
         this.rotationMultiplier = rotationMultiplier;
         this.rotationOffset = rotationOffset;
         this.blendMethod = blendMethod;
         this.debug = debug;
      }

      @Override
      public String toString() {
         return String.format("Layer{%s %f %f %+f %+f x%f}", this.textureName, this.scaleX, this.scaleY, this.offsetX, this.offsetY, this.rotationMultiplier);
      }
   }
}
