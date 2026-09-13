package net.minecraft.src;

import java.util.Random;

public class BiomeGenForest extends BiomeGenBase {
   public BiomeGenForest(int var1) {
      super(var1);
      this.spawnableCreatureList.add(new SpawnListEntry(EntityWolf.class, 5, 4, 4));
      this.theBiomeDecorator.treesPerChunk = 10;
      this.theBiomeDecorator.grassPerChunk = 2;
   }

   @Override
   public WorldGenerator getRandomWorldGenForTrees(Random var1) {
      if (var1.nextInt(5) == 0) {
         return this.worldGeneratorForest;
      } else {
         return (WorldGenerator)(var1.nextInt(10) == 0 ? this.worldGeneratorBigTree : this.worldGeneratorTrees);
      }
   }
}
