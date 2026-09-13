package net.minecraft.src;

import java.util.Random;

public class MapGenCaves extends MapGenBase {
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

      boolean var57 = false;
      if (var15 == -1) {
         var15 = var16 / 2;
         var57 = true;
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
         if (!var57 && var15 == var27 && var12 > 1.0F && var16 > 0) {
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

         if (var57 || var25.nextInt(4) != 0) {
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
               int var58 = MathHelper.floor_double(var6 - var29) - var3 * 16 - 1;
               int var36 = MathHelper.floor_double(var6 + var29) - var3 * 16 + 1;
               int var59 = MathHelper.floor_double(var8 - var31) - 1;
               int var38 = MathHelper.floor_double(var8 + var31) + 1;
               int var60 = MathHelper.floor_double(var10 - var29) - var4 * 16 - 1;
               int var40 = MathHelper.floor_double(var10 + var29) - var4 * 16 + 1;
               if (var58 < 0) {
                  var58 = 0;
               }

               if (var36 > 16) {
                  var36 = 16;
               }

               if (var59 < 1) {
                  var59 = 1;
               }

               if (var38 > 120) {
                  var38 = 120;
               }

               if (var60 < 0) {
                  var60 = 0;
               }

               if (var40 > 16) {
                  var40 = 16;
               }

               boolean var61 = false;

               for (int var42 = var58; !var61 && var42 < var36; var42++) {
                  for (int var43 = var60; !var61 && var43 < var40; var43++) {
                     for (int var44 = var38 + 1; !var61 && var44 >= var59 - 1; var44--) {
                        int var45 = (var42 * 16 + var43) * 128 + var44;
                        if (var44 >= 0 && var44 < 128) {
                           if (var5[var45] == Block.waterMoving.blockID || var5[var45] == Block.waterStill.blockID) {
                              var61 = true;
                           }

                           if (var44 != var59 - 1 && var42 != var58 && var42 != var36 - 1 && var43 != var60 && var43 != var40 - 1) {
                              var44 = var59;
                           }
                        }
                     }
                  }
               }

               if (!var61) {
                  for (int var62 = var58; var62 < var36; var62++) {
                     double var63 = (var62 + var3 * 16 + 0.5 - var6) / var29;

                     for (int var64 = var60; var64 < var40; var64++) {
                        double var46 = (var64 + var4 * 16 + 0.5 - var10) / var29;
                        int var48 = (var62 * 16 + var64) * 128 + var38;
                        boolean var49 = false;
                        if (var63 * var63 + var46 * var46 < 1.0) {
                           for (int var50 = var38 - 1; var50 >= var59; var50--) {
                              double var51 = (var50 + 0.5 - var8) / var31;
                              if (var51 > -0.7 && var63 * var63 + var51 * var51 + var46 * var46 < 1.0) {
                                 byte var53 = var5[var48];
                                 if (var53 == Block.grass.blockID) {
                                    var49 = true;
                                 }

                                 if (var53 == Block.stone.blockID || var53 == Block.dirt.blockID || var53 == Block.grass.blockID) {
                                    if (var50 < 10) {
                                       var5[var48] = (byte)Block.lavaMoving.blockID;
                                    } else {
                                       var5[var48] = 0;
                                       if (var49 && var5[var48 - 1] == Block.dirt.blockID) {
                                          var5[var48 - 1] = this.worldObj.getBiomeGenForCoords(var62 + var3 * 16, var64 + var4 * 16).topBlock;
                                       }
                                    }
                                 }
                              }

                              var48--;
                           }
                        }
                     }
                  }

                  if (var57) {
                     break;
                  }
               }
            }
         }
      }
   }

   @Override
   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, byte[] var6) {
      int var7 = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(40) + 1) + 1);
      if (this.rand.nextInt(15) != 0) {
         var7 = 0;
      }

      for (int var8 = 0; var8 < var7; var8++) {
         double var9 = var2 * 16 + this.rand.nextInt(16);
         double var11 = this.rand.nextInt(this.rand.nextInt(120) + 8);
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
            if (this.rand.nextInt(10) == 0) {
               var19 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0F + 1.0F;
            }

            this.generateCaveNode(this.rand.nextLong(), var4, var5, var6, var9, var11, var13, var19, var17, var18, 0, 0, 1.0);
         }
      }
   }
}
