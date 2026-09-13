package net.minecraft.src;

public class EnchantmentLootBonus extends Enchantment {
   protected EnchantmentLootBonus(int var1, int var2, EnumEnchantmentType var3) {
      super(var1, var2, var3);
      this.b("lootBonus");
      if (var3 == EnumEnchantmentType.digger) {
         this.b("lootBonusDigger");
      }
   }

   @Override
   public int getMinEnchantability(int var1) {
      return 15 + (var1 - 1) * 9;
   }

   @Override
   public int getMaxEnchantability(int var1) {
      return super.getMinEnchantability(var1) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 3;
   }

   @Override
   public boolean canApplyTogether(Enchantment var1) {
      return super.canApplyTogether(var1) && var1.effectId != s.effectId;
   }
}
