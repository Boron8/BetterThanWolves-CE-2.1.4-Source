package net.minecraft.src;

public class EntityAITempt extends EntityAIBase {
   private EntityCreature temptedEntity;
   private float field_75282_b;
   private double field_75283_c;
   private double field_75280_d;
   private double field_75281_e;
   private double field_75278_f;
   private double field_75279_g;
   private EntityPlayer temptingPlayer;
   private int delayTemptCounter = 0;
   private boolean field_75287_j;
   private int breedingFood;
   private boolean scaredByPlayerMovement;
   private boolean field_75286_m;

   public EntityAITempt(EntityCreature par1EntityCreature, float par2, int par3, boolean par4) {
      this.temptedEntity = par1EntityCreature;
      this.field_75282_b = par2;
      this.breedingFood = par3;
      this.scaredByPlayerMovement = par4;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.delayTemptCounter > 0) {
         this.delayTemptCounter--;
         return false;
      } else {
         this.temptingPlayer = this.temptedEntity.worldObj.getClosestPlayerToEntity(this.temptedEntity, 10.0);
         if (this.temptingPlayer == null) {
            return false;
         } else {
            ItemStack var1 = this.temptingPlayer.getCurrentEquippedItem();
            return var1 == null ? false : var1.itemID == this.breedingFood;
         }
      }
   }

   @Override
   public boolean continueExecuting() {
      if (this.scaredByPlayerMovement) {
         if (this.temptedEntity.e(this.temptingPlayer) < 36.0) {
            if (this.temptingPlayer.e(this.field_75283_c, this.field_75280_d, this.field_75281_e) > 0.010000000000000002) {
               return false;
            }

            if (Math.abs(this.temptingPlayer.rotationPitch - this.field_75278_f) > 5.0 || Math.abs(this.temptingPlayer.rotationYaw - this.field_75279_g) > 5.0) {
               return false;
            }
         } else {
            this.field_75283_c = this.temptingPlayer.posX;
            this.field_75280_d = this.temptingPlayer.posY;
            this.field_75281_e = this.temptingPlayer.posZ;
         }

         this.field_75278_f = this.temptingPlayer.rotationPitch;
         this.field_75279_g = this.temptingPlayer.rotationYaw;
      }

      return this.shouldContinueFollowing();
   }

   @Override
   public void startExecuting() {
      this.field_75283_c = this.temptingPlayer.posX;
      this.field_75280_d = this.temptingPlayer.posY;
      this.field_75281_e = this.temptingPlayer.posZ;
      this.field_75287_j = true;
      this.field_75286_m = this.temptedEntity.aC().getAvoidsWater();
      this.temptedEntity.aC().setAvoidsWater(false);
   }

   @Override
   public void resetTask() {
      this.temptingPlayer = null;
      this.temptedEntity.aC().clearPathEntity();
      this.delayTemptCounter = 33;
      this.field_75287_j = false;
      this.temptedEntity.aC().setAvoidsWater(this.field_75286_m);
   }

   @Override
   public void updateTask() {
      this.temptedEntity.az().setLookPositionWithEntity(this.temptingPlayer, 30.0F, this.temptedEntity.bs());
      if (this.temptedEntity.e(this.temptingPlayer) < 6.25) {
         this.temptedEntity.aC().clearPathEntity();
      } else {
         this.temptedEntity.aC().tryMoveToEntityLiving(this.temptingPlayer, this.field_75282_b);
      }
   }

   public boolean func_75277_f() {
      return this.field_75287_j;
   }

   private boolean shouldContinueFollowing() {
      if (this.delayTemptCounter > 0) {
         this.delayTemptCounter--;
         return false;
      } else if (this.temptingPlayer != null && !this.temptingPlayer.isLivingDead) {
         if (this.temptedEntity.e(this.temptingPlayer) > 256.0) {
            return false;
         } else {
            ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
            return itemstack == null ? false : itemstack.itemID == this.breedingFood;
         }
      } else {
         return false;
      }
   }
}
