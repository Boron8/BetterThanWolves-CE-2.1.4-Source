package net.minecraft.src;

import btw.AddonHandler;
import btw.world.biome.BiomeDecoratorBase;
import java.util.Random;

public class BiomeDecorator implements BiomeDecoratorBase {
   protected World currentWorld;
   protected Random randomGenerator;
   protected int chunk_X;
   protected int chunk_Z;
   protected BiomeGenBase biome;
   protected WorldGenerator clayGen = new WorldGenClay(4);
   protected WorldGenerator sandGen = new WorldGenSand(7, Block.sand.blockID);
   protected WorldGenerator gravelAsSandGen = new WorldGenSand(6, Block.gravel.blockID);
   protected WorldGenerator dirtGen = new WorldGenMinable(Block.dirt.blockID, 32);
   protected WorldGenerator gravelGen = new WorldGenMinable(Block.gravel.blockID, 32);
   protected WorldGenerator coalGen = new WorldGenMinable(Block.oreCoal.blockID, 16);
   protected WorldGenerator ironGen = new WorldGenMinable(Block.oreIron.blockID, 8);
   protected WorldGenerator goldGen = new WorldGenMinable(Block.oreGold.blockID, 8);
   protected WorldGenerator redstoneGen = new WorldGenMinable(Block.oreRedstone.blockID, 7);
   protected WorldGenerator diamondGen = new WorldGenMinable(Block.oreDiamond.blockID, 7);
   protected WorldGenerator lapisGen = new WorldGenMinable(Block.oreLapis.blockID, 6);
   protected WorldGenerator plantYellowGen = new WorldGenFlowers(Block.plantYellow.blockID);
   protected WorldGenerator plantRedGen = new WorldGenFlowers(Block.plantRed.blockID);
   protected WorldGenerator mushroomBrownGen = new WorldGenFlowers(Block.mushroomBrown.blockID);
   protected WorldGenerator mushroomRedGen = new WorldGenFlowers(Block.mushroomRed.blockID);
   protected WorldGenerator bigMushroomGen = new WorldGenBigMushroom();
   public WorldGenerator reedGen = new WorldGenReed();
   protected WorldGenerator cactusGen = new WorldGenCactus();
   protected WorldGenerator waterlilyGen = new WorldGenWaterlily();
   protected int waterlilyPerChunk = 0;
   protected int treesPerChunk = 0;
   protected int flowersPerChunk = 2;
   protected int grassPerChunk = 1;
   protected int deadBushPerChunk = 0;
   protected int mushroomsPerChunk = 0;
   protected int reedsPerChunk = 0;
   protected int cactiPerChunk = 0;
   protected int sandPerChunk = 1;
   protected int sandPerChunk2 = 3;
   protected int clayPerChunk = 1;
   protected int bigMushroomsPerChunk = 0;
   public boolean generateLakes = true;

   public BiomeDecorator(BiomeGenBase par1BiomeGenBase) {
      this.biome = par1BiomeGenBase;
   }

   public void decorate(World par1World, Random par2Random, int par3, int par4) {
      if (this.currentWorld != null) {
         throw new RuntimeException("Already decorating!!");
      } else {
         this.currentWorld = par1World;
         this.randomGenerator = par2Random;
         this.chunk_X = par3;
         this.chunk_Z = par4;
         this.decorate();
         this.currentWorld = null;
         this.randomGenerator = null;
      }
   }

   protected void decorate() {
      this.generateOres();

      for (int var1 = 0; var1 < this.sandPerChunk2; var1++) {
         int var2 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.sandGen.generate(this.currentWorld, this.randomGenerator, var2, this.currentWorld.getTopSolidOrLiquidBlock(var2, var3), var3);
      }

      for (int var71 = 0; var71 < this.clayPerChunk; var71++) {
         int var2 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.clayGen.generate(this.currentWorld, this.randomGenerator, var2, this.currentWorld.getTopSolidOrLiquidBlock(var2, var3), var3);
      }

      for (int var8 = 0; var8 < this.sandPerChunk; var8++) {
         int var2 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.sandGen.generate(this.currentWorld, this.randomGenerator, var2, this.currentWorld.getTopSolidOrLiquidBlock(var2, var3), var3);
      }

      int var9 = this.treesPerChunk;
      if (this.randomGenerator.nextInt(10) == 0) {
         var9++;
      }

      for (int var2 = 0; var2 < var9; var2++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         WorldGenerator var5 = this.biome.getRandomWorldGenForTrees(this.randomGenerator);
         var5.setScale(1.0, 1.0, 1.0);
         var5.generate(this.currentWorld, this.randomGenerator, var3, this.currentWorld.getHeightValue(var3, var4), var4);
      }

      for (int var13 = 0; var13 < this.bigMushroomsPerChunk; var13++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, var3, this.currentWorld.getHeightValue(var3, var4), var4);
      }

      for (int var14 = 0; var14 < this.flowersPerChunk; var14++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.randomGenerator.nextInt(128);
         int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.plantYellowGen.generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
         if (this.randomGenerator.nextInt(4) == 0) {
            var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            var4 = this.randomGenerator.nextInt(128);
            var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            this.plantRedGen.generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
         }
      }

      for (int var15 = 0; var15 < this.grassPerChunk; var15++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.randomGenerator.nextInt(128);
         int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         WorldGenerator var6 = this.biome.getRandomWorldGenForGrass(this.randomGenerator);
         var6.generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
      }

      for (int var16 = 0; var16 < this.deadBushPerChunk; var16++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.randomGenerator.nextInt(128);
         int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         new WorldGenDeadBush(Block.deadBush.blockID).generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
      }

      for (int var17 = 0; var17 < this.waterlilyPerChunk; var17++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         int var7 = this.randomGenerator.nextInt(128);

         while (var7 > 0 && this.currentWorld.getBlockId(var3, var7 - 1, var4) == 0) {
            var7--;
         }

         this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, var3, var7, var4);
      }

      for (int var18 = 0; var18 < this.mushroomsPerChunk; var18++) {
         if (this.randomGenerator.nextInt(4) == 0) {
            int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            int var7 = this.currentWorld.getHeightValue(var3, var4);
            this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, var3, var7, var4);
         }

         if (this.randomGenerator.nextInt(8) == 0) {
            int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            int var7 = this.randomGenerator.nextInt(128);
            this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, var3, var7, var4);
         }
      }

      if (this.randomGenerator.nextInt(4) == 0) {
         int var19 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.randomGenerator.nextInt(128);
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, var19, var3, var4);
      }

      if (this.randomGenerator.nextInt(8) == 0) {
         int var20 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.randomGenerator.nextInt(128);
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, var20, var3, var4);
      }

      for (int var21 = 0; var21 < this.reedsPerChunk; var21++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         int var7 = this.randomGenerator.nextInt(128);
         this.reedGen.generate(this.currentWorld, this.randomGenerator, var3, var7, var4);
      }

      for (int var22 = 0; var22 < 10; var22++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.randomGenerator.nextInt(128);
         int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.reedGen.generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
      }

      if (this.randomGenerator.nextInt(32) == 0) {
         int var23 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var3 = this.randomGenerator.nextInt(128);
         int var4 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         new WorldGenPumpkin().generate(this.currentWorld, this.randomGenerator, var23, var3, var4);
      }

      for (int var24 = 0; var24 < this.cactiPerChunk; var24++) {
         int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
         int var4 = this.randomGenerator.nextInt(128);
         int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
         this.cactusGen.generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
      }

      if (this.generateLakes) {
         for (int var25 = 0; var25 < 50; var25++) {
            int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int var4 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(120) + 8);
            int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            new WorldGenLiquids(Block.waterMoving.blockID).generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
         }

         for (int var26 = 0; var26 < 20; var26++) {
            int var3 = this.chunk_X + this.randomGenerator.nextInt(16) + 8;
            int var4 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(112) + 8) + 8);
            int var7 = this.chunk_Z + this.randomGenerator.nextInt(16) + 8;
            new WorldGenLiquids(Block.lavaMoving.blockID).generate(this.currentWorld, this.randomGenerator, var3, var4, var7);
         }
      }

      AddonHandler.decorateWorld(this, this.currentWorld, this.randomGenerator, this.chunk_X, this.chunk_Z, this.biome);
   }

   protected void genStandardOre1(int par1, WorldGenerator par2WorldGenerator, int par3, int par4) {
      for (int var5 = 0; var5 < par1; var5++) {
         int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
         int var7 = this.randomGenerator.nextInt(par4 - par3) + par3;
         int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
         par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6, var7, var8);
      }
   }

   protected void genStandardOre2(int par1, WorldGenerator par2WorldGenerator, int par3, int par4) {
      for (int var5 = 0; var5 < par1; var5++) {
         int var6 = this.chunk_X + this.randomGenerator.nextInt(16);
         int var7 = this.randomGenerator.nextInt(par4) + this.randomGenerator.nextInt(par4) + (par3 - par4);
         int var8 = this.chunk_Z + this.randomGenerator.nextInt(16);
         par2WorldGenerator.generate(this.currentWorld, this.randomGenerator, var6, var7, var8);
      }
   }

   protected void generateOres() {
      this.genStandardOre1(20, this.dirtGen, 0, 128);
      this.genStandardOre1(10, this.gravelGen, 0, 128);
      this.genStandardOre1(20, this.coalGen, 0, 128);
      this.genStandardOre1(20, this.ironGen, 0, 64);
      this.genStandardOre1(2, this.goldGen, 0, 32);
      this.genStandardOre1(8, this.redstoneGen, 0, 16);
      this.genStandardOre1(1, this.diamondGen, 0, 16);
      this.genStandardOre2(1, this.lapisGen, 16, 16);
   }
}
