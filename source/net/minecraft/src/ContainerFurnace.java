package net.minecraft.src;

public class ContainerFurnace extends Container {
   private TileEntityFurnace furnace;
   private int lastCookTime = 0;
   private int lastBurnTime = 0;
   private int lastItemBurnTime = 0;

   public ContainerFurnace(InventoryPlayer var1, TileEntityFurnace var2) {
      this.furnace = var2;
      this.a(new Slot(var2, 0, 56, 17));
      this.a(new Slot(var2, 1, 56, 53));
      this.a(new SlotFurnace(var1.player, var2, 2, 116, 35));

      for (int var3 = 0; var3 < 3; var3++) {
         for (int var4 = 0; var4 < 9; var4++) {
            this.a(new Slot(var1, var4 + var3 * 9 + 9, 8 + var4 * 18, 84 + var3 * 18));
         }
      }

      for (int var5 = 0; var5 < 9; var5++) {
         this.a(new Slot(var1, var5, 8 + var5 * 18, 142));
      }
   }

   @Override
   public void addCraftingToCrafters(ICrafting var1) {
      super.addCraftingToCrafters(var1);
      var1.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
      var1.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
      var1.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (int var1 = 0; var1 < this.crafters.size(); var1++) {
         ICrafting var2 = (ICrafting)this.crafters.get(var1);
         if (this.lastCookTime != this.furnace.furnaceCookTime) {
            var2.sendProgressBarUpdate(this, 0, this.furnace.furnaceCookTime);
         }

         if (this.lastBurnTime != this.furnace.furnaceBurnTime) {
            var2.sendProgressBarUpdate(this, 1, this.furnace.furnaceBurnTime);
         }

         if (this.lastItemBurnTime != this.furnace.currentItemBurnTime) {
            var2.sendProgressBarUpdate(this, 2, this.furnace.currentItemBurnTime);
         }
      }

      this.lastCookTime = this.furnace.furnaceCookTime;
      this.lastBurnTime = this.furnace.furnaceBurnTime;
      this.lastItemBurnTime = this.furnace.currentItemBurnTime;
   }

   @Override
   public void updateProgressBar(int var1, int var2) {
      if (var1 == 0) {
         this.furnace.furnaceCookTime = var2;
      }

      if (var1 == 1) {
         this.furnace.furnaceBurnTime = var2;
      }

      if (var1 == 2) {
         this.furnace.currentItemBurnTime = var2;
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer var1) {
      return this.furnace.isUseableByPlayer(var1);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer var1, int var2) {
      ItemStack var3 = null;
      Slot var4 = (Slot)this.inventorySlots.get(var2);
      if (var4 != null && var4.getHasStack()) {
         ItemStack var5 = var4.getStack();
         var3 = var5.copy();
         if (var2 == 2) {
            if (!this.a(var5, 3, 39, true)) {
               return null;
            }

            var4.onSlotChange(var5, var3);
         } else if (var2 != 1 && var2 != 0) {
            if (FurnaceRecipes.smelting().getSmeltingResult(var5.getItem().itemID) != null) {
               if (!this.a(var5, 0, 1, false)) {
                  return null;
               }
            } else if (TileEntityFurnace.isItemFuel(var5)) {
               if (!this.a(var5, 1, 2, false)) {
                  return null;
               }
            } else if (var2 >= 3 && var2 < 30) {
               if (!this.a(var5, 30, 39, false)) {
                  return null;
               }
            } else if (var2 >= 30 && var2 < 39 && !this.a(var5, 3, 30, false)) {
               return null;
            }
         } else if (!this.a(var5, 3, 39, false)) {
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
