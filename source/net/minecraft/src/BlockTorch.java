package net.minecraft.src;

import java.util.Random;

public class BlockTorch extends Block {
   protected BlockTorch(int var1) {
      super(var1, Material.circuits);
      this.b(true);
      this.a(CreativeTabs.tabDecorations);
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
      return 2;
   }

   private boolean canPlaceTorchOn(World var1, int var2, int var3, int var4) {
      if (var1.doesBlockHaveSolidTopSurface(var2, var3, var4)) {
         return true;
      } else {
         int var5 = var1.getBlockId(var2, var3, var4);
         return var5 == Block.fence.blockID || var5 == Block.netherFence.blockID || var5 == Block.glass.blockID || var5 == Block.cobblestoneWall.blockID;
      }
   }

   @Override
   public boolean canPlaceBlockAt(World var1, int var2, int var3, int var4) {
      if (var1.isBlockNormalCubeDefault(var2 - 1, var3, var4, true)) {
         return true;
      } else if (var1.isBlockNormalCubeDefault(var2 + 1, var3, var4, true)) {
         return true;
      } else if (var1.isBlockNormalCubeDefault(var2, var3, var4 - 1, true)) {
         return true;
      } else {
         return var1.isBlockNormalCubeDefault(var2, var3, var4 + 1, true) ? true : this.canPlaceTorchOn(var1, var2, var3 - 1, var4);
      }
   }

   @Override
   public int onBlockPlaced(World var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, int var9) {
      int var10 = var9;
      if (var5 == 1 && this.canPlaceTorchOn(var1, var2, var3 - 1, var4)) {
         var10 = 5;
      }

      if (var5 == 2 && var1.isBlockNormalCubeDefault(var2, var3, var4 + 1, true)) {
         var10 = 4;
      }

      if (var5 == 3 && var1.isBlockNormalCubeDefault(var2, var3, var4 - 1, true)) {
         var10 = 3;
      }

      if (var5 == 4 && var1.isBlockNormalCubeDefault(var2 + 1, var3, var4, true)) {
         var10 = 2;
      }

      if (var5 == 5 && var1.isBlockNormalCubeDefault(var2 - 1, var3, var4, true)) {
         var10 = 1;
      }

      return var10;
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      super.updateTick(var1, var2, var3, var4, var5);
      if (var1.getBlockMetadata(var2, var3, var4) == 0) {
         this.onBlockAdded(var1, var2, var3, var4);
      }
   }

   @Override
   public void onBlockAdded(World var1, int var2, int var3, int var4) {
      if (var1.getBlockMetadata(var2, var3, var4) == 0) {
         if (var1.isBlockNormalCubeDefault(var2 - 1, var3, var4, true)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 1, 2);
         } else if (var1.isBlockNormalCubeDefault(var2 + 1, var3, var4, true)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 2, 2);
         } else if (var1.isBlockNormalCubeDefault(var2, var3, var4 - 1, true)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 3, 2);
         } else if (var1.isBlockNormalCubeDefault(var2, var3, var4 + 1, true)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 4, 2);
         } else if (this.canPlaceTorchOn(var1, var2, var3 - 1, var4)) {
            var1.setBlockMetadataWithNotify(var2, var3, var4, 5, 2);
         }
      }

      this.dropTorchIfCantStay(var1, var2, var3, var4);
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      this.func_94397_d(var1, var2, var3, var4, var5);
   }

   protected boolean func_94397_d(World var1, int var2, int var3, int var4, int var5) {
      if (this.dropTorchIfCantStay(var1, var2, var3, var4)) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         boolean var7 = false;
         if (!var1.isBlockNormalCubeDefault(var2 - 1, var3, var4, true) && var6 == 1) {
            var7 = true;
         }

         if (!var1.isBlockNormalCubeDefault(var2 + 1, var3, var4, true) && var6 == 2) {
            var7 = true;
         }

         if (!var1.isBlockNormalCubeDefault(var2, var3, var4 - 1, true) && var6 == 3) {
            var7 = true;
         }

         if (!var1.isBlockNormalCubeDefault(var2, var3, var4 + 1, true) && var6 == 4) {
            var7 = true;
         }

         if (!this.canPlaceTorchOn(var1, var2, var3 - 1, var4) && var6 == 5) {
            var7 = true;
         }

         if (var7) {
            this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
            var1.setBlockToAir(var2, var3, var4);
            return true;
         } else {
            return false;
         }
      } else {
         return true;
      }
   }

   protected boolean dropTorchIfCantStay(World var1, int var2, int var3, int var4) {
      if (!this.canPlaceBlockAt(var1, var2, var3, var4)) {
         if (var1.getBlockId(var2, var3, var4) == this.blockID) {
            this.c(var1, var2, var3, var4, var1.getBlockMetadata(var2, var3, var4), 0);
            var1.setBlockToAir(var2, var3, var4);
         }

         return false;
      } else {
         return true;
      }
   }

   @Override
   public MovingObjectPosition collisionRayTrace(World var1, int var2, int var3, int var4, Vec3 var5, Vec3 var6) {
      int var7 = var1.getBlockMetadata(var2, var3, var4) & 7;
      float var8 = 0.15F;
      if (var7 == 1) {
         this.a(0.0F, 0.2F, 0.5F - var8, var8 * 2.0F, 0.8F, 0.5F + var8);
      } else if (var7 == 2) {
         this.a(1.0F - var8 * 2.0F, 0.2F, 0.5F - var8, 1.0F, 0.8F, 0.5F + var8);
      } else if (var7 == 3) {
         this.a(0.5F - var8, 0.2F, 0.0F, 0.5F + var8, 0.8F, var8 * 2.0F);
      } else if (var7 == 4) {
         this.a(0.5F - var8, 0.2F, 1.0F - var8 * 2.0F, 0.5F + var8, 0.8F, 1.0F);
      } else {
         var8 = 0.1F;
         this.a(0.5F - var8, 0.0F, 0.5F - var8, 0.5F + var8, 0.6F, 0.5F + var8);
      }

      return super.collisionRayTrace(var1, var2, var3, var4, var5, var6);
   }

   @Override
   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      double var7 = var2 + 0.5F;
      double var9 = var3 + 0.7F;
      double var11 = var4 + 0.5F;
      double var13 = 0.22F;
      double var15 = 0.27F;
      if (var6 == 1) {
         var1.spawnParticle("smoke", var7 - var15, var9 + var13, var11, 0.0, 0.0, 0.0);
         var1.spawnParticle("flame", var7 - var15, var9 + var13, var11, 0.0, 0.0, 0.0);
      } else if (var6 == 2) {
         var1.spawnParticle("smoke", var7 + var15, var9 + var13, var11, 0.0, 0.0, 0.0);
         var1.spawnParticle("flame", var7 + var15, var9 + var13, var11, 0.0, 0.0, 0.0);
      } else if (var6 == 3) {
         var1.spawnParticle("smoke", var7, var9 + var13, var11 - var15, 0.0, 0.0, 0.0);
         var1.spawnParticle("flame", var7, var9 + var13, var11 - var15, 0.0, 0.0, 0.0);
      } else if (var6 == 4) {
         var1.spawnParticle("smoke", var7, var9 + var13, var11 + var15, 0.0, 0.0, 0.0);
         var1.spawnParticle("flame", var7, var9 + var13, var11 + var15, 0.0, 0.0, 0.0);
      } else {
         var1.spawnParticle("smoke", var7, var9, var11, 0.0, 0.0, 0.0);
         var1.spawnParticle("flame", var7, var9, var11, 0.0, 0.0, 0.0);
      }
   }
}
