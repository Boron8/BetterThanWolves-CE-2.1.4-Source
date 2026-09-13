package btw.block.tileentity.dispenser;

import btw.entity.BroadheadArrowEntity;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntityList;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class BroadheadArrowDispenserBehavior extends BehaviorProjectileDispense {
   @Override
   protected IProjectile getProjectileEntity(World world, IPosition position) {
      BroadheadArrowEntity arrow = (BroadheadArrowEntity)EntityList.createEntityOfType(
         BroadheadArrowEntity.class, world, position.getX(), position.getY(), position.getZ()
      );
      arrow.canBePickedUp = 1;
      return arrow;
   }
}
