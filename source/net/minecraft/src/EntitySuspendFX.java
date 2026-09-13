package net.minecraft.src;

import com.prupe.mcpatcher.cc.ColorizeEntity;
import com.prupe.mcpatcher.cc.Colorizer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class EntitySuspendFX extends EntityFX {
   public EntitySuspendFX(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4 - 0.125, par6, par8, par10, par12);
      ColorizeEntity.computeSuspendColor(6710962, (int)par2, (int)par4, (int)par6);
      this.particleRed = Colorizer.setColor[0];
      this.particleGreen = Colorizer.setColor[1];
      this.particleBlue = Colorizer.setColor[2];
      this.i(0);
      this.a(0.01F, 0.01F);
      this.particleScale = this.particleScale * (this.rand.nextFloat() * 0.6F + 0.2F);
      this.motionX = par8 * 0.0;
      this.motionY = par10 * 0.0;
      this.motionZ = par12 * 0.0;
      this.particleMaxAge = (int)(16.0 / (Math.random() * 0.8 + 0.2));
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.d(this.motionX, this.motionY, this.motionZ);
      if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
         != Material.water) {
         this.w();
      }

      if (this.particleMaxAge-- <= 0) {
         this.w();
      }
   }
}
