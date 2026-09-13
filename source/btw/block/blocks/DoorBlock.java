package btw.block.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockDoor;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.PathFinder;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class DoorBlock extends BlockDoor {
   private boolean suppressBottomDrop = false;

   public DoorBlock(int iBlockID, Material material) {
      super(iBlockID, material);
      this.initBlockBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
   }

   @Override
   public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
      if (this.blockMaterial == Material.iron) {
         return true;
      } else {
         int var10 = this.c_(par1World, par2, par3, par4);
         int var11 = var10 & 7;
         var11 ^= 4;
         if ((var10 & 8) == 0) {
            par1World.SetBlockMetadataWithNotify(par2, par3, par4, var11, 3);
            par1World.notifyBlockChange(par2, par3 + 1, par4, this.blockID);
            par1World.markBlockRangeForRenderUpdate(par2, par3, par4, par2, par3, par4);
         } else {
            par1World.SetBlockMetadataWithNotify(par2, par3 - 1, par4, var11, 3);
            par1World.notifyBlockChange(par2, par3, par4, this.blockID);
            par1World.markBlockRangeForRenderUpdate(par2, par3 - 1, par4, par2, par3, par4);
         }

         par1World.playAuxSFXAtEntity(par5EntityPlayer, 1003, par2, par3, par4, 0);
         return true;
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborID) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      if ((iMetadata & 8) == 0) {
         boolean bIsDestroyed = false;
         int iBlockAboveID = world.getBlockId(i, j + 1, k);
         if (iBlockAboveID != this.blockID) {
            world.setBlockToAir(i, j, k);
            bIsDestroyed = true;
         } else if (!world.doesBlockHaveSolidTopSurface(i, j - 1, k)) {
            world.setBlockToAir(i, j, k);
            bIsDestroyed = true;
            if (world.getBlockId(i, j + 1, k) == this.blockID) {
               world.setBlockToAir(i, j + 1, k);
            }
         }

         if (bIsDestroyed) {
            if (!this.suppressBottomDrop && !world.isRemote) {
               this.c(world, i, j, k, iMetadata, 0);
            }

            this.suppressBottomDrop = false;
         } else {
            boolean var8 = world.isBlockIndirectlyGettingPowered(i, j, k) || world.isBlockIndirectlyGettingPowered(i, j + 1, k);
            if ((var8 || iNeighborID > 0 && Block.blocksList[iNeighborID].canProvidePower()) && iNeighborID != this.blockID) {
               this.onPoweredBlockChange(world, i, j, k, var8);
            }
         }
      } else {
         if (world.getBlockId(i, j - 1, k) != this.blockID) {
            world.setBlockToAir(i, j, k);
         }

         if (iNeighborID > 0 && iNeighborID != this.blockID) {
            this.onNeighborBlockChange(world, i, j - 1, k, iNeighborID);
         }
      }
   }

   @Override
   public void onDestroyedByFire(World world, int i, int j, int k, int iFireAge, boolean bForcedFireSpread) {
      if ((world.getBlockMetadata(i, j, k) & 8) != 0) {
         this.suppressBottomDrop = true;
      }

      super.onDestroyedByFire(world, i, j, k, iFireAge, bForcedFireSpread);
   }

   @Override
   public void onPoweredBlockChange(World world, int i, int j, int k, boolean bOn) {
      int var6 = this.c_(world, i, j, k);
      boolean var7 = (var6 & 4) != 0;
      if (var7 != bOn) {
         int var8 = var6 & 7;
         var8 ^= 4;
         if ((var6 & 8) == 0) {
            world.SetBlockMetadataWithNotify(i, j, k, var8, 3);
            world.notifyBlockChange(i, j + 1, k, this.blockID);
            world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
         } else {
            world.SetBlockMetadataWithNotify(i, j - 1, k, var8, 3);
            world.notifyBlockChange(i, j, k, this.blockID);
            world.markBlockRangeForRenderUpdate(i, j - 1, k, i, j, k);
         }

         world.playAuxSFXAtEntity((EntityPlayer)null, 1003, i, j, k, 0);
      }
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean getPreventsFluidFlow(World world, int i, int j, int k, Block fluidBlock) {
      return true;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = this.c_(blockAccess, i, j, k);
      float var2x = 0.1875F;
      int var3x = iMetadata & 3;
      boolean var4x = (iMetadata & 4) != 0;
      boolean var5 = (iMetadata & 16) != 0;
      if (var3x == 0) {
         if (var4x) {
            return !var5
               ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, var2x)
               : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 1.0F - var2x, 1.0, 1.0, 1.0);
         } else {
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, var2x, 1.0, 1.0);
         }
      } else if (var3x == 1) {
         if (var4x) {
            return !var5
               ? AxisAlignedBB.getAABBPool().getAABB(1.0F - var2x, 0.0, 0.0, 1.0, 1.0, 1.0)
               : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, var2x, 1.0, 1.0);
         } else {
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, var2x);
         }
      } else if (var3x == 2) {
         if (var4x) {
            return !var5
               ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 1.0F - var2x, 1.0, 1.0, 1.0)
               : AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 1.0, var2x);
         } else {
            return AxisAlignedBB.getAABBPool().getAABB(1.0F - var2x, 0.0, 0.0, 1.0, 1.0, 1.0);
         }
      } else if (var3x == 3) {
         if (var4x) {
            return !var5
               ? AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, var2x, 1.0, 1.0)
               : AxisAlignedBB.getAABBPool().getAABB(1.0F - var2x, 0.0, 0.0, 1.0, 1.0, 1.0);
         } else {
            return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 1.0F - var2x, 1.0, 1.0, 1.0);
         }
      } else {
         return AxisAlignedBB.getAABBPool().getAABB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      AxisAlignedBB collisionBox = this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
      MovingObjectPosition collisionPoint = collisionBox.calculateIntercept(startRay, endRay);
      if (collisionPoint != null) {
         collisionPoint.blockX = i;
         collisionPoint.blockY = j;
         collisionPoint.blockZ = k;
      }

      return collisionPoint;
   }

   @Override
   public boolean shouldOffsetPositionIfPathingOutOf(IBlockAccess blockAccess, int i, int j, int k, Entity entity, PathFinder pathFinder) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockDoor(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return this.currentBlockRenderer.shouldSideBeRenderedBasedOnCurrentBounds(iNeighborI, iNeighborJ, iNeighborK, iSide);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
      return this.getBlockBoundsFromPoolBasedOnState(world, i, j, k).offset(i, j, k);
   }
}
