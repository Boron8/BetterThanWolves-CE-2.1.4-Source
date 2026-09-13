package net.minecraft.src;

public class TileEntityComparator extends TileEntity {
   private int field_96101_a = 0;

   @Override
   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      var1.setInteger("OutputSignal", this.field_96101_a);
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      this.field_96101_a = var1.getInteger("OutputSignal");
   }

   public int func_96100_a() {
      return this.field_96101_a;
   }

   public void func_96099_a(int var1) {
      this.field_96101_a = var1;
   }
}
