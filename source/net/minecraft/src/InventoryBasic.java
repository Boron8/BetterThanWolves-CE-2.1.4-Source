package net.minecraft.src;

import java.util.List;

public class InventoryBasic implements IInventory {
   private String inventoryTitle;
   private int slotsCount;
   private ItemStack[] inventoryContents;
   private List field_70480_d;
   private boolean field_94051_e;

   public InventoryBasic(String var1, boolean var2, int var3) {
      this.inventoryTitle = var1;
      this.field_94051_e = var2;
      this.slotsCount = var3;
      this.inventoryContents = new ItemStack[var3];
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.inventoryContents[var1];
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      if (this.inventoryContents[var1] != null) {
         if (this.inventoryContents[var1].stackSize <= var2) {
            ItemStack var4 = this.inventoryContents[var1];
            this.inventoryContents[var1] = null;
            this.onInventoryChanged();
            return var4;
         } else {
            ItemStack var3 = this.inventoryContents[var1].splitStack(var2);
            if (this.inventoryContents[var1].stackSize == 0) {
               this.inventoryContents[var1] = null;
            }

            this.onInventoryChanged();
            return var3;
         }
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int var1) {
      if (this.inventoryContents[var1] != null) {
         ItemStack var2 = this.inventoryContents[var1];
         this.inventoryContents[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.inventoryContents[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.onInventoryChanged();
   }

   @Override
   public int getSizeInventory() {
      return this.slotsCount;
   }

   @Override
   public String getInvName() {
      return this.inventoryTitle;
   }

   @Override
   public boolean isInvNameLocalized() {
      return this.field_94051_e;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void onInventoryChanged() {
      if (this.field_70480_d != null) {
         for (int var1 = 0; var1 < this.field_70480_d.size(); var1++) {
            ((IInvBasic)this.field_70480_d.get(var1)).onInventoryChanged(this);
         }
      }
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
