package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class WorldServerMulti extends WorldServer {
   public WorldServerMulti(MinecraftServer var1, ISaveHandler var2, String var3, int var4, WorldSettings var5, WorldServer var6, Profiler var7, ILogAgent var8) {
      super(var1, var2, var3, var4, var5, var7, var8);
      this.mapStorage = var6.mapStorage;
      this.worldScoreboard = var6.W();
      this.worldInfo = new DerivedWorldInfo(var6.M());
   }

   @Override
   protected void saveLevel() {
   }
}
