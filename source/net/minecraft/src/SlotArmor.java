package net.minecraft.src;

class SlotArmor extends Slot {
   SlotArmor(ContainerPlayer var1, IInventory var2, int var3, int var4, int var5, int var6) {
      super(var2, var3, var4, var5);
      this.parent = var1;
      this.armorType = var6;
   }

   @Override
   public int getSlotStackLimit() {
      return 1;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      if (var1 == null) {
         return false;
      } else if (var1.getItem() instanceof ItemArmor) {
         return ((ItemArmor)var1.getItem()).armorType == this.armorType;
      } else {
         return var1.getItem().itemID != Block.pumpkin.blockID && var1.getItem().itemID != Item.skull.itemID ? false : this.armorType == 0;
      }
   }

   @Override
   public Icon getBackgroundIconIndex() {
      return ItemArmor.func_94602_b(this.armorType);
   }
}
