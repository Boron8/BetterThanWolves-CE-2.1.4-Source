package btw.item.items;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumAction;
import net.minecraft.src.EnumToolMaterial;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class HoeItemSteel extends HoeItem {
   public HoeItemSteel(int iItemID) {
      super(iItemID, EnumToolMaterial.SOULFORGED_STEEL);
      this.b("fcItemHoeRefined");
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
}
