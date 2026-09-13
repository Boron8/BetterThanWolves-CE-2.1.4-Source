package btw.item.blockitems;

import net.minecraft.src.ItemBlock;

public class IceBlockItem extends ItemBlock {
   public IceBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public int getMetadata(int iDamage) {
      return 8;
   }
}
