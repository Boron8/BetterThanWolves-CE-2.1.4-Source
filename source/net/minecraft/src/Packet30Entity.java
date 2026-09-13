package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet30Entity extends Packet {
   public int entityId;
   public byte xPosition;
   public byte yPosition;
   public byte zPosition;
   public byte yaw;
   public byte pitch;
   public boolean rotating = false;

   public Packet30Entity() {
   }

   public Packet30Entity(int var1) {
      this.entityId = var1;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = var1.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityId);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleEntity(this);
   }

   @Override
   public int getPacketSize() {
      return 4;
   }

   @Override
   public String toString() {
      return "Entity_" + super.toString();
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      Packet30Entity var2 = (Packet30Entity)var1;
      return var2.entityId == this.entityId;
   }
}
