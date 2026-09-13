package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;

public class BrickItem extends PlaceAsBlockItem {
   public BrickItem(int iItemID) {
      super(iItemID);
      this.b("brick");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public int g() {
      return BTWBlocks.placedBrick.blockID;
   }
}
