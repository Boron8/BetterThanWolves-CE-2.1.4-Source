package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagDouble extends NBTBase {
   public double data;

   public NBTTagDouble(String var1) {
      super(var1);
   }

   public NBTTagDouble(String var1, double var2) {
      super(var1);
      this.data = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeDouble(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readDouble();
   }

   @Override
   public byte getId() {
      return 6;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagDouble(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagDouble var2 = (NBTTagDouble)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      long var1 = Double.doubleToLongBits(this.data);
      return super.hashCode() ^ (int)(var1 ^ var1 >>> 32);
   }
}
