package btw.client.fx.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityFX;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Tessellator;
import net.minecraft.src.World;

@Environment(EnvType.CLIENT)
public class SmallFlameFX extends EntityFX {
   private float startScale;

   public SmallFlameFX(World world, double dXPos, double dYPos, double dZPos, double dXVel, double dYVel, double dZVel) {
      super(world, dXPos, dYPos, dZPos, dXVel, dYVel, dZVel);
      this.motionX = this.motionX * 0.01F + dXVel;
      this.motionY = this.motionY * 0.01F + dYVel;
      this.motionZ = this.motionZ * 0.01F + dZVel;
      this.particleScale *= 0.5F;
      this.startScale = this.particleScale;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2)) + 4;
      this.noClip = true;
      this.i(48);
   }

   @Override
   public void renderParticle(Tessellator tesselator, float fPartialTicks, float par3, float par4, float par5, float par6, float par7) {
      float fDecay = this.getDecay(fPartialTicks);
      this.particleScale = this.startScale * (1.0F - fDecay * fDecay * 0.5F);
      super.renderParticle(tesselator, fPartialTicks, par3, par4, par5, par6, par7);
   }

   @Override
   public int getBrightnessForRender(float fPartialTicks) {
      float fDecay = this.getDecay(fPartialTicks);
      int var3 = super.b(fPartialTicks);
      int var4 = var3 & 0xFF;
      int var5 = var3 >> 16 & 0xFF;
      var4 += (int)(fDecay * 15.0F * 16.0F);
      if (var4 > 240) {
         var4 = 240;
      }

      return var4 | var5 << 16;
   }

   @Override
   public float getBrightness(float fPartialTicks) {
      float fDecay = this.getDecay(fPartialTicks);
      float var3 = super.c(fPartialTicks);
      return var3 * fDecay + (1.0F - fDecay);
   }

   private float getDecay(float fPartialTicks) {
      float fDecay = (this.particleAge + fPartialTicks) / this.particleMaxAge;
      return MathHelper.clamp_float(fDecay, 0.0F, 1.0F);
   }
}
