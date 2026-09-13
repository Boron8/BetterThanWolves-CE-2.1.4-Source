package btw.client.fx.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityFX;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

@Environment(EnvType.CLIENT)
public class CindersFX extends EntityFX {
   private float cinderParticleScale;

   public CindersFX(World par1World, double fXPos, double fYPos, double fZPos) {
      super(par1World, fXPos, fYPos, fZPos, 0.0, 0.0, 0.0);
      this.motionX *= 1.5;
      this.motionY *= 2.0;
      this.motionZ *= 1.5;
      this.motionY = this.rand.nextDouble() * 0.4 + 0.05;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleScale = this.particleScale * (this.rand.nextFloat() * 2.0F + 0.2F);
      this.cinderParticleScale = this.particleScale;
      this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
      this.noClip = false;
      this.i(49);
   }

   @Override
   public int getBrightnessForRender(float fPartialTicks) {
      float fAgeFraction = (this.particleAge + fPartialTicks) / this.particleMaxAge;
      MathHelper.clamp_float(fAgeFraction, 0.0F, 1.0F);
      int iBrightnessHighBits = super.b(fPartialTicks) >> 16 & 0xFF;
      int iBrightnessLowBits = 240;
      return iBrightnessLowBits | iBrightnessHighBits << 16;
   }

   @Override
   public float getBrightness(float fPartialTicks) {
      return 1.0F;
   }

   @Override
   public void renderParticle(Tessellator tessellator, float fPartialTicks, float par3, float par4, float par5, float par6, float par7) {
      float fAgeFraction = (this.particleAge + fPartialTicks) / this.particleMaxAge;
      this.particleScale = this.cinderParticleScale * (1.0F - fAgeFraction * fAgeFraction);
      super.renderParticle(tessellator, fPartialTicks, par3, par4, par5, par6, par7);
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }

      float fAgeFraction = (float)this.particleAge / this.particleMaxAge;
      if (this.rand.nextFloat() > fAgeFraction) {
         this.worldObj.spawnParticle("smoke", this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ);
      }

      this.motionY -= 0.03;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.999F;
      this.motionY *= 0.999F;
      this.motionZ *= 0.999F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }
}
