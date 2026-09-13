package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class TileEntityBeaconRenderer extends TileEntitySpecialRenderer {
   public void renderTileEntityBeaconAt(TileEntityBeacon par1TileEntityBeacon, double par2, double par4, double par6, float par8) {
      float var9 = par1TileEntityBeacon.func_82125_v_();
      if (var9 > 0.0F) {
         Tessellator var10 = Tessellator.instance;
         if (par1TileEntityBeacon.isHomeBeacon()) {
            this.a("/btwmodtex/fcBeam.png");
         } else {
            this.a("/misc/beam.png");
         }

         GL11.glTexParameterf(3553, 10242, 10497.0F);
         GL11.glTexParameterf(3553, 10243, 10497.0F);
         GL11.glDisable(2896);
         GL11.glDisable(2884);
         GL11.glDisable(3042);
         GL11.glDepthMask(true);
         GL11.glBlendFunc(770, 1);
         float var11 = (float)(par1TileEntityBeacon.az().getTotalWorldTime() % 8388471L) + par8;
         float var12 = -var11 * 0.2F - MathHelper.floor_float(-var11 * 0.1F);
         byte var13 = 1;
         double var14 = var11 * 0.025 * (1.0 - (var13 & 1) * 2.5);
         var10.startDrawingQuads();
         var10.setColorRGBA(255, 255, 255, 32);
         double var16 = var13 * 0.2;
         if (par1TileEntityBeacon.isHomeBeacon()) {
            var16 *= 1.5;
         }

         double var18 = 0.5 + Math.cos(var14 + (Math.PI * 3.0 / 4.0)) * var16;
         double var20 = 0.5 + Math.sin(var14 + (Math.PI * 3.0 / 4.0)) * var16;
         double var22 = 0.5 + Math.cos(var14 + (Math.PI / 4)) * var16;
         double var24 = 0.5 + Math.sin(var14 + (Math.PI / 4)) * var16;
         double var26 = 0.5 + Math.cos(var14 + (Math.PI * 5.0 / 4.0)) * var16;
         double var28 = 0.5 + Math.sin(var14 + (Math.PI * 5.0 / 4.0)) * var16;
         double var30 = 0.5 + Math.cos(var14 + (Math.PI * 7.0 / 4.0)) * var16;
         double var32 = 0.5 + Math.sin(var14 + (Math.PI * 7.0 / 4.0)) * var16;
         double var34 = 256.0F * var9;
         double var36 = 0.0;
         double var38 = 1.0;
         double var40 = -1.0F + var12;
         double var42 = 256.0F * var9 * (0.5 / var16) + var40;
         var10.addVertexWithUV(par2 + var18, par4 + var34, par6 + var20, var38, var42);
         var10.addVertexWithUV(par2 + var18, par4, par6 + var20, var38, var40);
         var10.addVertexWithUV(par2 + var22, par4, par6 + var24, var36, var40);
         var10.addVertexWithUV(par2 + var22, par4 + var34, par6 + var24, var36, var42);
         var10.addVertexWithUV(par2 + var30, par4 + var34, par6 + var32, var38, var42);
         var10.addVertexWithUV(par2 + var30, par4, par6 + var32, var38, var40);
         var10.addVertexWithUV(par2 + var26, par4, par6 + var28, var36, var40);
         var10.addVertexWithUV(par2 + var26, par4 + var34, par6 + var28, var36, var42);
         var10.addVertexWithUV(par2 + var22, par4 + var34, par6 + var24, var38, var42);
         var10.addVertexWithUV(par2 + var22, par4, par6 + var24, var38, var40);
         var10.addVertexWithUV(par2 + var30, par4, par6 + var32, var36, var40);
         var10.addVertexWithUV(par2 + var30, par4 + var34, par6 + var32, var36, var42);
         var10.addVertexWithUV(par2 + var26, par4 + var34, par6 + var28, var38, var42);
         var10.addVertexWithUV(par2 + var26, par4, par6 + var28, var38, var40);
         var10.addVertexWithUV(par2 + var18, par4, par6 + var20, var36, var40);
         var10.addVertexWithUV(par2 + var18, par4 + var34, par6 + var20, var36, var42);
         var10.draw();
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glDepthMask(false);
         var10.startDrawingQuads();
         var10.setColorRGBA(255, 255, 255, 32);
         double var44 = 0.2;
         double var15 = 0.2;
         double var17 = 0.8;
         double var19 = 0.2;
         double var21 = 0.2;
         double var23 = 0.8;
         double var25 = 0.8;
         double var27 = 0.8;
         if (par1TileEntityBeacon.isHomeBeacon()) {
            var44 = 0.0;
            var15 = 0.0;
            var17 = 1.0;
            var19 = 0.0;
            var21 = 0.0;
            var23 = 1.0;
            var25 = 1.0;
            var27 = 1.0;
         }

         double var29 = 256.0F * var9;
         double var31 = 0.0;
         double var33 = 1.0;
         double var35 = -1.0F + var12;
         double var37 = 256.0F * var9 + var35;
         var10.addVertexWithUV(par2 + var44, par4 + var29, par6 + var15, var33, var37);
         var10.addVertexWithUV(par2 + var44, par4, par6 + var15, var33, var35);
         var10.addVertexWithUV(par2 + var17, par4, par6 + var19, var31, var35);
         var10.addVertexWithUV(par2 + var17, par4 + var29, par6 + var19, var31, var37);
         var10.addVertexWithUV(par2 + var25, par4 + var29, par6 + var27, var33, var37);
         var10.addVertexWithUV(par2 + var25, par4, par6 + var27, var33, var35);
         var10.addVertexWithUV(par2 + var21, par4, par6 + var23, var31, var35);
         var10.addVertexWithUV(par2 + var21, par4 + var29, par6 + var23, var31, var37);
         var10.addVertexWithUV(par2 + var17, par4 + var29, par6 + var19, var33, var37);
         var10.addVertexWithUV(par2 + var17, par4, par6 + var19, var33, var35);
         var10.addVertexWithUV(par2 + var25, par4, par6 + var27, var31, var35);
         var10.addVertexWithUV(par2 + var25, par4 + var29, par6 + var27, var31, var37);
         var10.addVertexWithUV(par2 + var21, par4 + var29, par6 + var23, var33, var37);
         var10.addVertexWithUV(par2 + var21, par4, par6 + var23, var33, var35);
         var10.addVertexWithUV(par2 + var44, par4, par6 + var15, var31, var35);
         var10.addVertexWithUV(par2 + var44, par4 + var29, par6 + var15, var31, var37);
         var10.draw();
         GL11.glEnable(2896);
         GL11.glEnable(3553);
         GL11.glDepthMask(true);
      }
   }

   @Override
   public void renderTileEntityAt(TileEntity par1TileEntity, double par2, double par4, double par6, float par8) {
      this.renderTileEntityBeaconAt((TileEntityBeacon)par1TileEntity, par2, par4, par6, par8);
   }
}
