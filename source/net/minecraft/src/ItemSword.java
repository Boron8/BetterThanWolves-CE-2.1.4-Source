package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class ItemSword extends Item {
   private int weaponDamage;
   private final EnumToolMaterial toolMaterial;

   public ItemSword(int par1, EnumToolMaterial par2EnumToolMaterial) {
      super(par1);
      this.toolMaterial = par2EnumToolMaterial;
      this.maxStackSize = 1;
      this.e(par2EnumToolMaterial.getMaxUses());
      this.a(CreativeTabs.tabCombat);
      this.weaponDamage = 4 + par2EnumToolMaterial.getDamageVsEntity();
   }

   public int func_82803_g() {
      return this.toolMaterial.getDamageVsEntity();
   }

   public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block) {
      if (par2Block.blockID == Block.web.blockID) {
         return 15.0F;
      } else {
         Material var3 = par2Block.blockMaterial;
         return var3 != Material.plants && var3 != Material.vine && var3 != Material.coral && var3 != Material.leaves && var3 != Material.pumpkin ? 1.0F : 1.5F;
      }
   }

   @Override
   public boolean hitEntity(ItemStack par1ItemStack, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving) {
      par1ItemStack.damageItem(1, par3EntityLiving);
      return true;
   }

   @Override
   public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLiving par7EntityLiving) {
      if (Block.blocksList[par3].getBlockHardness(par2World, par4, par5, par6) != 0.0) {
         par1ItemStack.damageItem(2, par7EntityLiving);
      }

      return true;
   }

   @Override
   public int getDamageVsEntity(Entity par1Entity) {
      return this.weaponDamage;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isFull3D() {
      return true;
   }

   @Override
   public EnumAction getItemUseAction(ItemStack par1ItemStack) {
      return EnumAction.block;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack par1ItemStack) {
      return 72000;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
      par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
      return par1ItemStack;
   }

   public boolean canHarvestBlock(Block par1Block) {
      return par1Block.blockID == Block.web.blockID;
   }

   @Override
   public int getItemEnchantability() {
      return this.toolMaterial.getEnchantability();
   }

   public String getToolMaterialName() {
      return this.toolMaterial.toString();
   }

   @Override
   public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
      return this.toolMaterial.getToolCraftingMaterial() == par2ItemStack.itemID ? true : super.getIsRepairable(par1ItemStack, par2ItemStack);
   }
}
