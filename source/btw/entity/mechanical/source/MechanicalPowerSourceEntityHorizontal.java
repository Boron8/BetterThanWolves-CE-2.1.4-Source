package btw.entity.mechanical.source;

import btw.block.BTWBlocks;
import btw.block.blocks.AxleBlock;
import btw.block.util.MechPowerUtils;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public abstract class MechanicalPowerSourceEntityHorizontal extends MechanicalPowerSourceEntity {
   public boolean alignedToX;

   public MechanicalPowerSourceEntityHorizontal(World world) {
      super(world);
      this.alignedToX = true;
   }

   public MechanicalPowerSourceEntityHorizontal(World world, double x, double y, double z, boolean bIAligned) {
      super(world, x, y, z);
      this.alignedToX = bIAligned;
      this.initBoundingBox();
   }

   @Override
   public void setDead() {
      if (this.providingPower) {
         int iCenterI = MathHelper.floor_double(this.posX);
         int iCenterJ = MathHelper.floor_double(this.posY);
         int iCenterK = MathHelper.floor_double(this.posZ);
         int iCenterBlockID = this.worldObj.getBlockId(iCenterI, iCenterJ, iCenterK);
         if (iCenterBlockID == BTWBlocks.axlePowerSource.blockID) {
            int iAxisAlignment = ((AxleBlock)BTWBlocks.axlePowerSource).getAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK);
            this.worldObj.setBlockWithNotify(iCenterI, iCenterJ, iCenterK, BTWBlocks.axle.blockID);
            ((AxleBlock)BTWBlocks.axle).setAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment);
         }
      }

      super.w();
   }

   @Override
   protected boolean validateConnectedAxles() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      int iCenterBlockID = this.worldObj.getBlockId(iCenterI, iCenterJ, iCenterK);
      if (!MechPowerUtils.isBlockIDAxle(iCenterBlockID)) {
         return false;
      } else {
         AxleBlock centerAxleBlock = (AxleBlock)Block.blocksList[iCenterBlockID];
         int iAxisAlignment = centerAxleBlock.getAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK);
         if (iAxisAlignment != 0 && (iAxisAlignment != 1 || !this.alignedToX) && (iAxisAlignment != 2 || this.alignedToX)) {
            if (!this.providingPower) {
               if (iCenterBlockID == BTWBlocks.axlePowerSource.blockID) {
                  this.worldObj.setBlockWithNotify(iCenterI, iCenterJ, iCenterK, BTWBlocks.axle.blockID);
                  ((AxleBlock)BTWBlocks.axle).setAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment);
               } else if (centerAxleBlock.getPowerLevel(this.worldObj, iCenterI, iCenterJ, iCenterK) > 0) {
                  return false;
               }
            } else if (iCenterBlockID == BTWBlocks.axle.blockID) {
               this.worldObj.setBlockWithNotify(iCenterI, iCenterJ, iCenterK, BTWBlocks.axlePowerSource.blockID);
               ((AxleBlock)BTWBlocks.axlePowerSource).setAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment);
            }

            return true;
         } else {
            return false;
         }
      }
   }

   @Override
   public void transferPowerStateToConnectedAxles() {
      int iCenterI = MathHelper.floor_double(this.posX);
      int iCenterJ = MathHelper.floor_double(this.posY);
      int iCenterK = MathHelper.floor_double(this.posZ);
      int iCenterBlockID = this.worldObj.getBlockId(iCenterI, iCenterJ, iCenterK);
      AxleBlock centerAxleBlock = (AxleBlock)Block.blocksList[iCenterBlockID];
      int iAxisAlignment = centerAxleBlock.getAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK);
      if (this.providingPower) {
         if (iCenterBlockID == BTWBlocks.axle.blockID) {
            this.worldObj.setBlockWithNotify(iCenterI, iCenterJ, iCenterK, BTWBlocks.axlePowerSource.blockID);
            ((AxleBlock)BTWBlocks.axlePowerSource).setAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment);
         }
      } else if (iCenterBlockID == BTWBlocks.axlePowerSource.blockID) {
         this.worldObj.setBlockWithNotify(iCenterI, iCenterJ, iCenterK, BTWBlocks.axle.blockID);
         ((AxleBlock)BTWBlocks.axle).setAxisAlignment(this.worldObj, iCenterI, iCenterJ, iCenterK, iAxisAlignment);
      }
   }

   @Override
   public void initBoundingBox() {
      if (this.alignedToX) {
         this.boundingBox
            .setBounds(
               this.posX - this.getDepth() * 0.5F,
               this.posY - this.getHeight() * 0.5F,
               this.posZ - this.getWidth() * 0.5F,
               this.posX + this.getDepth() * 0.5F,
               this.posY + this.getHeight() * 0.5F,
               this.posZ + this.getWidth() * 0.5F
            );
      } else {
         this.boundingBox
            .setBounds(
               this.posX - this.getWidth() * 0.5F,
               this.posY - this.getHeight() * 0.5F,
               this.posZ - this.getDepth() * 0.5F,
               this.posX + this.getWidth() * 0.5F,
               this.posY + this.getHeight() * 0.5F,
               this.posZ + this.getDepth() * 0.5F
            );
      }
   }

   @Override
   public AxisAlignedBB getDeviceBounds() {
      return this.alignedToX
         ? AxisAlignedBB.getAABBPool()
            .getAABB(
               this.posX - this.getDepth() * 0.5F,
               this.posY - this.getHeight() * 0.5F,
               this.posZ - this.getWidth() * 0.5F,
               this.posX + this.getDepth() * 0.5F,
               this.posY + this.getHeight() * 0.5F,
               this.posZ + this.getWidth() * 0.5F
            )
         : AxisAlignedBB.getAABBPool()
            .getAABB(
               this.posX - this.getWidth() * 0.5F,
               this.posY - this.getHeight() * 0.5F,
               this.posZ - this.getDepth() * 0.5F,
               this.posX + this.getWidth() * 0.5F,
               this.posY + this.getHeight() * 0.5F,
               this.posZ + this.getDepth() * 0.5F
            );
   }
}
