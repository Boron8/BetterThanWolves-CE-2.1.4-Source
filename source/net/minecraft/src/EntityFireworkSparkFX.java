package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityFireworkSparkFX extends EntityFX {
   private int field_92049_a = 160;
   private boolean field_92054_ax;
   private boolean field_92048_ay;
   private final EffectRenderer field_92047_az;
   private float field_92050_aA;
   private float field_92051_aB;
   private float field_92052_aC;
   private boolean field_92053_aD;

   public EntityFireworkSparkFX(
      World par1World, double par2, double par4, double par6, double par8, double par10, double par12, EffectRenderer par14EffectRenderer
   ) {
      super(par1World, par2, par4, par6);
      this.motionX = par8;
      this.motionY = par10;
      this.motionZ = par12;
      this.field_92047_az = par14EffectRenderer;
      this.particleScale *= 0.75F;
      this.particleMaxAge = 48 + this.rand.nextInt(12);
      this.noClip = false;
   }

   public void func_92045_e(boolean par1) {
      this.field_92054_ax = par1;
   }

   public void func_92043_f(boolean par1) {
      this.field_92048_ay = par1;
   }

   public void func_92044_a(int par1) {
      float var2 = ((par1 & 0xFF0000) >> 16) / 255.0F;
      float var3 = ((par1 & 0xFF00) >> 8) / 255.0F;
      float var4 = ((par1 & 0xFF) >> 0) / 255.0F;
      float var5 = 1.0F;
      this.b(var2 * var5, var3 * var5, var4 * var5);
   }

   public void func_92046_g(int par1) {
      this.field_92050_aA = ((par1 & 0xFF0000) >> 16) / 255.0F;
      this.field_92051_aB = ((par1 & 0xFF00) >> 8) / 255.0F;
      this.field_92052_aC = ((par1 & 0xFF) >> 0) / 255.0F;
      this.field_92053_aD = true;
   }

   @Override
   public AxisAlignedBB getBoundingBox() {
      return null;
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      if (!this.field_92048_ay || this.particleAge < this.particleMaxAge / 3 || (this.particleAge + this.particleMaxAge) / 3 % 2 == 0) {
         super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
      }
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }

      if (this.particleAge > this.particleMaxAge / 2) {
         this.g(1.0F - ((float)this.particleAge - this.particleMaxAge / 2) / this.particleMaxAge);
         if (this.field_92053_aD) {
            this.particleRed = this.particleRed + (this.field_92050_aA - this.particleRed) * 0.2F;
            this.particleGreen = this.particleGreen + (this.field_92051_aB - this.particleGreen) * 0.2F;
            this.particleBlue = this.particleBlue + (this.field_92052_aC - this.particleBlue) * 0.2F;
         }
      }

      this.i(this.field_92049_a + (7 - this.particleAge * 8 / this.particleMaxAge));
      this.motionY -= 0.004;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.91F;
      this.motionY *= 0.91F;
      this.motionZ *= 0.91F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }

      if (this.field_92054_ax && this.particleAge < this.particleMaxAge / 2 && (this.particleAge + this.particleMaxAge) % 2 == 0) {
         EntityFireworkSparkFX var1 = (EntityFireworkSparkFX)EntityList.createEntityOfType(
            EntityFireworkSparkFX.class, this.worldObj, this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0, this.field_92047_az
         );
         var1.b(this.particleRed, this.particleGreen, this.particleBlue);
         var1.particleAge = var1.particleMaxAge / 2;
         if (this.field_92053_aD) {
            var1.field_92053_aD = true;
            var1.field_92050_aA = this.field_92050_aA;
            var1.field_92051_aB = this.field_92051_aB;
            var1.field_92052_aC = this.field_92052_aC;
         }

         var1.field_92048_ay = this.field_92048_ay;
         this.field_92047_az.addEffect(var1);
      }
   }

   @Override
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   @Override
   public float getBrightness(float par1) {
      return 1.0F;
   }
}
