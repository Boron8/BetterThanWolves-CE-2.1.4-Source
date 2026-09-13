package net.minecraft.src;

public class EntityBreakingFX extends EntityFX {
   public EntityBreakingFX(World var1, double var2, double var4, double var6, Item var8, RenderEngine var9) {
      super(var1, var2, var4, var6, 0.0, 0.0, 0.0);
      this.a(var9, var8.getIconFromDamage(0));
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleGravity = Block.blockSnow.blockParticleGravity;
      this.particleScale /= 2.0F;
   }

   public EntityBreakingFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12, Item var14, RenderEngine var15) {
      this(var1, var2, var4, var6, var14, var15);
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      this.motionX += var8;
      this.motionY += var10;
      this.motionZ += var12;
   }

   @Override
   public int getFXLayer() {
      return 2;
   }

   @Override
   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = (this.particleTextureIndexX + this.particleTextureJitterX / 4.0F) / 16.0F;
      float var9 = var8 + 0.015609375F;
      float var10 = (this.particleTextureIndexY + this.particleTextureJitterY / 4.0F) / 16.0F;
      float var11 = var10 + 0.015609375F;
      float var12 = 0.1F * this.particleScale;
      if (this.particleIcon != null) {
         var8 = this.particleIcon.getInterpolatedU(this.particleTextureJitterX / 4.0F * 16.0F);
         var9 = this.particleIcon.getInterpolatedU((this.particleTextureJitterX + 1.0F) / 4.0F * 16.0F);
         var10 = this.particleIcon.getInterpolatedV(this.particleTextureJitterY / 4.0F * 16.0F);
         var11 = this.particleIcon.getInterpolatedV((this.particleTextureJitterY + 1.0F) / 4.0F * 16.0F);
      }

      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * var2 - ay);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * var2 - az);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * var2 - aA);
      float var16 = 1.0F;
      var1.setColorOpaque_F(var16 * this.particleRed, var16 * this.particleGreen, var16 * this.particleBlue);
      var1.addVertexWithUV(var13 - var3 * var12 - var6 * var12, var14 - var4 * var12, var15 - var5 * var12 - var7 * var12, var8, var11);
      var1.addVertexWithUV(var13 - var3 * var12 + var6 * var12, var14 + var4 * var12, var15 - var5 * var12 + var7 * var12, var8, var10);
      var1.addVertexWithUV(var13 + var3 * var12 + var6 * var12, var14 + var4 * var12, var15 + var5 * var12 + var7 * var12, var9, var10);
      var1.addVertexWithUV(var13 + var3 * var12 - var6 * var12, var14 - var4 * var12, var15 + var5 * var12 - var7 * var12, var9, var11);
   }
}
