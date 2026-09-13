package btw.entity.mob.behavior;

import btw.entity.mob.DireWolfEntity;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class DireWolfHowlBehavior extends EntityAIBase {
   protected DireWolfEntity associatedWolf;
   protected World world;
   protected int howlCounter;
   protected static final int CHANCE_OF_HOWLING = 2400;
   protected static final int HOWL_DURATION = 80;

   public DireWolfHowlBehavior(DireWolfEntity wolf) {
      this.associatedWolf = wolf;
      this.world = wolf.worldObj;
      this.a(7);
   }

   @Override
   public boolean shouldExecute() {
      int iTimeOfDay = (int)(this.world.worldInfo.getWorldTime() % 24000L);
      return iTimeOfDay > 13500 && iTimeOfDay < 22500 ? this.associatedWolf.aE().nextInt(2400) == 0 : false;
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
      this.notifyOtherWolvesInAreaOfHowl();
      this.associatedWolf.heardHowlCountdown = 0;
      int iTargetI = MathHelper.floor_double(this.associatedWolf.posX);
      int iTargetJ = MathHelper.floor_double(this.associatedWolf.posY) + 1;
      int iTargetK = MathHelper.floor_double(this.associatedWolf.posZ);
      this.world.func_82739_e(2256, iTargetI, iTargetJ, iTargetK, 1);
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
