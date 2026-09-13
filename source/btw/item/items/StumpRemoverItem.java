package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class StumpRemoverItem extends Item {
   public StumpRemoverItem(int iItemID) {
      super(iItemID);
      this.e(0);
      this.a(false);
      this.maxStackSize = 16;
      this.setBuoyant();
      this.setBellowsBlowDistance(1);
      this.setFilterableProperties(2);
      this.b("fcItemStumpRemover");
      this.a(CreativeTabs.tabTools);
   }

   @Override
   public boolean onItemUse(
      ItemStack itemStack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ
   ) {
      if (player != null && !player.canPlayerEdit(i, j, k, iFacing, itemStack)) {
         return false;
      } else if (itemStack.stackSize == 0) {
         return false;
      } else {
         int iTargetBlockID = world.getBlockId(i, j, k);
         Block blockTarget = Block.blocksList[iTargetBlockID];
         if (blockTarget != null && blockTarget.getDoesStumpRemoverWorkOnBlock(world, i, j, k)) {
            if (!world.isRemote) {
               world.setBlockWithNotify(i, j, k, 0);
               world.playAuxSFX(2267, i, j, k, 0);
            }

            itemStack.stackSize--;
            return true;
         } else {
            return false;
         }
      }
   }
}
