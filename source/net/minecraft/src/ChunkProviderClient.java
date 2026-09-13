package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class ChunkProviderClient implements IChunkProvider {
   private Chunk blankChunk;
   private LongHashMap chunkMapping = new LongHashMap();
   private List chunkListing = new ArrayList();
   private World worldObj;

   public ChunkProviderClient(World var1) {
      this.blankChunk = new EmptyChunk(var1, 0, 0);
      this.worldObj = var1;
   }

   @Override
   public boolean chunkExists(int var1, int var2) {
      return true;
   }

   public void unloadChunk(int var1, int var2) {
      Chunk var3 = this.provideChunk(var1, var2);
      if (!var3.isEmpty()) {
         var3.onChunkUnload();
      }

      this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
      this.chunkListing.remove(var3);
   }

   @Override
   public Chunk loadChunk(int var1, int var2) {
      Chunk var3 = new Chunk(this.worldObj, var1, var2);
      this.chunkMapping.add(ChunkCoordIntPair.chunkXZ2Int(var1, var2), var3);
      var3.isChunkLoaded = true;
      return var3;
   }

   @Override
   public Chunk provideChunk(int var1, int var2) {
      Chunk var3 = (Chunk)this.chunkMapping.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(var1, var2));
      return var3 == null ? this.blankChunk : var3;
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
      return false;
   }

   @Override
   public void populate(IChunkProvider var1, int var2, int var3) {
   }

   @Override
   public String makeString() {
      return "MultiplayerChunkCache: " + this.chunkMapping.getNumHashElements();
   }

   @Override
   public List getPossibleCreatures(EnumCreatureType var1, int var2, int var3, int var4) {
      return null;
   }

   @Override
   public ChunkPosition findClosestStructure(World var1, String var2, int var3, int var4, int var5) {
      return null;
   }

   @Override
   public int getLoadedChunkCount() {
      return this.chunkListing.size();
   }

   @Override
   public void recreateStructures(int var1, int var2) {
   }
}
