package net.minecraft.src;

public class EntityAIHurtByTarget extends EntityAITarget {
   private boolean nearbyEntitiesOfSameTypeAttack;
   EntityLiving entityPathNavigate;

   public EntityAIHurtByTarget(EntityLiving par1EntityLiving, boolean par2) {
      super(par1EntityLiving, 16.0F, false);
      this.nearbyEntitiesOfSameTypeAttack = par2;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      return this.a(this.taskOwner.getAITarget(), false);
   }

   @Override
   public boolean continueExecuting() {
      return this.taskOwner.getAITarget() != null
         && this.taskOwner.getAITarget().isEntityAlive()
         && this.taskOwner.getAITarget() == this.entityPathNavigate
         && this.taskOwner.getAttackTarget() != null
         && this.taskOwner.getAttackTarget() == this.entityPathNavigate;
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
      this.entityPathNavigate = this.taskOwner.getAITarget();
      if (this.nearbyEntitiesOfSameTypeAttack) {
         for (EntityLiving var3 : this.taskOwner
            .worldObj
            .getEntitiesWithinAABB(
               this.taskOwner.getClass(),
               AxisAlignedBB.getAABBPool()
                  .getAABB(
                     this.taskOwner.posX,
                     this.taskOwner.posY,
                     this.taskOwner.posZ,
                     this.taskOwner.posX + 1.0,
                     this.taskOwner.posY + 1.0,
                     this.taskOwner.posZ + 1.0
                  )
                  .expand(this.targetDistance, 10.0, this.targetDistance)
            )) {
            if (this.taskOwner != var3 && var3.getAttackTarget() == null && var3.getAITarget() == null) {
               var3.setRevengeTarget(this.taskOwner.getAITarget());
            }
         }
      }

      super.startExecuting();
   }

   @Override
   public void resetTask() {
      if (this.taskOwner.getAttackTarget() != null && this.entityPathNavigate == this.taskOwner.getAttackTarget()) {
         this.taskOwner.setAttackTarget((EntityLiving)null);
      }

      if (this.taskOwner.getAITarget() != null && this.entityPathNavigate == this.taskOwner.getAITarget()) {
         this.taskOwner.setRevengeTarget(null);
      }
   }
}
