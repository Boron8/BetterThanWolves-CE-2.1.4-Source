package net.minecraft.src;

import java.util.concurrent.Callable;

class CallablePacketClass implements Callable {
   CallablePacketClass(NetServerHandler var1, Packet var2) {
      this.theNetServerHandler = var1;
      this.thePacket = var2;
   }

   public String getPacketClass() {
      return this.thePacket.getClass().getCanonicalName();
   }
}
