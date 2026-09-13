package btw.entity.mechanical.platform;

import btw.block.BTWBlocks;
import btw.entity.EntityWithCustomPacket;
import btw.entity.IgnoreServerValidationEntity;
import btw.item.util.ItemUtils;
import btw.world.util.WorldUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRailBase;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class BlockLiftedByPlatformEntity extends Entity implements EntityWithCustomPacket, IgnoreServerValidationEntity {
   private int blockID;
   private int blockMetadata;

   public BlockLiftedByPlatformEntity(World world) {
      super(world);
      this.preventEntitySpawning = true;
      this.a(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
      this.motionX = 0.0;
      this.motionY = 0.0;
      this.motionZ = 0.0;
   }

   public BlockLiftedByPlatformEntity(World world, int i, int j, int k) {
      this(world);
      int iBlockID = world.getBlockId(i, j, k);
      int iMetadata = world.getBlockMetadata(i, j, k);
      if (iBlockID == Block.railPowered.blockID
         || iBlockID == Block.railDetector.blockID
         || iBlockID == BTWBlocks.woodenDetectorRail.blockID
         || iBlockID == BTWBlocks.steelDetectorRail.blockID) {
         iMetadata &= 7;
      } else if (iBlockID == Block.redstoneWire.blockID) {
         iMetadata = 0;
      }

      this.setBlockID(iBlockID);
      this.setBlockMetadata(iMetadata);
      this.b(i + 0.5F, j + 0.5F, k + 0.5F);
      this.lastTickPosX = this.prevPosX = this.posX;
      this.lastTickPosY = this.prevPosY = this.posY;
      this.lastTickPosZ = this.prevPosZ = this.posZ;
      world.spawnEntityInWorld(this);
      world.setBlockWithNotify(i, j, k, 0);
   }

   public BlockLiftedByPlatformEntity(World world, double x, double y, double z) {
      this(world);
      this.posX = x;
      this.posY = y;
      this.posZ = z;
      this.lastTickPosX = this.prevPosX = this.posX;
      this.lastTickPosY = this.prevPosY = this.posY;
      this.lastTickPosZ = this.prevPosZ = this.posZ;
   }

   public BlockLiftedByPlatformEntity(World world, double x, double y, double z, int iBlockID, int iMetadata) {
      this(world, x, y, z);
      this.blockID = iBlockID;
      this.blockMetadata = iMetadata;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("m_iBlockID", this.getBlockID());
      nbttagcompound.setInteger("m_iBlockMetaData", this.getBlockMetadata());
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.setBlockID(nbttagcompound.getInteger("m_iBlockID"));
      this.setBlockMetadata(nbttagcompound.getInteger("m_iBlockMetaData"));
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public AxisAlignedBB getCollisionBox(Entity entity) {
      return null;
   }

   @Override
   public AxisAlignedBB getBoundingBox() {
      return null;
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public void applyEntityCollision(Entity entity) {
   }

   @Override
   public void onUpdate() {
      if (!this.isDead) {
         MovingPlatformEntity associatedMovingPlatform = null;
         List collisionList = this.worldObj
            .getEntitiesWithinAABB(
               MovingPlatformEntity.class,
               AxisAlignedBB.getAABBPool().getAABB(this.posX - 0.25, this.posY - 1.25, this.posZ - 0.25, this.posX + 0.25, this.posY - 0.75, this.posZ + 0.25)
            );
         if (collisionList != null && collisionList.size() > 0) {
            associatedMovingPlatform = (MovingPlatformEntity)collisionList.get(0);
            if (!associatedMovingPlatform.isDead) {
               double newPosX = associatedMovingPlatform.posX;
               double newPosY = associatedMovingPlatform.posY + 1.0;
               double newPosZ = associatedMovingPlatform.posZ;
               this.prevPosX = this.posX;
               this.prevPosY = this.posY;
               this.prevPosZ = this.posZ;
               this.b(newPosX, newPosY, newPosZ);
            } else {
               associatedMovingPlatform = null;
            }
         }

         if (!this.worldObj.isRemote && associatedMovingPlatform == null) {
            int i = MathHelper.floor_double(this.posX);
            int j = MathHelper.floor_double(this.posY);
            int k = MathHelper.floor_double(this.posZ);
            this.convertToBlock(i, j, k);
         }
      }
   }

   @Override
   public void moveEntity(double deltaX, double deltaY, double deltaZ) {
      this.destroyBlockWithDrop();
   }

   @Override
   protected boolean shouldSetPositionOnLoad() {
      return false;
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(8);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeInt(this.getBlockID());
         dataStream.writeInt(this.getBlockMetadata());
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
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

   public int getBlockID() {
      return this.blockID;
   }

   public void setBlockID(int iBlockID) {
      this.blockID = iBlockID;
   }

   public int getBlockMetadata() {
      return this.blockMetadata;
   }

   public void setBlockMetadata(int iMetadata) {
      this.blockMetadata = iMetadata;
   }

   public void destroyBlockWithDrop() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      int idDropped = Block.blocksList[this.getBlockID()].idDropped(0, this.worldObj.rand, 0);
      if (idDropped > 0) {
         ItemUtils.ejectSingleItemWithRandomOffset(this.worldObj, i, j, k, idDropped, 0);
      }

      this.w();
   }

   private void convertToBlock(int i, int j, int k) {
      boolean bDestroyBlock = true;
      if (this.worldObj.getBlockId(i, j - 1, k) == BTWBlocks.platform.blockID && WorldUtils.isReplaceableBlock(this.worldObj, i, j, k)) {
         this.worldObj.setBlockAndMetadataWithNotify(i, j, k, this.getBlockID(), this.getBlockMetadata());
         bDestroyBlock = false;
      }

      if (bDestroyBlock) {
         this.destroyBlockWithDrop();
      } else {
         this.w();
      }
   }

   public static boolean canBlockBeConvertedToEntity(World world, int i, int j, int k) {
      int iTargetBlockID = world.getBlockId(i, j, k);
      Block targetBlock = Block.blocksList[iTargetBlockID];
      if (targetBlock != null) {
         if (targetBlock instanceof BlockRailBase) {
            int iTargetMetaData = world.getBlockMetadata(i, j, k);
            if (iTargetMetaData >= 2 & iTargetMetaData <= 5) {
               return false;
            }

            return true;
         }

         if (iTargetBlockID == Block.redstoneWire.blockID) {
            return true;
         }
      }

      return false;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
   }
}
