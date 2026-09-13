package net.minecraft.src;

public class ItemSaddle extends Item {
   public ItemSaddle(int var1) {
      super(var1);
      this.maxStackSize = 1;
      this.a(CreativeTabs.tabTransport);
   }

   @Override
   public boolean itemInteractionForEntity(ItemStack var1, EntityLiving var2) {
      if (var2 instanceof EntityPig) {
         EntityPig var3 = (EntityPig)var2;
         if (!var3.getSaddled() && !var3.h_()) {
            var3.setSaddled(true);
            var1.stackSize--;
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean hitEntity(ItemStack var1, EntityLiving var2, EntityLiving var3) {
      this.itemInteractionForEntity(var1, var2);
      return true;
   }
}
