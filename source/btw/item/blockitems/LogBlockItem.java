package btw.item.blockitems;

import net.minecraft.src.Block;
import net.minecraft.src.ItemMultiTextureTile;

public class LogBlockItem extends ItemMultiTextureTile {
   public LogBlockItem(int iBlockID, Block block, String[] sTypeNames) {
      super(iBlockID, block, sTypeNames);
   }

   @Override
   public int getCampfireBurnTime(int iItemDamage) {
      return 0;
   }
}
