package net.minecraft.src;

import java.util.Random;

public class WorldGenTaiga1 extends WorldGenerator {
   @Override
   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      int var6 = par2Random.nextInt(5) + 7;
      int var7 = var6 - par2Random.nextInt(2) - 3;
      int var8 = var6 - var7;
      int var9 = 1 + par2Random.nextInt(var8 + 1);
      boolean var10 = true;
      if (par4 >= 1 && par4 + var6 + 1 <= 128) {
         for (int var11 = par4; var11 <= par4 + 1 + var6 && var10; var11++) {
            boolean var12 = true;
            int var18;
            if (var11 - par4 < var7) {
               var18 = 0;
            } else {
               var18 = var9;
            }

            for (int var13 = par3 - var18; var13 <= par3 + var18 && var10; var13++) {
               for (int var14 = par5 - var18; var14 <= par5 + var18 && var10; var14++) {
                  if (var11 >= 0 && var11 < 128) {
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
            int var181 = par1World.getBlockId(par3, par4 - 1, par5);
            if ((var181 == Block.grass.blockID || var181 == Block.dirt.blockID) && par4 < 128 - var6 - 1) {
               this.a(par1World, par3, par4 - 1, par5, Block.dirt.blockID);
               int var18x = 0;

               for (int var13 = par4 + var6; var13 >= par4 + var7; var13--) {
                  for (int var14x = par3 - var18x; var14x <= par3 + var18x; var14x++) {
                     int var15 = var14x - par3;

                     for (int var16 = par5 - var18x; var16 <= par5 + var18x; var16++) {
                        int var17 = var16 - par5;
                        if ((Math.abs(var15) != var18x || Math.abs(var17) != var18x || var18x <= 0)
                           && !Block.opaqueCubeLookup[par1World.getBlockId(var14x, var13, var16)]) {
                           this.a(par1World, var14x, var13, var16, Block.leaves.blockID, 1);
                        }
                     }
                  }

                  if (var18x >= 1 && var13 == par4 + var7 + 1) {
                     var18x--;
                  } else if (var18x < var9) {
                     var18x++;
                  }
               }

               for (int var20 = 0; var20 < var6 - 1; var20++) {
                  int var14x = par1World.getBlockId(par3, par4 + var20, par5);
                  if (var14x == 0 || var14x == Block.leaves.blockID) {
                     this.a(par1World, par3, par4 + var20, par5, Block.wood.blockID, 1);
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
