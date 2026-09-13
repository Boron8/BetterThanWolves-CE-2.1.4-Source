package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.EnumToolMaterial;

public class ChiselItemWood extends ChiselItem {
   public ChiselItemWood(int iItemID) {
      super(iItemID, EnumToolMaterial.WOOD, 2);
      this.setBuoyant();
      this.setfurnaceburntime(FurnaceBurnTime.SHAFT.burnTime / 2);
      this.setFilterableProperties(4);
      this.efficiencyOnProperMaterial /= 4.0F;
      this.b("fcItemChiselWood");
   }
}
