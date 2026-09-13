package net.minecraft.src;

public class EntityEnderPearl extends EntityThrowable {
   public EntityEnderPearl(World var1) {
      super(var1);
   }

   public EntityEnderPearl(World var1, EntityLiving var2) {
      super(var1, var2);
   }

   public EntityEnderPearl(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   protected void onImpact(MovingObjectPosition var1) {
      if (var1.entityHit != null) {
         var1.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.h()), 0);
      }

      for (int var2 = 0; var2 < 32; var2++) {
         this.worldObj
            .spawnParticle("portal", this.posX, this.posY + this.rand.nextDouble() * 2.0, this.posZ, this.rand.nextGaussian(), 0.0, this.rand.nextGaussian());
      }

      if (!this.worldObj.isRemote) {
         if (this.h() != null && this.h() instanceof EntityPlayerMP) {
            EntityPlayerMP var3 = (EntityPlayerMP)this.h();
            if (!var3.playerNetServerHandler.connectionClosed && var3.worldObj == this.worldObj) {
               this.h().setPositionAndUpdate(this.posX, this.posY, this.posZ);
               this.h().fallDistance = 0.0F;
               this.h().attackEntityFrom(DamageSource.fall, 5);
            }
         }

         this.w();
      }
   }
}
