package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.TurntableBlock;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class TurntableTileEntity extends TileEntity {
   private final int maxHeightOfBlocksRotated = 2;
   private int rotationTickCount = 0;
   public int craftingRotationCount = 0;
   private static int[] ticksToRotate = new int[]{10, 20, 40, 80, 200, 600, 1200, 2400, 6000, 12000, 24000};
   private int switchOverride = -1;

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      this.rotationTickCount = nbttagcompound.getInteger("m_iRotationCount");
      if (nbttagcompound.hasKey("m_iSwitchSetting")) {
         this.switchOverride = nbttagcompound.getInteger("m_iSwitchSetting");
         if (this.switchOverride > 3) {
            this.switchOverride = 3;
         }
      }

      if (nbttagcompound.hasKey("m_iPotteryRotationCount")) {
         this.craftingRotationCount = nbttagcompound.getInteger("m_iPotteryRotationCount");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("m_iRotationCount", this.rotationTickCount);
      nbttagcompound.setInteger("m_iPotteryRotationCount", this.craftingRotationCount);
   }

   @Override
   public void updateEntity() {
      if (this.worldObj.isRemote) {
         if (((TurntableBlock)BTWBlocks.turntable).isBlockMechanicalOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
            this.rotationTickCount++;
            if (this.rotationTickCount >= this.getTicksToRotate()) {
               this.worldObj.playSound(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5, "random.click", 0.05F, 1.0F);
               this.rotationTickCount = 0;
            }
         } else {
            this.rotationTickCount = 0;
         }
      } else {
         if (this.switchOverride >= 0) {
            ((TurntableBlock)BTWBlocks.turntable).setSwitchSetting(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.switchOverride);
            this.switchOverride = -1;
         }

         byte updateOffset = 9;
         if (this.worldObj
            .checkChunksExist(
               this.xCoord - updateOffset,
               this.yCoord - updateOffset,
               this.zCoord - updateOffset,
               this.xCoord + updateOffset,
               this.yCoord + updateOffset,
               this.zCoord + updateOffset
            )) {
            if (((TurntableBlock)BTWBlocks.turntable).isBlockMechanicalOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
               this.rotationTickCount++;
               if (this.rotationTickCount >= this.getTicksToRotate()) {
                  this.rotateTurntable();
                  this.rotationTickCount = 0;
               }
            } else {
               this.rotationTickCount = 0;
            }
         }
      }
   }

   private int getTicksToRotate() {
      return ticksToRotate[((TurntableBlock)BTWBlocks.turntable).getSwitchSetting(this.worldObj, this.xCoord, this.yCoord, this.zCoord)];
   }

   private void rotateTurntable() {
      boolean reverseDirection = ((TurntableBlock)BTWBlocks.turntable).isBlockRedstoneOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
      int craftingCounter = this.craftingRotationCount;

      for (int j = this.yCoord + 1; j <= this.yCoord + 2; j++) {
         Block targetBlock = Block.blocksList[this.worldObj.getBlockId(this.xCoord, j, this.zCoord)];
         if (targetBlock == null || !targetBlock.canRotateOnTurntable(this.worldObj, this.xCoord, j, this.zCoord)) {
            break;
         }

         boolean canTransmitHorizontally = targetBlock.canTransmitRotationHorizontallyOnTurntable(this.worldObj, this.xCoord, j, this.zCoord);
         boolean canTransmitVertically = targetBlock.canTransmitRotationVerticallyOnTurntable(this.worldObj, this.xCoord, j, this.zCoord);
         craftingCounter = targetBlock.rotateOnTurntable(this.worldObj, this.xCoord, j, this.zCoord, reverseDirection, craftingCounter);
         if (canTransmitHorizontally) {
            this.rotateBlocksAttachedToBlock(this.xCoord, j, this.zCoord, reverseDirection);
         }

         if (!canTransmitVertically) {
            break;
         }
      }

      if (craftingCounter > this.craftingRotationCount) {
         this.craftingRotationCount = craftingCounter;
      } else {
         this.craftingRotationCount = 0;
      }

      this.worldObj.notifyBlocksOfNeighborChange(this.xCoord, this.yCoord, this.zCoord, BTWBlocks.turntable.blockID);
   }

   private void rotateBlocksAttachedToBlock(int x, int y, int z, boolean reverseDirection) {
      int[] newBlockIDs = new int[4];
      int[] newMetadataArray = new int[4];

      for (int i = 0; i < 4; i++) {
         newBlockIDs[i] = 0;
         newMetadataArray[i] = 0;
      }

      for (int i = 2; i <= 5; i++) {
         BlockPos pos = new BlockPos(x, y, z, i);
         int blockID = this.worldObj.getBlockId(pos.x, pos.y, pos.z);
         Block block = Block.blocksList[blockID];
         if (block != null) {
            int oppositeFacing = Block.getOppositeFacing(i);
            if (block.canRotateAroundBlockOnTurntableToFacing(this.worldObj, pos.x, pos.y, pos.z, oppositeFacing)
               && block.onRotatedAroundBlockOnTurntableToFacing(this.worldObj, pos.x, pos.y, pos.z, oppositeFacing)) {
               int destinationFacing = Block.rotateFacingAroundY(i, reverseDirection);
               newBlockIDs[destinationFacing - 2] = blockID;
               newMetadataArray[destinationFacing - 2] = block.getNewMetadataRotatedAroundBlockOnTurntableToFacing(
                  this.worldObj, pos.x, pos.y, pos.z, oppositeFacing, Block.getOppositeFacing(destinationFacing)
               );
               this.worldObj.setBlockWithNotify(pos.x, pos.y, pos.z, 0);
            }
         }
      }

      for (int ix = 0; ix < 4; ix++) {
         int blockID = newBlockIDs[ix];
         if (blockID != 0) {
            int facing = ix + 2;
            int metadata = newMetadataArray[ix];
            BlockPos pos = new BlockPos(x, y, z);
            pos.addFacingAsOffset(facing);
            if (WorldUtils.isReplaceableBlock(this.worldObj, pos.x, pos.y, pos.z)) {
               this.worldObj.setBlockAndMetadataWithNotify(pos.x, pos.y, pos.z, blockID, metadata);
            } else {
               Block block = Block.blocksList[blockID];
               int oldFacing = Block.rotateFacingAroundY(facing, !reverseDirection);
               BlockPos oldPos = new BlockPos(x, y, z, oldFacing);
               block.dropBlockAsItem(this.worldObj, oldPos.x, oldPos.y, oldPos.z, blockID, 0);
            }
         }
      }
   }
}
