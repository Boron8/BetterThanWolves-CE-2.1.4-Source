package net.minecraft.src;

public class SlotFurnace extends Slot {
   private EntityPlayer thePlayer;
   private int field_75228_b;

   public SlotFurnace(EntityPlayer par1EntityPlayer, IInventory par2IInventory, int par3, int par4, int par5) {
      super(par2IInventory, par3, par4, par5);
      this.thePlayer = par1EntityPlayer;
   }

   @Override
   public boolean isItemValid(ItemStack par1ItemStack) {
      return false;
   }

   @Override
   public ItemStack decrStackSize(int par1) {
      if (this.d()) {
         this.field_75228_b = this.field_75228_b + Math.min(par1, this.c().stackSize);
      }

      return super.decrStackSize(par1);
   }

   @Override
   public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
      this.onCrafting(par2ItemStack);
      super.onPickupFromSlot(par1EntityPlayer, par2ItemStack);
   }

   @Override
   protected void onCrafting(ItemStack par1ItemStack, int par2) {
      this.field_75228_b += par2;
      this.onCrafting(par1ItemStack);
   }

   @Override
   protected void onCrafting(ItemStack par1ItemStack) {
      par1ItemStack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75228_b);
      if (!this.thePlayer.worldObj.isRemote) {
         int var2 = this.field_75228_b;
         float var3 = FurnaceRecipes.smelting().getExperience(par1ItemStack.itemID);
         if (var3 == 0.0F) {
            int var5 = false;
         } else if (var3 < 1.0F) {
            int var4 = MathHelper.floor_float(var2 * var3);
            if (var4 < MathHelper.ceiling_float_int(var2 * var3) && (float)Math.random() < var2 * var3 - var4) {
               var4++;
            }
         }
      }

      this.field_75228_b = 0;
      if (par1ItemStack.itemID == Item.ingotIron.itemID) {
         this.thePlayer.addStat(AchievementList.acquireIron, 1);
      }

      if (par1ItemStack.itemID == Item.fishCooked.itemID) {
         this.thePlayer.addStat(AchievementList.cookFish, 1);
      }

      if (par1ItemStack.getItem().itemID == Item.bread.itemID) {
         this.thePlayer.addStat(AchievementList.makeBread, 1);
      }
   }
}
