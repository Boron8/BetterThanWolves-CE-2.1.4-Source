package btw.item.items;

import btw.block.BTWBlocks;
import btw.util.MiscUtils;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class BucketItemWater extends BucketItemFull {
   public BucketItemWater(int iItemID) {
      super(iItemID);
      this.b("bucketWater");
   }

   @Override
   public int g() {
      return BTWBlocks.placedWaterBucket.blockID;
   }

   @Override
   protected boolean attemptPlaceContentsAtLocation(World world, int i, int j, int k) {
      if (!world.isAirBlock(i, j, k) && world.getBlockMaterial(i, j, k).isSolid()) {
         return false;
      } else {
         if (!world.isRemote) {
            if (world.provider.isHellWorld) {
               world.playAuxSFX(2278, i, j, k, 0);
            } else {
               int iTargetBlockID = world.getBlockId(i, j, k);
               int iTargetMetadata = world.getBlockMetadata(i, j, k);
               if (iTargetBlockID != Block.lavaMoving.blockID && iTargetBlockID != Block.lavaStill.blockID) {
                  if (iTargetBlockID != Block.waterMoving.blockID && iTargetBlockID != Block.waterStill.blockID || iTargetMetadata != 0) {
                     if (world.provider.dimensionId == 1) {
                        world.setBlockWithNotify(i, j, k, Block.waterMoving.blockID);
                     } else {
                        MiscUtils.placeNonPersistentWater(world, i, j, k);
                     }
                  }
               } else {
                  world.playAuxSFX(2278, i, j, k, 0);
                  if (iTargetMetadata == 0) {
                     world.setBlockWithNotify(i, j, k, Block.obsidian.blockID);
                  } else {
                     world.setBlockWithNotify(i, j, k, BTWBlocks.lavaPillow.blockID);
                  }
               }
            }
         }

         return true;
      }
   }
}
