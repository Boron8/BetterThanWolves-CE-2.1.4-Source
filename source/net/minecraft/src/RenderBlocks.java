package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.block.blocks.PistonBlockBase;
import btw.block.blocks.TorchBlockBase;
import btw.world.util.WorldUtils;
import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.cc.Colorizer;
import com.prupe.mcpatcher.ctm.CTMUtils;
import com.prupe.mcpatcher.ctm.GlassPaneRenderer;
import com.prupe.mcpatcher.mal.block.RenderBlocksUtils;
import com.prupe.mcpatcher.renderpass.RenderPass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderBlocks {
   public IBlockAccess blockAccess;
   private Icon overrideBlockTexture = null;
   private boolean flipTexture = false;
   private boolean renderAllFaces = false;
   public static boolean fancyGrass = true;
   public boolean useInventoryTint = true;
   private double renderMinX;
   private double renderMaxX;
   private double renderMinY;
   private double renderMaxY;
   private double renderMinZ;
   private double renderMaxZ;
   private boolean lockBlockBounds = false;
   private boolean partialRenderBounds = false;
   private final Minecraft minecraftRB;
   private int uvRotateEast = 0;
   private int uvRotateWest = 0;
   private int uvRotateSouth = 0;
   private int uvRotateNorth = 0;
   private int uvRotateTop = 0;
   private int uvRotateBottom = 0;
   private boolean enableAO;
   private float aoLightValueScratchXYZNNN;
   private float aoLightValueScratchXYNN;
   private float aoLightValueScratchXYZNNP;
   private float aoLightValueScratchYZNN;
   private float aoLightValueScratchYZNP;
   private float aoLightValueScratchXYZPNN;
   private float aoLightValueScratchXYPN;
   private float aoLightValueScratchXYZPNP;
   private float aoLightValueScratchXYZNPN;
   private float aoLightValueScratchXYNP;
   private float aoLightValueScratchXYZNPP;
   private float aoLightValueScratchYZPN;
   private float aoLightValueScratchXYZPPN;
   private float aoLightValueScratchXYPP;
   private float aoLightValueScratchYZPP;
   private float aoLightValueScratchXYZPPP;
   private float aoLightValueScratchXZNN;
   private float aoLightValueScratchXZPN;
   private float aoLightValueScratchXZNP;
   private float aoLightValueScratchXZPP;
   private int aoBrightnessXYZNNN;
   private int aoBrightnessXYNN;
   private int aoBrightnessXYZNNP;
   private int aoBrightnessYZNN;
   private int aoBrightnessYZNP;
   private int aoBrightnessXYZPNN;
   private int aoBrightnessXYPN;
   private int aoBrightnessXYZPNP;
   private int aoBrightnessXYZNPN;
   private int aoBrightnessXYNP;
   private int aoBrightnessXYZNPP;
   private int aoBrightnessYZPN;
   private int aoBrightnessXYZPPN;
   private int aoBrightnessXYPP;
   private int aoBrightnessYZPP;
   private int aoBrightnessXYZPPP;
   private int aoBrightnessXZNN;
   private int aoBrightnessXZPN;
   private int aoBrightnessXZNP;
   private int aoBrightnessXZPP;
   private int brightnessTopLeft;
   private int brightnessBottomLeft;
   private int brightnessBottomRight;
   private int brightnessTopRight;
   public float colorRedTopLeft;
   public float colorRedBottomLeft;
   public float colorRedBottomRight;
   public float colorRedTopRight;
   public float colorGreenTopLeft;
   public float colorGreenBottomLeft;
   public float colorGreenBottomRight;
   public float colorGreenTopRight;
   public float colorBlueTopLeft;
   public float colorBlueBottomLeft;
   public float colorBlueBottomRight;
   public float colorBlueTopRight;

   public RenderBlocks(IBlockAccess par1IBlockAccess) {
      this.blockAccess = par1IBlockAccess;
      this.minecraftRB = Minecraft.getMinecraft();
   }

   public RenderBlocks() {
      this.minecraftRB = Minecraft.getMinecraft();
   }

   public void setOverrideBlockTexture(Icon par1Icon) {
      this.overrideBlockTexture = par1Icon;
   }

   public void clearOverrideBlockTexture() {
      this.overrideBlockTexture = null;
   }

   public boolean hasOverrideBlockTexture() {
      return this.overrideBlockTexture != null;
   }

   public void setRenderBounds(double par1, double par3, double par5, double par7, double par9, double par11) {
      if (!this.lockBlockBounds) {
         this.renderMinX = par1;
         this.renderMaxX = par7;
         this.renderMinY = par3;
         this.renderMaxY = par9;
         this.renderMinZ = par5;
         this.renderMaxZ = par11;
         this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2
            && (
               this.renderMinX > 0.0
                  || this.renderMaxX < 1.0
                  || this.renderMinY > 0.0
                  || this.renderMaxY < 1.0
                  || this.renderMinZ > 0.0
                  || this.renderMaxZ < 1.0
            );
      }
   }

   public void setRenderBoundsFromBlock(Block par1Block) {
      if (!this.lockBlockBounds) {
         AxisAlignedBB blockBounds = par1Block.getFixedBlockBoundsFromPool();
         this.renderMinX = blockBounds.minX;
         this.renderMinY = blockBounds.minY;
         this.renderMinZ = blockBounds.minZ;
         this.renderMaxX = blockBounds.maxX;
         this.renderMaxY = blockBounds.maxY;
         this.renderMaxZ = blockBounds.maxZ;
         this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2
            && (
               this.renderMinX > 0.0
                  || this.renderMaxX < 1.0
                  || this.renderMinY > 0.0
                  || this.renderMaxY < 1.0
                  || this.renderMinZ > 0.0
                  || this.renderMaxZ < 1.0
            );
      }
   }

   public void overrideBlockBounds(double par1, double par3, double par5, double par7, double par9, double par11) {
      this.renderMinX = par1;
      this.renderMaxX = par7;
      this.renderMinY = par3;
      this.renderMaxY = par9;
      this.renderMinZ = par5;
      this.renderMaxZ = par11;
      this.lockBlockBounds = true;
      this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2
         && (this.renderMinX > 0.0 || this.renderMaxX < 1.0 || this.renderMinY > 0.0 || this.renderMaxY < 1.0 || this.renderMinZ > 0.0 || this.renderMaxZ < 1.0);
   }

   public void unlockBlockBounds() {
      this.lockBlockBounds = false;
   }

   public void renderBlockUsingTexture(Block par1Block, int par2, int par3, int par4, Icon par5Icon) {
      this.setOverrideBlockTexture(par5Icon);
      this.renderBlockByRenderType(par1Block, par2, par3, par4);
      this.clearOverrideBlockTexture();
   }

   public void renderBlockAllFaces(Block par1Block, int par2, int par3, int par4) {
      this.renderAllFaces = true;
      this.renderBlockByRenderType(par1Block, par2, par3, par4);
      this.renderAllFaces = false;
   }

   public boolean renderBlockEndPortalFrame(BlockEndPortalFrame par1BlockEndPortalFrame, int par2, int par3, int par4) {
      int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var6 = var5 & 3;
      if (var6 == 0) {
         this.uvRotateTop = 3;
      } else if (var6 == 3) {
         this.uvRotateTop = 1;
      } else if (var6 == 1) {
         this.uvRotateTop = 2;
      }

      if (!BlockEndPortalFrame.isEnderEyeInserted(var5)) {
         this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);
         this.renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
         this.uvRotateTop = 0;
         return true;
      } else {
         this.renderAllFaces = true;
         this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.8125, 1.0);
         this.renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
         this.setOverrideBlockTexture(par1BlockEndPortalFrame.func_94398_p());
         this.setRenderBounds(0.25, 0.8125, 0.25, 0.75, 1.0, 0.75);
         this.renderStandardBlock(par1BlockEndPortalFrame, par2, par3, par4);
         this.renderAllFaces = false;
         this.clearOverrideBlockTexture();
         this.uvRotateTop = 0;
         return true;
      }
   }

   public boolean renderBlockBed(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var7 = BlockBed.j(var6);
      boolean var8 = BlockBed.isBlockHeadOfBed(var6);
      float var9 = 0.5F;
      float var10 = 1.0F;
      float var11 = 0.8F;
      float var12 = 0.6F;
      int var25 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
      var5.setBrightness(var25);
      var5.setColorOpaque_F(var9, var9, var9);
      Icon var27 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
      double var28 = var27.getMinU();
      double var30 = var27.getMaxU();
      double var32 = var27.getMinV();
      double var34 = var27.getMaxV();
      double var36 = par2 + this.renderMinX;
      double var38 = par2 + this.renderMaxX;
      double var40 = par3 + this.renderMinY + 0.1875;
      double var42 = par4 + this.renderMinZ;
      double var44 = par4 + this.renderMaxZ;
      var5.addVertexWithUV(var36, var40, var44, var28, var34);
      var5.addVertexWithUV(var36, var40, var42, var28, var32);
      var5.addVertexWithUV(var38, var40, var42, var30, var32);
      var5.addVertexWithUV(var38, var40, var44, var30, var34);
      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
      var5.setColorOpaque_F(var10, var10, var10);
      var27 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1);
      var28 = var27.getMinU();
      var30 = var27.getMaxU();
      var32 = var27.getMinV();
      var34 = var27.getMaxV();
      var36 = var28;
      var38 = var30;
      var40 = var32;
      var42 = var32;
      var44 = var28;
      double var46 = var30;
      double var48 = var34;
      double var50 = var34;
      if (var7 == 0) {
         var38 = var28;
         var40 = var34;
         var44 = var30;
         var50 = var32;
      } else if (var7 == 2) {
         var36 = var30;
         var42 = var34;
         var46 = var28;
         var48 = var32;
      } else if (var7 == 3) {
         var36 = var30;
         var42 = var34;
         var46 = var28;
         var48 = var32;
         var38 = var28;
         var40 = var34;
         var44 = var30;
         var50 = var32;
      }

      double var52 = par2 + this.renderMinX;
      double var54 = par2 + this.renderMaxX;
      double var56 = par3 + this.renderMaxY;
      double var58 = par4 + this.renderMinZ;
      double var60 = par4 + this.renderMaxZ;
      var5.addVertexWithUV(var54, var56, var60, var44, var48);
      var5.addVertexWithUV(var54, var56, var58, var36, var40);
      var5.addVertexWithUV(var52, var56, var58, var38, var42);
      var5.addVertexWithUV(var52, var56, var60, var46, var50);
      int var62 = Direction.directionToFacing[var7];
      if (var8) {
         var62 = Direction.directionToFacing[Direction.rotateOpposite[var7]];
      }

      byte var63 = 4;
      switch (var7) {
         case 0:
            var63 = 5;
            break;
         case 1:
            var63 = 3;
         case 2:
         default:
            break;
         case 3:
            var63 = 2;
      }

      if (var62 != 2 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2))) {
         var5.setBrightness(this.renderMinZ > 0.0 ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
         var5.setColorOpaque_F(var11, var11, var11);
         this.flipTexture = var63 == 2;
         this.renderFaceZNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2));
      }

      if (var62 != 3 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3))) {
         var5.setBrightness(this.renderMaxZ < 1.0 ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
         var5.setColorOpaque_F(var11, var11, var11);
         this.flipTexture = var63 == 3;
         this.renderFaceZPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
      }

      if (var62 != 4 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4))) {
         var5.setBrightness(this.renderMinZ > 0.0 ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
         var5.setColorOpaque_F(var12, var12, var12);
         this.flipTexture = var63 == 4;
         this.renderFaceXNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4));
      }

      if (var62 != 5 && (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5))) {
         var5.setBrightness(this.renderMaxZ < 1.0 ? var25 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
         var5.setColorOpaque_F(var12, var12, var12);
         this.flipTexture = var63 == 5;
         this.renderFaceXPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5));
      }

      this.flipTexture = false;
      return true;
   }

   public boolean renderBlockBrewingStand(BlockBrewingStand par1BlockBrewingStand, int par2, int par3, int par4) {
      this.setRenderBounds(0.4375, 0.0, 0.4375, 0.5625, 0.875, 0.5625);
      this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
      this.setOverrideBlockTexture(par1BlockBrewingStand.getBrewingStandIcon());
      this.setRenderBounds(0.5625, 0.0, 0.3125, 0.9375, 0.125, 0.6875);
      this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
      this.setRenderBounds(0.125, 0.0, 0.0625, 0.5, 0.125, 0.4375);
      this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
      this.setRenderBounds(0.125, 0.0, 0.5625, 0.5, 0.125, 0.9375);
      this.renderStandardBlock(par1BlockBrewingStand, par2, par3, par4);
      this.clearOverrideBlockTexture();
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1BlockBrewingStand.e(this.blockAccess, par2, par3, par4));
      float var6 = 1.0F;
      int var7 = par1BlockBrewingStand.c(this.blockAccess, par2, par3, par4);
      float var8 = (var7 >> 16 & 0xFF) / 255.0F;
      float var9 = (var7 >> 8 & 0xFF) / 255.0F;
      float var10 = (var7 & 0xFF) / 255.0F;
      var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
      Icon var32 = this.blockAccess == null
         ? this.getBlockIconFromSideAndMetadata(par1BlockBrewingStand, 0, 0)
         : this.getBlockIcon(par1BlockBrewingStand, this.blockAccess, par2, par3, par4, 0);
      if (this.hasOverrideBlockTexture()) {
         var32 = this.overrideBlockTexture;
      }

      double var33 = var32.getMinV();
      double var14 = var32.getMaxV();
      int var16 = this.blockAccess.getBlockMetadata(par2, par3, par4);

      for (int var17 = 0; var17 < 3; var17++) {
         double var18 = var17 * Math.PI * 2.0 / 3.0 + (Math.PI / 2);
         double var20 = var32.getInterpolatedU(8.0);
         double var22 = var32.getMaxU();
         if ((var16 & 1 << var17) != 0) {
            var22 = var32.getMinU();
         }

         double var24 = par2 + 0.5;
         double var26 = par2 + 0.5 + Math.sin(var18) * 8.0 / 16.0;
         double var28 = par4 + 0.5;
         double var30 = par4 + 0.5 + Math.cos(var18) * 8.0 / 16.0;
         var5.addVertexWithUV(var24, par3 + 1, var28, var20, var33);
         var5.addVertexWithUV(var24, par3 + 0, var28, var20, var14);
         var5.addVertexWithUV(var26, par3 + 0, var30, var22, var14);
         var5.addVertexWithUV(var26, par3 + 1, var30, var22, var33);
         var5.addVertexWithUV(var26, par3 + 1, var30, var22, var33);
         var5.addVertexWithUV(var26, par3 + 0, var30, var22, var14);
         var5.addVertexWithUV(var24, par3 + 0, var28, var20, var14);
         var5.addVertexWithUV(var24, par3 + 1, var28, var20, var33);
      }

      return true;
   }

   public boolean renderBlockCauldron(BlockCauldron par1BlockCauldron, int par2, int par3, int par4) {
      this.renderStandardBlock(par1BlockCauldron, par2, par3, par4);
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1BlockCauldron.e(this.blockAccess, par2, par3, par4));
      float var6 = 1.0F;
      int var7 = par1BlockCauldron.c(this.blockAccess, par2, par3, par4);
      float var8 = (var7 >> 16 & 0xFF) / 255.0F;
      float var9 = (var7 >> 8 & 0xFF) / 255.0F;
      float var10 = (var7 & 0xFF) / 255.0F;
      var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
      Icon var16 = par1BlockCauldron.m(2);
      ColorizeBlock.computeWaterColor();
      var5.setColorOpaque_F(Colorizer.setColor[0], Colorizer.setColor[1], Colorizer.setColor[2]);
      float var12 = 0.124F;
      this.renderFaceXPos(par1BlockCauldron, par2 - 1.0F + var12, par3, par4, var16);
      this.renderFaceXNeg(par1BlockCauldron, par2 + 1.0F - var12, par3, par4, var16);
      this.renderFaceZPos(par1BlockCauldron, par2, par3, par4 - 1.0F + var12, var16);
      this.renderFaceZNeg(par1BlockCauldron, par2, par3, par4 + 1.0F - var12, var16);
      Icon var17 = BlockCauldron.func_94375_b("cauldron_inner");
      this.renderFaceYPos(par1BlockCauldron, par2, par3 - 1.0F + 0.25F, par4, var17);
      this.renderFaceYNeg(par1BlockCauldron, par2, par3 + 1.0F - 0.75F, par4, var17);
      int var14 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      if (var14 > 0) {
         Icon var15 = BlockFluid.func_94424_b("water");
         if (var14 > 3) {
            var14 = 3;
         }

         this.renderFaceYPos(par1BlockCauldron, par2, par3 - 1.0F + (6.0F + var14 * 3.0F) / 16.0F, par4, var15);
      }

      return true;
   }

   public boolean renderBlockFlowerpot(BlockFlowerPot par1BlockFlowerPot, int par2, int par3, int par4) {
      this.renderStandardBlock(par1BlockFlowerPot, par2, par3, par4);
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1BlockFlowerPot.e(this.blockAccess, par2, par3, par4));
      float var6 = 1.0F;
      int var7 = par1BlockFlowerPot.c(this.blockAccess, par2, par3, par4);
      Icon var8 = this.blockAccess == null
         ? this.getBlockIconFromSide(par1BlockFlowerPot, 0)
         : this.getBlockIcon(par1BlockFlowerPot, this.blockAccess, par2, par3, par4, 0);
      float var9 = (var7 >> 16 & 0xFF) / 255.0F;
      float var10 = (var7 >> 8 & 0xFF) / 255.0F;
      float var11 = (var7 & 0xFF) / 255.0F;
      var5.setColorOpaque_F(var6 * var9, var6 * var10, var6 * var11);
      float var12 = 0.1865F;
      this.renderFaceXPos(par1BlockFlowerPot, par2 - 0.5F + var12, par3, par4, var8);
      this.renderFaceXNeg(par1BlockFlowerPot, par2 + 0.5F - var12, par3, par4, var8);
      this.renderFaceZPos(par1BlockFlowerPot, par2, par3, par4 - 0.5F + var12, var8);
      this.renderFaceZNeg(par1BlockFlowerPot, par2, par3, par4 + 0.5F - var12, var8);
      this.renderFaceYPos(par1BlockFlowerPot, par2, par3 - 0.5F + var12 + 0.1875F, par4, this.getBlockIcon(Block.dirt));
      int var19 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      if (var19 != 0) {
         float var14 = 0.0F;
         float var15 = 4.0F;
         float var16 = 0.0F;
         BlockFlower var17 = null;
         switch (var19) {
            case 1:
               var17 = Block.plantRed;
               break;
            case 2:
               var17 = Block.plantYellow;
            case 3:
            case 4:
            case 5:
            case 6:
            default:
               break;
            case 7:
               var17 = Block.mushroomRed;
               break;
            case 8:
               var17 = Block.mushroomBrown;
         }

         var5.addTranslation(var14 / 16.0F, var15 / 16.0F, var16 / 16.0F);
         if (var17 != null) {
            this.renderBlockByRenderType(var17, par2, par3, par4);
         } else if (var19 == 9) {
            this.renderAllFaces = true;
            float var18 = 0.125F;
            this.setRenderBounds(0.5F - var18, 0.0, 0.5F - var18, 0.5F + var18, 0.25, 0.5F + var18);
            this.renderStandardBlock(Block.cactus, par2, par3, par4);
            this.setRenderBounds(0.5F - var18, 0.25, 0.5F - var18, 0.5F + var18, 0.5, 0.5F + var18);
            this.renderStandardBlock(Block.cactus, par2, par3, par4);
            this.setRenderBounds(0.5F - var18, 0.5, 0.5F - var18, 0.5F + var18, 0.75, 0.5F + var18);
            this.renderStandardBlock(Block.cactus, par2, par3, par4);
            this.renderAllFaces = false;
            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         } else if (var19 == 3) {
            this.drawCrossedSquares(BTWBlocks.oakSapling, 15, par2, par3, par4, 0.75F);
         } else if (var19 == 4) {
            this.drawCrossedSquares(BTWBlocks.spruceSapling, 15, par2, par3, par4, 0.75F);
         } else if (var19 == 5) {
            this.drawCrossedSquares(BTWBlocks.birchSapling, 15, par2, par3, par4, 0.75F);
         } else if (var19 == 6) {
            this.drawCrossedSquares(BTWBlocks.jungleSapling, 15, par2, par3, par4, 0.75F);
         } else if (var19 == 11) {
            var7 = Block.tallGrass.colorMultiplier(this.blockAccess, par2, par3, par4);
            var9 = (var7 >> 16 & 0xFF) / 255.0F;
            var10 = (var7 >> 8 & 0xFF) / 255.0F;
            var11 = (var7 & 0xFF) / 255.0F;
            var5.setColorOpaque_F(var6 * var9, var6 * var10, var6 * var11);
            this.drawCrossedSquares(Block.tallGrass, 2, par2, par3, par4, 0.75F);
         } else if (var19 == 10) {
            this.drawCrossedSquares(Block.deadBush, 2, par2, par3, par4, 0.75F);
         }

         var5.addTranslation(-var14 / 16.0F, -var15 / 16.0F, -var16 / 16.0F);
      }

      return true;
   }

   public boolean renderBlockAnvil(BlockAnvil par1BlockAnvil, int par2, int par3, int par4) {
      return this.renderBlockAnvilMetadata(par1BlockAnvil, par2, par3, par4, this.blockAccess.getBlockMetadata(par2, par3, par4));
   }

   public boolean renderBlockAnvilMetadata(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5) {
      Tessellator var6 = Tessellator.instance;
      var6.setBrightness(par1BlockAnvil.e(this.blockAccess, par2, par3, par4));
      float var7 = 1.0F;
      int var8 = par1BlockAnvil.c(this.blockAccess, par2, par3, par4);
      float var9 = (var8 >> 16 & 0xFF) / 255.0F;
      float var10 = (var8 >> 8 & 0xFF) / 255.0F;
      float var11 = (var8 & 0xFF) / 255.0F;
      var6.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
      return this.renderBlockAnvilOrient(par1BlockAnvil, par2, par3, par4, par5, false);
   }

   private boolean renderBlockAnvilOrient(BlockAnvil par1BlockAnvil, int par2, int par3, int par4, int par5, boolean par6) {
      int var7 = par6 ? 0 : par5 & 3;
      boolean var8 = false;
      float var9 = 0.0F;
      switch (var7) {
         case 0:
            this.uvRotateSouth = 2;
            this.uvRotateNorth = 1;
            this.uvRotateTop = 3;
            this.uvRotateBottom = 3;
            break;
         case 1:
            this.uvRotateEast = 1;
            this.uvRotateWest = 2;
            this.uvRotateTop = 2;
            this.uvRotateBottom = 1;
            var8 = true;
            break;
         case 2:
            this.uvRotateSouth = 1;
            this.uvRotateNorth = 2;
            break;
         case 3:
            this.uvRotateEast = 2;
            this.uvRotateWest = 1;
            this.uvRotateTop = 1;
            this.uvRotateBottom = 2;
            var8 = true;
      }

      var9 = this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 0, var9, 0.75F, 0.25F, 0.75F, var8, par6, par5);
      var9 = this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 1, var9, 0.5F, 0.0625F, 0.625F, var8, par6, par5);
      var9 = this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 2, var9, 0.25F, 0.3125F, 0.5F, var8, par6, par5);
      this.renderBlockAnvilRotate(par1BlockAnvil, par2, par3, par4, 3, var9, 0.625F, 0.375F, 1.0F, var8, par6, par5);
      this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.uvRotateEast = 0;
      this.uvRotateWest = 0;
      this.uvRotateSouth = 0;
      this.uvRotateNorth = 0;
      this.uvRotateTop = 0;
      this.uvRotateBottom = 0;
      return true;
   }

   private float renderBlockAnvilRotate(
      BlockAnvil par1BlockAnvil,
      int par2,
      int par3,
      int par4,
      int par5,
      float par6,
      float par7,
      float par8,
      float par9,
      boolean par10,
      boolean par11,
      int par12
   ) {
      if (par10) {
         float var13 = par7;
         par7 = par9;
         par9 = var13;
      }

      par7 /= 2.0F;
      par9 /= 2.0F;
      par1BlockAnvil.field_82521_b = par5;
      this.setRenderBounds(0.5F - par7, par6, 0.5F - par9, 0.5F + par7, par6 + par8, 0.5F + par9);
      if (par11) {
         Tessellator var14 = Tessellator.instance;
         var14.startDrawingQuads();
         var14.setNormal(0.0F, -1.0F, 0.0F);
         this.renderFaceYNeg(
            par1BlockAnvil,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 0, par12)
               : this.getBlockIcon(par1BlockAnvil, this.blockAccess, par2, par3, par4, 0)
         );
         var14.draw();
         var14.startDrawingQuads();
         var14.setNormal(0.0F, 1.0F, 0.0F);
         this.renderFaceYPos(
            par1BlockAnvil,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 1, par12)
               : this.getBlockIcon(par1BlockAnvil, this.blockAccess, par2, par3, par4, 1)
         );
         var14.draw();
         var14.startDrawingQuads();
         var14.setNormal(0.0F, 0.0F, -1.0F);
         this.renderFaceZNeg(
            par1BlockAnvil,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 2, par12)
               : this.getBlockIcon(par1BlockAnvil, this.blockAccess, par2, par3, par4, 2)
         );
         var14.draw();
         var14.startDrawingQuads();
         var14.setNormal(0.0F, 0.0F, 1.0F);
         this.renderFaceZPos(
            par1BlockAnvil,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 3, par12)
               : this.getBlockIcon(par1BlockAnvil, this.blockAccess, par2, par3, par4, 3)
         );
         var14.draw();
         var14.startDrawingQuads();
         var14.setNormal(-1.0F, 0.0F, 0.0F);
         this.renderFaceXNeg(
            par1BlockAnvil,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 4, par12)
               : this.getBlockIcon(par1BlockAnvil, this.blockAccess, par2, par3, par4, 4)
         );
         var14.draw();
         var14.startDrawingQuads();
         var14.setNormal(1.0F, 0.0F, 0.0F);
         this.renderFaceXPos(
            par1BlockAnvil,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockAnvil, 5, par12)
               : this.getBlockIcon(par1BlockAnvil, this.blockAccess, par2, par3, par4, 5)
         );
         var14.draw();
      } else {
         this.renderStandardBlock(par1BlockAnvil, par2, par3, par4);
      }

      return par6 + par8;
   }

   public boolean renderBlockTorch(Block par1Block, int par2, int par3, int par4) {
      int iMetadata = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var5 = TorchBlockBase.getOrientation(iMetadata);
      Tessellator var6 = Tessellator.instance;
      var6.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      double var7 = 0.4F;
      double var9 = 0.5 - var7;
      double var11 = 0.2F;
      if (var5 == 1) {
         this.renderTorchAtAngle(par1Block, par2 - var9, par3 + var11, par4, -var7, 0.0, iMetadata);
      } else if (var5 == 2) {
         this.renderTorchAtAngle(par1Block, par2 + var9, par3 + var11, par4, var7, 0.0, iMetadata);
      } else if (var5 == 3) {
         this.renderTorchAtAngle(par1Block, par2, par3 + var11, par4 - var9, 0.0, -var7, iMetadata);
      } else if (var5 == 4) {
         this.renderTorchAtAngle(par1Block, par2, par3 + var11, par4 + var9, 0.0, var7, iMetadata);
      } else {
         this.renderTorchAtAngle(par1Block, par2, par3, par4, 0.0, 0.0, iMetadata);
      }

      return true;
   }

   public boolean renderBlockRepeater(BlockRedstoneRepeater par1BlockRedstoneRepeater, int par2, int par3, int par4) {
      int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var6 = var5 & 3;
      int var7 = (var5 & 12) >> 2;
      Tessellator var8 = Tessellator.instance;
      var8.setBrightness(par1BlockRedstoneRepeater.e(this.blockAccess, par2, par3, par4));
      var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      double var9 = -0.1875;
      boolean var11 = par1BlockRedstoneRepeater.func_94476_e(this.blockAccess, par2, par3, par4, var5);
      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = 0.0;
      double var18 = 0.0;
      switch (var6) {
         case 0:
            var18 = -0.3125;
            var14 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
            break;
         case 1:
            var16 = 0.3125;
            var12 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
            break;
         case 2:
            var18 = 0.3125;
            var14 = -BlockRedstoneRepeater.repeaterTorchOffset[var7];
            break;
         case 3:
            var16 = -0.3125;
            var12 = BlockRedstoneRepeater.repeaterTorchOffset[var7];
      }

      if (!var11) {
         this.renderTorchAtAngle(par1BlockRedstoneRepeater, par2 + var12, par3 + var9, par4 + var14, 0.0, 0.0, 0);
      } else {
         Icon var20 = this.getBlockIcon(Block.bedrock);
         this.setOverrideBlockTexture(var20);
         float var21 = 2.0F;
         float var22 = 14.0F;
         float var23 = 7.0F;
         float var24 = 9.0F;
         switch (var6) {
            case 1:
            case 3:
               var21 = 7.0F;
               var22 = 9.0F;
               var23 = 2.0F;
               var24 = 14.0F;
            case 0:
            case 2:
            default:
               this.setRenderBounds(
                  var21 / 16.0F + (float)var12, 0.125, var23 / 16.0F + (float)var14, var22 / 16.0F + (float)var12, 0.25, var24 / 16.0F + (float)var14
               );
               double var25 = var20.getInterpolatedU(var21);
               double var27 = var20.getInterpolatedV(var23);
               double var29 = var20.getInterpolatedU(var22);
               double var31 = var20.getInterpolatedV(var24);
               var8.addVertexWithUV(par2 + var21 / 16.0F + var12, par3 + 0.25F, par4 + var23 / 16.0F + var14, var25, var27);
               var8.addVertexWithUV(par2 + var21 / 16.0F + var12, par3 + 0.25F, par4 + var24 / 16.0F + var14, var25, var31);
               var8.addVertexWithUV(par2 + var22 / 16.0F + var12, par3 + 0.25F, par4 + var24 / 16.0F + var14, var29, var31);
               var8.addVertexWithUV(par2 + var22 / 16.0F + var12, par3 + 0.25F, par4 + var23 / 16.0F + var14, var29, var27);
               this.renderStandardBlock(par1BlockRedstoneRepeater, par2, par3, par4);
               this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.125, 1.0);
               this.clearOverrideBlockTexture();
         }
      }

      var8.setBrightness(par1BlockRedstoneRepeater.e(this.blockAccess, par2, par3, par4));
      var8.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      this.renderTorchAtAngle(par1BlockRedstoneRepeater, par2 + var16, par3 + var9, par4 + var18, 0.0, 0.0, 0);
      this.renderBlockRedstoneLogic(par1BlockRedstoneRepeater, par2, par3, par4);
      return true;
   }

   private boolean renderBlockComparator(BlockComparator par1BlockComparator, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1BlockComparator.e(this.blockAccess, par2, par3, par4));
      var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var7 = var6 & 3;
      double var8 = 0.0;
      double var10 = -0.1875;
      double var12 = 0.0;
      double var14 = 0.0;
      double var16 = 0.0;
      Icon var18;
      if (par1BlockComparator.func_94490_c(var6)) {
         var18 = Block.torchRedstoneActive.getBlockTextureFromSide(0);
      } else {
         var10 -= 0.1875;
         var18 = Block.torchRedstoneIdle.getBlockTextureFromSide(0);
      }

      switch (var7) {
         case 0:
            var12 = -0.3125;
            var16 = 1.0;
            break;
         case 1:
            var8 = 0.3125;
            var14 = -1.0;
            break;
         case 2:
            var12 = 0.3125;
            var16 = -1.0;
            break;
         case 3:
            var8 = -0.3125;
            var14 = 1.0;
      }

      this.renderTorchAtAngle(par1BlockComparator, par2 + 0.25 * var14 + 0.1875 * var16, par3 - 0.1875F, par4 + 0.25 * var16 + 0.1875 * var14, 0.0, 0.0, var6);
      this.renderTorchAtAngle(par1BlockComparator, par2 + 0.25 * var14 + -0.1875 * var16, par3 - 0.1875F, par4 + 0.25 * var16 + -0.1875 * var14, 0.0, 0.0, var6);
      this.setOverrideBlockTexture(var18);
      this.renderTorchAtAngle(par1BlockComparator, par2 + var8, par3 + var10, par4 + var12, 0.0, 0.0, var6);
      this.clearOverrideBlockTexture();
      this.renderBlockRedstoneLogicMetadata(par1BlockComparator, par2, par3, par4, var7);
      return true;
   }

   public boolean renderBlockRedstoneLogic(BlockRedstoneLogic par1BlockRedstoneLogic, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      this.renderBlockRedstoneLogicMetadata(par1BlockRedstoneLogic, par2, par3, par4, this.blockAccess.getBlockMetadata(par2, par3, par4) & 3);
      return true;
   }

   public void renderBlockRedstoneLogicMetadata(BlockRedstoneLogic par1BlockRedstoneLogic, int par2, int par3, int par4, int par5) {
      this.renderStandardBlock(par1BlockRedstoneLogic, par2, par3, par4);
      Tessellator var6 = Tessellator.instance;
      var6.setBrightness(par1BlockRedstoneLogic.e(this.blockAccess, par2, par3, par4));
      var6.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      int var7 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      Icon var8 = this.blockAccess == null
         ? this.getBlockIconFromSideAndMetadata(par1BlockRedstoneLogic, 1, var7)
         : this.getBlockIcon(par1BlockRedstoneLogic, this.blockAccess, par2, par3, par4, 1);
      double var9 = var8.getMinU();
      double var11 = var8.getMaxU();
      double var13 = var8.getMinV();
      double var15 = var8.getMaxV();
      double var17 = 0.125;
      double var19 = par2 + 1;
      double var21 = par2 + 1;
      double var23 = par2 + 0;
      double var25 = par2 + 0;
      double var27 = par4 + 0;
      double var29 = par4 + 1;
      double var31 = par4 + 1;
      double var33 = par4 + 0;
      double var35 = par3 + var17;
      if (par5 == 2) {
         var19 = var21 = par2 + 0;
         var23 = var25 = par2 + 1;
         var27 = var33 = par4 + 1;
         var29 = var31 = par4 + 0;
      } else if (par5 == 3) {
         var19 = var25 = par2 + 0;
         var21 = var23 = par2 + 1;
         var27 = var29 = par4 + 0;
         var31 = var33 = par4 + 1;
      } else if (par5 == 1) {
         var19 = var25 = par2 + 1;
         var21 = var23 = par2 + 0;
         var27 = var29 = par4 + 1;
         var31 = var33 = par4 + 0;
      }

      var6.addVertexWithUV(var25, var35, var33, var9, var13);
      var6.addVertexWithUV(var23, var35, var31, var9, var15);
      var6.addVertexWithUV(var21, var35, var29, var11, var15);
      var6.addVertexWithUV(var19, var35, var27, var11, var13);
   }

   public void renderPistonBaseAllFaces(Block par1Block, int par2, int par3, int par4) {
      this.renderAllFaces = true;
      this.renderPistonBase(par1Block, par2, par3, par4, true);
      this.renderAllFaces = false;
   }

   public boolean renderPistonBase(Block par1Block, int par2, int par3, int par4, boolean par5) {
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      boolean var7 = par5 || (var6 & 8) != 0;
      int var8 = BlockPistonBase.getOrientation(var6);
      if (var7) {
         switch (var8) {
            case 0:
               this.uvRotateEast = 3;
               this.uvRotateWest = 3;
               this.uvRotateSouth = 3;
               this.uvRotateNorth = 3;
               this.setRenderBounds(0.0, 0.25, 0.0, 1.0, 1.0, 1.0);
               break;
            case 1:
               this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.75, 1.0);
               break;
            case 2:
               this.uvRotateSouth = 1;
               this.uvRotateNorth = 2;
               this.setRenderBounds(0.0, 0.0, 0.25, 1.0, 1.0, 1.0);
               break;
            case 3:
               this.uvRotateSouth = 2;
               this.uvRotateNorth = 1;
               this.uvRotateTop = 3;
               this.uvRotateBottom = 3;
               this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 0.75);
               break;
            case 4:
               this.uvRotateEast = 1;
               this.uvRotateWest = 2;
               this.uvRotateTop = 2;
               this.uvRotateBottom = 1;
               this.setRenderBounds(0.25, 0.0, 0.0, 1.0, 1.0, 1.0);
               break;
            case 5:
               this.uvRotateEast = 2;
               this.uvRotateWest = 1;
               this.uvRotateTop = 1;
               this.uvRotateBottom = 2;
               this.setRenderBounds(0.0, 0.0, 0.0, 0.75, 1.0, 1.0);
         }

         PistonBlockBase.isRenderingExtendedBase = true;
         this.renderStandardBlock(par1Block, par2, par3, par4);
         this.uvRotateEast = 0;
         this.uvRotateWest = 0;
         this.uvRotateSouth = 0;
         this.uvRotateNorth = 0;
         this.uvRotateTop = 0;
         this.uvRotateBottom = 0;
         this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         PistonBlockBase.isRenderingExtendedBase = false;
      } else {
         switch (var8) {
            case 0:
               this.uvRotateEast = 3;
               this.uvRotateWest = 3;
               this.uvRotateSouth = 3;
               this.uvRotateNorth = 3;
            case 1:
            default:
               break;
            case 2:
               this.uvRotateSouth = 1;
               this.uvRotateNorth = 2;
               break;
            case 3:
               this.uvRotateSouth = 2;
               this.uvRotateNorth = 1;
               this.uvRotateTop = 3;
               this.uvRotateBottom = 3;
               break;
            case 4:
               this.uvRotateEast = 1;
               this.uvRotateWest = 2;
               this.uvRotateTop = 2;
               this.uvRotateBottom = 1;
               break;
            case 5:
               this.uvRotateEast = 2;
               this.uvRotateWest = 1;
               this.uvRotateTop = 1;
               this.uvRotateBottom = 2;
         }

         this.renderStandardBlock(par1Block, par2, par3, par4);
         this.uvRotateEast = 0;
         this.uvRotateWest = 0;
         this.uvRotateSouth = 0;
         this.uvRotateNorth = 0;
         this.uvRotateTop = 0;
         this.uvRotateBottom = 0;
      }

      return true;
   }

   private void renderPistonRodUD(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
      Icon var16 = BlockPistonBase.func_94496_b("piston_side");
      if (this.hasOverrideBlockTexture()) {
         var16 = this.overrideBlockTexture;
      }

      Tessellator var17 = Tessellator.instance;
      double var18 = var16.getMinU();
      double var20 = var16.getMinV();
      double var22 = var16.getInterpolatedU(par14);
      double var24 = var16.getInterpolatedV(4.0);
      var17.setColorOpaque_F(par13, par13, par13);
      var17.addVertexWithUV(par1, par7, par9, var22, var20);
      var17.addVertexWithUV(par1, par5, par9, var18, var20);
      var17.addVertexWithUV(par3, par5, par11, var18, var24);
      var17.addVertexWithUV(par3, par7, par11, var22, var24);
   }

   private void renderPistonRodSN(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
      Icon var16 = BlockPistonBase.func_94496_b("piston_side");
      if (this.hasOverrideBlockTexture()) {
         var16 = this.overrideBlockTexture;
      }

      Tessellator var17 = Tessellator.instance;
      double var18 = var16.getMinU();
      double var20 = var16.getMinV();
      double var22 = var16.getInterpolatedU(par14);
      double var24 = var16.getInterpolatedV(4.0);
      var17.setColorOpaque_F(par13, par13, par13);
      var17.addVertexWithUV(par1, par5, par11, var22, var20);
      var17.addVertexWithUV(par1, par5, par9, var18, var20);
      var17.addVertexWithUV(par3, par7, par9, var18, var24);
      var17.addVertexWithUV(par3, par7, par11, var22, var24);
   }

   private void renderPistonRodEW(double par1, double par3, double par5, double par7, double par9, double par11, float par13, double par14) {
      Icon var16 = BlockPistonBase.func_94496_b("piston_side");
      if (this.hasOverrideBlockTexture()) {
         var16 = this.overrideBlockTexture;
      }

      Tessellator var17 = Tessellator.instance;
      double var18 = var16.getMinU();
      double var20 = var16.getMinV();
      double var22 = var16.getInterpolatedU(par14);
      double var24 = var16.getInterpolatedV(4.0);
      var17.setColorOpaque_F(par13, par13, par13);
      var17.addVertexWithUV(par3, par5, par9, var22, var20);
      var17.addVertexWithUV(par1, par5, par9, var18, var20);
      var17.addVertexWithUV(par1, par7, par11, var18, var24);
      var17.addVertexWithUV(par3, par7, par11, var22, var24);
   }

   public void renderPistonExtensionAllFaces(Block par1Block, int par2, int par3, int par4, boolean par5) {
      this.renderAllFaces = true;
      this.renderPistonExtension(par1Block, par2, par3, par4, par5);
      this.renderAllFaces = false;
   }

   public boolean renderPistonExtension(Block par1Block, int par2, int par3, int par4, boolean par5) {
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var7 = BlockPistonExtension.getDirectionMeta(var6);
      float var11 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4);
      float var12 = par5 ? 1.0F : 0.5F;
      double var13 = par5 ? 16.0 : 8.0;
      switch (var7) {
         case 0:
            this.uvRotateEast = 3;
            this.uvRotateWest = 3;
            this.uvRotateSouth = 3;
            this.uvRotateNorth = 3;
            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 0.25, 1.0);
            this.renderStandardBlock(par1Block, par2, par3, par4);
            this.renderPistonRodUD(par2 + 0.375F, par2 + 0.625F, par3 + 0.25F, par3 + 0.25F + var12, par4 + 0.625F, par4 + 0.625F, var11 * 0.8F, var13);
            this.renderPistonRodUD(par2 + 0.625F, par2 + 0.375F, par3 + 0.25F, par3 + 0.25F + var12, par4 + 0.375F, par4 + 0.375F, var11 * 0.8F, var13);
            this.renderPistonRodUD(par2 + 0.375F, par2 + 0.375F, par3 + 0.25F, par3 + 0.25F + var12, par4 + 0.375F, par4 + 0.625F, var11 * 0.6F, var13);
            this.renderPistonRodUD(par2 + 0.625F, par2 + 0.625F, par3 + 0.25F, par3 + 0.25F + var12, par4 + 0.625F, par4 + 0.375F, var11 * 0.6F, var13);
            break;
         case 1:
            this.setRenderBounds(0.0, 0.75, 0.0, 1.0, 1.0, 1.0);
            this.renderStandardBlock(par1Block, par2, par3, par4);
            this.renderPistonRodUD(
               par2 + 0.375F, par2 + 0.625F, par3 - 0.25F + 1.0F - var12, par3 - 0.25F + 1.0F, par4 + 0.625F, par4 + 0.625F, var11 * 0.8F, var13
            );
            this.renderPistonRodUD(
               par2 + 0.625F, par2 + 0.375F, par3 - 0.25F + 1.0F - var12, par3 - 0.25F + 1.0F, par4 + 0.375F, par4 + 0.375F, var11 * 0.8F, var13
            );
            this.renderPistonRodUD(
               par2 + 0.375F, par2 + 0.375F, par3 - 0.25F + 1.0F - var12, par3 - 0.25F + 1.0F, par4 + 0.375F, par4 + 0.625F, var11 * 0.6F, var13
            );
            this.renderPistonRodUD(
               par2 + 0.625F, par2 + 0.625F, par3 - 0.25F + 1.0F - var12, par3 - 0.25F + 1.0F, par4 + 0.625F, par4 + 0.375F, var11 * 0.6F, var13
            );
            break;
         case 2:
            this.uvRotateSouth = 1;
            this.uvRotateNorth = 2;
            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 0.25);
            this.renderStandardBlock(par1Block, par2, par3, par4);
            this.renderPistonRodSN(par2 + 0.375F, par2 + 0.375F, par3 + 0.625F, par3 + 0.375F, par4 + 0.25F, par4 + 0.25F + var12, var11 * 0.6F, var13);
            this.renderPistonRodSN(par2 + 0.625F, par2 + 0.625F, par3 + 0.375F, par3 + 0.625F, par4 + 0.25F, par4 + 0.25F + var12, var11 * 0.6F, var13);
            this.renderPistonRodSN(par2 + 0.375F, par2 + 0.625F, par3 + 0.375F, par3 + 0.375F, par4 + 0.25F, par4 + 0.25F + var12, var11 * 0.5F, var13);
            this.renderPistonRodSN(par2 + 0.625F, par2 + 0.375F, par3 + 0.625F, par3 + 0.625F, par4 + 0.25F, par4 + 0.25F + var12, var11, var13);
            break;
         case 3:
            this.uvRotateSouth = 2;
            this.uvRotateNorth = 1;
            this.uvRotateTop = 3;
            this.uvRotateBottom = 3;
            this.setRenderBounds(0.0, 0.0, 0.75, 1.0, 1.0, 1.0);
            this.renderStandardBlock(par1Block, par2, par3, par4);
            this.renderPistonRodSN(
               par2 + 0.375F, par2 + 0.375F, par3 + 0.625F, par3 + 0.375F, par4 - 0.25F + 1.0F - var12, par4 - 0.25F + 1.0F, var11 * 0.6F, var13
            );
            this.renderPistonRodSN(
               par2 + 0.625F, par2 + 0.625F, par3 + 0.375F, par3 + 0.625F, par4 - 0.25F + 1.0F - var12, par4 - 0.25F + 1.0F, var11 * 0.6F, var13
            );
            this.renderPistonRodSN(
               par2 + 0.375F, par2 + 0.625F, par3 + 0.375F, par3 + 0.375F, par4 - 0.25F + 1.0F - var12, par4 - 0.25F + 1.0F, var11 * 0.5F, var13
            );
            this.renderPistonRodSN(par2 + 0.625F, par2 + 0.375F, par3 + 0.625F, par3 + 0.625F, par4 - 0.25F + 1.0F - var12, par4 - 0.25F + 1.0F, var11, var13);
            break;
         case 4:
            this.uvRotateEast = 1;
            this.uvRotateWest = 2;
            this.uvRotateTop = 2;
            this.uvRotateBottom = 1;
            this.setRenderBounds(0.0, 0.0, 0.0, 0.25, 1.0, 1.0);
            this.renderStandardBlock(par1Block, par2, par3, par4);
            this.renderPistonRodEW(par2 + 0.25F, par2 + 0.25F + var12, par3 + 0.375F, par3 + 0.375F, par4 + 0.625F, par4 + 0.375F, var11 * 0.5F, var13);
            this.renderPistonRodEW(par2 + 0.25F, par2 + 0.25F + var12, par3 + 0.625F, par3 + 0.625F, par4 + 0.375F, par4 + 0.625F, var11, var13);
            this.renderPistonRodEW(par2 + 0.25F, par2 + 0.25F + var12, par3 + 0.375F, par3 + 0.625F, par4 + 0.375F, par4 + 0.375F, var11 * 0.6F, var13);
            this.renderPistonRodEW(par2 + 0.25F, par2 + 0.25F + var12, par3 + 0.625F, par3 + 0.375F, par4 + 0.625F, par4 + 0.625F, var11 * 0.6F, var13);
            break;
         case 5:
            this.uvRotateEast = 2;
            this.uvRotateWest = 1;
            this.uvRotateTop = 1;
            this.uvRotateBottom = 2;
            this.setRenderBounds(0.75, 0.0, 0.0, 1.0, 1.0, 1.0);
            this.renderStandardBlock(par1Block, par2, par3, par4);
            this.renderPistonRodEW(
               par2 - 0.25F + 1.0F - var12, par2 - 0.25F + 1.0F, par3 + 0.375F, par3 + 0.375F, par4 + 0.625F, par4 + 0.375F, var11 * 0.5F, var13
            );
            this.renderPistonRodEW(par2 - 0.25F + 1.0F - var12, par2 - 0.25F + 1.0F, par3 + 0.625F, par3 + 0.625F, par4 + 0.375F, par4 + 0.625F, var11, var13);
            this.renderPistonRodEW(
               par2 - 0.25F + 1.0F - var12, par2 - 0.25F + 1.0F, par3 + 0.375F, par3 + 0.625F, par4 + 0.375F, par4 + 0.375F, var11 * 0.6F, var13
            );
            this.renderPistonRodEW(
               par2 - 0.25F + 1.0F - var12, par2 - 0.25F + 1.0F, par3 + 0.625F, par3 + 0.375F, par4 + 0.625F, par4 + 0.625F, var11 * 0.6F, var13
            );
      }

      this.uvRotateEast = 0;
      this.uvRotateWest = 0;
      this.uvRotateSouth = 0;
      this.uvRotateNorth = 0;
      this.uvRotateTop = 0;
      this.uvRotateBottom = 0;
      this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return true;
   }

   public boolean renderBlockLever(Block par1Block, int par2, int par3, int par4) {
      int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var6 = var5 & 7;
      boolean var7 = (var5 & 8) > 0;
      Tessellator var8 = Tessellator.instance;
      boolean var9 = this.hasOverrideBlockTexture();
      if (!var9) {
         this.setOverrideBlockTexture(this.getBlockIcon(Block.cobblestone));
      }

      float var10 = 0.25F;
      float var11 = 0.1875F;
      float var12 = 0.1875F;
      if (var6 == 5) {
         this.setRenderBounds(0.5F - var11, 0.0, 0.5F - var10, 0.5F + var11, var12, 0.5F + var10);
      } else if (var6 == 6) {
         this.setRenderBounds(0.5F - var10, 0.0, 0.5F - var11, 0.5F + var10, var12, 0.5F + var11);
      } else if (var6 == 4) {
         this.setRenderBounds(0.5F - var11, 0.5F - var10, 1.0F - var12, 0.5F + var11, 0.5F + var10, 1.0);
      } else if (var6 == 3) {
         this.setRenderBounds(0.5F - var11, 0.5F - var10, 0.0, 0.5F + var11, 0.5F + var10, var12);
      } else if (var6 == 2) {
         this.setRenderBounds(1.0F - var12, 0.5F - var10, 0.5F - var11, 1.0, 0.5F + var10, 0.5F + var11);
      } else if (var6 == 1) {
         this.setRenderBounds(0.0, 0.5F - var10, 0.5F - var11, var12, 0.5F + var10, 0.5F + var11);
      } else if (var6 == 0) {
         this.setRenderBounds(0.5F - var10, 1.0F - var12, 0.5F - var11, 0.5F + var10, 1.0, 0.5F + var11);
      } else if (var6 == 7) {
         this.setRenderBounds(0.5F - var11, 1.0F - var12, 0.5F - var10, 0.5F + var11, 1.0, 0.5F + var10);
      }

      this.renderStandardBlock(par1Block, par2, par3, par4);
      if (!var9) {
         this.clearOverrideBlockTexture();
      }

      var8.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var13 = 1.0F;
      if (Block.blocksList[par1Block.blockID].getLightValue(this.blockAccess, par2, par3, par4) > 0) {
         var13 = 1.0F;
      }

      var8.setColorOpaque_F(var13, var13, var13);
      Icon var14 = this.blockAccess == null ? this.getBlockIconFromSide(par1Block, 0) : this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
      if (this.hasOverrideBlockTexture()) {
         var14 = this.overrideBlockTexture;
      }

      double var15 = var14.getMinU();
      double var17 = var14.getMinV();
      double var19 = var14.getMaxU();
      double var21 = var14.getMaxV();
      Vec3[] var23 = new Vec3[8];
      float var24 = 0.0625F;
      float var25 = 0.0625F;
      float var26 = 0.625F;
      var23[0] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var24, 0.0, -var25);
      var23[1] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var24, 0.0, -var25);
      var23[2] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var24, 0.0, var25);
      var23[3] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var24, 0.0, var25);
      var23[4] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var24, var26, -var25);
      var23[5] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var24, var26, -var25);
      var23[6] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var24, var26, var25);
      var23[7] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var24, var26, var25);

      for (int var27 = 0; var27 < 8; var27++) {
         if (var7) {
            var23[var27].zCoord -= 0.0625;
            var23[var27].rotateAroundX((float) Math.PI * 2.0F / 9.0F);
         } else {
            var23[var27].zCoord += 0.0625;
            var23[var27].rotateAroundX((float) -Math.PI * 2.0F / 9.0F);
         }

         if (var6 == 0 || var6 == 7) {
            var23[var27].rotateAroundZ((float) Math.PI);
         }

         if (var6 == 6 || var6 == 0) {
            var23[var27].rotateAroundY((float) (Math.PI / 2));
         }

         if (var6 > 0 && var6 < 5) {
            var23[var27].yCoord -= 0.375;
            var23[var27].rotateAroundX((float) (Math.PI / 2));
            if (var6 == 4) {
               var23[var27].rotateAroundY(0.0F);
            }

            if (var6 == 3) {
               var23[var27].rotateAroundY((float) Math.PI);
            }

            if (var6 == 2) {
               var23[var27].rotateAroundY((float) (Math.PI / 2));
            }

            if (var6 == 1) {
               var23[var27].rotateAroundY((float) (-Math.PI / 2));
            }

            var23[var27].xCoord += par2 + 0.5;
            var23[var27].yCoord += par3 + 0.5F;
            var23[var27].zCoord += par4 + 0.5;
         } else if (var6 != 0 && var6 != 7) {
            var23[var27].xCoord += par2 + 0.5;
            var23[var27].yCoord += par3 + 0.125F;
            var23[var27].zCoord += par4 + 0.5;
         } else {
            var23[var27].xCoord += par2 + 0.5;
            var23[var27].yCoord += par3 + 0.875F;
            var23[var27].zCoord += par4 + 0.5;
         }
      }

      Vec3 var32 = null;
      Vec3 var28 = null;
      Vec3 var29 = null;
      Vec3 var30 = null;

      for (int var31 = 0; var31 < 6; var31++) {
         if (var31 == 0) {
            var15 = var14.getInterpolatedU(7.0);
            var17 = var14.getInterpolatedV(6.0);
            var19 = var14.getInterpolatedU(9.0);
            var21 = var14.getInterpolatedV(8.0);
         } else if (var31 == 2) {
            var15 = var14.getInterpolatedU(7.0);
            var17 = var14.getInterpolatedV(6.0);
            var19 = var14.getInterpolatedU(9.0);
            var21 = var14.getMaxV();
         }

         if (var31 == 0) {
            var32 = var23[0];
            var28 = var23[1];
            var29 = var23[2];
            var30 = var23[3];
         } else if (var31 == 1) {
            var32 = var23[7];
            var28 = var23[6];
            var29 = var23[5];
            var30 = var23[4];
         } else if (var31 == 2) {
            var32 = var23[1];
            var28 = var23[0];
            var29 = var23[4];
            var30 = var23[5];
         } else if (var31 == 3) {
            var32 = var23[2];
            var28 = var23[1];
            var29 = var23[5];
            var30 = var23[6];
         } else if (var31 == 4) {
            var32 = var23[3];
            var28 = var23[2];
            var29 = var23[6];
            var30 = var23[7];
         } else if (var31 == 5) {
            var32 = var23[0];
            var28 = var23[3];
            var29 = var23[7];
            var30 = var23[4];
         }

         var8.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var15, var21);
         var8.addVertexWithUV(var28.xCoord, var28.yCoord, var28.zCoord, var19, var21);
         var8.addVertexWithUV(var29.xCoord, var29.yCoord, var29.zCoord, var19, var17);
         var8.addVertexWithUV(var30.xCoord, var30.yCoord, var30.zCoord, var15, var17);
      }

      return true;
   }

   public boolean renderBlockTripWireSource(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var7 = var6 & 3;
      boolean var8 = (var6 & 4) == 4;
      boolean var9 = (var6 & 8) == 8;
      boolean var10 = !this.blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4);
      boolean var11 = this.hasOverrideBlockTexture();
      if (!var11) {
         this.setOverrideBlockTexture(this.getBlockIcon(Block.planks));
      }

      float var12 = 0.25F;
      float var13 = 0.125F;
      float var14 = 0.125F;
      float var15 = 0.3F - var12;
      float var16 = 0.3F + var12;
      if (var7 == 2) {
         this.setRenderBounds(0.5F - var13, var15, 1.0F - var14, 0.5F + var13, var16, 1.0);
      } else if (var7 == 0) {
         this.setRenderBounds(0.5F - var13, var15, 0.0, 0.5F + var13, var16, var14);
      } else if (var7 == 1) {
         this.setRenderBounds(1.0F - var14, var15, 0.5F - var13, 1.0, var16, 0.5F + var13);
      } else if (var7 == 3) {
         this.setRenderBounds(0.0, var15, 0.5F - var13, var14, var16, 0.5F + var13);
      }

      this.renderStandardBlock(par1Block, par2, par3, par4);
      if (!var11) {
         this.clearOverrideBlockTexture();
      }

      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var17 = 1.0F;
      if (Block.blocksList[par1Block.blockID].getLightValue(this.blockAccess, par2, par3, par4) > 0) {
         var17 = 1.0F;
      }

      var5.setColorOpaque_F(var17, var17, var17);
      Icon var18 = this.blockAccess == null ? this.getBlockIconFromSide(par1Block, 0) : this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
      if (this.hasOverrideBlockTexture()) {
         var18 = this.overrideBlockTexture;
      }

      double var19 = var18.getMinU();
      double var21 = var18.getMinV();
      double var23 = var18.getMaxU();
      double var25 = var18.getMaxV();
      Vec3[] var27 = new Vec3[8];
      float var28 = 0.046875F;
      float var29 = 0.046875F;
      float var30 = 0.3125F;
      var27[0] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var28, 0.0, -var29);
      var27[1] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var28, 0.0, -var29);
      var27[2] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var28, 0.0, var29);
      var27[3] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var28, 0.0, var29);
      var27[4] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var28, var30, -var29);
      var27[5] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var28, var30, -var29);
      var27[6] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var28, var30, var29);
      var27[7] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var28, var30, var29);

      for (int var31 = 0; var31 < 8; var31++) {
         var27[var31].zCoord += 0.0625;
         if (var9) {
            var27[var31].rotateAroundX((float) (Math.PI / 6));
            var27[var31].yCoord -= 0.4375;
         } else if (var8) {
            var27[var31].rotateAroundX(0.08726647F);
            var27[var31].yCoord -= 0.4375;
         } else {
            var27[var31].rotateAroundX((float) -Math.PI * 2.0F / 9.0F);
            var27[var31].yCoord -= 0.375;
         }

         var27[var31].rotateAroundX((float) (Math.PI / 2));
         if (var7 == 2) {
            var27[var31].rotateAroundY(0.0F);
         }

         if (var7 == 0) {
            var27[var31].rotateAroundY((float) Math.PI);
         }

         if (var7 == 1) {
            var27[var31].rotateAroundY((float) (Math.PI / 2));
         }

         if (var7 == 3) {
            var27[var31].rotateAroundY((float) (-Math.PI / 2));
         }

         var27[var31].xCoord += par2 + 0.5;
         var27[var31].yCoord += par3 + 0.3125F;
         var27[var31].zCoord += par4 + 0.5;
      }

      Vec3 var62 = null;
      Vec3 var32 = null;
      Vec3 var33 = null;
      Vec3 var34 = null;
      byte var35 = 7;
      byte var36 = 9;
      byte var37 = 9;
      byte var38 = 16;

      for (int var39 = 0; var39 < 6; var39++) {
         if (var39 == 0) {
            var62 = var27[0];
            var32 = var27[1];
            var33 = var27[2];
            var34 = var27[3];
            var19 = var18.getInterpolatedU(var35);
            var21 = var18.getInterpolatedV(var37);
            var23 = var18.getInterpolatedU(var36);
            var25 = var18.getInterpolatedV(var37 + 2);
         } else if (var39 == 1) {
            var62 = var27[7];
            var32 = var27[6];
            var33 = var27[5];
            var34 = var27[4];
         } else if (var39 == 2) {
            var62 = var27[1];
            var32 = var27[0];
            var33 = var27[4];
            var34 = var27[5];
            var19 = var18.getInterpolatedU(var35);
            var21 = var18.getInterpolatedV(var37);
            var23 = var18.getInterpolatedU(var36);
            var25 = var18.getInterpolatedV(var38);
         } else if (var39 == 3) {
            var62 = var27[2];
            var32 = var27[1];
            var33 = var27[5];
            var34 = var27[6];
         } else if (var39 == 4) {
            var62 = var27[3];
            var32 = var27[2];
            var33 = var27[6];
            var34 = var27[7];
         } else if (var39 == 5) {
            var62 = var27[0];
            var32 = var27[3];
            var33 = var27[7];
            var34 = var27[4];
         }

         var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
         var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
         var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
         var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
      }

      float var63 = 0.09375F;
      float var40 = 0.09375F;
      float var41 = 0.03125F;
      var27[0] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var63, 0.0, -var40);
      var27[1] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var63, 0.0, -var40);
      var27[2] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var63, 0.0, var40);
      var27[3] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var63, 0.0, var40);
      var27[4] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var63, var41, -var40);
      var27[5] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var63, var41, -var40);
      var27[6] = this.blockAccess.getWorldVec3Pool().getVecFromPool(var63, var41, var40);
      var27[7] = this.blockAccess.getWorldVec3Pool().getVecFromPool(-var63, var41, var40);

      for (int var42 = 0; var42 < 8; var42++) {
         var27[var42].zCoord += 0.21875;
         if (var9) {
            var27[var42].yCoord -= 0.09375;
            var27[var42].zCoord -= 0.1625;
            var27[var42].rotateAroundX(0.0F);
         } else if (var8) {
            var27[var42].yCoord += 0.015625;
            var27[var42].zCoord -= 0.171875;
            var27[var42].rotateAroundX(0.17453294F);
         } else {
            var27[var42].rotateAroundX(0.87266463F);
         }

         if (var7 == 2) {
            var27[var42].rotateAroundY(0.0F);
         }

         if (var7 == 0) {
            var27[var42].rotateAroundY((float) Math.PI);
         }

         if (var7 == 1) {
            var27[var42].rotateAroundY((float) (Math.PI / 2));
         }

         if (var7 == 3) {
            var27[var42].rotateAroundY((float) (-Math.PI / 2));
         }

         var27[var42].xCoord += par2 + 0.5;
         var27[var42].yCoord += par3 + 0.3125F;
         var27[var42].zCoord += par4 + 0.5;
      }

      byte var65 = 5;
      byte var43 = 11;
      byte var44 = 3;
      byte var45 = 9;

      for (int var46 = 0; var46 < 6; var46++) {
         if (var46 == 0) {
            var62 = var27[0];
            var32 = var27[1];
            var33 = var27[2];
            var34 = var27[3];
            var19 = var18.getInterpolatedU(var65);
            var21 = var18.getInterpolatedV(var44);
            var23 = var18.getInterpolatedU(var43);
            var25 = var18.getInterpolatedV(var45);
         } else if (var46 == 1) {
            var62 = var27[7];
            var32 = var27[6];
            var33 = var27[5];
            var34 = var27[4];
         } else if (var46 == 2) {
            var62 = var27[1];
            var32 = var27[0];
            var33 = var27[4];
            var34 = var27[5];
            var19 = var18.getInterpolatedU(var65);
            var21 = var18.getInterpolatedV(var44);
            var23 = var18.getInterpolatedU(var43);
            var25 = var18.getInterpolatedV(var44 + 2);
         } else if (var46 == 3) {
            var62 = var27[2];
            var32 = var27[1];
            var33 = var27[5];
            var34 = var27[6];
         } else if (var46 == 4) {
            var62 = var27[3];
            var32 = var27[2];
            var33 = var27[6];
            var34 = var27[7];
         } else if (var46 == 5) {
            var62 = var27[0];
            var32 = var27[3];
            var33 = var27[7];
            var34 = var27[4];
         }

         var5.addVertexWithUV(var62.xCoord, var62.yCoord, var62.zCoord, var19, var25);
         var5.addVertexWithUV(var32.xCoord, var32.yCoord, var32.zCoord, var23, var25);
         var5.addVertexWithUV(var33.xCoord, var33.yCoord, var33.zCoord, var23, var21);
         var5.addVertexWithUV(var34.xCoord, var34.yCoord, var34.zCoord, var19, var21);
      }

      if (var8) {
         double var64 = var27[0].yCoord;
         float var48 = 0.03125F;
         float var49 = 0.5F - var48 / 2.0F;
         float var50 = var49 + var48;
         Icon var51 = this.getBlockIcon(Block.tripWire);
         double var52 = var18.getMinU();
         double var54 = var18.getInterpolatedV(var8 ? 2.0 : 0.0);
         double var56 = var18.getMaxU();
         double var58 = var18.getInterpolatedV(var8 ? 4.0 : 2.0);
         double var60 = (var10 ? 3.5F : 1.5F) / 16.0;
         var17 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4) * 0.75F;
         var5.setColorOpaque_F(var17, var17, var17);
         if (var7 == 2) {
            var5.addVertexWithUV(par2 + var49, par3 + var60, par4 + 0.25, var52, var54);
            var5.addVertexWithUV(par2 + var50, par3 + var60, par4 + 0.25, var52, var58);
            var5.addVertexWithUV(par2 + var50, par3 + var60, par4, var56, var58);
            var5.addVertexWithUV(par2 + var49, par3 + var60, par4, var56, var54);
            var5.addVertexWithUV(par2 + var49, var64, par4 + 0.5, var52, var54);
            var5.addVertexWithUV(par2 + var50, var64, par4 + 0.5, var52, var58);
            var5.addVertexWithUV(par2 + var50, par3 + var60, par4 + 0.25, var56, var58);
            var5.addVertexWithUV(par2 + var49, par3 + var60, par4 + 0.25, var56, var54);
         } else if (var7 == 0) {
            var5.addVertexWithUV(par2 + var49, par3 + var60, par4 + 0.75, var52, var54);
            var5.addVertexWithUV(par2 + var50, par3 + var60, par4 + 0.75, var52, var58);
            var5.addVertexWithUV(par2 + var50, var64, par4 + 0.5, var56, var58);
            var5.addVertexWithUV(par2 + var49, var64, par4 + 0.5, var56, var54);
            var5.addVertexWithUV(par2 + var49, par3 + var60, par4 + 1, var52, var54);
            var5.addVertexWithUV(par2 + var50, par3 + var60, par4 + 1, var52, var58);
            var5.addVertexWithUV(par2 + var50, par3 + var60, par4 + 0.75, var56, var58);
            var5.addVertexWithUV(par2 + var49, par3 + var60, par4 + 0.75, var56, var54);
         } else if (var7 == 1) {
            var5.addVertexWithUV(par2, par3 + var60, par4 + var50, var52, var58);
            var5.addVertexWithUV(par2 + 0.25, par3 + var60, par4 + var50, var56, var58);
            var5.addVertexWithUV(par2 + 0.25, par3 + var60, par4 + var49, var56, var54);
            var5.addVertexWithUV(par2, par3 + var60, par4 + var49, var52, var54);
            var5.addVertexWithUV(par2 + 0.25, par3 + var60, par4 + var50, var52, var58);
            var5.addVertexWithUV(par2 + 0.5, var64, par4 + var50, var56, var58);
            var5.addVertexWithUV(par2 + 0.5, var64, par4 + var49, var56, var54);
            var5.addVertexWithUV(par2 + 0.25, par3 + var60, par4 + var49, var52, var54);
         } else {
            var5.addVertexWithUV(par2 + 0.5, var64, par4 + var50, var52, var58);
            var5.addVertexWithUV(par2 + 0.75, par3 + var60, par4 + var50, var56, var58);
            var5.addVertexWithUV(par2 + 0.75, par3 + var60, par4 + var49, var56, var54);
            var5.addVertexWithUV(par2 + 0.5, var64, par4 + var49, var52, var54);
            var5.addVertexWithUV(par2 + 0.75, par3 + var60, par4 + var50, var52, var58);
            var5.addVertexWithUV(par2 + 1, par3 + var60, par4 + var50, var56, var58);
            var5.addVertexWithUV(par2 + 1, par3 + var60, par4 + var49, var56, var54);
            var5.addVertexWithUV(par2 + 0.75, par3 + var60, par4 + var49, var52, var54);
         }
      }

      return true;
   }

   public boolean renderBlockTripWire(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      Icon var6 = this.blockAccess == null ? this.getBlockIconFromSide(par1Block, 0) : this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
      int var7 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      boolean var8 = (var7 & 4) == 4;
      boolean var9 = (var7 & 2) == 2;
      if (this.hasOverrideBlockTexture()) {
         var6 = this.overrideBlockTexture;
      }

      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var10 = par1Block.getBlockBrightness(this.blockAccess, par2, par3, par4) * 0.75F;
      var5.setColorOpaque_F(var10, var10, var10);
      double var11 = var6.getMinU();
      double var13 = var6.getInterpolatedV(var8 ? 2.0 : 0.0);
      double var15 = var6.getMaxU();
      double var17 = var6.getInterpolatedV(var8 ? 4.0 : 2.0);
      double var19 = (var9 ? 3.5F : 1.5F) / 16.0;
      boolean var21 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 1);
      boolean var22 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 3);
      boolean var23 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 2);
      boolean var24 = BlockTripWire.func_72148_a(this.blockAccess, par2, par3, par4, var7, 0);
      float var25 = 0.03125F;
      float var26 = 0.5F - var25 / 2.0F;
      float var27 = var26 + var25;
      if (!var23 && !var22 && !var24 && !var21) {
         var23 = true;
         var24 = true;
      }

      if (var23) {
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.25, var11, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.25, var11, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4, var15, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4, var15, var13);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4, var15, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4, var15, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.25, var11, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.25, var11, var13);
      }

      if (var23 || var24 && !var22 && !var21) {
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.5, var11, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.5, var11, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.25, var15, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.25, var15, var13);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.25, var15, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.25, var15, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.5, var11, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.5, var11, var13);
      }

      if (var24 || var23 && !var22 && !var21) {
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.75, var11, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.75, var11, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.5, var15, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.5, var15, var13);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.5, var15, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.5, var15, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.75, var11, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.75, var11, var13);
      }

      if (var24) {
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 1, var11, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 1, var11, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.75, var15, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.75, var15, var13);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 0.75, var15, var13);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 0.75, var15, var17);
         var5.addVertexWithUV(par2 + var27, par3 + var19, par4 + 1, var11, var17);
         var5.addVertexWithUV(par2 + var26, par3 + var19, par4 + 1, var11, var13);
      }

      if (var21) {
         var5.addVertexWithUV(par2, par3 + var19, par4 + var27, var11, var17);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2, par3 + var19, par4 + var27, var11, var17);
      }

      if (var21 || var22 && !var23 && !var24) {
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var27, var11, var17);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 0.25, par3 + var19, par4 + var27, var11, var17);
      }

      if (var22 || var21 && !var23 && !var24) {
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var27, var11, var17);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 0.5, par3 + var19, par4 + var27, var11, var17);
      }

      if (var22) {
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var27, var11, var17);
         var5.addVertexWithUV(par2 + 1, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 1, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var26, var11, var13);
         var5.addVertexWithUV(par2 + 1, par3 + var19, par4 + var26, var15, var13);
         var5.addVertexWithUV(par2 + 1, par3 + var19, par4 + var27, var15, var17);
         var5.addVertexWithUV(par2 + 0.75, par3 + var19, par4 + var27, var11, var17);
      }

      return true;
   }

   public boolean renderBlockFire(BlockFire par1BlockFire, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      Icon var6 = par1BlockFire.func_94438_c(0);
      Icon var7 = par1BlockFire.func_94438_c(1);
      Icon var8 = var6;
      if (this.hasOverrideBlockTexture()) {
         var8 = this.overrideBlockTexture;
      }

      var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      var5.setBrightness(par1BlockFire.e(this.blockAccess, par2, par3, par4));
      double var9 = var8.getMinU();
      double var11 = var8.getMinV();
      double var13 = var8.getMaxU();
      double var15 = var8.getMaxV();
      float var17 = 1.4F;
      boolean bFireRendered = false;
      if (!Block.fire.shouldFirePreferToDisplayUpwards(this.blockAccess, par2, par3, par4)) {
         float var36 = 0.2F;
         float var19 = 0.0625F;
         if ((par2 + par3 + par4 & 1) == 1) {
            var9 = var7.getMinU();
            var11 = var7.getMinV();
            var13 = var7.getMaxU();
            var15 = var7.getMaxV();
         }

         if ((par2 / 2 + par3 / 2 + par4 / 2 & 1) == 1) {
            double var20 = var13;
            var13 = var9;
            var9 = var20;
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, par2 - 1, par3, par4)) {
            var5.addVertexWithUV(par2 + var36, par3 + var17 + var19, par4 + 1, var13, var11);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 1, var13, var15);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 0, var9, var15);
            var5.addVertexWithUV(par2 + var36, par3 + var17 + var19, par4 + 0, var9, var11);
            var5.addVertexWithUV(par2 + var36, par3 + var17 + var19, par4 + 0, var9, var11);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 0, var9, var15);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 1, var13, var15);
            var5.addVertexWithUV(par2 + var36, par3 + var17 + var19, par4 + 1, var13, var11);
            bFireRendered = true;
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, par2 + 1, par3, par4)) {
            var5.addVertexWithUV(par2 + 1 - var36, par3 + var17 + var19, par4 + 0, var9, var11);
            var5.addVertexWithUV(par2 + 1 - 0, par3 + 0 + var19, par4 + 0, var9, var15);
            var5.addVertexWithUV(par2 + 1 - 0, par3 + 0 + var19, par4 + 1, var13, var15);
            var5.addVertexWithUV(par2 + 1 - var36, par3 + var17 + var19, par4 + 1, var13, var11);
            var5.addVertexWithUV(par2 + 1 - var36, par3 + var17 + var19, par4 + 1, var13, var11);
            var5.addVertexWithUV(par2 + 1 - 0, par3 + 0 + var19, par4 + 1, var13, var15);
            var5.addVertexWithUV(par2 + 1 - 0, par3 + 0 + var19, par4 + 0, var9, var15);
            var5.addVertexWithUV(par2 + 1 - var36, par3 + var17 + var19, par4 + 0, var9, var11);
            bFireRendered = true;
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 - 1)) {
            var5.addVertexWithUV(par2 + 0, par3 + var17 + var19, par4 + var36, var13, var11);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 0, var13, var15);
            var5.addVertexWithUV(par2 + 1, par3 + 0 + var19, par4 + 0, var9, var15);
            var5.addVertexWithUV(par2 + 1, par3 + var17 + var19, par4 + var36, var9, var11);
            var5.addVertexWithUV(par2 + 1, par3 + var17 + var19, par4 + var36, var9, var11);
            var5.addVertexWithUV(par2 + 1, par3 + 0 + var19, par4 + 0, var9, var15);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 0, var13, var15);
            var5.addVertexWithUV(par2 + 0, par3 + var17 + var19, par4 + var36, var13, var11);
            bFireRendered = true;
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3, par4 + 1)) {
            var5.addVertexWithUV(par2 + 1, par3 + var17 + var19, par4 + 1 - var36, var9, var11);
            var5.addVertexWithUV(par2 + 1, par3 + 0 + var19, par4 + 1 - 0, var9, var15);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 1 - 0, var13, var15);
            var5.addVertexWithUV(par2 + 0, par3 + var17 + var19, par4 + 1 - var36, var13, var11);
            var5.addVertexWithUV(par2 + 0, par3 + var17 + var19, par4 + 1 - var36, var13, var11);
            var5.addVertexWithUV(par2 + 0, par3 + 0 + var19, par4 + 1 - 0, var13, var15);
            var5.addVertexWithUV(par2 + 1, par3 + 0 + var19, par4 + 1 - 0, var9, var15);
            var5.addVertexWithUV(par2 + 1, par3 + var17 + var19, par4 + 1 - var36, var9, var11);
            bFireRendered = true;
         }

         if (Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 + 1, par4)) {
            double var20 = par2 + 0.5 + 0.5;
            double var22 = par2 + 0.5 - 0.5;
            double var24 = par4 + 0.5 + 0.5;
            double var26 = par4 + 0.5 - 0.5;
            double var28 = par2 + 0.5 - 0.5;
            double var30 = par2 + 0.5 + 0.5;
            double var32 = par4 + 0.5 - 0.5;
            double var34 = par4 + 0.5 + 0.5;
            var9 = var6.getMinU();
            var11 = var6.getMinV();
            var13 = var6.getMaxU();
            var15 = var6.getMaxV();
            par3++;
            var17 = -0.2F;
            if ((par2 + par3 + par4 & 1) == 0) {
               var5.addVertexWithUV(var28, par3 + var17, par4 + 0, var13, var11);
               var5.addVertexWithUV(var20, par3 + 0, par4 + 0, var13, var15);
               var5.addVertexWithUV(var20, par3 + 0, par4 + 1, var9, var15);
               var5.addVertexWithUV(var28, par3 + var17, par4 + 1, var9, var11);
               var9 = var7.getMinU();
               var11 = var7.getMinV();
               var13 = var7.getMaxU();
               var15 = var7.getMaxV();
               var5.addVertexWithUV(var30, par3 + var17, par4 + 1, var13, var11);
               var5.addVertexWithUV(var22, par3 + 0, par4 + 1, var13, var15);
               var5.addVertexWithUV(var22, par3 + 0, par4 + 0, var9, var15);
               var5.addVertexWithUV(var30, par3 + var17, par4 + 0, var9, var11);
            } else {
               var5.addVertexWithUV(par2 + 0, par3 + var17, var34, var13, var11);
               var5.addVertexWithUV(par2 + 0, par3 + 0, var26, var13, var15);
               var5.addVertexWithUV(par2 + 1, par3 + 0, var26, var9, var15);
               var5.addVertexWithUV(par2 + 1, par3 + var17, var34, var9, var11);
               var9 = var7.getMinU();
               var11 = var7.getMinV();
               var13 = var7.getMaxU();
               var15 = var7.getMaxV();
               var5.addVertexWithUV(par2 + 1, par3 + var17, var32, var13, var11);
               var5.addVertexWithUV(par2 + 1, par3 + 0, var24, var13, var15);
               var5.addVertexWithUV(par2 + 0, par3 + 0, var24, var9, var15);
               var5.addVertexWithUV(par2 + 0, par3 + var17, var32, var9, var11);
            }

            bFireRendered = true;
         }
      }

      if (!bFireRendered
         && (this.blockAccess.doesBlockHaveSolidTopSurface(par2, par3 - 1, par4) || Block.fire.canBlockCatchFire(this.blockAccess, par2, par3 - 1, par4))) {
         double var18 = par2 + 0.5 + 0.2;
         double var20 = par2 + 0.5 - 0.2;
         double var22 = par4 + 0.5 + 0.2;
         double var24 = par4 + 0.5 - 0.2;
         double var26 = par2 + 0.5 - 0.3;
         double var28 = par2 + 0.5 + 0.3;
         double var30 = par4 + 0.5 - 0.3;
         double var32 = par4 + 0.5 + 0.3;
         var5.addVertexWithUV(var26, par3 + var17, par4 + 1, var13, var11);
         var5.addVertexWithUV(var18, par3 + 0, par4 + 1, var13, var15);
         var5.addVertexWithUV(var18, par3 + 0, par4 + 0, var9, var15);
         var5.addVertexWithUV(var26, par3 + var17, par4 + 0, var9, var11);
         var5.addVertexWithUV(var28, par3 + var17, par4 + 0, var13, var11);
         var5.addVertexWithUV(var20, par3 + 0, par4 + 0, var13, var15);
         var5.addVertexWithUV(var20, par3 + 0, par4 + 1, var9, var15);
         var5.addVertexWithUV(var28, par3 + var17, par4 + 1, var9, var11);
         var9 = var7.getMinU();
         var11 = var7.getMinV();
         var13 = var7.getMaxU();
         var15 = var7.getMaxV();
         var5.addVertexWithUV(par2 + 1, par3 + var17, var32, var13, var11);
         var5.addVertexWithUV(par2 + 1, par3 + 0, var24, var13, var15);
         var5.addVertexWithUV(par2 + 0, par3 + 0, var24, var9, var15);
         var5.addVertexWithUV(par2 + 0, par3 + var17, var32, var9, var11);
         var5.addVertexWithUV(par2 + 0, par3 + var17, var30, var13, var11);
         var5.addVertexWithUV(par2 + 0, par3 + 0, var22, var13, var15);
         var5.addVertexWithUV(par2 + 1, par3 + 0, var22, var9, var15);
         var5.addVertexWithUV(par2 + 1, par3 + var17, var30, var9, var11);
         var18 = par2 + 0.5 - 0.5;
         var20 = par2 + 0.5 + 0.5;
         var22 = par4 + 0.5 - 0.5;
         var24 = par4 + 0.5 + 0.5;
         var26 = par2 + 0.5 - 0.4;
         var28 = par2 + 0.5 + 0.4;
         var30 = par4 + 0.5 - 0.4;
         var32 = par4 + 0.5 + 0.4;
         var5.addVertexWithUV(var26, par3 + var17, par4 + 0, var9, var11);
         var5.addVertexWithUV(var18, par3 + 0, par4 + 0, var9, var15);
         var5.addVertexWithUV(var18, par3 + 0, par4 + 1, var13, var15);
         var5.addVertexWithUV(var26, par3 + var17, par4 + 1, var13, var11);
         var5.addVertexWithUV(var28, par3 + var17, par4 + 1, var9, var11);
         var5.addVertexWithUV(var20, par3 + 0, par4 + 1, var9, var15);
         var5.addVertexWithUV(var20, par3 + 0, par4 + 0, var13, var15);
         var5.addVertexWithUV(var28, par3 + var17, par4 + 0, var13, var11);
         var9 = var6.getMinU();
         var11 = var6.getMinV();
         var13 = var6.getMaxU();
         var15 = var6.getMaxV();
         var5.addVertexWithUV(par2 + 0, par3 + var17, var32, var9, var11);
         var5.addVertexWithUV(par2 + 0, par3 + 0, var24, var9, var15);
         var5.addVertexWithUV(par2 + 1, par3 + 0, var24, var13, var15);
         var5.addVertexWithUV(par2 + 1, par3 + var17, var32, var13, var11);
         var5.addVertexWithUV(par2 + 1, par3 + var17, var30, var9, var11);
         var5.addVertexWithUV(par2 + 1, par3 + 0, var22, var9, var15);
         var5.addVertexWithUV(par2 + 0, par3 + 0, var22, var13, var15);
         var5.addVertexWithUV(par2 + 0, par3 + var17, var30, var13, var11);
      }

      return true;
   }

   public boolean renderBlockRedstoneWire(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      Icon var7 = BlockRedstoneWire.func_94409_b("redstoneDust_cross");
      Icon var8 = BlockRedstoneWire.func_94409_b("redstoneDust_line");
      Icon var9 = BlockRedstoneWire.func_94409_b("redstoneDust_cross_overlay");
      Icon var10 = BlockRedstoneWire.func_94409_b("redstoneDust_line_overlay");
      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var11 = 1.0F;
      float var13;
      float var14;
      float var15;
      if (ColorizeBlock.computeRedstoneWireColor(var6)) {
         var13 = Colorizer.setColor[0];
         var14 = Colorizer.setColor[1];
         var15 = Colorizer.setColor[2];
      } else {
         float var12 = var6 / 15.0F;
         var13 = var12 * 0.6F + 0.4F;
         if (var6 == 0) {
            var13 = 0.3F;
         }

         var14 = var12 * var12 * 0.7F - 0.5F;
         var15 = var12 * var12 * 0.6F - 0.7F;
      }

      if (var14 < 0.0F) {
         var14 = 0.0F;
      }

      if (var15 < 0.0F) {
         var15 = 0.0F;
      }

      var5.setColorOpaque_F(var13, var14, var15);
      boolean var20 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3, par4, 1)
         || !this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 - 1, par4, -1);
      boolean var21 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3, par4, 3)
         || !this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 - 1, par4, -1);
      boolean var22 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 - 1, 2)
         || !this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 - 1, -1);
      boolean var23 = BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3, par4 + 1, 0)
         || !this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 - 1, par4 + 1, -1);
      if (!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
         if (this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 - 1, par3 + 1, par4, -1)) {
            var20 = true;
         }

         if (this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2 + 1, par3 + 1, par4, -1)) {
            var21 = true;
         }

         if (this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 - 1, -1)) {
            var22 = true;
         }

         if (this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1)
            && BlockRedstoneWire.isPowerProviderOrWire(this.blockAccess, par2, par3 + 1, par4 + 1, -1)) {
            var23 = true;
         }
      }

      float var24 = par2 + 0;
      float var25 = par2 + 1;
      float var26 = par4 + 0;
      float var27 = par4 + 1;
      int var28 = 0;
      if ((var20 || var21) && !var22 && !var23) {
         var28 = 1;
      }

      if ((var22 || var23) && !var21 && !var20) {
         var28 = 2;
      }

      if (var28 == 0) {
         int var29 = 0;
         int var30 = 0;
         int var31 = 16;
         int var32 = 16;
         if (!var20) {
            var24 += 0.3125F;
         }

         if (!var20) {
            var29 += 5;
         }

         if (!var21) {
            var25 -= 0.3125F;
         }

         if (!var21) {
            var31 -= 5;
         }

         if (!var22) {
            var26 += 0.3125F;
         }

         if (!var22) {
            var30 += 5;
         }

         if (!var23) {
            var27 -= 0.3125F;
         }

         if (!var23) {
            var32 -= 5;
         }

         var5.addVertexWithUV(var25, par3 + 0.015625, var27, var7.getInterpolatedU(var31), var7.getInterpolatedV(var32));
         var5.addVertexWithUV(var25, par3 + 0.015625, var26, var7.getInterpolatedU(var31), var7.getInterpolatedV(var30));
         var5.addVertexWithUV(var24, par3 + 0.015625, var26, var7.getInterpolatedU(var29), var7.getInterpolatedV(var30));
         var5.addVertexWithUV(var24, par3 + 0.015625, var27, var7.getInterpolatedU(var29), var7.getInterpolatedV(var32));
         var5.setColorOpaque_F(var11, var11, var11);
         var5.addVertexWithUV(var25, par3 + 0.015625, var27, var9.getInterpolatedU(var31), var9.getInterpolatedV(var32));
         var5.addVertexWithUV(var25, par3 + 0.015625, var26, var9.getInterpolatedU(var31), var9.getInterpolatedV(var30));
         var5.addVertexWithUV(var24, par3 + 0.015625, var26, var9.getInterpolatedU(var29), var9.getInterpolatedV(var30));
         var5.addVertexWithUV(var24, par3 + 0.015625, var27, var9.getInterpolatedU(var29), var9.getInterpolatedV(var32));
      } else if (var28 == 1) {
         var5.addVertexWithUV(var25, par3 + 0.015625, var27, var8.getMaxU(), var8.getMaxV());
         var5.addVertexWithUV(var25, par3 + 0.015625, var26, var8.getMaxU(), var8.getMinV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var26, var8.getMinU(), var8.getMinV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var27, var8.getMinU(), var8.getMaxV());
         var5.setColorOpaque_F(var11, var11, var11);
         var5.addVertexWithUV(var25, par3 + 0.015625, var27, var10.getMaxU(), var10.getMaxV());
         var5.addVertexWithUV(var25, par3 + 0.015625, var26, var10.getMaxU(), var10.getMinV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var26, var10.getMinU(), var10.getMinV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var27, var10.getMinU(), var10.getMaxV());
      } else {
         var5.addVertexWithUV(var25, par3 + 0.015625, var27, var8.getMaxU(), var8.getMaxV());
         var5.addVertexWithUV(var25, par3 + 0.015625, var26, var8.getMinU(), var8.getMaxV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var26, var8.getMinU(), var8.getMinV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var27, var8.getMaxU(), var8.getMinV());
         var5.setColorOpaque_F(var11, var11, var11);
         var5.addVertexWithUV(var25, par3 + 0.015625, var27, var10.getMaxU(), var10.getMaxV());
         var5.addVertexWithUV(var25, par3 + 0.015625, var26, var10.getMinU(), var10.getMaxV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var26, var10.getMinU(), var10.getMinV());
         var5.addVertexWithUV(var24, par3 + 0.015625, var27, var10.getMaxU(), var10.getMinV());
      }

      if (!this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
         if (this.blockAccess.isBlockNormalCube(par2 - 1, par3, par4) && this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
            var5.addVertexWithUV(par2 + 0.015625, par3 + 1 + 0.021875F, par4 + 1, var8.getMaxU(), var8.getMinV());
            var5.addVertexWithUV(par2 + 0.015625, par3 + 0, par4 + 1, var8.getMinU(), var8.getMinV());
            var5.addVertexWithUV(par2 + 0.015625, par3 + 0, par4 + 0, var8.getMinU(), var8.getMaxV());
            var5.addVertexWithUV(par2 + 0.015625, par3 + 1 + 0.021875F, par4 + 0, var8.getMaxU(), var8.getMaxV());
            var5.setColorOpaque_F(var11, var11, var11);
            var5.addVertexWithUV(par2 + 0.015625, par3 + 1 + 0.021875F, par4 + 1, var10.getMaxU(), var10.getMinV());
            var5.addVertexWithUV(par2 + 0.015625, par3 + 0, par4 + 1, var10.getMinU(), var10.getMinV());
            var5.addVertexWithUV(par2 + 0.015625, par3 + 0, par4 + 0, var10.getMinU(), var10.getMaxV());
            var5.addVertexWithUV(par2 + 0.015625, par3 + 1 + 0.021875F, par4 + 0, var10.getMaxU(), var10.getMaxV());
         }

         if (this.blockAccess.isBlockNormalCube(par2 + 1, par3, par4) && this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 0, par4 + 1, var8.getMinU(), var8.getMaxV());
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 1 + 0.021875F, par4 + 1, var8.getMaxU(), var8.getMaxV());
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 1 + 0.021875F, par4 + 0, var8.getMaxU(), var8.getMinV());
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 0, par4 + 0, var8.getMinU(), var8.getMinV());
            var5.setColorOpaque_F(var11, var11, var11);
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 0, par4 + 1, var10.getMinU(), var10.getMaxV());
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 1 + 0.021875F, par4 + 1, var10.getMaxU(), var10.getMaxV());
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 1 + 0.021875F, par4 + 0, var10.getMaxU(), var10.getMinV());
            var5.addVertexWithUV(par2 + 1 - 0.015625, par3 + 0, par4 + 0, var10.getMinU(), var10.getMinV());
         }

         if (this.blockAccess.isBlockNormalCube(par2, par3, par4 - 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
            var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + 0.015625, var8.getMinU(), var8.getMaxV());
            var5.addVertexWithUV(par2 + 1, par3 + 1 + 0.021875F, par4 + 0.015625, var8.getMaxU(), var8.getMaxV());
            var5.addVertexWithUV(par2 + 0, par3 + 1 + 0.021875F, par4 + 0.015625, var8.getMaxU(), var8.getMinV());
            var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + 0.015625, var8.getMinU(), var8.getMinV());
            var5.setColorOpaque_F(var11, var11, var11);
            var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + 0.015625, var10.getMinU(), var10.getMaxV());
            var5.addVertexWithUV(par2 + 1, par3 + 1 + 0.021875F, par4 + 0.015625, var10.getMaxU(), var10.getMaxV());
            var5.addVertexWithUV(par2 + 0, par3 + 1 + 0.021875F, par4 + 0.015625, var10.getMaxU(), var10.getMinV());
            var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + 0.015625, var10.getMinU(), var10.getMinV());
         }

         if (this.blockAccess.isBlockNormalCube(par2, par3, par4 + 1) && this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1) == Block.redstoneWire.blockID) {
            var5.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
            var5.addVertexWithUV(par2 + 1, par3 + 1 + 0.021875F, par4 + 1 - 0.015625, var8.getMaxU(), var8.getMinV());
            var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + 1 - 0.015625, var8.getMinU(), var8.getMinV());
            var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + 1 - 0.015625, var8.getMinU(), var8.getMaxV());
            var5.addVertexWithUV(par2 + 0, par3 + 1 + 0.021875F, par4 + 1 - 0.015625, var8.getMaxU(), var8.getMaxV());
            var5.setColorOpaque_F(var11, var11, var11);
            var5.addVertexWithUV(par2 + 1, par3 + 1 + 0.021875F, par4 + 1 - 0.015625, var10.getMaxU(), var10.getMinV());
            var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + 1 - 0.015625, var10.getMinU(), var10.getMinV());
            var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + 1 - 0.015625, var10.getMinU(), var10.getMaxV());
            var5.addVertexWithUV(par2 + 0, par3 + 1 + 0.021875F, par4 + 1 - 0.015625, var10.getMaxU(), var10.getMaxV());
         }
      }

      return true;
   }

   public boolean renderBlockMinecartTrack(BlockRailBase par1BlockRailBase, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      Icon var7 = this.blockAccess == null
         ? this.getBlockIconFromSideAndMetadata(par1BlockRailBase, 0, var6)
         : this.getBlockIcon(par1BlockRailBase, this.blockAccess, par2, par3, par4, 0);
      if (this.hasOverrideBlockTexture()) {
         var7 = this.overrideBlockTexture;
      }

      if (par1BlockRailBase.isPowered()) {
         var6 &= 7;
      }

      var5.setBrightness(par1BlockRailBase.e(this.blockAccess, par2, par3, par4));
      var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      double var8 = var7.getMinU();
      double var10 = var7.getMinV();
      double var12 = var7.getMaxU();
      double var14 = var7.getMaxV();
      double var16 = 0.0625;
      double var18 = par2 + 1;
      double var20 = par2 + 1;
      double var22 = par2 + 0;
      double var24 = par2 + 0;
      double var26 = par4 + 0;
      double var28 = par4 + 1;
      double var30 = par4 + 1;
      double var32 = par4 + 0;
      double var34 = par3 + var16;
      double var36 = par3 + var16;
      double var38 = par3 + var16;
      double var40 = par3 + var16;
      if (var6 == 1 || var6 == 2 || var6 == 3 || var6 == 7) {
         var18 = var24 = par2 + 1;
         var20 = var22 = par2 + 0;
         var26 = var28 = par4 + 1;
         var30 = var32 = par4 + 0;
      } else if (var6 == 8) {
         var18 = var20 = par2 + 0;
         var22 = var24 = par2 + 1;
         var26 = var32 = par4 + 1;
         var28 = var30 = par4 + 0;
      } else if (var6 == 9) {
         var18 = var24 = par2 + 0;
         var20 = var22 = par2 + 1;
         var26 = var28 = par4 + 0;
         var30 = var32 = par4 + 1;
      }

      if (var6 == 2 || var6 == 4) {
         var34++;
         var40++;
      } else if (var6 == 3 || var6 == 5) {
         var36++;
         var38++;
      }

      var5.addVertexWithUV(var18, var34, var26, var12, var10);
      var5.addVertexWithUV(var20, var36, var28, var12, var14);
      var5.addVertexWithUV(var22, var38, var30, var8, var14);
      var5.addVertexWithUV(var24, var40, var32, var8, var10);
      var5.addVertexWithUV(var24, var40, var32, var8, var10);
      var5.addVertexWithUV(var22, var38, var30, var8, var14);
      var5.addVertexWithUV(var20, var36, var28, var12, var14);
      var5.addVertexWithUV(var18, var34, var26, var12, var10);
      return true;
   }

   public boolean renderBlockLadder(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      Icon var6 = this.blockAccess == null ? this.getBlockIconFromSide(par1Block, 0) : this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
      if (this.hasOverrideBlockTexture()) {
         var6 = this.overrideBlockTexture;
      }

      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var7 = 1.0F;
      var5.setColorOpaque_F(var7, var7, var7);
      double var20 = var6.getMinU();
      double var9 = var6.getMinV();
      double var11 = var6.getMaxU();
      double var13 = var6.getMaxV();
      int var15 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      double var16 = 0.0;
      double var18 = 0.05F;
      if (var15 == 5) {
         var5.addVertexWithUV(par2 + var18, par3 + 1 + var16, par4 + 1 + var16, var20, var9);
         var5.addVertexWithUV(par2 + var18, par3 + 0 - var16, par4 + 1 + var16, var20, var13);
         var5.addVertexWithUV(par2 + var18, par3 + 0 - var16, par4 + 0 - var16, var11, var13);
         var5.addVertexWithUV(par2 + var18, par3 + 1 + var16, par4 + 0 - var16, var11, var9);
      }

      if (var15 == 4) {
         var5.addVertexWithUV(par2 + 1 - var18, par3 + 0 - var16, par4 + 1 + var16, var11, var13);
         var5.addVertexWithUV(par2 + 1 - var18, par3 + 1 + var16, par4 + 1 + var16, var11, var9);
         var5.addVertexWithUV(par2 + 1 - var18, par3 + 1 + var16, par4 + 0 - var16, var20, var9);
         var5.addVertexWithUV(par2 + 1 - var18, par3 + 0 - var16, par4 + 0 - var16, var20, var13);
      }

      if (var15 == 3) {
         var5.addVertexWithUV(par2 + 1 + var16, par3 + 0 - var16, par4 + var18, var11, var13);
         var5.addVertexWithUV(par2 + 1 + var16, par3 + 1 + var16, par4 + var18, var11, var9);
         var5.addVertexWithUV(par2 + 0 - var16, par3 + 1 + var16, par4 + var18, var20, var9);
         var5.addVertexWithUV(par2 + 0 - var16, par3 + 0 - var16, par4 + var18, var20, var13);
      }

      if (var15 == 2) {
         var5.addVertexWithUV(par2 + 1 + var16, par3 + 1 + var16, par4 + 1 - var18, var20, var9);
         var5.addVertexWithUV(par2 + 1 + var16, par3 + 0 - var16, par4 + 1 - var18, var20, var13);
         var5.addVertexWithUV(par2 + 0 - var16, par3 + 0 - var16, par4 + 1 - var18, var11, var13);
         var5.addVertexWithUV(par2 + 0 - var16, par3 + 1 + var16, par4 + 1 - var18, var11, var9);
      }

      return true;
   }

   public boolean renderBlockVine(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      Icon var6 = this.blockAccess == null ? this.getBlockIconFromSide(par1Block, 0) : this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0);
      if (this.hasOverrideBlockTexture()) {
         var6 = this.overrideBlockTexture;
      }

      float var7 = 1.0F;
      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      int var8 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
      float var9 = (var8 >> 16 & 0xFF) / 255.0F;
      float var10 = (var8 >> 8 & 0xFF) / 255.0F;
      float var11 = (var8 & 0xFF) / 255.0F;
      var5.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
      double var19 = var6.getMinU();
      double var20 = var6.getMinV();
      double var12 = var6.getMaxU();
      double var14 = var6.getMaxV();
      double var16 = 0.05F;
      int var18 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      if ((var18 & 2) != 0) {
         var5.addVertexWithUV(par2 + var16, par3 + 1, par4 + 1, var19, var20);
         var5.addVertexWithUV(par2 + var16, par3 + 0, par4 + 1, var19, var14);
         var5.addVertexWithUV(par2 + var16, par3 + 0, par4 + 0, var12, var14);
         var5.addVertexWithUV(par2 + var16, par3 + 1, par4 + 0, var12, var20);
         var5.addVertexWithUV(par2 + var16, par3 + 1, par4 + 0, var12, var20);
         var5.addVertexWithUV(par2 + var16, par3 + 0, par4 + 0, var12, var14);
         var5.addVertexWithUV(par2 + var16, par3 + 0, par4 + 1, var19, var14);
         var5.addVertexWithUV(par2 + var16, par3 + 1, par4 + 1, var19, var20);
      }

      if ((var18 & 8) != 0) {
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 0, par4 + 1, var12, var14);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 1, par4 + 1, var12, var20);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 1, par4 + 0, var19, var20);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 0, par4 + 0, var19, var14);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 0, par4 + 0, var19, var14);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 1, par4 + 0, var19, var20);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 1, par4 + 1, var12, var20);
         var5.addVertexWithUV(par2 + 1 - var16, par3 + 0, par4 + 1, var12, var14);
      }

      if ((var18 & 4) != 0) {
         var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + var16, var12, var14);
         var5.addVertexWithUV(par2 + 1, par3 + 1, par4 + var16, var12, var20);
         var5.addVertexWithUV(par2 + 0, par3 + 1, par4 + var16, var19, var20);
         var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + var16, var19, var14);
         var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + var16, var19, var14);
         var5.addVertexWithUV(par2 + 0, par3 + 1, par4 + var16, var19, var20);
         var5.addVertexWithUV(par2 + 1, par3 + 1, par4 + var16, var12, var20);
         var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + var16, var12, var14);
      }

      if ((var18 & 1) != 0) {
         var5.addVertexWithUV(par2 + 1, par3 + 1, par4 + 1 - var16, var19, var20);
         var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + 1 - var16, var19, var14);
         var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + 1 - var16, var12, var14);
         var5.addVertexWithUV(par2 + 0, par3 + 1, par4 + 1 - var16, var12, var20);
         var5.addVertexWithUV(par2 + 0, par3 + 1, par4 + 1 - var16, var12, var20);
         var5.addVertexWithUV(par2 + 0, par3 + 0, par4 + 1 - var16, var12, var14);
         var5.addVertexWithUV(par2 + 1, par3 + 0, par4 + 1 - var16, var19, var14);
         var5.addVertexWithUV(par2 + 1, par3 + 1, par4 + 1 - var16, var19, var20);
      }

      if (this.blockAccess.isBlockNormalCube(par2, par3 + 1, par4)) {
         var5.addVertexWithUV(par2 + 1, par3 + 1 - var16, par4 + 0, var19, var20);
         var5.addVertexWithUV(par2 + 1, par3 + 1 - var16, par4 + 1, var19, var14);
         var5.addVertexWithUV(par2 + 0, par3 + 1 - var16, par4 + 1, var12, var14);
         var5.addVertexWithUV(par2 + 0, par3 + 1 - var16, par4 + 0, var12, var20);
      }

      return true;
   }

   public boolean renderBlockPane(BlockPane par1BlockPane, int par2, int par3, int par4) {
      int var5 = this.blockAccess.getHeight();
      Tessellator var6 = Tessellator.instance;
      var6.setBrightness(par1BlockPane.e(this.blockAccess, par2, par3, par4));
      float var7 = 1.0F;
      int var8 = par1BlockPane.c(this.blockAccess, par2, par3, par4);
      float var9 = (var8 >> 16 & 0xFF) / 255.0F;
      float var10 = (var8 >> 8 & 0xFF) / 255.0F;
      float var11 = (var8 & 0xFF) / 255.0F;
      var6.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
      Icon var64;
      Icon var65;
      if (this.hasOverrideBlockTexture()) {
         var64 = this.overrideBlockTexture;
         var65 = this.overrideBlockTexture;
      } else {
         int var66 = this.blockAccess.getBlockMetadata(par2, par3, par4);
         var64 = this.blockAccess == null
            ? this.getBlockIconFromSideAndMetadata(par1BlockPane, 0, var66)
            : this.getBlockIcon(par1BlockPane, this.blockAccess, par2, par3, par4, 0);
         var65 = par1BlockPane.getSideTextureIndex();
      }

      int var66 = var64.getOriginX();
      int var15 = var64.getOriginY();
      double var16 = var64.getMinU();
      double var18 = var64.getInterpolatedU(8.0);
      double var20 = var64.getMaxU();
      double var22 = var64.getMinV();
      double var24 = var64.getMaxV();
      int var26 = var65.getOriginX();
      int var27 = var65.getOriginY();
      double var28 = var65.getInterpolatedU(7.0);
      double var30 = var65.getInterpolatedU(9.0);
      double var32 = var65.getMinV();
      double var34 = var65.getInterpolatedV(8.0);
      double var36 = var65.getMaxV();
      double var38 = par2;
      double var40 = par2 + 0.5;
      double var42 = par2 + 1;
      double var44 = par4;
      double var46 = par4 + 0.5;
      double var48 = par4 + 1;
      double var50 = par2 + 0.5 - 0.0625;
      double var52 = par2 + 0.5 + 0.0625;
      double var54 = par4 + 0.5 - 0.0625;
      double var56 = par4 + 0.5 + 0.0625;
      boolean var58 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2, par3, par4 - 1));
      boolean var59 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2, par3, par4 + 1));
      boolean var60 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2 - 1, par3, par4));
      boolean var61 = par1BlockPane.canThisPaneConnectToThisBlockID(this.blockAccess.getBlockId(par2 + 1, par3, par4));
      boolean var62 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 + 1, par4, 1);
      boolean var63 = par1BlockPane.shouldSideBeRendered(this.blockAccess, par2, par3 - 1, par4, 0);
      GlassPaneRenderer.renderThin(this, par1BlockPane, var64, par2, par3, par4, var58, var59, var60, var61);
      if ((!var60 || !var61) && (var60 || var61 || var58 || var59)) {
         if (var60 && !var61) {
            if (!GlassPaneRenderer.skipPaneRendering) {
               var6.addVertexWithUV(var38, par3 + 1, var46, var16, var22);
               var6.addVertexWithUV(var38, par3 + 0, var46, var16, var24);
               var6.addVertexWithUV(var40, par3 + 0, var46, var18, var24);
               var6.addVertexWithUV(var40, par3 + 1, var46, var18, var22);
               var6.addVertexWithUV(var40, par3 + 1, var46, var16, var22);
               var6.addVertexWithUV(var40, par3 + 0, var46, var16, var24);
               var6.addVertexWithUV(var38, par3 + 0, var46, var18, var24);
               var6.addVertexWithUV(var38, par3 + 1, var46, var18, var22);
            }

            if (!var59 && !var58) {
               var6.addVertexWithUV(var40, par3 + 1, var56, var28, var32);
               var6.addVertexWithUV(var40, par3 + 0, var56, var28, var36);
               var6.addVertexWithUV(var40, par3 + 0, var54, var30, var36);
               var6.addVertexWithUV(var40, par3 + 1, var54, var30, var32);
               var6.addVertexWithUV(var40, par3 + 1, var54, var28, var32);
               var6.addVertexWithUV(var40, par3 + 0, var54, var28, var36);
               var6.addVertexWithUV(var40, par3 + 0, var56, var30, var36);
               var6.addVertexWithUV(var40, par3 + 1, var56, var30, var32);
            }

            if (var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) {
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var36);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var36);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var56, var30, var36);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var54, var28, var36);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var34);
            }

            if ((var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4)) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var38, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var36);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var36);
               var6.addVertexWithUV(var38, par3 - 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var38, par3 - 0.01, var56, var30, var36);
               var6.addVertexWithUV(var38, par3 - 0.01, var54, var28, var36);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var34);
            }
         } else if (!var60 && var61) {
            if (!GlassPaneRenderer.skipPaneRendering) {
               var6.addVertexWithUV(var40, par3 + 1, var46, var18, var22);
               var6.addVertexWithUV(var40, par3 + 0, var46, var18, var24);
               var6.addVertexWithUV(var42, par3 + 0, var46, var20, var24);
               var6.addVertexWithUV(var42, par3 + 1, var46, var20, var22);
               var6.addVertexWithUV(var42, par3 + 1, var46, var18, var22);
               var6.addVertexWithUV(var42, par3 + 0, var46, var18, var24);
               var6.addVertexWithUV(var40, par3 + 0, var46, var20, var24);
               var6.addVertexWithUV(var40, par3 + 1, var46, var20, var22);
            }

            if (!var59 && !var58) {
               var6.addVertexWithUV(var40, par3 + 1, var54, var28, var32);
               var6.addVertexWithUV(var40, par3 + 0, var54, var28, var36);
               var6.addVertexWithUV(var40, par3 + 0, var56, var30, var36);
               var6.addVertexWithUV(var40, par3 + 1, var56, var30, var32);
               var6.addVertexWithUV(var40, par3 + 1, var56, var28, var32);
               var6.addVertexWithUV(var40, par3 + 0, var56, var28, var36);
               var6.addVertexWithUV(var40, par3 + 0, var54, var30, var36);
               var6.addVertexWithUV(var40, par3 + 1, var54, var30, var32);
            }

            if ((var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4)) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var32);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var32);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var56, var30, var32);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var34);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var54, var28, var32);
            }

            if ((var63 || par3 > 1 && this.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4)) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var32);
               var6.addVertexWithUV(var42, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var42, par3 - 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var32);
               var6.addVertexWithUV(var42, par3 - 0.01, var56, var30, var32);
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var34);
               var6.addVertexWithUV(var42, par3 - 0.01, var54, var28, var32);
            }
         }
      } else {
         if (!GlassPaneRenderer.skipPaneRendering) {
            var6.addVertexWithUV(var38, par3 + 1, var46, var16, var22);
            var6.addVertexWithUV(var38, par3 + 0, var46, var16, var24);
            var6.addVertexWithUV(var42, par3 + 0, var46, var20, var24);
            var6.addVertexWithUV(var42, par3 + 1, var46, var20, var22);
            var6.addVertexWithUV(var42, par3 + 1, var46, var16, var22);
            var6.addVertexWithUV(var42, par3 + 0, var46, var16, var24);
            var6.addVertexWithUV(var38, par3 + 0, var46, var20, var24);
            var6.addVertexWithUV(var38, par3 + 1, var46, var20, var22);
         }

         if (var62) {
            if (!GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var56, var30, var36);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var56, var30, var32);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var54, var28, var32);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var54, var28, var36);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var56, var30, var36);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var56, var30, var32);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var54, var28, var32);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var54, var28, var36);
            }
         } else {
            if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 - 1, par3 + 1, par4) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var36);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var36);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var56, var30, var36);
               var6.addVertexWithUV(var38, par3 + 1 + 0.01, var54, var28, var36);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var34);
            }

            if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2 + 1, par3 + 1, par4) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var32);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var32);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var56, var30, var32);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 + 1 + 0.01, var54, var28, var34);
               var6.addVertexWithUV(var42, par3 + 1 + 0.01, var54, var28, var32);
            }
         }

         if (var63) {
            if (!GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var38, par3 - 0.01, var56, var30, var36);
               var6.addVertexWithUV(var42, par3 - 0.01, var56, var30, var32);
               var6.addVertexWithUV(var42, par3 - 0.01, var54, var28, var32);
               var6.addVertexWithUV(var38, par3 - 0.01, var54, var28, var36);
               var6.addVertexWithUV(var42, par3 - 0.01, var56, var30, var36);
               var6.addVertexWithUV(var38, par3 - 0.01, var56, var30, var32);
               var6.addVertexWithUV(var38, par3 - 0.01, var54, var28, var32);
               var6.addVertexWithUV(var42, par3 - 0.01, var54, var28, var36);
            }
         } else {
            if (par3 > 1 && this.blockAccess.isAirBlock(par2 - 1, par3 - 1, par4) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var38, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var36);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var36);
               var6.addVertexWithUV(var38, par3 - 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var38, par3 - 0.01, var56, var30, var36);
               var6.addVertexWithUV(var38, par3 - 0.01, var54, var28, var36);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var34);
            }

            if (par3 > 1 && this.blockAccess.isAirBlock(par2 + 1, par3 - 1, par4) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var32);
               var6.addVertexWithUV(var42, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var42, par3 - 0.01, var54, var28, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var32);
               var6.addVertexWithUV(var42, par3 - 0.01, var56, var30, var32);
               var6.addVertexWithUV(var40, par3 - 0.01, var56, var30, var34);
               var6.addVertexWithUV(var40, par3 - 0.01, var54, var28, var34);
               var6.addVertexWithUV(var42, par3 - 0.01, var54, var28, var32);
            }
         }
      }

      if ((!var58 || !var59) && (var60 || var61 || var58 || var59)) {
         if (var58 && !var59) {
            if (!GlassPaneRenderer.skipPaneRendering) {
               var6.addVertexWithUV(var40, par3 + 1, var44, var16, var22);
               var6.addVertexWithUV(var40, par3 + 0, var44, var16, var24);
               var6.addVertexWithUV(var40, par3 + 0, var46, var18, var24);
               var6.addVertexWithUV(var40, par3 + 1, var46, var18, var22);
               var6.addVertexWithUV(var40, par3 + 1, var46, var16, var22);
               var6.addVertexWithUV(var40, par3 + 0, var46, var16, var24);
               var6.addVertexWithUV(var40, par3 + 0, var44, var18, var24);
               var6.addVertexWithUV(var40, par3 + 1, var44, var18, var22);
            }

            if (!var61 && !var60) {
               var6.addVertexWithUV(var50, par3 + 1, var46, var28, var32);
               var6.addVertexWithUV(var50, par3 + 0, var46, var28, var36);
               var6.addVertexWithUV(var52, par3 + 0, var46, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1, var46, var30, var32);
               var6.addVertexWithUV(var52, par3 + 1, var46, var28, var32);
               var6.addVertexWithUV(var52, par3 + 0, var46, var28, var36);
               var6.addVertexWithUV(var50, par3 + 0, var46, var30, var36);
               var6.addVertexWithUV(var50, par3 + 1, var46, var30, var32);
            }

            if ((var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1)) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var44, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var30, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var28, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var44, var28, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var44, var30, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var44, var28, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var28, var32);
            }

            if ((var63 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1)) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var50, par3 - 0.005, var44, var30, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var30, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var28, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var44, var28, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var30, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var44, var30, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var44, var28, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var28, var32);
            }
         } else if (!var58 && var59) {
            if (!GlassPaneRenderer.skipPaneRendering) {
               var6.addVertexWithUV(var40, par3 + 1, var46, var18, var22);
               var6.addVertexWithUV(var40, par3 + 0, var46, var18, var24);
               var6.addVertexWithUV(var40, par3 + 0, var48, var20, var24);
               var6.addVertexWithUV(var40, par3 + 1, var48, var20, var22);
               var6.addVertexWithUV(var40, par3 + 1, var48, var18, var22);
               var6.addVertexWithUV(var40, par3 + 0, var48, var18, var24);
               var6.addVertexWithUV(var40, par3 + 0, var46, var20, var24);
               var6.addVertexWithUV(var40, par3 + 1, var46, var20, var22);
            }

            if (!var61 && !var60) {
               var6.addVertexWithUV(var52, par3 + 1, var46, var28, var32);
               var6.addVertexWithUV(var52, par3 + 0, var46, var28, var36);
               var6.addVertexWithUV(var50, par3 + 0, var46, var30, var36);
               var6.addVertexWithUV(var50, par3 + 1, var46, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1, var46, var28, var32);
               var6.addVertexWithUV(var50, par3 + 0, var46, var28, var36);
               var6.addVertexWithUV(var52, par3 + 0, var46, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1, var46, var30, var32);
            }

            if ((var62 || par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1)) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var28, var34);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var48, var28, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var48, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var30, var34);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var48, var28, var34);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var28, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var48, var30, var34);
            }

            if ((var63 || par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1)) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var28, var34);
               var6.addVertexWithUV(var50, par3 - 0.005, var48, var28, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var48, var30, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var30, var34);
               var6.addVertexWithUV(var50, par3 - 0.005, var48, var28, var34);
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var28, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var30, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var48, var30, var34);
            }
         }
      } else {
         if (!GlassPaneRenderer.skipPaneRendering) {
            var6.addVertexWithUV(var40, par3 + 1, var48, var16, var22);
            var6.addVertexWithUV(var40, par3 + 0, var48, var16, var24);
            var6.addVertexWithUV(var40, par3 + 0, var44, var20, var24);
            var6.addVertexWithUV(var40, par3 + 1, var44, var20, var22);
            var6.addVertexWithUV(var40, par3 + 1, var44, var16, var22);
            var6.addVertexWithUV(var40, par3 + 0, var44, var16, var24);
            var6.addVertexWithUV(var40, par3 + 0, var48, var20, var24);
            var6.addVertexWithUV(var40, par3 + 1, var48, var20, var22);
         }

         if (var62) {
            if (!GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var48, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var44, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var44, var28, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var48, var28, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var44, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var48, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var48, var28, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var44, var28, var36);
            }
         } else {
            if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 - 1) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var44, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var30, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var28, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var44, var28, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var30, var32);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var44, var30, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var44, var28, var34);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var28, var32);
            }

            if (par3 < var5 - 1 && this.blockAccess.isAirBlock(par2, par3 + 1, par4 + 1) && !GlassPaneRenderer.skipTopEdgeRendering) {
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var28, var34);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var48, var28, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var48, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var30, var34);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var48, var28, var34);
               var6.addVertexWithUV(var50, par3 + 1 + 0.005, var46, var28, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var46, var30, var36);
               var6.addVertexWithUV(var52, par3 + 1 + 0.005, var48, var30, var34);
            }
         }

         if (var63) {
            if (!GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var52, par3 - 0.005, var48, var30, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var44, var30, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var44, var28, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var48, var28, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var44, var30, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var48, var30, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var48, var28, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var44, var28, var36);
            }
         } else {
            if (par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 - 1) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var50, par3 - 0.005, var44, var30, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var30, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var28, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var44, var28, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var30, var32);
               var6.addVertexWithUV(var50, par3 - 0.005, var44, var30, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var44, var28, var34);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var28, var32);
            }

            if (par3 > 1 && this.blockAccess.isAirBlock(par2, par3 - 1, par4 + 1) && !GlassPaneRenderer.skipBottomEdgeRendering) {
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var28, var34);
               var6.addVertexWithUV(var50, par3 - 0.005, var48, var28, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var48, var30, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var30, var34);
               var6.addVertexWithUV(var50, par3 - 0.005, var48, var28, var34);
               var6.addVertexWithUV(var50, par3 - 0.005, var46, var28, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var46, var30, var36);
               var6.addVertexWithUV(var52, par3 - 0.005, var48, var30, var34);
            }
         }
      }

      return true;
   }

   public boolean renderCrossedSquares(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var6 = 1.0F;
      int var7 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
      float var8 = (var7 >> 16 & 0xFF) / 255.0F;
      float var9 = (var7 >> 8 & 0xFF) / 255.0F;
      float var10 = (var7 & 0xFF) / 255.0F;
      var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
      double var19 = par2;
      double var20 = par3;
      double var15 = par4;
      if (par1Block == Block.tallGrass) {
         long var17 = par2 * 3129871 ^ par4 * 116129781L ^ par3;
         var17 = var17 * var17 * 42317861L + var17 * 11L;
         var19 += ((float)(var17 >> 16 & 15L) / 15.0F - 0.5) * 0.5;
         var20 += ((float)(var17 >> 20 & 15L) / 15.0F - 1.0) * 0.2;
         var15 += ((float)(var17 >> 24 & 15L) / 15.0F - 0.5) * 0.5;
      }

      this.drawCrossedSquares(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), var19, var20, var15, 1.0F);
      return true;
   }

   public boolean renderBlockStem(Block par1Block, int par2, int par3, int par4) {
      BlockStem var5 = (BlockStem)par1Block;
      Tessellator var6 = Tessellator.instance;
      var6.setBrightness(var5.e(this.blockAccess, par2, par3, par4));
      float var7 = 1.0F;
      int var8 = var5.colorMultiplier(this.blockAccess, par2, par3, par4);
      float var9 = (var8 >> 16 & 0xFF) / 255.0F;
      float var10 = (var8 >> 8 & 0xFF) / 255.0F;
      float var11 = (var8 & 0xFF) / 255.0F;
      var6.setColorOpaque_F(var7 * var9, var7 * var10, var7 * var11);
      var5.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
      int var15 = var5.getState(this.blockAccess, par2, par3, par4);
      Block blockBelow = Block.blocksList[this.blockAccess.getBlockId(par2, par3 - 1, par4)];
      float fVerticalOffset = 0.0F;
      if (blockBelow != null) {
         fVerticalOffset = blockBelow.groundCoverRestingOnVisualOffset(this.blockAccess, par2, par3 - 1, par4);
      }

      if (var15 < 0) {
         this.renderBlockStemSmall(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), this.renderMaxY, par2, par3 + fVerticalOffset, par4);
      } else {
         this.renderBlockStemSmall(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), 0.5, par2, par3 + fVerticalOffset, par4);
         this.renderBlockStemBig(var5, this.blockAccess.getBlockMetadata(par2, par3, par4), var15, this.renderMaxY, par2, par3 + fVerticalOffset, par4);
      }

      return true;
   }

   public boolean renderBlockCrops(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      Block blockBelow = Block.blocksList[this.blockAccess.getBlockId(par2, par3 - 1, par4)];
      double dVerticalOffset = 0.0;
      if (blockBelow != null) {
         dVerticalOffset = blockBelow.groundCoverRestingOnVisualOffset(this.blockAccess, par2, par3 - 1, par4);
      }

      this.renderBlockCropsImpl(par1Block, this.blockAccess.getBlockMetadata(par2, par3, par4), par2, par3 + dVerticalOffset, par4);
      return true;
   }

   public void renderTorchAtAngle(Block par1Block, double par2, double par4, double par6, double par8, double par10, int par12) {
      Tessellator var13 = Tessellator.instance;
      Icon var14 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par12);
      if (this.hasOverrideBlockTexture()) {
         var14 = this.overrideBlockTexture;
      }

      double var15 = var14.getMinU();
      double var17 = var14.getMinV();
      double var19 = var14.getMaxU();
      double var21 = var14.getMaxV();
      double var23 = var14.getInterpolatedU(7.0);
      double var25 = var14.getInterpolatedV(6.0);
      double var27 = var14.getInterpolatedU(9.0);
      double var29 = var14.getInterpolatedV(8.0);
      double var31 = var14.getInterpolatedU(7.0);
      double var33 = var14.getInterpolatedV(13.0);
      double var35 = var14.getInterpolatedU(9.0);
      double var37 = var14.getInterpolatedV(15.0);
      par2 += 0.5;
      par6 += 0.5;
      double var39 = par2 - 0.5;
      double var41 = par2 + 0.5;
      double var43 = par6 - 0.5;
      double var45 = par6 + 0.5;
      double var47 = 0.0625;
      double var49 = 0.625;
      var13.addVertexWithUV(par2 + par8 * (1.0 - var49) - var47, par4 + var49, par6 + par10 * (1.0 - var49) - var47, var23, var25);
      var13.addVertexWithUV(par2 + par8 * (1.0 - var49) - var47, par4 + var49, par6 + par10 * (1.0 - var49) + var47, var23, var29);
      var13.addVertexWithUV(par2 + par8 * (1.0 - var49) + var47, par4 + var49, par6 + par10 * (1.0 - var49) + var47, var27, var29);
      var13.addVertexWithUV(par2 + par8 * (1.0 - var49) + var47, par4 + var49, par6 + par10 * (1.0 - var49) - var47, var27, var25);
      var13.addVertexWithUV(par2 + var47 + par8, par4, par6 - var47 + par10, var35, var33);
      var13.addVertexWithUV(par2 + var47 + par8, par4, par6 + var47 + par10, var35, var37);
      var13.addVertexWithUV(par2 - var47 + par8, par4, par6 + var47 + par10, var31, var37);
      var13.addVertexWithUV(par2 - var47 + par8, par4, par6 - var47 + par10, var31, var33);
      var13.addVertexWithUV(par2 - var47, par4 + 1.0, var43, var15, var17);
      var13.addVertexWithUV(par2 - var47 + par8, par4 + 0.0, var43 + par10, var15, var21);
      var13.addVertexWithUV(par2 - var47 + par8, par4 + 0.0, var45 + par10, var19, var21);
      var13.addVertexWithUV(par2 - var47, par4 + 1.0, var45, var19, var17);
      var13.addVertexWithUV(par2 + var47, par4 + 1.0, var45, var15, var17);
      var13.addVertexWithUV(par2 + par8 + var47, par4 + 0.0, var45 + par10, var15, var21);
      var13.addVertexWithUV(par2 + par8 + var47, par4 + 0.0, var43 + par10, var19, var21);
      var13.addVertexWithUV(par2 + var47, par4 + 1.0, var43, var19, var17);
      var13.addVertexWithUV(var39, par4 + 1.0, par6 + var47, var15, var17);
      var13.addVertexWithUV(var39 + par8, par4 + 0.0, par6 + var47 + par10, var15, var21);
      var13.addVertexWithUV(var41 + par8, par4 + 0.0, par6 + var47 + par10, var19, var21);
      var13.addVertexWithUV(var41, par4 + 1.0, par6 + var47, var19, var17);
      var13.addVertexWithUV(var41, par4 + 1.0, par6 - var47, var15, var17);
      var13.addVertexWithUV(var41 + par8, par4 + 0.0, par6 - var47 + par10, var15, var21);
      var13.addVertexWithUV(var39 + par8, par4 + 0.0, par6 - var47 + par10, var19, var21);
      var13.addVertexWithUV(var39, par4 + 1.0, par6 - var47, var19, var17);
   }

   public void drawCrossedSquares(Block par1Block, int par2, double par3, double par5, double par7, float par9) {
      Tessellator var10 = Tessellator.instance;
      Icon var11 = this.blockAccess == null
         ? this.getBlockIconFromSideAndMetadata(par1Block, 0, par2)
         : this.getBlockIcon(par1Block, this.blockAccess, (int)Math.round(par3), (int)Math.round(par5), (int)Math.round(par7), -1);
      if (this.hasOverrideBlockTexture()) {
         var11 = this.overrideBlockTexture;
      }

      double var12 = var11.getMinU();
      double var14 = var11.getMinV();
      double var16 = var11.getMaxU();
      double var18 = var11.getMaxV();
      double var20 = 0.45 * par9;
      double var22 = par3 + 0.5 - var20;
      double var24 = par3 + 0.5 + var20;
      double var26 = par7 + 0.5 - var20;
      double var28 = par7 + 0.5 + var20;
      var10.addVertexWithUV(var22, par5 + par9, var26, var12, var14);
      var10.addVertexWithUV(var22, par5 + 0.0, var26, var12, var18);
      var10.addVertexWithUV(var24, par5 + 0.0, var28, var16, var18);
      var10.addVertexWithUV(var24, par5 + par9, var28, var16, var14);
      var10.addVertexWithUV(var24, par5 + par9, var28, var12, var14);
      var10.addVertexWithUV(var24, par5 + 0.0, var28, var12, var18);
      var10.addVertexWithUV(var22, par5 + 0.0, var26, var16, var18);
      var10.addVertexWithUV(var22, par5 + par9, var26, var16, var14);
      var10.addVertexWithUV(var22, par5 + par9, var28, var12, var14);
      var10.addVertexWithUV(var22, par5 + 0.0, var28, var12, var18);
      var10.addVertexWithUV(var24, par5 + 0.0, var26, var16, var18);
      var10.addVertexWithUV(var24, par5 + par9, var26, var16, var14);
      var10.addVertexWithUV(var24, par5 + par9, var26, var12, var14);
      var10.addVertexWithUV(var24, par5 + 0.0, var26, var12, var18);
      var10.addVertexWithUV(var22, par5 + 0.0, var28, var16, var18);
      var10.addVertexWithUV(var22, par5 + par9, var28, var16, var14);
   }

   public void renderBlockStemSmall(Block par1Block, int par2, double par3, double par5, double par7, double par9) {
      Tessellator var11 = Tessellator.instance;
      Icon var12 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par2);
      if (this.hasOverrideBlockTexture()) {
         var12 = this.overrideBlockTexture;
      }

      double var13 = var12.getMinU();
      double var15 = var12.getMinV();
      double var17 = var12.getMaxU();
      double var19 = var12.getInterpolatedV(par3 * 16.0);
      double var21 = par5 + 0.5 - 0.45F;
      double var23 = par5 + 0.5 + 0.45F;
      double var25 = par9 + 0.5 - 0.45F;
      double var27 = par9 + 0.5 + 0.45F;
      var11.addVertexWithUV(var21, par7 + par3, var25, var13, var15);
      var11.addVertexWithUV(var21, par7 + 0.0, var25, var13, var19);
      var11.addVertexWithUV(var23, par7 + 0.0, var27, var17, var19);
      var11.addVertexWithUV(var23, par7 + par3, var27, var17, var15);
      var11.addVertexWithUV(var23, par7 + par3, var27, var13, var15);
      var11.addVertexWithUV(var23, par7 + 0.0, var27, var13, var19);
      var11.addVertexWithUV(var21, par7 + 0.0, var25, var17, var19);
      var11.addVertexWithUV(var21, par7 + par3, var25, var17, var15);
      var11.addVertexWithUV(var21, par7 + par3, var27, var13, var15);
      var11.addVertexWithUV(var21, par7 + 0.0, var27, var13, var19);
      var11.addVertexWithUV(var23, par7 + 0.0, var25, var17, var19);
      var11.addVertexWithUV(var23, par7 + par3, var25, var17, var15);
      var11.addVertexWithUV(var23, par7 + par3, var25, var13, var15);
      var11.addVertexWithUV(var23, par7 + 0.0, var25, var13, var19);
      var11.addVertexWithUV(var21, par7 + 0.0, var27, var17, var19);
      var11.addVertexWithUV(var21, par7 + par3, var27, var17, var15);
   }

   public boolean renderBlockLilyPad(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      Icon var6 = this.blockAccess == null ? this.getBlockIconFromSide(par1Block, 1) : this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1);
      if (this.hasOverrideBlockTexture()) {
         var6 = this.overrideBlockTexture;
      }

      float var7 = 0.015625F;
      double var8 = var6.getMinU();
      double var10 = var6.getMinV();
      double var12 = var6.getMaxU();
      double var14 = var6.getMaxV();
      long var16 = par2 * 3129871 ^ par4 * 116129781L ^ par3;
      var16 = var16 * var16 * 42317861L + var16 * 11L;
      int var18 = (int)(var16 >> 16 & 3L);
      var5.setBrightness(par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4));
      float var19 = par2 + 0.5F;
      float var20 = par4 + 0.5F;
      float var21 = (var18 & 1) * 0.5F * (1 - var18 / 2 % 2 * 2);
      float var22 = (var18 + 1 & 1) * 0.5F * (1 - (var18 + 1) / 2 % 2 * 2);
      var5.setColorOpaque_I(par1Block.getBlockColor());
      var5.addVertexWithUV(var19 + var21 - var22, par3 + var7, var20 + var21 + var22, var8, var10);
      var5.addVertexWithUV(var19 + var21 + var22, par3 + var7, var20 - var21 + var22, var12, var10);
      var5.addVertexWithUV(var19 - var21 + var22, par3 + var7, var20 - var21 - var22, var12, var14);
      var5.addVertexWithUV(var19 - var21 - var22, par3 + var7, var20 + var21 - var22, var8, var14);
      var5.setColorOpaque_I((par1Block.getBlockColor() & 16711422) >> 1);
      var5.addVertexWithUV(var19 - var21 - var22, par3 + var7, var20 + var21 - var22, var8, var14);
      var5.addVertexWithUV(var19 - var21 + var22, par3 + var7, var20 - var21 - var22, var12, var14);
      var5.addVertexWithUV(var19 + var21 + var22, par3 + var7, var20 - var21 + var22, var12, var10);
      var5.addVertexWithUV(var19 + var21 - var22, par3 + var7, var20 + var21 + var22, var8, var10);
      return true;
   }

   public void renderBlockStemBig(BlockStem par1BlockStem, int par2, int par3, double par4, double par6, double par8, double par10) {
      Tessellator var12 = Tessellator.instance;
      Icon var13 = par1BlockStem.func_94368_p();
      if (this.hasOverrideBlockTexture()) {
         var13 = this.overrideBlockTexture;
      }

      double var14 = var13.getMinU();
      double var16 = var13.getMinV();
      double var18 = var13.getMaxU();
      double var20 = var13.getMaxV();
      double var22 = par6 + 0.5 - 0.5;
      double var24 = par6 + 0.5 + 0.5;
      double var26 = par10 + 0.5 - 0.5;
      double var28 = par10 + 0.5 + 0.5;
      double var30 = par6 + 0.5;
      double var32 = par10 + 0.5;
      if ((par3 + 1) / 2 % 2 == 1) {
         double var34 = var18;
         var18 = var14;
         var14 = var34;
      }

      if (par3 < 2) {
         var12.addVertexWithUV(var22, par8 + par4, var32, var14, var16);
         var12.addVertexWithUV(var22, par8 + 0.0, var32, var14, var20);
         var12.addVertexWithUV(var24, par8 + 0.0, var32, var18, var20);
         var12.addVertexWithUV(var24, par8 + par4, var32, var18, var16);
         var12.addVertexWithUV(var24, par8 + par4, var32, var18, var16);
         var12.addVertexWithUV(var24, par8 + 0.0, var32, var18, var20);
         var12.addVertexWithUV(var22, par8 + 0.0, var32, var14, var20);
         var12.addVertexWithUV(var22, par8 + par4, var32, var14, var16);
      } else {
         var12.addVertexWithUV(var30, par8 + par4, var28, var14, var16);
         var12.addVertexWithUV(var30, par8 + 0.0, var28, var14, var20);
         var12.addVertexWithUV(var30, par8 + 0.0, var26, var18, var20);
         var12.addVertexWithUV(var30, par8 + par4, var26, var18, var16);
         var12.addVertexWithUV(var30, par8 + par4, var26, var18, var16);
         var12.addVertexWithUV(var30, par8 + 0.0, var26, var18, var20);
         var12.addVertexWithUV(var30, par8 + 0.0, var28, var14, var20);
         var12.addVertexWithUV(var30, par8 + par4, var28, var14, var16);
      }
   }

   public void renderBlockCropsImpl(Block par1Block, int par2, double par3, double par5, double par7) {
      Tessellator var9 = Tessellator.instance;
      Icon var10 = this.getBlockIconFromSideAndMetadata(par1Block, 0, par2);
      if (this.hasOverrideBlockTexture()) {
         var10 = this.overrideBlockTexture;
      }

      double var11 = var10.getMinU();
      double var13 = var10.getMinV();
      double var15 = var10.getMaxU();
      double var17 = var10.getMaxV();
      double var19 = par3 + 0.5 - 0.25;
      double var21 = par3 + 0.5 + 0.25;
      double var23 = par7 + 0.5 - 0.5;
      double var25 = par7 + 0.5 + 0.5;
      var9.addVertexWithUV(var19, par5 + 1.0, var23, var11, var13);
      var9.addVertexWithUV(var19, par5 + 0.0, var23, var11, var17);
      var9.addVertexWithUV(var19, par5 + 0.0, var25, var15, var17);
      var9.addVertexWithUV(var19, par5 + 1.0, var25, var15, var13);
      var9.addVertexWithUV(var19, par5 + 1.0, var25, var11, var13);
      var9.addVertexWithUV(var19, par5 + 0.0, var25, var11, var17);
      var9.addVertexWithUV(var19, par5 + 0.0, var23, var15, var17);
      var9.addVertexWithUV(var19, par5 + 1.0, var23, var15, var13);
      var9.addVertexWithUV(var21, par5 + 1.0, var25, var11, var13);
      var9.addVertexWithUV(var21, par5 + 0.0, var25, var11, var17);
      var9.addVertexWithUV(var21, par5 + 0.0, var23, var15, var17);
      var9.addVertexWithUV(var21, par5 + 1.0, var23, var15, var13);
      var9.addVertexWithUV(var21, par5 + 1.0, var23, var11, var13);
      var9.addVertexWithUV(var21, par5 + 0.0, var23, var11, var17);
      var9.addVertexWithUV(var21, par5 + 0.0, var25, var15, var17);
      var9.addVertexWithUV(var21, par5 + 1.0, var25, var15, var13);
      var19 = par3 + 0.5 - 0.5;
      var21 = par3 + 0.5 + 0.5;
      var23 = par7 + 0.5 - 0.25;
      var25 = par7 + 0.5 + 0.25;
      var9.addVertexWithUV(var19, par5 + 1.0, var23, var11, var13);
      var9.addVertexWithUV(var19, par5 + 0.0, var23, var11, var17);
      var9.addVertexWithUV(var21, par5 + 0.0, var23, var15, var17);
      var9.addVertexWithUV(var21, par5 + 1.0, var23, var15, var13);
      var9.addVertexWithUV(var21, par5 + 1.0, var23, var11, var13);
      var9.addVertexWithUV(var21, par5 + 0.0, var23, var11, var17);
      var9.addVertexWithUV(var19, par5 + 0.0, var23, var15, var17);
      var9.addVertexWithUV(var19, par5 + 1.0, var23, var15, var13);
      var9.addVertexWithUV(var21, par5 + 1.0, var25, var11, var13);
      var9.addVertexWithUV(var21, par5 + 0.0, var25, var11, var17);
      var9.addVertexWithUV(var19, par5 + 0.0, var25, var15, var17);
      var9.addVertexWithUV(var19, par5 + 1.0, var25, var15, var13);
      var9.addVertexWithUV(var19, par5 + 1.0, var25, var11, var13);
      var9.addVertexWithUV(var19, par5 + 0.0, var25, var11, var17);
      var9.addVertexWithUV(var21, par5 + 0.0, var25, var15, var17);
      var9.addVertexWithUV(var21, par5 + 1.0, var25, var15, var13);
   }

   private float getFluidHeight(int par1, int par2, int par3, Material par4Material) {
      int var5 = 0;
      float var6 = 0.0F;

      for (int var7 = 0; var7 < 4; var7++) {
         int var8 = par1 - (var7 & 1);
         int var10 = par3 - (var7 >> 1 & 1);
         if (this.blockAccess.getBlockMaterial(var8, par2 + 1, var10) == par4Material) {
            return 1.0F;
         }

         Material var11 = this.blockAccess.getBlockMaterial(var8, par2, var10);
         if (var11 == par4Material) {
            int var12 = this.blockAccess.getBlockMetadata(var8, par2, var10);
            if (var12 >= 8 || var12 == 0) {
               var6 += BlockFluid.getFluidHeightPercent(var12) * 10.0F;
               var5 += 10;
            }

            var6 += BlockFluid.getFluidHeightPercent(var12);
            var5++;
         } else if (!var11.isSolid()) {
            var6++;
            var5++;
         }
      }

      return 1.0F - var6 / var5;
   }

   public void renderBlockSandFalling(Block par1Block, World par2World, int par3, int par4, int par5, int par6) {
      float var7 = 0.5F;
      float var8 = 1.0F;
      float var9 = 0.8F;
      float var10 = 0.6F;
      Tessellator var11 = Tessellator.instance;
      var11.startDrawingQuads();
      var11.setBrightness(par1Block.getMixedBrightnessForBlock(par2World, par3, par4, par5));
      float var12 = 1.0F;
      float var13 = 1.0F;
      if (var13 < var12) {
         var13 = var12;
      }

      if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, par2World, par3, par4, par5, 0)) {
         var11.setColorOpaque_F(var7 * var13, var7 * var13, var7 * var13);
      }

      this.renderFaceYNeg(par1Block, -0.5, -0.5, -0.5, this.getBlockIconFromSideAndMetadata(par1Block, 0, par6));
      var13 = 1.0F;
      if (var13 < var12) {
         var13 = var12;
      }

      if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, par2World, par3, par4, par5, 1)) {
         var11.setColorOpaque_F(var8 * var13, var8 * var13, var8 * var13);
      }

      this.renderFaceYPos(par1Block, -0.5, -0.5, -0.5, this.getBlockIconFromSideAndMetadata(par1Block, 1, par6));
      var13 = 1.0F;
      if (var13 < var12) {
         var13 = var12;
      }

      if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, par2World, par3, par4, par5, 2)) {
         var11.setColorOpaque_F(var9 * var13, var9 * var13, var9 * var13);
      }

      this.renderFaceZNeg(par1Block, -0.5, -0.5, -0.5, this.getBlockIconFromSideAndMetadata(par1Block, 2, par6));
      var13 = 1.0F;
      if (var13 < var12) {
         var13 = var12;
      }

      if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, par2World, par3, par4, par5, 2)) {
         var11.setColorOpaque_F(var9 * var13, var9 * var13, var9 * var13);
      }

      this.renderFaceZPos(par1Block, -0.5, -0.5, -0.5, this.getBlockIconFromSideAndMetadata(par1Block, 3, par6));
      var13 = 1.0F;
      if (var13 < var12) {
         var13 = var12;
      }

      if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, par2World, par3, par4, par5, 2)) {
         var11.setColorOpaque_F(var9 * var13, var9 * var13, var9 * var13);
      }

      this.renderFaceXNeg(par1Block, -0.5, -0.5, -0.5, this.getBlockIconFromSideAndMetadata(par1Block, 4, par6));
      var13 = 1.0F;
      if (var13 < var12) {
         var13 = var12;
      }

      if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, par2World, par3, par4, par5, 2)) {
         var11.setColorOpaque_F(var9 * var13, var9 * var13, var9 * var13);
      }

      this.renderFaceXPos(par1Block, -0.5, -0.5, -0.5, this.getBlockIconFromSideAndMetadata(par1Block, 5, par6));
      var11.draw();
   }

   public boolean renderStandardBlock(Block block, int x, int y, int z) {
      int color = block.colorMultiplier(this.blockAccess, x, y, z);
      float red = (color >> 16 & 0xFF) / 255.0F;
      float green = (color >> 8 & 0xFF) / 255.0F;
      float blue = (color & 0xFF) / 255.0F;
      RenderBlocksUtils.setupColorMultiplier(block, this.blockAccess, x, y, z, this.hasOverrideBlockTexture(), red, green, blue);
      if (!Minecraft.isAmbientOcclusionEnabled() || Block.blocksList[block.blockID].getLightValue(this.blockAccess, x, y, z) != 0) {
         return this.renderStandardBlockWithColorMultiplier(block, x, y, z, red, green, blue);
      } else {
         return this.partialRenderBounds
            ? this.func_102027_b(block, x, y, z, red, green, blue)
            : this.renderStandardBlockWithAmbientOcclusion(block, x, y, z, red, green, blue);
      }
   }

   public boolean renderBlockLog(Block par1Block, int par2, int par3, int par4) {
      int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var6 = var5 & 12;
      if (var6 == 4) {
         this.uvRotateEast = 1;
         this.uvRotateWest = 1;
         this.uvRotateTop = 1;
         this.uvRotateBottom = 1;
      } else if (var6 == 8) {
         this.uvRotateSouth = 1;
         this.uvRotateNorth = 1;
      }

      boolean var7 = this.renderStandardBlock(par1Block, par2, par3, par4);
      this.uvRotateSouth = 0;
      this.uvRotateEast = 0;
      this.uvRotateWest = 0;
      this.uvRotateNorth = 0;
      this.uvRotateTop = 0;
      this.uvRotateBottom = 0;
      return var7;
   }

   public boolean renderBlockQuartz(Block par1Block, int par2, int par3, int par4) {
      int var5 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      if (var5 == 3) {
         this.uvRotateEast = 1;
         this.uvRotateWest = 1;
         this.uvRotateTop = 1;
         this.uvRotateBottom = 1;
      } else if (var5 == 4) {
         this.uvRotateSouth = 1;
         this.uvRotateNorth = 1;
      }

      boolean var6 = this.renderStandardBlock(par1Block, par2, par3, par4);
      this.uvRotateSouth = 0;
      this.uvRotateEast = 0;
      this.uvRotateWest = 0;
      this.uvRotateNorth = 0;
      this.uvRotateTop = 0;
      this.uvRotateBottom = 0;
      return var6;
   }

   public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
      this.enableAO = true;
      boolean var8 = false;
      float var9 = 0.0F;
      float var10 = 0.0F;
      float var11 = 0.0F;
      float var12 = 0.0F;
      boolean var13 = true;
      int var14 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
      Tessellator var15 = Tessellator.instance;
      var15.setBrightness(983055);
      if (this.hasOverrideBlockTexture()) {
         var13 = false;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
         if (this.renderMinY <= 0.0) {
            par3--;
         }

         this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         boolean var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
         boolean var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
         boolean var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
         boolean var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];
         if (!var19 && !var17) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
            this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
         }

         if (!var18 && !var17) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
            this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
         }

         if (!var19 && !var16) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
            this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
         }

         if (!var18 && !var16) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
            this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
         }

         if (this.renderMinY <= 0.0) {
            par3++;
         }

         int var20 = var14;
         if (this.renderMinY <= 0.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4)) {
            var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + var21) / 4.0F;
         var12 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
         var11 = (var21 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
         var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + var21 + this.aoLightValueScratchYZNN) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, var20);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, var20);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, var20);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, var20);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 0, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(0)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.5F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6
                  * RenderPass.getAOBaseMultiplier(0.5F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7
                  * RenderPass.getAOBaseMultiplier(0.5F);
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.5F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.5F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.5F);
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
         if (this.renderMaxY >= 1.0) {
            par3++;
         }

         this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         boolean var16x = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
         boolean var17x = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
         boolean var18x = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
         boolean var19x = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
         if (!var19x && !var17x) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
            this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
         }

         if (!var19x && !var16x) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
            this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
         }

         if (!var18x && !var17x) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
            this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
         }

         if (!var18x && !var16x) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
            this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
         }

         if (this.renderMaxY >= 1.0) {
            par3--;
         }

         int var20x = var14;
         if (this.renderMaxY >= 1.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4)) {
            var20x = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + var21) / 4.0F;
         var9 = (this.aoLightValueScratchYZPP + var21 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
         var10 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
         var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, var20x);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, var20x);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, var20x);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20x);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 1, var9, var10, var11, var12)) {
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
         if (this.renderMinZ <= 0.0) {
            par4--;
         }

         this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         boolean var16xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
         boolean var17xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
         boolean var18xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
         boolean var19xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];
         if (!var17xx && !var19xx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
            this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
         }

         if (!var17xx && !var18xx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
            this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
         }

         if (!var16xx && !var19xx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
            this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
         }

         if (!var16xx && !var18xx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
            this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
         }

         if (this.renderMinZ <= 0.0) {
            par4++;
         }

         int var20xx = var14;
         if (this.renderMinZ <= 0.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1)) {
            var20xx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         var9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
         var10 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
         var11 = (this.aoLightValueScratchYZNN + var21 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
         var12 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + var21) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20xx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, var20xx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, var20xx);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, var20xx);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 2, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(2)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.8F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6
                  * RenderPass.getAOBaseMultiplier(0.8F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7
                  * RenderPass.getAOBaseMultiplier(0.8F);
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.8F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.8F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.8F);
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
         this.renderFaceZNeg(par1Block, par2, par3, par4, var22);
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
         if (this.renderMaxZ >= 1.0) {
            par4++;
         }

         this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         boolean var16xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
         boolean var17xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
         boolean var18xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
         boolean var19xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
         if (!var17xxx && !var19xxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
            this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
         }

         if (!var17xxx && !var18xxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
            this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
         }

         if (!var16xxx && !var19xxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
            this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
         }

         if (!var16xxx && !var18xxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
            this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
         }

         if (this.renderMaxZ >= 1.0) {
            par4--;
         }

         int var20xxx = var14;
         if (this.renderMaxZ >= 1.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1)) {
            var20xxx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         var9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + var21 + this.aoLightValueScratchYZPP) / 4.0F;
         var12 = (var21 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         var11 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
         var10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + var21) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, var20xxx);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, var20xxx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20xxx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, var20xxx);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 3, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(3)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.8F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6
                  * RenderPass.getAOBaseMultiplier(0.8F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7
                  * RenderPass.getAOBaseMultiplier(0.8F);
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.8F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.8F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.8F);
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
         this.renderFaceZPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
         if (this.renderMinX <= 0.0) {
            par2--;
         }

         this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         boolean var16xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
         boolean var17xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
         boolean var18xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
         boolean var19xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
         if (!var18xxxx && !var17xxxx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
            this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
         }

         if (!var19xxxx && !var17xxxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
            this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
         }

         if (!var18xxxx && !var16xxxx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
            this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
         }

         if (!var19xxxx && !var16xxxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
            this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
         }

         if (this.renderMinX <= 0.0) {
            par2++;
         }

         int var20xxxx = var14;
         if (this.renderMinX <= 0.0 || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4)) {
            var20xxxx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         var12 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + var21 + this.aoLightValueScratchXZNP) / 4.0F;
         var9 = (var21 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
         var10 = (this.aoLightValueScratchXZNN + var21 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
         var11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + var21) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, var20xxxx);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, var20xxxx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, var20xxxx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, var20xxxx);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 4, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(4)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.6F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6
                  * RenderPass.getAOBaseMultiplier(0.6F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7
                  * RenderPass.getAOBaseMultiplier(0.6F);
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.6F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.6F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.6F);
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
         this.renderFaceXNeg(par1Block, par2, par3, par4, var22);
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
         if (this.renderMaxX >= 1.0) {
            par2++;
         }

         this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         boolean var16xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
         boolean var17xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
         boolean var18xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
         boolean var19xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
         if (!var17xxxxx && !var19xxxxx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
            this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
         }

         if (!var17xxxxx && !var18xxxxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
            this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
         }

         if (!var16xxxxx && !var19xxxxx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
            this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
         }

         if (!var16xxxxx && !var18xxxxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
            this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
         }

         if (this.renderMaxX >= 1.0) {
            par2--;
         }

         int var20xxxxx = var14;
         if (this.renderMaxX >= 1.0 || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4)) {
            var20xxxxx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         var9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + var21 + this.aoLightValueScratchXZPP) / 4.0F;
         var10 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + var21) / 4.0F;
         var11 = (this.aoLightValueScratchXZPN + var21 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
         var12 = (var21 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20xxxxx);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, var20xxxxx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, var20xxxxx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, var20xxxxx);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 5, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(5)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * RenderPass.getAOBaseMultiplier(0.6F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6
                  * RenderPass.getAOBaseMultiplier(0.6F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7
                  * RenderPass.getAOBaseMultiplier(0.6F);
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = RenderPass.getAOBaseMultiplier(0.6F);
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = RenderPass.getAOBaseMultiplier(0.6F);
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = RenderPass.getAOBaseMultiplier(0.6F);
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var22 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
         this.renderFaceXPos(par1Block, par2, par3, par4, var22);
         var8 = true;
      }

      this.enableAO = false;
      return var8;
   }

   public boolean func_102027_b(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
      this.enableAO = true;
      boolean var8 = false;
      float var9 = 0.0F;
      float var10 = 0.0F;
      float var11 = 0.0F;
      float var12 = 0.0F;
      boolean var13 = true;
      int var14 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
      Tessellator var15 = Tessellator.instance;
      var15.setBrightness(983055);
      if (this.hasOverrideBlockTexture()) {
         var13 = false;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
         if (this.renderMinY <= 0.0) {
            par3--;
         }

         this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         boolean var16 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
         boolean var17 = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
         boolean var18 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
         boolean var19 = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];
         if (!var19 && !var17) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
            this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
         }

         if (!var18 && !var17) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
            this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
         }

         if (!var19 && !var16) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
            this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
         }

         if (!var18 && !var16) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
            this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
         }

         if (this.renderMinY <= 0.0) {
            par3++;
         }

         int var20 = var14;
         if (this.renderMinY <= 0.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4)) {
            var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + var21) / 4.0F;
         var12 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
         var11 = (var21 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
         var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + var21 + this.aoLightValueScratchYZNN) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, var20);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, var20);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, var20);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, var20);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 0, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(0)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.5F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.5F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.5F;
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
         if (this.renderMaxY >= 1.0) {
            par3++;
         }

         this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         boolean var16x = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
         boolean var17x = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
         boolean var18x = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
         boolean var19x = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
         if (!var19x && !var17x) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
            this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
         }

         if (!var19x && !var16x) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
            this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
         }

         if (!var18x && !var17x) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
            this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
         }

         if (!var18x && !var16x) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
            this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
         }

         if (this.renderMaxY >= 1.0) {
            par3--;
         }

         int var20x = var14;
         if (this.renderMaxY >= 1.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4)) {
            var20x = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + var21) / 4.0F;
         var9 = (this.aoLightValueScratchYZPP + var21 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
         var10 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
         var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, var20x);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, var20x);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, var20x);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20x);
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 1, var9, var10, var11, var12)) {
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
         if (this.renderMinZ <= 0.0) {
            par4--;
         }

         this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         boolean var16xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
         boolean var17xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
         boolean var18xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
         boolean var19xx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];
         if (!var17xx && !var19xx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
            this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
         }

         if (!var17xx && !var18xx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
            this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
         }

         if (!var16xx && !var19xx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
            this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
         }

         if (!var16xx && !var18xx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
            this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
         }

         if (this.renderMinZ <= 0.0) {
            par4++;
         }

         int var20xx = var14;
         if (this.renderMinZ <= 0.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1)) {
            var20xx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         float var22 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + var21 + this.aoLightValueScratchYZPN) / 4.0F;
         float var23 = (var21 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
         float var24 = (this.aoLightValueScratchYZNN + var21 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
         float var25 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + var21) / 4.0F;
         var9 = (float)(
            var22 * this.renderMaxY * (1.0 - this.renderMinX)
               + var23 * this.renderMinY * this.renderMinX
               + var24 * (1.0 - this.renderMaxY) * this.renderMinX
               + var25 * (1.0 - this.renderMaxY) * (1.0 - this.renderMinX)
         );
         var10 = (float)(
            var22 * this.renderMaxY * (1.0 - this.renderMaxX)
               + var23 * this.renderMaxY * this.renderMaxX
               + var24 * (1.0 - this.renderMaxY) * this.renderMaxX
               + var25 * (1.0 - this.renderMaxY) * (1.0 - this.renderMaxX)
         );
         var11 = (float)(
            var22 * this.renderMinY * (1.0 - this.renderMaxX)
               + var23 * this.renderMinY * this.renderMaxX
               + var24 * (1.0 - this.renderMinY) * this.renderMaxX
               + var25 * (1.0 - this.renderMinY) * (1.0 - this.renderMaxX)
         );
         var12 = (float)(
            var22 * this.renderMinY * (1.0 - this.renderMinX)
               + var23 * this.renderMinY * this.renderMinX
               + var24 * (1.0 - this.renderMinY) * this.renderMinX
               + var25 * (1.0 - this.renderMinY) * (1.0 - this.renderMinX)
         );
         int var26 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var20xx);
         int var27 = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, var20xx);
         int var28 = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, var20xx);
         int var29 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, var20xx);
         this.brightnessTopLeft = this.mixAoBrightness(
            var26,
            var27,
            var28,
            var29,
            this.renderMaxY * (1.0 - this.renderMinX),
            this.renderMaxY * this.renderMinX,
            (1.0 - this.renderMaxY) * this.renderMinX,
            (1.0 - this.renderMaxY) * (1.0 - this.renderMinX)
         );
         this.brightnessBottomLeft = this.mixAoBrightness(
            var26,
            var27,
            var28,
            var29,
            this.renderMaxY * (1.0 - this.renderMaxX),
            this.renderMaxY * this.renderMaxX,
            (1.0 - this.renderMaxY) * this.renderMaxX,
            (1.0 - this.renderMaxY) * (1.0 - this.renderMaxX)
         );
         this.brightnessBottomRight = this.mixAoBrightness(
            var26,
            var27,
            var28,
            var29,
            this.renderMinY * (1.0 - this.renderMaxX),
            this.renderMinY * this.renderMaxX,
            (1.0 - this.renderMinY) * this.renderMaxX,
            (1.0 - this.renderMinY) * (1.0 - this.renderMaxX)
         );
         this.brightnessTopRight = this.mixAoBrightness(
            var26,
            var27,
            var28,
            var29,
            this.renderMinY * (1.0 - this.renderMinX),
            this.renderMinY * this.renderMinX,
            (1.0 - this.renderMinY) * this.renderMinX,
            (1.0 - this.renderMinY) * (1.0 - this.renderMinX)
         );
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 2, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(2)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
         this.renderFaceZNeg(par1Block, par2, par3, par4, var30);
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
         if (this.renderMaxZ >= 1.0) {
            par4++;
         }

         this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         boolean var16xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
         boolean var17xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
         boolean var18xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
         boolean var19xxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
         if (!var17xxx && !var19xxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
            this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
         }

         if (!var17xxx && !var18xxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
            this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
         }

         if (!var16xxx && !var19xxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
            this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
         }

         if (!var16xxx && !var18xxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
            this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
         }

         if (this.renderMaxZ >= 1.0) {
            par4--;
         }

         int var20xxx = var14;
         if (this.renderMaxZ >= 1.0 || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1)) {
            var20xxx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         float var22 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + var21 + this.aoLightValueScratchYZPP) / 4.0F;
         float var23 = (var21 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         float var24 = (this.aoLightValueScratchYZNP + var21 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
         float var25 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + var21) / 4.0F;
         var9 = (float)(
            var22 * this.renderMaxY * (1.0 - this.renderMinX)
               + var23 * this.renderMaxY * this.renderMinX
               + var24 * (1.0 - this.renderMaxY) * this.renderMinX
               + var25 * (1.0 - this.renderMaxY) * (1.0 - this.renderMinX)
         );
         var10 = (float)(
            var22 * this.renderMinY * (1.0 - this.renderMinX)
               + var23 * this.renderMinY * this.renderMinX
               + var24 * (1.0 - this.renderMinY) * this.renderMinX
               + var25 * (1.0 - this.renderMinY) * (1.0 - this.renderMinX)
         );
         var11 = (float)(
            var22 * this.renderMinY * (1.0 - this.renderMaxX)
               + var23 * this.renderMinY * this.renderMaxX
               + var24 * (1.0 - this.renderMinY) * this.renderMaxX
               + var25 * (1.0 - this.renderMinY) * (1.0 - this.renderMaxX)
         );
         var12 = (float)(
            var22 * this.renderMaxY * (1.0 - this.renderMaxX)
               + var23 * this.renderMaxY * this.renderMaxX
               + var24 * (1.0 - this.renderMaxY) * this.renderMaxX
               + var25 * (1.0 - this.renderMaxY) * (1.0 - this.renderMaxX)
         );
         int var26 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, var20xxx);
         int var27 = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, var20xxx);
         int var28 = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20xxx);
         int var29 = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, var20xxx);
         this.brightnessTopLeft = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            this.renderMaxY * (1.0 - this.renderMinX),
            (1.0 - this.renderMaxY) * (1.0 - this.renderMinX),
            (1.0 - this.renderMaxY) * this.renderMinX,
            this.renderMaxY * this.renderMinX
         );
         this.brightnessBottomLeft = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            this.renderMinY * (1.0 - this.renderMinX),
            (1.0 - this.renderMinY) * (1.0 - this.renderMinX),
            (1.0 - this.renderMinY) * this.renderMinX,
            this.renderMinY * this.renderMinX
         );
         this.brightnessBottomRight = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            this.renderMinY * (1.0 - this.renderMaxX),
            (1.0 - this.renderMinY) * (1.0 - this.renderMaxX),
            (1.0 - this.renderMinY) * this.renderMaxX,
            this.renderMinY * this.renderMaxX
         );
         this.brightnessTopRight = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            this.renderMaxY * (1.0 - this.renderMaxX),
            (1.0 - this.renderMaxY) * (1.0 - this.renderMaxX),
            (1.0 - this.renderMaxY) * this.renderMaxX,
            this.renderMaxY * this.renderMaxX
         );
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 3, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(3)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.8F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.8F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.8F;
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
         this.renderFaceZPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
         if (this.renderMinX <= 0.0) {
            par2--;
         }

         this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         boolean var16xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
         boolean var17xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
         boolean var18xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
         boolean var19xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
         if (!var18xxxx && !var17xxxx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
            this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
         }

         if (!var19xxxx && !var17xxxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
            this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
         }

         if (!var18xxxx && !var16xxxx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
            this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
         }

         if (!var19xxxx && !var16xxxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
            this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
         }

         if (this.renderMinX <= 0.0) {
            par2++;
         }

         int var20xxxx = var14;
         if (this.renderMinX <= 0.0 || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4)) {
            var20xxxx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
         float var22 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + var21 + this.aoLightValueScratchXZNP) / 4.0F;
         float var23 = (var21 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
         float var24 = (this.aoLightValueScratchXZNN + var21 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
         float var25 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + var21) / 4.0F;
         var9 = (float)(
            var23 * this.renderMaxY * this.renderMaxZ
               + var24 * this.renderMaxY * (1.0 - this.renderMaxZ)
               + var25 * (1.0 - this.renderMaxY) * (1.0 - this.renderMaxZ)
               + var22 * (1.0 - this.renderMaxY) * this.renderMaxZ
         );
         var10 = (float)(
            var23 * this.renderMaxY * this.renderMinZ
               + var24 * this.renderMaxY * (1.0 - this.renderMinZ)
               + var25 * (1.0 - this.renderMaxY) * (1.0 - this.renderMinZ)
               + var22 * (1.0 - this.renderMaxY) * this.renderMinZ
         );
         var11 = (float)(
            var23 * this.renderMinY * this.renderMinZ
               + var24 * this.renderMinY * (1.0 - this.renderMinZ)
               + var25 * (1.0 - this.renderMinY) * (1.0 - this.renderMinZ)
               + var22 * (1.0 - this.renderMinY) * this.renderMinZ
         );
         var12 = (float)(
            var23 * this.renderMinY * this.renderMaxZ
               + var24 * this.renderMinY * (1.0 - this.renderMaxZ)
               + var25 * (1.0 - this.renderMinY) * (1.0 - this.renderMaxZ)
               + var22 * (1.0 - this.renderMinY) * this.renderMaxZ
         );
         int var26 = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, var20xxxx);
         int var27 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, var20xxxx);
         int var28 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, var20xxxx);
         int var29 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, var20xxxx);
         this.brightnessTopLeft = this.mixAoBrightness(
            var27,
            var28,
            var29,
            var26,
            this.renderMaxY * this.renderMaxZ,
            this.renderMaxY * (1.0 - this.renderMaxZ),
            (1.0 - this.renderMaxY) * (1.0 - this.renderMaxZ),
            (1.0 - this.renderMaxY) * this.renderMaxZ
         );
         this.brightnessBottomLeft = this.mixAoBrightness(
            var27,
            var28,
            var29,
            var26,
            this.renderMaxY * this.renderMinZ,
            this.renderMaxY * (1.0 - this.renderMinZ),
            (1.0 - this.renderMaxY) * (1.0 - this.renderMinZ),
            (1.0 - this.renderMaxY) * this.renderMinZ
         );
         this.brightnessBottomRight = this.mixAoBrightness(
            var27,
            var28,
            var29,
            var26,
            this.renderMinY * this.renderMinZ,
            this.renderMinY * (1.0 - this.renderMinZ),
            (1.0 - this.renderMinY) * (1.0 - this.renderMinZ),
            (1.0 - this.renderMinY) * this.renderMinZ
         );
         this.brightnessTopRight = this.mixAoBrightness(
            var27,
            var28,
            var29,
            var26,
            this.renderMinY * this.renderMaxZ,
            this.renderMinY * (1.0 - this.renderMaxZ),
            (1.0 - this.renderMinY) * (1.0 - this.renderMaxZ),
            (1.0 - this.renderMinY) * this.renderMaxZ
         );
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 4, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(4)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
         this.renderFaceXNeg(par1Block, par2, par3, par4, var30);
         var8 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
         if (this.renderMaxX >= 1.0) {
            par2++;
         }

         this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
         this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
         this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
         this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
         this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
         this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
         this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
         this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
         boolean var16xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
         boolean var17xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
         boolean var18xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
         boolean var19xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
         if (!var17xxxxx && !var19xxxxx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
            this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
         }

         if (!var17xxxxx && !var18xxxxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
            this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
         }

         if (!var16xxxxx && !var19xxxxx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
            this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
         }

         if (!var16xxxxx && !var18xxxxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
            this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
         }

         if (this.renderMaxX >= 1.0) {
            par2--;
         }

         int var20xxxxx = var14;
         if (this.renderMaxX >= 1.0 || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4)) {
            var20xxxxx = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
         }

         float var21 = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
         float var22 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + var21 + this.aoLightValueScratchXZPP) / 4.0F;
         float var23 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + var21) / 4.0F;
         float var24 = (this.aoLightValueScratchXZPN + var21 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
         float var25 = (var21 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         var9 = (float)(
            var22 * (1.0 - this.renderMinY) * this.renderMaxZ
               + var23 * (1.0 - this.renderMinY) * (1.0 - this.renderMaxZ)
               + var24 * this.renderMinY * (1.0 - this.renderMaxZ)
               + var25 * this.renderMinY * this.renderMaxZ
         );
         var10 = (float)(
            var22 * (1.0 - this.renderMinY) * this.renderMinZ
               + var23 * (1.0 - this.renderMinY) * (1.0 - this.renderMinZ)
               + var24 * this.renderMinY * (1.0 - this.renderMinZ)
               + var25 * this.renderMinY * this.renderMinZ
         );
         var11 = (float)(
            var22 * (1.0 - this.renderMaxY) * this.renderMinZ
               + var23 * (1.0 - this.renderMaxY) * (1.0 - this.renderMinZ)
               + var24 * this.renderMaxY * (1.0 - this.renderMinZ)
               + var25 * this.renderMaxY * this.renderMinZ
         );
         var12 = (float)(
            var22 * (1.0 - this.renderMaxY) * this.renderMaxZ
               + var23 * (1.0 - this.renderMaxY) * (1.0 - this.renderMaxZ)
               + var24 * this.renderMaxY * (1.0 - this.renderMaxZ)
               + var25 * this.renderMaxY * this.renderMaxZ
         );
         int var26 = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var20xxxxx);
         int var27 = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, var20xxxxx);
         int var28 = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, var20xxxxx);
         int var29 = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, var20xxxxx);
         this.brightnessTopLeft = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            (1.0 - this.renderMinY) * this.renderMaxZ,
            (1.0 - this.renderMinY) * (1.0 - this.renderMaxZ),
            this.renderMinY * (1.0 - this.renderMaxZ),
            this.renderMinY * this.renderMaxZ
         );
         this.brightnessBottomLeft = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            (1.0 - this.renderMinY) * this.renderMinZ,
            (1.0 - this.renderMinY) * (1.0 - this.renderMinZ),
            this.renderMinY * (1.0 - this.renderMinZ),
            this.renderMinY * this.renderMinZ
         );
         this.brightnessBottomRight = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            (1.0 - this.renderMaxY) * this.renderMinZ,
            (1.0 - this.renderMaxY) * (1.0 - this.renderMinZ),
            this.renderMaxY * (1.0 - this.renderMinZ),
            this.renderMaxY * this.renderMinZ
         );
         this.brightnessTopRight = this.mixAoBrightness(
            var26,
            var29,
            var28,
            var27,
            (1.0 - this.renderMaxY) * this.renderMaxZ,
            (1.0 - this.renderMaxY) * (1.0 - this.renderMaxZ),
            this.renderMaxY * (1.0 - this.renderMaxZ),
            this.renderMaxY * this.renderMaxZ
         );
         if (!ColorizeBlock.setupBlockSmoothing(this, par1Block, this.blockAccess, par2, par3, par4, 5, var9, var10, var11, var12)) {
            if (RenderBlocksUtils.useColorMultiplier(5)) {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = par5 * 0.6F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = par6 * 0.6F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = par7 * 0.6F;
            } else {
               this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
               this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
               this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
         }

         Icon var30 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
         this.renderFaceXPos(par1Block, par2, par3, par4, var30);
         var8 = true;
      }

      this.enableAO = false;
      return var8;
   }

   private int getAoBrightness(int par1, int par2, int par3, int par4) {
      if (par1 == 0) {
         par1 = par4;
      }

      if (par2 == 0) {
         par2 = par4;
      }

      if (par3 == 0) {
         par3 = par4;
      }

      return par1 + par2 + par3 + par4 >> 2 & 16711935;
   }

   private int mixAoBrightness(int par1, int par2, int par3, int par4, double par5, double par7, double par9, double par11) {
      int var13 = (int)((par1 >> 16 & 0xFF) * par5 + (par2 >> 16 & 0xFF) * par7 + (par3 >> 16 & 0xFF) * par9 + (par4 >> 16 & 0xFF) * par11) & 0xFF;
      int var14 = (int)((par1 & 0xFF) * par5 + (par2 & 0xFF) * par7 + (par3 & 0xFF) * par9 + (par4 & 0xFF) * par11) & 0xFF;
      return var13 << 16 | var14;
   }

   public boolean renderStandardBlockWithColorMultiplier(Block block, int x, int y, int z, float red, float green, float blue) {
      this.enableAO = false;
      Tessellator var8 = Tessellator.instance;
      boolean var9 = false;
      float var10 = 0.5F;
      float var11 = 1.0F;
      float var12 = 0.8F;
      float var13 = 0.6F;
      float var10000 = var11 * red;
      var10000 = var11 * green;
      var10000 = var11 * blue;
      var10000 = var10 * red;
      float var18 = var12 * red;
      float var19 = var13 * red;
      float var20 = var10 * green;
      float var21 = var12 * green;
      float var22 = var13 * green;
      float var23 = var10 * blue;
      float var24 = var12 * blue;
      float var20x = var13 * blue;
      int var26 = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z);
      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(block, this.blockAccess, x, y - 1, z, 0)) {
         var8.setBrightness(this.renderMinY > 0.0 ? var26 : block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z));
         var8.setColorOpaque_F(
            RenderBlocksUtils.getColorMultiplierRed(0), RenderBlocksUtils.getColorMultiplierGreen(0), RenderBlocksUtils.getColorMultiplierBlue(0)
         );
         this.renderFaceYNeg(block, x, y, z, this.getBlockIcon(block, this.blockAccess, x, y, z, 0));
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(block, this.blockAccess, x, y + 1, z, 1)) {
         var8.setBrightness(this.renderMaxY < 1.0 ? var26 : block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z));
         var8.setColorOpaque_F(
            RenderBlocksUtils.getColorMultiplierRed(1), RenderBlocksUtils.getColorMultiplierGreen(1), RenderBlocksUtils.getColorMultiplierBlue(1)
         );
         this.renderFaceYPos(block, x, y, z, this.getBlockIcon(block, this.blockAccess, x, y, z, 1));
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(block, this.blockAccess, x, y, z - 1, 2)) {
         var8.setBrightness(this.renderMinZ > 0.0 ? var26 : block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1));
         var8.setColorOpaque_F(
            RenderBlocksUtils.getColorMultiplierRed(2), RenderBlocksUtils.getColorMultiplierGreen(2), RenderBlocksUtils.getColorMultiplierBlue(2)
         );
         Icon var28 = this.getBlockIcon(block, this.blockAccess, x, y, z, 2);
         this.renderFaceZNeg(block, x, y, z, var28);
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(block, this.blockAccess, x, y, z + 1, 3)) {
         var8.setBrightness(this.renderMaxZ < 1.0 ? var26 : block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1));
         var8.setColorOpaque_F(
            RenderBlocksUtils.getColorMultiplierRed(3), RenderBlocksUtils.getColorMultiplierGreen(3), RenderBlocksUtils.getColorMultiplierBlue(3)
         );
         Icon var28 = this.getBlockIcon(block, this.blockAccess, x, y, z, 3);
         this.renderFaceZPos(block, x, y, z, var28);
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(block, this.blockAccess, x - 1, y, z, 4)) {
         var8.setBrightness(this.renderMinX > 0.0 ? var26 : block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z));
         var8.setColorOpaque_F(
            RenderBlocksUtils.getColorMultiplierRed(4), RenderBlocksUtils.getColorMultiplierGreen(4), RenderBlocksUtils.getColorMultiplierBlue(4)
         );
         Icon var28 = this.getBlockIcon(block, this.blockAccess, x, y, z, 4);
         this.renderFaceXNeg(block, x, y, z, var28);
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(block, this.blockAccess, x + 1, y, z, 5)) {
         var8.setBrightness(this.renderMaxX < 1.0 ? var26 : block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z));
         var8.setColorOpaque_F(
            RenderBlocksUtils.getColorMultiplierRed(5), RenderBlocksUtils.getColorMultiplierGreen(5), RenderBlocksUtils.getColorMultiplierBlue(5)
         );
         Icon var28 = this.getBlockIcon(block, this.blockAccess, x, y, z, 5);
         this.renderFaceXPos(block, x, y, z, var28);
         var9 = true;
      }

      return var9;
   }

   public boolean renderBlockCocoa(BlockCocoa par1BlockCocoa, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1BlockCocoa.e(this.blockAccess, par2, par3, par4));
      var5.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      int var7 = BlockDirectional.getDirection(var6);
      int var8 = BlockCocoa.func_72219_c(var6);
      Icon var9 = par1BlockCocoa.func_94468_i_(var8);
      int var10 = 4 + var8 * 2;
      int var11 = 5 + var8 * 2;
      double var12 = 15.0 - var10;
      double var14 = 15.0;
      double var16 = 4.0;
      double var18 = 4.0 + var11;
      double var20 = var9.getInterpolatedU(var12);
      double var22 = var9.getInterpolatedU(var14);
      double var24 = var9.getInterpolatedV(var16);
      double var26 = var9.getInterpolatedV(var18);
      double var28 = 0.0;
      double var30 = 0.0;
      switch (var7) {
         case 0:
            var28 = 8.0 - var10 / 2;
            var30 = 15.0 - var10;
            break;
         case 1:
            var28 = 1.0;
            var30 = 8.0 - var10 / 2;
            break;
         case 2:
            var28 = 8.0 - var10 / 2;
            var30 = 1.0;
            break;
         case 3:
            var28 = 15.0 - var10;
            var30 = 8.0 - var10 / 2;
      }

      double var32 = par2 + var28 / 16.0;
      double var34 = par2 + (var28 + var10) / 16.0;
      double var36 = par3 + (12.0 - var11) / 16.0;
      double var38 = par3 + 0.75;
      double var40 = par4 + var30 / 16.0;
      double var42 = par4 + (var30 + var10) / 16.0;
      var5.addVertexWithUV(var32, var36, var40, var20, var26);
      var5.addVertexWithUV(var32, var36, var42, var22, var26);
      var5.addVertexWithUV(var32, var38, var42, var22, var24);
      var5.addVertexWithUV(var32, var38, var40, var20, var24);
      var5.addVertexWithUV(var34, var36, var42, var20, var26);
      var5.addVertexWithUV(var34, var36, var40, var22, var26);
      var5.addVertexWithUV(var34, var38, var40, var22, var24);
      var5.addVertexWithUV(var34, var38, var42, var20, var24);
      var5.addVertexWithUV(var34, var36, var40, var20, var26);
      var5.addVertexWithUV(var32, var36, var40, var22, var26);
      var5.addVertexWithUV(var32, var38, var40, var22, var24);
      var5.addVertexWithUV(var34, var38, var40, var20, var24);
      var5.addVertexWithUV(var32, var36, var42, var20, var26);
      var5.addVertexWithUV(var34, var36, var42, var22, var26);
      var5.addVertexWithUV(var34, var38, var42, var22, var24);
      var5.addVertexWithUV(var32, var38, var42, var20, var24);
      int var44 = var10;
      if (var8 >= 2) {
         var44 = var10 - 1;
      }

      var20 = var9.getMinU();
      var22 = var9.getInterpolatedU(var44);
      var24 = var9.getMinV();
      var26 = var9.getInterpolatedV(var44);
      var5.addVertexWithUV(var32, var38, var42, var20, var26);
      var5.addVertexWithUV(var34, var38, var42, var22, var26);
      var5.addVertexWithUV(var34, var38, var40, var22, var24);
      var5.addVertexWithUV(var32, var38, var40, var20, var24);
      var5.addVertexWithUV(var32, var36, var40, var20, var24);
      var5.addVertexWithUV(var34, var36, var40, var22, var24);
      var5.addVertexWithUV(var34, var36, var42, var22, var26);
      var5.addVertexWithUV(var32, var36, var42, var20, var26);
      var20 = var9.getInterpolatedU(12.0);
      var22 = var9.getMaxU();
      var24 = var9.getMinV();
      var26 = var9.getInterpolatedV(4.0);
      var28 = 8.0;
      var30 = 0.0;
      switch (var7) {
         case 0: {
            var28 = 8.0;
            var30 = 12.0;
            double var45 = var20;
            var20 = var22;
            var22 = var45;
            break;
         }
         case 1:
            var28 = 0.0;
            var30 = 8.0;
            break;
         case 2:
            var28 = 8.0;
            var30 = 0.0;
            break;
         case 3: {
            var28 = 12.0;
            var30 = 8.0;
            double var45 = var20;
            var20 = var22;
            var22 = var45;
         }
      }

      var32 = par2 + var28 / 16.0;
      var34 = par2 + (var28 + 4.0) / 16.0;
      var36 = par3 + 0.75;
      var38 = par3 + 1.0;
      var40 = par4 + var30 / 16.0;
      var42 = par4 + (var30 + 4.0) / 16.0;
      if (var7 == 2 || var7 == 0) {
         var5.addVertexWithUV(var32, var36, var40, var22, var26);
         var5.addVertexWithUV(var32, var36, var42, var20, var26);
         var5.addVertexWithUV(var32, var38, var42, var20, var24);
         var5.addVertexWithUV(var32, var38, var40, var22, var24);
         var5.addVertexWithUV(var32, var36, var42, var20, var26);
         var5.addVertexWithUV(var32, var36, var40, var22, var26);
         var5.addVertexWithUV(var32, var38, var40, var22, var24);
         var5.addVertexWithUV(var32, var38, var42, var20, var24);
      } else if (var7 == 1 || var7 == 3) {
         var5.addVertexWithUV(var34, var36, var40, var20, var26);
         var5.addVertexWithUV(var32, var36, var40, var22, var26);
         var5.addVertexWithUV(var32, var38, var40, var22, var24);
         var5.addVertexWithUV(var34, var38, var40, var20, var24);
         var5.addVertexWithUV(var32, var36, var40, var22, var26);
         var5.addVertexWithUV(var34, var36, var40, var20, var26);
         var5.addVertexWithUV(var34, var38, var40, var20, var24);
         var5.addVertexWithUV(var32, var38, var40, var22, var24);
      }

      return true;
   }

   public boolean renderBlockBeacon(BlockBeacon par1BlockBeacon, int par2, int par3, int par4) {
      float var5 = 0.1875F;
      this.setOverrideBlockTexture(this.getBlockIcon(Block.obsidian));
      this.setRenderBounds(0.125, 0.00625F, 0.125, 0.875, var5, 0.875);
      this.renderStandardBlock(par1BlockBeacon, par2, par3, par4);
      this.setOverrideBlockTexture(this.getBlockIcon(Block.glass));
      this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      this.renderStandardBlock(par1BlockBeacon, par2, par3, par4);
      this.setOverrideBlockTexture(par1BlockBeacon.getBeaconIcon());
      this.setRenderBounds(0.1875, var5, 0.1875, 0.8125, 0.875, 0.8125);
      this.renderStandardBlock(par1BlockBeacon, par2, par3, par4);
      this.clearOverrideBlockTexture();
      return true;
   }

   public boolean renderBlockCactus(Block par1Block, int par2, int par3, int par4) {
      int var5 = par1Block.colorMultiplier(this.blockAccess, par2, par3, par4);
      float var6 = (var5 >> 16 & 0xFF) / 255.0F;
      float var7 = (var5 >> 8 & 0xFF) / 255.0F;
      float var8 = (var5 & 0xFF) / 255.0F;
      return this.renderBlockCactusImpl(par1Block, par2, par3, par4, var6, var7, var8);
   }

   public boolean renderBlockCactusImpl(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7) {
      Tessellator var8 = Tessellator.instance;
      boolean var9 = false;
      float var10 = 0.5F;
      float var11 = 1.0F;
      float var12 = 0.8F;
      float var13 = 0.6F;
      float var14 = var10 * par5;
      float var15 = var11 * par5;
      float var16 = var12 * par5;
      float var17 = var13 * par5;
      float var18 = var10 * par6;
      float var19 = var11 * par6;
      float var20 = var12 * par6;
      float var21 = var13 * par6;
      float var22 = var10 * par7;
      float var23 = var11 * par7;
      float var24 = var12 * par7;
      float var25 = var13 * par7;
      float var26 = 0.0625F;
      int var28 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 - 1, par4, 0)) {
         var8.setBrightness(this.renderMinY > 0.0 ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
         var8.setColorOpaque_F(var14, var18, var22);
         this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3 + 1, par4, 1)) {
         var8.setBrightness(this.renderMaxY < 1.0 ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
         var8.setColorOpaque_F(var15, var19, var23);
         this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 - 1, 2)) {
         var8.setBrightness(this.renderMinZ > 0.0 ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
         var8.setColorOpaque_F(var16, var20, var24);
         var8.addTranslation(0.0F, 0.0F, var26);
         this.renderFaceZNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2));
         var8.addTranslation(0.0F, 0.0F, -var26);
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2, par3, par4 + 1, 3)) {
         var8.setBrightness(this.renderMaxZ < 1.0 ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
         var8.setColorOpaque_F(var16, var20, var24);
         var8.addTranslation(0.0F, 0.0F, -var26);
         this.renderFaceZPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3));
         var8.addTranslation(0.0F, 0.0F, var26);
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 - 1, par3, par4, 4)) {
         var8.setBrightness(this.renderMinX > 0.0 ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
         var8.setColorOpaque_F(var17, var21, var25);
         var8.addTranslation(var26, 0.0F, 0.0F);
         this.renderFaceXNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4));
         var8.addTranslation(-var26, 0.0F, 0.0F);
         var9 = true;
      }

      if (this.renderAllFaces || RenderPass.shouldSideBeRendered(par1Block, this.blockAccess, par2 + 1, par3, par4, 5)) {
         var8.setBrightness(this.renderMaxX < 1.0 ? var28 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
         var8.setColorOpaque_F(var17, var21, var25);
         var8.addTranslation(-var26, 0.0F, 0.0F);
         this.renderFaceXPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5));
         var8.addTranslation(var26, 0.0F, 0.0F);
         var9 = true;
      }

      return var9;
   }

   public boolean renderBlockFence(BlockFence par1BlockFence, int par2, int par3, int par4) {
      boolean var5 = false;
      float var6 = 0.375F;
      float var7 = 0.625F;
      this.setRenderBounds(var6, 0.0, var6, var7, 1.0, var7);
      this.renderStandardBlock(par1BlockFence, par2, par3, par4);
      var5 = true;
      boolean var8 = false;
      boolean var9 = false;
      if (par1BlockFence.canConnectFenceTo(this.blockAccess, par2 - 1, par3, par4) || par1BlockFence.canConnectFenceTo(this.blockAccess, par2 + 1, par3, par4)) {
         var8 = true;
      }

      if (par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 - 1) || par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 + 1)) {
         var9 = true;
      }

      boolean var10 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2 - 1, par3, par4);
      boolean var11 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2 + 1, par3, par4);
      boolean var12 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 - 1);
      boolean var13 = par1BlockFence.canConnectFenceTo(this.blockAccess, par2, par3, par4 + 1);
      if (!var8 && !var9) {
         var8 = true;
      }

      var6 = 0.4375F;
      var7 = 0.5625F;
      float var14 = 0.75F;
      float var15 = 0.9375F;
      float var16 = var10 ? 0.0F : var6;
      float var17 = var11 ? 1.0F : var7;
      float var18 = var12 ? 0.0F : var6;
      float var19 = var13 ? 1.0F : var7;
      if (var8) {
         this.setRenderBounds(var16, var14, var6, var17, var15, var7);
         this.renderStandardBlock(par1BlockFence, par2, par3, par4);
         var5 = true;
      }

      if (var9) {
         this.setRenderBounds(var6, var14, var18, var7, var15, var19);
         this.renderStandardBlock(par1BlockFence, par2, par3, par4);
         var5 = true;
      }

      var14 = 0.375F;
      var15 = 0.5625F;
      if (var8) {
         this.setRenderBounds(var16, var14, var6, var17, var15, var7);
         this.renderStandardBlock(par1BlockFence, par2, par3, par4);
         var5 = true;
      }

      if (var9) {
         this.setRenderBounds(var6, var14, var18, var7, var15, var19);
         this.renderStandardBlock(par1BlockFence, par2, par3, par4);
         var5 = true;
      }

      par1BlockFence.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
      return var5;
   }

   public boolean renderBlockWall(BlockWall par1BlockWall, int par2, int par3, int par4) {
      boolean var5 = par1BlockWall.canConnectWallTo(this.blockAccess, par2 - 1, par3, par4);
      boolean var6 = par1BlockWall.canConnectWallTo(this.blockAccess, par2 + 1, par3, par4);
      boolean var7 = par1BlockWall.canConnectWallTo(this.blockAccess, par2, par3, par4 - 1);
      boolean var8 = par1BlockWall.canConnectWallTo(this.blockAccess, par2, par3, par4 + 1);
      boolean var9 = var7 && var8 && !var5 && !var6;
      boolean var10 = !var7 && !var8 && var5 && var6;
      boolean var11 = this.blockAccess.isAirBlock(par2, par3 + 1, par4);
      var11 = var11 || WorldUtils.isGroundCoverOnBlock(this.blockAccess, par2, par3, par4);
      if ((var9 || var10) && var11) {
         if (var9) {
            this.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, 0.8125, 1.0);
            this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         } else {
            this.setRenderBounds(0.0, 0.0, 0.3125, 1.0, 0.8125, 0.6875);
            this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         }
      } else {
         this.setRenderBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
         this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         if (var5) {
            this.setRenderBounds(0.0, 0.0, 0.3125, 0.25, 0.8125, 0.6875);
            this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         }

         if (var6) {
            this.setRenderBounds(0.75, 0.0, 0.3125, 1.0, 0.8125, 0.6875);
            this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         }

         if (var7) {
            this.setRenderBounds(0.3125, 0.0, 0.0, 0.6875, 0.8125, 0.25);
            this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         }

         if (var8) {
            this.setRenderBounds(0.3125, 0.0, 0.75, 0.6875, 0.8125, 1.0);
            this.renderStandardBlock(par1BlockWall, par2, par3, par4);
         }
      }

      par1BlockWall.setBlockBoundsBasedOnState(this.blockAccess, par2, par3, par4);
      return true;
   }

   public boolean renderBlockDragonEgg(BlockDragonEgg par1BlockDragonEgg, int par2, int par3, int par4) {
      boolean var5 = false;
      int var6 = 0;

      for (int var7 = 0; var7 < 8; var7++) {
         byte var8 = 0;
         byte var9 = 1;
         if (var7 == 0) {
            var8 = 2;
         }

         if (var7 == 1) {
            var8 = 3;
         }

         if (var7 == 2) {
            var8 = 4;
         }

         if (var7 == 3) {
            var8 = 5;
            var9 = 2;
         }

         if (var7 == 4) {
            var8 = 6;
            var9 = 3;
         }

         if (var7 == 5) {
            var8 = 7;
            var9 = 5;
         }

         if (var7 == 6) {
            var8 = 6;
            var9 = 2;
         }

         if (var7 == 7) {
            var8 = 3;
         }

         float var10 = var8 / 16.0F;
         float var11 = 1.0F - var6 / 16.0F;
         float var12 = 1.0F - (var6 + var9) / 16.0F;
         var6 += var9;
         this.setRenderBounds(0.5F - var10, var12, 0.5F - var10, 0.5F + var10, var11, 0.5F + var10);
         this.renderStandardBlock(par1BlockDragonEgg, par2, par3, par4);
      }

      var5 = true;
      this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return var5;
   }

   public boolean renderBlockFenceGate(BlockFenceGate par1BlockFenceGate, int par2, int par3, int par4) {
      boolean var5 = true;
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      boolean var7 = BlockFenceGate.isFenceGateOpen(var6);
      int var8 = BlockDirectional.getDirection(var6);
      float var9 = 0.375F;
      float var10 = 0.5625F;
      float var11 = 0.75F;
      float var12 = 0.9375F;
      float var13 = 0.3125F;
      float var14 = 1.0F;
      if ((var8 == 2 || var8 == 0)
            && this.blockAccess.getBlockId(par2 - 1, par3, par4) == Block.cobblestoneWall.blockID
            && this.blockAccess.getBlockId(par2 + 1, par3, par4) == Block.cobblestoneWall.blockID
         || (var8 == 3 || var8 == 1)
            && this.blockAccess.getBlockId(par2, par3, par4 - 1) == Block.cobblestoneWall.blockID
            && this.blockAccess.getBlockId(par2, par3, par4 + 1) == Block.cobblestoneWall.blockID) {
         var9 -= 0.1875F;
         var10 -= 0.1875F;
         var11 -= 0.1875F;
         var12 -= 0.1875F;
         var13 -= 0.1875F;
         var14 -= 0.1875F;
      }

      this.renderAllFaces = true;
      if (var8 != 3 && var8 != 1) {
         float var15 = 0.0F;
         float var16 = 0.125F;
         float var17 = 0.4375F;
         float var18 = 0.5625F;
         this.setRenderBounds(var15, var13, var17, var16, var14, var18);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var15 = 0.875F;
         var16 = 1.0F;
         this.setRenderBounds(var15, var13, var17, var16, var14, var18);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
      } else {
         this.uvRotateTop = 1;
         float var15 = 0.4375F;
         float var16 = 0.5625F;
         float var17 = 0.0F;
         float var18 = 0.125F;
         this.setRenderBounds(var15, var13, var17, var16, var14, var18);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var17 = 0.875F;
         var18 = 1.0F;
         this.setRenderBounds(var15, var13, var17, var16, var14, var18);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         this.uvRotateTop = 0;
      }

      if (var7) {
         if (var8 == 2 || var8 == 0) {
            this.uvRotateTop = 1;
         }

         if (var8 == 3) {
            this.setRenderBounds(0.8125, var9, 0.0, 0.9375, var12, 0.125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.8125, var9, 0.875, 0.9375, var12, 1.0);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.5625, var9, 0.0, 0.8125, var10, 0.125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.5625, var9, 0.875, 0.8125, var10, 1.0);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.5625, var11, 0.0, 0.8125, var12, 0.125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.5625, var11, 0.875, 0.8125, var12, 1.0);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         } else if (var8 == 1) {
            this.setRenderBounds(0.0625, var9, 0.0, 0.1875, var12, 0.125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.0625, var9, 0.875, 0.1875, var12, 1.0);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.1875, var9, 0.0, 0.4375, var10, 0.125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.1875, var9, 0.875, 0.4375, var10, 1.0);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.1875, var11, 0.0, 0.4375, var12, 0.125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.1875, var11, 0.875, 0.4375, var12, 1.0);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         } else if (var8 == 0) {
            this.setRenderBounds(0.0, var9, 0.8125, 0.125, var12, 0.9375);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.875, var9, 0.8125, 1.0, var12, 0.9375);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.0, var9, 0.5625, 0.125, var10, 0.8125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.875, var9, 0.5625, 1.0, var10, 0.8125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.0, var11, 0.5625, 0.125, var12, 0.8125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.875, var11, 0.5625, 1.0, var12, 0.8125);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         } else if (var8 == 2) {
            this.setRenderBounds(0.0, var9, 0.0625, 0.125, var12, 0.1875);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.875, var9, 0.0625, 1.0, var12, 0.1875);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.0, var9, 0.1875, 0.125, var10, 0.4375);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.875, var9, 0.1875, 1.0, var10, 0.4375);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.0, var11, 0.1875, 0.125, var12, 0.4375);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
            this.setRenderBounds(0.875, var11, 0.1875, 1.0, var12, 0.4375);
            this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         }
      } else if (var8 != 3 && var8 != 1) {
         float var22 = 0.375F;
         float var36 = 0.5F;
         float var32 = 0.4375F;
         float var46 = 0.5625F;
         this.setRenderBounds(var22, var9, var32, var36, var12, var46);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var22 = 0.5F;
         var36 = 0.625F;
         this.setRenderBounds(var22, var9, var32, var36, var12, var46);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var22 = 0.625F;
         var36 = 0.875F;
         this.setRenderBounds(var22, var9, var32, var36, var10, var46);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         this.setRenderBounds(var22, var11, var32, var36, var12, var46);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var22 = 0.125F;
         var36 = 0.375F;
         this.setRenderBounds(var22, var9, var32, var36, var10, var46);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         this.setRenderBounds(var22, var11, var32, var36, var12, var46);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
      } else {
         this.uvRotateTop = 1;
         float var21 = 0.4375F;
         float var35 = 0.5625F;
         float var28 = 0.375F;
         float var42 = 0.5F;
         this.setRenderBounds(var21, var9, var28, var35, var12, var42);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var28 = 0.5F;
         var42 = 0.625F;
         this.setRenderBounds(var21, var9, var28, var35, var12, var42);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var28 = 0.625F;
         var42 = 0.875F;
         this.setRenderBounds(var21, var9, var28, var35, var10, var42);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         this.setRenderBounds(var21, var11, var28, var35, var12, var42);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         var28 = 0.125F;
         var42 = 0.375F;
         this.setRenderBounds(var21, var9, var28, var35, var10, var42);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
         this.setRenderBounds(var21, var11, var28, var35, var12, var42);
         this.renderStandardBlock(par1BlockFenceGate, par2, par3, par4);
      }

      this.renderAllFaces = false;
      this.uvRotateTop = 0;
      this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
      return var5;
   }

   private boolean renderBlockHopper(BlockHopper par1BlockHopper, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      var5.setBrightness(par1BlockHopper.e(this.blockAccess, par2, par3, par4));
      float var6 = 1.0F;
      int var7 = par1BlockHopper.c(this.blockAccess, par2, par3, par4);
      float var8 = (var7 >> 16 & 0xFF) / 255.0F;
      float var9 = (var7 >> 8 & 0xFF) / 255.0F;
      float var10 = (var7 & 0xFF) / 255.0F;
      var5.setColorOpaque_F(var6 * var8, var6 * var9, var6 * var10);
      return this.renderBlockHopperMetadata(par1BlockHopper, par2, par3, par4, this.blockAccess.getBlockMetadata(par2, par3, par4), false);
   }

   private boolean renderBlockHopperMetadata(BlockHopper par1BlockHopper, int par2, int par3, int par4, int par5, boolean par6) {
      Tessellator var7 = Tessellator.instance;
      int var8 = BlockHopper.getDirectionFromMetadata(par5);
      double var9 = 0.625;
      this.setRenderBounds(0.0, var9, 0.0, 1.0, 1.0, 1.0);
      if (par6) {
         var7.startDrawingQuads();
         var7.setNormal(0.0F, -1.0F, 0.0F);
         this.renderFaceYNeg(
            par1BlockHopper,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockHopper, 0, par5)
               : this.getBlockIcon(par1BlockHopper, this.blockAccess, par2, par3, par4, 0)
         );
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 1.0F, 0.0F);
         this.renderFaceYPos(
            par1BlockHopper,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockHopper, 1, par5)
               : this.getBlockIcon(par1BlockHopper, this.blockAccess, par2, par3, par4, 1)
         );
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 0.0F, -1.0F);
         this.renderFaceZNeg(
            par1BlockHopper,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockHopper, 2, par5)
               : this.getBlockIcon(par1BlockHopper, this.blockAccess, par2, par3, par4, 2)
         );
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 0.0F, 1.0F);
         this.renderFaceZPos(
            par1BlockHopper,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockHopper, 3, par5)
               : this.getBlockIcon(par1BlockHopper, this.blockAccess, par2, par3, par4, 3)
         );
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(-1.0F, 0.0F, 0.0F);
         this.renderFaceXNeg(
            par1BlockHopper,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockHopper, 4, par5)
               : this.getBlockIcon(par1BlockHopper, this.blockAccess, par2, par3, par4, 4)
         );
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(1.0F, 0.0F, 0.0F);
         this.renderFaceXPos(
            par1BlockHopper,
            0.0,
            0.0,
            0.0,
            this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(par1BlockHopper, 5, par5)
               : this.getBlockIcon(par1BlockHopper, this.blockAccess, par2, par3, par4, 5)
         );
         var7.draw();
      } else {
         this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
      }

      if (!par6) {
         var7.setBrightness(par1BlockHopper.e(this.blockAccess, par2, par3, par4));
         float var11 = 1.0F;
         int var12 = par1BlockHopper.c(this.blockAccess, par2, par3, par4);
         float var13 = (var12 >> 16 & 0xFF) / 255.0F;
         float var14 = (var12 >> 8 & 0xFF) / 255.0F;
         float var15 = (var12 & 0xFF) / 255.0F;
         var7.setColorOpaque_F(var11 * var13, var11 * var14, var11 * var15);
      }

      Icon var24 = BlockHopper.getHopperIcon("hopper");
      Icon var25 = BlockHopper.getHopperIcon("hopper_inside");
      float var13 = 0.125F;
      if (par6) {
         var7.startDrawingQuads();
         var7.setNormal(1.0F, 0.0F, 0.0F);
         this.renderFaceXPos(par1BlockHopper, -1.0F + var13, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(-1.0F, 0.0F, 0.0F);
         this.renderFaceXNeg(par1BlockHopper, 1.0F - var13, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 0.0F, 1.0F);
         this.renderFaceZPos(par1BlockHopper, 0.0, 0.0, -1.0F + var13, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 0.0F, -1.0F);
         this.renderFaceZNeg(par1BlockHopper, 0.0, 0.0, 1.0F - var13, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 1.0F, 0.0F);
         this.renderFaceYPos(par1BlockHopper, 0.0, -1.0 + var9, 0.0, var25);
         var7.draw();
      } else {
         this.renderFaceXPos(par1BlockHopper, par2 - 1.0F + var13, par3, par4, var24);
         this.renderFaceXNeg(par1BlockHopper, par2 + 1.0F - var13, par3, par4, var24);
         this.renderFaceZPos(par1BlockHopper, par2, par3, par4 - 1.0F + var13, var24);
         this.renderFaceZNeg(par1BlockHopper, par2, par3, par4 + 1.0F - var13, var24);
         this.renderFaceYPos(par1BlockHopper, par2, par3 - 1.0F + var9, par4, var25);
      }

      this.setOverrideBlockTexture(var24);
      double var26 = 0.25;
      double var27 = 0.25;
      this.setRenderBounds(var26, var27, var26, 1.0 - var26, var9 - 0.002, 1.0 - var26);
      if (par6) {
         var7.startDrawingQuads();
         var7.setNormal(1.0F, 0.0F, 0.0F);
         this.renderFaceXPos(par1BlockHopper, 0.0, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(-1.0F, 0.0F, 0.0F);
         this.renderFaceXNeg(par1BlockHopper, 0.0, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 0.0F, 1.0F);
         this.renderFaceZPos(par1BlockHopper, 0.0, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 0.0F, -1.0F);
         this.renderFaceZNeg(par1BlockHopper, 0.0, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, 1.0F, 0.0F);
         this.renderFaceYPos(par1BlockHopper, 0.0, 0.0, 0.0, var24);
         var7.draw();
         var7.startDrawingQuads();
         var7.setNormal(0.0F, -1.0F, 0.0F);
         this.renderFaceYNeg(par1BlockHopper, 0.0, 0.0, 0.0, var24);
         var7.draw();
      } else {
         this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
      }

      if (!par6) {
         double var20 = 0.375;
         double var22 = 0.25;
         this.setOverrideBlockTexture(var24);
         if (var8 == 0) {
            this.setRenderBounds(var20, 0.0, var20, 1.0 - var20, 0.25, 1.0 - var20);
            this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
         }

         if (var8 == 2) {
            this.setRenderBounds(var20, var27, 0.0, 1.0 - var20, var27 + var22, var26);
            this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
         }

         if (var8 == 3) {
            this.setRenderBounds(var20, var27, 1.0 - var26, 1.0 - var20, var27 + var22, 1.0);
            this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
         }

         if (var8 == 4) {
            this.setRenderBounds(0.0, var27, var20, var26, var27 + var22, 1.0 - var20);
            this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
         }

         if (var8 == 5) {
            this.setRenderBounds(1.0 - var26, var27, var20, 1.0, var27 + var22, 1.0 - var20);
            this.renderStandardBlock(par1BlockHopper, par2, par3, par4);
         }
      }

      this.clearOverrideBlockTexture();
      return true;
   }

   public boolean renderBlockStairs(BlockStairs par1BlockStairs, int par2, int par3, int par4) {
      par1BlockStairs.func_82541_d(this.blockAccess, par2, par3, par4);
      this.setRenderBoundsFromBlock(par1BlockStairs);
      this.renderStandardBlock(par1BlockStairs, par2, par3, par4);
      boolean var5 = par1BlockStairs.func_82542_g(this.blockAccess, par2, par3, par4);
      this.setRenderBoundsFromBlock(par1BlockStairs);
      this.renderStandardBlock(par1BlockStairs, par2, par3, par4);
      if (var5 && par1BlockStairs.func_82544_h(this.blockAccess, par2, par3, par4)) {
         this.setRenderBoundsFromBlock(par1BlockStairs);
         this.renderStandardBlock(par1BlockStairs, par2, par3, par4);
      }

      return true;
   }

   public boolean renderBlockDoor(Block par1Block, int par2, int par3, int par4) {
      Tessellator var5 = Tessellator.instance;
      int var6 = this.blockAccess.getBlockMetadata(par2, par3, par4);
      if ((var6 & 8) != 0) {
         if (this.blockAccess.getBlockId(par2, par3 - 1, par4) != par1Block.blockID) {
            return false;
         }
      } else if (this.blockAccess.getBlockId(par2, par3 + 1, par4) != par1Block.blockID) {
         return false;
      }

      boolean var7 = false;
      float var8 = 0.5F;
      float var9 = 1.0F;
      float var10 = 0.8F;
      float var11 = 0.6F;
      int var12 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
      var5.setBrightness(this.renderMinY > 0.0 ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
      var5.setColorOpaque_F(var8, var8, var8);
      this.renderFaceYNeg(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 0));
      var7 = true;
      var5.setBrightness(this.renderMaxY < 1.0 ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
      var5.setColorOpaque_F(var9, var9, var9);
      this.renderFaceYPos(par1Block, par2, par3, par4, this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 1));
      var7 = true;
      var5.setBrightness(this.renderMinZ > 0.0 ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
      var5.setColorOpaque_F(var10, var10, var10);
      Icon var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 2);
      this.renderFaceZNeg(par1Block, par2, par3, par4, var14);
      var7 = true;
      this.flipTexture = false;
      var5.setBrightness(this.renderMaxZ < 1.0 ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
      var5.setColorOpaque_F(var10, var10, var10);
      var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 3);
      this.renderFaceZPos(par1Block, par2, par3, par4, var14);
      var7 = true;
      this.flipTexture = false;
      var5.setBrightness(this.renderMinX > 0.0 ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
      var5.setColorOpaque_F(var11, var11, var11);
      var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 4);
      this.renderFaceXNeg(par1Block, par2, par3, par4, var14);
      var7 = true;
      this.flipTexture = false;
      var5.setBrightness(this.renderMaxX < 1.0 ? var12 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
      var5.setColorOpaque_F(var11, var11, var11);
      var14 = this.getBlockIcon(par1Block, this.blockAccess, par2, par3, par4, 5);
      this.renderFaceXPos(par1Block, par2, par3, par4, var14);
      var7 = true;
      this.flipTexture = false;
      return var7;
   }

   public void renderFaceYNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      if (this.hasOverrideBlockTexture()) {
         par8Icon = this.overrideBlockTexture;
      }

      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(this.renderMinZ * 16.0);
      double var16 = par8Icon.getInterpolatedV(this.renderMaxZ * 16.0);
      if (this.renderMinX < 0.0 || this.renderMaxX > 1.0) {
         var10 = par8Icon.getMinU();
         var12 = par8Icon.getMaxU();
      }

      if (this.renderMinZ < 0.0 || this.renderMaxZ > 1.0) {
         var14 = par8Icon.getMinV();
         var16 = par8Icon.getMaxV();
      }

      double var18 = var12;
      double var20 = var10;
      double var22 = var14;
      double var24 = var16;
      if (this.uvRotateBottom == 2) {
         var10 = par8Icon.getInterpolatedU(this.renderMinZ * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxX * 16.0);
         var12 = par8Icon.getInterpolatedU(this.renderMaxZ * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinX * 16.0);
         var22 = var14;
         var24 = var16;
         var18 = var10;
         var20 = var12;
         var14 = var16;
         var16 = var14;
      } else if (this.uvRotateBottom == 1) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMaxZ * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMinZ * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMaxX * 16.0);
         var18 = var12;
         var20 = var10;
         var10 = var12;
         var12 = var10;
         var22 = var16;
         var24 = var14;
      } else if (this.uvRotateBottom == 3) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMaxX * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMinZ * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMaxZ * 16.0);
         var18 = var12;
         var20 = var10;
         var22 = var14;
         var24 = var16;
      }

      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMinY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var30, var34, var20, var24);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var28, var30, var32, var18, var22);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
      } else {
         var9.addVertexWithUV(var26, var30, var34, var20, var24);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.addVertexWithUV(var28, var30, var32, var18, var22);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
      }
   }

   public void renderFaceYPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      if (this.hasOverrideBlockTexture()) {
         par8Icon = this.overrideBlockTexture;
      }

      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(this.renderMinZ * 16.0);
      double var16 = par8Icon.getInterpolatedV(this.renderMaxZ * 16.0);
      if (this.renderMinX < 0.0 || this.renderMaxX > 1.0) {
         var10 = par8Icon.getMinU();
         var12 = par8Icon.getMaxU();
      }

      if (this.renderMinZ < 0.0 || this.renderMaxZ > 1.0) {
         var14 = par8Icon.getMinV();
         var16 = par8Icon.getMaxV();
      }

      double var18 = var12;
      double var20 = var10;
      double var22 = var14;
      double var24 = var16;
      if (this.uvRotateTop == 1) {
         var10 = par8Icon.getInterpolatedU(this.renderMinZ * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxX * 16.0);
         var12 = par8Icon.getInterpolatedU(this.renderMaxZ * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinX * 16.0);
         var22 = var14;
         var24 = var16;
         var18 = var10;
         var20 = var12;
         var14 = var16;
         var16 = var14;
      } else if (this.uvRotateTop == 2) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMaxZ * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMinZ * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMaxX * 16.0);
         var18 = var12;
         var20 = var10;
         var10 = var12;
         var12 = var10;
         var22 = var16;
         var24 = var14;
      } else if (this.uvRotateTop == 3) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMaxX * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMinZ * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMaxZ * 16.0);
         var18 = var12;
         var20 = var10;
         var22 = var14;
         var24 = var16;
      }

      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMaxY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var28, var30, var32, var18, var22);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var30, var34, var20, var24);
      } else {
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.addVertexWithUV(var28, var30, var32, var18, var22);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.addVertexWithUV(var26, var30, var34, var20, var24);
      }
   }

   public void renderFaceZNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      if (this.hasOverrideBlockTexture()) {
         par8Icon = this.overrideBlockTexture;
      }

      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      if (this.flipTexture) {
         double var18 = var10;
         var10 = var12;
         var12 = var18;
      }

      if (this.renderMinX < 0.0 || this.renderMaxX > 1.0) {
         var10 = par8Icon.getMinU();
         var12 = par8Icon.getMaxU();
      }

      if (this.renderMinY < 0.0 || this.renderMaxY > 1.0) {
         var14 = par8Icon.getMinV();
         var16 = par8Icon.getMaxV();
      }

      double var18 = var12;
      double var20 = var10;
      double var22 = var14;
      double var24 = var16;
      if (this.uvRotateEast == 2) {
         var10 = par8Icon.getInterpolatedU(this.renderMinY * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMaxX * 16.0);
         var22 = var14;
         var24 = var16;
         var18 = var10;
         var20 = var12;
         var14 = var16;
         var16 = var14;
      } else if (this.uvRotateEast == 1) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMaxY * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMaxX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMinY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMinX * 16.0);
         var18 = var12;
         var20 = var10;
         var10 = var12;
         var12 = var10;
         var22 = var16;
         var24 = var14;
      } else if (this.uvRotateEast == 3) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMaxX * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMinY * 16.0);
         var18 = var12;
         var20 = var10;
         var22 = var14;
         var24 = var16;
      }

      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMinY;
      double var32 = par4 + this.renderMaxY;
      double var34 = par6 + this.renderMinZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var32, var34, var18, var22);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var28, var32, var34, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var28, var30, var34, var20, var24);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var30, var34, var12, var16);
      } else {
         var9.addVertexWithUV(var26, var32, var34, var18, var22);
         var9.addVertexWithUV(var28, var32, var34, var10, var14);
         var9.addVertexWithUV(var28, var30, var34, var20, var24);
         var9.addVertexWithUV(var26, var30, var34, var12, var16);
      }
   }

   public void renderFaceZPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      if (this.hasOverrideBlockTexture()) {
         par8Icon = this.overrideBlockTexture;
      }

      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      if (this.flipTexture) {
         double var18 = var10;
         var10 = var12;
         var12 = var18;
      }

      if (this.renderMinX < 0.0 || this.renderMaxX > 1.0) {
         var10 = par8Icon.getMinU();
         var12 = par8Icon.getMaxU();
      }

      if (this.renderMinY < 0.0 || this.renderMaxY > 1.0) {
         var14 = par8Icon.getMinV();
         var16 = par8Icon.getMaxV();
      }

      double var18 = var12;
      double var20 = var10;
      double var22 = var14;
      double var24 = var16;
      if (this.uvRotateWest == 1) {
         var10 = par8Icon.getInterpolatedU(this.renderMinY * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(this.renderMaxY * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxX * 16.0);
         var22 = var14;
         var24 = var16;
         var18 = var10;
         var20 = var12;
         var14 = var16;
         var16 = var14;
      } else if (this.uvRotateWest == 2) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMaxY * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMinY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMaxX * 16.0);
         var18 = var12;
         var20 = var10;
         var10 = var12;
         var12 = var10;
         var22 = var16;
         var24 = var14;
      } else if (this.uvRotateWest == 3) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMinX * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMaxX * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMinY * 16.0);
         var18 = var12;
         var20 = var10;
         var22 = var14;
         var24 = var16;
      }

      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMinY;
      double var32 = par4 + this.renderMaxY;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var32, var34, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var30, var34, var20, var24);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var28, var32, var34, var18, var22);
      } else {
         var9.addVertexWithUV(var26, var32, var34, var10, var14);
         var9.addVertexWithUV(var26, var30, var34, var20, var24);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.addVertexWithUV(var28, var32, var34, var18, var22);
      }
   }

   public void renderFaceXNeg(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      if (this.hasOverrideBlockTexture()) {
         par8Icon = this.overrideBlockTexture;
      }

      double var10 = par8Icon.getInterpolatedU(this.renderMinZ * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxZ * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      if (this.flipTexture) {
         double var18 = var10;
         var10 = var12;
         var12 = var18;
      }

      if (this.renderMinZ < 0.0 || this.renderMaxZ > 1.0) {
         var10 = par8Icon.getMinU();
         var12 = par8Icon.getMaxU();
      }

      if (this.renderMinY < 0.0 || this.renderMaxY > 1.0) {
         var14 = par8Icon.getMinV();
         var16 = par8Icon.getMaxV();
      }

      double var18 = var12;
      double var20 = var10;
      double var22 = var14;
      double var24 = var16;
      if (this.uvRotateNorth == 1) {
         var10 = par8Icon.getInterpolatedU(this.renderMinY * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxZ * 16.0);
         var12 = par8Icon.getInterpolatedU(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinZ * 16.0);
         var22 = var14;
         var24 = var16;
         var18 = var10;
         var20 = var12;
         var14 = var16;
         var16 = var14;
      } else if (this.uvRotateNorth == 2) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMaxY * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMinZ * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMinY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMaxZ * 16.0);
         var18 = var12;
         var20 = var10;
         var10 = var12;
         var12 = var10;
         var22 = var16;
         var24 = var14;
      } else if (this.uvRotateNorth == 3) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMinZ * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMaxZ * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMinY * 16.0);
         var18 = var12;
         var20 = var10;
         var22 = var14;
         var24 = var16;
      }

      double var26 = par2 + this.renderMinX;
      double var28 = par4 + this.renderMinY;
      double var30 = par4 + this.renderMaxY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var30, var34, var18, var22);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var26, var28, var32, var20, var24);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var28, var34, var12, var16);
      } else {
         var9.addVertexWithUV(var26, var30, var34, var18, var22);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.addVertexWithUV(var26, var28, var32, var20, var24);
         var9.addVertexWithUV(var26, var28, var34, var12, var16);
      }
   }

   public void renderFaceXPos(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      if (this.hasOverrideBlockTexture()) {
         par8Icon = this.overrideBlockTexture;
      }

      double var10 = par8Icon.getInterpolatedU(this.renderMinZ * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxZ * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      if (this.flipTexture) {
         double var18 = var10;
         var10 = var12;
         var12 = var18;
      }

      if (this.renderMinZ < 0.0 || this.renderMaxZ > 1.0) {
         var10 = par8Icon.getMinU();
         var12 = par8Icon.getMaxU();
      }

      if (this.renderMinY < 0.0 || this.renderMaxY > 1.0) {
         var14 = par8Icon.getMinV();
         var16 = par8Icon.getMaxV();
      }

      double var18 = var12;
      double var20 = var10;
      double var22 = var14;
      double var24 = var16;
      if (this.uvRotateSouth == 2) {
         var10 = par8Icon.getInterpolatedU(this.renderMinY * 16.0);
         var14 = par8Icon.getInterpolatedV(16.0 - this.renderMinZ * 16.0);
         var12 = par8Icon.getInterpolatedU(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(16.0 - this.renderMaxZ * 16.0);
         var22 = var14;
         var24 = var16;
         var18 = var10;
         var20 = var12;
         var14 = var16;
         var16 = var14;
      } else if (this.uvRotateSouth == 1) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMaxY * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMaxZ * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMinY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMinZ * 16.0);
         var18 = var12;
         var20 = var10;
         var10 = var12;
         var12 = var10;
         var22 = var16;
         var24 = var14;
      } else if (this.uvRotateSouth == 3) {
         var10 = par8Icon.getInterpolatedU(16.0 - this.renderMinZ * 16.0);
         var12 = par8Icon.getInterpolatedU(16.0 - this.renderMaxZ * 16.0);
         var14 = par8Icon.getInterpolatedV(this.renderMaxY * 16.0);
         var16 = par8Icon.getInterpolatedV(this.renderMinY * 16.0);
         var18 = var12;
         var20 = var10;
         var22 = var14;
         var24 = var16;
      }

      double var26 = par2 + this.renderMaxX;
      double var28 = par4 + this.renderMinY;
      double var30 = par4 + this.renderMaxY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var28, var34, var20, var24);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var28, var32, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var26, var30, var32, var18, var22);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var30, var34, var10, var14);
      } else {
         var9.addVertexWithUV(var26, var28, var34, var20, var24);
         var9.addVertexWithUV(var26, var28, var32, var12, var16);
         var9.addVertexWithUV(var26, var30, var32, var18, var22);
         var9.addVertexWithUV(var26, var30, var34, var10, var14);
      }
   }

   public void renderBlockAsItemVanilla(Block par1Block, int par2, float par3) {
      Tessellator var4 = Tessellator.instance;
      boolean var5 = par1Block.blockID == Block.grass.blockID;
      if (par1Block == Block.dispenser || par1Block == Block.dropper || par1Block == Block.furnaceIdle) {
         par2 = 3;
      }

      if (this.useInventoryTint) {
         int var6 = par1Block.getRenderColor(par2);
         if (var5) {
            var6 = 16777215;
         }

         float var7 = (var6 >> 16 & 0xFF) / 255.0F;
         float var8 = (var6 >> 8 & 0xFF) / 255.0F;
         float var9 = (var6 & 0xFF) / 255.0F;
         GL11.glColor4f(var7 * par3, var8 * par3, var9 * par3, 1.0F);
      }

      int var6 = par1Block.getRenderType();
      this.setRenderBoundsFromBlock(par1Block);
      if (var6 != 0 && var6 != 31 && var6 != 39 && var6 != 16 && var6 != 26) {
         if (var6 == 1) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.drawCrossedSquares(par1Block, par2, -0.5, -0.5, -0.5, 1.0F);
            var4.draw();
         } else if (var6 == 19) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBlockStemSmall(par1Block, par2, this.renderMaxY, -0.5, -0.5, -0.5);
            var4.draw();
         } else if (var6 == 23) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            var4.draw();
         } else if (var6 == 13) {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            float var7 = 0.0625F;
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 0));
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 1.0F, 0.0F);
            this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 1));
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 0.0F, -1.0F);
            var4.addTranslation(0.0F, 0.0F, var7);
            this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 2));
            var4.addTranslation(0.0F, 0.0F, -var7);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(0.0F, 0.0F, 1.0F);
            var4.addTranslation(0.0F, 0.0F, -var7);
            this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 3));
            var4.addTranslation(0.0F, 0.0F, var7);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(-1.0F, 0.0F, 0.0F);
            var4.addTranslation(var7, 0.0F, 0.0F);
            this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 4));
            var4.addTranslation(-var7, 0.0F, 0.0F);
            var4.draw();
            var4.startDrawingQuads();
            var4.setNormal(1.0F, 0.0F, 0.0F);
            var4.addTranslation(-var7, 0.0F, 0.0F);
            this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 5));
            var4.addTranslation(var7, 0.0F, 0.0F);
            var4.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         } else if (var6 == 22) {
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            ChestItemRenderHelper.instance.renderChest(par1Block, par2, par3);
            GL11.glEnable(32826);
         } else if (var6 == 6) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderBlockCropsImpl(par1Block, par2, -0.5, -0.5, -0.5);
            var4.draw();
         } else if (var6 == 2) {
            var4.startDrawingQuads();
            var4.setNormal(0.0F, -1.0F, 0.0F);
            this.renderTorchAtAngle(par1Block, -0.5, -0.5, -0.5, 0.0, 0.0, 0);
            var4.draw();
         } else if (var6 == 10) {
            for (int var14 = 0; var14 < 2; var14++) {
               if (var14 == 0) {
                  this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 0.5);
               }

               if (var14 == 1) {
                  this.setRenderBounds(0.0, 0.0, 0.5, 1.0, 0.5, 1.0);
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 0, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 1, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 2, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 3, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 4, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 5, par2));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
         } else if (var6 == 27) {
            int var14 = 0;
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            var4.startDrawingQuads();

            for (int var15 = 0; var15 < 8; var15++) {
               byte var16 = 0;
               byte var17 = 1;
               if (var15 == 0) {
                  var16 = 2;
               }

               if (var15 == 1) {
                  var16 = 3;
               }

               if (var15 == 2) {
                  var16 = 4;
               }

               if (var15 == 3) {
                  var16 = 5;
                  var17 = 2;
               }

               if (var15 == 4) {
                  var16 = 6;
                  var17 = 3;
               }

               if (var15 == 5) {
                  var16 = 7;
                  var17 = 5;
               }

               if (var15 == 6) {
                  var16 = 6;
                  var17 = 2;
               }

               if (var15 == 7) {
                  var16 = 3;
               }

               float var11 = var16 / 16.0F;
               float var12 = 1.0F - var14 / 16.0F;
               float var13 = 1.0F - (var14 + var17) / 16.0F;
               var14 += var17;
               this.setRenderBounds(0.5F - var11, var13, 0.5F - var11, 0.5F + var11, var12, 0.5F + var11);
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 0));
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 1));
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 2));
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 3));
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 4));
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 5));
            }

            var4.draw();
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         } else if (var6 == 11) {
            for (int var14 = 0; var14 < 4; var14++) {
               float var8 = 0.125F;
               if (var14 == 0) {
                  this.setRenderBounds(0.5F - var8, 0.0, 0.0, 0.5F + var8, 1.0, var8 * 2.0F);
               }

               if (var14 == 1) {
                  this.setRenderBounds(0.5F - var8, 0.0, 1.0F - var8 * 2.0F, 0.5F + var8, 1.0, 1.0);
               }

               var8 = 0.0625F;
               if (var14 == 2) {
                  this.setRenderBounds(0.5F - var8, 1.0F - var8 * 3.0F, -var8 * 2.0F, 0.5F + var8, 1.0F - var8, 1.0F + var8 * 2.0F);
               }

               if (var14 == 3) {
                  this.setRenderBounds(0.5F - var8, 0.5F - var8 * 3.0F, -var8 * 2.0F, 0.5F + var8, 0.5F - var8, 1.0F + var8 * 2.0F);
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 0));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 1));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 3));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 4));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 5));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         } else if (var6 == 21) {
            for (int var14 = 0; var14 < 3; var14++) {
               float var8x = 0.0625F;
               if (var14 == 0) {
                  this.setRenderBounds(0.5F - var8x, 0.3F, 0.0, 0.5F + var8x, 1.0, var8x * 2.0F);
               }

               if (var14 == 1) {
                  this.setRenderBounds(0.5F - var8x, 0.3F, 1.0F - var8x * 2.0F, 0.5F + var8x, 1.0, 1.0);
               }

               var8x = 0.0625F;
               if (var14 == 2) {
                  this.setRenderBounds(0.5F - var8x, 0.5, 0.0, 0.5F + var8x, 1.0F - var8x, 1.0);
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 0));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 1));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 3));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 4));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSide(par1Block, 5));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }
         } else if (var6 == 32) {
            for (int var14 = 0; var14 < 2; var14++) {
               if (var14 == 0) {
                  this.setRenderBounds(0.0, 0.0, 0.3125, 1.0, 0.8125, 0.6875);
               }

               if (var14 == 1) {
                  this.setRenderBounds(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 0, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 1, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 2, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 3, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 4, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 5, par2));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
         } else if (var6 == 35) {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            this.renderBlockAnvilOrient((BlockAnvil)par1Block, 0, 0, 0, par2, true);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         } else if (var6 == 34) {
            for (int var14 = 0; var14 < 3; var14++) {
               if (var14 == 0) {
                  this.setRenderBounds(0.125, 0.0, 0.125, 0.875, 0.1875, 0.875);
                  this.setOverrideBlockTexture(this.getBlockIcon(Block.obsidian));
               } else if (var14 == 1) {
                  this.setRenderBounds(0.1875, 0.1875, 0.1875, 0.8125, 0.875, 0.8125);
                  this.setOverrideBlockTexture(Block.beacon.getBeaconIcon());
               } else if (var14 == 2) {
                  this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
                  this.setOverrideBlockTexture(this.getBlockIcon(Block.glass));
               }

               GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
               var4.startDrawingQuads();
               var4.setNormal(0.0F, -1.0F, 0.0F);
               this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 0, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 1.0F, 0.0F);
               this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 1, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, -1.0F);
               this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 2, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(0.0F, 0.0F, 1.0F);
               this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 3, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(-1.0F, 0.0F, 0.0F);
               this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 4, par2));
               var4.draw();
               var4.startDrawingQuads();
               var4.setNormal(1.0F, 0.0F, 0.0F);
               this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 5, par2));
               var4.draw();
               GL11.glTranslatef(0.5F, 0.5F, 0.5F);
            }

            this.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
            this.clearOverrideBlockTexture();
         } else if (var6 == 38) {
            GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
            this.renderBlockHopperMetadata((BlockHopper)par1Block, 0, 0, 0, 0, true);
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
         }
      } else {
         if (var6 == 16) {
            par2 = 1;
         }

         this.setRenderBounds(par1Block.getBlockBoundsFromPoolForItemRender(par2));
         GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
         var4.startDrawingQuads();
         var4.setNormal(0.0F, -1.0F, 0.0F);
         this.renderFaceYNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 0, par2));
         var4.draw();
         if (var5 && this.useInventoryTint) {
            int var14 = par1Block.getRenderColor(par2);
            float var8xx = (var14 >> 16 & 0xFF) / 255.0F;
            float var9 = (var14 >> 8 & 0xFF) / 255.0F;
            float var10 = (var14 & 0xFF) / 255.0F;
            GL11.glColor4f(var8xx * par3, var9 * par3, var10 * par3, 1.0F);
         }

         var4.startDrawingQuads();
         var4.setNormal(0.0F, 1.0F, 0.0F);
         this.renderFaceYPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 1, par2));
         var4.draw();
         if (var5 && this.useInventoryTint) {
            GL11.glColor4f(par3, par3, par3, 1.0F);
         }

         var4.startDrawingQuads();
         var4.setNormal(0.0F, 0.0F, -1.0F);
         this.renderFaceZNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 2, par2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(0.0F, 0.0F, 1.0F);
         this.renderFaceZPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 3, par2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(-1.0F, 0.0F, 0.0F);
         this.renderFaceXNeg(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 4, par2));
         var4.draw();
         var4.startDrawingQuads();
         var4.setNormal(1.0F, 0.0F, 0.0F);
         this.renderFaceXPos(par1Block, 0.0, 0.0, 0.0, this.getBlockIconFromSideAndMetadata(par1Block, 5, par2));
         var4.draw();
         GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      }
   }

   public static boolean doesRenderIDRenderItemIn3D(int par0) {
      return par0 == 0
         ? true
         : (
            par0 == 31
               ? true
               : (
                  par0 == 39
                     ? true
                     : (
                        par0 == 13
                           ? true
                           : (
                              par0 == 10
                                 ? true
                                 : (
                                    par0 == 11
                                       ? true
                                       : (
                                          par0 == 27
                                             ? true
                                             : (
                                                par0 == 22
                                                   ? true
                                                   : (
                                                      par0 == 21
                                                         ? true
                                                         : (par0 == 16 ? true : (par0 == 26 ? true : (par0 == 32 ? true : (par0 == 34 ? true : par0 == 35))))
                                                   )
                                             )
                                       )
                                 )
                           )
                     )
               )
         );
   }

   public Icon getBlockIcon(Block par1Block, IBlockAccess par2IBlockAccess, int par3, int par4, int par5, int par6) {
      return CTMUtils.getBlockIcon(
         this.getIconSafe(par1Block.getBlockTexture(par2IBlockAccess, par3, par4, par5, par6)), this, par1Block, par2IBlockAccess, par3, par4, par5, par6
      );
   }

   public Icon getBlockIconFromSideAndMetadata(Block par1Block, int par2, int par3) {
      return CTMUtils.getBlockIcon(this.getIconSafe(par1Block.getIcon(par2, par3)), this, par1Block, par2, par3);
   }

   public Icon getBlockIconFromSide(Block par1Block, int par2) {
      return CTMUtils.getBlockIcon(this.getIconSafe(par1Block.getBlockTextureFromSide(par2)), this, par1Block, par2);
   }

   public Icon getBlockIcon(Block par1Block) {
      return this.getIconSafe(par1Block.getBlockTextureFromSide(1));
   }

   public Icon getIconSafe(Icon par1Icon) {
      return par1Icon == null ? this.minecraftRB.renderEngine.getMissingIcon(0) : par1Icon;
   }

   public boolean renderBlockFluids(Block block, int i, int j, int k) {
      BlockFluid.isAnySideBeingRendered = false;
      boolean bRenderTop = RenderPass.shouldSideBeRendered(block, this.blockAccess, i, j + 1, k, 1);
      boolean bRenderBottom = RenderPass.shouldSideBeRendered(block, this.blockAccess, i, j - 1, k, 0);
      boolean[] bRenderSides = new boolean[]{
         RenderPass.shouldSideBeRendered(block, this.blockAccess, i, j, k - 1, 2),
         RenderPass.shouldSideBeRendered(block, this.blockAccess, i, j, k + 1, 3),
         RenderPass.shouldSideBeRendered(block, this.blockAccess, i - 1, j, k, 4),
         RenderPass.shouldSideBeRendered(block, this.blockAccess, i + 1, j, k, 5)
      };
      if (!BlockFluid.isAnySideBeingRendered) {
         return false;
      } else {
         Tessellator tesselator = Tessellator.instance;
         int iColor = block.colorMultiplier(this.blockAccess, i, j, k);
         float fRed = (iColor >> 16 & 0xFF) / 255.0F;
         float fGreen = (iColor >> 8 & 0xFF) / 255.0F;
         float fBlue = (iColor & 0xFF) / 255.0F;
         float var14 = 0.5F;
         float var15 = 1.0F;
         float var16 = 0.8F;
         float var17 = 0.6F;
         double var18 = 0.0;
         double var20 = 1.0;
         Material material = block.blockMaterial;
         int iMetadata = this.blockAccess.getBlockMetadata(i, j, k);
         double var24 = this.getFluidHeight(i, j, k, material);
         double var26 = this.getFluidHeight(i, j, k + 1, material);
         double var28 = this.getFluidHeight(i + 1, j, k + 1, material);
         double var30 = this.getFluidHeight(i + 1, j, k, material);
         double var32 = 0.001F;
         if (bRenderTop) {
            Icon var36 = this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(block, 1, iMetadata)
               : this.getBlockIcon(block, this.blockAccess, i, j, k, 1);
            float var37 = (float)BlockFluid.getFlowDirection(this.blockAccess, i, j, k, material);
            if (var37 > -999.0F) {
               var36 = this.blockAccess == null
                  ? this.getBlockIconFromSideAndMetadata(block, 2, iMetadata)
                  : this.getBlockIcon(block, this.blockAccess, i, j, k, 2);
            }

            var24 -= var32;
            var26 -= var32;
            var28 -= var32;
            var30 -= var32;
            double var42;
            double var40;
            double var46;
            double var44;
            double var50;
            double var48;
            double var38;
            double var52;
            if (var37 < -999.0F) {
               var40 = var36.getInterpolatedU(0.0);
               var48 = var36.getInterpolatedV(0.0);
               var38 = var40;
               var46 = var36.getInterpolatedV(16.0);
               var44 = var36.getInterpolatedU(16.0);
               var52 = var46;
               var42 = var44;
               var50 = var48;
            } else {
               float var35 = MathHelper.sin(var37) * 0.25F;
               float var34 = MathHelper.cos(var37) * 0.25F;
               var40 = var36.getInterpolatedU(8.0F + (-var34 - var35) * 16.0F);
               var48 = var36.getInterpolatedV(8.0F + (-var34 + var35) * 16.0F);
               var38 = var36.getInterpolatedU(8.0F + (-var34 + var35) * 16.0F);
               var46 = var36.getInterpolatedV(8.0F + (var34 + var35) * 16.0F);
               var44 = var36.getInterpolatedU(8.0F + (var34 + var35) * 16.0F);
               var52 = var36.getInterpolatedV(8.0F + (var34 - var35) * 16.0F);
               var42 = var36.getInterpolatedU(8.0F + (var34 - var35) * 16.0F);
               var50 = var36.getInterpolatedV(8.0F + (-var34 - var35) * 16.0F);
            }

            tesselator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i, j, k));
            float var35 = 1.0F;
            ColorizeBlock.isSmooth = ColorizeBlock.setupBlockSmoothing(this, block, this.blockAccess, i, j, k, 7);
            if (!ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(var15 * var35 * fRed, var15 * var35 * fGreen, var15 * var35 * fBlue);
            }

            if (ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
               tesselator.addVertexWithUV(i + 0, j + var24, k + 0, var40, var48);
               tesselator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
               tesselator.addVertexWithUV(i + 0, j + var26, k + 1, var38, var46);
               tesselator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
               tesselator.addVertexWithUV(i + 1, j + var28, k + 1, var44, var52);
               tesselator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
               tesselator.addVertexWithUV(i + 1, j + var30, k + 0, var42, var50);
            } else {
               tesselator.addVertexWithUV(i + 0, j + var24, k + 0, var40, var48);
               tesselator.addVertexWithUV(i + 0, j + var26, k + 1, var38, var46);
               tesselator.addVertexWithUV(i + 1, j + var28, k + 1, var44, var52);
               tesselator.addVertexWithUV(i + 1, j + var30, k + 0, var42, var50);
            }
         }

         if (bRenderBottom) {
            tesselator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k));
            float var58 = 1.0F;
            ColorizeBlock.isSmooth = ColorizeBlock.setupBlockSmoothing(this, block, this.blockAccess, i, j, k, 6);
            if (!ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(var14 * var58, var14 * var58, var14 * var58);
            }

            if (ColorizeBlock.isSmooth) {
               this.enableAO = true;
            }

            double var10002 = i;
            double var10003 = j + var32;
            double var10004 = k;
            Icon var10005 = this.blockAccess == null ? this.getBlockIconFromSide(block, 0) : this.getBlockIcon(block, this.blockAccess, i, j, k, 0);
            this.enableAO = false;
            this.renderFaceYNeg(block, var10002, var10003, var10004, var10005);
         }

         if (bRenderSides[0]) {
            Icon texture = this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(block, 2, iMetadata)
               : this.getBlockIcon(block, this.blockAccess, i, j, k, 2);
            double var42x = i;
            double var46x = i + 1;
            double var48x = k + var32;
            double var50x = k + var32;
            float var60 = texture.getInterpolatedU(0.0);
            float var35x = texture.getInterpolatedU(8.0);
            float var34 = texture.getInterpolatedV((1.0 - var24) * 16.0 * 0.5);
            float var53 = texture.getInterpolatedV((1.0 - var30) * 16.0 * 0.5);
            float var54 = texture.getInterpolatedV(8.0);
            tesselator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1));
            ColorizeBlock.isSmooth = ColorizeBlock.setupBlockSmoothing(this, block, this.blockAccess, i, j, k, 8);
            if (!ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(var15 * var16 * fRed, var15 * var16 * fGreen, var15 * var16 * fBlue);
            }

            if (ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
               tesselator.addVertexWithUV(var42x, j + var24, var48x, var60, var34);
               tesselator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
               tesselator.addVertexWithUV(var46x, j + var30, var50x, var35x, var53);
               tesselator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
               tesselator.addVertexWithUV(var46x, j + 0, var50x, var35x, var54);
               tesselator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
               tesselator.addVertexWithUV(var42x, j + 0, var48x, var60, var54);
            } else {
               tesselator.addVertexWithUV(var42x, j + var24, var48x, var60, var34);
               tesselator.addVertexWithUV(var46x, j + var30, var50x, var35x, var53);
               tesselator.addVertexWithUV(var46x, j + 0, var50x, var35x, var54);
               tesselator.addVertexWithUV(var42x, j + 0, var48x, var60, var54);
            }
         }

         if (bRenderSides[1]) {
            Icon texturex = this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(block, 3, iMetadata)
               : this.getBlockIcon(block, this.blockAccess, i, j, k, 3);
            double var42xx = i + 1;
            double var46xx = i;
            double var48xx = k + 1 - var32;
            double var50xx = k + 1 - var32;
            float var60x = texturex.getInterpolatedU(0.0);
            float var35xx = texturex.getInterpolatedU(8.0);
            float var34x = texturex.getInterpolatedV((1.0 - var28) * 16.0 * 0.5);
            float var53x = texturex.getInterpolatedV((1.0 - var26) * 16.0 * 0.5);
            float var54x = texturex.getInterpolatedV(8.0);
            tesselator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i, j, k + 1));
            ColorizeBlock.isSmooth = ColorizeBlock.setupBlockSmoothing(this, block, this.blockAccess, i, j, k, 9);
            if (!ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(var15 * var16 * fRed, var15 * var16 * fGreen, var15 * var16 * fBlue);
            }

            if (ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
               tesselator.addVertexWithUV(var42xx, j + var28, var48xx, var60x, var34x);
               tesselator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
               tesselator.addVertexWithUV(var46xx, j + var26, var50xx, var35xx, var53x);
               tesselator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
               tesselator.addVertexWithUV(var46xx, j + 0, var50xx, var35xx, var54x);
               tesselator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
               tesselator.addVertexWithUV(var42xx, j + 0, var48xx, var60x, var54x);
            } else {
               tesselator.addVertexWithUV(var42xx, j + var28, var48xx, var60x, var34x);
               tesselator.addVertexWithUV(var46xx, j + var26, var50xx, var35xx, var53x);
               tesselator.addVertexWithUV(var46xx, j + 0, var50xx, var35xx, var54x);
               tesselator.addVertexWithUV(var42xx, j + 0, var48xx, var60x, var54x);
            }
         }

         if (bRenderSides[2]) {
            Icon texturexx = this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(block, 4, iMetadata)
               : this.getBlockIcon(block, this.blockAccess, i, j, k, 4);
            double var42xxx = i + var32;
            double var46xxx = i + var32;
            double var48xxx = k + 1;
            double var50xxx = k;
            float var60xx = texturexx.getInterpolatedU(0.0);
            float var35xxx = texturexx.getInterpolatedU(8.0);
            float var34xx = texturexx.getInterpolatedV((1.0 - var26) * 16.0 * 0.5);
            float var53xx = texturexx.getInterpolatedV((1.0 - var24) * 16.0 * 0.5);
            float var54xx = texturexx.getInterpolatedV(8.0);
            tesselator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k));
            ColorizeBlock.isSmooth = ColorizeBlock.setupBlockSmoothing(this, block, this.blockAccess, i, j, k, 10);
            if (!ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(var15 * var17 * fRed, var15 * var17 * fGreen, var15 * var17 * fBlue);
            }

            if (ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
               tesselator.addVertexWithUV(var42xxx, j + var26, var48xxx, var60xx, var34xx);
               tesselator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
               tesselator.addVertexWithUV(var46xxx, j + var24, var50xxx, var35xxx, var53xx);
               tesselator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
               tesselator.addVertexWithUV(var46xxx, j + 0, var50xxx, var35xxx, var54xx);
               tesselator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
               tesselator.addVertexWithUV(var42xxx, j + 0, var48xxx, var60xx, var54xx);
            } else {
               tesselator.addVertexWithUV(var42xxx, j + var26, var48xxx, var60xx, var34xx);
               tesselator.addVertexWithUV(var46xxx, j + var24, var50xxx, var35xxx, var53xx);
               tesselator.addVertexWithUV(var46xxx, j + 0, var50xxx, var35xxx, var54xx);
               tesselator.addVertexWithUV(var42xxx, j + 0, var48xxx, var60xx, var54xx);
            }
         }

         if (bRenderSides[3]) {
            Icon texturexxx = this.blockAccess == null
               ? this.getBlockIconFromSideAndMetadata(block, 5, iMetadata)
               : this.getBlockIcon(block, this.blockAccess, i, j, k, 5);
            double var42xxxx = i + 1 - var32;
            double var46xxxx = i + 1 - var32;
            double var48xxxx = k;
            double var50xxxx = k + 1;
            float var60xxx = texturexxx.getInterpolatedU(0.0);
            float var35xxxx = texturexxx.getInterpolatedU(8.0);
            float var34xxx = texturexxx.getInterpolatedV((1.0 - var30) * 16.0 * 0.5);
            float var53xxx = texturexxx.getInterpolatedV((1.0 - var28) * 16.0 * 0.5);
            float var54xxx = texturexxx.getInterpolatedV(8.0);
            tesselator.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k));
            ColorizeBlock.isSmooth = ColorizeBlock.setupBlockSmoothing(this, block, this.blockAccess, i, j, k, 11);
            if (!ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(var15 * var17 * fRed, var15 * var17 * fGreen, var15 * var17 * fBlue);
            }

            if (ColorizeBlock.isSmooth) {
               tesselator.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
               tesselator.addVertexWithUV(var42xxxx, j + var30, var48xxxx, var60xxx, var34xxx);
               tesselator.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
               tesselator.addVertexWithUV(var46xxxx, j + var28, var50xxxx, var35xxxx, var53xxx);
               tesselator.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
               tesselator.addVertexWithUV(var46xxxx, j + 0, var50xxxx, var35xxxx, var54xxx);
               tesselator.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
               tesselator.addVertexWithUV(var42xxxx, j + 0, var48xxxx, var60xxx, var54xxx);
            } else {
               tesselator.addVertexWithUV(var42xxxx, j + var30, var48xxxx, var60xxx, var34xxx);
               tesselator.addVertexWithUV(var46xxxx, j + var28, var50xxxx, var35xxxx, var53xxx);
               tesselator.addVertexWithUV(var46xxxx, j + 0, var50xxxx, var35xxxx, var54xxx);
               tesselator.addVertexWithUV(var42xxxx, j + 0, var48xxxx, var60xxx, var54xxx);
            }
         }

         this.renderMinY = var18;
         this.renderMaxY = var20;
         return true;
      }
   }

   public boolean renderGrassBlockWithAmbientOcclusion(Block block, int x, int y, int z, float red, float green, float blue, Icon sideOverlayIcon) {
      this.enableAO = true;
      boolean var8x = false;
      float topLeftColorMultiplier = 0.0F;
      float bottomLeftColorMultiplier = 0.0F;
      float bottomRightColorMultiplier = 0.0F;
      float topRightColorMultiplier = 0.0F;
      boolean var13 = true;
      int baseMixedBrightness = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z);
      Tessellator tessellator = Tessellator.instance;
      tessellator.setBrightness(983055);
      if (block.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
         if (this.renderMinY <= 0.0) {
            y--;
         }

         this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
         this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
         this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
         this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
         this.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z);
         this.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z - 1);
         this.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z + 1);
         this.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z);
         boolean canBlockGrassXPos = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y - 1, z)];
         boolean canBlockGrassXNeg = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y - 1, z)];
         boolean canBlockGrassZPos = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z + 1)];
         boolean canBlockGrassZNeg = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z - 1)];
         if (!canBlockGrassZNeg && !canBlockGrassXNeg) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z - 1);
            this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z - 1);
         }

         if (!canBlockGrassZPos && !canBlockGrassXNeg) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z + 1);
            this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z + 1);
         }

         if (!canBlockGrassZNeg && !canBlockGrassXPos) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z - 1);
            this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z - 1);
         }

         if (!canBlockGrassZPos && !canBlockGrassXPos) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z + 1);
            this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z + 1);
         }

         if (this.renderMinY <= 0.0) {
            y++;
         }

         int mixedBrightness = baseMixedBrightness;
         if (this.renderMinY <= 0.0 || !this.blockAccess.isBlockOpaqueCube(x, y - 1, z)) {
            mixedBrightness = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
         }

         float aoLightValue = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z);
         topLeftColorMultiplier = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + aoLightValue) / 4.0F;
         topRightColorMultiplier = (this.aoLightValueScratchYZNP + aoLightValue + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
         bottomRightColorMultiplier = (aoLightValue + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
         bottomLeftColorMultiplier = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + aoLightValue + this.aoLightValueScratchYZNN) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, mixedBrightness);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, mixedBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, mixedBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, mixedBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
         this.colorRedTopLeft *= topLeftColorMultiplier;
         this.colorGreenTopLeft *= topLeftColorMultiplier;
         this.colorBlueTopLeft *= topLeftColorMultiplier;
         this.colorRedBottomLeft *= bottomLeftColorMultiplier;
         this.colorGreenBottomLeft *= bottomLeftColorMultiplier;
         this.colorBlueBottomLeft *= bottomLeftColorMultiplier;
         this.colorRedBottomRight *= bottomRightColorMultiplier;
         this.colorGreenBottomRight *= bottomRightColorMultiplier;
         this.colorBlueBottomRight *= bottomRightColorMultiplier;
         this.colorRedTopRight *= topRightColorMultiplier;
         this.colorGreenTopRight *= topRightColorMultiplier;
         this.colorBlueTopRight *= topRightColorMultiplier;
         this.renderFaceYNeg(block, x, y, z, this.getBlockIcon(block, this.blockAccess, x, y, z, 0));
         var8x = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
         if (this.renderMaxY >= 1.0) {
            y++;
         }

         this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
         this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
         this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
         this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
         this.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z);
         this.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z);
         this.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z - 1);
         this.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z + 1);
         boolean canBlockGrassXPosx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y + 1, z)];
         boolean canBlockGrassXNegx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y + 1, z)];
         boolean canBlockGrassZPosx = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z + 1)];
         boolean canBlockGrassZNegx = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z - 1)];
         if (!canBlockGrassZNegx && !canBlockGrassXNegx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z - 1);
            this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z - 1);
         }

         if (!canBlockGrassZNegx && !canBlockGrassXPosx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z - 1);
            this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z - 1);
         }

         if (!canBlockGrassZPosx && !canBlockGrassXNegx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z + 1);
            this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z + 1);
         }

         if (!canBlockGrassZPosx && !canBlockGrassXPosx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z + 1);
            this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z + 1);
         }

         if (this.renderMaxY >= 1.0) {
            y--;
         }

         int mixedBrightness = baseMixedBrightness;
         if (this.renderMaxY >= 1.0 || !this.blockAccess.isBlockOpaqueCube(x, y + 1, z)) {
            mixedBrightness = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
         }

         float aoLightValue = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z);
         topRightColorMultiplier = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + aoLightValue) / 4.0F;
         topLeftColorMultiplier = (this.aoLightValueScratchYZPP + aoLightValue + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
         bottomLeftColorMultiplier = (aoLightValue + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
         bottomRightColorMultiplier = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + aoLightValue + this.aoLightValueScratchYZPN) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, mixedBrightness);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, mixedBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, mixedBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, mixedBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = red;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = green;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = blue;
         this.colorRedTopLeft *= topLeftColorMultiplier;
         this.colorGreenTopLeft *= topLeftColorMultiplier;
         this.colorBlueTopLeft *= topLeftColorMultiplier;
         this.colorRedBottomLeft *= bottomLeftColorMultiplier;
         this.colorGreenBottomLeft *= bottomLeftColorMultiplier;
         this.colorBlueBottomLeft *= bottomLeftColorMultiplier;
         this.colorRedBottomRight *= bottomRightColorMultiplier;
         this.colorGreenBottomRight *= bottomRightColorMultiplier;
         this.colorBlueBottomRight *= bottomRightColorMultiplier;
         this.colorRedTopRight *= topRightColorMultiplier;
         this.colorGreenTopRight *= topRightColorMultiplier;
         this.colorBlueTopRight *= topRightColorMultiplier;
         this.renderFaceYPos(block, x, y, z, this.getBlockIcon(block, this.blockAccess, x, y, z, 1));
         var8x = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
         if (this.renderMinZ <= 0.0) {
            z--;
         }

         this.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z);
         this.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z);
         this.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z);
         this.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z);
         this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
         this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
         this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
         this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
         boolean canBlockGrassXPosxx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z - 1)];
         boolean canBlockGrassXNegxx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z - 1)];
         boolean canBlockGrassZPosxx = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z - 1)];
         boolean canBlockGrassZNegxx = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z - 1)];
         if (!canBlockGrassXNegxx && !canBlockGrassZNegxx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y - 1, z);
            this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y - 1, z);
         }

         if (!canBlockGrassXNegxx && !canBlockGrassZPosxx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y + 1, z);
            this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y + 1, z);
         }

         if (!canBlockGrassXPosxx && !canBlockGrassZNegxx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y - 1, z);
            this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y - 1, z);
         }

         if (!canBlockGrassXPosxx && !canBlockGrassZPosxx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y + 1, z);
            this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y + 1, z);
         }

         if (this.renderMinZ <= 0.0) {
            z++;
         }

         int mixedBrightness = baseMixedBrightness;
         if (this.renderMinZ <= 0.0 || !this.blockAccess.isBlockOpaqueCube(x, y, z - 1)) {
            mixedBrightness = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
         }

         float aoLightValue = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z - 1);
         topLeftColorMultiplier = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + aoLightValue + this.aoLightValueScratchYZPN) / 4.0F;
         bottomLeftColorMultiplier = (aoLightValue + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
         bottomRightColorMultiplier = (this.aoLightValueScratchYZNN + aoLightValue + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
         topRightColorMultiplier = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + aoLightValue) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, mixedBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, mixedBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, mixedBrightness);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, mixedBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
         this.colorRedTopLeft *= topLeftColorMultiplier;
         this.colorGreenTopLeft *= topLeftColorMultiplier;
         this.colorBlueTopLeft *= topLeftColorMultiplier;
         this.colorRedBottomLeft *= bottomLeftColorMultiplier;
         this.colorGreenBottomLeft *= bottomLeftColorMultiplier;
         this.colorBlueBottomLeft *= bottomLeftColorMultiplier;
         this.colorRedBottomRight *= bottomRightColorMultiplier;
         this.colorGreenBottomRight *= bottomRightColorMultiplier;
         this.colorBlueBottomRight *= bottomRightColorMultiplier;
         this.colorRedTopRight *= topRightColorMultiplier;
         this.colorGreenTopRight *= topRightColorMultiplier;
         this.colorBlueTopRight *= topRightColorMultiplier;
         Icon blockIcon = this.getBlockIcon(block, this.blockAccess, x, y, z, 2);
         boolean betterGrass = RenderBlocksUtils.getGrassTexture(Block.grass, this.blockAccess, x, y, z, 2, blockIcon) != null;
         if (!betterGrass) {
            this.renderFaceZNeg(block, x, y, z, blockIcon);
         }

         this.colorRedTopLeft *= red;
         this.colorRedBottomLeft *= red;
         this.colorRedBottomRight *= red;
         this.colorRedTopRight *= red;
         this.colorGreenTopLeft *= green;
         this.colorGreenBottomLeft *= green;
         this.colorGreenBottomRight *= green;
         this.colorGreenTopRight *= green;
         this.colorBlueTopLeft *= blue;
         this.colorBlueBottomLeft *= blue;
         this.colorBlueBottomRight *= blue;
         this.colorBlueTopRight *= blue;
         if (betterGrass) {
            this.renderFaceZNeg(block, x, y, z, blockIcon);
         } else {
            this.renderFaceZNeg(block, x, y, z, sideOverlayIcon);
         }

         var8x = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
         if (this.renderMaxZ >= 1.0) {
            z++;
         }

         this.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z);
         this.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z);
         this.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z);
         this.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z);
         this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
         this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
         this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
         this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
         boolean canBlockGrassXPosxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z + 1)];
         boolean canBlockGrassXNegxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z + 1)];
         boolean canBlockGrassZPosxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x, y + 1, z + 1)];
         boolean canBlockGrassZNegxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x, y - 1, z + 1)];
         if (!canBlockGrassXNegxxx && !canBlockGrassZNegxxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y - 1, z);
            this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y - 1, z);
         }

         if (!canBlockGrassXNegxxx && !canBlockGrassZPosxxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y + 1, z);
            this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y + 1, z);
         }

         if (!canBlockGrassXPosxxx && !canBlockGrassZNegxxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y - 1, z);
            this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y - 1, z);
         }

         if (!canBlockGrassXPosxxx && !canBlockGrassZPosxxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y + 1, z);
            this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y + 1, z);
         }

         if (this.renderMaxZ >= 1.0) {
            z--;
         }

         int mixedBrightnessx = baseMixedBrightness;
         if (this.renderMaxZ >= 1.0 || !this.blockAccess.isBlockOpaqueCube(x, y, z + 1)) {
            mixedBrightnessx = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
         }

         float aoLightValuex = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z + 1);
         topLeftColorMultiplier = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + aoLightValuex + this.aoLightValueScratchYZPP) / 4.0F;
         topRightColorMultiplier = (aoLightValuex + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         bottomRightColorMultiplier = (this.aoLightValueScratchYZNP + aoLightValuex + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
         bottomLeftColorMultiplier = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + aoLightValuex) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, mixedBrightnessx);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, mixedBrightnessx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, mixedBrightnessx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, mixedBrightnessx);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
         this.colorRedTopLeft *= topLeftColorMultiplier;
         this.colorGreenTopLeft *= topLeftColorMultiplier;
         this.colorBlueTopLeft *= topLeftColorMultiplier;
         this.colorRedBottomLeft *= bottomLeftColorMultiplier;
         this.colorGreenBottomLeft *= bottomLeftColorMultiplier;
         this.colorBlueBottomLeft *= bottomLeftColorMultiplier;
         this.colorRedBottomRight *= bottomRightColorMultiplier;
         this.colorGreenBottomRight *= bottomRightColorMultiplier;
         this.colorBlueBottomRight *= bottomRightColorMultiplier;
         this.colorRedTopRight *= topRightColorMultiplier;
         this.colorGreenTopRight *= topRightColorMultiplier;
         this.colorBlueTopRight *= topRightColorMultiplier;
         Icon blockIconx = this.getBlockIcon(block, this.blockAccess, x, y, z, 3);
         boolean betterGrassx = RenderBlocksUtils.getGrassTexture(Block.grass, this.blockAccess, x, y, z, 3, blockIconx) != null;
         if (!betterGrassx) {
            this.renderFaceZPos(block, x, y, z, blockIconx);
         }

         this.colorRedTopLeft *= red;
         this.colorRedBottomLeft *= red;
         this.colorRedBottomRight *= red;
         this.colorRedTopRight *= red;
         this.colorGreenTopLeft *= green;
         this.colorGreenBottomLeft *= green;
         this.colorGreenBottomRight *= green;
         this.colorGreenTopRight *= green;
         this.colorBlueTopLeft *= blue;
         this.colorBlueBottomLeft *= blue;
         this.colorBlueBottomRight *= blue;
         this.colorBlueTopRight *= blue;
         if (betterGrassx) {
            this.renderFaceZPos(block, x, y, z, blockIconx);
         } else {
            this.renderFaceZPos(block, x, y, z, sideOverlayIcon);
         }

         var8x = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
         if (this.renderMinX <= 0.0) {
            x--;
         }

         this.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z);
         this.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z - 1);
         this.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z + 1);
         this.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z);
         this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
         this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
         this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
         this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
         boolean canBlockGrassXPosxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y + 1, z)];
         boolean canBlockGrassXNegxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y - 1, z)];
         boolean canBlockGrassZPosxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z - 1)];
         boolean canBlockGrassZNegxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x - 1, y, z + 1)];
         if (!canBlockGrassZPosxxxx && !canBlockGrassXNegxxxx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z - 1);
            this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z - 1);
         }

         if (!canBlockGrassZNegxxxx && !canBlockGrassXNegxxxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z + 1);
            this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z + 1);
         }

         if (!canBlockGrassZPosxxxx && !canBlockGrassXPosxxxx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z - 1);
            this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z - 1);
         }

         if (!canBlockGrassZNegxxxx && !canBlockGrassXPosxxxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z + 1);
            this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z + 1);
         }

         if (this.renderMinX <= 0.0) {
            x++;
         }

         int mixedBrightnessxx = baseMixedBrightness;
         if (this.renderMinX <= 0.0 || !this.blockAccess.isBlockOpaqueCube(x - 1, y, z)) {
            mixedBrightnessxx = block.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z);
         }

         float aoLightValuexx = block.getAmbientOcclusionLightValue(this.blockAccess, x - 1, y, z);
         topRightColorMultiplier = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + aoLightValuexx + this.aoLightValueScratchXZNP) / 4.0F;
         topLeftColorMultiplier = (aoLightValuexx + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
         bottomLeftColorMultiplier = (this.aoLightValueScratchXZNN + aoLightValuexx + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
         bottomRightColorMultiplier = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + aoLightValuexx) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, mixedBrightnessxx);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, mixedBrightnessxx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, mixedBrightnessxx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, mixedBrightnessxx);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
         this.colorRedTopLeft *= topLeftColorMultiplier;
         this.colorGreenTopLeft *= topLeftColorMultiplier;
         this.colorBlueTopLeft *= topLeftColorMultiplier;
         this.colorRedBottomLeft *= bottomLeftColorMultiplier;
         this.colorGreenBottomLeft *= bottomLeftColorMultiplier;
         this.colorBlueBottomLeft *= bottomLeftColorMultiplier;
         this.colorRedBottomRight *= bottomRightColorMultiplier;
         this.colorGreenBottomRight *= bottomRightColorMultiplier;
         this.colorBlueBottomRight *= bottomRightColorMultiplier;
         this.colorRedTopRight *= topRightColorMultiplier;
         this.colorGreenTopRight *= topRightColorMultiplier;
         this.colorBlueTopRight *= topRightColorMultiplier;
         Icon blockIconxx = this.getBlockIcon(block, this.blockAccess, x, y, z, 4);
         boolean betterGrassxx = RenderBlocksUtils.getGrassTexture(Block.grass, this.blockAccess, x, y, z, 4, blockIconxx) != null;
         if (!betterGrassxx) {
            this.renderFaceXNeg(block, x, y, z, blockIconxx);
         }

         this.colorRedTopLeft *= red;
         this.colorRedBottomLeft *= red;
         this.colorRedBottomRight *= red;
         this.colorRedTopRight *= red;
         this.colorGreenTopLeft *= green;
         this.colorGreenBottomLeft *= green;
         this.colorGreenBottomRight *= green;
         this.colorGreenTopRight *= green;
         this.colorBlueTopLeft *= blue;
         this.colorBlueBottomLeft *= blue;
         this.colorBlueBottomRight *= blue;
         this.colorBlueTopRight *= blue;
         if (betterGrassxx) {
            this.renderFaceXNeg(block, x, y, z, blockIconxx);
         } else {
            this.renderFaceXNeg(block, x, y, z, sideOverlayIcon);
         }

         var8x = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
         if (this.renderMaxX >= 1.0) {
            x++;
         }

         this.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z);
         this.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z - 1);
         this.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y, z + 1);
         this.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z);
         this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z);
         this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1);
         this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1);
         this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z);
         boolean canBlockGrassXPosxxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y + 1, z)];
         boolean canBlockGrassXNegxxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y - 1, z)];
         boolean canBlockGrassZPosxxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z + 1)];
         boolean canBlockGrassZNegxxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(x + 1, y, z - 1)];
         if (!canBlockGrassXNegxxxxx && !canBlockGrassZNegxxxxx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z - 1);
            this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z - 1);
         }

         if (!canBlockGrassXNegxxxxx && !canBlockGrassZPosxxxxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y - 1, z + 1);
            this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z + 1);
         }

         if (!canBlockGrassXPosxxxxx && !canBlockGrassZNegxxxxx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z - 1);
            this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z - 1);
         }

         if (!canBlockGrassXPosxxxxx && !canBlockGrassZPosxxxxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(this.blockAccess, x, y + 1, z + 1);
            this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z + 1);
         }

         if (this.renderMaxX >= 1.0) {
            x--;
         }

         int mixedBrightnessxxx = baseMixedBrightness;
         if (this.renderMaxX >= 1.0 || !this.blockAccess.isBlockOpaqueCube(x + 1, y, z)) {
            mixedBrightnessxxx = block.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z);
         }

         float aoLightValuexxx = block.getAmbientOcclusionLightValue(this.blockAccess, x + 1, y, z);
         topLeftColorMultiplier = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + aoLightValuexxx + this.aoLightValueScratchXZPP) / 4.0F;
         bottomLeftColorMultiplier = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + aoLightValuexxx) / 4.0F;
         bottomRightColorMultiplier = (this.aoLightValueScratchXZPN + aoLightValuexxx + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
         topRightColorMultiplier = (aoLightValuexxx + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, mixedBrightnessxxx);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, mixedBrightnessxxx);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, mixedBrightnessxxx);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, mixedBrightnessxxx);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
         this.colorRedTopLeft *= topLeftColorMultiplier;
         this.colorGreenTopLeft *= topLeftColorMultiplier;
         this.colorBlueTopLeft *= topLeftColorMultiplier;
         this.colorRedBottomLeft *= bottomLeftColorMultiplier;
         this.colorGreenBottomLeft *= bottomLeftColorMultiplier;
         this.colorBlueBottomLeft *= bottomLeftColorMultiplier;
         this.colorRedBottomRight *= bottomRightColorMultiplier;
         this.colorGreenBottomRight *= bottomRightColorMultiplier;
         this.colorBlueBottomRight *= bottomRightColorMultiplier;
         this.colorRedTopRight *= topRightColorMultiplier;
         this.colorGreenTopRight *= topRightColorMultiplier;
         this.colorBlueTopRight *= topRightColorMultiplier;
         Icon blockIconxxx = this.getBlockIcon(block, this.blockAccess, x, y, z, 5);
         boolean betterGrassxxx = RenderBlocksUtils.getGrassTexture(Block.grass, this.blockAccess, x, y, z, 5, blockIconxxx) != null;
         if (!betterGrassxxx) {
            this.renderFaceXPos(block, x, y, z, blockIconxxx);
         }

         this.colorRedTopLeft *= red;
         this.colorRedBottomLeft *= red;
         this.colorRedBottomRight *= red;
         this.colorRedTopRight *= red;
         this.colorGreenTopLeft *= green;
         this.colorGreenBottomLeft *= green;
         this.colorGreenBottomRight *= green;
         this.colorGreenTopRight *= green;
         this.colorBlueTopLeft *= blue;
         this.colorBlueBottomLeft *= blue;
         this.colorBlueBottomRight *= blue;
         this.colorBlueTopRight *= blue;
         if (betterGrassxxx) {
            this.renderFaceXPos(block, x, y, z, blockIconxxx);
         } else {
            this.renderFaceXPos(block, x, y, z, sideOverlayIcon);
         }

         var8x = true;
      }

      this.enableAO = false;
      return var8x;
   }

   public boolean renderGrassBlockWithColorMultiplier(Block grassBlock, int x, int y, int z, float red, float green, float blue, Icon sideOverlayIcon) {
      this.enableAO = false;
      Tessellator tessellator = Tessellator.instance;
      boolean renderSuccess = false;
      float colorBottom = 0.5F;
      float colorMultiplierTop = 1.0F;
      float var12 = 0.8F;
      float var13 = 0.6F;
      float redTop = colorMultiplierTop * red;
      float greenTop = colorMultiplierTop * green;
      float blueTop = colorMultiplierTop * blue;
      int mixedBrightness = grassBlock.getMixedBrightnessForBlock(this.blockAccess, x, y, z);
      if (grassBlock.shouldSideBeRendered(this.blockAccess, x, y - 1, z, 0)) {
         tessellator.setBrightness(this.renderMinY > 0.0 ? mixedBrightness : grassBlock.getMixedBrightnessForBlock(this.blockAccess, x, y - 1, z));
         tessellator.setColorOpaque_F(colorBottom, colorBottom, colorBottom);
         this.renderFaceYNeg(grassBlock, x, y, z, this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 0));
         renderSuccess = true;
      }

      if (grassBlock.shouldSideBeRendered(this.blockAccess, x, y + 1, z, 1)) {
         tessellator.setBrightness(this.renderMaxY < 1.0 ? mixedBrightness : grassBlock.getMixedBrightnessForBlock(this.blockAccess, x, y + 1, z));
         tessellator.setColorOpaque_F(redTop, greenTop, blueTop);
         this.renderFaceYPos(grassBlock, x, y, z, this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 1));
         renderSuccess = true;
      }

      Icon grassTopIcon = this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 1);
      if (grassBlock.shouldSideBeRendered(this.blockAccess, x, y, z - 1, 2)) {
         tessellator.setBrightness(this.renderMinZ > 0.0 ? mixedBrightness : grassBlock.getMixedBrightnessForBlock(this.blockAccess, x, y, z - 1));
         Icon blockIcon = this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 2);
         if (RenderBlocksUtils.enableBetterGrass && blockIcon == grassTopIcon) {
            tessellator.setColorOpaque_F(var12 * red, var12 * green, var12 * blue);
            this.renderFaceZNeg(grassBlock, x, y, z, blockIcon);
         } else {
            tessellator.setColorOpaque_F(var12, var12, var12);
            this.renderFaceZNeg(grassBlock, x, y, z, blockIcon);
            tessellator.setColorOpaque_F(var12 * red, var12 * green, var12 * blue);
            this.renderFaceZNeg(grassBlock, x, y, z, sideOverlayIcon);
            renderSuccess = true;
         }
      }

      if (grassBlock.shouldSideBeRendered(this.blockAccess, x, y, z + 1, 3)) {
         tessellator.setBrightness(this.renderMaxZ < 1.0 ? mixedBrightness : grassBlock.getMixedBrightnessForBlock(this.blockAccess, x, y, z + 1));
         Icon blockIcon = this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 3);
         if (RenderBlocksUtils.enableBetterGrass && blockIcon == grassTopIcon) {
            tessellator.setColorOpaque_F(var12 * red, var12 * green, var12 * blue);
            this.renderFaceZPos(grassBlock, x, y, z, blockIcon);
         } else {
            tessellator.setColorOpaque_F(var12, var12, var12);
            this.renderFaceZPos(grassBlock, x, y, z, blockIcon);
            tessellator.setColorOpaque_F(var12 * red, var12 * green, var12 * blue);
            this.renderFaceZPos(grassBlock, x, y, z, sideOverlayIcon);
         }

         renderSuccess = true;
      }

      if (grassBlock.shouldSideBeRendered(this.blockAccess, x - 1, y, z, 4)) {
         tessellator.setBrightness(this.renderMinX > 0.0 ? mixedBrightness : grassBlock.getMixedBrightnessForBlock(this.blockAccess, x - 1, y, z));
         Icon blockIcon = this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 4);
         if (RenderBlocksUtils.enableBetterGrass && blockIcon == grassTopIcon) {
            tessellator.setColorOpaque_F(var13 * red, var13 * green, var13 * blue);
            this.renderFaceXNeg(grassBlock, x, y, z, blockIcon);
         } else {
            tessellator.setColorOpaque_F(var13, var13, var13);
            this.renderFaceXNeg(grassBlock, x, y, z, blockIcon);
            tessellator.setColorOpaque_F(var13 * red, var13 * green, var13 * blue);
            this.renderFaceXNeg(grassBlock, x, y, z, sideOverlayIcon);
         }

         renderSuccess = true;
      }

      if (grassBlock.shouldSideBeRendered(this.blockAccess, x + 1, y, z, 5)) {
         tessellator.setBrightness(this.renderMaxX < 1.0 ? mixedBrightness : grassBlock.getMixedBrightnessForBlock(this.blockAccess, x + 1, y, z));
         Icon blockIcon = this.getBlockIcon(grassBlock, this.blockAccess, x, y, z, 5);
         if (RenderBlocksUtils.enableBetterGrass && blockIcon == grassTopIcon) {
            tessellator.setColorOpaque_F(var13 * red, var13 * green, var13 * blue);
            this.renderFaceXPos(grassBlock, x, y, z, blockIcon);
         } else {
            tessellator.setColorOpaque_F(var13, var13, var13);
            this.renderFaceXPos(grassBlock, x, y, z, blockIcon);
            tessellator.setColorOpaque_F(var13 * red, var13 * green, var13 * blue);
            this.renderFaceXPos(grassBlock, x, y, z, sideOverlayIcon);
         }

         renderSuccess = true;
      }

      return renderSuccess;
   }

   public boolean renderBlockByRenderType(Block block, int i, int j, int k) {
      block.currentBlockRenderer = this;
      boolean bReturnValue = block.renderBlock(this, i, j, k);
      block.renderCookingByKiLnOverlay(this, i, j, k, bReturnValue);
      block.renderBlockSecondPass(this, i, j, k, bReturnValue);
      return bReturnValue;
   }

   public void renderBlockAsItem(Block block, int iItemDamage, float fBrightness) {
      block.renderBlockAsItem(this, iItemDamage, fBrightness);
   }

   public boolean renderStandardFullBlock(Block block, int i, int j, int k) {
      this.renderMinX = this.renderMinY = this.renderMinZ = 0.0;
      this.renderMaxX = this.renderMaxY = this.renderMaxZ = 1.0;
      this.partialRenderBounds = false;
      return Minecraft.isAmbientOcclusionEnabled()
         ? this.renderStandardFullBlockWithAmbientOcclusion(block, i, j, k)
         : this.renderStandardFullBlockWithColorMultiplier(block, i, j, k);
   }

   public boolean renderStandardFullBlockWithAmbientOcclusion(Block block, int i, int j, int k) {
      this.enableAO = true;
      boolean bSideRendered = false;
      Tessellator tesselator = Tessellator.instance;
      tesselator.setBrightness(983055);
      if (block.shouldSideBeRendered(this.blockAccess, i, j - 1, k, 0)) {
         j--;
         this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k);
         this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1);
         this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k + 1);
         this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k);
         this.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k);
         this.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k - 1);
         this.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k + 1);
         this.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k);
         boolean var17 = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j - 1, k)];
         boolean var16 = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j - 1, k)];
         boolean var19 = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k + 1)];
         boolean var18 = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k - 1)];
         if (!var18 && !var16) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k - 1);
            this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k - 1);
         }

         if (!var19 && !var16) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
            this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
         } else {
            this.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k + 1);
            this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k + 1);
         }

         if (!var18 && !var17) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k - 1);
            this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k - 1);
         }

         if (!var19 && !var17) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
            this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
         } else {
            this.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k + 1);
            this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k + 1);
         }

         j++;
         int iFaceBrightness = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k);
         float var20 = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k);
         float var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + var20) / 4.0F;
         float var12 = (this.aoLightValueScratchYZNP + var20 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
         float var11 = (var20 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
         float var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + var20 + this.aoLightValueScratchYZNN) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, iFaceBrightness);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, iFaceBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, iFaceBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, iFaceBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
         this.colorRedTopLeft *= var9;
         this.colorGreenTopLeft *= var9;
         this.colorBlueTopLeft *= var9;
         this.colorRedBottomLeft *= var10;
         this.colorGreenBottomLeft *= var10;
         this.colorBlueBottomLeft *= var10;
         this.colorRedBottomRight *= var11;
         this.colorGreenBottomRight *= var11;
         this.colorBlueBottomRight *= var11;
         this.colorRedTopRight *= var12;
         this.colorGreenTopRight *= var12;
         this.colorBlueTopRight *= var12;
         this.renderFullBottomFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 0));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i, j + 1, k, 1)) {
         j++;
         this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k);
         this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k);
         this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1);
         this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k + 1);
         this.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k);
         this.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k);
         this.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k - 1);
         this.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k + 1);
         boolean var17x = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j + 1, k)];
         boolean var16x = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j + 1, k)];
         boolean var19x = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k + 1)];
         boolean var18x = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k - 1)];
         if (!var18x && !var16x) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k - 1);
            this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k - 1);
         }

         if (!var18x && !var17x) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k - 1);
            this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k - 1);
         }

         if (!var19x && !var16x) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
         } else {
            this.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k + 1);
            this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k + 1);
         }

         if (!var19x && !var17x) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
         } else {
            this.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k + 1);
            this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k + 1);
         }

         int iFaceBrightness = block.getMixedBrightnessForBlock(this.blockAccess, i, --j + 1, k);
         float var20 = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k);
         float var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + var20) / 4.0F;
         float var9 = (this.aoLightValueScratchYZPP + var20 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
         float var10 = (var20 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
         float var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + var20 + this.aoLightValueScratchYZPN) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, iFaceBrightness);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, iFaceBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, iFaceBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, iFaceBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 1.0F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 1.0F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 1.0F;
         this.colorRedTopLeft *= var9;
         this.colorGreenTopLeft *= var9;
         this.colorBlueTopLeft *= var9;
         this.colorRedBottomLeft *= var10;
         this.colorGreenBottomLeft *= var10;
         this.colorBlueBottomLeft *= var10;
         this.colorRedBottomRight *= var11;
         this.colorGreenBottomRight *= var11;
         this.colorBlueBottomRight *= var11;
         this.colorRedTopRight *= var12;
         this.colorGreenTopRight *= var12;
         this.colorBlueTopRight *= var12;
         this.renderFullTopFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 1));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2)) {
         k--;
         this.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k);
         this.aoLightValueScratchYZNN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k);
         this.aoLightValueScratchYZPN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k);
         this.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k);
         this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k);
         this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k);
         this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k);
         this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k);
         boolean var17xx = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k - 1)];
         boolean var16xx = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k - 1)];
         boolean var19xx = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k - 1)];
         boolean var18xx = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k - 1)];
         if (!var16xx && !var18xx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j - 1, k);
            this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j - 1, k);
         }

         if (!var16xx && !var19xx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j + 1, k);
            this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j + 1, k);
         }

         if (!var17xx && !var18xx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j - 1, k);
            this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j - 1, k);
         }

         if (!var17xx && !var19xx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j + 1, k);
            this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j + 1, k);
         }

         k++;
         int iFaceBrightness = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1);
         float var20 = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k - 1);
         float var9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + var20 + this.aoLightValueScratchYZPN) / 4.0F;
         float var10 = (var20 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
         float var11 = (this.aoLightValueScratchYZNN + var20 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
         float var12 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + var20) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, iFaceBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, iFaceBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, iFaceBrightness);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, iFaceBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
         this.colorRedTopLeft *= var9;
         this.colorGreenTopLeft *= var9;
         this.colorBlueTopLeft *= var9;
         this.colorRedBottomLeft *= var10;
         this.colorGreenBottomLeft *= var10;
         this.colorBlueBottomLeft *= var10;
         this.colorRedBottomRight *= var11;
         this.colorGreenBottomRight *= var11;
         this.colorBlueBottomRight *= var11;
         this.colorRedTopRight *= var12;
         this.colorGreenTopRight *= var12;
         this.colorBlueTopRight *= var12;
         this.renderFullEastFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 2));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3)) {
         k++;
         this.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k);
         this.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k);
         this.aoLightValueScratchYZNP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k);
         this.aoLightValueScratchYZPP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k);
         this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k);
         this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k);
         this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k);
         this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k);
         boolean var17xxx = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k + 1)];
         boolean var16xxx = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k + 1)];
         boolean var19xxx = Block.canBlockGrass[this.blockAccess.getBlockId(i, j + 1, k + 1)];
         boolean var18xxx = Block.canBlockGrass[this.blockAccess.getBlockId(i, j - 1, k + 1)];
         if (!var16xxx && !var18xxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j - 1, k);
            this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j - 1, k);
         }

         if (!var16xxx && !var19xxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j + 1, k);
            this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j + 1, k);
         }

         if (!var17xxx && !var18xxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j - 1, k);
            this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j - 1, k);
         }

         if (!var17xxx && !var19xxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j + 1, k);
            this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j + 1, k);
         }

         int iFaceBrightness = block.getMixedBrightnessForBlock(this.blockAccess, i, j, --k + 1);
         float var20 = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k + 1);
         float var9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + var20 + this.aoLightValueScratchYZPP) / 4.0F;
         float var12 = (var20 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         float var11 = (this.aoLightValueScratchYZNP + var20 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
         float var10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + var20) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, iFaceBrightness);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, iFaceBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, iFaceBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, iFaceBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
         this.colorRedTopLeft *= var9;
         this.colorGreenTopLeft *= var9;
         this.colorBlueTopLeft *= var9;
         this.colorRedBottomLeft *= var10;
         this.colorGreenBottomLeft *= var10;
         this.colorBlueBottomLeft *= var10;
         this.colorRedBottomRight *= var11;
         this.colorGreenBottomRight *= var11;
         this.colorBlueBottomRight *= var11;
         this.colorRedTopRight *= var12;
         this.colorGreenTopRight *= var12;
         this.colorBlueTopRight *= var12;
         this.renderFullWestFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 3));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4)) {
         this.aoLightValueScratchXYNN = block.getAmbientOcclusionLightValue(this.blockAccess, --i, j - 1, k);
         this.aoLightValueScratchXZNN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k - 1);
         this.aoLightValueScratchXZNP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k + 1);
         this.aoLightValueScratchXYNP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k);
         this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k);
         this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1);
         this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k + 1);
         this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k);
         boolean var17xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j + 1, k)];
         boolean var16xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j - 1, k)];
         boolean var19xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k - 1)];
         boolean var18xxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i - 1, j, k + 1)];
         if (!var19xxxx && !var16xxxx) {
            this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNNN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k - 1);
            this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k - 1);
         }

         if (!var18xxxx && !var16xxxx) {
            this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNNP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k + 1);
            this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k + 1);
         }

         if (!var19xxxx && !var17xxxx) {
            this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
            this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
         } else {
            this.aoLightValueScratchXYZNPN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k - 1);
            this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k - 1);
         }

         if (!var18xxxx && !var17xxxx) {
            this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
            this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
         } else {
            this.aoLightValueScratchXYZNPP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k + 1);
            this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k + 1);
         }

         i++;
         int iFaceBrightness = block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k);
         float var20 = block.getAmbientOcclusionLightValue(this.blockAccess, i - 1, j, k);
         float var12 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + var20 + this.aoLightValueScratchXZNP) / 4.0F;
         float var9 = (var20 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
         float var10 = (this.aoLightValueScratchXZNN + var20 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
         float var11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + var20) / 4.0F;
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, iFaceBrightness);
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, iFaceBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, iFaceBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, iFaceBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
         this.colorRedTopLeft *= var9;
         this.colorGreenTopLeft *= var9;
         this.colorBlueTopLeft *= var9;
         this.colorRedBottomLeft *= var10;
         this.colorGreenBottomLeft *= var10;
         this.colorBlueBottomLeft *= var10;
         this.colorRedBottomRight *= var11;
         this.colorGreenBottomRight *= var11;
         this.colorBlueBottomRight *= var11;
         this.colorRedTopRight *= var12;
         this.colorGreenTopRight *= var12;
         this.colorBlueTopRight *= var12;
         this.renderFullNorthFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 4));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5)) {
         this.aoLightValueScratchXYPN = block.getAmbientOcclusionLightValue(this.blockAccess, ++i, j - 1, k);
         this.aoLightValueScratchXZPN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k - 1);
         this.aoLightValueScratchXZPP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j, k + 1);
         this.aoLightValueScratchXYPP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k);
         this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k);
         this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1);
         this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k + 1);
         this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k);
         boolean var17xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j + 1, k)];
         boolean var16xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j - 1, k)];
         boolean var19xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k + 1)];
         boolean var18xxxxx = Block.canBlockGrass[this.blockAccess.getBlockId(i + 1, j, k - 1)];
         if (!var16xxxxx && !var18xxxxx) {
            this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPNN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k - 1);
            this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k - 1);
         }

         if (!var16xxxxx && !var19xxxxx) {
            this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPNP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j - 1, k + 1);
            this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k + 1);
         }

         if (!var17xxxxx && !var18xxxxx) {
            this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
            this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
         } else {
            this.aoLightValueScratchXYZPPN = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k - 1);
            this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k - 1);
         }

         if (!var17xxxxx && !var19xxxxx) {
            this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
            this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
         } else {
            this.aoLightValueScratchXYZPPP = block.getAmbientOcclusionLightValue(this.blockAccess, i, j + 1, k + 1);
            this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k + 1);
         }

         int iFaceBrightness = block.getMixedBrightnessForBlock(this.blockAccess, --i + 1, j, k);
         float var20 = block.getAmbientOcclusionLightValue(this.blockAccess, i + 1, j, k);
         float var9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + var20 + this.aoLightValueScratchXZPP) / 4.0F;
         float var10 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + var20) / 4.0F;
         float var11 = (this.aoLightValueScratchXZPN + var20 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
         float var12 = (var20 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
         this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, iFaceBrightness);
         this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, iFaceBrightness);
         this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, iFaceBrightness);
         this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, iFaceBrightness);
         this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
         this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
         this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
         this.colorRedTopLeft *= var9;
         this.colorGreenTopLeft *= var9;
         this.colorBlueTopLeft *= var9;
         this.colorRedBottomLeft *= var10;
         this.colorGreenBottomLeft *= var10;
         this.colorBlueBottomLeft *= var10;
         this.colorRedBottomRight *= var11;
         this.colorGreenBottomRight *= var11;
         this.colorBlueBottomRight *= var11;
         this.colorRedTopRight *= var12;
         this.colorGreenTopRight *= var12;
         this.colorBlueTopRight *= var12;
         this.renderFullSouthFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 5));
         bSideRendered = true;
      }

      this.enableAO = false;
      return bSideRendered;
   }

   public boolean renderStandardFullBlockWithColorMultiplier(Block block, int i, int j, int k) {
      this.enableAO = false;
      Tessellator bTesselator = Tessellator.instance;
      boolean bSideRendered = false;
      int iMixedBrightness = block.getMixedBrightnessForBlock(this.blockAccess, i, j, k);
      if (block.shouldSideBeRendered(this.blockAccess, i, j - 1, k, 0)) {
         bTesselator.setBrightness(this.renderMinY > 0.0 ? iMixedBrightness : block.getMixedBrightnessForBlock(this.blockAccess, i, j - 1, k));
         bTesselator.setColorOpaque_F(0.5F, 0.5F, 0.5F);
         this.renderFullBottomFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 0));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i, j + 1, k, 1)) {
         bTesselator.setBrightness(this.renderMaxY < 1.0 ? iMixedBrightness : block.getMixedBrightnessForBlock(this.blockAccess, i, j + 1, k));
         bTesselator.setColorOpaque_F(1.0F, 1.0F, 1.0F);
         this.renderFullTopFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 1));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i, j, k - 1, 2)) {
         bTesselator.setBrightness(this.renderMinZ > 0.0 ? iMixedBrightness : block.getMixedBrightnessForBlock(this.blockAccess, i, j, k - 1));
         bTesselator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
         this.renderFullEastFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 2));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i, j, k + 1, 3)) {
         bTesselator.setBrightness(this.renderMaxZ < 1.0 ? iMixedBrightness : block.getMixedBrightnessForBlock(this.blockAccess, i, j, k + 1));
         bTesselator.setColorOpaque_F(0.8F, 0.8F, 0.8F);
         this.renderFullWestFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 3));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i - 1, j, k, 4)) {
         bTesselator.setBrightness(this.renderMinX > 0.0 ? iMixedBrightness : block.getMixedBrightnessForBlock(this.blockAccess, i - 1, j, k));
         bTesselator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
         this.renderFullNorthFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 4));
         bSideRendered = true;
      }

      if (block.shouldSideBeRendered(this.blockAccess, i + 1, j, k, 5)) {
         bTesselator.setBrightness(this.renderMaxX < 1.0 ? iMixedBrightness : block.getMixedBrightnessForBlock(this.blockAccess, i + 1, j, k));
         bTesselator.setColorOpaque_F(0.6F, 0.6F, 0.6F);
         this.renderFullSouthFace(block, i, j, k, this.getBlockIcon(block, this.blockAccess, i, j, k, 5));
         bSideRendered = true;
      }

      return bSideRendered;
   }

   public void renderFullBottomFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(this.renderMinZ * 16.0);
      double var16 = par8Icon.getInterpolatedV(this.renderMaxZ * 16.0);
      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMinY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var30, var34, var10, var16);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var28, var30, var32, var12, var14);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
      } else {
         var9.addVertexWithUV(var26, var30, var34, var10, var16);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.addVertexWithUV(var28, var30, var32, var12, var14);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
      }
   }

   public void renderFullTopFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(this.renderMinZ * 16.0);
      double var16 = par8Icon.getInterpolatedV(this.renderMaxZ * 16.0);
      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMaxY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var28, var30, var32, var12, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var30, var34, var10, var16);
      } else {
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.addVertexWithUV(var28, var30, var32, var12, var14);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.addVertexWithUV(var26, var30, var34, var10, var16);
      }
   }

   public void renderFullEastFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMinY;
      double var32 = par4 + this.renderMaxY;
      double var34 = par6 + this.renderMinZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var32, var34, var12, var14);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var28, var32, var34, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var28, var30, var34, var10, var16);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var30, var34, var12, var16);
      } else {
         var9.addVertexWithUV(var26, var32, var34, var12, var14);
         var9.addVertexWithUV(var28, var32, var34, var10, var14);
         var9.addVertexWithUV(var28, var30, var34, var10, var16);
         var9.addVertexWithUV(var26, var30, var34, var12, var16);
      }
   }

   public void renderFullWestFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      double var10 = par8Icon.getInterpolatedU(this.renderMinX * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxX * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      double var26 = par2 + this.renderMinX;
      double var28 = par2 + this.renderMaxX;
      double var30 = par4 + this.renderMinY;
      double var32 = par4 + this.renderMaxY;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var32, var34, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var30, var34, var10, var16);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var28, var32, var34, var12, var14);
      } else {
         var9.addVertexWithUV(var26, var32, var34, var10, var14);
         var9.addVertexWithUV(var26, var30, var34, var10, var16);
         var9.addVertexWithUV(var28, var30, var34, var12, var16);
         var9.addVertexWithUV(var28, var32, var34, var12, var14);
      }
   }

   public void renderFullNorthFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      double var10 = par8Icon.getInterpolatedU(this.renderMinZ * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxZ * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      double var26 = par2 + this.renderMinX;
      double var28 = par4 + this.renderMinY;
      double var30 = par4 + this.renderMaxY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var30, var34, var12, var14);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var26, var28, var32, var10, var16);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var28, var34, var12, var16);
      } else {
         var9.addVertexWithUV(var26, var30, var34, var12, var14);
         var9.addVertexWithUV(var26, var30, var32, var10, var14);
         var9.addVertexWithUV(var26, var28, var32, var10, var16);
         var9.addVertexWithUV(var26, var28, var34, var12, var16);
      }
   }

   public void renderFullSouthFace(Block par1Block, double par2, double par4, double par6, Icon par8Icon) {
      Tessellator var9 = Tessellator.instance;
      double var10 = par8Icon.getInterpolatedU(this.renderMinZ * 16.0);
      double var12 = par8Icon.getInterpolatedU(this.renderMaxZ * 16.0);
      double var14 = par8Icon.getInterpolatedV(16.0 - this.renderMaxY * 16.0);
      double var16 = par8Icon.getInterpolatedV(16.0 - this.renderMinY * 16.0);
      double var26 = par2 + this.renderMaxX;
      double var28 = par4 + this.renderMinY;
      double var30 = par4 + this.renderMaxY;
      double var32 = par6 + this.renderMinZ;
      double var34 = par6 + this.renderMaxZ;
      if (this.enableAO) {
         var9.setColorOpaque_F(this.colorRedTopLeft, this.colorGreenTopLeft, this.colorBlueTopLeft);
         var9.setBrightness(this.brightnessTopLeft);
         var9.addVertexWithUV(var26, var28, var34, var10, var16);
         var9.setColorOpaque_F(this.colorRedBottomLeft, this.colorGreenBottomLeft, this.colorBlueBottomLeft);
         var9.setBrightness(this.brightnessBottomLeft);
         var9.addVertexWithUV(var26, var28, var32, var12, var16);
         var9.setColorOpaque_F(this.colorRedBottomRight, this.colorGreenBottomRight, this.colorBlueBottomRight);
         var9.setBrightness(this.brightnessBottomRight);
         var9.addVertexWithUV(var26, var30, var32, var12, var14);
         var9.setColorOpaque_F(this.colorRedTopRight, this.colorGreenTopRight, this.colorBlueTopRight);
         var9.setBrightness(this.brightnessTopRight);
         var9.addVertexWithUV(var26, var30, var34, var10, var14);
      } else {
         var9.addVertexWithUV(var26, var28, var34, var10, var16);
         var9.addVertexWithUV(var26, var28, var32, var12, var16);
         var9.addVertexWithUV(var26, var30, var32, var12, var14);
         var9.addVertexWithUV(var26, var30, var34, var10, var14);
      }
   }

   public Icon getOverrideTexture() {
      return this.overrideBlockTexture;
   }

   public void setUVRotateEast(int iValue) {
      this.uvRotateEast = iValue;
   }

   public void setUVRotateWest(int iValue) {
      this.uvRotateWest = iValue;
   }

   public void setUVRotateSouth(int iValue) {
      this.uvRotateSouth = iValue;
   }

   public void setUVRotateNorth(int iValue) {
      this.uvRotateNorth = iValue;
   }

   public void setUVRotateTop(int iValue) {
      this.uvRotateTop = iValue;
   }

   public void setUVRotateBottom(int iValue) {
      this.uvRotateBottom = iValue;
   }

   public void clearUVRotation() {
      this.uvRotateEast = 0;
      this.uvRotateWest = 0;
      this.uvRotateSouth = 0;
      this.uvRotateNorth = 0;
      this.uvRotateTop = 0;
      this.uvRotateBottom = 0;
   }

   public boolean getRenderAllFaces() {
      return this.renderAllFaces;
   }

   public void setRenderAllFaces(boolean bValue) {
      this.renderAllFaces = bValue;
   }

   public void setRenderBounds(AxisAlignedBB box) {
      if (!this.lockBlockBounds) {
         this.renderMinX = box.minX;
         this.renderMaxX = box.maxX;
         this.renderMinY = box.minY;
         this.renderMaxY = box.maxY;
         this.renderMinZ = box.minZ;
         this.renderMaxZ = box.maxZ;
         this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2
            && (
               this.renderMinX > 0.0
                  || this.renderMaxX < 1.0
                  || this.renderMinY > 0.0
                  || this.renderMaxY < 1.0
                  || this.renderMinZ > 0.0
                  || this.renderMaxZ < 1.0
            );
      }
   }

   public void renderStandardFallingBlock(Block block, int i, int j, int k, int iMetadata) {
      this.enableAO = false;
      Tessellator tess = Tessellator.instance;
      tess.setBrightness(block.getMixedBrightnessForBlock(this.blockAccess, i, j, k));
      float fBottomColor = 0.5F;
      float fTopColor = 1.0F;
      float fZColor = 0.8F;
      float fXColor = 0.6F;
      int iColorMultiplier = block.colorMultiplier(this.blockAccess, i, j, k);
      float fRedMul = (iColorMultiplier >> 16 & 0xFF) / 255.0F;
      float fGreenMul = (iColorMultiplier >> 8 & 0xFF) / 255.0F;
      float fBlueMul = (iColorMultiplier & 0xFF) / 255.0F;
      if (block.shouldSideBeRenderedOnFallingBlock(0, iMetadata)) {
         tess.setColorOpaque_F(0.5F * fRedMul, 0.5F * fGreenMul, 0.5F * fBlueMul);
         this.renderFaceYNeg(block, i, j, k, this.getBlockIconFromSideAndMetadata(block, 0, iMetadata));
      }

      if (block.shouldSideBeRenderedOnFallingBlock(1, iMetadata)) {
         tess.setColorOpaque_F(1.0F * fRedMul, 1.0F * fGreenMul, 1.0F * fBlueMul);
         this.renderFaceYPos(block, i, j, k, this.getBlockIconFromSideAndMetadata(block, 1, iMetadata));
      }

      if (block.shouldSideBeRenderedOnFallingBlock(2, iMetadata)) {
         tess.setColorOpaque_F(0.8F * fRedMul, 0.8F * fGreenMul, 0.8F * fBlueMul);
         this.renderFaceZNeg(block, i, j, k, this.getBlockIconFromSideAndMetadata(block, 2, iMetadata));
      }

      if (block.shouldSideBeRenderedOnFallingBlock(3, iMetadata)) {
         tess.setColorOpaque_F(0.8F * fRedMul, 0.8F * fGreenMul, 0.8F * fBlueMul);
         this.renderFaceZPos(block, i, j, k, this.getBlockIconFromSideAndMetadata(block, 3, iMetadata));
      }

      if (block.shouldSideBeRenderedOnFallingBlock(4, iMetadata)) {
         tess.setColorOpaque_F(0.6F * fRedMul, 0.6F * fGreenMul, 0.6F * fBlueMul);
         this.renderFaceXNeg(block, i, j, k, this.getBlockIconFromSideAndMetadata(block, 4, iMetadata));
      }

      if (block.shouldSideBeRenderedOnFallingBlock(5, iMetadata)) {
         tess.setColorOpaque_F(0.6F * fRedMul, 0.6F * fGreenMul, 0.6F * fBlueMul);
         this.renderFaceXPos(block, i, j, k, this.getBlockIconFromSideAndMetadata(block, 5, iMetadata));
      }
   }

   public void renderStandardFullBlockMovedByPiston(Block block, int i, int j, int k) {
      this.setRenderAllFaces(true);
      this.setRenderBoundsFromBlock(block);
      this.renderStandardBlock(block, i, j, k);
      this.setRenderAllFaces(false);
   }

   public boolean shouldSideBeRenderedBasedOnCurrentBounds(int iNeighborI, int iNeighborJ, int iNeighborK, int iSide) {
      if ((iSide != 0 || !(this.renderMinY > 0.0))
         && (iSide != 1 || !(this.renderMaxY < 1.0))
         && (iSide != 2 || !(this.renderMinZ > 0.0))
         && (iSide != 3 || !(this.renderMaxZ < 1.0))
         && (iSide != 4 || !(this.renderMinX > 0.0))
         && (iSide != 5 || !(this.renderMaxX < 1.0))) {
         Block neighborBlock = Block.blocksList[this.blockAccess.getBlockId(iNeighborI, iNeighborJ, iNeighborK)];
         return neighborBlock != null ? neighborBlock.shouldRenderNeighborFullFaceSide(this.blockAccess, iNeighborI, iNeighborJ, iNeighborK, iSide) : true;
      } else {
         return true;
      }
   }
}
