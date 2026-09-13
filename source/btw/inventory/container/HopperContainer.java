package btw.inventory.container;

import btw.block.tileentity.HopperTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class HopperContainer extends Container {
   private static final int NUM_HOPPER_SLOT_ROWS = 2;
   private static final int NUM_HOPPER_SLOT_COLUMNS = 9;
   private static final int NUM_HOPPER_SLOTS = 18;
   private HopperTileEntity localTileEntityHopper;
   private int lastMechanicalPowerIndicator;

   public HopperContainer(IInventory playerinventory, HopperTileEntity tileentityHopper) {
      this.localTileEntityHopper = tileentityHopper;
      this.lastMechanicalPowerIndicator = 0;

      for (int iRow = 0; iRow < 2; iRow++) {
         for (int iColumn = 0; iColumn < 9; iColumn++) {
            this.a(new Slot(tileentityHopper, iColumn + iRow * 9, 8 + iColumn * 18, 60 + iRow * 18));
         }
      }

      this.a(new Slot(tileentityHopper, 18, 80, 37));

      for (int iRow = 0; iRow < 3; iRow++) {
         for (int iColumn = 0; iColumn < 9; iColumn++) {
            this.a(new Slot(playerinventory, iColumn + iRow * 9 + 9, 8 + iColumn * 18, 111 + iRow * 18));
         }
      }

      for (int iColumn = 0; iColumn < 9; iColumn++) {
         this.a(new Slot(playerinventory, iColumn, 8 + iColumn * 18, 169));
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer entityplayer) {
      return this.localTileEntityHopper.isUseableByPlayer(entityplayer);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotIndex) {
      ItemStack clickedStack = null;
      Slot slot = (Slot)this.inventorySlots.get(iSlotIndex);
      if (slot != null && slot.getHasStack()) {
         ItemStack processedStack = slot.getStack();
         clickedStack = processedStack.copy();
         if (iSlotIndex < 19) {
            if (!this.a(processedStack, 19, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.a(processedStack, 0, 18, false)) {
            return null;
         }

         if (processedStack.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }
      }

      return clickedStack;
   }

   @Override
   public void addCraftingToCrafters(ICrafting craftingInterface) {
      super.addCraftingToCrafters(craftingInterface);
      craftingInterface.sendProgressBarUpdate(this, 0, this.localTileEntityHopper.mechanicalPowerIndicator);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (ICrafting icrafting : this.crafters) {
         if (this.lastMechanicalPowerIndicator != this.localTileEntityHopper.mechanicalPowerIndicator) {
            icrafting.sendProgressBarUpdate(this, 0, this.localTileEntityHopper.mechanicalPowerIndicator);
         }
      }

      this.lastMechanicalPowerIndicator = this.localTileEntityHopper.mechanicalPowerIndicator;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int iVariableIndex, int iValue) {
      if (iVariableIndex == 0) {
         this.localTileEntityHopper.mechanicalPowerIndicator = iValue;
      }
   }
}
