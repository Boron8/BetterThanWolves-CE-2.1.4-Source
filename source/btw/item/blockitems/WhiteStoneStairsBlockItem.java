package btw.item.blockitems;

import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class WhiteStoneStairsBlockItem extends ItemBlock {
   public WhiteStoneStairsBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b("fcBlockWhiteStoneStairs");
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      int iDamage = itemstack.getItemDamage();
      return (iDamage & 8) > 0 ? super.getUnlocalizedName() + "." + "cobble" : super.getUnlocalizedName() + "." + "smooth";
   }
}
