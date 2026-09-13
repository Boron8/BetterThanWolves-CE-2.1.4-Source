package btw.crafting.recipe.types;

import btw.inventory.util.InventoryUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;

public class BulkRecipe {
   private final List<ItemStack> recipeOutputStacks;
   private final List<ItemStack> recipeInputStacks;
   private final boolean metadataExclusive;

   public BulkRecipe(ItemStack recipeOutputStack, List<ItemStack> recipeInputStacks) {
      this(recipeOutputStack, recipeInputStacks, false);
   }

   public BulkRecipe(ItemStack recipeOutputStack, List<ItemStack> recipeInputStacks, boolean bMetaDataExclusive) {
      ArrayList<ItemStack> outputArrayList = new ArrayList<>();
      outputArrayList.add(recipeOutputStack.copy());
      this.recipeOutputStacks = outputArrayList;
      this.recipeInputStacks = recipeInputStacks;
      this.metadataExclusive = bMetaDataExclusive;
   }

   public BulkRecipe(List<ItemStack> recipeOutputStacks, List<ItemStack> recipeInputStacks, boolean bMetaDataExclusive) {
      this.recipeOutputStacks = recipeOutputStacks;
      this.recipeInputStacks = recipeInputStacks;
      this.metadataExclusive = bMetaDataExclusive;
   }

   public List<ItemStack> getCraftingOutputList() {
      return this.recipeOutputStacks;
   }

   public List<ItemStack> getCraftingIngrediantList() {
      return this.recipeInputStacks;
   }

   public ItemStack getFirstIngredient() {
      return this.recipeInputStacks != null && this.recipeInputStacks.size() > 0 ? this.recipeInputStacks.get(0) : null;
   }

   public boolean doesInventoryContainIngredients(IInventory inventory) {
      if (this.recipeInputStacks != null && this.recipeInputStacks.size() > 0) {
         for (int listIndex = 0; listIndex < this.recipeInputStacks.size(); listIndex++) {
            ItemStack tempStack = this.recipeInputStacks.get(listIndex);
            if (tempStack != null
               && InventoryUtils.countItemsInInventory(inventory, tempStack.getItem().itemID, tempStack.getItemDamage(), this.metadataExclusive)
                  < tempStack.stackSize) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean doesStackSatisfyIngredients(ItemStack stack) {
      if (this.recipeInputStacks != null && this.recipeInputStacks.size() == 1) {
         ItemStack recipeStack = this.recipeInputStacks.get(0);
         int recipeItemDamage = recipeStack.getItemDamage();
         if (stack.itemID == recipeStack.itemID
            && stack.stackSize >= recipeStack.stackSize
            && (
               recipeItemDamage == 32767
                  || !this.metadataExclusive && stack.getItemDamage() == recipeItemDamage
                  || this.metadataExclusive && stack.getItemDamage() != recipeItemDamage
            )) {
            return true;
         }
      }

      return false;
   }

   public boolean consumeInventoryIngredients(IInventory inventory) {
      boolean bSuccessful = true;
      if (this.recipeInputStacks != null && this.recipeInputStacks.size() > 0) {
         for (int listIndex = 0; listIndex < this.recipeInputStacks.size(); listIndex++) {
            ItemStack tempStack = this.recipeInputStacks.get(listIndex);
            if (tempStack != null
               && !InventoryUtils.consumeItemsInInventory(
                  inventory, tempStack.getItem().itemID, tempStack.getItemDamage(), tempStack.stackSize, this.metadataExclusive
               )) {
               bSuccessful = false;
            }
         }
      }

      return bSuccessful;
   }

   public boolean matches(BulkRecipe recipe) {
      if (this.metadataExclusive == recipe.metadataExclusive
         && this.recipeInputStacks.size() == recipe.recipeInputStacks.size()
         && this.recipeOutputStacks.size() == recipe.recipeOutputStacks.size()) {
         for (int iListIndex = 0; iListIndex < this.recipeInputStacks.size(); iListIndex++) {
            if (!this.doStacksMatch(this.recipeInputStacks.get(iListIndex), recipe.recipeInputStacks.get(iListIndex))) {
               return false;
            }
         }

         for (int iListIndexx = 0; iListIndexx < this.recipeOutputStacks.size(); iListIndexx++) {
            if (!this.doStacksMatch(this.recipeOutputStacks.get(iListIndexx), recipe.recipeOutputStacks.get(iListIndexx))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean doStacksMatch(ItemStack stack1, ItemStack stack2) {
      return stack1.getItem().itemID == stack2.getItem().itemID && stack1.stackSize == stack2.stackSize && stack1.getItemDamage() == stack2.getItemDamage();
   }
}
