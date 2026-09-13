package net.minecraft.src;

import java.util.Random;

public class BlockTripWireSource extends Block {
   public BlockTripWireSource(int var1) {
      super(var1, Material.circuits);
      this.a(CreativeTabs.tabRedstone);
      this.b(true);
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
      return 29;
   }

   @Override
   public int tickRate(World var1) {
      return 10;
   }

   @Override
   public boolean canPlaceBlockOnSide(World var1, int var2, int var3, int var4, int var5) {
      if (var5 == 2 && var1.isBlockNormalCube(var2, var3, var4 + 1)) {
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
      } else {
         return var1.isBlockNormalCube(var2, var3, var4 - 1) ? true : var1.isBlockNormalCube(var2, var3, var4 + 1);
      }
   }

   @Override
   public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      byte var10 = 0;
      if (var5 == 2 && var1.isBlockNormalCubeDefault(var2, var3, var4 + 1, true)) {
         var10 = 2;
      }

      if (var5 == 3 && var1.isBlockNormalCubeDefault(var2, var3, var4 - 1, true)) {
         var10 = 0;
      }

      if (var5 == 4 && var1.isBlockNormalCubeDefault(var2 + 1, var3, var4, true)) {
         var10 = 1;
      }

      if (var5 == 5 && var1.isBlockNormalCubeDefault(var2 - 1, var3, var4, true)) {
         var10 = 3;
      }

      return var10;
   }

   @Override
   public void onPostBlockPlaced(World var1, int var2, int var3, int var4, int var5) {
      this.func_72143_a(var1, var2, var3, var4, this.blockID, var5, false, -1, 0);
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      if (var5 != this.blockID) {
         if (this.func_72144_l(var1, var2, var3, var4)) {
            int var6 = var1.getBlockMetadata(var2, var3, var4);
            int var7 = var6 & 3;
            boolean var8 = false;
            if (!var1.isBlockNormalCube(var2 - 1, var3, var4) && var7 == 3) {
               var8 = true;
            }

            if (!var1.isBlockNormalCube(var2 + 1, var3, var4) && var7 == 1) {
               var8 = true;
            }

            if (!var1.isBlockNormalCube(var2, var3, var4 - 1) && var7 == 0) {
               var8 = true;
            }

            if (!var1.isBlockNormalCube(var2, var3, var4 + 1) && var7 == 2) {
               var8 = true;
            }

            if (var8) {
               this.c(var1, var2, var3, var4, var6, 0);
               var1.setBlockToAir(var2, var3, var4);
            }
         }
      }
   }

   public void func_72143_a(World var1, int var2, int var3, int var4, int var5, int var6, boolean var7, int var8, int var9) {
      int var10 = var6 & 3;
      boolean var11 = (var6 & 4) == 4;
      boolean var12 = (var6 & 8) == 8;
      boolean var13 = var5 == Block.tripWireSource.blockID;
      boolean var14 = false;
      boolean var15 = !var1.doesBlockHaveSolidTopSurface(var2, var3 - 1, var4);
      int var16 = Direction.offsetX[var10];
      int var17 = Direction.offsetZ[var10];
      int var18 = 0;
      int[] var19 = new int[42];

      for (int var20 = 1; var20 < 42; var20++) {
         int var21 = var2 + var16 * var20;
         int var22 = var4 + var17 * var20;
         int var23 = var1.getBlockId(var21, var3, var22);
         if (var23 == Block.tripWireSource.blockID) {
            int var38 = var1.getBlockMetadata(var21, var3, var22);
            if ((var38 & 3) == Direction.rotateOpposite[var10]) {
               var18 = var20;
            }
            break;
         }

         if (var23 != Block.tripWire.blockID && var20 != var8) {
            var19[var20] = -1;
            var13 = false;
         } else {
            int var24 = var20 == var8 ? var9 : var1.getBlockMetadata(var21, var3, var22);
            boolean var25 = (var24 & 8) != 8;
            boolean var26 = (var24 & 1) == 1;
            boolean var27 = (var24 & 2) == 2;
            var13 &= var27 == var15;
            var14 |= var25 && var26;
            var19[var20] = var24;
            if (var20 == var8) {
               var1.scheduleBlockUpdate(var2, var3, var4, var5, this.tickRate(var1));
               var13 &= var25;
            }
         }
      }

      var13 &= var18 > 1;
      var14 &= var13;
      int var31 = (var13 ? 4 : 0) | (var14 ? 8 : 0);
      var6 = var10 | var31;
      if (var18 > 0) {
         int var32 = var2 + var16 * var18;
         int var34 = var4 + var17 * var18;
         int var36 = Direction.rotateOpposite[var10];
         var1.setBlockMetadataWithNotify(var32, var3, var34, var36 | var31, 3);
         this.notifyNeighborOfChange(var1, var32, var3, var34, var36);
         this.playSoundEffect(var1, var32, var3, var34, var13, var14, var11, var12);
      }

      this.playSoundEffect(var1, var2, var3, var4, var13, var14, var11, var12);
      if (var5 > 0) {
         var1.setBlockMetadataWithNotify(var2, var3, var4, var6, 3);
         if (var7) {
            this.notifyNeighborOfChange(var1, var2, var3, var4, var10);
         }
      }

      if (var11 != var13) {
         for (int var33 = 1; var33 < var18; var33++) {
            int var35 = var2 + var16 * var33;
            int var37 = var4 + var17 * var33;
            int var39 = var19[var33];
            if (var39 >= 0) {
               if (var13) {
                  var39 |= 4;
               } else {
                  var39 &= -5;
               }

               var1.setBlockMetadataWithNotify(var35, var3, var37, var39, 3);
            }
         }
      }
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      this.func_72143_a(var1, var2, var3, var4, this.blockID, var1.getBlockMetadata(var2, var3, var4), true, -1, 0);
   }

   private void playSoundEffect(World var1, int var2, int var3, int var4, boolean var5, boolean var6, boolean var7, boolean var8) {
      if (var6 && !var8) {
         var1.playSoundEffect(var2 + 0.5, var3 + 0.1, var4 + 0.5, "random.click", 0.4F, 0.6F);
      } else if (!var6 && var8) {
         var1.playSoundEffect(var2 + 0.5, var3 + 0.1, var4 + 0.5, "random.click", 0.4F, 0.5F);
      } else if (var5 && !var7) {
         var1.playSoundEffect(var2 + 0.5, var3 + 0.1, var4 + 0.5, "random.click", 0.4F, 0.7F);
      } else if (!var5 && var7) {
         var1.playSoundEffect(var2 + 0.5, var3 + 0.1, var4 + 0.5, "random.bowhit", 0.4F, 1.2F / (var1.rand.nextFloat() * 0.2F + 0.9F));
      }
   }

   private void notifyNeighborOfChange(World var1, int var2, int var3, int var4, int var5) {
      var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
      if (var5 == 3) {
         var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
      } else if (var5 == 1) {
         var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
      } else if (var5 == 0) {
         var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
      } else if (var5 == 2) {
         var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
      }
   }

   private boolean func_72144_l(World var1, int var2, int var3, int var4) {
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
      int var5 = var1.getBlockMetadata(var2, var3, var4) & 3;
      float var6 = 0.1875F;
      if (var5 == 3) {
         this.a(0.0F, 0.2F, 0.5F - var6, var6 * 2.0F, 0.8F, 0.5F + var6);
      } else if (var5 == 1) {
         this.a(1.0F - var6 * 2.0F, 0.2F, 0.5F - var6, 1.0F, 0.8F, 0.5F + var6);
      } else if (var5 == 0) {
         this.a(0.5F - var6, 0.2F, 0.0F, 0.5F + var6, 0.8F, var6 * 2.0F);
      } else if (var5 == 2) {
         this.a(0.5F - var6, 0.2F, 1.0F - var6 * 2.0F, 0.5F + var6, 0.8F, 1.0F);
      }
   }

   @Override
   public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
      boolean var7 = (var6 & 4) == 4;
      boolean var8 = (var6 & 8) == 8;
      if (var7 || var8) {
         this.func_72143_a(var1, var2, var3, var4, 0, var6, false, -1, 0);
      }

      if (var8) {
         var1.notifyBlocksOfNeighborChange(var2, var3, var4, this.blockID);
         int var9 = var6 & 3;
         if (var9 == 3) {
            var1.notifyBlocksOfNeighborChange(var2 - 1, var3, var4, this.blockID);
         } else if (var9 == 1) {
            var1.notifyBlocksOfNeighborChange(var2 + 1, var3, var4, this.blockID);
         } else if (var9 == 0) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 - 1, this.blockID);
         } else if (var9 == 2) {
            var1.notifyBlocksOfNeighborChange(var2, var3, var4 + 1, this.blockID);
         }
      }

      super.breakBlock(var1, var2, var3, var4, var5, var6);
   }

   @Override
   public int isProvidingWeakPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return (var1.getBlockMetadata(var2, var3, var4) & 8) == 8 ? 15 : 0;
   }

   @Override
   public int isProvidingStrongPower(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      if ((var6 & 8) != 8) {
         return 0;
      } else {
         int var7 = var6 & 3;
         if (var7 == 2 && var5 == 2) {
            return 15;
         } else if (var7 == 0 && var5 == 3) {
            return 15;
         } else if (var7 == 1 && var5 == 4) {
            return 15;
         } else {
            return var7 == 3 && var5 == 5 ? 15 : 0;
         }
      }
   }

   @Override
   public boolean canProvidePower() {
      return true;
   }
}
