package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet4UpdateTime extends Packet {
   public long worldAge;
   public long time;

   public Packet4UpdateTime() {
   }

   public Packet4UpdateTime(long var1, long var3) {
      this.worldAge = var1;
      this.time = var3;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.worldAge = var1.readLong();
      this.time = var1.readLong();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeLong(this.worldAge);
      var1.writeLong(this.time);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleUpdateTime(this);
   }

   @Override
   public int getPacketSize() {
      return 16;
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
