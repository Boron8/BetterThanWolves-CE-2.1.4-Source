package btw.block.blocks;

import btw.client.render.util.RenderUtils;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class PlanterBlock extends PlanterBlockBase {
   public static final int TYPE_EMPTY = 0;
   public static final int TYPE_SOIL = 1;
   public static final int TYPE_SOIL_FERTILIZED = 2;
   public static final int TYPE_SOUL_SAND = 8;
   public static final int TYPE_GRASS_0 = 9;
   public static final int TYPE_GRASS_1 = 11;
   public static final int TYPE_GRASS_2 = 13;
   public static final int TYPE_GRASS_3 = 15;
   @Environment(EnvType.CLIENT)
   private Icon iconTopGrass;
   @Environment(EnvType.CLIENT)
   private Icon iconTopSoulSand;

   public PlanterBlock(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockPlanter");
   }

   @Override
   public int damageDropped(int iMetadata) {
      if (iMetadata == 11 || iMetadata == 13 || iMetadata == 15) {
         iMetadata = 9;
      }

      return iMetadata;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random random) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      if (iPlanterType == 9 || iPlanterType == 11 || iPlanterType == 13 || iPlanterType == 15) {
         int iOldGrowthState = this.getGrassGrowthState(world, i, j, k);
         int iNewGrowthState = 0;
         if (world.isAirBlock(i, j + 1, k) && world.getBlockLightValue(i, j + 1, k) >= 8) {
            iNewGrowthState = iOldGrowthState + 1;
            if (iNewGrowthState > 3) {
               iNewGrowthState = 0;
               int iPlantType = random.nextInt(4);
               if (iPlantType == 0) {
                  world.setBlockWithNotify(i, j + 1, k, Block.plantRed.blockID);
               } else if (iPlantType == 1) {
                  world.setBlockWithNotify(i, j + 1, k, Block.plantYellow.blockID);
               } else {
                  world.setBlockAndMetadataWithNotify(i, j + 1, k, Block.tallGrass.blockID, 1);
               }
            }
         }

         if (world.getBlockLightValue(i, j + 1, k) >= 9) {
            for (int tempCount = 0; tempCount < 4; tempCount++) {
               int tempi = i + random.nextInt(3) - 1;
               int tempj = j + random.nextInt(5) - 3;
               int tempk = k + random.nextInt(3) - 1;
               int iTempBlockAboveID = world.getBlockId(tempi, tempj + 1, tempk);
               if (world.getBlockId(tempi, tempj, tempk) == Block.dirt.blockID
                  && world.getBlockLightValue(tempi, tempj + 1, tempk) >= 4
                  && Block.lightOpacity[iTempBlockAboveID] <= 2) {
                  world.setBlockWithNotify(tempi, tempj, tempk, Block.grass.blockID);
               }
            }
         }

         if (iNewGrowthState != iOldGrowthState) {
            this.setGrassGrowthState(world, i, j, k, iNewGrowthState);
         }
      }
   }

   @Override
   public boolean attemptToApplyFertilizerTo(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      if (iPlanterType == 1) {
         this.setPlanterType(world, i, j, k, 2);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean hasLargeCenterHardPointToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing, boolean bIgnoreTransparency) {
      return this.getPlanterType(blockAccess, i, j, k) == 0 ? false : super.hasLargeCenterHardPointToFacing(blockAccess, i, j, k, iFacing, bIgnoreTransparency);
   }

   @Override
   public boolean canDomesticatedCropsGrowOnBlock(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType == 1 || iPlanterType == 2;
   }

   @Override
   public boolean canReedsGrowOnBlock(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType != 0 && iPlanterType != 8;
   }

   @Override
   public boolean canSaplingsGrowOnBlock(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType != 0 && iPlanterType != 8;
   }

   @Override
   public boolean canWildVegetationGrowOnBlock(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType != 0 && iPlanterType != 8;
   }

   @Override
   public boolean canNetherWartGrowOnBlock(World world, int i, int j, int k) {
      return this.getPlanterType(world, i, j, k) == 8;
   }

   @Override
   public boolean canCactusGrowOnBlock(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType != 0 && iPlanterType != 8;
   }

   @Override
   public boolean isBlockHydratedForPlantGrowthOn(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType == 1 || iPlanterType == 2;
   }

   @Override
   public boolean isConsideredNeighbouringWaterForReedGrowthOn(World world, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      return iPlanterType == 1 || iPlanterType == 2 || super.isConsideredNeighbouringWaterForReedGrowthOn(world, i, j, k);
   }

   @Override
   public boolean getIsFertilizedForPlantGrowth(World world, int i, int j, int k) {
      return this.getPlanterType(world, i, j, k) == 2;
   }

   @Override
   public void notifyOfFullStagePlantGrowthOn(World world, int i, int j, int k, Block plantBlock) {
      int iPlanterType = this.getPlanterType(world, i, j, k);
      if (iPlanterType == 2) {
         this.setPlanterType(world, i, j, k, 1);
      }
   }

   public int getPlanterType(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getPlanterTypeFromMetadata(blockAccess.getBlockMetadata(i, j, k));
   }

   public void setPlanterType(World world, int i, int j, int k, int iType) {
      world.setBlockMetadataWithNotify(i, j, k, iType);
   }

   public int getPlanterTypeFromMetadata(int iMetadata) {
      return iMetadata;
   }

   public int getGrassGrowthState(IBlockAccess blockAccess, int i, int j, int k) {
      int iPlanterType = this.getPlanterType(blockAccess, i, j, k);
      switch (iPlanterType) {
         case 9:
            return 0;
         case 10:
         case 12:
         case 14:
         default:
            return 0;
         case 11:
            return 1;
         case 13:
            return 2;
         case 15:
            return 3;
      }
   }

   public void setGrassGrowthState(World world, int i, int j, int k, int iGrowthState) {
      int iPlanterType = 9;
      if (iGrowthState == 1) {
         iPlanterType = 11;
      } else if (iGrowthState == 2) {
         iPlanterType = 13;
      } else if (iGrowthState == 3) {
         iPlanterType = 15;
      }

      this.setPlanterType(world, i, j, k, iPlanterType);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconTopGrass = register.registerIcon("fcBlockPlanter_top_grass");
      this.iconTopSoulSand = register.registerIcon("fcBlockPlanter_top_soulsand");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      int iPlanterType = this.getPlanterTypeFromMetadata(iMetadata);
      if (iSide != 1 || iPlanterType == 0) {
         return this.blockIcon;
      } else if (iPlanterType == 1) {
         return this.iconTopSoilWet;
      } else if (iPlanterType == 8) {
         return this.iconTopSoulSand;
      } else {
         return iPlanterType == 2 ? this.iconTopSoilWetFertilized : this.iconTopGrass;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void getSubBlocks(int blockID, CreativeTabs creativeTabs, List list) {
      list.add(new ItemStack(blockID, 1, 0));
      list.add(new ItemStack(blockID, 1, 8));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iPlanterType = this.getPlanterType(blockAccess, i, j, k);
      return iPlanterType == 0 ? renderEmptyPlanterBlock(renderBlocks, blockAccess, i, j, k, this) : this.renderFilledPlanterBlock(renderBlocks, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   public static boolean renderEmptyPlanterBlock(RenderBlocks renderBlocks, IBlockAccess blockAccess, int i, int j, int k, Block block) {
      renderBlocks.setRenderBounds(0.125, 0.0, 0.125, 0.25, 0.6875, 0.75);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.125, 0.0, 0.75, 0.75, 0.6875, 0.875);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.75, 0.0, 0.25, 0.875, 0.6875, 0.875);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.25, 0.0, 0.125, 0.875, 0.6875, 0.25);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.125, 0.75);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.0, 0.6875, 0.0, 0.125, 1.0, 0.875);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.0, 0.6875, 0.875, 0.875, 1.0, 1.0);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.875, 0.6875, 0.125, 1.0, 1.0, 1.0);
      renderBlocks.renderStandardBlock(block, i, j, k);
      renderBlocks.setRenderBounds(0.125, 0.6875, 0.0, 1.0, 1.0, 0.125);
      renderBlocks.renderStandardBlock(block, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      if (iItemDamage == 0) {
         renderEmptyPlanterInvBlock(renderBlocks, this, iItemDamage);
      } else {
         this.renderFilledPlanterInvBlock(renderBlocks, this, iItemDamage);
      }
   }

   @Environment(EnvType.CLIENT)
   public static void renderEmptyPlanterInvBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      renderBlocks.setRenderBounds(0.125, 0.0, 0.125, 0.25, 0.6875, 0.75);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.125, 0.0, 0.75, 0.75, 0.6875, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.75, 0.0, 0.25, 0.875, 0.6875, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.25, 0.0, 0.125, 0.875, 0.6875, 0.25);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.25, 0.0, 0.25, 0.75, 0.125, 0.75);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.0, 0.6875, 0.0, 0.125, 1.0, 0.875);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.0, 0.6875, 0.875, 0.875, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.875, 0.6875, 0.125, 1.0, 1.0, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
      renderBlocks.setRenderBounds(0.125, 0.6875, 0.0, 1.0, 1.0, 0.125);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, block, -0.5F, -0.5F, -0.5F, iItemDamage);
   }
}
