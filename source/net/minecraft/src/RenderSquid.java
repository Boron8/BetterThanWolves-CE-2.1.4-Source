package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSquid extends RenderLiving {
   public RenderSquid(ModelBase var1, float var2) {
      super(var1, var2);
   }

   public void renderLivingSquid(EntitySquid var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }

   protected void rotateSquidsCorpse(EntitySquid var1, float var2, float var3, float var4) {
      float var5 = var1.prevSquidPitch + (var1.squidPitch - var1.prevSquidPitch) * var4;
      float var6 = var1.prevSquidYaw + (var1.squidYaw - var1.prevSquidYaw) * var4;
      GL11.glTranslatef(0.0F, 0.5F, 0.0F);
      GL11.glRotatef(180.0F - var3, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(var5, 1.0F, 0.0F, 0.0F);
      GL11.glRotatef(var6, 0.0F, 1.0F, 0.0F);
      GL11.glTranslatef(0.0F, -1.2F, 0.0F);
   }

   protected float handleRotationFloat(EntitySquid var1, float var2) {
      return var1.prevTentacleAngle + (var1.tentacleAngle - var1.prevTentacleAngle) * var2;
   }
}
