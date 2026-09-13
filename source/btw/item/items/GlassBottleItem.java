package btw.item.items;

import btw.util.MiscUtils;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemGlassBottle;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.World;

public class GlassBottleItem extends ItemGlassBottle {
   public GlassBottleItem(int par1) {
      super(par1);
   }

   @Override
   public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
      MovingObjectPosition movingobjectposition = MiscUtils.getMovingObjectPositionFromPlayerHitWaterAndLava(world, player, true);
      if (movingobjectposition == null) {
         return itemStack;
      } else {
         if (movingobjectposition.typeOfHit == EnumMovingObjectType.TILE) {
            int i = movingobjectposition.blockX;
            int j = movingobjectposition.blockY;
            int k = movingobjectposition.blockZ;
            if (!world.canMineBlock(player, i, j, k)) {
               return itemStack;
            }

            if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, itemStack)) {
               return itemStack;
            }

            if (world.getBlockMaterial(i, j, k) == Material.water) {
               itemStack.stackSize--;
               if (itemStack.stackSize <= 0) {
                  return new ItemStack(Item.potion);
               }

               if (!player.inventory.addItemStackToInventory(new ItemStack(Item.potion))) {
                  player.dropPlayerItem(new ItemStack(Item.potion.itemID, 1, 0));
               }
            }
         }

         return itemStack;
      }
   }
}
