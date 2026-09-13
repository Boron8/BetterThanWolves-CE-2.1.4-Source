package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class NetherBrickItem extends Item {
   public NetherBrickItem(int iItemID) {
      super(iItemID);
      this.b("fcItemBrickNether");
      this.a(CreativeTabs.tabMaterials);
   }
}
