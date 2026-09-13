package btw.block.blocks;

import btw.block.BTWBlocks;
import btw.block.MechanicalBlock;
import btw.block.util.MechPowerUtils;
import btw.client.render.util.RenderUtils;
import btw.world.util.BlockPos;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Icon;
import net.minecraft.src.IconRegister;
import net.minecraft.src.Material;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public abstract class VesselBlock extends BlockContainer implements MechanicalBlock {
   private static final int TICK_RATE = 1;
   public static final double COLLISION_BOX_HEIGHT = 1.0;
   public static final float MODEL_HEIGHT = 1.0F;
   public static final float MODEL_WIDTH = 0.875F;
   public static final float MODEL_HALF_WIDTH = 0.4375F;
   public static final float MODEL_BAND_HEIGHT = 0.75F;
   public static final float MODEL_BAND_HALF_HEIGHT = 0.375F;
   @Environment(EnvType.CLIENT)
   protected Icon[] iconWideBandBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   protected Icon[] iconCenterColumnBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   protected Icon[] iconInteriorBySideArray = new Icon[6];
   @Environment(EnvType.CLIENT)
   private boolean renderingInterior = false;
   @Environment(EnvType.CLIENT)
   private boolean renderingWideBand = false;

   public VesselBlock(int iBlockID, Material material) {
      super(iBlockID, material);
   }

   @Override
   public int tickRate(World world) {
      return 1;
   }

   @Override
   public void onBlockAdded(World world, int i, int j, int k) {
      super.onBlockAdded(world, i, j, k);
      world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
   }

   @Override
   public boolean isOpaqueCube() {
      return false;
   }

   @Override
   public boolean renderAsNormalBlock() {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
      return AxisAlignedBB.getAABBPool().getAABB(i, j, k, i + 1, j + 1.0, k + 1.0);
   }

   @Override
   public void updateTick(World world, int i, int j, int k, Random rand) {
      boolean bWasPowered = this.getMechanicallyPoweredFlag(world, i, j, k);
      boolean bIsPowered = false;
      int iPoweredFromFacing = 0;

      for (int iFacing = 2; iFacing <= 5; iFacing++) {
         if (MechPowerUtils.isBlockPoweredByAxleToSide(world, i, j, k, iFacing) || MechPowerUtils.isBlockPoweredByHandCrankToSide(world, i, j, k, iFacing)) {
            bIsPowered = true;
            iPoweredFromFacing = iFacing;
            this.breakPowerSourceThatOpposePoweredFacing(world, i, j, k, iFacing);
         }
      }

      if (bWasPowered != bIsPowered) {
         world.playSoundEffect(i + 0.5, j + 0.5, k + 0.5, "step.gravel", 2.0F + rand.nextFloat() * 0.1F, 0.5F + rand.nextFloat() * 0.1F);
         this.setMechanicallyPoweredFlag(world, i, j, k, bIsPowered);
         if (!bIsPowered) {
            this.setTiltFacing(world, i, j, k, 0);
         } else {
            this.setFacingBasedOnPoweredFromFacing(world, i, j, k, iPoweredFromFacing);
         }
      }
   }

   @Override
   public void onNeighborBlockChange(World world, int i, int j, int k, int iBlockID) {
      if (!world.isUpdatePendingThisTickForBlock(i, j, k, this.blockID)) {
         world.scheduleBlockUpdate(i, j, k, this.blockID, this.tickRate(world));
      }
   }

   @Override
   public int getFacing(IBlockAccess blockAccess, int i, int j, int k) {
      int iFacing = 1;
      if (this.getMechanicallyPoweredFlag(blockAccess, i, j, k)) {
         iFacing = this.getTiltFacing(blockAccess, i, j, k);
      }

      return iFacing;
   }

   @Override
   public void setFacing(World world, int i, int j, int k, int iFacing) {
   }

   @Override
   public int getFacing(int iMetadata) {
      return 0;
   }

   @Override
   public int setFacing(int iMetadata, int iFacing) {
      return iMetadata;
   }

   @Override
   public boolean canOutputMechanicalPower() {
      return false;
   }

   @Override
   public boolean canInputMechanicalPower() {
      return true;
   }

   @Override
   public boolean isInputtingMechanicalPower(World world, int i, int j, int k) {
      return MechPowerUtils.isBlockPoweredByAxle(world, i, j, k, this);
   }

   @Override
   public boolean canInputAxlePowerToFacing(World world, int i, int j, int k, int iFacing) {
      return iFacing >= 2;
   }

   @Override
   public boolean isOutputtingMechanicalPower(World world, int i, int j, int k) {
      return false;
   }

   @Override
   public void overpower(World world, int i, int j, int k) {
   }

   public int getTiltFacing(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 3) + 2;
   }

   public void setTiltFacing(World world, int i, int j, int k, int iFacing) {
      int iFlatFacing = iFacing - 2;
      if (iFlatFacing < 0) {
         iFlatFacing = 0;
      }

      int iMetaData = world.getBlockMetadata(i, j, k) & -4;
      iMetaData |= iFlatFacing & 3;
      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
      world.markBlockRangeForRenderUpdate(i, j, k, i, j, k);
   }

   public boolean getMechanicallyPoweredFlag(IBlockAccess iBlockAccess, int i, int j, int k) {
      return (iBlockAccess.getBlockMetadata(i, j, k) & 4) > 0;
   }

   private void setMechanicallyPoweredFlag(World world, int i, int j, int k, boolean bFlag) {
      int iMetaData = world.getBlockMetadata(i, j, k) & -5;
      if (bFlag) {
         iMetaData |= 4;
      }

      world.setBlockMetadataWithNotify(i, j, k, iMetaData);
   }

   private void setFacingBasedOnPoweredFromFacing(World world, int i, int j, int k, int iPoweredFromFacing) {
      int iNewFacing = Block.rotateFacingAroundY(iPoweredFromFacing, false);
      this.setTiltFacing(world, i, j, k, iNewFacing);
   }

   private void breakPowerSourceThatOpposePoweredFacing(World world, int i, int j, int k, int iPoweredFromFacing) {
      int iOppositePoweredFromFacing = Block.getOppositeFacing(iPoweredFromFacing);

      for (int iFacing = 2; iFacing <= 5; iFacing++) {
         if (iFacing != iPoweredFromFacing) {
            boolean bShouldBreak = false;
            if (iFacing == iOppositePoweredFromFacing) {
               if (MechPowerUtils.isBlockPoweredByAxleToSide(world, i, j, k, iFacing)) {
                  bShouldBreak = true;
               }
            } else if (MechPowerUtils.doesBlockHaveFacingAxleToSide(world, i, j, k, iFacing)) {
               bShouldBreak = true;
            }

            if (bShouldBreak) {
               BlockPos tempPos = new BlockPos(i, j, k);
               tempPos.addFacingAsOffset(iFacing);
               ((AxleBlock)BTWBlocks.axle).breakAxle(world, tempPos.x, tempPos.y, tempPos.z);
            }

            if (MechPowerUtils.isBlockPoweredByHandCrankToSide(world, i, j, k, iFacing)) {
               BlockPos tempPos = new BlockPos(i, j, k);
               tempPos.addFacingAsOffset(iFacing);
               ((HandCrankBlock)BTWBlocks.handCrank).breakCrankWithDrop(world, tempPos.x, tempPos.y, tempPos.z);
            }
         }
      }
   }

   public boolean isOpenSideBlocked(World world, int i, int j, int k) {
      int iFacing = this.getFacing(world, i, j, k);
      BlockPos targetPos = new BlockPos(i, j, k, iFacing);
      return world.isBlockNormalCube(targetPos.x, targetPos.y, targetPos.z);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getIcon(int iSide, int iMetadata) {
      if (this.renderingInterior) {
         return this.iconInteriorBySideArray[iSide];
      } else {
         return this.renderingWideBand ? this.iconWideBandBySideArray[iSide] : this.iconCenterColumnBySideArray[iSide];
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getBlockTexture(IBlockAccess blockAccess, int i, int j, int k, int iSide) {
      int iTextureSide = iSide;
      if (this.getMechanicallyPoweredFlag(blockAccess, i, j, k)) {
         int iFacing = this.getTiltFacing(blockAccess, i, j, k);
         if (iFacing == iSide) {
            iTextureSide = 1;
         } else if (iSide == Block.getOppositeFacing(iFacing)) {
            iTextureSide = 0;
         } else {
            iTextureSide = 2;
         }
      }

      return this.getIcon(iTextureSide, 0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public abstract void registerIcons(IconRegister var1);

   @Environment(EnvType.CLIENT)
   @Override
   public void randomDisplayTick(World world, int i, int j, int k, Random random) {
      if (!this.getMechanicallyPoweredFlag(world, i, j, k)) {
         int iBlockUnderID = world.getBlockId(i, j - 1, k);
         if (iBlockUnderID == Block.fire.blockID || iBlockUnderID == BTWBlocks.stokedFire.blockID) {
            for (int counter = 0; counter < 1; counter++) {
               float smokeX = i + random.nextFloat();
               float smokeY = j + random.nextFloat() * 0.5F + 1.0F;
               float smokeZ = k + random.nextFloat();
               world.spawnParticle("fcwhitesmoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean shouldSideBeRendered(IBlockAccess blockAccess, int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderBlock(RenderBlocks renderBlocks, int i, int j, int k) {
      IBlockAccess blockAccess = renderBlocks.blockAccess;
      int iFacing = this.getFacing(blockAccess, i, j, k);
      this.renderingInterior = false;
      this.renderingWideBand = true;
      RenderUtils.renderBlockWithBoundsAndTextureRotation(renderBlocks, this, i, j, k, iFacing, 0.0F, 0.125F, 0.0F, 1.0F, 0.875F, 1.0F);
      this.renderingWideBand = false;
      RenderUtils.renderBlockWithBoundsAndTextureRotation(renderBlocks, this, i, j, k, iFacing, 0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
      this.renderingInterior = true;
      Tessellator tesselator = Tessellator.instance;
      RenderUtils.setRenderBoundsToBlockFacing(renderBlocks, iFacing, 0.0625F, 0.0F, 0.0625F, 0.9375F, 1.0F, 0.9375F);
      RenderUtils.setTextureRotationBasedOnBlockFacing(renderBlocks, iFacing);
      tesselator.setBrightness(this.e(blockAccess, i, j, k));
      float fInteriorBrightnessMultiplier = 0.66F;
      int iColorMultiplier = this.c(blockAccess, i, j, k);
      float iColorRed = (iColorMultiplier >> 16 & 0xFF) / 255.0F;
      float iColorGreen = (iColorMultiplier >> 8 & 0xFF) / 255.0F;
      float iColorBlue = (iColorMultiplier & 0xFF) / 255.0F;
      tesselator.setColorOpaque_F(0.66F * iColorRed, 0.66F * iColorGreen, 0.66F * iColorBlue);
      double dInteriorOffset = 0.249;
      renderBlocks.renderFaceXPos(this, i - 1.0 + 0.249, j, k, this.getBlockTexture(blockAccess, i, j, k, 4));
      renderBlocks.renderFaceXNeg(this, i + 1.0 - 0.249, j, k, this.getBlockTexture(blockAccess, i, j, k, 5));
      renderBlocks.renderFaceZPos(this, i, j, k - 1.0 + 0.249, this.getBlockTexture(blockAccess, i, j, k, 2));
      renderBlocks.renderFaceZNeg(this, i, j, k + 1.0 - 0.249, this.getBlockTexture(blockAccess, i, j, k, 3));
      renderBlocks.renderFaceYPos(this, i, j - 1.0F + 0.249, k, this.getBlockTexture(blockAccess, i, j, k, 0));
      renderBlocks.renderFaceYNeg(this, i, j + 1.0F - 0.249, k, this.getBlockTexture(blockAccess, i, j, k, 1));
      renderBlocks.clearUVRotation();
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderBlockAsItem(RenderBlocks renderBlocks, int iItemDamage, float fBrightness) {
      this.renderingInterior = false;
      this.renderingWideBand = true;
      renderBlocks.setRenderBounds(0.0, 0.125, 0.0, 1.0, 0.875, 1.0);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 0);
      this.renderingWideBand = false;
      renderBlocks.setRenderBounds(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
      RenderUtils.renderInvBlockWithMetadata(renderBlocks, this, -0.5F, -0.5F, -0.5F, 0);
      this.renderingInterior = true;
      renderBlocks.setRenderBounds(0.0625, 0.0, 0.0625, 0.9375, 1.0, 0.9375);
      Tessellator tessellator = Tessellator.instance;
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      double dInteriorOffset = 0.249;
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      renderBlocks.renderFaceYPos(this, 0.0, -0.751, 0.0, this.getIcon(0, 0));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      renderBlocks.renderFaceZNeg(this, 0.0, 0.0, 0.751, this.getIcon(3, 0));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      renderBlocks.renderFaceZPos(this, 0.0, 0.0, -0.751, this.getIcon(2, 0));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      renderBlocks.renderFaceXNeg(this, 0.751, 0.0, 0.0, this.getIcon(5, 0));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      renderBlocks.renderFaceXPos(this, -0.751, 0.0, 0.0, this.getIcon(4, 0));
      tessellator.draw();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
   }
}
