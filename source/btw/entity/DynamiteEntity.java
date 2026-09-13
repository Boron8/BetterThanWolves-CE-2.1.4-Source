package btw.entity;

import btw.BTWMod;
import btw.block.BTWBlocks;
import btw.entity.item.FloatingItemEntity;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.IProjectile;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class DynamiteEntity extends Entity implements IProjectile, EntityWithCustomPacket {
   public static final int TICKS_TO_DETONATE = 100;
   public int fuse;
   public int itemShiftedIndex;

   public DynamiteEntity(World world) {
      super(world);
      this.a(0.25F, 0.4F);
      this.fuse = -1;
      this.yOffset = 0.07F;
      this.preventEntitySpawning = true;
      this.itemShiftedIndex = 0;
      this.isImmuneToFire = true;
   }

   public DynamiteEntity(World world, int iItemShiftedIndex) {
      this(world);
      this.itemShiftedIndex = iItemShiftedIndex;
   }

   public DynamiteEntity(World world, EntityLiving entityliving, int iItemShiftedIndex, boolean bLit) {
      this(world, iItemShiftedIndex);
      this.b(entityliving.posX, entityliving.posY + entityliving.getEyeHeight(), entityliving.posZ, entityliving.rotationYaw, entityliving.rotationPitch);
      this.posX = this.posX - MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
      this.posY -= 0.1F;
      this.posZ = this.posZ - MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * 0.16F;
      this.b(this.posX, this.posY, this.posZ);
      float f = 0.4F;
      this.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f;
      this.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * 3.141593F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.141593F) * f;
      this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * 3.141593F) * f;
      this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, 0.75F, 1.0F);
      if (bLit) {
         this.fuse = 100;
      }
   }

   public DynamiteEntity(World world, double d, double d1, double d2, int iItemShiftedIndex) {
      this(world, iItemShiftedIndex);
      this.b(d, d1, d2);
   }

   @Override
   protected void entityInit() {
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      nbttagcompound.setInteger("m_iItemShiftedIndex", this.itemShiftedIndex);
      nbttagcompound.setInteger("m_iFuse", this.fuse);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      this.itemShiftedIndex = nbttagcompound.getInteger("m_iItemShiftedIndex");
      this.fuse = nbttagcompound.getInteger("m_iFuse");
   }

   @Override
   public void onUpdate() {
      if (!this.isDead) {
         int iPosI = MathHelper.floor_double(this.posX);
         int iPosJ = MathHelper.floor_double(this.posY);
         int iPosK = MathHelper.floor_double(this.posZ);
         int iInBlockID = this.worldObj.getBlockId(iPosI, iPosJ, iPosK);
         if (iInBlockID == Block.lavaMoving.blockID || iInBlockID == Block.lavaStill.blockID) {
            this.fuse = 1;
         }

         if (this.fuse > 0) {
            this.fuse--;
            if (this.fuse == 0) {
               this.w();
               if (!this.worldObj.isRemote) {
                  this.dynamiteExplode();
                  return;
               }

               this.fuse = 1;
            }

            float f3 = 0.25F;
            this.worldObj
               .spawnParticle(
                  "smoke",
                  this.posX - this.motionX * f3,
                  this.posY + 0.5 - this.motionY * f3,
                  this.posZ - this.motionZ * f3,
                  this.motionX * 0.1F,
                  this.motionY * 0.1F,
                  this.motionZ * 0.1F
               );
         } else if (iInBlockID == Block.fire.blockID || iInBlockID == BTWBlocks.stokedFire.blockID) {
            this.fuse = 100;
            this.worldObj.playSoundAtEntity(this, "random.fuse", 1.0F, 1.0F);
         } else if (this.onGround && Math.abs(this.motionX) < 0.01 && Math.abs(this.motionY) < 0.01 && Math.abs(this.motionZ) < 0.01 && !this.worldObj.isRemote
            )
          {
            this.convertToItem();
            return;
         }

         this.prevPosX = this.posX;
         this.prevPosY = this.posY;
         this.prevPosZ = this.posZ;
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

         this.A();
      }
   }

   @Override
   protected boolean shouldSetPositionOnLoad() {
      return false;
   }

   @Override
   public void setThrowableHeading(double dVectorX, double dVectorY, double dVectorZ, float fSpeed, float fRandomFactorMultiplier) {
      float fVectorLength = MathHelper.sqrt_double(dVectorX * dVectorX + dVectorY * dVectorY + dVectorZ * dVectorZ);
      dVectorX /= fVectorLength;
      dVectorY /= fVectorLength;
      dVectorZ /= fVectorLength;
      dVectorX += this.rand.nextGaussian() * 0.0075F * fRandomFactorMultiplier;
      dVectorY += this.rand.nextGaussian() * 0.0075F * fRandomFactorMultiplier;
      dVectorZ += this.rand.nextGaussian() * 0.0075F * fRandomFactorMultiplier;
      dVectorX *= fSpeed;
      dVectorY *= fSpeed;
      dVectorZ *= fSpeed;
      this.motionX = dVectorX;
      this.motionY = dVectorY;
      this.motionZ = dVectorZ;
      float fFlatVectorLength = MathHelper.sqrt_double(dVectorX * dVectorX + dVectorZ * dVectorZ);
      this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(dVectorX, dVectorZ) * 180.0 / (float) Math.PI);
      this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(dVectorY, fFlatVectorLength) * 180.0 / (float) Math.PI);
   }

   @Override
   public int getTrackerViewDistance() {
      return 64;
   }

   @Override
   public int getTrackerUpdateFrequency() {
      return 10;
   }

   @Override
   public boolean getTrackMotion() {
      return true;
   }

   @Override
   public boolean shouldServerTreatAsOversized() {
      return false;
   }

   @Override
   public Packet getSpawnPacketForThisEntity() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(6);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeInt(this.itemShiftedIndex);
         dataStream.writeInt(this.fuse);
         dataStream.writeByte((byte)(this.motionX * 128.0));
         dataStream.writeByte((byte)(this.motionY * 128.0));
         dataStream.writeByte((byte)(this.motionZ * 128.0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   private void dynamiteExplode() {
      float f = 1.5F;
      this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, f, true);
      int iExplosionI = MathHelper.floor_double(this.posX);
      int iExplosionJ = MathHelper.floor_double(this.posY);
      int iExplosionK = MathHelper.floor_double(this.posZ);
      int iTargetBlockID = this.worldObj.getBlockId(iExplosionI, iExplosionJ, iExplosionK);
      if (iTargetBlockID == Block.waterMoving.blockID || iTargetBlockID == Block.waterStill.blockID) {
         this.redneckFishing(iExplosionI, iExplosionJ, iExplosionK);
      }
   }

   private void redneckFishing(int i, int j, int k) {
      for (int tempI = i - 2; tempI <= i + 2; tempI++) {
         for (int tempJ = j - 2; tempJ <= j + 4; tempJ++) {
            for (int tempK = k - 2; tempK <= k + 2; tempK++) {
               if (this.isValidBlockForRedneckFishing(tempI, tempJ, tempK) && this.worldObj.rand.nextInt(25) == 0) {
                  this.spawnRedneckFish(tempI, tempJ, tempK);
               }
            }
         }
      }
   }

   private boolean isValidBlockForRedneckFishing(int i, int j, int k) {
      for (int tempI = i - 1; tempI <= i + 1; tempI++) {
         for (int tempJ = j - 1; tempJ <= j; tempJ++) {
            for (int tempK = k - 1; tempK <= k + 1; tempK++) {
               int iTargetBlockID = this.worldObj.getBlockId(tempI, tempJ, tempK);
               if (iTargetBlockID != Block.waterMoving.blockID && iTargetBlockID != Block.waterStill.blockID) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private void spawnRedneckFish(int i, int j, int k) {
      ItemStack stack = new ItemStack(Item.fishRaw.itemID, 1, 0);
      EntityItem entityItem;
      if (BTWMod.isHardcoreBuoyEnabled(this.worldObj)) {
         entityItem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, this.worldObj, i + 0.5F, j + 0.5F, k + 0.5F, stack);
      } else {
         entityItem = (EntityItem)EntityList.createEntityOfType(FloatingItemEntity.class, this.worldObj, i + 0.5F, j + 0.5F, k + 0.5F, stack);
      }

      this.worldObj.spawnEntityInWorld(entityItem);
   }

   private void convertToItem() {
      ItemUtils.ejectSingleItemWithRandomVelocity(this.worldObj, (float)this.posX, (float)this.posY, (float)this.posZ, BTWItems.dynamite.itemID, 0);
      this.w();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return 0.0F;
   }
}
