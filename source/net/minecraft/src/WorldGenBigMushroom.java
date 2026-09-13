package net.minecraft.src;

import java.util.Random;

public class WorldGenBigMushroom extends WorldGenerator {
   private int mushroomType = -1;

   public WorldGenBigMushroom(int var1) {
      super(true);
      this.mushroomType = var1;
   }

   public WorldGenBigMushroom() {
      super(false);
   }

   @Override
   public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
      int var6 = var2.nextInt(2);
      if (this.mushroomType >= 0) {
         var6 = this.mushroomType;
      }

      int var7 = var2.nextInt(3) + 4;
      boolean var8 = true;
      if (var4 >= 1 && var4 + var7 + 1 < 256) {
         for (int var9 = var4; var9 <= var4 + 1 + var7; var9++) {
            byte var10 = 3;
            if (var9 <= var4 + 3) {
               var10 = 0;
            }

            for (int var11 = var3 - var10; var11 <= var3 + var10 && var8; var11++) {
               for (int var12 = var5 - var10; var12 <= var5 + var10 && var8; var12++) {
                  if (var9 >= 0 && var9 < 256) {
                     int var13 = var1.getBlockId(var11, var9, var12);
                     if (var13 != 0 && var13 != Block.leaves.blockID) {
                        var8 = false;
                     }
                  } else {
                     var8 = false;
                  }
               }
            }
         }

         if (!var8) {
            return false;
         } else {
            int var16 = var1.getBlockId(var3, var4 - 1, var5);
            if (var16 != Block.dirt.blockID && var16 != Block.grass.blockID && var16 != Block.mycelium.blockID) {
               return false;
            } else {
               int var17 = var4 + var7;
               if (var6 == 1) {
                  var17 = var4 + var7 - 3;
               }

               for (int var18 = var17; var18 <= var4 + var7; var18++) {
                  int var20 = 1;
                  if (var18 < var4 + var7) {
                     var20++;
                  }

                  if (var6 == 0) {
                     var20 = 3;
                  }

                  for (int var22 = var3 - var20; var22 <= var3 + var20; var22++) {
                     for (int var14 = var5 - var20; var14 <= var5 + var20; var14++) {
                        int var15 = 5;
                        if (var22 == var3 - var20) {
                           var15--;
                        }

                        if (var22 == var3 + var20) {
                           var15++;
                        }

                        if (var14 == var5 - var20) {
                           var15 -= 3;
                        }

                        if (var14 == var5 + var20) {
                           var15 += 3;
                        }

                        if (var6 == 0 || var18 < var4 + var7) {
                           if ((var22 == var3 - var20 || var22 == var3 + var20) && (var14 == var5 - var20 || var14 == var5 + var20)) {
                              continue;
                           }

                           if (var22 == var3 - (var20 - 1) && var14 == var5 - var20) {
                              var15 = 1;
                           }

                           if (var22 == var3 - var20 && var14 == var5 - (var20 - 1)) {
                              var15 = 1;
                           }

                           if (var22 == var3 + (var20 - 1) && var14 == var5 - var20) {
                              var15 = 3;
                           }

                           if (var22 == var3 + var20 && var14 == var5 - (var20 - 1)) {
                              var15 = 3;
                           }

                           if (var22 == var3 - (var20 - 1) && var14 == var5 + var20) {
                              var15 = 7;
                           }

                           if (var22 == var3 - var20 && var14 == var5 + (var20 - 1)) {
                              var15 = 7;
                           }

                           if (var22 == var3 + (var20 - 1) && var14 == var5 + var20) {
                              var15 = 9;
                           }

                           if (var22 == var3 + var20 && var14 == var5 + (var20 - 1)) {
                              var15 = 9;
                           }
                        }

                        if (var15 == 5 && var18 < var4 + var7) {
                           var15 = 0;
                        }

                        if ((var15 != 0 || var4 >= var4 + var7 - 1) && !Block.opaqueCubeLookup[var1.getBlockId(var22, var18, var14)]) {
                           this.a(var1, var22, var18, var14, Block.mushroomCapBrown.blockID + var6, var15);
                        }
                     }
                  }
               }

               for (int var19 = 0; var19 < var7; var19++) {
                  int var21 = var1.getBlockId(var3, var4 + var19, var5);
                  if (!Block.opaqueCubeLookup[var21]) {
                     this.a(var1, var3, var4 + var19, var5, Block.mushroomCapBrown.blockID + var6, 10);
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }
}
