package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.client.render.util.RenderUtils;
import btw.entity.FallingBlockEntity;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.World;

public class SmolderingLogBlock extends FallingBlock {
   private static final int CHANCE_OF_DECAY = 5;
   private static final int CHANCE_OF_EXTINGUISH_IN_RAIN = 5;
   private static final float EXPLOSION_STRENGTH = 1.0F;
   @Environment(EnvType.CLIENT)
   private Icon iconEmbers;

   public SmolderingLogBlock(int iBlockID) {
      super(iBlockID, BTWBlocks.logMaterial);
      this.c(2.0F);
      this.setAxesEffectiveOn();
      this.setChiselsEffectiveOn();
      this.setBuoyant();
      this.b(true);
      this.a(g);
      this.c("fcBlockLogSmouldering");
   }

   @Override
   public float getBlockHardness(World world, int i, int j, int k) {
      float fHardness = super.l(world, i, j, k);
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (this.getIsStump(world, i, j, k)) {
         fHardness *= 3.0F;
      }

      return fHardness;
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return 0;
   }

   @Override
   public boolean getIsProblemToRemove(ItemStack toolStack, IBlockAccess blockAccess, int i, int j, int k) {
      return this.getIsStump(blockAccess, i, j, k);
   }

   @Override
   public boolean getCanBlockBeIncinerated(World world, int i, int j, int k) {
      return !this.getIsStump(world, i, j, k);
   }

   @Override
   public boolean canConvertBlock(ItemStack stack, World world, int i, int j, int k) {
      return this.getIsStump(world, i, j, k);
   }

   @Override
   public boolean convertBlock(ItemStack stack, World world, int i, int j, int k, int iFromSide) {
      if (this.getIsStump(world.getBlockMetadata(i, j, k))) {
         world.setBlockWithNotify(i, j, k, BTWBlocks.charredStump.blockID);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      if (!this.hasWaterToSidesOrTop(world, i, j, k)) {
         int iMetadata = world.getBlockMetadata(i, j, k);
         if (!this.getIsStump(iMetadata) && this.getBurnLevel(iMetadata) > 0) {
            super.updateTick(world, i, j, k, rand);
         }
      } else {
         this.convertToCinders(world, i, j, k);
         world.playAuxSFX(2227, i, j, k, 0);
      }
   }

   @Override
   public void randomUpdateTick(World world, int i, int j, int k, Random rand) {
      if (world.getGameRules().getGameRuleBooleanValue("doFireTick") && !this.checkForGoOutInRain(world, i, j, k)) {
         FireBlock.checkForSmoulderingSpreadFromLocation(world, i, j, k);
         int iBurnLevel = this.getBurnLevel(world, i, j, k);
         if (iBurnLevel == 0) {
            if (!FireBlock.hasFlammableNeighborsWithinSmoulderRange(world, i, j, k)) {
               int iMetadata = world.getBlockMetadata(i, j, k);
               iMetadata = this.setBurnLevel(iMetadata, 1);
               if (this.isSupportedBySolidBlocks(world, i, j, k)) {
                  iMetadata = this.setShouldSuppressSnapOnFall(iMetadata, true);
               }

               world.setBlockMetadataWithNotify(i, j, k, iMetadata);
               this.scheduleCheckForFall(world, i, j, k);
            }
         } else if (rand.nextInt(5) == 0) {
            if (iBurnLevel < 3) {
               this.setBurnLevel(world, i, j, k, iBurnLevel + 1);
            } else {
               this.convertToCinders(world, i, j, k);
            }
         }
      }
   }

   @Override
   public boolean getIsBlockWarm(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   public boolean getCanBlockLightItemOnFire(IBlockAccess blockAccess, int i, int j, int k) {
      return true;
   }

   @Override
   protected void a(EntityFallingSand entity) {
      if (!this.getShouldSuppressSnapOnFall(entity.metadata)) {
         entity.worldObj.playAuxSFX(2276, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ), 0);
         entity.metadata = this.setShouldSuppressSnapOnFall(entity.metadata, true);
      }
   }

   @Override
   public void onFallingUpdate(FallingBlockEntity entity) {
      if (entity.worldObj.isRemote) {
         this.emitSmokeParticles(entity.worldObj, entity.posX, entity.posY, entity.posZ, entity.worldObj.rand, this.getBurnLevel(entity.metadata));
      }
   }

   @Override
   public boolean onFinishedFalling(EntityFallingSand entity, float fFallDistance) {
      if (!entity.worldObj.isRemote) {
         int i = MathHelper.floor_double(entity.posX);
         int j = MathHelper.floor_double(entity.posY);
         int k = MathHelper.floor_double(entity.posZ);
         int iFallDistance = MathHelper.ceiling_float_int(fFallDistance - 5.0F);
         if (iFallDistance >= 0 && !Material.water.equals(entity.worldObj.getBlockMaterial(i, j, k)) && entity.rand.nextInt(5) < iFallDistance) {
            this.explode(entity.worldObj, i + 0.5, j + 0.5, k + 0.5);
            return false;
         }
      }

      return true;
   }

   @Override
   public int getHarvestToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 1000;
   }

   @Override
   public void onBlockDestroyedWithImproperTool(World world, EntityPlayer player, int i, int j, int k, int iMetadata) {
      this.explode(world, i + 0.5, j + 0.5, k + 0.5);
   }

   public int getBurnLevel(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return this.getBurnLevel(iMetadata);
   }

   public int getBurnLevel(int iMetadata) {
      return iMetadata & 3;
   }

   public void setBurnLevel(World world, int i, int j, int k, int iLevel) {
      int iMetadata = this.setBurnLevel(world.getBlockMetadata(i, j, k), iLevel);
      world.setBlockMetadataWithNotify(i, j, k, iMetadata);
   }

   public int setBurnLevel(int iMetadata, int iLevel) {
      iMetadata &= -4;
      return iMetadata | iLevel;
   }

   public boolean getShouldSuppressSnapOnFall(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return this.getShouldSuppressSnapOnFall(iMetadata);
   }

   public boolean getShouldSuppressSnapOnFall(int iMetadata) {
      return (iMetadata & 4) != 0;
   }

   public int setShouldSuppressSnapOnFall(int iMetadata, boolean bSnap) {
      if (bSnap) {
         iMetadata |= 4;
      } else {
         iMetadata &= -5;
      }

      return iMetadata;
   }

   public boolean getIsStump(IBlockAccess blockAccess, int i, int j, int k) {
      int iMetadata = blockAccess.getBlockMetadata(i, j, k);
      return this.getIsStump(iMetadata);
   }

   public boolean getIsStump(int iMetadata) {
      return (iMetadata & 8) != 0;
   }

   public int setIsStump(int iMetadata, boolean bStump) {
      if (bStump) {
         iMetadata |= 8;
      } else {
         iMetadata &= -9;
      }

      return iMetadata;
   }

   private boolean checkForGoOutInRain(World world, int i, int j, int k) {
      if (world.rand.nextInt(5) == 0 && world.isRainingAtPos(i, j + 1, k)) {
         world.playAuxSFX(2227, i, j, k, 0);
         this.convertToCinders(world, i, j, k);
         return true;
      } else {
         return false;
      }
   }

   private void convertToCinders(World world, int i, int j, int k) {
      if (this.getIsStump(world, i, j, k)) {
         int iNewMetadata = BTWBlocks.woodCinders.setIsStump(0, true);
         world.setBlockAndMetadataWithNotify(i, j, k, BTWBlocks.woodCinders.blockID, iNewMetadata);
      } else {
         world.setBlockWithNotify(i, j, k, BTWBlocks.woodCinders.blockID);
      }
   }

   private void emitSmokeParticles(World world, double dCenterX, double dCenterY, double dCenterZ, Random rand, int iBurnLevel) {
      for (int iTempCount = 0; iTempCount < 5; iTempCount++) {
         double xPos = dCenterX - 0.6 + rand.nextDouble() * 1.2;
         double yPos = dCenterY + 0.25 + rand.nextDouble() * 0.25;
         double zPos = dCenterZ - 0.6 + rand.nextDouble() * 1.2;
         if (iBurnLevel > 0) {
            world.spawnParticle("fcwhitesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
         } else {
            world.spawnParticle("largesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
         }
      }
   }

   @Override
   public void onBlockDestroyedByExplosion(World world, int i, int j, int k, Explosion explosion) {
      if (!world.isRemote) {
         explosion.addSecondaryExplosionNoFX(i + 0.5, j + 0.5, k + 0.5, 1.0F, true, false);
      }
   }

   private void explode(World world, double posX, double posY, double posZ) {
      world.newExplosionNoFX((Entity)null, posX, posY, posZ, 1.0F, true, false);
      this.notifyNearbyAnimalsFinishedFalling(world, MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
      world.playAuxSFX(2277, MathHelper.floor_double(posX), MathHelper.floor_double(posY), MathHelper.floor_double(posZ), 0);
   }

   protected boolean isSupportedBySolidBlocks(World world, int i, int j, int k) {
      Block blockBelow = Block.blocksList[world.getBlockId(i, j - 1, k)];
      return blockBelow != null && blockBelow.hasLargeCenterHardPointToFacing(world, i, j - 1, k, 1, false);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconEmbers = register.registerIcon("fcOverlayLogEmbers");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderer, int i, int j, int k) {
      renderer.setRenderBounds(this.getBlockBoundsFromPoolBasedOnState(renderer.blockAccess, i, j, k));
      return renderer.renderBlockLog(this, i, j, k);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockSecondPass(RenderBlocks renderBlocks, int i, int j, int k, boolean bFirstPassResult) {
      if (bFirstPassResult) {
         RenderUtils.renderBlockFullBrightWithTexture(renderBlocks, renderBlocks.blockAccess, i, j, k, this.iconEmbers);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      renderBlocks.renderBlockAsItemVanilla(this, iItemDamage, fBrightness);
      RenderUtils.renderInvBlockFullBrightWithTexture(renderBlocks, this, -0.5F, -0.5F, -0.5F, this.iconEmbers);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderAllFaces(true);
      renderBlocks.setRenderBounds(this.getFixedBlockBoundsFromPool());
      renderBlocks.renderStandardBlock(this, i, j, k);
      RenderUtils.renderBlockFullBrightWithTexture(renderBlocks, renderBlocks.blockAccess, i, j, k, this.iconEmbers);
      renderBlocks.setRenderAllFaces(false);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random rand) {
      this.emitSmokeParticles(world, i + 0.5, j + 0.5, k + 0.5, rand, this.getBurnLevel(world, i, j, k));
      if (rand.nextInt(24) == 0) {
         float fVolume = 0.1F + rand.nextFloat() * 0.1F;
         world.playSound(i + 0.5, j + 0.5, k + 0.5, "fire.fire", fVolume, rand.nextFloat() * 0.7F + 0.3F, false);
      }
   }
}
