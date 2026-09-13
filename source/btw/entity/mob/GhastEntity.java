package btw.entity.mob;

import btw.BTWMod;
import btw.item.BTWItems;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityLargeFireball;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class GhastEntity extends EntityGhast {
   private static final long PLAYER_SWITCH_DIMENSIONS_GRACE_PERIOD = 600L;
   private static final double MAX_FIREBALL_LAUNCH_RANGE = 64.0;
   private static final double MAX_FIREBALL_LAUNCH_RANGE_SQ = 4096.0;
   private static final double FIREBALL_SPAWN_DIST_FROM_GHAST = 4.0;
   private static final int FIREBALL_EXPLOSION_POWER = 1;
   private Entity entityTargeted = null;
   private int retargetCountdown = 0;

   public GhastEntity(World world) {
      super(world);
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.worldObj.isRemote) {
         this.updateAttackStateClient();
      }
   }

   private void updateAttackStateClient() {
      byte state = this.dataWatcher.getWatchableObjectByte(16);
      if (state == 0 && this.attackCounter > 10) {
         this.attackCounter = 10;
      } else if (state == 1 && this.attackCounter <= 10) {
         this.attackCounter = 11;
      }

      this.prevAttackCounter = this.attackCounter;
      if (this.entityTargeted == null || !this.entityTargeted.isEntityAlive() || this.retargetCountdown-- <= 0) {
         this.entityTargeted = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0);
         if (this.entityTargeted != null) {
            EntityPlayer targetPlayer = (EntityPlayer)this.entityTargeted;
            long lTargetChangedDimensionTime = targetPlayer.timeOfLastDimensionSwitch;
            long lWorldTime = this.worldObj.getWorldTime();
            if (lWorldTime > lTargetChangedDimensionTime && lWorldTime - lTargetChangedDimensionTime <= 600L && targetPlayer != this.waypointZ) {
               this.entityTargeted = null;
            }
         }

         if (this.entityTargeted != null) {
            this.retargetCountdown = 20;
         }
      }

      if (this.entityTargeted != null && this.entityTargeted.getDistanceSqToEntity(this) < 4096.0) {
         double dTargetDeltaX = this.entityTargeted.posX - this.posX;
         double dTargetDeltaZ = this.entityTargeted.posZ - this.posZ;
         this.renderYawOffset = this.rotationYaw = -((float)(Math.atan2(dTargetDeltaX, dTargetDeltaZ) * 180.0 / Math.PI));
         if (this.canEntityBeSeen(this.entityTargeted)) {
            this.attackCounter++;
            if (this.attackCounter == 20) {
               this.attackCounter = -40;
            }
         } else if (this.attackCounter > 0) {
            this.attackCounter--;
         }
      } else {
         this.renderYawOffset = this.rotationYaw = -((float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI));
         if (this.attackCounter > 0) {
            this.attackCounter--;
         }
      }
   }

   @Override
   protected void updateEntityActionState() {
      this.entityAge++;
      this.bn();
      this.prevAttackCounter = this.attackCounter;
      double dWaypointDeltaX = this.waypointX - this.posX;
      double dWaypointDeltaY = this.waypointY - this.posY;
      double dWaypointDeltaZ = this.waypointZ - this.posZ;
      double dWaypointDistSq = dWaypointDeltaX * dWaypointDeltaX + dWaypointDeltaY * dWaypointDeltaY + dWaypointDeltaZ * dWaypointDeltaZ;
      if (dWaypointDistSq < 1.0 || dWaypointDistSq > 3600.0) {
         this.waypointX = this.posX + (this.rand.nextDouble() * 2.0 - 1.0) * 16.0;
         this.waypointY = this.posY + (this.rand.nextDouble() * 2.0 - 1.0) * 16.0;
         this.waypointZ = this.posZ + (this.rand.nextDouble() * 2.0 - 1.0) * 16.0;
      }

      if (this.courseChangeCooldown-- <= 0) {
         this.courseChangeCooldown = this.courseChangeCooldown + this.rand.nextInt(5) + 2;
         double dWaypointDist = MathHelper.sqrt_double(dWaypointDistSq);
         if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, dWaypointDist)) {
            this.motionX += dWaypointDeltaX / dWaypointDist * 0.1;
            this.motionY += dWaypointDeltaY / dWaypointDist * 0.1;
            this.motionZ += dWaypointDeltaZ / dWaypointDist * 0.1;
         } else {
            this.waypointX = this.posX;
            this.waypointY = this.posY;
            this.waypointZ = this.posZ;
         }
      }

      if (this.entityTargeted == null || !this.entityTargeted.isEntityAlive() || this.retargetCountdown-- <= 0) {
         this.entityTargeted = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0);
         if (this.entityTargeted != null && this.entityTargeted instanceof EntityPlayer) {
            EntityPlayer targetPlayer = (EntityPlayer)this.entityTargeted;
            long lTargetChangedDimensionTime = targetPlayer.timeOfLastDimensionSwitch;
            long lWorldTime = this.worldObj.getWorldTime();
            if (lWorldTime > lTargetChangedDimensionTime && lWorldTime - lTargetChangedDimensionTime <= 600L && targetPlayer != this.waypointZ) {
               this.entityTargeted = null;
            }
         }

         if (this.entityTargeted != null) {
            this.retargetCountdown = 20;
         }
      }

      if (this.entityTargeted != null && this.entityTargeted.getDistanceSqToEntity(this) < 4096.0) {
         double dTargetDeltaX = this.entityTargeted.posX - this.posX;
         double dTargetDeltaZ = this.entityTargeted.posZ - this.posZ;
         double dTargetDeltaY = this.entityTargeted.boundingBox.minY + this.entityTargeted.height / 2.0F - (this.posY + this.height / 2.0F);
         this.renderYawOffset = this.rotationYaw = -((float)(Math.atan2(dTargetDeltaX, dTargetDeltaZ) * 180.0 / Math.PI));
         if (this.canEntityBeSeen(this.entityTargeted)) {
            if (this.attackCounter == 10) {
               this.worldObj.playAuxSFXAtEntity(null, 1007, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }

            this.attackCounter++;
            if (this.attackCounter == 20) {
               this.fireAtTarget();
            }
         } else if (this.attackCounter > 0) {
            this.attackCounter--;
         }
      } else {
         this.renderYawOffset = this.rotationYaw = -((float)(Math.atan2(this.motionX, this.motionZ) * 180.0 / Math.PI));
         if (this.attackCounter > 0) {
            this.attackCounter--;
         }
      }

      if (!this.worldObj.isRemote) {
         boolean bMouthOpen = this.dataWatcher.getWatchableObjectByte(16) != 0;
         boolean bShouldMouthBeOpen = this.attackCounter > 10;
         if (bMouthOpen != bShouldMouthBeOpen) {
            this.setMouthOpen(bShouldMouthBeOpen);
         }
      }
   }

   @Override
   protected double minDistFromPlayerForDespawn() {
      return 144.0;
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(500) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.punch.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   public int getTalkInterval() {
      return 80 + this.rand.nextInt(480);
   }

   @Override
   public boolean canEntityBeSeen(Entity entity) {
      return this.worldObj
            .rayTraceBlocks_do_do(
               this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY + this.height / 2.0, this.posZ),
               this.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
               false,
               true
            )
         == null;
   }

   @Override
   public boolean doesEntityApplyToSquidPossessionCap() {
      return this.R() && this.getIsPersistent();
   }

   @Override
   protected int getDropItemId() {
      return Item.fireballCharge.itemID;
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iFortuneModifier) {
      int iNumDrops = this.rand.nextInt(2) + this.rand.nextInt(1 + iFortuneModifier);

      for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
         this.b(Item.ghastTear.itemID, 1);
      }

      iNumDrops = this.rand.nextInt(3) + this.rand.nextInt(1 + iFortuneModifier);

      for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
         this.b(Item.fireballCharge.itemID, 1);
      }
   }

   @Override
   protected float getSoundVolume() {
      return this.worldObj.provider.dimensionId == -1 ? 10.0F : 3.0F;
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.getCanSpawnHereNoPlayerDistanceRestrictions() && this.worldObj.getClosestPlayer(this.posX, this.posY, this.posZ, 64.0) == null;
   }

   @Override
   public boolean attractsLightning() {
      return false;
   }

   private boolean isCourseTraversable(double dDestX, double dDestY, double dDestZ, double dDistToDest) {
      if (dDestY >= 0.0 && dDestY <= this.worldObj.getHeight()) {
         double dDeltaXNorm = (this.waypointX - this.posX) / dDistToDest;
         double dDeltaYNorm = (this.waypointY - this.posY) / dDistToDest;
         double dDeltaZNorm = (this.waypointZ - this.posZ) / dDistToDest;
         AxisAlignedBB tempBox = this.boundingBox.copy();

         for (double dTempDist = 1.0; dTempDist < dDistToDest; dTempDist++) {
            tempBox.offset(dDeltaXNorm, dDeltaYNorm, dDeltaZNorm);
            if (!this.worldObj.getCollidingBoundingBoxes(this, tempBox).isEmpty()) {
               return false;
            }
         }

         return !this.worldObj.isAnyLiquid(tempBox);
      } else {
         return false;
      }
   }

   public boolean getCanSpawnHereNoPlayerDistanceRestrictions() {
      return this.worldObj.checkNoEntityCollision(this.boundingBox)
         && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()
         && !this.worldObj.isAnyLiquid(this.boundingBox)
         && (this.rand.nextInt(5) == 0 || BTWMod.increaseGhastSpawns);
   }

   private void setMouthOpen(boolean bOpen) {
      Byte tempByte = (byte)0;
      if (bOpen) {
         tempByte = (byte)1;
      }

      this.dataWatcher.updateObject(16, tempByte);
   }

   private void fireAtTarget() {
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      Vec3 ghastLookVec = this.i(1.0F);
      double dFireballX = this.posX + ghastLookVec.xCoord;
      double dFireballY = this.posY + this.height / 2.0F;
      double dFireballZ = this.posZ + ghastLookVec.zCoord;
      double dDeltaX = this.entityTargeted.posX - dFireballX;
      double dDeltaY = this.entityTargeted.posY + this.entityTargeted.getEyeHeight() - dFireballY;
      double dDeltaZ = this.entityTargeted.posZ - dFireballZ;
      EntityLargeFireball fireball = (EntityLargeFireball)EntityList.createEntityOfType(
         EntityLargeFireball.class, this.worldObj, this, dDeltaX, dDeltaY, dDeltaZ
      );
      fireball.field_92057_e = 1;
      double dDeltaLength = MathHelper.sqrt_double(dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ);
      double dUnitDeltaX = dDeltaX / dDeltaLength;
      double dUnitDeltaY = dDeltaY / dDeltaLength;
      double dUnitDeltaZ = dDeltaZ / dDeltaLength;
      fireball.posX = dFireballX + dUnitDeltaX * 4.0;
      fireball.posY = dFireballY + dUnitDeltaY * 4.0 - fireball.height / 2.0;
      fireball.posZ = dFireballZ + dUnitDeltaZ * 4.0;
      this.worldObj.spawnEntityInWorld(fireball);
      this.attackCounter = -40;
   }
}
