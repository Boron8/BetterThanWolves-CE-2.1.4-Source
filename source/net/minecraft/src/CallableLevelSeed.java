package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelSeed implements Callable {
   CallableLevelSeed(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelSeed() {
      return String.valueOf(this.worldInfoInstance.getSeed());
   }
}
