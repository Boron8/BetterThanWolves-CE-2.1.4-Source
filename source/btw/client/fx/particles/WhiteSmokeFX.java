package btw.client.fx.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityFX;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

@Environment(EnvType.CLIENT)
public class WhiteSmokeFX extends EntityFX {
   protected float smokeParticleScale;

   public WhiteSmokeFX(World world, double fXPos, double fYPos, double fZPos, double fXVel, double fYVel, double fZVel) {
      super(world, fXPos, fYPos, fZPos, 0.0, 0.0, 0.0);
      float f = 2.5F;
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      this.motionX += fXVel;
      this.motionY += fYVel;
      this.motionZ += fZVel;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F - (float)(Math.random() * 0.3F);
      this.particleScale *= 0.75F;
      this.particleScale *= f;
      this.smokeParticleScale = this.particleScale;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.3));
      this.particleMaxAge = (int)(this.particleMaxAge * f);
      this.noClip = false;
   }

   @Override
   public void renderParticle(Tessellator tessellator, float fPartialTicks, float par3, float par4, float par5, float par6, float par7) {
      float f = (this.particleAge + fPartialTicks) / this.particleMaxAge * 32.0F;
      if (f < 0.0F) {
         f = 0.0F;
      }

      if (f > 1.0F) {
         f = 1.0F;
      }

      this.particleScale = this.smokeParticleScale * f;
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

      this.i(7 - this.particleAge * 8 / this.particleMaxAge);
      this.motionY += 0.004;
      this.d(this.motionX, this.motionY, this.motionZ);
      if (this.posY == this.prevPosY) {
         this.motionX *= 1.1;
         this.motionZ *= 1.1;
      }

      this.motionX *= 0.96F;
      this.motionY *= 0.96F;
      this.motionZ *= 0.96F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }
}
