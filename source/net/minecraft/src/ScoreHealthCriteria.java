package net.minecraft.src;

import java.util.List;

public class ScoreHealthCriteria extends ScoreDummyCriteria {
   public ScoreHealthCriteria(String var1) {
      super(var1);
   }

   @Override
   public int func_96635_a(List var1) {
      float var2 = 0.0F;

      for (EntityPlayer var4 : var1) {
         int var5 = var4.aX();
         float var6 = var4.getMaxHealth();
         if (var5 < 0) {
            var5 = 0;
         }

         if (var5 > var6) {
            var5 = var4.getMaxHealth();
         }

         var2 += var5 / var6;
      }

      if (var1.size() > 0) {
         var2 /= var1.size();
      }

      return MathHelper.floor_float(var2 * 19.0F) + (var2 > 0.0F ? 1 : 0);
   }

   @Override
   public boolean isReadOnly() {
      return true;
   }
}
