package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public abstract class TileEntitySpecialRenderer {
   protected TileEntityRenderer tileEntityRenderer;

   public abstract void renderTileEntityAt(TileEntity var1, double var2, double var4, double var6, float var8);

   protected void bindTextureByName(String var1) {
      RenderEngine var2 = this.tileEntityRenderer.renderEngine;
      if (var2 != null) {
         var2.bindTexture(var1);
      }
   }

   protected void bindTextureByURL(String var1, String var2) {
      RenderEngine var3 = this.tileEntityRenderer.renderEngine;
      if (var3 != null) {
         GL11.glBindTexture(3553, var3.getTextureForDownloadableImage(var1, var2));
      }

      var3.resetBoundTexture();
   }

   public void setTileEntityRenderer(TileEntityRenderer var1) {
      this.tileEntityRenderer = var1;
   }

   public void onWorldChange(World var1) {
   }

   public FontRenderer getFontRenderer() {
      return this.tileEntityRenderer.getFontRenderer();
   }
}
