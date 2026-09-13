package btw.entity.util;

import net.minecraft.src.Entity;

public class ClosestEntitySelectionCriteriaSquidSecondaryTarget extends ClosestEntitySelectionCriteria {
   @Override
   public void processEntity(ClosestEntityInfo closestEntityInfo, Entity entity) {
      if (entity.isSecondaryTargetForSquid() && entity.isEntityAlive()) {
         double dDeltaX = entity.posX - closestEntityInfo.sourcePosX;
         double dDeltaY = entity.posY - closestEntityInfo.sourcePosY;
         double dDeltaZ = entity.posZ - closestEntityInfo.sourcePosZ;
         double dDistSq = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
         if (dDistSq < closestEntityInfo.closestDistanceSq
            && (!entity.worldObj.isDaytime() || entity.getBrightness(1.0F) < 0.1F)
            && (entity.inWater || this.canEntityBeSeenBySource(closestEntityInfo, entity))) {
            closestEntityInfo.closestEntity = entity;
            closestEntityInfo.closestDistanceSq = dDistSq;
         }
      }
   }

   private boolean canEntityBeSeenBySource(ClosestEntityInfo closestEntityInfo, Entity entity) {
      return entity.worldObj
            .rayTraceBlocks_do_do(
               entity.worldObj.getWorldVec3Pool().getVecFromPool(closestEntityInfo.sourcePosX, closestEntityInfo.sourcePosY, closestEntityInfo.sourcePosZ),
               entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY + entity.height / 2.0F, entity.posZ),
               false,
               true
            )
         == null;
   }
}
