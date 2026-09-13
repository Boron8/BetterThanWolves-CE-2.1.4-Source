package btw.entity.mob.behavior;

import btw.entity.mob.WolfEntity;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class WolfHowlBehavior extends EntityAIBase {
   public WolfEntity associatedWolf;
   public World world;
   public int howlCounter;
   public boolean howlingGroupInitiator;
   public static final int CHANCE_OF_HOWLING = 4800;
   public static final int CHANCE_OF_HOWLING_DURING_FULL_MOON = 240;
   public static final int CHANCE_OF_HOWLING_WHEN_OTHERS_HOWL = 240;
   public static final int HOWL_DURATION = 80;
   public static final int HEARD_HOWL_DURATION = 95;
   public static final double HEAR_HOWL_DISTANCE = 320.0;
   public static final double HEAR_HOWL_DISTANCE_SQ = 102400.0;

   public WolfHowlBehavior(WolfEntity wolf) {
      this.associatedWolf = wolf;
      this.world = wolf.worldObj;
      this.a(7);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.associatedWolf.h_()) {
         int iTimeOfDay = (int)(this.world.worldInfo.getWorldTime() % 24000L);
         if (iTimeOfDay > 13500 && iTimeOfDay < 22500) {
            int iMoonPhase = this.world.getMoonPhase();
            if (iMoonPhase == 0 && this.world.worldInfo.getWorldTime() > 24000L) {
               if (!this.associatedWolf.m()) {
                  this.howlingGroupInitiator = false;
                  return this.associatedWolf.aE().nextInt(240) == 0;
               }
            } else {
               if (this.associatedWolf.heardHowlCountdown > 0 && this.associatedWolf.heardHowlCountdown <= 80) {
                  this.howlingGroupInitiator = false;
                  return this.associatedWolf.aE().nextInt(240) == 0;
               }

               if (!this.associatedWolf.m()) {
                  this.howlingGroupInitiator = true;
                  return this.associatedWolf.aE().nextInt(4800) == 0;
               }
            }
         }
      }

      return false;
   }

   @Override
   public boolean continueExecuting() {
      return this.howlCounter < 80;
   }

   @Override
   public void startExecuting() {
      this.howlCounter = 0;
      this.world.setEntityState(this.associatedWolf, (byte)10);
      this.associatedWolf.aC().clearPathEntity();
      if (this.howlingGroupInitiator) {
         this.notifyOtherWolvesInAreaOfHowl();
         this.howlingGroupInitiator = false;
      }

      this.associatedWolf.heardHowlCountdown = 0;
      int iTargetI = MathHelper.floor_double(this.associatedWolf.posX);
      int iTargetJ = MathHelper.floor_double(this.associatedWolf.posY) + 1;
      int iTargetK = MathHelper.floor_double(this.associatedWolf.posZ);
      this.world.func_82739_e(2256, iTargetI, iTargetJ, iTargetK, 0);
   }

   @Override
   public void resetTask() {
      this.howlCounter = 0;
   }

   @Override
   public void updateTask() {
      this.howlCounter++;
   }

   private void notifyOtherWolvesInAreaOfHowl() {
      for (int l = 0; l < this.world.loadedEntityList.size(); l++) {
         Entity tempEntity = (Entity)this.world.loadedEntityList.get(l);
         tempEntity.notifyOfWolfHowl(this.associatedWolf);
      }
   }
}
