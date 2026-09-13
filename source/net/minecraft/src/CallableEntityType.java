package net.minecraft.src;

import java.util.concurrent.Callable;

class CallableEntityType implements Callable {
   CallableEntityType(Entity var1) {
      this.theEntity = var1;
   }

   public String callEntityType() {
      return EntityList.getEntityString(this.theEntity) + " (" + this.theEntity.getClass().getCanonicalName() + ")";
   }
}
