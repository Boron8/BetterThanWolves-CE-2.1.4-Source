package btw.block.blocks;

import btw.client.render.util.RenderUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class SlabBlock extends Block {
   public SlabBlock(int iBlockID, Material material) {
      super(iBlockID, material);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.k(255);
      Block.useNeighborBrightness[iBlockID] = true;
   }

   @Override
   public int onBlockPlaced(World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ, int iMetadata) {
      if ((iFacing == 0 || iFacing != 1 && fClickY > 0.5F) && this.canBePlacedUpsideDownAtLocation(world, i, j, k)) {
         iMetadata = this.setIsUpsideDown(iMetadata, true);
      }

      return iMetadata;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getBlockBoundsFromPoolFromMetadata(blockAccess.getBlockMetadata(i, j, k));
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
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      boolean bIsUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
      return bIsUpsideDown ? iFacing == 1 : iFacing == 0;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsUpsideDown(blockAccess, i, j, k) ? -0.5F : 0.0F;
   }

   @Override
   public boolean isSnowCoveringTopSurface(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.getIsUpsideDown(blockAccess, i, j, k)
         ? blockAccess.getBlockId(i, j + 1, k) == Block.snow.blockID
         : super.isSnowCoveringTopSurface(blockAccess, i, j, k);
   }

   @Override
   public boolean hasContactPointToFullFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      if (iFacing < 2) {
         boolean bIsUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         return bIsUpsideDown == (iFacing == 1);
      } else {
         return true;
      }
   }

   @Override
   public boolean hasContactPointToSlabSideFace(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIsSlabUpsideDown) {
      return bIsSlabUpsideDown == this.getIsUpsideDown(blockAccess, i, j, k);
   }

   @Override
   public boolean hasNeighborWithMortarInContact(World world, int i, int j, int k) {
      boolean bIsUpsideDown = this.getIsUpsideDown(world, i, j, k);
      return this.hasNeighborWithMortarInContact(world, i, j, k, bIsUpsideDown);
   }

   @Override
   public boolean hasStickySnowNeighborInContact(World world, int i, int j, int k) {
      boolean bIsUpsideDown = this.getIsUpsideDown(world, i, j, k);
      return this.hasStickySnowNeighborInContact(world, i, j, k, bIsUpsideDown);
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      iMetadata = this.setIsUpsideDown(iMetadata, false);
      return super.createStackedBlock(iMetadata);
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return this.blockMaterial.getMobsCanSpawnOn(world.provider.dimensionId);
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      return !this.getIsUpsideDown(world, i, j, k) ? -0.5F : 0.0F;
   }

   protected boolean hasNeighborWithMortarInContact(World world, int i, int j, int k, boolean bIsUpsideDown) {
      if (bIsUpsideDown) {
         if (WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, 1)) {
            return true;
         }
      } else if (WorldUtils.hasNeighborWithMortarInFullFaceContactToFacing(world, i, j, k, 0)) {
         return true;
      }

      for (int iTempFacing = 2; iTempFacing < 6; iTempFacing++) {
         if (WorldUtils.hasNeighborWithMortarInSlabSideContactToFacing(world, i, j, k, iTempFacing, bIsUpsideDown)) {
            return true;
         }
      }

      return false;
   }

   protected boolean hasStickySnowNeighborInContact(World world, int i, int j, int k, boolean bIsUpsideDown) {
      if (bIsUpsideDown) {
         if (WorldUtils.hasStickySnowNeighborInFullFaceContactToFacing(world, i, j, k, 1)) {
            return true;
         }
      } else if (WorldUtils.hasStickySnowNeighborInFullFaceContactToFacing(world, i, j, k, 0)) {
         return true;
      }

      for (int iTempFacing = 2; iTempFacing < 6; iTempFacing++) {
         if (WorldUtils.hasStickySnowNeighborInSlabSideContactToFacing(world, i, j, k, iTempFacing, bIsUpsideDown)) {
            return true;
         }
      }

      return false;
   }

   public boolean canBePlacedUpsideDownAtLocation(World world, int i, int j, int k) {
      return true;
   }

   public boolean getIsUpsideDown(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsUpsideDown(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsUpsideDown(int iMetadata) {
      return (iMetadata & 1) > 0;
   }

   public void setIsUpsideDown(World world, int i, int j, int k, boolean bUpsideDown) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      world.setBlockMetadataWithNotify(i, j, k, this.setIsUpsideDown(iMetadata, bUpsideDown));
   }

   public int setIsUpsideDown(int iMetadata, boolean bUpsideDown) {
      if (bUpsideDown) {
         iMetadata |= 1;
      } else {
         iMetadata &= -2;
      }

      return iMetadata;
   }

   public boolean convertToFullBlock(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      return world.setBlockAndMetadataWithNotify(i, j, k, this.getCombinedBlockID(iMetadata), this.getCombinedMetadata(iMetadata));
   }

   public abstract int getCombinedBlockID(int var1);

   public int getCombinedMetadata(int iMetadata) {
      return 0;
   }

   protected AxisAlignedBB getBlockBoundsFromPoolFromMetadata(int iMetadata) {
      return this.getIsUpsideDown(iMetadata)
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      BlockPos myPos = new BlockPos(iNeighborI, iNeighborJ, iNeighborK, getOppositeFacing(iSide));
      boolean bUpsideDown = this.getIsUpsideDown(blockAccess, myPos.x, myPos.y, myPos.z);
      if (iSide >= 2) {
         return RenderUtils.shouldRenderNeighborHalfSlabSide(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide, bUpsideDown);
      } else {
         return iSide == 0
            ? bUpsideDown || !blockAccess.isBlockOpaqueCube(iNeighborI, iNeighborJ, iNeighborK)
            : !bUpsideDown || !blockAccess.isBlockOpaqueCube(iNeighborI, iNeighborJ, iNeighborK);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown) {
      return this.getIsUpsideDown(blockAccess, i, j, k) != bNeighborUpsideDown;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
      if (iNeighborSide < 2) {
         boolean bUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         return iNeighborSide == 0 ? !bUpsideDown : bUpsideDown;
      } else {
         return true;
      }
   }
}
