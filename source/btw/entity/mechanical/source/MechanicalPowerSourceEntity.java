package btw.entity.mechanical.source;

import btw.entity.EntityWithCustomPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

public abstract class MechanicalPowerSourceEntity extends Entity implements EntityWithCustomPacket {
   private static final int ROTATION_SPEED_DATA_WATCHER_ID = 22;
   public float rotation;
   public int currentDamage;
   public int timeSinceHit;
   public int rockDirection;
   protected boolean providingPower = false;
   protected int fullUpdateTickCount;

   public MechanicalPowerSourceEntity(World world) {
      super(world);
      this.currentDamage = 0;
      this.timeSinceHit = 0;
      this.rockDirection = 1;
      this.rotation = 0.0F;
      this.fullUpdateTickCount = 0;
      this.preventEntitySpawning = true;
      this.a(this.getWidth(), this.getHeight());
      this.yOffset = this.height / 2.0F;
   }

   public MechanicalPowerSourceEntity(World world, double x, double y, double z) {
      this(world);
      this.b(x, y, z);
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(22, new Integer(0));
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBox(Entity entity) {
      return entity.boundingBox;
   }

   @Override
   public AxisAlignedBB getBoundingBox() {
      return this.boundingBox;
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public void moveEntity(double deltaX, double deltaY, double deltaZ) {
      if (!this.isDead) {
         this.destroyWithDrop();
      }
   }

   @Override
   public void setFire(int i) {
   }

   @Override
   public boolean attackEntityFrom(DamageSource damageSource, int i) {
      if (this.isDead) {
         return true;
      } else {
         this.currentDamage += i * 5;
         this.rockDirection = -this.rockDirection;
         this.timeSinceHit = 10;
         if (!this.worldObj.isRemote) {
            Entity attackingEntity = damageSource.getEntity();
            if (attackingEntity instanceof EntityPlayer && ((EntityPlayer)attackingEntity).capabilities.isCreativeMode) {
               this.destroyWithDrop();
            } else {
               this.J();
               if (this.currentDamage > this.getMaxDamage()) {
                  this.destroyWithDrop();
               }
            }
         }

         return true;
      }
   }

   @Override
   public void onUpdate() {
      if (!this.isDead) {
         if (!this.worldObj.isRemote) {
            this.fullUpdateTickCount--;
            if (this.fullUpdateTickCount <= 0) {
               this.fullUpdateTickCount = this.getTicksPerFullUpdate();
               this.onFullUpdateServer();
            }

            this.updateRotationAndDamageState();
         } else {
            float m_fPrevRotation = this.rotation;
            this.updateRotationAndDamageState();
            int iNewOctant = (int)(this.rotation / 45.0F);
            int iOldOctant = (int)(m_fPrevRotation / 45.0F);
            if (iOldOctant != iNewOctant) {
               this.onClientRotationOctantChange();
            }
         }
      }
   }

   @Override
   protected boolean shouldSetPositionOnLoad() {
      return false;
   }

   @Override
   public boolean attractsLightning() {
      return true;
   }

   @Override
   public int getTrackerViewDistance() {
      return 160;
   }

   @Override
   public int getTrackerUpdateFrequency() {
      return 3;
   }

   @Override
   public boolean getTrackMotion() {
      return false;
   }

   @Override
   public boolean shouldServerTreatAsOversized() {
      return true;
   }

   public abstract float getWidth();

   public abstract float getHeight();

   public abstract float getDepth();

   public abstract void initBoundingBox();

   public abstract AxisAlignedBB getDeviceBounds();

   public abstract int getMaxDamage();

   public abstract int getTicksPerFullUpdate();

   public abstract void destroyWithDrop();

   public abstract boolean validateAreaAroundDevice();

   protected abstract boolean validateConnectedAxles();

   public abstract float computeRotation();

   public abstract void transferPowerStateToConnectedAxles();

   private void updateRotationAndDamageState() {
      this.rotation = this.rotation + this.getRotationSpeed();
      if (this.rotation > 360.0F) {
         this.rotation -= 360.0F;
      } else if (this.rotation < -360.0F) {
         this.rotation += 360.0F;
      }

      if (this.timeSinceHit > 0) {
         this.timeSinceHit--;
      }

      if (this.currentDamage > 0) {
         this.currentDamage--;
      }
   }

   protected void onClientRotationOctantChange() {
   }

   public boolean isClearOfBlockingEntities() {
      AxisAlignedBB deviceBounds = this.getDeviceBounds();
      return this.worldObj.checkNoEntityCollision(deviceBounds, this);
   }

   public float getRotationSpeed() {
      return this.dataWatcher.getWatchableObjectInt(22) / 100.0F;
   }

   public void setRotationSpeed(float fRotation) {
      this.dataWatcher.updateObject(22, (int)(fRotation * 100.0F));
   }

   public int getRotationSpeedScaled() {
      return this.dataWatcher.getWatchableObjectInt(22);
   }

   public void setRotationSpeedScaled(int iRotationSpeedScaled) {
      this.dataWatcher.updateObject(22, iRotationSpeedScaled);
   }

   protected void onFullUpdateServer() {
      if (this.validateAreaAroundDevice() && this.validateConnectedAxles()) {
         this.setRotationSpeed(this.computeRotation());
         float fCurrentSpeed = this.getRotationSpeed();
         boolean bNewPoweredState = false;
         if (fCurrentSpeed > 0.01F || fCurrentSpeed < -0.01F) {
            bNewPoweredState = true;
         }

         if (this.providingPower != bNewPoweredState) {
            this.providingPower = bNewPoweredState;
            this.transferPowerStateToConnectedAxles();
         }
      } else {
         this.destroyWithDrop();
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void performHurtAnimation() {
      this.rockDirection = -this.rockDirection;
      this.timeSinceHit = 10;
      this.currentDamage = this.currentDamage + this.currentDamage * 5;
   }
}
