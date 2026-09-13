package btw.inventory.container;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.ContainerWorkbench;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;
import net.minecraft.src.World;

public class WorkbenchContainer extends ContainerWorkbench {
   public World world;
   public int blockX;
   public int blockY;
   public int blockZ;

   public WorkbenchContainer(InventoryPlayer inventory, World world, int i, int j, int k) {
      super(inventory, world, i, j, k);
      this.world = world;
      this.blockX = i;
      this.blockY = j;
      this.blockZ = k;
   }

   @Override
   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      int iBlockID = this.world.getBlockId(this.blockX, this.blockY, this.blockZ);
      return (
            iBlockID == BTWBlocks.workStump.blockID
               || iBlockID == BTWBlocks.workbench.blockID
               || iBlockID == Block.anvil.blockID
               || iBlockID == Block.workbench.blockID
         )
         && par1EntityPlayer.e(this.blockX + 0.5, this.blockY + 0.5, this.blockZ + 0.5) <= 64.0;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotClicked) {
      ItemStack oldStackInSlotClicked = null;
      Slot slotClicked = (Slot)this.inventorySlots.get(iSlotClicked);
      if (slotClicked != null && slotClicked.getHasStack()) {
         ItemStack newStackInSlotClicked = slotClicked.getStack();
         oldStackInSlotClicked = newStackInSlotClicked.copy();
         if (iSlotClicked == 0) {
            if (!this.a(newStackInSlotClicked, 10, 46, true)) {
               return null;
            }

            slotClicked.onSlotChange(newStackInSlotClicked, oldStackInSlotClicked);
         } else if (iSlotClicked >= 10 && iSlotClicked < 37) {
            if (!this.a(newStackInSlotClicked, 1, 10, false)) {
               return null;
            }
         } else if (iSlotClicked >= 37 && iSlotClicked < 46) {
            if (!this.a(newStackInSlotClicked, 1, 10, false)) {
               return null;
            }
         } else if (!this.a(newStackInSlotClicked, 10, 46, true)) {
            return null;
         }

         if (newStackInSlotClicked.stackSize == 0) {
            slotClicked.putStack((ItemStack)null);
         } else {
            slotClicked.onSlotChanged();
         }

         if (newStackInSlotClicked.stackSize == oldStackInSlotClicked.stackSize) {
            return null;
         }

         slotClicked.onPickupFromSlot(player, newStackInSlotClicked);
      }

      return oldStackInSlotClicked;
   }
}
