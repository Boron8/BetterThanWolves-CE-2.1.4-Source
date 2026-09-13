package net.minecraft.src;

import java.util.Random;

public class BiomeGenDesert extends BiomeGenBase {
   public BiomeGenDesert(int var1) {
      super(var1);
      this.spawnableCreatureList.clear();
      this.topBlock = (byte)Block.sand.blockID;
      this.fillerBlock = (byte)Block.sand.blockID;
      this.theBiomeDecorator.treesPerChunk = -999;
      this.theBiomeDecorator.deadBushPerChunk = 2;
      this.theBiomeDecorator.reedsPerChunk = 50;
      this.theBiomeDecorator.cactiPerChunk = 10;
   }

   @Override
   public void decorate(World var1, Random var2, int var3, int var4) {
      super.decorate(var1, var2, var3, var4);
      if (var2.nextInt(1000) == 0) {
         int var5 = var3 + var2.nextInt(16) + 8;
         int var6 = var4 + var2.nextInt(16) + 8;
         WorldGenDesertWells var7 = new WorldGenDesertWells();
         var7.generate(var1, var2, var5, var1.getHeightValue(var5, var6) + 1, var6);
      }
   }
}
