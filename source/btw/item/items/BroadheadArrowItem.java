package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.entity.BroadheadArrowEntity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityList;
import net.minecraft.src.World;

public class BroadheadArrowItem extends ArrowItem {
   public BroadheadArrowItem(int iItemID) {
      super(iItemID);
      this.setNeutralBuoyant();
      this.setBellowsBlowDistance(0);
      this.setNotIncineratedInCrucible();
      this.setfurnaceburntime(FurnaceBurnTime.NONE);
      this.b("fcItemArrowBroadhead");
   }

   @Override
   EntityArrow getFiredArrowEntity(World world, double dXPos, double dYPos, double dZPos) {
      EntityArrow entity = (EntityArrow)EntityList.createEntityOfType(BroadheadArrowEntity.class, world, dXPos, dYPos, dZPos);
      entity.canBePickedUp = 1;
      return entity;
   }
}
