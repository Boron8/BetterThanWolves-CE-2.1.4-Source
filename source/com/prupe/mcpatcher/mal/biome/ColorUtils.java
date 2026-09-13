package com.prupe.mcpatcher.mal.biome;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ColorUtils {
   public static void intToFloat3(int rgb, float[] f, int offset) {
      if ((rgb & 16777215) == 16777215) {
         f[offset] = f[offset + 1] = f[offset + 2] = 1.0F;
      } else {
         f[offset] = (rgb & 0xFF0000) / 1.671168E7F;
         f[offset + 1] = (rgb & 0xFF00) / 65280.0F;
         f[offset + 2] = (rgb & 0xFF) / 255.0F;
      }
   }

   public static void intToFloat3(int rgb, float[] f) {
      intToFloat3(rgb, f, 0);
   }

   public static int float3ToInt(float[] f, int offset) {
      return (int)(255.0F * f[offset]) << 16 | (int)(255.0F * f[offset + 1]) << 8 | (int)(255.0F * f[offset + 2]);
   }

   public static int float3ToInt(float[] f) {
      return float3ToInt(f, 0);
   }

   public static float clamp(float f) {
      if (f < 0.0F) {
         return 0.0F;
      } else {
         return f > 1.0F ? 1.0F : f;
      }
   }

   public static double clamp(double d) {
      if (d < 0.0) {
         return 0.0;
      } else {
         return d > 1.0 ? 1.0 : d;
      }
   }

   public static void clamp(float[] f) {
      for (int i = 0; i < f.length; i++) {
         f[i] = clamp(f[i]);
      }
   }
}
