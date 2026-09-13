package btw.entity;

import btw.block.BTWBlocks;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.World;

public class SpiderWebEntity extends EntityThrowable implements EntityWithCustomPacket {
   public SpiderWebEntity(World world) {
      super(world);
   }

   public SpiderWebEntity(World world, int iItemShiftedIndex) {
      this(world);
   }

   public SpiderWebEntity(World world, EntityLiving throwingEntity) {
      super(world, throwingEntity);
   }

   public SpiderWebEntity(World world, double d, double d1, double d2) {
      super(world, d, d1, d2);
   }

   public SpiderWebEntity(World world, EntityLiving throwingEntity, Entity targetEntity) {
      super(world);
      this.setThrower(throwingEntity);
      this.a(0.25F, 0.25F);
      this.b(
         throwingEntity.posX,
         throwingEntity.posY + throwingEntity.getEyeHeight(),
         throwingEntity.posZ,
         throwingEntity.rotationYaw,
         throwingEntity.rotationPitch
      );
      this.posX = this.posX - MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
      this.posY -= 0.2;
      this.posZ = this.posZ - MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F;
      this.b(this.posX, this.posY, this.posZ);
      this.yOffset = 0.0F;
      double targetY = targetEntity.posY;
      if (this.worldObj
            .rayTraceBlocks_do_do(
               this.worldObj.getWorldVec3Pool().getVecFromPool(throwingEntity.posX, throwingEntity.posY + throwingEntity.getEyeHeight(), throwingEntity.posZ),
               this.worldObj.getWorldVec3Pool().getVecFromPool(targetEntity.posX, targetY, throwingEntity.posZ),
               false,
               true
            )
         != null) {
         targetY = targetEntity.posY + targetEntity.getEyeHeight() / 2.0F;
         if (this.worldObj
               .rayTraceBlocks_do_do(
                  this.worldObj
                     .getWorldVec3Pool()
                     .getVecFromPool(throwingEntity.posX, throwingEntity.posY + throwingEntity.getEyeHeight(), throwingEntity.posZ),
                  this.worldObj.getWorldVec3Pool().getVecFromPool(targetEntity.posX, targetY, throwingEntity.posZ),
                  false,
                  true
               )
            != null) {
            targetY = targetEntity.posY + targetEntity.getEyeHeight();
         }
      }

      double deltaX = targetEntity.posX - this.posX;
      double deltaY = targetY - this.posY;
      double deltaZ = targetEntity.posZ - this.posZ;
      this.c(deltaX, deltaY, deltaZ, 1.5F, 1.0F);
      this.motionY += 0.1F;
   }

   @Override
   protected void onImpact(MovingObjectPosition impactPos) {
      Entity entityHit = impactPos.entityHit;
      if (entityHit != null) {
         entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.h()), 0);
         if (!this.worldObj.isRemote) {
            int x = MathHelper.floor_double(entityHit.posX);
            int y = MathHelper.floor_double(entityHit.posY);
            int z = MathHelper.floor_double(entityHit.posZ);
            if (!this.attemptToPlaceWebInBlock(x, y - 1, z) && !this.attemptToPlaceWebInBlock(x, y, z)) {
               this.spawnTangledWebItem(x, y, z);
            }
         }
      } else if (!this.worldObj.isRemote) {
         BlockPos targetPos = new BlockPos(impactPos.blockX, impactPos.blockY, impactPos.blockZ, impactPos.sideHit);
         if (!this.attemptToPlaceWebInBlock(targetPos.x, targetPos.y, targetPos.z)) {
            this.spawnTangledWebItem(targetPos.x, targetPos.y, targetPos.z);
         }
      }

      this.w();
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
         dataStream.writeInt(10);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeByte((byte)(this.motionX * 128.0));
         dataStream.writeByte((byte)(this.motionY * 128.0));
         dataStream.writeByte((byte)(this.motionZ * 128.0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   private boolean attemptToPlaceWebInBlock(int x, int y, int z) {
      if (this.canWebReplaceBlock(x, y, z)) {
         this.worldObj.setBlockWithNotify(x, y, z, BTWBlocks.web.blockID);
         return true;
      } else {
         return false;
      }
   }

   private void spawnTangledWebItem(int x, int y, int z) {
      float f1 = 0.7F;
      double d = this.worldObj.rand.nextFloat() * f1 + (1.0F - f1) * 0.5;
      double d1 = this.worldObj.rand.nextFloat() * f1 + (1.0F - f1) * 0.5;
      double d2 = this.worldObj.rand.nextFloat() * f1 + (1.0F - f1) * 0.5;
      EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(
         EntityItem.class, this.worldObj, x + d, y + d1, z + d2, new ItemStack(BTWItems.tangledWeb)
      );
      entityitem.delayBeforeCanPickup = 10;
      this.worldObj.spawnEntityInWorld(entityitem);
   }

   private boolean canWebReplaceBlock(int i, int j, int k) {
      int iBlockID = this.worldObj.getBlockId(i, j, k);
      Block block = Block.blocksList[iBlockID];
      return block == null || block.canSpitWebReplaceBlock(this.worldObj, i, j, k);
   }
}
