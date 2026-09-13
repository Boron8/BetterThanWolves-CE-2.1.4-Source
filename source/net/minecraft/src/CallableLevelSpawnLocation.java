package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelSpawnLocation implements Callable {
   CallableLevelSpawnLocation(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelSpawnLocation() {
      return CrashReportCategory.getLocationInfo(
         WorldInfo.getSpawnXCoordinate(this.worldInfoInstance),
         WorldInfo.getSpawnYCoordinate(this.worldInfoInstance),
         WorldInfo.getSpawnZCoordinate(this.worldInfoInstance)
      );
   }
}
