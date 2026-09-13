package net.minecraft.src;

import java.awt.image.BufferedImage;
import org.lwjgl.opengl.GL11;

public class MapItemRenderer {
   private int[] intArray = new int[16384];
   private int bufferedImage;
   private GameSettings gameSettings;
   private FontRenderer fontRenderer;

   public MapItemRenderer(FontRenderer var1, GameSettings var2, RenderEngine var3) {
      this.gameSettings = var2;
      this.fontRenderer = var1;
      this.bufferedImage = var3.allocateAndSetupTexture(new BufferedImage(128, 128, 2));

      for (int var4 = 0; var4 < 16384; var4++) {
         this.intArray[var4] = 0;
      }
   }

   public void renderMap(EntityPlayer var1, RenderEngine var2, MapData var3) {
      for (int var4 = 0; var4 < 16384; var4++) {
         byte var5 = var3.colors[var4];
         if (var5 / 4 == 0) {
            this.intArray[var4] = (var4 + var4 / 128 & 1) * 8 + 16 << 24;
         } else {
            int var6 = MapColor.mapColorArray[var5 / 4].colorValue;
            int var7 = var5 & 3;
            short var8 = 220;
            if (var7 == 2) {
               var8 = 255;
            }

            if (var7 == 0) {
               var8 = 180;
            }

            int var9 = (var6 >> 16 & 0xFF) * var8 / 255;
            int var10 = (var6 >> 8 & 0xFF) * var8 / 255;
            int var11 = (var6 & 0xFF) * var8 / 255;
            if (this.gameSettings.anaglyph) {
               int var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
               int var13 = (var9 * 30 + var10 * 70) / 100;
               int var14 = (var9 * 30 + var11 * 70) / 100;
               var9 = var12;
               var10 = var13;
               var11 = var14;
            }

            this.intArray[var4] = 0xFF000000 | var9 << 16 | var10 << 8 | var11;
         }
      }

      var2.createTextureFromBytes(this.intArray, 128, 128, this.bufferedImage);
      byte var15 = 0;
      byte var16 = 0;
      Tessellator var17 = Tessellator.instance;
      float var18 = 0.0F;
      GL11.glBindTexture(3553, this.bufferedImage);
      GL11.glEnable(3042);
      GL11.glBlendFunc(1, 771);
      GL11.glDisable(3008);
      var17.startDrawingQuads();
      var17.addVertexWithUV(var15 + 0 + var18, var16 + 128 - var18, -0.01F, 0.0, 1.0);
      var17.addVertexWithUV(var15 + 128 - var18, var16 + 128 - var18, -0.01F, 1.0, 1.0);
      var17.addVertexWithUV(var15 + 128 - var18, var16 + 0 + var18, -0.01F, 1.0, 0.0);
      var17.addVertexWithUV(var15 + 0 + var18, var16 + 0 + var18, -0.01F, 0.0, 0.0);
      var17.draw();
      GL11.glEnable(3008);
      GL11.glDisable(3042);
      var2.resetBoundTexture();
      var2.bindTexture("/misc/mapicons.png");
      int var19 = 0;

      for (MapCoord var21 : var3.playersVisibleOnMap.values()) {
         GL11.glPushMatrix();
         GL11.glTranslatef(var15 + var21.centerX / 2.0F + 64.0F, var16 + var21.centerZ / 2.0F + 64.0F, -0.02F);
         GL11.glRotatef(var21.iconRotation * 360 / 16.0F, 0.0F, 0.0F, 1.0F);
         GL11.glScalef(4.0F, 4.0F, 3.0F);
         GL11.glTranslatef(-0.125F, 0.125F, 0.0F);
         float var22 = (var21.iconSize % 4 + 0) / 4.0F;
         float var23 = (var21.iconSize / 4 + 0) / 4.0F;
         float var24 = (var21.iconSize % 4 + 1) / 4.0F;
         float var25 = (var21.iconSize / 4 + 1) / 4.0F;
         var17.startDrawingQuads();
         var17.addVertexWithUV(-1.0, 1.0, var19 * 0.001F, var22, var23);
         var17.addVertexWithUV(1.0, 1.0, var19 * 0.001F, var24, var23);
         var17.addVertexWithUV(1.0, -1.0, var19 * 0.001F, var24, var25);
         var17.addVertexWithUV(-1.0, -1.0, var19 * 0.001F, var22, var25);
         var17.draw();
         GL11.glPopMatrix();
         var19++;
      }

      GL11.glPushMatrix();
      GL11.glTranslatef(0.0F, 0.0F, -0.04F);
      GL11.glScalef(1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }
}
