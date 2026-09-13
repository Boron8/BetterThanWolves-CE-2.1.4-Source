package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet0KeepAlive extends Packet {
   public int randomId;

   public Packet0KeepAlive() {
   }

   public Packet0KeepAlive(int var1) {
      this.randomId = var1;
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleKeepAlive(this);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.randomId = var1.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.randomId);
   }

   @Override
   public int getPacketSize() {
      return 4;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      return true;
   }

   @Override
   public boolean canProcessAsync() {
      return true;
   }
}
