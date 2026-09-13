package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet53BlockChange extends Packet {
   public int xPosition;
   public int yPosition;
   public int zPosition;
   public int type;
   public int metadata;

   public Packet53BlockChange() {
      this.isChunkDataPacket = true;
   }

   public Packet53BlockChange(int var1, int var2, int var3, World var4) {
      this.isChunkDataPacket = true;
      this.xPosition = var1;
      this.yPosition = var2;
      this.zPosition = var3;
      this.type = var4.getBlockId(var1, var2, var3);
      this.metadata = var4.getBlockMetadata(var1, var2, var3);
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.xPosition = var1.readInt();
      this.yPosition = var1.read();
      this.zPosition = var1.readInt();
      this.type = var1.readShort();
      this.metadata = var1.read();
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.xPosition);
      var1.write(this.yPosition);
      var1.writeInt(this.zPosition);
      var1.writeShort(this.type);
      var1.write(this.metadata);
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleBlockChange(this);
   }

   @Override
   public int getPacketSize() {
      return 11;
   }
}
