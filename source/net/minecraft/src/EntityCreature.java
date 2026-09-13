package net.minecraft.src;

import btw.world.util.WorldUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.util.List;

public abstract class EntityCreature extends EntityLiving {
   public PathEntity pathToEntity;
   public Entity entityToAttack;
   public boolean hasAttacked = false;
   public int fleeingTick = 0;
   private static final int IS_POSSESSED_DATA_WATCHER_ID = 24;
   protected int possessionTimer = -1;

   public EntityCreature(World par1World) {
      super(par1World);
   }

   protected boolean isMovementCeased() {
      return false;
   }

   @Override
   protected void updateEntityActionState() {
      this.worldObj.theProfiler.startSection("ai");
      if (this.fleeingTick > 0) {
         this.fleeingTick--;
      }

      this.hasAttacked = this.isMovementCeased();
      float var1 = 16.0F;
      if (this.entityToAttack == null) {
         this.entityToAttack = this.findPlayerToAttack();
         if (this.entityToAttack != null) {
            this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, var1, true, false, false, true);
         }
      } else if (this.entityToAttack.isEntityAlive()) {
         float var2 = this.entityToAttack.getDistanceToEntity(this);
         if (this.shouldContinueAttacking(var2)) {
            if (this.n(this.entityToAttack)) {
               this.attackEntity(this.entityToAttack, var2);
            }
         } else {
            this.entityToAttack = null;
         }
      } else {
         this.entityToAttack = null;
      }

      this.worldObj.theProfiler.endSection();
      if (this.hasAttacked || this.entityToAttack == null || this.pathToEntity != null && this.rand.nextInt(20) != 0) {
         if (!this.hasAttacked && (this.pathToEntity == null && this.rand.nextInt(180) == 0 || this.rand.nextInt(120) == 0 || this.fleeingTick > 0)) {
            this.updateWanderPath();
         }
      } else {
         this.pathToEntity = this.worldObj.getPathEntityToEntity(this, this.entityToAttack, var1, true, false, false, true);
      }

      int var21 = MathHelper.floor_double(this.boundingBox.minY + 0.5);
      boolean var3 = this.G();
      boolean var4 = this.I();
      this.rotationPitch = 0.0F;
      if (this.pathToEntity != null && this.rand.nextInt(100) != 0) {
         this.worldObj.theProfiler.startSection("followpath");
         Vec3 var5 = this.pathToEntity.getPosition(this);
         double var6 = this.width * 2.0F;

         while (var5 != null && var5.squareDistanceTo(this.posX, var5.yCoord, this.posZ) < var6 * var6) {
            this.pathToEntity.incrementPathIndex();
            if (this.pathToEntity.isFinished()) {
               var5 = null;
               this.pathToEntity = null;
            } else {
               var5 = this.pathToEntity.getPosition(this);
            }
         }

         this.isJumping = false;
         if (var5 != null) {
            double var8 = var5.xCoord - this.posX;
            double var10 = var5.zCoord - this.posZ;
            double var12 = var5.yCoord - var21;
            float var14 = (float)(Math.atan2(var10, var8) * 180.0 / Math.PI) - 90.0F;
            float var15 = MathHelper.wrapAngleTo180_float(var14 - this.rotationYaw);
            this.moveForward = this.moveSpeed;
            if (var15 > 30.0F) {
               var15 = 30.0F;
            }

            if (var15 < -30.0F) {
               var15 = -30.0F;
            }

            this.rotationYaw += var15;
            if (this.hasAttacked && this.entityToAttack != null) {
               double var16 = this.entityToAttack.posX - this.posX;
               double var18 = this.entityToAttack.posZ - this.posZ;
               float var20 = this.rotationYaw;
               this.rotationYaw = (float)(Math.atan2(var18, var16) * 180.0 / Math.PI) - 90.0F;
               var15 = (var20 - this.rotationYaw + 90.0F) * (float) Math.PI / 180.0F;
               this.moveStrafing = -MathHelper.sin(var15) * this.moveForward * 1.0F;
               this.moveForward = MathHelper.cos(var15) * this.moveForward * 1.0F;
            }

            if (var12 > 0.0) {
               this.isJumping = true;
            }
         }

         if (this.entityToAttack != null) {
            this.a(this.entityToAttack, 30.0F, 30.0F);
         }

         if (this.isCollidedHorizontally && !this.hasPath()) {
            this.isJumping = true;
         }

         if (this.rand.nextFloat() < 0.8F && (var3 || var4)) {
            this.isJumping = true;
         }

         this.worldObj.theProfiler.endSection();
         this.entityAge++;
         this.bn();
      } else {
         super.updateEntityActionState();
         this.pathToEntity = null;
      }
   }

   protected void updateWanderPath() {
      this.worldObj.theProfiler.startSection("stroll");
      boolean var1 = false;
      int var2 = -1;
      int var3 = -1;
      int var4 = -1;
      float var5 = -99999.0F;

      for (int var6 = 0; var6 < 10; var6++) {
         int var7 = MathHelper.floor_double(this.posX + this.rand.nextInt(13) - 6.0);
         int var8 = MathHelper.floor_double(this.posY + this.rand.nextInt(7) - 3.0);
         int var9 = MathHelper.floor_double(this.posZ + this.rand.nextInt(13) - 6.0);
         float var10 = this.getBlockPathWeight(var7, var8, var9);
         if (var10 > var5) {
            var5 = var10;
            var2 = var7;
            var3 = var8;
            var4 = var9;
            var1 = true;
         }
      }

      if (var1) {
         this.pathToEntity = this.worldObj.getEntityPathToXYZ(this, var2, var3, var4, 10.0F, true, false, false, true);
      }

      this.worldObj.theProfiler.endSection();
   }

   protected void attackEntity(Entity par1Entity, float par2) {
   }

   public float getBlockPathWeight(int par1, int par2, int par3) {
      return 0.0F;
   }

   protected Entity findPlayerToAttack() {
      return null;
   }

   @Override
   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.boundingBox.minY);
      int var3 = MathHelper.floor_double(this.posZ);
      return super.getCanSpawnHere() && this.getBlockPathWeight(var1, var2, var3) >= 0.0F;
   }

   public boolean hasPath() {
      return this.pathToEntity != null;
   }

   public void setPathToEntity(PathEntity par1PathEntity) {
      this.pathToEntity = par1PathEntity;
   }

   public Entity getEntityToAttack() {
      return this.entityToAttack;
   }

   public void setTarget(Entity par1Entity) {
      this.entityToAttack = par1Entity;
   }

   @Override
   public float getSpeedModifier() {
      float var1 = super.getSpeedModifier();
      if (this.fleeingTick > 0 && !this.bh()) {
         var1 *= 2.0F;
      }

      return var1;
   }

   @Override
   protected void entityInit() {
      this.entityCreatureEntityInit();
   }

   protected void entityCreatureEntityInit() {
      super.entityInit();
      if (this.getCanCreatureTypeBePossessed()) {
         this.dataWatcher.addObject(24, new Byte((byte)0));
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      if (this.getCanCreatureTypeBePossessed()) {
         tag.setInteger("fcPossessionTimer", this.possessionTimer);
         tag.setByte("fcPossessionLevel", (byte)this.getPossessionLevel());
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (this.getCanCreatureTypeBePossessed()) {
         if (tag.hasKey("fcPossessionTimer")) {
            this.possessionTimer = tag.getInteger("fcPossessionTimer");
            if (this.possessionTimer >= 0) {
               this.setPossessionLevel(1);
            } else {
               this.setPossessionLevel(0);
            }
         } else {
            this.possessionTimer = -1;
            this.setPossessionLevel(0);
         }

         if (tag.hasKey("fcPossessionLevel")) {
            this.setPossessionLevel(tag.getByte("fcPossessionLevel"));
         }
      }
   }

   @Override
   protected void modSpecificOnLivingUpdate() {
      super.modSpecificOnLivingUpdate();
      if (this.getCanCreatureTypeBePossessed()) {
         this.handlePossession();
      }
   }

   public boolean getCanCreatureTypeBePossessed() {
      return false;
   }

   public boolean getCanCreatureBePossessedFromDistance(boolean bPersistentSpirit) {
      return this.getCanCreatureTypeBePossessed() && this.R() && !this.isPossessed();
   }

   public boolean isPossessed() {
      return this.getCanCreatureTypeBePossessed() && this.dataWatcher.getWatchableObjectByte(24) != 0;
   }

   public boolean isFullyPossessed() {
      return this.getCanCreatureTypeBePossessed() && this.dataWatcher.getWatchableObjectByte(24) > 1;
   }

   public void setPossessionLevel(int iLevel) {
      if (this.getCanCreatureTypeBePossessed()) {
         byte byteValue = (byte)iLevel;
         this.dataWatcher.updateObject(24, byteValue);
      }
   }

   public int getPossessionLevel() {
      return this.getCanCreatureTypeBePossessed() ? this.dataWatcher.getWatchableObjectByte(24) : 0;
   }

   public int getInitialPossessionChance() {
      return 1000;
   }

   public int getTimeToFullPossession() {
      return 2400 + this.worldObj.rand.nextInt(2400);
   }

   public void onInitialPossession() {
      this.worldObj.playSoundAtEntity(this, this.bd(), this.ba(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.worldObj.playAuxSFX(2228, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
   }

   public void onFullPossession() {
   }

   public void initiatePossession() {
      this.setPossessionLevel(1);
      this.possessionTimer = this.getTimeToFullPossession();
      this.onInitialPossession();
   }

   protected void handlePossession() {
      if (this.worldObj.getWorldInfo().getGameType() != EnumGameType.CREATIVE) {
         if (!this.worldObj.isRemote) {
            if (!this.isPossessed()) {
               if (this.worldObj.provider.dimensionId == -1 && this.worldObj.rand.nextInt(this.getInitialPossessionChance()) == 0) {
                  this.initiatePossession();
               }
            } else if (!this.h_() && this.getPossessionLevel() == 1) {
               this.possessionTimer--;
               if (this.possessionTimer < 0) {
                  this.possessionTimer = 0;
               }

               if (this.possessionTimer == 0) {
                  this.setPossessionLevel(2);
                  this.onFullPossession();
               }
            }
         }
      }
   }

   public boolean attemptToPossessNearbyCreature(double dRange, boolean bPersistentSpirit) {
      for (EntityCreature tempCreature : this.worldObj.getEntitiesWithinAABB(EntityCreature.class, this.boundingBox.expand(dRange, dRange, dRange))) {
         if (tempCreature.getCanCreatureBePossessedFromDistance(bPersistentSpirit) && tempCreature != this) {
            tempCreature.initiatePossession();
            return true;
         }
      }

      return false;
   }

   public static int attemptToPossessCreaturesAroundBlock(World world, int i, int j, int k, int iPossessionCount, int iCubicRange) {
      AxisAlignedBB possessionBox = AxisAlignedBB.getAABBPool()
         .getAABB(i - iCubicRange, j - iCubicRange, k - iCubicRange, i + 1 + iCubicRange, j + 1 + iCubicRange, k + 1 + iCubicRange);
      List nearbyCreatures = world.getEntitiesWithinAABB(EntityCreature.class, possessionBox);
      Iterator creatureIterator = nearbyCreatures.iterator();

      while (creatureIterator.hasNext() && iPossessionCount > 0) {
         EntityCreature tempCreature = (EntityCreature)creatureIterator.next();
         if (tempCreature.getCanCreatureBePossessedFromDistance(false)) {
            tempCreature.initiatePossession();
            iPossessionCount--;
         }
      }

      return iPossessionCount;
   }

   protected void attemptToPossessNearbyCreatureOnDeath() {
      this.attemptToPossessNearbyCreature(16.0, false);
   }

   @Override
   public void onDeath(DamageSource source) {
      super.onDeath(source);
      if (!this.worldObj.isRemote && this.isPossessed() && (this.riddenByEntity == null || !this.riddenByEntity.onPossessedRidingEntityDeath())) {
         this.attemptToPossessNearbyCreatureOnDeath();
      }
   }

   protected boolean shouldContinueAttacking(float fDistanceToTarget) {
      return true;
   }

   protected void transmitAttackTargetToClients() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(this.entityId);
         dataStream.writeByte(0);
         if (this.entityToAttack != null) {
            dataStream.writeInt(this.entityToAttack.entityId);
         } else {
            dataStream.writeInt(-1);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      Packet250CustomPayload packet = new Packet250CustomPayload("BTW|EV", byteStream.toByteArray());
      WorldUtils.sendPacketToAllPlayersTrackingEntity((WorldServer)this.worldObj, this, packet);
   }

   public boolean canSoulAffectEntity(Entity soulEntity) {
      if (this.getCanCreatureBePossessedFromDistance(true)) {
         this.initiatePossession();
         return true;
      } else {
         return false;
      }
   }
}
