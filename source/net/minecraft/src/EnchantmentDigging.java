package net.minecraft.src;

import btw.item.items.ShearsItem;

public class EnchantmentDigging extends Enchantment {
   protected EnchantmentDigging(int par1, int par2) {
      super(par1, par2, EnumEnchantmentType.digger);
      this.b("digging");
   }

   @Override
   public int getMinEnchantability(int par1) {
      return 1 + 10 * (par1 - 1);
   }

   @Override
   public int getMaxEnchantability(int par1) {
      return super.getMinEnchantability(par1) + 50;
   }

   @Override
   public int getMaxLevel() {
      return 5;
   }

   @Override
   public boolean canApply(ItemStack par1ItemStack) {
      return par1ItemStack.getItem() instanceof ShearsItem ? true : super.canApply(par1ItemStack);
   }
}
