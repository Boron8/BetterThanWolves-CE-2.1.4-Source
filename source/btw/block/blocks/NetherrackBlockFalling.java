package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.entity.FallingBlockEntity;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

public class NetherrackBlockFalling extends FallingBlock {
   @Environment(EnvType.CLIENT)
   private Icon iconEmbers;

   public NetherrackBlockFalling(int iBlockID) {
      super(iBlockID, BTWBlocks.netherRockMaterial);
      this.c(0.6F);
      this.b(0.6666667F);
      this.setPicksEffectiveOn();
      this.a(j);
      this.c("hellrock");
   }

   @Override
   public int idDropped(int iMetadata, Random rand, int iFortuneModifier) {
      return Block.netherrack.blockID;
   }

   @Override
   public float getMovementModifier(World world, int i, int j, int k) {
      return 1.0F;
   }

   @Override
   public int getEfficientToolLevel(IBlockAccess blockAccess, int i, int j, int k) {
      return 2;
   }

   @Override
   protected boolean canSilkHarvest() {
      return false;
   }

   @Override
   protected void a(EntityFallingSand entity) {
      super.a(entity);
      int i = MathHelper.floor_double(entity.posX);
      int j = (int)entity.posY;
      int k = MathHelper.floor_double(entity.posZ);
      entity.worldObj.playAuxSFX(2228, i, j, k, 0);
      if (entity.worldObj.getBlockId(i, j + 1, k) == Block.fire.blockID) {
         entity.metadata = 1;
         entity.worldObj.playAuxSFX(2281, MathHelper.floor_double(entity.posX), MathHelper.floor_double(entity.posY), MathHelper.floor_double(entity.posZ), 0);
      }
   }

   @Override
   public void a_(World world, int i, int j, int k, int iMetadata) {
      super.a_(world, i, j, k, iMetadata);
      if (iMetadata != 0 && world.isAirBlock(i, j + 1, k)) {
         world.setBlockWithNotify(i, j + 1, k, Block.fire.blockID);
         world.SetBlockMetadataWithNotify(i, j, k, 0, 2);
      }
   }

   @Override
   public void onFallingUpdate(FallingBlockEntity entity) {
      if (entity.worldObj.isRemote && entity.metadata != 0) {
         this.emitSmokeParticles(entity.worldObj, entity.posX, entity.posY, entity.posZ, entity.worldObj.rand);
      }
   }

   @Override
   public void onBlockDestroyedLandingFromFall(World world, int i, int j, int k, int iMetadata) {
      this.c(world, i, j, k, iMetadata, 0);
   }

   @Override
   public boolean doesInfiniteBurnToFacing(IBlockAccess blockAccess, int i, int j, int k, int iFacing) {
      return iFacing == 1;
   }

   private void emitSmokeParticles(World world, double dCenterX, double dCenterY, double dCenterZ, Random rand) {
      for (int iTempCount = 0; iTempCount < 5; iTempCount++) {
         double xPos = dCenterX - 0.6 + rand.nextDouble() * 1.2;
         double yPos = dCenterY + 0.25 + rand.nextDouble() * 0.25;
         double zPos = dCenterZ - 0.6 + rand.nextDouble() * 1.2;
         world.spawnParticle("largesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void registerIcons(IconRegister register) {
      super.a(register);
      this.iconEmbers = register.registerIcon("fcOverlayNetherrackEmbers");
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderFallingBlock(RenderBlocks renderBlocks, int i, int j, int k, int iMetadata) {
      renderBlocks.setRenderAllFaces(true);
      renderBlocks.setRenderBounds(this.getFixedBlockBoundsFromPool());
      renderBlocks.renderStandardBlock(this, i, j, k);
      if (iMetadata != 0) {
         Tessellator tessellator = Tessellator.instance;
         tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
         tessellator.setBrightness(renderBlocks.blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
         renderBlocks.renderFaceYPos(null, i, j, k, this.iconEmbers);
      }

      renderBlocks.setRenderAllFaces(false);
   }
}
