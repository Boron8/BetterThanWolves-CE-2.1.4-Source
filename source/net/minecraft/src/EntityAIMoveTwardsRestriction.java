package net.minecraft.src;

public class EntityAIMoveTwardsRestriction extends EntityAIBase {
   private EntityCreature theEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private float movementSpeed;

   public EntityAIMoveTwardsRestriction(EntityCreature var1, float var2) {
      this.theEntity = var1;
      this.movementSpeed = var2;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.theEntity.aL()) {
         return false;
      } else {
         ChunkCoordinates var1 = this.theEntity.aM();
         Vec3 var2 = RandomPositionGenerator.findRandomTargetBlockTowards(
            this.theEntity, 16, 7, this.theEntity.worldObj.getWorldVec3Pool().getVecFromPool(var1.posX, var1.posY, var1.posZ)
         );
         if (var2 == null) {
            return false;
         } else {
            this.movePosX = var2.xCoord;
            this.movePosY = var2.yCoord;
            this.movePosZ = var2.zCoord;
            return true;
         }
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.theEntity.aC().noPath();
   }

   @Override
   public void startExecuting() {
      this.theEntity.aC().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
   }
}
