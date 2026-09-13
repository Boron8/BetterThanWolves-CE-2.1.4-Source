package btw.entity.mob.behavior;

import btw.entity.mob.WolfEntity;
import net.minecraft.src.EntityAINearestAttackableTarget;

public class WildWolfTargetIfHungryBehavior extends EntityAINearestAttackableTarget {
   private WolfEntity associatedWolf;

   public WildWolfTargetIfHungryBehavior(WolfEntity wolf, Class targetClass, float fTargetRange, int iChanceOfTargeting, boolean bCheckLineOfSight) {
      super(wolf, targetClass, fTargetRange, iChanceOfTargeting, bCheckLineOfSight);
      this.associatedWolf = wolf;
   }

   @Override
   public boolean continueExecuting() {
      return !this.associatedWolf.isWildAndHungry() ? false : super.b();
   }

   @Override
   public boolean shouldExecute() {
      return !this.associatedWolf.isWildAndHungry() ? false : super.shouldExecute();
   }
}
