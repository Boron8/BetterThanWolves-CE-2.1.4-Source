package net.minecraft.src;

final class StatTypeSimple implements IStatType {
   @Override
   public String format(int var1) {
      return StatBase.getNumberFormat().format((long)var1);
   }
}
