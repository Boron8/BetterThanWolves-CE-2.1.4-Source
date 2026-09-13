package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableServerProfiler implements Callable {
   public CallableServerProfiler(MinecraftServer var1) {
      this.mcServer = var1;
   }

   public String callServerProfiler() {
      int var1 = this.mcServer.worldServers[0].U().getPoolSize();
      int var2 = 56 * var1;
      int var3 = var2 / 1024 / 1024;
      int var4 = this.mcServer.worldServers[0].U().func_82590_d();
      int var5 = 56 * var4;
      int var6 = var5 / 1024 / 1024;
      return var1 + " (" + var2 + " bytes; " + var3 + " MB) allocated, " + var4 + " (" + var5 + " bytes; " + var6 + " MB) used";
   }
}
