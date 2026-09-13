package net.minecraft.src;

import btw.entity.mob.CowEntity;
import btw.entity.model.CowUdderModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderCow extends RenderLiving {
   CowUdderModel modelUdder = new CowUdderModel();

   public RenderCow(ModelBase par1ModelBase, float par2) {
      super(par1ModelBase, par2);
      this.a(this.modelUdder);
   }

   public void renderCow(EntityCow par1EntityCow, double par2, double par4, double par6, float par8, float par9) {
      super.doRenderLiving(par1EntityCow, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      this.renderCow((EntityCow)par1EntityLiving, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
      this.renderCow((EntityCow)par1Entity, par2, par4, par6, par8, par9);
   }

   @Override
   protected int shouldRenderPass(EntityLiving par1EntityLiving, int par2, float par3) {
      if (par2 == 0 && ((CowEntity)par1EntityLiving).gotMilk()) {
         this.a("/btwmodtex/cow_udder.png");
         return 1;
      } else {
         return -1;
      }
   }

   public void renderKickAttackDebug(CowEntity cow, double dRenderX, double dRenderY, double dRenderZ, float par8, float dPartialTick) {
      Vec3 worldTipPos = cow.computeKickAttackCenter();
      double dLocalSourcePosY = dRenderY + cow.height / 2.0F;
      double dLocalTipPosX = worldTipPos.xCoord - cow.posX + dRenderX;
      double dLocalTipPosY = worldTipPos.yCoord - cow.posY + dRenderY;
      double dLocalTipPosZ = worldTipPos.zCoord - cow.posZ + dRenderZ;
      Tessellator tesslator = Tessellator.instance;
      GL11.glDisable(3553);
      GL11.glDisable(2896);
      tesslator.startDrawing(3);
      if (cow.kickAttackInProgressCounter >= 0) {
         tesslator.setColorOpaque_I(16711680);
      } else {
         tesslator.setColorOpaque_I(0);
      }

      tesslator.addVertex(dRenderX, dLocalSourcePosY, dRenderZ);
      tesslator.addVertex(dLocalTipPosX, dLocalTipPosY, dLocalTipPosZ);
      tesslator.draw();
      tesslator.startDrawing(3);
      if (cow.kickAttackInProgressCounter >= 0) {
         tesslator.setColorOpaque_I(16776960);
      } else {
         tesslator.setColorOpaque_I(16777215);
      }

      tesslator.addVertex(dLocalTipPosX + 1.375, dLocalTipPosY, dLocalTipPosZ);
      tesslator.addVertex(dLocalTipPosX - 1.375, dLocalTipPosY, dLocalTipPosZ);
      tesslator.draw();
      tesslator.startDrawing(3);
      if (cow.kickAttackInProgressCounter >= 0) {
         tesslator.setColorOpaque_I(16776960);
      } else {
         tesslator.setColorOpaque_I(16777215);
      }

      tesslator.addVertex(dLocalTipPosX, dLocalTipPosY, dLocalTipPosZ + 1.375);
      tesslator.addVertex(dLocalTipPosX, dLocalTipPosY, dLocalTipPosZ - 1.375);
      tesslator.draw();
      tesslator.startDrawing(3);
      if (cow.kickAttackInProgressCounter >= 0) {
         tesslator.setColorOpaque_I(16776960);
      } else {
         tesslator.setColorOpaque_I(16777215);
      }

      tesslator.addVertex(dLocalTipPosX, dLocalTipPosY + 1.0, dLocalTipPosZ);
      tesslator.addVertex(dLocalTipPosX, dLocalTipPosY - 1.0, dLocalTipPosZ);
      tesslator.draw();
      GL11.glEnable(2896);
      GL11.glEnable(3553);
   }
}
