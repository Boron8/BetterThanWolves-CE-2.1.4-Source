package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelGenerator implements Callable {
   CallableLevelGenerator(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelGeneratorInfo() {
      return String.format(
         "ID %02d - %s, ver %d. Features enabled: %b",
         WorldInfo.getTerrainTypeOfWorld(this.worldInfoInstance).getWorldTypeID(),
         WorldInfo.getTerrainTypeOfWorld(this.worldInfoInstance).getWorldTypeName(),
         WorldInfo.getTerrainTypeOfWorld(this.worldInfoInstance).getGeneratorVersion(),
         WorldInfo.getMapFeaturesEnabled(this.worldInfoInstance)
      );
   }
}
