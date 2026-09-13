package btw.client.render.entity;

import btw.entity.CanvasEntity;
import btw.entity.util.CanvasArt;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.OpenGlHelper;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class CanvasRenderer extends Render {
   private Random rand = new Random();

   public void func_158_a(CanvasEntity canvas, double par2, double par4, double par6, float par8, float par9) {
      this.rand.setSeed(187L);
      GL11.glPushMatrix();
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
      GL11.glRotatef(par8, 0.0F, 1.0F, 0.0F);
      GL11.glEnable(32826);
      this.a("/btwmodtex/btwart01.png");
      CanvasArt enumart = canvas.art;
      float f = 0.0625F;
      GL11.glScalef(f, f, f);
      this.func_159_a(canvas, enumart.sizeX, enumart.sizeY, enumart.offsetX, enumart.offsetY);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   private void func_159_a(CanvasEntity canvas, int par2, int par3, int par4, int par5) {
      float f = -par2 / 2.0F;
      float f1 = -par3 / 2.0F;
      float f2 = -0.5F;
      float f3 = 0.5F;
      float f12 = 0.0F;
      float f13 = f12 + 0.0625F;
      float f14 = 0.8125F;
      float f15 = f14 + 0.0625F;
      float f16 = f12;
      float f17 = f13;
      float f18 = f14 + 0.001953125F;
      float f19 = f14 + 0.001953125F;
      float f20 = f12 + 0.001953125F;
      float f21 = f12 + 0.001953125F;
      float f22 = f14;
      float f23 = f14 + 0.0625F;

      for (int i = 0; i < par2 / 16; i++) {
         for (int j = 0; j < par3 / 16; j++) {
            float f4 = f + (i + 1) * 16;
            float f5 = f + i * 16;
            float f6 = f1 + (j + 1) * 16;
            float f7 = f1 + j * 16;
            this.func_160_a(canvas, (f4 + f5) / 2.0F, (f6 + f7) / 2.0F);
            float f8 = (par4 + par2 - i * 16) / 256.0F;
            float f9 = (par4 + par2 - (i + 1) * 16) / 256.0F;
            float f10 = (par5 + par3 - j * 16) / 256.0F;
            float f11 = (par5 + par3 - (j + 1) * 16) / 256.0F;
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            tessellator.addVertexWithUV(f4, f7, f2, f9, f10);
            tessellator.addVertexWithUV(f5, f7, f2, f8, f10);
            tessellator.addVertexWithUV(f5, f6, f2, f8, f11);
            tessellator.addVertexWithUV(f4, f6, f2, f9, f11);
            tessellator.setNormal(0.0F, 0.0F, 1.0F);
            tessellator.addVertexWithUV(f4, f6, f3, f12, f14);
            tessellator.addVertexWithUV(f5, f6, f3, f13, f14);
            tessellator.addVertexWithUV(f5, f7, f3, f13, f15);
            tessellator.addVertexWithUV(f4, f7, f3, f12, f15);
            tessellator.setNormal(0.0F, 1.0F, 0.0F);
            tessellator.addVertexWithUV(f4, f6, f2, f16, f18);
            tessellator.addVertexWithUV(f5, f6, f2, f17, f18);
            tessellator.addVertexWithUV(f5, f6, f3, f17, f19);
            tessellator.addVertexWithUV(f4, f6, f3, f16, f19);
            tessellator.setNormal(0.0F, -1.0F, 0.0F);
            tessellator.addVertexWithUV(f4, f7, f3, f16, f18);
            tessellator.addVertexWithUV(f5, f7, f3, f17, f18);
            tessellator.addVertexWithUV(f5, f7, f2, f17, f19);
            tessellator.addVertexWithUV(f4, f7, f2, f16, f19);
            tessellator.setNormal(-1.0F, 0.0F, 0.0F);
            tessellator.addVertexWithUV(f4, f6, f3, f21, f22);
            tessellator.addVertexWithUV(f4, f7, f3, f21, f23);
            tessellator.addVertexWithUV(f4, f7, f2, f20, f23);
            tessellator.addVertexWithUV(f4, f6, f2, f20, f22);
            tessellator.setNormal(1.0F, 0.0F, 0.0F);
            tessellator.addVertexWithUV(f5, f6, f2, f21, f22);
            tessellator.addVertexWithUV(f5, f7, f2, f21, f23);
            tessellator.addVertexWithUV(f5, f7, f3, f20, f23);
            tessellator.addVertexWithUV(f5, f6, f3, f20, f22);
            tessellator.draw();
         }
      }
   }

   private void func_160_a(CanvasEntity canvas, float par2, float par3) {
      int i = MathHelper.floor_double(canvas.posX);
      int j = MathHelper.floor_double(canvas.posY + par3 / 16.0F);
      int k = MathHelper.floor_double(canvas.posZ);
      if (canvas.direction == 0) {
         i = MathHelper.floor_double(canvas.posX + par2 / 16.0F);
      }

      if (canvas.direction == 1) {
         k = MathHelper.floor_double(canvas.posZ - par2 / 16.0F);
      }

      if (canvas.direction == 2) {
         i = MathHelper.floor_double(canvas.posX - par2 / 16.0F);
      }

      if (canvas.direction == 3) {
         k = MathHelper.floor_double(canvas.posZ + par2 / 16.0F);
      }

      int l = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(i, j, k, 0);
      int i1 = l % 65536;
      int j1 = l / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i1, j1);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.func_158_a((CanvasEntity)par1Entity, par2, par4, par6, par8, par9);
   }
}
