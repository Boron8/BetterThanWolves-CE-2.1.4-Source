package btw.item.blockitems;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;

public class DormantSoulforgeBlockItem extends ItemBlock {
   public DormantSoulforgeBlockItem(int iItemID) {
      super(iItemID);
      this.b("fcBlockSoulforgeDormant");
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      if (player.timesCraftedThisTick == 0) {
         player.playSound("random.anvil_land", 0.3F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }
   }
}
