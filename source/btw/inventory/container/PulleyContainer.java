package btw.inventory.container;

import btw.block.tileentity.PulleyTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class PulleyContainer extends Container {
   private static final int NUM_SLOT_ROWS = 2;
   private static final int NUM_SLOT_COLUMNS = 2;
   private static final int NUM_SLOTS = 4;
   private PulleyTileEntity localTileEntity;
   private int lastMechanicalPowerIndicator;

   public PulleyContainer(IInventory playerinventory, PulleyTileEntity tileEntityPulley) {
      this.localTileEntity = tileEntityPulley;
      this.lastMechanicalPowerIndicator = 0;

      for (int iRow = 0; iRow < 2; iRow++) {
         for (int iColumn = 0; iColumn < 2; iColumn++) {
            this.a(new Slot(tileEntityPulley, iColumn + iRow * 2, 71 + iColumn * 18, 43 + iRow * 18));
         }
      }

      for (int iRow = 0; iRow < 3; iRow++) {
         for (int iColumn = 0; iColumn < 9; iColumn++) {
            this.a(new Slot(playerinventory, iColumn + iRow * 9 + 9, 8 + iColumn * 18, 93 + iRow * 18));
         }
      }

      for (int iColumn = 0; iColumn < 9; iColumn++) {
         this.a(new Slot(playerinventory, iColumn, 8 + iColumn * 18, 151));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer entityplayer) {
      return this.localTileEntity.isUseableByPlayer(entityplayer);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotIndex) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(iSlotIndex);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (iSlotIndex < 4) {
            if (!this.a(itemstack1, 4, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.a(itemstack1, 0, 4, false)) {
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

   @Override
   public void addCraftingToCrafters(ICrafting craftingInterface) {
      super.addCraftingToCrafters(craftingInterface);
      craftingInterface.sendProgressBarUpdate(this, 0, this.localTileEntity.mechanicalPowerIndicator);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (ICrafting icrafting : this.crafters) {
         if (this.lastMechanicalPowerIndicator != this.localTileEntity.mechanicalPowerIndicator) {
            icrafting.sendProgressBarUpdate(this, 0, this.localTileEntity.mechanicalPowerIndicator);
         }
      }

      this.lastMechanicalPowerIndicator = this.localTileEntity.mechanicalPowerIndicator;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int iVariableIndex, int iValue) {
      if (iVariableIndex == 0) {
         this.localTileEntity.mechanicalPowerIndicator = iValue;
      }
   }
}
