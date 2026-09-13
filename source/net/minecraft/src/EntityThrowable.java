package net.minecraft.src;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class EntityThrowable extends Entity implements IProjectile {
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private int inTile = 0;
   protected boolean inGround = false;
   public int throwableShake = 0;
   private EntityLiving thrower;
   private String throwerName = null;
   private int ticksInGround;
   private int ticksInAir = 0;

   public EntityThrowable(World par1World) {
      super(par1World);
      this.a(0.25F, 0.25F);
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

   public EntityThrowable(World par1World, EntityLiving par2EntityLiving) {
      super(par1World);
      this.thrower = par2EntityLiving;
      this.a(0.25F, 0.25F);
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
      float var3 = 0.4F;
      this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var3;
      this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var3;
      this.motionY = -MathHelper.sin((this.rotationPitch + this.func_70183_g()) / 180.0F * (float) Math.PI) * var3;
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, this.func_70182_d(), 1.0F);
   }

   public EntityThrowable(World par1World, double par2, double par4, double par6) {
      super(par1World);
      this.ticksInGround = 0;
      this.a(0.25F, 0.25F);
      this.b(par2, par4, par6);
      this.yOffset = 0.0F;
   }

   protected float func_70182_d() {
      return 1.5F;
   }

   protected float func_70183_g() {
      return 0.0F;
   }

   @Override
   public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8) {
      float var9 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
      par1 /= var9;
      par3 /= var9;
      par5 /= var9;
      par1 += this.rand.nextGaussian() * 0.0075F * par8;
      par3 += this.rand.nextGaussian() * 0.0075F * par8;
      par5 += this.rand.nextGaussian() * 0.0075F * par8;
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
   public void setVelocity(double par1, double par3, double par5) {
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
      if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
         float var7 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
         this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0 / Math.PI);
         this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, var7) * 180.0 / Math.PI);
      }
   }

   @Override
   public void onUpdate() {
      this.lastTickPosX = this.posX;
      this.lastTickPosY = this.posY;
      this.lastTickPosZ = this.posZ;
      super.onUpdate();
      if (this.throwableShake > 0) {
         this.throwableShake--;
      }

      if (this.inGround) {
         int var1 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
         if (var1 == this.inTile) {
            this.ticksInGround++;
            if (this.ticksInGround == 1200) {
               this.w();
            }

            return;
         }

         this.inGround = false;
         this.motionX = this.motionX * (this.rand.nextFloat() * 0.2F);
         this.motionY = this.motionY * (this.rand.nextFloat() * 0.2F);
         this.motionZ = this.motionZ * (this.rand.nextFloat() * 0.2F);
         this.ticksInGround = 0;
         this.ticksInAir = 0;
      } else {
         this.ticksInAir++;
      }

      Vec3 var16 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
      Vec3 var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      MovingObjectPosition var3 = this.worldObj.rayTraceBlocks_do_do(var16, var2, false, true);
      var16 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
      var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
      if (var3 != null) {
         var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(var3.hitVec.xCoord, var3.hitVec.yCoord, var3.hitVec.zCoord);
      }

      if (!this.worldObj.isRemote) {
         Entity var4 = null;
         List var5 = this.worldObj
            .getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
         double var6 = 0.0;
         EntityLiving var8 = this.getThrower();

         for (int var9 = 0; var9 < var5.size(); var9++) {
            Entity var10 = (Entity)var5.get(var9);
            if (var10.canBeCollidedWith() && (var10 != var8 || this.ticksInAir >= 5)) {
               float var11 = 0.3F;
               AxisAlignedBB var12 = var10.boundingBox.expand(var11, var11, var11);
               MovingObjectPosition var13 = var12.calculateIntercept(var16, var2);
               if (var13 != null) {
                  double var14 = var16.distanceTo(var13.hitVec);
                  if (var14 < var6 || var6 == 0.0) {
                     var4 = var10;
                     var6 = var14;
                  }
               }
            }
         }

         if (var4 != null) {
            var3 = new MovingObjectPosition(var4);
         }
      }

      if (var3 != null) {
         if (var3.typeOfHit == EnumMovingObjectType.TILE && this.worldObj.getBlockId(var3.blockX, var3.blockY, var3.blockZ) == Block.portal.blockID) {
            this.Z();
         } else {
            this.onImpact(var3);
         }
      }

      this.posX = this.posX + this.motionX;
      this.posY = this.posY + this.motionY;
      this.posZ = this.posZ + this.motionZ;
      float var17 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
      this.rotationPitch = (float)(Math.atan2(this.motionY, var17) * 180.0 / Math.PI);

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
      float var18 = 0.99F;
      float var19 = this.getGravityVelocity();
      if (this.G()) {
         for (int var7 = 0; var7 < 4; var7++) {
            float var20 = 0.25F;
            this.worldObj
               .spawnParticle(
                  "bubble",
                  this.posX - this.motionX * var20,
                  this.posY - this.motionY * var20,
                  this.posZ - this.motionZ * var20,
                  this.motionX,
                  this.motionY,
                  this.motionZ
               );
         }

         var18 = 0.8F;
      }

      this.motionX *= var18;
      this.motionY *= var18;
      this.motionZ *= var18;
      this.motionY -= var19;
      this.b(this.posX, this.posY, this.posZ);
   }

   protected float getGravityVelocity() {
      return 0.03F;
   }

   protected abstract void onImpact(MovingObjectPosition var1);

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("xTile", (short)this.xTile);
      par1NBTTagCompound.setShort("yTile", (short)this.yTile);
      par1NBTTagCompound.setShort("zTile", (short)this.zTile);
      par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
      par1NBTTagCompound.setByte("shake", (byte)this.throwableShake);
      par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
      if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower != null && this.thrower instanceof EntityPlayer) {
         this.throwerName = this.thrower.getEntityName();
      }

      par1NBTTagCompound.setString("ownerName", this.throwerName == null ? "" : this.throwerName);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.xTile = par1NBTTagCompound.getShort("xTile");
      this.yTile = par1NBTTagCompound.getShort("yTile");
      this.zTile = par1NBTTagCompound.getShort("zTile");
      this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
      this.throwableShake = par1NBTTagCompound.getByte("shake") & 255;
      this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
      this.throwerName = par1NBTTagCompound.getString("ownerName");
      if (this.throwerName != null && this.throwerName.length() == 0) {
         this.throwerName = null;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   public EntityLiving getThrower() {
      if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
         this.thrower = this.worldObj.getPlayerEntityByName(this.throwerName);
      }

      return this.thrower;
   }

   protected void setThrower(EntityLiving throwerParam) {
      this.thrower = throwerParam;
   }
}
