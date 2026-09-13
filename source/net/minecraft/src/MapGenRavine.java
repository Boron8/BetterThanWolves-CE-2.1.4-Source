package net.minecraft.src;

import java.util.Random;

public class MapGenRavine extends MapGenBase {
   private float[] field_75046_d = new float[1024];

   protected void generateRavine(
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
      Random var19 = new Random(var1);
      double var20 = var3 * 16 + 8;
      double var22 = var4 * 16 + 8;
      float var24 = 0.0F;
      float var25 = 0.0F;
      if (var16 <= 0) {
         int var26 = this.range * 16 - 16;
         var16 = var26 - var19.nextInt(var26 / 4);
      }

      boolean var56 = false;
      if (var15 == -1) {
         var15 = var16 / 2;
         var56 = true;
      }

      float var27 = 1.0F;

      for (int var28 = 0; var28 < 128; var28++) {
         if (var28 == 0 || var19.nextInt(3) == 0) {
            var27 = 1.0F + var19.nextFloat() * var19.nextFloat() * 1.0F;
         }

         this.field_75046_d[var28] = var27 * var27;
      }

      for (; var15 < var16; var15++) {
         double var57 = 1.5 + MathHelper.sin(var15 * (float) Math.PI / var16) * var12 * 1.0F;
         double var30 = var57 * var17;
         var57 *= var19.nextFloat() * 0.25 + 0.75;
         var30 *= var19.nextFloat() * 0.25 + 0.75;
         float var32 = MathHelper.cos(var14);
         float var33 = MathHelper.sin(var14);
         var6 += MathHelper.cos(var13) * var32;
         var8 += var33;
         var10 += MathHelper.sin(var13) * var32;
         var14 *= 0.7F;
         var14 += var25 * 0.05F;
         var13 += var24 * 0.05F;
         var25 *= 0.8F;
         var24 *= 0.5F;
         var25 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 2.0F;
         var24 += (var19.nextFloat() - var19.nextFloat()) * var19.nextFloat() * 4.0F;
         if (var56 || var19.nextInt(4) != 0) {
            double var34 = var6 - var20;
            double var36 = var10 - var22;
            double var38 = var16 - var15;
            double var40 = var12 + 2.0F + 16.0F;
            if (var34 * var34 + var36 * var36 - var38 * var38 > var40 * var40) {
               return;
            }

            if (!(var6 < var20 - 16.0 - var57 * 2.0)
               && !(var10 < var22 - 16.0 - var57 * 2.0)
               && !(var6 > var20 + 16.0 + var57 * 2.0)
               && !(var10 > var22 + 16.0 + var57 * 2.0)) {
               int var60 = MathHelper.floor_double(var6 - var57) - var3 * 16 - 1;
               int var35 = MathHelper.floor_double(var6 + var57) - var3 * 16 + 1;
               int var61 = MathHelper.floor_double(var8 - var30) - 1;
               int var37 = MathHelper.floor_double(var8 + var30) + 1;
               int var62 = MathHelper.floor_double(var10 - var57) - var4 * 16 - 1;
               int var39 = MathHelper.floor_double(var10 + var57) - var4 * 16 + 1;
               if (var60 < 0) {
                  var60 = 0;
               }

               if (var35 > 16) {
                  var35 = 16;
               }

               if (var61 < 1) {
                  var61 = 1;
               }

               if (var37 > 120) {
                  var37 = 120;
               }

               if (var62 < 0) {
                  var62 = 0;
               }

               if (var39 > 16) {
                  var39 = 16;
               }

               boolean var63 = false;

               for (int var41 = var60; !var63 && var41 < var35; var41++) {
                  for (int var42 = var62; !var63 && var42 < var39; var42++) {
                     for (int var43 = var37 + 1; !var63 && var43 >= var61 - 1; var43--) {
                        int var44 = (var41 * 16 + var42) * 128 + var43;
                        if (var43 >= 0 && var43 < 128) {
                           if (var5[var44] == Block.waterMoving.blockID || var5[var44] == Block.waterStill.blockID) {
                              var63 = true;
                           }

                           if (var43 != var61 - 1 && var41 != var60 && var41 != var35 - 1 && var42 != var62 && var42 != var39 - 1) {
                              var43 = var61;
                           }
                        }
                     }
                  }
               }

               if (!var63) {
                  for (int var64 = var60; var64 < var35; var64++) {
                     double var65 = (var64 + var3 * 16 + 0.5 - var6) / var57;

                     for (int var66 = var62; var66 < var39; var66++) {
                        double var45 = (var66 + var4 * 16 + 0.5 - var10) / var57;
                        int var47 = (var64 * 16 + var66) * 128 + var37;
                        boolean var48 = false;
                        if (var65 * var65 + var45 * var45 < 1.0) {
                           for (int var49 = var37 - 1; var49 >= var61; var49--) {
                              double var50 = (var49 + 0.5 - var8) / var30;
                              if ((var65 * var65 + var45 * var45) * this.field_75046_d[var49] + var50 * var50 / 6.0 < 1.0) {
                                 byte var52 = var5[var47];
                                 if (var52 == Block.grass.blockID) {
                                    var48 = true;
                                 }

                                 if (var52 == Block.stone.blockID || var52 == Block.dirt.blockID || var52 == Block.grass.blockID) {
                                    if (var49 < 10) {
                                       var5[var47] = (byte)Block.lavaMoving.blockID;
                                    } else {
                                       var5[var47] = 0;
                                       if (var48 && var5[var47 - 1] == Block.dirt.blockID) {
                                          var5[var47 - 1] = this.worldObj.getBiomeGenForCoords(var64 + var3 * 16, var66 + var4 * 16).topBlock;
                                       }
                                    }
                                 }
                              }

                              var47--;
                           }
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
      if (this.rand.nextInt(50) == 0) {
         double var7 = var2 * 16 + this.rand.nextInt(16);
         double var9 = this.rand.nextInt(this.rand.nextInt(40) + 8) + 20;
         double var11 = var3 * 16 + this.rand.nextInt(16);
         byte var13 = 1;

         for (int var14 = 0; var14 < var13; var14++) {
            float var15 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
            float var16 = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
            float var17 = (this.rand.nextFloat() * 2.0F + this.rand.nextFloat()) * 2.0F;
            this.generateRavine(this.rand.nextLong(), var4, var5, var6, var7, var9, var11, var17, var15, var16, 0, 0, 3.0);
         }
      }
   }
}
