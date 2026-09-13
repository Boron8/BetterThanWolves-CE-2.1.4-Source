package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.AnchorBlock;
import btw.block.blocks.PulleyBlock;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.world.util.WorldUtils;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class PulleyTileEntity extends TileEntity implements IInventory {
   private static final int PULLEY_INVENTORY_SIZE = 4;
   private static final int PULLEY_STACK_SIZE_LIMIT = 64;
   private static final double PULLEY_MAX_PLAYER_INTERACTION_DIST = 64.0;
   private static final int TICKS_TO_UPDATE_ROPE_STATE = 20;
   private boolean hasAssociatedAnchorEntity;
   private ItemStack[] pulleyContents = new ItemStack[4];
   public int updateRopeStateCounter;
   public int mechanicalPowerIndicator;

   public PulleyTileEntity() {
      this.hasAssociatedAnchorEntity = false;
      this.updateRopeStateCounter = 20;
      this.mechanicalPowerIndicator = 0;
   }

   @Override
   public String getInvName() {
      return "Pulley";
   }

   @Override
   public int getSizeInventory() {
      return 4;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public ItemStack getStackInSlot(int iSlot) {
      return this.pulleyContents[iSlot];
   }

   @Override
   public void setInventorySlotContents(int iSlot, ItemStack itemstack) {
      this.pulleyContents[iSlot] = itemstack;
      if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
         itemstack.stackSize = this.getInventoryStackLimit();
      }

      this.k_();
   }

   @Override
   public ItemStack decrStackSize(int iSlot, int iAmount) {
      return InventoryUtils.decreaseStackSize(this, iSlot, iAmount);
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.pulleyContents[par1] != null) {
         ItemStack itemstack = this.pulleyContents[par1];
         this.pulleyContents[par1] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
      this.pulleyContents = new ItemStack[this.getSizeInventory()];

      for (int i = 0; i < nbttaglist.tagCount(); i++) {
         NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
         int j = nbttagcompound1.getByte("Slot") & 255;
         if (j >= 0 && j < this.pulleyContents.length) {
            this.pulleyContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
         }
      }

      if (nbttagcompound.hasKey("iUpdateRopeStateCounter")) {
         this.updateRopeStateCounter = nbttagcompound.getInteger("iUpdateRopeStateCounter");
      }

      if (nbttagcompound.hasKey("m_bHasAssociatedAnchorEntity")) {
         this.hasAssociatedAnchorEntity = nbttagcompound.getBoolean("m_bHasAssociatedAnchorEntity");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      NBTTagList nbttaglist = new NBTTagList();

      for (int i = 0; i < this.pulleyContents.length; i++) {
         if (this.pulleyContents[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.pulleyContents[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbttagcompound.setTag("Items", nbttaglist);
      nbttagcompound.setInteger("iUpdateRopeStateCounter", this.updateRopeStateCounter);
      nbttagcompound.setBoolean("m_bHasAssociatedAnchorEntity", this.hasAssociatedAnchorEntity);
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer entityplayer) {
      return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this
         ? false
         : entityplayer.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
   }

   @Override
   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         if (this.isMechanicallyPowered()) {
            this.mechanicalPowerIndicator = 1;
         } else {
            this.mechanicalPowerIndicator = 0;
         }

         this.updateRopeStateCounter--;
         if (this.updateRopeStateCounter <= 0) {
            if (!this.hasAssociatedAnchorEntity) {
               boolean bIsRedstoneOn = ((PulleyBlock)BTWBlocks.pulley).isRedstoneOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
               if (!bIsRedstoneOn) {
                  boolean bIsOn = ((PulleyBlock)BTWBlocks.pulley).isBlockOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
                  if (bIsOn) {
                     this.attemptToRetractRope();
                  } else {
                     this.attemptToDispenseRope();
                  }
               }
            }

            this.updateRopeStateCounter = 20;
         }
      }
   }

   @Override
   public boolean isStackValidForSlot(int iSlot, ItemStack stack) {
      return true;
   }

   @Override
   public boolean isInvNameLocalized() {
      return true;
   }

   private boolean isMechanicallyPowered() {
      return ((PulleyBlock)BTWBlocks.pulley).isBlockOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }

   private boolean isRedstonePowered() {
      return ((PulleyBlock)BTWBlocks.pulley).isRedstoneOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }

   public boolean isRaising() {
      return !this.isRedstonePowered() && this.isMechanicallyPowered();
   }

   public boolean isLowering() {
      return !this.isRedstonePowered() && !this.isMechanicallyPowered() && InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.rope.itemID) >= 0;
   }

   public void notifyPulleyEntityOfBlockStateChange() {
      this.updateRopeStateCounter = 20;
      this.notifyAttachedAnchorOfEntityStateChange();
   }

   private void notifyAttachedAnchorOfEntityStateChange() {
      for (int tempj = this.yCoord - 1; tempj >= 0; tempj--) {
         int iTempBlockID = this.worldObj.getBlockId(this.xCoord, tempj, this.zCoord);
         if (iTempBlockID == BTWBlocks.anchor.blockID) {
            if (((AnchorBlock)BTWBlocks.anchor).getFacing(this.worldObj, this.xCoord, tempj, this.zCoord) != 1) {
               break;
            }

            if (((AnchorBlock)BTWBlocks.anchor).notifyAnchorBlockOfAttachedPulleyStateChange(this, this.worldObj, this.xCoord, tempj, this.zCoord)) {
               this.hasAssociatedAnchorEntity = true;
            }
         } else if (iTempBlockID != BTWBlocks.ropeBlock.blockID) {
            break;
         }
      }
   }

   public boolean attemptToRetractRope() {
      for (int tempj = this.yCoord - 1; tempj >= 0; tempj--) {
         int iTempBlockID = this.worldObj.getBlockId(this.xCoord, tempj, this.zCoord);
         if (iTempBlockID != BTWBlocks.ropeBlock.blockID) {
            return false;
         }

         if (this.worldObj.getBlockId(this.xCoord, tempj - 1, this.zCoord) != BTWBlocks.ropeBlock.blockID) {
            this.addRopeToInventory();
            Block targetBlock = BTWBlocks.ropeBlock;
            this.worldObj
               .playSoundEffect(
                  this.xCoord + 0.5F,
                  tempj + 0.5F,
                  this.zCoord + 0.5F,
                  targetBlock.stepSound.getStepSound(),
                  targetBlock.stepSound.getStepVolume() / 4.0F,
                  targetBlock.stepSound.getStepPitch() * 0.8F
               );
            this.worldObj.setBlockWithNotify(this.xCoord, tempj, this.zCoord, 0);
            return true;
         }
      }

      return false;
   }

   public boolean attemptToDispenseRope() {
      int iRopeSlot = InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.rope.itemID);
      this.updateRopeStateCounter = 20;
      if (iRopeSlot >= 0) {
         for (int tempj = this.yCoord - 1; tempj >= 0; tempj--) {
            int iTempBlockID = this.worldObj.getBlockId(this.xCoord, tempj, this.zCoord);
            if (WorldUtils.isReplaceableBlock(this.worldObj, this.xCoord, tempj, this.zCoord)) {
               int iMetadata = BTWBlocks.ropeBlock.onBlockPlaced(this.worldObj, this.xCoord, tempj, this.zCoord, 0, 0.0F, 0.0F, 0.0F, 0);
               if (this.worldObj.setBlockAndMetadataWithNotify(this.xCoord, tempj, this.zCoord, BTWBlocks.ropeBlock.blockID, iMetadata)) {
                  Block targetBlock = BTWBlocks.ropeBlock;
                  this.worldObj
                     .playSoundEffect(
                        this.xCoord + 0.5F,
                        tempj + 0.5F,
                        this.zCoord + 0.5F,
                        targetBlock.stepSound.getStepSound(),
                        targetBlock.stepSound.getStepVolume() / 4.0F,
                        targetBlock.stepSound.getStepPitch() * 0.8F
                     );
                  this.removeRopeFromInventory();
                  int iBlockBelowTargetID = this.worldObj.getBlockId(this.xCoord, tempj - 1, this.zCoord);
                  if (iBlockBelowTargetID == BTWBlocks.anchor.blockID
                     && ((AnchorBlock)BTWBlocks.anchor).getFacing(this.worldObj, this.xCoord, tempj - 1, this.zCoord) == 1) {
                     ((AnchorBlock)BTWBlocks.anchor).notifyAnchorBlockOfAttachedPulleyStateChange(this, this.worldObj, this.xCoord, tempj - 1, this.zCoord);
                  }

                  return true;
               }

               return false;
            }

            if (iTempBlockID != BTWBlocks.ropeBlock.blockID) {
               return false;
            }
         }
      }

      return false;
   }

   public void addRopeToInventory() {
      ItemStack ropeStack = new ItemStack(BTWItems.rope);
      this.updateRopeStateCounter = 20;
      if (InventoryUtils.addItemStackToInventory(this, ropeStack)) {
         this.worldObj
            .playSoundEffect(
               this.xCoord + 0.5,
               this.yCoord + 0.5,
               this.zCoord + 0.5,
               "random.pop",
               0.05F,
               (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.7F + 1.0F
            );
      } else {
         ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ropeStack);
      }
   }

   public int getContainedRopeCount() {
      return InventoryUtils.countItemsInInventory(this, BTWItems.rope.itemID, 32767);
   }

   public void removeRopeFromInventory() {
      int iRopeSlot = InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.rope.itemID);
      if (iRopeSlot >= 0) {
         InventoryUtils.decreaseStackSize(this, iRopeSlot, 1);
      }
   }

   public void notifyOfLossOfAnchorEntity() {
      this.hasAssociatedAnchorEntity = false;
   }
}
