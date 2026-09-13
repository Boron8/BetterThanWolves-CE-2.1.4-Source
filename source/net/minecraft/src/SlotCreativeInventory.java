package net.minecraft.src;

class SlotCreativeInventory extends Slot {
   private final Slot theSlot;

   public SlotCreativeInventory(GuiContainerCreative var1, Slot var2, int var3) {
      super(var2.inventory, var3, 0, 0);
      this.theCreativeInventory = var1;
      this.theSlot = var2;
   }

   @Override
   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      this.theSlot.onPickupFromSlot(var1, var2);
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return this.theSlot.isItemValid(var1);
   }

   @Override
   public ItemStack getStack() {
      return this.theSlot.getStack();
   }

   @Override
   public boolean getHasStack() {
      return this.theSlot.getHasStack();
   }

   @Override
   public void putStack(ItemStack var1) {
      this.theSlot.putStack(var1);
   }

   @Override
   public void onSlotChanged() {
      this.theSlot.onSlotChanged();
   }

   @Override
   public int getSlotStackLimit() {
      return this.theSlot.getSlotStackLimit();
   }

   @Override
   public Icon getBackgroundIconIndex() {
      return this.theSlot.getBackgroundIconIndex();
   }

   @Override
   public ItemStack decrStackSize(int var1) {
      return this.theSlot.decrStackSize(var1);
   }

   @Override
   public boolean isSlotInInventory(IInventory var1, int var2) {
      return this.theSlot.isSlotInInventory(var1, var2);
   }
}
