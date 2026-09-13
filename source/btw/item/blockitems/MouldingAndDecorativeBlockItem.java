package btw.item.blockitems;

import net.minecraft.src.ItemStack;

public class MouldingAndDecorativeBlockItem extends MouldingBlockItem {
   public MouldingAndDecorativeBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemStack) {
      switch (itemStack.getItemDamage()) {
         case 12:
            return super.a() + "." + "column";
         case 13:
         case 14:
            return super.a() + "." + "pedestal";
         case 15:
            return super.a() + "." + "table";
         default:
            return super.getUnlocalizedName(itemStack);
      }
   }
}
