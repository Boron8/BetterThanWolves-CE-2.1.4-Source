package net.minecraft.src;

public class BiomeGenEnd extends BiomeGenBase {
   public BiomeGenEnd(int var1) {
      super(var1);
      this.spawnableMonsterList.clear();
      this.spawnableCreatureList.clear();
      this.spawnableWaterCreatureList.clear();
      this.spawnableCaveCreatureList.clear();
      this.spawnableMonsterList.add(new SpawnListEntry(EntityEnderman.class, 10, 4, 4));
      this.topBlock = (byte)Block.dirt.blockID;
      this.fillerBlock = (byte)Block.dirt.blockID;
      this.theBiomeDecorator = new BiomeEndDecorator(this);
   }

   @Override
   public int getSkyColorByTemp(float var1) {
      return 0;
   }
}
