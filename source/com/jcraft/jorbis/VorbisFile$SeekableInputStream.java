package com.jcraft.jorbis;

import java.io.InputStream;
import java.io.RandomAccessFile;

class VorbisFile$SeekableInputStream extends InputStream {
   RandomAccessFile raf;
   final String mode;

   VorbisFile$SeekableInputStream(VorbisFile var1, String var2) {
      this.this$0 = var1;
      this.raf = null;
      this.mode = "r";
      this.raf = new RandomAccessFile(var2, "r");
   }

   @Override
   public int read() {
      return this.raf.read();
   }

   @Override
   public int read(byte[] var1) {
      return this.raf.read(var1);
   }

   @Override
   public int read(byte[] var1, int var2, int var3) {
      return this.raf.read(var1, var2, var3);
   }

   @Override
   public long skip(long var1) {
      return this.raf.skipBytes((int)var1);
   }

   public long getLength() {
      return this.raf.length();
   }

   public long tell() {
      return this.raf.getFilePointer();
   }

   @Override
   public int available() {
      return this.raf.length() == this.raf.getFilePointer() ? 0 : 1;
   }

   @Override
   public void close() {
      this.raf.close();
   }

   @Override
   public synchronized void mark(int var1) {
   }

   @Override
   public synchronized void reset() {
   }

   @Override
   public boolean markSupported() {
      return false;
   }

   public void seek(long var1) {
      this.raf.seek(var1);
   }
}
