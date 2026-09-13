package net.minecraft.src;

final class EntitySelectorAlive implements IEntitySelector {
   @Override
   public boolean isEntityApplicable(Entity var1) {
      return var1.isEntityAlive();
   }
}
