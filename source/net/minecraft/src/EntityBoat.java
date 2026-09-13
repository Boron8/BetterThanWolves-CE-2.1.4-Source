package net.minecraft.src;

import btw.block.blocks.BlockDispenserBlock;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityBoat extends Entity {
   private boolean field_70279_a = true;
   private double speedMultiplier = 0.07;
   private int boatPosRotationIncrements;
   private double boatX;
   private double boatY;
   private double boatZ;
   private double boatYaw;
   private double boatPitch;
   @Environment(EnvType.CLIENT)
   private double velocityX;
   @Environment(EnvType.CLIENT)
   private double velocityY;
   @Environment(EnvType.CLIENT)
   private double velocityZ;

   public EntityBoat(World par1World) {
      super(par1World);
      this.preventEntitySpawning = true;
      this.a(1.5F, 0.6F);
      this.yOffset = this.height / 2.0F;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(1));
      this.dataWatcher.addObject(19, new Integer(0));
   }

   @Override
   public AxisAlignedBB getCollisionBox(Entity par1Entity) {
      return par1Entity.boundingBox;
   }

   @Override
   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }

   @Override
   public boolean canBePushed() {
      return true;
   }

   public EntityBoat(World par1World, double par2, double par4, double par6) {
      this(par1World);
      this.b(par2, par4 + this.yOffset, par6);
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.prevPosX = par2;
      this.prevPosY = par4;
      this.prevPosZ = par6;
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.0 - 0.3F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if (!this.worldObj.isRemote && !this.isDead) {
         this.setForwardDirection(-this.getForwardDirection());
         this.setTimeSinceHit(10);
         this.setDamageTaken(this.getDamageTaken() + par2 * 10);
         this.J();
         boolean var3 = par1DamageSource.getEntity() instanceof EntityPlayer && ((EntityPlayer)par1DamageSource.getEntity()).capabilities.isCreativeMode;
         if (var3 || this.getDamageTaken() > 40) {
            if (this.riddenByEntity != null) {
               this.riddenByEntity.mountEntity(this);
            }

            if (!var3) {
               this.a(Item.boat.itemID, 1, 0.0F);
            }

            this.w();
         }

         return true;
      } else {
         return true;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void performHurtAnimation() {
      this.setForwardDirection(-this.getForwardDirection());
      this.setTimeSinceHit(10);
      this.setDamageTaken(this.getDamageTaken() * 11);
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      if (this.field_70279_a) {
         this.boatPosRotationIncrements = par9 + 5;
      } else {
         double var10 = par1 - this.posX;
         double var12 = par3 - this.posY;
         double var14 = par5 - this.posZ;
         double var16 = var10 * var10 + var12 * var12 + var14 * var14;
         if (var16 <= 1.0) {
            return;
         }

         this.boatPosRotationIncrements = 3;
      }

      this.boatX = par1;
      this.boatY = par3;
      this.boatZ = par5;
      this.boatYaw = par7;
      this.boatPitch = par8;
      this.motionX = this.velocityX;
      this.motionY = this.velocityY;
      this.motionZ = this.velocityZ;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setVelocity(double par1, double par3, double par5) {
      this.velocityX = this.motionX = par1;
      this.velocityY = this.motionY = par3;
      this.velocityZ = this.motionZ = par5;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.getTimeSinceHit() > 0) {
         this.setTimeSinceHit(this.getTimeSinceHit() - 1);
      }

      if (this.getDamageTaken() > 0) {
         this.setDamageTaken(this.getDamageTaken() - 1);
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      byte var1 = 5;
      double var2 = 0.0;

      for (int var4 = 0; var4 < var1; var4++) {
         double var5 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (var4 + 0) / var1 - 0.125;
         double var7 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (var4 + 1) / var1 - 0.125;
         AxisAlignedBB var9 = AxisAlignedBB.getAABBPool()
            .getAABB(this.boundingBox.minX, var5, this.boundingBox.minZ, this.boundingBox.maxX, var7, this.boundingBox.maxZ);
         if (this.worldObj.isAABBInMaterial(var9, Material.water)) {
            var2 += 1.0 / var1;
         }
      }

      if (var2 > 0.1F) {
         this.fallDistance = 0.0F;
         this.A();
      }

      double var23 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
      if (var23 > 0.26249999999999996) {
         double var6 = Math.cos(this.rotationYaw * Math.PI / 180.0);
         double var8 = Math.sin(this.rotationYaw * Math.PI / 180.0);

         for (int var10 = 0; var10 < 1.0 + var23 * 60.0; var10++) {
            double var11 = this.rand.nextFloat() * 2.0F - 1.0F;
            double var13 = (this.rand.nextInt(2) * 2 - 1) * 0.7;
            if (this.rand.nextBoolean()) {
               double var15 = this.posX - var6 * var11 * 0.8 + var8 * var13;
               double var17 = this.posZ - var8 * var11 * 0.8 - var6 * var13;
               this.worldObj.spawnParticle("splash", var15, this.posY - 0.125, var17, this.motionX, this.motionY, this.motionZ);
            } else {
               double var15 = this.posX + var6 + var8 * var11 * 0.7;
               double var17 = this.posZ + var8 - var6 * var11 * 0.7;
               this.worldObj.spawnParticle("splash", var15, this.posY - 0.125, var17, this.motionX, this.motionY, this.motionZ);
            }
         }
      }

      if (this.worldObj.isRemote && this.field_70279_a) {
         if (this.boatPosRotationIncrements > 0) {
            double var6 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
            double var8 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
            double var25 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
            double var12 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
            this.rotationYaw = (float)(this.rotationYaw + var12 / this.boatPosRotationIncrements);
            this.rotationPitch = (float)(this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
            this.boatPosRotationIncrements--;
            this.b(var6, var8, var25);
            this.b(this.rotationYaw, this.rotationPitch);
         } else {
            double var6 = this.posX + this.motionX;
            double var8 = this.posY + this.motionY;
            double var25 = this.posZ + this.motionZ;
            this.b(var6, var8, var25);
            if (this.onGround) {
               this.motionX *= 0.5;
               this.motionY *= 0.5;
               this.motionZ *= 0.5;
            }

            this.motionX *= 0.99F;
            this.motionY *= 0.95F;
            this.motionZ *= 0.99F;
         }
      } else {
         if (var2 < 1.0) {
            double var6 = var2 * 2.0 - 1.0;
            this.motionY += 0.04F * var6;
         } else {
            if (this.motionY < 0.0) {
               this.motionY /= 2.0;
            }

            this.motionY += 0.007F;
         }

         double dMaxSpeed = 0.35;
         if (this.riddenByEntity != null) {
            dMaxSpeed *= this.riddenByEntity.movementModifierWhenRidingBoat();
            this.motionX = this.motionX + this.riddenByEntity.motionX * this.speedMultiplier;
            this.motionZ = this.motionZ + this.riddenByEntity.motionZ * this.speedMultiplier;
            if (this.riddenByEntity.appliesConstantForceWhenRidingBoat()) {
               this.motionX = this.motionX - Math.cos(this.rotationYaw * Math.PI / 180.0) * dMaxSpeed * 0.02;
               this.motionZ = this.motionZ - Math.sin(this.rotationYaw * Math.PI / 180.0) * dMaxSpeed * 0.02;
            }
         }

         double var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
         if (var6 > dMaxSpeed) {
            double dSpeedModifier = dMaxSpeed / var6;
            if (dSpeedModifier < 0.9) {
               dSpeedModifier = 0.9;
            }

            this.motionX *= dSpeedModifier;
            this.motionZ *= dSpeedModifier;
            var6 *= dSpeedModifier;
         }

         if (var6 > var23 && this.speedMultiplier < 0.35) {
            this.speedMultiplier = this.speedMultiplier + (0.35 - this.speedMultiplier) / 35.0;
            if (this.speedMultiplier > 0.35) {
               this.speedMultiplier = 0.35;
            }
         } else {
            this.speedMultiplier = this.speedMultiplier - (this.speedMultiplier - 0.07) / 35.0;
            if (this.speedMultiplier < 0.07) {
               this.speedMultiplier = 0.07;
            }
         }

         if (this.onGround) {
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
         }

         this.d(this.motionX, this.motionY, this.motionZ);
         if (this.isCollidedHorizontally && var23 > 0.2) {
            this.breakBoat();
         } else {
            this.motionX *= 0.99F;
            this.motionY *= 0.95F;
            this.motionZ *= 0.99F;
         }

         this.rotationPitch = 0.0F;
         double var8 = this.rotationYaw;
         double var25 = this.prevPosX - this.posX;
         double var12 = this.prevPosZ - this.posZ;
         if (var25 * var25 + var12 * var12 > 0.001) {
            var8 = (float)(Math.atan2(var12, var25) * 180.0 / Math.PI);
         }

         double var14 = MathHelper.wrapAngleTo180_double(var8 - this.rotationYaw);
         if (var14 > 20.0) {
            var14 = 20.0;
         }

         if (var14 < -20.0) {
            var14 = -20.0;
         }

         this.rotationYaw = (float)(this.rotationYaw + var14);
         this.b(this.rotationYaw, this.rotationPitch);
         if (!this.worldObj.isRemote) {
            List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2F, 0.0, 0.2F));
            if (var16 != null && !var16.isEmpty()) {
               for (int var26 = 0; var26 < var16.size(); var26++) {
                  Entity var18 = (Entity)var16.get(var26);
                  if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityBoat) {
                     var18.applyEntityCollision(this);
                  }
               }
            }

            for (int var26x = 0; var26x < 4; var26x++) {
               int var27 = MathHelper.floor_double(this.posX + (var26x % 2 - 0.5) * 0.8);
               int var19 = MathHelper.floor_double(this.posZ + (var26x / 2 - 0.5) * 0.8);

               for (int var20 = 0; var20 < 2; var20++) {
                  int var21 = MathHelper.floor_double(this.posY) + var20;
                  int var22 = this.worldObj.getBlockId(var27, var21, var19);
                  Block tempBlock = Block.blocksList[var22];
                  if (tempBlock != null && tempBlock.isGroundCover()) {
                     this.worldObj.setBlockToAir(var27, var21, var19);
                  } else if (var22 == Block.waterlily.blockID) {
                     this.worldObj.destroyBlock(var27, var21, var19, true);
                  }
               }
            }

            if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
               this.riddenByEntity = null;
            }
         }
      }
   }

   @Override
   public void updateRiderPosition() {
      if (this.riddenByEntity != null) {
         double var1 = Math.cos(this.rotationYaw * Math.PI / 180.0) * 0.4;
         double var3 = Math.sin(this.rotationYaw * Math.PI / 180.0) * 0.4;
         this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != par1EntityPlayer) {
         return true;
      } else {
         if (!this.worldObj.isRemote) {
            par1EntityPlayer.mountEntity(this);
         }

         return true;
      }
   }

   public void setDamageTaken(int par1) {
      this.dataWatcher.updateObject(19, par1);
   }

   public int getDamageTaken() {
      return this.dataWatcher.getWatchableObjectInt(19);
   }

   public void setTimeSinceHit(int par1) {
      this.dataWatcher.updateObject(17, par1);
   }

   public int getTimeSinceHit() {
      return this.dataWatcher.getWatchableObjectInt(17);
   }

   public void setForwardDirection(int par1) {
      this.dataWatcher.updateObject(18, par1);
   }

   public int getForwardDirection() {
      return this.dataWatcher.getWatchableObjectInt(18);
   }

   @Environment(EnvType.CLIENT)
   public void func_70270_d(boolean par1) {
      this.field_70279_a = par1;
   }

   @Override
   public boolean canCollideWithEntity(Entity entity) {
      return !entity.isItemEntity();
   }

   @Override
   protected void fall(float fFallDistance) {
      super.fall(fFallDistance);
      if (fFallDistance > 5.0F) {
         this.breakBoat();
      }
   }

   public void breakBoat() {
      if (!this.worldObj.isRemote && !this.isDead) {
         this.w();

         for (int iCount = 0; iCount < 4; iCount++) {
            this.a(Item.stick.itemID, 1, 0.0F);
            this.a(BTWItems.sawDust.itemID, 1, 0.0F);
         }

         this.a("mob.zombie.woodbreak", 0.5F, 0.5F + this.rand.nextFloat() * 0.25F);
      }
   }

   @Override
   public boolean onBlockDispenserConsume(BlockDispenserBlock blockDispenser, BlockDispenserTileEntity tileEentityDispenser) {
      this.w();
      InventoryUtils.addSingleItemToInventory(tileEentityDispenser, Item.boat.itemID, 0);
      this.worldObj.playAuxSFX(1001, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      return true;
   }
}
