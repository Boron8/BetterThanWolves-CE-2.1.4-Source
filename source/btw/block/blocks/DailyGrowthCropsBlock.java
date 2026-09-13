package btw.block.blocks;

import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public abstract class DailyGrowthCropsBlock extends CropsBlock {
   protected DailyGrowthCropsBlock(int iBlockID) {
      super(iBlockID);
   }

   @Override
   public float getBaseGrowthChance(World world, int i, int j, int k) {
      return 0.4F;
   }

   @Override
   protected void attemptToGrow(World world, int x, int y, int z, Random rand) {
      int timeOfDay = (int)(world.worldInfo.getWorldTime() % 24000L);
      if (timeOfDay > 14000 && timeOfDay < 22000) {
         if (this.getHasGrownToday(world, x, y, z)) {
            this.setHasGrownToday(world, x, y, z, false);
         }
      } else if (!this.getHasGrownToday(world, x, y, z) && this.getWeedsGrowthLevel(world, x, y, z) == 0 && this.canGrowAtCurrentLightLevel(world, x, y, z)) {
         Block blockBelow = Block.blocksList[world.getBlockId(x, y - 1, z)];
         if (blockBelow != null && blockBelow.isBlockHydratedForPlantGrowthOn(world, x, y - 1, z)) {
            float growthChance = this.getBaseGrowthChance(world, x, y, z);
            if (blockBelow.getIsFertilizedForPlantGrowth(world, x, y - 1, z)) {
               growthChance *= 2.0F;
            }

            if (rand.nextFloat() <= growthChance) {
               this.incrementGrowthLevel(world, x, y, z);
               this.updateFlagForGrownToday(world, x, y, z);
            }
         }
      }
   }

   protected void updateFlagForGrownToday(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      if (blockBelow != null && (!blockBelow.getIsFertilizedForPlantGrowth(world, i, j - 1, k) || this.getGrowthLevel(world, i, j, k) % 2 == 0)) {
         this.setHasGrownToday(world, i, j, k, true);
      }
   }

   protected boolean getHasGrownToday(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHasGrownToday(blockAccess.getBlockMetadata(i, j, k));
   }

   protected boolean getHasGrownToday(int iMetadata) {
      return (iMetadata & 8) != 0;
   }

   protected void setHasGrownToday(World world, int i, int j, int k, boolean bHasGrown) {
      int iMetadata = this.setHasGrownToday(world.getBlockMetadata(i, j, k), bHasGrown);
      world.setBlockMetadata(i, j, k, iMetadata);
   }

   protected int setHasGrownToday(int iMetadata, boolean bHasGrown) {
      if (bHasGrown) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }
}
