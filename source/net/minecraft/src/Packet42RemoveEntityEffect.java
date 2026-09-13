package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet42RemoveEntityEffect extends Packet {
   public int entityId;
   public byte effectId;

   public Packet42RemoveEntityEffect() {
   }

   public Packet42RemoveEntityEffect(int var1, PotionEffect var2) {
      this.entityId = var1;
      this.effectId = (byte)(var2.getPotionID() & 0xFF);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = var1.readInt();
      this.effectId = var1.readByte();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityId);
      var1.writeByte(this.effectId);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleRemoveEntityEffect(this);
   }

   @Override
   public int getPacketSize() {
      return 5;
   }
}
