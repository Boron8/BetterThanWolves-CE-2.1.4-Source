package net.minecraft.src;

public class ExceptionMcoService extends Exception {
   public final int field_96392_a;
   public final String field_96391_b;

   public ExceptionMcoService(int var1, String var2) {
      super(var2);
      this.field_96392_a = var1;
      this.field_96391_b = var2;
   }
}
