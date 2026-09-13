package btw.item.items;

import btw.entity.DynamiteEntity;
import btw.world.util.BlockPos;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class DynamiteItem extends Item {
   public DynamiteItem(int iItemID) {
      super(iItemID);
      this.setBuoyant();
      this.b("fcItemDynamite");
      this.a(CreativeTabs.tabCombat);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
      int iFlintAndSteelSlot = -1;

      for (int j = 0; j < entityPlayer.inventory.mainInventory.length; j++) {
         if (entityPlayer.inventory.mainInventory[j] != null && entityPlayer.inventory.mainInventory[j].itemID == Item.flintAndSteel.itemID) {
            iFlintAndSteelSlot = j;
            break;
         }
      }

      if (!world.isRemote) {
         boolean bLit = false;
         if (iFlintAndSteelSlot >= 0) {
            bLit = true;
            ItemStack flintAndSteelItemStack = entityPlayer.inventory.getStackInSlot(iFlintAndSteelSlot);
            flintAndSteelItemStack.damageItem(1, entityPlayer);
            if (flintAndSteelItemStack.stackSize <= 0) {
               entityPlayer.inventory.mainInventory[iFlintAndSteelSlot] = null;
            }
         }

         itemStack.stackSize--;
         DynamiteEntity dynamiteEnt = (DynamiteEntity)EntityList.createEntityOfType(DynamiteEntity.class, world, entityPlayer, this.itemID, bLit);
         world.spawnEntityInWorld(dynamiteEnt);
         if (bLit) {
            world.playSoundAtEntity(dynamiteEnt, "random.fuse", 1.0F, 1.0F);
         } else {
            world.playSoundAtEntity(dynamiteEnt, "random.bow", 0.5F, 0.4F / (e.nextFloat() * 0.4F + 0.8F));
         }
      }

      return itemStack;
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

      DynamiteEntity entity = (DynamiteEntity)EntityList.createEntityOfType(DynamiteEntity.class, world, dXPos, dYPos, dZPos, this.itemID);
      entity.setThrowableHeading(offsetPos.x, dYHeading, offsetPos.z, 1.1F, 6.0F);
      world.spawnEntityInWorld(entity);
      world.playAuxSFX(1002, i, j, k, 0);
      return true;
   }
}
