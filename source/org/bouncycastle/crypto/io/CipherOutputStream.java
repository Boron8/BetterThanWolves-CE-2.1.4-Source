package org.bouncycastle.crypto.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;

public class CipherOutputStream extends FilterOutputStream {
   private BufferedBlockCipher theBufferedBlockCipher;
   private StreamCipher theStreamCipher;
   private byte[] oneByte = new byte[1];
   private byte[] buf;

   public CipherOutputStream(OutputStream var1, BufferedBlockCipher var2) {
      super(var1);
      this.theBufferedBlockCipher = var2;
      this.buf = new byte[var2.getBlockSize()];
   }

   @Override
   public void write(int var1) {
      this.oneByte[0] = (byte)var1;
      if (this.theBufferedBlockCipher != null) {
         int var2 = this.theBufferedBlockCipher.processByte(this.oneByte, 0, 1, this.buf, 0);
         if (var2 != 0) {
            this.out.write(this.buf, 0, var2);
         }
      } else {
         this.out.write(this.theStreamCipher.returnByte((byte)var1));
      }
   }

   @Override
   public void write(byte[] var1) {
      this.write(var1, 0, var1.length);
   }

   @Override
   public void write(byte[] var1, int var2, int var3) {
      if (this.theBufferedBlockCipher != null) {
         byte[] var4 = new byte[this.theBufferedBlockCipher.getOutputSize(var3)];
         int var5 = this.theBufferedBlockCipher.processByte(var1, var2, var3, var4, 0);
         if (var5 != 0) {
            this.out.write(var4, 0, var5);
         }
      } else {
         byte[] var6 = new byte[var3];
         this.theStreamCipher.processBytes(var1, var2, var3, var6, 0);
         this.out.write(var6, 0, var3);
      }
   }

   @Override
   public void flush() {
      super.flush();
   }

   @Override
   public void close() {
      try {
         if (this.theBufferedBlockCipher != null) {
            byte[] var1 = new byte[this.theBufferedBlockCipher.getOutputSize(0)];
            int var2 = this.theBufferedBlockCipher.doFinal(var1, 0);
            if (var2 != 0) {
               this.out.write(var1, 0, var2);
            }
         }
      } catch (Exception var3) {
         throw new IOException("Error closing stream: " + var3.toString());
      }

      this.flush();
      super.close();
   }
}
