package net.minecraft.src;

import java.util.Random;

public class WorldGenSpikes extends WorldGenerator {
   private int replaceID;

   public WorldGenSpikes(int par1) {
      this.replaceID = par1;
   }

   @Override
   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      if (par1World.isAirBlock(par3, par4, par5) && par1World.getBlockId(par3, par4 - 1, par5) == this.replaceID) {
         int var6 = par2Random.nextInt(32) + 6;
         int var7 = par2Random.nextInt(4) + 1;

         for (int var8 = par3 - var7; var8 <= par3 + var7; var8++) {
            for (int var9 = par5 - var7; var9 <= par5 + var7; var9++) {
               int var10 = var8 - par3;
               int var11 = var9 - par5;
               if (var10 * var10 + var11 * var11 <= var7 * var7 + 1 && par1World.getBlockId(var8, par4 - 1, var9) != this.replaceID) {
                  return false;
               }
            }
         }

         for (int var131 = par4; var131 < par4 + var6 && var131 < 128; var131++) {
            for (int var9x = par3 - var7; var9x <= par3 + var7; var9x++) {
               for (int var10 = par5 - var7; var10 <= par5 + var7; var10++) {
                  int var11 = var9x - par3;
                  int var12 = var10 - par5;
                  if (var11 * var11 + var12 * var12 <= var7 * var7 + 1) {
                     par1World.setBlock(var9x, var131, var10, Block.obsidian.blockID, 0, 2);
                  }
               }
            }
         }

         EntityEnderCrystal var13 = (EntityEnderCrystal)EntityList.createEntityOfType(EntityEnderCrystal.class, par1World);
         var13.b(par3 + 0.5F, par4 + var6, par5 + 0.5F, par2Random.nextFloat() * 360.0F, 0.0F);
         par1World.spawnEntityInWorld(var13);
         par1World.setBlock(par3, par4 + var6, par5, Block.bedrock.blockID, 0, 2);
         return true;
      } else {
         return false;
      }
   }
}
