package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.crafting.util.FurnaceBurnTime;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class BloodWoodLogBlock extends Block {
   private static final float HARDNESS = 2.0F;
   @Environment(EnvType.CLIENT)
   private Icon iconSide;

   public BloodWoodLogBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.logMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn(true);
      this.setBuoyancy(1.0F);
      this.setFurnaceBurnTime(4 * FurnaceBurnTime.PLANKS_BLOOD.burnTime);
      this.setFireProperties(Flammability.EXTREME);
      this.b(true);
      this.a(BTWBlocks.stepSoundSquish);
      this.c("fcBlockBloodWood");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, iFacing);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      world.playAuxSFX(2225, i, j, k, 0);
      this.notifySurroundingBloodLeavesOfBlockRemoval(world, i, j, k);
      super.breakBlock(world, i, j, k, iBlockID, iMetadata);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      if (this.getCanGrow(world, i, j, k)) {
         int iGrowthDirection = this.getFacing(world, i, j, k);
         if (iGrowthDirection != 0 && world.provider.dimensionId == -1) {
            this.grow(world, i, j, k, random);
         }

         this.setCanGrow(world, i, j, k, false);
      }
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sawDust.itemID, 4, 0, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.bark.itemID, 1, 4, fChanceOfDrop);
      this.dropItemsIndividually(world, i, j, k, BTWItems.soulDust.itemID, 1, 0, fChanceOfDrop);
      return true;
   }

   @Override
   public int getFacing(int iMetadata) {
      return iMetadata & 7;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iMetadata &= -8;
      return iMetadata | iFacing;
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      return true;
   }

   @Override
   public boolean isLog(IBlockAccess blockAccess, int x, int y, int z) {
      return true;
   }

   public boolean getCanGrow(IBlockAccess blockAccess, int i, int j, int k) {
      return (blockAccess.getBlockMetadata(i, j, k) & 8) > 0;
   }

   public void setCanGrow(World world, int i, int j, int k, boolean bCanGrow) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -9;
      if (bCanGrow) {
         iMetaData |= 8;
      }

      world.setBlockMetadata(i, j, k, iMetaData);
   }

   public void grow(World world, int i, int j, int k, Random random) {
      if (this.countBloodWoodNeighboringOnBlockWithSoulSand(world, i, j, k) < 2) {
         int iGrowthDirection = this.getFacing(world, i, j, k);
         if (iGrowthDirection == 1) {
            int iRandomFactor = random.nextInt(100);
            if (iRandomFactor < 25) {
               this.attemptToGrowIntoBlock(world, i, j + 1, k, 1);
            } else if (iRandomFactor < 90) {
               int iTargetFacing = random.nextInt(4) + 2;
               BlockPos targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(iTargetFacing);
               this.attemptToGrowIntoBlock(world, targetPos.x, targetPos.y, targetPos.z, iTargetFacing);
               this.attemptToGrowIntoBlock(world, i, j + 1, k, 1);
            } else {
               for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
                  int iTargetFacing = random.nextInt(4) + 2;
                  BlockPos targetPos = new BlockPos(i, j, k);
                  targetPos.addFacingAsOffset(iTargetFacing);
                  this.attemptToGrowIntoBlock(world, targetPos.x, targetPos.y, targetPos.z, iTargetFacing);
               }
            }
         } else {
            int iRandomFactor = random.nextInt(100);
            if (iRandomFactor < 40) {
               this.attemptToGrowIntoBlock(world, i, j + 1, k, iGrowthDirection);
               this.setFacing(world, i, j, k, 1);
            } else if (iRandomFactor < 65) {
               BlockPos targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(iGrowthDirection);
               this.attemptToGrowIntoBlock(world, targetPos.x, targetPos.y, targetPos.z, iGrowthDirection);
            } else if (iRandomFactor < 90) {
               int iTargetFacing = random.nextInt(4) + 2;
               if (iTargetFacing == iGrowthDirection) {
                  iTargetFacing = 1;
               }

               BlockPos targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(iTargetFacing);
               int iTargetGrowthDirection = iGrowthDirection;
               if (iTargetFacing >= 2) {
                  iTargetGrowthDirection = iTargetFacing;
               }

               this.attemptToGrowIntoBlock(world, targetPos.x, targetPos.y, targetPos.z, iTargetGrowthDirection);
               targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(iGrowthDirection);
               if (!this.attemptToGrowIntoBlock(world, targetPos.x, targetPos.y, targetPos.z, iGrowthDirection) && iTargetFacing == 1) {
                  this.setFacing(world, i, j, k, 1);
               }
            } else {
               int[] iGrowthDirections = new int[2];

               for (int iTempCount = 0; iTempCount < 2; iTempCount++) {
                  iGrowthDirections[iTempCount] = 0;
                  int iTargetFacingx = random.nextInt(4) + 2;
                  if (iTargetFacingx == iGrowthDirection) {
                     iTargetFacingx = 1;
                  }

                  BlockPos targetPosx = new BlockPos(i, j, k);
                  targetPosx.addFacingAsOffset(iTargetFacingx);
                  int iTargetGrowthDirectionx = iGrowthDirection;
                  if (iTargetFacingx >= 2) {
                     iTargetGrowthDirectionx = iTargetFacingx;
                  }

                  if (this.attemptToGrowIntoBlock(world, targetPosx.x, targetPosx.y, targetPosx.z, iTargetGrowthDirectionx)) {
                     iGrowthDirections[iTempCount] = iTargetFacingx;
                  }
               }

               if (iGrowthDirections[0] == 1 && iGrowthDirections[1] <= 1 || iGrowthDirections[1] == 1 && iGrowthDirections[0] == 0) {
                  this.setFacing(world, i, j, k, 1);
               }
            }
         }
      }
   }

   public boolean attemptToGrowIntoBlock(World world, int i, int j, int k, int iGrowthDirection) {
      if ((world.isAirBlock(i, j, k) || this.isBloodLeafBlock(world, i, j, k)) && this.countBloodWoodNeighboringOnBlockWithSoulSand(world, i, j, k) < 2) {
         world.setBlockAndMetadataWithNotify(i, j, k, this.blockID, iGrowthDirection | 8);
         this.growLeaves(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   public void growLeaves(World world, int i, int j, int k) {
      for (int tempI = i - 1; tempI <= i + 1; tempI++) {
         for (int tempJ = j - 1; tempJ <= j + 1; tempJ++) {
            for (int tempK = k - 1; tempK <= k + 1; tempK++) {
               if (world.isAirBlock(tempI, tempJ, tempK)) {
                  world.setBlockAndMetadataWithNotify(tempI, tempJ, tempK, BTWBlocks.bloodWoodLeaves.blockID, 0);
               }
            }
         }
      }
   }

   public boolean isBloodLeafBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID == BTWBlocks.bloodWoodLeaves.blockID) {
         return true;
      } else {
         if (iBlockID == BTWBlocks.aestheticVegetation.blockID) {
            int iSubType = world.getBlockMetadata(i, j, k);
            if (iSubType == 3) {
               return true;
            }
         }

         return false;
      }
   }

   public int countBloodWoodNeighboringOnBlockWithSoulSand(World world, int i, int j, int k) {
      int iNeighborWoodCount = 0;

      for (int iTempFacing = 0; iTempFacing < 6; iTempFacing++) {
         BlockPos tempTargetPos = new BlockPos(i, j, k);
         tempTargetPos.addFacingAsOffset(iTempFacing);
         if (world.getBlockId(tempTargetPos.x, tempTargetPos.y, tempTargetPos.z) == this.blockID) {
            iNeighborWoodCount++;
         }
      }

      if (world.getBlockId(i, j - 1, k) == Block.slowSand.blockID) {
         iNeighborWoodCount++;
      }

      return iNeighborWoodCount;
   }

   public int countBloodWoodNeighboringOnBlockIncludingDiagnals(World world, int i, int j, int k) {
      int iNeighborWoodCount = 0;

      for (int tempI = i - 1; tempI <= i + 1; tempI++) {
         for (int tempJ = j - 1; tempJ <= j + 1; tempJ++) {
            for (int tempK = k - 1; tempK <= k + 1; tempK++) {
               if (world.getBlockId(tempI, tempJ, tempK) == this.blockID && (tempI != i || tempJ != j || tempK != k)) {
                  iNeighborWoodCount++;
               }
            }
         }
      }

      return iNeighborWoodCount;
   }

   public void notifySurroundingBloodLeavesOfBlockRemoval(World world, int i, int j, int k) {
      byte byte0 = 4;
      int l = byte0 + 1;
      if (world.checkChunksExist(i - l, j - l, k - l, i + l, j + l, k + l)) {
         for (int i1 = -byte0; i1 <= byte0; i1++) {
            for (int j1 = -byte0; j1 <= byte0; j1++) {
               for (int k1 = -byte0; k1 <= byte0; k1++) {
                  int l1 = world.getBlockId(i + i1, j + j1, k + k1);
                  if (l1 == BTWBlocks.bloodWoodLeaves.blockID) {
                     int i2 = world.getBlockMetadata(i + i1, j + j1, k + k1);
                     if ((i2 & 8) == 0) {
                        world.setBlockMetadata(i + i1, j + j1, k + k1, i2 | 8);
                     }
                  }
               }
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconSide = register.registerIcon("fcBlockBloodWood_side");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iFacing = iMetadata & -9;
      if (iFacing < 2) {
         if (iSide >= 2) {
            return this.iconSide;
         }
      } else if (iFacing < 4) {
         if (iSide != 2 && iSide != 3) {
            return this.iconSide;
         }
      } else if (iSide < 4) {
         return this.iconSide;
      }

      return this.blockIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      IBlockAccess blockAccess = renderer.blockAccess;
      int iFacing = this.getFacing(blockAccess, i, j, k);
      if (iFacing == 2) {
         renderer.setUVRotateSouth(1);
         renderer.setUVRotateNorth(2);
      } else if (iFacing == 3) {
         renderer.setUVRotateSouth(2);
         renderer.setUVRotateNorth(1);
         renderer.setUVRotateTop(3);
         renderer.setUVRotateBottom(3);
      } else if (iFacing == 4) {
         renderer.setUVRotateEast(1);
         renderer.setUVRotateWest(2);
         renderer.setUVRotateTop(2);
         renderer.setUVRotateBottom(1);
      } else if (iFacing == 5) {
         renderer.setUVRotateEast(2);
         renderer.setUVRotateWest(1);
         renderer.setUVRotateTop(1);
         renderer.setUVRotateBottom(2);
      }

      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderStandardBlock(this, i, j, k);
      renderer.clearUVRotation();
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      this.renderCookingByKiLnOverlay(renderBlocks, i, j, k, bFirstPassResult);
   }
}
