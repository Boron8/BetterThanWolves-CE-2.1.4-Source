package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ContainerMerchant extends Container {
   private IMerchant theMerchant;
   private InventoryMerchant merchantInventory;
   private final World theWorld;
   public int associatedVillagerTradeLevel = 0;
   public int associatedVillagerTradeXP = 0;
   public int associatedVillagerTradeMaxXP = 0;

   public ContainerMerchant(InventoryPlayer par1InventoryPlayer, IMerchant par2IMerchant, World par3World) {
      this.theMerchant = par2IMerchant;
      this.theWorld = par3World;
      this.merchantInventory = new InventoryMerchant(par1InventoryPlayer.player, par2IMerchant);
      this.a(new Slot(this.merchantInventory, 0, 36, 119));
      this.a(new Slot(this.merchantInventory, 1, 62, 119));
      this.a(new SlotMerchantResult(par1InventoryPlayer.player, par2IMerchant, this.merchantInventory, 2, 120, 119));

      for (int var4 = 0; var4 < 3; var4++) {
         for (int var5 = 0; var5 < 9; var5++) {
            this.a(new Slot(par1InventoryPlayer, var5 + var4 * 9 + 9, 8 + var5 * 18, 157 + var4 * 18));
         }
      }

      for (int var6 = 0; var6 < 9; var6++) {
         this.a(new Slot(par1InventoryPlayer, var6, 8 + var6 * 18, 215));
      }
   }

   public InventoryMerchant getMerchantInventory() {
      return this.merchantInventory;
   }

   @Override
   public void addCraftingToCrafters(ICrafting par1ICrafting) {
      super.addCraftingToCrafters(par1ICrafting);
      this.onCrafterAdded(par1ICrafting);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();
      this.detectAndSendChangesToBTSMTradeVariables();
   }

   @Override
   public void onCraftMatrixChanged(IInventory par1IInventory) {
      this.merchantInventory.resetRecipeAndSlots();
      super.onCraftMatrixChanged(par1IInventory);
   }

   public void setCurrentRecipeIndex(int par1) {
      this.merchantInventory.setCurrentRecipeIndex(par1);
   }

   @Override
   public boolean canInteractWith(EntityPlayer par1EntityPlayer) {
      return this.theMerchant.getCustomer() == par1EntityPlayer;
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int slotNum) {
      ItemStack stackCopy = null;
      Slot slot = (Slot)this.inventorySlots.get(slotNum);
      if (slot != null && slot.getHasStack()) {
         ItemStack stack = slot.getStack();
         stackCopy = stack.copy();
         if (slotNum == 2) {
            if (this.merchantInventory.getCurrentRecipe().func_82784_g()) {
               slot.putStack((ItemStack)null);
               return null;
            }

            if (!this.a(stack, 3, 39, true)) {
               return null;
            }

            slot.onSlotChange(stack, stackCopy);
         } else if (slotNum != 0 && slotNum != 1) {
            if (slotNum >= 3 && slotNum < 30) {
               if (!this.a(stack, 0, 2, false)) {
                  return null;
               }
            } else if (slotNum >= 30 && slotNum < 39 && !this.a(stack, 0, 2, false)) {
               return null;
            }
         } else if (!this.a(stack, 3, 39, true)) {
            return null;
         }

         if (stack.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }

         if (stack.stackSize == stackCopy.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(player, stack);
      }

      return stackCopy;
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer par1EntityPlayer) {
      super.onCraftGuiClosed(par1EntityPlayer);
      this.theMerchant.setCustomer((EntityPlayer)null);
      super.onCraftGuiClosed(par1EntityPlayer);
      if (!this.theWorld.isRemote) {
         ItemStack var2 = this.merchantInventory.getStackInSlotOnClosing(0);
         if (var2 != null) {
            par1EntityPlayer.dropPlayerItem(var2);
         }

         var2 = this.merchantInventory.getStackInSlotOnClosing(1);
         if (var2 != null) {
            par1EntityPlayer.dropPlayerItem(var2);
         }
      }
   }

   private void detectAndSendChangesToBTSMTradeVariables() {
      int iCurrentTradeLevel = this.theMerchant.getCurrentTradeLevel();
      if (iCurrentTradeLevel != this.associatedVillagerTradeLevel) {
         this.sendProgressBarUpdateToAllCrafters(0, iCurrentTradeLevel);
         this.associatedVillagerTradeLevel = iCurrentTradeLevel;
      }

      int iCurrentTradeXP = this.theMerchant.getCurrentTradeXP();
      if (iCurrentTradeXP != this.associatedVillagerTradeXP) {
         this.sendProgressBarUpdateToAllCrafters(1, iCurrentTradeXP);
         this.associatedVillagerTradeXP = iCurrentTradeXP;
      }

      int iCurrentTradeMaxXP = this.theMerchant.getCurrentTradeMaxXP();
      if (iCurrentTradeMaxXP != this.associatedVillagerTradeMaxXP) {
         this.sendProgressBarUpdateToAllCrafters(2, iCurrentTradeMaxXP);
         this.associatedVillagerTradeMaxXP = iCurrentTradeMaxXP;
      }
   }

   public void sendProgressBarUpdateToAllCrafters(int iVariableIndex, int iValue) {
      for (ICrafting icrafting : this.crafters) {
         icrafting.sendProgressBarUpdate(this, iVariableIndex, iValue);
      }
   }

   public void onCrafterAdded(ICrafting crafter) {
      crafter.sendProgressBarUpdate(this, 0, this.associatedVillagerTradeLevel);
      crafter.sendProgressBarUpdate(this, 1, this.associatedVillagerTradeXP);
      crafter.sendProgressBarUpdate(this, 2, this.associatedVillagerTradeMaxXP);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int iVariableIndex, int iValue) {
      if (iVariableIndex == 0) {
         this.associatedVillagerTradeLevel = iValue;
      } else if (iVariableIndex == 1) {
         this.associatedVillagerTradeXP = iValue;
      } else if (iVariableIndex == 2) {
         this.associatedVillagerTradeMaxXP = iValue;
      }
   }
}
