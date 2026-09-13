package btw.block.blocks;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockPistonExtension;
import net.minecraft.src.Entity;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class PistonExtensionBlock extends BlockPistonExtension {
   public PistonExtensionBlock(int iBlockID) {
      super(iBlockID);
      this.setPicksEffectiveOn(true);
   }

   @Override
   public AxisAlignedBB getAsPistonMovingBoundingBox(World world, int i, int j, int k) {
      return this.getFixedBlockBoundsFromPool().offset(i, j, k);
   }

   @Override
   public boolean canContainPistonPackingToFacing(World world, int i, int j, int k, int iFacing) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return BlockPistonExtension.getDirectionMeta(iMetadata) == iFacing;
   }

   @Override
   public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB intersectingBox, List list, Entity entity) {
      int iFacing = d(world.getBlockMetadata(i, j, k));
      AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
      tempBox.tiltToFacingAlongY(iFacing);
      tempBox.offset(i, j, k);
      tempBox.addToListIfIntersects(intersectingBox, list);
      tempBox = AxisAlignedBB.getAABBPool().getAABB(0.375, 0.0, 0.375, 0.625, 0.75, 0.625);
      tempBox.tiltToFacingAlongY(iFacing);
      tempBox.offset(i, j, k);
      tempBox.addToListIfIntersects(intersectingBox, list);
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = d(blockAccess.getBlockMetadata(i, j, k));
      AxisAlignedBB tempBox = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
      tempBox.tiltToFacingAlongY(iFacing);
      return tempBox;
   }

   @Override
   public boolean canSupportFallingBlocks(IBlockAccess blockAccess, int i, int j, int k) {
      return d(blockAccess.getBlockMetadata(i, j, k)) == 1;
   }

   @Override
   public ItemStack getStackRetrievedByBlockDispenser(World world, int i, int j, int k) {
      return null;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderPistonExtension(this, i, j, k, true);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
