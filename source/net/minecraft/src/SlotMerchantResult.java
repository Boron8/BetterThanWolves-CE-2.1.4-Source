package net.minecraft.src;

public class SlotMerchantResult extends Slot {
   private final InventoryMerchant theMerchantInventory;
   private EntityPlayer thePlayer;
   private int field_75231_g;
   private final IMerchant theMerchant;

   public SlotMerchantResult(EntityPlayer var1, IMerchant var2, InventoryMerchant var3, int var4, int var5, int var6) {
      super(var3, var4, var5, var6);
      this.thePlayer = var1;
      this.theMerchant = var2;
      this.theMerchantInventory = var3;
   }

   @Override
   public boolean isItemValid(ItemStack var1) {
      return false;
   }

   @Override
   public ItemStack decrStackSize(int var1) {
      if (this.d()) {
         this.field_75231_g = this.field_75231_g + Math.min(var1, this.c().stackSize);
      }

      return super.decrStackSize(var1);
   }

   @Override
   protected void onCrafting(ItemStack var1, int var2) {
      this.field_75231_g += var2;
      this.onCrafting(var1);
   }

   @Override
   protected void onCrafting(ItemStack var1) {
      var1.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
      this.field_75231_g = 0;
   }

   @Override
   public void onPickupFromSlot(EntityPlayer var1, ItemStack var2) {
      this.onCrafting(var2);
      MerchantRecipe var3 = this.theMerchantInventory.getCurrentRecipe();
      if (var3 != null) {
         ItemStack var4 = this.theMerchantInventory.getStackInSlot(0);
         ItemStack var5 = this.theMerchantInventory.getStackInSlot(1);
         if (this.func_75230_a(var3, var4, var5) || this.func_75230_a(var3, var5, var4)) {
            if (var4 != null && var4.stackSize <= 0) {
               var4 = null;
            }

            if (var5 != null && var5.stackSize <= 0) {
               var5 = null;
            }

            this.theMerchantInventory.setInventorySlotContents(0, var4);
            this.theMerchantInventory.setInventorySlotContents(1, var5);
            this.theMerchant.useRecipe(var3);
         }
      }
   }

   private boolean func_75230_a(MerchantRecipe var1, ItemStack var2, ItemStack var3) {
      ItemStack var4 = var1.getItemToBuy();
      ItemStack var5 = var1.getSecondItemToBuy();
      if (var2 != null && var2.itemID == var4.itemID) {
         if (var5 != null && var3 != null && var5.itemID == var3.itemID) {
            var2.stackSize = var2.stackSize - var4.stackSize;
            var3.stackSize = var3.stackSize - var5.stackSize;
            return true;
         }

         if (var5 == null && var3 == null) {
            var2.stackSize = var2.stackSize - var4.stackSize;
            return true;
         }
      }

      return false;
   }
}
