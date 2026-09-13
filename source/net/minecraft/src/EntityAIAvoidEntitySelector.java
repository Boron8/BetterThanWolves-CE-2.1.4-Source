package net.minecraft.src;

class EntityAIAvoidEntitySelector implements IEntitySelector {
   EntityAIAvoidEntitySelector(EntityAIAvoidEntity var1) {
      this.entityAvoiderAI = var1;
   }

   @Override
   public boolean isEntityApplicable(Entity var1) {
      return var1.isEntityAlive() && EntityAIAvoidEntity.func_98217_a(this.entityAvoiderAI).aD().canSee(var1);
   }
}
