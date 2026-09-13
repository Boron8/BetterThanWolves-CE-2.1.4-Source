package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneOre extends Block {
   private boolean glowing;

   public BlockRedstoneOre(int var1, boolean var2) {
      super(var1, Material.rock);
      if (var2) {
         this.b(true);
      }

      this.glowing = var2;
   }

   @Override
   public int tickRate(World var1) {
      return 30;
   }

   @Override
   public void onBlockClicked(World var1, int var2, int var3, int var4, EntityPlayer var5) {
      this.glow(var1, var2, var3, var4);
      super.onBlockClicked(var1, var2, var3, var4, var5);
   }

   @Override
   public void onEntityWalking(World var1, int var2, int var3, int var4, Entity var5) {
      this.glow(var1, var2, var3, var4);
      super.onEntityWalking(var1, var2, var3, var4, var5);
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      this.glow(var1, var2, var3, var4);
      return super.onBlockActivated(var1, var2, var3, var4, var5, var6, var7, var8, var9);
   }

   private void glow(World var1, int var2, int var3, int var4) {
      this.sparkle(var1, var2, var3, var4);
      if (this.blockID == Block.oreRedstone.blockID) {
         var1.setBlock(var2, var3, var4, Block.oreRedstoneGlowing.blockID);
      }
   }

   @Override
   public void updateTick(World var1, int var2, int var3, int var4, Random var5) {
      if (this.blockID == Block.oreRedstoneGlowing.blockID) {
         var1.setBlock(var2, var3, var4, Block.oreRedstone.blockID);
      }
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Item.redstone.itemID;
   }

   @Override
   public int quantityDroppedWithBonus(int var1, Random var2) {
      return this.quantityDropped(var2) + var2.nextInt(var1 + 1);
   }

   @Override
   public int quantityDropped(Random var1) {
      return 4 + var1.nextInt(2);
   }

   @Override
   public void dropBlockAsItemWithChance(World var1, int var2, int var3, int var4, int var5, float var6, int var7) {
      super.dropBlockAsItemWithChance(var1, var2, var3, var4, var5, var6, var7);
      if (this.idDropped(var5, var1.rand, var7) != this.blockID) {
         int var8 = 1 + var1.rand.nextInt(5);
         this.j(var1, var2, var3, var4, var8);
      }
   }

   @Override
   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      if (this.glowing) {
         this.sparkle(var1, var2, var3, var4);
      }
   }

   private void sparkle(World var1, int var2, int var3, int var4) {
      Random var5 = var1.rand;
      double var6 = 0.0625;

      for (int var8 = 0; var8 < 6; var8++) {
         double var9 = var2 + var5.nextFloat();
         double var11 = var3 + var5.nextFloat();
         double var13 = var4 + var5.nextFloat();
         if (var8 == 0 && !var1.isBlockOpaqueCube(var2, var3 + 1, var4)) {
            var11 = var3 + 1 + var6;
         }

         if (var8 == 1 && !var1.isBlockOpaqueCube(var2, var3 - 1, var4)) {
            var11 = var3 + 0 - var6;
         }

         if (var8 == 2 && !var1.isBlockOpaqueCube(var2, var3, var4 + 1)) {
            var13 = var4 + 1 + var6;
         }

         if (var8 == 3 && !var1.isBlockOpaqueCube(var2, var3, var4 - 1)) {
            var13 = var4 + 0 - var6;
         }

         if (var8 == 4 && !var1.isBlockOpaqueCube(var2 + 1, var3, var4)) {
            var9 = var2 + 1 + var6;
         }

         if (var8 == 5 && !var1.isBlockOpaqueCube(var2 - 1, var3, var4)) {
            var9 = var2 + 0 - var6;
         }

         if (var9 < var2 || var9 > var2 + 1 || var11 < 0.0 || var11 > var3 + 1 || var13 < var4 || var13 > var4 + 1) {
            var1.spawnParticle("reddust", var9, var11, var13, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   protected ItemStack createStackedBlock(int var1) {
      return new ItemStack(Block.oreRedstone);
   }
}
