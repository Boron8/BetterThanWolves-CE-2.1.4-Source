package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.Item;

public class KnittingNeedlesItem extends Item {
   public KnittingNeedlesItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setfurnaceburntime(FurnaceBurnTime.SHAFT);
      this.setFilterableProperties(4);
      this.b("fcItemKnittingNeedles");
      this.a(CreativeTabs.tabTools);
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
