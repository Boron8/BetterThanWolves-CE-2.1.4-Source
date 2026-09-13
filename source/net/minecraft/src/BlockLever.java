package net.minecraft.src;

public class BlockLever extends Block {
   protected BlockLever(int var1) {
      super(var1, Material.circuits);
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
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
      return 12;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
      if (var5 == 0 && var1.isBlockNormalCube(var2, var3 + 1, var4)) {
         return true;
      } else if (var5 == 1 && var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4)) {
         return true;
      } else if (var5 == 2 && var1.isBlockNormalCube(var2, var3, var4 + 1)) {
         return true;
      } else if (var5 == 3 && var1.isBlockNormalCube(var2, var3, var4 - 1)) {
         return true;
      } else {
         return var5 == 4 && var1.isBlockNormalCube(var2 + 1, var3, var4) ? true : var5 == 5 && var1.isBlockNormalCube(var2 - 1, var3, var4);
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
      if (var1.isBlockNormalCube(var2 - 1, var3, var4)) {
         return true;
      } else if (var1.isBlockNormalCube(var2 + 1, var3, var4)) {
         return true;
      } else if (var1.isBlockNormalCube(var2, var3, var4 - 1)) {
         return true;
      } else if (var1.isBlockNormalCube(var2, var3, var4 + 1)) {
         return true;
      } else {
         return var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) ? true : var1.isBlockNormalCube(var2, var3 + 1, var4);
      }
   }

   @Override
   public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      int var11 = var9 & 8;
      int var10 = var9 & 7;
      byte var12 = -1;
      if (var5 == 0 && var1.isBlockNormalCube(var2, var3 + 1, var4)) {
         var12 = 0;
      }

      if (var5 == 1 && var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4)) {
         var12 = 5;
      }

      if (var5 == 2 && var1.isBlockNormalCube(var2, var3, var4 + 1)) {
         var12 = 4;
      }

      if (var5 == 3 && var1.isBlockNormalCube(var2, var3, var4 - 1)) {
         var12 = 3;
      }

      if (var5 == 4 && var1.isBlockNormalCube(var2 + 1, var3, var4)) {
         var12 = 2;
      }

      if (var5 == 5 && var1.isBlockNormalCube(var2 - 1, var3, var4)) {
         var12 = 1;
      }

      return var12 + var11;
   }

   @Override
   public void onBlockPlacedBy(World var1, int var2, int var3, int var4, EntityLiving var5, ItemStack var6) {
      int var7 = var1.getBlockMetadata(var2, var3, var4);
      int var8 = var7 & 7;
      int var9 = var7 & 8;
      if (var8 == invertMetadata(1)) {
         if ((MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 1) == 0) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 5 | var9, 2);
         } else {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 6 | var9, 2);
         }
      } else if (var8 == invertMetadata(0)) {
         if ((MathHelper.floor_double(var5.rotationYaw * 4.0F / 360.0F + 0.5) & 1) == 0) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 7 | var9, 2);
         } else {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 0 | var9, 2);
         }
      }
   }

   public static int invertMetadata(int var0) {
      switch (var0) {
         case 0:
            return 0;
         case 1:
            return 5;
         case 2:
            return 4;
         case 3:
            return 3;
         case 4:
            return 2;
         case 5:
            return 1;
         default:
            return -1;
      }
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (this.checkIfAttachedToBlock(var1, var2, var3, var4)) {
         int var6 = var1.getBlockMetadata(var2, var3, var4) & 7;
         boolean var7 = false;
         if (!var1.isBlockNormalCube(var2 - 1, var3, var4) && var6 == 1) {
            var7 = true;
         }

         if (!var1.isBlockNormalCube(var2 + 1, var3, var4) && var6 == 2) {
            var7 = true;
         }

         if (!var1.isBlockNormalCube(var2, var3, var4 - 1) && var6 == 3) {
            var7 = true;
         }

         if (!var1.isBlockNormalCube(var2, var3, var4 + 1) && var6 == 4) {
            var7 = true;
         }

         if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) && var6 == 5) {
            var7 = true;
         }

         if (!var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4) && var6 == 6) {
            var7 = true;
         }

         if (!var1.isBlockNormalCube(var2, var3 + 1, var4) && var6 == 0) {
            var7 = true;
         }

         if (!var1.isBlockNormalCube(var2, var3 + 1, var4) && var6 == 7) {
            var7 = true;
         }

         if (var7) {
            this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
            var1.setBlockToAir(var2, var3, var4);
         }
      }
   }

   private boolean checkIfAttachedToBlock(World var1, int var2, int var3, int var4) {
      if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
         this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
         var1.setBlockToAir(var2, var3, var4);
         return false;
      } else {
         return true;
      }
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      int var5 = var1.getBlockMetadata(var2, var3, var4) & 7;
      float var6 = 0.1875F;
      if (var5 == 1) {
         this.a(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
      } else if (var5 == 2) {
         this.a(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
      } else if (var5 == 3) {
         this.a(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
      } else if (var5 == 4) {
         this.a(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
      } else if (var5 == 5 || var5 == 6) {
         var6 = 0.25F;
         this.a(0.5F - var6, 0.0F, 0.5F - var6, 0.5F + var6, 0.6F, 0.5F + var6);
      } else if (var5 == 0 || var5 == 7) {
         var6 = 0.25F;
         this.a(0.5F - var6, 0.4F, 0.5F - var6, 0.5F + var6, 1.0F, 0.5F + var6);
      }
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      if (var1.isRemote) {
         return true;
      } else {
         int var10 = var1.getBlockMetadata(var2, var3, var4);
         int var11 = var10 & 7;
         int var12 = 8 - (var10 & 8);
         var1.setBlockMetadataWithNotify(var2, var3, var4, var11 + var12, 3);
         var1.playSoundEffect(var2 + 0.5, var3 + 0.5, var4 + 0.5, "random.click", 0.3F, var12 > 0 ? 0.6F : 0.5F);
         var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
         if (var11 == 1) {
            var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
         } else if (var11 == 2) {
            var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
         } else if (var11 == 3) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
         } else if (var11 == 4) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
         } else if (var11 == 5 || var11 == 6) {
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
         } else if (var11 == 0 || var11 == 7) {
            var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
         }

         return true;
      }
   }

   @Override
   public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
      if ((var6 & 8) > 0) {
         var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
         int var7 = var6 & 7;
         if (var7 == 1) {
            var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
         } else if (var7 == 2) {
            var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
         } else if (var7 == 3) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
         } else if (var7 == 4) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
         } else if (var7 == 5 || var7 == 6) {
            var1.notifyBlocksOfNeighborChange(var2, var3 - 1, var4, this.blockID);
         } else if (var7 == 0 || var7 == 7) {
            var1.notifyBlocksOfNeighborChange(var2, var3 + 1, var4, this.blockID);
         }
      }

      super.breakBlock(var1, var2, var3, var4, var5, var6);
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return (var1.getBlockMetadata(var2, var3, var4) & 8) > 0 ? 15 : 0;
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      if ((var6 & 8) == 0) {
         return 0;
      } else {
         int var7 = var6 & 7;
         if (var7 == 0 && var5 == 0) {
            return 15;
         } else if (var7 == 7 && var5 == 0) {
            return 15;
         } else if (var7 == 6 && var5 == 1) {
            return 15;
         } else if (var7 == 5 && var5 == 1) {
            return 15;
         } else if (var7 == 4 && var5 == 2) {
            return 15;
         } else if (var7 == 3 && var5 == 3) {
            return 15;
         } else if (var7 == 2 && var5 == 4) {
            return 15;
         } else {
            return var7 == 1 && var5 == 5 ? 15 : 0;
         }
      }
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }
}
