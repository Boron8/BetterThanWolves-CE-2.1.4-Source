package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

class ContainerCreative extends Container {
   public List itemList = new ArrayList();

   public ContainerCreative(EntityPlayer var1) {
      InventoryPlayer var2 = var1.inventory;

      for (int var3 = 0; var3 < 5; var3++) {
         for (int var4 = 0; var4 < 9; var4++) {
            this.a(new Slot(GuiContainerCreative.getInventory(), var3 * 9 + var4, 9 + var4 * 18, 18 + var3 * 18));
         }
      }

      for (int var5 = 0; var5 < 9; var5++) {
         this.a(new Slot(var2, var5, 9 + var5 * 18, 112));
      }

      this.scrollTo(0.0F);
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return true;
   }

   public void scrollTo(float var1) {
      int var2 = this.itemList.size() / 9 - 5 + 1;
      int var3 = (int)(var1 * var2 + 0.5);
      if (var3 < 0) {
         var3 = 0;
      }

      for (int var4 = 0; var4 < 5; var4++) {
         for (int var5 = 0; var5 < 9; var5++) {
            int var6 = var5 + (var4 + var3) * 9;
            if (var6 >= 0 && var6 < this.itemList.size()) {
               GuiContainerCreative.getInventory().setInventorySlotContents(var5 + var4 * 9, (ItemStack)this.itemList.get(var6));
            } else {
               GuiContainerCreative.getInventory().setInventorySlotContents(var5 + var4 * 9, null);
            }
         }
      }
   }

   public boolean hasMoreThan1PageOfItemsInList() {
      return this.itemList.size() > 45;
   }

   @Override
   protected void retrySlotClick(int var1, int var2, boolean var3, EntityPlayer var4) {
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      if (var2 >= this.inventorySlots.size() - 9 && var2 < this.inventorySlots.size()) {
         Slot var3 = (Slot)this.inventorySlots.get(var2);
         if (var3 != null && var3.getHasStack()) {
            var3.putStack(null);
         }
      }

      return null;
   }

   @Override
   public boolean func_94530_a(ItemStack var1, Slot var2) {
      return var2.yDisplayPosition > 90;
   }

   @Override
   public boolean func_94531_b(Slot var1) {
      return var1.inventory instanceof InventoryPlayer || var1.yDisplayPosition > 90 && var1.xDisplayPosition <= 162;
   }
}
