package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet205ClientCommand extends Packet {
   public int forceRespawn;

   public Packet205ClientCommand() {
   }

   public Packet205ClientCommand(int var1) {
      this.forceRespawn = var1;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.forceRespawn = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.forceRespawn & 0xFF);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleClientCommand(this);
   }

   @Override
   public int getPacketSize() {
      return 1;
   }
}
