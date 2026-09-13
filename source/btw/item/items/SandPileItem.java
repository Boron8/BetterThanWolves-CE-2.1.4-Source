package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class SandPileItem extends Item {
   public SandPileItem(int iItemID) {
      super(iItemID);
      this.setBellowsBlowDistance(2);
      this.setFilterableProperties(8);
      this.b("fcItemPileSand");
      this.a(CreativeTabs.tabMaterials);
   }
}
