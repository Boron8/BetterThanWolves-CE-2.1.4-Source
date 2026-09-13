package btw.world.biome;

import btw.entity.mob.EndermanEntity;
import net.minecraft.src.BiomeGenEnd;
import net.minecraft.src.SpawnListEntry;

public class EndBiome extends BiomeGenEnd {
   public EndBiome(int iBiomeID) {
      super(iBiomeID);
      this.spawnableMonsterList.clear();
      this.spawnableMonsterList.add(new SpawnListEntry(EndermanEntity.class, 10, 4, 4));
   }
}
