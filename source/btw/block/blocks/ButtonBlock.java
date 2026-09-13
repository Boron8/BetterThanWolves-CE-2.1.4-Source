package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockButton;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class ButtonBlock extends BlockButton {
   @Environment(EnvType.CLIENT)
   private static final double ITEM_DEPTH = 0.375;
   @Environment(EnvType.CLIENT)
   private static final double ITEM_HALF_DEPTH = 0.1875;
   @Environment(EnvType.CLIENT)
   private static final double ITEM_WIDTH = 0.25;
   @Environment(EnvType.CLIENT)
   private static final double ITEM_HALF_WIDTH = 0.125;
   @Environment(EnvType.CLIENT)
   private static final double ITEM_HEIGHT = 0.25;
   @Environment(EnvType.CLIENT)
   private static final double ITEM_HALF_HEIGHT = 0.125;

   protected ButtonBlock(int iBlockID, boolean bSensitive) {
      super(iBlockID, bSensitive);
   }

   @Override
   public int getFacing(int iMetadata) {
      return MathHelper.clamp_int(6 - (iMetadata & 7), 2, 5);
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
   public void setBlockBoundsForItemRender() {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      int iDirection = iMetadata & 7;
      boolean bDepressed = (iMetadata & 8) > 0;
      float fMinY = 0.375F;
      float fMaxY = 0.625F;
      float fHalfWidth = 0.1875F;
      float fThickness = 0.125F;
      if (bDepressed) {
         fThickness = 0.0625F;
      }

      if (iDirection == 1) {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, fMinY, 0.5F - fHalfWidth, fThickness, fMaxY, 0.5F + fHalfWidth);
      } else if (iDirection == 2) {
         return AxisAlignedBB.getAABBPool().getAABB(1.0F - fThickness, fMinY, 0.5F - fHalfWidth, 1.0, fMaxY, 0.5F + fHalfWidth);
      } else {
         return iDirection == 3
            ? AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, fMinY, 0.0, 0.5F + fHalfWidth, fMaxY, fThickness)
            : AxisAlignedBB.getAABBPool().getAABB(0.5F - fHalfWidth, fMinY, 1.0F - fThickness, 0.5F + fHalfWidth, fMaxY, 1.0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getBlockBoundsFromPoolForItemRender(int iItemDamage) {
      return AxisAlignedBB.getAABBPool().getAABB(0.3125, 0.375, 0.375, 0.6875, 0.625, 0.625);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
