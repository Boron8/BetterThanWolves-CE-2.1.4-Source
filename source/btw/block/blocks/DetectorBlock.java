package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public class DetectorBlock extends Block {
   private static final int DETECTOR_TICK_RATE = 4;
   @Environment(EnvType.CLIENT)
   private Icon[] iconBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private Icon iconFront;
   @Environment(EnvType.CLIENT)
   private Icon iconFrontOn;

   public DetectorBlock(int iBlockID) {
      super(iBlockID, Material.rock);
      this.c(3.5F);
      this.a(Block.soundStoneFootstep);
      this.c("fcBlockDetectorBlock");
      this.b(true);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public int tickRate(World world) {
      return 4;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      return this.setFacing(iMetadata, Block.getOppositeFacing(iFacing));
   }

   @Override
   public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityLiving, ItemStack stack) {
      int iFacing = MiscUtils.convertPlacingEntityOrientationToBlockFacingReversed(entityLiving);
      this.setFacing(world, i, j, k, iFacing);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      this.setBlockOn(world, i, j, k, false);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int l) {
      if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bPlacedLogic = this.placeDetectorLogicIfNecessary(world, i, j, k);
      boolean bDetected = this.checkForDetection(world, i, j, k);
      int iFacingDirection = this.getFacing(world, i, j, k);
      if (iFacingDirection == 1) {
         if (!bDetected && world.isPrecipitatingAtPos(i, j + 1, k)) {
            bDetected = true;
         }

         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }

      if (bDetected) {
         if (!this.isBlockOn(world, i, j, k)) {
            this.setBlockOn(world, i, j, k, true);
         }
      } else if (this.isBlockOn(world, i, j, k)) {
         if (!bPlacedLogic) {
            this.setBlockOn(world, i, j, k, false);
         } else {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         }
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (!world.isUpdateScheduledForBlock(i, j, k, this.blockID)) {
         int iFacing = this.getFacing(world, i, j, k);
         if (iFacing == 1) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         } else if (this.checkForDetection(world, i, j, k) != this.isBlockOn(world, i, j, k)) {
            world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         }
      }
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess iblockaccess, int i, int j, int k, int l) {
      return this.isBlockOn(iblockaccess, i, j, k) ? 15 : 0;
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return 0;
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }

   @Override
   public void onArrowCollide(World world, int i, int j, int k, EntityArrow arrow) {
      if (!world.isRemote) {
         int iFacingDirection = this.getFacing(world, i, j, k);
         BlockPos logicBlockPos = new BlockPos(i, j, k);
         logicBlockPos.addFacingAsOffset(iFacingDirection);
         if (world.getBlockId(logicBlockPos.x, logicBlockPos.y, logicBlockPos.z) == BTWBlocks.detectorLogic.blockID) {
            BTWBlocks.detectorLogic.onEntityCollidedWithBlock(world, logicBlockPos.x, logicBlockPos.y, logicBlockPos.z, arrow);
         }
      }
   }

   @Override
   public int getFacing(int iMetadata) {
      return (iMetadata & -2) >> 1;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iMetadata & 1 | iFacing << 1;
   }

   @Override
   public boolean rotateAroundJAxis(World world, int i, int j, int k, boolean bReverse) {
      if (super.rotateAroundJAxis(world, i, j, k, bReverse)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean toggleFacing(World world, int i, int j, int k, boolean bReverse) {
      int iFacing = this.getFacing(world, i, j, k);
      iFacing = Block.cycleFacing(iFacing, bReverse);
      this.setFacing(world, i, j, k, iFacing);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      world.notifyBlockChange(i, j, k, this.blockID);
      return true;
   }

   public boolean isBlockOn(IBlockAccess blockAccess, int i, int j, int k) {
      return this.isBlockOnFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean isBlockOnFromMetadata(int iMetadata) {
      return (iMetadata & 1) > 0;
   }

   public void setBlockOn(World world, int i, int j, int k, boolean bOn) {
      if (bOn != this.isBlockOn(world, i, j, k)) {
         int iMetaData = world.getBlockMetadata(i, j, k);
         if (bOn) {
            iMetaData |= 1;
            world.playAuxSFX(2234, i, j, k, 0);
         } else {
            iMetaData &= -2;
         }

         world.setBlockMetadataWithNotify(i, j, k, iMetaData);
         world.notifyBlocksOfNeighborChange(i, j - 1, k, this.blockID);
         world.notifyBlocksOfNeighborChange(i, j + 1, k, this.blockID);
         world.notifyBlocksOfNeighborChange(i - 1, j, k, this.blockID);
         world.notifyBlocksOfNeighborChange(i + 1, j, k, this.blockID);
         world.notifyBlocksOfNeighborChange(i, j, k - 1, this.blockID);
         world.notifyBlocksOfNeighborChange(i, j, k + 1, this.blockID);
         world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      }
   }

   public boolean placeDetectorLogicIfNecessary(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      if (iTargetBlockID == 0) {
         DetectorLogicBlock logicBlock = (DetectorLogicBlock)BTWBlocks.detectorLogic;
         world.setBlock(targetPos.x, targetPos.y, targetPos.z, logicBlock.blockID, 0, 0);
         logicBlock.setIsDetectorLogicFlag(world, targetPos.x, targetPos.y, targetPos.z, true);
         logicBlock.fullyValidateBlock(world, targetPos.x, targetPos.y, targetPos.z);
         return true;
      } else {
         if (iTargetBlockID == BTWBlocks.detectorLogic.blockID || iTargetBlockID == BTWBlocks.glowingDetectorLogic.blockID) {
            DetectorLogicBlock logicBlock = (DetectorLogicBlock)BTWBlocks.detectorLogic;
            if (!logicBlock.isDetectorLogicFlagOn(world, targetPos.x, targetPos.y, targetPos.z)) {
               logicBlock.setIsDetectorLogicFlag(world, targetPos.x, targetPos.y, targetPos.z, true);
               if (logicBlock.hasValidLensSource(world, targetPos.x, targetPos.y, targetPos.z)) {
                  logicBlock.setIsIntersectionPointFlag(world, targetPos.x, targetPos.y, targetPos.z, true);
               }
            }
         }

         return false;
      }
   }

   public boolean checkForDetection(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      int targetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      if (targetBlockID > 0) {
         if (!DetectorLogicBlock.isLogicBlock(targetBlockID)) {
            if (targetBlockID == BTWBlocks.lens.blockID) {
               LensBlock lensBlock = (LensBlock)BTWBlocks.lens;
               if (lensBlock.getFacing(world, targetPos.x, targetPos.y, targetPos.z) == Block.getOppositeFacing(iFacing)) {
                  return lensBlock.isLit(world, targetPos.x, targetPos.y, targetPos.z);
               }
            }

            return true;
         }

         DetectorLogicBlock logicBlock = (DetectorLogicBlock)BTWBlocks.detectorLogic;
         if (logicBlock.isEntityCollidingFlagOn(world, targetPos.x, targetPos.y, targetPos.z)
            || logicBlock.isLitFlagOn(world, targetPos.x, targetPos.y, targetPos.z)) {
            return true;
         }

         int iBlockBelowID = world.getBlockId(targetPos.x, targetPos.y - 1, targetPos.z);
         if (iBlockBelowID == Block.crops.blockID && world.getBlockMetadata(targetPos.x, targetPos.y - 1, targetPos.z) >= 7) {
            return true;
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      Icon topIcon = register.registerIcon("fcBlockDetectorBlock_top");
      this.blockIcon = topIcon;
      this.iconFront = register.registerIcon("fcBlockDetectorBlock_front");
      this.iconFrontOn = register.registerIcon("fcBlockDetectorBlock_front_on");
      this.iconBySideArray[0] = register.registerIcon("fcBlockDetectorBlock_bottom");
      this.iconBySideArray[1] = topIcon;
      Icon sideIcon = register.registerIcon("fcBlockDetectorBlock_side");
      this.iconBySideArray[2] = sideIcon;
      this.iconBySideArray[3] = sideIcon;
      this.iconBySideArray[4] = sideIcon;
      this.iconBySideArray[5] = sideIcon;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return iSide == 3 ? this.iconFront : this.iconBySideArray[iSide];
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int iFacing = this.getFacing(iMetadata);
      if (iFacing == iSide) {
         return this.isBlockOnFromMetadata(iMetadata) ? this.iconFrontOn : this.iconFront;
      } else {
         return this.iconBySideArray[iSide];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (this.isBlockOn(world, i, j, k)) {
         int iFacingDirection = this.getFacing(world, i, j, k);
         float targeti = i;
         float targetj = j;
         float targetk = k;
         float targeti2 = targeti;
         float targetk2 = targetk;
         float targetj2;
         if (iFacingDirection == 0) {
            targetj2 = targetj -= 0.2F;
            targetk2 = targetk += 0.25F;
            targeti += 0.33F;
            targeti2 += 0.66F;
         } else if (iFacingDirection == 1) {
            targetj2 = ++targetj;
            targetk2 = targetk += 0.25F;
            targeti += 0.33F;
            targeti2 += 0.66F;
         } else if (iFacingDirection == 3) {
            targetj2 = targetj += 0.75F;
            targetk2 = ++targetk;
            targeti += 0.33F;
            targeti2 += 0.66F;
         } else if (iFacingDirection == 2) {
            targetj2 = targetj += 0.75F;
            targetk2 = targetk -= 0.1F;
            targeti += 0.33F;
            targeti2 += 0.66F;
         } else if (iFacingDirection == 5) {
            targeti2 = ++targeti;
            targetj2 = targetj += 0.75F;
            targetk = (float)(targetk + 0.33);
            targetk2 += 0.66F;
         } else {
            targeti2 = targeti -= 0.1F;
            targetj2 = targetj += 0.75F;
            targetk += 0.33F;
            targetk2 += 0.66F;
         }

         targeti += (random.nextFloat() - 0.5F) * 0.1F;
         targetj += (random.nextFloat() - 0.5F) * 0.1F;
         targetk += (random.nextFloat() - 0.5F) * 0.1F;
         float f = 0.06666667F;
         float f1 = f * 0.6F + 0.4F;
         float f2 = f * f * 0.7F - 0.5F;
         float f3 = f * f * 0.6F - 0.7F;
         if (f2 < 0.0F) {
            f2 = 0.0F;
         }

         if (f3 < 0.0F) {
            f3 = 0.0F;
         }

         if (random.nextFloat() >= 0.5F) {
            world.spawnParticle("reddust", targeti, targetj, targetk, f1, f2, f3);
         } else {
            world.spawnParticle("reddust", targeti2, targetj2, targetk2, f1, f2, f3);
         }
      }
   }
}
