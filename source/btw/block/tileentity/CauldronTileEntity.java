package btw.block.tileentity;

import btw.crafting.manager.BulkCraftingManager;
import btw.crafting.manager.CauldronCraftingManager;
import btw.crafting.manager.CauldronStokedCraftingManager;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import net.minecraft.src.FurnaceRecipes;
import net.minecraft.src.Item;
import net.minecraft.src.ItemFood;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;

public class CauldronTileEntity extends CookingVesselTileEntity {
   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      if (nbttagcompound.hasKey("m_iCauldronCookCounter")) {
         this.cookCounter = nbttagcompound.getInteger("m_iCauldronCookCounter");
      }

      if (nbttagcompound.hasKey("m_iRenderCooldownCounter")) {
         this.stokedCooldownCounter = nbttagcompound.getInteger("m_iRenderCooldownCounter");
      }

      if (nbttagcompound.hasKey("m_bContainsValidIngrediantsForState")) {
         this.containsValidIngredientsForState = nbttagcompound.getBoolean("m_bContainsValidIngrediantsForState");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("m_iCauldronCookCounter", this.cookCounter);
      nbttagcompound.setInteger("m_iRenderCooldownCounter", this.stokedCooldownCounter);
   }

   @Override
   public String getInvName() {
      return "Cauldron";
   }

   @Override
   public boolean isStackValidForSlot(int iSlot, ItemStack stack) {
      return true;
   }

   @Override
   public boolean isInvNameLocalized() {
      return true;
   }

   @Override
   public void validateContentsForState() {
      this.containsValidIngredientsForState = false;
      if (this.fireUnderType == 1) {
         if (CauldronCraftingManager.getInstance().getCraftingResult(this) != null) {
            this.containsValidIngredientsForState = true;
         } else if (this.getUncookedItemInventoryIndex() >= 0) {
            this.containsValidIngredientsForState = true;
         } else if (InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.dung.itemID) >= 0 && this.containsNonFoulFood()) {
            this.containsValidIngredientsForState = true;
         }
      } else if (this.fireUnderType == 2) {
         if (this.doesContainExplosives()) {
            this.containsValidIngredientsForState = true;
         } else if (CauldronStokedCraftingManager.getInstance().getCraftingResult(this) != null) {
            this.containsValidIngredientsForState = true;
         }
      }
   }

   @Override
   protected BulkCraftingManager getCraftingManager(int iFireType) {
      if (iFireType == 1) {
         return CauldronCraftingManager.getInstance();
      } else {
         return iFireType == 2 ? CauldronStokedCraftingManager.getInstance() : null;
      }
   }

   @Override
   protected boolean attemptToCookNormal() {
      int iDungIndex = InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.dung.itemID);
      if (iDungIndex >= 0 && this.taintAllNonFoulFoodInInventory()) {
         return true;
      } else {
         return super.attemptToCookNormal() ? true : this.attemptToCookFood();
      }
   }

   private boolean attemptToCookFood() {
      int iUncookedFoodIndex = this.getUncookedItemInventoryIndex();
      if (iUncookedFoodIndex >= 0) {
         ItemStack tempStack = FurnaceRecipes.smelting().getSmeltingResult(this.contents[iUncookedFoodIndex].getItem().itemID);
         ItemStack cookedStack = tempStack.copy();
         this.a(iUncookedFoodIndex, 1);
         if (!InventoryUtils.addItemStackToInventory(this, cookedStack)) {
            ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord, cookedStack);
         }

         return true;
      } else {
         return false;
      }
   }

   public int getUncookedItemInventoryIndex() {
      for (int tempIndex = 0; tempIndex < 27; tempIndex++) {
         if (this.contents[tempIndex] != null) {
            Item tempItem = this.contents[tempIndex].getItem();
            if (tempItem != null && tempItem instanceof ItemFood && FurnaceRecipes.smelting().getSmeltingResult(tempItem.itemID) != null) {
               return tempIndex;
            }
         }
      }

      return -1;
   }

   private boolean containsNonFoulFood() {
      for (int tempIndex = 0; tempIndex < 27; tempIndex++) {
         if (this.contents[tempIndex] != null) {
            Item tempItem = this.contents[tempIndex].getItem();
            if (tempItem != null) {
               int iTempItemID = tempItem.itemID;
               if (tempItem.itemID != BTWItems.foulFood.itemID
                  && iTempItemID != BTWItems.brownMushroom.itemID
                  && iTempItemID != BTWItems.redMushroom.itemID
                  && tempItem instanceof ItemFood) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean taintAllNonFoulFoodInInventory() {
      boolean bFoodDestroyed = false;

      for (int tempIndex = 0; tempIndex < 27; tempIndex++) {
         if (this.contents[tempIndex] != null) {
            Item tempItem = this.contents[tempIndex].getItem();
            if (tempItem != null) {
               int iTempItemID = tempItem.itemID;
               if (tempItem.itemID != BTWItems.foulFood.itemID
                  && iTempItemID != BTWItems.brownMushroom.itemID
                  && iTempItemID != BTWItems.redMushroom.itemID
                  && tempItem instanceof ItemFood) {
                  int stackSize = this.contents[tempIndex].stackSize;
                  this.contents[tempIndex] = null;
                  ItemStack spoiledStack = new ItemStack(BTWItems.foulFood, stackSize);
                  this.a(tempIndex, spoiledStack);
                  bFoodDestroyed = true;
               }
            }
         }
      }

      return bFoodDestroyed;
   }
}
