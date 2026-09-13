package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class MapGenStronghold extends MapGenStructure {
   private BiomeGenBase[] allowedBiomeGenBases = new BiomeGenBase[]{
      BiomeGenBase.desert,
      BiomeGenBase.forest,
      BiomeGenBase.extremeHills,
      BiomeGenBase.swampland,
      BiomeGenBase.taiga,
      BiomeGenBase.icePlains,
      BiomeGenBase.iceMountains,
      BiomeGenBase.desertHills,
      BiomeGenBase.forestHills,
      BiomeGenBase.extremeHillsEdge,
      BiomeGenBase.jungle,
      BiomeGenBase.jungleHills
   };
   private boolean ranBiomeCheck;
   private ChunkCoordIntPair[] structureCoords = new ChunkCoordIntPair[3];
   private double field_82671_h = 32.0;
   private int field_82672_i = 3;

   public MapGenStronghold() {
   }

   public MapGenStronghold(Map var1) {
      for (Entry var3 : var1.entrySet()) {
         if (((String)var3.getKey()).equals("distance")) {
            this.field_82671_h = MathHelper.func_82713_a((String)var3.getValue(), this.field_82671_h, 1.0);
         } else if (((String)var3.getKey()).equals("count")) {
            this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.structureCoords.length, 1)];
         } else if (((String)var3.getKey()).equals("spread")) {
            this.field_82672_i = MathHelper.parseIntWithDefaultAndMax((String)var3.getValue(), this.field_82672_i, 1);
         }
      }
   }

   @Override
   protected boolean canSpawnStructureAtCoords(int var1, int var2) {
      if (!this.ranBiomeCheck) {
         Random var3 = new Random();
         var3.setSeed(this.worldObj.getSeed());
         double var4 = var3.nextDouble() * Math.PI * 2.0;
         int var6 = 1;

         for (int var7 = 0; var7 < this.structureCoords.length; var7++) {
            double var8 = (1.25 * var6 + var3.nextDouble()) * (this.field_82671_h * var6);
            int var10 = (int)Math.round(Math.cos(var4) * var8);
            int var11 = (int)Math.round(Math.sin(var4) * var8);
            ArrayList var12 = new ArrayList();
            Collections.addAll(var12, this.allowedBiomeGenBases);
            ChunkPosition var13 = this.worldObj.getWorldChunkManager().findBiomePosition((var10 << 4) + 8, (var11 << 4) + 8, 112, var12, var3);
            if (var13 != null) {
               var10 = var13.x >> 4;
               var11 = var13.z >> 4;
            }

            this.structureCoords[var7] = new ChunkCoordIntPair(var10, var11);
            var4 += (Math.PI * 2) * var6 / this.field_82672_i;
            if (var7 == this.field_82672_i) {
               var6 += 2 + var3.nextInt(5);
               this.field_82672_i = this.field_82672_i + 1 + var3.nextInt(2);
            }
         }

         this.ranBiomeCheck = true;
      }

      for (ChunkCoordIntPair var16 : this.structureCoords) {
         if (var1 == var16.chunkXPos && var2 == var16.chunkZPos) {
            return true;
         }
      }

      return false;
   }

   @Override
   protected List getCoordList() {
      ArrayList var1 = new ArrayList();

      for (ChunkCoordIntPair var5 : this.structureCoords) {
         if (var5 != null) {
            var1.add(var5.getChunkPosition(64));
         }
      }

      return var1;
   }

   @Override
   protected StructureStart getStructureStart(int var1, int var2) {
      StructureStrongholdStart var3 = new StructureStrongholdStart(this.worldObj, this.rand, var1, var2);

      while (var3.b().isEmpty() || ((ComponentStrongholdStairs2)var3.b().get(0)).strongholdPortalRoom == null) {
         var3 = new StructureStrongholdStart(this.worldObj, this.rand, var1, var2);
      }

      return var3;
   }
}
