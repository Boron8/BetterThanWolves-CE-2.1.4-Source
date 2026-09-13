package net.minecraft.src;

public class EntityAIOcelotAttack extends EntityAIBase {
   World theWorld;
   EntityLiving theEntity;
   EntityLiving theVictim;
   int attackCountdown = 0;

   public EntityAIOcelotAttack(EntityLiving var1) {
      this.theEntity = var1;
      this.theWorld = var1.worldObj;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      EntityLiving var1 = this.theEntity.getAttackTarget();
      if (var1 == null) {
         return false;
      } else {
         this.theVictim = var1;
         return true;
      }
   }

   @Override
   public boolean continueExecuting() {
      if (!this.theVictim.isEntityAlive()) {
         return false;
      } else {
         return this.theEntity.e(this.theVictim) > 225.0 ? false : !this.theEntity.getNavigator().noPath() || this.shouldExecute();
      }
   }

   @Override
   public void resetTask() {
      this.theVictim = null;
      this.theEntity.getNavigator().clearPathEntity();
   }

   @Override
   public void updateTask() {
      this.theEntity.getLookHelper().setLookPositionWithEntity(this.theVictim, 30.0F, 30.0F);
      double var1 = this.theEntity.width * 2.0F * (this.theEntity.width * 2.0F);
      double var3 = this.theEntity.e(this.theVictim.posX, this.theVictim.boundingBox.minY, this.theVictim.posZ);
      float var5 = 0.23F;
      if (var3 > var1 && var3 < 16.0) {
         var5 = 0.4F;
      } else if (var3 < 225.0) {
         var5 = 0.18F;
      }

      this.theEntity.getNavigator().tryMoveToEntityLiving(this.theVictim, var5);
      this.attackCountdown = Math.max(this.attackCountdown - 1, 0);
      if (!(var3 > var1)) {
         if (this.attackCountdown <= 0) {
            this.attackCountdown = 20;
            this.theEntity.attackEntityAsMob(this.theVictim);
         }
      }
   }
}
