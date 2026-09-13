package btw.block.tileentity.dispenser;

import btw.inventory.util.InventoryUtils;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class BlockDispenserTileEntity extends TileEntity implements IInventory {
   private ItemStack[] dispenserContents = new ItemStack[16];
   public int nextSlotIndexToDispense = 0;

   @Override
   public int getSizeInventory() {
      return 16;
   }

   @Override
   public ItemStack getStackInSlot(int i) {
      return this.dispenserContents[i];
   }

   @Override
   public ItemStack decrStackSize(int iSlot, int iAmount) {
      return InventoryUtils.decreaseStackSize(this, iSlot, iAmount);
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.dispenserContents[par1] != null) {
         ItemStack itemstack = this.dispenserContents[par1];
         this.dispenserContents[par1] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int iSlot, ItemStack itemstack) {
      super.onInventoryChanged();
      this.dispenserContents[iSlot] = itemstack;
      if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
         itemstack.stackSize = this.getInventoryStackLimit();
      }

      this.k_();
   }

   @Override
   public String getInvName() {
      return "BlockDispenser";
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
      this.dispenserContents = new ItemStack[this.getSizeInventory()];

      for (int i = 0; i < nbttaglist.tagCount(); i++) {
         NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
         int j = nbttagcompound1.getByte("Slot") & 255;
         if (j >= 0 && j < this.dispenserContents.length) {
            this.dispenserContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
         }
      }

      if (nbttagcompound.hasKey("iNextSlotIndexToDispense")) {
         this.nextSlotIndexToDispense = nbttagcompound.getInteger("iNextSlotIndexToDispense");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      NBTTagList nbttaglist = new NBTTagList();

      for (int i = 0; i < this.dispenserContents.length; i++) {
         if (this.dispenserContents[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.dispenserContents[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbttagcompound.setTag("Items", nbttaglist);
      nbttagcompound.setInteger("iNextSlotIndexToDispense", this.nextSlotIndexToDispense);
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer entityplayer) {
      return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this
         ? false
         : entityplayer.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int iSlot, ItemStack stack) {
      return true;
   }

   @Override
   public boolean isInvNameLocalized() {
      return true;
   }

   public ItemStack getCurrentItemToDispense() {
      if (this.nextSlotIndexToDispense >= this.dispenserContents.length || this.dispenserContents[this.nextSlotIndexToDispense] == null) {
         int iTempSlot = this.findNextValidSlotIndex(this.nextSlotIndexToDispense);
         if (iTempSlot < 0) {
            return null;
         }

         this.nextSlotIndexToDispense = iTempSlot;
      }

      ItemStack stack = this.getStackInSlot(this.nextSlotIndexToDispense).copy();
      stack.stackSize = 1;
      return stack;
   }

   public void onDispenseCurrentSlot() {
      this.decrStackSize(this.nextSlotIndexToDispense, 1);
      int iTempSlot = this.findNextValidSlotIndex(this.nextSlotIndexToDispense);
      if (iTempSlot < 0) {
         this.nextSlotIndexToDispense = 0;
      } else {
         this.nextSlotIndexToDispense = iTempSlot;
      }
   }

   private int findNextValidSlotIndex(int iCurrentSlot) {
      for (int iTempSlot = iCurrentSlot + 1; iTempSlot < this.dispenserContents.length; iTempSlot++) {
         if (this.dispenserContents[iTempSlot] != null) {
            return iTempSlot;
         }
      }

      for (int iTempSlotx = 0; iTempSlotx < iCurrentSlot; iTempSlotx++) {
         if (this.dispenserContents[iTempSlotx] != null) {
            return iTempSlotx;
         }
      }

      return this.dispenserContents[iCurrentSlot] != null ? iCurrentSlot : -1;
   }
}
