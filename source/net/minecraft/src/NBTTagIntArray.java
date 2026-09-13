package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase {
   public int[] intArray;

   public NBTTagIntArray(String var1) {
      super(var1);
   }

   public NBTTagIntArray(String var1, int[] var2) {
      super(var1);
      this.intArray = var2;
   }

   @Override
   void write(DataOutput var1) {
      var1.writeInt(this.intArray.length);

      for (int var2 = 0; var2 < this.intArray.length; var2++) {
         var1.writeInt(this.intArray[var2]);
      }
   }

   @Override
   void load(DataInput var1) {
      int var2 = var1.readInt();
      this.intArray = new int[var2];

      for (int var3 = 0; var3 < var2; var3++) {
         this.intArray[var3] = var1.readInt();
      }
   }

   @Override
   public byte getId() {
      return 11;
   }

   @Override
   public String toString() {
      return "[" + this.intArray.length + " bytes]";
   }

   @Override
   public NBTBase copy() {
      int[] var1 = new int[this.intArray.length];
      System.arraycopy(this.intArray, 0, var1, 0, this.intArray.length);
      return new NBTTagIntArray(this.e(), var1);
   }

   @Override
   public boolean equals(Object var1) {
      if (!super.equals(var1)) {
         return false;
      } else {
         NBTTagIntArray var2 = (NBTTagIntArray)var1;
         return this.intArray == null && var2.intArray == null || this.intArray != null && Arrays.equals(this.intArray, var2.intArray);
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(this.intArray);
   }
}
