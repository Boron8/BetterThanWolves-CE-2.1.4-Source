package net.minecraft.src;

class SlotEnchantmentTable extends InventoryBasic {
   SlotEnchantmentTable(ContainerEnchantment var1, String var2, boolean var3, int var4) {
      super(var2, var3, var4);
      this.container = var1;
   }

   @Override
   public int getInventoryStackLimit() {
      return 1;
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
      this.container.onCraftMatrixChanged(this);
   }

   @Override
   public boolean isStackValidForSlot(int var1, ItemStack var2) {
      return true;
   }
}
