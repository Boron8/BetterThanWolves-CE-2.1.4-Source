package btw.item.blockitems;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class FreshPumpkinBlockItem extends ItemBlock {
   public FreshPumpkinBlockItem(int iBlockID) {
      super(iBlockID);
      this.d(16);
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      if (outputStack.itemID == Block.pumpkin.blockID && player.timesCraftedThisTick == 0) {
         player.playSound("mob.slime.attack", 0.5F, (player.worldObj.rand.nextFloat() - player.worldObj.rand.nextFloat()) * 0.1F + 0.7F);
      }
   }
}
