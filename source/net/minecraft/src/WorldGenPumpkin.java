package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.ArrayList;
import java.util.Random;

public class WorldGenPumpkin extends WorldGenerator {
   private static ArrayList<BiomeGenBase> validBiomeList = new ArrayList<>();
   private static final double DIST_FOR_FRESH_PUMPKINS = 2500.0;
   private static final double DIST_SQUARED_FOR_FRESH_PUMPKINS = 6250000.0;

   public static boolean isBiomeValid(BiomeGenBase biome) {
      return validBiomeList.contains(biome);
   }

   public static void addBiomeToGenerator(BiomeGenBase biome) {
      validBiomeList.add(biome);
   }

   @Override
   public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
      boolean bIsValidBiome = isBiomeValid(par1World.getBiomeGenForCoords(par3, par5));
      int iPlacedPumpkinCount = 0;
      boolean bIsFresh = this.checkIfFresh(par1World, par3, par5);

      for (int var6 = 0; var6 < 64; var6++) {
         int var7 = par3 + par2Random.nextInt(8) - par2Random.nextInt(8);
         int var8 = par4 + par2Random.nextInt(4) - par2Random.nextInt(4);
         int var9 = par5 + par2Random.nextInt(8) - par2Random.nextInt(8);
         if (par1World.isAirBlock(var7, var8, var9)
            && par1World.getBlockId(var7, var8 - 1, var9) == Block.grass.blockID
            && Block.pumpkin.canPlaceBlockAt(par1World, var7, var8, var9)) {
            int iFacing = par2Random.nextInt(4);
            if (bIsValidBiome && iPlacedPumpkinCount < 3) {
               if (bIsFresh) {
                  par1World.setBlock(var7, var8, var9, BTWBlocks.freshPumpkin.blockID, iFacing, 2);
               } else {
                  par1World.setBlock(var7, var8, var9, Block.pumpkin.blockID, iFacing, 2);
               }

               iPlacedPumpkinCount++;
            }
         }
      }

      return true;
   }

   public boolean checkIfFresh(World world, int i, int k) {
      int iSpawnX = world.getWorldInfo().getSpawnX();
      int iSpawnZ = world.getWorldInfo().getSpawnZ();
      double dDeltaX = iSpawnX - i;
      double dDeltaZ = iSpawnZ - k;
      double dDistSqFromSpawn = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
      return dDistSqFromSpawn > 6250000.0;
   }
}
