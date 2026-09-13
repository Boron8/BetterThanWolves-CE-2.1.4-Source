package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Packet60Explosion extends Packet {
   public double explosionX;
   public double explosionY;
   public double explosionZ;
   public float explosionSize;
   public List chunkPositionRecords;
   private float playerVelocityX;
   private float playerVelocityY;
   private float playerVelocityZ;

   public Packet60Explosion() {
   }

   public Packet60Explosion(double var1, double var3, double var5, float var7, List var8, Vec3 var9) {
      this.explosionX = var1;
      this.explosionY = var3;
      this.explosionZ = var5;
      this.explosionSize = var7;
      this.chunkPositionRecords = new ArrayList(var8);
      if (var9 != null) {
         this.playerVelocityX = (float)var9.xCoord;
         this.playerVelocityY = (float)var9.yCoord;
         this.playerVelocityZ = (float)var9.zCoord;
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.explosionX = var1.readDouble();
      this.explosionY = var1.readDouble();
      this.explosionZ = var1.readDouble();
      this.explosionSize = var1.readFloat();
      int var2 = var1.readInt();
      this.chunkPositionRecords = new ArrayList(var2);
      int var3 = (int)this.explosionX;
      int var4 = (int)this.explosionY;
      int var5 = (int)this.explosionZ;

      for (int var6 = 0; var6 < var2; var6++) {
         int var7 = var1.readByte() + var3;
         int var8 = var1.readByte() + var4;
         int var9 = var1.readByte() + var5;
         this.chunkPositionRecords.add(new ChunkPosition(var7, var8, var9));
      }

      this.playerVelocityX = var1.readFloat();
      this.playerVelocityY = var1.readFloat();
      this.playerVelocityZ = var1.readFloat();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeDouble(this.explosionX);
      var1.writeDouble(this.explosionY);
      var1.writeDouble(this.explosionZ);
      var1.writeFloat(this.explosionSize);
      var1.writeInt(this.chunkPositionRecords.size());
      int var2 = (int)this.explosionX;
      int var3 = (int)this.explosionY;
      int var4 = (int)this.explosionZ;

      for (ChunkPosition var6 : this.chunkPositionRecords) {
         int var7 = var6.x - var2;
         int var8 = var6.y - var3;
         int var9 = var6.z - var4;
         var1.writeByte(var7);
         var1.writeByte(var8);
         var1.writeByte(var9);
      }

      var1.writeFloat(this.playerVelocityX);
      var1.writeFloat(this.playerVelocityY);
      var1.writeFloat(this.playerVelocityZ);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleExplosion(this);
   }

   @Override
   public int getPacketSize() {
      return 32 + this.chunkPositionRecords.size() * 3 + 3;
   }

   public float getPlayerVelocityX() {
      return this.playerVelocityX;
   }

   public float getPlayerVelocityY() {
      return this.playerVelocityY;
   }

   public float getPlayerVelocityZ() {
      return this.playerVelocityZ;
   }
}
