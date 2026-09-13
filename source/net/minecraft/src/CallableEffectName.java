package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableEffectName implements Callable {
   CallableEffectName(EntityLiving var1, PotionEffect var2) {
      this.field_102030_b = var1;
      this.field_102031_a = var2;
   }

   public String func_102029_a() {
      return Potion.potionTypes[this.field_102031_a.getPotionID()].getName();
   }
}
