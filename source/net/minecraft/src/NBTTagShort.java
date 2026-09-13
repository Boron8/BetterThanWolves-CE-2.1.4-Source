package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagShort extends NBTBase {
   public short data;

   public NBTTagShort(String var1) {
      super(var1);
   }

   public NBTTagShort(String var1, short var2) {
      super(var1);
      this.data = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeShort(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readShort();
   }

   @Override
   public byte getId() {
      return 2;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagShort(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagShort var2 = (NBTTagShort)var1;
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
