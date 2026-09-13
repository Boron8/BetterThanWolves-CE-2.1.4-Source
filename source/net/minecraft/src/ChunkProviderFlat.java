package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ChunkProviderFlat implements IChunkProvider {
   private World worldObj;
   private Random random;
   private final byte[] field_82700_c = new byte[256];
   private final byte[] field_82698_d = new byte[256];
   private final FlatGeneratorInfo field_82699_e;
   private final List structureGenerators = new ArrayList();
   private final boolean field_82697_g;
   private final boolean field_82702_h;
   private WorldGenLakes waterLakeGenerator;
   private WorldGenLakes lavaLakeGenerator;

   public ChunkProviderFlat(World var1, long var2, boolean var4, String var5) {
      this.worldObj = var1;
      this.random = new Random(var2);
      this.field_82699_e = FlatGeneratorInfo.createFlatGeneratorFromString(var5);
      if (var4) {
         Map var6 = this.field_82699_e.getWorldFeatures();
         if (var6.containsKey("village")) {
            Map var7 = (Map)var6.get("village");
            if (!var7.containsKey("size")) {
               var7.put("size", "1");
            }

            this.structureGenerators.add(new MapGenVillage(var7));
         }

         if (var6.containsKey("biome_1")) {
            this.structureGenerators.add(new MapGenScatteredFeature((Map)var6.get("biome_1")));
         }

         if (var6.containsKey("mineshaft")) {
            this.structureGenerators.add(new MapGenMineshaft((Map)var6.get("mineshaft")));
         }

         if (var6.containsKey("stronghold")) {
            this.structureGenerators.add(new MapGenStronghold((Map)var6.get("stronghold")));
         }
      }

      this.field_82697_g = this.field_82699_e.getWorldFeatures().containsKey("decoration");
      if (this.field_82699_e.getWorldFeatures().containsKey("lake")) {
         this.waterLakeGenerator = new WorldGenLakes(Block.waterStill.blockID);
      }

      if (this.field_82699_e.getWorldFeatures().containsKey("lava_lake")) {
         this.lavaLakeGenerator = new WorldGenLakes(Block.lavaStill.blockID);
      }

      this.field_82702_h = this.field_82699_e.getWorldFeatures().containsKey("dungeon");

      for (FlatLayerInfo var10 : this.field_82699_e.getFlatLayers()) {
         for (int var8 = var10.getMinY(); var8 < var10.getMinY() + var10.getLayerCount(); var8++) {
            this.field_82700_c[var8] = (byte)(var10.getFillBlock() & 0xFF);
            this.field_82698_d[var8] = (byte)var10.getFillBlockMeta();
         }
      }
   }

   @Override
   public Chunk loadChunk(int var1, int var2) {
      return this.provideChunk(var1, var2);
   }

   @Override
   public Chunk provideChunk(int var1, int var2) {
      Chunk var3 = new Chunk(this.worldObj, var1, var2);

      for (int var4 = 0; var4 < this.field_82700_c.length; var4++) {
         int var5 = var4 >> 4;
         ExtendedBlockStorage var6 = var3.getBlockStorageArray()[var5];
         if (var6 == null) {
            var6 = new ExtendedBlockStorage(var4, !this.worldObj.provider.hasNoSky);
            var3.getBlockStorageArray()[var5] = var6;
         }

         for (int var7 = 0; var7 < 16; var7++) {
            for (int var8 = 0; var8 < 16; var8++) {
               var6.setExtBlockID(var7, var4 & 15, var8, this.field_82700_c[var4] & 255);
               var6.setExtBlockMetadata(var7, var4 & 15, var8, this.field_82698_d[var4]);
            }
         }
      }

      var3.generateSkylightMap();
      BiomeGenBase[] var9 = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, var1 * 16, var2 * 16, 16, 16);
      byte[] var10 = var3.getBiomeArray();

      for (int var11 = 0; var11 < var10.length; var11++) {
         var10[var11] = (byte)var9[var11].biomeID;
      }

      for (MapGenStructure var13 : this.structureGenerators) {
         var13.generate(this, this.worldObj, var1, var2, null);
      }

      var3.generateSkylightMap();
      return var3;
   }

   @Override
   public boolean chunkExists(int var1, int var2) {
      return true;
   }

   @Override
   public void populate(IChunkProvider var1, int var2, int var3) {
      int var4 = var2 * 16;
      int var5 = var3 * 16;
      BiomeGenBase var6 = this.worldObj.getBiomeGenForCoords(var4 + 16, var5 + 16);
      boolean var7 = false;
      this.random.setSeed(this.worldObj.getSeed());
      long var8 = this.random.nextLong() / 2L * 2L + 1L;
      long var10 = this.random.nextLong() / 2L * 2L + 1L;
      this.random.setSeed(var2 * var8 + var3 * var10 ^ this.worldObj.getSeed());

      for (MapGenStructure var13 : this.structureGenerators) {
         boolean var14 = var13.generateStructuresInChunk(this.worldObj, this.random, var2, var3);
         if (var13 instanceof MapGenVillage) {
            var7 |= var14;
         }
      }

      if (this.waterLakeGenerator != null && !var7 && this.random.nextInt(4) == 0) {
         int var16 = var4 + this.random.nextInt(16) + 8;
         int var19 = this.random.nextInt(128);
         int var22 = var5 + this.random.nextInt(16) + 8;
         this.waterLakeGenerator.generate(this.worldObj, this.random, var16, var19, var22);
      }

      if (this.lavaLakeGenerator != null && !var7 && this.random.nextInt(8) == 0) {
         int var17 = var4 + this.random.nextInt(16) + 8;
         int var20 = this.random.nextInt(this.random.nextInt(120) + 8);
         int var23 = var5 + this.random.nextInt(16) + 8;
         if (var20 < 63 || this.random.nextInt(10) == 0) {
            this.lavaLakeGenerator.generate(this.worldObj, this.random, var17, var20, var23);
         }
      }

      if (this.field_82702_h) {
         for (int var18 = 0; var18 < 8; var18++) {
            int var21 = var4 + this.random.nextInt(16) + 8;
            int var24 = this.random.nextInt(128);
            int var15 = var5 + this.random.nextInt(16) + 8;
            new WorldGenDungeons().generate(this.worldObj, this.random, var21, var24, var15);
         }
      }

      if (this.field_82697_g) {
         var6.decorate(this.worldObj, this.random, var4, var5);
      }
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
      return "FlatLevelSource";
   }

   @Override
   public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4) {
      BiomeGenBase var5 = this.worldObj.getBiomeGenForCoords(var2, var4);
      return var5 == null ? null : var5.getSpawnableList(var1);
   }

   @Override
   public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5) {
      if ("Stronghold".equals(var2)) {
         for (MapGenStructure var7 : this.structureGenerators) {
            if (var7 instanceof MapGenStronghold) {
               return var7.getNearestInstance(var1, var3, var4, var5);
            }
         }
      }

      return null;
   }

   @Override
   public int getLoadedChunkCount() {
      return 0;
   }

   @Override
   public void recreateStructures(int var1, int var2) {
      for (MapGenStructure var4 : this.structureGenerators) {
         var4.a(this, this.worldObj, var1, var2, null);
      }
   }
}
