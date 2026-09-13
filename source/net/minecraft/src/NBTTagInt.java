package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagInt extends NBTBase {
   public int data;

   public NBTTagInt(String var1) {
      super(var1);
   }

   public NBTTagInt(String var1, int var2) {
      super(var1);
      this.data = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeInt(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readInt();
   }

   @Override
   public byte getId() {
      return 3;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagInt(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (super.equals(var1)) {
         NBTTagInt var2 = (NBTTagInt)var1;
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
