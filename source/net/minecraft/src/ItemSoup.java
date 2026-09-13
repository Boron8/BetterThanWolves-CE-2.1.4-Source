package net.minecraft.src;

public class ItemSoup extends ItemFood {
   public ItemSoup(int var1, int var2) {
      super(var1, var2, false);
      this.d(1);
   }

   @Override
   public ItemStack onEaten(ItemStack var1, World var2, EntityPlayer var3) {
      super.onEaten(var1, var2, var3);
      return new ItemStack(Item.bowlEmpty);
   }
}
