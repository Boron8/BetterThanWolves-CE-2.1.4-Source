package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;

public class RedstoneRepeaterItem extends PlaceAsBlockItem {
   public RedstoneRepeaterItem(int iItemID) {
      super(iItemID, Block.redstoneRepeaterIdle.blockID);
      this.b("diode");
      this.a(CreativeTabs.tabRedstone);
   }
}
