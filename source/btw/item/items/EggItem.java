package btw.item.items;

import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityEgg;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class EggItem extends ThrowableItem {
   public EggItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 16;
      this.setIncineratedInCrucible();
      this.setFilterableProperties(2);
      this.b("egg");
      this.a(CreativeTabs.tabFood);
   }

   @Override
   protected void spawnThrownEntity(ItemStack stack, World world, EntityPlayer player) {
      world.spawnEntityInWorld(EntityList.createEntityOfType(EntityEgg.class, world, player));
   }

   @Override
   protected EntityThrowable getEntityFiredByByBlockDispenser(World world, double dXPos, double dYPos, double dZPos) {
      return (EntityThrowable)EntityList.createEntityOfType(EntityEgg.class, world, dXPos, dYPos, dZPos);
   }
}
