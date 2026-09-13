package btw.crafting.manager;

import btw.crafting.recipe.types.KilnRecipe;
import java.util.ArrayList;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;

public class KilnCraftingManager {
   public static KilnCraftingManager instance = new KilnCraftingManager();
   private ArrayList<KilnRecipe> recipes = new ArrayList<>();

   private KilnCraftingManager() {
   }

   public void addRecipe(ItemStack[] output, Block block, int[] metadatas, byte cookTimeMultiplier) {
      KilnRecipe recipe = new KilnRecipe(output, block, metadatas, cookTimeMultiplier);
      this.recipes.add(recipe);
      Block.hasKilnRecipe[block.blockID] = true;
   }

   public boolean removeRecipe(ItemStack[] output, Block block, int[] metadatas, byte cookTimeMultiplier) {
      KilnRecipe recipeToRemove = new KilnRecipe(output, block, metadatas, cookTimeMultiplier);

      for (KilnRecipe recipe : this.recipes) {
         if (recipe.matchesRecipe(recipeToRemove)) {
            this.recipes.remove(recipe);
            Block.hasKilnRecipe[block.blockID] = false;
            return true;
         }
      }

      return false;
   }

   public KilnRecipe getRecipe(Block block, int metadata) {
      for (KilnRecipe recipe : this.recipes) {
         if (recipe.matchesInputs(block, metadata)) {
            return recipe;
         }
      }

      return null;
   }

   public ItemStack[] getRecipeResult(Block block, int metadata) {
      for (KilnRecipe recipe : this.recipes) {
         if (recipe.matchesInputs(block, metadata)) {
            return recipe.getOutput();
         }
      }

      return null;
   }
}
