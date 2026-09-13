package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.model.BucketFullModel;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Icon;
import net.minecraft.src.World;

public abstract class BucketBlockFull extends BucketBlock {
   public BucketBlockFull(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!this.checkForFall(world, i, j, k)) {
         this.checkForSpillContents(world, i, j, k);
      }
   }

   @Override
   protected void initModels() {
      this.model = new BucketFullModel();
      this.modelTransformed = this.model;
   }

   public void checkForSpillContents(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      if (iFacing >= 2) {
         BlockPos targetPos = new BlockPos(i, j, k, iFacing);
         if (this.attemptToSpillIntoBlock(world, targetPos.x, targetPos.y, targetPos.z) && world.getBlockId(i, j, k) == this.blockID) {
            world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.placedBucket.blockID, this.setFacing(0, iFacing));
         }
      }
   }

   public abstract boolean attemptToSpillIntoBlock(World var1, int var2, int var3, int var4);

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRenderedOnFallingBlock(int iSide, int iMetadata) {
      int iFacing = this.getFacing(iMetadata);
      int iActiveID = this.modelTransformed.getActivePrimitiveID();
      return iActiveID == 4 ? iFacing == iSide : super.shouldSideBeRenderedOnFallingBlock(iSide, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      return this.modelTransformed.getActivePrimitiveID() == 4 ? this.getContentsIcon() : super.getIcon(iSide, iMetadata);
   }

   @Environment(EnvType.CLIENT)
   protected abstract Icon getContentsIcon();
}
