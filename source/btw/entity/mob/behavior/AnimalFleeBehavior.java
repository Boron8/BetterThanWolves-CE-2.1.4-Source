package btw.entity.mob.behavior;

import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.RandomPositionGenerator;
import net.minecraft.src.Vec3;

public class AnimalFleeBehavior extends EntityAIBase {
   private EntityAnimal theAnimal;
   private float speed;
   private double targetPosX;
   private double targetPosY;
   private double targetPosZ;

   public AnimalFleeBehavior(EntityAnimal animal, float fSpeed) {
      this.theAnimal = animal;
      this.speed = fSpeed;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      Vec3 targetVec = null;
      if (this.theAnimal.ae()) {
         targetVec = RandomPositionGenerator.findRandomTarget(this.theAnimal, 5, 4);
      } else if (this.theAnimal.aF() != null) {
         targetVec = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
            this.theAnimal,
            5,
            4,
            this.theAnimal.worldObj.getWorldVec3Pool().getVecFromPool(this.theAnimal.aF().posX, this.theAnimal.aF().posY, this.theAnimal.aF().posZ)
         );
      }

      if (targetVec != null) {
         this.targetPosX = targetVec.xCoord;
         this.targetPosY = targetVec.yCoord;
         this.targetPosZ = targetVec.zCoord;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void startExecuting() {
      this.theAnimal.aC().tryMoveToXYZ(this.targetPosX, this.targetPosY, this.targetPosZ, this.speed);
   }

   @Override
   public boolean continueExecuting() {
      if (!this.theAnimal.aC().noPath() && this.theAnimal.aF() != null) {
         EntityLiving aiTarget = this.theAnimal.aF();
         if (aiTarget == null) {
            return true;
         }

         double dDistanceSqToTarget = this.theAnimal.e(this.targetPosX, this.targetPosY, this.targetPosZ);
         if (dDistanceSqToTarget > 4.0) {
            double dDistanceSqToAttacker = this.theAnimal.e(aiTarget);
            double dDistanceSqAttackerToTarget = aiTarget.e(this.targetPosX, this.targetPosY, this.targetPosZ);
            if (dDistanceSqToAttacker < dDistanceSqAttackerToTarget) {
               return true;
            }
         }
      }

      return false;
   }
}
