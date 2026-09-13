package net.minecraft.src;

import btw.client.render.util.RenderUtils;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class BlockHalfSlab extends Block {
   protected final boolean isDoubleSlab;

   public BlockHalfSlab(int par1, boolean par2, Material par3Material) {
      super(par1, par3Material);
      this.isDoubleSlab = par2;
      if (par2) {
         s[par1] = true;
         this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      } else {
         this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
         w[par1] = true;
      }

      this.k(255);
   }

   @Override
   public boolean isOpaqueCube() {
      return this.isDoubleSlab;
   }

   @Override
   public int onBlockPlaced(World par1World, int par2, int par3, int par4, int par5, float par6, float par7, float par8, int par9) {
      return this.isDoubleSlab ? par9 : (par5 == 0 || par5 != 1 && !(par7 <= 0.5) ? par9 | 8 : par9);
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return this.isDoubleSlab ? 2 : 1;
   }

   @Override
   public int damageDropped(int par1) {
      return par1 & 7;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return this.isDoubleSlab;
   }

   @Environment(EnvType.CLIENT)
   private static boolean isBlockSingleSlab(int par0) {
      return par0 == Block.stoneSingleSlab.blockID || par0 == Block.woodSingleSlab.blockID;
   }

   public abstract String getFullSlabName(int var1);

   @Override
   public int getDamageValue(World par1World, int par2, int par3, int par4) {
      return super.getDamageValue(par1World, par2, par3, par4) & 7;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return isBlockSingleSlab(this.blockID)
         ? this.blockID
         : (
            this.blockID == Block.stoneDoubleSlab.blockID
               ? Block.stoneSingleSlab.blockID
               : (this.blockID == Block.woodDoubleSlab.blockID ? Block.woodSingleSlab.blockID : Block.stoneSingleSlab.blockID)
         );
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      AxisAlignedBB bounds;
      if (this.isDoubleSlab) {
         bounds = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      } else if (this.getIsUpsideDown(blockAccess, i, j, k)) {
         bounds = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.5, 0.0, 1.0, 1.0, 1.0);
      } else {
         bounds = AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      }

      return bounds;
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      if (!this.isDoubleSlab) {
         boolean bIsUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         if (iFacing == 0) {
            if (!bIsUpsideDown) {
               return true;
            }
         } else if (iFacing == 1 && bIsUpsideDown) {
            return true;
         }
      }

      return super.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
   }

   @Override
   public float groundCoverRestingOnVisualOffset(IBlockAccess blockAccess, int i, int j, int k) {
      return !this.isDoubleSlab && (blockAccess.getBlockMetadata(i, j, k) & 8) == 0 ? -0.5F : 0.0F;
   }

   @Override
   public boolean canGroundCoverRestOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canMobsSpawnOn(World world, int i, int j, int k) {
      return this.blockMaterial.getMobsCanSpawnOn(world.provider.dimensionId);
   }

   @Override
   public float mobSpawnOnVerticalOffset(World world, int i, int j, int k) {
      return !this.isDoubleSlab && !this.getIsUpsideDown(world, i, j, k) ? -0.5F : 0.0F;
   }

   public boolean getIsUpsideDown(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsUpsideDown(blockAccess.getBlockMetadata(i, j, k));
   }

   public boolean getIsUpsideDown(int iMetadata) {
      return (iMetadata & 8) > 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if (this.isDoubleSlab) {
         return super.shouldSideBeRendered(blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide);
      } else {
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
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborHalfSlabSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSlabSide, boolean bNeighborUpsideDown) {
      return this.isDoubleSlab ? false : this.getIsUpsideDown(blockAccess, i, j, k) != bNeighborUpsideDown;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderNeighborFullFaceSide(IBlockAccess blockAccess, int i, int j, int k, int iNeighborSide) {
      if (this.isDoubleSlab) {
         return false;
      } else if (iNeighborSide < 2) {
         boolean bUpsideDown = this.getIsUpsideDown(blockAccess, i, j, k);
         return iNeighborSide == 0 ? !bUpsideDown : bUpsideDown;
      } else {
         return true;
      }
   }
}
