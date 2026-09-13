package btw.item.items;

import net.minecraft.src.Item;
import net.minecraft.src.PotionHelper;

public class MysteriousGlandItem extends Item {
   public MysteriousGlandItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setBellowsBlowDistance(2);
      this.setFilterableProperties(2);
      this.c(PotionHelper.speckledMelonEffect);
      this.b("fcItemMysteriousGland");
   }
}
