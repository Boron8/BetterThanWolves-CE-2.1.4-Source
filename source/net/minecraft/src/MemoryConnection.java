package net.minecraft.src;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryConnection implements INetworkManager {
   private static final SocketAddress mySocketAddress = new InetSocketAddress("127.0.0.1", 0);
   private final List readPacketCache = Collections.synchronizedList(new ArrayList());
   private final ILogAgent field_98214_c;
   private MemoryConnection pairedConnection;
   private NetHandler myNetHandler;
   private boolean shuttingDown = false;
   private String shutdownReason = "";
   private Object[] field_74439_g;
   private boolean gamePaused = false;

   public MemoryConnection(ILogAgent var1, NetHandler var2) {
      this.myNetHandler = var2;
      this.field_98214_c = var1;
   }

   @Override
   public void setNetHandler(NetHandler var1) {
      this.myNetHandler = var1;
   }

   @Override
   public void addToSendQueue(Packet var1) {
      if (!this.shuttingDown) {
         this.pairedConnection.processOrCachePacket(var1);
      }
   }

   @Override
   public void closeConnections() {
      this.pairedConnection = null;
      this.myNetHandler = null;
   }

   public boolean isConnectionActive() {
      return !this.shuttingDown && this.pairedConnection != null;
   }

   @Override
   public void wakeThreads() {
   }

   @Override
   public void processReadPackets() {
      int var1 = 2500;

      while (var1-- >= 0 && !this.readPacketCache.isEmpty()) {
         Packet var2 = (Packet)this.readPacketCache.remove(0);
         var2.processPacket(this.myNetHandler);
      }

      if (this.readPacketCache.size() > var1) {
         this.field_98214_c
            .logWarning("Memory connection overburdened; after processing 2500 packets, we still have " + this.readPacketCache.size() + " to go!");
      }

      if (this.shuttingDown && this.readPacketCache.isEmpty()) {
         this.myNetHandler.handleErrorMessage(this.shutdownReason, this.field_74439_g);
      }
   }

   @Override
   public SocketAddress getSocketAddress() {
      return mySocketAddress;
   }

   @Override
   public void serverShutdown() {
      this.shuttingDown = true;
   }

   @Override
   public void networkShutdown(String var1, Object... var2) {
      this.shuttingDown = true;
      this.shutdownReason = var1;
      this.field_74439_g = var2;
   }

   @Override
   public int packetSize() {
      return 0;
   }

   public void pairWith(MemoryConnection var1) {
      this.pairedConnection = var1;
      var1.pairedConnection = this;
   }

   public boolean isGamePaused() {
      return this.gamePaused;
   }

   public void setGamePaused(boolean var1) {
      this.gamePaused = var1;
   }

   public MemoryConnection getPairedConnection() {
      return this.pairedConnection;
   }

   public void processOrCachePacket(Packet var1) {
      if (var1.canProcessAsync() && this.myNetHandler.canProcessPacketsAsync()) {
         var1.processPacket(this.myNetHandler);
      } else {
         this.readPacketCache.add(var1);
      }
   }
}
