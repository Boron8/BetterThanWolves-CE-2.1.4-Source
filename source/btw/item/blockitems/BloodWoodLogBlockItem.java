package btw.item.blockitems;

import net.minecraft.src.ItemBlock;

public class BloodWoodLogBlockItem extends ItemBlock {
   public BloodWoodLogBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public int getCampfireBurnTime(int iItemDamage) {
      return 0;
   }
}
