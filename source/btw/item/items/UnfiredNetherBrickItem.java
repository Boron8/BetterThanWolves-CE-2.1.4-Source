package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class UnfiredNetherBrickItem extends PlaceAsBlockItem {
   public UnfiredNetherBrickItem(int iItemID) {
      super(iItemID, BTWBlocks.unfiredPottery.blockID, 7);
      this.setNeutralBuoyant();
      this.b("fcItemBrickNetherUnfired");
      this.a(CreativeTabs.tabMaterials);
   }

   @Override
   public void onCreated(ItemStack stack, World world, EntityPlayer player) {
      if (player.timesCraftedThisTick == 0 && world.isRemote) {
         player.playSound("mob.slime.attack", 0.25F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.1F + 0.7F);
      }

      super.d(stack, world, player);
   }
}
