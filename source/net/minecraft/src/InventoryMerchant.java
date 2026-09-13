package net.minecraft.src;

import btw.item.BTWItems;
import btw.item.items.AncientProphecyItem;

public class InventoryMerchant implements IInventory {
   private final IMerchant theMerchant;
   private ItemStack[] theInventory = new ItemStack[3];
   private final EntityPlayer thePlayer;
   private MerchantRecipe currentRecipe;
   private int currentRecipeIndex;

   public InventoryMerchant(EntityPlayer par1EntityPlayer, IMerchant par2IMerchant) {
      this.thePlayer = par1EntityPlayer;
      this.theMerchant = par2IMerchant;
   }

   @Override
   public int getSizeInventory() {
      return this.theInventory.length;
   }

   @Override
   public ItemStack getStackInSlot(int par1) {
      return this.theInventory[par1];
   }

   @Override
   public ItemStack decrStackSize(int par1, int par2) {
      if (this.theInventory[par1] != null) {
         if (par1 == 2) {
            ItemStack var3 = this.theInventory[par1];
            this.theInventory[par1] = null;
            return var3;
         } else if (this.theInventory[par1].stackSize <= par2) {
            ItemStack var3 = this.theInventory[par1];
            this.theInventory[par1] = null;
            if (this.inventoryResetNeededOnSlotChange(par1)) {
               this.resetRecipeAndSlots();
            }

            return var3;
         } else {
            ItemStack var3 = this.theInventory[par1].splitStack(par2);
            if (this.theInventory[par1].stackSize == 0) {
               this.theInventory[par1] = null;
            }

            if (this.inventoryResetNeededOnSlotChange(par1)) {
               this.resetRecipeAndSlots();
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   private boolean inventoryResetNeededOnSlotChange(int par1) {
      return par1 == 0 || par1 == 1;
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.theInventory[par1] != null) {
         ItemStack var2 = this.theInventory[par1];
         this.theInventory[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.theInventory[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }

      if (this.inventoryResetNeededOnSlotChange(par1)) {
         this.resetRecipeAndSlots();
      }
   }

   @Override
   public String getInvName() {
      return "mob.villager";
   }

   @Override
   public boolean isInvNameLocalized() {
      return false;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.theMerchant.getCustomer() == par1EntityPlayer;
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
      return true;
   }

   @Override
   public void onInventoryChanged() {
      this.resetRecipeAndSlots();
   }

   public void resetRecipeAndSlots() {
      this.currentRecipe = null;
      ItemStack var1 = this.theInventory[0];
      ItemStack var2 = this.theInventory[1];
      if (var1 == null) {
         var1 = var2;
         var2 = null;
      }

      if (var1 == null) {
         this.setInventorySlotContents(2, (ItemStack)null);
      } else {
         MerchantRecipeList var3 = this.theMerchant.getRecipes(this.thePlayer);
         if (var3 != null) {
            MerchantRecipe var4 = var3.canRecipeBeUsed(var1, var2, this.currentRecipeIndex);
            if (var4 != null && !var4.func_82784_g()) {
               this.currentRecipe = var4;
               this.setInventorySlotContents(2, var4.getItemToSell().copy());
            } else if (var2 != null) {
               var4 = var3.canRecipeBeUsed(var2, var1, this.currentRecipeIndex);
               if (var4 != null && !var4.func_82784_g()) {
                  this.currentRecipe = var4;
                  this.setInventorySlotContents(2, var4.getItemToSell().copy());
               } else {
                  this.setInventorySlotContents(2, (ItemStack)null);
               }
            } else {
               this.setInventorySlotContents(2, (ItemStack)null);
            }
         }
      }

      this.resetRecipeAndSlotsModProcessing();
   }

   public MerchantRecipe getCurrentRecipe() {
      return this.currentRecipe;
   }

   public void setCurrentRecipeIndex(int par1) {
      this.currentRecipeIndex = par1;
      this.resetRecipeAndSlots();
   }

   private void resetRecipeAndSlotsModProcessing() {
      ItemStack outputStack = this.theInventory[2];
      if (outputStack != null && outputStack.itemID == BTWItems.ancientProphecy.itemID) {
         ItemStack manuscriptStack = this.theInventory[0];
         if (manuscriptStack.itemID != Item.enchantedBook.itemID) {
            manuscriptStack = this.theInventory[1];
         }

         int iEnchantmentID = -1;
         NBTTagList enchantmentTagList = Item.enchantedBook.func_92110_g(manuscriptStack);
         if (enchantmentTagList != null && enchantmentTagList.tagCount() > 0) {
            short iTempID = ((NBTTagCompound)enchantmentTagList.tagAt(0)).getShort("id");
            if (Enchantment.enchantmentsList[iTempID] != null) {
               iEnchantmentID = iTempID;
            }
         }

         ((AncientProphecyItem)BTWItems.ancientProphecy).initializeProphecyDataFromEnchantmentID(outputStack, iEnchantmentID);
      }
   }
}
