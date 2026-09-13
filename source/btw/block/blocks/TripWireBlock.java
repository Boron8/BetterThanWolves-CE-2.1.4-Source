package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockTripWire;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class TripWireBlock extends BlockTripWire {
   public TripWireBlock(int iBlockID) {
      super(iBlockID);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.15625, 1.0);
   }

   @Override
   public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity) {
      if (entity.canEntityTriggerTripwire()) {
         super.onEntityCollidedWithBlock(world, i, j, k, entity);
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int var5 = blockAccess.getBlockMetadata(i, j, k);
      boolean var6 = (var5 & 4) == 4;
      boolean var7 = (var5 & 2) == 2;
      if (!var7) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.09375, 1.0);
      } else {
         return !var6
            ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0)
            : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0625, 0.0, 1.0, 0.15625, 1.0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockTripWire(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
