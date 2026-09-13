package net.minecraft.src;

public class ItemMapBase extends Item {
   protected ItemMapBase(int var1) {
      super(var1);
   }

   @Override
   public boolean isMap() {
      return true;
   }

   public Packet createMapDataPacket(ItemStack var1, World var2, EntityPlayer var3) {
      return null;
   }
}
