package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagLong extends NBTBase {
   public long data;

   public NBTTagLong(String var1) {
      super(var1);
   }

   public NBTTagLong(String var1, long var2) {
      super(var1);
      this.data = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeLong(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readLong();
   }

   @Override
   public byte getId() {
      return 4;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagLong(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagLong var2 = (NBTTagLong)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ (int)(this.data ^ this.data >>> 32);
   }
}
