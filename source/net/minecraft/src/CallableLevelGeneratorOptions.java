package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGeneratorOptions implements Callable {
   CallableLevelGeneratorOptions(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelGeneratorOptions() {
      return WorldInfo.getWorldGeneratorOptions(this.worldInfoInstance);
   }
}
