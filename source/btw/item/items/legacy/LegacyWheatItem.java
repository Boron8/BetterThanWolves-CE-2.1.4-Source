package btw.item.items.legacy;

import net.minecraft.src.Item;

public class LegacyWheatItem extends Item {
   public LegacyWheatItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(4);
      this.setAsBasicHerbivoreFood();
      this.setAsBasicPigFood();
      this.b("wheat");
   }
}
