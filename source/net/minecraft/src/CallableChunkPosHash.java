package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableChunkPosHash implements Callable {
   CallableChunkPosHash(MapGenStructure var1, int var2, int var3) {
      this.theMapStructureGenerator = var1;
      this.field_85165_a = var2;
      this.field_85163_b = var3;
   }

   public String callChunkPositionHash() {
      return String.valueOf(ChunkCoordIntPair.chunkXZ2Int(this.field_85165_a, this.field_85163_b));
   }
}
