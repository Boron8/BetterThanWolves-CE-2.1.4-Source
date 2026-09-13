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
import net.minecraft.src.World;

public class WheatCropBlock extends DailyGrowthCropsBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconArray;
   @Environment(EnvType.CLIENT)
   private Icon[] connectToTopIconArray;

   public WheatCropBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockWheatCrop");
   }

   @Override
   protected int getSeedItemID() {
      return 0;
   }

   @Override
   protected int getCropItemID() {
      return BTWItems.straw.itemID;
   }

   @Override
   public boolean isBlockHydratedForPlantGrowthOn(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      return blockBelow != null && blockBelow.isBlockHydratedForPlantGrowthOn(world, i, j - 1, k);
   }

   @Override
   protected void incrementGrowthLevel(World world, int i, int j, int k) {
      int iGrowthLevel = this.getGrowthLevel(world, i, j, k);
      if (iGrowthLevel == 6) {
         if (world.isAirBlock(i, j + 1, k)) {
            this.setGrowthLevel(world, i, j, k, iGrowthLevel + 1);
            int iTopMetadata = 0;
            Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
            if (blockBelow == null || !blockBelow.getIsFertilizedForPlantGrowth(world, i, j - 1, k)) {
               iTopMetadata = BTWBlocks.wheatCrop.setHasGrownToday(iTopMetadata, true);
            }

            world.setBlockAndMetadataWithNotify(i, j + 1, k, BTWBlocks.wheatCropTop.blockID, iTopMetadata);
         }
      } else {
         super.incrementGrowthLevel(world, i, j, k);
      }
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      Block blockBelow = Block.blocksList[blockAccess.getBlockId(i, j - 1, k)];
      return blockBelow != null ? blockBelow.groundCoverRestingOnVisualOffset(blockAccess, i, j - 1, k) : 0.0F;
   }

   @Override
   public boolean getIsFertilizedForPlantGrowth(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      return blockBelow != null && blockBelow.getIsFertilizedForPlantGrowth(world, i, j - 1, k);
   }

   @Override
   public void notifyOfFullStagePlantGrowthOn(World world, int i, int j, int k, Block plantBlock) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         blockBelow.notifyOfFullStagePlantGrowthOn(world, i, j - 1, k, plantBlock);
      }
   }

   @Override
   protected int getLightLevelForGrowth() {
      return 11;
   }

   public boolean hasTopBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return blockAccess.getBlockId(i, j + 1, k) == BTWBlocks.wheatCropTop.blockID;
   }

   public int getTopBlockGrowthLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return BTWBlocks.wheatCropTop.getGrowthLevel(blockAccess, i, j + 1, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.iconArray = new Icon[8];

      for (int iTempIndex = 0; iTempIndex < this.iconArray.length; iTempIndex++) {
         this.iconArray[iTempIndex] = register.registerIcon("fcBlockWheatCrop_" + iTempIndex);
      }

      this.blockIcon = this.iconArray[7];
      this.connectToTopIconArray = new Icon[4];

      for (int iTempIndex = 0; iTempIndex < this.connectToTopIconArray.length; iTempIndex++) {
         this.connectToTopIconArray[iTempIndex] = register.registerIcon("fcBlockWheatCrop_7_" + iTempIndex);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iGrowthLevel = this.getGrowthLevel(blockAccess, i, j, k);
      iGrowthLevel = MathHelper.clamp_int(iGrowthLevel, 0, 7);
      if (iGrowthLevel == 7 && this.hasTopBlock(blockAccess, i, j, k)) {
         int iTopGrowthLevel = this.getTopBlockGrowthLevel(blockAccess, i, j, k);
         iTopGrowthLevel = MathHelper.clamp_int(iTopGrowthLevel, 0, 3);
         return this.connectToTopIconArray[iTopGrowthLevel];
      } else {
         return this.iconArray[iGrowthLevel];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
      if (this.isFullyGrown(world, x, y, z) && this.hasTopBlock(world, x, y, z)) {
         int topGrowthLevel = this.getTopBlockGrowthLevel(world, x, y, z);
         float verticalOffset = 0.0F;
         Block blockBelow = Block.blocksList[world.getBlockId(x, y - 1, z)];
         if (blockBelow != null) {
            verticalOffset = blockBelow.groundCoverRestingOnVisualOffset(world, x, y - 1, z);
         }

         double topHeight = (1 + topGrowthLevel) / 8.0;
         return AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0 + verticalOffset, 0.125, 0.875, 1.0 + verticalOffset + topHeight, 0.875).offset(x, y, z);
      } else {
         return super.c_(world, x, y, z);
      }
   }
}
