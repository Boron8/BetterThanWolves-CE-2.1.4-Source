package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemDoor;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public abstract class DoorItem extends Item {
   public DoorItem(int iITemID) {
      super(iITemID);
      this.maxStackSize = 1;
      this.a(CreativeTabs.tabRedstone);
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (iFacing == 1) {
         j++;
         if (player.canPlayerEdit(i, j, k, iFacing, stack)
            && player.canPlayerEdit(i, j + 1, k, iFacing, stack)
            && this.getDoorBlock().canPlaceBlockAt(world, i, j, k)) {
            int iDirection = MathHelper.floor_double((player.rotationYaw + 180.0F) * 4.0F / 360.0F - 0.5) & 3;
            ItemDoor.placeDoorBlock(world, i, j, k, iDirection, this.getDoorBlock());
            stack.stackSize--;
            return true;
         }
      }

      return false;
   }

   public abstract Block getDoorBlock();
}
