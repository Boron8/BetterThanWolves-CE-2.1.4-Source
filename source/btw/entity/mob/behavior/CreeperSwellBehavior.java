package btw.entity.mob.behavior;

import btw.entity.mob.CreeperEntity;
import net.minecraft.src.EntityAICreeperSwell;

public class CreeperSwellBehavior extends EntityAICreeperSwell {
   private CreeperEntity myCreeper;

   public CreeperSwellBehavior(CreeperEntity creeper) {
      super(creeper);
      this.myCreeper = creeper;
   }

   @Override
   public boolean shouldExecute() {
      if (this.myCreeper.o() <= 0 && this.myCreeper.getNeuteredState() > 0) {
         return false;
      } else {
         return this.myCreeper.getIsDeterminedToExplode() ? true : super.shouldExecute();
      }
   }

   @Override
   public void updateTask() {
      if (this.myCreeper.getNeuteredState() > 0) {
         this.myCreeper.a(-1);
      } else if (this.myCreeper.getIsDeterminedToExplode()
         || this.creeperAttackTarget != null && !(this.myCreeper.e(this.creeperAttackTarget) > 36.0) && this.myCreeper.aD().canSee(this.creeperAttackTarget)) {
         this.myCreeper.a(1);
      } else {
         this.myCreeper.a(-1);
      }
   }
}
