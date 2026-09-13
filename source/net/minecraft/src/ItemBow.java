package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemBow extends Item {
   public static final String[] bowPullIconNameArray = new String[]{"bow_pull_0", "bow_pull_1", "bow_pull_2"};
   @Environment(EnvType.CLIENT)
   private Icon[] iconArray;

   public ItemBow(int par1) {
      super(par1);
      this.maxStackSize = 1;
      this.e(384);
      this.a(CreativeTabs.tabCombat);
   }

   @Override
   public void onPlayerStoppedUsing(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer, int par4) {
      boolean var5 = par3EntityPlayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantment.infinity.effectId, par1ItemStack) > 0;
      if (var5 || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID)) {
         int var6 = this.getMaxItemUseDuration(par1ItemStack) - par4;
         float var7 = var6 / 20.0F;
         var7 = (var7 * var7 + var7 * 2.0F) / 3.0F;
         if (var7 < 0.1) {
            return;
         }

         if (var7 > 1.0F) {
            var7 = 1.0F;
         }

         EntityArrow var8 = (EntityArrow)EntityList.createEntityOfType(EntityArrow.class, par2World, par3EntityPlayer, var7 * 2.0F);
         if (var7 == 1.0F) {
            var8.setIsCritical(true);
         }

         int var9 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, par1ItemStack);
         if (var9 > 0) {
            var8.setDamage(var8.getDamage() + var9 * 0.5 + 0.5);
         }

         int var10 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, par1ItemStack);
         if (var10 > 0) {
            var8.setKnockbackStrength(var10);
         }

         if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, par1ItemStack) > 0) {
            var8.d(100);
         }

         par1ItemStack.damageItem(1, par3EntityPlayer);
         par2World.playSoundAtEntity(par3EntityPlayer, "random.bow", 1.0F, 1.0F / (e.nextFloat() * 0.4F + 1.2F) + var7 * 0.5F);
         if (var5) {
            var8.canBePickedUp = 2;
         } else {
            par3EntityPlayer.inventory.consumeInventoryItem(Item.arrow.itemID);
         }

         if (!par2World.isRemote) {
            par2World.spawnEntityInWorld(var8);
         }
      }
   }

   @Override
   public ItemStack onEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      return par1ItemStack;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.bow;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      if (par3EntityPlayer.capabilities.isCreativeMode || par3EntityPlayer.inventory.hasItem(Item.arrow.itemID)) {
         par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
      }

      return par1ItemStack;
   }

   @Override
   public int getItemEnchantability() {
      return 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      super.registerIcons(par1IconRegister);
      this.iconArray = new Icon[bowPullIconNameArray.length];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = par1IconRegister.registerIcon(bowPullIconNameArray[var2]);
      }
   }

   @Environment(EnvType.CLIENT)
   public Icon getItemIconForUseDuration(int par1) {
      return this.iconArray[par1];
   }
}
