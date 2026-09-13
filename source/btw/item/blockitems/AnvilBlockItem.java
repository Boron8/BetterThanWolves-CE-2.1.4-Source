package btw.item.blockitems;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class AnvilBlockItem extends ItemBlock {
   public AnvilBlockItem(int iItemID) {
      super(iItemID);
   }

   @Override
   public void onUsedInCrafting(EntityPlayer player, ItemStack outputStack) {
      if (player.timesCraftedThisTick == 0) {
         player.playSound("random.anvil_land", 0.3F, player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }
   }

   @Override
   protected void playPlaceSound(World world, int i, int j, int k, Block block) {
      world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.anvil_use", 0.5F, world.rand.nextFloat() * 0.05F + 0.7F);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         player.playSound("random.anvil_use", 0.5F, world.rand.nextFloat() * 0.25F + 0.75F);
      }

      super.d(stack, world, player);
   }
}
