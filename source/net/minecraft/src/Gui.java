package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class Gui {
   protected float zLevel = 0.0F;

   protected void drawHorizontalLine(int var1, int var2, int var3, int var4) {
      if (var2 < var1) {
         int var5 = var1;
         var1 = var2;
         var2 = var5;
      }

      drawRect(var1, var3, var2 + 1, var3 + 1, var4);
   }

   protected void drawVerticalLine(int var1, int var2, int var3, int var4) {
      if (var3 < var2) {
         int var5 = var2;
         var2 = var3;
         var3 = var5;
      }

      drawRect(var1, var2 + 1, var1 + 1, var3, var4);
   }

   public static void drawRect(int var0, int var1, int var2, int var3, int var4) {
      if (var0 < var2) {
         int var5 = var0;
         var0 = var2;
         var2 = var5;
      }

      if (var1 < var3) {
         int var10 = var1;
         var1 = var3;
         var3 = var10;
      }

      float var11 = (var4 >> 24 & 0xFF) / 255.0F;
      float var6 = (var4 >> 16 & 0xFF) / 255.0F;
      float var7 = (var4 >> 8 & 0xFF) / 255.0F;
      float var8 = (var4 & 0xFF) / 255.0F;
      Tessellator var9 = Tessellator.instance;
      GL11.glEnable(3042);
      GL11.glDisable(3553);
      GL11.glBlendFunc(770, 771);
      GL11.glColor4f(var6, var7, var8, var11);
      var9.startDrawingQuads();
      var9.addVertex(var0, var3, 0.0);
      var9.addVertex(var2, var3, 0.0);
      var9.addVertex(var2, var1, 0.0);
      var9.addVertex(var0, var1, 0.0);
      var9.draw();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
   }

   protected void drawGradientRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      float var7 = (var5 >> 24 & 0xFF) / 255.0F;
      float var8 = (var5 >> 16 & 0xFF) / 255.0F;
      float var9 = (var5 >> 8 & 0xFF) / 255.0F;
      float var10 = (var5 & 0xFF) / 255.0F;
      float var11 = (var6 >> 24 & 0xFF) / 255.0F;
      float var12 = (var6 >> 16 & 0xFF) / 255.0F;
      float var13 = (var6 >> 8 & 0xFF) / 255.0F;
      float var14 = (var6 & 0xFF) / 255.0F;
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glDisable(3008);
      GL11.glBlendFunc(770, 771);
      GL11.glShadeModel(7425);
      Tessellator var15 = Tessellator.instance;
      var15.startDrawingQuads();
      var15.setColorRGBA_F(var8, var9, var10, var7);
      var15.addVertex(var3, var2, this.zLevel);
      var15.addVertex(var1, var2, this.zLevel);
      var15.setColorRGBA_F(var12, var13, var14, var11);
      var15.addVertex(var1, var4, this.zLevel);
      var15.addVertex(var3, var4, this.zLevel);
      var15.draw();
      GL11.glShadeModel(7424);
      GL11.glDisable(3042);
      GL11.glEnable(3008);
      GL11.glEnable(3553);
   }

   public void drawCenteredString(FontRenderer var1, String var2, int var3, int var4, int var5) {
      var1.drawStringWithShadow(var2, var3 - var1.getStringWidth(var2) / 2, var4, var5);
   }

   public void drawString(FontRenderer var1, String var2, int var3, int var4, int var5) {
      var1.drawStringWithShadow(var2, var3, var4, var5);
   }

   public void drawTexturedModalRect(int var1, int var2, int var3, int var4, int var5, int var6) {
      float var7 = 0.00390625F;
      float var8 = 0.00390625F;
      Tessellator var9 = Tessellator.instance;
      var9.startDrawingQuads();
      var9.addVertexWithUV(var1 + 0, var2 + var6, this.zLevel, (var3 + 0) * var7, (var4 + var6) * var8);
      var9.addVertexWithUV(var1 + var5, var2 + var6, this.zLevel, (var3 + var5) * var7, (var4 + var6) * var8);
      var9.addVertexWithUV(var1 + var5, var2 + 0, this.zLevel, (var3 + var5) * var7, (var4 + 0) * var8);
      var9.addVertexWithUV(var1 + 0, var2 + 0, this.zLevel, (var3 + 0) * var7, (var4 + 0) * var8);
      var9.draw();
   }

   public void drawTexturedModelRectFromIcon(int var1, int var2, Icon var3, int var4, int var5) {
      Tessellator var6 = Tessellator.instance;
      var6.startDrawingQuads();
      var6.addVertexWithUV(var1 + 0, var2 + var5, this.zLevel, var3.getMinU(), var3.getMaxV());
      var6.addVertexWithUV(var1 + var4, var2 + var5, this.zLevel, var3.getMaxU(), var3.getMaxV());
      var6.addVertexWithUV(var1 + var4, var2 + 0, this.zLevel, var3.getMaxU(), var3.getMinV());
      var6.addVertexWithUV(var1 + 0, var2 + 0, this.zLevel, var3.getMinU(), var3.getMinV());
      var6.draw();
   }
}
