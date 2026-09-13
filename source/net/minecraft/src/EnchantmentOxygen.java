package net.minecraft.src;

public class EnchantmentOxygen extends Enchantment {
   public EnchantmentOxygen(int var1, int var2) {
      super(var1, var2, EnumEnchantmentType.armor_head);
      this.b("oxygen");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 10 * var1;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(var1) + 30;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }
}
