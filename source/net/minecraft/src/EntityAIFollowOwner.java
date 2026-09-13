package net.minecraft.src;

public class EntityAIFollowOwner extends EntityAIBase {
   private EntityTameable thePet;
   private EntityLiving theOwner;
   World theWorld;
   private float field_75336_f;
   private PathNavigate petPathfinder;
   private int field_75343_h;
   float maxDist;
   float minDist;
   private boolean field_75344_i;

   public EntityAIFollowOwner(EntityTameable par1EntityTameable, float par2, float par3, float par4) {
      this.thePet = par1EntityTameable;
      this.theWorld = par1EntityTameable.worldObj;
      this.field_75336_f = par2;
      this.petPathfinder = par1EntityTameable.aC();
      this.minDist = par3;
      this.maxDist = par4;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      EntityLiving var1 = this.thePet.getOwner();
      if (var1 == null) {
         return false;
      } else if (this.thePet.isSitting()) {
         return false;
      } else if (this.thePet.e(var1) < this.minDist * this.minDist) {
         return false;
      } else {
         this.theOwner = var1;
         return true;
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.petPathfinder.noPath() && this.thePet.e(this.theOwner) > this.maxDist * this.maxDist && !this.thePet.isSitting();
   }

   @Override
   public void startExecuting() {
      this.field_75343_h = 0;
      this.field_75344_i = this.thePet.aC().getAvoidsWater();
      this.thePet.aC().setAvoidsWater(false);
   }

   @Override
   public void resetTask() {
      this.theOwner = null;
      this.petPathfinder.clearPathEntity();
      this.thePet.aC().setAvoidsWater(this.field_75344_i);
   }

   @Override
   public void updateTask() {
      this.thePet.az().setLookPositionWithEntity(this.theOwner, 10.0F, this.thePet.bs());
      if (this.thePet.ridingEntity == null) {
         if (!this.thePet.isSitting() && --this.field_75343_h <= 0) {
            this.field_75343_h = 10;
            if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.field_75336_f) && this.thePet.e(this.theOwner) >= 144.0) {
               this.handleTeleportation();
            }
         }
      }
   }

   private void handleTeleportation() {
      if (!this.thePet.isAITryingToSit()) {
         Float xVector = MathHelper.sin(this.theOwner.rotationYaw / 180.0F * (float) Math.PI) * 4.0F;
         Float zVector = -(MathHelper.cos(this.theOwner.rotationYaw / 180.0F * (float) Math.PI) * 4.0F);
         int var1 = MathHelper.floor_double(this.theOwner.posX + xVector.floatValue());
         int var2 = MathHelper.floor_double(this.theOwner.posZ + zVector.floatValue());
         int var3 = MathHelper.floor_double(this.theOwner.boundingBox.minY);

         for (int xCount = 0; xCount <= 4; xCount++) {
            for (int zCount = 0; zCount <= 4; zCount++) {
               int xOffset = xCount + 1 >> 1;
               int zOffset = zCount + 1 >> 1;
               if ((xCount & 1) == 0) {
                  xOffset = -xOffset;
               }

               if ((zOffset & 1) == 0) {
                  zOffset = -zOffset;
               }

               if (this.theWorld.doesBlockHaveSolidTopSurface(var1 + xOffset, var3 - 1, var2 + zOffset)
                  && !this.theWorld.isBlockNormalCube(var1 + xOffset, var3, var2 + zOffset)
                  && !this.theWorld.isBlockNormalCube(var1 + xOffset, var3 + 1, var2 + zOffset)) {
                  this.thePet.b(var1 + xOffset + 0.5F, var3, var2 + zOffset + 0.5F, this.thePet.rotationYaw, this.thePet.rotationPitch);
                  this.petPathfinder.clearPathEntity();
                  return;
               }
            }
         }
      }
   }
}
