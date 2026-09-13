package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class BucketItemMilkChocolate extends BucketItemDrinkable {
   public BucketItemMilkChocolate(int iItemID) {
      super(iItemID, 9, 0.25F);
      this.b("fcItemBucketChocolateMilk");
   }

   @Override
   public int g() {
      return BTWBlocks.placedMilkChocolateBucket.blockID;
   }

   @Override
   public ItemStack onEaten(ItemStack itemStack, World world, EntityPlayer player) {
      if (!world.isRemote) {
         player.bB();
      }

      return super.onEaten(itemStack, world, player);
   }

   @Override
   public boolean doesConsumeContainerItemWhenCrafted(Item containerItem) {
      return containerItem.itemID == Item.bucketEmpty.itemID;
   }
}
