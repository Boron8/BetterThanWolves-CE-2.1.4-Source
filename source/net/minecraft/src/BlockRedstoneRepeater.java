package net.minecraft.src;

import java.util.Random;

public class BlockRedstoneRepeater extends BlockRedstoneLogic {
   public static final double[] repeaterTorchOffset = new double[]{-0.0625, 0.0625, 0.1875, 0.3125};
   private static final int[] repeaterState = new int[]{1, 2, 3, 4};

   protected BlockRedstoneRepeater(int var1, boolean var2) {
      super(var1, var2);
   }

   @Override
   public boolean onBlockActivated(World var1, int var2, int var3, int var4, EntityPlayer var5, int var6, float var7, float var8, float var9) {
      int var10 = var1.getBlockMetadata(var2, var3, var4);
      int var11 = (var10 & 12) >> 2;
      var11 = var11 + 1 << 2 & 12;
      var1.setBlockMetadataWithNotify(var2, var3, var4, var11 | var10 & 3, 3);
      return true;
   }

   @Override
   protected int func_94481_j_(int var1) {
      return repeaterState[(var1 & 12) >> 2] * 2;
   }

   @Override
   protected BlockRedstoneLogic func_94485_e() {
      return Block.redstoneRepeaterActive;
   }

   @Override
   protected BlockRedstoneLogic func_94484_i() {
      return Block.redstoneRepeaterIdle;
   }

   @Override
   public int idDropped(int var1, Random var2, int var3) {
      return Item.redstoneRepeater.itemID;
   }

   @Override
   public int idPicked(World var1, int var2, int var3, int var4) {
      return Item.redstoneRepeater.itemID;
   }

   @Override
   public int getRenderType() {
      return 15;
   }

   @Override
   public boolean func_94476_e(IBlockAccess var1, int var2, int var3, int var4, int var5) {
      return this.f(var1, var2, var3, var4, var5) > 0;
   }

   @Override
   protected boolean func_94477_d(int var1) {
      return f(var1);
   }

   @Override
   public void randomDisplayTick(World var1, int var2, int var3, int var4, Random var5) {
      if (this.isRepeaterPowered) {
         int var6 = var1.getBlockMetadata(var2, var3, var4);
         int var7 = j(var6);
         double var8 = var2 + 0.5F + (var5.nextFloat() - 0.5F) * 0.2;
         double var10 = var3 + 0.4F + (var5.nextFloat() - 0.5F) * 0.2;
         double var12 = var4 + 0.5F + (var5.nextFloat() - 0.5F) * 0.2;
         double var14 = 0.0;
         double var16 = 0.0;
         if (var5.nextInt(2) == 0) {
            switch (var7) {
               case 0:
                  var16 = -0.3125;
                  break;
               case 1:
                  var14 = 0.3125;
                  break;
               case 2:
                  var16 = 0.3125;
                  break;
               case 3:
                  var14 = -0.3125;
            }
         } else {
            int var18 = (var6 & 12) >> 2;
            switch (var7) {
               case 0:
                  var16 = repeaterTorchOffset[var18];
                  break;
               case 1:
                  var14 = -repeaterTorchOffset[var18];
                  break;
               case 2:
                  var16 = -repeaterTorchOffset[var18];
                  break;
               case 3:
                  var14 = repeaterTorchOffset[var18];
            }
         }

         var1.spawnParticle("reddust", var8 + var14, var10, var12 + var16, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public void breakBlock(World var1, int var2, int var3, int var4, int var5, int var6) {
      super.a(var1, var2, var3, var4, var5, var6);
      this.h_(var1, var2, var3, var4);
   }
}
