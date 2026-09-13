package net.minecraft.src;

public class GenLayerShore extends GenLayer {
   public GenLayerShore(long var1, GenLayer var3) {
      super(var1);
      this.parent = var3;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int[] var5 = this.parent.getInts(var1 - 1, var2 - 1, var3 + 2, var4 + 2);
      int[] var6 = IntCache.getIntCache(var3 * var4);

      for (int var7 = 0; var7 < var4; var7++) {
         for (int var8 = 0; var8 < var3; var8++) {
            this.a(var8 + var1, var7 + var2);
            int var9 = var5[var8 + 1 + (var7 + 1) * (var3 + 2)];
            if (var9 == BiomeGenBase.mushroomIsland.biomeID) {
               int var10 = var5[var8 + 1 + (var7 + 1 - 1) * (var3 + 2)];
               int var11 = var5[var8 + 1 + 1 + (var7 + 1) * (var3 + 2)];
               int var12 = var5[var8 + 1 - 1 + (var7 + 1) * (var3 + 2)];
               int var13 = var5[var8 + 1 + (var7 + 1 + 1) * (var3 + 2)];
               if (var10 != BiomeGenBase.ocean.biomeID
                  && var11 != BiomeGenBase.ocean.biomeID
                  && var12 != BiomeGenBase.ocean.biomeID
                  && var13 != BiomeGenBase.ocean.biomeID) {
                  var6[var8 + var7 * var3] = var9;
               } else {
                  var6[var8 + var7 * var3] = BiomeGenBase.mushroomIslandShore.biomeID;
               }
            } else if (var9 != BiomeGenBase.ocean.biomeID
               && var9 != BiomeGenBase.river.biomeID
               && var9 != BiomeGenBase.swampland.biomeID
               && var9 != BiomeGenBase.extremeHills.biomeID) {
               int var15 = var5[var8 + 1 + (var7 + 1 - 1) * (var3 + 2)];
               int var17 = var5[var8 + 1 + 1 + (var7 + 1) * (var3 + 2)];
               int var19 = var5[var8 + 1 - 1 + (var7 + 1) * (var3 + 2)];
               int var21 = var5[var8 + 1 + (var7 + 1 + 1) * (var3 + 2)];
               if (var15 != BiomeGenBase.ocean.biomeID
                  && var17 != BiomeGenBase.ocean.biomeID
                  && var19 != BiomeGenBase.ocean.biomeID
                  && var21 != BiomeGenBase.ocean.biomeID) {
                  var6[var8 + var7 * var3] = var9;
               } else {
                  var6[var8 + var7 * var3] = BiomeGenBase.beach.biomeID;
               }
            } else if (var9 == BiomeGenBase.extremeHills.biomeID) {
               int var14 = var5[var8 + 1 + (var7 + 1 - 1) * (var3 + 2)];
               int var16 = var5[var8 + 1 + 1 + (var7 + 1) * (var3 + 2)];
               int var18 = var5[var8 + 1 - 1 + (var7 + 1) * (var3 + 2)];
               int var20 = var5[var8 + 1 + (var7 + 1 + 1) * (var3 + 2)];
               if (var14 == BiomeGenBase.extremeHills.biomeID
                  && var16 == BiomeGenBase.extremeHills.biomeID
                  && var18 == BiomeGenBase.extremeHills.biomeID
                  && var20 == BiomeGenBase.extremeHills.biomeID) {
                  var6[var8 + var7 * var3] = var9;
               } else {
                  var6[var8 + var7 * var3] = BiomeGenBase.extremeHillsEdge.biomeID;
               }
            } else {
               var6[var8 + var7 * var3] = var9;
            }
         }
      }

      return var6;
   }
}
