package btw.item.items;

import net.minecraft.src.ItemSnowball;

public class SnowballItem extends ItemSnowball {
   public SnowballItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setFilterableProperties(2);
      this.b("snowball");
   }
}
