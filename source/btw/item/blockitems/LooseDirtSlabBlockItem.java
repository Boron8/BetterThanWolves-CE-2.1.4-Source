package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.SlabBlock;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class LooseDirtSlabBlockItem extends SlabBlockItem {
   public LooseDirtSlabBlockItem(int itemID) {
      super(itemID);
   }

   @Override
   public boolean canCombineWithBlock(World world, int x, int y, int z, int itemDamage) {
      int blockID = world.getBlockId(x, y, z);
      if (blockID == BTWBlocks.dirtSlab.blockID) {
         int metadata = world.getBlockMetadata(x, y, z);
         int subtype = BTWBlocks.dirtSlab.getSubtype(metadata);
         return subtype != 3 && !BTWBlocks.dirtSlab.getIsUpsideDown(metadata);
      } else {
         return blockID != BTWBlocks.myceliumSlab.blockID && blockID != BTWBlocks.grassSlab.blockID
            ? super.canCombineWithBlock(world, x, y, z, itemDamage)
            : true;
      }
   }

   @Override
   public boolean convertToFullBlock(World world, int x, int y, int z) {
      int newBlockID = ((SlabBlock)Block.blocksList[this.g()]).getCombinedBlockID(0);
      return world.setBlockWithNotify(x, y, z, newBlockID);
   }
}
