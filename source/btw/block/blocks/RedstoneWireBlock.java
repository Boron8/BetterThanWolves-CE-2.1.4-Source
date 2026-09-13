package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class RedstoneWireBlock extends BlockRedstoneWire {
   public RedstoneWireBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.0625, 1.0);
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
   public boolean triggersBuddy() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockRedstoneWire(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
