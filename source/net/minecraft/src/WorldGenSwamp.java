package net.minecraft.src;

import java.util.Random;

public class WorldGenSwamp extends WorldGenerator {
   @Override
   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      int var6 = par2Random.nextInt(4) + 5;

      while (par1World.getBlockMaterial(par3, par4 - 1, par5) == Material.water) {
         par4--;
      }

      boolean var7 = true;
      if (par4 >= 1 && par4 + var6 + 1 <= 128) {
         for (int var8 = par4; var8 <= par4 + 1 + var6; var8++) {
            byte var9 = 1;
            if (var8 == par4) {
               var9 = 0;
            }

            if (var8 >= par4 + 1 + var6 - 2) {
               var9 = 3;
            }

            for (int var10 = par3 - var9; var10 <= par3 + var9 && var7; var10++) {
               for (int var11 = par5 - var9; var11 <= par5 + var9 && var7; var11++) {
                  if (var8 >= 0 && var8 < 128) {
                     int var12 = par1World.getBlockId(var10, var8, var11);
                     if (var12 != 0 && var12 != Block.leaves.blockID) {
                        if (var12 != Block.waterStill.blockID && var12 != Block.waterMoving.blockID) {
                           var7 = false;
                        } else if (var8 > par4) {
                           var7 = false;
                        }
                     }
                  } else {
                     var7 = false;
                  }
               }
            }
         }

         if (!var7) {
            return false;
         } else {
            int var161 = par1World.getBlockId(par3, par4 - 1, par5);
            if ((var161 == Block.grass.blockID || var161 == Block.dirt.blockID) && par4 < 128 - var6 - 1) {
               this.a(par1World, par3, par4 - 1, par5, Block.dirt.blockID);

               for (int var16x = par4 - 3 + var6; var16x <= par4 + var6; var16x++) {
                  int var10 = var16x - (par4 + var6);
                  int var11x = 2 - var10 / 2;

                  for (int var12 = par3 - var11x; var12 <= par3 + var11x; var12++) {
                     int var13 = var12 - par3;

                     for (int var14 = par5 - var11x; var14 <= par5 + var11x; var14++) {
                        int var15 = var14 - par5;
                        if ((Math.abs(var13) != var11x || Math.abs(var15) != var11x || par2Random.nextInt(2) != 0 && var10 != 0)
                           && !Block.opaqueCubeLookup[par1World.getBlockId(var12, var16x, var14)]) {
                           this.a(par1World, var12, var16x, var14, Block.leaves.blockID);
                        }
                     }
                  }
               }

               for (int var26 = 0; var26 < var6; var26++) {
                  int var10 = par1World.getBlockId(par3, par4 + var26, par5);
                  if (var10 == 0 || var10 == Block.leaves.blockID || var10 == Block.waterMoving.blockID || var10 == Block.waterStill.blockID) {
                     this.a(par1World, par3, par4 + var26, par5, Block.wood.blockID);
                  }
               }

               for (int var27 = par4 - 3 + var6; var27 <= par4 + var6; var27++) {
                  int var10 = var27 - (par4 + var6);
                  int var11x = 2 - var10 / 2;

                  for (int var12 = par3 - var11x; var12 <= par3 + var11x; var12++) {
                     for (int var13 = par5 - var11x; var13 <= par5 + var11x; var13++) {
                        if (par1World.getBlockId(var12, var27, var13) == Block.leaves.blockID) {
                           if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var12 - 1, var27, var13) == 0) {
                              this.generateVines(par1World, var12 - 1, var27, var13, 8);
                           }

                           if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var12 + 1, var27, var13) == 0) {
                              this.generateVines(par1World, var12 + 1, var27, var13, 2);
                           }

                           if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var12, var27, var13 - 1) == 0) {
                              this.generateVines(par1World, var12, var27, var13 - 1, 1);
                           }

                           if (par2Random.nextInt(4) == 0 && par1World.getBlockId(var12, var27, var13 + 1) == 0) {
                              this.generateVines(par1World, var12, var27, var13 + 1, 4);
                           }
                        }
                     }
                  }
               }

               if (var6 > 2) {
                  int iTrunkBlockId = par1World.getBlockId(par3, par4, par5);
                  if (iTrunkBlockId == Block.wood.blockID) {
                     int iTrunkMetadata = par1World.getBlockMetadata(par3, par4, par5);
                     if (iTrunkMetadata == 0) {
                        par1World.setBlockMetadataWithClient(par3, par4, par5, iTrunkMetadata | 12);
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   private void generateVines(World par1World, int par2, int par3, int par4, int par5) {
      this.a(par1World, par2, par3, par4, Block.vine.blockID, par5);

      for (int var6 = 4; par1World.getBlockId(par2, --par3, par4) == 0 && var6 > 0; var6--) {
         this.a(par1World, par2, par3, par4, Block.vine.blockID, par5);
      }
   }
}
