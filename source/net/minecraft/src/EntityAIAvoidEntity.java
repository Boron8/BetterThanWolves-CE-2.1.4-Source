package net.minecraft.src;

import java.util.List;

public class EntityAIAvoidEntity extends EntityAIBase {
   public final IEntitySelector field_98218_a = new EntityAIAvoidEntitySelector(this);
   private EntityCreature theEntity;
   private float farSpeed;
   private float nearSpeed;
   private Entity closestLivingEntity;
   private float distanceFromEntity;
   private PathEntity entityPathEntity;
   private PathNavigate entityPathNavigate;
   private Class targetEntityClass;

   public EntityAIAvoidEntity(EntityCreature var1, Class var2, float var3, float var4, float var5) {
      this.theEntity = var1;
      this.targetEntityClass = var2;
      this.distanceFromEntity = var3;
      this.farSpeed = var4;
      this.nearSpeed = var5;
      this.entityPathNavigate = var1.aC();
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.targetEntityClass == EntityPlayer.class) {
         if (this.theEntity instanceof EntityTameable && ((EntityTameable)this.theEntity).isTamed()) {
            return false;
         }

         this.closestLivingEntity = this.theEntity.worldObj.getClosestPlayerToEntity(this.theEntity, this.distanceFromEntity);
         if (this.closestLivingEntity == null) {
            return false;
         }
      } else {
         List var1 = this.theEntity
            .worldObj
            .selectEntitiesWithinAABB(
               this.targetEntityClass, this.theEntity.boundingBox.expand(this.distanceFromEntity, 3.0, this.distanceFromEntity), this.field_98218_a
            );
         if (var1.isEmpty()) {
            return false;
         }

         this.closestLivingEntity = (Entity)var1.get(0);
      }

      Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(
         this.theEntity,
         16,
         7,
         this.theEntity.worldObj.getWorldVec3Pool().getVecFromPool(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ)
      );
      if (var2 == null) {
         return false;
      } else if (this.closestLivingEntity.getDistanceSq(var2.xCoord, var2.yCoord, var2.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)
         )
       {
         return false;
      } else {
         this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(var2.xCoord, var2.yCoord, var2.zCoord);
         return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(var2);
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.entityPathNavigate.noPath();
   }

   @Override
   public void startExecuting() {
      this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
   }

   @Override
   public void resetTask() {
      this.closestLivingEntity = null;
   }

   @Override
   public void updateTask() {
      if (this.theEntity.e(this.closestLivingEntity) < 49.0) {
         this.theEntity.aC().setSpeed(this.nearSpeed);
      } else {
         this.theEntity.aC().setSpeed(this.farSpeed);
      }
   }
}
