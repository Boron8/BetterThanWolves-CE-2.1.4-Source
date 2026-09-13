package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockTripWireSource;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;

public class TripWireHookBlock extends BlockTripWireSource {
   public TripWireHookBlock(int iBlockID) {
      super(iBlockID);
      this.c("tripWireSource");
      this.a(null);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iDirection = blockAccess.getBlockMetadata(i, j, k) & 3;
      float fHalfWidth = 0.1875F;
      if (iDirection == 3) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.2F, 0.5F - fHalfWidth, fHalfWidth * 2.0F, 0.8F, 0.5F + fHalfWidth);
      } else if (iDirection == 1) {
         return AxisAlignedBB.getAABBPool().getAABB(1.0F - fHalfWidth * 2.0F, 0.2F, 0.5F - fHalfWidth, 1.0, 0.8F, 0.5F + fHalfWidth);
      } else {
         return iDirection == 0
            ? AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.2F, 0.0, 0.5F + fHalfWidth, 0.8F, fHalfWidth * 2.0F)
            : AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, 0.2F, 1.0F - fHalfWidth * 2.0F, 0.5F + fHalfWidth, 0.8F, 1.0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockTripWireSource(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
