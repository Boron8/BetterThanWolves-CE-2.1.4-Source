package net.minecraft.src;

public class EntityAIRestrictSun extends EntityAIBase {
   private EntityCreature theEntity;

   public EntityAIRestrictSun(EntityCreature var1) {
      this.theEntity = var1;
   }

   @Override
   public boolean shouldExecute() {
      return this.theEntity.worldObj.isDaytime();
   }

   @Override
   public void startExecuting() {
      this.theEntity.aC().setAvoidSun(true);
   }

   @Override
   public void resetTask() {
      this.theEntity.aC().setAvoidSun(false);
   }
}
