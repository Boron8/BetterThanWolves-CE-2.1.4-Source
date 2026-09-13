package net.minecraft.src;

public class RecipesCrafting {
   public void addRecipes(CraftingManager var1) {
      var1.addRecipe(new ItemStack(Block.chest), "###", "# #", "###", '#', Block.planks);
      var1.addRecipe(new ItemStack(Block.chestTrapped), "#-", '#', Block.chest, '-', Block.tripWireSource);
      var1.addRecipe(new ItemStack(Block.enderChest), "###", "#E#", "###", '#', Block.obsidian, 'E', Item.eyeOfEnder);
      var1.addRecipe(new ItemStack(Block.furnaceIdle), "###", "# #", "###", '#', Block.cobblestone);
      var1.addRecipe(new ItemStack(Block.workbench), "##", "##", '#', Block.planks);
      var1.addRecipe(new ItemStack(Block.sandStone), "##", "##", '#', Block.sand);
      var1.addRecipe(new ItemStack(Block.sandStone, 4, 2), "##", "##", '#', Block.sandStone);
      var1.addRecipe(new ItemStack(Block.sandStone, 1, 1), "#", "#", '#', new ItemStack(Block.stoneSingleSlab, 1, 1));
      var1.addRecipe(new ItemStack(Block.blockNetherQuartz, 1, 1), "#", "#", '#', new ItemStack(Block.stoneSingleSlab, 1, 7));
      var1.addRecipe(new ItemStack(Block.blockNetherQuartz, 2, 2), "#", "#", '#', new ItemStack(Block.blockNetherQuartz, 1, 0));
      var1.addRecipe(new ItemStack(Block.stoneBrick, 4), "##", "##", '#', Block.stone);
      var1.addRecipe(new ItemStack(Block.fenceIron, 16), "###", "###", '#', Item.ingotIron);
      var1.addRecipe(new ItemStack(Block.thinGlass, 16), "###", "###", '#', Block.glass);
      var1.addRecipe(new ItemStack(Block.redstoneLampIdle, 1), " R ", "RGR", " R ", 'R', Item.redstone, 'G', Block.glowStone);
      var1.addRecipe(new ItemStack(Block.beacon, 1), "GGG", "GSG", "OOO", 'G', Block.glass, 'S', Item.netherStar, 'O', Block.obsidian);
      var1.addRecipe(new ItemStack(Block.netherBrick, 1), "NN", "NN", 'N', Item.netherrackBrick);
   }
}
