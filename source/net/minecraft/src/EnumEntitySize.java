package net.minecraft.src;

public enum EnumEntitySize {
   SIZE_1,
   SIZE_2,
   SIZE_3,
   SIZE_4,
   SIZE_5,
   SIZE_6;

   public int multiplyBy32AndRound(double var1) {
      double var3 = var1 - (MathHelper.floor_double(var1) + 0.5);
      switch (this) {
         case SIZE_1:
            return (var3 < 0.0 ? !(var3 < -0.3125) : !(var3 < 0.3125)) ? MathHelper.floor_double(var1 * 32.0) : MathHelper.ceiling_double_int(var1 * 32.0);
         case SIZE_2:
            if (var3 < 0.0 ? !(var3 < -0.3125) : !(var3 < 0.3125)) {
               return MathHelper.ceiling_double_int(var1 * 32.0);
            }

            return MathHelper.floor_double(var1 * 32.0);
         case SIZE_3:
            if (var3 > 0.0) {
               return MathHelper.floor_double(var1 * 32.0);
            }

            return MathHelper.ceiling_double_int(var1 * 32.0);
         case SIZE_4:
            if (var3 < 0.0 ? !(var3 < -0.1875) : !(var3 < 0.1875)) {
               return MathHelper.floor_double(var1 * 32.0);
            }

            return MathHelper.ceiling_double_int(var1 * 32.0);
         case SIZE_5:
            if (var3 < 0.0 ? !(var3 < -0.1875) : !(var3 < 0.1875)) {
               return MathHelper.ceiling_double_int(var1 * 32.0);
            }

            return MathHelper.floor_double(var1 * 32.0);
         case SIZE_6:
         default:
            return var3 > 0.0 ? MathHelper.ceiling_double_int(var1 * 32.0) : MathHelper.floor_double(var1 * 32.0);
      }
   }
}
