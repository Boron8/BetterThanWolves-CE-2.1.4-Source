package net.minecraft.src;

import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityFishHook extends Entity {
   private int xTile = -1;
   private int yTile = -1;
   private int zTile = -1;
   private int inTile = 0;
   private boolean inGround = false;
   public int shake = 0;
   public EntityPlayer angler;
   private int ticksInGround;
   private int ticksInAir = 0;
   private int ticksCatchable = 0;
   public Entity bobber = null;
   private int fishPosRotationIncrements;
   private double fishX;
   private double fishY;
   private double fishZ;
   private double fishYaw;
   private double fishPitch;
   @Environment(EnvType.CLIENT)
   private double velocityX;
   @Environment(EnvType.CLIENT)
   private double velocityY;
   @Environment(EnvType.CLIENT)
   private double velocityZ;
   private boolean isBaited;

   public EntityFishHook(World par1World) {
      super(par1World);
      this.a(0.25F, 0.25F);
      this.ignoreFrustumCheck = true;
      this.isBaited = false;
   }

   @Environment(EnvType.CLIENT)
   public EntityFishHook(World par1World, double par2, double par4, double par6, EntityPlayer par8EntityPlayer) {
      this(par1World);
      this.b(par2, par4, par6);
      this.ignoreFrustumCheck = true;
      this.angler = par8EntityPlayer;
      par8EntityPlayer.fishEntity = this;
   }

   public EntityFishHook(World par1World, EntityPlayer par2EntityPlayer) {
      super(par1World);
      this.ignoreFrustumCheck = true;
      this.angler = par2EntityPlayer;
      this.angler.fishEntity = this;
      this.a(0.25F, 0.25F);
      this.b(
         par2EntityPlayer.posX,
         par2EntityPlayer.posY + 1.62 - par2EntityPlayer.yOffset,
         par2EntityPlayer.posZ,
         par2EntityPlayer.rotationYaw,
         par2EntityPlayer.rotationPitch
      );
      this.posX = this.posX - MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
      this.posY -= 0.1F;
      this.posZ = this.posZ - MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
      this.b(this.posX, this.posY, this.posZ);
      this.yOffset = 0.0F;
      float var3 = 0.4F;
      this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var3;
      this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var3;
      this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI) * var3;
      this.calculateVelocity(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
      this.isBaited = false;
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

   public void calculateVelocity(double par1, double par3, double par5, float par7, float par8) {
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
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.fishX = par1;
      this.fishY = par3;
      this.fishZ = par5;
      this.fishYaw = par7;
      this.fishPitch = par8;
      this.fishPosRotationIncrements = par9;
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
      if (this.fishPosRotationIncrements > 0) {
         double var21 = this.posX + (this.fishX - this.posX) / this.fishPosRotationIncrements;
         double var22 = this.posY + (this.fishY - this.posY) / this.fishPosRotationIncrements;
         double var23 = this.posZ + (this.fishZ - this.posZ) / this.fishPosRotationIncrements;
         double var7 = MathHelper.wrapAngleTo180_double(this.fishYaw - this.rotationYaw);
         this.rotationYaw = (float)(this.rotationYaw + var7 / this.fishPosRotationIncrements);
         this.rotationPitch = (float)(this.rotationPitch + (this.fishPitch - this.rotationPitch) / this.fishPosRotationIncrements);
         this.fishPosRotationIncrements--;
         this.b(var21, var22, var23);
         this.b(this.rotationYaw, this.rotationPitch);
      } else {
         if (!this.worldObj.isRemote) {
            if (this.angler == null) {
               this.setDead();
               return;
            }

            ItemStack var1 = this.angler.getCurrentEquippedItem();
            if (this.angler.isDead
               || !this.angler.R()
               || var1 == null
               || (var1.getItem() != Item.fishingRod || this.isBaited) && (var1.getItem() != BTWItems.baitedFishingRod || !this.isBaited)
               || this.e(this.angler) > 1024.0) {
               this.setDead();
               this.angler.fishEntity = null;
               return;
            }

            if (this.bobber != null) {
               if (!this.bobber.isDead) {
                  this.posX = this.bobber.posX;
                  this.posY = this.bobber.boundingBox.minY + this.bobber.height * 0.8;
                  this.posZ = this.bobber.posZ;
                  return;
               }

               this.bobber = null;
            }
         }

         if (this.shake > 0) {
            this.shake--;
         }

         if (this.inGround) {
            int var19 = this.worldObj.getBlockId(this.xTile, this.yTile, this.zTile);
            if (var19 == this.inTile) {
               this.ticksInGround++;
               if (this.ticksInGround == 1200) {
                  this.setDead();
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

         Vec3 var20 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
         Vec3 var2 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
         MovingObjectPosition var3 = this.worldObj.rayTraceBlocks(var20, var2);
         var20 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
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
            if (var9.canBeCollidedWith() && (var9 != this.angler || this.ticksInAir >= 5)) {
               float var10 = 0.3F;
               AxisAlignedBB var11 = var9.boundingBox.expand(var10, var10, var10);
               MovingObjectPosition var12 = var11.calculateIntercept(var20, var2);
               if (var12 != null) {
                  double var13 = var20.distanceTo(var12.hitVec);
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
            if (var3.entityHit != null) {
               if (var3.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.angler), 0)) {
                  this.bobber = var3.entityHit;
               }
            } else {
               this.inGround = true;
            }
         }

         if (!this.inGround) {
            this.d(this.motionX, this.motionY, this.motionZ);
            float var24 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI);
            this.rotationPitch = (float)(Math.atan2(this.motionY, var24) * 180.0 / Math.PI);

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
            float var25 = 0.92F;
            if (this.onGround || this.isCollidedHorizontally) {
               var25 = 0.5F;
            }

            byte var27 = 5;
            double var26 = 0.0;

            for (int var29 = 0; var29 < var27; var29++) {
               double var14 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (var29 + 0) / var27 - 0.125 + 0.125;
               double var16 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (var29 + 1) / var27 - 0.125 + 0.125;
               AxisAlignedBB var18 = AxisAlignedBB.getAABBPool()
                  .getAABB(this.boundingBox.minX, var14, this.boundingBox.minZ, this.boundingBox.maxX, var16, this.boundingBox.maxZ);
               if (this.worldObj.isAABBInMaterial(var18, Material.water)) {
                  var26 += 1.0 / var27;
                  if (this.rand.nextInt(10) == 1 && this.isBodyOfWaterLargeEnoughForFishing()) {
                     float y = MathHelper.floor_double(this.boundingBox.minY);
                     float x = this.rand.nextFloat() * 2.0F - 1.0F;
                     float z = this.rand.nextFloat() * 2.0F - 1.0F;
                     this.worldObj.spawnParticle("bubble", this.posX + x, y + 1.0F, this.posZ + z, 0.0, 0.0, 0.0);
                  }
               }
            }

            if (var26 > 0.0) {
               if (this.ticksCatchable > 0) {
                  this.ticksCatchable--;
               } else if (this.checkForBite()) {
                  this.ticksCatchable = this.rand.nextInt(30) + 10;
                  this.motionY -= 0.2F;
                  this.a("random.splash", 0.5F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                  float var30 = MathHelper.floor_double(this.boundingBox.minY);

                  for (int var15 = 0; var15 < 1.0F + this.width * 20.0F; var15++) {
                     float var31 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                     float var17 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                     this.worldObj
                        .spawnParticle(
                           "bubble",
                           this.posX + var31,
                           var30 + 1.0F,
                           this.posZ + var17,
                           this.motionX,
                           this.motionY - this.rand.nextFloat() * 0.2F,
                           this.motionZ
                        );
                  }

                  for (int var38 = 0; var38 < 1.0F + this.width * 20.0F; var38++) {
                     float var31 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                     float var17 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
                     this.worldObj.spawnParticle("splash", this.posX + var31, var30 + 1.0F, this.posZ + var17, this.motionX, this.motionY, this.motionZ);
                  }

                  if (this.rand.nextInt(5) == 0) {
                     this.loseBait();
                  }
               }
            }

            if (this.ticksCatchable > 0) {
               this.motionY = this.motionY - this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat() * 0.2;
            }

            double var13 = var26 * 2.0 - 1.0;
            this.motionY += 0.04F * var13;
            if (var26 > 0.0) {
               var25 = (float)(var25 * 0.9);
               this.motionY *= 0.8;
            }

            this.motionX *= var25;
            this.motionY *= var25;
            this.motionZ *= var25;
            this.b(this.posX, this.posY, this.posZ);
         }
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("xTile", (short)this.xTile);
      par1NBTTagCompound.setShort("yTile", (short)this.yTile);
      par1NBTTagCompound.setShort("zTile", (short)this.zTile);
      par1NBTTagCompound.setByte("inTile", (byte)this.inTile);
      par1NBTTagCompound.setByte("shake", (byte)this.shake);
      par1NBTTagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.xTile = par1NBTTagCompound.getShort("xTile");
      this.yTile = par1NBTTagCompound.getShort("yTile");
      this.zTile = par1NBTTagCompound.getShort("zTile");
      this.inTile = par1NBTTagCompound.getByte("inTile") & 255;
      this.shake = par1NBTTagCompound.getByte("shake") & 255;
      this.inGround = par1NBTTagCompound.getByte("inGround") == 1;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   public int catchFish() {
      if (this.worldObj.isRemote) {
         return 0;
      } else {
         byte var1 = 0;
         if (this.bobber != null) {
            double var2 = this.angler.posX - this.posX;
            double var4 = this.angler.posY - this.posY;
            double var6 = this.angler.posZ - this.posZ;
            double var8 = MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
            double var10 = 0.1;
            this.bobber.motionX += var2 * var10;
            this.bobber.motionY = this.bobber.motionY + (var4 * var10 + MathHelper.sqrt_double(var8) * 0.08);
            this.bobber.motionZ += var6 * var10;
            var1 = 3;
         } else if (this.ticksCatchable > 0) {
            EntityItem var13 = (EntityItem)EntityList.createEntityOfType(
               EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.fishRaw)
            );
            this.loseBait();
            if (this.rand.nextInt(100) == 0) {
               var13 = (EntityItem)EntityList.createEntityOfType(
                  EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Item.bootsLeather)
               );
               var13.getEntityItem().setItemDamage(var13.getEntityItem().getMaxDamage() - (1 + this.rand.nextInt(10)));
            }

            double var3 = this.angler.posX - this.posX;
            double var5 = this.angler.posY - this.posY;
            double var7 = this.angler.posZ - this.posZ;
            double var9 = MathHelper.sqrt_double(var3 * var3 + var5 * var5 + var7 * var7);
            double var11 = 0.1;
            var13.motionX = var3 * var11;
            var13.motionY = var5 * var11 + MathHelper.sqrt_double(var9) * 0.08;
            var13.motionZ = var7 * var11;
            this.worldObj.spawnEntityInWorld(var13);
            this.angler.addStat(StatList.fishCaughtStat, 1);
            var1 = 1;
         }

         if (this.inGround) {
            var1 = 2;
         }

         this.setDead();
         this.angler.fishEntity = null;
         return var1;
      }
   }

   @Override
   public void setDead() {
      super.setDead();
      if (this.angler != null) {
         this.angler.fishEntity = null;
      }
   }

   public EntityFishHook(World world, EntityPlayer player, boolean bIsBaited) {
      this(world, player);
      this.isBaited = bIsBaited;
   }

   private void loseBait() {
      if (this.isBaited) {
         this.isBaited = false;
         ItemStack rodStack = this.angler.getCurrentEquippedItem();
         if (rodStack != null && rodStack.getItem() == BTWItems.baitedFishingRod) {
            ItemStack unbaitedStack = rodStack.copy();
            unbaitedStack.stackSize = 1;
            unbaitedStack.itemID = Item.fishingRod.itemID;
            this.angler.inventory.setInventorySlotContents(this.angler.inventory.currentItem, unbaitedStack);
         }
      }
   }

   private boolean isBodyOfWaterLargeEnoughForFishing() {
      int x = MathHelper.floor_double(this.posX);
      int y = MathHelper.floor_double(this.posY) - 1;
      int z = MathHelper.floor_double(this.posZ);
      int maxRadius = 2;

      for (int i = x - maxRadius; i <= x + maxRadius; i++) {
         for (int j = y - maxRadius; j <= y; j++) {
            for (int k = z - maxRadius; k <= z + maxRadius; k++) {
               if (!WorldUtils.isWaterSourceBlock(this.worldObj, i, j, k) && Math.abs(i) + Math.abs(j) + Math.abs(k) > maxRadius) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private boolean checkForBite() {
      if (this.isBaited) {
         int iBiteOdds = 1500;
         int iTimeOfDay = (int)(this.worldObj.worldInfo.getWorldTime() % 24000L);
         if (iTimeOfDay > 14000 && iTimeOfDay < 22000) {
            int iMoonPhase = this.worldObj.getMoonPhase();
            if (iMoonPhase == 0) {
               iBiteOdds /= 8;
            } else {
               iBiteOdds *= 4;
               if (this.worldObj.isPrecipitatingAtPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ))) {
                  iBiteOdds /= 2;
               }
            }
         } else {
            if (iTimeOfDay < 2000 || iTimeOfDay > 22000 || iTimeOfDay > 10000 && iTimeOfDay < 14000) {
               iBiteOdds /= 2;
            }

            if (this.worldObj.isPrecipitatingAtPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ))) {
               iBiteOdds /= 2;
            }
         }

         if (this.rand.nextInt(iBiteOdds) == 0
            && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) + 1, MathHelper.floor_double(this.posZ))
            && this.isBodyOfWaterLargeEnoughForFishing()) {
            return true;
         }
      }

      return false;
   }
}
