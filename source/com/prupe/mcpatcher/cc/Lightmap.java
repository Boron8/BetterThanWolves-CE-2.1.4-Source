package com.prupe.mcpatcher.cc;

import com.prupe.mcpatcher.Config;
import com.prupe.mcpatcher.MCLogger;
import com.prupe.mcpatcher.mal.biome.ColorUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mal.resource.TexturePackAPI;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.World;

@Environment(EnvType.CLIENT)
public final class Lightmap {
   private static final MCLogger logger = MCLogger.getLogger("Custom Colors");
   private static final String LIGHTMAP_FORMAT1 = "/environment/lightmap%d.png";
   private static final int LIGHTMAP_SIZE = 16;
   private static final int HEIGHT_WITHOUT_NIGHTVISION = 32;
   private static final int HEIGHT_WITH_NIGHTVISION = 64;
   private static final boolean useLightmaps = Config.getBoolean("Custom Colors", "lightmaps", true);
   private static final HashMap<Integer, Lightmap> lightmaps = new HashMap<>();
   private final int width;
   private final boolean customNightvision;
   private final int[] origMap;
   private final boolean valid;
   private final float[] sunrgb = new float[48];
   private final float[] torchrgb = new float[48];
   private final float[] sunrgbnv = new float[48];
   private final float[] torchrgbnv = new float[48];
   private final float[] rgb = new float[3];

   static void reset() {
      lightmaps.clear();
   }

   public static boolean computeLightmap(EntityRenderer renderer, World world, int[] mapRGB, float partialTick) {
      if (world != null && useLightmaps) {
         Lightmap lightmap = null;
         int worldType = world.provider.getWorldType();
         if (lightmaps.containsKey(worldType)) {
            lightmap = lightmaps.get(worldType);
         } else {
            FakeResourceLocation resource = TexturePackAPI.newMCPatcherResourceLocation(String.format("/environment/lightmap%d.png", worldType));
            BufferedImage image = TexturePackAPI.getImage(resource);
            if (image != null) {
               lightmap = new Lightmap(resource, image);
               if (!lightmap.valid) {
                  lightmap = null;
               }
            }

            lightmaps.put(worldType, lightmap);
         }

         return lightmap != null && lightmap.compute(renderer, world, mapRGB, partialTick);
      } else {
         return false;
      }
   }

   private Lightmap(FakeResourceLocation resource, BufferedImage image) {
      this.width = image.getWidth();
      int height = image.getHeight();
      this.customNightvision = height == 64;
      this.origMap = new int[this.width * height];
      image.getRGB(0, 0, this.width, height, this.origMap, 0, this.width);
      this.valid = height == 32 || height == 64;
      if (!this.valid) {
         logger.error("%s must be exactly %d or %d pixels high", resource, 32, 64);
      }
   }

   private boolean compute(EntityRenderer renderer, World world, int[] mapRGB, float partialTick) {
      float sun = ColorUtils.clamp(world.lastLightningBolt > 0 ? 1.0F : 1.1666666F * (world.getSunBrightness(1.0F) - 0.2F)) * (this.width - 1);
      float torch = ColorUtils.clamp(renderer.torchFlickerX + 0.5F) * (this.width - 1);
      float nightVisionStrength = renderer.getNightVisionStrength(partialTick);
      float gamma = ColorUtils.clamp(Minecraft.getMinecraft().gameSettings.gammaSetting);

      for (int i = 0; i < 16; i++) {
         interpolate(this.origMap, i * this.width, sun, this.sunrgb, 3 * i);
         interpolate(this.origMap, (i + 16) * this.width, torch, this.torchrgb, 3 * i);
         if (this.customNightvision && nightVisionStrength > 0.0F) {
            interpolate(this.origMap, (i + 32) * this.width, sun, this.sunrgbnv, 3 * i);
            interpolate(this.origMap, (i + 48) * this.width, torch, this.torchrgbnv, 3 * i);
         }
      }

      for (int s = 0; s < 16; s++) {
         for (int t = 0; t < 16; t++) {
            for (int k = 0; k < 3; k++) {
               this.rgb[k] = ColorUtils.clamp(this.sunrgb[3 * s + k] + this.torchrgb[3 * t + k]);
            }

            if (nightVisionStrength > 0.0F) {
               if (this.customNightvision) {
                  for (int k = 0; k < 3; k++) {
                     this.rgb[k] = ColorUtils.clamp(
                        (1.0F - nightVisionStrength) * this.rgb[k] + nightVisionStrength * (this.sunrgbnv[3 * s + k] + this.torchrgbnv[3 * t + k])
                     );
                  }
               } else {
                  float nightVisionMultiplier = Math.max(Math.max(this.rgb[0], this.rgb[1]), this.rgb[2]);
                  if (nightVisionMultiplier > 0.0F) {
                     nightVisionMultiplier = 1.0F - nightVisionStrength + nightVisionStrength / nightVisionMultiplier;

                     for (int k = 0; k < 3; k++) {
                        this.rgb[k] = ColorUtils.clamp(this.rgb[k] * nightVisionMultiplier);
                     }
                  }
               }
            }

            if (gamma != 0.0F) {
               for (int k = 0; k < 3; k++) {
                  float tmp = 1.0F - this.rgb[k];
                  tmp = 1.0F - tmp * tmp * tmp * tmp;
                  this.rgb[k] = gamma * tmp + (1.0F - gamma) * this.rgb[k];
               }
            }

            mapRGB[s * 16 + t] = 0xFF000000 | ColorUtils.float3ToInt(this.rgb);
         }
      }

      return true;
   }

   private static void interpolate(int[] map, int offset1, float x, float[] rgb, int offset2) {
      int x0 = (int)Math.floor(x);
      int x1 = (int)Math.ceil(x);
      if (x0 == x1) {
         ColorUtils.intToFloat3(map[offset1 + x0], rgb, offset2);
      } else {
         float xf = x - x0;
         float xg = 1.0F - xf;
         float[] rgb0 = new float[3];
         float[] rgb1 = new float[3];
         ColorUtils.intToFloat3(map[offset1 + x0], rgb0);
         ColorUtils.intToFloat3(map[offset1 + x1], rgb1);

         for (int i = 0; i < 3; i++) {
            rgb[offset2 + i] = xg * rgb0[i] + xf * rgb1[i];
         }
      }
   }
}
