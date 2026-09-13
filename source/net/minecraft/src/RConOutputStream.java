package net.minecraft.src;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class RConOutputStream {
   private ByteArrayOutputStream byteArrayOutput;
   private DataOutputStream output;

   public RConOutputStream(int var1) {
      this.byteArrayOutput = new ByteArrayOutputStream(var1);
      this.output = new DataOutputStream(this.byteArrayOutput);
   }

   public void writeByteArray(byte[] var1) {
      this.output.write(var1, 0, var1.length);
   }

   public void writeString(String var1) {
      this.output.writeBytes(var1);
      this.output.write(0);
   }

   public void writeInt(int var1) {
      this.output.write(var1);
   }

   public void writeShort(short var1) {
      this.output.writeShort(Short.reverseBytes(var1));
   }

   public byte[] toByteArray() {
      return this.byteArrayOutput.toByteArray();
   }

   public void reset() {
      this.byteArrayOutput.reset();
   }
}
