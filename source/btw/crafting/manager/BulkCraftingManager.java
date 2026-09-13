package btw.crafting.manager;

import btw.crafting.recipe.types.BulkRecipe;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public abstract class BulkCraftingManager {
   private List<BulkRecipe> recipes = new ArrayList<>();

   protected BulkCraftingManager() {
   }

   public void addRecipe(ItemStack outputStack, ItemStack[] inputStacks) {
      this.addRecipe(outputStack, inputStacks, false);
   }

   public void addRecipe(ItemStack outputStack, ItemStack inputStack) {
      this.addRecipe(outputStack, inputStack, false);
   }

   public void addRecipe(ItemStack[] outputStacks, ItemStack[] inputStacks) {
      this.addRecipe(outputStacks, inputStacks, false);
   }

   public void addRecipe(ItemStack outputStack, ItemStack[] inputStacks, boolean bMetadataExclusive) {
      ItemStack[] outputStacks = new ItemStack[]{outputStack.copy()};
      this.addRecipe(outputStacks, inputStacks, bMetadataExclusive);
   }

   public void addRecipe(ItemStack outputStack, ItemStack inputStack, boolean bMetadataExclusive) {
      ItemStack[] outputStacks = new ItemStack[]{outputStack.copy()};
      ItemStack[] inputStacks = new ItemStack[]{inputStack.copy()};
      this.addRecipe(outputStacks, inputStacks, bMetadataExclusive);
   }

   public void addRecipe(ItemStack[] outputStacks, ItemStack[] inputStacks, boolean bMetadataExclusive) {
      BulkRecipe recipe = this.createRecipe(outputStacks, inputStacks, bMetadataExclusive);
      this.recipes.add(recipe);
   }

   public boolean removeRecipe(ItemStack outputStack, ItemStack[] inputStacks) {
      return this.removeRecipe(outputStack, inputStacks, false);
   }

   public boolean removeRecipe(ItemStack outputStack, ItemStack inputStack) {
      return this.removeRecipe(outputStack, inputStack, false);
   }

   public boolean removeRecipe(ItemStack[] outputStacks, ItemStack[] inputStacks) {
      return this.removeRecipe(outputStacks, inputStacks, false);
   }

   public boolean removeRecipe(ItemStack outputStack, ItemStack[] inputStacks, boolean bMetadataExclusive) {
      ItemStack[] outputStacks = new ItemStack[]{outputStack.copy()};
      return this.removeRecipe(outputStacks, inputStacks, bMetadataExclusive);
   }

   public boolean removeRecipe(ItemStack outputStack, ItemStack inputStack, boolean bMetadataExclusive) {
      ItemStack[] outputStacks = new ItemStack[]{outputStack.copy()};
      ItemStack[] inputStacks = new ItemStack[]{inputStack.copy()};
      return this.removeRecipe(outputStacks, inputStacks, bMetadataExclusive);
   }

   public boolean removeRecipe(ItemStack[] outputStacks, ItemStack[] inputStacks, boolean bMetadataExclusive) {
      BulkRecipe recipe = this.createRecipe(outputStacks, inputStacks, bMetadataExclusive);
      int iMatchingIndex = this.getMatchingRecipeIndex(recipe);
      if (iMatchingIndex >= 0) {
         this.recipes.remove(iMatchingIndex);
         return true;
      } else {
         return false;
      }
   }

   public List<ItemStack> getCraftingResult(IInventory inventory) {
      for (int i = 0; i < this.recipes.size(); i++) {
         BulkRecipe tempRecipe = this.recipes.get(i);
         if (tempRecipe.doesInventoryContainIngredients(inventory)) {
            return tempRecipe.getCraftingOutputList();
         }
      }

      return null;
   }

   public List<ItemStack> getCraftingResult(ItemStack inputStack) {
      for (int i = 0; i < this.recipes.size(); i++) {
         BulkRecipe tempRecipe = this.recipes.get(i);
         if (tempRecipe.doesStackSatisfyIngredients(inputStack)) {
            return tempRecipe.getCraftingOutputList();
         }
      }

      return null;
   }

   public List<ItemStack> getValidCraftingIngrediants(IInventory inventory) {
      for (int i = 0; i < this.recipes.size(); i++) {
         BulkRecipe tempRecipe = this.recipes.get(i);
         if (tempRecipe.doesInventoryContainIngredients(inventory)) {
            return tempRecipe.getCraftingIngrediantList();
         }
      }

      return null;
   }

   public ItemStack getValidSingleIngredient(ItemStack inputStack) {
      for (int i = 0; i < this.recipes.size(); i++) {
         BulkRecipe tempRecipe = this.recipes.get(i);
         if (tempRecipe.doesStackSatisfyIngredients(inputStack)) {
            return tempRecipe.getFirstIngredient();
         }
      }

      return null;
   }

   public boolean hasRecipeForSingleIngredient(ItemStack inputStack) {
      return this.getValidSingleIngredient(inputStack) != null;
   }

   public List<ItemStack> consumeIngredientsAndReturnResult(IInventory inventory) {
      for (int i = 0; i < this.recipes.size(); i++) {
         BulkRecipe tempRecipe = this.recipes.get(i);
         if (tempRecipe.doesInventoryContainIngredients(inventory)) {
            tempRecipe.consumeInventoryIngredients(inventory);
            return tempRecipe.getCraftingOutputList();
         }
      }

      return null;
   }

   private BulkRecipe createRecipe(ItemStack[] outputStacks, ItemStack[] inputStacks, boolean bMetadataExclusive) {
      ArrayList<ItemStack> inputArrayList = new ArrayList<>();
      int iInputStacksArrayLength = inputStacks.length;

      for (int iTempIndex = 0; iTempIndex < iInputStacksArrayLength; iTempIndex++) {
         inputArrayList.add(inputStacks[iTempIndex].copy());
      }

      ArrayList<ItemStack> outputArrayList = new ArrayList<>();
      int iOutputStacksArrayLength = outputStacks.length;

      for (int iTempIndex = 0; iTempIndex < iOutputStacksArrayLength; iTempIndex++) {
         outputArrayList.add(outputStacks[iTempIndex].copy());
      }

      return new BulkRecipe(outputArrayList, inputArrayList, bMetadataExclusive);
   }

   private int getMatchingRecipeIndex(BulkRecipe recipe) {
      int iMatchingRecipeIndex = -1;

      for (int iIndex = 0; iIndex < this.recipes.size(); iIndex++) {
         BulkRecipe tempRecipe = this.recipes.get(iIndex);
         if (tempRecipe.matches(recipe)) {
            return iIndex;
         }
      }

      return -1;
   }
}
