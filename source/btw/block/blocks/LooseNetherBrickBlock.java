package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.World;

public class LooseNetherBrickBlock extends MortarReceiverBlock {
   public LooseNetherBrickBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.netherRockMaterial);
      this.c(1.0F);
      this.b(5.0F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.c("fcBlockNetherBrickLoose");
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public boolean onMortarApplied(World world, int i, int j, int k) {
      world.setBlockWithNotify(i, j, k, Block.netherBrick.blockID);
      return true;
   }
}
