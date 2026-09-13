package net.minecraft.src;

import java.io.DataInput;
import java.io.DataOutput;

public class NBTTagEnd extends NBTBase {
   public NBTTagEnd() {
      super(null);
   }

   @Override
   void load(DataInput var1) {
   }

   @Override
   void write(DataOutput var1) {
   }

   @Override
   public byte getId() {
      return 0;
   }

   @Override
   public String toString() {
      return "END";
   }

   @Override
   public NBTBase copy() {
      return new NBTTagEnd();
   }
}
