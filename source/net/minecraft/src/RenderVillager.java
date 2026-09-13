package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderVillager extends RenderLiving {
   protected ModelVillager villagerModel = (ModelVillager)this.mainModel;

   public RenderVillager() {
      super(new ModelVillager(0.0F), 0.5F);
      this.a(this.villagerModel);
   }

   protected int shouldVillagerRenderPass(EntityVillager par1EntityVillager, int par2, float par3) {
      return this.renderEyes(par1EntityVillager, par2);
   }

   public void renderVillager(EntityVillager par1EntityVillager, double par2, double par4, double par6, float par8, float par9) {
      super.doRenderLiving(par1EntityVillager, par2, par4, par6, par8, par9);
   }

   protected void renderVillagerEquipedItems(EntityVillager par1EntityVillager, float par2) {
      super.renderEquippedItems(par1EntityVillager, par2);
   }

   protected void preRenderVillager(EntityVillager par1EntityVillager, float par2) {
      float var3 = 0.9375F;
      if (par1EntityVillager.b() < 0) {
         var3 = (float)(var3 * 0.5);
         this.shadowSize = 0.25F;
      } else {
         this.shadowSize = 0.5F;
      }

      GL11.glScalef(var3, var3, var3);
   }

   @Override
   protected void preRenderCallback(EntityLiving par1EntityLiving, float par2) {
      this.preRenderVillager((EntityVillager)par1EntityLiving, par2);
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      return this.shouldVillagerRenderPass((EntityVillager)par1EntityLiving, par2, par3);
   }

   @Override
   protected void renderEquippedItems(EntityLiving par1EntityLiving, float par2) {
      this.renderVillagerEquipedItems((EntityVillager)par1EntityLiving, par2);
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderVillager((EntityVillager)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderVillager((EntityVillager)par1Entity, par2, par4, par6, par8, par9);
   }

   protected int renderEyes(EntityVillager villager, int iRenderPass) {
      if (iRenderPass == 0 && villager.getProfession() == 2 && villager.getCurrentTradeLevel() >= 5) {
         this.a("/btwmodtex/fcPriestEyes.png");
         GL11.glEnable(3042);
         GL11.glDisable(3008);
         GL11.glBlendFunc(1, 1);
         GL11.glDisable(2896);
         if (villager.ai()) {
            GL11.glDepthMask(false);
         } else {
            GL11.glDepthMask(true);
         }

         char var5 = '\uf0f0';
         int var6 = var5 % 65536;
         int var7 = var5 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var6 / 1.0F, var7 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glEnable(2896);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         return 1;
      } else {
         return -1;
      }
   }
}
