package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class PickaxeItem extends ToolItem {
   public PickaxeItem(int iItemID, EnumToolMaterial material) {
      super(iItemID, 2, material);
   }

   public PickaxeItem(int iItemID, EnumToolMaterial material, int iMaxUses) {
      super(iItemID, 2, material);
      this.e(iMaxUses);
   }

   @Override
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      int iToolLevel = this.toolMaterial.getHarvestLevel();
      int iBlockToolLevel = block.getHarvestToolLevel(world, i, j, k);
      if (iBlockToolLevel > iToolLevel) {
         return false;
      } else if (block == Block.obsidian) {
         return this.toolMaterial.getHarvestLevel() >= 3;
      } else if (block == Block.blockDiamond || block == Block.blockEmerald || block == Block.blockGold) {
         return this.toolMaterial.getHarvestLevel() >= 2;
      } else {
         return block != Block.blockIron && block != Block.blockLapis
            ? block.blockMaterial == Material.rock
               || block.blockMaterial == Material.iron
               || block.blockMaterial == Material.anvil
               || block.blockMaterial == BTWBlocks.netherRockMaterial
            : this.toolMaterial.getHarvestLevel() >= 1;
      }
   }

   @Override
   public boolean isToolTypeEfficientVsBlockType(Block block) {
      return block.arePicksEffectiveOn();
   }

   @Override
   public float getStrVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      int iToolLevel = this.toolMaterial.getHarvestLevel();
      int iBlockToolLevel = block.getEfficientToolLevel(world, i, j, k);
      if (iBlockToolLevel > iToolLevel) {
         return 1.0F;
      } else {
         Material material = block.blockMaterial;
         return material != Material.iron && material != Material.rock && block.blockMaterial != Material.anvil && material != BTWBlocks.netherRockMaterial
            ? super.getStrVsBlock(stack, world, block, i, j, k)
            : this.efficiencyOnProperMaterial;
      }
   }

   @Override
   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      int iToolLevel = this.toolMaterial.getHarvestLevel();
      int iBlockToolLevel = block.getEfficientToolLevel(world, i, j, k);
      return iBlockToolLevel > iToolLevel ? false : super.isEfficientVsBlock(stack, world, block, i, j, k);
   }

   @Override
   public float getVisualVerticalOffsetAsBlock() {
      return 0.72F;
   }

   @Override
   public float getVisualHorizontalOffsetAsBlock() {
      return 0.35F;
   }

   @Override
   public float getVisualRollOffsetAsBlock() {
      return 20.0F;
   }

   @Override
   public float getBlockBoundingBoxHeight() {
      return 0.65F;
   }

   @Override
   public float getBlockBoundingBoxWidth() {
      return 1.0F;
   }

   @Override
   public void playPlacementSound(ItemStack stack, Block blockStuckIn, World world, int i, int j, int k) {
      world.playSoundEffect(i + 0.5F, j + 0.5F, k + 0.5F, "random.anvil_land", 0.5F, world.rand.nextFloat() * 0.25F + 1.75F);
   }

   @Override
   public boolean canToolStickInBlock(ItemStack stack, Block block, World world, int i, int j, int k) {
      return block.blockMaterial == Material.glass ? false : super.canToolStickInBlock(stack, block, world, i, j, k);
   }
}
