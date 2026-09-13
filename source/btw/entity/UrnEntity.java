package btw.entity;

import btw.block.BTWBlocks;
import btw.entity.mob.SnowmanEntity;
import btw.entity.mob.WitherEntityPersistent;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityThrowable;
import net.minecraft.src.MathHelper;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntitySkull;
import net.minecraft.src.World;

public class UrnEntity extends EntityThrowable implements EntityWithCustomPacket {
   public static final double CUBIC_RANGE = 4.0;
   public int itemShiftedIndex;

   public UrnEntity(World world) {
      super(world);
      this.itemShiftedIndex = 0;
   }

   public UrnEntity(World world, int iItemShiftedIndex) {
      this(world);
      this.itemShiftedIndex = iItemShiftedIndex;
   }

   public UrnEntity(World world, EntityLiving throwingEntity, int iItemShiftedIndex) {
      super(world, throwingEntity);
      this.itemShiftedIndex = iItemShiftedIndex;
   }

   public UrnEntity(World world, double d, double d1, double d2, int iItemShiftedIndex) {
      super(world, d, d1, d2);
      this.itemShiftedIndex = iItemShiftedIndex;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound nbttagcompound) {
      super.writeEntityToNBT(nbttagcompound);
      nbttagcompound.setInteger("m_iItemShiftedIndex", this.itemShiftedIndex);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound nbttagcompound) {
      super.readEntityFromNBT(nbttagcompound);
      this.itemShiftedIndex = nbttagcompound.getInteger("m_iItemShiftedIndex");
   }

   @Override
   protected void onImpact(MovingObjectPosition impactPosition) {
      this.w();
      if (!this.worldObj.isRemote) {
         if (this.itemShiftedIndex == BTWItems.soulUrn.itemID) {
            boolean looseSoul = true;
            if (impactPosition.entityHit != null) {
               impactPosition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.h()), 0);
               if (impactPosition.entityHit instanceof EntityCreature) {
                  looseSoul = !((EntityCreature)impactPosition.entityHit).canSoulAffectEntity(this);
               }
            } else if (attemptToCreateGolemOrWither(this.worldObj, impactPosition.blockX, impactPosition.blockY, impactPosition.blockZ)) {
               looseSoul = false;
            } else {
               Block impactBlock = Block.blocksList[this.worldObj.getBlockId(impactPosition.blockX, impactPosition.blockY, impactPosition.blockZ)];
               if (impactBlock != null
                  && impactBlock.attemptToAffectBlockWithSoul(this.worldObj, impactPosition.blockX, impactPosition.blockY, impactPosition.blockZ)) {
                  looseSoul = false;
               }
            }

            if (looseSoul) {
               this.looseSoulEffects(this.worldObj, impactPosition.hitVec.xCoord, impactPosition.hitVec.yCoord, impactPosition.hitVec.zCoord);
            }

            this.worldObj.playAuxSFX(2248, (int)Math.round(this.posX), (int)Math.round(this.posY), (int)Math.round(this.posZ), 0);
         }
      }
   }

   public void looseSoulEffects(World worldObj, double xCoord, double yCoord, double zCoord) {
      AxisAlignedBB possessionBox = AxisAlignedBB.getAABBPool().getAABB(xCoord - 4.0, yCoord - 4.0, zCoord - 4.0, xCoord + 4.0, yCoord + 4.0, zCoord + 4.0);

      for (Object nearbyCreature : worldObj.getEntitiesWithinAABB(EntityCreature.class, possessionBox)) {
         EntityCreature tempCreature = (EntityCreature)nearbyCreature;
         if (tempCreature.canSoulAffectEntity(this)) {
            tempCreature.a(DamageSource.causeThrownDamage(this, this.h()), 0);
            return;
         }
      }

      int range = 2;
      List blockPositions = new ArrayList();

      for (int i = (int)xCoord - range; i <= xCoord + range; i++) {
         for (int j = (int)yCoord - range; j <= yCoord + range; j++) {
            for (int k = (int)zCoord - range; k <= zCoord + range; k++) {
               blockPositions.add(new BlockPos(i, j, k));
            }
         }
      }

      Collections.shuffle(blockPositions, this.rand);

      for (Object blockPos : blockPositions) {
         BlockPos tempPos = (BlockPos)blockPos;
         if (attemptToCreateGolemOrWither(worldObj, tempPos.x, tempPos.y, tempPos.z)) {
            return;
         }

         Block tempBlock = Block.blocksList[worldObj.getBlockId(tempPos.x, tempPos.y, tempPos.z)];
         if (tempBlock != null && tempBlock.attemptToAffectBlockWithSoul(worldObj, tempPos.x, tempPos.y, tempPos.z)) {
            return;
         }
      }
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
         dataStream.writeInt(7);
         dataStream.writeInt(this.entityId);
         dataStream.writeInt(MathHelper.floor_double(this.posX * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posY * 32.0));
         dataStream.writeInt(MathHelper.floor_double(this.posZ * 32.0));
         dataStream.writeInt(this.itemShiftedIndex);
         dataStream.writeByte((byte)(this.motionX * 128.0));
         dataStream.writeByte((byte)(this.motionY * 128.0));
         dataStream.writeByte((byte)(this.motionZ * 128.0));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return new Packet250CustomPayload("BTW|SE", byteStream.toByteArray());
   }

   public static boolean attemptToCreateGolemOrWither(World world, int x, int y, int z) {
      for (int j = y; j <= y + 2; j++) {
         if (isGolemHeadBlock(world, x, j, z)) {
            return attemptToCreateSnowOrIronGolem(world, x, j, z);
         }

         if (isWitherHeadBlock(world, x, j, z)) {
            return attemptToCreateWither(world, x, j, z);
         }

         if (!isValidBodyBlockForSnowGolem(world, x, j, z) && !isValidBodyBlockForIronGolem(world, x, j, z) && !isWitherBodyBlock(world, x, j, z)) {
            break;
         }
      }

      if (isValidBodyBlockForIronGolem(world, x, y, z)) {
         int j = y + 1;

         for (int i = x - 1; i <= x + 1; i++) {
            for (int k = z - 1; k <= z + 1; k++) {
               if (isGolemHeadBlock(world, i, j, k)) {
                  return attemptToCreateSnowOrIronGolem(world, i, j, k);
               }
            }
         }
      }

      return false;
   }

   private static boolean isGolemHeadBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == Block.pumpkin.blockID || iBlockID == Block.pumpkinLantern.blockID;
   }

   private static boolean isWitherHeadBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID == Block.skull.blockID) {
         TileEntity tileEntity = world.getBlockTileEntity(i, j, k);
         if (tileEntity != null && tileEntity instanceof TileEntitySkull) {
            return ((TileEntitySkull)tileEntity).getSkullType() == 5;
         }
      }

      return false;
   }

   private static boolean isValidBodyBlockForSnowGolem(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == Block.blockSnow.blockID || iBlockID == BTWBlocks.looseSnow.blockID || iBlockID == BTWBlocks.solidSnow.blockID;
   }

   private static boolean isValidBodyBlockForIronGolem(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      return iBlockID == Block.blockIron.blockID;
   }

   private static boolean isWitherBodyBlock(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID == BTWBlocks.aestheticOpaque.blockID) {
         int iSubtype = world.getBlockMetadata(i, j, k);
         if (iSubtype == 15) {
            return true;
         }
      }

      return false;
   }

   private static boolean attemptToCreateSnowOrIronGolem(World world, int i, int j, int k) {
      if (isValidBodyBlockForSnowGolem(world, i, j - 1, k) && isValidBodyBlockForSnowGolem(world, i, j - 2, k)) {
         world.setBlock(i, j, k, 0);
         world.setBlock(i, j - 1, k, 0);
         world.setBlock(i, j - 2, k, 0);
         world.notifyBlockChange(i, j, k, 0);
         world.notifyBlockChange(i, j - 1, k, 0);
         world.notifyBlockChange(i, j - 2, k, 0);
         SnowmanEntity snowGolem = (SnowmanEntity)EntityList.createEntityOfType(SnowmanEntity.class, world);
         snowGolem.b(i + 0.5, j - 1.95, k + 0.5, 0.0F, 0.0F);
         world.spawnEntityInWorld(snowGolem);
         world.playAuxSFX(2263, i, j, k, 0);
         return true;
      } else if (isValidBodyBlockForIronGolem(world, i, j - 1, k) && isValidBodyBlockForIronGolem(world, i, j - 2, k)) {
         boolean bIronAlongIAxis = isValidBodyBlockForIronGolem(world, i - 1, j - 1, k) && isValidBodyBlockForIronGolem(world, i + 1, j - 1, k);
         boolean bIronAlongKAxis = isValidBodyBlockForIronGolem(world, i, j - 1, k - 1) && isValidBodyBlockForIronGolem(world, i, j - 1, k + 1);
         if (bIronAlongIAxis || bIronAlongKAxis) {
            world.setBlock(i, j, k, 0);
            world.setBlock(i, j - 1, k, 0);
            world.setBlock(i, j - 2, k, 0);
            if (bIronAlongIAxis) {
               world.setBlock(i - 1, j - 1, k, 0);
               world.setBlock(i + 1, j - 1, k, 0);
            } else {
               world.setBlock(i, j - 1, k - 1, 0);
               world.setBlock(i, j - 1, k + 1, 0);
            }

            world.notifyBlockChange(i, j, k, 0);
            world.notifyBlockChange(i, j - 1, k, 0);
            world.notifyBlockChange(i, j - 2, k, 0);
            if (bIronAlongIAxis) {
               world.notifyBlockChange(i - 1, j - 1, k, 0);
               world.notifyBlockChange(i + 1, j - 1, k, 0);
            } else {
               world.notifyBlockChange(i, j - 1, k - 1, 0);
               world.notifyBlockChange(i, j - 1, k + 1, 0);
            }

            EntityIronGolem ironGolem = (EntityIronGolem)EntityList.createEntityOfType(EntityIronGolem.class, world);
            ironGolem.setPlayerCreated(true);
            ironGolem.b(i + 0.5, j - 1.95, k + 0.5, 0.0F, 0.0F);
            world.spawnEntityInWorld(ironGolem);
            world.playAuxSFX(2264, i, j, k, 0);
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean attemptToCreateWither(World world, int i, int j, int k) {
      if (j >= 2 && world.provider.dimensionId == 0) {
         for (int iTempKOffset = -2; iTempKOffset <= 0; iTempKOffset++) {
            if (isWitherBodyBlock(world, i, j - 1, k + iTempKOffset)
               && isWitherBodyBlock(world, i, j - 1, k + iTempKOffset + 1)
               && isWitherBodyBlock(world, i, j - 2, k + iTempKOffset + 1)
               && isWitherBodyBlock(world, i, j - 1, k + iTempKOffset + 2)
               && isWitherHeadBlock(world, i, j, k + iTempKOffset)
               && isWitherHeadBlock(world, i, j, k + iTempKOffset + 1)
               && isWitherHeadBlock(world, i, j, k + iTempKOffset + 2)) {
               world.SetBlockMetadataWithNotify(i, j, k + iTempKOffset, 8, 2);
               world.SetBlockMetadataWithNotify(i, j, k + iTempKOffset + 1, 8, 2);
               world.SetBlockMetadataWithNotify(i, j, k + iTempKOffset + 2, 8, 2);
               world.setBlock(i, j, k + iTempKOffset, 0, 0, 2);
               world.setBlock(i, j, k + iTempKOffset + 1, 0, 0, 2);
               world.setBlock(i, j, k + iTempKOffset + 2, 0, 0, 2);
               world.setBlock(i, j - 1, k + iTempKOffset, 0, 0, 2);
               world.setBlock(i, j - 1, k + iTempKOffset + 1, 0, 0, 2);
               world.setBlock(i, j - 1, k + iTempKOffset + 2, 0, 0, 2);
               world.setBlock(i, j - 2, k + iTempKOffset + 1, 0, 0, 2);
               WitherEntityPersistent.summonWitherAtLocation(world, i, j, k + iTempKOffset + 1);
               world.notifyBlockChange(i, j, k + iTempKOffset, 0);
               world.notifyBlockChange(i, j, k + iTempKOffset + 1, 0);
               world.notifyBlockChange(i, j, k + iTempKOffset + 2, 0);
               world.notifyBlockChange(i, j - 1, k + iTempKOffset, 0);
               world.notifyBlockChange(i, j - 1, k + iTempKOffset + 1, 0);
               world.notifyBlockChange(i, j - 1, k + iTempKOffset + 2, 0);
               world.notifyBlockChange(i, j - 2, k + iTempKOffset + 1, 0);
               return true;
            }
         }

         for (int iTempIOffset = -2; iTempIOffset <= 0; iTempIOffset++) {
            if (isWitherBodyBlock(world, i + iTempIOffset, j - 1, k)
               && isWitherBodyBlock(world, i + iTempIOffset + 1, j - 1, k)
               && isWitherBodyBlock(world, i + iTempIOffset + 1, j - 2, k)
               && isWitherBodyBlock(world, i + iTempIOffset + 2, j - 1, k)
               && isWitherHeadBlock(world, i + iTempIOffset, j, k)
               && isWitherHeadBlock(world, i + iTempIOffset + 1, j, k)
               && isWitherHeadBlock(world, i + iTempIOffset + 2, j, k)) {
               world.SetBlockMetadataWithNotify(i + iTempIOffset, j, k, 8, 2);
               world.SetBlockMetadataWithNotify(i + iTempIOffset + 1, j, k, 8, 2);
               world.SetBlockMetadataWithNotify(i + iTempIOffset + 2, j, k, 8, 2);
               world.setBlock(i + iTempIOffset, j, k, 0, 0, 2);
               world.setBlock(i + iTempIOffset + 1, j, k, 0, 0, 2);
               world.setBlock(i + iTempIOffset + 2, j, k, 0, 0, 2);
               world.setBlock(i + iTempIOffset, j - 1, k, 0, 0, 2);
               world.setBlock(i + iTempIOffset + 1, j - 1, k, 0, 0, 2);
               world.setBlock(i + iTempIOffset + 2, j - 1, k, 0, 0, 2);
               world.setBlock(i + iTempIOffset + 1, j - 2, k, 0, 0, 2);
               WitherEntityPersistent.summonWitherAtLocation(world, i + iTempIOffset + 1, j, k);
               world.notifyBlockChange(i + iTempIOffset, j, k, 0);
               world.notifyBlockChange(i + iTempIOffset + 1, j, k, 0);
               world.notifyBlockChange(i + iTempIOffset + 2, j, k, 0);
               world.notifyBlockChange(i + iTempIOffset, j - 1, k, 0);
               world.notifyBlockChange(i + iTempIOffset + 1, j - 1, k, 0);
               world.notifyBlockChange(i + iTempIOffset + 2, j - 1, k, 0);
               world.notifyBlockChange(i + iTempIOffset + 1, j - 2, k, 0);
               return true;
            }
         }
      }

      return false;
   }
}
