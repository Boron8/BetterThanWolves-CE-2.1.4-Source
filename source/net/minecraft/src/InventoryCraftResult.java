package net.minecraft.src;

public class InventoryCraftResult implements IInventory {
   private ItemStack[] stackResult = new ItemStack[1];

   @Override
   public int getSizeInventory() {
      return 1;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.stackResult[0];
   }

   @Override
   public String getInvName() {
      return "Result";
   }

   @Override
   public boolean isInvNameLocalized() {
      return false;
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      if (this.stackResult[0] != null) {
         ItemStack var3 = this.stackResult[0];
         this.stackResult[0] = null;
         return var3;
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int var1) {
      if (this.stackResult[0] != null) {
         ItemStack var2 = this.stackResult[0];
         this.stackResult[0] = null;
         return var2;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.stackResult[0] = var2;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void onInventoryChanged() {
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer var1) {
      return true;
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int var1, ItemStack var2) {
      return true;
   }
}
