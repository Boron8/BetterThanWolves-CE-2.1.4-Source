package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityPortalFX extends EntityFX {
   private float portalParticleScale;
   private double portalPosX;
   private double portalPosY;
   private double portalPosZ;

   public EntityPortalFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      this.motionX = par8;
      this.motionY = par10;
      this.motionZ = par12;
      this.portalPosX = this.posX = par2;
      this.portalPosY = this.posY = par4;
      this.portalPosZ = this.posZ = par6;
      float var14 = this.rand.nextFloat() * 0.6F + 0.4F;
      this.portalParticleScale = this.particleScale = this.rand.nextFloat() * 0.2F + 0.5F;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F * var14;
      this.particleGreen = this.particleGreen * ColorizeEntity.portalColor[1];
      this.particleRed = this.particleRed * ColorizeEntity.portalColor[0];
      this.particleMaxAge = (int)(Math.random() * 10.0) + 40;
      this.noClip = true;
      this.i((int)(Math.random() * 8.0));
      this.particleBlue = ColorizeEntity.portalColor[2];
   }

   @Override
   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float var8 = (this.particleAge + par2) / this.particleMaxAge;
      var8 = 1.0F - var8;
      var8 *= var8;
      var8 = 1.0F - var8;
      this.particleScale = this.portalParticleScale * var8;
      super.renderParticle(par1Tessellator, par2, par3, par4, par5, par6, par7);
   }

   @Override
   public int getBrightnessForRender(float par1) {
      int var2 = super.b(par1);
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
   public float getBrightness(float par1) {
      float var2 = super.c(par1);
      float var3 = (float)this.particleAge / this.particleMaxAge;
      var3 = var3 * var3 * var3 * var3;
      return var2 * (1.0F - var3) + var3;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      float var1 = (float)this.particleAge / this.particleMaxAge;
      float var3 = -var1 + var1 * var1 * 2.0F;
      float var4 = 1.0F - var3;
      this.posX = this.portalPosX + this.motionX * var4;
      this.posY = this.portalPosY + this.motionY * var4 + (1.0F - var1);
      this.posZ = this.portalPosZ + this.motionZ * var4;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }
   }
}
