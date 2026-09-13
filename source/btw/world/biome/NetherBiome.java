package btw.world.biome;

import btw.entity.mob.GhastEntity;
import btw.entity.mob.MagmaCubeEntity;
import btw.entity.mob.ZombiePigmanEntity;
import net.minecraft.src.BiomeGenHell;
import net.minecraft.src.SpawnListEntry;

public class NetherBiome extends BiomeGenHell {
   public NetherBiome(int iBiomeID) {
      super(iBiomeID);
      this.spawnableMonsterList.clear();
      this.spawnableMonsterList.add(new SpawnListEntry(GhastEntity.class, 50, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(ZombiePigmanEntity.class, 100, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(MagmaCubeEntity.class, 1, 4, 4));
   }
}
