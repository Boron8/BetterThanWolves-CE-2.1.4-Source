package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class ThreadMinecraftServer extends Thread {
   public ThreadMinecraftServer(MinecraftServer var1, String var2) {
      super(var2);
      this.theServer = var1;
   }

   @Override
   public void run() {
      this.theServer.run();
   }
}
