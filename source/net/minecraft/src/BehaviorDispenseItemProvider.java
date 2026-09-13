package net.minecraft.src;

final class BehaviorDispenseItemProvider implements IBehaviorDispenseItem {
   @Override
   public ItemStack dispense(IBlockSource var1, ItemStack var2) {
      return var2;
   }
}
