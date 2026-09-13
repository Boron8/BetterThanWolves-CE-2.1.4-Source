package com.prupe.mcpatcher.sky;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.resource.BlendMethod;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.GLAPI;
import com.prupe.mcpatcher.mal.resource.PropertiesFile;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import com.prupe.mcpatcher.mal.resource.TexturePackChangeHandler;
import com.prupe.mcpatcher.mal.tessellator.TessellatorAPI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class SkyRenderer {
   private static final MCLogger logger = MCLogger.getLogger("Better Skies");
   private static final boolean enable = Config.getBoolean("Better Skies", "skybox", true);
   private static final boolean unloadTextures = Config.getBoolean("Better Skies", "unloadTextures", true);
   public static final double horizonHeight = Config.getInt("Better Skies", "horizon", 16);
   private static double worldTime;
   private static float celestialAngle;
   private static float rainStrength;
   private static final HashMap<Integer, SkyRenderer.WorldEntry> worldSkies = new HashMap<>();
   private static SkyRenderer.WorldEntry currentWorld;
   public static boolean active;

   public static void setup(World world, float partialTick, float celestialAngle) {
      if (TexturePackAPI.isDefaultTexturePack()) {
         active = false;
      } else {
         int worldType = Minecraft.getMinecraft().theWorld.provider.getWorldType();
         SkyRenderer.WorldEntry newEntry = getWorldEntry(worldType);
         if (newEntry != currentWorld && currentWorld != null) {
            currentWorld.unloadTextures();
         }

         currentWorld = newEntry;
         active = currentWorld.active();
         if (active) {
            worldTime = (float)world.getWorldTime() + partialTick;
            rainStrength = 1.0F - world.getRainStrength(partialTick);
            SkyRenderer.celestialAngle = celestialAngle;
         }
      }
   }

   public static void renderAll() {
      if (active) {
         currentWorld.renderAll(TessellatorAPI.getTessellator());
      }
   }

   public static FakeResourceLocation setupCelestialObject(FakeResourceLocation defaultTexture) {
      if (active) {
         SkyRenderer.Layer.clearBlendingMethod();
         SkyRenderer.Layer layer = currentWorld.getCelestialObject(defaultTexture);
         if (layer != null) {
            layer.setBlendingMethod(rainStrength);
            return layer.texture;
         }
      }

      return defaultTexture;
   }

   private static SkyRenderer.WorldEntry getWorldEntry(int worldType) {
      SkyRenderer.WorldEntry entry = worldSkies.get(worldType);
      if (entry == null) {
         entry = new SkyRenderer.WorldEntry(worldType);
         worldSkies.put(worldType, entry);
      }

      return entry;
   }

   static {
      TexturePackChangeHandler.register(new TexturePackChangeHandler("Better Skies", 2) {
         @Override
         public void beforeChange() {
            SkyRenderer.worldSkies.clear();
         }

         @Override
         public void afterChange() {
            if (SkyRenderer.enable) {
               World world = Minecraft.getMinecraft().theWorld;
               if (world != null) {
                  SkyRenderer.getWorldEntry(world.provider.getWorldType());
               }
            }

            FireworksHelper.reload();
         }
      });
   }

   @Environment(EnvType.CLIENT)
   private static class Layer {
      private static final int SECS_PER_DAY = 86400;
      private static final int TICKS_PER_DAY = 24000;
      private static final double TOD_OFFSET = -0.25;
      private static final double SKY_DISTANCE = 100.0;
      private final PropertiesFile properties;
      private FakeResourceLocation texture;
      private boolean fade;
      private boolean rotate;
      private float[] axis;
      private float speed;
      private BlendMethod blendMethod;
      private double a;
      private double b;
      private double c;
      float brightness;

      static SkyRenderer.Layer create(FakeResourceLocation resource) {
         PropertiesFile properties = PropertiesFile.get(SkyRenderer.logger, resource);
         return properties == null ? null : new SkyRenderer.Layer(properties);
      }

      Layer(PropertiesFile properties) {
         this.properties = properties;
         if (this.readTexture() && this.readRotation() & this.readBlendingMethod() && this.readFadeTimers()) {
            boolean var3 = true;
         } else {
            boolean var10000 = false;
         }
      }

      private boolean readTexture() {
         this.texture = this.properties.getResourceLocation("source", this.properties.toString().replaceFirst("\\.properties$", ".png"));
         return TexturePackAPI.hasResource(this.texture) ? true : this.properties.error("source texture %s not found", this.texture);
      }

      private boolean readRotation() {
         this.rotate = this.properties.getBoolean("rotate", true);
         if (this.rotate) {
            this.speed = this.properties.getFloat("speed", 1.0F);
            String value = this.properties.getString("axis", "0.0 0.0 1.0");
            String[] tokens = value.split("\\s+");
            if (tokens.length != 3) {
               return this.properties.error("invalid rotate value %s", value);
            }

            float x;
            float y;
            float z;
            try {
               x = Float.parseFloat(tokens[0]);
               y = Float.parseFloat(tokens[1]);
               z = Float.parseFloat(tokens[2]);
            } catch (NumberFormatException var7) {
               return this.properties.error("invalid rotation axis");
            }

            if (x * x + y * y + z * z == 0.0F) {
               return this.properties.error("rotation axis cannot be 0");
            }

            this.axis = new float[]{z, y, -x};
         }

         return true;
      }

      private boolean readBlendingMethod() {
         String value = this.properties.getString("blend", "add");
         this.blendMethod = BlendMethod.parse(value);
         return this.blendMethod == null ? this.properties.error("unknown blend method %s", value) : true;
      }

      private boolean readFadeTimers() {
         this.fade = this.properties.getBoolean("fade", true);
         if (!this.fade) {
            return true;
         } else {
            int startFadeIn = this.parseTime(this.properties, "startFadeIn");
            int endFadeIn = this.parseTime(this.properties, "endFadeIn");
            int endFadeOut = this.parseTime(this.properties, "endFadeOut");
            if (!this.properties.valid()) {
               return false;
            } else {
               while (endFadeIn <= startFadeIn) {
                  endFadeIn += 86400;
               }

               while (endFadeOut <= endFadeIn) {
                  endFadeOut += 86400;
               }

               if (endFadeOut - startFadeIn >= 86400) {
                  return this.properties.error("fade times must fall within a 24 hour period");
               } else {
                  int startFadeOut = startFadeIn + endFadeOut - endFadeIn;
                  double s0 = normalize(startFadeIn, 86400, -0.25);
                  double s1 = normalize(endFadeIn, 86400, -0.25);
                  double e0 = normalize(startFadeOut, 86400, -0.25);
                  double e1 = normalize(endFadeOut, 86400, -0.25);
                  double det = Math.cos(s0) * Math.sin(s1)
                     + Math.cos(e1) * Math.sin(s0)
                     + Math.cos(s1) * Math.sin(e1)
                     - Math.cos(s0) * Math.sin(e1)
                     - Math.cos(s1) * Math.sin(s0)
                     - Math.cos(e1) * Math.sin(s1);
                  if (det == 0.0) {
                     return this.properties.error("determinant is 0");
                  } else {
                     this.a = (Math.sin(e1) - Math.sin(s0)) / det;
                     this.b = (Math.cos(s0) - Math.cos(e1)) / det;
                     this.c = (Math.cos(e1) * Math.sin(s0) - Math.cos(s0) * Math.sin(e1)) / det;
                     SkyRenderer.logger.finer("%s: y = %f cos x + %f sin x + %f", this.properties, this.a, this.b, this.c);
                     SkyRenderer.logger.finer("  at %f: %f", s0, this.f(s0));
                     SkyRenderer.logger.finer("  at %f: %f", s1, this.f(s1));
                     SkyRenderer.logger.finer("  at %f: %f", e0, this.f(e0));
                     SkyRenderer.logger.finer("  at %f: %f", e1, this.f(e1));
                     return true;
                  }
               }
            }
         }
      }

      private int parseTime(PropertiesFile properties, String key) {
         String s = properties.getString(key, "");
         if ("".equals(s)) {
            properties.error("missing value for %s", key);
            return -1;
         } else {
            String[] t = s.split(":");
            if (t.length >= 2) {
               try {
                  int hh = Integer.parseInt(t[0].trim());
                  int mm = Integer.parseInt(t[1].trim());
                  int ss;
                  if (t.length >= 3) {
                     ss = Integer.parseInt(t[2].trim());
                  } else {
                     ss = 0;
                  }

                  return (3600 * hh + 60 * mm + ss) % 86400;
               } catch (NumberFormatException var8) {
               }
            }

            properties.error("invalid %s time %s", key, s);
            return -1;
         }
      }

      private static double normalize(double time, int period, double offset) {
         return (Math.PI * 2) * (time / period + offset);
      }

      private double f(double x) {
         return this.a * Math.cos(x) + this.b * Math.sin(x) + this.c;
      }

      boolean prepare() {
         this.brightness = SkyRenderer.rainStrength;
         if (this.fade) {
            double x = normalize(SkyRenderer.worldTime, 24000, 0.0);
            this.brightness = this.brightness * (float)this.f(x);
         }

         if (this.brightness <= 0.0F) {
            return false;
         } else {
            if (this.brightness > 1.0F) {
               this.brightness = 1.0F;
            }

            return true;
         }
      }

      boolean render(Tessellator tessellator) {
         TexturePackAPI.bindTexture(this.texture);
         this.setBlendingMethod(this.brightness);
         GL11.glPushMatrix();
         if (this.rotate) {
            GL11.glRotatef(SkyRenderer.celestialAngle * 360.0F * this.speed, this.axis[0], this.axis[1], this.axis[2]);
         }

         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
         drawTile(tessellator, 4);
         GL11.glPushMatrix();
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         drawTile(tessellator, 1);
         GL11.glPopMatrix();
         GL11.glPushMatrix();
         GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
         drawTile(tessellator, 0);
         GL11.glPopMatrix();
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         drawTile(tessellator, 5);
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         drawTile(tessellator, 2);
         GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
         drawTile(tessellator, 3);
         GL11.glPopMatrix();
         return true;
      }

      private static void drawTile(Tessellator tessellator, int tile) {
         double tileX = tile % 3 / 3.0;
         double tileY = tile / 3 / 2.0;
         TessellatorAPI.startDrawingQuads(tessellator);
         TessellatorAPI.addVertexWithUV(tessellator, -100.0, -100.0, -100.0, tileX, tileY);
         TessellatorAPI.addVertexWithUV(tessellator, -100.0, -100.0, 100.0, tileX, tileY + 0.5);
         TessellatorAPI.addVertexWithUV(tessellator, 100.0, -100.0, 100.0, tileX + 0.3333333333333333, tileY + 0.5);
         TessellatorAPI.addVertexWithUV(tessellator, 100.0, -100.0, -100.0, tileX + 0.3333333333333333, tileY);
         TessellatorAPI.draw(tessellator);
      }

      void setBlendingMethod(float brightness) {
         this.blendMethod.applyFade(brightness);
         this.blendMethod.applyAlphaTest();
         this.blendMethod.applyBlending();
         GL11.glEnable(3553);
      }

      static void clearBlendingMethod() {
         GL11.glDisable(3008);
         GL11.glEnable(3042);
         GLAPI.glBlendFunc(770, 1);
         GLAPI.glColor4f(1.0F, 1.0F, 1.0F, SkyRenderer.rainStrength);
      }
   }

   @Environment(EnvType.CLIENT)
   private static class WorldEntry {
      private final int worldType;
      private final List<SkyRenderer.Layer> skies = new ArrayList<>();
      private final Map<FakeResourceLocation, SkyRenderer.Layer> objects = new HashMap<>();
      private final Set<FakeResourceLocation> textures = new HashSet<>();

      WorldEntry(int worldType) {
         this.worldType = worldType;
         this.loadSkies();
         this.loadCelestialObject("sun");
         this.loadCelestialObject("moon_phases");
      }

      private void loadSkies() {
         int i = -1;

         while (true) {
            String path = "/environment/sky" + this.worldType + "/sky" + (i < 0 ? "" : String.valueOf(i)) + ".properties";
            FakeResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation(path);
            SkyRenderer.Layer layer = SkyRenderer.Layer.create(resource);
            if (layer == null) {
               if (i > 0) {
                  return;
               }
            } else if (layer.properties.valid()) {
               SkyRenderer.logger.fine("loaded %s", resource);
               this.skies.add(layer);
               this.textures.add(layer.texture);
            }

            i++;
         }
      }

      private void loadCelestialObject(String objName) {
         FakeResourceLocation textureName = new FakeResourceLocation("/environment/" + objName + ".png");
         String path = "/environment/sky" + this.worldType + "/" + objName + ".properties";
         FakeResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation(path);
         PropertiesFile properties = PropertiesFile.get(SkyRenderer.logger, resource);
         if (properties != null) {
            properties.setProperty("fade", "false");
            properties.setProperty("rotate", "true");
            SkyRenderer.Layer layer = new SkyRenderer.Layer(properties);
            if (properties.valid()) {
               SkyRenderer.logger.fine("using %s (%s) for the %s", resource, layer.texture, objName);
               this.objects.put(textureName, layer);
            }
         }
      }

      boolean active() {
         return !this.skies.isEmpty() || !this.objects.isEmpty();
      }

      void renderAll(Tessellator tessellator) {
         if (SkyRenderer.unloadTextures) {
            Set<FakeResourceLocation> texturesNeeded = new HashSet<>();

            for (SkyRenderer.Layer layer : this.skies) {
               if (layer.prepare()) {
                  texturesNeeded.add(layer.texture);
               }
            }

            Set<FakeResourceLocation> texturesToUnload = new HashSet<>();
            texturesToUnload.addAll(this.textures);
            texturesToUnload.removeAll(texturesNeeded);

            for (FakeResourceLocation resource : texturesToUnload) {
               TexturePackAPI.unloadTexture(resource);
            }
         }

         for (SkyRenderer.Layer layerx : this.skies) {
            if (!SkyRenderer.unloadTextures) {
               layerx.prepare();
            }

            if (layerx.brightness > 0.0F) {
               layerx.render(tessellator);
               SkyRenderer.Layer.clearBlendingMethod();
            }
         }
      }

      SkyRenderer.Layer getCelestialObject(FakeResourceLocation defaultTexture) {
         return this.objects.get(defaultTexture);
      }

      void unloadTextures() {
         for (SkyRenderer.Layer layer : this.skies) {
            TexturePackAPI.unloadTexture(layer.texture);
         }
      }
   }
}
