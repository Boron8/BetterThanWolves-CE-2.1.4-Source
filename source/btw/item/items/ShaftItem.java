package btw.item.items;

import btw.block.BTWBlocks;
import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.ItemReed;

public class ShaftItem extends ItemReed {
   public ShaftItem(int iItemID) {
      super(iItemID, 0);
      this.p();
      this.setBuoyant();
      this.setfurnaceburntime(FurnaceBurnTime.SHAFT);
      this.setIncineratedInCrucible();
      this.setFilterableProperties(4);
      this.b("stick");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public int g() {
      return BTWBlocks.placedShaft.blockID;
   }
}
