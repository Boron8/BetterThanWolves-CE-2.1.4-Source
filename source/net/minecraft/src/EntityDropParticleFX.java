package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.cc.ColorizeEntity;
import com.prupe.mcpatcher.cc.Colorizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityDropParticleFX extends EntityFX {
   private Material materialType;
   protected int bobTimer;

   public EntityDropParticleFX(World par1World, double par2, double par4, double par6, Material par8Material) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      this.motionX = this.motionY = this.motionZ = 0.0;
      if (par8Material == Material.water) {
         if (ColorizeBlock.computeWaterColor(true, (int)this.posX, (int)this.posY, (int)this.posZ)) {
            this.particleRed = Colorizer.setColor[0];
            this.particleGreen = Colorizer.setColor[1];
            this.particleBlue = Colorizer.setColor[2];
         } else {
            this.particleRed = 0.2F;
            this.particleGreen = 0.3F;
            this.particleBlue = 1.0F;
         }
      } else {
         this.particleRed = 1.0F;
         this.particleGreen = 0.0F;
         this.particleBlue = 0.0F;
      }

      this.i(113);
      this.a(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.materialType = par8Material;
      this.bobTimer = 40;
      this.particleMaxAge = (int)(64.0 / (Math.random() * 0.8 + 0.2));
      this.motionX = this.motionY = this.motionZ = 0.0;
   }

   public EntityDropParticleFX(World world, double x, double y, double z, Material material, int bobTimer, float red, float green, float blue, float alpha) {
      this(world, x, y, z, material);
      this.particleRed = red;
      this.particleGreen = green;
      this.particleBlue = blue;
      this.bobTimer = bobTimer;
      this.g(alpha);
   }

   @Override
   public int getBrightnessForRender(float par1) {
      return this.materialType == Material.lava ? 257 : super.b(par1);
   }

   @Override
   public float getBrightness(float par1) {
      return this.materialType == Material.lava ? 1.0F : super.c(par1);
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.materialType == Material.lava) {
         if (ColorizeEntity.computeLavaDropColor(40 - this.bobTimer)) {
            this.particleRed = Colorizer.setColor[0];
            this.particleGreen = Colorizer.setColor[1];
            this.particleBlue = Colorizer.setColor[2];
         } else {
            this.particleRed = 1.0F;
            this.particleGreen = 16.0F / (40 - this.bobTimer + 16);
            this.particleBlue = 4.0F / (40 - this.bobTimer + 8);
         }
      }

      this.motionY = this.motionY - this.particleGravity;
      if (this.bobTimer-- > 0) {
         this.motionX *= 0.02;
         this.motionY *= 0.02;
         this.motionZ *= 0.02;
         this.i(113);
      } else {
         this.i(112);
      }

      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.particleMaxAge-- <= 0) {
         this.w();
      }

      if (this.onGround) {
         if (this.materialType == Material.water) {
            this.w();
            this.worldObj.spawnParticle("splash", this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
         } else {
            this.i(114);
         }

         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }

      Material var1 = this.worldObj
         .getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
      if (var1.isLiquid() || var1.isSolid()) {
         double var2 = MathHelper.floor_double(this.posY) + 1
            - BlockFluid.getFluidHeightPercent(
               this.worldObj.getBlockMetadata(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
            );
         if (this.posY < var2) {
            this.w();
         }
      }
   }
}
