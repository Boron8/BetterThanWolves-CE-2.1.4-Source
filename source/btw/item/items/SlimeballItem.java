package btw.item.items;

import net.minecraft.src.CreativeTabs;

public class SlimeballItem extends MortarItem {
   public SlimeballItem(int iItemID) {
      super(iItemID);
      this.setNeutralBuoyant();
      this.setFilterableProperties(2);
      this.b("slimeball");
      this.a(CreativeTabs.tabMisc);
   }
}
