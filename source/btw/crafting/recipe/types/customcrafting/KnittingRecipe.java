package btw.crafting.recipe.types.customcrafting;

import btw.item.BTWItems;
import btw.item.items.KnittingItem;
import btw.item.items.WoolItem;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class KnittingRecipe implements IRecipe {
   private ItemStack stackNeedles;
   private ItemStack stackWool;
   private ItemStack stackWool2;

   @Override
   public boolean matches(InventoryCrafting inventory, World world) {
      return this.checkForIngredients(inventory);
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting inventory) {
      if (this.checkForIngredients(inventory)) {
         ItemStack resultStack = new ItemStack(BTWItems.knitting.itemID, 1, 599);
         int iWoolColor = WoolItem.woolColors[WoolItem.getClosestColorIndex(WoolItem.averageWoolColorsInGrid(inventory))];
         KnittingItem.setColor(resultStack, iWoolColor);
         return resultStack;
      } else {
         return null;
      }
   }

   @Override
   public int getRecipeSize() {
      return 3;
   }

   @Override
   public ItemStack getRecipeOutput() {
      return null;
   }

   @Override
   public boolean matches(IRecipe recipe) {
      return false;
   }

   @Override
   public boolean hasSecondaryOutput() {
      return false;
   }

   @Override
   public ItemStack[] getSecondaryOutput(IInventory inventory) {
      return null;
   }

   private boolean checkForIngredients(InventoryCrafting inventory) {
      this.stackNeedles = null;
      this.stackWool = null;
      this.stackWool2 = null;

      for (int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++) {
         ItemStack tempStack = inventory.getStackInSlot(iTempSlot);
         if (tempStack != null) {
            if (tempStack.itemID == BTWItems.knittingNeedles.itemID) {
               if (this.stackNeedles != null) {
                  return false;
               }

               this.stackNeedles = tempStack;
            } else {
               if (tempStack.itemID != BTWItems.wool.itemID) {
                  return false;
               }

               if (this.stackWool == null) {
                  this.stackWool = tempStack;
               } else {
                  if (this.stackWool2 != null) {
                     return false;
                  }

                  this.stackWool2 = tempStack;
               }
            }
         }
      }

      return this.stackNeedles != null && this.stackWool != null && this.stackWool2 != null;
   }
}
