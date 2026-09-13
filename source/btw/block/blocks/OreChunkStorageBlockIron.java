package btw.block.blocks;

import btw.item.BTWItems;
import net.minecraft.src.World;

public class OreChunkStorageBlockIron extends OreChunkStorageBlock {
   public OreChunkStorageBlockIron(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockChunkOreStorageIron");
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.ironOreChunk.itemID, 9, 0, fChanceOfDrop);
      return true;
   }
}
