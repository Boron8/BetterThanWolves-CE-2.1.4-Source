package net.minecraft.src;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.MinecraftServer;

public abstract class NetworkListenThread {
   private final MinecraftServer mcServer;
   private final List connections = Collections.synchronizedList(new ArrayList());
   public volatile boolean isListening = false;

   public NetworkListenThread(MinecraftServer var1) {
      this.mcServer = var1;
      this.isListening = true;
   }

   public void addPlayer(NetServerHandler var1) {
      this.connections.add(var1);
   }

   public void stopListening() {
      this.isListening = false;
   }

   public void networkTick() {
      for (int var1 = 0; var1 < this.connections.size(); var1++) {
         NetServerHandler var2 = (NetServerHandler)this.connections.get(var1);

         try {
            var2.networkTick();
         } catch (Exception var5) {
            if (var2.netManager instanceof MemoryConnection) {
               CrashReport var4 = CrashReport.makeCrashReport(var5, "Ticking memory connection");
               throw new ReportedException(var4);
            }

            this.mcServer
               .getLogAgent()
               .logWarningException("Failed to handle packet for " + var2.playerEntity.am() + "/" + var2.playerEntity.getPlayerIP() + ": " + var5, var5);
            var2.kickPlayerFromServer("Internal server error");
         }

         if (var2.connectionClosed) {
            this.connections.remove(var1--);
         }

         var2.netManager.wakeThreads();
      }
   }

   public MinecraftServer getServer() {
      return this.mcServer;
   }
}
