package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import btw.block.blocks.MushroomCapBlock;
import btw.block.util.Flammability;
import net.minecraft.src.World;

public class LegacyMushroomCapBlock extends MushroomCapBlock {
   public LegacyMushroomCapBlock(int iBlockID, int iMushroomType) {
      super(iBlockID, iMushroomType);
      this.setFireProperties(Flammability.NONE);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.a(world, i, j, k);
      int iNewBlockID = BTWBlocks.brownMushroomCap.blockID;
      if (this.mushroomType != 0) {
         iNewBlockID = BTWBlocks.redMushroomCap.blockID;
      }

      world.setBlock(i, j, k, iNewBlockID, world.getBlockMetadata(i, j, k), 2);
   }
}
