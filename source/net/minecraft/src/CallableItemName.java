package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableItemName implements Callable {
   CallableItemName(InventoryPlayer var1, ItemStack var2) {
      this.playerInventory = var1;
      this.theItemStack = var2;
   }

   public String callItemDisplayName() {
      return this.theItemStack.getDisplayName();
   }
}
