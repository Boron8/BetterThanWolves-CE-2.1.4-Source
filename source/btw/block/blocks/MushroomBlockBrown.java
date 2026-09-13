package btw.block.blocks;

import btw.item.BTWItems;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.World;

public class MushroomBlockBrown extends MushroomBlock {
   public MushroomBlockBrown(int iBlockID, String iconName) {
      super(iBlockID, iconName);
   }

   @Override
   public int idDropped(int iMetaData, Random rand, int iFortuneModifier) {
      return BTWItems.brownMushroom.itemID;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (world.provider.dimensionId == 0) {
         if (world.getBlockId(i, j - 1, k) == Block.mycelium.blockID && rand.nextInt(50) == 0) {
            this.c(world, i, j, k, rand);
         } else {
            this.checkForSpread(world, i, j, k, rand);
         }
      }
   }

   protected boolean canSpreadToOrFromLocation(World world, int i, int j, int k) {
      int iBlockBelowID = world.getBlockId(i, j - 1, k);
      return iBlockBelowID == Block.mycelium.blockID || world.getFullBlockLightValue(i, j, k) == 0;
   }

   protected void checkForSpread(World world, int i, int j, int k, Random rand) {
      if (rand.nextInt(25) == 0 && this.canSpreadToOrFromLocation(world, i, j, k)) {
         int iHorizontalSpreadRange = 4;
         int iNeighboringMushroomsCountdown = 5;

         for (int iTempI = i - iHorizontalSpreadRange; iTempI <= i + iHorizontalSpreadRange; iTempI++) {
            for (int iTempK = k - iHorizontalSpreadRange; iTempK <= k + iHorizontalSpreadRange; iTempK++) {
               for (int iTempJ = j - 1; iTempJ <= j + 1; iTempJ++) {
                  if (world.getBlockId(iTempI, iTempJ, iTempK) == this.blockID) {
                     if (--iNeighboringMushroomsCountdown <= 0) {
                        return;
                     }
                  }
               }
            }
         }

         int iSpreadI = i + rand.nextInt(3) - 1;
         int iSpreadK = j + rand.nextInt(2) - rand.nextInt(2);
         int iSpreadJ = k + rand.nextInt(3) - 1;

         for (int iTempCount = 0; iTempCount < 4; iTempCount++) {
            if (world.isAirBlock(iSpreadI, iSpreadK, iSpreadJ)
               && this.f(world, iSpreadI, iSpreadK, iSpreadJ)
               && this.canSpreadToOrFromLocation(world, iSpreadI, iSpreadK, iSpreadJ)) {
               i = iSpreadI;
               j = iSpreadK;
               k = iSpreadJ;
            }

            iSpreadI = i + rand.nextInt(3) - 1;
            iSpreadK = j + rand.nextInt(2) - rand.nextInt(2);
            iSpreadJ = k + rand.nextInt(3) - 1;
         }

         if (world.isAirBlock(iSpreadI, iSpreadK, iSpreadJ)
            && this.f(world, iSpreadI, iSpreadK, iSpreadJ)
            && this.canSpreadToOrFromLocation(world, iSpreadI, iSpreadK, iSpreadJ)) {
            world.setBlock(iSpreadI, iSpreadK, iSpreadJ, this.blockID);
         }
      }
   }

   @Override
   public boolean canBlockStayDuringGenerate(World world, int i, int j, int k) {
      if (j > 24 || world.provider.dimensionId != 0) {
         int iBlockBelowID = world.getBlockId(i, j - 1, k);
         if (iBlockBelowID != Block.mycelium.blockID) {
            return false;
         }
      }

      return super.canBlockStayDuringGenerate(world, i, j, k);
   }
}
