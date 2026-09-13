package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityRendererPiston extends TileEntitySpecialRenderer {
   private RenderBlocks blockRenderer;

   public void renderPiston(TileEntityPiston tePiston, double par2, double par4, double par6, float par8) {
      Block var9 = Block.blocksList[tePiston.getStoredBlockID()];
      if (var9 != null && tePiston.getProgress(par8) < 1.0F) {
         Tessellator var10 = Tessellator.instance;
         this.a("/terrain.png");
         RenderHelper.disableStandardItemLighting();
         GL11.glBlendFunc(770, 771);
         GL11.glEnable(3042);
         GL11.glDisable(2884);
         if (Minecraft.isAmbientOcclusionEnabled()) {
            GL11.glShadeModel(7425);
         } else {
            GL11.glShadeModel(7424);
         }

         var10.startDrawingQuads();
         var10.setTranslation(
            (float)par2 - tePiston.xCoord + tePiston.getOffsetX(par8),
            (float)par4 - tePiston.yCoord + tePiston.getOffsetY(par8),
            (float)par6 - tePiston.zCoord + tePiston.getOffsetZ(par8)
         );
         var10.setColorOpaque(1, 1, 1);
         if (var9 == Block.pistonExtension && tePiston.getProgress(par8) < 0.5F) {
            this.blockRenderer.renderPistonExtensionAllFaces(var9, tePiston.xCoord, tePiston.yCoord, tePiston.zCoord, false);
         } else if (tePiston.shouldRenderHead() && !tePiston.isExtending()) {
            Block.pistonExtension.setHeadTexture(((BlockPistonBase)var9).getPistonExtensionTexture());
            this.blockRenderer
               .renderPistonExtensionAllFaces(Block.pistonExtension, tePiston.xCoord, tePiston.yCoord, tePiston.zCoord, tePiston.getProgress(par8) < 0.5F);
            Block.pistonExtension.clearHeadTexture();
            var10.setTranslation((float)par2 - tePiston.xCoord, (float)par4 - tePiston.yCoord, (float)par6 - tePiston.zCoord);
            this.blockRenderer.renderPistonBaseAllFaces(var9, tePiston.xCoord, tePiston.yCoord, tePiston.zCoord);
         } else if (tePiston.getBlockMetadata() == tePiston.worldObj.getBlockMetadata(tePiston.xCoord, tePiston.yCoord, tePiston.zCoord)) {
            var9.currentBlockRenderer = this.blockRenderer;
            var9.renderBlockMovedByPiston(this.blockRenderer, tePiston.xCoord, tePiston.yCoord, tePiston.zCoord);
            if (tePiston.cachedTileEntity != null && this.tileEntityRenderer.hasSpecialRenderer(tePiston.cachedTileEntity)) {
               var10.draw();
               RenderHelper.enableStandardItemLighting();
               int var3 = this.tileEntityRenderer.worldObj.getLightBrightnessForSkyBlocks(tePiston.xCoord, tePiston.yCoord, tePiston.zCoord, 0);
               int var4x = var3 % 65536;
               int var5 = var3 / 65536;
               OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var4x, var5);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               double renderX = par2 + tePiston.getOffsetX(par8);
               double renderY = par4 + tePiston.getOffsetY(par8);
               double renderZ = par6 + tePiston.getOffsetZ(par8);
               this.tileEntityRenderer.renderTileEntityAt(tePiston.cachedTileEntity, renderX, renderY, renderZ, 1.0F);
               var10.startDrawingQuads();
            }
         }

         var10.setTranslation(0.0, 0.0, 0.0);
         var10.draw();
         RenderHelper.enableStandardItemLighting();
      }
   }

   @Override
   public void onWorldChange(World par1World) {
      this.blockRenderer = new RenderBlocks(par1World);
   }

   @Override
   public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
      this.renderPiston((TileEntityPiston)par1TileEntity, par2, par4, par6, par8);
   }
}
