package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import btw.item.util.ItemUtils;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class StakeStringBlock extends Block {
   public static final double HEIGHT = 0.015625;
   public static final double HALF_HEIGHT = 0.0078125;
   public static final double SELECTION_BOX_HEIGHT = 0.0625;
   public static final double SELECTION_BOX_HALF_HEIGHT = 0.03125;
   private static final long MIN_TIME_BETWEEN_LENGTH_DISPLAYS = 200L;
   static long timeOfLastLengthDisplay = 0L;
   static int lengthOfLastLengthDisplay = 0;

   public StakeStringBlock(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.setAxesEffectiveOn(true);
      this.setFireProperties(Flammability.EXTREME);
      this.a(m);
      this.c("fcBlockStakeString");
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      MovingObjectPosition rayIntersectPos = null;
      MovingObjectPosition[] possibleIntersectPoints = new MovingObjectPosition[8];
      int iCurrentIntersectIndex = 0;

      for (int iAxis = 0; iAxis < 3; iAxis++) {
         if (this.getExtendsAlongAxis(world, i, j, k, iAxis)) {
            Vec3 boxMin = Vec3.createVectorHelper(0.0, 0.0, 0.0);
            Vec3 boxMax = Vec3.createVectorHelper(0.0, 0.0, 0.0);
            this.getBlockBoundsForAxis(iAxis, boxMin, boxMax, 0.03125);
            possibleIntersectPoints[iCurrentIntersectIndex] = MiscUtils.rayTraceWithBox(world, i, j, k, boxMin, boxMax, startRay, endRay);
            if (possibleIntersectPoints[iCurrentIntersectIndex] != null) {
               iCurrentIntersectIndex++;
            }
         }
      }

      if (iCurrentIntersectIndex > 0) {
         iCurrentIntersectIndex--;

         for (double dMaxDistance = 0.0; iCurrentIntersectIndex >= 0; iCurrentIntersectIndex--) {
            double dCurrentIntersectDistance = possibleIntersectPoints[iCurrentIntersectIndex].hitVec.squareDistanceTo(endRay);
            if (dCurrentIntersectDistance > dMaxDistance) {
               rayIntersectPos = possibleIntersectPoints[iCurrentIntersectIndex];
               dMaxDistance = dCurrentIntersectDistance;
            }
         }
      }

      return rayIntersectPos;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return Item.silk.itemID;
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      if (!world.isRemote) {
         for (int iAxis = 0; iAxis < 3; iAxis++) {
            if (this.getExtendsAlongAxisFromMetadata(iMetadata, iAxis)) {
               ItemUtils.dropSingleItemAsIfBlockHarvested(world, i, j, k, this.idDropped(iMetadata, world.rand, iFortuneModifier), this.a(iMetadata));
            }
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      this.validateState(world, i, j, k);
   }

   @Override
   public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int iFacing, float fXClick, float fYClick, float fZClick) {
      int iLength = this.computeStringLength(world, i, j, k);
      double dInvertedLength = 64.0 - iLength;
      if (!world.isRemote) {
         float fPitch = (float)Math.pow(2.0, (dInvertedLength - 32.0) / 32.0);
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "note.bass", 3.0F, fPitch - 0.0F);
      } else {
         world.spawnParticle("note", i + 0.5, j + 1.2, k + 0.5, dInvertedLength / 64.0, 0.0, 0.0);
         long lCurrentTime = world.getWorldTime();
         long lDeltaTime = lCurrentTime - timeOfLastLengthDisplay;
         if (lDeltaTime < 0L || lDeltaTime >= 200L || lengthOfLastLengthDisplay != iLength) {
            player.addChatMessage("Sounds like " + (iLength + 1) + ".");
            timeOfLastLengthDisplay = lCurrentTime;
            lengthOfLastLengthDisplay = iLength;
         }
      }

      return true;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   public void setExtendsAlongAxis(World world, int i, int j, int k, int iAxis, boolean bExtends) {
      this.setExtendsAlongAxis(world, i, j, k, iAxis, bExtends, true);
   }

   public void setExtendsAlongAxis(World world, int i, int j, int k, int iAxis, boolean bExtends, boolean bNotify) {
      int iMetadata = world.getBlockMetadata(i, j, k) & ~(1 << iAxis);
      if (bExtends) {
         iMetadata |= 1 << iAxis;
      }

      if (bNotify) {
         world.setBlockMetadataWithNotify(i, j, k, iMetadata);
      } else {
         world.setBlockMetadataWithClient(i, j, k, iMetadata);
      }
   }

   public void setExtendsAlongFacing(World world, int i, int j, int k, int iFacing, boolean bExtends) {
      this.setExtendsAlongAxis(world, i, j, k, convertFacingToAxis(iFacing), bExtends);
   }

   public void setExtendsAlongFacing(World world, int i, int j, int k, int iFacing, boolean bExtends, boolean bNotify) {
      this.setExtendsAlongAxis(world, i, j, k, convertFacingToAxis(iFacing), bExtends, bNotify);
   }

   public boolean getExtendsAlongAxis(IBlockAccess blockAccess, int i, int j, int k, int iAxis) {
      return this.getExtendsAlongAxisFromMetadata(blockAccess.getBlockMetadata(i, j, k), iAxis);
   }

   public boolean getExtendsAlongAxisFromMetadata(int iMetadata, int iAxis) {
      return (iMetadata & 1 << iAxis) > 0;
   }

   public boolean getExtendsAlongFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.getExtendsAlongAxis(blockAccess, i, j, k, convertFacingToAxis(iFacing));
   }

   public boolean getExtendsAlongOtherFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return this.getExtendsAlongOtherAxis(blockAccess, i, j, k, convertFacingToAxis(iFacing));
   }

   public boolean getExtendsAlongOtherAxis(IBlockAccess blockAccess, int i, int j, int k, int iAxis) {
      return this.getExtendsAlongOtherAxisFromMetadata(blockAccess.getBlockMetadata(i, j, k), iAxis);
   }

   public boolean getExtendsAlongOtherAxisFromMetadata(int iMetadata, int iAxis) {
      iMetadata &= ~(1 << iAxis);
      return (iMetadata & 7) != 0;
   }

   public static int convertFacingToAxis(int iFacing) {
      if (iFacing == 4 || iFacing == 5) {
         return 0;
      } else {
         return iFacing != 0 && iFacing != 1 ? 2 : 1;
      }
   }

   private void getBlockBoundsForAxis(int iAxis, Vec3 min, Vec3 max, double dHalfHeight) {
      if (iAxis == 0) {
         min.setComponents(0.0, 0.5 - dHalfHeight, 0.5 - dHalfHeight);
         max.setComponents(1.0, 0.5 + dHalfHeight, 0.5 + dHalfHeight);
      } else if (iAxis == 1) {
         min.setComponents(0.5 - dHalfHeight, 0.0, 0.5 - dHalfHeight);
         max.setComponents(0.5 + dHalfHeight, 1.0, 0.5 + dHalfHeight);
      } else {
         min.setComponents(0.5 - dHalfHeight, 0.5 - dHalfHeight, 0.0);
         max.setComponents(0.5 + dHalfHeight, 0.5 + dHalfHeight, 1.0);
      }
   }

   public void validateState(World world, int i, int j, int k) {
      int iValidAxisCount = 0;

      for (int iTempAxis = 0; iTempAxis < 3; iTempAxis++) {
         if (this.getExtendsAlongAxis(world, i, j, k, iTempAxis)) {
            if (this.hasValidAttachmentPointsAlongAxis(world, i, j, k, iTempAxis)) {
               iValidAxisCount++;
            } else {
               this.setExtendsAlongAxis(world, i, j, k, iTempAxis, false);
               ItemUtils.dropSingleItemAsIfBlockHarvested(world, i, j, k, Item.silk.itemID, 0);
            }
         }
      }

      if (iValidAxisCount <= 0) {
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   private boolean hasValidAttachmentPointsAlongAxis(World world, int i, int j, int k, int iAxis) {
      int iFacing1;
      int iFacing2;
      switch (iAxis) {
         case 0:
            iFacing1 = 4;
            iFacing2 = 5;
            break;
         case 1:
            iFacing1 = 0;
            iFacing2 = 1;
            break;
         default:
            iFacing1 = 2;
            iFacing2 = 3;
      }

      return this.hasValidAttachmentPointToFacing(world, i, j, k, iFacing1) && this.hasValidAttachmentPointToFacing(world, i, j, k, iFacing2);
   }

   private boolean hasValidAttachmentPointToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos targetPos = new BlockPos(i, j, k);
      targetPos.addFacingAsOffset(iFacing);
      int iTargetBlockID = world.getBlockId(targetPos.x, targetPos.y, targetPos.z);
      if (iTargetBlockID == this.blockID) {
         if (this.getExtendsAlongFacing(world, targetPos.x, targetPos.y, targetPos.z, iFacing)) {
            return true;
         }
      } else if (iTargetBlockID == BTWBlocks.stake.blockID) {
         return true;
      }

      return false;
   }

   protected int computeStringLength(World world, int i, int j, int k) {
      int iLength = 0;

      for (int iAxis = 0; iAxis < 3; iAxis++) {
         int iAxisLength = this.computeStringLengthAlongAxis(world, i, j, k, iAxis);
         if (iAxisLength > iLength) {
            iLength = iAxisLength;
         }
      }

      return iLength;
   }

   protected int computeStringLengthAlongAxis(World world, int i, int j, int k, int iAxis) {
      int iLength = 0;
      if (this.getExtendsAlongAxis(world, i, j, k, iAxis)) {
         int iTempFacing = this.getFirstFacingForAxis(iAxis);
         iLength = this.computeStringLengthToFacing(world, i, j, k, iTempFacing);
         iTempFacing = Block.getOppositeFacing(iTempFacing);
         iLength += this.computeStringLengthToFacing(world, i, j, k, iTempFacing);
         iLength++;
      }

      return iLength;
   }

   protected int computeStringLengthToFacing(World world, int i, int j, int k, int iFacing) {
      int iLength = 0;
      BlockPos tempPos = new BlockPos(i, j, k, iFacing);

      while (world.blockExists(tempPos.x, tempPos.y, tempPos.z) && world.getBlockId(tempPos.x, tempPos.y, tempPos.z) == this.blockID) {
         iLength++;
         tempPos.addFacingAsOffset(iFacing);
      }

      return iLength;
   }

   protected int getFirstFacingForAxis(int iAxis) {
      if (iAxis == 0) {
         return 4;
      } else {
         return iAxis == 1 ? 0 : 2;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, getOppositeFacing(iSide));
      int iMetadata = blockAccess.getBlockMetadata(myPos.x, myPos.y, myPos.z);
      int iAxis = convertFacingToAxis(iSide);
      return this.getExtendsAlongAxisFromMetadata(iMetadata, iAxis) ? this.getExtendsAlongOtherAxisFromMetadata(iMetadata, iAxis) : true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      double minXBox = i + 0.5 - 0.03125;
      double minYBox = j + 0.5 - 0.03125;
      double minZBox = k + 0.5 - 0.03125;
      double maxXBox = i + 0.5 + 0.03125;
      double maxYBox = j + 0.5 + 0.03125;
      double maxZBox = k + 0.5 + 0.03125;
      if (this.getExtendsAlongAxis(world, i, j, k, 0)) {
         minXBox = i;
         maxXBox = i + 1.0;
      }

      if (this.getExtendsAlongAxis(world, i, j, k, 1)) {
         minYBox = j;
         maxYBox = j + 1.0;
      }

      if (this.getExtendsAlongAxis(world, i, j, k, 2)) {
         minZBox = k;
         maxZBox = k + 1.0;
      }

      return AxisAlignedBB.getAABBPool().getAABB(minXBox, minYBox, minZBox, maxXBox, maxYBox, maxZBox);
   }

   @Environment(EnvType.CLIENT)
   private void setRenderBoundsForAxis(RenderBlocks renderBlocks, int iAxis) {
      Vec3 min = Vec3.createVectorHelper(0.0, 0.0, 0.0);
      Vec3 max = Vec3.createVectorHelper(0.0, 0.0, 0.0);
      this.getBlockBoundsForAxis(iAxis, min, max, 0.0078125);
      renderBlocks.setRenderBounds((float)min.xCoord, (float)min.yCoord, (float)min.zCoord, (float)max.xCoord, (float)max.yCoord, (float)max.zCoord);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.idDropped(world.getBlockMetadata(i, j, k), world.rand, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;

      for (int iAxis = 0; iAxis < 3; iAxis++) {
         if (this.getExtendsAlongAxis(blockAccess, i, j, k, iAxis)) {
            this.setRenderBoundsForAxis(renderBlocks, iAxis);
            renderBlocks.renderStandardBlock(this, i, j, k);
         }
      }

      return true;
   }
}
