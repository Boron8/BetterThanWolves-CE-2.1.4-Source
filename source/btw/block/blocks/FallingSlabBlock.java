package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public abstract class FallingSlabBlock extends SlabBlock {
   public FallingSlabBlock(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Override
   public boolean attemptToCombineWithFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      if (entity.blockID == this.blockID && !this.getIsUpsideDown(world, i, j, k)) {
         this.convertToFullBlock(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean isFallingBlock() {
      return true;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      this.scheduleCheckForFall(world, i, j, k);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      this.scheduleCheckForFall(world, i, j, k);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!this.checkForFall(world, i, j, k) && this.getIsUpsideDown(world, i, j, k)) {
         this.setIsUpsideDown(world, i, j, k, false);
      }
   }

   @Override
   public int tickRate(World world) {
      return 2;
   }

   @Override
   protected void a(EntityFallingSand entity) {
      if (this.getIsUpsideDown(entity.metadata)) {
         entity.posY += 0.5;
      }

      entity.metadata = this.setIsUpsideDown(entity.metadata, false);
   }

   @Override
   public boolean canBePlacedUpsideDownAtLocation(World world, int i, int j, int k) {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldRenderWhileFalling(World world, EntityFallingSand entity) {
      int iCurrentBlockI = MathHelper.floor_double(entity.posX);
      int iCurrentBlockJ = MathHelper.floor_double(entity.posY);
      int iCurrentBlockK = MathHelper.floor_double(entity.posZ);
      int iBlockIDAtLocation = world.getBlockId(iCurrentBlockI, iCurrentBlockJ, iCurrentBlockK);
      Block fallingBlock = Block.blocksList[entity.blockID];
      if (iBlockIDAtLocation == entity.blockID) {
         if (entity.posY - iCurrentBlockJ < 0.4) {
            return false;
         }
      } else {
         FallingSlabBlock fallingSlab = (FallingSlabBlock)fallingBlock;
         if (fallingSlab.getCombinedBlockID(entity.metadata) == iBlockIDAtLocation) {
            return false;
         }
      }

      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolFromMetadata(iMetadata));
      renderBlocks.renderStandardFallingBlock(this, i, j, k, iMetadata);
   }
}
