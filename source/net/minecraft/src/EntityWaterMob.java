package net.minecraft.src;

public abstract class EntityWaterMob extends EntityCreature implements IAnimals {
   public EntityWaterMob(World par1World) {
      super(par1World);
   }

   @Override
   public boolean canBreatheUnderwater() {
      return true;
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.worldObj.checkNoEntityCollision(this.boundingBox);
   }

   @Override
   public int getTalkInterval() {
      return 120;
   }

   @Override
   protected boolean canDespawn() {
      return true;
   }

   @Override
   protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
      return 1 + this.worldObj.rand.nextInt(3);
   }

   @Override
   public void onEntityUpdate() {
      int var1 = this.ak();
      super.x();
      if (this.R() && !this.inWater) {
         this.g(--var1);
         if (this.ak() == -20) {
            this.g(0);
            this.a(DamageSource.drown, 2);
         }
      } else {
         this.g(300);
      }
   }
}
