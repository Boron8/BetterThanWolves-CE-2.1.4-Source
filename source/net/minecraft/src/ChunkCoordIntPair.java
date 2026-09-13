package net.minecraft.src;

public class ChunkCoordIntPair {
   public final int chunkXPos;
   public final int chunkZPos;

   public ChunkCoordIntPair(int var1, int var2) {
      this.chunkXPos = var1;
      this.chunkZPos = var2;
   }

   public static long chunkXZ2Int(int var0, int var1) {
      return var0 & 4294967295L | (var1 & 4294967295L) << 32;
   }

   @Override
   public int hashCode() {
      long var1 = chunkXZ2Int(this.chunkXPos, this.chunkZPos);
      int var3 = (int)var1;
      int var4 = (int)(var1 >> 32);
      return var3 ^ var4;
   }

   @Override
   public boolean equals(Object var1) {
      ChunkCoordIntPair var2 = (ChunkCoordIntPair)var1;
      return var2.chunkXPos == this.chunkXPos && var2.chunkZPos == this.chunkZPos;
   }

   public int getCenterXPos() {
      return (this.chunkXPos << 4) + 8;
   }

   public int getCenterZPosition() {
      return (this.chunkZPos << 4) + 8;
   }

   public ChunkPosition getChunkPosition(int var1) {
      return new ChunkPosition(this.getCenterXPos(), var1, this.getCenterZPosition());
   }

   @Override
   public String toString() {
      return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
   }
}
