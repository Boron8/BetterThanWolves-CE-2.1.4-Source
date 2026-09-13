package btw.entity;

import btw.world.util.BlockPos;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class MiningChargeEntity extends Entity implements EntityWithCustomPacket {
   public int fuse = 0;
   public int facing = 0;
   public boolean attachedToBlock = true;

   public MiningChargeEntity(World world) {
      super(world);
      this.preventEntitySpawning = true;
      this.a(0.98F, 0.98F);
      this.yOffset = this.height / 2.0F;
   }

   public MiningChargeEntity(World world, int i, int j, int k, int iFacing) {
      this(world);
      this.fuse = 80;
      this.facing = iFacing;
      this.b(i + 0.5F, j + 0.5F, k + 0.5F);
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
   }

   public MiningChargeEntity(World world, double x, double y, double z, int iFacing, int iFuse, boolean bAttachedToBlock) {
      this(world);
      this.b(x, y, z);
      this.facing = iFacing;
      this.fuse = iFuse;
      this.attachedToBlock = bAttachedToBlock;
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public boolean canBePushed() {
      return false;
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public boolean attackEntityFrom(DamageSource damagesource, int i) {
      if (damagesource.isExplosion() && this.fuse > 1) {
         this.fuse = 1;
      }

      this.J();
      return false;
   }

   @Override
   public void onUpdate() {
      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      if (this.attachedToBlock) {
         boolean bStillAttached = false;
         BlockPos attachedBlockPos = new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
         attachedBlockPos.addFacingAsOffset(this.facing);
         if (this.worldObj.isBlockNormalCube(attachedBlockPos.x, attachedBlockPos.y, attachedBlockPos.z)
            || this.facing == 0 && this.worldObj.doesBlockHaveSolidTopSurface(attachedBlockPos.x, attachedBlockPos.y, attachedBlockPos.z)) {
            bStillAttached = true;
         }

         this.attachedToBlock = bStillAttached;
      }

      if (!this.attachedToBlock) {
         if (this.facing == 1) {
            this.facing = 0;
         }

         this.motionY -= 0.04F;
         this.d(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.98F;
         this.motionY *= 0.98F;
         this.motionZ *= 0.98F;
         if (this.onGround) {
            this.motionX *= 0.7F;
            this.motionZ *= 0.7F;
            this.motionY *= -0.5;
         }
      }

      if (this.fuse-- <= 0) {
         if (!this.worldObj.isRemote) {
            this.w();
            this.explode();
         } else {
            this.fuse = 0;
         }
      } else {
         this.worldObj.spawnParticle("smoke", this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0);
      }
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setByte("m_iFuse", (byte)this.fuse);
      nbttagcompound.setByte("m_iFacing", (byte)this.facing);
      nbttagcompound.setBoolean("m_bAttachedToBlock", this.attachedToBlock);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.fuse = nbttagcompound.getByte("m_iFuse");
      this.facing = nbttagcompound.getByte("m_iFacing");
      this.attachedToBlock = nbttagcompound.getBoolean("m_bAttachedToBlock");
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
         dataStream.writeInt(3);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt((int)(this.posX * 32.0));
         dataStream.writeInt((int)(this.posY * 32.0));
         dataStream.writeInt((int)(this.posZ * 32.0));
         dataStream.writeByte((byte)this.facing);
         dataStream.writeByte((byte)this.fuse);
         dataStream.writeByte(this.attachedToBlock ? 1 : 0);
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
      return 10;
   }

   @Override
   public boolean getTrackMotion() {
      return false;
   }

   @Override
   public boolean shouldServerTreatAsOversized() {
      return false;
   }

   private void explode() {
      MiningChargeExplosion explosion = new MiningChargeExplosion(this.worldObj, this.posX, this.posY, this.posZ, this.facing);
      explosion.doExplosion();
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
}
