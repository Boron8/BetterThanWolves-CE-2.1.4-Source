package btw.block.blocks;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.entity.mechanical.platform.BlockLiftedByPlatformEntity;
import btw.world.util.BlockPos;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFX;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class DetectorLogicBlock extends Block {
   private static final int DETECTOR_LOGIC_TICK_RATE = 5;
   public static final boolean LOGIC_DEBUG_DISPLAY = false;

   public DetectorLogicBlock(int iBlockID) {
      super(iBlockID, Material.air);
      this.b(true);
   }

   @Override
   public int tickRate(World world) {
      return 5;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public int getMobilityFlag() {
      return 1;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
   }

   @Override
   public void breakBlock(World world, int i, int j, int k, int iBlockID, int iMetadata) {
      if (!BTWMod.isLensBeamBeingRemoved && (!this.isDetectorLogicFlagOn(world, i, j, k) || this.isIntersectionPointFlagOn(world, i, j, k))) {
         for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
            int iRangeToSource = this.getRangeToValidLensSourceToFacing(world, i, j, k, iTempFacing);
            if (iRangeToSource > 0) {
               int iBeamRangeRemaining = 128 - iRangeToSource;
               if (iBeamRangeRemaining > 0) {
                  this.removeLensBeamFromBlock(world, i, j, k, Block.getOppositeFacing(iTempFacing), iBeamRangeRemaining);
               }
            }
         }
      }
   }

   @Override
   public int idDropped(int i, Random random, int iFortuneModifier) {
      return 0;
   }

   @Override
   public boolean canCollideCheck(int i, boolean flag) {
      return false;
   }

   @Override
   public int quantityDropped(Random random) {
      return 0;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      if (this.isDetectorLogicFlagOn(world, i, j, k)) {
         if (!this.checkForNeighboringDetector(world, i, j, k)) {
            if (this.isIntersectionPointFlagOn(world, i, j, k)) {
               this.setIsDetectorLogicFlag(world, i, j, k, false);
               if (!this.hasMultipleValidLensSources(world, i, j, k)) {
                  this.setIsIntersectionPointFlag(world, i, j, k, false);
               }
            } else {
               this.removeSelf(world, i, j, k);
            }
         } else {
            this.notifyNeighboringDetectorBlocksOfChange(world, i, j, k);
         }
      }

      if ((!this.isDetectorLogicFlagOn(world, i, j, k) || this.isIntersectionPointFlagOn(world, i, j, k))
         && !world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, world.getBlockId(i, j, k), this.tickRate(world));
      }

      if (this.isLitFlagOn(world, i, j, k) && this.isBlockGlowing(world, i, j, k)) {
         world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
      }
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (!world.isRemote && this.isEntityWithinBounds(world, i, j, k)) {
         boolean bIsOn = this.isEntityCollidingFlagOn(world, i, j, k);
         if (!bIsOn) {
            this.changeStateToRegisterEntityCollision(world, i, j, k);
            world.scheduleBlockUpdate(i, j, k, world.getBlockId(i, j, k), this.tickRate(world));
         }
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      boolean bIsOn = this.isEntityCollidingFlagOn(world, i, j, k);
      boolean bShouldBeOn = this.isEntityWithinBounds(world, i, j, k);
      if (bShouldBeOn) {
         if (!bIsOn) {
            this.changeStateToRegisterEntityCollision(world, i, j, k);
         }

         world.scheduleBlockUpdate(i, j, k, world.getBlockId(i, j, k), this.tickRate(world));
      } else if (bIsOn) {
         this.changeStateToClearEntityCollision(world, i, j, k);
      }

      this.fullyValidateBlock(world, i, j, k);
   }

   @Override
   public boolean isAirBlock() {
      return true;
   }

   @Override
   public boolean triggersBuddy() {
      return false;
   }

   protected void removeSelf(World world, int i, int j, int k) {
      world.setBlock(i, j, k, 0, 0, 0);
   }

   public boolean isEntityCollidingFlagOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      int iMetaData = iBlockAccess.getBlockMetadata(i, j, k);
      return (iMetaData & 1) > 0;
   }

   public void setEntityCollidingFlag(World world, int i, int j, int k, boolean bEntityColliding) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -2;
      if (bEntityColliding) {
         iMetaData |= 1;
      }

      world.setBlockMetadataWithNotifyNoClient(i, j, k, iMetaData);
   }

   public boolean isDetectorLogicFlagOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      int iMetaData = iBlockAccess.getBlockMetadata(i, j, k);
      return (iMetaData & 2) > 0;
   }

   public void setIsDetectorLogicFlag(World world, int i, int j, int k, boolean bIsDetectorLogic) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -3;
      if (bIsDetectorLogic) {
         iMetaData |= 2;
      }

      world.setBlockMetadata(i, j, k, iMetaData);
   }

   public boolean isIntersectionPointFlagOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      int iMetaData = iBlockAccess.getBlockMetadata(i, j, k);
      return (iMetaData & 4) > 0;
   }

   public void setIsIntersectionPointFlag(World world, int i, int j, int k, boolean bIsIntersectionPoint) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -5;
      if (bIsIntersectionPoint) {
         iMetaData |= 4;
      }

      world.setBlockMetadata(i, j, k, iMetaData);
   }

   public boolean isLitFlagOn(IBlockAccess iBlockAccess, int i, int j, int k) {
      int iMetaData = iBlockAccess.getBlockMetadata(i, j, k);
      return (iMetaData & 8) > 0;
   }

   public void setIsLitFlag(World world, int i, int j, int k, boolean bIsLitByLens) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -9;
      if (bIsLitByLens) {
         iMetaData |= 8;
      }

      world.setBlockMetadata(i, j, k, iMetaData);
   }

   public boolean isBlockGlowing(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == BTWBlocks.glowingDetectorLogic.blockID;
   }

   public void setBlockAsGlowing(World world, int i, int j, int k) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      BTWMod.isLensBeamBeingRemoved = true;
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.glowingDetectorLogic.blockID, iMetaData);
      BTWMod.isLensBeamBeingRemoved = false;
      if (this.isEntityCollidingFlagOn(world, i, j, k)) {
         world.scheduleBlockUpdate(i, j, k, world.getBlockId(i, j, k), this.tickRate(world));
      }
   }

   public void setBlockAsNotGlowing(World world, int i, int j, int k) {
      int iMetaData = world.getBlockMetadata(i, j, k);
      BTWMod.isLensBeamBeingRemoved = true;
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.detectorLogic.blockID, iMetaData);
      BTWMod.isLensBeamBeingRemoved = false;
      if (this.isEntityCollidingFlagOn(world, i, j, k)) {
         world.scheduleBlockUpdate(i, j, k, world.getBlockId(i, j, k), this.tickRate(world));
      }
   }

   private boolean isEntityWithinBounds(World world, int i, int j, int k) {
      List list = world.getEntitiesWithinAABB(Entity.class, AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1, j + 1, k + 1));
      if (list != null && list.size() > 0) {
         for (int listIndex = 0; listIndex < list.size(); listIndex++) {
            Entity targetEntity = (Entity)list.get(listIndex);
            if ((!(targetEntity instanceof EntityFX) || MinecraftServer.getIsServer()) && !(targetEntity instanceof BlockLiftedByPlatformEntity)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean checkForNeighboringDetector(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         BlockPos tempPos = new BlockPos(i, j, k);
         tempPos.addFacingAsOffset(iTempFacing);
         if (world.getBlockId(tempPos.x, tempPos.y, tempPos.z) == BTWBlocks.detectorBlock.blockID
            && ((DetectorBlock)BTWBlocks.detectorBlock).getFacing(world, tempPos.x, tempPos.y, tempPos.z) == Block.getOppositeFacing(iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public void notifyNeighboringDetectorBlocksOfChange(World world, int i, int j, int k) {
      for (int iFacing = 0; iFacing <= 5; iFacing++) {
         BlockPos targetPos = new BlockPos(i, j, k);
         targetPos.addFacingAsOffset(iFacing);
         int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
         if (iTargetBlockID == BTWBlocks.detectorBlock.blockID) {
            Block.blocksList[iTargetBlockID].onNeighborBlockChange(world, targetPos.x, targetPos.y, targetPos.z, world.getBlockId(i, j, k));
         }
      }
   }

   public boolean hasValidLensSource(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (this.hasValidLensSourceToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasValidLensSourceIgnoreFacing(World world, int i, int j, int k, int iIgnoreFacing) {
      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (iTempFacing != iIgnoreFacing && this.hasValidLensSourceToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasMultipleValidLensSources(World world, int i, int j, int k) {
      int iLensCount = 0;

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (this.hasValidLensSourceToFacing(world, i, j, k, iTempFacing)) {
            if (++iLensCount > 1) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean hasMultipleValidLensSourcesIgnoreFacing(World world, int i, int j, int k, int iIgnoreFacing) {
      int iLensCount = 0;

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (iTempFacing != iIgnoreFacing && this.hasValidLensSourceToFacing(world, i, j, k, iTempFacing)) {
            if (++iLensCount > 1) {
               return true;
            }
         }
      }

      return false;
   }

   public int countValidLensSources(World world, int i, int j, int k) {
      int iLensCount = 0;

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (this.hasValidLensSourceToFacing(world, i, j, k, iTempFacing)) {
            iLensCount++;
         }
      }

      return iLensCount;
   }

   public int countValidLensSourcesIgnoreFacing(World world, int i, int j, int k, int iIgnoreFacing) {
      int iLensCount = 0;

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (iTempFacing != iIgnoreFacing && this.hasValidLensSourceToFacing(world, i, j, k, iTempFacing)) {
            iLensCount++;
         }
      }

      return iLensCount;
   }

   public boolean hasValidLensSourceToFacing(World world, int i, int j, int k, int iFacing) {
      return this.getRangeToValidLensSourceToFacing(world, i, j, k, iFacing) > 0;
   }

   public int getRangeToValidLensSourceToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos tempPos = new BlockPos(i, j, k);

      for (int iDistance = 1; iDistance <= 128; iDistance++) {
         tempPos.addFacingAsOffset(iFacing);
         int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (iTempBlockID == BTWBlocks.lens.blockID) {
            LensBlock lensBlock = (LensBlock)BTWBlocks.lens;
            if (lensBlock.getFacing(world, tempPos.x, tempPos.y, tempPos.z) == Block.getOppositeFacing(iFacing)) {
               return iDistance;
            }

            return 0;
         }

         if (!isLogicBlock(iTempBlockID)) {
            return 0;
         }
      }

      return 0;
   }

   public boolean verifyLitByLens(World world, int i, int j, int k) {
      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (this.hasValidLitLensSourceToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public boolean verifyLitByLensIgnoreFacing(World world, int i, int j, int k, int iIgnoreFacing) {
      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         if (iTempFacing != iIgnoreFacing && this.hasValidLitLensSourceToFacing(world, i, j, k, iTempFacing)) {
            return true;
         }
      }

      return false;
   }

   public boolean hasValidLitLensSourceToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.getRangeToValidLitLensSourceToFacing(blockAccess, i, j, k, iFacing) > 0;
   }

   public int getRangeToValidLitLensSourceToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      BlockPos tempPos = new BlockPos(i, j, k);

      for (int iDistance = 1; iDistance <= 128; iDistance++) {
         tempPos.addFacingAsOffset(iFacing);
         int iTempBlockID = blockAccess.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (iTempBlockID == BTWBlocks.lens.blockID) {
            LensBlock lensBlock = (LensBlock)BTWBlocks.lens;
            if (lensBlock.getFacing(blockAccess, tempPos.x, tempPos.y, tempPos.z) == Block.getOppositeFacing(iFacing)
               && lensBlock.isLit(blockAccess, tempPos.x, tempPos.y, tempPos.z)) {
               return iDistance;
            }

            return 0;
         }

         if (!isLogicBlock(iTempBlockID)) {
            return 0;
         }

         if (!this.isLitFlagOn(blockAccess, tempPos.x, tempPos.y, tempPos.z) || this.isEntityCollidingFlagOn(blockAccess, tempPos.x, tempPos.y, tempPos.z)) {
            return 0;
         }
      }

      return 0;
   }

   public void createLensBeamFromBlock(World world, int i, int j, int k, int iFacing, int iMaxRange) {
      BlockPos tempPos = new BlockPos(i, j, k);

      for (int iDistance = 1; iDistance <= iMaxRange; iDistance++) {
         tempPos.addFacingAsOffset(iFacing);
         int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (iTempBlockID == 0) {
            if (!world.setBlock(tempPos.x, tempPos.y, tempPos.z, BTWBlocks.detectorLogic.blockID, 0, 0)) {
               break;
            }
         } else {
            if (!isLogicBlock(iTempBlockID)) {
               break;
            }

            this.setIsIntersectionPointFlag(world, tempPos.x, tempPos.y, tempPos.z, true);
         }
      }
   }

   public void removeLensBeamFromBlock(World world, int i, int j, int k, int iFacing, int iMaxRange) {
      BTWMod.isLensBeamBeingRemoved = true;
      BlockPos tempPos = new BlockPos(i, j, k);
      int iOppositeFacing = Block.getOppositeFacing(iFacing);

      for (int iDistance = 1; iDistance <= 128; iDistance++) {
         tempPos.addFacingAsOffset(iFacing);
         if (!isLogicBlock(world, tempPos.x, tempPos.y, tempPos.z)) {
            break;
         }

         if (this.isIntersectionPointFlagOn(world, tempPos.x, tempPos.y, tempPos.z)) {
            if (this.isDetectorLogicFlagOn(world, tempPos.x, tempPos.y, tempPos.z)) {
               if (!this.hasValidLensSourceIgnoreFacing(world, tempPos.x, tempPos.y, tempPos.z, iOppositeFacing)) {
                  this.setIsIntersectionPointFlag(world, tempPos.x, tempPos.y, tempPos.z, false);
               }
            } else if (!this.hasMultipleValidLensSourcesIgnoreFacing(world, tempPos.x, tempPos.y, tempPos.z, iOppositeFacing)) {
               this.setIsIntersectionPointFlag(world, tempPos.x, tempPos.y, tempPos.z, false);
            }

            if (this.isLitFlagOn(world, tempPos.x, tempPos.y, tempPos.z)) {
               if (!this.verifyLitByLensIgnoreFacing(world, tempPos.x, tempPos.y, tempPos.z, iOppositeFacing)) {
                  this.unlightBlock(world, tempPos.x, tempPos.y, tempPos.z);
               } else if (this.isBlockGlowing(world, tempPos.x, tempPos.y, tempPos.z) && !this.shouldBeGlowing(world, tempPos.x, tempPos.y, tempPos.z)) {
                  this.setBlockAsNotGlowing(world, tempPos.x, tempPos.y, tempPos.z);
               }
            }
         } else if (!this.isBlockGlowing(world, tempPos.x, tempPos.y, tempPos.z)
            ? !world.setBlock(tempPos.x, tempPos.y, tempPos.z, 0, 0, 0)
            : !world.setBlockWithNotify(tempPos.x, tempPos.y, tempPos.z, 0)) {
            break;
         }
      }

      BTWMod.isLensBeamBeingRemoved = false;
   }

   public void lightBlock(World world, int i, int j, int k) {
      this.setIsLitFlag(world, i, j, k, true);
      if (this.isDetectorLogicFlagOn(world, i, j, k)) {
         this.notifyNeighboringDetectorBlocksOfChange(world, i, j, k);
      }
   }

   public void unlightBlock(World world, int i, int j, int k) {
      this.setIsLitFlag(world, i, j, k, false);
      if (this.isBlockGlowing(world, i, j, k)) {
         this.setBlockAsNotGlowing(world, i, j, k);
      }

      if (this.isDetectorLogicFlagOn(world, i, j, k)) {
         this.notifyNeighboringDetectorBlocksOfChange(world, i, j, k);
      }
   }

   public void changeStateToRegisterEntityCollision(World world, int i, int j, int k) {
      this.setEntityCollidingFlag(world, i, j, k, true);
      if (this.isLitFlagOn(world, i, j, k)) {
         if (!this.isBlockGlowing(world, i, j, k)) {
            this.setBlockAsGlowing(world, i, j, k);
         } else {
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         }

         for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
            int iRangeToSource = this.getRangeToValidLitLensSourceToFacing(world, i, j, k, iTempFacing);
            if (iRangeToSource > 0) {
               int iBeamRangeRemaining = 128 - iRangeToSource;
               if (iBeamRangeRemaining > 0) {
                  this.turnBeamOffFromBlock(world, i, j, k, Block.getOppositeFacing(iTempFacing), iBeamRangeRemaining);
               }
            }
         }
      }
   }

   public void changeStateToClearEntityCollision(World world, int i, int j, int k) {
      this.setEntityCollidingFlag(world, i, j, k, false);

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         int iRangeToSource = this.getRangeToValidLitLensSourceToFacing(world, i, j, k, iTempFacing);
         if (iRangeToSource > 0) {
            int iBeamRangeRemaining = 128 - iRangeToSource;
            if (iBeamRangeRemaining > 0) {
               this.turnBeamOnFromBlock(world, i, j, k, Block.getOppositeFacing(iTempFacing), iBeamRangeRemaining);
            }
         }
      }

      if (this.isLitFlagOn(world, i, j, k) && this.isBlockGlowing(world, i, j, k)) {
         if (!this.shouldBeGlowing(world, i, j, k)) {
            this.setBlockAsNotGlowing(world, i, j, k);
         } else {
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         }
      }
   }

   public void turnBeamOnFromBlock(World world, int i, int j, int k, int iFacing, int iMaxRange) {
      BlockPos tempPos = new BlockPos(i, j, k);

      for (int iDistance = 1; iDistance <= iMaxRange; iDistance++) {
         tempPos.addFacingAsOffset(iFacing);
         int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (!isLogicBlock(iTempBlockID)) {
            if (!world.isAirBlock(tempPos.x, tempPos.y, tempPos.z)) {
               BlockPos previousPos = new BlockPos(tempPos.x, tempPos.y, tempPos.z);
               previousPos.addFacingAsOffset(Block.getOppositeFacing(iFacing));
               if (isLogicBlock(world, previousPos.x, previousPos.y, previousPos.z) && !this.isBlockGlowing(world, previousPos.x, previousPos.y, previousPos.z)
                  )
                {
                  this.setBlockAsGlowing(world, previousPos.x, previousPos.y, previousPos.z);
               }
            }
            break;
         }

         this.lightBlock(world, tempPos.x, tempPos.y, tempPos.z);
         if (this.isEntityCollidingFlagOn(world, tempPos.x, tempPos.y, tempPos.z)) {
            if (isLogicBlock(world, tempPos.x, tempPos.y, tempPos.z) && !this.isBlockGlowing(world, tempPos.x, tempPos.y, tempPos.z)) {
               this.setBlockAsGlowing(world, tempPos.x, tempPos.y, tempPos.z);
            }
            break;
         }
      }
   }

   public void turnBeamOffFromBlock(World world, int i, int j, int k, int iFacing, int iMaxRange) {
      BlockPos tempPos = new BlockPos(i, j, k);
      int iOppositeFacing = Block.getOppositeFacing(iFacing);

      for (int iDistance = 1; iDistance <= iMaxRange; iDistance++) {
         tempPos.addFacingAsOffset(iFacing);
         int iTempBlockID = world.getBlockId(tempPos.x, tempPos.y, tempPos.z);
         if (!isLogicBlock(iTempBlockID)) {
            break;
         }

         if (this.isIntersectionPointFlagOn(world, tempPos.x, tempPos.y, tempPos.z)) {
            if (!this.verifyLitByLensIgnoreFacing(world, tempPos.x, tempPos.y, tempPos.z, iOppositeFacing)) {
               this.unlightBlock(world, tempPos.x, tempPos.y, tempPos.z);
            } else if (this.isBlockGlowing(world, tempPos.x, tempPos.y, tempPos.z) && !this.shouldBeGlowing(world, tempPos.x, tempPos.y, tempPos.z)) {
               this.setBlockAsNotGlowing(world, tempPos.x, tempPos.y, tempPos.z);
            }
         } else {
            this.unlightBlock(world, tempPos.x, tempPos.y, tempPos.z);
         }

         if (this.isEntityCollidingFlagOn(world, tempPos.x, tempPos.y, tempPos.z)) {
            break;
         }
      }
   }

   protected boolean shouldBeProjectingToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      if (this.isEntityCollidingFlagOn(blockAccess, i, j, k)) {
         return false;
      } else {
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         return blockAccess.isBlockNormalCube(targetPos.x, targetPos.y, targetPos.z)
            && this.hasValidLitLensSourceToFacing(blockAccess, i, j, k, Block.getOppositeFacing(iFacing));
      }
   }

   protected boolean shouldBeGlowingToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      return !world.isAirBlock(targetPos.x, targetPos.y, targetPos.z) && this.hasValidLitLensSourceToFacing(world, i, j, k, Block.getOppositeFacing(iFacing));
   }

   private boolean shouldBeGlowing(World world, int i, int j, int k) {
      if (this.isEntityCollidingFlagOn(world, i, j, k)) {
         return true;
      } else {
         for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
            if (this.shouldBeGlowingToFacing(world, i, j, k, iTempFacing)) {
               return true;
            }
         }

         return false;
      }
   }

   static boolean isLogicBlock(IBlockAccess blockAccess, int i, int j, int k) {
      int iBlockID = blockAccess.getBlockId(i, j, k);
      return isLogicBlock(iBlockID);
   }

   static boolean isLogicBlock(int iBlockID) {
      return iBlockID == BTWBlocks.detectorLogic.blockID || iBlockID == BTWBlocks.glowingDetectorLogic.blockID;
   }

   void propagateBeamsThroughBlock(World world, int i, int j, int k) {
      boolean bIsLit = false;
      int iSourceCount = 0;
      if (world.setBlock(i, j, k, BTWBlocks.detectorLogic.blockID, 0, 0)) {
         for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
            int iRangeToSource = this.getRangeToValidLensSourceToFacing(world, i, j, k, iTempFacing);
            if (iRangeToSource > 0) {
               iSourceCount++;
               int iRangeRemaining = 128 - iRangeToSource;
               if (iRangeRemaining > 0) {
                  int iOppositeFacing = Block.getOppositeFacing(iTempFacing);
                  this.createLensBeamFromBlock(world, i, j, k, iOppositeFacing, iRangeRemaining);
                  if (this.hasValidLitLensSourceToFacing(world, i, j, k, iTempFacing)) {
                     this.turnBeamOnFromBlock(world, i, j, k, iOppositeFacing, iRangeRemaining);
                     bIsLit = true;
                  }
               } else if (this.hasValidLitLensSourceToFacing(world, i, j, k, iTempFacing)) {
                  bIsLit = true;
               }
            }
         }

         if (bIsLit) {
            this.lightBlock(world, i, j, k);
            if (this.shouldBeGlowing(world, i, j, k)) {
               this.setBlockAsGlowing(world, i, j, k);
            }
         }

         if (iSourceCount > 1) {
            this.setIsIntersectionPointFlag(world, i, j, k, true);
         }
      }
   }

   public void fullyValidateBlock(World world, int i, int j, int k) {
      boolean bHasDetector = this.checkForNeighboringDetector(world, i, j, k);
      if (bHasDetector != this.isDetectorLogicFlagOn(world, i, j, k)) {
         this.setIsDetectorLogicFlag(world, i, j, k, bHasDetector);
      }

      boolean bShouldBeLit = false;
      int iNumLensSources = 0;

      for (int iTempFacing = 0; iTempFacing <= 5; iTempFacing++) {
         int iRangeToSource = this.getRangeToValidLensSourceToFacing(world, i, j, k, iTempFacing);
         if (iRangeToSource > 0) {
            iNumLensSources++;
            if (!bShouldBeLit && this.hasValidLitLensSourceToFacing(world, i, j, k, iTempFacing)) {
               bShouldBeLit = true;
            }

            int iRangeRemaining = 128 - iRangeToSource;
            if (iRangeRemaining > 0) {
               BlockPos targetPos = new BlockPos(i, j, k);
               targetPos.addFacingAsOffset(Block.getOppositeFacing(iTempFacing));
               if (world.getBlockId(targetPos.x, targetPos.y, targetPos.z) == 0) {
                  this.propagateBeamsThroughBlock(world, targetPos.x, targetPos.y, targetPos.z);
               }
            }
         }
      }

      if (iNumLensSources == 0 && !this.isDetectorLogicFlagOn(world, i, j, k)) {
         BTWMod.isLensBeamBeingRemoved = true;
         this.removeSelf(world, i, j, k);
         BTWMod.isLensBeamBeingRemoved = false;
      } else {
         boolean bShouldBeIntersectionPoint = false;
         if (iNumLensSources > 1 || iNumLensSources == 1 && this.isDetectorLogicFlagOn(world, i, j, k)) {
            bShouldBeIntersectionPoint = true;
         }

         if (bShouldBeIntersectionPoint != this.isIntersectionPointFlagOn(world, i, j, k)) {
            this.setIsIntersectionPointFlag(world, i, j, k, bShouldBeIntersectionPoint);
         }

         if (bShouldBeLit != this.isLitFlagOn(world, i, j, k)) {
            if (bShouldBeLit) {
               this.lightBlock(world, i, j, k);
            } else {
               this.unlightBlock(world, i, j, k);
            }
         }
      }

      if (this.isLitFlagOn(world, i, j, k)) {
         boolean bShouldGlow = this.shouldBeGlowing(world, i, j, k);
         if (bShouldGlow != this.isBlockGlowing(world, i, j, k)) {
            if (bShouldGlow) {
               this.setBlockAsGlowing(world, i, j, k);
            } else {
               this.setBlockAsNotGlowing(world, i, j, k);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      this.blockIcon = register.registerIcon("fcBlockLens_spotlight");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   public boolean renderDetectorLogicDebug(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   public void renderDetectorLogicDebugInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage, int iRenderType) {
   }
}
