package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagFloat extends NBTBase {
   public float data;

   public NBTTagFloat(String var1) {
      super(var1);
   }

   public NBTTagFloat(String var1, float var2) {
      super(var1);
      this.data = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeFloat(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readFloat();
   }

   @Override
   public byte getId() {
      return 5;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagFloat(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagFloat var2 = (NBTTagFloat)var1;
         return this.data == var2.data;
      } else {
         return false;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Float.floatToIntBits(this.data);
   }
}
