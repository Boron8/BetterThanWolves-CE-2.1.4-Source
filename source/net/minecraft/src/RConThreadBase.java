package net.minecraft.src;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public abstract class RConThreadBase implements Runnable {
   protected boolean running = false;
   protected IServer server;
   protected Thread rconThread;
   protected int field_72615_d = 5;
   protected List socketList = new ArrayList();
   protected List serverSocketList = new ArrayList();

   RConThreadBase(IServer var1) {
      this.server = var1;
      if (this.server.isDebuggingEnabled()) {
         this.logWarning("Debugging is enabled, performance maybe reduced!");
      }
   }

   public synchronized void startThread() {
      this.rconThread = new Thread(this);
      this.rconThread.start();
      this.running = true;
   }

   public boolean isRunning() {
      return this.running;
   }

   protected void logDebug(String var1) {
      this.server.logDebug(var1);
   }

   protected void logInfo(String var1) {
      this.server.logInfo(var1);
   }

   protected void logWarning(String var1) {
      this.server.logWarning(var1);
   }

   protected void logSevere(String var1) {
      this.server.logSevere(var1);
   }

   protected int getNumberOfPlayers() {
      return this.server.getCurrentPlayerCount();
   }

   protected void registerSocket(DatagramSocket var1) {
      this.logDebug("registerSocket: " + var1);
      this.socketList.add(var1);
   }

   protected boolean closeSocket(DatagramSocket var1, boolean var2) {
      this.logDebug("closeSocket: " + var1);
      if (null == var1) {
         return false;
      } else {
         boolean var3 = false;
         if (!var1.isClosed()) {
            var1.close();
            var3 = true;
         }

         if (var2) {
            this.socketList.remove(var1);
         }

         return var3;
      }
   }

   protected boolean closeServerSocket(ServerSocket var1) {
      return this.closeServerSocket_do(var1, true);
   }

   protected boolean closeServerSocket_do(ServerSocket var1, boolean var2) {
      this.logDebug("closeSocket: " + var1);
      if (null == var1) {
         return false;
      } else {
         boolean var3 = false;

         try {
            if (!var1.isClosed()) {
               var1.close();
               var3 = true;
            }
         } catch (IOException var5) {
            this.logWarning("IO: " + var5.getMessage());
         }

         if (var2) {
            this.serverSocketList.remove(var1);
         }

         return var3;
      }
   }

   protected void closeAllSockets() {
      this.closeAllSockets_do(false);
   }

   protected void closeAllSockets_do(boolean var1) {
      int var2 = 0;

      for (DatagramSocket var4 : this.socketList) {
         if (this.closeSocket(var4, false)) {
            var2++;
         }
      }

      this.socketList.clear();

      for (ServerSocket var6 : this.serverSocketList) {
         if (this.closeServerSocket_do(var6, false)) {
            var2++;
         }
      }

      this.serverSocketList.clear();
      if (var1 && 0 < var2) {
         this.logWarning("Force closed " + var2 + " sockets");
      }
   }
}
