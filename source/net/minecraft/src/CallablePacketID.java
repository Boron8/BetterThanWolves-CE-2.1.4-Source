package net.minecraft.src;

import java.util.concurrent.Callable;

class CallablePacketID implements Callable {
   CallablePacketID(NetServerHandler var1, Packet var2) {
      this.theNetServerHandler = var1;
      this.thePacket = var2;
   }

   public String callPacketID() {
      return String.valueOf(this.thePacket.getPacketId());
   }
}
