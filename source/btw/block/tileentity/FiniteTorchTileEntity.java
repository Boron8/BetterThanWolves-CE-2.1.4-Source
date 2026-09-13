package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.FiniteBurningTorchBlock;
import btw.block.blocks.FiniteUnlitTorchBlock;
import btw.block.blocks.FireBlock;
import btw.block.blocks.TorchBlockBase;
import net.minecraft.src.Block;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class FiniteTorchTileEntity extends TileEntity {
   private static final float CHANCE_OF_FIRE_SPREAD = 0.01F;
   private static final float CHANCE_OF_GOING_OUT_FROM_RAIN = 0.01F;
   public static final int MAX_BURN_TIME = 24000;
   public static final int SPUTTER_TIME = 600;
   public int burnTimeCountdown = 0;

   public FiniteTorchTileEntity() {
      this.burnTimeCountdown = 24000;
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      if (tag.hasKey("fcBurnCounter")) {
         this.burnTimeCountdown = tag.getInteger("fcBurnCounter");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      tag.setInteger("fcBurnCounter", this.burnTimeCountdown);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         this.burnTimeCountdown--;
         if (this.burnTimeCountdown <= 0 || this.worldObj.rand.nextFloat() <= 0.01F && this.isRainingOnTorch()) {
            if (this.worldObj.doChunksNearChunkExist(this.xCoord, this.yCoord, this.zCoord, 32)) {
               this.extinguishTorch();
            } else {
               this.burnTimeCountdown = 0;
            }
         } else {
            if (this.burnTimeCountdown < 600) {
               int iMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
               if (!FiniteBurningTorchBlock.getIsSputtering(iMetadata)) {
                  FiniteBurningTorchBlock block = (FiniteBurningTorchBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
                  block.setIsSputtering(this.worldObj, this.xCoord, this.yCoord, this.zCoord, true);
               }
            }

            if (this.worldObj.rand.nextFloat() <= 0.01F) {
               FireBlock.checkForFireSpreadAndDestructionToOneBlockLocation(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord);
            }
         }
      } else {
         int iMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
         if (FiniteBurningTorchBlock.getIsSputtering(iMetadata)) {
            this.sputter();
         }
      }
   }

   private boolean isRainingOnTorch() {
      FiniteBurningTorchBlock block = (FiniteBurningTorchBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      return block.isRainingOnTorch(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }

   private void extinguishTorch() {
      this.burnTimeCountdown = 0;
      int iOldMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
      int iOrientation = TorchBlockBase.getOrientation(iOldMetadata);
      int iNewMetadata = TorchBlockBase.setOrientation(0, iOrientation);
      iNewMetadata = FiniteUnlitTorchBlock.setIsBurnedOut(iNewMetadata, true);
      this.worldObj.playAuxSFX(1004, this.xCoord, this.yCoord, this.zCoord, 0);
      this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, BTWBlocks.finiteUnlitTorch.blockID, iNewMetadata);
   }

   private void sputter() {
      int iMetadata = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
      int iOrientation = TorchBlockBase.getOrientation(iMetadata);
      double dHorizontalOffset = 0.27;
      double xPos = this.xCoord + 0.5 + (this.worldObj.rand.nextDouble() * 0.1 - 0.05);
      double yPos = this.yCoord + 0.92 + (this.worldObj.rand.nextDouble() * 0.1 - 0.05);
      double zPos = this.zCoord + 0.5 + (this.worldObj.rand.nextDouble() * 0.1 - 0.05);
      if (iOrientation == 1) {
         xPos -= dHorizontalOffset;
      } else if (iOrientation == 2) {
         xPos += dHorizontalOffset;
      } else if (iOrientation == 3) {
         zPos -= dHorizontalOffset;
      } else if (iOrientation == 4) {
         zPos += dHorizontalOffset;
      } else {
         yPos -= 0.22;
      }

      this.worldObj.spawnParticle("smoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
   }
}
