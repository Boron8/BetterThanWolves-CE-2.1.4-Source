package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class FlintItem extends Item {
   public FlintItem(int iItemID) {
      super(iItemID);
      this.setFilterableProperties(2);
      this.b("flint");
      this.a(CreativeTabs.tabMaterials);
   }
}
