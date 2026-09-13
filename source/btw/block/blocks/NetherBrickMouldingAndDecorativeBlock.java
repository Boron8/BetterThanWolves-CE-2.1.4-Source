package btw.block.blocks;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;

public class NetherBrickMouldingAndDecorativeBlock extends MouldingAndDecorativeWallBlock {
   public NetherBrickMouldingAndDecorativeBlock(int iBlockID, int iMatchingCornerBlockID) {
      super(
         iBlockID,
         BTWBlocks.netherRockMaterial,
         "fcBlockDecorativeNetherBrick",
         "fcBlockColumnNetherBrick_side",
         iMatchingCornerBlockID,
         2.0F,
         10.0F,
         Block.soundStoneFootstep,
         "fcNetherBrickMoulding"
      );
   }
}
