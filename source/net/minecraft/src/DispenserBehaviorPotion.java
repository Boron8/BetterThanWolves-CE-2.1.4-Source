package net.minecraft.src;

public final class DispenserBehaviorPotion implements IBehaviorDispenseItem {
   private final BehaviorDefaultDispenseItem defaultDispenserItemBehavior = new BehaviorDefaultDispenseItem();

   @Override
   public ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack) {
      return ItemPotion.isSplash(par2ItemStack.getItemDamage())
         ? new DispenserBehaviorPotionProjectile(this, par2ItemStack).a(par1IBlockSource, par2ItemStack)
         : this.defaultDispenserItemBehavior.dispense(par1IBlockSource, par2ItemStack);
   }
}
