package btw.item.blockitems;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class SidingAndCornerBlockItem extends ItemBlock {
   public SidingAndCornerBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b(Block.blocksList[this.g()].getUnlocalizedName());
   }

   @Override
   public int getMetadata(int iItemDamage) {
      return iItemDamage;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      if (itemstack.getItemDamage() == 12) {
         return super.getUnlocalizedName() + "." + "bench";
      } else if (itemstack.getItemDamage() == 14) {
         return super.getUnlocalizedName() + "." + "fence";
      } else {
         return itemstack.getItemDamage() == 0 ? super.getUnlocalizedName() + "." + "siding" : super.getUnlocalizedName() + "." + "corner";
      }
   }
}
