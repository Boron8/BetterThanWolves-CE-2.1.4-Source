package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagString extends NBTBase {
   public String data;

   public NBTTagString(String var1) {
      super(var1);
   }

   public NBTTagString(String var1, String var2) {
      super(var1);
      this.data = var2;
      if (var2 == null) {
         throw new IllegalArgumentException("Empty string not allowed");
      }
   }

   @Override
   void write(DataOutput var1) {
      var1.writeUTF(this.data);
   }

   @Override
   void load(DataInput var1) {
      this.data = var1.readUTF();
   }

   @Override
   public byte getId() {
      return 8;
   }

   @Override
   public String toString() {
      return "" + this.data;
   }

   @Override
   public NBTBase copy() {
      return new NBTTagString(this.e(), this.data);
   }

   @Override
   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         NBTTagString var2 = (NBTTagString)var1;
         return this.data == null && var2.data == null || this.data != null && this.data.equals(var2.data);
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ this.data.hashCode();
   }
}
