package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet8UpdateHealth extends Packet {
   public int healthMP;
   public int food;
   public float foodSaturation;

   public Packet8UpdateHealth() {
   }

   public Packet8UpdateHealth(int var1, int var2, float var3) {
      this.healthMP = var1;
      this.food = var2;
      this.foodSaturation = var3;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.healthMP = var1.readShort();
      this.food = var1.readShort();
      this.foodSaturation = var1.readFloat();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeShort(this.healthMP);
      var1.writeShort(this.food);
      var1.writeFloat(this.foodSaturation);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleUpdateHealth(this);
   }

   @Override
   public int getPacketSize() {
      return 8;
   }

   @Override
   public boolean isRealPacket() {
      return true;
   }

   @Override
   public boolean containsSameEntityIDAs(Packet var1) {
      return true;
   }
}
