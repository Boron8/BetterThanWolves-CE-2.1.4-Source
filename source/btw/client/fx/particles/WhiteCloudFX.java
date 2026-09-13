package btw.client.fx.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.World;

@Environment(EnvType.CLIENT)
public class WhiteCloudFX extends WhiteSmokeFX {
   public WhiteCloudFX(World world, double fXPos, double fYPos, double fZPos, double fXVel, double fYVel, double fZVel) {
      super(world, fXPos, fYPos, fZPos, fXVel, fYVel, fZVel);
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
      this.motionX *= 0.96F;
      this.motionY *= 0.96F;
      this.motionZ *= 0.96F;
      if (this.onGround) {
         this.motionX *= 0.7F;
         this.motionZ *= 0.7F;
      }
   }
}
