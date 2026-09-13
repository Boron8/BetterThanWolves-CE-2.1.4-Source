package net.minecraft.src;

public class EntityAIOwnerHurtByTarget extends EntityAITarget {
   EntityTameable theDefendingTameable;
   EntityLiving theOwnerAttacker;

   public EntityAIOwnerHurtByTarget(EntityTameable par1EntityTameable) {
      super(par1EntityTameable, 32.0F, false);
      this.theDefendingTameable = par1EntityTameable;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.theDefendingTameable.isSitting()) {
         return false;
      } else if (!this.theDefendingTameable.isTamed()) {
         return false;
      } else {
         EntityLiving var1 = this.theDefendingTameable.getOwner();
         if (var1 == null) {
            return false;
         } else {
            this.theOwnerAttacker = var1.getAITarget();
            return this.a(this.theOwnerAttacker, false);
         }
      }
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.theOwnerAttacker);
      super.startExecuting();
   }

   @Override
   public boolean continueExecuting() {
      return this.theDefendingTameable.isSitting() ? false : super.continueExecuting();
   }
}
