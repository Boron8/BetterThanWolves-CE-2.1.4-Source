package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemExpBottle extends Item {
   public ItemExpBottle(int par1) {
      super(par1);
      this.a(CreativeTabs.tabMisc);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean hasEffect(ItemStack par1ItemStack) {
      return true;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (!par3EntityPlayer.capabilities.isCreativeMode) {
         par1ItemStack.stackSize--;
      }

      par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
      if (!par2World.isRemote) {
         par2World.spawnEntityInWorld(EntityList.createEntityOfType(EntityExpBottle.class, par2World, par3EntityPlayer));
      }

      return par1ItemStack;
   }
}
