package net.minecraft.src;

import java.util.concurrent.Callable;
import net.minecraft.server.MinecraftServer;

public class CallableIsServerModded implements Callable {
   public CallableIsServerModded(MinecraftServer var1) {
      this.mcServer = var1;
   }

   public String func_96558_a() {
      return this.mcServer.theProfiler.profilingEnabled ? this.mcServer.theProfiler.getNameOfLastSection() : "N/A (disabled)";
   }
}
