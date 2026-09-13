package net.minecraft.src;

public class GenLayerFuzzyZoom extends GenLayer {
   public GenLayerFuzzyZoom(long var1, GenLayer var3) {
      super(var1);
      super.parent = var3;
   }

   @Override
   public int[] getInts(int var1, int var2, int var3, int var4) {
      int var5 = var1 >> 1;
      int var6 = var2 >> 1;
      int var7 = (var3 >> 1) + 3;
      int var8 = (var4 >> 1) + 3;
      int[] var9 = this.parent.getInts(var5, var6, var7, var8);
      int[] var10 = IntCache.getIntCache(var7 * 2 * var8 * 2);
      int var11 = var7 << 1;

      for (int var12 = 0; var12 < var8 - 1; var12++) {
         int var13 = var12 << 1;
         int var14 = var13 * var11;
         int var15 = var9[0 + (var12 + 0) * var7];
         int var16 = var9[0 + (var12 + 1) * var7];

         for (int var17 = 0; var17 < var7 - 1; var17++) {
            this.a(var17 + var5 << 1, var12 + var6 << 1);
            int var18 = var9[var17 + 1 + (var12 + 0) * var7];
            int var19 = var9[var17 + 1 + (var12 + 1) * var7];
            var10[var14] = var15;
            var10[var14++ + var11] = this.choose(var15, var16);
            var10[var14] = this.choose(var15, var18);
            var10[var14++ + var11] = this.choose(var15, var18, var16, var19);
            var15 = var18;
            var16 = var19;
         }
      }

      int[] var20 = IntCache.getIntCache(var3 * var4);

      for (int var21 = 0; var21 < var4; var21++) {
         System.arraycopy(var10, (var21 + (var2 & 1)) * (var7 << 1) + (var1 & 1), var20, var21 * var3, var3);
      }

      return var20;
   }

   protected int choose(int var1, int var2) {
      return this.a(2) == 0 ? var1 : var2;
   }

   protected int choose(int var1, int var2, int var3, int var4) {
      int var5 = this.a(4);
      if (var5 == 0) {
         return var1;
      } else if (var5 == 1) {
         return var2;
      } else {
         return var5 == 2 ? var3 : var4;
      }
   }
}
