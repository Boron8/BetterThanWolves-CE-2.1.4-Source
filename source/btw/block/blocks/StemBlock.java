package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockStem;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class StemBlock extends BlockStem {
   private static final int STEM_TICK_RATE = 2;
   private static final double WIDTH = 0.25;
   private static final double HALF_WIDTH = 0.125;

   public StemBlock(int iBlockID, Block fruitBlock) {
      super(iBlockID, fruitBlock);
      this.c(0.0F);
      this.setBuoyant();
      this.initBlockBounds(0.375, 0.0, 0.375, 0.625, 0.25, 0.625);
      this.a(i);
      this.c("pumpkinStem");
   }

   @Override
   public int tickRate(World world) {
      return 2;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      this.e(world, i, j, k);
      if (world.getBlockId(i, j, k) == this.blockID) {
         this.validateFruitState(world, i, j, k, rand);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      this.updateTick(world, i, j, k, rand);
      if (world.getBlockId(i, j, k) == this.blockID && world.provider.dimensionId != 1) {
         this.checkForGrowth(world, i, j, k, rand);
      }
   }

   @Override
   public boolean onBlockSawed(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (iMetadata == 15 && !this.hasConnectedFruit(blockAccess, i, j, k)) {
         iMetadata = 7;
      }

      double dMaxY = (iMetadata + 1) / 16.0;
      if (dMaxY < 0.125) {
         dMaxY = 0.125;
      }

      double dHalfWidth = 0.125;
      int iWeedsGrowthLevel = this.getWeedsGrowthLevel(blockAccess, i, j, k);
      if (iWeedsGrowthLevel > 0) {
         dMaxY = Math.max(dMaxY, WeedsBlock.getWeedsBoundsHeight(iWeedsGrowthLevel));
         dHalfWidth = 0.375;
      }

      return AxisAlignedBB.getAABBPool().getAABB(0.5 - dHalfWidth, 0.0, 0.5 - dHalfWidth, 0.5 + dHalfWidth, dMaxY, 0.5 + dHalfWidth);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      super.a(world, i, j, k, iBlockID);
      if (world.getBlockId(i, j, k) == this.blockID) {
         this.validateFruitState(world, i, j, k, world.rand);
      }
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return true;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      Block blockOn = Block.blocksList[world.getBlockId(i, j, k)];
      return blockOn != null && blockOn.canDomesticatedCropsGrowOnBlock(world, i, j, k);
   }

   @Override
   public boolean canWeedsGrowInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   public void setFruitBlock(Block block) {
      this.fruitType = block;
   }

   private boolean hasConnectedFruit(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getConnectedFruitDirection(blockAccess, i, j, k) > 0;
   }

   private int getConnectedFruitDirection(IBlockAccess blockAccess, int i, int j, int k) {
      for (int iTempFacing = 2; iTempFacing < 6; iTempFacing++) {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iTempFacing);
         int iTempBlockID = blockAccess.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         if (iTempBlockID == this.fruitType.blockID
            && this.fruitType.isBlockAttachedToFacing(blockAccess, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iTempFacing))) {
            return iTempFacing;
         }
      }

      return -1;
   }

   private void validateFruitState(World world, int i, int j, int k, Random rand) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iMetadata == 15 && !this.hasConnectedFruit(world, i, j, k)) {
         world.setBlockMetadataWithNotify(i, j, k, 7);
      }
   }

   private void checkForGrowth(World world, int i, int j, int k, Random rand) {
      if (this.getWeedsGrowthLevel(world, i, j, k) == 0 && world.getBlockLightValue(i, j + 1, k) >= 9) {
         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null && blockBelow.isBlockHydratedForPlantGrowthOn(world, i, j - 1, k)) {
            float fGrowthChance = 0.2F * blockBelow.getPlantGrowthOnMultiplier(world, i, j - 1, k, this);
            if (rand.nextFloat() <= fGrowthChance) {
               int iMetadata = world.getBlockMetadata(i, j, k);
               if (iMetadata < 14) {
                  world.setBlockMetadataWithNotify(i, j, k, ++iMetadata);
               } else if (iMetadata == 14) {
                  BlockPos targetPos = new BlockPos(i, j, k);
                  int iTargetFacing = 0;
                  if (this.hasSpaceToGrow(world, i, j, k)) {
                     iTargetFacing = rand.nextInt(4) + 2;
                     targetPos.addFacingAsOffset(iTargetFacing);
                  }

                  if (this.canGrowFruitAt(world, targetPos.x, targetPos.y, targetPos.z)) {
                     blockBelow.notifyOfFullStagePlantGrowthOn(world, i, j - 1, k, this);
                     world.setBlockWithNotify(targetPos.x, targetPos.y, targetPos.z, this.fruitType.blockID);
                     if (iTargetFacing != 0) {
                        this.fruitType.attachToFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iTargetFacing));
                        world.setBlockMetadataWithNotify(i, j, k, 15);
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean hasSpaceToGrow(World world, int i, int j, int k) {
      for (int iTargetFacing = 2; iTargetFacing <= 5; iTargetFacing++) {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iTargetFacing);
         if (this.canGrowFruitAt(world, targetPos.x, targetPos.y, targetPos.z)) {
            return true;
         }
      }

      return false;
   }

   protected boolean canGrowFruitAt(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return (WorldUtils.isReplaceableBlock(world, i, j, k) || block != null && block.blockMaterial == Material.plants && iBlockID != Block.cocoaPlant.blockID)
         && (world.doesBlockHaveSolidTopSurface(i, j - 1, k) || this.canGrowOnBlock(world, i, j - 1, k));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderBlockStem(this, i, j, k);
      BTWBlocks.weeds.renderWeeds(this, renderer, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getRenderColor(int iMetadata) {
      int iRed = iMetadata * 16;
      int iGreen = 255 - iMetadata * 4;
      int iBlue = iMetadata * 2;
      return iRed << 16 | iGreen << 8 | iBlue;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int colorMultiplier(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (iMetadata == 15 && !this.hasConnectedFruit(blockAccess, i, j, k)) {
         iMetadata = 7;
      }

      return this.getRenderColor(iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (iMetadata == 15) {
         int iFruitDirection = this.getConnectedFruitDirection(blockAccess, i, j, k);
         if (iFruitDirection > 0) {
            if (iFruitDirection == 2) {
               return 2;
            }

            if (iFruitDirection == 3) {
               return 3;
            }

            if (iFruitDirection == 4) {
               return 0;
            }

            if (iFruitDirection == 5) {
               return 1;
            }
         }
      }

      return -1;
   }
}
