package net.minecraft.src;

import java.util.Random;

public class BiomeGenTaiga extends BiomeGenBase {
   public BiomeGenTaiga(int var1) {
      super(var1);
      this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 8, 4, 4));
      this.theBiomeDecorator.treesPerChunk = 10;
      this.theBiomeDecorator.grassPerChunk = 1;
   }

   @Override
   public WorldGenerator getRandomWorldGenForTrees(Random var1) {
      return (WorldGenerator)(var1.nextInt(3) == 0 ? new WorldGenTaiga1() : new WorldGenTaiga2(false));
   }
}
