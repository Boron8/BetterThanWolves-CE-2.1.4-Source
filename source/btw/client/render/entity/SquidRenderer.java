package btw.client.render.entity;

import btw.entity.mob.SquidEntity;
import btw.entity.model.SquidModel;
import btw.entity.model.SquidTentacleModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MathHelper;
import net.minecraft.src.RenderLiving;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class SquidRenderer extends RenderLiving {
   SquidTentacleModel tentacleAttackModel = new SquidTentacleModel();

   public SquidRenderer() {
      super(new SquidModel(), 0.7F);
   }

   @Override
   protected float handleRotationFloat(EntityLiving entity, float par2) {
      SquidEntity squid = (SquidEntity)entity;
      return squid.prevTentacleAngle + (squid.tentacleAngle - squid.prevTentacleAngle) * par2;
   }

   @Override
   protected void rotateCorpse(EntityLiving entity, float par2, float par3, float par4) {
      SquidEntity squid = (SquidEntity)entity;
      float var5 = squid.prevSquidPitch + (squid.squidPitch - squid.prevSquidPitch) * par4;
      float var6 = squid.prevSquidYaw + (squid.squidYaw - squid.prevSquidYaw) * par4;
      GL11.glTranslatef(0.0F, 0.5F, 0.0F);
      GL11.glRotatef(180.0F - par3, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, -1.2F, 0.0F);
   }

   @Override
   public void doRenderLiving(EntityLiving par1EntityLiving, double par2, double par4, double par6, float par8, float par9) {
      super.doRenderLiving(par1EntityLiving, par2, par4, par6, par8, par9);
   }

   @Override
   public void doRender(Entity entity, double par2, double par4, double par6, float par8, float par9) {
      super.doRenderLiving((EntityLiving)entity, par2, par4, par6, par8, par9);
      this.renderTentacleAttack((SquidEntity)entity, par2, par4, par6, par8, par9);
   }

   public void renderTentacleAttack(SquidEntity squid, double dRenderX, double dRenderY, double dRenderZ, float par8, float dPartialTick) {
      int iAttackProgressCounter = squid.tentacleAttackInProgressCounter;
      if (iAttackProgressCounter > 0) {
         float fPartialAttackProgress = iAttackProgressCounter - 1 + dPartialTick;
         Vec3 worldTipPos = squid.computeTentacleAttackTip(fPartialAttackProgress);
         if (squid.isHeadCrab()) {
            dRenderY -= squid.height * 2.0F / 3.0F;
         }

         double dLocalSourcePosY = dRenderY;
         if (!squid.isHeadCrab()) {
            dLocalSourcePosY = dRenderY + squid.height / 2.0F;
         }

         double dLocalTipPosX = worldTipPos.xCoord - squid.posX + dRenderX;
         double dLocalTipPosY = worldTipPos.yCoord - squid.posY + dRenderY;
         double dLocalTipPosZ = worldTipPos.zCoord - squid.posZ + dRenderZ;
         double dDeltaX = dLocalTipPosX - dRenderX;
         double dDeltaY = dLocalTipPosY - dLocalSourcePosY;
         double dDeltaZ = dLocalTipPosZ - dRenderZ;
         double dTentacleLength = MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ);
         double dFlatTentacleLength = MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaZ * dDeltaZ);
         Tessellator tesslator = Tessellator.instance;
         GL11.glPushMatrix();
         GL11.glDisable(2884);
         GL11.glTranslatef((float)dRenderX, (float)dRenderY, (float)dRenderZ);
         GL11.glEnable(32826);
         this.a("/mob/squid.png");
         float fTentacleYaw = (float)(Math.atan2(dDeltaX, dDeltaZ) * 180.0 / Math.PI);
         float fTentaclePitch = (float)(Math.atan2(dFlatTentacleLength, dDeltaY) * 180.0 / Math.PI);
         float fScaleTentacleWidth = (float)(1.0 - 0.6 * squid.getAttackProgressSin(fPartialAttackProgress));
         if (fScaleTentacleWidth > 1.0F) {
            fScaleTentacleWidth = 1.0F;
         }

         float fScaleTentacleLength = (float)dTentacleLength;
         this.tentacleAttackModel.render(squid, fScaleTentacleWidth, fScaleTentacleLength, fScaleTentacleWidth, fTentacleYaw, fTentaclePitch, 0.0625F);
         GL11.glPopMatrix();
      }
   }
}
