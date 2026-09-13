package btw.item.blockitems;

import net.minecraft.src.ItemBlock;

public class LeverBlockItem extends ItemBlock {
   public LeverBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public int getTargetFacingPlacedByBlockDispenser(int iDispenserFacing) {
      return 1;
   }
}
