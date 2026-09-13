package net.minecraft.src;

public class EntitySquid extends EntityWaterMob {
   public float squidPitch = 0.0F;
   public float prevSquidPitch = 0.0F;
   public float squidYaw = 0.0F;
   public float prevSquidYaw = 0.0F;
   public float field_70867_h = 0.0F;
   public float field_70868_i = 0.0F;
   public float tentacleAngle = 0.0F;
   public float prevTentacleAngle = 0.0F;
   private float randomMotionSpeed = 0.0F;
   private float field_70864_bA = 0.0F;
   private float field_70871_bB = 0.0F;
   private float randomMotionVecX = 0.0F;
   private float randomMotionVecY = 0.0F;
   private float randomMotionVecZ = 0.0F;

   public EntitySquid(World var1) {
      super(var1);
      this.texture = "/mob/squid.png";
      this.a(0.95F, 0.95F);
      this.field_70864_bA = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
   }

   @Override
   public int getMaxHealth() {
      return 10;
   }

   @Override
   protected String getLivingSound() {
      return null;
   }

   @Override
   protected String getHurtSound() {
      return null;
   }

   @Override
   protected String getDeathSound() {
      return null;
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   protected int getDropItemId() {
      return 0;
   }

   @Override
   protected void dropFewItems(boolean var1, int var2) {
      int var3 = this.rand.nextInt(3 + var2) + 1;

      for (int var4 = 0; var4 < var3; var4++) {
         this.a(new ItemStack(Item.dyePowder, 1, 0), 0.0F);
      }
   }

   @Override
   public boolean isInWater() {
      return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0, -0.6F, 0.0), Material.water, this);
   }

   @Override
   public void onLivingUpdate() {
      super.c();
      this.prevSquidPitch = this.squidPitch;
      this.prevSquidYaw = this.squidYaw;
      this.field_70868_i = this.field_70867_h;
      this.prevTentacleAngle = this.tentacleAngle;
      this.field_70867_h = this.field_70867_h + this.field_70864_bA;
      if (this.field_70867_h > (float) (Math.PI * 2)) {
         this.field_70867_h -= (float) (Math.PI * 2);
         if (this.rand.nextInt(10) == 0) {
            this.field_70864_bA = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
         }
      }

      if (this.isInWater()) {
         if (this.field_70867_h < (float) Math.PI) {
            float var1 = this.field_70867_h / (float) Math.PI;
            this.tentacleAngle = MathHelper.sin(var1 * var1 * (float) Math.PI) * (float) Math.PI * 0.25F;
            if (var1 > 0.75) {
               this.randomMotionSpeed = 1.0F;
               this.field_70871_bB = 1.0F;
            } else {
               this.field_70871_bB *= 0.8F;
            }
         } else {
            this.tentacleAngle = 0.0F;
            this.randomMotionSpeed *= 0.9F;
            this.field_70871_bB *= 0.99F;
         }

         if (!this.worldObj.isRemote) {
            this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
            this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
            this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
         }

         float var2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.renderYawOffset = this.renderYawOffset
            + (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI - this.renderYawOffset) * 0.1F;
         this.rotationYaw = this.renderYawOffset;
         this.squidYaw = this.squidYaw + (float) Math.PI * this.field_70871_bB * 1.5F;
         this.squidPitch = this.squidPitch + (-((float)Math.atan2(var2, this.motionY)) * 180.0F / (float) Math.PI - this.squidPitch) * 0.1F;
      } else {
         this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.field_70867_h)) * (float) Math.PI * 0.25F;
         if (!this.worldObj.isRemote) {
            this.motionX = 0.0;
            this.motionY -= 0.08;
            this.motionY *= 0.98F;
            this.motionZ = 0.0;
         }

         this.squidPitch = (float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02);
      }
   }

   @Override
   public void moveEntityWithHeading(float var1, float var2) {
      this.d(this.motionX, this.motionY, this.motionZ);
   }

   @Override
   protected void updateEntityActionState() {
      this.entityAge++;
      if (this.entityAge > 100) {
         this.randomMotionVecX = this.randomMotionVecY = this.randomMotionVecZ = 0.0F;
      } else if (this.rand.nextInt(50) == 0 || !this.inWater || this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F
         )
       {
         float var1 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
         this.randomMotionVecX = MathHelper.cos(var1) * 0.2F;
         this.randomMotionVecY = -0.1F + this.rand.nextFloat() * 0.2F;
         this.randomMotionVecZ = MathHelper.sin(var1) * 0.2F;
      }

      this.bn();
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.posY > 45.0 && this.posY < 63.0 && super.getCanSpawnHere();
   }
}
