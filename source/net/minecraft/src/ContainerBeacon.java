package net.minecraft.src;

public class ContainerBeacon extends Container {
   private TileEntityBeacon theBeacon;
   private final SlotBeacon beaconSlot;
   private int field_82865_g;
   private int field_82867_h;
   private int field_82868_i;

   public ContainerBeacon(InventoryPlayer var1, TileEntityBeacon var2) {
      this.theBeacon = var2;
      this.a(this.beaconSlot = new SlotBeacon(this, var2, 0, 136, 110));
      byte var3 = 36;
      short var4 = 137;

      for (int var5 = 0; var5 < 3; var5++) {
         for (int var6 = 0; var6 < 9; var6++) {
            this.a(new Slot(var1, var6 + var5 * 9 + 9, var3 + var6 * 18, var4 + var5 * 18));
         }
      }

      for (int var7 = 0; var7 < 9; var7++) {
         this.a(new Slot(var1, var7, var3 + var7 * 18, 58 + var4));
      }

      this.field_82865_g = var2.getLevels();
      this.field_82867_h = var2.getPrimaryEffect();
      this.field_82868_i = var2.getSecondaryEffect();
   }

   @Override
   public void addCraftingToCrafters(ICrafting var1) {
      super.addCraftingToCrafters(var1);
      var1.sendProgressBarUpdate(this, 0, this.field_82865_g);
      var1.sendProgressBarUpdate(this, 1, this.field_82867_h);
      var1.sendProgressBarUpdate(this, 2, this.field_82868_i);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      if (var1 == 0) {
         this.theBeacon.setLevels(var2);
      }

      if (var1 == 1) {
         this.theBeacon.setPrimaryEffect(var2);
      }

      if (var1 == 2) {
         this.theBeacon.setSecondaryEffect(var2);
      }
   }

   public TileEntityBeacon getBeacon() {
      return this.theBeacon;
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.theBeacon.isUseableByPlayer(var1);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 == 0) {
            if (!this.a(var5, 1, 37, true)) {
               return null;
            }

            var4.onSlotChange(var5, var3);
         } else if (!this.beaconSlot.d() && this.beaconSlot.isItemValid(var5) && var5.stackSize == 1) {
            if (!this.a(var5, 0, 1, false)) {
               return null;
            }
         } else if (var2 >= 1 && var2 < 28) {
            if (!this.a(var5, 28, 37, false)) {
               return null;
            }
         } else if (var2 >= 28 && var2 < 37) {
            if (!this.a(var5, 1, 28, false)) {
               return null;
            }
         } else if (!this.a(var5, 1, 37, false)) {
            return null;
         }

         if (var5.stackSize == 0) {
            var4.putStack(null);
         } else {
            var4.onSlotChanged();
         }

         if (var5.stackSize == var3.stackSize) {
            return null;
         }

         var4.onPickupFromSlot(var1, var5);
      }

      return var3;
   }
}
