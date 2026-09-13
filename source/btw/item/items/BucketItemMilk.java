package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BucketItemMilk extends BucketItemDrinkable {
   public BucketItemMilk(int iItemID) {
      super(iItemID, 6, 0.25F);
      this.b("milk");
   }

   @Override
   public int g() {
      return BTWBlocks.placedMilkBucket.blockID;
   }

   @Override
   public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
      if (!world.isRemote) {
         player.bB();
      }

      return super.onEaten(itemStack, world, player);
   }
}
