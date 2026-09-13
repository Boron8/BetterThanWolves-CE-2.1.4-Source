package net.minecraft.src;

public class EntityAISit extends EntityAIBase {
   private EntityTameable theEntity;
   private boolean isSitting = false;

   public EntityAISit(EntityTameable par1EntityTameable) {
      this.theEntity = par1EntityTameable;
      this.a(5);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.theEntity.isTamed()) {
         return false;
      } else if (this.theEntity.G()) {
         return false;
      } else if (!this.theEntity.onGround) {
         return false;
      } else {
         EntityLiving var1 = this.theEntity.getOwner();
         return var1 == null ? true : this.isSitting;
      }
   }

   @Override
   public void startExecuting() {
      this.theEntity.aC().clearPathEntity();
      this.theEntity.setSitting(true);
   }

   @Override
   public void resetTask() {
      this.theEntity.setSitting(false);
   }

   public void setSitting(boolean par1) {
      this.isSitting = par1;
   }

   public boolean isTryingToSit() {
      return this.isSitting;
   }
}
