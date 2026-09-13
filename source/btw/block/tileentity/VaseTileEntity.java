package btw.block.tileentity;

import btw.inventory.util.InventoryUtils;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.TileEntity;

public class VaseTileEntity extends TileEntity implements IInventory {
   private static final int VASE_INVENTORY_SIZE = 1;
   private static final int VASE_STACK_SIZE_LIMIT = 1;
   private static final double VASE_MAX_PLAYER_INTERACTION_DIST = 64.0;
   private ItemStack[] vaseContents = new ItemStack[1];

   @Override
   public int getSizeInventory() {
      return 1;
   }

   @Override
   public int getInventoryStackLimit() {
      return 1;
   }

   @Override
   public ItemStack getStackInSlot(int iSlot) {
      return this.vaseContents[iSlot];
   }

   @Override
   public ItemStack decrStackSize(int iSlot, int iAmount) {
      return InventoryUtils.decreaseStackSize(this, iSlot, iAmount);
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.vaseContents[par1] != null) {
         ItemStack itemstack = this.vaseContents[par1];
         this.vaseContents[par1] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int iSlot, ItemStack itemstack) {
      this.vaseContents[iSlot] = itemstack;
      if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
         itemstack.stackSize = this.getInventoryStackLimit();
      }

      this.k_();
   }

   @Override
   public String getInvName() {
      return "Vase";
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
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
      this.vaseContents = new ItemStack[this.getSizeInventory()];

      for (int i = 0; i < nbttaglist.tagCount(); i++) {
         NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
         int j = nbttagcompound1.getByte("Slot") & 255;
         if (j >= 0 && j < this.vaseContents.length) {
            this.vaseContents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      NBTTagList nbttaglist = new NBTTagList();

      for (int i = 0; i < this.vaseContents.length; i++) {
         if (this.vaseContents[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.vaseContents[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbttagcompound.setTag("Items", nbttaglist);
   }

   @Override
   public boolean isStackValidForSlot(int iSlot, ItemStack stack) {
      return true;
   }

   @Override
   public boolean isInvNameLocalized() {
      return true;
   }
}
