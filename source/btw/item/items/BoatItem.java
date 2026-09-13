package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.world.util.BlockPos;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityList;
import net.minecraft.src.ItemBoat;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BoatItem extends ItemBoat {
   public BoatItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setfurnaceburntime(5 * FurnaceBurnTime.PLANKS_JUNGLE.burnTime / 2);
      this.b("boat");
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos offsetPos = new BlockPos(0, 0, 0, iFacing);
      double dXPos = i + offsetPos.x * 1.6 + 0.5;
      double dYPos = j + offsetPos.y;
      double dZPos = k + offsetPos.z * 1.6 + 0.5;
      double dBoatYPos = j + offsetPos.y;
      Entity entity = EntityList.createEntityOfType(EntityBoat.class, world, dXPos, dYPos, dZPos);
      world.spawnEntityInWorld(entity);
      world.playAuxSFX(1000, i, j, k, 0);
      return true;
   }
}
