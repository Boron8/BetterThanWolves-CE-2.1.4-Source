package btw.block.blocks;

import btw.block.BTWBlocks;
import java.util.Random;
import net.minecraft.src.BiomeGenBase;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.World;

public class FarmlandBlock extends FarmlandBlockBase {
   public static final int LIGHT_LEVEL_FOR_WEED_GROWTH = 11;

   public FarmlandBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockFarmlandNew");
      this.a(CreativeTabs.tabDecorations);
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      super.a(world, i, j, k, iNeighborBlockID);
      if (world.getBlockMaterial(i, j + 1, k).isSolid() || this.canFallIntoBlockAtPos(world, i, j - 1, k)) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
      } else if (this.getWeedsGrowthLevel(world, i, j, k) > 0 && !this.canWeedsShareSpaceWithBlockAt(world, i, j + 1, k)) {
         this.setWeedsGrowthLevel(world, i, j, k, 0);
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      super.updateTick(world, i, j, k, rand);
      if (world.getBlockId(i, j, k) == this.blockID && !this.checkForSnowReversion(world, i, j, k, rand)) {
         this.updateWeedGrowth(world, i, j, k, rand);
      }
   }

   @Override
   public void notifyOfPlantAboveRemoved(World world, int i, int j, int k, Block plantBlock) {
      if (world.getBlockId(i, j + 1, k) != Block.tallGrass.blockID) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
      }
   }

   @Override
   protected boolean isHydrated(int iMetadata) {
      return (iMetadata & 1) > 0;
   }

   @Override
   public int setFullyHydrated(int iMetadata) {
      return iMetadata | 1;
   }

   @Override
   protected void dryIncrementally(World world, int i, int j, int k) {
      int iMetadata = world.getBlockMetadata(i, j, k);
      int iHydrationLevel = iMetadata & 1;
      if (iHydrationLevel > 0) {
         iMetadata &= -2;
         world.setBlockMetadataWithNotify(i, j, k, iMetadata);
      }
   }

   @Override
   protected boolean isFertilized(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   protected void setFertilized(World world, int i, int j, int k) {
      int iTargetBlockMetadata = world.getBlockMetadata(i, j, k);
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.fertilizedFarmland.blockID, iTargetBlockMetadata);
   }

   @Override
   public int getWeedsGrowthLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getWeedsGrowthLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   @Override
   public void removeWeeds(World world, int i, int j, int k) {
      this.setWeedsGrowthLevel(world, i, j, k, 0);
   }

   @Override
   protected void checkForSoilReversion(World world, int i, int j, int k) {
      if (world.rand.nextInt(8) == 0) {
         super.checkForSoilReversion(world, i, j, k);
      }
   }

   @Override
   protected int getHorizontalHydrationRange(World world, int i, int j, int k) {
      BiomeGenBase biome = world.getBiomeGenForCoords(i, k);
      return !biome.getEnableSnow() && !biome.canRainInBiome() ? 2 : 4;
   }

   public boolean canWeedsShareSpaceWithBlockAt(World world, int i, int j, int k) {
      Block block = Block.blocksList[world.getBlockId(i, j, k)];
      return block != null ? block.canWeedsGrowInBlock(world, i, j, k) : false;
   }

   protected int getWeedsGrowthLevel(int iMetadata) {
      return (iMetadata & 14) >> 1;
   }

   protected void setWeedsGrowthLevel(World world, int i, int j, int k, int iGrowthLevel) {
      int iMetadata = this.setWeedsGrowthLevel(world.getBlockMetadata(i, j, k), iGrowthLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected int setWeedsGrowthLevel(int iMetadata, int iGrowthLevel) {
      iMetadata &= -15;
      return iMetadata | iGrowthLevel << 1;
   }

   public boolean checkForSnowReversion(World world, int i, int j, int k, Random rand) {
      if (world.isSnowingAtPos(i, j + 1, k) && rand.nextInt(2) == 0) {
         if (world.getSavedLightValue(EnumSkyBlock.Block, i, j + 1, k) < 10) {
            world.setBlockWithNotify(i, j, k, BTWBlocks.looseDirt.blockID);
            if (Block.snow.canPlaceBlockAt(world, i, j + 1, k)) {
               world.setBlockWithNotify(i, j + 1, k, Block.snow.blockID);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void updateWeedGrowth(World world, int i, int j, int k, Random rand) {
      if (world.getBlockId(i, j, k) == this.blockID) {
         int iWeedsLevel = this.getWeedsGrowthLevel(world, i, j, k);
         int iTimeOfDay = (int)(world.worldInfo.getWorldTime() % 24000L);
         if (world.isAirBlock(i, j + 1, k)) {
            if (iTimeOfDay > 14000 && iTimeOfDay < 22000 && rand.nextInt(20) == 0 && world.getBlockNaturalLightValueMaximum(i, j + 1, k) >= 11) {
               world.setBlockWithNotify(i, j + 1, k, BTWBlocks.weeds.blockID);
               this.setWeedsGrowthLevel(world, i, j, k, 1);
            }
         } else if (this.canWeedsShareSpaceWithBlockAt(world, i, j + 1, k)) {
            if (iTimeOfDay <= 14000 || iTimeOfDay >= 22000) {
               if (world.getBlockNaturalLightValue(i, j + 1, k) >= 11) {
                  if (iWeedsLevel == 7 && world.getDifficulty().canWeedsKillPlants()) {
                     this.setWeedsGrowthLevel(world, i, j, k, 0);
                     world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.tallGrass.blockID, 1);
                  } else if (iWeedsLevel % 2 == 1) {
                     this.setWeedsGrowthLevel(world, i, j, k, iWeedsLevel + 1);
                  }
               }
            } else if (iWeedsLevel == 0) {
               if (rand.nextInt(20) == 0) {
                  this.setWeedsGrowthLevel(world, i, j, k, 1);
               }
            } else if (iWeedsLevel % 2 == 0) {
               this.setWeedsGrowthLevel(world, i, j, k, iWeedsLevel + 1);
            }
         } else if (iWeedsLevel > 0) {
            this.setWeedsGrowthLevel(world, i, j, k, 0);
         }
      }
   }
}
