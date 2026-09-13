package btw.crafting.recipe;

import btw.block.BTWBlocks;
import btw.crafting.recipe.types.TurntableRecipe;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class TurntableRecipeList {
   private static final int NUM_ROTATIONS_POTTERY = 8;
   public static TurntableRecipe.TurntableEffect potteryEffect = (world, x, y, z) -> world.playAuxSFX(2252, x, y, z, BTWBlocks.unfiredClay.blockID);

   public static void addRecipes() {
      RecipeManager.addTurntableRecipe(BTWBlocks.unfiredPottery, 0, new ItemStack[]{new ItemStack(Item.clay, 1)}, BTWBlocks.unfiredClay, 8)
         .setEffect(potteryEffect);
      RecipeManager.addTurntableRecipe(BTWBlocks.unfiredPottery, 1, new ItemStack[]{new ItemStack(Item.clay, 2)}, BTWBlocks.unfiredPottery, 0, 8)
         .setEffect(potteryEffect);
      RecipeManager.addTurntableRecipe(BTWBlocks.unfiredPottery, 2, new ItemStack[]{new ItemStack(Item.clay, 2)}, BTWBlocks.unfiredPottery, 1, 8)
         .setEffect(potteryEffect);
      RecipeManager.addTurntableRecipe(BTWBlocks.unfiredPottery, 3, new ItemStack[]{new ItemStack(Item.clay, 2)}, BTWBlocks.unfiredPottery, 2, 8)
         .setEffect(potteryEffect);
      RecipeManager.addTurntableRecipe(null, 0, new ItemStack[]{new ItemStack(Item.clay, 2)}, BTWBlocks.unfiredPottery, 3, 8).setEffect(potteryEffect);
   }
}
