package btw.entity.mechanical.platform;

import btw.block.BTWBlocks;
import btw.block.blocks.AnchorBlock;
import btw.block.blocks.RopeBlock;
import btw.block.tileentity.PulleyTileEntity;
import btw.entity.EntityWithCustomPacket;
import btw.entity.IgnoreServerValidationEntity;
import btw.entity.mechanical.source.MechanicalPowerSourceEntity;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
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

public class MovingAnchorEntity extends Entity implements EntityWithCustomPacket, IgnoreServerValidationEntity {
   public static final float MOVEMENT_SPEED = 0.05F;
   private static final int Y_MOTION_DATA_WATCHER_ID = 22;
   private static final int VEHICLE_SPAWN_PACKET_TYPE = 102;
   private BlockPos associatedPulleyPos = new BlockPos();
   private int associatedPulleyRopeStateCounter;
   private int oldBottomY;

   public MovingAnchorEntity(World world) {
      super(world);
      this.preventEntitySpawning = true;
      this.a(0.98F, 1.98F);
      this.yOffset = 0.5F;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
      this.associatedPulleyRopeStateCounter = -1;
      this.oldBottomY = 0;
   }

   public MovingAnchorEntity(World world, double x, double y, double z) {
      this(world);
      this.b(x, y, z);
      this.lastTickPosX = this.prevPosX = x;
      this.lastTickPosY = this.prevPosY = y;
      this.lastTickPosZ = this.prevPosZ = z;
      this.oldBottomY = MathHelper.floor_double(this.posY - this.yOffset);
   }

   public MovingAnchorEntity(World world, double x, double y, double z, BlockPos pulleyPos, int iMovementDirection) {
      this(world);
      this.associatedPulleyPos.x = pulleyPos.x;
      this.associatedPulleyPos.y = pulleyPos.y;
      this.associatedPulleyPos.z = pulleyPos.z;
      if (iMovementDirection > 0) {
         this.motionY = 0.05F;
      } else {
         this.motionY = -0.05F;
      }

      this.b(x, y, z);
      this.lastTickPosX = this.prevPosX = x;
      this.lastTickPosY = this.prevPosY = y;
      this.lastTickPosZ = this.prevPosZ = z;
      int associatedPulleyBlockID = this.worldObj.getBlockId(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
      if (associatedPulleyBlockID == BTWBlocks.pulley.blockID) {
         PulleyTileEntity tileEntityPulley = (PulleyTileEntity)this.worldObj
            .getBlockTileEntity(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
         if (tileEntityPulley != null) {
            this.associatedPulleyRopeStateCounter = tileEntityPulley.updateRopeStateCounter;
         }
      }

      this.oldBottomY = MathHelper.floor_double(this.posY - this.yOffset);
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(22, new Integer(0));
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("associatedPulleyPosI", this.associatedPulleyPos.x);
      nbttagcompound.setInteger("associatedPulleyPosJ", this.associatedPulleyPos.y);
      nbttagcompound.setInteger("associatedPulleyPosK", this.associatedPulleyPos.z);
      nbttagcompound.setInteger("m_iAssociatedPulleyRopeStateCounter", this.associatedPulleyRopeStateCounter);
      nbttagcompound.setInteger("m_iOldBottomJ", this.oldBottomY);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.associatedPulleyPos.x = nbttagcompound.getInteger("associatedPulleyPosI");
      this.associatedPulleyPos.y = nbttagcompound.getInteger("associatedPulleyPosJ");
      this.associatedPulleyPos.z = nbttagcompound.getInteger("associatedPulleyPosK");
      if (nbttagcompound.hasKey("m_iAssociatedPulleyRopeStateCounter")) {
         this.associatedPulleyRopeStateCounter = nbttagcompound.getInteger("m_iAssociatedPulleyRopeStateCounter");
      }

      if (nbttagcompound.hasKey("m_iOldBottomJ")) {
         this.oldBottomY = nbttagcompound.getInteger("m_iOldBottomJ");
      } else {
         this.oldBottomY = MathHelper.floor_double(this.posY - this.yOffset);
      }
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
      return AxisAlignedBB.getBoundingBox(
         this.boundingBox.minX,
         this.boundingBox.minY,
         this.boundingBox.minZ,
         this.boundingBox.maxX,
         this.boundingBox.minY + AnchorBlock.anchorBaseHeight,
         this.boundingBox.maxZ
      );
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

         int i = MathHelper.floor_double(this.posX);
         int k = MathHelper.floor_double(this.posZ);
         byte checkChunksRange = 35;
         if (this.worldObj.checkChunksExist(i - checkChunksRange, 0, k - checkChunksRange, i + checkChunksRange, 0, k + checkChunksRange)) {
            PulleyTileEntity tileEntityPulley = null;
            int iBlockAboveID = this.worldObj.getBlockId(i, this.oldBottomY + 1, k);
            boolean bForceValidation = false;
            if (!this.worldObj.isRemote) {
               int associatedPulleyBlockID = this.worldObj.getBlockId(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
               int i2BlockAboveID = this.worldObj.getBlockId(i, this.oldBottomY + 2, k);
               boolean bPauseMotion = false;
               if (associatedPulleyBlockID == BTWBlocks.pulley.blockID) {
                  if (iBlockAboveID == BTWBlocks.pulley.blockID
                     || iBlockAboveID == BTWBlocks.ropeBlock.blockID
                     || i2BlockAboveID == BTWBlocks.pulley.blockID
                     || i2BlockAboveID == BTWBlocks.ropeBlock.blockID) {
                     tileEntityPulley = (PulleyTileEntity)this.worldObj
                        .getBlockTileEntity(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
                     if (this.associatedPulleyRopeStateCounter == tileEntityPulley.updateRopeStateCounter) {
                        return;
                     }

                     if (this.motionY > 0.0) {
                        if (tileEntityPulley.isLowering()) {
                           this.motionY = -this.motionY;
                           bForceValidation = true;
                        }
                     } else if (tileEntityPulley.isRaising()) {
                        this.motionY = -this.motionY;
                        bForceValidation = true;
                     }

                     this.associatedPulleyRopeStateCounter = tileEntityPulley.updateRopeStateCounter;
                  }

                  this.setCorseYMotion(this.motionY);
               }

               if (this.motionY <= 0.01 && this.motionY >= -0.01) {
                  this.convertToBlock(i, this.oldBottomY, k);
                  return;
               }
            }

            this.moveEntityInternal(this.motionX, this.motionY, this.motionZ);
            double newPosY = this.posY;
            int newBottomJ = MathHelper.floor_double(newPosY - this.yOffset);
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(0.0, 0.15, 0.0));
            if (list != null && list.size() > 0) {
               for (int j1 = 0; j1 < list.size(); j1++) {
                  Entity entity = (Entity)list.get(j1);
                  if (entity.canBePushed() || entity instanceof EntityItem || entity instanceof EntityXPOrb) {
                     this.pushEntity(entity);
                  } else if (!entity.isDead && entity instanceof MechanicalPowerSourceEntity) {
                     MechanicalPowerSourceEntity entityDevice = (MechanicalPowerSourceEntity)entity;
                     entityDevice.destroyWithDrop();
                  }
               }
            }

            if (!this.worldObj.isRemote && (this.oldBottomY != newBottomJ || bForceValidation)) {
               if (this.motionY > 0.0) {
                  if (this.worldObj.getBlockId(i, newBottomJ, k) == BTWBlocks.ropeBlock.blockID) {
                     tileEntityPulley.attemptToRetractRope();
                  }

                  int iTargetBlockID = this.worldObj.getBlockId(i, newBottomJ + 1, k);
                  if (iTargetBlockID != BTWBlocks.ropeBlock.blockID
                     || tileEntityPulley == null
                     || !tileEntityPulley.isRaising()
                     || newBottomJ + 1 >= this.associatedPulleyPos.y) {
                     this.convertToBlock(i, newBottomJ, k);
                     return;
                  }
               } else {
                  boolean bEnoughRope = false;
                  if (tileEntityPulley != null) {
                     int iRopeRequiredToDescend = 2;
                     if (iBlockAboveID == BTWBlocks.pulley.blockID || iBlockAboveID == BTWBlocks.ropeBlock.blockID) {
                        iRopeRequiredToDescend = 1;
                        int iOldBlockID = this.worldObj.getBlockId(i, this.oldBottomY, k);
                        if (iOldBlockID == BTWBlocks.pulley.blockID || iOldBlockID == BTWBlocks.ropeBlock.blockID) {
                           iRopeRequiredToDescend = 0;
                        }
                     }

                     if (tileEntityPulley.getContainedRopeCount() >= iRopeRequiredToDescend) {
                        bEnoughRope = true;
                     } else {
                        bEnoughRope = false;
                     }
                  }

                  int iTargetBlockID = this.worldObj.getBlockId(i, newBottomJ, k);
                  boolean bStop = false;
                  if (tileEntityPulley != null && tileEntityPulley.isLowering() && bEnoughRope) {
                     if (!WorldUtils.isReplaceableBlock(this.worldObj, i, newBottomJ, k)) {
                        if (Block.blocksList[iTargetBlockID].blockMaterial.isSolid()
                           && iTargetBlockID != Block.web.blockID
                           && iTargetBlockID != BTWBlocks.web.blockID) {
                           bStop = true;
                        } else {
                           int iTargetMetadata = this.worldObj.getBlockMetadata(i, newBottomJ, k);
                           if (iTargetBlockID == BTWBlocks.ropeBlock.blockID) {
                              if (!this.returnRopeToPulley()) {
                                 Block.blocksList[iTargetBlockID].dropBlockAsItem(this.worldObj, i, newBottomJ, k, iTargetMetadata, 0);
                              }
                           } else {
                              this.worldObj.playAuxSFX(2252, i, newBottomJ, k, iTargetBlockID + (iTargetMetadata << 12));
                              Block.blocksList[iTargetBlockID].dropBlockAsItem(this.worldObj, i, newBottomJ, k, iTargetMetadata, 0);
                           }

                           this.worldObj.setBlockWithNotify(i, newBottomJ, k, 0);
                        }
                     }
                  } else {
                     bStop = true;
                  }

                  if (bStop) {
                     this.convertToBlock(i, this.oldBottomY, k);
                     return;
                  }

                  if (tileEntityPulley != null
                     && this.worldObj.getBlockId(i, newBottomJ + 1, k) != BTWBlocks.ropeBlock.blockID
                     && this.worldObj.getBlockId(i, newBottomJ + 1, k) != BTWBlocks.pulley.blockID) {
                     tileEntityPulley.attemptToDispenseRope();
                  }
               }

               this.oldBottomY = newBottomJ;
            }
         }
      }
   }

   @Override
   public void moveEntity(double deltaX, double deltaY, double deltaZ) {
      this.notifyAssociatedPulleyOfLossOfAnchorEntity();
      this.destroyAnchorWithDrop();
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
      return 102;
   }

   public void destroyAnchorWithDrop() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      ItemStack anchorStack = new ItemStack(BTWBlocks.anchor);
      ItemUtils.ejectStackWithRandomOffset(this.worldObj, i, j, k, anchorStack);
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
      int i1 = MathHelper.floor_double(this.getBoundingBox().minX + 0.001);
      int k1 = MathHelper.floor_double(this.getBoundingBox().minY + 0.001);
      int i2 = MathHelper.floor_double(this.getBoundingBox().minZ + 0.001);
      int k3 = MathHelper.floor_double(this.getBoundingBox().maxX - 0.001);
      int l3 = MathHelper.floor_double(this.getBoundingBox().maxY - 0.001);
      int i4 = MathHelper.floor_double(this.getBoundingBox().maxZ - 0.001);
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
      AxisAlignedBB collisionBox = this.getBoundingBox();
      double testZoneMaxY = collisionBox.maxY + 0.075;
      double entityMinY = entity.boundingBox.minY;
      if (entityMinY < testZoneMaxY) {
         if (entityMinY > collisionBox.maxY - 0.25) {
            if (entity instanceof EntityPlayer) {
               if (this.worldObj.isRemote) {
                  this.clientPushPlayer(entity);
               }
            } else {
               double entityYOffset = collisionBox.maxY + 0.01 - entityMinY;
               entity.setPosition(entity.posX, entity.posY + entityYOffset, entity.posZ);
               if (entity.riddenByEntity != null) {
                  entity.riddenByEntity.setPosition(entity.riddenByEntity.posX, entity.riddenByEntity.posY + entityYOffset, entity.riddenByEntity.posZ);
               }
            }
         } else if (entity instanceof EntityLiving && this.motionY < 0.0) {
            double entityMaxY = entity.boundingBox.maxY;
            if (collisionBox.minY < entityMaxY - 0.25 && testZoneMaxY > entityMaxY) {
               entity.attackEntityFrom(DamageSource.inWall, 1);
            }
         }
      }
   }

   public void forceStopByPlatform() {
      if (!this.isDead) {
         if (this.motionY > 0.0) {
            int i = MathHelper.floor_double(this.posX);
            int jAbove = MathHelper.floor_double(this.posY) + 1;
            int k = MathHelper.floor_double(this.posZ);
            int iBlockAboveID = this.worldObj.getBlockId(i, jAbove, k);
            if (iBlockAboveID == BTWBlocks.ropeBlock.blockID) {
               ((RopeBlock)BTWBlocks.ropeBlock).breakRope(this.worldObj, i, jAbove, k);
            }
         }

         int i = MathHelper.floor_double(this.posX);
         int j = MathHelper.floor_double(this.posY);
         int k = MathHelper.floor_double(this.posZ);
         this.convertToBlock(i, j, k);
      }
   }

   private void convertToBlock(int i, int j, int k) {
      boolean bCanPlace = true;
      int iTargetBlockID = this.worldObj.getBlockId(i, j, k);
      if (!WorldUtils.isReplaceableBlock(this.worldObj, i, j, k)) {
         if (iTargetBlockID == BTWBlocks.ropeBlock.blockID) {
            if (!this.returnRopeToPulley()) {
               ItemUtils.ejectSingleItemWithRandomOffset(this.worldObj, i, j, k, BTWItems.rope.itemID, 0);
            }
         } else if (Block.blocksList[iTargetBlockID].blockMaterial.isSolid() && iTargetBlockID != Block.web.blockID && iTargetBlockID != BTWBlocks.web.blockID) {
            bCanPlace = false;
         } else {
            int iTargetMetadata = this.worldObj.getBlockMetadata(i, j, k);
            Block.blocksList[iTargetBlockID].dropBlockAsItem(this.worldObj, i, j, k, iTargetMetadata, 0);
            this.worldObj.playAuxSFX(2252, i, j, k, iTargetBlockID + (iTargetMetadata << 12));
         }
      }

      if (bCanPlace) {
         this.worldObj.setBlockWithNotify(i, j, k, BTWBlocks.anchor.blockID);
         ((AnchorBlock)BTWBlocks.anchor).setFacing(this.worldObj, i, j, k, 1);
      } else {
         ItemUtils.ejectSingleItemWithRandomOffset(this.worldObj, i, j, k, BTWBlocks.anchor.blockID, 0);
      }

      this.notifyAssociatedPulleyOfLossOfAnchorEntity();
      this.w();
   }

   public boolean returnRopeToPulley() {
      int associatedPulleyBlockID = this.worldObj.getBlockId(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
      if (associatedPulleyBlockID == BTWBlocks.pulley.blockID) {
         PulleyTileEntity tileEntityPulley = (PulleyTileEntity)this.worldObj
            .getBlockTileEntity(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
         if (tileEntityPulley != null) {
            tileEntityPulley.addRopeToInventory();
            return true;
         }
      }

      return false;
   }

   private void notifyAssociatedPulleyOfLossOfAnchorEntity() {
      int associatedPulleyBlockID = this.worldObj.getBlockId(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
      if (associatedPulleyBlockID == BTWBlocks.pulley.blockID) {
         PulleyTileEntity tileEntityPulley = (PulleyTileEntity)this.worldObj
            .getBlockTileEntity(this.associatedPulleyPos.x, this.associatedPulleyPos.y, this.associatedPulleyPos.z);
         tileEntityPulley.notifyOfLossOfAnchorEntity();
      }
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
      AxisAlignedBB collisionBox = this.getBoundingBox();
      double entityYOffset = collisionBox.maxY + 0.01 - entityMinY;
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
