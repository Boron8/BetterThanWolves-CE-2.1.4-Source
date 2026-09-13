package btw.block.blocks;

import btw.item.BTWItems;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class SandstoneStairsBlock extends StairsBlock {
   public SandstoneStairsBlock(int iBlockID) {
      super(iBlockID, Block.sandStone, 0);
      this.setPicksEffectiveOn();
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 3;
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.sandPile.itemID, 12, 0, fChanceOfDrop);
      return true;
   }
}
