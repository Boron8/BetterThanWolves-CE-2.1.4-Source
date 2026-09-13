package btw.block.blocks;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class PlanterBlockSoil extends PlanterBlockBase {
   @Environment(EnvType.CLIENT)
   private Icon iconTopSoilDry;
   @Environment(EnvType.CLIENT)
   private Icon iconTopSoilDryFertilized;

   public PlanterBlockSoil(int iBlockID) {
      super(iBlockID);
      this.c("fcBlockPlanterSoil");
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      boolean bHasIrrigation = this.hasIrrigatingBlocks(world, i, j, k) || world.isRainingAtPos(i, j + 1, k);
      if (bHasIrrigation != this.getIsHydrated(world, i, j, k)) {
         this.setIsHydrated(world, i, j, k, bHasIrrigation);
      }
   }

   @Override
   public boolean attemptToApplyFertilizerTo(World world, int i, int j, int k) {
      if (!this.getIsFertilized(world, i, j, k)) {
         this.setIsFertilized(world, i, j, k, true);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean canDomesticatedCropsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canReedsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canSaplingsGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canWildVegetationGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean canCactusGrowOnBlock(World world, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean isBlockHydratedForPlantGrowthOn(World world, int i, int j, int k) {
      return this.getIsHydrated(world, i, j, k);
   }

   @Override
   public boolean isConsideredNeighbouringWaterForReedGrowthOn(World world, int i, int j, int k) {
      return this.getIsHydrated(world, i, j, k);
   }

   @Override
   public boolean getIsFertilizedForPlantGrowth(World world, int i, int j, int k) {
      return this.getIsFertilized(world, i, j, k);
   }

   @Override
   public void notifyOfFullStagePlantGrowthOn(World world, int i, int j, int k, Block plantBlock) {
      if (this.getIsFertilized(world, i, j, k)) {
         this.setIsFertilized(world, i, j, k, false);
      }
   }

   protected boolean hasIrrigatingBlocks(World world, int i, int j, int k) {
      return world.getBlockMaterial(i, j - 1, k) == Material.water
         || world.getBlockMaterial(i, j + 1, k) == Material.water
         || world.getBlockMaterial(i, j, k - 1) == Material.water
         || world.getBlockMaterial(i, j, k + 1) == Material.water
         || world.getBlockMaterial(i - 1, j, k) == Material.water
         || world.getBlockMaterial(i + 1, j, k) == Material.water;
   }

   protected boolean getIsHydrated(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsHydrated(blockAccess.getBlockMetadata(i, j, k));
   }

   protected boolean getIsHydrated(int iMetadata) {
      return (iMetadata & 1) != 0;
   }

   protected void setIsHydrated(World world, int i, int j, int k, boolean bHydrated) {
      int iMetadata = this.setIsHydrated(world.getBlockMetadata(i, j, k), bHydrated);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected int setIsHydrated(int iMetadata, boolean bHydrated) {
      if (bHydrated) {
         iMetadata |= 1;
      } else {
         iMetadata &= -2;
      }

      return iMetadata;
   }

   protected boolean getIsFertilized(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsFertilized(blockAccess.getBlockMetadata(i, j, k));
   }

   protected boolean getIsFertilized(int iMetadata) {
      return (iMetadata & 2) != 0;
   }

   protected void setIsFertilized(World world, int i, int j, int k, boolean bFertilized) {
      int iMetadata = this.setIsFertilized(world.getBlockMetadata(i, j, k), bFertilized);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   protected int setIsFertilized(int iMetadata, boolean bFertilized) {
      if (bFertilized) {
         iMetadata |= 2;
      } else {
         iMetadata &= -3;
      }

      return iMetadata;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.registerIcons(register);
      this.iconTopSoilDry = register.registerIcon("fcBlockPlanter_top_dry");
      this.iconTopSoilDryFertilized = register.registerIcon("fcBlockPlanter_top_dry_fertilized");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (iSide == 1) {
         if (this.getIsFertilized(iMetadata)) {
            return this.getIsHydrated(iMetadata) ? this.iconTopSoilWetFertilized : this.iconTopSoilDryFertilized;
         } else {
            return this.getIsHydrated(iMetadata) ? this.iconTopSoilWet : this.iconTopSoilDry;
         }
      } else {
         return this.blockIcon;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      return this.renderFilledPlanterBlock(renderer, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderer, int iItemDamage, float fBrightness) {
      this.renderFilledPlanterInvBlock(renderer, this, iItemDamage);
   }
}
