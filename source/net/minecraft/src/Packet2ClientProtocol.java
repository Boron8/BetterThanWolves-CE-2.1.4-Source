package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet2ClientProtocol extends Packet {
   private int protocolVersion;
   private String username;
   private String serverHost;
   private int serverPort;

   public Packet2ClientProtocol() {
   }

   public Packet2ClientProtocol(int var1, String var2, String var3, int var4) {
      this.protocolVersion = var1;
      this.username = var2;
      this.serverHost = var3;
      this.serverPort = var4;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.protocolVersion = var1.readByte();
      this.username = a(var1, 16);
      this.serverHost = a(var1, 255);
      this.serverPort = var1.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.protocolVersion);
      a(this.username, var1);
      a(this.serverHost, var1);
      var1.writeInt(this.serverPort);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleClientProtocol(this);
   }

   @Override
   public int getPacketSize() {
      return 3 + 2 * this.username.length();
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }

   public String getUsername() {
      return this.username;
   }
}
