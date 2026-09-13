package btw.item.blockitems;

import net.minecraft.src.ItemBlock;

public class VaseBlockItem extends ItemBlock {
   public VaseBlockItem(int i) {
      super(i);
      this.e(0);
      this.a(true);
      this.b("fcBlockVase");
   }

   @Override
   public int getMetadata(int i) {
      return i;
   }
}
