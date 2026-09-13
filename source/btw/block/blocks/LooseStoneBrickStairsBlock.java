package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class LooseStoneBrickStairsBlock extends MortarReceiverStairsBlock {
   private int strata;

   public LooseStoneBrickStairsBlock(int iBlockID, int strata) {
      super(iBlockID, BTWBlocks.looseStoneBrick, strata << 2);
      this.strata = strata;
      this.setPicksEffectiveOn();
      this.c("fcBlockStoneBrickLooseStairs");
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      int blockID = Block.stairsStoneBrick.blockID;
      if (this.strata != 0) {
         if (this.strata == 1) {
            blockID = BTWBlocks.midStrataStoneBrickStairs.blockID;
         } else {
            blockID = BTWBlocks.deepStrataStoneBrickStairs.blockID;
         }
      }

      world.setBlockAndMetadataWithNotify(i, j, k, blockID, world.getBlockMetadata(i, j, k));
      return true;
   }

   public int getStrata() {
      return this.strata;
   }
}
