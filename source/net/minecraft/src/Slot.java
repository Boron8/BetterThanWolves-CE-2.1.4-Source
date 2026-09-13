package net.minecraft.src;

public class Slot {
   private final int slotIndex;
   public final IInventory inventory;
   public int slotNumber;
   public int xDisplayPosition;
   public int yDisplayPosition;

   public Slot(IInventory var1, int var2, int var3, int var4) {
      this.inventory = var1;
      this.slotIndex = var2;
      this.xDisplayPosition = var3;
      this.yDisplayPosition = var4;
   }

   public void onSlotChange(ItemStack var1, ItemStack var2) {
      if (var1 != null && var2 != null) {
         if (var1.itemID == var2.itemID) {
            int var3 = var2.stackSize - var1.stackSize;
            if (var3 > 0) {
               this.onCrafting(var1, var3);
            }
         }
      }
   }

   protected void onCrafting(ItemStack var1, int var2) {
   }

   protected void onCrafting(ItemStack var1) {
   }

   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      this.onSlotChanged();
   }

   public boolean isItemValid(ItemStack var1) {
      return true;
   }

   public ItemStack getStack() {
      return this.inventory.getStackInSlot(this.slotIndex);
   }

   public boolean getHasStack() {
      return this.getStack() != null;
   }

   public void putStack(ItemStack var1) {
      this.inventory.setInventorySlotContents(this.slotIndex, var1);
      this.onSlotChanged();
   }

   public void onSlotChanged() {
      this.inventory.onInventoryChanged();
   }

   public int getSlotStackLimit() {
      return this.inventory.getInventoryStackLimit();
   }

   public Icon getBackgroundIconIndex() {
      return null;
   }

   public ItemStack decrStackSize(int var1) {
      return this.inventory.decrStackSize(this.slotIndex, var1);
   }

   public boolean isSlotInInventory(IInventory var1, int var2) {
      return var1 == this.inventory && var2 == this.slotIndex;
   }

   public boolean canTakeStack(EntityPlayer var1) {
      return true;
   }
}
