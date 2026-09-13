package btw.entity.mechanical.platform;

import btw.block.BTWBlocks;
import btw.entity.EntityWithCustomPacket;
import btw.entity.IgnoreServerValidationEntity;
import btw.entity.mechanical.source.MechanicalPowerSourceEntity;
import btw.item.util.ItemUtils;
import btw.util.MiscUtils;
import btw.world.util.WorldUtils;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet23VehicleSpawn;
import net.minecraft.src.World;

public class MovingPlatformEntity extends Entity implements EntityWithCustomPacket, IgnoreServerValidationEntity {
   private static final int Y_MOTION_DATA_WATCHER_ID = 22;
   private static final int VEHICLE_SPAWN_PACKET_TYPE = 103;
   private double associatedAnchorLastKnownXPos;
   private double associatedAnchorLastKnownYPos;
   private double associatedAnchorLastKnownZPos;

   public MovingPlatformEntity(World world) {
      super(world);
      this.preventEntitySpawning = true;
      this.a(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.associatedAnchorLastKnownXPos = 0.0;
      this.associatedAnchorLastKnownYPos = 0.0;
      this.associatedAnchorLastKnownZPos = 0.0;
   }

   public MovingPlatformEntity(World world, double x, double y, double z) {
      this(world, x, y, z, null);
   }

   public MovingPlatformEntity(World world, double x, double y, double z, MovingAnchorEntity entityMovingAnchor) {
      this(world);
      if (entityMovingAnchor != null) {
         this.associatedAnchorLastKnownXPos = entityMovingAnchor.posX;
         this.associatedAnchorLastKnownYPos = entityMovingAnchor.posY;
         this.associatedAnchorLastKnownZPos = entityMovingAnchor.posZ;
         this.motionY = entityMovingAnchor.motionY;
      }

      this.b(x, y, z);
      this.lastTickPosX = this.prevPosX = x;
      this.lastTickPosY = this.prevPosY = y;
      this.lastTickPosZ = this.prevPosZ = z;
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(22, new Integer(0));
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setDouble("m_AssociatedAnchorLastKnownXPos", this.associatedAnchorLastKnownXPos);
      nbttagcompound.setDouble("m_AssociatedAnchorLastKnownYPos", this.associatedAnchorLastKnownYPos);
      nbttagcompound.setDouble("m_AssociatedAnchorLastKnownZPos", this.associatedAnchorLastKnownZPos);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.associatedAnchorLastKnownXPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownXPos");
      this.associatedAnchorLastKnownYPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownYPos");
      this.associatedAnchorLastKnownZPos = nbttagcompound.getDouble("m_AssociatedAnchorLastKnownZPos");
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
   public void onUpdate() {
      if (!this.isDead) {
         if (this.worldObj.isRemote) {
            this.motionY = this.getCorseYMotion();
         }

         MovingAnchorEntity associatedMovingAnchor = null;
         boolean bPauseMotion = false;
         int i = MathHelper.floor_double(this.posX);
         int oldCenterJ = MathHelper.floor_double(this.posY);
         int k = MathHelper.floor_double(this.posZ);
         if (!this.worldObj.isRemote) {
            List list = this.worldObj
               .getEntitiesWithinAABB(
                  MovingAnchorEntity.class,
                  AxisAlignedBB.getAABBPool()
                     .getAABB(
                        this.associatedAnchorLastKnownXPos - 0.25,
                        this.associatedAnchorLastKnownYPos - 0.25,
                        this.associatedAnchorLastKnownZPos - 0.25,
                        this.associatedAnchorLastKnownXPos + 0.25,
                        this.associatedAnchorLastKnownYPos + 0.25,
                        this.associatedAnchorLastKnownZPos + 0.25
                     )
               );
            if (list != null && list.size() > 0) {
               associatedMovingAnchor = (MovingAnchorEntity)list.get(0);
               if (!associatedMovingAnchor.isDead) {
                  this.motionY = associatedMovingAnchor.posY - this.associatedAnchorLastKnownYPos;
                  if (this.motionY < 0.01 && this.motionY > -0.01) {
                     this.motionY = 0.0;
                     bPauseMotion = true;
                  }

                  this.associatedAnchorLastKnownXPos = associatedMovingAnchor.posX;
                  this.associatedAnchorLastKnownYPos = associatedMovingAnchor.posY;
                  this.associatedAnchorLastKnownZPos = associatedMovingAnchor.posZ;
               } else {
                  associatedMovingAnchor = null;
               }
            }

            this.setCorseYMotion(this.motionY);
         }

         double oldPosY = this.posY;
         this.moveEntityInternal(this.motionX, this.motionY, this.motionZ);
         double newPosY = this.posY;
         List collisionList = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.0, 0.15, 0.0));
         if (collisionList != null && collisionList.size() > 0) {
            for (int j1 = 0; j1 < collisionList.size(); j1++) {
               Entity entity = (Entity)collisionList.get(j1);
               if (entity.canBePushed() || entity instanceof EntityItem || entity instanceof EntityXPOrb) {
                  this.pushEntity(entity);
               } else if (!entity.isDead && entity instanceof MechanicalPowerSourceEntity) {
                  MechanicalPowerSourceEntity entityDevice = (MechanicalPowerSourceEntity)entity;
                  entityDevice.destroyWithDrop();
               }
            }
         }

         if (!this.worldObj.isRemote) {
            if (associatedMovingAnchor == null) {
               this.convertToBlock(i, oldCenterJ, k, null, this.motionY > 0.0);
               return;
            }

            if (!bPauseMotion) {
               if (this.motionY > 0.0) {
                  int newTopJ = MathHelper.floor_double(newPosY + 0.49F);
                  int iTargetBlockID = this.worldObj.getBlockId(i, newTopJ, k);
                  if (!WorldUtils.isReplaceableBlock(this.worldObj, i, newTopJ, k)) {
                     if (Block.blocksList[iTargetBlockID].blockMaterial.isSolid()
                        && iTargetBlockID != Block.web.blockID
                        && iTargetBlockID != BTWBlocks.web.blockID) {
                        this.convertToBlock(i, oldCenterJ, k, associatedMovingAnchor, true);
                        associatedMovingAnchor.forceStopByPlatform();
                        return;
                     }

                     int iTargetMetadata = this.worldObj.getBlockMetadata(i, newTopJ, k);
                     Block.blocksList[iTargetBlockID].dropBlockAsItem(this.worldObj, i, newTopJ, k, iTargetMetadata, 0);
                     this.worldObj.setBlockWithNotify(i, newTopJ, k, 0);
                     this.worldObj.playAuxSFX(2252, i, newTopJ, k, iTargetBlockID + (iTargetMetadata << 12));
                  }
               } else {
                  int newBottomJ = MathHelper.floor_double(newPosY - 0.49F);
                  int iTargetBlockID = this.worldObj.getBlockId(i, newBottomJ, k);
                  if (!WorldUtils.isReplaceableBlock(this.worldObj, i, newBottomJ, k)) {
                     if (Block.blocksList[iTargetBlockID].blockMaterial.isSolid()
                        && iTargetBlockID != Block.web.blockID
                        && iTargetBlockID != BTWBlocks.web.blockID) {
                        this.convertToBlock(i, oldCenterJ, k, associatedMovingAnchor, false);
                        associatedMovingAnchor.forceStopByPlatform();
                        return;
                     }

                     int iTargetMetadata = this.worldObj.getBlockMetadata(i, newBottomJ, k);
                     Block.blocksList[iTargetBlockID].dropBlockAsItem(this.worldObj, i, newBottomJ, k, iTargetMetadata, 0);
                     this.worldObj.setBlockWithNotify(i, newBottomJ, k, 0);
                     this.worldObj.playAuxSFX(2252, i, newBottomJ, k, iTargetBlockID + (iTargetMetadata << 12));
                  }
               }
            }
         }
      }
   }

   @Override
   public void moveEntity(double deltaX, double deltaY, double deltaZ) {
      this.destroyPlatformWithDrop();
   }

   @Override
   protected boolean shouldSetPositionOnLoad() {
      return false;
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      return new Packet23VehicleSpawn(this, getVehicleSpawnPacketType(), 0);
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
      return false;
   }

   private double getCorseYMotion() {
      return this.dataWatcher.getWatchableObjectInt(22) / 100.0;
   }

   private void setCorseYMotion(double yMotion) {
      this.dataWatcher.updateObject(22, (int)(yMotion * 100.0));
   }

   public static int getVehicleSpawnPacketType() {
      return 103;
   }

   public void destroyPlatformWithDrop() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      ItemStack platformStack = new ItemStack(BTWBlocks.platform);
      ItemUtils.ejectStackWithRandomOffset(this.worldObj, i, j, k, platformStack);
      this.w();
   }

   private void moveEntityInternal(double deltaX, double deltaY, double deltaZ) {
      double newPosX = this.posX + deltaX;
      double newPosY = this.posY + deltaY;
      double newPosZ = this.posZ + deltaZ;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.b(newPosX, newPosY, newPosZ);
      this.testForBlockCollisions();
   }

   private void testForBlockCollisions() {
      int i1 = MathHelper.floor_double(this.boundingBox.minX + 0.001);
      int k1 = MathHelper.floor_double(this.boundingBox.minY + 0.001);
      int i2 = MathHelper.floor_double(this.boundingBox.minZ + 0.001);
      int k3 = MathHelper.floor_double(this.boundingBox.maxX - 0.001);
      int l3 = MathHelper.floor_double(this.boundingBox.maxY - 0.001);
      int i4 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001);
      if (this.worldObj.checkChunksExist(i1, k1, i2, k3, l3, i4)) {
         for (int j4 = i1; j4 <= k3; j4++) {
            for (int k4 = k1; k4 <= l3; k4++) {
               for (int l4 = i2; l4 <= i4; l4++) {
                  int i5 = this.worldObj.getBlockId(j4, k4, l4);
                  if (i5 > 0) {
                     Block.blocksList[i5].onEntityCollidedWithBlock(this.worldObj, j4, k4, l4, this);
                  }
               }
            }
         }
      }
   }

   private void pushEntity(Entity entity) {
      double testZoneMaxY = this.boundingBox.maxY + 0.075;
      double entityMinY = entity.boundingBox.minY;
      if (entityMinY < testZoneMaxY) {
         if (entityMinY > this.boundingBox.maxY - 0.5) {
            if (entity instanceof EntityPlayer) {
               if (this.worldObj.isRemote) {
                  this.clientPushPlayer(entity);
               }
            } else {
               double entityYOffset = this.boundingBox.maxY + 0.01 - entityMinY;
               entity.setPosition(entity.posX, entity.posY + entityYOffset, entity.posZ);
               if (entity.riddenByEntity != null) {
                  entity.riddenByEntity.setPosition(entity.riddenByEntity.posX, entity.riddenByEntity.posY + entityYOffset, entity.riddenByEntity.posZ);
               }
            }
         } else if (entity instanceof EntityLiving && this.motionY < 0.0) {
            double entityMaxY = entity.boundingBox.maxY;
            if (this.boundingBox.minY < entityMaxY - 0.25 && testZoneMaxY > entityMaxY) {
               entity.attackEntityFrom(DamageSource.inWall, 1);
            }
         }
      }
   }

   private void convertToBlock(int i, int j, int k, MovingAnchorEntity associatedAnchor, boolean bMovingUpwards) {
      boolean moveEntities = true;
      int iTargetBlockID = this.worldObj.getBlockId(i, j, k);
      if (WorldUtils.isReplaceableBlock(this.worldObj, i, j, k)) {
         this.worldObj.setBlockWithNotify(i, j, k, BTWBlocks.platform.blockID);
      } else if (Block.blocksList[iTargetBlockID].blockMaterial.isSolid() && iTargetBlockID != Block.web.blockID && iTargetBlockID != BTWBlocks.web.blockID) {
         ItemUtils.ejectSingleItemWithRandomOffset(this.worldObj, i, j, k, BTWBlocks.platform.blockID, 0);
         moveEntities = false;
      } else {
         int iTargetMetadata = this.worldObj.getBlockMetadata(i, j, k);
         Block.blocksList[iTargetBlockID].dropBlockAsItem(this.worldObj, i, j, k, iTargetMetadata, 0);
         this.worldObj.playAuxSFX(2252, i, j, k, iTargetBlockID + (iTargetMetadata << 12));
         this.worldObj.setBlockWithNotify(i, j, k, BTWBlocks.platform.blockID);
      }

      MiscUtils.positionAllNonPlayerMoveableEntitiesOutsideOfLocation(this.worldObj, i, j, k);
      if (!bMovingUpwards) {
         MiscUtils.serverPositionAllPlayerEntitiesOutsideOfLocation(this.worldObj, i, j + 1, k);
         MiscUtils.serverPositionAllPlayerEntitiesOutsideOfLocation(this.worldObj, i, j, k);
      } else {
         MiscUtils.serverPositionAllPlayerEntitiesOutsideOfLocation(this.worldObj, i, j - 1, k);
         MiscUtils.serverPositionAllPlayerEntitiesOutsideOfLocation(this.worldObj, i, j, k);
      }

      this.w();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.b(par1, par3, par5);
   }

   @Environment(EnvType.CLIENT)
   private void clientPushPlayer(Entity entity) {
      double entityMinY = entity.boundingBox.minY;
      double entityYOffset = this.boundingBox.maxY + 0.01 - entityMinY;
      entity.setPosition(entity.posX, entity.posY + entityYOffset, entity.posZ);
      entity.serverPosX = (int)(entity.posX * 32.0);
      entity.serverPosY = (int)(entity.posY * 32.0);
      entity.serverPosZ = (int)(entity.posZ * 32.0);
      if (entity.riddenByEntity != null) {
         entity.riddenByEntity.setPosition(entity.riddenByEntity.posX, entity.riddenByEntity.posY + entityYOffset, entity.riddenByEntity.posZ);
      }

      entity.onGround = true;
   }
}
