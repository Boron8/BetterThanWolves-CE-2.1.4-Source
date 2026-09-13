package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet22Collect extends Packet {
   public int collectedEntityId;
   public int collectorEntityId;

   public Packet22Collect() {
   }

   public Packet22Collect(int var1, int var2) {
      this.collectedEntityId = var1;
      this.collectorEntityId = var2;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.collectedEntityId = var1.readInt();
      this.collectorEntityId = var1.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.collectedEntityId);
      var1.writeInt(this.collectorEntityId);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleCollect(this);
   }

   @Override
   public int getPacketSize() {
      return 8;
   }
}
