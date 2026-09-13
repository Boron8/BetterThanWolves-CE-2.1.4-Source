package btw.item.items;

import net.minecraft.src.CreativeTabs;

public class ClayItem extends MortarItem {
   public ClayItem(int iItemID) {
      super(iItemID);
      this.setFilterableProperties(2);
      this.b("clay");
      this.a(CreativeTabs.tabMaterials);
   }
}
