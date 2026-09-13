package net.minecraft.src;

public class RecipesFood {
   public void addRecipes(CraftingManager var1) {
      var1.addShapelessRecipe(new ItemStack(Item.bowlSoup), Block.mushroomBrown, Block.mushroomRed, Item.bowlEmpty);
      var1.addRecipe(new ItemStack(Item.cookie, 8), "#X#", 'X', new ItemStack(Item.dyePowder, 1, 3), '#', Item.wheat);
      var1.addRecipe(new ItemStack(Block.melon), "MMM", "MMM", "MMM", 'M', Item.melon);
      var1.addRecipe(new ItemStack(Item.melonSeeds), "M", 'M', Item.melon);
      var1.addRecipe(new ItemStack(Item.pumpkinSeeds, 4), "M", 'M', Block.pumpkin);
      var1.addShapelessRecipe(new ItemStack(Item.pumpkinPie), Block.pumpkin, Item.sugar, Item.egg);
      var1.addShapelessRecipe(new ItemStack(Item.fermentedSpiderEye), Item.spiderEye, Block.mushroomBrown, Item.sugar);
      var1.addShapelessRecipe(new ItemStack(Item.speckledMelon), Item.melon, Item.goldNugget);
      var1.addShapelessRecipe(new ItemStack(Item.blazePowder, 2), Item.blazeRod);
      var1.addShapelessRecipe(new ItemStack(Item.magmaCream), Item.blazePowder, Item.slimeBall);
   }
}
