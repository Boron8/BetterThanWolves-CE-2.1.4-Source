package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockPistonMoving;
import net.minecraft.src.Facing;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityPiston;
import net.minecraft.src.World;

public class PistonBlockMoving extends BlockPistonMoving {
   public PistonBlockMoving(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public AxisAlignedBB getAxisAlignedBB(World world, int i, int j, int k, int iBlockID, float fPistonPushProgress, int iOrientation) {
      if (iBlockID != 0 && iBlockID != this.blockID) {
         AxisAlignedBB boundingBox = Block.blocksList[iBlockID].getAsPistonMovingBoundingBox(world, i, j, k);
         if (boundingBox == null) {
            return null;
         } else {
            if (Facing.offsetsXForSide[iOrientation] < 0) {
               boundingBox.minX = boundingBox.minX - Facing.offsetsXForSide[iOrientation] * fPistonPushProgress;
            } else {
               boundingBox.maxX = boundingBox.maxX - Facing.offsetsXForSide[iOrientation] * fPistonPushProgress;
            }

            if (Facing.offsetsYForSide[iOrientation] < 0) {
               boundingBox.minY = boundingBox.minY - Facing.offsetsYForSide[iOrientation] * fPistonPushProgress;
            } else {
               boundingBox.maxY = boundingBox.maxY - Facing.offsetsYForSide[iOrientation] * fPistonPushProgress;
            }

            if (Facing.offsetsZForSide[iOrientation] < 0) {
               boundingBox.minZ = boundingBox.minZ - Facing.offsetsZForSide[iOrientation] * fPistonPushProgress;
            } else {
               boundingBox.maxZ = boundingBox.maxZ - Facing.offsetsZForSide[iOrientation] * fPistonPushProgress;
            }

            return boundingBox;
         }
      } else {
         return null;
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      TileEntity tileEntity = blockAccess.getBlockTileEntity(i, j, k);
      if (tileEntity != null && tileEntity instanceof TileEntityPiston) {
         TileEntityPiston pistonEntity = (TileEntityPiston)tileEntity;
         Block block = Block.blocksList[pistonEntity.getStoredBlockID()];
         if (block != null && block != this) {
            AxisAlignedBB bounds = block.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
            float fExtensionRatio = pistonEntity.getProgress(0.0F);
            if (pistonEntity.isExtending()) {
               fExtensionRatio = 1.0F - fExtensionRatio;
            }

            int iFacing = pistonEntity.getPistonOrientation();
            bounds.minX = bounds.minX - Facing.offsetsXForSide[iFacing] * fExtensionRatio;
            bounds.minY = bounds.minY - Facing.offsetsYForSide[iFacing] * fExtensionRatio;
            bounds.minZ = bounds.minZ - Facing.offsetsZForSide[iFacing] * fExtensionRatio;
            bounds.maxX = bounds.maxX - Facing.offsetsXForSide[iFacing] * fExtensionRatio;
            bounds.maxY = bounds.maxY - Facing.offsetsYForSide[iFacing] * fExtensionRatio;
            bounds.maxZ = bounds.maxZ - Facing.offsetsZForSide[iFacing] * fExtensionRatio;
            return bounds;
         }
      }

      return super.getBlockBoundsFromPoolBasedOnState(blockAccess, i, j, k);
   }

   @Override
   public boolean canSupportFallingBlocks(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   public static TileEntity getShoveledTileEntity(int iBlockID, int iMetadata, int iFacing) {
      return new TileEntityPiston(iBlockID, iMetadata, iFacing, true, false, true);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return false;
   }
}
