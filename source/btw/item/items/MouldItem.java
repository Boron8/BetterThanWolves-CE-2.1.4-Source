package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class MouldItem extends Item {
   public MouldItem(int iItemID) {
      super(iItemID);
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean isConsumedInCrafting() {
      return false;
   }
}
