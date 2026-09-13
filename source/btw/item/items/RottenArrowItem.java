package btw.item.items;

import btw.entity.RottenArrowEntity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityList;
import net.minecraft.src.World;

public class RottenArrowItem extends ArrowItem {
   public RottenArrowItem(int iItemID) {
      super(iItemID);
      this.b("fcItemArrowRotten");
   }

   @Override
   EntityArrow getFiredArrowEntity(World world, double dXPos, double dYPos, double dZPos) {
      EntityArrow entity = (EntityArrow)EntityList.createEntityOfType(RottenArrowEntity.class, world, dXPos, dYPos, dZPos);
      entity.canBePickedUp = 2;
      return entity;
   }
}
