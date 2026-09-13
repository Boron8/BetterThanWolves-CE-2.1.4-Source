package net.minecraft.src;

import btw.entity.mob.ChickenEntity;
import btw.entity.mob.PigEntity;
import btw.entity.mob.SlimeEntity;
import btw.entity.mob.WitchEntity;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BiomeGenSwamp extends BiomeGenBase {
   protected BiomeGenSwamp(int par1) {
      super(par1);
      this.theBiomeDecorator.treesPerChunk = 2;
      this.theBiomeDecorator.flowersPerChunk = -999;
      this.theBiomeDecorator.deadBushPerChunk = 1;
      this.theBiomeDecorator.mushroomsPerChunk = 8;
      this.theBiomeDecorator.reedsPerChunk = 10;
      this.theBiomeDecorator.clayPerChunk = 1;
      this.theBiomeDecorator.waterlilyPerChunk = 4;
      this.waterColorMultiplier = 14745518;
      this.spawnableMonsterList.add(new SpawnListEntry(SlimeEntity.class, 1, 1, 1));
      this.spawnableMonsterList.add(new SpawnListEntry(WitchEntity.class, 1, 1, 1));
      this.spawnableCreatureList.clear();
      this.spawnableCreatureList.add(new SpawnListEntry(ChickenEntity.class, 10, 2, 2));
      this.spawnableCreatureList.add(new SpawnListEntry(PigEntity.class, 10, 2, 2));
   }

   @Override
   public WorldGenerator getRandomWorldGenForTrees(Random par1Random) {
      return this.worldGeneratorSwamp;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBiomeGrassColor() {
      double var1 = this.j();
      double var3 = this.i();
      return ((ColorizerGrass.getGrassColor(var1, var3) & 16711422) + 5115470) / 2;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBiomeFoliageColor() {
      double var1 = this.j();
      double var3 = this.i();
      return ((ColorizerFoliage.getFoliageColor(var1, var3) & 16711422) + 5115470) / 2;
   }

   @Override
   public boolean canSlimesSpawnOnSurface() {
      return true;
   }
}
