package net.minecraft.src;

class SlotBeacon extends Slot {
   public SlotBeacon(ContainerBeacon var1, IInventory var2, int var3, int var4, int var5) {
      super(var2, var3, var4, var5);
      this.beacon = var1;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return var1 == null
         ? false
         : var1.itemID == Item.emerald.itemID
            || var1.itemID == Item.diamond.itemID
            || var1.itemID == Item.ingotGold.itemID
            || var1.itemID == Item.ingotIron.itemID;
   }

   @Override
   public int getSlotStackLimit() {
      return 1;
   }
}
