package btw.item;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class ItemTemplate extends Item {
   public ItemTemplate(int itemID) {
      super(itemID);
      this.setNonBuoyant();
      this.setBellowsBlowDistance(0);
      this.setNotIncineratedInCrucible();
      this.setfurnaceburntime(FurnaceBurnTime.NONE);
      this.setFilterableProperties(0);
      this.a(CreativeTabs.tabMisc);
      this.b("fcItemTemplate");
   }
}
