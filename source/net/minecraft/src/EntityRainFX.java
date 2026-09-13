package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.cc.Colorizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityRainFX extends EntityFX {
   public EntityRainFX(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6, 0.0, 0.0, 0.0);
      this.motionX *= 0.3F;
      this.motionY = (float)Math.random() * 0.2F + 0.1F;
      this.motionZ *= 0.3F;
      if (ColorizeBlock.computeWaterColor(false, (int)this.posX, (int)this.posY, (int)this.posZ)) {
         this.particleRed = Colorizer.setColor[0];
         this.particleGreen = Colorizer.setColor[1];
         this.particleBlue = Colorizer.setColor[2];
      } else {
         this.particleRed = 0.2F;
         this.particleGreen = 0.3F;
         this.particleBlue = 1.0F;
      }

      this.i(19 + this.rand.nextInt(4));
      this.a(0.01F, 0.01F);
      this.particleGravity = 0.06F;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY = this.motionY - this.particleGravity;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.98F;
      this.motionY *= 0.98F;
      this.motionZ *= 0.98F;
      if (this.particleMaxAge-- <= 0) {
         this.w();
      }

      if (this.onGround) {
         if (Math.random() < 0.5) {
            this.w();
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
