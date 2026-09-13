package net.minecraft.src;

public class EnchantmentWaterWorker extends Enchantment {
   public EnchantmentWaterWorker(int var1, int var2) {
      super(var1, var2, EnumEnchantmentType.armor_head);
      this.b("waterWorker");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 1;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return this.getMinEnchantability(var1) + 40;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }
}
