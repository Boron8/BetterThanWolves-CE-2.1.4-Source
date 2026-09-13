package net.minecraft.src;

public class PortalPosition extends ChunkCoordinates {
   public long lastUpdateTime;

   public PortalPosition(Teleporter var1, int var2, int var3, int var4, long var5) {
      super(var2, var3, var4);
      this.teleporterInstance = var1;
      this.lastUpdateTime = var5;
   }
}
