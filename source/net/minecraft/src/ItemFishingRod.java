package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemFishingRod extends Item {
   @Environment(EnvType.CLIENT)
   private Icon theIcon;

   public ItemFishingRod(int par1) {
      super(par1);
      this.e(64);
      this.d(1);
      this.a(CreativeTabs.tabTools);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isFull3D() {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRotateAroundWhenRendering() {
      return true;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (par3EntityPlayer.fishEntity != null) {
         int var4 = par3EntityPlayer.fishEntity.catchFish();
         par1ItemStack.damageItem(var4, par3EntityPlayer);
         par3EntityPlayer.bK();
      } else {
         par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
         if (!par2World.isRemote) {
            par2World.spawnEntityInWorld(EntityList.createEntityOfType(EntityFishHook.class, par2World, par3EntityPlayer));
         }

         par3EntityPlayer.bK();
      }

      return par1ItemStack;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      this.theIcon = par1IconRegister.registerIcon("fishingRod_empty");
   }

   @Environment(EnvType.CLIENT)
   public Icon func_94597_g() {
      return this.theIcon;
   }
}
