package net.minecraft.src;

public class EntityAIMoveIndoors extends EntityAIBase {
   private EntityCreature entityObj;
   private VillageDoorInfo doorInfo;
   private int insidePosX = -1;
   private int insidePosZ = -1;

   public EntityAIMoveIndoors(EntityCreature var1) {
      this.entityObj = var1;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if ((!this.entityObj.worldObj.isDaytime() || this.entityObj.worldObj.isRaining()) && !this.entityObj.worldObj.provider.hasNoSky) {
         if (this.entityObj.aE().nextInt(50) != 0) {
            return false;
         } else if (this.insidePosX != -1 && this.entityObj.e(this.insidePosX, this.entityObj.posY, this.insidePosZ) < 4.0) {
            return false;
         } else {
            Village var1 = this.entityObj
               .worldObj
               .villageCollectionObj
               .findNearestVillage(
                  MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ), 14
               );
            if (var1 == null) {
               return false;
            } else {
               this.doorInfo = var1.findNearestDoorUnrestricted(
                  MathHelper.floor_double(this.entityObj.posX), MathHelper.floor_double(this.entityObj.posY), MathHelper.floor_double(this.entityObj.posZ)
               );
               return this.doorInfo != null;
            }
         }
      } else {
         return false;
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.entityObj.aC().noPath();
   }

   @Override
   public void startExecuting() {
      this.insidePosX = -1;
      if (this.entityObj.e(this.doorInfo.getInsidePosX(), this.doorInfo.posY, this.doorInfo.getInsidePosZ()) > 256.0) {
         Vec3 var1 = RandomPositionGenerator.findRandomTargetBlockTowards(
            this.entityObj,
            14,
            3,
            this.entityObj
               .worldObj
               .getWorldVec3Pool()
               .getVecFromPool(this.doorInfo.getInsidePosX() + 0.5, this.doorInfo.getInsidePosY(), this.doorInfo.getInsidePosZ() + 0.5)
         );
         if (var1 != null) {
            this.entityObj.aC().tryMoveToXYZ(var1.xCoord, var1.yCoord, var1.zCoord, 0.3F);
         }
      } else {
         this.entityObj
            .aC()
            .tryMoveToXYZ(this.doorInfo.getInsidePosX() + 0.5, (double)this.doorInfo.getInsidePosY(), this.doorInfo.getInsidePosZ() + 0.5, 0.3F);
      }
   }

   @Override
   public void resetTask() {
      this.insidePosX = this.doorInfo.getInsidePosX();
      this.insidePosZ = this.doorInfo.getInsidePosZ();
      this.doorInfo = null;
   }
}
