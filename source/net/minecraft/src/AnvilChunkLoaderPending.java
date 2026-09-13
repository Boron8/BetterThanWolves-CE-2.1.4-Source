package net.minecraft.src;

class AnvilChunkLoaderPending {
   public final ChunkCoordIntPair chunkCoordinate;
   public final NBTTagCompound nbtTags;

   public AnvilChunkLoaderPending(ChunkCoordIntPair var1, NBTTagCompound var2) {
      this.chunkCoordinate = var1;
      this.nbtTags = var2;
   }
}
