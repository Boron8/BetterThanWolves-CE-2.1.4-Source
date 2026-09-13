package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class DirtPileItem extends Item {
   public DirtPileItem(int iItemID) {
      super(iItemID);
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(8);
      this.b("fcItemPileDirt");
      this.a(CreativeTabs.tabMaterials);
   }
}
