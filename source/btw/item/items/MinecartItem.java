package btw.item.items;

import btw.world.util.BlockPos;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.ItemMinecart;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class MinecartItem extends ItemMinecart {
   public MinecartItem(int iItemID, int iMinecartType) {
      super(iItemID, iMinecartType);
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos offsetPos = new BlockPos(0, 0, 0, iFacing);
      double dXPos = i + offsetPos.x * 1.0 + 0.5;
      double dYPos = j + offsetPos.y;
      double dZPos = k + offsetPos.z * 1.0 + 0.5;
      List list = world.getEntitiesWithinAABB(
         EntityMinecart.class, AxisAlignedBB.getAABBPool().getAABB(dXPos, dYPos, dZPos, dXPos + 1.0, dYPos + 1.0, dZPos + 1.0)
      );
      if (list != null && list.size() > 0) {
         return false;
      } else {
         Entity entity = EntityMinecart.createMinecart(world, dXPos, dYPos, dZPos, ((ItemMinecart)stack.getItem()).minecartType);
         world.spawnEntityInWorld(entity);
         entity.setVelocity(offsetPos.x, offsetPos.y, offsetPos.z);
         world.playAuxSFX(1000, i, j, k, 0);
         return true;
      }
   }
}
