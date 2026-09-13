package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet132TileEntityData extends Packet {
   public int xPosition;
   public int yPosition;
   public int zPosition;
   public int actionType;
   public NBTTagCompound customParam1;

   public Packet132TileEntityData() {
      this.isChunkDataPacket = true;
   }

   public Packet132TileEntityData(int var1, int var2, int var3, int var4, NBTTagCompound var5) {
      this.isChunkDataPacket = true;
      this.xPosition = var1;
      this.yPosition = var2;
      this.zPosition = var3;
      this.actionType = var4;
      this.customParam1 = var5;
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.xPosition = var1.readInt();
      this.yPosition = var1.readShort();
      this.zPosition = var1.readInt();
      this.actionType = var1.readByte();
      this.customParam1 = d(var1);
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.xPosition);
      var1.writeShort(this.yPosition);
      var1.writeInt(this.zPosition);
      var1.writeByte((byte)this.actionType);
      a(this.customParam1, var1);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleTileEntityData(this);
   }

   @Override
   public int getPacketSize() {
      return 25;
   }
}
