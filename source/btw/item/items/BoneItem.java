package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class BoneItem extends Item {
   public BoneItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 16;
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setFilterableProperties(4);
      this.p();
      this.b("bone");
      this.a(CreativeTabs.tabMisc);
   }
}
