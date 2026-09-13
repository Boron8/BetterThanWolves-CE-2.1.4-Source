package net.minecraft.src;

public interface IChunkLoader {
   Chunk loadChunk(World var1, int var2, int var3);

   void saveChunk(World var1, Chunk var2);

   void saveExtraChunkData(World var1, Chunk var2);

   void chunkTick();

   void saveExtraData();
}
