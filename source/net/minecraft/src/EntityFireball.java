package net.minecraft.src;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class EntityFireball extends Entity {
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private int inTile = 0;
   private boolean inGround = false;
   public EntityLiving shootingEntity;
   private int ticksAlive;
   private int ticksInAir = 0;
   public double accelerationX;
   public double accelerationY;
   public double accelerationZ;

   public EntityFireball(World par1World) {
      super(par1World);
      this.a(1.0F, 1.0F);
   }

   @Override
   protected void entityInit() {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean isInRangeToRenderDist(double par1) {
      double var3 = this.boundingBox.getAverageEdgeLength() * 4.0;
      var3 *= 64.0;
      return par1 < var3 * var3;
   }

   public EntityFireball(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World);
      this.a(1.0F, 1.0F);
      this.b(par2, par4, par6, this.rotationYaw, this.rotationPitch);
      this.b(par2, par4, par6);
      double var14 = MathHelper.sqrt_double(par8 * par8 + par10 * par10 + par12 * par12);
      this.accelerationX = par8 / var14 * 0.1;
      this.accelerationY = par10 / var14 * 0.1;
      this.accelerationZ = par12 / var14 * 0.1;
   }

   public EntityFireball(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7) {
      super(par1World);
      this.shootingEntity = par2EntityLiving;
      this.a(1.0F, 1.0F);
      this.b(par2EntityLiving.posX, par2EntityLiving.posY, par2EntityLiving.posZ, par2EntityLiving.rotationYaw, par2EntityLiving.rotationPitch);
      this.b(this.posX, this.posY, this.posZ);
      this.yOffset = 0.0F;
      this.motionX = this.motionY = this.motionZ = 0.0;
      par3 += this.rand.nextGaussian() * 0.4;
      par5 += this.rand.nextGaussian() * 0.4;
      par7 += this.rand.nextGaussian() * 0.4;
      double var9 = MathHelper.sqrt_double(par3 * par3 + par5 * par5 + par7 * par7);
      this.accelerationX = par3 / var9 * 0.1;
      this.accelerationY = par5 / var9 * 0.1;
      this.accelerationZ = par7 / var9 * 0.1;
   }

   @Override
   public void onUpdate() {
      if (this.worldObj.isRemote
         || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.worldObj.blockExists((int)this.posX, (int)this.posY, (int)this.posZ)) {
         super.onUpdate();
         this.d(1);
         if (this.inGround) {
            int var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            if (var1 == this.inTile) {
               this.ticksAlive++;
               if (this.ticksAlive == 600) {
                  this.w();
               }

               return;
            }

            this.inGround = false;
            this.motionX = this.motionX * (this.rand.nextFloat() * 0.2F);
            this.motionY = this.motionY * (this.rand.nextFloat() * 0.2F);
            this.motionZ = this.motionZ * (this.rand.nextFloat() * 0.2F);
            this.ticksAlive = 0;
            this.ticksInAir = 0;
         } else {
            this.ticksInAir++;
            if (this.ticksInAir >= 600) {
               this.w();
               return;
            }
         }

         Vec3 var15 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
         Vec3 var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var15, var2);
         var15 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
         var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         if (var3 != null) {
            var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
         }

         Entity var4 = null;
         List var5 = this.worldObj
            .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
         double var6 = 0.0;

         for (int var8 = 0; var8 < var5.size(); var8++) {
            Entity var9 = (Entity)var5.get(var8);
            if (var9.canBeCollidedWith() && (!var9.isEntityEqual(this.shootingEntity) || this.ticksInAir >= 25)) {
               float var10 = 0.3F;
               AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
               MovingObjectPosition var12 = var11.calculateIntercept(var15, var2);
               if (var12 != null) {
                  double var13 = var15.distanceTo(var12.hitVec);
                  if (var13 < var6 || var6 == 0.0) {
                     var4 = var9;
                     var6 = var13;
                  }
               }
            }
         }

         if (var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }

         if (var3 != null) {
            this.onImpact(var3);
         }

         this.posX = this.posX + this.motionX;
         this.posY = this.posY + this.motionY;
         this.posZ = this.posZ + this.motionZ;
         float var16 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
         this.rotationYaw = (float)(Math.atan2(this.motionZ, this.motionX) * 180.0 / Math.PI) + 90.0F;
         this.rotationPitch = (float)(Math.atan2(var16, this.motionY) * 180.0 / Math.PI) - 90.0F;

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
         float var17 = this.getMotionFactor();
         if (this.G()) {
            for (int var19 = 0; var19 < 4; var19++) {
               float var18 = 0.25F;
               this.worldObj
                  .spawnParticle(
                     "bubble",
                     this.posX - this.motionX * var18,
                     this.posY - this.motionY * var18,
                     this.posZ - this.motionZ * var18,
                     this.motionX,
                     this.motionY,
                     this.motionZ
                  );
            }

            var17 = 0.8F;
         }

         this.motionX = this.motionX + this.accelerationX;
         this.motionY = this.motionY + this.accelerationY;
         this.motionZ = this.motionZ + this.accelerationZ;
         this.motionX *= var17;
         this.motionY *= var17;
         this.motionZ *= var17;
         this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
         this.b(this.posX, this.posY, this.posZ);
      } else {
         this.w();
      }
   }

   protected float getMotionFactor() {
      return 0.95F;
   }

   protected abstract void onImpact(MovingObjectPosition var1);

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("xTile", (short)this.xTile);
      par1NBTTagCompound.setShort("yTile", (short)this.yTile);
      par1NBTTagCompound.setShort("zTile", (short)this.zTile);
      par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
      par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      par1NBTTagCompound.setTag("direction", this.a(new double[]{this.motionX, this.motionY, this.motionZ}));
      par1NBTTagCompound.setShort("ticksInAir", (short)this.ticksInAir);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.xTile = par1NBTTagCompound.getShort("xTile");
      this.yTile = par1NBTTagCompound.getShort("yTile");
      this.zTile = par1NBTTagCompound.getShort("zTile");
      this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
      this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
      if (par1NBTTagCompound.hasKey("direction")) {
         NBTTagList var2 = par1NBTTagCompound.getTagList("direction");
         this.motionX = ((NBTTagDouble)var2.tagAt(0)).data;
         this.motionY = ((NBTTagDouble)var2.tagAt(1)).data;
         this.motionZ = ((NBTTagDouble)var2.tagAt(2)).data;
      } else {
         this.w();
      }

      if (par1NBTTagCompound.hasKey("ticksInAir")) {
         this.ticksInAir = par1NBTTagCompound.getShort("ticksInAir");
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public float getCollisionBorderSize() {
      return 1.0F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         this.J();
         if (par1DamageSource.getEntity() != null) {
            Vec3 var3 = par1DamageSource.getEntity().getLookVec();
            if (var3 != null) {
               this.motionX = var3.xCoord;
               this.motionY = var3.yCoord;
               this.motionZ = var3.zCoord;
               this.accelerationX = this.motionX * 0.1;
               this.accelerationY = this.motionY * 0.1;
               this.accelerationZ = this.motionZ * 0.1;
            }

            if (par1DamageSource.getEntity() instanceof EntityLiving) {
               this.shootingEntity = (EntityLiving)par1DamageSource.getEntity();
            }

            return true;
         } else {
            return false;
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Override
   public float getBrightness(float par1) {
      return 1.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }
}
