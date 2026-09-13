package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class WickerPieceItem extends Item {
   public WickerPieceItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setBellowsBlowDistance(2);
      this.setIncineratedInCrucible();
      this.setfurnaceburntime(FurnaceBurnTime.WICKER_PIECE);
      this.setFilterableProperties(16);
      this.b("fcItemWickerPiece");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean getCanBeFedDirectlyIntoCampfire(int iItemDamage) {
      return false;
   }

   @Override
   public boolean getCanBeFedDirectlyIntoBrickOven(int iItemDamage) {
      return false;
   }
}
