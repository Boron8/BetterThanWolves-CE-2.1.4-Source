package btw.block.tileentity.dispenser;

import btw.entity.RottenArrowEntity;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntityList;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class RottedArrowDispenserBehavior extends BehaviorProjectileDispense {
   @Override
   protected IProjectile getProjectileEntity(World world, IPosition position) {
      RottenArrowEntity arrow = (RottenArrowEntity)EntityList.createEntityOfType(
         RottenArrowEntity.class, world, position.getX(), position.getY(), position.getZ()
      );
      arrow.canBePickedUp = 2;
      return arrow;
   }
}
