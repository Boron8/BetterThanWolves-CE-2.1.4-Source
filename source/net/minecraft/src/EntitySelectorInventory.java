package net.minecraft.src;

final class EntitySelectorInventory implements IEntitySelector {
   @Override
   public boolean isEntityApplicable(Entity var1) {
      return var1 instanceof IInventory && var1.isEntityAlive();
   }
}
