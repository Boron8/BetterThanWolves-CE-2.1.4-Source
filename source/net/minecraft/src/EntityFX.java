package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityFX extends Entity {
   public int particleTextureIndexX;
   public int particleTextureIndexY;
   public float particleTextureJitterX;
   public float particleTextureJitterY;
   public int particleAge = 0;
   public int particleMaxAge = 0;
   public float particleScale;
   public float particleGravity;
   public float particleRed;
   public float particleGreen;
   public float particleBlue;
   public float particleAlpha = 1.0F;
   public Icon particleIcon = null;
   public static double interpPosX;
   public static double interpPosY;
   public static double interpPosZ;

   public EntityFX(World par1World, double par2, double par4, double par6) {
      super(par1World);
      this.a(0.2F, 0.2F);
      this.yOffset = this.height / 2.0F;
      this.b(par2, par4, par6);
      this.lastTickPosX = par2;
      this.lastTickPosY = par4;
      this.lastTickPosZ = par6;
      this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
      this.particleTextureJitterX = this.rand.nextFloat() * 3.0F;
      this.particleTextureJitterY = this.rand.nextFloat() * 3.0F;
      this.particleScale = (this.rand.nextFloat() * 0.5F + 0.5F) * 2.0F;
      this.particleMaxAge = (int)(4.0F / (this.rand.nextFloat() * 0.9F + 0.1F));
      this.particleAge = 0;
   }

   public EntityFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      this(par1World, par2, par4, par6);
      this.motionX = par8 + (float)(Math.random() * 2.0 - 1.0) * 0.4F;
      this.motionY = par10 + (float)(Math.random() * 2.0 - 1.0) * 0.4F;
      this.motionZ = par12 + (float)(Math.random() * 2.0 - 1.0) * 0.4F;
      float var14 = (float)(Math.random() + Math.random() + 1.0) * 0.15F;
      float var15 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
      this.motionX = this.motionX / var15 * var14 * 0.4F;
      this.motionY = this.motionY / var15 * var14 * 0.4F + 0.1F;
      this.motionZ = this.motionZ / var15 * var14 * 0.4F;
   }

   public EntityFX multiplyVelocity(float par1) {
      this.motionX *= par1;
      this.motionY = (this.motionY - 0.1F) * par1 + 0.1F;
      this.motionZ *= par1;
      return this;
   }

   public EntityFX multipleParticleScaleBy(float par1) {
      this.a(0.2F * par1, 0.2F * par1);
      this.particleScale *= par1;
      return this;
   }

   public void setRBGColorF(float par1, float par2, float par3) {
      this.particleRed = par1;
      this.particleGreen = par2;
      this.particleBlue = par3;
   }

   public void setAlphaF(float par1) {
      this.particleAlpha = par1;
   }

   public float getRedColorF() {
      return this.particleRed;
   }

   public float getGreenColorF() {
      return this.particleGreen;
   }

   public float getBlueColorF() {
      return this.particleBlue;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.particleAge++ >= this.particleMaxAge) {
         this.w();
      }

      this.motionY = this.motionY - 0.04 * this.particleGravity;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }

   public void renderParticle(Tessellator par1Tessellator, float par2, float par3, float par4, float par5, float par6, float par7) {
      float var8 = this.particleTextureIndexX / 16.0F;
      float var9 = var8 + 0.0624375F;
      float var10 = this.particleTextureIndexY / 16.0F;
      float var11 = var10 + 0.0624375F;
      float var12 = 0.1F * this.particleScale;
      if (this.particleIcon != null) {
         var8 = this.particleIcon.getMinU();
         var9 = this.particleIcon.getMaxU();
         var10 = this.particleIcon.getMinV();
         var11 = this.particleIcon.getMaxV();
      }

      float var13 = (float)(this.prevPosX + (this.posX - this.prevPosX) * par2 - interpPosX);
      float var14 = (float)(this.prevPosY + (this.posY - this.prevPosY) * par2 - interpPosY);
      float var15 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * par2 - interpPosZ);
      float var16 = 1.0F;
      par1Tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, this.particleAlpha);
      par1Tessellator.addVertexWithUV(var13 - par3 * var12 - par6 * var12, var14 - par4 * var12, var15 - par5 * var12 - par7 * var12, var9, var11);
      par1Tessellator.addVertexWithUV(var13 - par3 * var12 + par6 * var12, var14 + par4 * var12, var15 - par5 * var12 + par7 * var12, var9, var10);
      par1Tessellator.addVertexWithUV(var13 + par3 * var12 + par6 * var12, var14 + par4 * var12, var15 + par5 * var12 + par7 * var12, var8, var10);
      par1Tessellator.addVertexWithUV(var13 + par3 * var12 - par6 * var12, var14 - par4 * var12, var15 + par5 * var12 - par7 * var12, var8, var11);
   }

   public int getFXLayer() {
      return 0;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
   }

   public void setParticleIcon(RenderEngine par1RenderEngine, Icon par2Icon) {
      if (this.getFXLayer() == 1) {
         this.particleIcon = par2Icon;
      } else {
         if (this.getFXLayer() != 2) {
            throw new RuntimeException("Invalid call to Particle.setTex, use coordinate methods");
         }

         this.particleIcon = par2Icon;
      }
   }

   public void setParticleTextureIndex(int par1) {
      if (this.getFXLayer() != 0) {
         throw new RuntimeException("Invalid call to Particle.setMiscTex");
      } else {
         this.particleTextureIndexX = par1 % 16;
         this.particleTextureIndexY = par1 / 16;
      }
   }

   public void nextTextureIndexX() {
      this.particleTextureIndexX++;
   }

   @Override
   public boolean canAttackWithItem() {
      return false;
   }

   @Override
   public String toString() {
      return this.getClass().getSimpleName()
         + ", Pos ("
         + this.posX
         + ","
         + this.posY
         + ","
         + this.posZ
         + "), RGBA ("
         + this.particleRed
         + ","
         + this.particleGreen
         + ","
         + this.particleBlue
         + ","
         + this.particleAlpha
         + "), Age "
         + this.particleAge;
   }
}
