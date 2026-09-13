package btw.util;

import btw.block.util.RayTraceUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Icon;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import org.lwjgl.opengl.GL11;

public class PrimitiveQuad extends PrimitiveGeometric {
   private Vec3[] vertices = new Vec3[4];
   private float minUFrac = 0.0F;
   private float minVFrac = 0.0F;
   private float maxUFrac = 1.0F;
   private float maxVFrac = 1.0F;
   private int iconIndex = 0;
   private static final double MIND_THE_GAP = 1.0E-4;

   public PrimitiveQuad(Vec3 vertex1, Vec3 vertex2, Vec3 vertex3, Vec3 vertex4) {
      this.vertices[0] = vertex1;
      this.vertices[1] = vertex2;
      this.vertices[2] = vertex3;
      this.vertices[3] = vertex4;
   }

   @Override
   public void rotateAroundYToFacing(int iFacing) {
      this.vertices[0].rotateAsBlockPosAroundJToFacing(iFacing);
      this.vertices[1].rotateAsBlockPosAroundJToFacing(iFacing);
      this.vertices[2].rotateAsBlockPosAroundJToFacing(iFacing);
      this.vertices[3].rotateAsBlockPosAroundJToFacing(iFacing);
   }

   @Override
   public void tiltToFacingAlongY(int iFacing) {
      this.vertices[0].tiltAsBlockPosToFacingAlongJ(iFacing);
      this.vertices[1].tiltAsBlockPosToFacingAlongJ(iFacing);
      this.vertices[2].tiltAsBlockPosToFacingAlongJ(iFacing);
      this.vertices[3].tiltAsBlockPosToFacingAlongJ(iFacing);
   }

   @Override
   public void translate(double dDeltaX, double dDeltaY, double dDeltaZ) {
      this.vertices[0].addVector(dDeltaX, dDeltaY, dDeltaZ);
      this.vertices[1].addVector(dDeltaX, dDeltaY, dDeltaZ);
      this.vertices[2].addVector(dDeltaX, dDeltaY, dDeltaZ);
      this.vertices[3].addVector(dDeltaX, dDeltaY, dDeltaZ);
   }

   @Override
   public void addToRayTrace(RayTraceUtils rayTrace) {
      rayTrace.addQuadWithLocalCoordsToIntersectionList(this, this.vertices[0]);
   }

   public PrimitiveQuad makeTemporaryCopy() {
      PrimitiveQuad newQuad = new PrimitiveQuad(
         Vec3.createVectorHelper(this.vertices[0]),
         Vec3.createVectorHelper(this.vertices[1]),
         Vec3.createVectorHelper(this.vertices[2]),
         Vec3.createVectorHelper(this.vertices[3])
      );
      newQuad.setUVFractions(this.minUFrac, this.minVFrac, this.maxUFrac, this.maxVFrac);
      newQuad.setIconIndex(this.iconIndex);
      return newQuad;
   }

   public boolean isPointOnPlaneWithinBounds(Vec3 point) {
      Vec3 minBounds = Vec3.createVectorHelper(this.vertices[0]);
      Vec3 maxBounds = Vec3.createVectorHelper(this.vertices[0]);
      this.computeBounds(minBounds, maxBounds);
      return (maxBounds.xCoord - minBounds.xCoord < 1.0E-4 || point.xCoord >= minBounds.xCoord && point.xCoord <= maxBounds.xCoord)
         && (maxBounds.yCoord - minBounds.yCoord < 1.0E-4 || point.yCoord >= minBounds.yCoord && point.yCoord <= maxBounds.yCoord)
         && (maxBounds.zCoord - minBounds.zCoord < 1.0E-4 || point.zCoord >= minBounds.zCoord && point.zCoord <= maxBounds.zCoord);
   }

   public void computeBounds(Vec3 min, Vec3 max) {
      for (int iTempCount = 1; iTempCount <= 3; iTempCount++) {
         Vec3 tempPoint = this.vertices[iTempCount];
         if (tempPoint.xCoord < min.xCoord) {
            min.xCoord = tempPoint.xCoord;
         } else if (tempPoint.xCoord > max.xCoord) {
            max.xCoord = tempPoint.xCoord;
         }

         if (tempPoint.yCoord < min.yCoord) {
            min.yCoord = tempPoint.yCoord;
         } else if (tempPoint.yCoord > max.yCoord) {
            max.yCoord = tempPoint.yCoord;
         }

         if (tempPoint.zCoord < min.zCoord) {
            min.zCoord = tempPoint.zCoord;
         } else if (tempPoint.zCoord > max.zCoord) {
            max.zCoord = tempPoint.zCoord;
         }
      }
   }

   public Vec3 computeNormal() {
      Vec3 vec1 = this.vertices[0].subtractFrom(this.vertices[1]);
      Vec3 vec2 = this.vertices[0].subtractFrom(this.vertices[3]);
      return vec1.crossProduct(vec2);
   }

   public PrimitiveQuad setUVFractions(float fMinUFrac, float fMinVFrac, float fMaxVFrac, float fMaxUFrac) {
      this.minUFrac = fMinUFrac;
      this.minVFrac = fMinVFrac;
      this.maxVFrac = fMaxVFrac;
      this.maxUFrac = fMaxUFrac;
      return this;
   }

   public PrimitiveQuad setIconIndex(int iIconIndex) {
      this.iconIndex = iIconIndex;
      return this;
   }

   @Environment(EnvType.CLIENT)
   private void addVertices(int i, int j, int k, Icon icon) {
      double dUDelta = icon.getMaxU() - icon.getMinU();
      double dVDelta = icon.getMaxV() - icon.getMinV();
      double dMinU = icon.getMinU() + dUDelta * this.minUFrac;
      double dMinV = icon.getMinV() + dVDelta * this.minVFrac;
      double dMaxU = icon.getMinU() + dUDelta * this.maxUFrac;
      double dMaxV = icon.getMinV() + dVDelta * this.maxVFrac;
      Tessellator.instance.addVertexWithUV(i + this.vertices[0].xCoord, j + this.vertices[0].yCoord, k + this.vertices[0].zCoord, dMinU, dMinV);
      Tessellator.instance.addVertexWithUV(i + this.vertices[1].xCoord, j + this.vertices[1].yCoord, k + this.vertices[1].zCoord, dMinU, dMaxV);
      Tessellator.instance.addVertexWithUV(i + this.vertices[2].xCoord, j + this.vertices[2].yCoord, k + this.vertices[2].zCoord, dMaxU, dMaxV);
      Tessellator.instance.addVertexWithUV(i + this.vertices[3].xCoord, j + this.vertices[3].yCoord, k + this.vertices[3].zCoord, dMaxU, dMinV);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlock(RenderBlocks renderBlocks, Block block, int i, int j, int k) {
      Icon icon = block.getIconByIndex(this.iconIndex);
      Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, i, j, k));
      Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      this.addVertices(i, j, k, icon);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlockWithColorMultiplier(RenderBlocks renderBlocks, Block block, int i, int j, int k, float fRed, float fGreen, float fBlue) {
      Icon icon = block.getIconByIndex(this.iconIndex);
      Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, i, j, k));
      Tessellator.instance.setColorOpaque_F(fRed, fGreen, fBlue);
      this.addVertices(i, j, k, icon);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlockWithTexture(RenderBlocks renderBlocks, Block block, int i, int j, int k, Icon icon) {
      Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(renderBlocks.blockAccess, i, j, k));
      Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      this.addVertices(i, j, k, icon);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean renderAsBlockFullBrightWithTexture(RenderBlocks renderBlocks, Block block, int i, int j, int k, Icon icon) {
      Tessellator.instance.setBrightness(renderBlocks.blockAccess.getLightBrightnessForSkyBlocks(i, j, k, 15));
      Tessellator.instance.setColorOpaque_F(1.0F, 1.0F, 1.0F);
      this.addVertices(i, j, k, icon);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderAsItemBlock(RenderBlocks renderBlocks, Block block, int iItemDamage) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      tessellator.startDrawingQuads();
      Vec3 normal = this.computeNormal().normalize();
      tessellator.setNormal((float)normal.xCoord, (float)normal.yCoord, (float)normal.zCoord);
      Icon icon = block.getIconByIndex(this.iconIndex);
      double dUDelta = icon.getMaxU() - icon.getMinU();
      double dVDelta = icon.getMaxV() - icon.getMinV();
      double dMinU = icon.getMinU() + dUDelta * this.minUFrac;
      double dMinV = icon.getMinV() + dVDelta * this.minVFrac;
      double dMaxU = icon.getMinU() + dUDelta * this.maxUFrac;
      double dMaxV = icon.getMinV() + dVDelta * this.maxVFrac;
      tessellator.addVertexWithUV(this.vertices[0].xCoord, this.vertices[0].yCoord, this.vertices[0].zCoord, dMinU, dMinV);
      tessellator.addVertexWithUV(this.vertices[1].xCoord, this.vertices[1].yCoord, this.vertices[1].zCoord, dMinU, dMaxV);
      tessellator.addVertexWithUV(this.vertices[2].xCoord, this.vertices[2].yCoord, this.vertices[2].zCoord, dMaxU, dMaxV);
      tessellator.addVertexWithUV(this.vertices[3].xCoord, this.vertices[3].yCoord, this.vertices[3].zCoord, dMaxU, dMinV);
      tessellator.draw();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void renderAsFallingBlock(RenderBlocks renderBlocks, Block block, int i, int j, int k, int iMetadata) {
      this.renderAsBlock(renderBlocks, block, i, j, k);
   }
}
