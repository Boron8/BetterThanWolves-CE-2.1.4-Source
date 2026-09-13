package btw.crafting.recipe;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class SoulforgeRecipeList {
   public static void addRecipes() {
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.steelSword, 1), new Object[]{"#", "#", "#", "X", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.steelShovel, 1), new Object[]{"#", "X", "X", "X", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.steelPickaxe, 1), new Object[]{"###", " X ", " X ", " X ", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.mattock, 1), new Object[]{" ###", "# X ", "  X ", "  X ", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.steelHoe, 1), new Object[]{"##", " X", " X", " X", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.battleaxe, 1), new Object[]{"###", "#X#", " X ", " X ", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.steelAxe, 1), new Object[]{"# ", "#X", " X", " X", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.haft}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.plateHelmet, 1),
         new Object[]{"####", "#  #", "#  #", " XX ", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.steelArmorPlate}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.plateBreastplate, 1),
         new Object[]{"X  X", "####", "####", "####", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.steelArmorPlate}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.plateLeggings, 1),
         new Object[]{"####", "X##X", "X  X", "X  X", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.steelArmorPlate}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.plateBoots, 1), new Object[]{" ## ", " ## ", "#XX#", '#', BTWItems.soulforgedSteelIngot, 'X', BTWItems.steelArmorPlate}
      );
      RecipeManager.addSoulforgeRecipe(new ItemStack(BTWItems.broadheadArrowHead, 2), new Object[]{" # ", " # ", "###", " # ", '#', BTWItems.steelNugget});
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWBlocks.steelPressurePlate, 2), new Object[]{"####", " rr ", '#', BTWItems.soulforgedSteelIngot, 'r', Item.redstone}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWBlocks.detectorBlock),
         new Object[]{
            "####",
            "XTTX",
            "#YY#",
            "#YY#",
            '#',
            new ItemStack(BTWItems.stoneBrick, 1, 32767),
            'X',
            BTWItems.redstoneEye,
            'Y',
            Item.redstone,
            'T',
            Block.torchRedstoneActive
         }
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWBlocks.blockDispenser),
         new Object[]{
            "####",
            "#ZZ#",
            "YTTY",
            "YXXY",
            '#',
            new ItemStack(Block.cobblestoneMossy, 1, 32767),
            'X',
            Item.redstone,
            'Y',
            new ItemStack(BTWItems.stoneBrick, 1, 32767),
            'Z',
            BTWItems.soulUrn,
            'T',
            Block.torchRedstoneActive
         }
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWBlocks.buddyBlock, 1),
         new Object[]{
            "##X#", "XYY#", "#YYX", "#X##", '#', new ItemStack(BTWItems.stoneBrick, 1, 32767), 'X', BTWItems.redstoneEye, 'Y', Block.torchRedstoneActive
         }
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWBlocks.soulforgedSteelBlock, 1), new Object[]{"####", "####", "####", "####", '#', BTWItems.soulforgedSteelIngot}
      );
      RecipeManager.addSoulforgeRecipe(new ItemStack(BTWItems.tuningFork, 6), new Object[]{"# #", "# #", " # ", " # ", '#', BTWItems.soulforgedSteelIngot});
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWItems.canvas),
         new Object[]{"MMMM", "MFFM", "MFFM", "MMMM", 'F', BTWItems.fabric, 'M', new ItemStack(BTWItems.woodMouldingStubID, 1, 32767)}
      );
      RecipeManager.addSoulforgeRecipe(
         new ItemStack(BTWBlocks.aestheticOpaque, 1, 13), new Object[]{"#  #", "#  #", "####", '#', new ItemStack(BTWItems.stoneBrick, 1, 32767)}
      );
      RecipeManager.addSoulforgeRecipe(new ItemStack(BTWItems.mail, 2), new Object[]{"# # ", " # #", "# # ", " # #", '#', BTWItems.ironNugget});
      RecipeManager.addSoulforgeRecipe(new ItemStack(BTWItems.mail, 2), new Object[]{" # #", "# # ", " # #", "# # ", '#', BTWItems.ironNugget});
      RecipeManager.addSoulforgeRecipe(new ItemStack(BTWBlocks.dormandSoulforge), new Object[]{"####", " #  ", " #  ", "####", '#', Item.ingotGold});
   }
}
