package net.minecraft.src;

public class EntityMoveHelper {
   private EntityLiving entity;
   private double posX;
   private double posY;
   private double posZ;
   private float speed;
   private boolean update = false;

   public EntityMoveHelper(EntityLiving var1) {
      this.entity = var1;
      this.posX = var1.posX;
      this.posY = var1.posY;
      this.posZ = var1.posZ;
   }

   public boolean isUpdating() {
      return this.update;
   }

   public float getSpeed() {
      return this.speed;
   }

   public void setMoveTo(double var1, double var3, double var5, float var7) {
      this.posX = var1;
      this.posY = var3;
      this.posZ = var5;
      this.speed = var7;
      this.update = true;
   }

   public void onUpdateMoveHelper() {
      this.entity.setMoveForward(0.0F);
      if (this.update) {
         this.update = false;
         int var1 = MathHelper.floor_double(this.entity.boundingBox.minY + 0.5);
         double var2 = this.posX - this.entity.posX;
         double var4 = this.posZ - this.entity.posZ;
         double var6 = this.posY - var1;
         double var8 = var2 * var2 + var6 * var6 + var4 * var4;
         if (!(var8 < 2.5000003E-7F)) {
            float var10 = (float)(Math.atan2(var4, var2) * 180.0 / (float) Math.PI) - 90.0F;
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, var10, 30.0F);
            this.entity.setAIMoveSpeed(this.speed * this.entity.getSpeedModifier());
            if (var6 > 0.0 && var2 * var2 + var4 * var4 < 1.0) {
               this.entity.getJumpHelper().setJumping();
            }
         }
      }
   }

   private float limitAngle(float var1, float var2, float var3) {
      float var4 = MathHelper.wrapAngleTo180_float(var2 - var1);
      if (var4 > var3) {
         var4 = var3;
      }

      if (var4 < -var3) {
         var4 = -var3;
      }

      return var1 + var4;
   }
}
