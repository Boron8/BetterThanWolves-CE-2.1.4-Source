package net.minecraft.src;

final class AABBLocalPool extends ThreadLocal {
   protected AABBPool createNewDefaultPool() {
      return new AABBPool(300, 2000);
   }
}
