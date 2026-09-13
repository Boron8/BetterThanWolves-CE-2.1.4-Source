package net.minecraft.src;

import java.util.Calendar;

public class EntityBat extends EntityAmbientCreature {
   private ChunkCoordinates currentFlightTarget;

   public EntityBat(World par1World) {
      super(par1World);
      this.texture = "/mob/bat.png";
      this.a(0.5F, 0.9F);
      this.setIsBatHanging(true);
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   @Override
   protected float getSoundVolume() {
      return 0.1F;
   }

   @Override
   protected float getSoundPitch() {
      return super.aY() * 0.95F;
   }

   @Override
   protected String getLivingSound() {
      return this.getIsBatHanging() && this.rand.nextInt(4) != 0 ? null : "mob.bat.idle";
   }

   @Override
   protected String getHurtSound() {
      return "mob.bat.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.bat.death";
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   protected void collideWithEntity(Entity par1Entity) {
   }

   @Override
   protected void func_85033_bc() {
   }

   @Override
   public int getMaxHealth() {
      return 6;
   }

   public boolean getIsBatHanging() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setIsBatHanging(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }
   }

   @Override
   protected boolean isAIEnabled() {
      return true;
   }

   @Override
   public void onUpdate() {
      super.l_();
      if (this.getIsBatHanging()) {
         this.motionX = this.motionY = this.motionZ = 0.0;
         this.posY = MathHelper.floor_double(this.posY) + 1.0 - this.height;
      } else {
         this.motionY *= 0.6F;
      }
   }

   @Override
   protected void updateAITasks() {
      super.bo();
      if (this.getIsBatHanging()) {
         if (!this.worldObj.isBlockNormalCube(MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ))) {
            this.setIsBatHanging(false);
            this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
         } else {
            if (this.rand.nextInt(200) == 0) {
               this.rotationYawHead = this.rand.nextInt(360);
            }

            if (this.worldObj.getClosestPlayerToEntity(this, 4.0) != null) {
               this.setIsBatHanging(false);
               this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1015, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }
         }
      } else {
         if (this.currentFlightTarget != null
            && (
               !this.worldObj.isAirBlock(this.currentFlightTarget.posX, this.currentFlightTarget.posY, this.currentFlightTarget.posZ)
                  || this.currentFlightTarget.posY < 1
            )) {
            this.currentFlightTarget = null;
         }

         if (this.currentFlightTarget == null
            || this.rand.nextInt(30) == 0
            || this.currentFlightTarget.getDistanceSquared((int)this.posX, (int)this.posY, (int)this.posZ) < 4.0F) {
            this.currentFlightTarget = new ChunkCoordinates(
               (int)this.posX + this.rand.nextInt(7) - this.rand.nextInt(7),
               (int)this.posY + this.rand.nextInt(6) - 2,
               (int)this.posZ + this.rand.nextInt(7) - this.rand.nextInt(7)
            );
         }

         double var1 = this.currentFlightTarget.posX + 0.5 - this.posX;
         double var3 = this.currentFlightTarget.posY + 0.1 - this.posY;
         double var5 = this.currentFlightTarget.posZ + 0.5 - this.posZ;
         this.motionX = this.motionX + (Math.signum(var1) * 0.5 - this.motionX) * 0.1F;
         this.motionY = this.motionY + (Math.signum(var3) * 0.7F - this.motionY) * 0.1F;
         this.motionZ = this.motionZ + (Math.signum(var5) * 0.5 - this.motionZ) * 0.1F;
         float var7 = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0 / Math.PI) - 90.0F;
         float var8 = MathHelper.wrapAngleTo180_float(var7 - this.rotationYaw);
         this.moveForward = 0.5F;
         this.rotationYaw += var8;
         if (this.rand.nextInt(100) == 0
            && this.worldObj.isBlockNormalCube(MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ))) {
            this.setIsBatHanging(true);
         }
      }
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void fall(float par1) {
   }

   @Override
   protected void updateFallState(double par1, boolean par3) {
   }

   @Override
   public boolean doesEntityNotTriggerPressurePlate() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         if (!this.worldObj.isRemote && this.getIsBatHanging()) {
            this.setIsBatHanging(false);
         }

         return super.a(par1DamageSource, par2);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      this.dataWatcher.updateObject(16, par1NBTTagCompound.getByte("BatFlags"));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      par1NBTTagCompound.setByte("BatFlags", this.dataWatcher.getWatchableObjectByte(16));
   }

   @Override
   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(this.boundingBox.minY);
      if (var1 >= 63) {
         return false;
      } else {
         int var2 = MathHelper.floor_double(this.posX);
         int var3 = MathHelper.floor_double(this.posZ);
         int var4 = this.worldObj.getBlockLightValue(var2, var1, var3);
         byte var5 = 4;
         Calendar var6 = this.worldObj.getCurrentDate();
         if ((var6.get(2) + 1 != 10 || var6.get(5) < 20) && (var6.get(2) + 1 != 11 || var6.get(5) > 3)) {
            if (this.rand.nextBoolean()) {
               return false;
            }
         } else {
            var5 = 7;
         }

         return var4 > this.rand.nextInt(var5) ? false : super.bv();
      }
   }

   @Override
   public void initCreature() {
   }
}
