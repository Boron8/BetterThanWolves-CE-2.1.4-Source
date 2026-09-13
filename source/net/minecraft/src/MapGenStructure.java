package net.minecraft.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class MapGenStructure extends MapGenBase {
   protected Map structureMap = new HashMap();

   @Override
   protected void recursiveGenerate(World var1, int var2, int var3, int var4, int var5, byte[] var6) {
      if (!this.structureMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(var2, var3))) {
         this.rand.nextInt();

         try {
            if (this.canSpawnStructureAtCoords(var2, var3)) {
               StructureStart var7 = this.getStructureStart(var2, var3);
               this.structureMap.put(ChunkCoordIntPair.chunkXZ2Int(var2, var3), var7);
            }
         } catch (Throwable var10) {
            CrashReport var8 = CrashReport.makeCrashReport(var10, "Exception preparing structure feature");
            CrashReportCategory var9 = var8.makeCategory("Feature being prepared");
            var9.addCrashSectionCallable("Is feature chunk", new CallableIsFeatureChunk(this, var2, var3));
            var9.addCrashSection("Chunk location", String.format("%d,%d", var2, var3));
            var9.addCrashSectionCallable("Chunk pos hash", new CallableChunkPosHash(this, var2, var3));
            var9.addCrashSectionCallable("Structure type", new CallableStructureType(this));
            throw new ReportedException(var8);
         }
      }
   }

   public boolean generateStructuresInChunk(World var1, Random var2, int var3, int var4) {
      int var5 = (var3 << 4) + 8;
      int var6 = (var4 << 4) + 8;
      boolean var7 = false;

      for (StructureStart var9 : this.structureMap.values()) {
         if (var9.isSizeableStructure() && var9.getBoundingBox().intersectsWith(var5, var6, var5 + 15, var6 + 15)) {
            var9.generateStructure(var1, var2, new StructureBoundingBox(var5, var6, var5 + 15, var6 + 15));
            var7 = true;
         }
      }

      return var7;
   }

   public boolean hasStructureAt(int var1, int var2, int var3) {
      for (StructureStart var5 : this.structureMap.values()) {
         if (var5.isSizeableStructure() && var5.getBoundingBox().intersectsWith(var1, var3, var1, var3)) {
            for (StructureComponent var7 : var5.getComponents()) {
               if (var7.getBoundingBox().isVecInside(var1, var2, var3)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public ChunkPosition getNearestInstance(World var1, int var2, int var3, int var4) {
      this.worldObj = var1;
      this.rand.setSeed(var1.getSeed());
      long var5 = this.rand.nextLong();
      long var7 = this.rand.nextLong();
      long var9 = (var2 >> 4) * var5;
      long var11 = (var4 >> 4) * var7;
      this.rand.setSeed(var9 ^ var11 ^ var1.getSeed());
      this.recursiveGenerate(var1, var2 >> 4, var4 >> 4, 0, 0, null);
      double var13 = Double.MAX_VALUE;
      ChunkPosition var15 = null;

      for (StructureStart var17 : this.structureMap.values()) {
         if (var17.isSizeableStructure()) {
            StructureComponent var18 = (StructureComponent)var17.getComponents().get(0);
            ChunkPosition var19 = var18.getCenter();
            int var20 = var19.x - var2;
            int var21 = var19.y - var3;
            int var22 = var19.z - var4;
            double var23 = var20 + var20 * var21 * var21 + var22 * var22;
            if (var23 < var13) {
               var13 = var23;
               var15 = var19;
            }
         }
      }

      if (var15 != null) {
         return var15;
      } else {
         List var25 = this.getCoordList();
         if (var25 != null) {
            ChunkPosition var26 = null;

            for (ChunkPosition var28 : var25) {
               int var29 = var28.x - var2;
               int var30 = var28.y - var3;
               int var31 = var28.z - var4;
               double var32 = var29 + var29 * var30 * var30 + var31 * var31;
               if (var32 < var13) {
                  var13 = var32;
                  var26 = var28;
               }
            }

            return var26;
         } else {
            return null;
         }
      }
   }

   protected List getCoordList() {
      return null;
   }

   protected abstract boolean canSpawnStructureAtCoords(int var1, int var2);

   protected abstract StructureStart getStructureStart(int var1, int var2);
}
