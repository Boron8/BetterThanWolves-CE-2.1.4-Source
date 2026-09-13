package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet70GameEvent extends Packet {
   public static final String[] clientMessage = new String[]{"tile.bed.notValid", null, null, "gameMode.changed"};
   public int eventType;
   public int gameMode;

   public Packet70GameEvent() {
   }

   public Packet70GameEvent(int var1, int var2) {
      this.eventType = var1;
      this.gameMode = var2;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.eventType = var1.readByte();
      this.gameMode = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.eventType);
      var1.writeByte(this.gameMode);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleGameEvent(this);
   }

   @Override
   public int getPacketSize() {
      return 2;
   }
}
