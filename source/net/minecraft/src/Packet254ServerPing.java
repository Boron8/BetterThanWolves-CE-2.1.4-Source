package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet254ServerPing extends Packet {
   public int readSuccessfully = 0;

   @Override
   public void readPacketData(DataInputStream var1) {
      try {
         this.readSuccessfully = var1.readByte();
      } catch (Throwable var3) {
         this.readSuccessfully = 0;
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleServerPing(this);
   }

   @Override
   public int getPacketSize() {
      return 0;
   }
}
