package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet255KickDisconnect extends Packet {
   public String reason;

   public Packet255KickDisconnect() {
   }

   public Packet255KickDisconnect(String var1) {
      this.reason = var1;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.reason = a(var1, 256);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.reason, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleKickDisconnect(this);
   }

   @Override
   public int getPacketSize() {
      return this.reason.length();
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      return true;
   }
}
