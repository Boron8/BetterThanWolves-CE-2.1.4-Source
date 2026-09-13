package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.DirtSlabBlock;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class GrassSlabBlockITem extends SlabBlockItem {
   public GrassSlabBlockITem(int id) {
      super(id);
   }

   @Override
   public boolean canCombineWithBlock(World world, int x, int y, int z, int itemDamage) {
      int blockID = world.getBlockId(x, y, z);
      if (blockID == BTWBlocks.dirtSlab.blockID) {
         int targetSubtype = BTWBlocks.dirtSlab.getSubtype(world, x, y, z);
         if (targetSubtype != 3) {
            return true;
         }
      } else if (blockID == BTWBlocks.grassSlab.blockID) {
         return true;
      }

      return false;
   }

   @Override
   public boolean convertToFullBlock(World world, int x, int y, int z) {
      int blockID = world.getBlockId(x, y, z);
      if (blockID == BTWBlocks.dirtSlab.blockID) {
         DirtSlabBlock slabBlock = BTWBlocks.dirtSlab;
         boolean isTargetUpsideDown = slabBlock.getIsUpsideDown(world, x, y, z);
         int targetSubType = slabBlock.getSubtype(world, x, y, z);
         return isTargetUpsideDown && targetSubType == 0
            ? world.setBlockWithNotify(x, y, z, Block.dirt.blockID)
            : world.setBlockWithNotify(x, y, z, Block.grass.blockID);
      } else {
         return blockID == BTWBlocks.grassSlab.blockID ? world.setBlockWithNotify(x, y, z, Block.grass.blockID) : false;
      }
   }
}
