package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class LegacyFarmlandBlockUnfertilized extends LegacyFarmlandBlockBase {
   public LegacyFarmlandBlockUnfertilized(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockFarmlandFertilizedNew");
   }

   @Override
   public boolean getCanGrassSpreadToBlock(World world, int i, int j, int k) {
      return world.isAirBlock(i, j + 1, k);
   }

   @Override
   public boolean spreadGrassToBlock(World world, int i, int j, int k) {
      if (world.rand.nextInt(3) == 0) {
         world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.tallGrass.blockID, 1);
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected boolean isFertilized(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   protected void setFertilized(World world, int i, int j, int k) {
      int iTargetBlockMetadata = world.getBlockMetadata(i, j, k);
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.legacyFertilizedFarmland.blockID, iTargetBlockMetadata);
   }

   @Override
   protected void convertToNewSoil(World world, int i, int j, int k) {
      int iNewMetadata = 0;
      if (this.isHydrated(world, i, j, k)) {
         iNewMetadata = BTWBlocks.farmland.setFullyHydrated(iNewMetadata);
      }

      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.farmland.blockID, iNewMetadata);
   }
}
