package net.minecraft.src;

public class EntityEnchantmentTableParticleFX extends EntityFX {
   private float field_70565_a;
   private double field_70568_aq;
   private double field_70567_ar;
   private double field_70566_as;

   public EntityEnchantmentTableParticleFX(World var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      super(var1, var2, var4, var6, var8, var10, var12);
      this.motionX = var8;
      this.motionY = var10;
      this.motionZ = var12;
      this.field_70568_aq = this.posX = var2;
      this.field_70567_ar = this.posY = var4;
      this.field_70566_as = this.posZ = var6;
      float var14 = this.rand.nextFloat() * 0.6F + 0.4F;
      this.field_70565_a = this.particleScale = this.rand.nextFloat() * 0.5F + 0.2F;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F * var14;
      this.particleGreen *= 0.9F;
      this.particleRed *= 0.9F;
      this.particleMaxAge = (int)(Math.random() * 10.0) + 30;
      this.noClip = true;
      this.i((int)(Math.random() * 26.0 + 1.0 + 224.0));
   }

   @Override
   public int getBrightnessForRender(float var1) {
      int var2 = super.b(var1);
      float var3 = (float)this.particleAge / this.particleMaxAge;
      var3 *= var3;
      var3 *= var3;
      int var4 = var2 & 0xFF;
      int var5 = var2 >> 16 & 0xFF;
      var5 += (int)(var3 * 15.0F * 16.0F);
      if (var5 > 240) {
         var5 = 240;
      }

      return var4 | var5 << 16;
   }

   @Override
   public float getBrightness(float var1) {
      float var2 = super.c(var1);
      float var3 = (float)this.particleAge / this.particleMaxAge;
      var3 *= var3;
      var3 *= var3;
      return var2 * (1.0F - var3) + var3;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      float var1 = (float)this.particleAge / this.particleMaxAge;
      var1 = 1.0F - var1;
      float var2 = 1.0F - var1;
      var2 *= var2;
      var2 *= var2;
      this.posX = this.field_70568_aq + this.motionX * var1;
      this.posY = this.field_70567_ar + this.motionY * var1 - var2 * 1.2F;
      this.posZ = this.field_70566_as + this.motionZ * var1;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }
   }
}
