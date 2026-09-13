package btw.block.blocks;

import btw.block.BTWBlocks;

public class InfiniteUnlitTorchBlock extends TorchBlockUnlitBase {
   public InfiniteUnlitTorchBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockTorchIdle");
   }

   @Override
   protected int getLitBlockID() {
      return BTWBlocks.infiniteBurningTorch.blockID;
   }
}
