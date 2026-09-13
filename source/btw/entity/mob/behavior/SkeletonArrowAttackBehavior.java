package btw.entity.mob.behavior;

import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IRangedAttackMob;

public class SkeletonArrowAttackBehavior extends EntityAIBase {
   private final EntityLiving entityOwner;
   private final IRangedAttackMob entityRangedAttackOwner;
   private EntityLiving entityAttackTarget;
   private int attackCooldownCounter;
   private float entityMoveSpeed;
   private int canSeeTargetCounter = 0;
   private int minRangedAttackTime;
   private int attackInterval;
   private double attackRange;
   private double attackRangeSq;

   public SkeletonArrowAttackBehavior(IRangedAttackMob rangedAttackMob, float fMoveSpeed, int iAttackInterval, float fAttackRange) {
      this.entityRangedAttackOwner = rangedAttackMob;
      this.entityOwner = (EntityLiving)rangedAttackMob;
      this.entityMoveSpeed = fMoveSpeed;
      this.attackInterval = iAttackInterval;
      this.attackCooldownCounter = iAttackInterval >> 1;
      this.attackRange = fAttackRange;
      this.attackRangeSq = fAttackRange * fAttackRange;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      EntityLiving target = this.entityOwner.getAttackTarget();
      if (target == null) {
         return false;
      } else {
         this.entityAttackTarget = target;
         return true;
      }
   }

   @Override
   public boolean continueExecuting() {
      return this.shouldExecute() || !this.entityOwner.getNavigator().noPath();
   }

   @Override
   public void resetTask() {
      this.entityAttackTarget = null;
      this.canSeeTargetCounter = 0;
      this.attackCooldownCounter = this.attackInterval;
   }

   @Override
   public void updateTask() {
      double dDistSqToTarget = this.entityOwner.e(this.entityAttackTarget.posX, this.entityAttackTarget.boundingBox.minY, this.entityAttackTarget.posZ);
      boolean bCanSeeTarget = this.entityOwner.getEntitySenses().canSee(this.entityAttackTarget);
      if (bCanSeeTarget) {
         this.canSeeTargetCounter++;
      } else {
         this.canSeeTargetCounter = 0;
      }

      if (dDistSqToTarget <= this.attackRangeSq && this.canSeeTargetCounter >= 20) {
         this.entityOwner.getNavigator().clearPathEntity();
      } else {
         this.entityOwner.getNavigator().tryMoveToEntityLiving(this.entityAttackTarget, this.entityMoveSpeed);
      }

      this.entityOwner.getLookHelper().setLookPositionWithEntity(this.entityAttackTarget, 30.0F, 30.0F);
      if (this.attackCooldownCounter > 1) {
         this.attackCooldownCounter--;
      } else if (dDistSqToTarget <= this.attackRangeSq && bCanSeeTarget) {
         this.entityRangedAttackOwner.attackEntityWithRangedAttack(this.entityAttackTarget, 1.0F);
         this.attackCooldownCounter = this.attackInterval;
      }
   }
}
