package net.minecraft.src;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class BlockRailBase extends Block {
   protected final boolean isPowered;

   public static final boolean isRailBlockAt(World par0World, int par1, int par2, int par3) {
      return isRailBlock(par0World.getBlockId(par1, par2, par3));
   }

   public static final boolean isRailBlock(int par0) {
      return Block.blocksList[par0] instanceof BlockRailBase;
   }

   protected BlockRailBase(int par1, boolean par2) {
      super(par1, Material.circuits);
      this.isPowered = par2;
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
      this.a(CreativeTabs.tabTransport);
   }

   public boolean isPowered() {
      return this.isPowered;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4) {
      return null;
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
   public int getRenderType() {
      return 9;
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return 1;
   }

   @Override
   public boolean canPlaceBlockAt(World par1World, int par2, int par3, int par4) {
      return par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
   }

   @Override
   public void onBlockAdded(World par1World, int par2, int par3, int par4) {
      if (!par1World.isRemote) {
         this.refreshTrackShape(par1World, par2, par3, par4, true);
         if (this.isPowered) {
            this.onNeighborBlockChange(par1World, par2, par3, par4, this.blockID);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, int par5) {
      if (!par1World.isRemote) {
         int var6 = par1World.getBlockMetadata(par2, par3, par4);
         int var7 = var6;
         if (this.isPowered) {
            var7 = var6 & 7;
         }

         boolean var8 = false;
         if (!par1World.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4)) {
            var8 = true;
         }

         if (var7 == 2 && !par1World.doesBlockHaveSolidTopSurface(par2 + 1, par3, par4)) {
            var8 = true;
         }

         if (var7 == 3 && !par1World.doesBlockHaveSolidTopSurface(par2 - 1, par3, par4)) {
            var8 = true;
         }

         if (var7 == 4 && !par1World.doesBlockHaveSolidTopSurface(par2, par3, par4 - 1)) {
            var8 = true;
         }

         if (var7 == 5 && !par1World.doesBlockHaveSolidTopSurface(par2, par3, par4 + 1)) {
            var8 = true;
         }

         if (var8) {
            if (par1World.getBlockId(par2, par3, par4) != this.blockID) {
               return;
            }

            this.c(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
            par1World.setBlockToAir(par2, par3, par4);
         } else {
            this.func_94358_a(par1World, par2, par3, par4, var6, var7, par5);
         }
      }
   }

   protected void func_94358_a(World par1World, int par2, int par3, int par4, int par5, int par6, int par7) {
   }

   protected void refreshTrackShape(World par1World, int par2, int par3, int par4, boolean par5) {
      if (!par1World.isRemote) {
         new BlockBaseRailLogic(this, par1World, par2, par3, par4).func_94511_a(par1World.isBlockIndirectlyGettingPowered(par2, par3, par4), par5);
      }
   }

   @Override
   public int getMobilityFlag() {
      return 0;
   }

   @Override
   public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6) {
      int var7 = par6;
      if (this.isPowered) {
         var7 = par6 & 7;
      }

      super.breakBlock(par1World, par2, par3, par4, par5, par6);
      if (var7 == 2 || var7 == 3 || var7 == 4 || var7 == 5) {
         par1World.notifyBlocksOfNeighborChange(par2, par3 + 1, par4, par5);
      }

      if (this.isPowered) {
         par1World.notifyBlocksOfNeighborChange(par2, par3, par4, par5);
         par1World.notifyBlocksOfNeighborChange(par2, par3 - 1, par4, par5);
      }
   }

   @Override
   public boolean canRotateOnTurntable(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public int rotateMetadataAroundJAxis(int iMetadata, boolean bReverse) {
      int iDirection = iMetadata;
      if (this.isPowered()) {
         iDirection = iMetadata & 7;
      }

      if (iDirection == 0) {
         iDirection = 1;
      } else if (iDirection == 1) {
         iDirection = 0;
      } else if (iDirection != 2 && iDirection != 3 && iDirection != 4 && iDirection != 5) {
         if (iDirection == 6) {
            if (bReverse) {
               iDirection = 7;
            } else {
               iDirection = 9;
            }
         } else if (iDirection == 7) {
            if (bReverse) {
               iDirection = 8;
            } else {
               iDirection = 6;
            }
         } else if (iDirection == 8) {
            if (bReverse) {
               iDirection = 9;
            } else {
               iDirection = 7;
            }
         } else if (iDirection == 9) {
            if (bReverse) {
               iDirection = 6;
            } else {
               iDirection = 8;
            }
         }
      }

      if (this.isPowered()) {
         iMetadata = iMetadata & 8 | iDirection;
      } else {
         iMetadata = iDirection;
      }

      return iMetadata;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iDirection = blockAccess.getBlockMetadata(i, j, k);
      return iDirection >= 2 && iDirection <= 5
         ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.625, 1.0)
         : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockMinecartTrack(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }
}
