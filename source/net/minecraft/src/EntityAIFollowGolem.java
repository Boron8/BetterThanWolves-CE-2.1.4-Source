package net.minecraft.src;

import java.util.List;

public class EntityAIFollowGolem extends EntityAIBase {
   private EntityVillager theVillager;
   private EntityIronGolem theGolem;
   private int takeGolemRoseTick;
   private boolean tookGolemRose = false;

   public EntityAIFollowGolem(EntityVillager var1) {
      this.theVillager = var1;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.theVillager.b() >= 0) {
         return false;
      } else if (!this.theVillager.worldObj.isDaytime()) {
         return false;
      } else {
         List var1 = this.theVillager.worldObj.getEntitiesWithinAABB(EntityIronGolem.class, this.theVillager.boundingBox.expand(6.0, 2.0, 6.0));
         if (var1.isEmpty()) {
            return false;
         } else {
            for (EntityIronGolem var3 : var1) {
               if (var3.getHoldRoseTick() > 0) {
                  this.theGolem = var3;
                  break;
               }
            }

            return this.theGolem != null;
         }
      }
   }

   @Override
   public boolean continueExecuting() {
      return this.theGolem.getHoldRoseTick() > 0;
   }

   @Override
   public void startExecuting() {
      this.takeGolemRoseTick = this.theVillager.aE().nextInt(320);
      this.tookGolemRose = false;
      this.theGolem.aC().clearPathEntity();
   }

   @Override
   public void resetTask() {
      this.theGolem = null;
      this.theVillager.aC().clearPathEntity();
   }

   @Override
   public void updateTask() {
      this.theVillager.az().setLookPositionWithEntity(this.theGolem, 30.0F, 30.0F);
      if (this.theGolem.getHoldRoseTick() == this.takeGolemRoseTick) {
         this.theVillager.aC().tryMoveToEntityLiving(this.theGolem, 0.15F);
         this.tookGolemRose = true;
      }

      if (this.tookGolemRose && this.theVillager.e(this.theGolem) < 4.0) {
         this.theGolem.setHoldingRose(false);
         this.theVillager.aC().clearPathEntity();
      }
   }
}
