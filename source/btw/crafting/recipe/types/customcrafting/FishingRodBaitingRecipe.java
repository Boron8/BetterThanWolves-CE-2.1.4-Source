package btw.crafting.recipe.types.customcrafting;

import btw.item.BTWItems;
import net.minecraft.src.IInventory;
import net.minecraft.src.IRecipe;
import net.minecraft.src.InventoryCrafting;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class FishingRodBaitingRecipe implements IRecipe {
   @Override
   public boolean matches(InventoryCrafting craftingInventory, World world) {
      ItemStack rodStack = null;
      ItemStack baitStack = null;

      for (int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); iTempSlot++) {
         ItemStack tempStack = craftingInventory.getStackInSlot(iTempSlot);
         if (tempStack != null) {
            if (tempStack.itemID == Item.fishingRod.itemID) {
               if (rodStack != null) {
                  return false;
               }

               rodStack = tempStack;
            } else {
               if (!isFishingBait(tempStack)) {
                  return false;
               }

               if (baitStack != null) {
                  return false;
               }

               baitStack = tempStack;
            }
         }
      }

      return rodStack != null && baitStack != null;
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting craftingInventory) {
      ItemStack resultStack = null;
      ItemStack rodStack = null;
      ItemStack baitStack = null;

      for (int iTempSlot = 0; iTempSlot < craftingInventory.getSizeInventory(); iTempSlot++) {
         ItemStack tempStack = craftingInventory.getStackInSlot(iTempSlot);
         if (tempStack != null) {
            if (tempStack.itemID == Item.fishingRod.itemID) {
               if (rodStack != null) {
                  return null;
               }

               rodStack = tempStack;
               resultStack = tempStack.copy();
               resultStack.stackSize = 1;
               resultStack.itemID = BTWItems.baitedFishingRod.itemID;
            } else {
               if (!isFishingBait(tempStack)) {
                  return null;
               }

               if (baitStack != null) {
                  return null;
               }

               baitStack = tempStack;
            }
         }
      }

      return baitStack != null && rodStack != null ? resultStack : null;
   }

   @Override
   public int getRecipeSize() {
      return 2;
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

   public static boolean isFishingBait(ItemStack stack) {
      int iItemID = stack.itemID;
      return iItemID == BTWItems.creeperOysters.itemID
         || iItemID == BTWItems.batWing.itemID
         || iItemID == BTWItems.witchWart.itemID
         || iItemID == Item.spiderEye.itemID
         || iItemID == Item.rottenFlesh.itemID;
   }
}
