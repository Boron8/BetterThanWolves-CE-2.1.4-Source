package btw.item.items;

import btw.entity.UrnEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class SoulUrnItem extends ThrowableItem {
   public SoulUrnItem(int iItemID) {
      super(iItemID);
      this.maxStackSize = 16;
      this.setBuoyant();
      this.b("fcItemUrnSoul");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public boolean isMultiUsePerClick() {
      return false;
   }

   @Override
   protected void spawnThrownEntity(ItemStack stack, World world, EntityPlayer player) {
      world.spawnEntityInWorld(EntityList.createEntityOfType(UrnEntity.class, world, player, this.itemID));
   }

   @Override
   protected EntityThrowable getEntityFiredByByBlockDispenser(World world, double dXPos, double dYPos, double dZPos) {
      return (EntityThrowable)EntityList.createEntityOfType(UrnEntity.class, world, dXPos, dYPos, dZPos, this.itemID);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean hasEffect(ItemStack itemStack) {
      return true;
   }
}
