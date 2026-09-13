package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableLevelWeather implements Callable {
   CallableLevelWeather(WorldInfo var1) {
      this.worldInfoInstance = var1;
   }

   public String callLevelWeatherInfo() {
      return String.format(
         "Rain time: %d (now: %b), thunder time: %d (now: %b)",
         WorldInfo.getRainTime(this.worldInfoInstance),
         WorldInfo.getRaining(this.worldInfoInstance),
         WorldInfo.getThunderTime(this.worldInfoInstance),
         WorldInfo.getThundering(this.worldInfoInstance)
      );
   }
}
