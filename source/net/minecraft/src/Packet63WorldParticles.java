package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet63WorldParticles extends Packet {
   private String particleName;
   private float posX;
   private float posY;
   private float posZ;
   private float offsetX;
   private float offsetY;
   private float offsetZ;
   private float speed;
   private int quantity;

   @Override
   public void readPacketData(DataInputStream var1) {
      this.particleName = a(var1, 64);
      this.posX = var1.readFloat();
      this.posY = var1.readFloat();
      this.posZ = var1.readFloat();
      this.offsetX = var1.readFloat();
      this.offsetY = var1.readFloat();
      this.offsetZ = var1.readFloat();
      this.speed = var1.readFloat();
      this.quantity = var1.readInt();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      a(this.particleName, var1);
      var1.writeFloat(this.posX);
      var1.writeFloat(this.posY);
      var1.writeFloat(this.posZ);
      var1.writeFloat(this.offsetX);
      var1.writeFloat(this.offsetY);
      var1.writeFloat(this.offsetZ);
      var1.writeFloat(this.speed);
      var1.writeInt(this.quantity);
   }

   public String getParticleName() {
      return this.particleName;
   }

   public double getPositionX() {
      return this.posX;
   }

   public double getPositionY() {
      return this.posY;
   }

   public double getPositionZ() {
      return this.posZ;
   }

   public float getOffsetX() {
      return this.offsetX;
   }

   public float getOffsetY() {
      return this.offsetY;
   }

   public float getOffsetZ() {
      return this.offsetZ;
   }

   public float getSpeed() {
      return this.speed;
   }

   public int getQuantity() {
      return this.quantity;
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleWorldParticles(this);
   }

   @Override
   public int getPacketSize() {
      return 64;
   }
}
