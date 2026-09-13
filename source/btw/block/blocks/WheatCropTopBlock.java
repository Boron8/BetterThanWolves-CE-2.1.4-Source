package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class WheatCropTopBlock extends DailyGrowthCropsBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconArray;

   public WheatCropTopBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockWheatCropTop");
   }

   @Override
   public boolean doesBlockDropAsItemOnSaw(World world, int i, int j, int k) {
      return true;
   }

   @Override
   protected int getSeedItemID() {
      return 0;
   }

   @Override
   protected int getCropItemID() {
      return BTWItems.wheat.itemID;
   }

   @Override
   protected boolean isFullyGrown(int iMetadata) {
      return this.getGrowthLevel(iMetadata) >= 3;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      return world.getBlockId(i, j, k) == BTWBlocks.wheatCrop.blockID;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int x, int y, int z) {
      float verticalOffset = 0.0F;
      Block blockBelow = Block.blocksList[blockAccess.getBlockId(x, y - 1, z)];
      if (blockBelow != null) {
         verticalOffset = blockBelow.groundCoverRestingOnVisualOffset(blockAccess, x, y - 1, z);
      }

      int metadata = blockAccess.getBlockMetadata(x, y, z);
      int growthLevel = this.getGrowthLevel(metadata);
      return AxisAlignedBB.getAABBPool().getAABB(0.125, verticalOffset - 1.0F, 0.125, 0.875, (1 + growthLevel) / 8.0 + verticalOffset, 0.875);
   }

   @Override
   public void breakBlock(World world, int x, int y, int z, int blockID, int metadata) {
      super.a(world, x, y, z, blockID, metadata);
      int blockBelowID = world.getBlockId(x, y - 1, z);
      if (blockBelowID == BTWBlocks.wheatCrop.blockID) {
         int blockBelowMetadata = world.getBlockMetadata(x, y - 1, z);
         if (this.isFullyGrown(metadata)) {
            BTWBlocks.wheatCrop.a(world, x, y, z, blockBelowMetadata, 1.0F, 0);
         }

         world.setBlockToAir(x, y - 1, z);
      }
   }

   @Override
   protected void updateFlagForGrownToday(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null && (!blockBelow.getIsFertilizedForPlantGrowth(world, i, j - 1, k) || this.getGrowthLevel(world, i, j, k) % 2 == 1)) {
         this.setHasGrownToday(world, i, j, k, true);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconArray = new Icon[4];

      for (int iTempIndex = 0; iTempIndex < this.iconArray.length; iTempIndex++) {
         this.iconArray[iTempIndex] = register.registerIcon("fcBlockWheatCropTop_" + iTempIndex);
      }

      this.blockIcon = this.iconArray[3];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iGrowthLevel = this.getGrowthLevel(blockAccess, i, j, k);
      iGrowthLevel = MathHelper.clamp_int(iGrowthLevel, 0, 3);
      return this.iconArray[iGrowthLevel];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      this.renderCrops(renderer, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      if (!this.isFullyGrown(world, i, j, k)) {
         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null) {
            return blockBelow.getSelectedBoundingBoxFromPool(world, i, j - 1, k);
         }
      }

      return super.c_(world, i, j, k);
   }
}
