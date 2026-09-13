package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelTime implements Callable {
   CallableLevelTime(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelTime() {
      return String.format("%d game time, %d day time", WorldInfo.func_85126_g(this.worldInfoInstance), WorldInfo.getWorldTime(this.worldInfoInstance));
   }
}
