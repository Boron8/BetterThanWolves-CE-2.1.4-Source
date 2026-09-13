package btw.inventory.container;

import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class BlockDispenserContainer extends Container {
   private BlockDispenserTileEntity localTileEntity;
   private static final int NUM_SLOTS = 16;
   private int lastNextSlotIndexToDispense;

   public BlockDispenserContainer(IInventory iinventory, BlockDispenserTileEntity tileEntityBlockDispenser) {
      this.localTileEntity = tileEntityBlockDispenser;
      this.localTileEntity.openChest();

      for (int i = 0; i < 4; i++) {
         for (int l = 0; l < 4; l++) {
            this.a(new Slot(tileEntityBlockDispenser, l + i * 4, 53 + l * 18, 17 + i * 18));
         }
      }

      for (int j = 0; j < 3; j++) {
         for (int i1 = 0; i1 < 9; i1++) {
            this.a(new Slot(iinventory, i1 + j * 9 + 9, 8 + i1 * 18, 102 + j * 18));
         }
      }

      for (int k = 0; k < 9; k++) {
         this.a(new Slot(iinventory, k, 8 + k * 18, 160));
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
         if (iSlotIndex < 16) {
            if (!this.a(itemstack1, 16, this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (!this.a(itemstack1, 0, 16, false)) {
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
   public ItemStack slotClick(int i, int j, int k, EntityPlayer entityplayer) {
      if (i < 16) {
         this.localTileEntity.nextSlotIndexToDispense = 0;
      }

      return super.slotClick(i, j, k, entityplayer);
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer entityplayer) {
      super.onCraftGuiClosed(entityplayer);
      this.localTileEntity.closeChest();
   }

   @Override
   public void addCraftingToCrafters(ICrafting craftingInterface) {
      super.addCraftingToCrafters(craftingInterface);
      craftingInterface.sendProgressBarUpdate(this, 0, this.localTileEntity.nextSlotIndexToDispense);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (ICrafting icrafting : this.crafters) {
         if (this.lastNextSlotIndexToDispense != this.localTileEntity.nextSlotIndexToDispense) {
            icrafting.sendProgressBarUpdate(this, 0, this.localTileEntity.nextSlotIndexToDispense);
         }
      }

      this.lastNextSlotIndexToDispense = this.localTileEntity.nextSlotIndexToDispense;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int iVariableIndex, int iValue) {
      if (iVariableIndex == 0) {
         this.localTileEntity.nextSlotIndexToDispense = iValue;
      }
   }
}
