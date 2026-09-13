package btw.item.items;

import btw.entity.mob.SheepEntity;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class DungItem extends Item {
   public DungItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.setIncineratedInCrucible();
      this.setFilterableProperties(2);
      this.b("fcItemDung");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean itemInteractionForEntity(ItemStack itemstack, EntityLiving entity) {
      if (entity instanceof SheepEntity) {
         entity.attackEntityFrom(DamageSource.generic, 0);
         return true;
      } else {
         return false;
      }
   }
}
