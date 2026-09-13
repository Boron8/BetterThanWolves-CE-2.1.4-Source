package btw.crafting.recipe;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class KilnRecipeList {
   public static final byte COOK_TIME_MULTIPLIER_CLAY = 4;
   public static final byte COOK_TIME_MULTIPLIER_ORE = 8;

   public static void addRecipes() {
      RecipeManager.addKilnRecipe(new ItemStack(Item.goldNugget), BTWBlocks.goldOreChunk, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(Item.ingotGold), BTWBlocks.goldOreChunkStorage, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(Item.goldNugget), Block.oreGold, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(BTWItems.ironNugget), BTWBlocks.ironOreChunk, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(Item.ingotIron), BTWBlocks.ironOreChunkStorage, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(BTWItems.ironNugget), Block.oreIron, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(Item.coal, 1, 1), BTWBlocks.bloodWoodLog, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(Item.coal, 1, 1), Block.wood, (byte)8);
      RecipeManager.addKilnRecipe(new ItemStack(Item.brick), BTWBlocks.placedUnfiredBrick, (byte)4);
      RecipeManager.addKilnRecipe(new ItemStack(BTWBlocks.crucible), BTWBlocks.unfiredPottery, 0, (byte)4);
      RecipeManager.addKilnRecipe(new ItemStack(BTWBlocks.planter), BTWBlocks.unfiredPottery, 1, (byte)4);
      RecipeManager.addKilnRecipe(new ItemStack(BTWBlocks.vase), BTWBlocks.unfiredPottery, 2, (byte)4);
      RecipeManager.addKilnRecipe(new ItemStack(BTWItems.urn), BTWBlocks.unfiredPottery, 3, (byte)4);
      RecipeManager.addKilnRecipe(new ItemStack(BTWItems.netherBrick), BTWBlocks.unfiredPottery, new int[]{7, 8}, (byte)4);
      RecipeManager.addKilnRecipe(new ItemStack(Item.cake), BTWBlocks.unfiredPottery, 9);
      RecipeManager.addKilnRecipe(new ItemStack(Item.pumpkinPie), BTWBlocks.unfiredPottery, 12);
      RecipeManager.addKilnRecipe(new ItemStack(Item.bread), BTWBlocks.unfiredPottery, new int[]{13, 14});
      RecipeManager.addKilnRecipe(new ItemStack(Item.cookie, 8), BTWBlocks.unfiredPottery, new int[]{10, 11});
      RecipeManager.addKilnRecipe(
         new ItemStack[]{new ItemStack(BTWItems.enderSlag), new ItemStack(BTWBlocks.aestheticOpaque, 1, 10)}, Block.whiteStone, (byte)8
      );
   }
}
