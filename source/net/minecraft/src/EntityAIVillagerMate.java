package net.minecraft.src;

public class EntityAIVillagerMate extends EntityAIBase {
   private EntityVillager villagerObj;
   private EntityVillager mate;
   private World worldObj;
   private int matingTimeout = 0;
   Village villageObj;

   public EntityAIVillagerMate(EntityVillager var1) {
      this.villagerObj = var1;
      this.worldObj = var1.worldObj;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.villagerObj.b() != 0) {
         return false;
      } else if (this.villagerObj.aE().nextInt(500) != 0) {
         return false;
      } else {
         this.villageObj = this.worldObj
            .villageCollectionObj
            .findNearestVillage(
               MathHelper.floor_double(this.villagerObj.posX),
               MathHelper.floor_double(this.villagerObj.posY),
               MathHelper.floor_double(this.villagerObj.posZ),
               0
            );
         if (this.villageObj == null) {
            return false;
         } else if (!this.checkSufficientDoorsPresentForNewVillager()) {
            return false;
         } else {
            Entity var1 = this.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.villagerObj.boundingBox.expand(8.0, 3.0, 8.0), this.villagerObj);
            if (var1 == null) {
               return false;
            } else {
               this.mate = (EntityVillager)var1;
               return this.mate.b() == 0;
            }
         }
      }
   }

   @Override
   public void startExecuting() {
      this.matingTimeout = 300;
      this.villagerObj.setMating(true);
   }

   @Override
   public void resetTask() {
      this.villageObj = null;
      this.mate = null;
      this.villagerObj.setMating(false);
   }

   @Override
   public boolean continueExecuting() {
      return this.matingTimeout >= 0 && this.checkSufficientDoorsPresentForNewVillager() && this.villagerObj.b() == 0;
   }

   @Override
   public void updateTask() {
      this.matingTimeout--;
      this.villagerObj.az().setLookPositionWithEntity(this.mate, 10.0F, 30.0F);
      if (this.villagerObj.e(this.mate) > 2.25) {
         this.villagerObj.aC().tryMoveToEntityLiving(this.mate, 0.25F);
      } else if (this.matingTimeout == 0 && this.mate.isMating()) {
         this.giveBirth();
      }

      if (this.villagerObj.aE().nextInt(35) == 0) {
         this.worldObj.setEntityState(this.villagerObj, (byte)12);
      }
   }

   private boolean checkSufficientDoorsPresentForNewVillager() {
      if (!this.villageObj.isMatingSeason()) {
         return false;
      } else {
         int var1 = (int)(this.villageObj.getNumVillageDoors() * 0.35);
         return this.villageObj.getNumVillagers() < var1;
      }
   }

   private void giveBirth() {
      EntityVillager var1 = this.villagerObj.func_90012_b(this.mate);
      this.mate.a(6000);
      this.villagerObj.a(6000);
      var1.a(-24000);
      var1.b(this.villagerObj.posX, this.villagerObj.posY, this.villagerObj.posZ, 0.0F, 0.0F);
      this.worldObj.spawnEntityInWorld(var1);
      this.worldObj.setEntityState(var1, (byte)12);
   }
}
