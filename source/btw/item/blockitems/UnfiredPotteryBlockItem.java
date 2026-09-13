package btw.item.blockitems;

import net.minecraft.src.ItemBlock;

public class UnfiredPotteryBlockItem extends ItemBlock {
   public UnfiredPotteryBlockItem(int i) {
      super(i);
      this.e(0);
      this.a(true);
      this.b("fcBlockUnfiredPottery");
   }

   @Override
   public int getMetadata(int i) {
      return i;
   }
}
