package net.minecraft.src;

public class EntityCloudFX extends EntityFX {
   float field_70569_a;

   public EntityCloudFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, 0.0, 0.0, 0.0);
      float var14 = 2.5F;
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      this.motionX += var8;
      this.motionY += var10;
      this.motionZ += var12;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.3F);
      this.particleScale *= 0.75F;
      this.particleScale *= var14;
      this.field_70569_a = this.particleScale;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.3));
      this.particleMaxAge = (int)(this.particleMaxAge * var14);
      this.noClip = false;
   }

   @Override
   public void renderParticle(Tessellator var1, float var2, float var3, float var4, float var5, float var6, float var7) {
      float var8 = (this.particleAge + var2) / this.particleMaxAge * 32.0F;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      }

      if (var8 > 1.0F) {
         var8 = 1.0F;
      }

      this.particleScale = this.field_70569_a * var8;
      super.renderParticle(var1, var2, var3, var4, var5, var6, var7);
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
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.96F;
      this.motionY *= 0.96F;
      this.motionZ *= 0.96F;
      EntityPlayer var1 = this.worldObj.getClosestPlayerToEntity(this, 2.0);
      if (var1 != null && this.posY > var1.boundingBox.minY) {
         this.posY = this.posY + (var1.boundingBox.minY - this.posY) * 0.2;
         this.motionY = this.motionY + (var1.motionY - this.motionY) * 0.2;
         this.b(this.posX, this.posY, this.posZ);
      }

      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }
}
