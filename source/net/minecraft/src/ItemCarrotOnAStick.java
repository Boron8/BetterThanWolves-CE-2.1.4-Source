package net.minecraft.src;

public class ItemCarrotOnAStick extends Item {
   public ItemCarrotOnAStick(int var1) {
      super(var1);
      this.a(CreativeTabs.tabTransport);
      this.d(1);
      this.e(25);
   }

   @Override
   public boolean isFull3D() {
      return true;
   }

   @Override
   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack var1, World var2, EntityPlayer var3) {
      if (var3.af() && var3.ridingEntity instanceof EntityPig) {
         EntityPig var4 = (EntityPig)var3.ridingEntity;
         if (var4.getAIControlledByPlayer().isControlledByPlayer() && var1.getMaxDamage() - var1.getItemDamage() >= 7) {
            var4.getAIControlledByPlayer().boostSpeed();
            var1.damageItem(7, var3);
            if (var1.stackSize == 0) {
               ItemStack var5 = new ItemStack(Item.fishingRod);
               var5.setTagCompound(var1.stackTagCompound);
               return var5;
            }
         }
      }

      return var1;
   }
}
