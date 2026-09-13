package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class StrawItem extends Item {
   public StrawItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setBellowsBlowDistance(2);
      this.setfurnaceburntime(FurnaceBurnTime.KINDLING);
      this.setFilterableProperties(4);
      this.setHerbivoreFoodValue(1600);
      this.b("fcItemStraw");
      this.a(CreativeTabs.tabMaterials);
   }
}
