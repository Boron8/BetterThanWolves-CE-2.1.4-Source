package net.minecraft.src;

public final class BossStatus {
   public static float healthScale;
   public static int statusBarLength;
   public static String bossName;
   public static boolean field_82825_d;

   public static void func_82824_a(IBossDisplayData var0, boolean var1) {
      healthScale = (float)var0.getBossHealth() / var0.getMaxHealth();
      statusBarLength = 100;
      bossName = var0.getEntityName();
      field_82825_d = var1;
   }
}
