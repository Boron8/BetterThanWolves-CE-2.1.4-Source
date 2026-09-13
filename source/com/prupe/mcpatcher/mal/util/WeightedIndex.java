package com.prupe.mcpatcher.mal.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public abstract class WeightedIndex {
   private static final long K1 = -5435081209227447693L;
   private static final long KMUL = -7070675565921424023L;
   final int size;

   public static WeightedIndex create(int size) {
      return size <= 0 ? null : new WeightedIndex(size) {
         @Override
         public int choose(long key) {
            return this.mod(key, this.size);
         }

         @Override
         public String toString() {
            return "unweighted";
         }
      };
   }

   public static WeightedIndex create(int size, String weightList) {
      if (size > 0 && weightList != null) {
         final int[] weights = new int[size];
         int sum1 = 0;
         boolean useWeight = false;
         String[] list = weightList.trim().split("\\s+");

         for (int i = 0; i < size; i++) {
            if (i < list.length && list[i].matches("^\\d+$")) {
               weights[i] = Math.max(Integer.parseInt(list[i]), 0);
            } else {
               weights[i] = 1;
            }

            if (i > 0 && weights[i] != weights[0]) {
               useWeight = true;
            }

            sum1 += weights[i];
         }

         if (useWeight && sum1 > 0) {
            final int sum = sum1;
            return new WeightedIndex(size) {
               @Override
               public int choose(long key) {
                  int m = this.mod(key, sum);

                  int index;
                  for (index = 0; index < this.size - 1 && m >= weights[index]; index++) {
                     m -= weights[index];
                  }

                  return index;
               }

               @Override
               public String toString() {
                  StringBuilder sb = new StringBuilder();
                  sb.append("%(");

                  for (int i = 0; i < weights.length; i++) {
                     if (i > 0) {
                        sb.append(", ");
                     }

                     sb.append(String.format("%.1f", 100.0 * weights[i] / sum));
                  }

                  sb.append(")");
                  return sb.toString();
               }
            };
         } else {
            return create(size);
         }
      } else {
         return create(size);
      }
   }

   protected WeightedIndex(int size) {
      this.size = size;
   }

   protected final int mod(long n, int modulus) {
      return (int)((n >> 32 ^ n) & 2147483647L) % modulus;
   }

   public abstract int choose(long var1);

   public static long hash128To64(int i, int j, int k, int l) {
      return hash128To64((long)i << 32 | j & 4294967295L, (long)k << 32 | l & 4294967295L);
   }

   public static long hash128To64(long a, long b) {
      a = shiftMix(a * -5435081209227447693L) * -5435081209227447693L;
      long c = b * -5435081209227447693L + mix128to64(a, b);
      long d = shiftMix(a + b);
      a = mix128to64(a, c);
      b = mix128to64(d, b);
      return a ^ b ^ mix128to64(b, a);
   }

   private static long shiftMix(long val) {
      return val ^ val >>> 47;
   }

   private static long mix128to64(long u, long v) {
      long a = shiftMix((u ^ v) * -7070675565921424023L);
      long b = shiftMix((u ^ a) * -7070675565921424023L);
      return b * -7070675565921424023L;
   }
}
