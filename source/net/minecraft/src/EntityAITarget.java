package net.minecraft.src;

public abstract class EntityAITarget extends EntityAIBase {
   protected EntityLiving taskOwner;
   protected float targetDistance;
   protected boolean shouldCheckSight;
   private boolean field_75303_a;
   private int field_75301_b = 0;
   private int field_75302_c = 0;
   private int field_75298_g = 0;

   public EntityAITarget(EntityLiving var1, float var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   public EntityAITarget(EntityLiving var1, float var2, boolean var3, boolean var4) {
      this.taskOwner = var1;
      this.targetDistance = var2;
      this.shouldCheckSight = var3;
      this.field_75303_a = var4;
   }

   @Override
   public boolean continueExecuting() {
      EntityLiving var1 = this.taskOwner.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else if (this.taskOwner.e(var1) > this.targetDistance * this.targetDistance) {
         return false;
      } else {
         if (this.shouldCheckSight) {
            if (this.taskOwner.getEntitySenses().canSee(var1)) {
               this.field_75298_g = 0;
            } else if (++this.field_75298_g > 60) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public void startExecuting() {
      this.field_75301_b = 0;
      this.field_75302_c = 0;
      this.field_75298_g = 0;
   }

   @Override
   public void resetTask() {
      this.taskOwner.setAttackTarget(null);
   }

   protected boolean isSuitableTarget(EntityLiving var1, boolean var2) {
      if (var1 == null) {
         return false;
      } else if (var1 == this.taskOwner) {
         return false;
      } else if (!var1.isEntityAlive()) {
         return false;
      } else if (!this.taskOwner.canAttackClass(var1.getClass())) {
         return false;
      } else {
         if (this.taskOwner instanceof EntityTameable && ((EntityTameable)this.taskOwner).isTamed()) {
            if (var1 instanceof EntityTameable && ((EntityTameable)var1).isTamed()) {
               return false;
            }

            if (var1 == ((EntityTameable)this.taskOwner).getOwner()) {
               return false;
            }
         } else if (var1 instanceof EntityPlayer && !var2 && ((EntityPlayer)var1).capabilities.disableDamage) {
            return false;
         }

         if (!this.taskOwner.isWithinHomeDistance(MathHelper.floor_double(var1.posX), MathHelper.floor_double(var1.posY), MathHelper.floor_double(var1.posZ))) {
            return false;
         } else if (this.shouldCheckSight && !this.taskOwner.getEntitySenses().canSee(var1)) {
            return false;
         } else {
            if (this.field_75303_a) {
               if (--this.field_75302_c <= 0) {
                  this.field_75301_b = 0;
               }

               if (this.field_75301_b == 0) {
                  this.field_75301_b = this.func_75295_a(var1) ? 1 : 2;
               }

               if (this.field_75301_b == 2) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   private boolean func_75295_a(EntityLiving var1) {
      this.field_75302_c = 10 + this.taskOwner.getRNG().nextInt(5);
      PathEntity var2 = this.taskOwner.getNavigator().getPathToEntityLiving(var1);
      if (var2 == null) {
         return false;
      } else {
         PathPoint var3 = var2.getFinalPathPoint();
         if (var3 == null) {
            return false;
         } else {
            int var4 = var3.xCoord - MathHelper.floor_double(var1.posX);
            int var5 = var3.zCoord - MathHelper.floor_double(var1.posZ);
            return var4 * var4 + var5 * var5 <= 2.25;
         }
      }
   }
}
