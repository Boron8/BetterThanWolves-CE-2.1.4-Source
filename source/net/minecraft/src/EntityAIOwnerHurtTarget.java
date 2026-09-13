package net.minecraft.src;

public class EntityAIOwnerHurtTarget extends EntityAITarget {
   EntityTameable theEntityTameable;
   EntityLiving theTarget;

   public EntityAIOwnerHurtTarget(EntityTameable par1EntityTameable) {
      super(par1EntityTameable, 32.0F, false);
      this.theEntityTameable = par1EntityTameable;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.theEntityTameable.isSitting()) {
         return false;
      } else if (!this.theEntityTameable.isTamed()) {
         return false;
      } else {
         EntityLiving var1 = this.theEntityTameable.getOwner();
         if (var1 == null) {
            return false;
         } else {
            this.theTarget = var1.getLastAttackingEntity();
            return this.a(this.theTarget, false);
         }
      }
   }

   @Override
   public void startExecuting() {
      this.taskOwner.setAttackTarget(this.theTarget);
      super.startExecuting();
   }

   @Override
   public boolean continueExecuting() {
      return this.theEntityTameable.isSitting() ? false : super.continueExecuting();
   }
}
