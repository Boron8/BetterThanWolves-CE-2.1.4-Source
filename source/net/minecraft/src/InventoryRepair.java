package net.minecraft.src;

class InventoryRepair extends InventoryBasic {
   InventoryRepair(ContainerRepair var1, String var2, boolean var3, int var4) {
      super(var2, var3, var4);
      this.theContainer = var1;
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
      this.theContainer.onCraftMatrixChanged(this);
   }

   @Override
   public boolean isStackValidForSlot(int var1, ItemStack var2) {
      return true;
   }
}
