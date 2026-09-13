package btw.block.blocks;

import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class TorchBlockBase extends Block {
   protected TorchBlockBase(int iBlockID) {
      super(iBlockID, Material.circuits);
      this.c(0.0F);
      this.setBuoyant();
      this.setFilterableProperties(4);
      this.a(g);
      this.b(true);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return null;
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
   public int getRenderType() {
      return 2;
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      for (int iFacing = 2; iFacing < 6; iFacing++) {
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, Block.getOppositeFacing(iFacing))) {
            return true;
         }
      }

      return this.canPlaceTorchOn(world, i, j - 1, k);
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing == 1) {
         if (this.canPlaceTorchOn(world, i, j - 1, k)) {
            iMetadata = setOrientation(iMetadata, 5);
         }
      } else if (iFacing != 0) {
         BlockPos targetPos = new BlockPos(i, j, k, Block.getOppositeFacing(iFacing));
         if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, iFacing)) {
            iMetadata = setOrientation(iMetadata, 6 - iFacing);
         }
      }

      return iMetadata;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      super.updateTick(world, i, j, k, rand);
      if (this.getOrientation(world, i, j, k) == 0) {
         this.onBlockAdded(world, i, j, k);
      }
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (this.getOrientation(world, i, j, k) == 0) {
         for (int iFacing = 1; iFacing < 6; iFacing++) {
            BlockPos targetPos = new BlockPos(i, j, k, Block.getOppositeFacing(iFacing));
            if (WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, iFacing)) {
               this.setOrientation(world, i, j, k, 6 - iFacing);
               return;
            }
         }

         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborID) {
      this.validateState(world, i, j, k, iNeighborID);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iOrientation = this.getOrientation(blockAccess, i, j, k);
      float fTorchWidth = 0.15F;
      if (iOrientation == 1) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.2F, 0.5F - fTorchWidth, fTorchWidth * 2.0F, 0.8F, 0.5F + fTorchWidth);
      } else if (iOrientation == 2) {
         return AxisAlignedBB.getAABBPool().getAABB(1.0F - fTorchWidth * 2.0F, 0.2F, 0.5F - fTorchWidth, 1.0, 0.8F, 0.5F + fTorchWidth);
      } else if (iOrientation == 3) {
         return AxisAlignedBB.getAABBPool().getAABB(0.5F - fTorchWidth, 0.2F, 0.0, 0.5F + fTorchWidth, 0.8F, fTorchWidth * 2.0F);
      } else if (iOrientation == 4) {
         return AxisAlignedBB.getAABBPool().getAABB(0.5F - fTorchWidth, 0.2F, 1.0F - fTorchWidth * 2.0F, 0.5F + fTorchWidth, 0.8F, 1.0);
      } else {
         fTorchWidth = 0.1F;
         return AxisAlignedBB.getAABBPool().getAABB(0.5F - fTorchWidth, 0.0, 0.5F - fTorchWidth, 0.5F + fTorchWidth, 0.6F, 0.5F + fTorchWidth);
      }
   }

   @Override
   public boolean isBlockRestingOnThatBelow(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getOrientation(blockAccess, i, j, k) == 5;
   }

   @Override
   public int getFacing(int iMetadata) {
      return MathHelper.clamp_int(6 - getOrientation(iMetadata), 1, 5);
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      iFacing = MathHelper.clamp_int(iFacing, 1, 5);
      return setOrientation(iMetadata, 6 - iFacing);
   }

   @Override
   public boolean canRotateAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing == Block.getOppositeFacing(this.getFacing(world, i, j, k));
   }

   @Override
   public int getNewMetadataRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iInitialFacing, int iRotatedFacing) {
      int iOldMetadata = world.getBlockMetadata(i, j, k);
      return this.setFacing(iOldMetadata, Block.getOppositeFacing(iRotatedFacing));
   }

   @Override
   public void onNeighborDisrupted(World world, int i, int j, int k, int iToFacing) {
      if (iToFacing == Block.getOppositeFacing(this.getFacing(world, i, j, k))) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockWithNotify(i, j, k, 0);
      }
   }

   protected boolean canPlaceTorchOn(World world, int i, int j, int k) {
      return WorldUtils.doesBlockHaveSmallCenterHardpointToFacing(world, i, j, k, 1, true);
   }

   protected boolean validateState(World world, int i, int j, int k, int iNeighborID) {
      int iOrientation = this.getOrientation(world, i, j, k);
      int iFacing = 0;
      if (iOrientation != 0) {
         iFacing = 6 - iOrientation;
         boolean bShouldDrop = false;
         if (iFacing == 1) {
            if (!this.canPlaceTorchOn(world, i, j - 1, k)) {
               bShouldDrop = true;
            }
         } else {
            BlockPos targetPos = new BlockPos(i, j, k, Block.getOppositeFacing(iFacing));
            if (!WorldUtils.doesBlockHaveCenterHardpointToFacing(world, targetPos.x, targetPos.y, targetPos.z, iFacing)) {
               bShouldDrop = true;
            }
         }

         if (bShouldDrop) {
            this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
            world.setBlockWithNotify(i, j, k, 0);
            return true;
         }
      }

      return false;
   }

   public int getOrientation(IBlockAccess blockAccess, int i, int j, int k) {
      return getOrientation(blockAccess.getBlockMetadata(i, j, k));
   }

   public static int getOrientation(int iMetadata) {
      return iMetadata & 7;
   }

   public void setOrientation(World world, int i, int j, int k, int iOrientation) {
      int iMetadata = setOrientation(world.getBlockMetadata(i, j, k), iOrientation);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public static int setOrientation(int iMetadata, int iOrientation) {
      iMetadata &= -8;
      return iMetadata | iOrientation;
   }

   public boolean isRainingOnTorch(World world, int i, int j, int k) {
      return world.isRaining() && world.isRainingAtPos(i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      return renderBlocks.renderBlockTorch(this, i, j, k);
   }
}
