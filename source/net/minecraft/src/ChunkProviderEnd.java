package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderEnd implements IChunkProvider {
   private Random endRNG;
   private NoiseGeneratorOctaves noiseGen1;
   private NoiseGeneratorOctaves noiseGen2;
   private NoiseGeneratorOctaves noiseGen3;
   public NoiseGeneratorOctaves noiseGen4;
   public NoiseGeneratorOctaves noiseGen5;
   private World endWorld;
   private double[] densities;
   private BiomeGenBase[] biomesForGeneration;
   double[] noiseData1;
   double[] noiseData2;
   double[] noiseData3;
   double[] noiseData4;
   double[] noiseData5;
   int[][] field_73203_h = new int[32][32];

   public ChunkProviderEnd(World var1, long var2) {
      this.endWorld = var1;
      this.endRNG = new Random(var2);
      this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
      this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
      this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
      this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
      this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
   }

   public void generateTerrain(int var1, int var2, byte[] var3, BiomeGenBase[] var4) {
      byte var5 = 2;
      int var6 = var5 + 1;
      byte var7 = 33;
      int var8 = var5 + 1;
      this.densities = this.initializeNoiseField(this.densities, var1 * var5, 0, var2 * var5, var6, var7, var8);

      for (int var9 = 0; var9 < var5; var9++) {
         for (int var10 = 0; var10 < var5; var10++) {
            for (int var11 = 0; var11 < 32; var11++) {
               double var12 = 0.25;
               double var14 = this.densities[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 0];
               double var16 = this.densities[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 0];
               double var18 = this.densities[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 0];
               double var20 = this.densities[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 0];
               double var22 = (this.densities[((var9 + 0) * var8 + var10 + 0) * var7 + var11 + 1] - var14) * var12;
               double var24 = (this.densities[((var9 + 0) * var8 + var10 + 1) * var7 + var11 + 1] - var16) * var12;
               double var26 = (this.densities[((var9 + 1) * var8 + var10 + 0) * var7 + var11 + 1] - var18) * var12;
               double var28 = (this.densities[((var9 + 1) * var8 + var10 + 1) * var7 + var11 + 1] - var20) * var12;

               for (int var30 = 0; var30 < 4; var30++) {
                  double var31 = 0.125;
                  double var33 = var14;
                  double var35 = var16;
                  double var37 = (var18 - var14) * var31;
                  double var39 = (var20 - var16) * var31;

                  for (int var41 = 0; var41 < 8; var41++) {
                     int var42 = var41 + var9 * 8 << 11 | 0 + var10 * 8 << 7 | var11 * 4 + var30;
                     short var43 = 128;
                     double var44 = 0.125;
                     double var46 = var33;
                     double var48 = (var35 - var33) * var44;

                     for (int var50 = 0; var50 < 8; var50++) {
                        int var51 = 0;
                        if (var46 > 0.0) {
                           var51 = Block.whiteStone.blockID;
                        }

                        var3[var42] = (byte)var51;
                        var42 += var43;
                        var46 += var48;
                     }

                     var33 += var37;
                     var35 += var39;
                  }

                  var14 += var22;
                  var16 += var24;
                  var18 += var26;
                  var20 += var28;
               }
            }
         }
      }
   }

   public void replaceBlocksForBiome(int var1, int var2, byte[] var3, BiomeGenBase[] var4) {
      for (int var5 = 0; var5 < 16; var5++) {
         for (int var6 = 0; var6 < 16; var6++) {
            byte var7 = 1;
            int var8 = -1;
            byte var9 = (byte)Block.whiteStone.blockID;
            byte var10 = (byte)Block.whiteStone.blockID;

            for (int var11 = 127; var11 >= 0; var11--) {
               int var12 = (var6 * 16 + var5) * 128 + var11;
               byte var13 = var3[var12];
               if (var13 == 0) {
                  var8 = -1;
               } else if (var13 == Block.stone.blockID) {
                  if (var8 == -1) {
                     if (var7 <= 0) {
                        var9 = 0;
                        var10 = (byte)Block.whiteStone.blockID;
                     }

                     var8 = var7;
                     if (var11 >= 0) {
                        var3[var12] = var9;
                     } else {
                        var3[var12] = var10;
                     }
                  } else if (var8 > 0) {
                     var8--;
                     var3[var12] = var10;
                  }
               }
            }
         }
      }
   }

   @Override
   public Chunk loadChunk(int var1, int var2) {
      return this.provideChunk(var1, var2);
   }

   @Override
   public Chunk provideChunk(int var1, int var2) {
      this.endRNG.setSeed(var1 * 341873128712L + var2 * 132897987541L);
      byte[] var3 = new byte[32768];
      this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, var1 * 16, var2 * 16, 16, 16);
      this.generateTerrain(var1, var2, var3, this.biomesForGeneration);
      this.replaceBlocksForBiome(var1, var2, var3, this.biomesForGeneration);
      Chunk var4 = new Chunk(this.endWorld, var3, var1, var2);
      byte[] var5 = var4.getBiomeArray();

      for (int var6 = 0; var6 < var5.length; var6++) {
         var5[var6] = (byte)this.biomesForGeneration[var6].biomeID;
      }

      var4.generateSkylightMap();
      return var4;
   }

   private double[] initializeNoiseField(double[] var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      if (var1 == null) {
         var1 = new double[var5 * var6 * var7];
      }

      double var8 = 684.412;
      double var10 = 684.412;
      this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, var2, var4, var5, var7, 1.121, 1.121, 0.5);
      this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, var2, var4, var5, var7, 200.0, 200.0, 0.5);
      var8 *= 2.0;
      this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, var2, var3, var4, var5, var6, var7, var8 / 80.0, var10 / 160.0, var8 / 80.0);
      this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, var2, var3, var4, var5, var6, var7, var8, var10, var8);
      this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, var2, var3, var4, var5, var6, var7, var8, var10, var8);
      int var12 = 0;
      int var13 = 0;

      for (int var14 = 0; var14 < var5; var14++) {
         for (int var15 = 0; var15 < var7; var15++) {
            double var16 = (this.noiseData4[var13] + 256.0) / 512.0;
            if (var16 > 1.0) {
               var16 = 1.0;
            }

            double var18 = this.noiseData5[var13] / 8000.0;
            if (var18 < 0.0) {
               var18 = -var18 * 0.3;
            }

            var18 = var18 * 3.0 - 2.0;
            float var20 = (var14 + var2 - 0) / 1.0F;
            float var21 = (var15 + var4 - 0) / 1.0F;
            float var22 = 100.0F - MathHelper.sqrt_float(var20 * var20 + var21 * var21) * 8.0F;
            if (var22 > 80.0F) {
               var22 = 80.0F;
            }

            if (var22 < -100.0F) {
               var22 = -100.0F;
            }

            if (var18 > 1.0) {
               var18 = 1.0;
            }

            var18 /= 8.0;
            var18 = 0.0;
            if (var16 < 0.0) {
               var16 = 0.0;
            }

            var16 += 0.5;
            var18 = var18 * var6 / 16.0;
            var13++;
            double var23 = var6 / 2.0;

            for (int var25 = 0; var25 < var6; var25++) {
               double var26 = 0.0;
               double var28 = (var25 - var23) * 8.0 / var16;
               if (var28 < 0.0) {
                  var28 *= -1.0;
               }

               double var30 = this.noiseData2[var12] / 512.0;
               double var32 = this.noiseData3[var12] / 512.0;
               double var34 = (this.noiseData1[var12] / 10.0 + 1.0) / 2.0;
               if (var34 < 0.0) {
                  var26 = var30;
               } else if (var34 > 1.0) {
                  var26 = var32;
               } else {
                  var26 = var30 + (var32 - var30) * var34;
               }

               var26 -= 8.0;
               var26 += var22;
               byte var36 = 2;
               if (var25 > var6 / 2 - var36) {
                  double var37 = (var25 - (var6 / 2 - var36)) / 64.0F;
                  if (var37 < 0.0) {
                     var37 = 0.0;
                  }

                  if (var37 > 1.0) {
                     var37 = 1.0;
                  }

                  var26 = var26 * (1.0 - var37) + -3000.0 * var37;
               }

               var36 = 8;
               if (var25 < var36) {
                  double var50 = (var36 - var25) / (var36 - 1.0F);
                  var26 = var26 * (1.0 - var50) + -30.0 * var50;
               }

               var1[var12] = var26;
               var12++;
            }
         }
      }

      return var1;
   }

   @Override
   public boolean chunkExists(int var1, int var2) {
      return true;
   }

   @Override
   public void populate(IChunkProvider var1, int var2, int var3) {
      BlockSand.fallInstantly = true;
      int var4 = var2 * 16;
      int var5 = var3 * 16;
      BiomeGenBase var6 = this.endWorld.getBiomeGenForCoords(var4 + 16, var5 + 16);
      var6.decorate(this.endWorld, this.endWorld.rand, var4, var5);
      BlockSand.fallInstantly = false;
   }

   @Override
   public boolean saveChunks(boolean var1, IProgressUpdate var2) {
      return true;
   }

   @Override
   public void func_104112_b() {
   }

   @Override
   public boolean unloadQueuedChunks() {
      return false;
   }

   @Override
   public boolean canSave() {
      return true;
   }

   @Override
   public String makeString() {
      return "RandomLevelSource";
   }

   @Override
   public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4) {
      BiomeGenBase var5 = this.endWorld.getBiomeGenForCoords(var2, var4);
      return var5 == null ? null : var5.getSpawnableList(var1);
   }

   @Override
   public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5) {
      return null;
   }

   @Override
   public int getLoadedChunkCount() {
      return 0;
   }

   @Override
   public void recreateStructures(int var1, int var2) {
   }
}
