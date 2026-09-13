package net.minecraft.src;

import java.util.Random;

public class MathHelper {
   private static float[] SIN_TABLE = new float[65536];

   public static final float sin(float var0) {
      return SIN_TABLE[(int)(var0 * 10430.378F) & 65535];
   }

   public static final float cos(float var0) {
      return SIN_TABLE[(int)(var0 * 10430.378F + 16384.0F) & 65535];
   }

   public static final float sqrt_float(float var0) {
      return (float)Math.sqrt(var0);
   }

   public static final float sqrt_double(double var0) {
      return (float)Math.sqrt(var0);
   }

   public static int floor_float(float var0) {
      int var1 = (int)var0;
      return var0 < var1 ? var1 - 1 : var1;
   }

   public static int truncateDoubleToInt(double var0) {
      return (int)(var0 + 1024.0) - 1024;
   }

   public static int floor_double(double var0) {
      int var2 = (int)var0;
      return var0 < var2 ? var2 - 1 : var2;
   }

   public static long floor_double_long(double var0) {
      long var2 = (long)var0;
      return var0 < var2 ? var2 - 1L : var2;
   }

   public static float abs(float var0) {
      return var0 >= 0.0F ? var0 : -var0;
   }

   public static int abs_int(int var0) {
      return var0 >= 0 ? var0 : -var0;
   }

   public static int ceiling_float_int(float var0) {
      int var1 = (int)var0;
      return var0 > var1 ? var1 + 1 : var1;
   }

   public static int ceiling_double_int(double var0) {
      int var2 = (int)var0;
      return var0 > var2 ? var2 + 1 : var2;
   }

   public static int clamp_int(int var0, int var1, int var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   public static float clamp_float(float var0, float var1, float var2) {
      if (var0 < var1) {
         return var1;
      } else {
         return var0 > var2 ? var2 : var0;
      }
   }

   public static double abs_max(double var0, double var2) {
      if (var0 < 0.0) {
         var0 = -var0;
      }

      if (var2 < 0.0) {
         var2 = -var2;
      }

      return var0 > var2 ? var0 : var2;
   }

   public static int bucketInt(int var0, int var1) {
      return var0 < 0 ? -((-var0 - 1) / var1) - 1 : var0 / var1;
   }

   public static boolean stringNullOrLengthZero(String var0) {
      return var0 == null || var0.length() == 0;
   }

   public static int getRandomIntegerInRange(Random var0, int var1, int var2) {
      return var1 >= var2 ? var1 : var0.nextInt(var2 - var1 + 1) + var1;
   }

   public static double getRandomDoubleInRange(Random var0, double var1, double var3) {
      return var1 >= var3 ? var1 : var0.nextDouble() * (var3 - var1) + var1;
   }

   public static double average(long[] var0) {
      long var1 = 0L;

      for (long var6 : var0) {
         var1 += var6;
      }

      return (double)var1 / var0.length;
   }

   public static float wrapAngleTo180_float(float var0) {
      var0 %= 360.0F;
      if (var0 >= 180.0F) {
         var0 -= 360.0F;
      }

      if (var0 < -180.0F) {
         var0 += 360.0F;
      }

      return var0;
   }

   public static double wrapAngleTo180_double(double var0) {
      var0 %= 360.0;
      if (var0 >= 180.0) {
         var0 -= 360.0;
      }

      if (var0 < -180.0) {
         var0 += 360.0;
      }

      return var0;
   }

   public static int parseIntWithDefault(String var0, int var1) {
      int var2 = var1;

      try {
         var2 = Integer.parseInt(var0);
      } catch (Throwable var4) {
      }

      return var2;
   }

   public static int parseIntWithDefaultAndMax(String var0, int var1, int var2) {
      int var3 = var1;

      try {
         var3 = Integer.parseInt(var0);
      } catch (Throwable var5) {
      }

      if (var3 < var2) {
         var3 = var2;
      }

      return var3;
   }

   public static double parseDoubleWithDefault(String var0, double var1) {
      double var3 = var1;

      try {
         var3 = Double.parseDouble(var0);
      } catch (Throwable var6) {
      }

      return var3;
   }

   public static double func_82713_a(String var0, double var1, double var3) {
      double var5 = var1;

      try {
         var5 = Double.parseDouble(var0);
      } catch (Throwable var8) {
      }

      if (var5 < var3) {
         var5 = var3;
      }

      return var5;
   }

   static {
      for (int var0 = 0; var0 < 65536; var0++) {
         SIN_TABLE[var0] = (float)Math.sin(var0 * Math.PI * 2.0 / 65536.0);
      }
   }
}
