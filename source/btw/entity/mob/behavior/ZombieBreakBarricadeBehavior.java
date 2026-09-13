package btw.entity.mob.behavior;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.MathHelper;
import net.minecraft.src.PathEntity;
import net.minecraft.src.PathNavigate;
import net.minecraft.src.PathPoint;
import net.minecraft.src.World;

public class ZombieBreakBarricadeBehavior extends EntityAIBase {
   private EntityLiving associatedEntity;
   private int doorPosX;
   private int doorPosY;
   private int doorPosZ;
   private Block targetBlock;
   private int breakingTime;
   private int field_75358_j = -1;

   public ZombieBreakBarricadeBehavior(EntityLiving par1EntityLiving) {
      this.associatedEntity = par1EntityLiving;
   }

   @Override
   public boolean shouldExecute() {
      if (this.associatedEntity.isCollidedHorizontally) {
         PathNavigate pathNavigate = this.associatedEntity.getNavigator();
         PathEntity path = pathNavigate.getPath();
         if (path != null && !path.isFinished() && pathNavigate.getCanBreakDoors()) {
            for (int iTempPathIndex = 0; iTempPathIndex < Math.min(path.getCurrentPathIndex() + 2, path.getCurrentPathLength()); iTempPathIndex++) {
               PathPoint tempPathPoint = path.getPathPointFromIndex(iTempPathIndex);
               if (!(this.associatedEntity.e(tempPathPoint.xCoord, this.associatedEntity.posY, tempPathPoint.zCoord) <= 2.25)) {
                  break;
               }

               this.doorPosX = tempPathPoint.xCoord;
               this.doorPosY = tempPathPoint.yCoord + 1;
               this.doorPosZ = tempPathPoint.zCoord;
               this.targetBlock = this.shouldBreakBarricadeAtPos(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ);
               if (this.targetBlock == null) {
                  this.doorPosY = tempPathPoint.yCoord;
                  this.targetBlock = this.shouldBreakBarricadeAtPos(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ);
                  if (this.targetBlock == null) {
                     this.doorPosY = MathHelper.floor_double(this.associatedEntity.posY + 1.0);
                     this.targetBlock = this.shouldBreakBarricadeAtPos(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ);
                     if (this.targetBlock == null) {
                        this.doorPosY = MathHelper.floor_double(this.associatedEntity.posY);
                        this.targetBlock = this.shouldBreakBarricadeAtPos(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ);
                     }
                  }
               }

               if (this.targetBlock != null) {
                  return true;
               }
            }

            this.doorPosX = MathHelper.floor_double(this.associatedEntity.posX);
            this.doorPosY = MathHelper.floor_double(this.associatedEntity.posY + 1.0);
            this.doorPosZ = MathHelper.floor_double(this.associatedEntity.posZ);
            this.targetBlock = this.shouldBreakBarricadeAtPos(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ);
            if (this.targetBlock == null) {
               this.doorPosY = MathHelper.floor_double(this.associatedEntity.posY);
               this.targetBlock = this.shouldBreakBarricadeAtPos(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ);
            }

            if (this.targetBlock != null) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   public void startExecuting() {
      this.breakingTime = 0;
   }

   @Override
   public boolean continueExecuting() {
      if (this.breakingTime <= 240 && this.associatedEntity.worldObj.getBlockId(this.doorPosX, this.doorPosY, this.doorPosZ) == this.targetBlock.blockID) {
         double dDistSqToDoor = this.associatedEntity.e(this.doorPosX, this.doorPosY, this.doorPosZ);
         return dDistSqToDoor < 4.0;
      } else {
         return false;
      }
   }

   @Override
   public void resetTask() {
      super.resetTask();
      this.associatedEntity.worldObj.destroyBlockInWorldPartially(this.associatedEntity.entityId, this.doorPosX, this.doorPosY, this.doorPosZ, -1);
   }

   @Override
   public void updateTask() {
      if (this.associatedEntity.getRNG().nextInt(20) == 0) {
         this.associatedEntity.worldObj.playAuxSFX(1010, this.doorPosX, this.doorPosY, this.doorPosZ, 0);
      }

      this.breakingTime++;
      int iModifiedBreakTime = (int)(this.breakingTime / 240.0F * 10.0F);
      if (iModifiedBreakTime != this.field_75358_j) {
         this.associatedEntity
            .worldObj
            .destroyBlockInWorldPartially(this.associatedEntity.entityId, this.doorPosX, this.doorPosY, this.doorPosZ, iModifiedBreakTime);
         this.field_75358_j = iModifiedBreakTime;
      }

      if (this.breakingTime == 240) {
         int iMetadata = this.associatedEntity.worldObj.getBlockMetadata(this.doorPosX, this.doorPosY, this.doorPosZ);
         this.associatedEntity.worldObj.setBlockToAir(this.doorPosX, this.doorPosY, this.doorPosZ);
         if (this.targetBlock.blockID != Block.doorWood.blockID && this.targetBlock.blockID != BTWBlocks.woodenDoor.blockID) {
            this.targetBlock.dropBlockAsItem(this.associatedEntity.worldObj, this.doorPosX, this.doorPosY, this.doorPosZ, iMetadata, 0);
         }

         this.associatedEntity.worldObj.playAuxSFX(1012, this.doorPosX, this.doorPosY, this.doorPosZ, 0);
         this.associatedEntity.worldObj.playAuxSFX(2001, this.doorPosX, this.doorPosY, this.doorPosZ, this.targetBlock.blockID);
      }
   }

   private Block shouldBreakBarricadeAtPos(World world, int i, int j, int k) {
      int iBlockID = world.getBlockId(i, j, k);
      if (iBlockID != 0) {
         Block block = Block.blocksList[iBlockID];
         if (block.isBreakableBarricade(world, i, j, k)) {
            return block;
         }
      }

      return null;
   }
}
