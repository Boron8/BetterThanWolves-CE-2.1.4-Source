package net.minecraft.src;

import btw.block.blocks.BlockDispenserBlock;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.entity.LightningBoltEntity;
import btw.entity.mob.CowEntity;
import btw.entity.mob.SquidEntity;
import btw.util.MathUtils;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

public abstract class Entity {
   private static int nextEntityID = 0;
   public int entityId = nextEntityID++;
   public double renderDistanceWeight = 1.0;
   public boolean preventEntitySpawning = false;
   public Entity riddenByEntity;
   public Entity ridingEntity;
   public boolean field_98038_p;
   public World worldObj;
   public double prevPosX;
   public double prevPosY;
   public double prevPosZ;
   public double posX;
   public double posY;
   public double posZ;
   public double motionX;
   public double motionY;
   public double motionZ;
   public float rotationYaw;
   public float rotationPitch;
   public float prevRotationYaw;
   public float prevRotationPitch;
   public final AxisAlignedBB boundingBox = AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
   public boolean onGround = false;
   public boolean isCollidedHorizontally;
   public boolean isCollidedVertically;
   public boolean isCollided = false;
   public boolean velocityChanged = false;
   protected boolean isInWeb;
   public boolean field_70135_K = true;
   public boolean isDead = false;
   public float yOffset = 0.0F;
   public float width = 0.6F;
   public float height = 1.8F;
   public float prevDistanceWalkedModified = 0.0F;
   public float distanceWalkedModified = 0.0F;
   public float distanceWalkedOnStepModified = 0.0F;
   public float fallDistance = 0.0F;
   private int nextStepDistance = 1;
   protected float nextBlockCheckDistance = 0.5F;
   protected int lastGroundPosX;
   protected int lastGroundPosY;
   protected int lastGroundPosZ;
   public double lastTickPosX;
   public double lastTickPosY;
   public double lastTickPosZ;
   public float ySize = 0.0F;
   public float stepHeight = 0.0F;
   public boolean noClip = false;
   public float entityCollisionReduction = 0.0F;
   public Random rand = new Random();
   public int ticksExisted = 0;
   public int fireResistance = 1;
   private int fire = 0;
   public boolean inWater = false;
   public int hurtResistantTime = 0;
   private boolean firstUpdate = true;
   @Environment(EnvType.CLIENT)
   public String skinUrl;
   @Environment(EnvType.CLIENT)
   public String cloakUrl;
   protected boolean isImmuneToFire = false;
   protected DataWatcher dataWatcher = new DataWatcher();
   private double entityRiderPitchDelta;
   private double entityRiderYawDelta;
   public boolean addedToChunk = false;
   public int chunkCoordX;
   public int chunkCoordY;
   public int chunkCoordZ;
   @Environment(EnvType.CLIENT)
   public int serverPosX;
   @Environment(EnvType.CLIENT)
   public int serverPosY;
   @Environment(EnvType.CLIENT)
   public int serverPosZ;
   public boolean ignoreFrustumCheck;
   public boolean isAirBorne;
   public int timeUntilPortal;
   protected boolean inPortal;
   protected int field_82153_h;
   public int dimension;
   protected int teleportDirection = 0;
   private boolean invulnerable = false;
   private UUID entityUniqueID = UUID.randomUUID();
   public EnumEntitySize myEntitySize = EnumEntitySize.SIZE_2;

   public Entity(World par1World) {
      this.worldObj = par1World;
      this.setPosition(0.0, 0.0, 0.0);
      if (par1World != null) {
         this.dimension = par1World.provider.dimensionId;
      }

      this.dataWatcher.addObject(0, (byte)0);
      this.dataWatcher.addObject(1, (short)300);
      this.entityInit();
   }

   protected abstract void entityInit();

   public DataWatcher getDataWatcher() {
      return this.dataWatcher;
   }

   @Override
   public boolean equals(Object par1Obj) {
      return par1Obj instanceof Entity ? ((Entity)par1Obj).entityId == this.entityId : false;
   }

   @Override
   public int hashCode() {
      return this.entityId;
   }

   @Environment(EnvType.CLIENT)
   protected void preparePlayerToSpawn() {
      if (this.worldObj != null) {
         while (true) {
            if (this.posY > 0.0) {
               this.setPosition(this.posX, this.posY, this.posZ);
               if (!this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()) {
                  this.posY++;
                  continue;
               }
            }

            this.motionX = this.motionY = this.motionZ = 0.0;
            this.rotationPitch = 0.0F;
            break;
         }
      }
   }

   public void setDead() {
      this.isDead = true;
   }

   protected void setSize(float par1, float par2) {
      if (par1 != this.width || par2 != this.height) {
         this.width = par1;
         this.height = par2;
         this.boundingBox.maxX = this.boundingBox.minX + this.width;
         this.boundingBox.maxZ = this.boundingBox.minZ + this.width;
         this.boundingBox.maxY = this.boundingBox.minY + this.height;
      }

      float var3 = par1 % 2.0F;
      if (var3 < 0.375) {
         this.myEntitySize = EnumEntitySize.SIZE_1;
      } else if (var3 < 0.75) {
         this.myEntitySize = EnumEntitySize.SIZE_2;
      } else if (var3 < 1.0) {
         this.myEntitySize = EnumEntitySize.SIZE_3;
      } else if (var3 < 1.375) {
         this.myEntitySize = EnumEntitySize.SIZE_4;
      } else if (var3 < 1.75) {
         this.myEntitySize = EnumEntitySize.SIZE_5;
      } else {
         this.myEntitySize = EnumEntitySize.SIZE_6;
      }
   }

   protected void setRotation(float par1, float par2) {
      this.rotationYaw = par1 % 360.0F;
      this.rotationPitch = par2 % 360.0F;
   }

   public void setPosition(double par1, double par3, double par5) {
      this.posX = par1;
      this.posY = par3;
      this.posZ = par5;
      float var7 = this.width / 2.0F;
      float var8 = this.height;
      this.boundingBox.setBounds(par1 - var7, par3 - this.yOffset + this.ySize, par5 - var7, par1 + var7, par3 - this.yOffset + this.ySize + var8, par5 + var7);
   }

   @Environment(EnvType.CLIENT)
   public void setAngles(float par1, float par2) {
      float var3 = this.rotationPitch;
      float var4 = this.rotationYaw;
      this.rotationYaw = (float)(this.rotationYaw + par1 * 0.15);
      this.rotationPitch = (float)(this.rotationPitch - par2 * 0.15);
      if (this.rotationPitch < -90.0F) {
         this.rotationPitch = -90.0F;
      }

      if (this.rotationPitch > 90.0F) {
         this.rotationPitch = 90.0F;
      }

      this.prevRotationPitch = this.prevRotationPitch + (this.rotationPitch - var3);
      this.prevRotationYaw = this.prevRotationYaw + (this.rotationYaw - var4);
   }

   public void onUpdate() {
      this.onEntityUpdate();
   }

   public void onEntityUpdate() {
      this.worldObj.theProfiler.startSection("entityBaseTick");
      if (this.ridingEntity != null && this.ridingEntity.isDead) {
         this.ridingEntity = null;
      }

      this.prevDistanceWalkedModified = this.distanceWalkedModified;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.prevRotationPitch = this.rotationPitch;
      this.prevRotationYaw = this.rotationYaw;
      if (!this.worldObj.isRemote && this.worldObj instanceof WorldServer) {
         this.worldObj.theProfiler.startSection("portal");
         MinecraftServer var1 = ((WorldServer)this.worldObj).getMinecraftServer();
         int var2 = this.getMaxInPortalTime();
         if (this.inPortal) {
            if (var1.getAllowNether()) {
               if (this.ridingEntity == null && this.field_82153_h++ >= var2) {
                  this.field_82153_h = var2;
                  this.timeUntilPortal = this.getPortalCooldown();
                  byte var3;
                  if (this.worldObj.provider.dimensionId == -1) {
                     var3 = 0;
                  } else {
                     var3 = -1;
                  }

                  this.travelToDimension(var3);
               }

               this.inPortal = false;
            }
         } else {
            if (this.field_82153_h > 0) {
               this.field_82153_h -= 4;
            }

            if (this.field_82153_h < 0) {
               this.field_82153_h = 0;
            }
         }

         if (this.timeUntilPortal > 0) {
            this.timeUntilPortal--;
         }

         this.worldObj.theProfiler.endSection();
      }

      if (this.isSprinting() && !this.isInWater()) {
         int var5 = MathHelper.floor_double(this.posX);
         int var2x = MathHelper.floor_double(this.posY - 0.2F - this.yOffset);
         int var6 = MathHelper.floor_double(this.posZ);
         int var4 = this.worldObj.getBlockId(var5, var2x, var6);
         if (var4 > 0) {
            this.worldObj
               .spawnParticle(
                  "tilecrack_" + var4 + "_" + this.worldObj.getBlockMetadata(var5, var2x, var6),
                  this.posX + (this.rand.nextFloat() - 0.5) * this.width,
                  this.boundingBox.minY + 0.1,
                  this.posZ + (this.rand.nextFloat() - 0.5) * this.width,
                  -this.motionX * 4.0,
                  1.5,
                  -this.motionZ * 4.0
               );
         }
      }

      this.handleWaterMovement();
      if (this.worldObj.isRemote) {
         this.fire = 0;
      } else if (this.fire > 0) {
         if (this.isImmuneToFire) {
            this.fire -= 4;
            if (this.fire < 0) {
               this.fire = 0;
            }
         } else {
            if (this.fire % 20 == 0) {
               this.attackEntityFrom(DamageSource.onFire, 1);
            }

            this.fire--;
         }
      }

      if (this.handleLavaMovement()) {
         this.setOnFireFromLava();
         this.fallDistance *= 0.5F;
      }

      if (this.posY < -64.0) {
         this.kill();
      }

      if (!this.worldObj.isRemote) {
         this.setFlag(0, this.fire > 0);
         this.setFlag(2, this.ridingEntity != null);
      }

      this.firstUpdate = false;
      this.worldObj.theProfiler.endSection();
   }

   public int getMaxInPortalTime() {
      return 0;
   }

   protected void setOnFireFromLava() {
      if (!this.isImmuneToFire) {
         this.attackEntityFrom(DamageSource.lava, 4);
         this.setFire(15);
      }
   }

   public void setFire(int par1) {
      int var2 = par1 * 20;
      var2 = EnchantmentProtection.func_92093_a(this, var2);
      if (this.fire < var2) {
         this.fire = var2;
      }
   }

   public void extinguish() {
      this.fire = 0;
   }

   protected void kill() {
      this.setDead();
   }

   public boolean isOffsetPositionInLiquid(double par1, double par3, double par5) {
      AxisAlignedBB var7 = this.boundingBox.getOffsetBoundingBox(par1, par3, par5);
      List var8 = this.worldObj.getCollidingBoundingBoxes(this, var7);
      return !var8.isEmpty() ? false : !this.worldObj.isAnyLiquid(var7);
   }

   protected void doBlockCollisions() {
      int var1 = MathHelper.floor_double(this.boundingBox.minX + 0.001);
      int var2 = MathHelper.floor_double(this.boundingBox.minY + 0.001);
      int var3 = MathHelper.floor_double(this.boundingBox.minZ + 0.001);
      int var4 = MathHelper.floor_double(this.boundingBox.maxX - 0.001);
      int var5 = MathHelper.floor_double(this.boundingBox.maxY - 0.001);
      int var6 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001);
      if (this.worldObj.checkChunksExist(var1, var2, var3, var4, var5, var6)) {
         for (int var7 = var1; var7 <= var4; var7++) {
            for (int var8 = var2; var8 <= var5; var8++) {
               for (int var9 = var3; var9 <= var6; var9++) {
                  int var10 = this.worldObj.getBlockId(var7, var8, var9);
                  if (var10 > 0) {
                     Block.blocksList[var10].onEntityCollidedWithBlock(this.worldObj, var7, var8, var9, this);
                  }
               }
            }
         }
      }
   }

   protected void playStepSound(int par1, int par2, int par3, int par4) {
      StepSound var5 = Block.blocksList[par4].getStepSound(this.worldObj, par1, par2, par3);
      int iBlockAboveID = this.worldObj.getBlockId(par1, par2 + 1, par3);
      Block blockAbove = Block.blocksList[iBlockAboveID];
      if (blockAbove != null && blockAbove.isGroundCover()) {
         var5 = blockAbove.getStepSound(this.worldObj, par1, par2, par3);
         this.playSound(var5.getStepSound(), var5.getStepVolume() * 0.15F, var5.getStepPitch());
      } else if (!Block.blocksList[par4].blockMaterial.isLiquid()) {
         this.playSound(var5.getStepSound(), var5.getStepVolume() * 0.15F, var5.getStepPitch());
      }
   }

   public void playSound(String par1Str, float par2, float par3) {
      this.worldObj.playSoundAtEntity(this, par1Str, par2, par3);
   }

   protected boolean canTriggerWalking() {
      return true;
   }

   protected void updateFallState(double par1, boolean par3) {
      if (par3) {
         if (this.fallDistance > 0.0F) {
            this.fall(this.fallDistance);
            this.fallDistance = 0.0F;
         }
      } else if (par1 < 0.0) {
         this.fallDistance = (float)(this.fallDistance - par1);
      }
   }

   public AxisAlignedBB getBoundingBox() {
      return null;
   }

   public void dealFireDamage(int par1) {
      if (!this.isImmuneToFire) {
         this.attackEntityFrom(DamageSource.inFire, par1);
      }
   }

   public final boolean isImmuneToFire() {
      return this.isImmuneToFire;
   }

   protected void fall(float par1) {
      if (this.riddenByEntity != null) {
         this.riddenByEntity.fall(par1);
      }
   }

   public boolean isWet() {
      return this.inWater || this.isBeingRainedOn();
   }

   public boolean isInWater() {
      return this.inWater;
   }

   public boolean handleWaterMovement() {
      if (this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0, -0.4F, 0.0).contract(0.001, 0.001, 0.001), Material.water, this)) {
         if (!this.inWater && !this.firstUpdate) {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX * 0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.2F) * 0.2F;
            if (var1 > 1.0F) {
               var1 = 1.0F;
            }

            this.playSound("liquid.splash", var1, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            float var2 = MathHelper.floor_double(this.boundingBox.minY);

            for (int var3 = 0; var3 < 1.0F + this.width * 20.0F; var3++) {
               float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
               float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
               this.worldObj
                  .spawnParticle(
                     "bubble", this.posX + var4, var2 + 1.0F, this.posZ + var5, this.motionX, this.motionY - this.rand.nextFloat() * 0.2F, this.motionZ
                  );
            }

            for (int var6 = 0; var6 < 1.0F + this.width * 20.0F; var6++) {
               float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
               float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width;
               this.worldObj.spawnParticle("splash", this.posX + var4, var2 + 1.0F, this.posZ + var5, this.motionX, this.motionY, this.motionZ);
            }
         }

         this.fallDistance = 0.0F;
         this.inWater = true;
         this.fire = 0;
      } else {
         this.inWater = false;
      }

      return this.inWater;
   }

   public boolean isInsideOfMaterial(Material par1Material) {
      double var2 = this.posY + this.getEyeHeight();
      int var4 = MathHelper.floor_double(this.posX);
      int var5 = MathHelper.floor_float(MathHelper.floor_double(var2));
      int var6 = MathHelper.floor_double(this.posZ);
      int var7 = this.worldObj.getBlockId(var4, var5, var6);
      if (var7 != 0 && Block.blocksList[var7].blockMaterial == par1Material) {
         float var8 = BlockFluid.getFluidHeightPercent(this.worldObj.getBlockMetadata(var4, var5, var6)) - 0.11111111F;
         float var9 = var5 + 1 - var8;
         return var2 < var9;
      } else {
         return false;
      }
   }

   public float getEyeHeight() {
      return 0.0F;
   }

   public boolean handleLavaMovement() {
      return this.worldObj.isMaterialInBB(this.boundingBox.expand(-0.1F, -0.4F, -0.1F), Material.lava);
   }

   public void moveFlying(float par1, float par2, float par3) {
      float var4 = par1 * par1 + par2 * par2;
      if (var4 >= 1.0E-4F) {
         var4 = MathHelper.sqrt_float(var4);
         if (var4 < 1.0F) {
            var4 = 1.0F;
         }

         var4 = par3 / var4;
         par1 *= var4;
         par2 *= var4;
         float var5 = MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F);
         float var6 = MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F);
         this.motionX += par1 * var6 - par2 * var5;
         this.motionZ += par2 * var6 + par1 * var5;
      }
   }

   public int getBrightnessForRender(float par1) {
      int var2 = MathHelper.floor_double(this.posX);
      int var3 = MathHelper.floor_double(this.posZ);
      if (this.worldObj.blockExists(var2, 0, var3)) {
         double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66;
         int var6 = MathHelper.floor_double(this.posY - this.yOffset + var4);
         return this.worldObj.getLightBrightnessForSkyBlocks(var2, var6, var3, 0);
      } else {
         return 0;
      }
   }

   public float getBrightness(float par1) {
      int var2 = MathHelper.floor_double(this.posX);
      int var3 = MathHelper.floor_double(this.posZ);
      if (this.worldObj.blockExists(var2, 0, var3)) {
         double var4 = (this.boundingBox.maxY - this.boundingBox.minY) * 0.66;
         int var6 = MathHelper.floor_double(this.posY - this.yOffset + var4);
         return this.worldObj.getLightBrightness(var2, var6, var3);
      } else {
         return 0.0F;
      }
   }

   public void setWorld(World par1World) {
      this.worldObj = par1World;
   }

   public void setPositionAndRotation(double par1, double par3, double par5, float par7, float par8) {
      this.prevPosX = this.posX = par1;
      this.prevPosY = this.posY = par3;
      this.prevPosZ = this.posZ = par5;
      this.prevRotationYaw = this.rotationYaw = par7;
      this.prevRotationPitch = this.rotationPitch = par8;
      this.ySize = 0.0F;
      double var9 = this.prevRotationYaw - par7;
      if (var9 < -180.0) {
         this.prevRotationYaw += 360.0F;
      }

      if (var9 >= 180.0) {
         this.prevRotationYaw -= 360.0F;
      }

      this.setPosition(this.posX, this.posY, this.posZ);
      this.setRotation(par7, par8);
   }

   public void setLocationAndAngles(double par1, double par3, double par5, float par7, float par8) {
      this.lastTickPosX = this.prevPosX = this.posX = par1;
      this.lastTickPosY = this.prevPosY = this.posY = par3 + this.yOffset;
      this.lastTickPosZ = this.prevPosZ = this.posZ = par5;
      this.rotationYaw = par7;
      this.rotationPitch = par8;
      this.setPosition(this.posX, this.posY, this.posZ);
   }

   public float getDistanceToEntity(Entity par1Entity) {
      float var2 = (float)(this.posX - par1Entity.posX);
      float var3 = (float)(this.posY - par1Entity.posY);
      float var4 = (float)(this.posZ - par1Entity.posZ);
      return MathHelper.sqrt_float(var2 * var2 + var3 * var3 + var4 * var4);
   }

   public double getDistanceSq(double par1, double par3, double par5) {
      double var7 = this.posX - par1;
      double var9 = this.posY - par3;
      double var11 = this.posZ - par5;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public double getDistance(double par1, double par3, double par5) {
      double var7 = this.posX - par1;
      double var9 = this.posY - par3;
      double var11 = this.posZ - par5;
      return MathHelper.sqrt_double(var7 * var7 + var9 * var9 + var11 * var11);
   }

   public double getDistanceSqToEntity(Entity par1Entity) {
      double var2 = this.posX - par1Entity.posX;
      double var4 = this.posY - par1Entity.posY;
      double var6 = this.posZ - par1Entity.posZ;
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
   }

   public void applyEntityCollision(Entity par1Entity) {
      if (par1Entity.riddenByEntity != this && par1Entity.ridingEntity != this) {
         double var2 = par1Entity.posX - this.posX;
         double var4 = par1Entity.posZ - this.posZ;
         double var6 = MathHelper.abs_max(var2, var4);
         if (var6 >= 0.01F) {
            var6 = MathHelper.sqrt_double(var6);
            var2 /= var6;
            var4 /= var6;
            double var8 = 1.0 / var6;
            if (var8 > 1.0) {
               var8 = 1.0;
            }

            var2 *= var8;
            var4 *= var8;
            var2 *= 0.05F;
            var4 *= 0.05F;
            var2 *= 1.0F - this.entityCollisionReduction;
            var4 *= 1.0F - this.entityCollisionReduction;
            this.addVelocity(-var2, 0.0, -var4);
            par1Entity.addVelocity(var2, 0.0, var4);
         }
      }
   }

   public void addVelocity(double par1, double par3, double par5) {
      this.motionX += par1;
      this.motionY += par3;
      this.motionZ += par5;
      this.isAirBorne = true;
   }

   public void setBeenAttacked() {
      this.velocityChanged = true;
   }

   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.isEntityInvulnerable()) {
         return false;
      } else {
         this.setBeenAttacked();
         return false;
      }
   }

   public boolean canBeCollidedWith() {
      return false;
   }

   public boolean canBePushed() {
      return false;
   }

   public void addToPlayerScore(Entity par1Entity, int par2) {
   }

   @Environment(EnvType.CLIENT)
   public boolean isInRangeToRenderVec3D(Vec3 par1Vec3) {
      double var2 = this.posX - par1Vec3.xCoord;
      double var4 = this.posY - par1Vec3.yCoord;
      double var6 = this.posZ - par1Vec3.zCoord;
      double var8 = var2 * var2 + var4 * var4 + var6 * var6;
      return this.isInRangeToRenderDist(var8);
   }

   @Environment(EnvType.CLIENT)
   public boolean isInRangeToRenderDist(double par1) {
      double var3 = this.boundingBox.getAverageEdgeLength();
      var3 *= 64.0 * this.renderDistanceWeight;
      return par1 < var3 * var3;
   }

   @Environment(EnvType.CLIENT)
   public String getTexture() {
      return null;
   }

   public boolean addNotRiddenEntityID(NBTTagCompound par1NBTTagCompound) {
      String var2 = this.getEntityString();
      if (!this.isDead && var2 != null) {
         par1NBTTagCompound.setString("id", var2);
         this.writeToNBT(par1NBTTagCompound);
         return true;
      } else {
         return false;
      }
   }

   public boolean addEntityID(NBTTagCompound par1NBTTagCompound) {
      String var2 = this.getEntityString();
      if (!this.isDead && var2 != null && this.riddenByEntity == null) {
         par1NBTTagCompound.setString("id", var2);
         this.writeToNBT(par1NBTTagCompound);
         return true;
      } else {
         return false;
      }
   }

   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      try {
         par1NBTTagCompound.setTag("Pos", this.newDoubleNBTList(this.posX, this.posY + this.ySize, this.posZ));
         par1NBTTagCompound.setTag("fcMin", this.newDoubleNBTList(this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ));
         par1NBTTagCompound.setTag("fcMax", this.newDoubleNBTList(this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ));
         par1NBTTagCompound.setTag("Motion", this.newDoubleNBTList(this.motionX, this.motionY, this.motionZ));
         par1NBTTagCompound.setTag("Rotation", this.newFloatNBTList(this.rotationYaw, this.rotationPitch));
         par1NBTTagCompound.setFloat("FallDistance", this.fallDistance);
         par1NBTTagCompound.setShort("Fire", (short)this.fire);
         par1NBTTagCompound.setShort("Air", (short)this.getAir());
         par1NBTTagCompound.setBoolean("OnGround", this.onGround);
         par1NBTTagCompound.setInteger("Dimension", this.dimension);
         par1NBTTagCompound.setBoolean("Invulnerable", this.invulnerable);
         par1NBTTagCompound.setInteger("PortalCooldown", this.timeUntilPortal);
         par1NBTTagCompound.setLong("UUIDMost", this.entityUniqueID.getMostSignificantBits());
         par1NBTTagCompound.setLong("UUIDLeast", this.entityUniqueID.getLeastSignificantBits());
         this.writeEntityToNBT(par1NBTTagCompound);
         if (this.ridingEntity != null) {
            NBTTagCompound var2 = new NBTTagCompound("Riding");
            if (this.ridingEntity.addNotRiddenEntityID(var2)) {
               par1NBTTagCompound.setTag("Riding", var2);
            }
         }
      } catch (Throwable var51) {
         CrashReport var3 = CrashReport.makeCrashReport(var51, "Saving entity NBT");
         CrashReportCategory var4 = var3.makeCategory("Entity being saved");
         this.func_85029_a(var4);
         throw new ReportedException(var3);
      }
   }

   protected final String getEntityString() {
      return EntityList.getEntityString(this);
   }

   protected abstract void readEntityFromNBT(NBTTagCompound var1);

   protected abstract void writeEntityToNBT(NBTTagCompound var1);

   protected NBTTagList newDoubleNBTList(double... par1ArrayOfDouble) {
      NBTTagList var2 = new NBTTagList();

      for (double var6 : par1ArrayOfDouble) {
         var2.appendTag(new NBTTagDouble((String)null, var6));
      }

      return var2;
   }

   protected NBTTagList newFloatNBTList(float... par1ArrayOfFloat) {
      NBTTagList var2 = new NBTTagList();

      for (float var6 : par1ArrayOfFloat) {
         var2.appendTag(new NBTTagFloat((String)null, var6));
      }

      return var2;
   }

   public float getShadowSize() {
      return this.height / 2.0F;
   }

   public EntityItem dropItem(int par1, int par2) {
      return this.dropItemWithOffset(par1, par2, 0.0F);
   }

   public EntityItem dropItemWithOffset(int par1, int par2, float par3) {
      return this.entityDropItem(new ItemStack(par1, par2, 0), par3);
   }

   public EntityItem entityDropItem(ItemStack par1ItemStack, float par2) {
      EntityItem var3 = (EntityItem)EntityList.createEntityOfType(EntityItem.class, this.worldObj, this.posX, this.posY + par2, this.posZ, par1ItemStack);
      var3.delayBeforeCanPickup = 10;
      this.worldObj.spawnEntityInWorld(var3);
      return var3;
   }

   public boolean isEntityAlive() {
      return !this.isDead;
   }

   public boolean isEntityInsideOpaqueBlock() {
      for (int var1 = 0; var1 < 8; var1++) {
         float var2 = ((var1 >> 0) % 2 - 0.5F) * this.width * 0.8F;
         float var3 = ((var1 >> 1) % 2 - 0.5F) * 0.1F;
         float var4 = ((var1 >> 2) % 2 - 0.5F) * this.width * 0.8F;
         int var5 = MathHelper.floor_double(this.posX + var2);
         int var6 = MathHelper.floor_double(this.posY + this.getEyeHeight() + var3);
         int var7 = MathHelper.floor_double(this.posZ + var4);
         if (this.worldObj.isBlockNormalCube(var5, var6, var7)) {
            return true;
         }
      }

      return false;
   }

   public boolean interact(EntityPlayer par1EntityPlayer) {
      return false;
   }

   public AxisAlignedBB getCollisionBox(Entity par1Entity) {
      return null;
   }

   public void updateRidden() {
      if (this.ridingEntity.isDead) {
         this.ridingEntity = null;
      } else {
         this.motionX = 0.0;
         this.motionY = 0.0;
         this.motionZ = 0.0;
         this.onUpdate();
         if (this.ridingEntity != null) {
            this.ridingEntity.updateRiderPosition();
            this.entityRiderYawDelta = this.entityRiderYawDelta + (this.ridingEntity.rotationYaw - this.ridingEntity.prevRotationYaw);
            this.entityRiderPitchDelta = this.entityRiderPitchDelta + (this.ridingEntity.rotationPitch - this.ridingEntity.prevRotationPitch);

            while (this.entityRiderYawDelta >= 180.0) {
               this.entityRiderYawDelta -= 360.0;
            }

            while (this.entityRiderYawDelta < -180.0) {
               this.entityRiderYawDelta += 360.0;
            }

            while (this.entityRiderPitchDelta >= 180.0) {
               this.entityRiderPitchDelta -= 360.0;
            }

            while (this.entityRiderPitchDelta < -180.0) {
               this.entityRiderPitchDelta += 360.0;
            }

            double var1 = this.entityRiderYawDelta * 0.5;
            double var3 = this.entityRiderPitchDelta * 0.5;
            float var5 = 10.0F;
            if (var1 > var5) {
               var1 = var5;
            }

            if (var1 < -var5) {
               var1 = -var5;
            }

            if (var3 > var5) {
               var3 = var5;
            }

            if (var3 < -var5) {
               var3 = -var5;
            }

            this.entityRiderYawDelta -= var1;
            this.entityRiderPitchDelta -= var3;
            this.rotationYaw = (float)(this.rotationYaw + var1);
            this.rotationPitch = (float)(this.rotationPitch + var3);
         }
      }
   }

   public void updateRiderPosition() {
      if (this.riddenByEntity != null) {
         if (!(this.riddenByEntity instanceof EntityPlayer) || !((EntityPlayer)this.riddenByEntity).func_71066_bF()) {
            this.riddenByEntity.lastTickPosX = this.lastTickPosX;
            this.riddenByEntity.lastTickPosY = this.lastTickPosY + this.getMountedYOffset() + this.riddenByEntity.getYOffset();
            this.riddenByEntity.lastTickPosZ = this.lastTickPosZ;
         }

         this.riddenByEntity.setPosition(this.posX, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ);
      }
   }

   public double getYOffset() {
      return this.yOffset;
   }

   public double getMountedYOffset() {
      return this.height * 0.75;
   }

   public void mountEntity(Entity par1Entity) {
      this.entityRiderPitchDelta = 0.0;
      this.entityRiderYawDelta = 0.0;
      if (par1Entity == null) {
         if (this.ridingEntity != null) {
            this.setLocationAndAngles(
               this.ridingEntity.posX,
               this.ridingEntity.boundingBox.minY + this.ridingEntity.height,
               this.ridingEntity.posZ,
               this.rotationYaw,
               this.rotationPitch
            );
            this.ridingEntity.riddenByEntity = null;
         }

         this.ridingEntity = null;
      } else {
         if (this.ridingEntity != null) {
            this.ridingEntity.riddenByEntity = null;
         }

         this.ridingEntity = par1Entity;
         par1Entity.riddenByEntity = this;
      }
   }

   public void unmountEntity(Entity par1Entity) {
      double var3 = this.posX;
      double var5 = this.posY;
      double var7 = this.posZ;
      if (par1Entity != null) {
         var3 = par1Entity.posX;
         var5 = par1Entity.boundingBox.minY + par1Entity.height;
         var7 = par1Entity.posZ;
      }

      for (double var9 = -1.5; var9 < 2.0; var9++) {
         for (double var11 = -1.5; var11 < 2.0; var11++) {
            if (var9 != 0.0 || var11 != 0.0) {
               int var13 = (int)(this.posX + var9);
               int var14 = (int)(this.posZ + var11);
               AxisAlignedBB var2 = this.boundingBox.getOffsetBoundingBox(var9, 1.0, var11);
               if (this.worldObj.getCollidingBlockBounds(var2).isEmpty()) {
                  if (this.worldObj.doesBlockHaveSolidTopSurface(var13, (int)this.posY, var14)) {
                     this.setLocationAndAngles(this.posX + var9, this.posY + 1.0, this.posZ + var11, this.rotationYaw, this.rotationPitch);
                     return;
                  }

                  if (this.worldObj.doesBlockHaveSolidTopSurface(var13, (int)this.posY - 1, var14)
                     || this.worldObj.getBlockMaterial(var13, (int)this.posY - 1, var14) == Material.water) {
                     var3 = this.posX + var9;
                     var5 = this.posY + 1.0;
                     var7 = this.posZ + var11;
                  }
               }
            }
         }
      }

      this.setLocationAndAngles(var3, var5, var7, this.rotationYaw, this.rotationPitch);
   }

   @Environment(EnvType.CLIENT)
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.setPosition(par1, par3, par5);
      this.setRotation(par7, par8);
      List var10 = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.contract(0.03125, 0.0, 0.03125));
      if (!var10.isEmpty()) {
         double var11 = 0.0;

         for (int var13 = 0; var13 < var10.size(); var13++) {
            AxisAlignedBB var14 = (AxisAlignedBB)var10.get(var13);
            if (var14.maxY > var11) {
               var11 = var14.maxY;
            }
         }

         par3 += var11 - this.boundingBox.minY;
         this.setPosition(par1, par3, par5);
      }
   }

   public float getCollisionBorderSize() {
      return 0.1F;
   }

   public Vec3 getLookVec() {
      return null;
   }

   public void setInPortal() {
      if (this.timeUntilPortal > 0) {
         this.timeUntilPortal = this.getPortalCooldown();
      } else {
         double var1 = this.prevPosX - this.posX;
         double var3 = this.prevPosZ - this.posZ;
         if (!this.worldObj.isRemote && !this.inPortal) {
            this.teleportDirection = Direction.getMovementDirection(var1, var3);
         }

         this.inPortal = true;
      }
   }

   public int getPortalCooldown() {
      return 900;
   }

   public void setVelocity(double par1, double par3, double par5) {
      this.motionX = par1;
      this.motionY = par3;
      this.motionZ = par5;
   }

   @Environment(EnvType.CLIENT)
   public void handleHealthUpdate(byte par1) {
   }

   @Environment(EnvType.CLIENT)
   public void performHurtAnimation() {
   }

   @Environment(EnvType.CLIENT)
   public void updateCloak() {
   }

   public ItemStack[] getLastActiveItems() {
      return null;
   }

   public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
   }

   public boolean isBurning() {
      return this.fire > 0 || this.getFlag(0);
   }

   public boolean isRiding() {
      return this.ridingEntity != null || this.getFlag(2);
   }

   public boolean isSneaking() {
      return this.getFlag(1);
   }

   public void setSneaking(boolean par1) {
      this.setFlag(1, par1);
   }

   public boolean isUsingSpecialKey() {
      return false;
   }

   public boolean isSprinting() {
      return this.getFlag(3);
   }

   public void setSprinting(boolean par1) {
      this.setFlag(3, par1);
   }

   public boolean isInvisible() {
      return this.getFlag(5);
   }

   @Environment(EnvType.CLIENT)
   public boolean func_98034_c(EntityPlayer par1EntityPlayer) {
      return this.isInvisible();
   }

   public void setInvisible(boolean par1) {
      this.setFlag(5, par1);
   }

   @Environment(EnvType.CLIENT)
   public boolean isEating() {
      return this.getFlag(4);
   }

   public void setEating(boolean par1) {
      this.setFlag(4, par1);
   }

   protected boolean getFlag(int par1) {
      return (this.dataWatcher.getWatchableObjectByte(0) & 1 << par1) != 0;
   }

   protected void setFlag(int par1, boolean par2) {
      byte var3 = this.dataWatcher.getWatchableObjectByte(0);
      if (par2) {
         this.dataWatcher.updateObject(0, (byte)(var3 | 1 << par1));
      } else {
         this.dataWatcher.updateObject(0, (byte)(var3 & ~(1 << par1)));
      }
   }

   public int getAir() {
      return this.dataWatcher.getWatchableObjectShort(1);
   }

   public void setAir(int par1) {
      this.dataWatcher.updateObject(1, (short)par1);
   }

   public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
      this.dealFireDamage(5);
      this.fire++;
      if (this.fire == 0) {
         this.setFire(8);
      }
   }

   public void onKillEntity(EntityLiving par1EntityLiving) {
   }

   protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      double var10 = par1 - var7;
      double var12 = par3 - var8;
      double var14 = par5 - var9;
      List var16 = this.worldObj.getCollidingBlockBounds(this.boundingBox);
      if (var16.isEmpty() && !this.worldObj.func_85174_u(var7, var8, var9)) {
         return false;
      } else {
         boolean var17 = !this.worldObj.func_85174_u(var7 - 1, var8, var9);
         boolean var18 = !this.worldObj.func_85174_u(var7 + 1, var8, var9);
         boolean var19 = !this.worldObj.func_85174_u(var7, var8 - 1, var9);
         boolean var20 = !this.worldObj.func_85174_u(var7, var8 + 1, var9);
         boolean var21 = !this.worldObj.func_85174_u(var7, var8, var9 - 1);
         boolean var22 = !this.worldObj.func_85174_u(var7, var8, var9 + 1);
         byte var23 = 3;
         double var24 = 9999.0;
         if (var17 && var10 < var24) {
            var24 = var10;
            var23 = 0;
         }

         if (var18 && 1.0 - var10 < var24) {
            var24 = 1.0 - var10;
            var23 = 1;
         }

         if (var20 && 1.0 - var12 < var24) {
            var24 = 1.0 - var12;
            var23 = 3;
         }

         if (var21 && var14 < var24) {
            var24 = var14;
            var23 = 4;
         }

         if (var22 && 1.0 - var14 < var24) {
            var24 = 1.0 - var14;
            var23 = 5;
         }

         float var26 = this.rand.nextFloat() * 0.2F + 0.1F;
         if (var23 == 0) {
            this.motionX = -var26;
         }

         if (var23 == 1) {
            this.motionX = var26;
         }

         if (var23 == 2) {
            this.motionY = -var26;
         }

         if (var23 == 3) {
            this.motionY = var26;
         }

         if (var23 == 4) {
            this.motionZ = -var26;
         }

         if (var23 == 5) {
            this.motionZ = var26;
         }

         return true;
      }
   }

   public void setInWeb() {
      this.isInWeb = true;
      this.fallDistance = 0.0F;
   }

   public String getEntityName() {
      String var1 = EntityList.getEntityString(this);
      if (var1 == null) {
         var1 = "generic";
      }

      return StatCollector.translateToLocal("entity." + var1 + ".name");
   }

   public Entity[] getParts() {
      return null;
   }

   public boolean isEntityEqual(Entity par1Entity) {
      return this == par1Entity;
   }

   public float getRotationYawHead() {
      return 0.0F;
   }

   @Environment(EnvType.CLIENT)
   public void setRotationYawHead(float par1) {
   }

   public boolean canAttackWithItem() {
      return true;
   }

   public boolean func_85031_j(Entity par1Entity) {
      return false;
   }

   @Override
   public String toString() {
      return String.format(
         "%s['%s'/%d, l='%s', x=%.2f, y=%.2f, z=%.2f]",
         this.getClass().getSimpleName(),
         this.getEntityName(),
         this.entityId,
         this.worldObj == null ? "~NULL~" : this.worldObj.getWorldInfo().getWorldName(),
         this.posX,
         this.posY,
         this.posZ
      );
   }

   public boolean isEntityInvulnerable() {
      return this.invulnerable;
   }

   public void func_82149_j(Entity par1Entity) {
      this.setLocationAndAngles(par1Entity.posX, par1Entity.posY, par1Entity.posZ, par1Entity.rotationYaw, par1Entity.rotationPitch);
   }

   public void copyDataFrom(Entity par1Entity, boolean par2) {
      NBTTagCompound var3 = new NBTTagCompound();
      par1Entity.writeToNBT(var3);
      this.readFromNBT(var3);
      this.timeUntilPortal = par1Entity.timeUntilPortal;
      this.teleportDirection = par1Entity.teleportDirection;
   }

   public void travelToDimension(int par1) {
      if (!this.worldObj.isRemote && !this.isDead) {
         this.worldObj.theProfiler.startSection("changeDimension");
         MinecraftServer var2 = MinecraftServer.getServer();
         int var3 = this.dimension;
         WorldServer var4 = var2.worldServerForDimension(var3);
         WorldServer var5 = var2.worldServerForDimension(par1);
         this.dimension = par1;
         this.worldObj.removeEntity(this);
         this.isDead = false;
         this.worldObj.theProfiler.startSection("reposition");
         var2.getConfigurationManager().transferEntityToWorld(this, var3, var4, var5);
         this.worldObj.theProfiler.endStartSection("reloading");
         Entity var6 = EntityList.createEntityByName(EntityList.getEntityString(this), var5);
         if (var6 != null) {
            var6.copyDataFrom(this, true);
            var5.d(var6);
         }

         this.isDead = true;
         this.worldObj.theProfiler.endSection();
         var4.resetUpdateEntityTick();
         var5.resetUpdateEntityTick();
         this.worldObj.theProfiler.endSection();
      }
   }

   public float func_82146_a(Explosion par1Explosion, World par2World, int par3, int par4, int par5, Block par6Block) {
      return par6Block.getExplosionResistance(this, par2World, par3, par4, par5);
   }

   public boolean func_96091_a(Explosion par1Explosion, World par2World, int par3, int par4, int par5, int par6, float par7) {
      return true;
   }

   public int func_82143_as() {
      return 3;
   }

   public int getTeleportDirection() {
      return this.teleportDirection;
   }

   public boolean doesEntityNotTriggerPressurePlate() {
      return false;
   }

   public void func_85029_a(CrashReportCategory par1CrashReportCategory) {
      par1CrashReportCategory.addCrashSectionCallable("Entity Type", new CallableEntityType(this));
      par1CrashReportCategory.addCrashSection("Entity ID", this.entityId);
      par1CrashReportCategory.addCrashSectionCallable("Entity Name", new CallableEntityName(this));
      par1CrashReportCategory.addCrashSection("Entity's Exact location", String.format("%.2f, %.2f, %.2f", this.posX, this.posY, this.posZ));
      par1CrashReportCategory.addCrashSection(
         "Entity's Block location",
         CrashReportCategory.getLocationInfo(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
      );
      par1CrashReportCategory.addCrashSection("Entity's Momentum", String.format("%.2f, %.2f, %.2f", this.motionX, this.motionY, this.motionZ));
   }

   @Environment(EnvType.CLIENT)
   public boolean canRenderOnFire() {
      return this.isBurning();
   }

   public boolean func_96092_aw() {
      return true;
   }

   public String getTranslatedEntityName() {
      return this.getEntityName();
   }

   public boolean isAffectedByMovementModifiers() {
      return true;
   }

   public void notifyOfWolfHowl(Entity sourceEntity) {
   }

   protected boolean shouldSetPositionOnLoad() {
      return true;
   }

   public boolean canCollideWithEntity(Entity entity) {
      return true;
   }

   public boolean isItemEntity() {
      return false;
   }

   public boolean canEntityTriggerTripwire() {
      return true;
   }

   public AxisAlignedBB getVisualBoundingBox() {
      return this.boundingBox;
   }

   public boolean isSecondaryTargetForSquid() {
      return false;
   }

   public boolean getCanBeHeadCrabbed(boolean bSquidInWater) {
      return this.isEntityAlive() && this.riddenByEntity == null && this.ridingEntity == null && this.isSecondaryTargetForSquid();
   }

   public boolean isValidOngoingAttackTargetForSquid() {
      return this.isSecondaryTargetForSquid() && this.isEntityAlive();
   }

   public void onFlungBySquidTentacle(SquidEntity squid) {
   }

   public void onHeadCrabbedBySquid(SquidEntity squid) {
   }

   public boolean hasHeadCrabbedSquid() {
      return this.riddenByEntity != null && this.riddenByEntity instanceof SquidEntity;
   }

   public Entity getHeadCrabSharedAttackTarget() {
      return null;
   }

   public boolean isImmuneToHeadCrabDamage() {
      return false;
   }

   public void onKickedByCow(CowEntity cow) {
      this.flingAwayFromEntity(cow, this.getCowKickMovementMultiplier());
   }

   protected double getCowKickMovementMultiplier() {
      return 1.0;
   }

   public void flingAwayFromEntity(Entity repulsingEntity, double dForceMultiplier) {
      if (this.ridingEntity != null) {
         this.mountEntity(null);
      }

      double dVelocityX = this.motionX;
      double dVelocityZ = this.motionZ;
      double dDeltaX = this.posX - repulsingEntity.posX;
      double dDeltaZ = this.posZ - repulsingEntity.posZ;
      double dFlatDistToTargetSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
      if (dFlatDistToTargetSq > 0.1) {
         double dFlatDistToTarget = MathHelper.sqrt_double(dFlatDistToTargetSq);
         dVelocityX += dDeltaX / dFlatDistToTarget * 0.5 * dForceMultiplier;
         dVelocityZ += dDeltaZ / dFlatDistToTarget * 0.5 * dForceMultiplier;
      }

      this.isAirBorne = true;
      double dVelocityY = this.motionY + 0.25 * dForceMultiplier;
      dVelocityX *= this.rand.nextDouble() * 0.2 + 0.9;
      dVelocityZ *= this.rand.nextDouble() * 0.2 + 0.9;
      this.motionX = MathUtils.clampDouble(dVelocityX, -1.0, 1.0);
      this.motionY = MathUtils.clampDoubleTop(dVelocityY, 0.75);
      this.motionZ = MathUtils.clampDouble(dVelocityZ, -1.0, 1.0);
   }

   public boolean doesEntityApplyToSpawnCap() {
      return false;
   }

   public void outOfUpdateRangeUpdate() {
   }

   public boolean appliesConstantForceWhenRidingBoat() {
      return false;
   }

   public double movementModifierWhenRidingBoat() {
      return 1.0;
   }

   public boolean onPossessedRidingEntityDeath() {
      return false;
   }

   public boolean isBeingRainedOn() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.boundingBox.maxY);
      int k = MathHelper.floor_double(this.posZ);
      return this.worldObj.isRainingAtPos(i, j, k);
   }

   public boolean doesEntityApplyToSquidPossessionCap() {
      return false;
   }

   public boolean isValidZombieSecondaryTarget(EntityZombie zombie) {
      return false;
   }

   public boolean attractsLightning() {
      return false;
   }

   public void onStruckByLightning(LightningBoltEntity boltEntity) {
      this.dealFireDamage(7);
      this.fire++;
      if (this.fire == 0) {
         this.setFire(8);
      }

      if (!this.isEntityInvulnerable()) {
         this.flingAwayFromEntity(boltEntity, 2.0);
      }
   }

   public void moveEntity(double dMoveX, double dMoveY, double dMoveZ) {
      if (this.noClip) {
         this.boundingBox.offset(dMoveX, dMoveY, dMoveZ);
         this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
         this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
         this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
      } else {
         this.worldObj.theProfiler.startSection("move");
         this.ySize *= 0.4F;
         double dOldPosX = this.posX;
         double dOldPosY = this.posY;
         double dOldPosZ = this.posZ;
         AxisAlignedBB oldBoundingBox = this.boundingBox.copy();
         if (this.isInWeb) {
            this.isInWeb = false;
            dMoveX *= 0.25;
            dMoveY *= 0.05F;
            dMoveZ *= 0.25;
            this.motionX = 0.0;
            this.motionY = 0.0;
            this.motionZ = 0.0;
         }

         boolean bIsSneakingPlayer = this.onGround && this.isSneaking() && this instanceof EntityPlayer;
         if (bIsSneakingPlayer) {
            double dStepSize = 0.05;

            while (dMoveX != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(dMoveX, -1.0, 0.0)).isEmpty()) {
               if (dMoveX < dStepSize && dMoveX >= -dStepSize) {
                  dMoveX = 0.0;
               } else if (dMoveX > 0.0) {
                  dMoveX -= dStepSize;
               } else {
                  dMoveX += dStepSize;
               }
            }

            while (dMoveZ != 0.0 && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(0.0, -1.0, dMoveZ)).isEmpty()) {
               if (dMoveZ < dStepSize && dMoveZ >= -dStepSize) {
                  dMoveZ = 0.0;
               } else if (dMoveZ > 0.0) {
                  dMoveZ -= dStepSize;
               } else {
                  dMoveZ += dStepSize;
               }
            }

            while (
               dMoveX != 0.0
                  && dMoveZ != 0.0
                  && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.getOffsetBoundingBox(dMoveX, -1.0, dMoveZ)).isEmpty()
            ) {
               if (dMoveX < dStepSize && dMoveX >= -dStepSize) {
                  dMoveX = 0.0;
               } else if (dMoveX > 0.0) {
                  dMoveX -= dStepSize;
               } else {
                  dMoveX += dStepSize;
               }

               if (dMoveZ < dStepSize && dMoveZ >= -dStepSize) {
                  dMoveZ = 0.0;
               } else if (dMoveZ > 0.0) {
                  dMoveZ -= dStepSize;
               } else {
                  dMoveZ += dStepSize;
               }
            }
         }

         double dUnboundedMoveX = dMoveX;
         double dUnboundedMoveY = dMoveY;
         double dUnboundedMoveZ = dMoveZ;
         AxisAlignedBB moveRangeBoundingBox = this.boundingBox.addCoord(dMoveX, dMoveY, dMoveZ);
         if (this.stepHeight > dMoveY) {
            moveRangeBoundingBox.maxY = this.boundingBox.maxY + this.stepHeight;
         }

         List<AxisAlignedBB> moveRangeCollisionList = this.worldObj.getCollidingBoundingBoxes(this, moveRangeBoundingBox);

         for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
            dMoveY = moveRangeCollisionList.get(iTempIndex).calculateYOffset(this.boundingBox, dMoveY);
         }

         this.boundingBox.offset(0.0, dMoveY, 0.0);
         boolean bVerticallySupported = this.onGround || dUnboundedMoveY != dMoveY && dUnboundedMoveY < 0.0;

         for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
            dMoveX = moveRangeCollisionList.get(iTempIndex).calculateXOffset(this.boundingBox, dMoveX);
         }

         this.boundingBox.offset(dMoveX, 0.0, 0.0);

         for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
            dMoveZ = moveRangeCollisionList.get(iTempIndex).calculateZOffset(this.boundingBox, dMoveZ);
         }

         this.boundingBox.offset(0.0, 0.0, dMoveZ);
         if (this.stepHeight > 0.0F
            && bVerticallySupported
            && (bIsSneakingPlayer || this.ySize < 0.05F)
            && (dUnboundedMoveX != dMoveX || dUnboundedMoveZ != dMoveZ)) {
            double dBoundedMoveX = dMoveX;
            double dBoundedMoveY = dMoveY;
            double dBoundedMoveZ = dMoveZ;
            dMoveX = dUnboundedMoveX;
            dMoveY = this.stepHeight;
            dMoveZ = dUnboundedMoveZ;
            AxisAlignedBB dBoundedMoveBox = this.boundingBox.copy();
            this.boundingBox.setBB(oldBoundingBox);

            for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
               dMoveY = moveRangeCollisionList.get(iTempIndex).calculateYOffset(this.boundingBox, dMoveY);
            }

            this.boundingBox.offset(0.0, dMoveY, 0.0);

            for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
               dMoveX = moveRangeCollisionList.get(iTempIndex).calculateXOffset(this.boundingBox, dMoveX);
            }

            this.boundingBox.offset(dMoveX, 0.0, 0.0);

            for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
               dMoveZ = moveRangeCollisionList.get(iTempIndex).calculateZOffset(this.boundingBox, dMoveZ);
            }

            this.boundingBox.offset(0.0, 0.0, dMoveZ);
            if (dMoveY > 0.0) {
               dMoveY = -dMoveY;

               for (int iTempIndex = 0; iTempIndex < moveRangeCollisionList.size(); iTempIndex++) {
                  dMoveY = moveRangeCollisionList.get(iTempIndex).calculateYOffset(this.boundingBox, dMoveY);
               }

               this.boundingBox.offset(0.0, dMoveY, 0.0);
            }

            if (dBoundedMoveX * dBoundedMoveX + dBoundedMoveZ * dBoundedMoveZ >= dMoveX * dMoveX + dMoveZ * dMoveZ) {
               dMoveX = dBoundedMoveX;
               dMoveY = dBoundedMoveY;
               dMoveZ = dBoundedMoveZ;
               this.boundingBox.setBB(dBoundedMoveBox);
            }
         }

         this.worldObj.theProfiler.endSection();
         this.worldObj.theProfiler.startSection("rest");
         this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0;
         this.posY = this.boundingBox.minY + this.yOffset - this.ySize;
         this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0;
         this.isCollidedHorizontally = dUnboundedMoveX != dMoveX || dUnboundedMoveZ != dMoveZ;
         this.isCollidedVertically = dUnboundedMoveY != dMoveY;
         this.onGround = dUnboundedMoveY != dMoveY && dUnboundedMoveY < 0.0;
         this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
         this.updateFallState(dMoveY, this.onGround);
         if (dUnboundedMoveX != dMoveX) {
            this.motionX = 0.0;
         }

         if (dUnboundedMoveY != dMoveY) {
            this.motionY = 0.0;
         }

         if (dUnboundedMoveZ != dMoveZ) {
            this.motionZ = 0.0;
         }

         double dDeltaX = this.posX - dOldPosX;
         double dDeltaY = this.posY - dOldPosY;
         double dDeltaZ = this.posZ - dOldPosZ;
         if (this.canTriggerWalking() && !bIsSneakingPlayer && this.ridingEntity == null) {
            int iGroundI = MathHelper.floor_double(this.posX);
            int iGroundJ = MathHelper.floor_double(this.posY - 0.03 - this.yOffset);
            int iGroundK = MathHelper.floor_double(this.posZ);
            int iGroundBlockID = this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK);
            int iTempY = iGroundJ;
            if (iGroundBlockID == 0) {
               int iGroundRenderType = this.worldObj.blockGetRenderType(iGroundI, iGroundJ - 1, iGroundK);
               if (iGroundRenderType == 11 || iGroundRenderType == 32 || iGroundRenderType == 21) {
                  iGroundBlockID = this.worldObj.getBlockId(iGroundI, iGroundJ - 1, iGroundK);
                  iTempY = iGroundJ - 1;
               }
            }

            Block inBlock = Block.blocksList[iGroundBlockID];
            if (inBlock == null || !inBlock.isBlockClimbable(this.worldObj, iGroundI, iTempY, iGroundK)) {
               dDeltaY = 0.0;
            }

            this.distanceWalkedModified = this.distanceWalkedModified + MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaZ * dDeltaZ) * 0.6F;
            this.distanceWalkedOnStepModified = this.distanceWalkedOnStepModified
               + MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ) * 0.6F;
            if (this.distanceWalkedOnStepModified > this.nextStepDistance && iGroundBlockID > 0) {
               this.nextStepDistance = (int)this.distanceWalkedOnStepModified + 1;
               if (this.isInWater()) {
                  float fSwimSoundVolume = MathHelper.sqrt_double(
                        this.motionX * this.motionX * 0.2F + this.motionY * this.motionY + this.motionZ * this.motionZ * 0.2F
                     )
                     * 0.35F;
                  if (fSwimSoundVolume > 1.0F) {
                     fSwimSoundVolume = 1.0F;
                  }

                  this.playSound("liquid.swim", fSwimSoundVolume, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
               }

               this.playStepSound(iGroundI, iGroundJ, iGroundK, iGroundBlockID);
            }

            if (this.distanceWalkedOnStepModified > this.nextBlockCheckDistance && iGroundBlockID > 0) {
               this.nextBlockCheckDistance = this.distanceWalkedOnStepModified + 0.5F;
               if (this.lastGroundPosX != iGroundI || this.lastGroundPosY != iGroundJ || this.lastGroundPosZ != iGroundK) {
                  this.lastGroundPosX = iGroundI;
                  this.lastGroundPosY = iGroundJ;
                  this.lastGroundPosZ = iGroundK;
                  int feetBlockID = this.worldObj.getBlockId(iGroundI, iGroundJ + 1, iGroundK);
                  if (feetBlockID > 0) {
                     if (Block.blocksList[feetBlockID].groundCoverRestingOnVisualOffset(this.worldObj, iGroundI, iGroundJ + 1, iGroundK) < 0.0F) {
                        int groundCoverBlockID = this.worldObj.getBlockId(iGroundI, iGroundJ + 2, iGroundK);
                        if (groundCoverBlockID > 0) {
                           Block.blocksList[groundCoverBlockID].onEntityStepsIn(this.worldObj, iGroundI, iGroundJ + 2, iGroundK, this);
                        }
                     }

                     Block.blocksList[feetBlockID].onEntityStepsIn(this.worldObj, iGroundI, iGroundJ + 1, iGroundK, this);
                  }

                  Block.blocksList[iGroundBlockID].onEntityWalking(this.worldObj, iGroundI, iGroundJ, iGroundK, this);
               }
            }
         }

         this.doBlockCollisions();
         boolean bWet = this.isWet();
         if (this.worldObj.isBoundingBoxBurning(this)) {
            this.dealFireDamage(1);
            if (!bWet) {
               this.fire++;
               if (this.fire == 0) {
                  this.setFire(8);
               }
            }
         } else if (this.fire <= 0) {
            this.fire = -this.fireResistance;
         }

         if (bWet && this.fire > 0) {
            this.playSound("random.fizz", 0.7F, 1.6F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
            this.fire = -this.fireResistance;
         }

         this.worldObj.theProfiler.endSection();
      }
   }

   public void readFromNBT(NBTTagCompound tag) {
      try {
         boolean bHasBoundingData = false;
         NBTTagList posTag = tag.getTagList("Pos");
         this.prevPosX = this.lastTickPosX = this.posX = ((NBTTagDouble)posTag.tagAt(0)).data;
         this.prevPosY = this.lastTickPosY = this.posY = ((NBTTagDouble)posTag.tagAt(1)).data;
         this.prevPosZ = this.lastTickPosZ = this.posZ = ((NBTTagDouble)posTag.tagAt(2)).data;
         if (tag.hasKey("fcMin")) {
            bHasBoundingData = true;
            NBTTagList minTag = tag.getTagList("fcMin");
            double dMinX = ((NBTTagDouble)minTag.tagAt(0)).data;
            double dMinY = ((NBTTagDouble)minTag.tagAt(1)).data;
            double dMinZ = ((NBTTagDouble)minTag.tagAt(2)).data;
            NBTTagList maxTag = tag.getTagList("fcMax");
            double dMaxX = ((NBTTagDouble)maxTag.tagAt(0)).data;
            double dMaxY = ((NBTTagDouble)maxTag.tagAt(1)).data;
            double dMaxZ = ((NBTTagDouble)maxTag.tagAt(2)).data;
            this.boundingBox.setBounds(dMinX, dMinY, dMinZ, dMaxX, dMaxY, dMaxZ);
         } else {
            this.setPosition(this.posX, this.posY, this.posZ);
         }

         NBTTagList motionTag = tag.getTagList("Motion");
         this.motionX = ((NBTTagDouble)motionTag.tagAt(0)).data;
         this.motionY = ((NBTTagDouble)motionTag.tagAt(1)).data;
         this.motionZ = ((NBTTagDouble)motionTag.tagAt(2)).data;
         if (Math.abs(this.motionX) > 10.0) {
            this.motionX = 0.0;
         }

         if (Math.abs(this.motionY) > 10.0) {
            this.motionY = 0.0;
         }

         if (Math.abs(this.motionZ) > 10.0) {
            this.motionZ = 0.0;
         }

         NBTTagList rotationTag = tag.getTagList("Rotation");
         this.prevRotationYaw = this.rotationYaw = ((NBTTagFloat)rotationTag.tagAt(0)).data;
         this.prevRotationPitch = this.rotationPitch = ((NBTTagFloat)rotationTag.tagAt(1)).data;
         this.fallDistance = tag.getFloat("FallDistance");
         this.fire = tag.getShort("Fire");
         this.setAir(tag.getShort("Air"));
         this.onGround = tag.getBoolean("OnGround");
         this.dimension = tag.getInteger("Dimension");
         this.invulnerable = tag.getBoolean("Invulnerable");
         this.timeUntilPortal = tag.getInteger("PortalCooldown");
         if (tag.hasKey("UUIDMost") && tag.hasKey("UUIDLeast")) {
            this.entityUniqueID = new UUID(tag.getLong("UUIDMost"), tag.getLong("UUIDLeast"));
         }

         this.setRotation(this.rotationYaw, this.rotationPitch);
         this.readEntityFromNBT(tag);
         if (!bHasBoundingData && this.shouldSetPositionOnLoad()) {
            this.setPosition(this.posX, this.posY, this.posZ);
         }
      } catch (Throwable var18) {
         CrashReport var3 = CrashReport.makeCrashReport(var18, "Loading entity NBT");
         CrashReportCategory var4 = var3.makeCategory("Entity being loaded");
         this.func_85029_a(var4);
         throw new ReportedException(var3);
      }
   }

   public void mountEntityRemote(Entity entityToMount) {
      this.mountEntity(entityToMount);
   }

   public void flagAllWatchedObjectsDirty() {
      List watchList = this.dataWatcher.getAllWatched();
      if (watchList != null) {
         for (WatchableObject tempWatchable : watchList) {
            this.dataWatcher.setObjectWatched(tempWatchable.getDataValueId());
         }
      }
   }

   public boolean onBlockDispenserConsume(BlockDispenserBlock blockDispenser, BlockDispenserTileEntity tileEentityDispenser) {
      return false;
   }
}
