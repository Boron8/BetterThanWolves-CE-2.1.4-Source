package net.minecraft.src;

public class BiomeGenPlains extends BiomeGenBase {
   protected BiomeGenPlains(int var1) {
      super(var1);
      this.theBiomeDecorator.treesPerChunk = -999;
      this.theBiomeDecorator.flowersPerChunk = 4;
      this.theBiomeDecorator.grassPerChunk = 10;
   }
}
