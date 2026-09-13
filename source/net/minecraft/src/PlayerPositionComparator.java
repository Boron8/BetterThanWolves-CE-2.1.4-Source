package net.minecraft.src;

import java.util.Comparator;

public class PlayerPositionComparator implements Comparator {
   private final ChunkCoordinates theChunkCoordinates;

   public PlayerPositionComparator(ChunkCoordinates var1) {
      this.theChunkCoordinates = var1;
   }

   public int comparePlayers(EntityPlayerMP var1, EntityPlayerMP var2) {
      double var3 = var1.e(this.theChunkCoordinates.posX, this.theChunkCoordinates.posY, this.theChunkCoordinates.posZ);
      double var5 = var2.e(this.theChunkCoordinates.posX, this.theChunkCoordinates.posY, this.theChunkCoordinates.posZ);
      if (var3 < var5) {
         return -1;
      } else {
         return var3 > var5 ? 1 : 0;
      }
   }
}
