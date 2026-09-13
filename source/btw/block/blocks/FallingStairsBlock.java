package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class FallingStairsBlock extends StairsBlock {
   protected FallingStairsBlock(int iBlockID, Block referenceBlock, int iReferenceBlockMetadata) {
      super(iBlockID, referenceBlock, iReferenceBlockMetadata);
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
   public int tickRate(World par1World) {
      return 2;
   }

   @Override
   protected void a(EntityFallingSand entity) {
      entity.metadata = this.setIsUpsideDown(entity.metadata, false);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderAllFaces(true);
      renderBlocks.setRenderBounds(this.getBoundsFromPoolForBase(iMetadata));
      renderBlocks.renderStandardFallingBlock(this, i, j, k, iMetadata);
      renderBlocks.setRenderBounds(this.getBoundsFromPoolForSecondaryPiece(iMetadata));
      renderBlocks.renderStandardFallingBlock(this, i, j, k, iMetadata);
      renderBlocks.setRenderAllFaces(false);
   }
}
