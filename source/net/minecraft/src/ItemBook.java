package net.minecraft.src;

public class ItemBook extends Item {
   public ItemBook(int var1) {
      super(var1);
   }

   @Override
   public boolean isItemTool(ItemStack var1) {
      return var1.stackSize == 1;
   }

   @Override
   public int getItemEnchantability() {
      return 1;
   }
}
