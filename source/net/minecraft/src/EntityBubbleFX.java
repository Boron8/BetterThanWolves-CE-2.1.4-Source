package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeBlock;
import com.prupe.mcpatcher.cc.Colorizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntityBubbleFX extends EntityFX {
   public EntityBubbleFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      if (ColorizeBlock.computeWaterColor(false, (int)this.posX, (int)this.posY, (int)this.posZ)) {
         this.particleRed = Colorizer.setColor[0];
         this.particleGreen = Colorizer.setColor[1];
         this.particleBlue = Colorizer.setColor[2];
      } else {
         this.particleRed = 1.0F;
         this.particleGreen = 1.0F;
         this.particleBlue = 1.0F;
      }

      this.i(32);
      this.a(0.02F, 0.02F);
      this.particleScale = this.particleScale * (this.rand.nextFloat() * 0.6F + 0.2F);
      this.motionX = par8 * 0.2F + (float)(Math.random() * 2.0 - 1.0) * 0.02F;
      this.motionY = par10 * 0.2F + (float)(Math.random() * 2.0 - 1.0) * 0.02F;
      this.motionZ = par12 * 0.2F + (float)(Math.random() * 2.0 - 1.0) * 0.02F;
      this.particleMaxAge = (int)(8.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY += 0.002;
      this.d(this.motionX, this.motionY, this.motionZ);
      this.motionX *= 0.85F;
      this.motionY *= 0.85F;
      this.motionZ *= 0.85F;
      if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
         != Material.water) {
         this.w();
      }

      if (this.particleMaxAge-- <= 0) {
         this.w();
      }
   }
}
