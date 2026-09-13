package net.minecraft.src;

import java.util.Random;

public class WorldGenForest extends WorldGenerator {
   public WorldGenForest(boolean par1) {
      super(par1);
   }

   @Override
   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      int var6 = par2Random.nextInt(3) + 5;
      boolean var7 = true;
      if (par4 >= 1 && par4 + var6 + 1 <= 256) {
         for (int var8 = par4; var8 <= par4 + 1 + var6; var8++) {
            byte var9 = 1;
            if (var8 == par4) {
               var9 = 0;
            }

            if (var8 >= par4 + 1 + var6 - 2) {
               var9 = 2;
            }

            for (int var10 = par3 - var9; var10 <= par3 + var9 && var7; var10++) {
               for (int var11 = par5 - var9; var11 <= par5 + var9 && var7; var11++) {
                  if (var8 >= 0 && var8 < 256) {
                     int var12 = par1World.getBlockId(var10, var8, var11);
                     if (var12 != 0 && var12 != Block.leaves.blockID) {
                        var7 = false;
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
            int var171 = par1World.getBlockId(par3, par4 - 1, par5);
            if ((var171 == Block.grass.blockID || var171 == Block.dirt.blockID) && par4 < 256 - var6 - 1) {
               this.a(par1World, par3, par4 - 1, par5, Block.dirt.blockID);

               for (int var17x = par4 - 3 + var6; var17x <= par4 + var6; var17x++) {
                  int var10 = var17x - (par4 + var6);
                  int var11x = 1 - var10 / 2;

                  for (int var12 = par3 - var11x; var12 <= par3 + var11x; var12++) {
                     int var13 = var12 - par3;

                     for (int var14 = par5 - var11x; var14 <= par5 + var11x; var14++) {
                        int var15 = var14 - par5;
                        if (Math.abs(var13) != var11x || Math.abs(var15) != var11x || par2Random.nextInt(2) != 0 && var10 != 0) {
                           int var16 = par1World.getBlockId(var12, var17x, var14);
                           if (var16 == 0 || var16 == Block.leaves.blockID) {
                              this.a(par1World, var12, var17x, var14, Block.leaves.blockID, 2);
                           }
                        }
                     }
                  }
               }

               for (int var23 = 0; var23 < var6; var23++) {
                  int var10 = par1World.getBlockId(par3, par4 + var23, par5);
                  if (var10 == 0 || var10 == Block.leaves.blockID) {
                     this.a(par1World, par3, par4 + var23, par5, Block.wood.blockID, 2);
                  }
               }

               if (var6 > 2) {
                  int iTrunkBlockId = par1World.getBlockId(par3, par4, par5);
                  if (iTrunkBlockId == Block.wood.blockID) {
                     int iTrunkMetadata = par1World.getBlockMetadata(par3, par4, par5);
                     if (iTrunkMetadata == 2) {
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
