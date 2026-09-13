package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet106Transaction extends Packet {
   public int windowId;
   public short shortWindowId;
   public boolean accepted;

   public Packet106Transaction() {
   }

   public Packet106Transaction(int var1, short var2, boolean var3) {
      this.windowId = var1;
      this.shortWindowId = var2;
      this.accepted = var3;
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleTransaction(this);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.windowId = var1.readByte();
      this.shortWindowId = var1.readShort();
      this.accepted = var1.readByte() != 0;
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.windowId);
      var1.writeShort(this.shortWindowId);
      var1.writeByte(this.accepted ? 1 : 0);
   }

   @Override
   public int getPacketSize() {
      return 4;
   }
}
