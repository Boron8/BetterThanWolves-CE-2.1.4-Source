package btw.inventory.container;

import net.minecraft.src.ContainerPlayer;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemArmor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Slot;

public class PlayerContainer extends ContainerPlayer {
   public PlayerContainer(InventoryPlayer inventory, boolean bNotRemote, EntityPlayer player) {
      super(inventory, bNotRemote, player);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotClicked) {
      ItemStack oldStackInSlotClicked = null;
      Slot slotClicked = (Slot)this.inventorySlots.get(iSlotClicked);
      if (slotClicked != null && slotClicked.getHasStack()) {
         ItemStack newStackInSlotClicked = slotClicked.getStack();
         oldStackInSlotClicked = newStackInSlotClicked.copy();
         if (iSlotClicked == 0) {
            if (!this.a(newStackInSlotClicked, 9, 45, true)) {
               return null;
            }

            slotClicked.onSlotChange(newStackInSlotClicked, oldStackInSlotClicked);
         } else if (iSlotClicked >= 1 && iSlotClicked < 5) {
            if (!this.a(newStackInSlotClicked, 9, 45, true)) {
               return null;
            }
         } else if (iSlotClicked >= 5 && iSlotClicked < 9) {
            if (!this.a(newStackInSlotClicked, 9, 45, true)) {
               return null;
            }
         } else if (oldStackInSlotClicked.getItem() instanceof ItemArmor
            && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)oldStackInSlotClicked.getItem()).armorType)).getHasStack()) {
            int iArmorSlot = 5 + ((ItemArmor)oldStackInSlotClicked.getItem()).armorType;
            if (!this.a(newStackInSlotClicked, iArmorSlot, iArmorSlot + 1, false)) {
               return null;
            }
         } else if (iSlotClicked >= 9 && iSlotClicked < 36) {
            if (!this.a(newStackInSlotClicked, 36, 45, false)) {
               return null;
            }
         } else if (iSlotClicked >= 36 && iSlotClicked < 45) {
            if (!this.a(newStackInSlotClicked, 9, 36, false)) {
               return null;
            }
         } else if (!this.a(newStackInSlotClicked, 9, 45, true)) {
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
