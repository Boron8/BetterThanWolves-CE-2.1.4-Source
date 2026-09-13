package btw.item.items;

import btw.block.BTWBlocks;
import net.minecraft.src.World;

public class BucketItemCement extends BucketItemFull {
   public BucketItemCement(int iBlockID) {
      super(iBlockID);
      this.b("fcItemBucketCement");
   }

   @Override
   public int g() {
      return BTWBlocks.placedCementBucket.blockID;
   }

   @Override
   protected boolean attemptPlaceContentsAtLocation(World world, int i, int j, int k) {
      if (!world.isAirBlock(i, j, k) && world.getBlockMaterial(i, j, k).isSolid()) {
         return false;
      } else {
         if (!world.isRemote) {
            world.playSoundEffect(i, j, k, "mob.ghast.moan", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            world.setBlockWithNotify(i, j, k, BTWBlocks.cement.blockID);
         }

         return true;
      }
   }
}
