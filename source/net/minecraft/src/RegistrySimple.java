package net.minecraft.src;

import java.util.HashMap;
import java.util.Map;

public class RegistrySimple implements IRegistry {
   protected final Map registryObjects = new HashMap();

   @Override
   public Object func_82594_a(Object var1) {
      return this.registryObjects.get(var1);
   }

   @Override
   public void putObject(Object var1, Object var2) {
      this.registryObjects.put(var1, var2);
   }
}
