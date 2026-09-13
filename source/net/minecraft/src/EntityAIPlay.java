package net.minecraft.src;

import java.util.List;

public class EntityAIPlay extends EntityAIBase {
   private EntityVillager villagerObj;
   private EntityLiving targetVillager;
   private float field_75261_c;
   private int playTime;

   public EntityAIPlay(EntityVillager var1, float var2) {
      this.villagerObj = var1;
      this.field_75261_c = var2;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.villagerObj.b() >= 0) {
         return false;
      } else if (this.villagerObj.aE().nextInt(400) != 0) {
         return false;
      } else {
         List var1 = this.villagerObj.worldObj.getEntitiesWithinAABB(EntityVillager.class, this.villagerObj.boundingBox.expand(6.0, 3.0, 6.0));
         double var2 = Double.MAX_VALUE;

         for (EntityVillager var5 : var1) {
            if (var5 != this.villagerObj && !var5.isPlaying() && var5.b() < 0) {
               double var6 = var5.e(this.villagerObj);
               if (!(var6 > var2)) {
                  var2 = var6;
                  this.targetVillager = var5;
               }
            }
         }

         if (this.targetVillager == null) {
            Vec3 var8 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
            if (var8 == null) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public boolean continueExecuting() {
      return this.playTime > 0;
   }

   @Override
   public void startExecuting() {
      if (this.targetVillager != null) {
         this.villagerObj.setPlaying(true);
      }

      this.playTime = 1000;
   }

   @Override
   public void resetTask() {
      this.villagerObj.setPlaying(false);
      this.targetVillager = null;
   }

   @Override
   public void updateTask() {
      this.playTime--;
      if (this.targetVillager != null) {
         if (this.villagerObj.e(this.targetVillager) > 4.0) {
            this.villagerObj.aC().tryMoveToEntityLiving(this.targetVillager, this.field_75261_c);
         }
      } else if (this.villagerObj.aC().noPath()) {
         Vec3 var1 = RandomPositionGenerator.findRandomTarget(this.villagerObj, 16, 3);
         if (var1 == null) {
            return;
         }

         this.villagerObj.aC().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, this.field_75261_c);
      }
   }
}
