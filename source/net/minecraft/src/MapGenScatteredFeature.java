package net.minecraft.src;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class MapGenScatteredFeature extends MapGenStructure {
   private static List biomelist = Arrays.asList(
      BiomeGenBase.desert, BiomeGenBase.desertHills, BiomeGenBase.jungle, BiomeGenBase.jungleHills, BiomeGenBase.swampland
   );
   private List scatteredFeatureSpawnList = new ArrayList();
   private int maxDistanceBetweenScatteredFeatures = 32;
   private int minDistanceBetweenScatteredFeatures = 8;

   public MapGenScatteredFeature() {
   }

   public MapGenScatteredFeature(Map par1Map) {
      this();

      for (Entry var3 : par1Map.entrySet()) {
         if (((String)var3.getKey()).equals("distance")) {
            this.maxDistanceBetweenScatteredFeatures = MathHelper.parseIntWithDefaultAndMax(
               (String)var3.getValue(), this.maxDistanceBetweenScatteredFeatures, this.minDistanceBetweenScatteredFeatures + 1
            );
         }
      }
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int par1, int par2) {
      int var3 = par1;
      int var4 = par2;
      if (par1 < 0) {
         par1 -= this.maxDistanceBetweenScatteredFeatures - 1;
      }

      if (par2 < 0) {
         par2 -= this.maxDistanceBetweenScatteredFeatures - 1;
      }

      int var5 = par1 / this.maxDistanceBetweenScatteredFeatures;
      int var6 = par2 / this.maxDistanceBetweenScatteredFeatures;
      Random var7 = this.worldObj.setRandomSeed(var5, var6, 14357617);
      var5 *= this.maxDistanceBetweenScatteredFeatures;
      var6 *= this.maxDistanceBetweenScatteredFeatures;
      var5 += var7.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
      var6 += var7.nextInt(this.maxDistanceBetweenScatteredFeatures - this.minDistanceBetweenScatteredFeatures);
      if (var3 == var5 && var4 == var6) {
         BiomeGenBase var8 = this.worldObj.getWorldChunkManager().getBiomeGenAt(var3 * 16 + 8, var4 * 16 + 8);

         for (BiomeGenBase var10 : biomelist) {
            if (var8 == var10) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   protected StructureStart getStructureStart(int par1, int par2) {
      return new StructureScatteredFeatureStart(this.worldObj, this.rand, par1, par2);
   }

   public List getScatteredFeatureSpawnList() {
      return this.scatteredFeatureSpawnList;
   }
}
