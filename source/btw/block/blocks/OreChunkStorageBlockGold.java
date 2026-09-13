package btw.block.blocks;

import btw.item.BTWItems;
import net.minecraft.src.World;

public class OreChunkStorageBlockGold extends OreChunkStorageBlock {
   public OreChunkStorageBlockGold(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockChunkOreStorageGold");
   }

   @Override
   public boolean dropComponentItemsOnBadBreak(World world, int i, int j, int k, int iMetadata, float fChanceOfDrop) {
      this.dropItemsIndividually(world, i, j, k, BTWItems.goldOreChunk.itemID, 9, 0, fChanceOfDrop);
      return true;
   }
}
