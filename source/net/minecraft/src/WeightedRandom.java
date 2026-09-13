package net.minecraft.src;

import java.util.Collection;
import java.util.Random;

public class WeightedRandom {
   public static int getTotalWeight(Collection var0) {
      int var1 = 0;

      for (WeightedRandomItem var3 : var0) {
         var1 += var3.itemWeight;
      }

      return var1;
   }

   public static WeightedRandomItem getRandomItem(Random var0, Collection var1, int var2) {
      if (var2 <= 0) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.nextInt(var2);

         for (WeightedRandomItem var5 : var1) {
            var3 -= var5.itemWeight;
            if (var3 < 0) {
               return var5;
            }
         }

         return null;
      }
   }

   public static WeightedRandomItem getRandomItem(Random var0, Collection var1) {
      return getRandomItem(var0, var1, getTotalWeight(var1));
   }

   public static int getTotalWeight(WeightedRandomItem[] var0) {
      int var1 = 0;

      for (WeightedRandomItem var5 : var0) {
         var1 += var5.itemWeight;
      }

      return var1;
   }

   public static WeightedRandomItem getRandomItem(Random var0, WeightedRandomItem[] var1, int var2) {
      if (var2 <= 0) {
         throw new IllegalArgumentException();
      } else {
         int var3 = var0.nextInt(var2);

         for (WeightedRandomItem var7 : var1) {
            var3 -= var7.itemWeight;
            if (var3 < 0) {
               return var7;
            }
         }

         return null;
      }
   }

   public static WeightedRandomItem getRandomItem(Random var0, WeightedRandomItem[] var1) {
      return getRandomItem(var0, var1, getTotalWeight(var1));
   }
}
