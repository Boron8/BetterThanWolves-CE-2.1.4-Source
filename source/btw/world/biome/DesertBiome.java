package btw.world.biome;

import net.minecraft.src.BiomeGenDesert;

public class DesertBiome extends BiomeGenDesert {
   public DesertBiome(int iBiomeID) {
      super(iBiomeID);
   }

   @Override
   public boolean canLightningStrikeInBiome() {
      return true;
   }
}
