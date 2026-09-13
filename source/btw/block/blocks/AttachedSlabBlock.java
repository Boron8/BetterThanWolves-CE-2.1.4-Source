package btw.block.blocks;

import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class AttachedSlabBlock extends SlabBlock {
   protected AttachedSlabBlock(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Override
   public boolean canPlaceBlockOnSide(World world, int i, int j, int k, int iSide) {
      if (iSide != 0 && iSide != 1) {
         if (this.hasValidAnchorToFacing(world, i, j, k, 0) || this.hasValidAnchorToFacing(world, i, j, k, 1)) {
            return super.c(world, i, j, k, iSide);
         }
      } else if (this.hasValidAnchorToFacing(world, i, j, k, Block.getOppositeFacing(iSide))) {
         return super.c(world, i, j, k, iSide);
      }

      return false;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if (iFacing == 0) {
         iMetadata = this.setIsUpsideDown(iMetadata, true);
      } else if (iFacing != 1) {
         if (fClickY > 0.5) {
            if (this.hasValidAnchorToFacing(world, i, j, k, 1)) {
               iMetadata = this.setIsUpsideDown(iMetadata, true);
            }
         } else if (!this.hasValidAnchorToFacing(world, i, j, k, 0)) {
            iMetadata = this.setIsUpsideDown(iMetadata, true);
         }
      }

      return iMetadata;
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      int iAnchorSide = 0;
      if (this.getIsUpsideDown(world.getBlockMetadata(i, j, k))) {
         iAnchorSide = 1;
      }

      if (!this.hasValidAnchorToFacing(world, i, j, k, iAnchorSide)) {
         this.onAnchorBlockLost(world, i, j, k);
      }
   }

   protected boolean hasValidAnchorToFacing(World world, int i, int j, int k, int iFacing) {
      BlockPos attachedPos = new BlockPos(i, j, k, iFacing);
      return WorldUtils.doesBlockHaveLargeCenterHardpointToFacing(world, attachedPos.x, attachedPos.y, attachedPos.z, Block.getOppositeFacing(iFacing), true);
   }

   protected abstract void onAnchorBlockLost(World var1, int var2, int var3, int var4);
}
