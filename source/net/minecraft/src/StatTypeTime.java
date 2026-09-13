package net.minecraft.src;

final class StatTypeTime implements IStatType {
   @Override
   public String format(int var1) {
      double var2 = var1 / 20.0;
      double var4 = var2 / 60.0;
      double var6 = var4 / 60.0;
      double var8 = var6 / 24.0;
      double var10 = var8 / 365.0;
      if (var10 > 0.5) {
         return StatBase.getDecimalFormat().format(var10) + " y";
      } else if (var8 > 0.5) {
         return StatBase.getDecimalFormat().format(var8) + " d";
      } else if (var6 > 0.5) {
         return StatBase.getDecimalFormat().format(var6) + " h";
      } else {
         return var4 > 0.5 ? StatBase.getDecimalFormat().format(var4) + " m" : var2 + " s";
      }
   }
}
