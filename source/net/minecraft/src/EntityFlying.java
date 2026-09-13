package net.minecraft.src;

public abstract class EntityFlying extends EntityLiving {
   public EntityFlying(World var1) {
      super(var1);
   }

   @Override
   protected void fall(float var1) {
   }

   @Override
   protected void updateFallState(double var1, boolean var3) {
   }

   @Override
   public void moveEntityWithHeading(float var1, float var2) {
      if (this.G()) {
         this.a(var1, var2, 0.02F);
         this.d(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.8F;
         this.motionY *= 0.8F;
         this.motionZ *= 0.8F;
      } else if (this.I()) {
         this.a(var1, var2, 0.02F);
         this.d(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.5;
         this.motionY *= 0.5;
         this.motionZ *= 0.5;
      } else {
         float var3 = 0.91F;
         if (this.onGround) {
            var3 = 0.54600006F;
            int var4 = this.worldObj
               .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
            if (var4 > 0) {
               var3 = Block.blocksList[var4].slipperiness * 0.91F;
            }
         }

         float var10 = 0.16277136F / (var3 * var3 * var3);
         this.a(var1, var2, this.onGround ? 0.1F * var10 : 0.02F);
         var3 = 0.91F;
         if (this.onGround) {
            var3 = 0.54600006F;
            int var5 = this.worldObj
               .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
            if (var5 > 0) {
               var3 = Block.blocksList[var5].slipperiness * 0.91F;
            }
         }

         this.d(this.motionX, this.motionY, this.motionZ);
         this.motionX *= var3;
         this.motionY *= var3;
         this.motionZ *= var3;
      }

      this.prevLimbYaw = this.limbYaw;
      double var9 = this.posX - this.prevPosX;
      double var11 = this.posZ - this.prevPosZ;
      float var7 = MathHelper.sqrt_double(var9 * var9 + var11 * var11) * 4.0F;
      if (var7 > 1.0F) {
         var7 = 1.0F;
      }

      this.limbYaw = this.limbYaw + (var7 - this.limbYaw) * 0.4F;
      this.limbSwing = this.limbSwing + this.limbYaw;
   }

   @Override
   public boolean isOnLadder() {
      return false;
   }
}
