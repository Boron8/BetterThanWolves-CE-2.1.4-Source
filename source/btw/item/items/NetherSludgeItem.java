package btw.item.items;

import net.minecraft.src.CreativeTabs;

public class NetherSludgeItem extends MortarItem {
   public NetherSludgeItem(int iItemID) {
      super(iItemID);
      this.setNeutralBuoyant();
      this.b("fcItemNetherSludge");
      this.a(CreativeTabs.tabMaterials);
   }
}
