package net.minecraft.src;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.SecretKey;

public class TcpConnection implements INetworkManager {
   public static AtomicInteger field_74471_a = new AtomicInteger();
   public static AtomicInteger field_74469_b = new AtomicInteger();
   private final Object sendQueueLock = new Object();
   private final ILogAgent field_98215_i;
   private Socket networkSocket;
   private final SocketAddress remoteSocketAddress;
   private volatile DataInputStream socketInputStream;
   private volatile DataOutputStream socketOutputStream;
   private volatile boolean isRunning = true;
   private volatile boolean isTerminating = false;
   private List readPackets = Collections.synchronizedList(new ArrayList());
   private List dataPackets = Collections.synchronizedList(new ArrayList());
   private List chunkDataPackets = Collections.synchronizedList(new ArrayList());
   private NetHandler theNetHandler;
   private boolean isServerTerminating = false;
   private Thread writeThread;
   private Thread readThread;
   private String terminationReason = "";
   private Object[] field_74480_w;
   private int field_74490_x = 0;
   private int sendQueueByteLength = 0;
   public static int[] field_74470_c = new int[256];
   public static int[] field_74467_d = new int[256];
   public int field_74468_e = 0;
   boolean isInputBeingDecrypted = false;
   boolean isOutputEncrypted = false;
   private SecretKey sharedKeyForEncryption = null;
   private PrivateKey field_74463_A = null;
   private int chunkDataPacketsDelay = 50;

   public TcpConnection(ILogAgent var1, Socket var2, String var3, NetHandler var4) {
      this(var1, var2, var3, var4, null);
   }

   public TcpConnection(ILogAgent var1, Socket var2, String var3, NetHandler var4, PrivateKey var5) {
      this.field_74463_A = var5;
      this.networkSocket = var2;
      this.field_98215_i = var1;
      this.remoteSocketAddress = var2.getRemoteSocketAddress();
      this.theNetHandler = var4;

      try {
         var2.setSoTimeout(30000);
         var2.setTrafficClass(24);
      } catch (SocketException var7) {
         System.err.println(var7.getMessage());
      }

      this.socketInputStream = new DataInputStream(var2.getInputStream());
      this.socketOutputStream = new DataOutputStream(new BufferedOutputStream(var2.getOutputStream(), 5120));
      this.readThread = new TcpReaderThread(this, var3 + " read thread");
      this.writeThread = new TcpWriterThread(this, var3 + " write thread");
      this.readThread.start();
      this.writeThread.start();
   }

   @Override
   public void closeConnections() {
      this.wakeThreads();
      this.writeThread = null;
      this.readThread = null;
   }

   @Override
   public void setNetHandler(NetHandler var1) {
      this.theNetHandler = var1;
   }

   @Override
   public void addToSendQueue(Packet var1) {
      if (!this.isServerTerminating) {
         synchronized (this.sendQueueLock) {
            this.sendQueueByteLength = this.sendQueueByteLength + var1.getPacketSize() + 1;
            this.dataPackets.add(var1);
         }
      }
   }

   private boolean sendPacket() {
      boolean var1 = false;

      try {
         if (this.field_74468_e == 0
            || !this.dataPackets.isEmpty() && System.currentTimeMillis() - ((Packet)this.dataPackets.get(0)).creationTimeMillis >= this.field_74468_e) {
            Packet var2 = this.func_74460_a(false);
            if (var2 != null) {
               Packet.writePacket(var2, this.socketOutputStream);
               if (var2 instanceof Packet252SharedKey && !this.isOutputEncrypted) {
                  if (!this.theNetHandler.isServerHandler()) {
                     this.sharedKeyForEncryption = ((Packet252SharedKey)var2).getSharedKey();
                  }

                  this.encryptOuputStream();
               }

               field_74467_d[var2.getPacketId()] += var2.getPacketSize() + 1;
               var1 = true;
            }
         }

         if (this.chunkDataPacketsDelay-- <= 0
            && (
               this.field_74468_e == 0
                  || !this.chunkDataPackets.isEmpty()
                     && System.currentTimeMillis() - ((Packet)this.chunkDataPackets.get(0)).creationTimeMillis >= this.field_74468_e
            )) {
            Packet var4 = this.func_74460_a(true);
            if (var4 != null) {
               Packet.writePacket(var4, this.socketOutputStream);
               field_74467_d[var4.getPacketId()] += var4.getPacketSize() + 1;
               this.chunkDataPacketsDelay = 0;
               var1 = true;
            }
         }

         return var1;
      } catch (Exception var3) {
         if (!this.isTerminating) {
            this.onNetworkError(var3);
         }

         return false;
      }
   }

   private Packet func_74460_a(boolean var1) {
      Packet var2 = null;
      List var3 = var1 ? this.chunkDataPackets : this.dataPackets;
      synchronized (this.sendQueueLock) {
         while (!var3.isEmpty() && var2 == null) {
            var2 = (Packet)var3.remove(0);
            this.sendQueueByteLength = this.sendQueueByteLength - (var2.getPacketSize() + 1);
            if (this.func_74454_a(var2, var1)) {
               var2 = null;
            }
         }

         return var2;
      }
   }

   private boolean func_74454_a(Packet var1, boolean var2) {
      if (!var1.isRealPacket()) {
         return false;
      } else {
         for (Packet var5 : var2 ? this.chunkDataPackets : this.dataPackets) {
            if (var5.getPacketId() == var1.getPacketId()) {
               return var1.containsSameEntityIDAs(var5);
            }
         }

         return false;
      }
   }

   @Override
   public void wakeThreads() {
      if (this.readThread != null) {
         this.readThread.interrupt();
      }

      if (this.writeThread != null) {
         this.writeThread.interrupt();
      }
   }

   private boolean readPacket() {
      boolean var1 = false;

      try {
         Packet var2 = Packet.readPacket(this.field_98215_i, this.socketInputStream, this.theNetHandler.isServerHandler(), this.networkSocket);
         if (var2 != null) {
            if (var2 instanceof Packet252SharedKey && !this.isInputBeingDecrypted) {
               if (this.theNetHandler.isServerHandler()) {
                  this.sharedKeyForEncryption = ((Packet252SharedKey)var2).getSharedKey(this.field_74463_A);
               }

               this.decryptInputStream();
            }

            field_74470_c[var2.getPacketId()] += var2.getPacketSize() + 1;
            if (!this.isServerTerminating) {
               if (var2.canProcessAsync() && this.theNetHandler.canProcessPacketsAsync()) {
                  this.field_74490_x = 0;
                  var2.processPacket(this.theNetHandler);
               } else {
                  this.readPackets.add(var2);
               }
            }

            var1 = true;
         } else {
            this.networkShutdown("disconnect.endOfStream");
         }

         return var1;
      } catch (Exception var3) {
         if (!this.isTerminating) {
            this.onNetworkError(var3);
         }

         return false;
      }
   }

   private void onNetworkError(Exception var1) {
      var1.printStackTrace();
      this.networkShutdown("disconnect.genericReason", "Internal exception: " + var1.toString());
   }

   @Override
   public void networkShutdown(String var1, Object... var2) {
      if (this.isRunning) {
         this.isTerminating = true;
         this.terminationReason = var1;
         this.field_74480_w = var2;
         this.isRunning = false;
         new TcpMasterThread(this).start();

         try {
            this.socketInputStream.close();
         } catch (Throwable var6) {
         }

         try {
            this.socketOutputStream.close();
         } catch (Throwable var5) {
         }

         try {
            this.networkSocket.close();
         } catch (Throwable var4) {
         }

         this.socketInputStream = null;
         this.socketOutputStream = null;
         this.networkSocket = null;
      }
   }

   @Override
   public void processReadPackets() {
      if (this.sendQueueByteLength > 2097152) {
         this.networkShutdown("disconnect.overflow");
      }

      if (this.readPackets.isEmpty()) {
         if (this.field_74490_x++ == 1200) {
            this.networkShutdown("disconnect.timeout");
         }
      } else {
         this.field_74490_x = 0;
      }

      int var1 = 1000;

      while (!this.readPackets.isEmpty() && var1-- >= 0) {
         Packet var2 = (Packet)this.readPackets.remove(0);
         var2.processPacket(this.theNetHandler);
      }

      this.wakeThreads();
      if (this.isTerminating && this.readPackets.isEmpty()) {
         this.theNetHandler.handleErrorMessage(this.terminationReason, this.field_74480_w);
      }
   }

   @Override
   public SocketAddress getSocketAddress() {
      return this.remoteSocketAddress;
   }

   @Override
   public void serverShutdown() {
      if (!this.isServerTerminating) {
         this.wakeThreads();
         this.isServerTerminating = true;
         this.readThread.interrupt();
         new TcpMonitorThread(this).start();
      }
   }

   private void decryptInputStream() {
      this.isInputBeingDecrypted = true;
      InputStream var1 = this.networkSocket.getInputStream();
      this.socketInputStream = new DataInputStream(CryptManager.decryptInputStream(this.sharedKeyForEncryption, var1));
   }

   private void encryptOuputStream() {
      this.socketOutputStream.flush();
      this.isOutputEncrypted = true;
      BufferedOutputStream var1 = new BufferedOutputStream(
         CryptManager.encryptOuputStream(this.sharedKeyForEncryption, this.networkSocket.getOutputStream()), 5120
      );
      this.socketOutputStream = new DataOutputStream(var1);
   }

   @Override
   public int packetSize() {
      return this.chunkDataPackets.size();
   }

   public Socket getSocket() {
      return this.networkSocket;
   }
}
