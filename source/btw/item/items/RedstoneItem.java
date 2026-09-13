package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;

public class RedstoneItem extends PlaceAsBlockItem {
   public RedstoneItem(int iItemID) {
      super(iItemID, Block.redstoneWire.blockID);
      this.setBellowsBlowDistance(3);
      this.setFilterableProperties(8);
      this.b("redstone");
      this.a(CreativeTabs.tabRedstone);
   }
}
