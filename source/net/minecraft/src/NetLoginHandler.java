package net.minecraft.src;

import java.net.InetAddress;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.SecretKey;
import net.minecraft.server.MinecraftServer;

public class NetLoginHandler extends NetHandler {
   private static Random rand = new Random();
   private byte[] verifyToken;
   private final MinecraftServer mcServer;
   public final TcpConnection myTCPConnection;
   public boolean connectionComplete = false;
   private int connectionTimer = 0;
   private String clientUsername = null;
   private volatile boolean field_72544_i = false;
   private String loginServerId = "";
   private boolean field_92079_k = false;
   private SecretKey sharedKey = null;

   public NetLoginHandler(MinecraftServer var1, Socket var2, String var3) {
      this.mcServer = var1;
      this.myTCPConnection = new TcpConnection(var1.getLogAgent(), var2, var3, this, var1.getKeyPair().getPrivate());
      this.myTCPConnection.field_74468_e = 0;
   }

   public void tryLogin() {
      if (this.field_72544_i) {
         this.initializePlayerConnection();
      }

      if (this.connectionTimer++ == 600) {
         this.raiseErrorAndDisconnect("Took too long to log in");
      } else {
         this.myTCPConnection.processReadPackets();
      }
   }

   public void raiseErrorAndDisconnect(String var1) {
      try {
         this.mcServer.getLogAgent().logInfo("Disconnecting " + this.getUsernameAndAddress() + ": " + var1);
         this.myTCPConnection.addToSendQueue(new Packet255KickDisconnect(var1));
         this.myTCPConnection.serverShutdown();
         this.connectionComplete = true;
      } catch (Exception var3) {
         var3.printStackTrace();
      }
   }

   @Override
   public void handleClientProtocol(Packet2ClientProtocol var1) {
      this.clientUsername = var1.getUsername();
      if (!this.clientUsername.equals(StringUtils.stripControlCodes(this.clientUsername))) {
         this.raiseErrorAndDisconnect("Invalid username!");
      } else {
         PublicKey var2 = this.mcServer.getKeyPair().getPublic();
         if (var1.getProtocolVersion() != 61) {
            if (var1.getProtocolVersion() > 61) {
               this.raiseErrorAndDisconnect("Outdated server!");
            } else {
               this.raiseErrorAndDisconnect("Outdated client!");
            }
         } else {
            this.loginServerId = this.mcServer.isServerInOnlineMode() ? Long.toString(rand.nextLong(), 16) : "-";
            this.verifyToken = new byte[4];
            rand.nextBytes(this.verifyToken);
            this.myTCPConnection.addToSendQueue(new Packet253ServerAuthData(this.loginServerId, var2, this.verifyToken));
         }
      }
   }

   @Override
   public void handleSharedKey(Packet252SharedKey var1) {
      PrivateKey var2 = this.mcServer.getKeyPair().getPrivate();
      this.sharedKey = var1.getSharedKey(var2);
      if (!Arrays.equals(this.verifyToken, var1.getVerifyToken(var2))) {
         this.raiseErrorAndDisconnect("Invalid client reply");
      }

      this.myTCPConnection.addToSendQueue(new Packet252SharedKey());
   }

   @Override
   public void handleClientCommand(Packet205ClientCommand var1) {
      if (var1.forceRespawn == 0) {
         if (this.field_92079_k) {
            this.raiseErrorAndDisconnect("Duplicate login");
            return;
         }

         this.field_92079_k = true;
         if (this.mcServer.isServerInOnlineMode()) {
            new ThreadLoginVerifier(this).start();
         } else {
            this.field_72544_i = true;
         }
      }
   }

   @Override
   public void handleLogin(Packet1Login var1) {
   }

   public void initializePlayerConnection() {
      String var1 = this.mcServer.getConfigurationManager().allowUserToConnect(this.myTCPConnection.getSocketAddress(), this.clientUsername);
      if (var1 != null) {
         this.raiseErrorAndDisconnect(var1);
      } else {
         EntityPlayerMP var2 = this.mcServer.getConfigurationManager().createPlayerForUser(this.clientUsername);
         if (var2 != null) {
            this.mcServer.getConfigurationManager().initializeConnectionToPlayer(this.myTCPConnection, var2);
         }
      }

      this.connectionComplete = true;
   }

   @Override
   public void handleErrorMessage(String var1, Object[] var2) {
      this.mcServer.getLogAgent().logInfo(this.getUsernameAndAddress() + " lost connection");
      this.connectionComplete = true;
   }

   @Override
   public void handleServerPing(Packet254ServerPing var1) {
      try {
         ServerConfigurationManager var2 = this.mcServer.getConfigurationManager();
         String var3 = null;
         if (var1.readSuccessfully == 1) {
            for (Object var6 : Arrays.asList(
               1, 61, this.mcServer.getMinecraftVersion(), this.mcServer.getMOTD(), var2.getCurrentPlayerCount(), var2.getMaxPlayers()
            )) {
               if (var3 == null) {
                  var3 = "\u00a7";
               } else {
                  var3 = var3 + "\u0000";
               }

               var3 = var3 + var6.toString().replaceAll("\u0000", "");
            }
         } else {
            var3 = this.mcServer.getMOTD() + "\u00a7" + var2.getCurrentPlayerCount() + "\u00a7" + var2.getMaxPlayers();
         }

         InetAddress var9 = null;
         if (this.myTCPConnection.getSocket() != null) {
            var9 = this.myTCPConnection.getSocket().getInetAddress();
         }

         this.myTCPConnection.addToSendQueue(new Packet255KickDisconnect(var3));
         this.myTCPConnection.serverShutdown();
         if (var9 != null && this.mcServer.getNetworkThread() instanceof DedicatedServerListenThread) {
            ((DedicatedServerListenThread)this.mcServer.getNetworkThread()).func_71761_a(var9);
         }

         this.connectionComplete = true;
      } catch (Exception var7) {
         var7.printStackTrace();
      }
   }

   @Override
   public void unexpectedPacket(Packet var1) {
      this.raiseErrorAndDisconnect("Protocol error");
   }

   public String getUsernameAndAddress() {
      return this.clientUsername != null
         ? this.clientUsername + " [" + this.myTCPConnection.getSocketAddress().toString() + "]"
         : this.myTCPConnection.getSocketAddress().toString();
   }

   @Override
   public boolean isServerHandler() {
      return true;
   }
}
