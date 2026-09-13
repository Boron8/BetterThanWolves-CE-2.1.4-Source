package net.minecraft.src;

public abstract class GenLayer {
   private long worldGenSeed;
   protected GenLayer parent;
   private long chunkSeed;
   private long baseSeed;

   public static GenLayer[] initializeAllBiomeGenerators(long var0, WorldType var2) {
      GenLayerIsland var3 = new GenLayerIsland(1L);
      GenLayerFuzzyZoom var9 = new GenLayerFuzzyZoom(2000L, var3);
      GenLayerAddIsland var10 = new GenLayerAddIsland(1L, var9);
      GenLayerZoom var11 = new GenLayerZoom(2001L, var10);
      GenLayerAddIsland var12 = new GenLayerAddIsland(2L, var11);
      GenLayerAddSnow var13 = new GenLayerAddSnow(2L, var12);
      GenLayerZoom var14 = new GenLayerZoom(2002L, var13);
      GenLayerAddIsland var15 = new GenLayerAddIsland(3L, var14);
      GenLayerZoom var16 = new GenLayerZoom(2003L, var15);
      GenLayerAddIsland var17 = new GenLayerAddIsland(4L, var16);
      GenLayerAddMushroomIsland var18 = new GenLayerAddMushroomIsland(5L, var17);
      byte var4 = 4;
      if (var2 == WorldType.LARGE_BIOMES) {
         var4 = 6;
      }

      GenLayer var5 = GenLayerZoom.magnify(1000L, var18, 0);
      GenLayerRiverInit var19 = new GenLayerRiverInit(100L, var5);
      var5 = GenLayerZoom.magnify(1000L, var19, var4 + 2);
      GenLayerRiver var21 = new GenLayerRiver(1L, var5);
      GenLayerSmooth var22 = new GenLayerSmooth(1000L, var21);
      GenLayer var6 = GenLayerZoom.magnify(1000L, var18, 0);
      GenLayerBiome var23 = new GenLayerBiome(200L, var6, var2);
      var6 = GenLayerZoom.magnify(1000L, var23, 2);
      var6 = new GenLayerHills(1000L, var6);

      for (int var7 = 0; var7 < var4; var7++) {
         var6 = new GenLayerZoom(1000 + var7, var6);
         if (var7 == 0) {
            var6 = new GenLayerAddIsland(3L, var6);
         }

         if (var7 == 1) {
            var6 = new GenLayerShore(1000L, var6);
         }

         if (var7 == 1) {
            var6 = new GenLayerSwampRivers(1000L, var6);
         }
      }

      GenLayerSmooth var26 = new GenLayerSmooth(1000L, var6);
      GenLayerRiverMix var27 = new GenLayerRiverMix(100L, var26, var22);
      GenLayerVoronoiZoom var8 = new GenLayerVoronoiZoom(10L, var27);
      var27.initWorldGenSeed(var0);
      var8.initWorldGenSeed(var0);
      return new GenLayer[]{var27, var8, var27};
   }

   public GenLayer(long var1) {
      this.baseSeed = var1;
      this.baseSeed = this.baseSeed * (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
      this.baseSeed += var1;
      this.baseSeed = this.baseSeed * (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
      this.baseSeed += var1;
      this.baseSeed = this.baseSeed * (this.baseSeed * 6364136223846793005L + 1442695040888963407L);
      this.baseSeed += var1;
   }

   public void initWorldGenSeed(long var1) {
      this.worldGenSeed = var1;
      if (this.parent != null) {
         this.parent.initWorldGenSeed(var1);
      }

      this.worldGenSeed = this.worldGenSeed * (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
      this.worldGenSeed = this.worldGenSeed + this.baseSeed;
      this.worldGenSeed = this.worldGenSeed * (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
      this.worldGenSeed = this.worldGenSeed + this.baseSeed;
      this.worldGenSeed = this.worldGenSeed * (this.worldGenSeed * 6364136223846793005L + 1442695040888963407L);
      this.worldGenSeed = this.worldGenSeed + this.baseSeed;
   }

   public void initChunkSeed(long var1, long var3) {
      this.chunkSeed = this.worldGenSeed;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += var1;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += var3;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += var1;
      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed += var3;
   }

   protected int nextInt(int var1) {
      int var2 = (int)((this.chunkSeed >> 24) % var1);
      if (var2 < 0) {
         var2 += var1;
      }

      this.chunkSeed = this.chunkSeed * (this.chunkSeed * 6364136223846793005L + 1442695040888963407L);
      this.chunkSeed = this.chunkSeed + this.worldGenSeed;
      return var2;
   }

   public abstract int[] getInts(int var1, int var2, int var3, int var4);
}
