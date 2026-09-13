package btw.item.items;

import net.minecraft.src.ItemFood;

public class HardBoiledEggItem extends ItemFood {
   private static final int HARD_BOILED_EGG_HEALTH_HEALED = 3;
   private static final float HARD_BOILED_EGG_SATURATION_MODIFIER = 0.25F;

   public HardBoiledEggItem(int iItemID) {
      super(iItemID, 3, 0.25F, false);
      this.setNeutralBuoyant();
      this.setFilterableProperties(2);
      this.b("fcItemEggPoached");
   }
}
