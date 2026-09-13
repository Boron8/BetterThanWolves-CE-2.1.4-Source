package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumEnchantmentType;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BattleAxeItem extends AxeItem {
   private final int weaponDamage = 4 + this.toolMaterial.getDamageVsEntity();

   public BattleAxeItem(int i) {
      super(i, EnumToolMaterial.SOULFORGED_STEEL);
      this.b("fcItemAxeBattle");
   }

   @Override
   public EnumAction getItemUseAction(ItemStack itemstack) {
      return EnumAction.block;
   }

   @Override
   public int getMaxItemUseDuration(ItemStack itemstack) {
      return 72000;
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
      if (!entityplayer.isUsingSpecialKey()) {
         entityplayer.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
      }

      return itemstack;
   }

   @Override
   public int getDamageVsEntity(Entity entity) {
      return this.weaponDamage;
   }

   @Override
   public boolean hitEntity(ItemStack stack, EntityLiving defendingEntity, EntityLiving attackingEntity) {
      stack.damageItem(1, attackingEntity);
      return true;
   }

   @Override
   public float getStrVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block.blockID != Block.web.blockID && block.blockID != BTWBlocks.web.blockID ? super.getStrVsBlock(stack, world, block, i, j, k) : 15.0F;
   }

   @Override
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block.blockID != Block.web.blockID && block.blockID != BTWBlocks.web.blockID ? super.canHarvestBlock(stack, world, block, i, j, k) : true;
   }

   @Override
   public boolean isEfficientVsBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block.blockID != Block.web.blockID && block.blockID != BTWBlocks.web.blockID ? super.isEfficientVsBlock(stack, world, block, i, j, k) : true;
   }

   @Override
   public boolean isEnchantmentApplicable(Enchantment enchantment) {
      return enchantment.type == EnumEnchantmentType.weapon ? true : super.isEnchantmentApplicable(enchantment);
   }

   @Override
   public boolean getCanBePlacedAsBlock() {
      return true;
   }
}
