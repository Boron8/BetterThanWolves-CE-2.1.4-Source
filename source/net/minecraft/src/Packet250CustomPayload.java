package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet250CustomPayload extends Packet {
   public String channel;
   public int length;
   public byte[] data;

   public Packet250CustomPayload() {
   }

   public Packet250CustomPayload(String var1, byte[] var2) {
      this.channel = var1;
      this.data = var2;
      if (var2 != null) {
         this.length = var2.length;
         if (this.length > 32767) {
            throw new IllegalArgumentException("Payload may not be larger than 32k");
         }
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.channel = a(var1, 20);
      this.length = var1.readShort();
      if (this.length > 0 && this.length < 32767) {
         this.data = new byte[this.length];
         var1.readFully(this.data);
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.channel, var1);
      var1.writeShort((short)this.length);
      if (this.data != null) {
         var1.write(this.data);
      }
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleCustomPayload(this);
   }

   @Override
   public int getPacketSize() {
      return 2 + this.channel.length() * 2 + 2 + this.length;
   }
}
