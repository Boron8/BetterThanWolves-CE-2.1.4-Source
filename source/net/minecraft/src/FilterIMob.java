package net.minecraft.src;

final class FilterIMob implements IEntitySelector {
   @Override
   public boolean isEntityApplicable(Entity var1) {
      return var1 instanceof IMob;
   }
}
