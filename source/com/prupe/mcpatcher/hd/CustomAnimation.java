package com.prupe.mcpatcher.hd;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.ResourceList;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

@Environment(EnvType.CLIENT)
public class CustomAnimation implements Comparable<CustomAnimation> {
   private static final MCLogger logger = MCLogger.getLogger("Custom Animations", "Animation");
   private static final boolean enable = Config.getBoolean("Extended HD", "animations", true);
   private static final Set<PropertiesFile> pending = new HashSet<>();
   private static final List<CustomAnimation> animations = new ArrayList<>();
   private final PropertiesFile properties;
   private final FakeResourceLocation dstName;
   private final FakeResourceLocation srcName;
   private final int mipmapLevel;
   private final ByteBuffer imageData;
   private final int x;
   private final int y;
   private final int w;
   private final int h;
   private int currentFrame;
   private int currentDelay;
   private int numFrames;
   private int[] tileOrder;
   private int[] tileDelay;
   private final int numTiles;
   private boolean error;

   public static void updateAll() {
      if (!pending.isEmpty()) {
         try {
            checkPendingAnimations();
         } catch (Throwable var2) {
            var2.printStackTrace();
            logger.error("%d remaining animations cleared", pending.size());
            pending.clear();
         }
      }

      for (CustomAnimation animation : animations) {
         animation.update();
      }
   }

   private static void checkPendingAnimations() {
      List<PropertiesFile> done = new ArrayList<>();

      for (PropertiesFile properties : pending) {
         FakeResourceLocation textureName = properties.getResourceLocation("to", "");
         if (TexturePackAPI.isTextureLoaded(textureName)) {
            addStrip(properties);
            done.add(properties);
         }
      }

      if (!done.isEmpty()) {
         for (PropertiesFile name : done) {
            pending.remove(name);
         }

         Collections.sort(animations);
      }
   }

   private static void addStrip(PropertiesFile properties) {
      FakeResourceLocation dstName = properties.getResourceLocation("to", "");
      if (dstName == null) {
         properties.error("missing to= property");
      } else {
         FakeResourceLocation srcName = properties.getResourceLocation("from", "");
         if (srcName == null) {
            properties.error("missing from= property");
         } else {
            BufferedImage srcImage = TexturePackAPI.getImage(srcName);
            if (srcImage == null) {
               properties.error("image %s not found in texture pack", srcName);
            } else {
               int x = properties.getInt("x", 0);
               int y = properties.getInt("y", 0);
               int w = properties.getInt("w", 0);
               int h = properties.getInt("h", 0);
               if (dstName.toString().startsWith("minecraft:textures/atlas/")) {
                  properties.error("animations cannot have a target of %s", dstName);
               } else if (x >= 0 && y >= 0 && w > 0 && h > 0) {
                  TexturePackAPI.bindTexture(dstName);
                  int dstWidth = GL11.glGetTexLevelParameteri(3553, 0, 4096);
                  int dstHeight = GL11.glGetTexLevelParameteri(3553, 0, 4097);
                  int levels = MipmapHelper.getMipmapLevelsForCurrentTexture();
                  if (x + w <= dstWidth && y + h <= dstHeight) {
                     int width = srcImage.getWidth();
                     int height = srcImage.getHeight();
                     if (width != w) {
                        srcImage = resizeImage(srcImage, w);
                        width = srcImage.getWidth();
                        height = srcImage.getHeight();
                     }

                     if (width == w && height >= h) {
                        ByteBuffer imageData = ByteBuffer.allocateDirect(4 * width * height);
                        int[] argb = new int[width * height];
                        byte[] rgba = new byte[4 * width * height];
                        srcImage.getRGB(0, 0, width, height, argb, 0, width);
                        ARGBtoRGBA(argb, rgba);
                        ((Buffer)imageData.put(rgba)).flip();

                        for (int mipmapLevel = 0; mipmapLevel <= levels; mipmapLevel++) {
                           add(new CustomAnimation(properties, srcName, dstName, mipmapLevel, x, y, w, h, imageData, height / h));
                           if (((x | y | w | h) & 1) != 0 || w <= 0 || h <= 0) {
                              break;
                           }

                           ByteBuffer newImage = ByteBuffer.allocateDirect(width * height);
                           MipmapHelper.scaleHalf(imageData.asIntBuffer(), width, height, newImage.asIntBuffer(), 0);
                           imageData = newImage;
                           width >>= 1;
                           height >>= 1;
                           x >>= 1;
                           y >>= 1;
                           w >>= 1;
                           h >>= 1;
                        }
                     } else {
                        properties.error("%s dimensions %dx%d do not match %dx%d", srcName, width, height, w, h);
                     }
                  } else {
                     properties.error("%s dimensions x=%d,y=%d,w=%d,h=%d exceed %s size %dx%d", srcName, x, y, w, h, dstName, dstWidth, dstHeight);
                  }
               } else {
                  properties.error("%s has invalid dimensions x=%d,y=%d,w=%d,h=%d", srcName, x, y, w, h);
               }
            }
         }
      }
   }

   private static void add(CustomAnimation animation) {
      if (animation != null) {
         animations.add(animation);
         if (animation.mipmapLevel == 0) {
            logger.fine("new %s", animation);
         }
      }
   }

   private CustomAnimation(
      PropertiesFile properties,
      FakeResourceLocation srcName,
      FakeResourceLocation dstName,
      int mipmapLevel,
      int x,
      int y,
      int w,
      int h,
      ByteBuffer imageData,
      int numFrames
   ) {
      this.properties = properties;
      this.srcName = srcName;
      this.dstName = dstName;
      this.mipmapLevel = mipmapLevel;
      this.x = x;
      this.y = y;
      this.w = w;
      this.h = h;
      this.imageData = imageData;
      this.numFrames = numFrames;
      this.currentFrame = -1;
      this.numTiles = numFrames;
      this.loadProperties(properties);
   }

   void update() {
      if (!this.error) {
         int texture = TexturePackAPI.getTextureIfLoaded(this.dstName);
         if (texture >= 0) {
            if (--this.currentDelay <= 0) {
               if (++this.currentFrame >= this.numFrames) {
                  this.currentFrame = 0;
               }

               GLAPI.glBindTexture(texture);
               this.update(texture, 0, 0);
               int glError = GL11.glGetError();
               if (glError != 0) {
                  logger.severe("%s: %s", this, GLU.gluErrorString(glError));
                  this.error = true;
               } else {
                  this.currentDelay = this.getDelay();
               }
            }
         }
      }
   }

   public int compareTo(CustomAnimation o) {
      return this.dstName.toString().compareTo(o.dstName.toString());
   }

   @Override
   public String toString() {
      return String.format(
         "CustomAnimation{%s %s %dx%d -> %s%s @ %d,%d (%d frames)}",
         this.properties,
         this.srcName,
         this.w,
         this.h,
         this.dstName,
         this.mipmapLevel > 0 ? "#" + this.mipmapLevel : "",
         this.x,
         this.y,
         this.numFrames
      );
   }

   private static void ARGBtoRGBA(int[] src, byte[] dest) {
      for (int i = 0; i < src.length; i++) {
         int v = src[i];
         dest[i * 4 + 3] = (byte)(v >> 24 & 0xFF);
         dest[i * 4 + 0] = (byte)(v >> 16 & 0xFF);
         dest[i * 4 + 1] = (byte)(v >> 8 & 0xFF);
         dest[i * 4 + 2] = (byte)(v >> 0 & 0xFF);
      }
   }

   private static BufferedImage resizeImage(BufferedImage image, int width) {
      if (width == image.getWidth()) {
         return image;
      } else {
         int height = image.getHeight() * width / image.getWidth();
         logger.finer("resizing to %dx%d", width, height);
         BufferedImage newImage = new BufferedImage(width, height, 2);
         Graphics2D graphics2D = newImage.createGraphics();
         graphics2D.drawImage(image, 0, 0, width, height, null);
         return newImage;
      }
   }

   private void loadProperties(PropertiesFile properties) {
      this.loadTileOrder(properties);
      if (this.tileOrder == null) {
         this.tileOrder = new int[this.numFrames];

         for (int i = 0; i < this.numFrames; i++) {
            this.tileOrder[i] = i % this.numTiles;
         }
      }

      this.tileDelay = new int[this.numFrames];
      this.loadTileDelay(properties);

      for (int i = 0; i < this.numFrames; i++) {
         this.tileDelay[i] = Math.max(this.tileDelay[i], 1);
      }
   }

   private void loadTileOrder(PropertiesFile properties) {
      if (properties != null) {
         int i = 0;

         while (getIntValue(properties, "tile.", i) != null) {
            i++;
         }

         if (i > 0) {
            this.numFrames = i;
            this.tileOrder = new int[this.numFrames];

            for (int var3 = 0; var3 < this.numFrames; var3++) {
               this.tileOrder[var3] = Math.abs(getIntValue(properties, "tile.", var3)) % this.numTiles;
            }
         }
      }
   }

   private void loadTileDelay(PropertiesFile properties) {
      if (properties != null) {
         Integer defaultValue = getIntValue(properties, "duration");

         for (int i = 0; i < this.numFrames; i++) {
            Integer value = getIntValue(properties, "duration.", i);
            if (value != null) {
               this.tileDelay[i] = value;
            } else if (defaultValue != null) {
               this.tileDelay[i] = defaultValue;
            }
         }
      }
   }

   private static Integer getIntValue(PropertiesFile properties, String key) {
      try {
         String value = properties.getString(key, "");
         if (value != null && value.matches("^\\d+$")) {
            return Integer.parseInt(value);
         }
      } catch (NumberFormatException var3) {
      }

      return null;
   }

   private static Integer getIntValue(PropertiesFile properties, String prefix, int index) {
      return getIntValue(properties, prefix + index);
   }

   private void update(int texture, int dx, int dy) {
      GL11.glTexSubImage2D(
         3553,
         this.mipmapLevel,
         this.x + dx,
         this.y + dy,
         this.w,
         this.h,
         6408,
         5121,
         (ByteBuffer)((Buffer)this.imageData).position(4 * this.w * this.h * this.tileOrder[this.currentFrame])
      );
   }

   private int getDelay() {
      return this.tileDelay[this.currentFrame];
   }

   static {
      TexturePackChangeHandler.register(new TexturePackChangeHandler("Extended HD", 1) {
         @Override
         public void beforeChange() {
            if (!CustomAnimation.pending.isEmpty()) {
               CustomAnimation.logger.fine("%d animations were never registered:", CustomAnimation.pending.size());

               for (PropertiesFile properties : CustomAnimation.pending) {
                  CustomAnimation.logger.fine("  %s", properties);
               }

               CustomAnimation.pending.clear();
            }

            CustomAnimation.animations.clear();
            MipmapHelper.reset();
            FancyDial.clearAll();
         }

         @Override
         public void afterChange() {
            if (CustomAnimation.enable) {
               for (FakeResourceLocation resource : ResourceList.getInstance().listResources("/anim", ".properties", false)) {
                  PropertiesFile properties = PropertiesFile.get(CustomAnimation.logger, resource);
                  if (properties != null) {
                     CustomAnimation.pending.add(properties);
                  }
               }
            }
         }
      });
   }
}
