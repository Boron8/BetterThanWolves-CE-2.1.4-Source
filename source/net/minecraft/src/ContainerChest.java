package net.minecraft.src;

public class ContainerChest extends Container {
   private IInventory lowerChestInventory;
   private int numRows;

   public ContainerChest(IInventory var1, IInventory var2) {
      this.lowerChestInventory = var2;
      this.numRows = var2.getSizeInventory() / 9;
      var2.openChest();
      int var3 = (this.numRows - 4) * 18;

      for (int var4 = 0; var4 < this.numRows; var4++) {
         for (int var5 = 0; var5 < 9; var5++) {
            this.a(new Slot(var2, var5 + var4 * 9, 8 + var5 * 18, 18 + var4 * 18));
         }
      }

      for (int var6 = 0; var6 < 3; var6++) {
         for (int var8 = 0; var8 < 9; var8++) {
            this.a(new Slot(var1, var8 + var6 * 9 + 9, 8 + var8 * 18, 103 + var6 * 18 + var3));
         }
      }

      for (int var7 = 0; var7 < 9; var7++) {
         this.a(new Slot(var1, var7, 8 + var7 * 18, 161 + var3));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.lowerChestInventory.isUseableByPlayer(var1);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 < this.numRows * 9) {
            if (!this.a(var5, this.numRows * 9, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.a(var5, 0, this.numRows * 9, false)) {
            return null;
         }

         if (var5.stackSize == 0) {
            var4.putStack(null);
         } else {
            var4.onSlotChanged();
         }
      }

      return var3;
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer var1) {
      super.onCraftGuiClosed(var1);
      this.lowerChestInventory.closeChest();
   }

   public IInventory getLowerChestInventory() {
      return this.lowerChestInventory;
   }
}
