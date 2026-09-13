package net.minecraft.src;

public class InventoryLargeChest implements IInventory {
   private String name;
   private IInventory upperChest;
   private IInventory lowerChest;

   public InventoryLargeChest(String var1, IInventory var2, IInventory var3) {
      this.name = var1;
      if (var2 == null) {
         var2 = var3;
      }

      if (var3 == null) {
         var3 = var2;
      }

      this.upperChest = var2;
      this.lowerChest = var3;
   }

   @Override
   public int getSizeInventory() {
      return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
   }

   public boolean isPartOfLargeChest(IInventory var1) {
      return this.upperChest == var1 || this.lowerChest == var1;
   }

   @Override
   public String getInvName() {
      if (this.upperChest.isInvNameLocalized()) {
         return this.upperChest.getInvName();
      } else {
         return this.lowerChest.isInvNameLocalized() ? this.lowerChest.getInvName() : this.name;
      }
   }

   @Override
   public boolean isInvNameLocalized() {
      return this.upperChest.isInvNameLocalized() || this.lowerChest.isInvNameLocalized();
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return var1 >= this.upperChest.getSizeInventory()
         ? this.lowerChest.getStackInSlot(var1 - this.upperChest.getSizeInventory())
         : this.upperChest.getStackInSlot(var1);
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      return var1 >= this.upperChest.getSizeInventory()
         ? this.lowerChest.decrStackSize(var1 - this.upperChest.getSizeInventory(), var2)
         : this.upperChest.decrStackSize(var1, var2);
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int var1) {
      return var1 >= this.upperChest.getSizeInventory()
         ? this.lowerChest.getStackInSlotOnClosing(var1 - this.upperChest.getSizeInventory())
         : this.upperChest.getStackInSlotOnClosing(var1);
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      if (var1 >= this.upperChest.getSizeInventory()) {
         this.lowerChest.setInventorySlotContents(var1 - this.upperChest.getSizeInventory(), var2);
      } else {
         this.upperChest.setInventorySlotContents(var1, var2);
      }
   }

   @Override
   public int getInventoryStackLimit() {
      return this.upperChest.getInventoryStackLimit();
   }

   @Override
   public void onInventoryChanged() {
      this.upperChest.onInventoryChanged();
      this.lowerChest.onInventoryChanged();
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.upperChest.isUseableByPlayer(var1) && this.lowerChest.isUseableByPlayer(var1);
   }

   @Override
   public void openChest() {
      this.upperChest.openChest();
      this.lowerChest.openChest();
   }

   @Override
   public void closeChest() {
      this.upperChest.closeChest();
      this.lowerChest.closeChest();
   }

   @Override
   public boolean isStackValidForSlot(int var1, ItemStack var2) {
      return true;
   }
}
