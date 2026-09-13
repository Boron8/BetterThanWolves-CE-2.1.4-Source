package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class BucketBlockWater extends BucketBlockFull {
   @Environment(EnvType.CLIENT)
   private Icon iconWater;

   public BucketBlockWater(int iBlockID) {
      super(iBlockID);
      this.c("bucketWater");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneMod) {
      return Item.bucketWater.itemID;
   }

   @Override
   public boolean attemptToSpillIntoBlock(World world, int i, int j, int k) {
      if (!world.isAirBlock(i, j, k) && world.getBlockMaterial(i, j, k).isSolid()) {
         return false;
      } else {
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
                     world.setBlockAndMetadataWithNotify(i, j, k, Block.waterMoving.blockID, 6);
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

         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconWater = register.registerIcon("fcBlockBucket_water");
   }

   @Environment(EnvType.CLIENT)
   @Override
   protected Icon getContentsIcon() {
      return this.iconWater;
   }
}
