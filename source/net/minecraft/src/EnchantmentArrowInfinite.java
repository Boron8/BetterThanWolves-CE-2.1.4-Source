package net.minecraft.src;

public class EnchantmentArrowInfinite extends Enchantment {
   public EnchantmentArrowInfinite(int var1, int var2) {
      super(var1, var2, EnumEnchantmentType.bow);
      this.b("arrowInfinite");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 20;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return 50;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }
}
