package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet23VehicleSpawn extends Packet {
   public int entityId;
   public int xPosition;
   public int yPosition;
   public int zPosition;
   public int speedX;
   public int speedY;
   public int speedZ;
   public int pitch;
   public int yaw;
   public int type;
   public int throwerEntityId;

   public Packet23VehicleSpawn() {
   }

   public Packet23VehicleSpawn(Entity var1, int var2) {
      this(var1, var2, 0);
   }

   public Packet23VehicleSpawn(Entity var1, int var2, int var3) {
      this.entityId = var1.entityId;
      this.xPosition = MathHelper.floor_double(var1.posX * 32.0);
      this.yPosition = MathHelper.floor_double(var1.posY * 32.0);
      this.zPosition = MathHelper.floor_double(var1.posZ * 32.0);
      this.pitch = MathHelper.floor_float(var1.rotationPitch * 256.0F / 360.0F);
      this.yaw = MathHelper.floor_float(var1.rotationYaw * 256.0F / 360.0F);
      this.type = var2;
      this.throwerEntityId = var3;
      if (var3 > 0) {
         double var4 = var1.motionX;
         double var6 = var1.motionY;
         double var8 = var1.motionZ;
         double var10 = 3.9;
         if (var4 < -var10) {
            var4 = -var10;
         }

         if (var6 < -var10) {
            var6 = -var10;
         }

         if (var8 < -var10) {
            var8 = -var10;
         }

         if (var4 > var10) {
            var4 = var10;
         }

         if (var6 > var10) {
            var6 = var10;
         }

         if (var8 > var10) {
            var8 = var10;
         }

         this.speedX = (int)(var4 * 8000.0);
         this.speedY = (int)(var6 * 8000.0);
         this.speedZ = (int)(var8 * 8000.0);
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.entityId = var1.readInt();
      this.type = var1.readByte();
      this.xPosition = var1.readInt();
      this.yPosition = var1.readInt();
      this.zPosition = var1.readInt();
      this.pitch = var1.readByte();
      this.yaw = var1.readByte();
      this.throwerEntityId = var1.readInt();
      if (this.throwerEntityId > 0) {
         this.speedX = var1.readShort();
         this.speedY = var1.readShort();
         this.speedZ = var1.readShort();
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.entityId);
      var1.writeByte(this.type);
      var1.writeInt(this.xPosition);
      var1.writeInt(this.yPosition);
      var1.writeInt(this.zPosition);
      var1.writeByte(this.pitch);
      var1.writeByte(this.yaw);
      var1.writeInt(this.throwerEntityId);
      if (this.throwerEntityId > 0) {
         var1.writeShort(this.speedX);
         var1.writeShort(this.speedY);
         var1.writeShort(this.speedZ);
      }
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleVehicleSpawn(this);
   }

   @Override
   public int getPacketSize() {
      return 21 + this.throwerEntityId > 0 ? 6 : 0;
   }
}
