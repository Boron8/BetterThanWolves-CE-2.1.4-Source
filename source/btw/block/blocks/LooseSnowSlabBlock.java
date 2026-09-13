package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.util.MiscUtils;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class LooseSnowSlabBlock extends FallingSlabBlock {
   @Environment(EnvType.CLIENT)
   private Icon[] iconsHardening;

   public LooseSnowSlabBlock(int iBlockID) {
      super(iBlockID, Material.craftedSnow);
      this.c(0.2F);
      this.setShovelsEffectiveOn();
      this.setBuoyant();
      this.a(o);
      this.c("fcBlockSnowLooseSlab");
      this.b(true);
      this.k(2);
      Block.useNeighborBrightness[iBlockID] = true;
      this.a(CreativeTabs.tabBlock);
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return Item.snowball.itemID;
   }

   @Override
   public int quantityDropped(Random rand) {
      return 4;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      if (world.provider.isHellWorld) {
         world.setBlockToAir(i, j, k);
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "random.fizz", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);

         for (int iTempCount = 0; iTempCount < 8; iTempCount++) {
            world.spawnParticle("largesmoke", i + Math.random(), j + Math.random(), k + Math.random(), 0.0, 0.0, 0.0);
         }
      } else {
         this.scheduleCheckForFall(world, i, j, k);
      }
   }

   @Override
   public int getCombinedBlockID(int iMetadata) {
      return BTWBlocks.looseSnow.blockID;
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if ((!this.hasStickySnowNeighborInContact(world, i, j, k) || this.getIsUpsideDown(world, i, j, k) && this.hasFallingBlockRestingOn(world, i, j, k))
         && !this.checkForFall(world, i, j, k)
         && this.getIsUpsideDown(world, i, j, k)) {
         this.setIsUpsideDown(world, i, j, k, false);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (MiscUtils.isIKInColdBiome(world, i, k)) {
         if (rand.nextInt(1) == 0) {
            int iHardeningLevel = this.getHardeningLevel(world, i, j, k);
            if (iHardeningLevel < 7) {
               this.setHardeningLevel(world, i, j, k, iHardeningLevel + 1);
            } else {
               this.convertToSolidSnow(world, i, j, k);
            }
         }
      } else if (rand.nextInt(1) == 0) {
         int iHardeningLevel = this.getHardeningLevel(world, i, j, k);
         if (iHardeningLevel > 0) {
            this.setHardeningLevel(world, i, j, k, iHardeningLevel - 1);
         } else {
            this.melt(world, i, j, k);
         }
      }
   }

   @Override
   public boolean canBePlacedUpsideDownAtLocation(World world, int i, int j, int k) {
      return this.hasStickySnowNeighborInContact(world, i, j, k, true);
   }

   @Override
   public void onPlayerWalksOnBlock(World world, int i, int j, int k, EntityPlayer player) {
      if (!this.checkForFall(world, i, j, k) && this.getIsUpsideDown(world, i, j, k)) {
         this.setIsUpsideDown(world, i, j, k, false);
      }
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 0.8F;
   }

   @Override
   public boolean getCanBeSetOnFireDirectly(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBeSetOnFireDirectlyByItem(IBlockAccess blockAccess, int i, int j, int k) {
      return false;
   }

   @Override
   public boolean setOnFireDirectly(World world, int i, int j, int k) {
      this.melt(world, i, j, k);
      return true;
   }

   @Override
   public int getChanceOfFireSpreadingDirectlyTo(IBlockAccess blockAccess, int i, int j, int k) {
      return 60;
   }

   @Override
   protected boolean canSilkHarvest() {
      return true;
   }

   @Override
   protected ItemStack createStackedBlock(int iMetadata) {
      return new ItemStack(this.blockID, 1, 0);
   }

   @Override
   public boolean canBePistonShoveled(World world, int i, int j, int k) {
      return true;
   }

   public int getHardeningLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return this.getHardeningLevel(blockAccess.getBlockMetadata(i, j, k));
   }

   public int getHardeningLevel(int iMetadata) {
      return (iMetadata & 14) >> 1;
   }

   public void setHardeningLevel(World world, int i, int j, int k, int iLevel) {
      int iMetadata = this.setHardeningLevel(world.getBlockMetadata(i, j, k), iLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setHardeningLevel(int iMetadata, int iLevel) {
      iMetadata &= -15;
      return iMetadata | iLevel << 1;
   }

   private void convertToSolidSnow(World world, int i, int j, int k) {
      int iNewMetadata = BTWBlocks.solidSnowSlab.setIsUpsideDown(0, this.getIsUpsideDown(world, i, j, k));
      world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.solidSnowSlab.blockID, iNewMetadata);
   }

   private void melt(World world, int i, int j, int k) {
      MiscUtils.placeNonPersistentWaterMinorSpread(world, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconsHardening = new Icon[8];

      for (int iTempIndex = 0; iTempIndex < 8; iTempIndex++) {
         this.iconsHardening[iTempIndex] = register.registerIcon("fcOverlaySnowLoose_" + iTempIndex);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         int iHardeningLevel = this.getHardeningLevel(renderBlocks.blockAccess, i, j, k);
         if (iHardeningLevel >= 0 && iHardeningLevel <= 7) {
            this.renderBlockWithTexture(renderBlocks, i, j, k, this.iconsHardening[iHardeningLevel]);
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.renderBlockAsItemVanilla(this, iItemDamage, fBrightness);
      RenderUtils.renderInvBlockWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, this.iconsHardening[0]);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderAllFaces(true);
      renderBlocks.setRenderBounds(this.getBlockBoundsFromPoolFromMetadata(iMetadata));
      renderBlocks.renderStandardBlock(this, i, j, k);
      RenderUtils.renderStandardBlockWithTexture(renderBlocks, this, i, j, k, this.iconsHardening[this.getHardeningLevel(iMetadata)]);
      renderBlocks.setRenderAllFaces(false);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      this.emitHardeningParticles(world, i + 0.5, j + 0.5, k + 0.5, rand);
   }

   @Environment(EnvType.CLIENT)
   private void emitHardeningParticles(World world, double dCenterX, double dCenterY, double dCenterZ, Random rand) {
      for (int iTempCount = 0; iTempCount < 1; iTempCount++) {
         double xPos = dCenterX - 0.6 + rand.nextDouble() * 1.2;
         double yPos = dCenterY - 0.6 + rand.nextDouble() * 1.2;
         double zPos = dCenterZ - 0.6 + rand.nextDouble() * 1.2;
         world.spawnParticle("fcwhitecloud", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      }
   }
}
