package btw.crafting.manager;

import btw.crafting.recipe.types.PistonPackingRecipe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.ItemStack;

public class PistonPackingCraftingManager {
   public static PistonPackingCraftingManager instance = new PistonPackingCraftingManager();
   private ArrayList<PistonPackingRecipe> recipes = new ArrayList<>();

   private PistonPackingCraftingManager() {
   }

   public void addRecipe(Block output, int outputMetadata, ItemStack[] input) {
      this.recipes.add(new PistonPackingRecipe(output, outputMetadata, input));
   }

   public boolean removeRecipe(Block output, int outputMetadata, ItemStack[] input) {
      PistonPackingRecipe recipeToRemove = new PistonPackingRecipe(output, outputMetadata, input);

      for (PistonPackingRecipe recipe : this.recipes) {
         if (recipe.matchesRecipe(recipeToRemove)) {
            return true;
         }
      }

      return false;
   }

   public PistonPackingRecipe getRecipe(ItemStack[] input) {
      for (PistonPackingRecipe recipe : this.recipes) {
         if (recipe.matchesInputs(input)) {
            return recipe;
         }
      }

      return null;
   }

   public PistonPackingRecipe getValidRecipeFromItemList(List<EntityItem> itemList) {
      if (itemList != null && !itemList.isEmpty()) {
         Map<Integer, Map<Integer, Integer>> itemCountMap = new HashMap<>();

         for (EntityItem entity : itemList) {
            if (!entity.isDead) {
               int itemID = entity.getEntityItem().itemID;
               int metadata = entity.getEntityItem().getItemDamage();
               int stackSize = entity.getEntityItem().stackSize;
               if (itemCountMap.containsKey(itemID)) {
                  Map<Integer, Integer> metadataMap = itemCountMap.get(itemID);
                  int currentSize = 0;
                  if (metadataMap.containsKey(metadata)) {
                     currentSize = metadataMap.get(metadata);
                  }

                  metadataMap.put(metadata, currentSize + stackSize);
               } else {
                  itemCountMap.put(itemID, new HashMap<>());
                  Map<Integer, Integer> metadataMap = itemCountMap.get(itemID);
                  metadataMap.put(metadata, stackSize);
               }
            }
         }

         for (PistonPackingRecipe recipe : this.recipes) {
            boolean recipeMatch = true;

            for (ItemStack recipeStack : recipe.getInput()) {
               if (!itemCountMap.containsKey(recipeStack.itemID)) {
                  recipeMatch = false;
                  break;
               }

               if (recipeStack.getItemDamage() != 32767) {
                  if (!itemCountMap.get(recipeStack.itemID).containsKey(recipeStack.getItemDamage())) {
                     recipeMatch = false;
                     break;
                  }

                  int count = itemCountMap.get(recipeStack.itemID).get(recipeStack.getItemDamage());
                  if (count < recipeStack.stackSize) {
                     recipeMatch = false;
                     break;
                  }
               } else {
                  int totalCount = 0;

                  for (Integer i : itemCountMap.get(recipeStack.itemID).values()) {
                     totalCount += i;
                  }

                  if (totalCount < recipeStack.stackSize) {
                     recipeMatch = false;
                     break;
                  }
               }
            }

            if (recipeMatch) {
               return recipe;
            }
         }

         return null;
      } else {
         return null;
      }
   }
}
