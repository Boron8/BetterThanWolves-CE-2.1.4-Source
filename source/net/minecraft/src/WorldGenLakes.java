package net.minecraft.src;

import java.util.Random;

public class WorldGenLakes extends WorldGenerator {
   private int blockIndex;

   public WorldGenLakes(int var1) {
      this.blockIndex = var1;
   }

   @Override
   public boolean generate(World var1, Random var2, int var3, int var4, int var5) {
      var3 -= 8;
      var5 -= 8;

      while (var4 > 5 && var1.isAirBlock(var3, var4, var5)) {
         var4--;
      }

      if (var4 <= 4) {
         return false;
      } else {
         var4 -= 4;
         boolean[] var6 = new boolean[2048];
         int var7 = var2.nextInt(4) + 4;

         for (int var8 = 0; var8 < var7; var8++) {
            double var9 = var2.nextDouble() * 6.0 + 3.0;
            double var11 = var2.nextDouble() * 4.0 + 2.0;
            double var13 = var2.nextDouble() * 6.0 + 3.0;
            double var15 = var2.nextDouble() * (16.0 - var9 - 2.0) + 1.0 + var9 / 2.0;
            double var17 = var2.nextDouble() * (8.0 - var11 - 4.0) + 2.0 + var11 / 2.0;
            double var19 = var2.nextDouble() * (16.0 - var13 - 2.0) + 1.0 + var13 / 2.0;

            for (int var21 = 1; var21 < 15; var21++) {
               for (int var22 = 1; var22 < 15; var22++) {
                  for (int var23 = 1; var23 < 7; var23++) {
                     double var24 = (var21 - var15) / (var9 / 2.0);
                     double var26 = (var23 - var17) / (var11 / 2.0);
                     double var28 = (var22 - var19) / (var13 / 2.0);
                     double var30 = var24 * var24 + var26 * var26 + var28 * var28;
                     if (var30 < 1.0) {
                        var6[(var21 * 16 + var22) * 8 + var23] = true;
                     }
                  }
               }
            }
         }

         for (int var35 = 0; var35 < 16; var35++) {
            for (int var40 = 0; var40 < 16; var40++) {
               for (int var10 = 0; var10 < 8; var10++) {
                  boolean var49 = !var6[(var35 * 16 + var40) * 8 + var10]
                     && (
                        var35 < 15 && var6[((var35 + 1) * 16 + var40) * 8 + var10]
                           || var35 > 0 && var6[((var35 - 1) * 16 + var40) * 8 + var10]
                           || var40 < 15 && var6[(var35 * 16 + var40 + 1) * 8 + var10]
                           || var40 > 0 && var6[(var35 * 16 + (var40 - 1)) * 8 + var10]
                           || var10 < 7 && var6[(var35 * 16 + var40) * 8 + var10 + 1]
                           || var10 > 0 && var6[(var35 * 16 + var40) * 8 + (var10 - 1)]
                     );
                  if (var49) {
                     Material var12 = var1.getBlockMaterial(var3 + var35, var4 + var10, var5 + var40);
                     if (var10 >= 4 && var12.isLiquid()) {
                        return false;
                     }

                     if (var10 < 4 && !var12.isSolid() && var1.getBlockId(var3 + var35, var4 + var10, var5 + var40) != this.blockIndex) {
                        return false;
                     }
                  }
               }
            }
         }

         for (int var36 = 0; var36 < 16; var36++) {
            for (int var41 = 0; var41 < 16; var41++) {
               for (int var45 = 0; var45 < 8; var45++) {
                  if (var6[(var36 * 16 + var41) * 8 + var45]) {
                     var1.setBlock(var3 + var36, var4 + var45, var5 + var41, var45 >= 4 ? 0 : this.blockIndex, 0, 2);
                  }
               }
            }
         }

         for (int var37 = 0; var37 < 16; var37++) {
            for (int var42 = 0; var42 < 16; var42++) {
               for (int var46 = 4; var46 < 8; var46++) {
                  if (var6[(var37 * 16 + var42) * 8 + var46]
                     && var1.getBlockId(var3 + var37, var4 + var46 - 1, var5 + var42) == Block.dirt.blockID
                     && var1.getSavedLightValue(EnumSkyBlock.Sky, var3 + var37, var4 + var46, var5 + var42) > 0) {
                     BiomeGenBase var50 = var1.getBiomeGenForCoords(var3 + var37, var5 + var42);
                     if (var50.topBlock == Block.mycelium.blockID) {
                        var1.setBlock(var3 + var37, var4 + var46 - 1, var5 + var42, Block.mycelium.blockID, 0, 2);
                     } else {
                        var1.setBlock(var3 + var37, var4 + var46 - 1, var5 + var42, Block.grass.blockID, 0, 2);
                     }
                  }
               }
            }
         }

         if (Block.blocksList[this.blockIndex].blockMaterial == Material.lava) {
            for (int var38 = 0; var38 < 16; var38++) {
               for (int var43 = 0; var43 < 16; var43++) {
                  for (int var47 = 0; var47 < 8; var47++) {
                     boolean var51 = !var6[(var38 * 16 + var43) * 8 + var47]
                        && (
                           var38 < 15 && var6[((var38 + 1) * 16 + var43) * 8 + var47]
                              || var38 > 0 && var6[((var38 - 1) * 16 + var43) * 8 + var47]
                              || var43 < 15 && var6[(var38 * 16 + var43 + 1) * 8 + var47]
                              || var43 > 0 && var6[(var38 * 16 + (var43 - 1)) * 8 + var47]
                              || var47 < 7 && var6[(var38 * 16 + var43) * 8 + var47 + 1]
                              || var47 > 0 && var6[(var38 * 16 + var43) * 8 + (var47 - 1)]
                        );
                     if (var51 && (var47 < 4 || var2.nextInt(2) != 0) && var1.getBlockMaterial(var3 + var38, var4 + var47, var5 + var43).isSolid()) {
                        var1.setBlock(var3 + var38, var4 + var47, var5 + var43, Block.stone.blockID, 0, 2);
                     }
                  }
               }
            }
         }

         if (Block.blocksList[this.blockIndex].blockMaterial == Material.water) {
            for (int var39 = 0; var39 < 16; var39++) {
               for (int var44 = 0; var44 < 16; var44++) {
                  byte var48 = 4;
                  if (var1.isBlockFreezable(var3 + var39, var4 + var48, var5 + var44)) {
                     var1.setBlock(var3 + var39, var4 + var48, var5 + var44, Block.ice.blockID, 0, 2);
                  }
               }
            }
         }

         return true;
      }
   }
}
