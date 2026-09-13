package net.minecraft.src;

public class EnchantmentFireAspect extends Enchantment {
   protected EnchantmentFireAspect(int var1, int var2) {
      super(var1, var2, EnumEnchantmentType.weapon);
      this.b("fire");
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 10 + 20 * (var1 - 1);
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(var1) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 2;
   }
}
