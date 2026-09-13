package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet29DestroyEntity extends Packet {
   public int[] entityId;

   public Packet29DestroyEntity() {
   }

   public Packet29DestroyEntity(int... var1) {
      this.entityId = var1;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = new int[var1.readByte()];

      for (int var2 = 0; var2 < this.entityId.length; var2++) {
         this.entityId[var2] = var1.readInt();
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeByte(this.entityId.length);

      for (int var2 = 0; var2 < this.entityId.length; var2++) {
         var1.writeInt(this.entityId[var2]);
      }
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleDestroyEntity(this);
   }

   @Override
   public int getPacketSize() {
      return 1 + this.entityId.length * 4;
   }
}
