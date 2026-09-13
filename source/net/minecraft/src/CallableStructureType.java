package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableStructureType implements Callable {
   CallableStructureType(MapGenStructure var1) {
      this.theMapStructureGenerator = var1;
   }

   public String callStructureType() {
      return this.theMapStructureGenerator.getClass().getCanonicalName();
   }
}
