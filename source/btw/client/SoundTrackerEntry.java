package btw.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class SoundTrackerEntry {
   public String name;
   public float posX;
   public float posY;
   public float posZ;
   public float maxRangeSq;

   public SoundTrackerEntry(String sName, float fXPos, float fYPos, float fZPos, float fMaxRange) {
      this.name = sName;
      this.posX = fXPos;
      this.posY = fYPos;
      this.posZ = fZPos;
      this.maxRangeSq = fMaxRange * fMaxRange;
   }
}
