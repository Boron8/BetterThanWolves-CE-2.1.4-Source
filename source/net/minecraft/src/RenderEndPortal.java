package net.minecraft.src;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderEndPortal extends TileEntitySpecialRenderer {
   FloatBuffer field_76908_a = GLAllocation.createDirectFloatBuffer(16);

   public void renderEndPortalTileEntity(TileEntityEndPortal var1, double var2, double var4, double var6, float var8) {
      float var9 = (float)this.tileEntityRenderer.playerX;
      float var10 = (float)this.tileEntityRenderer.playerY;
      float var11 = (float)this.tileEntityRenderer.playerZ;
      GL11.glDisable(2896);
      Random var12 = new Random(31100L);
      float var13 = 0.75F;

      for (int var14 = 0; var14 < 16; var14++) {
         GL11.glPushMatrix();
         float var15 = 16 - var14;
         float var16 = 0.0625F;
         float var17 = 1.0F / (var15 + 1.0F);
         if (var14 == 0) {
            this.a("/misc/tunnel.png");
            var17 = 0.1F;
            var15 = 65.0F;
            var16 = 0.125F;
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
         }

         if (var14 == 1) {
            this.a("/misc/particlefield.png");
            GL11.glEnable(3042);
            GL11.glBlendFunc(1, 1);
            var16 = 0.5F;
         }

         float var18 = (float)(-(var4 + var13));
         float var19 = var18 + ActiveRenderInfo.objectY;
         float var20 = var18 + var15 + ActiveRenderInfo.objectY;
         float var21 = var19 / var20;
         var21 = (float)(var4 + var13) + var21;
         GL11.glTranslatef(var9, var21, var11);
         GL11.glTexGeni(8192, 9472, 9217);
         GL11.glTexGeni(8193, 9472, 9217);
         GL11.glTexGeni(8194, 9472, 9217);
         GL11.glTexGeni(8195, 9472, 9216);
         GL11.glTexGen(8192, 9473, this.func_76907_a(1.0F, 0.0F, 0.0F, 0.0F));
         GL11.glTexGen(8193, 9473, this.func_76907_a(0.0F, 0.0F, 1.0F, 0.0F));
         GL11.glTexGen(8194, 9473, this.func_76907_a(0.0F, 0.0F, 0.0F, 1.0F));
         GL11.glTexGen(8195, 9474, this.func_76907_a(0.0F, 1.0F, 0.0F, 0.0F));
         GL11.glEnable(3168);
         GL11.glEnable(3169);
         GL11.glEnable(3170);
         GL11.glEnable(3171);
         GL11.glPopMatrix();
         GL11.glMatrixMode(5890);
         GL11.glPushMatrix();
         GL11.glLoadIdentity();
         GL11.glTranslatef(0.0F, (float)(Minecraft.getSystemTime() % 700000L) / 700000.0F, 0.0F);
         GL11.glScalef(var16, var16, var16);
         GL11.glTranslatef(0.5F, 0.5F, 0.0F);
         GL11.glRotatef((var14 * var14 * 4321 + var14 * 9) * 2.0F, 0.0F, 0.0F, 1.0F);
         GL11.glTranslatef(-0.5F, -0.5F, 0.0F);
         GL11.glTranslatef(-var9, -var11, -var10);
         var19 = var18 + ActiveRenderInfo.objectY;
         GL11.glTranslatef(ActiveRenderInfo.objectX * var15 / var19, ActiveRenderInfo.objectZ * var15 / var19, -var10);
         Tessellator var25 = Tessellator.instance;
         var25.startDrawingQuads();
         var21 = var12.nextFloat() * 0.5F + 0.1F;
         float var22 = var12.nextFloat() * 0.5F + 0.4F;
         float var23 = var12.nextFloat() * 0.5F + 0.5F;
         if (var14 == 0) {
            var23 = 1.0F;
            var22 = 1.0F;
            var21 = 1.0F;
         }

         var25.setColorRGBA_F(var21 * var17, var22 * var17, var23 * var17, 1.0F);
         var25.addVertex(var2, var4 + var13, var6);
         var25.addVertex(var2, var4 + var13, var6 + 1.0);
         var25.addVertex(var2 + 1.0, var4 + var13, var6 + 1.0);
         var25.addVertex(var2 + 1.0, var4 + var13, var6);
         var25.draw();
         GL11.glPopMatrix();
         GL11.glMatrixMode(5888);
      }

      GL11.glDisable(3042);
      GL11.glDisable(3168);
      GL11.glDisable(3169);
      GL11.glDisable(3170);
      GL11.glDisable(3171);
      GL11.glEnable(2896);
   }

   private FloatBuffer func_76907_a(float var1, float var2, float var3, float var4) {
      ((Buffer)this.field_76908_a).clear();
      this.field_76908_a.put(var1).put(var2).put(var3).put(var4);
      ((Buffer)this.field_76908_a).flip();
      return this.field_76908_a;
   }
}
