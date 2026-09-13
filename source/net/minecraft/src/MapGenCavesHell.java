package net.minecraft.src;

import java.util.Random;

public class MapGenCavesHell extends MapGenBase {
   protected void generateLargeCaveNode(long var1, int var3, int var4, byte[] var5, double var6, double var8, double var10) {
      this.generateCaveNode(var1, var3, var4, var5, var6, var8, var10, 1.0F + this.rand.nextFloat() * 6.0F, 0.0F, 0.0F, -1, -1, 0.5);
   }

   protected void generateCaveNode(
      long var1,
      int var3,
      int var4,
      byte[] var5,
      double var6,
      double var8,
      double var10,
      float var12,
      float var13,
      float var14,
      int var15,
      int var16,
      double var17
   ) {
      double var19 = var3 * 16 + 8;
      double var21 = var4 * 16 + 8;
      float var23 = 0.0F;
      float var24 = 0.0F;
      Random var25 = new Random(var1);
      if (var16 <= 0) {
         int var26 = this.range * 16 - 16;
         var16 = var26 - var25.nextInt(var26 / 4);
      }

      boolean var56 = false;
      if (var15 == -1) {
         var15 = var16 / 2;
         var56 = true;
      }

      int var27 = var25.nextInt(var16 / 2) + var16 / 4;

      for (boolean var28 = var25.nextInt(6) == 0; var15 < var16; var15++) {
         double var29 = 1.5 + MathHelper.sin(var15 * (float) Math.PI / var16) * var12 * 1.0F;
         double var31 = var29 * var17;
         float var33 = MathHelper.cos(var14);
         float var34 = MathHelper.sin(var14);
         var6 += MathHelper.cos(var13) * var33;
         var8 += var34;
         var10 += MathHelper.sin(var13) * var33;
         if (var28) {
            var14 *= 0.92F;
         } else {
            var14 *= 0.7F;
         }

         var14 += var24 * 0.1F;
         var13 += var23 * 0.1F;
         var24 *= 0.9F;
         var23 *= 0.75F;
         var24 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 2.0F;
         var23 += (var25.nextFloat() - var25.nextFloat()) * var25.nextFloat() * 4.0F;
         if (!var56 && var15 == var27 && var12 > 1.0F) {
            this.generateCaveNode(
               var25.nextLong(),
               var3,
               var4,
               var5,
               var6,
               var8,
               var10,
               var25.nextFloat() * 0.5F + 0.5F,
               var13 - (float) (Math.PI / 2),
               var14 / 3.0F,
               var15,
               var16,
               1.0
            );
            this.generateCaveNode(
               var25.nextLong(),
               var3,
               var4,
               var5,
               var6,
               var8,
               var10,
               var25.nextFloat() * 0.5F + 0.5F,
               var13 + (float) (Math.PI / 2),
               var14 / 3.0F,
               var15,
               var16,
               1.0
            );
            return;
         }

         if (var56 || var25.nextInt(4) != 0) {
            double var35 = var6 - var19;
            double var37 = var10 - var21;
            double var39 = var16 - var15;
            double var41 = var12 + 2.0F + 16.0F;
            if (var35 * var35 + var37 * var37 - var39 * var39 > var41 * var41) {
               return;
            }

            if (!(var6 < var19 - 16.0 - var29 * 2.0)
               && !(var10 < var21 - 16.0 - var29 * 2.0)
               && !(var6 > var19 + 16.0 + var29 * 2.0)
               && !(var10 > var21 + 16.0 + var29 * 2.0)) {
               int var57 = MathHelper.floor_double(var6 - var29) - var3 * 16 - 1;
               int var36 = MathHelper.floor_double(var6 + var29) - var3 * 16 + 1;
               int var58 = MathHelper.floor_double(var8 - var31) - 1;
               int var38 = MathHelper.floor_double(var8 + var31) + 1;
               int var59 = MathHelper.floor_double(var10 - var29) - var4 * 16 - 1;
               int var40 = MathHelper.floor_double(var10 + var29) - var4 * 16 + 1;
               if (var57 < 0) {
                  var57 = 0;
               }

               if (var36 > 16) {
                  var36 = 16;
               }

               if (var58 < 1) {
                  var58 = 1;
               }

               if (var38 > 120) {
                  var38 = 120;
               }

               if (var59 < 0) {
                  var59 = 0;
               }

               if (var40 > 16) {
                  var40 = 16;
               }

               boolean var60 = false;

               for (int var42 = var57; !var60 && var42 < var36; var42++) {
                  for (int var43 = var59; !var60 && var43 < var40; var43++) {
                     for (int var44 = var38 + 1; !var60 && var44 >= var58 - 1; var44--) {
                        int var45 = (var42 * 16 + var43) * 128 + var44;
                        if (var44 >= 0 && var44 < 128) {
                           if (var5[var45] == Block.lavaMoving.blockID || var5[var45] == Block.lavaStill.blockID) {
                              var60 = true;
                           }

                           if (var44 != var58 - 1 && var42 != var57 && var42 != var36 - 1 && var43 != var59 && var43 != var40 - 1) {
                              var44 = var58;
                           }
                        }
                     }
                  }
               }

               if (!var60) {
                  for (int var61 = var57; var61 < var36; var61++) {
                     double var62 = (var61 + var3 * 16 + 0.5 - var6) / var29;

                     for (int var63 = var59; var63 < var40; var63++) {
                        double var46 = (var63 + var4 * 16 + 0.5 - var10) / var29;
                        int var48 = (var61 * 16 + var63) * 128 + var38;

                        for (int var49 = var38 - 1; var49 >= var58; var49--) {
                           double var50 = (var49 + 0.5 - var8) / var31;
                           if (var50 > -0.7 && var62 * var62 + var50 * var50 + var46 * var46 < 1.0) {
                              byte var52 = var5[var48];
                              if (var52 == Block.netherrack.blockID || var52 == Block.dirt.blockID || var52 == Block.grass.blockID) {
                                 var5[var48] = 0;
                              }
                           }

                           var48--;
                        }
                     }
                  }

                  if (var56) {
                     break;
                  }
               }
            }
         }
      }
   }

   @Override
   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, byte[] var6) {
      int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(10) + 1) + 1);
      if (this.rand.nextInt(5) != 0) {
         var7 = 0;
      }

      for (int var8 = 0; var8 < var7; var8++) {
         double var9 = var2 * 16 + this.rand.nextInt(16);
         double var11 = this.rand.nextInt(128);
         double var13 = var3 * 16 + this.rand.nextInt(16);
         int var15 = 1;
         if (this.rand.nextInt(4) == 0) {
            this.generateLargeCaveNode(this.rand.nextLong(), var4, var5, var6, var9, var11, var13);
            var15 += this.rand.nextInt(4);
         }

         for (int var16 = 0; var16 < var15; var16++) {
            float var17 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
            float var18 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
            float var19 = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();
            this.generateCaveNode(this.rand.nextLong(), var4, var5, var6, var9, var11, var13, var19 * 2.0F, var17, var18, 0, 0, 0.5);
         }
      }
   }
}
