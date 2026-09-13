package btw.item.blockitems;

import btw.block.BTWBlocks;
import btw.block.blocks.DirtSlabBlock;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.World;

public class DirtSlabBlockItem extends SlabBlockItem {
   public DirtSlabBlockItem(int id) {
      super(id);
   }

   @Override
   public int getMetadata(int itemDamage) {
      return itemDamage << 1;
   }

   @Override
   public String getUnlocalizedName(ItemStack itemstack) {
      switch (itemstack.getItemDamage()) {
         case 1:
            return super.a() + ".grass";
         case 3:
            return super.a() + ".packed";
         default:
            return super.a();
      }
   }

   @Override
   public boolean canCombineWithBlock(World world, int x, int y, int z, int itemDamage) {
      int blockID = world.getBlockId(x, y, z);
      if (blockID == BTWBlocks.dirtSlab.blockID) {
         int targetSubtype = BTWBlocks.dirtSlab.getSubtype(world, x, y, z);
         if (targetSubtype != 3 && itemDamage != 3) {
            return true;
         }

         if (targetSubtype == itemDamage) {
            return true;
         }
      } else if ((blockID == BTWBlocks.myceliumSlab.blockID || blockID == BTWBlocks.grassSlab.blockID) && itemDamage != 3) {
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
         int newBlockID = Block.dirt.blockID;
         int newMetadata = 0;
         if (targetSubType == 3) {
            newBlockID = BTWBlocks.aestheticEarth.blockID;
            newMetadata = 6;
         } else if (isTargetUpsideDown && targetSubType == 1) {
            newBlockID = Block.grass.blockID;
         }

         return world.setBlockAndMetadataWithNotify(x, y, z, newBlockID, newMetadata);
      } else if (blockID == BTWBlocks.myceliumSlab.blockID) {
         return BTWBlocks.myceliumSlab.getIsUpsideDown(world, x, y, z)
            ? world.setBlockAndMetadataWithNotify(x, y, z, Block.mycelium.blockID, 0)
            : world.setBlockAndMetadataWithNotify(x, y, z, Block.dirt.blockID, 0);
      } else if (blockID == BTWBlocks.grassSlab.blockID) {
         return BTWBlocks.grassSlab.getIsUpsideDown(world, x, y, z)
            ? world.setBlockAndMetadataWithNotify(x, y, z, Block.grass.blockID, 0)
            : world.setBlockAndMetadataWithNotify(x, y, z, Block.dirt.blockID, 0);
      } else {
         return false;
      }
   }
}
