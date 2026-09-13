package btw.inventory.container;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class InventoryContainer extends Container {
   public IInventory containerInventory;
   private int numSlotRows;
   private int numSlotColumns;
   private int numSlots;

   public InventoryContainer(
      IInventory playerInventory,
      IInventory containerInventory,
      int iNumSlotRows,
      int iNumSlotColumns,
      int iContainerInventoryX,
      int iContainerInventoryY,
      int iPlayerInventoryX,
      int iPlayerInventoryY
   ) {
      this.containerInventory = containerInventory;
      this.numSlotRows = iNumSlotRows;
      this.numSlotColumns = iNumSlotColumns;
      this.numSlots = this.numSlotRows * this.numSlotColumns;

      for (int iRow = 0; iRow < this.numSlotRows; iRow++) {
         for (int iColumn = 0; iColumn < this.numSlotColumns; iColumn++) {
            this.a(new Slot(containerInventory, iColumn + iRow * this.numSlotColumns, iContainerInventoryX + iColumn * 18, iContainerInventoryY + iRow * 18));
         }
      }

      for (int iRow = 0; iRow < 3; iRow++) {
         for (int iColumn = 0; iColumn < 9; iColumn++) {
            this.a(new Slot(playerInventory, iColumn + iRow * 9 + 9, iPlayerInventoryX + iColumn * 18, iPlayerInventoryY + iRow * 18));
         }
      }

      for (int iColumn = 0; iColumn < 9; iColumn++) {
         this.a(new Slot(playerInventory, iColumn, iPlayerInventoryX + iColumn * 18, iPlayerInventoryY + 58));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer entityplayer) {
      return this.containerInventory.isUseableByPlayer(entityplayer);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotIndex) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(iSlotIndex);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (iSlotIndex < this.numSlots) {
            if (!this.a(itemstack1, this.numSlots, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.a(itemstack1, 0, this.numSlots, false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemstack;
   }
}
