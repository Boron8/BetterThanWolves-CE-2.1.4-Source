package btw.item.blockitems;

import net.minecraft.src.ItemStack;

public class SandAndGravelSlabBlockItem extends SlabBlockItem {
   public SandAndGravelSlabBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 1:
            return super.a() + "." + "sand";
         default:
            return super.a();
      }
   }
}
