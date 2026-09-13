package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class BlockCrops extends BlockFlower {
   @Environment(EnvType.CLIENT)
   private Icon[] iconArray;

   protected BlockCrops(int par1) {
      super(par1);
      this.b(true);
      float var2 = 0.5F;
      this.initBlockBounds(0.5F - var2, 0.0, 0.5F - var2, 0.5F + var2, 0.25, 0.5F + var2);
      this.a((CreativeTabs)null);
      this.c(0.0F);
      this.a(i);
      this.D();
   }

   public void fertilize(World par1World, int par2, int par3, int par4) {
      int var5 = par1World.getBlockMetadata(par2, par3, par4) + MathHelper.getRandomIntegerInRange(par1World.rand, 2, 5);
      if (var5 > 7) {
         var5 = 7;
      }

      par1World.setBlockMetadataWithNotify(par2, par3, par4, var5, 2);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int par1, int par2) {
      if (par2 < 0 || par2 > 7) {
         par2 = 7;
      }

      return this.iconArray[par2];
   }

   @Override
   public int getRenderType() {
      return 6;
   }

   protected int getSeedItem() {
      return Item.seeds.itemID;
   }

   protected int getCropItem() {
      return Item.wheat.itemID;
   }

   @Override
   public void dropBlockAsItemWithChance(World par1World, int par2, int par3, int par4, int par5, float par6, int par7) {
      super.a(par1World, par2, par3, par4, par5, par6, 0);
      if (!par1World.isRemote && par5 >= 7) {
         this.dropSeeds(par1World, par2, par3, par4, par5, par6, par7);
      }
   }

   @Override
   public int quantityDropped(Random par1Random) {
      return 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int idPicked(World par1World, int par2, int par3, int par4) {
      return this.getSeedItem();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister par1IconRegister) {
      this.iconArray = new Icon[8];

      for (int var2 = 0; var2 < this.iconArray.length; var2++) {
         this.iconArray[var2] = par1IconRegister.registerIcon("crops_" + var2);
      }
   }

   @Override
   public int idDropped(int iMetadata, Random random, int iFortuneModifier) {
      return iMetadata == 7 ? this.getCropItem() : 0;
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
      super.updateTick(world, i, j, k, rand);
      if (world.provider.dimensionId != 1 && world.getBlockId(i, j, k) == this.blockID) {
         this.attemptToGrow(world, i, j, k, rand);
      }
   }

   @Override
   protected boolean canGrowOnBlock(World world, int i, int j, int k) {
      Block blockOn = Block.blocksList[world.getBlockId(i, j, k)];
      return blockOn != null && blockOn.canDomesticatedCropsGrowOnBlock(world, i, j, k);
   }

   @Override
   public boolean canWeedsGrowInBlock(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   protected void attemptToGrow(World world, int i, int j, int k, Random rand) {
      if (this.getWeedsGrowthLevel(world, i, j, k) == 0 && this.getGrowthLevel(world, i, j, k) < 7 && world.getBlockLightValue(i, j + 1, k) >= 9) {
         Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
         if (blockBelow != null && blockBelow.isBlockHydratedForPlantGrowthOn(world, i, j - 1, k)) {
            float fGrowthChance = this.getBaseGrowthChance(world, i, j, k) * blockBelow.getPlantGrowthOnMultiplier(world, i, j - 1, k, this);
            if (rand.nextFloat() <= fGrowthChance) {
               this.incrementGrowthLevel(world, i, j, k);
            }
         }
      }
   }

   public void dropSeeds(World world, int i, int j, int k, int iMetadata, float fChance, int iFortuneModifier) {
      this.b(world, i, j, k, new ItemStack(this.getSeedItem(), 1, 0));
      if (world.rand.nextInt(16) - iFortuneModifier < 4) {
         this.b(world, i, j, k, new ItemStack(this.getSeedItem(), 1, 0));
      }
   }

   public float getBaseGrowthChance(World world, int i, int j, int k) {
      return 0.05F;
   }

   protected void incrementGrowthLevel(World world, int i, int j, int k) {
      int iGrowthLevel = this.getGrowthLevel(world, i, j, k) + 1;
      this.setGrowthLevel(world, i, j, k, iGrowthLevel);
      if (iGrowthLevel == 7) {
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

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      renderer.renderBlockCrops(this, i, j, k);
      BTWBlocks.weeds.renderWeeds(this, renderer, i, j, k);
      return true;
   }
}
