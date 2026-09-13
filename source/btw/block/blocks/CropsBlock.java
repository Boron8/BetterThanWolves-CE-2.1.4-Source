package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.util.Flammability;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public abstract class CropsBlock extends PlantsBlock {
   protected static final double BOUNDS_WIDTH = 0.75;
   protected static final double BOUNDS_HALF_WIDTH = 0.375;

   protected CropsBlock(int iBlockID) {
      super(iBlockID, Material.plants);
      this.c(0.0F);
      this.setBuoyant();
      this.setFireProperties(Flammability.CROPS);
      this.initBlockBounds(0.125, 0.0, 0.125, 0.875, 1.0, 0.875);
      this.a(Block.soundGrassFootstep);
      this.b(true);
      this.D();
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iNeighborBlockID) {
      super.a(world, i, j, k, iNeighborBlockID);
      this.updateIfBlockStays(world, i, j, k);
   }

   @Override
   public void dropBlockAsItemWithChance(World world, int x, int y, int z, int metadata, float chance, int fortuneModifier) {
      if (!world.isRemote) {
         if (!this.onlyDropWhenFullyGrown() || this.isFullyGrown(metadata)) {
            super.a(world, x, y, z, metadata, chance, 0);
            this.dropSeeds(world, x, y, z, metadata);
         }
      }
   }

   protected boolean onlyDropWhenFullyGrown() {
      return true;
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return this.isFullyGrown(iMetadata) ? this.getCropItemID() : 0;
   }

   @Override
   public AxisAlignedBB getBlockBoundsFromPoolBasedOnState(IBlockAccess blockAccess, int i, int j, int k) {
      float dVerticalOffset = 0.0F;
      Block blockBelow = Block.blocksList[blockAccess.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         dVerticalOffset = blockBelow.groundCoverRestingOnVisualOffset(blockAccess, i, j - 1, k);
      }

      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      if (this.isFullyGrown(iMetadata)) {
         return AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0 + dVerticalOffset, 0.125, 0.875, 1.0 + dVerticalOffset, 0.875);
      } else {
         int iGrowthLevel = this.getGrowthLevel(iMetadata);
         double dBoundsHeight = (1 + iGrowthLevel) / 8.0;
         int iWeedsGrowthLevel = this.getWeedsGrowthLevel(blockAccess, i, j, k);
         if (iWeedsGrowthLevel > 0) {
            dBoundsHeight = Math.max(dBoundsHeight, WeedsBlock.getWeedsBoundsHeight(iWeedsGrowthLevel));
         }

         return AxisAlignedBB.getAABBPool().getAABB(0.125, 0.0F + dVerticalOffset, 0.125, 0.875, dBoundsHeight + dVerticalOffset, 0.875);
      }
   }

   @Override
   public boolean canBeGrazedOn(IBlockAccess blockAccess, int i, int j, int k, EntityAnimal animal) {
      return true;
   }

   @Override
   public void onGrazed(World world, int i, int j, int k, EntityAnimal animal) {
      this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
      super.onGrazed(world, i, j, k, animal);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (this.updateIfBlockStays(world, i, j, k) && world.provider.dimensionId != 1 && !this.isFullyGrown(world, i, j, k)) {
         this.attemptToGrow(world, i, j, k, rand);
      }
   }

   @Override
   public boolean canWeedsGrowInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getConvertsLegacySoil(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      Block blockOn = Block.blocksList[world.getBlockId(i, j, k)];
      return blockOn != null && blockOn.canDomesticatedCropsGrowOnBlock(world, i, j, k);
   }

   protected abstract int getCropItemID();

   protected abstract int getSeedItemID();

   protected boolean updateIfBlockStays(World world, int i, int j, int k) {
      if (!this.f(world, i, j, k)) {
         this.c(world, i, j, k, world.getBlockMetadata(i, j, k), 0);
         world.setBlockToAir(i, j, k);
         return false;
      } else {
         return true;
      }
   }

   protected void attemptToGrow(World world, int x, int y, int z, Random rand) {
      if (this.getWeedsGrowthLevel(world, x, y, z) == 0 && this.canGrowAtCurrentLightLevel(world, x, y, z)) {
         Block blockBelow = Block.blocksList[world.getBlockId(x, y - 1, z)];
         if (blockBelow != null && blockBelow.isBlockHydratedForPlantGrowthOn(world, x, y - 1, z)) {
            float fGrowthChance = this.getBaseGrowthChance(world, x, y, z) * blockBelow.getPlantGrowthOnMultiplier(world, x, y - 1, z, this);
            if (rand.nextFloat() <= fGrowthChance) {
               this.incrementGrowthLevel(world, x, y, z);
            }
         }
      }
   }

   public void dropSeeds(World world, int i, int j, int k, int iMetadata) {
      int iSeedItemID = this.getSeedItemID();
      if (iSeedItemID > 0) {
         this.b(world, i, j, k, new ItemStack(iSeedItemID, 1, 0));
      }
   }

   public float getBaseGrowthChance(World world, int i, int j, int k) {
      return 0.05F;
   }

   protected void incrementGrowthLevel(World world, int i, int j, int k) {
      int iGrowthLevel = this.getGrowthLevel(world, i, j, k) + 1;
      this.setGrowthLevel(world, i, j, k, iGrowthLevel);
      if (this.isFullyGrown(world, i, j, k)) {
         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null) {
            blockBelow.notifyOfFullStagePlantGrowthOn(world, i, j - 1, k, this);
         }
      }
   }

   protected int getGrowthLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getGrowthLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   protected int getGrowthLevel(int iMetadata) {
      return iMetadata & 7;
   }

   protected void setGrowthLevel(World world, int i, int j, int k, int iLevel) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -8;
      world.setBlockMetadataWithNotify(i, j, k, iMetadata | iLevel);
   }

   protected void setGrowthLevelNoNotify(World world, int i, int j, int k, int iLevel) {
      int iMetadata = world.getBlockMetadata(i, j, k) & -8;
      world.setBlockMetadata(i, j, k, iMetadata | iLevel);
   }

   protected boolean isFullyGrown(World world, int i, int j, int k) {
      return this.isFullyGrown(world.getBlockMetadata(i, j, k));
   }

   protected boolean isFullyGrown(int iMetadata) {
      return this.getGrowthLevel(iMetadata) >= 7;
   }

   protected int getLightLevelForGrowth() {
      return 9;
   }

   protected boolean canGrowAtCurrentLightLevel(World world, int x, int y, int z) {
      return !this.requiresNaturalLight()
         ? world.getBlockLightValue(x, y, z) >= this.getLightLevelForGrowth()
         : world.getBlockNaturalLightValue(x, y, z) >= this.getLightLevelForGrowth()
            || world.getBlockId(x, y + 1, z) == BTWBlocks.lightBlockOn.blockID
            || world.getBlockId(x, y + 2, z) == BTWBlocks.lightBlockOn.blockID;
   }

   protected boolean requiresNaturalLight() {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      this.renderCrops(renderer, i, j, k);
      BTWBlocks.weeds.renderWeeds(this, renderer, i, j, k);
      return true;
   }

   @Environment(EnvType.CLIENT)
   protected void renderCrops(RenderBlocks renderer, int i, int j, int k) {
      Tessellator tessellator = Tessellator.instance;
      tessellator.setBrightness(this.e(renderer.blockAccess, i, j, k));
      tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      double dVerticalOffset = 0.0;
      Block blockBelow = Block.blocksList[renderer.blockAccess.getBlockId(i, j - 1, k)];
      if (blockBelow != null) {
         dVerticalOffset = blockBelow.groundCoverRestingOnVisualOffset(renderer.blockAccess, i, j - 1, k);
      }

      this.renderCrossHatch(renderer, i, j, k, this.b_(renderer.blockAccess, i, j, k, 0), 0.25, dVerticalOffset);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World world, int i, int j, int k) {
      return this.getSeedItemID();
   }
}
