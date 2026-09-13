package btw.block.blocks;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class MortarReceiverBlock extends FallingFullBlock {
   public MortarReceiverBlock(int iBlockID, Material material) {
      super(iBlockID, material);
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
}
