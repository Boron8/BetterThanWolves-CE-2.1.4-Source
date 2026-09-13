package net.minecraft.src;

public class ItemPickaxe extends ItemTool {
   private static Block[] blocksEffectiveAgainst = new Block[]{
      Block.cobblestone,
      Block.stoneDoubleSlab,
      Block.stoneSingleSlab,
      Block.stone,
      Block.sandStone,
      Block.cobblestoneMossy,
      Block.oreIron,
      Block.blockIron,
      Block.oreCoal,
      Block.blockGold,
      Block.oreGold,
      Block.oreDiamond,
      Block.blockDiamond,
      Block.ice,
      Block.netherrack,
      Block.oreLapis,
      Block.blockLapis,
      Block.oreRedstone,
      Block.oreRedstoneGlowing,
      Block.rail,
      Block.railDetector,
      Block.railPowered,
      Block.railActivator
   };

   protected ItemPickaxe(int var1, EnumToolMaterial var2) {
      super(var1, 2, var2, blocksEffectiveAgainst);
   }

   public boolean canHarvestBlock(Block var1) {
      if (var1 == Block.obsidian) {
         return this.toolMaterial.getHarvestLevel() == 3;
      } else if (var1 == Block.blockDiamond || var1 == Block.oreDiamond) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (var1 == Block.oreEmerald || var1 == Block.blockEmerald) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (var1 == Block.blockGold || var1 == Block.oreGold) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (var1 == Block.blockIron || var1 == Block.oreIron) {
         return this.toolMaterial.getHarvestLevel() >= 1;
      } else if (var1 == Block.blockLapis || var1 == Block.oreLapis) {
         return this.toolMaterial.getHarvestLevel() >= 1;
      } else if (var1 == Block.oreRedstone || var1 == Block.oreRedstoneGlowing) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else if (var1.blockMaterial == Material.rock) {
         return true;
      } else {
         return var1.blockMaterial == Material.iron ? true : var1.blockMaterial == Material.anvil;
      }
   }

   @Override
   public float getStrVsBlock(ItemStack var1, Block var2) {
      return var2 == null || var2.blockMaterial != Material.iron && var2.blockMaterial != Material.anvil && var2.blockMaterial != Material.rock
         ? super.getStrVsBlock(var1, var2)
         : this.efficiencyOnProperMaterial;
   }
}
