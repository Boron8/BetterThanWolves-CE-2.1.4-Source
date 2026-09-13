package net.minecraft.src;

import btw.BTWMod;
import com.prupe.mcpatcher.cit.CITUtils;
import com.prupe.mcpatcher.mal.resource.FakeResourceLocation;
import com.prupe.mcpatcher.mob.MobRandomizer;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderLiving extends Render {
   protected ModelBase mainModel;
   protected ModelBase renderPassModel;

   public RenderLiving(ModelBase par1ModelBase, float par2) {
      this.mainModel = par1ModelBase;
      this.shadowSize = par2;
   }

   public void setRenderPassModel(ModelBase par1ModelBase) {
      this.renderPassModel = par1ModelBase;
   }

   private float interpolateRotation(float par1, float par2, float par3) {
      float var4 = par2 - par1;

      while (var4 < -180.0F) {
         var4 += 360.0F;
      }

      while (var4 >= 180.0F) {
         var4 -= 360.0F;
      }

      return par1 + par3 * var4;
   }

   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      GL11.glPushMatrix();
      GL11.glDisable(2884);
      this.mainModel.onGround = this.renderSwingProgress(par1EntityLiving, par9);
      if (this.renderPassModel != null) {
         this.renderPassModel.onGround = this.mainModel.onGround;
      }

      this.mainModel.isRiding = par1EntityLiving.af();
      if (this.renderPassModel != null) {
         this.renderPassModel.isRiding = this.mainModel.isRiding;
      }

      this.mainModel.isChild = par1EntityLiving.isChild();
      if (this.renderPassModel != null) {
         this.renderPassModel.isChild = this.mainModel.isChild;
      }

      try {
         float var10 = this.interpolateRotation(par1EntityLiving.prevRenderYawOffset, par1EntityLiving.renderYawOffset, par9);
         float var11 = this.interpolateRotation(par1EntityLiving.prevRotationYawHead, par1EntityLiving.rotationYawHead, par9);
         float var12 = par1EntityLiving.prevRotationPitch + (par1EntityLiving.rotationPitch - par1EntityLiving.prevRotationPitch) * par9;
         if (par1EntityLiving.hasHeadCrabbedSquid()) {
            var12 = 0.0F;
         }

         this.renderLivingAt(par1EntityLiving, par2, par4, par6);
         float var13 = this.handleRotationFloat(par1EntityLiving, par9);
         this.rotateCorpse(par1EntityLiving, var13, var10, par9);
         float var14 = 0.0625F;
         GL11.glEnable(32826);
         GL11.glScalef(-1.0F, -1.0F, 1.0F);
         this.preRenderCallback(par1EntityLiving, par9);
         GL11.glTranslatef(0.0F, -24.0F * var14 - 0.0078125F, 0.0F);
         float var15 = par1EntityLiving.prevLimbYaw + (par1EntityLiving.limbYaw - par1EntityLiving.prevLimbYaw) * par9;
         float var16 = par1EntityLiving.limbSwing - par1EntityLiving.limbYaw * (1.0F - par9);
         if (par1EntityLiving.isChild()) {
            var16 *= 3.0F;
         }

         if (var15 > 1.0F) {
            var15 = 1.0F;
         }

         GL11.glEnable(3008);
         this.mainModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
         this.renderModel(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

         for (int var17 = 0; var17 < 4; var17++) {
            int var18 = this.shouldRenderPass(par1EntityLiving, var17, par9);
            if (var18 > 0) {
               this.renderPassModel.setLivingAnimations(par1EntityLiving, var16, var15, par9);
               this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
               if ((var18 & 240) == 16) {
                  this.func_82408_c(par1EntityLiving, var17, par9);
                  this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
               }

               if (CITUtils.setupArmorEnchantments(par1EntityLiving, var17)) {
                  while (CITUtils.preRenderArmorEnchantment()) {
                     this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                     CITUtils.postRenderArmorEnchantment();
                  }
               } else if ((var18 & 15) == 15) {
                  float var19 = par1EntityLiving.ticksExisted + par9;
                  this.a("%blur%/misc/glint.png");
                  GL11.glEnable(3042);
                  float var20 = 0.5F;
                  GL11.glColor4f(var20, var20, var20, 1.0F);
                  GL11.glDepthFunc(514);
                  GL11.glDepthMask(false);

                  for (int var21 = 0; var21 < 2; var21++) {
                     GL11.glDisable(2896);
                     float var22 = 0.76F;
                     GL11.glColor4f(0.5F * var22, 0.25F * var22, 0.8F * var22, 1.0F);
                     GL11.glBlendFunc(768, 1);
                     GL11.glMatrixMode(5890);
                     GL11.glLoadIdentity();
                     float var23 = var19 * (0.001F + var21 * 0.003F) * 20.0F;
                     float var24 = 0.33333334F;
                     GL11.glScalef(var24, var24, var24);
                     GL11.glRotatef(30.0F - var21 * 60.0F, 0.0F, 0.0F, 1.0F);
                     GL11.glTranslatef(0.0F, var23, 0.0F);
                     GL11.glMatrixMode(5888);
                     this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                  }

                  GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                  GL11.glMatrixMode(5890);
                  GL11.glDepthMask(true);
                  GL11.glLoadIdentity();
                  GL11.glMatrixMode(5888);
                  GL11.glEnable(2896);
                  GL11.glDisable(3042);
                  GL11.glDepthFunc(515);
               }

               GL11.glDisable(3042);
               GL11.glEnable(3008);
            }
         }

         GL11.glDepthMask(true);
         this.renderEquippedItems(par1EntityLiving, par9);
         float var26 = par1EntityLiving.c(par9);
         int var18 = this.getColorMultiplier(par1EntityLiving, var26, par9);
         OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
         GL11.glDisable(3553);
         OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
         if ((var18 >> 24 & 0xFF) > 0 || par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDepthFunc(514);
            if (par1EntityLiving.hurtTime > 0 || par1EntityLiving.deathTime > 0) {
               GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
               this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

               for (int var27 = 0; var27 < 4; var27++) {
                  if (this.inheritRenderPass(par1EntityLiving, var27, par9) >= 0) {
                     GL11.glColor4f(var26, 0.0F, 0.0F, 0.4F);
                     this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                  }
               }
            }

            if ((var18 >> 24 & 0xFF) > 0) {
               float var19 = (var18 >> 16 & 0xFF) / 255.0F;
               float var20 = (var18 >> 8 & 0xFF) / 255.0F;
               float var29 = (var18 & 0xFF) / 255.0F;
               float var22 = (var18 >> 24 & 0xFF) / 255.0F;
               GL11.glColor4f(var19, var20, var29, var22);
               this.mainModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);

               for (int var28 = 0; var28 < 4; var28++) {
                  if (this.inheritRenderPass(par1EntityLiving, var28, par9) >= 0) {
                     GL11.glColor4f(var19, var20, var29, var22);
                     this.renderPassModel.render(par1EntityLiving, var16, var15, var13, var11 - var10, var12, var14);
                  }
               }
            }

            GL11.glDepthFunc(515);
            GL11.glDisable(3042);
            GL11.glEnable(3008);
            GL11.glEnable(3553);
         }

         GL11.glDisable(32826);
      } catch (Exception var251) {
         var251.printStackTrace();
      }

      OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
      GL11.glEnable(3553);
      OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
      GL11.glEnable(2884);
      GL11.glPopMatrix();
      this.passSpecialRender(par1EntityLiving, par2, par4, par6);
   }

   protected void renderModel(EntityLiving par1EntityLiving, float par2, float par3, float par4, float par5, float par6, float par7) {
      this.func_98190_a(par1EntityLiving);
      if (!par1EntityLiving.ai()) {
         this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
      } else if (!par1EntityLiving.c(Minecraft.getMinecraft().thePlayer)) {
         GL11.glPushMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
         GL11.glDepthMask(false);
         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         GL11.glAlphaFunc(516, 0.003921569F);
         this.mainModel.render(par1EntityLiving, par2, par3, par4, par5, par6, par7);
         GL11.glDisable(3042);
         GL11.glAlphaFunc(516, 0.1F);
         GL11.glPopMatrix();
         GL11.glDepthMask(true);
      } else {
         this.mainModel.setRotationAngles(par2, par3, par4, par5, par6, par7, par1EntityLiving);
      }
   }

   protected void func_98190_a(EntityLiving par1EntityLiving) {
      this.a(FakeResourceLocation.unwrap(MobRandomizer.randomTexture((Entity)par1EntityLiving, FakeResourceLocation.wrap(par1EntityLiving.getTexture()))));
   }

   protected void renderLivingAt(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
      GL11.glTranslatef((float)par2, (float)par4, (float)par6);
   }

   protected void rotateCorpse(EntityLiving par1EntityLiving, float par2, float par3, float par4) {
      GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
      if (par1EntityLiving.deathTime > 0) {
         float var5 = (par1EntityLiving.deathTime + par4 - 1.0F) / 20.0F * 1.6F;
         var5 = MathHelper.sqrt_float(var5);
         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         GL11.glRotatef(var5 * this.getDeathMaxRotation(par1EntityLiving), 0.0F, 0.0F, 1.0F);
      }
   }

   protected float renderSwingProgress(EntityLiving par1EntityLiving, float par2) {
      return par1EntityLiving.getSwingProgress(par2);
   }

   protected float handleRotationFloat(EntityLiving par1EntityLiving, float par2) {
      return par1EntityLiving.ticksExisted + par2;
   }

   protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
   }

   protected void renderArrowsStuckInEntity(EntityLiving par1EntityLiving, float par2) {
      int var3 = par1EntityLiving.getArrowCountInEntity();
      if (var3 > 0) {
         EntityArrow var4 = (EntityArrow)EntityList.createEntityOfType(
            EntityArrow.class, par1EntityLiving.worldObj, par1EntityLiving.posX, par1EntityLiving.posY, par1EntityLiving.posZ
         );
         Random var5 = new Random(par1EntityLiving.entityId);
         RenderHelper.disableStandardItemLighting();

         for (int var6 = 0; var6 < var3; var6++) {
            GL11.glPushMatrix();
            ModelRenderer var7 = this.mainModel.getRandomModelBox(var5);
            ModelBox var8 = (ModelBox)var7.cubeList.get(var5.nextInt(var7.cubeList.size()));
            var7.postRender(0.0625F);
            float var9 = var5.nextFloat();
            float var10 = var5.nextFloat();
            float var11 = var5.nextFloat();
            float var12 = (var8.posX1 + (var8.posX2 - var8.posX1) * var9) / 16.0F;
            float var13 = (var8.posY1 + (var8.posY2 - var8.posY1) * var10) / 16.0F;
            float var14 = (var8.posZ1 + (var8.posZ2 - var8.posZ1) * var11) / 16.0F;
            GL11.glTranslatef(var12, var13, var14);
            var9 = var9 * 2.0F - 1.0F;
            var10 = var10 * 2.0F - 1.0F;
            var11 = var11 * 2.0F - 1.0F;
            var9 *= -1.0F;
            var10 *= -1.0F;
            var11 *= -1.0F;
            float var15 = MathHelper.sqrt_float(var9 * var9 + var11 * var11);
            var4.prevRotationYaw = var4.rotationYaw = (float)(Math.atan2(var9, var11) * 180.0 / Math.PI);
            var4.prevRotationPitch = var4.rotationPitch = (float)(Math.atan2(var10, var15) * 180.0 / Math.PI);
            double var16 = 0.0;
            double var18 = 0.0;
            double var20 = 0.0;
            float var22 = 0.0F;
            this.renderManager.renderEntityWithPosYaw(var4, var16, var18, var20, var22, par2);
            GL11.glPopMatrix();
         }

         RenderHelper.enableStandardItemLighting();
      }
   }

   protected int inheritRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return this.shouldRenderPass(par1EntityLiving, par2, par3);
   }

   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return -1;
   }

   protected void func_82408_c(EntityLiving par1EntityLiving, int par2, float par3) {
   }

   protected float getDeathMaxRotation(EntityLiving par1EntityLiving) {
      return 90.0F;
   }

   protected int getColorMultiplier(EntityLiving par1EntityLiving, float par2, float par3) {
      return 0;
   }

   protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
   }

   protected void passSpecialRender(EntityLiving par1EntityLiving, double par2, double par4, double par6) {
      if (Minecraft.isGuiEnabled()
         && par1EntityLiving != this.renderManager.livingPlayer
         && !par1EntityLiving.c(Minecraft.getMinecraft().thePlayer)
         && (par1EntityLiving.func_94059_bO() || par1EntityLiving.func_94056_bM() && par1EntityLiving == this.renderManager.field_96451_i)) {
         float var8 = 1.6F;
         float var9 = 0.016666668F * var8;
         double var10 = par1EntityLiving.e(this.renderManager.livingPlayer);
         float var12 = par1EntityLiving.ag() ? 32.0F : 64.0F;
         if (var10 < var12 * var12) {
            String var13 = par1EntityLiving.ax();
            if (par1EntityLiving.ag()) {
               if (BTWMod.isHardcorePlayerNamesEnabled(par1EntityLiving.worldObj)) {
                  return;
               }

               FontRenderer var14 = this.a();
               GL11.glPushMatrix();
               GL11.glTranslatef((float)par2 + 0.0F, (float)par4 + par1EntityLiving.height + 0.5F, (float)par6);
               GL11.glNormal3f(0.0F, 1.0F, 0.0F);
               GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
               GL11.glScalef(-var9, -var9, var9);
               GL11.glDisable(2896);
               GL11.glTranslatef(0.0F, 0.25F / var9, 0.0F);
               GL11.glDepthMask(false);
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               Tessellator var15 = Tessellator.instance;
               GL11.glDisable(3553);
               var15.startDrawingQuads();
               int var16 = var14.getStringWidth(var13) / 2;
               var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
               var15.addVertex(-var16 - 1, -1.0, 0.0);
               var15.addVertex(-var16 - 1, 8.0, 0.0);
               var15.addVertex(var16 + 1, 8.0, 0.0);
               var15.addVertex(var16 + 1, -1.0, 0.0);
               var15.draw();
               GL11.glEnable(3553);
               GL11.glDepthMask(true);
               var14.drawString(var13, -var14.getStringWidth(var13) / 2, 0, 553648127);
               GL11.glEnable(2896);
               GL11.glDisable(3042);
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glPopMatrix();
            } else {
               this.func_96449_a(par1EntityLiving, par2, par4, par6, var13, var9, var10);
            }
         }
      }
   }

   protected void func_96449_a(EntityLiving par1EntityLiving, double par2, double par4, double par6, String par8Str, float par9, double par10) {
      if (par1EntityLiving.isPlayerSleeping()) {
         this.renderLivingLabel(par1EntityLiving, par8Str, par2, par4 - 1.5, par6, 64);
      } else {
         this.renderLivingLabel(par1EntityLiving, par8Str, par2, par4, par6, 64);
      }
   }

   protected void renderLivingLabel(EntityLiving par1EntityLiving, String par2Str, double par3, double par5, double par7, int par9) {
      double var10 = par1EntityLiving.e(this.renderManager.livingPlayer);
      if (var10 <= par9 * par9) {
         FontRenderer var12 = this.a();
         float var13 = 1.6F;
         float var14 = 0.016666668F * var13;
         GL11.glPushMatrix();
         GL11.glTranslatef((float)par3 + 0.0F, (float)par5 + par1EntityLiving.height + 0.5F, (float)par7);
         GL11.glNormal3f(0.0F, 1.0F, 0.0F);
         GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
         GL11.glScalef(-var14, -var14, var14);
         GL11.glDisable(2896);
         GL11.glDepthMask(false);
         if (!BTWMod.areHardcorePlayerNamesObstructed(par1EntityLiving.worldObj)) {
            GL11.glDisable(2929);
         }

         GL11.glEnable(3042);
         GL11.glBlendFunc(770, 771);
         Tessellator var15 = Tessellator.instance;
         byte var16 = 0;
         if (par2Str.equals("deadmau5")) {
            var16 = -10;
         }

         GL11.glDisable(3553);
         var15.startDrawingQuads();
         int var17 = var12.getStringWidth(par2Str) / 2;
         var15.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
         var15.addVertex(-var17 - 1, -1 + var16, 0.0);
         var15.addVertex(-var17 - 1, 8 + var16, 0.0);
         var15.addVertex(var17 + 1, 8 + var16, 0.0);
         var15.addVertex(var17 + 1, -1 + var16, 0.0);
         var15.draw();
         GL11.glEnable(3553);
         var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, 553648127);
         GL11.glEnable(2929);
         GL11.glDepthMask(true);
         var12.drawString(par2Str, -var12.getStringWidth(par2Str) / 2, var16, -1);
         GL11.glEnable(2896);
         GL11.glDisable(3042);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPopMatrix();
      }
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.doRenderLiving((EntityLiving)par1Entity, par2, par4, par6, par8, par9);
   }
}
