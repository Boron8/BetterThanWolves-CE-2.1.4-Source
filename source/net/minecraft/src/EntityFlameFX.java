package net.minecraft.src;

public class EntityFlameFX extends EntityFX {
   private float flameScale;

   public EntityFlameFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.motionX = this.motionX * 0.01F + var8;
      this.motionY = this.motionY * 0.01F + var10;
      this.motionZ = this.motionZ * 0.01F + var12;
      var2 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
      var4 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
      var6 += (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F;
      this.flameScale = this.particleScale;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
      this.noClip = true;
      this.i(48);
   }

   @Override
   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = (this.particleAge + var2) / this.particleMaxAge;
      this.particleScale = this.flameScale * (1.0F - var8 * var8 * 0.5F);
      super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
   }

   @Override
   public int getBrightnessForRender(float var1) {
      float var2 = (this.particleAge + var1) / this.particleMaxAge;
      if (var2 < 0.0F) {
         var2 = 0.0F;
      }

      if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      int var3 = super.b(var1);
      int var4 = var3 & 0xFF;
      int var5 = var3 >> 16 & 0xFF;
      var4 += (int)(var2 * 15.0F * 16.0F);
      if (var4 > 240) {
         var4 = 240;
      }

      return var4 | var5 << 16;
   }

   @Override
   public float getBrightness(float var1) {
      float var2 = (this.particleAge + var1) / this.particleMaxAge;
      if (var2 < 0.0F) {
         var2 = 0.0F;
      }

      if (var2 > 1.0F) {
         var2 = 1.0F;
      }

      float var3 = super.c(var1);
      return var3 * var2 + (1.0F - var2);
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }

      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.96F;
      this.motionY *= 0.96F;
      this.motionZ *= 0.96F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }
}
