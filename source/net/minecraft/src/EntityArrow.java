package net.minecraft.src;

import btw.block.BTWBlocks;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityArrow extends Entity implements IProjectile {
   protected int xTile = -1;
   protected int yTile = -1;
   protected int zTile = -1;
   protected int inTile = 0;
   protected int inData = 0;
   protected boolean inGround = false;
   public int canBePickedUp = 0;
   public int arrowShake = 0;
   public Entity shootingEntity;
   private int ticksInGround;
   private int ticksInAir = 0;
   private double damage = 2.0;
   private int knockbackStrength;

   public EntityArrow(World par1World) {
      super(par1World);
      this.renderDistanceWeight = 10.0;
      this.a(0.5F, 0.5F);
   }

   public EntityArrow(World par1World, double par2, double par4, double par6) {
      super(par1World);
      this.renderDistanceWeight = 10.0;
      this.a(0.5F, 0.5F);
      this.b(par2, par4, par6);
      this.yOffset = 0.0F;
   }

   public EntityArrow(World par1World, EntityLiving par2EntityLiving, EntityLiving par3EntityLiving, float par4, float par5) {
      super(par1World);
      this.renderDistanceWeight = 10.0;
      this.shootingEntity = par2EntityLiving;
      if (par2EntityLiving instanceof EntityPlayer) {
         this.canBePickedUp = 1;
      }

      this.posY = par2EntityLiving.posY + par2EntityLiving.getEyeHeight() - 0.1F;
      double var6 = par3EntityLiving.posX - par2EntityLiving.posX;
      double var8 = par3EntityLiving.boundingBox.minY + par3EntityLiving.height / 3.0F - this.posY;
      double var10 = par3EntityLiving.posZ - par2EntityLiving.posZ;
      double var12 = MathHelper.sqrt_double(var6 * var6 + var10 * var10);
      if (var12 >= 1.0E-7) {
         float var14 = (float)(Math.atan2(var10, var6) * 180.0 / Math.PI) - 90.0F;
         float var15 = (float)(-(Math.atan2(var8, var12) * 180.0 / Math.PI));
         double var16 = var6 / var12;
         double var18 = var10 / var12;
         this.b(par2EntityLiving.posX + var16, this.posY, par2EntityLiving.posZ + var18, var14, var15);
         this.yOffset = 0.0F;
         float var20 = (float)var12 * 0.2F;
         this.setThrowableHeading(var6, var8 + var20, var10, par4, par5);
      }
   }

   public EntityArrow(World par1World, EntityLiving par2EntityLiving, float par3) {
      super(par1World);
      this.renderDistanceWeight = 10.0;
      this.shootingEntity = par2EntityLiving;
      if (par2EntityLiving instanceof EntityPlayer) {
         this.canBePickedUp = 1;
      }

      this.a(0.5F, 0.5F);
      this.b(
         par2EntityLiving.posX,
         par2EntityLiving.posY + par2EntityLiving.getEyeHeight(),
         par2EntityLiving.posZ,
         par2EntityLiving.rotationYaw,
         par2EntityLiving.rotationPitch
      );
      this.posX = this.posX - MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
      this.posY -= 0.1F;
      this.posZ = this.posZ - MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
      this.b(this.posX, this.posY, this.posZ);
      this.yOffset = 0.0F;
      this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
      this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI);
      this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI);
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3 * 1.5F, 1.0F);
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(16, (byte)0);
   }

   @Override
   public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
      float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= var9;
      par3 /= var9;
      par5 /= var9;
      par1 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.0075F * par8;
      par3 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.0075F * par8;
      par5 += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.0075F * par8;
      par1 *= par7;
      par3 *= par7;
      par5 *= par7;
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
      float var10 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
      this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0 / Math.PI);
      this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, var10) * 180.0 / Math.PI);
      this.ticksInGround = 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.b(par1, par3, par5);
      this.b(par7, par8);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setVelocity(double par1, double par3, double par5) {
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
         this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0 / Math.PI);
         this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, var7) * 180.0 / Math.PI);
         this.prevRotationPitch = this.rotationPitch;
         this.prevRotationYaw = this.rotationYaw;
         this.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         this.ticksInGround = 0;
      }
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
         this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, var1) * 180.0 / Math.PI);
      }

      int var16 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
      if (var16 > 0) {
         AxisAlignedBB var2 = Block.blocksList[var16].getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);
         if (var2 != null && var2.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ))) {
            this.inGround = true;
         }
      }

      if (this.arrowShake > 0) {
         this.arrowShake--;
      }

      if (this.inGround) {
         int var18 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
         int var19 = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
         if (var18 == this.inTile && (var19 == this.inData || var18 == BTWBlocks.detectorBlock.blockID)) {
            this.ticksInGround++;
            if (this.ticksInGround >= 6000) {
               this.w();
            }
         } else {
            this.inGround = false;
            this.motionX = this.motionX * (this.rand.nextFloat() * 0.2F);
            this.motionY = this.motionY * (this.rand.nextFloat() * 0.2F);
            this.motionZ = this.motionZ * (this.rand.nextFloat() * 0.2F);
            this.ticksInGround = 0;
            this.ticksInAir = 0;
         }
      } else {
         this.ticksInAir++;
         Vec3 var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
         Vec3 var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         MovingObjectPosition var4 = this.worldObj.rayTraceBlocks_do_do(var17, var3, false, true);
         var17 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
         var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         if (var4 != null) {
            var3 = this.worldObj.getWorldVec3Pool().getVecFromPool(var4.hitVec.xCoord, var4.hitVec.yCoord, var4.hitVec.zCoord);
         }

         Entity var5 = null;
         List var6 = this.worldObj
            .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
         double var7 = 0.0;

         for (int var9 = 0; var9 < var6.size(); var9++) {
            Entity var10 = (Entity)var6.get(var9);
            if (var10.canBeCollidedWith() && (var10 != this.shootingEntity || this.ticksInAir >= 5)) {
               float var11 = 0.3F;
               AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
               MovingObjectPosition var13 = var12.calculateIntercept(var17, var3);
               if (var13 != null) {
                  double var14 = var17.distanceTo(var13.hitVec);
                  if (var14 < var7 || var7 == 0.0) {
                     var5 = var10;
                     var7 = var14;
                  }
               }
            }
         }

         if (var5 != null) {
            var4 = new MovingObjectPosition(var5);
         }

         if (var4 != null && var4.entityHit != null && var4.entityHit instanceof EntityPlayer) {
            EntityPlayer var21 = (EntityPlayer)var4.entityHit;
            if (var21.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).func_96122_a(var21)) {
               var4 = null;
            }
         }

         if (var4 != null) {
            if (var4.entityHit != null) {
               float var20 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
               int var24 = MathHelper.ceiling_double_int(var20 * this.damage);
               if (this.getIsCritical()) {
                  var24 += this.rand.nextInt(var24 / 2 + 2);
               }

               DamageSource var22 = null;
               if (this.shootingEntity == null) {
                  var22 = DamageSource.causeArrowDamage(this, this);
               } else {
                  var22 = DamageSource.causeArrowDamage(this, this.shootingEntity);
               }

               if (this.ae() && !(var4.entityHit instanceof EntityEnderman)) {
                  var4.entityHit.setFire(5);
               }

               if (var4.entityHit.attackEntityFrom(var22, (int)(var24 * this.getDamageMultiplier()))) {
                  if (var4.entityHit instanceof EntityLiving) {
                     EntityLiving var25 = (EntityLiving)var4.entityHit;
                     if (!this.worldObj.isRemote) {
                        var25.setArrowCountInEntity(var25.getArrowCountInEntity() + 1);
                     }

                     if (this.knockbackStrength > 0) {
                        float var27 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        if (var27 > 0.0F) {
                           var4.entityHit
                              .addVelocity(this.motionX * this.knockbackStrength * 0.6F / var27, 0.1, this.motionZ * this.knockbackStrength * 0.6F / var27);
                        }
                     }

                     if (this.shootingEntity != null) {
                        EnchantmentThorns.func_92096_a(this.shootingEntity, var25, this.rand);
                     }

                     if (this.shootingEntity != null
                        && var4.entityHit != this.shootingEntity
                        && var4.entityHit instanceof EntityPlayer
                        && this.shootingEntity instanceof EntityPlayerMP) {
                        ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacketToPlayer(new Packet70GameEvent(6, 0));
                     }
                  }

                  this.a("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                  if (!(var4.entityHit instanceof EntityEnderman)) {
                     this.w();
                  }
               } else {
                  this.motionX *= -0.1F;
                  this.motionY *= -0.1F;
                  this.motionZ *= -0.1F;
                  this.rotationYaw += 180.0F;
                  this.prevRotationYaw += 180.0F;
                  this.ticksInAir = 0;
               }
            } else {
               this.xTile = var4.blockX;
               this.yTile = var4.blockY;
               this.zTile = var4.blockZ;
               this.inTile = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
               this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
               this.notifyCollidingBlockOfImpact();
               this.motionX = (float)(var4.hitVec.xCoord - this.posX);
               this.motionY = (float)(var4.hitVec.yCoord - this.posY);
               this.motionZ = (float)(var4.hitVec.zCoord - this.posZ);
               float var20x = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
               this.posX = this.posX - this.motionX / var20x * 0.05F;
               this.posY = this.posY - this.motionY / var20x * 0.05F;
               this.posZ = this.posZ - this.motionZ / var20x * 0.05F;
               this.a("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
               this.inGround = true;
               this.arrowShake = 7;
               this.setIsCritical(false);
               if (this.inTile != 0) {
                  Block.blocksList[this.inTile].onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
               }
            }
         }

         if (this.getIsCritical()) {
            for (int var22x = 0; var22x < 4; var22x++) {
               this.worldObj
                  .spawnParticle(
                     "crit",
                     this.posX + this.motionX * var22x / 4.0,
                     this.posY + this.motionY * var22x / 4.0,
                     this.posZ + this.motionZ * var22x / 4.0,
                     -this.motionX,
                     -this.motionY + 0.2,
                     -this.motionZ
                  );
            }
         }

         this.posX = this.posX + this.motionX;
         this.posY = this.posY + this.motionY;
         this.posZ = this.posZ + this.motionZ;
         float var20x = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
         this.rotationPitch = (float)(Math.atan2(this.motionY, var20x) * 180.0 / Math.PI);

         while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
         }

         while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
         }

         while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
         }

         while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
         }

         this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
         this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
         float var23 = 0.99F;
         float var11 = 0.05F;
         if (this.G()) {
            for (int var26 = 0; var26 < 4; var26++) {
               float var27 = 0.25F;
               this.worldObj
                  .spawnParticle(
                     "bubble",
                     this.posX - this.motionX * var27,
                     this.posY - this.motionY * var27,
                     this.posZ - this.motionZ * var27,
                     this.motionX,
                     this.motionY,
                     this.motionZ
                  );
            }

            var23 = 0.8F;
         }

         this.motionX *= var23;
         this.motionY *= var23;
         this.motionZ *= var23;
         this.motionY -= var11;
         this.b(this.posX, this.posY, this.posZ);
         this.C();
      }

      this.notifyAnyCollidingBlocks();
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("xTile", (short)this.xTile);
      par1NBTTagCompound.setShort("yTile", (short)this.yTile);
      par1NBTTagCompound.setShort("zTile", (short)this.zTile);
      par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
      par1NBTTagCompound.setByte("inData", (byte)this.inData);
      par1NBTTagCompound.setByte("shake", (byte)this.arrowShake);
      par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      par1NBTTagCompound.setByte("pickup", (byte)this.canBePickedUp);
      par1NBTTagCompound.setDouble("damage", this.damage);
      par1NBTTagCompound.setShort("fcInTileEx", (short)this.inTile);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.xTile = par1NBTTagCompound.getShort("xTile");
      this.yTile = par1NBTTagCompound.getShort("yTile");
      this.zTile = par1NBTTagCompound.getShort("zTile");
      this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
      this.inData = par1NBTTagCompound.getByte("inData") & 255;
      this.arrowShake = par1NBTTagCompound.getByte("shake") & 255;
      this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
      if (par1NBTTagCompound.hasKey("damage")) {
         this.damage = par1NBTTagCompound.getDouble("damage");
      }

      if (par1NBTTagCompound.hasKey("pickup")) {
         this.canBePickedUp = par1NBTTagCompound.getByte("pickup");
      } else if (par1NBTTagCompound.hasKey("player")) {
         this.canBePickedUp = par1NBTTagCompound.getBoolean("player") ? 1 : 0;
      }

      if (par1NBTTagCompound.hasKey("fcInTileEx")) {
         this.inTile = par1NBTTagCompound.getShort("fcInTileEx");
      }
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
      if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
         boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && par1EntityPlayer.capabilities.isCreativeMode;
         if (this.canBePickedUp == 1 && !this.addArrowToPlayerInv(par1EntityPlayer)) {
            var2 = false;
         }

         if (var2) {
            this.a("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            par1EntityPlayer.a(this, 1);
            this.w();
         }
      }
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   public void setDamage(double par1) {
      this.damage = par1;
   }

   public double getDamage() {
      return this.damage;
   }

   public void setKnockbackStrength(int par1) {
      this.knockbackStrength = par1;
   }

   @Override
   public boolean canAttackWithItem() {
      return false;
   }

   public void setIsCritical(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -2));
      }
   }

   public boolean getIsCritical() {
      byte var1 = this.dataWatcher.getWatchableObjectByte(16);
      return (var1 & 1) != 0;
   }

   protected float getDamageMultiplier() {
      return 1.0F;
   }

   protected boolean addArrowToPlayerInv(EntityPlayer player) {
      return player.inventory.addItemStackToInventory(new ItemStack(this.getCorrespondingItem(), 1));
   }

   public Item getCorrespondingItem() {
      return Item.arrow;
   }

   public boolean canHopperCollect() {
      return true;
   }

   private void notifyAnyCollidingBlocks() {
      if (this.inGround) {
         Block blockHit = Block.blocksList[this.inTile];
         if (blockHit != null) {
            blockHit.onArrowCollide(this.worldObj, this.xTile, this.yTile, this.zTile, this);
         }
      }
   }

   private void notifyCollidingBlockOfImpact() {
      Block blockHit = Block.blocksList[this.inTile];
      if (blockHit != null) {
         blockHit.onArrowImpact(this.worldObj, this.xTile, this.yTile, this.zTile, this);
      }
   }
}
