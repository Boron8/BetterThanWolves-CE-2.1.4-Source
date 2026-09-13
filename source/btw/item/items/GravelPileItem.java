package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class GravelPileItem extends Item {
   public GravelPileItem(int iItemID) {
      super(iItemID);
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(2);
      this.b("fcItemPileGravel");
      this.a(CreativeTabs.tabMaterials);
   }
}
