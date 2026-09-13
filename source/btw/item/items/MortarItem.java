package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class MortarItem extends Item {
   public MortarItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (player != null && player.canPlayerEdit(i, j, k, iFacing, stack)) {
         Block targetBlock = Block.blocksList[world.getBlockId(i, j, k)];
         if (targetBlock != null && targetBlock.onMortarApplied(world, i, j, k)) {
            if (!world.isRemote) {
               world.playAuxSFX(2274, i, j, k, 0);
            }

            stack.stackSize--;
            return true;
         }
      }

      return false;
   }
}
