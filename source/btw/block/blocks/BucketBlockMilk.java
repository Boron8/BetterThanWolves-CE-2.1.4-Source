package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.World;

public class BucketBlockMilk extends BucketBlockFull {
   @Environment(EnvType.CLIENT)
   private Icon iconContents;

   public BucketBlockMilk(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockBucketMilk");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneMod) {
      return Item.bucketMilk.itemID;
   }

   @Override
   public boolean attemptToSpillIntoBlock(World world, int i, int j, int k) {
      if (!world.isAirBlock(i, j, k) && world.getBlockMaterial(i, j, k).isSolid()) {
         return false;
      } else {
         world.setBlockWithNotify(i, j, k, BTWBlocks.milkFluid.blockID);
         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconContents = register.registerIcon("fcBlockBucket_milk");
   }

   @Environment(EnvType.CLIENT)
   @Override
   protected Icon getContentsIcon() {
      return this.iconContents;
   }
}
