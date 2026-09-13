package net.minecraft.src;

import java.util.Comparator;

public class RenderSorter implements Comparator {
   private EntityLiving baseEntity;

   public RenderSorter(EntityLiving var1) {
      this.baseEntity = var1;
   }

   public int doCompare(WorldRenderer var1, WorldRenderer var2) {
      if (var1.isInFrustum && !var2.isInFrustum) {
         return 1;
      } else if (var2.isInFrustum && !var1.isInFrustum) {
         return -1;
      } else {
         double var3 = var1.distanceToEntitySquared(this.baseEntity);
         double var5 = var2.distanceToEntitySquared(this.baseEntity);
         if (var3 < var5) {
            return 1;
         } else if (var3 > var5) {
            return -1;
         } else {
            return var1.chunkIndex < var2.chunkIndex ? 1 : -1;
         }
      }
   }
}
