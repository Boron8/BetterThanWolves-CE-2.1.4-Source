package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderIronGolem extends RenderLiving {
   private ModelIronGolem ironGolemModel = (ModelIronGolem)this.mainModel;

   public RenderIronGolem() {
      super(new ModelIronGolem(), 0.5F);
   }

   public void doRenderIronGolem(EntityIronGolem var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }

   protected void rotateIronGolemCorpse(EntityIronGolem var1, float var2, float var3, float var4) {
      super.rotateCorpse(var1, var2, var3, var4);
      if (!(var1.limbYaw < 0.01)) {
         float var5 = 13.0F;
         float var6 = var1.limbSwing - var1.limbYaw * (1.0F - var4) + 6.0F;
         float var7 = (Math.abs(var6 % var5 - var5 * 0.5F) - var5 * 0.25F) / (var5 * 0.25F);
         GL11.glRotatef(6.5F * var7, 0.0F, 0.0F, 1.0F);
      }
   }

   protected void renderIronGolemEquippedItems(EntityIronGolem var1, float var2) {
      super.renderEquippedItems(var1, var2);
      if (var1.getHoldRoseTick() != 0) {
         GL11.glEnable(32826);
         GL11.glPushMatrix();
         GL11.glRotatef(5.0F + 180.0F * this.ironGolemModel.ironGolemRightArm.rotateAngleX / (float) Math.PI, 1.0F, 0.0F, 0.0F);
         GL11.glTranslatef(-0.6875F, 1.25F, -0.9375F);
         GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
         float var3 = 0.8F;
         GL11.glScalef(var3, -var3, var3);
         int var4 = var1.b(var2);
         int var5 = var4 % 65536;
         int var6 = var4 / 65536;
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, var5 / 1.0F, var6 / 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.a("/terrain.png");
         this.renderBlocks.renderBlockAsItem(Block.plantRed, 0, 1.0F);
         GL11.glPopMatrix();
         GL11.glDisable(32826);
      }
   }
}
