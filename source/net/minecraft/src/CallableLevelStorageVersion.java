package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelStorageVersion implements Callable {
   CallableLevelStorageVersion(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelStorageFormat() {
      String var1 = "Unknown?";

      try {
         switch (WorldInfo.getSaveVersion(this.worldInfoInstance)) {
            case 19132:
               var1 = "McRegion";
               break;
            case 19133:
               var1 = "Anvil";
         }
      } catch (Throwable var3) {
      }

      return String.format("0x%05X - %s", WorldInfo.getSaveVersion(this.worldInfoInstance), var1);
   }
}
