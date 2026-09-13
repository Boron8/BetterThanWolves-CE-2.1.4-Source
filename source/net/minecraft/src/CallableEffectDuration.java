package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableEffectDuration implements Callable {
   CallableEffectDuration(EntityLiving var1, PotionEffect var2) {
      this.field_102036_b = var1;
      this.field_102037_a = var2;
   }

   public String func_102035_a() {
      return this.field_102037_a.getDuration() + "";
   }
}
