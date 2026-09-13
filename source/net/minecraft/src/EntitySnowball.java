package net.minecraft.src;

public class EntitySnowball extends EntityThrowable {
   public EntitySnowball(World var1) {
      super(var1);
   }

   public EntitySnowball(World var1, EntityLiving var2) {
      super(var1, var2);
   }

   public EntitySnowball(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   protected void onImpact(MovingObjectPosition var1) {
      if (var1.entityHit != null) {
         byte var2 = 0;
         if (var1.entityHit instanceof EntityBlaze) {
            var2 = 3;
         }

         var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.h()), var2);
      }

      for (int var3 = 0; var3 < 8; var3++) {
         this.worldObj.spawnParticle("snowballpoof", this.posX, this.posY, this.posZ, 0.0, 0.0, 0.0);
      }

      if (!this.worldObj.isRemote) {
         this.w();
      }
   }
}
