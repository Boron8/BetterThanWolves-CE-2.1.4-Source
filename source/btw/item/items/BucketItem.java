package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public abstract class BucketItem extends PlaceAsBlockItem {
   public BucketItem(int iItemID) {
      super(iItemID);
      this.d(1);
      this.a(Item.bucketEmpty);
      this.a(CreativeTabs.tabMisc);
   }

   @Override
   public abstract int g();

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      return !player.isUsingSpecialKey() ? false : super.onItemUse(stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
   }

   @Override
   public boolean isMultiUsePerClick() {
      return false;
   }

   public boolean doesContextOverridePlacingAsBlock(
      ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      return true;
   }
}
