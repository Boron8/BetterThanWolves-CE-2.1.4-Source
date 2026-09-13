package net.minecraft.src;

public class EntityAICreeperSwell extends EntityAIBase {
   public EntityCreeper swellingCreeper;
   public EntityLiving creeperAttackTarget;

   public EntityAICreeperSwell(EntityCreeper par1EntityCreeper) {
      this.swellingCreeper = par1EntityCreeper;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      EntityLiving var1 = this.swellingCreeper.aJ();
      return this.swellingCreeper.getCreeperState() > 0 || var1 != null && this.swellingCreeper.e(var1) < 9.0;
   }

   @Override
   public void startExecuting() {
      this.swellingCreeper.aC().clearPathEntity();
      this.creeperAttackTarget = this.swellingCreeper.aJ();
   }

   @Override
   public void resetTask() {
      this.creeperAttackTarget = null;
   }

   @Override
   public void updateTask() {
      if (this.creeperAttackTarget == null) {
         this.swellingCreeper.setCreeperState(-1);
      } else if (this.swellingCreeper.e(this.creeperAttackTarget) > 49.0) {
         this.swellingCreeper.setCreeperState(-1);
      } else if (!this.swellingCreeper.aD().canSee(this.creeperAttackTarget)) {
         this.swellingCreeper.setCreeperState(-1);
      } else {
         this.swellingCreeper.setCreeperState(1);
      }
   }
}
