package btw.client.render.tileentity;

import btw.block.blocks.BasketBlock;
import btw.block.model.BlockModel;
import btw.block.tileentity.BasketTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySpecialRenderer;
import net.minecraft.src.Vec3;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class BasketRenderer extends TileEntitySpecialRenderer {
   protected RenderBlocks localRenderBlocks = new RenderBlocks();

   @Override
   public void renderTileEntityAt(TileEntity tileEntity, double xCoord, double yCoord, double zCoord, float fPartialTickCount) {
      BasketTileEntity basket = (BasketTileEntity)tileEntity;
      Block block = Block.blocksList[basket.worldObj.getBlockId(basket.xCoord, basket.yCoord, basket.zCoord)];
      if (block instanceof BasketBlock) {
         this.renderBasketLidAsBlock(basket, (BasketBlock)block, xCoord, yCoord, zCoord, fPartialTickCount);
      }
   }

   private void renderBasketLidAsBlock(BasketTileEntity tileEntity, BasketBlock block, double xCoord, double yCoord, double zCoord, float fPartialTickCount) {
      int iMetadata = tileEntity.worldObj.getBlockMetadata(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
      if (block.getIsOpen(iMetadata)) {
         int iFacing = block.getFacing(iMetadata);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)xCoord, (float)yCoord, (float)zCoord);
         GL11.glDisable(2896);
         this.a("/terrain.png");
         this.localRenderBlocks.blockAccess = tileEntity.worldObj;
         Tessellator.instance.startDrawingQuads();
         Tessellator.instance.setTranslation(-tileEntity.xCoord, -tileEntity.yCoord, -tileEntity.zCoord);
         BlockModel transformedModel = block.getLidModel(iMetadata).makeTemporaryCopy();
         transformedModel.rotateAroundYToFacing(iFacing);
         this.localRenderBlocks.setUVRotateTop(block.convertFacingToTopTextureRotation(iFacing));
         this.localRenderBlocks.setUVRotateBottom(block.convertFacingToBottomTextureRotation(iFacing));
         block.openLidBrightness = block.getMixedBrightnessForBlock(tileEntity.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
         block.renderingOpenLid = true;
         transformedModel.renderAsBlockWithColorMultiplier(this.localRenderBlocks, block, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
         block.renderingOpenLid = false;
         Tessellator.instance.setTranslation(0.0, 0.0, 0.0);
         float fOpenRatio = this.getCurrentOpenRatio(tileEntity, fPartialTickCount);
         float fLidAngle = fOpenRatio * 45.0F;
         Vec3 lidRotationPoint = Vec3.createVectorHelper(block.getLidRotationPoint());
         lidRotationPoint.rotateAsBlockPosAroundJToFacing(iFacing);
         GL11.glTranslatef((float)lidRotationPoint.xCoord, (float)lidRotationPoint.yCoord, (float)lidRotationPoint.zCoord);
         if (iFacing == 2) {
            GL11.glRotatef(fLidAngle, 1.0F, 0.0F, 0.0F);
         } else if (iFacing == 3) {
            GL11.glRotatef(-fLidAngle, 1.0F, 0.0F, 0.0F);
         } else if (iFacing == 4) {
            GL11.glRotatef(-fLidAngle, 0.0F, 0.0F, 1.0F);
         } else if (iFacing == 5) {
            GL11.glRotatef(fLidAngle, 0.0F, 0.0F, 1.0F);
         }

         GL11.glTranslatef(-((float)lidRotationPoint.xCoord), -((float)lidRotationPoint.yCoord), -((float)lidRotationPoint.zCoord));
         Tessellator.instance.draw();
         this.localRenderBlocks.clearUVRotation();
         GL11.glEnable(2896);
         GL11.glPopMatrix();
      }
   }

   protected float getCurrentOpenRatio(BasketTileEntity basket, float fPartialTickCount) {
      float fOpenRatio = basket.prevLidOpenRatio + (basket.lidOpenRatio - basket.prevLidOpenRatio) * fPartialTickCount;
      fOpenRatio = 1.0F - fOpenRatio;
      return 1.0F - fOpenRatio * fOpenRatio * fOpenRatio;
   }
}
