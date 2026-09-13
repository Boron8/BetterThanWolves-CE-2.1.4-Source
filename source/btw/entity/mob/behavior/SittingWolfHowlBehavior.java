package btw.entity.mob.behavior;

import btw.entity.mob.WolfEntity;

public class SittingWolfHowlBehavior extends WolfHowlBehavior {
   public SittingWolfHowlBehavior(WolfEntity wolf) {
      super(wolf);
      this.a(2);
   }

   @Override
   public boolean shouldExecute() {
      if (this.associatedWolf.n() && !this.associatedWolf.h_()) {
         int iTimeOfDay = (int)(this.world.worldInfo.getWorldTime() % 24000L);
         if (iTimeOfDay > 13500 && iTimeOfDay < 22500 && this.associatedWolf.heardHowlCountdown > 0 && this.associatedWolf.heardHowlCountdown <= 80) {
            this.howlingGroupInitiator = false;
            return this.associatedWolf.aE().nextInt(240) == 0;
         }
      }

      return false;
   }
}
