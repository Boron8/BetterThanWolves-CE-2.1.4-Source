package btw.item.items;

import btw.crafting.util.FurnaceBurnTime;
import btw.world.util.BlockPos;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityList;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class ArrowItem extends Item {
   public ArrowItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setBellowsBlowDistance(1);
      this.setIncineratedInCrucible();
      this.setfurnaceburntime(FurnaceBurnTime.SHAFT);
      this.setFilterableProperties(4);
      this.b("arrow");
      this.a(CreativeTabs.tabCombat);
   }

   @Override
   public boolean onItemUsedByBlockDispenser(ItemStack stack, World world, int i, int j, int k, int iFacing) {
      BlockPos offsetPos = new BlockPos(0, 0, 0, iFacing);
      double dXPos = i + offsetPos.x * 0.6 + 0.5;
      double dYPos = j + offsetPos.y * 0.6 + 0.5;
      double dZPos = k + offsetPos.z * 0.6 + 0.5;
      double dYHeading;
      if (iFacing > 2) {
         dYHeading = 0.1F;
      } else {
         dYHeading = offsetPos.y;
      }

      EntityArrow entityarrow = this.getFiredArrowEntity(world, dXPos, dYPos, dZPos);
      entityarrow.setThrowableHeading(offsetPos.x, dYHeading, offsetPos.z, 1.1F, 6.0F);
      world.spawnEntityInWorld(entityarrow);
      world.playAuxSFX(1002, i, j, k, 0);
      return true;
   }

   EntityArrow getFiredArrowEntity(World world, double dXPos, double dYPos, double dZPos) {
      EntityArrow entity = (EntityArrow)EntityList.createEntityOfType(EntityArrow.class, world, dXPos, dYPos, dZPos);
      entity.canBePickedUp = 1;
      return entity;
   }
}
