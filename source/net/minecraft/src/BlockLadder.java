package net.minecraft.src;

import java.util.Random;

public class BlockLadder extends Block {
   protected BlockLadder(int var1) {
      super(var1, Material.circuits);
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.getCollisionBoundingBoxFromPool(var1, var2, var3, var4);
   }

   @Override
   public AxisAlignedBB getSelectedBoundingBoxFromPool(World var1, int var2, int var3, int var4) {
      this.setBlockBoundsBasedOnState(var1, var2, var3, var4);
      return super.getSelectedBoundingBoxFromPool(var1, var2, var3, var4);
   }

   @Override
   public void setBlockBoundsBasedOnState(IBlockAccess var1, int var2, int var3, int var4) {
      this.updateLadderBounds(var1.getBlockMetadata(var2, var3, var4));
   }

   public void updateLadderBounds(int var1) {
      float var3 = 0.125F;
      if (var1 == 2) {
         this.a(0.0F, 0.0F, 1.0F - var3, 1.0F, 1.0F, 1.0F);
      }

      if (var1 == 3) {
         this.a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, var3);
      }

      if (var1 == 4) {
         this.a(1.0F - var3, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
      }

      if (var1 == 5) {
         this.a(0.0F, 0.0F, 0.0F, var3, 1.0F, 1.0F);
      }
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
      return 8;
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
      int var10 = var9;
      if ((var9 == 0 || var5 == 2) && var1.isBlockNormalCube(var2, var3, var4 + 1)) {
         var10 = 2;
      }

      if ((var10 == 0 || var5 == 3) && var1.isBlockNormalCube(var2, var3, var4 - 1)) {
         var10 = 3;
      }

      if ((var10 == 0 || var5 == 4) && var1.isBlockNormalCube(var2 + 1, var3, var4)) {
         var10 = 4;
      }

      if ((var10 == 0 || var5 == 5) && var1.isBlockNormalCube(var2 - 1, var3, var4)) {
         var10 = 5;
      }

      return var10;
   }

   @Override
   public void onNeighborBlockChange(World var1, int var2, int var3, int var4, int var5) {
      int var6 = var1.getBlockMetadata(var2, var3, var4);
      boolean var7 = false;
      if (var6 == 2 && var1.isBlockNormalCube(var2, var3, var4 + 1)) {
         var7 = true;
      }

      if (var6 == 3 && var1.isBlockNormalCube(var2, var3, var4 - 1)) {
         var7 = true;
      }

      if (var6 == 4 && var1.isBlockNormalCube(var2 + 1, var3, var4)) {
         var7 = true;
      }

      if (var6 == 5 && var1.isBlockNormalCube(var2 - 1, var3, var4)) {
         var7 = true;
      }

      if (!var7) {
         this.c(var1, var2, var3, var4, var6, 0);
         var1.setBlockToAir(var2, var3, var4);
      }

      super.onNeighborBlockChange(var1, var2, var3, var4, var5);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 1;
   }
}
