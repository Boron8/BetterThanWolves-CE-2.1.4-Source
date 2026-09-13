package net.minecraft.src;

final class StatTypeDistance implements IStatType {
   @Override
   public String format(int var1) {
      double var2 = var1 / 100.0;
      double var4 = var2 / 1000.0;
      if (var4 > 0.5) {
         return StatBase.getDecimalFormat().format(var4) + " km";
      } else {
         return var2 > 0.5 ? StatBase.getDecimalFormat().format(var2) + " m" : var1 + " cm";
      }
   }
}
