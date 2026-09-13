package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.BlockMushroom;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.World;

public class MushroomBlock extends BlockMushroom {
   public MushroomBlock(int iBlockID, String iconName) {
      super(iBlockID, iconName);
      this.initBlockBounds(0.3, 0.0, 0.3, 0.7, 0.4, 0.7);
      this.a(null);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (world.provider.dimensionId != 1) {
         if (world.getBlockId(i, j - 1, k) == Block.mycelium.blockID && rand.nextInt(50) == 0) {
            this.c(world, i, j, k, rand);
         } else {
            super.updateTick(world, i, j, k, rand);
         }
      }
   }

   @Override
   public boolean canPlaceBlockAt(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return (iBlockID == 0 || Block.blocksList[iBlockID].blockMaterial.isReplaceable()) && this.canBlockStay(world, i, j, k);
   }

   @Override
   public boolean canBlockStay(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      return iBlockBelowID == Block.mycelium.blockID || world.getFullBlockLightValue(i, j, k) < 13 && this.canGrowOnBlock(world, i, j - 1, k);
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      return BTWItems.redMushroom.itemID;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      return world.doesBlockHaveSolidTopSurface(i, j, k);
   }

   @Override
   public boolean canBeCrushedByFallingEntity(World world, int i, int j, int k, EntityFallingSand entity) {
      return true;
   }
}
