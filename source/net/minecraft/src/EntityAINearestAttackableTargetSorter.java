package net.minecraft.src;

import java.util.Comparator;

public class EntityAINearestAttackableTargetSorter implements Comparator {
   private Entity theEntity;

   public EntityAINearestAttackableTargetSorter(EntityAINearestAttackableTarget var1, Entity var2) {
      this.parent = var1;
      this.theEntity = var2;
   }

   public int compareDistanceSq(Entity var1, Entity var2) {
      double var3 = this.theEntity.getDistanceSqToEntity(var1);
      double var5 = this.theEntity.getDistanceSqToEntity(var2);
      if (var3 < var5) {
         return -1;
      } else {
         return var3 > var5 ? 1 : 0;
      }
   }
}
