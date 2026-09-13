package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeEntity;
import com.prupe.mcpatcher.cc.Colorizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityAuraFX extends EntityFX {
   public EntityAuraFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      float var14 = this.rand.nextFloat() * 0.1F + 0.2F;
      this.particleRed = var14;
      this.particleGreen = var14;
      this.particleBlue = var14;
      this.i(0);
      this.a(0.02F, 0.02F);
      this.particleScale = this.particleScale * (this.rand.nextFloat() * 0.6F + 0.5F);
      this.motionX *= 0.02F;
      this.motionY *= 0.02F;
      this.motionZ *= 0.02F;
      this.particleMaxAge = (int)(20.0 / (Math.random() * 0.8 + 0.2));
      this.noClip = true;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.99;
      this.motionY *= 0.99;
      this.motionZ *= 0.99;
      if (this.particleMaxAge-- <= 0) {
         this.w();
      }
   }

   public EntityAuraFX colorize() {
      if (ColorizeEntity.computeMyceliumParticleColor()) {
         this.particleRed = Colorizer.setColor[0];
         this.particleGreen = Colorizer.setColor[1];
         this.particleBlue = Colorizer.setColor[2];
      }

      return this;
   }
}
