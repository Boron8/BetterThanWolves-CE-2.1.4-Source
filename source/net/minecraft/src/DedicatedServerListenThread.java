package net.minecraft.src;

import java.net.InetAddress;
import net.minecraft.server.MinecraftServer;

public class DedicatedServerListenThread extends NetworkListenThread {
   private final ServerListenThread theServerListenThread;

   public DedicatedServerListenThread(MinecraftServer var1, InetAddress var2, int var3) {
      super(var1);
      this.theServerListenThread = new ServerListenThread(this, var2, var3);
      this.theServerListenThread.start();
   }

   @Override
   public void stopListening() {
      super.stopListening();
      this.theServerListenThread.func_71768_b();
      this.theServerListenThread.interrupt();
   }

   @Override
   public void networkTick() {
      this.theServerListenThread.processPendingConnections();
      super.networkTick();
   }

   public DedicatedServer getDedicatedServer() {
      return (DedicatedServer)super.getServer();
   }

   public void func_71761_a(InetAddress var1) {
      this.theServerListenThread.func_71769_a(var1);
   }
}
