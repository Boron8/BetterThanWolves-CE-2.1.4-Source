package net.minecraft.src;

public class EntityAISwimming extends EntityAIBase {
   private EntityLiving theEntity;

   public EntityAISwimming(EntityLiving var1) {
      this.theEntity = var1;
      this.a(4);
      var1.getNavigator().setCanSwim(true);
   }

   @Override
   public boolean shouldExecute() {
      return this.theEntity.G() || this.theEntity.I();
   }

   @Override
   public void updateTask() {
      if (this.theEntity.getRNG().nextFloat() < 0.8F) {
         this.theEntity.getJumpHelper().setJumping();
      }
   }
}
