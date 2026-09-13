package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet35EntityHeadRotation extends Packet {
   public int entityId;
   public byte headRotationYaw;

   public Packet35EntityHeadRotation() {
   }

   public Packet35EntityHeadRotation(int var1, byte var2) {
      this.entityId = var1;
      this.headRotationYaw = var2;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = var1.readInt();
      this.headRotationYaw = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityId);
      var1.writeByte(this.headRotationYaw);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleEntityHeadRotation(this);
   }

   @Override
   public int getPacketSize() {
      return 5;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      Packet35EntityHeadRotation var2 = (Packet35EntityHeadRotation)var1;
      return var2.entityId == this.entityId;
   }

   @Override
   public boolean canProcessAsync() {
      return true;
   }
}
