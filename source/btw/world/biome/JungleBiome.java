package btw.world.biome;

import btw.entity.mob.ChickenEntity;
import btw.entity.mob.CreeperEntity;
import btw.entity.mob.EndermanEntity;
import btw.entity.mob.JungleSpiderEntity;
import btw.entity.mob.OcelotEntity;
import btw.entity.mob.PigEntity;
import btw.entity.mob.SkeletonEntity;
import btw.entity.mob.SlimeEntity;
import btw.entity.mob.SpiderEntity;
import btw.entity.mob.ZombieEntity;
import java.util.Random;
import net.minecraft.src.BiomeGenJungle;
import net.minecraft.src.SpawnListEntry;
import net.minecraft.src.World;

public class JungleBiome extends BiomeGenJungle {
   private static final int EXTRA_REEDS_PER_CHUNK = 100;

   public JungleBiome(int iBiomeID) {
      super(iBiomeID);
      this.spawnableCreatureList.clear();
      this.spawnableCreatureList.add(new SpawnListEntry(ChickenEntity.class, 10, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(PigEntity.class, 10, 4, 4));
      this.spawnableCreatureList.add(new SpawnListEntry(ChickenEntity.class, 10, 4, 4));
      this.spawnableMonsterList.clear();
      this.spawnableMonsterList.add(new SpawnListEntry(JungleSpiderEntity.class, 2, 1, 1));
      this.spawnableMonsterList.add(new SpawnListEntry(SpiderEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(ZombieEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(SkeletonEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(CreeperEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(SlimeEntity.class, 10, 4, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(EndermanEntity.class, 1, 1, 4));
      this.spawnableMonsterList.add(new SpawnListEntry(OcelotEntity.class, 2, 1, 1));
   }

   @Override
   public void decorate(World world, Random rand, int iChunkX, int iChunkZ) {
      super.decorate(world, rand, iChunkX, iChunkZ);

      for (int iTempCount = 0; iTempCount < 100; iTempCount++) {
         int iXGen = iChunkX + rand.nextInt(16) + 8;
         int iZGen = iChunkZ + rand.nextInt(16) + 8;
         int iYGen = rand.nextInt(128);
         this.theBiomeDecorator.reedGen.generate(world, rand, iXGen, iYGen, iZGen);
      }
   }
}
