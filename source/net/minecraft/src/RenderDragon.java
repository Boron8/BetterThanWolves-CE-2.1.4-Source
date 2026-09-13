package net.minecraft.src;

import java.util.Random;
import org.lwjgl.opengl.GL11;

public class RenderDragon extends RenderLiving {
   private static int updateModelState = 0;
   protected ModelDragon modelDragon = (ModelDragon)this.mainModel;

   public RenderDragon() {
      super(new ModelDragon(0.0F), 0.5F);
      this.a(this.mainModel);
   }

   protected void rotateDragonBody(EntityDragon var1, float var2, float var3, float var4) {
      float var5 = (float)var1.getMovementOffsets(7, var4)[0];
      float var6 = (float)(var1.getMovementOffsets(5, var4)[1] - var1.getMovementOffsets(10, var4)[1]);
      GL11.glRotatef(-var5, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(var6 * 10.0F, 1.0F, 0.0F, 0.0F);
      GL11.glTranslatef(0.0F, 0.0F, 1.0F);
      if (var1.deathTime > 0) {
         float var7 = (var1.deathTime + var4 - 1.0F) / 20.0F * 1.6F;
         var7 = MathHelper.sqrt_float(var7);
         if (var7 > 1.0F) {
            var7 = 1.0F;
         }

         GL11.glRotatef(var7 * this.b(var1), 0.0F, 0.0F, 1.0F);
      }
   }

   protected void renderDragonModel(EntityDragon var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      if (var1.deathTicks > 0) {
         float var8 = var1.deathTicks / 200.0F;
         GL11.glDepthFunc(515);
         GL11.glEnable(3008);
         GL11.glAlphaFunc(516, var8);
         this.a("/mob/enderdragon/shuffle.png");
         this.mainModel.render(var1, var2, var3, var4, var5, var6, var7);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glDepthFunc(514);
      }

      this.a(var1.N());
      this.mainModel.render(var1, var2, var3, var4, var5, var6, var7);
      if (var1.hurtTime > 0) {
         GL11.glDepthFunc(514);
         GL11.glDisable(3553);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.5F);
         this.mainModel.render(var1, var2, var3, var4, var5, var6, var7);
         GL11.glEnable(3553);
         GL11.glDisable(3042);
         GL11.glDepthFunc(515);
      }
   }

   public void renderDragon(EntityDragon var1, double var2, double var4, double var6, float var8, float var9) {
      BossStatus.func_82824_a(var1, false);
      if (updateModelState != 4) {
         this.mainModel = new ModelDragon(0.0F);
         updateModelState = 4;
      }

      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
      if (var1.healingEnderCrystal != null) {
         float var10 = var1.healingEnderCrystal.innerRotation + var9;
         float var11 = MathHelper.sin(var10 * 0.2F) / 2.0F + 0.5F;
         var11 = (var11 * var11 + var11) * 0.2F;
         float var12 = (float)(var1.healingEnderCrystal.posX - var1.posX - (var1.prevPosX - var1.posX) * (1.0F - var9));
         float var13 = (float)(var11 + var1.healingEnderCrystal.posY - 1.0 - var1.posY - (var1.prevPosY - var1.posY) * (1.0F - var9));
         float var14 = (float)(var1.healingEnderCrystal.posZ - var1.posZ - (var1.prevPosZ - var1.posZ) * (1.0F - var9));
         float var15 = MathHelper.sqrt_float(var12 * var12 + var14 * var14);
         float var16 = MathHelper.sqrt_float(var12 * var12 + var13 * var13 + var14 * var14);
         GL11.glPushMatrix();
         GL11.glTranslatef((float)var2, (float)var4 + 2.0F, (float)var6);
         GL11.glRotatef((float)(-Math.atan2(var14, var12)) * 180.0F / (float) Math.PI - 90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef((float)(-Math.atan2(var15, var13)) * 180.0F / (float) Math.PI - 90.0F, 1.0F, 0.0F, 0.0F);
         Tessellator var17 = Tessellator.instance;
         RenderHelper.disableStandardItemLighting();
         GL11.glDisable(2884);
         this.a("/mob/enderdragon/beam.png");
         GL11.glShadeModel(7425);
         float var18 = 0.0F - (var1.ticksExisted + var9) * 0.01F;
         float var19 = MathHelper.sqrt_float(var12 * var12 + var13 * var13 + var14 * var14) / 32.0F - (var1.ticksExisted + var9) * 0.01F;
         var17.startDrawing(5);
         byte var20 = 8;

         for (int var21 = 0; var21 <= var20; var21++) {
            float var22 = MathHelper.sin(var21 % var20 * (float) Math.PI * 2.0F / var20) * 0.75F;
            float var23 = MathHelper.cos(var21 % var20 * (float) Math.PI * 2.0F / var20) * 0.75F;
            float var24 = var21 % var20 * 1.0F / var20;
            var17.setColorOpaque_I(0);
            var17.addVertexWithUV(var22 * 0.2F, var23 * 0.2F, 0.0, var24, var19);
            var17.setColorOpaque_I(16777215);
            var17.addVertexWithUV(var22, var23, var16, var24, var18);
         }

         var17.draw();
         GL11.glEnable(2884);
         GL11.glShadeModel(7424);
         RenderHelper.enableStandardItemLighting();
         GL11.glPopMatrix();
      }
   }

   protected void renderDragonDying(EntityDragon var1, float var2) {
      super.renderEquippedItems(var1, var2);
      Tessellator var3 = Tessellator.instance;
      if (var1.deathTicks > 0) {
         RenderHelper.disableStandardItemLighting();
         float var4 = (var1.deathTicks + var2) / 200.0F;
         float var5 = 0.0F;
         if (var4 > 0.8F) {
            var5 = (var4 - 0.8F) / 0.2F;
         }

         Random var6 = new Random(432L);
         GL11.glDisable(3553);
         GL11.glShadeModel(7425);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 1);
         GL11.glDisable(3008);
         GL11.glEnable(2884);
         GL11.glDepthMask(false);
         GL11.glPushMatrix();
         GL11.glTranslatef(0.0F, -1.0F, -2.0F);

         for (int var7 = 0; var7 < (var4 + var4 * var4) / 2.0F * 60.0F; var7++) {
            GL11.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(var6.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(var6.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(var6.nextFloat() * 360.0F + var4 * 90.0F, 0.0F, 0.0F, 1.0F);
            var3.startDrawing(6);
            float var8 = var6.nextFloat() * 20.0F + 5.0F + var5 * 10.0F;
            float var9 = var6.nextFloat() * 2.0F + 1.0F + var5 * 2.0F;
            var3.setColorRGBA_I(16777215, (int)(255.0F * (1.0F - var5)));
            var3.addVertex(0.0, 0.0, 0.0);
            var3.setColorRGBA_I(16711935, 0);
            var3.addVertex(-0.866 * var9, var8, -0.5F * var9);
            var3.addVertex(0.866 * var9, var8, -0.5F * var9);
            var3.addVertex(0.0, var8, 1.0F * var9);
            var3.addVertex(-0.866 * var9, var8, -0.5F * var9);
            var3.draw();
         }

         GL11.glPopMatrix();
         GL11.glDepthMask(true);
         GL11.glDisable(2884);
         GL11.glDisable(3042);
         GL11.glShadeModel(7424);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(3553);
         GL11.glEnable(3008);
         RenderHelper.enableStandardItemLighting();
      }
   }

   protected int renderGlow(EntityDragon var1, int var2, float var3) {
      if (var2 == 1) {
         GL11.glDepthFunc(515);
      }

      if (var2 != 0) {
         return -1;
      } else {
         this.a("/mob/enderdragon/ender_eyes.png");
         float var4 = 1.0F;
         GL11.glEnable(3042);
         GL11.glDisable(3008);
         GL11.glBlendFunc(1, 1);
         GL11.glDisable(2896);
         GL11.glDepthFunc(514);
         char var5 = '\uf0f0';
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(2896);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
         return 1;
      }
   }
}
