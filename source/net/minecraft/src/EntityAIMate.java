package net.minecraft.src;

import java.util.List;

public class EntityAIMate extends EntityAIBase {
   protected EntityAnimal theAnimal;
   World theWorld;
   protected EntityAnimal targetMate;
   int spawnBabyDelay = 0;
   float moveSpeed;

   public EntityAIMate(EntityAnimal par1EntityAnimal, float par2) {
      this.theAnimal = par1EntityAnimal;
      this.theWorld = par1EntityAnimal.worldObj;
      this.moveSpeed = par2;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.theAnimal.isInLove()) {
         return false;
      } else {
         this.targetMate = this.getNearbyMate();
         return this.targetMate != null;
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.theAnimal.isInLove() ? false : this.targetMate.R() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
   }

   @Override
   public void resetTask() {
      this.targetMate = null;
      this.spawnBabyDelay = 0;
   }

   @Override
   public void updateTask() {
      this.theAnimal.az().setLookPositionWithEntity(this.targetMate, 10.0F, this.theAnimal.bs());
      this.theAnimal.aC().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
      this.spawnBabyDelay++;
      if (this.spawnBabyDelay >= 60 && this.theAnimal.e(this.targetMate) < 9.0) {
         this.theAnimal.procreate(this.targetMate);
      }
   }

   private EntityAnimal getNearbyMate() {
      float var1 = 8.0F;
      List var2 = this.theWorld.getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.boundingBox.expand(var1, var1, var1));
      double var3 = Double.MAX_VALUE;
      EntityAnimal var5 = null;

      for (EntityAnimal var7 : var2) {
         if (this.theAnimal.canMateWith(var7) && this.theAnimal.e(var7) < var3) {
            var5 = var7;
            var3 = this.theAnimal.e(var7);
         }
      }

      return var5;
   }
}
