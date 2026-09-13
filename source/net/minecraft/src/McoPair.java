package net.minecraft.src;

public class McoPair {
   private final Object field_98160_a;
   private final Object field_98159_b;

   protected McoPair(Object var1, Object var2) {
      this.field_98160_a = var1;
      this.field_98159_b = var2;
   }

   public static McoPair func_98157_a(Object var0, Object var1) {
      return new McoPair(var0, var1);
   }

   public Object func_100005_a() {
      return this.field_98160_a;
   }

   public Object func_100004_b() {
      return this.field_98159_b;
   }
}
