package btw.item.items;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemFireball;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class FireChargeItem extends ItemFireball {
   public FireChargeItem(int iItemID) {
      super(iItemID);
      this.b("fireball");
   }

   @Override
   public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int i, int j, int k, int iFacing, float fClickX, float fClickY, float fClickZ) {
      if (!world.isRemote) {
         Block clickedBlock = Block.blocksList[world.getBlockId(i, j, k)];
         if (clickedBlock != null && clickedBlock.getCanBeSetOnFireDirectlyByItem(world, i, j, k)) {
            clickedBlock.setOnFireDirectly(world, i, j, k);
            if (!player.capabilities.isCreativeMode) {
               stack.stackSize--;
            }

            return true;
         }
      }

      return super.onItemUse(stack, player, world, i, j, k, iFacing, fClickX, fClickY, fClickZ);
   }
}
