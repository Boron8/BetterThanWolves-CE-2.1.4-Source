package net.minecraft.src;

import java.util.Random;

public class BlockPistonMoving extends BlockContainer {
   public BlockPistonMoving(int var1) {
      super(var1, Material.piston);
      this.c(-1.0F);
   }

   @Override
   public TileEntity createNewTileEntity(World var1) {
      return null;
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
   }

   @Override
   public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
      TileEntity var7 = var1.getBlockTileEntity(var2, var3, var4);
      if (var7 instanceof TileEntityPiston) {
         ((TileEntityPiston)var7).clearPistonTileEntity();
      } else {
         super.breakBlock(var1, var2, var3, var4, var5, var6);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
      return false;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
      return false;
   }

   @Override
   public int getRenderType() {
      return -1;
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
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      if (!var1.isRemote && var1.getBlockTileEntity(var2, var3, var4) == null) {
         var1.setBlockToAir(var2, var3, var4);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return 0;
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
      if (!var1.isRemote) {
         TileEntityPiston var8 = this.getTileEntityAtLocation(var1, var2, var3, var4);
         if (var8 != null) {
            Block.blocksList[var8.getStoredBlockID()].dropBlockAsItem(var1, var2, var3, var4, var8.getBlockMetadata(), 0);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (!var1.isRemote && var1.getBlockTileEntity(var2, var3, var4) == null) {
      }
   }

   public static TileEntity getTileEntity(int var0, int var1, int var2, boolean var3, boolean var4) {
      return new TileEntityPiston(var0, var1, var2, var3, var4);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      TileEntityPiston var5 = this.getTileEntityAtLocation(var1, var2, var3, var4);
      if (var5 == null) {
         return null;
      } else {
         float var6 = var5.getProgress(0.0F);
         if (var5.isExtending()) {
            var6 = 1.0F - var6;
         }

         return this.getAxisAlignedBB(var1, var2, var3, var4, var5.getStoredBlockID(), var6, var5.getPistonOrientation());
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      TileEntityPiston var5 = this.getTileEntityAtLocation(var1, var2, var3, var4);
      if (var5 != null) {
         Block var6 = Block.blocksList[var5.getStoredBlockID()];
         if (var6 == null || var6 == this) {
            return;
         }

         var6.setBlockBoundsBasedOnState(var1, var2, var3, var4);
         float var7 = var5.getProgress(0.0F);
         if (var5.isExtending()) {
            var7 = 1.0F - var7;
         }

         int var8 = var5.getPistonOrientation();
         this.minX = var6.getBlockBoundsMinX() - Facing.offsetsXForSide[var8] * var7;
         this.minY = var6.getBlockBoundsMinY() - Facing.offsetsYForSide[var8] * var7;
         this.minZ = var6.getBlockBoundsMinZ() - Facing.offsetsZForSide[var8] * var7;
         this.maxX = var6.getBlockBoundsMaxX() - Facing.offsetsXForSide[var8] * var7;
         this.maxY = var6.getBlockBoundsMaxY() - Facing.offsetsYForSide[var8] * var7;
         this.maxZ = var6.getBlockBoundsMaxZ() - Facing.offsetsZForSide[var8] * var7;
      }
   }

   public AxisAlignedBB getAxisAlignedBB(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
      if (var5 != 0 && var5 != this.blockID) {
         AxisAlignedBB var8 = Block.blocksList[var5].getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
         if (var8 == null) {
            return null;
         } else {
            if (Facing.offsetsXForSide[var7] < 0) {
               var8.minX = var8.minX - Facing.offsetsXForSide[var7] * var6;
            } else {
               var8.maxX = var8.maxX - Facing.offsetsXForSide[var7] * var6;
            }

            if (Facing.offsetsYForSide[var7] < 0) {
               var8.minY = var8.minY - Facing.offsetsYForSide[var7] * var6;
            } else {
               var8.maxY = var8.maxY - Facing.offsetsYForSide[var7] * var6;
            }

            if (Facing.offsetsZForSide[var7] < 0) {
               var8.minZ = var8.minZ - Facing.offsetsZForSide[var7] * var6;
            } else {
               var8.maxZ = var8.maxZ - Facing.offsetsZForSide[var7] * var6;
            }

            return var8;
         }
      } else {
         return null;
      }
   }

   private TileEntityPiston getTileEntityAtLocation(IBlockAccess var1, int var2, int var3, int var4) {
      TileEntity var5 = var1.getBlockTileEntity(var2, var3, var4);
      return var5 instanceof TileEntityPiston ? (TileEntityPiston)var5 : null;
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return 0;
   }

   @Override
   public void registerIcons(IconRegister var1) {
      this.blockIcon = var1.registerIcon("piston_top");
   }
}
