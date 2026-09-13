package net.minecraft.src;

final class DispenserBehaviorExperience extends BehaviorProjectileDispense {
   @Override
   protected IProjectile getProjectileEntity(World par1World, IPosition par2IPosition) {
      return (IProjectile)EntityList.createEntityOfType(EntityExpBottle.class, par1World, par2IPosition.getX(), par2IPosition.getY(), par2IPosition.getZ());
   }

   @Override
   protected float func_82498_a() {
      return super.func_82498_a() * 0.5F;
   }

   @Override
   protected float func_82500_b() {
      return super.func_82500_b() * 1.25F;
   }
}
