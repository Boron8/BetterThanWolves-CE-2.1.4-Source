package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderPainting extends Render {
   public void renderThePainting(EntityPainting var1, double var2, double var4, double var6, float var8, float var9) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)var2, (float)var4, (float)var6);
      GL11.glRotatef(var8, 0.0F, 1.0F, 0.0F);
      GL11.glEnable(32826);
      this.a("/art/kz.png");
      EnumArt var10 = var1.art;
      float var11 = 0.0625F;
      GL11.glScalef(var11, var11, var11);
      this.func_77010_a(var1, var10.sizeX, var10.sizeY, var10.offsetX, var10.offsetY);
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }

   private void func_77010_a(EntityPainting var1, int var2, int var3, int var4, int var5) {
      float var6 = -var2 / 2.0F;
      float var7 = -var3 / 2.0F;
      float var8 = 0.5F;
      float var9 = 0.75F;
      float var10 = 0.8125F;
      float var11 = 0.0F;
      float var12 = 0.0625F;
      float var13 = 0.75F;
      float var14 = 0.8125F;
      float var15 = 0.001953125F;
      float var16 = 0.001953125F;
      float var17 = 0.7519531F;
      float var18 = 0.7519531F;
      float var19 = 0.0F;
      float var20 = 0.0625F;

      for (int var21 = 0; var21 < var2 / 16; var21++) {
         for (int var22 = 0; var22 < var3 / 16; var22++) {
            float var23 = var6 + (var21 + 1) * 16;
            float var24 = var6 + var21 * 16;
            float var25 = var7 + (var22 + 1) * 16;
            float var26 = var7 + var22 * 16;
            this.func_77008_a(var1, (var23 + var24) / 2.0F, (var25 + var26) / 2.0F);
            float var27 = (var4 + var2 - var21 * 16) / 256.0F;
            float var28 = (var4 + var2 - (var21 + 1) * 16) / 256.0F;
            float var29 = (var5 + var3 - var22 * 16) / 256.0F;
            float var30 = (var5 + var3 - (var22 + 1) * 16) / 256.0F;
            Tessellator var31 = Tessellator.instance;
            var31.startDrawingQuads();
            var31.setNormal(0.0F, 0.0F, -1.0F);
            var31.addVertexWithUV(var23, var26, -var8, var28, var29);
            var31.addVertexWithUV(var24, var26, -var8, var27, var29);
            var31.addVertexWithUV(var24, var25, -var8, var27, var30);
            var31.addVertexWithUV(var23, var25, -var8, var28, var30);
            var31.setNormal(0.0F, 0.0F, 1.0F);
            var31.addVertexWithUV(var23, var25, var8, var9, var11);
            var31.addVertexWithUV(var24, var25, var8, var10, var11);
            var31.addVertexWithUV(var24, var26, var8, var10, var12);
            var31.addVertexWithUV(var23, var26, var8, var9, var12);
            var31.setNormal(0.0F, 1.0F, 0.0F);
            var31.addVertexWithUV(var23, var25, -var8, var13, var15);
            var31.addVertexWithUV(var24, var25, -var8, var14, var15);
            var31.addVertexWithUV(var24, var25, var8, var14, var16);
            var31.addVertexWithUV(var23, var25, var8, var13, var16);
            var31.setNormal(0.0F, -1.0F, 0.0F);
            var31.addVertexWithUV(var23, var26, var8, var13, var15);
            var31.addVertexWithUV(var24, var26, var8, var14, var15);
            var31.addVertexWithUV(var24, var26, -var8, var14, var16);
            var31.addVertexWithUV(var23, var26, -var8, var13, var16);
            var31.setNormal(-1.0F, 0.0F, 0.0F);
            var31.addVertexWithUV(var23, var25, var8, var18, var19);
            var31.addVertexWithUV(var23, var26, var8, var18, var20);
            var31.addVertexWithUV(var23, var26, -var8, var17, var20);
            var31.addVertexWithUV(var23, var25, -var8, var17, var19);
            var31.setNormal(1.0F, 0.0F, 0.0F);
            var31.addVertexWithUV(var24, var25, -var8, var18, var19);
            var31.addVertexWithUV(var24, var26, -var8, var18, var20);
            var31.addVertexWithUV(var24, var26, var8, var17, var20);
            var31.addVertexWithUV(var24, var25, var8, var17, var19);
            var31.draw();
         }
      }
   }

   private void func_77008_a(EntityPainting var1, float var2, float var3) {
      int var4 = MathHelper.floor_double(var1.posX);
      int var5 = MathHelper.floor_double(var1.posY + var3 / 16.0F);
      int var6 = MathHelper.floor_double(var1.posZ);
      if (var1.hangingDirection == 2) {
         var4 = MathHelper.floor_double(var1.posX + var2 / 16.0F);
      }

      if (var1.hangingDirection == 1) {
         var6 = MathHelper.floor_double(var1.posZ - var2 / 16.0F);
      }

      if (var1.hangingDirection == 0) {
         var4 = MathHelper.floor_double(var1.posX - var2 / 16.0F);
      }

      if (var1.hangingDirection == 3) {
         var6 = MathHelper.floor_double(var1.posZ + var2 / 16.0F);
      }

      int var7 = this.renderManager.worldObj.getLightBrightnessForSkyBlocks(var4, var5, var6, 0);
      int var8 = var7 % 65536;
      int var9 = var7 / 65536;
      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var8, var9);
      GL11.glColor3f(1.0F, 1.0F, 1.0F);
   }
}
