package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagByte extends NBTBase {
   public byte data;

   public NBTTagByte(String var1) {
      super(var1);
   }

   public NBTTagByte(String var1, byte var2) {
      super(var1);
      this.data = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeByte(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readByte();
   }

   @Override
   public byte getId() {
      return 1;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagByte(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagByte var2 = (NBTTagByte)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.data;
   }
}
