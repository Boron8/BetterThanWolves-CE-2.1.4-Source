package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Packet52MultiBlockChange extends Packet {
   public int xPosition;
   public int zPosition;
   public byte[] metadataArray;
   public int size;
   private static byte[] field_73449_e = new byte[0];

   public Packet52MultiBlockChange() {
      this.isChunkDataPacket = true;
   }

   public Packet52MultiBlockChange(int var1, int var2, short[] var3, int var4, World var5) {
      this.isChunkDataPacket = true;
      this.xPosition = var1;
      this.zPosition = var2;
      this.size = var4;
      int var6 = 4 * var4;
      Chunk var7 = var5.getChunkFromChunkCoords(var1, var2);

      try {
         if (var4 >= 64) {
            this.field_98193_m.logInfo("ChunkTilesUpdatePacket compress " + var4);
            if (field_73449_e.length < var6) {
               field_73449_e = new byte[var6];
            }
         } else {
            ByteArrayOutputStream var8 = new ByteArrayOutputStream(var6);
            DataOutputStream var9 = new DataOutputStream(var8);

            for (int var10 = 0; var10 < var4; var10++) {
               int var11 = var3[var10] >> 12 & 15;
               int var12 = var3[var10] >> 8 & 15;
               int var13 = var3[var10] & 255;
               var9.writeShort(var3[var10]);
               var9.writeShort((short)((var7.getBlockID(var11, var13, var12) & 4095) << 4 | var7.getBlockMetadata(var11, var13, var12) & 15));
            }

            this.metadataArray = var8.toByteArray();
            if (this.metadataArray.length != var6) {
               throw new RuntimeException("Expected length " + var6 + " doesn't match received length " + this.metadataArray.length);
            }
         }
      } catch (IOException var14) {
         this.field_98193_m.logSevereException("Couldn't create chunk packet", var14);
         this.metadataArray = null;
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      this.xPosition = var1.readInt();
      this.zPosition = var1.readInt();
      this.size = var1.readShort() & '\uffff';
      int var2 = var1.readInt();
      if (var2 > 0) {
         this.metadataArray = new byte[var2];
         var1.readFully(this.metadataArray);
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeInt(this.xPosition);
      var1.writeInt(this.zPosition);
      var1.writeShort((short)this.size);
      if (this.metadataArray != null) {
         var1.writeInt(this.metadataArray.length);
         var1.write(this.metadataArray);
      } else {
         var1.writeInt(0);
      }
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleMultiBlockChange(this);
   }

   @Override
   public int getPacketSize() {
      return 10 + this.size * 4;
   }
}
