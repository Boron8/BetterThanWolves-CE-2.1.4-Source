package btw.entity.mob;

import btw.entity.util.ClosestEntitySelectionCriteria;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityWaterMob;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;

public class SquidEntity extends EntityWaterMob {
   public static final float BRIGHTNESS_AGGRESSION_THRESHOLD = 0.1F;
   private static final float SAFE_ATTACK_DEPTH = 0.5F;
   private static final int SAFE_ATTACK_DEPTH_TEST_MAXIMUM = 1;
   private static final float SAFE_PASSIVE_DEPTH = 3.0F;
   private static final int SAFE_PASSIVE_DEPTH_TEST_MAXIMUM = 4;
   private static final double AGGRESSION_RANGE = 16.0;
   private static final int CHANCE_OF_LOSING_ATTACK_TARGET_IN_LIGHT = 400;
   private static final int TENTACLE_ATTACK_TICKS_TO_COOLDOWN = 100;
   private static final double TENTACLE_ATTACK_RANGE = 6.0;
   public static final int TENTACLE_ATTACK_DURATION = 20;
   private static final double TENTACLE_ATTACK_TIP_COLLISION_WIDTH = 0.2;
   private static final double TENTACLE_ATTACK_TIP_COLLISION_HALF_WIDTH = 0.1;
   private int tentacleAttackCooldownTimer = 100;
   public int tentacleAttackInProgressCounter = -1;
   private double tentacleAttackTargetX = 0.0;
   private double tentacleAttackTargetY = 0.0;
   private double tentacleAttackTargetZ = 0.0;
   private static final int HEAD_CRAB_DAMAGE_INITIAL_DELAY = 40;
   private static final int HEAD_CRAB_DAMAGE_PERIOD = 40;
   private int headCrabDamageCounter = 40;
   public float squidPitch = 0.0F;
   public float prevSquidPitch = 0.0F;
   public float squidYaw = 0.0F;
   public float prevSquidYaw = 0.0F;
   private float squidYawSpeed = 0.0F;
   public float tentacleAngle = 0.0F;
   public float prevTentacleAngle = 0.0F;
   private float tentacleAnimProgress = 0.0F;
   private float prevTentacleAnimProgress = 0.0F;
   private float tentacleAnimSpeed = 0.0F;
   private float randomMotionSpeed = 0.0F;
   private float randomMotionVecX = 0.0F;
   private float randomMotionVecY = 0.0F;
   private float randomMotionVecZ = 0.0F;
   private Entity entityToNotReCrab = null;
   private int reCrabEntityCountdown = 0;
   private static final int RE_CRAB_ENTITY_TICKS = 5;
   private static final float POSSESSED_LEAP_DEPTH = 0.5F;
   private static final int POSSESSED_LEAP_COUNTDOWN_DURATION = 200;
   private static final int POSSESSED_LEAP_PROPULSION_DURATION = 10;
   private int possessedLeapCountdown = 0;
   private int possessedLeapPropulsionCountdown = 0;
   private final float possessedLeapGhastConversionChance = 0.25F;
   private float possessedLeapGhastConversionDiceRoll = 1.0F;
   private static final int SQUID_POSSESSION_MAX_COUNT = 50;

   public SquidEntity(World world) {
      super(world);
      this.texture = "/mob/squid.png";
      this.a(0.95F, 0.95F);
      this.tentacleAnimSpeed = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
   }

   @Override
   public int getMaxHealth() {
      return 20;
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
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      int iNumInkSacks = this.rand.nextInt(3 + iLootingModifier) + 1;

      for (int iTempInkSack = 0; iTempInkSack < iNumInkSacks; iTempInkSack++) {
         this.a(new ItemStack(Item.dyePowder, 1, 0), 0.0F);
      }

      if (this.rand.nextInt(8) - iLootingModifier <= 0) {
         this.b(BTWItems.mysteriousGland.itemID, 1);
      }
   }

   @Override
   public void onLivingUpdate() {
      super.c();
      this.prevSquidPitch = this.squidPitch;
      this.prevSquidYaw = this.squidYaw;
      this.prevTentacleAnimProgress = this.tentacleAnimProgress;
      this.prevTentacleAngle = this.tentacleAngle;
      this.updateTentacleAttack();
      if (!this.R()) {
         if (!this.worldObj.isRemote) {
            this.motionX = 0.0;
            if (this.G()) {
               this.motionY -= 0.02;
               this.motionY *= 0.8;
            } else {
               this.motionY -= 0.08;
               this.motionY *= 0.98F;
            }

            this.motionZ = 0.0;
         }
      } else {
         this.tentacleAnimProgress = this.tentacleAnimProgress + this.tentacleAnimSpeed;
         if (this.tentacleAnimProgress > (float) (Math.PI * 2)) {
            this.tentacleAnimProgress -= (float) (Math.PI * 2);
            if (this.rand.nextInt(10) == 0) {
               this.tentacleAnimSpeed = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }
         }

         if (this.ridingEntity != null && !this.ridingEntity.isEntityAlive()) {
            this.a(null);
            if (!this.worldObj.isRemote) {
               this.worldObj.playAuxSFX(2226, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
            }
         }

         if (!this.inWater && this.ak() % 100 == 0 && (this.isPossessed() || this.isHeadCrab() || this.isBeingRainedOn())) {
            this.g(300);
         }

         if (this.isHeadCrab()) {
            this.updateHeadCrab();
         } else {
            if (this.G()) {
               if (this.tentacleAnimProgress < (float) Math.PI) {
                  float var1 = this.tentacleAnimProgress / (float) Math.PI;
                  this.tentacleAngle = MathHelper.sin(var1 * var1 * (float) Math.PI) * (float) Math.PI * 0.25F;
                  if (var1 > 0.75) {
                     this.randomMotionSpeed = 1.0F;
                     this.squidYawSpeed = 1.0F;
                  } else {
                     this.squidYawSpeed *= 0.8F;
                  }
               } else {
                  this.tentacleAngle = 0.0F;
                  this.randomMotionSpeed *= 0.9F;
                  this.squidYawSpeed *= 0.99F;
               }

               if (!this.worldObj.isRemote) {
                  this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                  this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                  this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
                  if (this.possessedLeapPropulsionCountdown > 0) {
                     this.motionY = 1.0;
                  }
               }

               if (this.possessedLeapPropulsionCountdown > 0) {
                  this.possessedLeapPropulsionCountdown--;
               }

               if (this.tentacleAttackInProgressCounter >= 0) {
                  this.orientToTentacleAttackPoint();
               } else if (this.entityToAttack != null) {
                  this.orientToEntity(this.entityToAttack);
               } else {
                  this.orientToMotion();
               }
            } else {
               this.possessedLeapPropulsionCountdown = 0;
               this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.tentacleAnimProgress)) * (float) Math.PI * 0.25F;
               if (!this.worldObj.isRemote) {
                  this.motionX = 0.0;
                  this.motionY -= 0.08;
                  this.motionY *= 0.98F;
                  this.motionZ = 0.0;
               }

               if (this.tentacleAttackInProgressCounter >= 0) {
                  this.orientToTentacleAttackPoint();
               } else if (this.motionY > 0.5) {
                  this.squidPitch = 0.0F;
               } else {
                  this.squidPitch = (float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02);
               }
            }
         }
      }
   }

   @Override
   public void moveEntityWithHeading(float par1, float par2) {
      this.d(this.motionX, this.motionY, this.motionZ);
   }

   @Override
   protected void updateEntityActionState() {
      this.tentacleAttackCooldownTimer--;
      this.checkForHeadCrab();
      if (this.isHeadCrab()) {
         this.updateHeadCrabActionState();
      } else {
         float fNaturalLightLevel = this.c(1.0F);
         boolean bIsInDarkness = fNaturalLightLevel < 0.1F;
         if (!this.worldObj.isDaytime()) {
            bIsInDarkness = true;
         }

         if (this.entityToAttack == null) {
            if (bIsInDarkness) {
               Entity targetEntity = this.findClosestValidAttackTargetWithinRange(16.0);
               if (targetEntity != null) {
                  this.setTarget(targetEntity);
               }
            }
         } else if (!bIsInDarkness && this.rand.nextInt(400) == 0) {
            this.setTarget(null);
         } else if (!this.entityToAttack.isValidOngoingAttackTargetForSquid()
            || this.d(this.entityToAttack) > 16.0
            || this.worldObj.isDaytime() && this.entityToAttack.getBrightness(1.0F) > 0.1F && this.rand.nextInt(400) == 0) {
            this.setTarget(null);
         }

         if (this.entityToAttack != null) {
            double dDeltaX = this.entityToAttack.posX - this.posX;
            double dDeltaY = this.entityToAttack.posY + this.entityToAttack.getEyeHeight() - (this.posY + this.height / 2.0F);
            double dDeltaZ = this.entityToAttack.posZ - this.posZ;
            double dDistSqToTarget = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
            if (dDistSqToTarget > 0.25) {
               double dDistToTarget = MathHelper.sqrt_double(dDistSqToTarget);
               double dUnitVectorToTargetX = dDeltaX / dDistToTarget;
               double dUnitVectorToTargetY = dDeltaY / dDistToTarget;
               double dUnitVectorToTargetZ = dDeltaZ / dDistToTarget;
               this.randomMotionVecX = (float)(dUnitVectorToTargetX * 0.4);
               this.randomMotionVecY = (float)(dUnitVectorToTargetY * 0.4);
               this.randomMotionVecZ = (float)(dUnitVectorToTargetZ * 0.4);
               if (!this.isFullyPossessed()) {
                  float fDepth = this.getDepthBeneathSurface(1.0F);
                  if (fDepth < 0.5F) {
                     if (this.randomMotionVecY > -0.1F) {
                        this.randomMotionVecY = -0.1F;
                     }
                  } else if (this.randomMotionVecY > 0.0F) {
                     float fDeltaSafeDepth = fDepth - 0.5F;
                     if (this.randomMotionVecY > fDeltaSafeDepth) {
                        this.randomMotionVecY = fDeltaSafeDepth;
                     }
                  }
               }

               if (this.inWater
                  && (!this.entityToAttack.inWater || this.entityToAttack.ridingEntity != null)
                  && this.tentacleAttackInProgressCounter < 0
                  && this.tentacleAttackCooldownTimer <= 0
                  && this.rand.nextInt(20) == 0) {
                  this.attemptTentacleAttackOnTarget();
               }
            } else {
               this.randomMotionVecX = this.randomMotionVecY = this.randomMotionVecZ = 0.0F;
            }
         } else if (this.rand.nextInt(50) == 0
            || !this.inWater
            || this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F) {
            float fFlatHeading = this.rand.nextFloat() * (float) Math.PI * 2.0F;
            this.randomMotionVecZ = MathHelper.sin(fFlatHeading) * 0.2F;
            this.randomMotionVecX = MathHelper.cos(fFlatHeading) * 0.2F;
            float fDepth = this.getDepthBeneathSurface(4.0F);
            if (this.isFullyPossessed() && this.inWater) {
               this.randomMotionVecY = 0.1F;
               if (fDepth < 0.5F && this.possessedLeapCountdown <= 0) {
                  this.possessedLeap();
               }
            } else if (fDepth >= 3.0F) {
               if (fNaturalLightLevel < 0.1F) {
                  int iSkylightSubtracted = this.worldObj.skylightSubtracted;
                  if (!this.worldObj.isDaytime()) {
                     this.randomMotionVecY = 0.1F;
                  } else {
                     this.randomMotionVecY = this.rand.nextFloat() * 0.15F - 0.1F;
                  }
               } else {
                  this.randomMotionVecY = -0.1F;
               }
            } else {
               this.randomMotionVecY = -0.1F;
            }
         }

         this.entityAge++;
         this.bn();
      }
   }

   @Override
   protected double minDistFromPlayerForDespawn() {
      return 144.0;
   }

   @Override
   protected boolean canDespawn() {
      return !this.isHeadCrab();
   }

   @Override
   public boolean getCanSpawnHere() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      if (!this.isBlockSurroundedByWater(i, j, k)
         && !this.isBlockSurroundedByWater(i + 1, j, k)
         && !this.isBlockSurroundedByWater(i - 1, j, k)
         && !this.isBlockSurroundedByWater(i, j + 1, k)
         && !this.isBlockSurroundedByWater(i, j - 1, k)
         && !this.isBlockSurroundedByWater(i, j, k + 1)
         && !this.isBlockSurroundedByWater(i, j, k - 1)) {
         return false;
      } else {
         int iLightLevel = this.worldObj.getBlockLightValue(i, j, k);
         return iLightLevel > 1 ? false : super.getCanSpawnHere();
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource damageSource, int iDamageAmount) {
      if (this.isHeadCrab()) {
         return damageSource == DamageSource.inWall ? false : super.a(damageSource, iDamageAmount);
      } else if (this.isPossessed() && damageSource == DamageSource.fall) {
         return false;
      } else if (super.a(damageSource, iDamageAmount)) {
         if (!this.worldObj.isRemote) {
            Entity attackingEntity = damageSource.getEntity();
            if (attackingEntity != null && attackingEntity != this) {
               this.setTarget(attackingEntity);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(250) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.respiration.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   public AxisAlignedBB getVisualBoundingBox() {
      if (this.tentacleAttackInProgressCounter >= 0) {
         double dExpandByAmount = 6.25;
         return this.boundingBox.expand(dExpandByAmount, dExpandByAmount, dExpandByAmount);
      } else {
         return this.boundingBox;
      }
   }

   @Override
   public void setTarget(Entity targetEntity) {
      if (!this.worldObj.isRemote && targetEntity != this.entityToAttack) {
         this.entityToAttack = targetEntity;
         this.transmitAttackTargetToClients();
      } else {
         this.entityToAttack = targetEntity;
      }
   }

   @Override
   public boolean getCanCreatureTypeBePossessed() {
      return true;
   }

   @Override
   public boolean getCanCreatureBePossessedFromDistance(boolean bPersistentSpirit) {
      return bPersistentSpirit || this.worldObj.getNumEntitiesThatApplyToSquidPossessionCap() < 50;
   }

   @Override
   public boolean onPossessedRidingEntityDeath() {
      if (this.R() && !this.isPossessed()) {
         this.initiatePossession();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public void initiatePossession() {
      super.initiatePossession();
      this.setPersistent(true);
   }

   @Override
   protected void handlePossession() {
      super.handlePossession();
      if (this.possessedLeapCountdown > 0) {
         this.possessedLeapCountdown--;
      }

      if (!this.worldObj.isRemote && this.isFullyPossessed()) {
         if (this.ridingEntity == null && !this.inWater && !this.onGround) {
            if (this.possessedLeapGhastConversionDiceRoll <= 0.25F && this.motionY <= 0.0) {
               GhastEntity ghast = (GhastEntity)EntityList.createEntityOfType(GhastEntity.class, this.worldObj);
               ghast.b(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
               if (this.worldObj.checkNoEntityCollision(ghast.boundingBox, this)
                  && this.worldObj.getCollidingBoundingBoxes(this, ghast.boundingBox).isEmpty()
                  && !this.worldObj.isAnyLiquid(ghast.boundingBox)) {
                  this.worldObj.playAuxSFX(2273, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
                  this.w();
                  ghast.setPersistent(true);
                  this.worldObj.spawnEntityInWorld(ghast);
               }
            }
         } else if (!this.inWater || this.motionY <= 0.0) {
            this.possessedLeapGhastConversionDiceRoll = 1.0F;
         }
      }
   }

   @Override
   public boolean doesEntityApplyToSquidPossessionCap() {
      return this.R() && this.getIsPersistent();
   }

   @Override
   public boolean isValidZombieSecondaryTarget(EntityZombie zombie) {
      return !this.inWater && this.ridingEntity == null && zombie.riddenByEntity == null;
   }

   @Override
   public boolean attractsLightning() {
      return false;
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.5F;
   }

   private void updateHeadCrabActionState() {
      Entity sharedTarget = this.ridingEntity.getHeadCrabSharedAttackTarget();
      if (sharedTarget == this) {
         sharedTarget = null;
      }

      this.setTarget(sharedTarget);
      if (this.entityToAttack != null && this.tentacleAttackInProgressCounter < 0 && this.tentacleAttackCooldownTimer <= 0 && this.rand.nextInt(20) == 0) {
         this.attemptTentacleAttackOnTarget();
      }

      if (this.isFullyPossessed() && this.possessedLeapCountdown <= 0 && !this.inWater && this.rand.nextInt(100) == 0) {
         this.a(null);
         this.possessedLeap();
      }
   }

   private void orientToMotion() {
      float fMotionVectorFlatLength = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      this.renderYawOffset = this.interpolateAngle(this.renderYawOffset, -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI, 1.0F);
      this.rotationYaw = this.renderYawOffset;
      this.squidPitch = this.squidPitch + (-((float)Math.atan2(fMotionVectorFlatLength, this.motionY)) * 180.0F / (float) Math.PI - this.squidPitch) * 0.1F;
      this.squidYaw = this.squidYaw + (float) Math.PI * this.squidYawSpeed * 1.5F;
   }

   private void orientToEntity(Entity entity) {
      double dDeltaX = entity.posX - this.posX;
      double dDeltaY = entity.posY + entity.getEyeHeight() - (this.posY + this.height / 2.0F);
      double dDeltaZ = entity.posZ - this.posZ;
      double dFlatDist = MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaZ * dDeltaZ);
      this.renderYawOffset = this.interpolateAngle(this.renderYawOffset, -((float)Math.atan2(dDeltaX, dDeltaZ)) * 180.0F / (float) Math.PI, 1.0F);
      this.rotationYaw = this.renderYawOffset;
      this.squidPitch = this.interpolateAngle(this.squidPitch, -((float)(Math.atan2(dFlatDist, dDeltaY) * 180.0 / Math.PI)), 10.0F);
      this.squidYaw = this.squidYaw + (float) Math.PI * this.squidYawSpeed * 1.5F;
   }

   private void orientToTentacleAttackPoint() {
      double dDeltaX = this.tentacleAttackTargetX - this.posX;
      double dDeltaY = this.tentacleAttackTargetY - (this.posY + this.height / 2.0F);
      double dDeltaZ = this.tentacleAttackTargetZ - this.posZ;
      double dFlatDist = MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaZ * dDeltaZ);
      this.renderYawOffset = this.interpolateAngle(this.renderYawOffset, -((float)Math.atan2(dDeltaX, dDeltaZ)) * 180.0F / (float) Math.PI, 50.0F);
      this.rotationYaw = this.renderYawOffset;
      this.squidPitch = this.interpolateAngle(this.squidPitch, -((float)(Math.atan2(dFlatDist, dDeltaY) * 180.0 / Math.PI - 150.0)), 50.0F);
      this.squidYaw = this.interpolateAngle(this.squidYaw, 0.0F, 50.0F);
   }

   private Entity findClosestValidAttackTargetWithinRange(double dRange) {
      Entity targetEntity = null;
      double dClosestDistSq = dRange * dRange;

      for (int iPlayerCount = 0; iPlayerCount < this.worldObj.playerEntities.size(); iPlayerCount++) {
         EntityPlayer tempPlayer = (EntityPlayer)this.worldObj.playerEntities.get(iPlayerCount);
         if (!tempPlayer.capabilities.disableDamage && tempPlayer.R()) {
            double dDeltaX = tempPlayer.posX - this.posX;
            double dDeltaY = tempPlayer.posY - this.posY;
            double dDeltaZ = tempPlayer.posZ - this.posZ;
            double dDistSq = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
            if (dDistSq < dClosestDistSq
               && (!this.worldObj.isDaytime() || tempPlayer.c(1.0F) < 0.1F)
               && (tempPlayer.inWater || this.n(tempPlayer) && this.worldObj.getDifficulty().shouldSquidsAttackDryPlayers())
               && (tempPlayer.ridingEntity == null || this.worldObj.getDifficulty().shouldSquidsAttackDryPlayers())) {
               targetEntity = tempPlayer;
               dClosestDistSq = dDistSq;
            }
         }
      }

      if ((this.worldObj.worldInfo.getWorldTime() + this.entityId & 31L) == 0L && targetEntity == null) {
         targetEntity = this.worldObj
            .getClosestEntityMatchingCriteriaWithinRange(this.posX, this.posY, this.posZ, dRange, ClosestEntitySelectionCriteria.secondarySquidTarget);
      }

      return targetEntity;
   }

   private void checkForHeadCrab() {
      if (this.R()) {
         if (this.ridingEntity == null) {
            if (this.motionY < 0.5) {
               if (this.reCrabEntityCountdown > 0) {
                  this.reCrabEntityCountdown--;
               } else {
                  this.entityToNotReCrab = null;
               }

               EntityLiving target = this.getValidHeadCrabTargetInRange();
               if (target != null) {
                  this.a(target);
                  this.a("mob.slime.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                  this.headCrabDamageCounter = 40;
                  target.onHeadCrabbedBySquid(this);
               }
            }
         } else {
            this.entityToNotReCrab = this.ridingEntity;
            this.reCrabEntityCountdown = 5;
         }
      }
   }

   private EntityLiving getValidHeadCrabTargetInRange() {
      double dRange = 0.25;
      if (!this.G()) {
         dRange = 0.5;
      }

      for (EntityLiving tempEntity : this.worldObj.getEntitiesWithinAABB(EntityLiving.class, this.boundingBox.expand(dRange, dRange, dRange))) {
         if (tempEntity.getCanBeHeadCrabbed(this.G()) && tempEntity != this.entityToNotReCrab && this.n(tempEntity)) {
            return tempEntity;
         }
      }

      return null;
   }

   private void updateHeadCrab() {
      this.tentacleAnimSpeed = 0.2F;
      this.squidPitch = 0.0F;
      float fSinTentacle = MathHelper.sin(this.tentacleAnimProgress);
      this.tentacleAngle = MathHelper.abs(MathHelper.sin(fSinTentacle)) * (float) Math.PI * 0.25F;
      if (!this.worldObj.isRemote) {
         this.headCrabDamageCounter--;
         if (this.headCrabDamageCounter <= 0) {
            if (!this.ridingEntity.isImmuneToHeadCrabDamage()) {
               DamageSource squidSource = DamageSource.causeMobDamage(this);
               squidSource.setDamageBypassesArmor();
               this.ridingEntity.attackEntityFrom(squidSource, 1);
            }

            this.headCrabDamageCounter = 40;
         }

         if (this.ridingEntity.ridingEntity != null) {
            this.ridingEntity.mountEntity(this.ridingEntity.ridingEntity);
            if (this.ridingEntity.ridingEntity != null) {
               this.ridingEntity.ridingEntity.riddenByEntity = null;
               this.ridingEntity.ridingEntity = null;
            }
         }
      } else {
         float fPrevSinTentacle = MathHelper.sin(this.prevTentacleAnimProgress);
         if (fPrevSinTentacle <= 0.0F && fSinTentacle > 0.0F || fPrevSinTentacle > 0.0F && fSinTentacle <= 0.0F) {
            if (!this.ridingEntity.isImmuneToHeadCrabDamage()) {
               this.worldObj.playSound(this.posX, this.posY, this.posZ, "random.eat", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.8F);
               this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.slime.big", 0.5F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F);
            } else {
               this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.slime.big", 0.025F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F);
            }
         }

         if (this.ridingEntity instanceof EntityLiving) {
            EntityLiving ridingCreature = (EntityLiving)this.ridingEntity;
            this.squidYaw = -ridingCreature.rotationYawHead;
            this.prevSquidYaw = -ridingCreature.prevRotationYawHead;
            this.renderYawOffset = 0.0F;
            this.rotationYaw = 0.0F;
         }
      }
   }

   private void attemptTentacleAttackOnTarget() {
      double dDeltaX = this.entityToAttack.posX - this.posX;
      double dDeltaY = this.entityToAttack.posY + this.entityToAttack.height / 2.0F - (this.posY + this.height / 2.0F);
      double dDeltaZ = this.entityToAttack.posZ - this.posZ;
      double dDistSqToTarget = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
      if (dDistSqToTarget < 36.0) {
         if (!this.canEntityCenterOfMassBeSeen(this.entityToAttack)) {
            if (!this.n(this.entityToAttack)) {
               return;
            }

            dDeltaY = this.entityToAttack.posY + this.entityToAttack.getEyeHeight() - (this.posY + this.height / 2.0F);
            dDistSqToTarget = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
         }

         double dDistToTarget = MathHelper.sqrt_double(dDistSqToTarget);
         double dUnitVectorToTargetX = dDeltaX / dDistToTarget;
         double dUnitVectorToTargetY = dDeltaY / dDistToTarget;
         double dUnitVectorToTargetZ = dDeltaZ / dDistToTarget;
         this.launchTentacleAttackInDirection(dUnitVectorToTargetX, dUnitVectorToTargetY, dUnitVectorToTargetZ);
      }
   }

   private void launchTentacleAttackInDirection(double dUnitVectorToTargetX, double dUnitVectorToTargetY, double dUnitVectorToTargetZ) {
      this.tentacleAttackInProgressCounter = 0;
      this.tentacleAttackCooldownTimer = 100;
      this.tentacleAttackTargetX = this.posX + dUnitVectorToTargetX * 6.0;
      this.tentacleAttackTargetY = this.posY + this.height / 2.0F + dUnitVectorToTargetY * 6.0;
      this.tentacleAttackTargetZ = this.posZ + dUnitVectorToTargetZ * 6.0;
      this.transmitTentacleAttackToClients();
   }

   private void transmitTentacleAttackToClients() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(this.entityId);
         dataStream.writeByte(1);
         dataStream.writeInt(MathHelper.floor_double(this.tentacleAttackTargetX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.tentacleAttackTargetY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.tentacleAttackTargetZ * 32.0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      Packet250CustomPayload packet = new Packet250CustomPayload("BTW|EV", byteStream.toByteArray());
      WorldUtils.sendPacketToAllPlayersTrackingEntity((WorldServer)this.worldObj, this, packet);
   }

   public void onClientNotifiedOfTentacleAttack(double dTargetX, double dTargetY, double dTargetZ) {
      this.tentacleAttackInProgressCounter = 0;
      this.tentacleAttackTargetX = dTargetX;
      this.tentacleAttackTargetY = dTargetY;
      this.tentacleAttackTargetZ = dTargetZ;
      this.worldObj.playSound(this.posX, this.posY, this.posZ, "random.bow", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
      this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.slime.big", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
      if (this.inWater) {
         for (int iParticleCount = 0; iParticleCount < 150; iParticleCount++) {
            this.worldObj
               .spawnParticle(
                  "bubble",
                  this.posX + this.rand.nextDouble() * 2.0 - 1.0,
                  this.posY + this.rand.nextDouble(),
                  this.posZ + this.rand.nextDouble() * 2.0 - 1.0,
                  0.0,
                  0.0,
                  0.0
               );
         }

         for (int iParticleCount = 0; iParticleCount < 10; iParticleCount++) {
            this.worldObj
               .spawnParticle(
                  "splash",
                  this.posX + this.rand.nextDouble() * 2.0 - 1.0,
                  this.posY + this.height,
                  this.posZ + this.rand.nextDouble() * 2.0 - 1.0,
                  0.0,
                  0.0,
                  0.0
               );
         }

         this.worldObj.playSound(this.posX, this.posY, this.posZ, "liquid.splash", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
      }
   }

   private void updateTentacleAttack() {
      if (this.tentacleAttackInProgressCounter >= 0) {
         this.tentacleAttackInProgressCounter++;
         if (this.tentacleAttackInProgressCounter >= 20) {
            this.tentacleAttackInProgressCounter = -1;
         } else if (this.tentacleAttackInProgressCounter <= 10) {
            Vec3 tentacleTip = this.computeTentacleAttackTip(this.tentacleAttackInProgressCounter);
            AxisAlignedBB tipBox = AxisAlignedBB.getAABBPool()
               .getAABB(
                  tentacleTip.xCoord - 0.1,
                  tentacleTip.yCoord - 0.1,
                  tentacleTip.zCoord - 0.1,
                  tentacleTip.xCoord + 0.1,
                  tentacleTip.yCoord + 0.1,
                  tentacleTip.zCoord + 0.1
               );
            List potentialCollisionList = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, tipBox);
            if (!potentialCollisionList.isEmpty()) {
               for (EntityLiving tempEntity : potentialCollisionList) {
                  if (!(tempEntity instanceof SquidEntity) && tempEntity != this.ridingEntity) {
                     this.retractTentacleAttackOnCollision();
                     if (!this.worldObj.isRemote) {
                        this.tentacleAttackFlingTarget(tempEntity, true);
                     }
                     break;
                  }
               }
            }
         }
      }
   }

   private void retractTentacleAttackOnCollision() {
      int iTurningPoint = 10;
      if (this.tentacleAttackInProgressCounter < iTurningPoint) {
         this.tentacleAttackInProgressCounter = iTurningPoint + (iTurningPoint - this.tentacleAttackInProgressCounter);
      }
   }

   public Vec3 computeTentacleAttackTip(float fAttackProgressTick) {
      double dAttackProgressSin = this.getAttackProgressSin(fAttackProgressTick);
      double dDeltaX = this.tentacleAttackTargetX - this.posX;
      double dDeltaY = this.tentacleAttackTargetY - (this.posY + this.height / 2.0F);
      double dDeltaZ = this.tentacleAttackTargetZ - this.posZ;
      double dTipOffsetX = dDeltaX * dAttackProgressSin;
      double dTipOffsetY = dDeltaY * dAttackProgressSin;
      double dTipOffsetZ = dDeltaZ * dAttackProgressSin;
      return Vec3.createVectorHelper(this.posX + dTipOffsetX, this.posY + this.height / 2.0F + dTipOffsetY, this.posZ + dTipOffsetZ);
   }

   public double getAttackProgressSin(float fAttackProgressTick) {
      double dAttackProgress = fAttackProgressTick / 20.0F;
      return MathHelper.sin((float)(dAttackProgress * Math.PI));
   }

   private void tentacleAttackFlingTarget(Entity targetEntity, boolean bPrimary) {
      Entity secondaryTargetEntity = null;
      if (targetEntity.ridingEntity != null) {
         secondaryTargetEntity = targetEntity.ridingEntity;
         targetEntity.mountEntity(null);
      }

      if (bPrimary) {
         int iFXI = MathHelper.floor_double(targetEntity.posX);
         int iFXJ = MathHelper.floor_double(targetEntity.posY) + 1;
         int iFXK = MathHelper.floor_double(targetEntity.posZ);
         this.worldObj.playAuxSFX(2262, iFXI, iFXJ, iFXK, 0);
      }

      double dVelocityX = targetEntity.motionX;
      double dVelocityZ = targetEntity.motionZ;
      double dDeltaX = targetEntity.posX - this.posX;
      double dDeltaZ = targetEntity.posZ - this.posZ;
      double dFlatDistToTargetSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
      if (dFlatDistToTargetSq > 0.1) {
         double dFlatDistToTarget = MathHelper.sqrt_double(dFlatDistToTargetSq);
         dVelocityX += (float)(-dDeltaX / dFlatDistToTarget) * 1.0F;
         dVelocityZ += (float)(-dDeltaZ / dFlatDistToTarget) * 1.0F;
      }

      if (targetEntity instanceof EntityPlayer && ((EntityPlayer)targetEntity).isBlocking()) {
         EntityPlayer blockingPlayer = (EntityPlayer)targetEntity;
         ItemStack blockItemStack = blockingPlayer.inventory.mainInventory[blockingPlayer.inventory.currentItem];
         if (blockItemStack != null) {
            ItemStack flingStack = new ItemStack(blockItemStack.itemID, blockItemStack.stackSize, blockItemStack.getItemDamage());
            InventoryUtils.copyEnchantments(flingStack, blockItemStack);
            double dItemXPos = targetEntity.posX;
            double dItemYPos = targetEntity.posY + 1.0;
            double dItemZPos = targetEntity.posZ;
            EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, this.worldObj, dItemXPos, dItemYPos, dItemZPos, flingStack);
            double dVelocityY = targetEntity.motionY + 0.5;
            entityitem.motionX = dVelocityX;
            entityitem.motionY = dVelocityY;
            entityitem.motionZ = dVelocityZ;
            entityitem.delayBeforeCanPickup = 10;
            this.worldObj.spawnEntityInWorld(entityitem);
            blockItemStack.stackSize = 0;
         }
      } else {
         targetEntity.isAirBorne = true;
         double dVelocityY = targetEntity.motionY + 0.75;
         dVelocityX *= this.rand.nextDouble() * 0.2 + 0.9;
         dVelocityZ *= this.rand.nextDouble() * 0.2 + 0.9;
         targetEntity.motionX = dVelocityX;
         targetEntity.motionY = dVelocityY;
         targetEntity.motionZ = dVelocityZ;
         this.capFlingMotionOfEntity(targetEntity);
         targetEntity.setBeenAttacked();
      }

      targetEntity.onFlungBySquidTentacle(this);
      if (secondaryTargetEntity != null) {
         this.tentacleAttackFlingTarget(secondaryTargetEntity, false);
      }
   }

   private void capFlingMotionOfEntity(Entity targetEntity) {
      if (targetEntity.motionY > 0.75) {
         targetEntity.motionY = 0.75;
      }

      if (targetEntity.motionX > 1.0) {
         targetEntity.motionX = 1.0;
      } else if (targetEntity.motionX < -1.0) {
         targetEntity.motionX = -1.0;
      }

      if (targetEntity.motionZ > 1.0) {
         targetEntity.motionZ = 1.0;
      } else if (targetEntity.motionZ < -1.0) {
         targetEntity.motionZ = -1.0;
      }
   }

   public boolean isHeadCrab() {
      return this.ridingEntity != null && this.ridingEntity instanceof EntityLiving;
   }

   private boolean isBlockSurroundedByWater(int i, int j, int k) {
      for (int iTempJ = j - 1; iTempJ <= j + 1; iTempJ++) {
         for (int iTempI = i - 1; iTempI <= i + 1; iTempI++) {
            for (int iTempK = k - 1; iTempK <= k + 1; iTempK++) {
               int iTempBlockID = this.worldObj.getBlockId(iTempI, iTempJ, iTempK);
               if (iTempBlockID != Block.waterMoving.blockID && iTempBlockID != Block.waterStill.blockID) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public float getDepthBeneathSurface(float fMaxDepthToConsider) {
      float fDepth = -1.0F;
      int iPosI = MathHelper.floor_double(this.posX);
      int iPosJ = (int)this.posY;
      int iPosK = MathHelper.floor_double(this.posZ);
      int iTempBlockID = this.worldObj.getBlockId(iPosI, iPosJ, iPosK);
      int iTempBlockAboveID = this.worldObj.getBlockId(iPosI, iPosJ + 1, iPosK);
      if (iTempBlockID == Block.waterStill.blockID
         || iTempBlockID == Block.waterMoving.blockID
         || iTempBlockAboveID == Block.waterStill.blockID
         || iTempBlockAboveID == Block.waterMoving.blockID) {
         fDepth = 0.0F;
         fDepth = (float)(fDepth + (this.posY - iPosJ));

         for (int iJOffset = 1; fDepth < fMaxDepthToConsider; iJOffset++) {
            iTempBlockID = this.worldObj.getBlockId(iPosI, iPosJ + iJOffset, iPosK);
            if (iTempBlockID != Block.waterStill.blockID && iTempBlockID != Block.waterMoving.blockID) {
               if (fDepth == 0.0F && this.posY > 32.0) {
               }
               break;
            }

            fDepth++;
         }
      }

      return fDepth;
   }

   private float interpolateAngle(float fStartAngle, float fDestAngle, float fMaxIncrement) {
      float fDelta = MathHelper.wrapAngleTo180_float(fDestAngle - fStartAngle);
      if (fDelta > fMaxIncrement) {
         fDelta = fMaxIncrement;
      } else if (fDelta < -fMaxIncrement) {
         fDelta = -fMaxIncrement;
      }

      return fStartAngle + fDelta;
   }

   private void possessedLeap() {
      this.motionY = 1.0;
      this.isAirBorne = true;
      this.possessedLeapCountdown = 200;
      this.possessedLeapGhastConversionDiceRoll = this.rand.nextFloat();
      if (this.inWater) {
         this.possessedLeapPropulsionCountdown = 10;
         this.a("liquid.splash", 1.0F, this.rand.nextFloat() * 0.1F + 0.5F);
      } else {
         this.possessedLeapPropulsionCountdown = 0;
         this.a("mob.slime.big", 1.0F, this.rand.nextFloat() * 0.1F + 0.5F);
      }
   }

   private AxisAlignedBB getGhastConversionCollisionBoxFromPool() {
      double dWidthOffset = this.width / 16.0F;
      return AxisAlignedBB.getAABBPool()
         .getAABB(
            this.boundingBox.minX + dWidthOffset,
            this.boundingBox.maxY,
            this.boundingBox.minZ + dWidthOffset,
            this.boundingBox.maxX - dWidthOffset,
            this.boundingBox.maxY + 0.1F,
            this.boundingBox.maxZ - dWidthOffset
         );
   }
}
