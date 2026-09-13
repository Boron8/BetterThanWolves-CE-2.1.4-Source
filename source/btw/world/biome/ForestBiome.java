package btw.world.biome;

import btw.entity.mob.ChickenEntity;
import btw.entity.mob.CowEntity;
import btw.entity.mob.PigEntity;
import btw.entity.mob.SheepEntity;
import btw.entity.mob.WolfEntity;
import net.minecraft.src.BiomeGenForest;
import net.minecraft.src.SpawnListEntry;

public class ForestBiome extends BiomeGenForest {
   public ForestBiome(int iBiomeID) {
      super(iBiomeID);
      this.spawnableCreatureList.clear();
      this.spawnableCreatureList.add(new SpawnListEntry(SheepEntity.class, 12, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(PigEntity.class, 10, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(ChickenEntity.class, 10, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(CowEntity.class, 8, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(WolfEntity.class, 5, 4, 4));
   }
}
