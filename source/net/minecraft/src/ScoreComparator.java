package net.minecraft.src;

import java.util.Comparator;

final class ScoreComparator implements Comparator {
   public int func_96659_a(Score var1, Score var2) {
      if (var1.func_96652_c() > var2.func_96652_c()) {
         return 1;
      } else {
         return var1.func_96652_c() < var2.func_96652_c() ? -1 : 0;
      }
   }
}
