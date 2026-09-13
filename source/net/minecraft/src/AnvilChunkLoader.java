package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnvilChunkLoader implements IChunkLoader, IThreadedFileIO {
   private List chunksToRemove = new ArrayList();
   private Set pendingAnvilChunksCoordinates = new HashSet();
   private Object syncLockObject = new Object();
   private final File chunkSaveLocation;

   public AnvilChunkLoader(File var1) {
      this.chunkSaveLocation = var1;
   }

   @Override
   public Chunk loadChunk(World var1, int var2, int var3) {
      NBTTagCompound var4 = null;
      ChunkCoordIntPair var5 = new ChunkCoordIntPair(var2, var3);
      synchronized (this.syncLockObject) {
         if (this.pendingAnvilChunksCoordinates.contains(var5)) {
            for (int var7 = 0; var7 < this.chunksToRemove.size(); var7++) {
               if (((AnvilChunkLoaderPending)this.chunksToRemove.get(var7)).chunkCoordinate.equals(var5)) {
                  var4 = ((AnvilChunkLoaderPending)this.chunksToRemove.get(var7)).nbtTags;
                  break;
               }
            }
         }
      }

      if (var4 == null) {
         DataInputStream var10 = RegionFileCache.getChunkInputStream(this.chunkSaveLocation, var2, var3);
         if (var10 == null) {
            return null;
         }

         var4 = CompressedStreamTools.read(var10);
      }

      return this.checkedReadChunkFromNBT(var1, var2, var3, var4);
   }

   protected Chunk checkedReadChunkFromNBT(World var1, int var2, int var3, NBTTagCompound var4) {
      if (!var4.hasKey("Level")) {
         var1.getWorldLogAgent().logSevere("Chunk file at " + var2 + "," + var3 + " is missing level data, skipping");
         return null;
      } else if (!var4.getCompoundTag("Level").hasKey("Sections")) {
         var1.getWorldLogAgent().logSevere("Chunk file at " + var2 + "," + var3 + " is missing block data, skipping");
         return null;
      } else {
         Chunk var5 = this.readChunkFromNBT(var1, var4.getCompoundTag("Level"));
         if (!var5.isAtLocation(var2, var3)) {
            var1.getWorldLogAgent()
               .logSevere(
                  "Chunk file at "
                     + var2
                     + ","
                     + var3
                     + " is in the wrong location; relocating. (Expected "
                     + var2
                     + ", "
                     + var3
                     + ", got "
                     + var5.xPosition
                     + ", "
                     + var5.zPosition
                     + ")"
               );
            var4.setInteger("xPos", var2);
            var4.setInteger("zPos", var3);
            var5 = this.readChunkFromNBT(var1, var4.getCompoundTag("Level"));
         }

         return var5;
      }
   }

   @Override
   public void saveChunk(World var1, Chunk var2) {
      var1.checkSessionLock();

      try {
         NBTTagCompound var3 = new NBTTagCompound();
         NBTTagCompound var4 = new NBTTagCompound();
         var3.setTag("Level", var4);
         this.writeChunkToNBT(var2, var1, var4);
         this.addChunkToPending(var2.getChunkCoordIntPair(), var3);
      } catch (Exception var5) {
         var5.printStackTrace();
      }
   }

   protected void addChunkToPending(ChunkCoordIntPair var1, NBTTagCompound var2) {
      synchronized (this.syncLockObject) {
         if (this.pendingAnvilChunksCoordinates.contains(var1)) {
            for (int var4 = 0; var4 < this.chunksToRemove.size(); var4++) {
               if (((AnvilChunkLoaderPending)this.chunksToRemove.get(var4)).chunkCoordinate.equals(var1)) {
                  this.chunksToRemove.set(var4, new AnvilChunkLoaderPending(var1, var2));
                  return;
               }
            }
         }

         this.chunksToRemove.add(new AnvilChunkLoaderPending(var1, var2));
         this.pendingAnvilChunksCoordinates.add(var1);
         ThreadedFileIOBase.threadedIOInstance.queueIO(this);
      }
   }

   @Override
   public boolean writeNextIO() {
      Object var1 = null;
      synchronized (this.syncLockObject) {
         if (this.chunksToRemove.isEmpty()) {
            return false;
         }

         var1 = (AnvilChunkLoaderPending)this.chunksToRemove.remove(0);
         this.pendingAnvilChunksCoordinates.remove(((AnvilChunkLoaderPending)var1).chunkCoordinate);
      }

      if (var1 != null) {
         try {
            this.writeChunkNBTTags((AnvilChunkLoaderPending)var1);
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return true;
   }

   private void writeChunkNBTTags(AnvilChunkLoaderPending var1) {
      DataOutputStream var2 = RegionFileCache.getChunkOutputStream(this.chunkSaveLocation, var1.chunkCoordinate.chunkXPos, var1.chunkCoordinate.chunkZPos);
      CompressedStreamTools.write(var1.nbtTags, var2);
      var2.close();
   }

   @Override
   public void saveExtraChunkData(World var1, Chunk var2) {
   }

   @Override
   public void chunkTick() {
   }

   @Override
   public void saveExtraData() {
      while (this.writeNextIO()) {
      }
   }

   private void writeChunkToNBT(Chunk var1, World var2, NBTTagCompound var3) {
      var3.setInteger("xPos", var1.xPosition);
      var3.setInteger("zPos", var1.zPosition);
      var3.setLong("LastUpdate", var2.getTotalWorldTime());
      var3.setIntArray("HeightMap", var1.heightMap);
      var3.setBoolean("TerrainPopulated", var1.isTerrainPopulated);
      ExtendedBlockStorage[] var4 = var1.getBlockStorageArray();
      NBTTagList var5 = new NBTTagList("Sections");
      boolean var6 = !var2.provider.hasNoSky;

      for (ExtendedBlockStorage var10 : var4) {
         if (var10 != null) {
            NBTTagCompound var11 = new NBTTagCompound();
            var11.setByte("Y", (byte)(var10.getYLocation() >> 4 & 0xFF));
            var11.setByteArray("Blocks", var10.getBlockLSBArray());
            if (var10.getBlockMSBArray() != null) {
               var11.setByteArray("Add", var10.getBlockMSBArray().data);
            }

            var11.setByteArray("Data", var10.getMetadataArray().data);
            var11.setByteArray("BlockLight", var10.getBlocklightArray().data);
            if (var6) {
               var11.setByteArray("SkyLight", var10.getSkylightArray().data);
            } else {
               var11.setByteArray("SkyLight", new byte[var10.getBlocklightArray().data.length]);
            }

            var5.appendTag(var11);
         }
      }

      var3.setTag("Sections", var5);
      var3.setByteArray("Biomes", var1.getBiomeArray());
      var1.hasEntities = false;
      NBTTagList var16 = new NBTTagList();

      for (int var17 = 0; var17 < var1.entityLists.length; var17++) {
         for (Entity var22 : var1.entityLists[var17]) {
            NBTTagCompound var25 = new NBTTagCompound();
            if (var22.addEntityID(var25)) {
               var1.hasEntities = true;
               var16.appendTag(var25);
            }
         }
      }

      var3.setTag("Entities", var16);
      NBTTagList var18 = new NBTTagList();

      for (TileEntity var23 : var1.chunkTileEntityMap.values()) {
         NBTTagCompound var26 = new NBTTagCompound();
         var23.writeToNBT(var26);
         var18.appendTag(var26);
      }

      var3.setTag("TileEntities", var18);
      List var21 = var2.getPendingBlockUpdates(var1, false);
      if (var21 != null) {
         long var24 = var2.getTotalWorldTime();
         NBTTagList var12 = new NBTTagList();

         for (NextTickListEntry var14 : var21) {
            NBTTagCompound var15 = new NBTTagCompound();
            var15.setInteger("i", var14.blockID);
            var15.setInteger("x", var14.xCoord);
            var15.setInteger("y", var14.yCoord);
            var15.setInteger("z", var14.zCoord);
            var15.setInteger("t", (int)(var14.scheduledTime - var24));
            var15.setInteger("p", var14.field_82754_f);
            var12.appendTag(var15);
         }

         var3.setTag("TileTicks", var12);
      }
   }

   private Chunk readChunkFromNBT(World var1, NBTTagCompound var2) {
      int var3 = var2.getInteger("xPos");
      int var4 = var2.getInteger("zPos");
      Chunk var5 = new Chunk(var1, var3, var4);
      var5.heightMap = var2.getIntArray("HeightMap");
      var5.isTerrainPopulated = var2.getBoolean("TerrainPopulated");
      NBTTagList var6 = var2.getTagList("Sections");
      byte var7 = 16;
      ExtendedBlockStorage[] var8 = new ExtendedBlockStorage[var7];
      boolean var9 = !var1.provider.hasNoSky;

      for (int var10 = 0; var10 < var6.tagCount(); var10++) {
         NBTTagCompound var11 = (NBTTagCompound)var6.tagAt(var10);
         byte var12 = var11.getByte("Y");
         ExtendedBlockStorage var13 = new ExtendedBlockStorage(var12 << 4, var9);
         var13.setBlockLSBArray(var11.getByteArray("Blocks"));
         if (var11.hasKey("Add")) {
            var13.setBlockMSBArray(new NibbleArray(var11.getByteArray("Add"), 4));
         }

         var13.setBlockMetadataArray(new NibbleArray(var11.getByteArray("Data"), 4));
         var13.setBlocklightArray(new NibbleArray(var11.getByteArray("BlockLight"), 4));
         if (var9) {
            var13.setSkylightArray(new NibbleArray(var11.getByteArray("SkyLight"), 4));
         }

         var13.removeInvalidBlocks();
         var8[var12] = var13;
      }

      var5.setStorageArrays(var8);
      if (var2.hasKey("Biomes")) {
         var5.setBiomeArray(var2.getByteArray("Biomes"));
      }

      NBTTagList var17 = var2.getTagList("Entities");
      if (var17 != null) {
         for (int var18 = 0; var18 < var17.tagCount(); var18++) {
            NBTTagCompound var20 = (NBTTagCompound)var17.tagAt(var18);
            Entity var23 = EntityList.createEntityFromNBT(var20, var1);
            var5.hasEntities = true;
            if (var23 != null) {
               var5.addEntity(var23);
               Entity var14 = var23;

               for (NBTTagCompound var15 = var20; var15.hasKey("Riding"); var15 = var15.getCompoundTag("Riding")) {
                  Entity var16 = EntityList.createEntityFromNBT(var15.getCompoundTag("Riding"), var1);
                  if (var16 != null) {
                     var5.addEntity(var16);
                     var14.mountEntity(var16);
                  }

                  var14 = var16;
               }
            }
         }
      }

      NBTTagList var19 = var2.getTagList("TileEntities");
      if (var19 != null) {
         for (int var21 = 0; var21 < var19.tagCount(); var21++) {
            NBTTagCompound var24 = (NBTTagCompound)var19.tagAt(var21);
            TileEntity var26 = TileEntity.createAndLoadEntity(var24);
            if (var26 != null) {
               var5.addTileEntity(var26);
            }
         }
      }

      if (var2.hasKey("TileTicks")) {
         NBTTagList var22 = var2.getTagList("TileTicks");
         if (var22 != null) {
            for (int var25 = 0; var25 < var22.tagCount(); var25++) {
               NBTTagCompound var27 = (NBTTagCompound)var22.tagAt(var25);
               var1.scheduleBlockUpdateFromLoad(
                  var27.getInteger("x"), var27.getInteger("y"), var27.getInteger("z"), var27.getInteger("i"), var27.getInteger("t"), var27.getInteger("p")
               );
            }
         }
      }

      return var5;
   }
}
