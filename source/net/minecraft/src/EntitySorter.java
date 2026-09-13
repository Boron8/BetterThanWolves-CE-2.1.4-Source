package net.minecraft.src;

import java.util.Comparator;

public class EntitySorter implements Comparator {
   private double entityPosX;
   private double entityPosY;
   private double entityPosZ;

   public EntitySorter(Entity var1) {
      this.entityPosX = -var1.posX;
      this.entityPosY = -var1.posY;
      this.entityPosZ = -var1.posZ;
   }

   public int sortByDistanceToEntity(WorldRenderer var1, WorldRenderer var2) {
      double var3 = var1.posXPlus + this.entityPosX;
      double var5 = var1.posYPlus + this.entityPosY;
      double var7 = var1.posZPlus + this.entityPosZ;
      double var9 = var2.posXPlus + this.entityPosX;
      double var11 = var2.posYPlus + this.entityPosY;
      double var13 = var2.posZPlus + this.entityPosZ;
      return (int)((var3 * var3 + var5 * var5 + var7 * var7 - (var9 * var9 + var11 * var11 + var13 * var13)) * 1024.0);
   }
}
