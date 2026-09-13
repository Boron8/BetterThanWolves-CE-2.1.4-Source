package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockLever;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LeverBlock extends BlockLever {
   public LeverBlock(int iBlockID) {
      super(iBlockID);
      this.setPicksEffectiveOn(true);
   }

   @Override
   public int getFacing(int iMetadata) {
      return MathHelper.clamp_int(6 - (iMetadata & 7), 1, 5);
   }

   @Override
   public boolean canRotateAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing == Block.getOppositeFacing(this.getFacing(world, i, j, k));
   }

   @Override
   public boolean onRotatedAroundBlockOnTurntableToFacing(World world, int i, int j, int k, int iFacing) {
      this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
      world.setBlockToAir(i, j, k);
      return false;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j - 1, k);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return -1.0F;
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iDirection = blockAccess.getBlockMetadata(i, j, k) & 7;
      float fHalfWidth = 0.1875F;
      if (iDirection == 1) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.2F, 0.5F - fHalfWidth, fHalfWidth * 2.0F, 0.8F, 0.5F + fHalfWidth);
      } else if (iDirection == 2) {
         return AxisAlignedBB.getAABBPool().getAABB(1.0F - fHalfWidth * 2.0F, 0.2F, 0.5F - fHalfWidth, 1.0, 0.8F, 0.5F + fHalfWidth);
      } else if (iDirection == 3) {
         return AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.2F, 0.0, 0.5F + fHalfWidth, 0.8F, fHalfWidth * 2.0F);
      } else if (iDirection == 4) {
         return AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.2F, 1.0F - fHalfWidth * 2.0F, 0.5F + fHalfWidth, 0.8F, 1.0);
      } else {
         fHalfWidth = 0.25F;
         return iDirection != 0 && iDirection != 7
            ? AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.0, 0.5F - fHalfWidth, 0.5F + fHalfWidth, 0.6F, 0.5F + fHalfWidth)
            : AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.4F, 0.5F - fHalfWidth, 0.5F + fHalfWidth, 1.0, 0.5F + fHalfWidth);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockLever(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
