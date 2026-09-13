package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {
   public byte[] byteArray;

   public NBTTagByteArray(String var1) {
      super(var1);
   }

   public NBTTagByteArray(String var1, byte[] var2) {
      super(var1);
      this.byteArray = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeInt(this.byteArray.length);
      var1.write(this.byteArray);
   }

   @Override
   void load(DataInput var1) {
      int var2 = var1.readInt();
      this.byteArray = new byte[var2];
      var1.readFully(this.byteArray);
   }

   @Override
   public byte getId() {
      return 7;
   }

   @Override
   public String toString() {
      return "[" + this.byteArray.length + " bytes]";
   }

   @Override
   public NBTBase copy() {
      byte[] var1 = new byte[this.byteArray.length];
      System.arraycopy(this.byteArray, 0, var1, 0, this.byteArray.length);
      return new NBTTagByteArray(this.e(), var1);
   }

   @Override
   public boolean equals(Object var1) {
      return super.equals(var1) ? Arrays.equals(this.byteArray, ((NBTTagByteArray)var1).byteArray) : false;
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.byteArray);
   }
}
