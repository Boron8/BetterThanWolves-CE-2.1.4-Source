package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class PickaxeItemSteel extends PickaxeItem {
   public PickaxeItemSteel(int i) {
      super(i, EnumToolMaterial.SOULFORGED_STEEL);
      this.b("fcItemPickAxeRefined");
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
   public boolean canHarvestBlock(ItemStack stack, World world, Block block, int i, int j, int k) {
      return block != null && block.blockMaterial == BTWBlocks.soulforgedSteelMaterial ? true : super.canHarvestBlock(stack, world, block, i, j, k);
   }

   @Override
   public float getStrVsBlock(ItemStack toolItemStack, World world, Block block, int i, int j, int k) {
      return block != null && block.blockMaterial == BTWBlocks.soulforgedSteelMaterial
         ? this.efficiencyOnProperMaterial
         : super.getStrVsBlock(toolItemStack, world, block, i, j, k);
   }
}
