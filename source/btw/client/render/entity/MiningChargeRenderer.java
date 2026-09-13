package btw.client.render.entity;

import btw.block.BTWBlocks;
import btw.block.blocks.MiningChargeBlock;
import btw.entity.MiningChargeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.Render;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class MiningChargeRenderer extends Render {
   private RenderBlocks blockRenderer = new RenderBlocks();

   public MiningChargeRenderer() {
      this.shadowSize = 0.5F;
   }

   @Override
   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderMiningCharge((MiningChargeEntity)entity, d, d1, d2, f, f1);
   }

   public void renderMiningCharge(MiningChargeEntity miningCharge, double d, double d1, double d2, float f, float f1) {
      int iFacing = miningCharge.facing;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      if (miningCharge.fuse - f1 + 1.0F < 10.0F) {
         float fScaleFactor = 1.0F - (miningCharge.fuse - f1 + 1.0F) / 10.0F;
         if (fScaleFactor < 0.0F) {
            fScaleFactor = 0.0F;
         }

         if (fScaleFactor > 1.0F) {
            fScaleFactor = 1.0F;
         }

         fScaleFactor *= fScaleFactor;
         fScaleFactor *= fScaleFactor;
         float fScale = 1.0F + fScaleFactor * 0.3F;
         GL11.glScalef(fScale, fScale, fScale);
      }

      float f3 = (1.0F - (miningCharge.fuse - f1 + 1.0F) / 100.0F) * 0.8F;
      this.a("/terrain.png");
      this.renderMiningChargeBlock(iFacing, miningCharge.c(f1));
      if (miningCharge.fuse / 5 % 2 == 0) {
         GL11.glDisable(3553);
         GL11.glDisable(2896);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 772);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, f3);
         this.renderMiningChargeBlock(iFacing, 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glDisable(3042);
         GL11.glEnable(2896);
         GL11.glEnable(3553);
      }

      GL11.glPopMatrix();
   }

   public void renderMiningChargeBlock(int iFacing, float fColorMultiplier) {
      MiningChargeBlock block = (MiningChargeBlock)BTWBlocks.miningCharge;
      Tessellator tessellator = Tessellator.instance;
      int iRenderColor = block.b(0);
      float fRed = (iRenderColor >> 16 & 0xFF) / 255.0F;
      float fGreen = (iRenderColor >> 8 & 0xFF) / 255.0F;
      float fBlue = (iRenderColor & 0xFF) / 255.0F;
      GL11.glColor4f(fRed * fColorMultiplier, fGreen * fColorMultiplier, fBlue * fColorMultiplier, 1.0F);
      this.setRenderBoundsBasedOnFacing(iFacing);
      GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, -1.0F, 0.0F);
      this.blockRenderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromMetadataCustom(0, iFacing));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      this.blockRenderer.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromMetadataCustom(1, iFacing));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, -1.0F);
      this.blockRenderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromMetadataCustom(2, iFacing));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 0.0F, 1.0F);
      this.blockRenderer.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromMetadataCustom(3, iFacing));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(-1.0F, 0.0F, 0.0F);
      this.blockRenderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getBlockTextureFromMetadataCustom(4, iFacing));
      tessellator.draw();
      tessellator.startDrawingQuads();
      tessellator.setNormal(1.0F, 0.0F, 0.0F);
      this.blockRenderer.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getBlockTextureFromMetadataCustom(5, iFacing));
      tessellator.draw();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
   }

   void setRenderBoundsBasedOnFacing(int iFacing) {
      MiningChargeBlock block = (MiningChargeBlock)BTWBlocks.miningCharge;
      double dBoundingBoxHeight = 0.5;
      switch (iFacing) {
         case 0:
            this.blockRenderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, dBoundingBoxHeight, 1.0);
            break;
         case 1:
            this.blockRenderer.setRenderBounds(0.0, dBoundingBoxHeight, 0.0, 1.0, 1.0, 1.0);
            break;
         case 2:
            this.blockRenderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, dBoundingBoxHeight);
            break;
         case 3:
            this.blockRenderer.setRenderBounds(0.0, 0.0, dBoundingBoxHeight, 1.0, 1.0, 1.0);
            break;
         case 4:
            this.blockRenderer.setRenderBounds(0.0, 0.0, 0.0, dBoundingBoxHeight, 1.0, 1.0);
            break;
         default:
            this.blockRenderer.setRenderBounds(dBoundingBoxHeight, 0.0, 0.0, 1.0, 1.0, 1.0);
      }
   }
}
