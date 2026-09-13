package org.bouncycastle.crypto.io;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.StreamCipher;

public class CipherInputStream extends FilterInputStream {
   private BufferedBlockCipher theBufferedBlockCipher;
   private StreamCipher theStreamCipher;
   private byte[] buf;
   private byte[] inBuf;
   private int bufOff;
   private int maxBuf;
   private boolean finalized;

   public CipherInputStream(InputStream var1, BufferedBlockCipher var2) {
      super(var1);
      this.theBufferedBlockCipher = var2;
      this.buf = new byte[var2.getOutputSize(2048)];
      this.inBuf = new byte[2048];
   }

   private int nextChunk() {
      int var1 = super.available();
      if (var1 <= 0) {
         var1 = 1;
      }

      if (var1 > this.inBuf.length) {
         var1 = super.read(this.inBuf, 0, this.inBuf.length);
      } else {
         var1 = super.read(this.inBuf, 0, var1);
      }

      if (var1 < 0) {
         if (this.finalized) {
            return -1;
         }

         try {
            if (this.theBufferedBlockCipher != null) {
               this.maxBuf = this.theBufferedBlockCipher.doFinal(this.buf, 0);
            } else {
               this.maxBuf = 0;
            }
         } catch (Exception var4) {
            throw new IOException("error processing stream: " + var4.toString());
         }

         this.bufOff = 0;
         this.finalized = true;
         if (this.bufOff == this.maxBuf) {
            return -1;
         }
      } else {
         this.bufOff = 0;

         try {
            if (this.theBufferedBlockCipher != null) {
               this.maxBuf = this.theBufferedBlockCipher.processByte(this.inBuf, 0, var1, this.buf, 0);
            } else {
               this.theStreamCipher.processBytes(this.inBuf, 0, var1, this.buf, 0);
               this.maxBuf = var1;
            }
         } catch (Exception var3) {
            throw new IOException("error processing stream: " + var3.toString());
         }

         if (this.maxBuf == 0) {
            return this.nextChunk();
         }
      }

      return this.maxBuf;
   }

   @Override
   public int read() {
      return this.bufOff == this.maxBuf && this.nextChunk() < 0 ? -1 : this.buf[this.bufOff++] & 0xFF;
   }

   @Override
   public int read(byte[] var1) {
      return this.read(var1, 0, var1.length);
   }

   @Override
   public int read(byte[] var1, int var2, int var3) {
      if (this.bufOff == this.maxBuf && this.nextChunk() < 0) {
         return -1;
      } else {
         int var4 = this.maxBuf - this.bufOff;
         if (var3 > var4) {
            System.arraycopy(this.buf, this.bufOff, var1, var2, var4);
            this.bufOff = this.maxBuf;
            return var4;
         } else {
            System.arraycopy(this.buf, this.bufOff, var1, var2, var3);
            this.bufOff += var3;
            return var3;
         }
      }
   }

   @Override
   public long skip(long var1) {
      if (var1 <= 0L) {
         return 0L;
      } else {
         int var3 = this.maxBuf - this.bufOff;
         if (var1 > var3) {
            this.bufOff = this.maxBuf;
            return var3;
         } else {
            this.bufOff += (int)var1;
            return (int)var1;
         }
      }
   }

   @Override
   public int available() {
      return this.maxBuf - this.bufOff;
   }

   @Override
   public void close() {
      super.close();
   }

   @Override
   public boolean markSupported() {
      return false;
   }
}
