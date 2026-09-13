package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;

public class NetherBrickSidingAndCornerAndDecorativeBlock extends SidingAndCornerAndDecorativeWallBlock {
   public NetherBrickSidingAndCornerAndDecorativeBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.netherRockMaterial, "fcBlockDecorativeNetherBrick", 2.0F, 10.0F, Block.soundStoneFootstep, "fcNetherBrickSiding");
   }
}
