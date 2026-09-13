package net.minecraft.src;

public class RegistryDefaulted extends RegistrySimple {
   private final Object defaultObject;

   public RegistryDefaulted(Object var1) {
      this.defaultObject = var1;
   }

   @Override
   public Object func_82594_a(Object var1) {
      Object var2 = super.func_82594_a(var1);
      return var2 == null ? this.defaultObject : var2;
   }
}
