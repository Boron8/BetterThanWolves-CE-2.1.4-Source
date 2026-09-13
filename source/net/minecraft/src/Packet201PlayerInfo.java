package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet201PlayerInfo extends Packet {
   public String playerName;
   public boolean isConnected;
   public int ping;

   public Packet201PlayerInfo() {
   }

   public Packet201PlayerInfo(String var1, boolean var2, int var3) {
      this.playerName = var1;
      this.isConnected = var2;
      this.ping = var3;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.playerName = a(var1, 16);
      this.isConnected = var1.readByte() != 0;
      this.ping = var1.readShort();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.playerName, var1);
      var1.writeByte(this.isConnected ? 1 : 0);
      var1.writeShort(this.ping);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handlePlayerInfo(this);
   }

   @Override
   public int getPacketSize() {
      return this.playerName.length() + 2 + 1 + 2;
   }
}
