package btw.block.tileentity;

import btw.block.BTWBlocks;
import net.minecraft.src.Block;
import net.minecraft.src.BlockChest;
import net.minecraft.src.TileEntityChest;

public class ChestTileEntity extends TileEntityChest {
   @Override
   public void openChest() {
      this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, BTWBlocks.chest.blockID);
      super.openChest();
   }

   @Override
   public void closeChest() {
      this.worldObj.notifyBlockChange(this.xCoord, this.yCoord, this.zCoord, BTWBlocks.chest.blockID);
      super.closeChest();
   }

   @Override
   public void checkForAdjacentChests() {
      if (this.worldObj != null) {
         if (!this.adjacentChestChecked) {
            this.adjacentChestChecked = true;
            this.adjacentChestZNeg = null;
            this.adjacentChestXPos = null;
            this.adjacentChestXNeg = null;
            this.adjacentChestZPosition = null;
            int iBlockID = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord);
            if (this.isAdjacentChest(iBlockID, this.xCoord - 1, this.yCoord, this.zCoord)) {
               this.adjacentChestXNeg = (TileEntityChest)this.worldObj.getBlockTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
            }

            if (this.isAdjacentChest(iBlockID, this.xCoord + 1, this.yCoord, this.zCoord)) {
               this.adjacentChestXPos = (TileEntityChest)this.worldObj.getBlockTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
            }

            if (this.isAdjacentChest(iBlockID, this.xCoord, this.yCoord, this.zCoord - 1)) {
               this.adjacentChestZNeg = (TileEntityChest)this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
            }

            if (this.isAdjacentChest(iBlockID, this.xCoord, this.yCoord, this.zCoord + 1)) {
               this.adjacentChestZPosition = (TileEntityChest)this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
            }

            if (this.adjacentChestZNeg != null) {
               ((ChestTileEntity)this.adjacentChestZNeg).validateNeighborConnection(this, 0);
            }

            if (this.adjacentChestZPosition != null) {
               ((ChestTileEntity)this.adjacentChestZPosition).validateNeighborConnection(this, 2);
            }

            if (this.adjacentChestXPos != null) {
               ((ChestTileEntity)this.adjacentChestXPos).validateNeighborConnection(this, 1);
            }

            if (this.adjacentChestXNeg != null) {
               ((ChestTileEntity)this.adjacentChestXNeg).validateNeighborConnection(this, 3);
            }
         }
      }
   }

   protected boolean isAdjacentChest(int iBlockID, int i, int j, int k) {
      int iAdjacentBlockID = this.worldObj.getBlockId(i, j, k);
      if (iBlockID != iAdjacentBlockID && Block.blocksList[iBlockID] instanceof BlockChest) {
         return false;
      } else {
         Block adjacentBlock = Block.blocksList[iAdjacentBlockID];
         return adjacentBlock != null && adjacentBlock instanceof BlockChest ? ((BlockChest)adjacentBlock).isTrapped == this.l() : false;
      }
   }

   protected void validateNeighborConnection(TileEntityChest neighborEntity, int iDirection) {
      if (neighborEntity.r()) {
         this.adjacentChestChecked = false;
      } else if (this.adjacentChestChecked) {
         switch (iDirection) {
            case 0:
               if (this.adjacentChestZPosition != neighborEntity) {
                  this.adjacentChestChecked = false;
               }
               break;
            case 1:
               if (this.adjacentChestXNeg != neighborEntity) {
                  this.adjacentChestChecked = false;
               }
               break;
            case 2:
               if (this.adjacentChestZNeg != neighborEntity) {
                  this.adjacentChestChecked = false;
               }
               break;
            case 3:
               if (this.adjacentChestXPos != neighborEntity) {
                  this.adjacentChestChecked = false;
               }
         }
      }
   }

   public void clearContents() {
      for (int iSlot = 0; iSlot < this.j_(); iSlot++) {
         if (this.a(iSlot) != null) {
            this.a(iSlot, null);
         }
      }
   }
}
