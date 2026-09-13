package net.minecraft.src;

public class ChunkLoader {
   public static AnvilConverterData load(NBTTagCompound var0) {
      int var1 = var0.getInteger("xPos");
      int var2 = var0.getInteger("zPos");
      AnvilConverterData var3 = new AnvilConverterData(var1, var2);
      var3.blocks = var0.getByteArray("Blocks");
      var3.data = new NibbleArrayReader(var0.getByteArray("Data"), 7);
      var3.skyLight = new NibbleArrayReader(var0.getByteArray("SkyLight"), 7);
      var3.blockLight = new NibbleArrayReader(var0.getByteArray("BlockLight"), 7);
      var3.heightmap = var0.getByteArray("HeightMap");
      var3.terrainPopulated = var0.getBoolean("TerrainPopulated");
      var3.entities = var0.getTagList("Entities");
      var3.tileEntities = var0.getTagList("TileEntities");
      var3.tileTicks = var0.getTagList("TileTicks");

      try {
         var3.lastUpdated = var0.getLong("LastUpdate");
      } catch (ClassCastException var5) {
         var3.lastUpdated = var0.getInteger("LastUpdate");
      }

      return var3;
   }

   public static void convertToAnvilFormat(AnvilConverterData var0, NBTTagCompound var1, WorldChunkManager var2) {
      var1.setInteger("xPos", var0.x);
      var1.setInteger("zPos", var0.z);
      var1.setLong("LastUpdate", var0.lastUpdated);
      int[] var3 = new int[var0.heightmap.length];

      for (int var4 = 0; var4 < var0.heightmap.length; var4++) {
         var3[var4] = var0.heightmap[var4];
      }

      var1.setIntArray("HeightMap", var3);
      var1.setBoolean("TerrainPopulated", var0.terrainPopulated);
      NBTTagList var16 = new NBTTagList("Sections");

      for (int var5 = 0; var5 < 8; var5++) {
         boolean var6 = true;

         for (int var7 = 0; var7 < 16 && var6; var7++) {
            for (int var8 = 0; var8 < 16 && var6; var8++) {
               for (int var9 = 0; var9 < 16; var9++) {
                  int var10 = var7 << 11 | var9 << 7 | var8 + (var5 << 4);
                  byte var11 = var0.blocks[var10];
                  if (var11 != 0) {
                     var6 = false;
                     break;
                  }
               }
            }
         }

         if (!var6) {
            byte[] var19 = new byte[4096];
            NibbleArray var21 = new NibbleArray(var19.length, 4);
            NibbleArray var22 = new NibbleArray(var19.length, 4);
            NibbleArray var23 = new NibbleArray(var19.length, 4);

            for (int var24 = 0; var24 < 16; var24++) {
               for (int var12 = 0; var12 < 16; var12++) {
                  for (int var13 = 0; var13 < 16; var13++) {
                     int var14 = var24 << 11 | var13 << 7 | var12 + (var5 << 4);
                     byte var15 = var0.blocks[var14];
                     var19[var12 << 8 | var13 << 4 | var24] = (byte)(var15 & 255);
                     var21.set(var24, var12, var13, var0.data.get(var24, var12 + (var5 << 4), var13));
                     var22.set(var24, var12, var13, var0.skyLight.get(var24, var12 + (var5 << 4), var13));
                     var23.set(var24, var12, var13, var0.blockLight.get(var24, var12 + (var5 << 4), var13));
                  }
               }
            }

            NBTTagCompound var25 = new NBTTagCompound();
            var25.setByte("Y", (byte)(var5 & 0xFF));
            var25.setByteArray("Blocks", var19);
            var25.setByteArray("Data", var21.data);
            var25.setByteArray("SkyLight", var22.data);
            var25.setByteArray("BlockLight", var23.data);
            var16.appendTag(var25);
         }
      }

      var1.setTag("Sections", var16);
      byte[] var17 = new byte[256];

      for (int var18 = 0; var18 < 16; var18++) {
         for (int var20 = 0; var20 < 16; var20++) {
            var17[var20 << 4 | var18] = (byte)(var2.getBiomeGenAt(var0.x << 4 | var18, var0.z << 4 | var20).biomeID & 0xFF);
         }
      }

      var1.setByteArray("Biomes", var17);
      var1.setTag("Entities", var0.entities);
      var1.setTag("TileEntities", var0.tileEntities);
      if (var0.tileTicks != null) {
         var1.setTag("TileTicks", var0.tileTicks);
      }
   }
}
