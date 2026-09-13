package net.minecraft.src;

import java.util.Random;

public class TileEntityDispenser extends TileEntity implements IInventory {
   private ItemStack[] dispenserContents = new ItemStack[9];
   private Random dispenserRandom = new Random();
   protected String customName;

   @Override
   public int getSizeInventory() {
      return 9;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.dispenserContents[var1];
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      if (this.dispenserContents[var1] != null) {
         if (this.dispenserContents[var1].stackSize <= var2) {
            ItemStack var4 = this.dispenserContents[var1];
            this.dispenserContents[var1] = null;
            this.k_();
            return var4;
         } else {
            ItemStack var3 = this.dispenserContents[var1].splitStack(var2);
            if (this.dispenserContents[var1].stackSize == 0) {
               this.dispenserContents[var1] = null;
            }

            this.k_();
            return var3;
         }
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int var1) {
      if (this.dispenserContents[var1] != null) {
         ItemStack var2 = this.dispenserContents[var1];
         this.dispenserContents[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   public int getRandomStackFromInventory() {
      int var1 = -1;
      int var2 = 1;

      for (int var3 = 0; var3 < this.dispenserContents.length; var3++) {
         if (this.dispenserContents[var3] != null && this.dispenserRandom.nextInt(var2++) == 0) {
            var1 = var3;
         }
      }

      return var1;
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.dispenserContents[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }

      this.k_();
   }

   public int addItem(ItemStack var1) {
      for (int var2 = 0; var2 < this.dispenserContents.length; var2++) {
         if (this.dispenserContents[var2] == null || this.dispenserContents[var2].itemID == 0) {
            this.setInventorySlotContents(var2, var1);
            return var2;
         }
      }

      return -1;
   }

   @Override
   public String getInvName() {
      return this.isInvNameLocalized() ? this.customName : "container.dispenser";
   }

   public void setCustomName(String var1) {
      this.customName = var1;
   }

   @Override
   public boolean isInvNameLocalized() {
      return this.customName != null;
   }

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      NBTTagList var2 = var1.getTagList("Items");
      this.dispenserContents = new ItemStack[this.getSizeInventory()];

      for (int var3 = 0; var3 < var2.tagCount(); var3++) {
         NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 >= 0 && var5 < this.dispenserContents.length) {
            this.dispenserContents[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

      if (var1.hasKey("CustomName")) {
         this.customName = var1.getString("CustomName");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for (int var3 = 0; var3 < this.dispenserContents.length; var3++) {
         if (this.dispenserContents[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.dispenserContents[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      var1.setTag("Items", var2);
      if (this.isInvNameLocalized()) {
         var1.setString("CustomName", this.customName);
      }
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer var1) {
      return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this
         ? false
         : !(var1.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) > 64.0);
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int var1, ItemStack var2) {
      return true;
   }
}
