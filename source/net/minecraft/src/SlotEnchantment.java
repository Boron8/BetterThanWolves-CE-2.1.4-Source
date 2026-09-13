package net.minecraft.src;

class SlotEnchantment extends Slot {
   SlotEnchantment(ContainerEnchantment var1, IInventory var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.container = var1;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return true;
   }
}
