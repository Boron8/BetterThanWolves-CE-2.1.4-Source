package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.cc.Colorizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityReddustFX extends EntityFX {
   float reddustParticleScale;

   public EntityReddustFX(World par1World, double par2, double par4, double par6, float par8, float par9, float par10) {
      this(par1World, par2, par4, par6, 1.0F, par8, par9, par10);
   }

   public EntityReddustFX(World par1World, double par2, double par4, double par6, float par8, float par9, float par10, float par11) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      this.motionX *= 0.1F;
      this.motionY *= 0.1F;
      this.motionZ *= 0.1F;
      if (par9 == 0.0F) {
         par9 = 1.0F;
         if (ColorizeBlock.computeRedstoneWireColor(15)) {
            par9 = Colorizer.setColor[0];
            par10 = Colorizer.setColor[1];
            par11 = Colorizer.setColor[2];
         }
      }

      float var12 = (float)Math.random() * 0.4F + 0.6F;
      this.particleRed = ((float)(Math.random() * 0.2F) + 0.8F) * par9 * var12;
      this.particleGreen = ((float)(Math.random() * 0.2F) + 0.8F) * par10 * var12;
      this.particleBlue = ((float)(Math.random() * 0.2F) + 0.8F) * par11 * var12;
      this.particleScale *= 0.75F;
      this.particleScale *= par8;
      this.reddustParticleScale = this.particleScale;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
      this.particleMaxAge = (int)(this.particleMaxAge * par8);
      this.noClip = false;
   }

   @Override
   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float var8 = (this.particleAge + par2) / this.particleMaxAge * 32.0F;
      if (var8 < 0.0F) {
         var8 = 0.0F;
      }

      if (var8 > 1.0F) {
         var8 = 1.0F;
      }

      this.particleScale = this.reddustParticleScale * var8;
      super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
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
