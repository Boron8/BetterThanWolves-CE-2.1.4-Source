package net.minecraft.src;

public class EntityExplodeFX extends EntityFX {
   public EntityExplodeFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.motionX = var8 + (float)(Math.random() * 2.0 - 1.0) * 0.05F;
      this.motionY = var10 + (float)(Math.random() * 2.0 - 1.0) * 0.05F;
      this.motionZ = var12 + (float)(Math.random() * 2.0 - 1.0) * 0.05F;
      this.particleRed = this.particleGreen = this.particleBlue = this.rand.nextFloat() * 0.3F + 0.7F;
      this.particleScale = this.rand.nextFloat() * this.rand.nextFloat() * 6.0F + 1.0F;
      this.particleMaxAge = (int)(16.0 / (this.rand.nextFloat() * 0.8 + 0.2)) + 2;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }

      this.i(7 - this.particleAge * 8 / this.particleMaxAge);
      this.motionY += 0.004;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.9F;
      this.motionY *= 0.9F;
      this.motionZ *= 0.9F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }
}
