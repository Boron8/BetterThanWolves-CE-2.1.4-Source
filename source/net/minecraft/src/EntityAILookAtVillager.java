package net.minecraft.src;

public class EntityAILookAtVillager extends EntityAIBase {
   private EntityIronGolem theGolem;
   private EntityVillager theVillager;
   private int lookTime;

   public EntityAILookAtVillager(EntityIronGolem var1) {
      this.theGolem = var1;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.theGolem.worldObj.isDaytime()) {
         return false;
      } else if (this.theGolem.aE().nextInt(8000) != 0) {
         return false;
      } else {
         this.theVillager = (EntityVillager)this.theGolem
            .worldObj
            .findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.boundingBox.expand(6.0, 2.0, 6.0), this.theGolem);
         return this.theVillager != null;
      }
   }

   @Override
   public boolean continueExecuting() {
      return this.lookTime > 0;
   }

   @Override
   public void startExecuting() {
      this.lookTime = 400;
      this.theGolem.setHoldingRose(true);
   }

   @Override
   public void resetTask() {
      this.theGolem.setHoldingRose(false);
      this.theVillager = null;
   }

   @Override
   public void updateTask() {
      this.theGolem.az().setLookPositionWithEntity(this.theVillager, 30.0F, 30.0F);
      this.lookTime--;
   }
}
