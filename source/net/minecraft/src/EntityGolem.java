package net.minecraft.src;

public abstract class EntityGolem extends EntityCreature implements IAnimals {
   public EntityGolem(World var1) {
      super(var1);
   }

   @Override
   protected void fall(float var1) {
   }

   @Override
   protected String getLivingSound() {
      return "none";
   }

   @Override
   protected String getHurtSound() {
      return "none";
   }

   @Override
   protected String getDeathSound() {
      return "none";
   }

   @Override
   public int getTalkInterval() {
      return 120;
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }
}
