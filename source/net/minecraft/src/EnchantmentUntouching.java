package net.minecraft.src;

import btw.item.items.ShearsItem;

public class EnchantmentUntouching extends Enchantment {
   protected EnchantmentUntouching(int par1, int par2) {
      super(par1, par2, EnumEnchantmentType.digger);
      this.b("untouching");
   }

   @Override
   public int getMinEnchantability(int par1) {
      return 15;
   }

   @Override
   public int getMaxEnchantability(int par1) {
      return super.getMinEnchantability(par1) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 1;
   }

   @Override
   public boolean canApplyTogether(Enchantment par1Enchantment) {
      return super.canApplyTogether(par1Enchantment) && par1Enchantment.effectId != u.effectId;
   }

   @Override
   public boolean canApply(ItemStack par1ItemStack) {
      return par1ItemStack.getItem() instanceof ShearsItem ? true : super.canApply(par1ItemStack);
   }
}
