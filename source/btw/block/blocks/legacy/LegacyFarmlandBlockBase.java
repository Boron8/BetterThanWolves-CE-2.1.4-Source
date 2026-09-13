package btw.block.blocks.legacy;

import btw.block.BTWBlocks;
import btw.block.blocks.FarmlandBlockBase;
import net.minecraft.src.Block;
import net.minecraft.src.Material;
import net.minecraft.src.World;

public abstract class LegacyFarmlandBlockBase extends FarmlandBlockBase {
   protected LegacyFarmlandBlockBase(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      super.a(world, i, j, k, iNeighborBlockID);
      Block blockAbove = Block.blocksList[world.getBlockId(i, j + 1, k)];
      Material material = world.getBlockMaterial(i, j + 1, k);
      if (blockAbove != null) {
         if (blockAbove.blockMaterial.isSolid()) {
            world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
         } else if (blockAbove.getConvertsLegacySoil(world, i, j + 1, k)) {
            this.convertToNewSoil(world, i, j, k);
         }
      }
   }

   @Override
   protected boolean isHydrated(int iMetadata) {
      return iMetadata > 0;
   }

   @Override
   protected int setFullyHydrated(int iMetadata) {
      return iMetadata | 7;
   }

   @Override
   protected void dryIncrementally(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata - 1);
   }

   protected abstract void convertToNewSoil(World var1, int var2, int var3, int var4);
}
