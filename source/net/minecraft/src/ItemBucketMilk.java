package net.minecraft.src;

public class ItemBucketMilk extends Item {
   public ItemBucketMilk(int var1) {
      super(var1);
      this.d(1);
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public ItemStack onEaten(ItemStack var1, World var2, EntityPlayer var3) {
      if (!var3.capabilities.isCreativeMode) {
         var1.stackSize--;
      }

      if (!var2.isRemote) {
         var3.bB();
      }

      return var1.stackSize <= 0 ? new ItemStack(Item.bucketEmpty) : var1;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack var1) {
      return 32;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack var1) {
      return EnumAction.drink;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      var3.setItemInUse(var1, this.getMaxItemUseDuration(var1));
      return var1;
   }
}
