package net.minecraft.src;

import java.util.Random;

public class WorldGenTaiga2 extends WorldGenerator {
   public WorldGenTaiga2(boolean par1) {
      super(par1);
   }

   @Override
   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      int var6 = par2Random.nextInt(4) + 6;
      int var7 = 1 + par2Random.nextInt(2);
      int var8 = var6 - var7;
      int var9 = 2 + par2Random.nextInt(2);
      boolean var10 = true;
      if (par4 >= 1 && par4 + var6 + 1 <= 256) {
         for (int var11 = par4; var11 <= par4 + 1 + var6 && var10; var11++) {
            boolean var12 = true;
            int var21;
            if (var11 - par4 < var7) {
               var21 = 0;
            } else {
               var21 = var9;
            }

            for (int var13 = par3 - var21; var13 <= par3 + var21 && var10; var13++) {
               for (int var14 = par5 - var21; var14 <= par5 + var21 && var10; var14++) {
                  if (var11 >= 0 && var11 < 256) {
                     int var15 = par1World.getBlockId(var13, var11, var14);
                     if (var15 != 0 && var15 != Block.leaves.blockID) {
                        var10 = false;
                     }
                  } else {
                     var10 = false;
                  }
               }
            }
         }

         if (!var10) {
            return false;
         } else {
            int var211 = par1World.getBlockId(par3, par4 - 1, par5);
            if ((var211 == Block.grass.blockID || var211 == Block.dirt.blockID) && par4 < 256 - var6 - 1) {
               this.a(par1World, par3, par4 - 1, par5, Block.dirt.blockID);
               int var21x = par2Random.nextInt(2);
               int var13 = 1;
               byte var22 = 0;

               for (int var15 = 0; var15 <= var8; var15++) {
                  int var16 = par4 + var6 - var15;

                  for (int var17 = par3 - var21x; var17 <= par3 + var21x; var17++) {
                     int var18 = var17 - par3;

                     for (int var19 = par5 - var21x; var19 <= par5 + var21x; var19++) {
                        int var20 = var19 - par5;
                        if ((Math.abs(var18) != var21x || Math.abs(var20) != var21x || var21x <= 0)
                           && !Block.opaqueCubeLookup[par1World.getBlockId(var17, var16, var19)]) {
                           this.a(par1World, var17, var16, var19, Block.leaves.blockID, 1);
                        }
                     }
                  }

                  if (var21x >= var13) {
                     var21x = var22;
                     var22 = 1;
                     if (++var13 > var9) {
                        var13 = var9;
                     }
                  } else {
                     var21x++;
                  }
               }

               int var24 = par2Random.nextInt(3);

               for (int var16 = 0; var16 < var6 - var24; var16++) {
                  int var17 = par1World.getBlockId(par3, par4 + var16, par5);
                  if (var17 == 0 || var17 == Block.leaves.blockID) {
                     this.a(par1World, par3, par4 + var16, par5, Block.wood.blockID, 1);
                  }
               }

               if (var6 > 2) {
                  int iTrunkBlockId = par1World.getBlockId(par3, par4, par5);
                  if (iTrunkBlockId == Block.wood.blockID) {
                     int iTrunkMetadata = par1World.getBlockMetadata(par3, par4, par5);
                     if (iTrunkMetadata == 1) {
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
}
