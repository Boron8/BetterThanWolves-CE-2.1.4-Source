package btw.block.tileentity.dispenser;

import btw.entity.DynamiteEntity;
import btw.item.BTWItems;
import net.minecraft.src.BehaviorProjectileDispense;
import net.minecraft.src.EntityList;
import net.minecraft.src.IPosition;
import net.minecraft.src.IProjectile;
import net.minecraft.src.World;

public class DynamiteDispenserBehavior extends BehaviorProjectileDispense {
   @Override
   protected IProjectile getProjectileEntity(World world, IPosition pos) {
      return (IProjectile)EntityList.createEntityOfType(DynamiteEntity.class, world, pos.getX(), pos.getY(), pos.getZ(), BTWItems.dynamite.itemID);
   }
}
