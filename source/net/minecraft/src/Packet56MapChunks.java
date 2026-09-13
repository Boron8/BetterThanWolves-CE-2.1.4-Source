package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packet56MapChunks extends Packet {
   private int[] chunkPostX;
   private int[] chunkPosZ;
   public int[] field_73590_a;
   public int[] field_73588_b;
   private byte[] chunkDataBuffer;
   private byte[][] field_73584_f;
   private int dataLength;
   private boolean skyLightSent;
   private static byte[] chunkDataNotCompressed = new byte[0];

   public Packet56MapChunks() {
   }

   public Packet56MapChunks(List var1) {
      int var2 = var1.size();
      this.chunkPostX = new int[var2];
      this.chunkPosZ = new int[var2];
      this.field_73590_a = new int[var2];
      this.field_73588_b = new int[var2];
      this.field_73584_f = new byte[var2][];
      this.skyLightSent = !var1.isEmpty() && !((Chunk)var1.get(0)).worldObj.provider.hasNoSky;
      int var3 = 0;

      for (int var4 = 0; var4 < var2; var4++) {
         Chunk var5 = (Chunk)var1.get(var4);
         Packet51MapChunkData var6 = Packet51MapChunk.getMapChunkData(var5, true, 65535);
         if (chunkDataNotCompressed.length < var3 + var6.compressedData.length) {
            byte[] var7 = new byte[var3 + var6.compressedData.length];
            System.arraycopy(chunkDataNotCompressed, 0, var7, 0, chunkDataNotCompressed.length);
            chunkDataNotCompressed = var7;
         }

         System.arraycopy(var6.compressedData, 0, chunkDataNotCompressed, var3, var6.compressedData.length);
         var3 += var6.compressedData.length;
         this.chunkPostX[var4] = var5.xPosition;
         this.chunkPosZ[var4] = var5.zPosition;
         this.field_73590_a[var4] = var6.chunkExistFlag;
         this.field_73588_b[var4] = var6.chunkHasAddSectionFlag;
         this.field_73584_f[var4] = var6.compressedData;
      }

      Deflater var11 = new Deflater(-1);

      try {
         var11.setInput(chunkDataNotCompressed, 0, var3);
         var11.finish();
         this.chunkDataBuffer = new byte[var3];
         this.dataLength = var11.deflate(this.chunkDataBuffer);
      } finally {
         var11.end();
      }
   }

   @Override
   public void readPacketData(DataInputStream var1) {
      short var2 = var1.readShort();
      this.dataLength = var1.readInt();
      this.skyLightSent = var1.readBoolean();
      this.chunkPostX = new int[var2];
      this.chunkPosZ = new int[var2];
      this.field_73590_a = new int[var2];
      this.field_73588_b = new int[var2];
      this.field_73584_f = new byte[var2][];
      if (chunkDataNotCompressed.length < this.dataLength) {
         chunkDataNotCompressed = new byte[this.dataLength];
      }

      var1.readFully(chunkDataNotCompressed, 0, this.dataLength);
      byte[] var3 = new byte[196864 * var2];
      Inflater var4 = new Inflater();
      var4.setInput(chunkDataNotCompressed, 0, this.dataLength);

      try {
         var4.inflate(var3);
      } catch (DataFormatException var12) {
         throw new IOException("Bad compressed data format");
      } finally {
         var4.end();
      }

      int var5 = 0;

      for (int var6 = 0; var6 < var2; var6++) {
         this.chunkPostX[var6] = var1.readInt();
         this.chunkPosZ[var6] = var1.readInt();
         this.field_73590_a[var6] = var1.readShort();
         this.field_73588_b[var6] = var1.readShort();
         int var7 = 0;
         int var8 = 0;

         for (int var9 = 0; var9 < 16; var9++) {
            var7 += this.field_73590_a[var6] >> var9 & 1;
            var8 += this.field_73588_b[var6] >> var9 & 1;
         }

         int var14 = 2048 * 4 * var7 + 256;
         var14 += 2048 * var8;
         if (this.skyLightSent) {
            var14 += 2048 * var7;
         }

         this.field_73584_f[var6] = new byte[var14];
         System.arraycopy(var3, var5, this.field_73584_f[var6], 0, var14);
         var5 += var14;
      }
   }

   @Override
   public void writePacketData(DataOutputStream var1) {
      var1.writeShort(this.chunkPostX.length);
      var1.writeInt(this.dataLength);
      var1.writeBoolean(this.skyLightSent);
      var1.write(this.chunkDataBuffer, 0, this.dataLength);

      for (int var2 = 0; var2 < this.chunkPostX.length; var2++) {
         var1.writeInt(this.chunkPostX[var2]);
         var1.writeInt(this.chunkPosZ[var2]);
         var1.writeShort((short)(this.field_73590_a[var2] & 65535));
         var1.writeShort((short)(this.field_73588_b[var2] & 65535));
      }
   }

   @Override
   public void processPacket(NetHandler var1) {
      var1.handleMapChunks(this);
   }

   @Override
   public int getPacketSize() {
      return 6 + this.dataLength + 12 * this.getNumberOfChunkInPacket();
   }

   public int getChunkPosX(int var1) {
      return this.chunkPostX[var1];
   }

   public int getChunkPosZ(int var1) {
      return this.chunkPosZ[var1];
   }

   public int getNumberOfChunkInPacket() {
      return this.chunkPostX.length;
   }

   public byte[] getChunkCompressedData(int var1) {
      return this.field_73584_f[var1];
   }
}
