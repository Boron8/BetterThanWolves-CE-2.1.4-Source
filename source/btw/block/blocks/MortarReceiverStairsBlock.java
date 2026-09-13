package btw.block.blocks;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public abstract class MortarReceiverStairsBlock extends FallingStairsBlock {
   protected MortarReceiverStairsBlock(int iBlockID, Block referenceBlock, int iReferenceBlockMetadata) {
      super(iBlockID, referenceBlock, iReferenceBlockMetadata);
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (this.hasNeighborWithMortarInContact(world, i, j, k)) {
         world.playAuxSFX(2275, i, j, k, 0);
         world.scheduleBlockUpdate(i, j, k, this.blockID, 40);
      } else {
         this.scheduleCheckForFall(world, i, j, k);
      }
   }

   @Override
   protected int validateMetadataForLocation(World world, int i, int j, int k, int iMetadata) {
      if (this.getIsUpsideDown(iMetadata)) {
         int iFacing = this.convertDirectionToFacing(this.getDirection(iMetadata));
         if (!this.hasNeighborWithMortarInContact(world, i, j, k, iFacing, true)) {
            iMetadata = this.setIsUpsideDown(iMetadata, false);
         }
      }

      return iMetadata;
   }
}
