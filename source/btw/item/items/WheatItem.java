package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class WheatItem extends Item {
   public WheatItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(8);
      this.setAsBasicHerbivoreFood();
      this.setAsBasicPigFood();
      this.b("fcItemWheat");
      this.a(CreativeTabs.tabMaterials);
   }
}
