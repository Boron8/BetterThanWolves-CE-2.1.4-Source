package net.minecraft.src;

import btw.item.BTWItems;
import java.util.ArrayList;
import java.util.List;

public class ShapelessRecipes implements IRecipe {
   private final ItemStack recipeOutput;
   private ItemStack[] recipeSecondaryOutputs;
   private final List recipeItems;

   public ShapelessRecipes(ItemStack recipeOutput, List recipeItems) {
      this(recipeOutput, null, recipeItems);
   }

   public ShapelessRecipes(ItemStack recipeOutput, ItemStack[] recipeSecondaryOutputs, List recipeItems) {
      this.recipeOutput = recipeOutput;
      this.recipeSecondaryOutputs = recipeSecondaryOutputs;
      this.recipeItems = recipeItems;
   }

   @Override
   public ItemStack getRecipeOutput() {
      return this.recipeOutput;
   }

   @Override
   public boolean matches(InventoryCrafting par1InventoryCrafting, World par2World) {
      ArrayList var3 = new ArrayList(this.recipeItems);

      for (int var4 = 0; var4 < 4; var4++) {
         for (int var5 = 0; var5 < 4; var5++) {
            ItemStack var6 = par1InventoryCrafting.getStackInRowAndColumn(var5, var4);
            if (var6 != null && var6.itemID != BTWItems.mould.itemID) {
               boolean var7 = false;

               for (ItemStack var9 : var3) {
                  if (var6.itemID == var9.itemID && (var9.getItemDamage() == 32767 || var6.getItemDamage() == var9.getItemDamage())) {
                     var7 = true;
                     var3.remove(var9);
                     break;
                  }
               }

               if (!var7) {
                  return false;
               }
            }
         }
      }

      return var3.isEmpty();
   }

   @Override
   public ItemStack getCraftingResult(InventoryCrafting par1InventoryCrafting) {
      return this.recipeOutput.copy();
   }

   @Override
   public int getRecipeSize() {
      return this.recipeItems.size();
   }

   @Override
   public boolean matches(IRecipe recipe) {
      if (recipe instanceof ShapelessRecipes) {
         ShapelessRecipes shapelessRecipe = (ShapelessRecipes)recipe;
         if (this.recipeOutput.getItem().itemID == shapelessRecipe.recipeOutput.getItem().itemID
            && this.recipeOutput.stackSize == shapelessRecipe.recipeOutput.stackSize
            && this.recipeOutput.getItemDamage() == shapelessRecipe.recipeOutput.getItemDamage()
            && this.recipeItems.size() == shapelessRecipe.recipeItems.size()) {
            for (int iTempIndex = 0; iTempIndex < this.recipeItems.size(); iTempIndex++) {
               ItemStack item1 = (ItemStack)this.recipeItems.get(iTempIndex);
               ItemStack item2 = (ItemStack)shapelessRecipe.recipeItems.get(iTempIndex);
               if (item1 != null && item2 != null) {
                  if (item1.getItem().itemID != item2.getItem().itemID || item1.stackSize != item2.stackSize || item1.getItemDamage() != item2.getItemDamage()) {
                     return false;
                  }
               } else if (item1 != null || item2 != null) {
                  return false;
               }
            }

            return true;
         }
      }

      return false;
   }

   @Override
   public boolean hasSecondaryOutput() {
      return this.recipeSecondaryOutputs != null;
   }

   public void setSecondaryOutput(ItemStack[] secondaryOutput) {
      this.recipeSecondaryOutputs = secondaryOutput;
   }

   @Override
   public ItemStack[] getSecondaryOutput(IInventory inventory) {
      return this.recipeSecondaryOutputs;
   }
}
