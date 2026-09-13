package btw.item.blockitems;

import net.minecraft.src.Block;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class MouldingBlockItem extends ItemBlock {
   public MouldingBlockItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(true);
      this.b(Block.blocksList[this.g()].getUnlocalizedName());
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      return super.getUnlocalizedName() + "." + "moulding";
   }
}
