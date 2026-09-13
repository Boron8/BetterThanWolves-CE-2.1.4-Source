package net.minecraft.src;

public class EntityAIMoveTowardsTarget extends EntityAIBase {
   private EntityCreature theEntity;
   private EntityLiving targetEntity;
   private double movePosX;
   private double movePosY;
   private double movePosZ;
   private float field_75425_f;
   private float field_75426_g;

   public EntityAIMoveTowardsTarget(EntityCreature var1, float var2, float var3) {
      this.theEntity = var1;
      this.field_75425_f = var2;
      this.field_75426_g = var3;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      this.targetEntity = this.theEntity.aJ();
      if (this.targetEntity == null) {
         return false;
      } else if (this.targetEntity.e(this.theEntity) > this.field_75426_g * this.field_75426_g) {
         return false;
      } else {
         Vec3 var1 = RandomPositionGenerator.findRandomTargetBlockTowards(
            this.theEntity,
            16,
            7,
            this.theEntity.worldObj.getWorldVec3Pool().getVecFromPool(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ)
         );
         if (var1 == null) {
            return false;
         } else {
            this.movePosX = var1.xCoord;
            this.movePosY = var1.yCoord;
            this.movePosZ = var1.zCoord;
            return true;
         }
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.theEntity.aC().noPath()
         && this.targetEntity.isEntityAlive()
         && this.targetEntity.e(this.theEntity) < this.field_75426_g * this.field_75426_g;
   }

   @Override
   public void resetTask() {
      this.targetEntity = null;
   }

   @Override
   public void startExecuting() {
      this.theEntity.aC().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.field_75425_f);
   }
}
