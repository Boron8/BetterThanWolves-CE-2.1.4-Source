package net.minecraft.src;

import java.util.List;

public class TileEntityHopper extends TileEntity implements Hopper {
   private ItemStack[] hopperItemStacks = new ItemStack[5];
   private String inventoryName;
   private int transferCooldown = -1;

   @Override
   public void readFromNBT(NBTTagCompound var1) {
      super.readFromNBT(var1);
      NBTTagList var2 = var1.getTagList("Items");
      this.hopperItemStacks = new ItemStack[this.getSizeInventory()];
      if (var1.hasKey("CustomName")) {
         this.inventoryName = var1.getString("CustomName");
      }

      this.transferCooldown = var1.getInteger("TransferCooldown");

      for (int var3 = 0; var3 < var2.tagCount(); var3++) {
         NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
         byte var5 = var4.getByte("Slot");
         if (var5 >= 0 && var5 < this.hopperItemStacks.length) {
            this.hopperItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound var1) {
      super.writeToNBT(var1);
      NBTTagList var2 = new NBTTagList();

      for (int var3 = 0; var3 < this.hopperItemStacks.length; var3++) {
         if (this.hopperItemStacks[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.hopperItemStacks[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      var1.setTag("Items", var2);
      var1.setInteger("TransferCooldown", this.transferCooldown);
      if (this.isInvNameLocalized()) {
         var1.setString("CustomName", this.inventoryName);
      }
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
   }

   @Override
   public int getSizeInventory() {
      return this.hopperItemStacks.length;
   }

   @Override
   public ItemStack getStackInSlot(int var1) {
      return this.hopperItemStacks[var1];
   }

   @Override
   public ItemStack decrStackSize(int var1, int var2) {
      if (this.hopperItemStacks[var1] != null) {
         if (this.hopperItemStacks[var1].stackSize <= var2) {
            ItemStack var4 = this.hopperItemStacks[var1];
            this.hopperItemStacks[var1] = null;
            return var4;
         } else {
            ItemStack var3 = this.hopperItemStacks[var1].splitStack(var2);
            if (this.hopperItemStacks[var1].stackSize == 0) {
               this.hopperItemStacks[var1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int var1) {
      if (this.hopperItemStacks[var1] != null) {
         ItemStack var2 = this.hopperItemStacks[var1];
         this.hopperItemStacks[var1] = null;
         return var2;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int var1, ItemStack var2) {
      this.hopperItemStacks[var1] = var2;
      if (var2 != null && var2.stackSize > this.getInventoryStackLimit()) {
         var2.stackSize = this.getInventoryStackLimit();
      }
   }

   @Override
   public String getInvName() {
      return this.isInvNameLocalized() ? this.inventoryName : "container.hopper";
   }

   @Override
   public boolean isInvNameLocalized() {
      return this.inventoryName != null && this.inventoryName.length() > 0;
   }

   public void setInventoryName(String var1) {
      this.inventoryName = var1;
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

   @Override
   public void updateEntity() {
      if (this.worldObj != null && !this.worldObj.isRemote) {
         this.transferCooldown--;
         if (!this.isCoolingDown()) {
            this.setTransferCooldown(0);
            this.func_98045_j();
         }
      }
   }

   public boolean func_98045_j() {
      if (this.worldObj != null && !this.worldObj.isRemote) {
         if (!this.isCoolingDown() && BlockHopper.getIsBlockNotPoweredFromMetadata(this.p())) {
            boolean var1 = this.insertItemToInventory() | suckItemsIntoHopper(this);
            if (var1) {
               this.setTransferCooldown(8);
               this.onInventoryChanged();
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean insertItemToInventory() {
      IInventory var1 = this.getOutputInventory();
      if (var1 == null) {
         return false;
      } else {
         for (int var2 = 0; var2 < this.getSizeInventory(); var2++) {
            if (this.getStackInSlot(var2) != null) {
               ItemStack var3 = this.getStackInSlot(var2).copy();
               ItemStack var4 = insertStack(var1, this.decrStackSize(var2, 1), Facing.oppositeSide[BlockHopper.getDirectionFromMetadata(this.p())]);
               if (var4 == null || var4.stackSize == 0) {
                  var1.onInventoryChanged();
                  return true;
               }

               this.setInventorySlotContents(var2, var3);
            }
         }

         return false;
      }
   }

   public static boolean suckItemsIntoHopper(Hopper var0) {
      IInventory var1 = getInventoryAboveHopper(var0);
      if (var1 != null) {
         byte var2 = 0;
         if (var1 instanceof ISidedInventory && var2 > -1) {
            ISidedInventory var7 = (ISidedInventory)var1;
            int[] var8 = var7.getAccessibleSlotsFromSide(var2);

            for (int var5 = 0; var5 < var8.length; var5++) {
               if (func_102012_a(var0, var1, var8[var5], var2)) {
                  return true;
               }
            }
         } else {
            int var3 = var1.getSizeInventory();

            for (int var4 = 0; var4 < var3; var4++) {
               if (func_102012_a(var0, var1, var4, var2)) {
                  return true;
               }
            }
         }
      } else {
         EntityItem var6 = func_96119_a(var0.getWorldObj(), var0.getXPos(), var0.getYPos() + 1.0, var0.getZPos());
         if (var6 != null) {
            return func_96114_a(var0, var6);
         }
      }

      return false;
   }

   private static boolean func_102012_a(Hopper var0, IInventory var1, int var2, int var3) {
      ItemStack var4 = var1.getStackInSlot(var2);
      if (var4 != null && canExtractItemFromInventory(var1, var4, var2, var3)) {
         ItemStack var5 = var4.copy();
         ItemStack var6 = insertStack(var0, var1.decrStackSize(var2, 1), -1);
         if (var6 == null || var6.stackSize == 0) {
            var1.onInventoryChanged();
            return true;
         }

         var1.setInventorySlotContents(var2, var5);
      }

      return false;
   }

   public static boolean func_96114_a(IInventory var0, EntityItem var1) {
      boolean var2 = false;
      if (var1 == null) {
         return false;
      } else {
         ItemStack var3 = var1.getEntityItem().copy();
         ItemStack var4 = insertStack(var0, var3, -1);
         if (var4 != null && var4.stackSize != 0) {
            var1.setEntityItemStack(var4);
         } else {
            var2 = true;
            var1.w();
         }

         return var2;
      }
   }

   public static ItemStack insertStack(IInventory var0, ItemStack var1, int var2) {
      if (var0 instanceof ISidedInventory && var2 > -1) {
         ISidedInventory var6 = (ISidedInventory)var0;
         int[] var7 = var6.getAccessibleSlotsFromSide(var2);

         for (int var5 = 0; var5 < var7.length && var1 != null && var1.stackSize > 0; var5++) {
            var1 = func_102014_c(var0, var1, var7[var5], var2);
         }
      } else {
         int var3 = var0.getSizeInventory();

         for (int var4 = 0; var4 < var3 && var1 != null && var1.stackSize > 0; var4++) {
            var1 = func_102014_c(var0, var1, var4, var2);
         }
      }

      if (var1 != null && var1.stackSize == 0) {
         var1 = null;
      }

      return var1;
   }

   private static boolean func_102015_a(IInventory var0, ItemStack var1, int var2, int var3) {
      return !var0.isStackValidForSlot(var2, var1) ? false : !(var0 instanceof ISidedInventory) || ((ISidedInventory)var0).canInsertItem(var2, var1, var3);
   }

   private static boolean canExtractItemFromInventory(IInventory var0, ItemStack var1, int var2, int var3) {
      return !(var0 instanceof ISidedInventory) || ((ISidedInventory)var0).canExtractItem(var2, var1, var3);
   }

   private static ItemStack func_102014_c(IInventory var0, ItemStack var1, int var2, int var3) {
      ItemStack var4 = var0.getStackInSlot(var2);
      if (func_102015_a(var0, var1, var2, var3)) {
         boolean var5 = false;
         if (var4 == null) {
            var0.setInventorySlotContents(var2, var1);
            var1 = null;
            var5 = true;
         } else if (areItemStacksEqualItem(var4, var1)) {
            int var6 = var1.getMaxStackSize() - var4.stackSize;
            int var7 = Math.min(var1.stackSize, var6);
            var1.stackSize -= var7;
            var4.stackSize += var7;
            var5 = var7 > 0;
         }

         if (var5) {
            if (var0 instanceof TileEntityHopper) {
               ((TileEntityHopper)var0).setTransferCooldown(8);
            }

            var0.onInventoryChanged();
         }
      }

      return var1;
   }

   private IInventory getOutputInventory() {
      int var1 = BlockHopper.getDirectionFromMetadata(this.p());
      return getInventoryAtLocation(
         this.az(), this.xCoord + Facing.offsetsXForSide[var1], this.yCoord + Facing.offsetsYForSide[var1], this.zCoord + Facing.offsetsZForSide[var1]
      );
   }

   public static IInventory getInventoryAboveHopper(Hopper var0) {
      return getInventoryAtLocation(var0.getWorldObj(), var0.getXPos(), var0.getYPos() + 1.0, var0.getZPos());
   }

   public static EntityItem func_96119_a(World var0, double var1, double var3, double var5) {
      List var7 = var0.selectEntitiesWithinAABB(
         EntityItem.class, AxisAlignedBB.getAABBPool().getAABB(var1, var3, var5, var1 + 1.0, var3 + 1.0, var5 + 1.0), IEntitySelector.selectAnything
      );
      return var7.size() > 0 ? (EntityItem)var7.get(0) : null;
   }

   public static IInventory getInventoryAtLocation(World var0, double var1, double var3, double var5) {
      IInventory var7 = null;
      int var8 = MathHelper.floor_double(var1);
      int var9 = MathHelper.floor_double(var3);
      int var10 = MathHelper.floor_double(var5);
      TileEntity var11 = var0.getBlockTileEntity(var8, var9, var10);
      if (var11 != null && var11 instanceof IInventory) {
         var7 = (IInventory)var11;
         if (var7 instanceof TileEntityChest) {
            int var12 = var0.getBlockId(var8, var9, var10);
            Block var13 = Block.blocksList[var12];
            if (var13 instanceof BlockChest) {
               var7 = ((BlockChest)var13).getInventory(var0, var8, var9, var10);
            }
         }
      }

      if (var7 == null) {
         List var14 = var0.getEntitiesWithinAABBExcludingEntity(
            null, AxisAlignedBB.getAABBPool().getAABB(var1, var3, var5, var1 + 1.0, var3 + 1.0, var5 + 1.0), IEntitySelector.selectInventories
         );
         if (var14 != null && var14.size() > 0) {
            var7 = (IInventory)var14.get(var0.rand.nextInt(var14.size()));
         }
      }

      return var7;
   }

   private static boolean areItemStacksEqualItem(ItemStack var0, ItemStack var1) {
      if (var0.itemID != var1.itemID) {
         return false;
      } else if (var0.getItemDamage() != var1.getItemDamage()) {
         return false;
      } else {
         return var0.stackSize > var0.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(var0, var1);
      }
   }

   @Override
   public double getXPos() {
      return this.xCoord;
   }

   @Override
   public double getYPos() {
      return this.yCoord;
   }

   @Override
   public double getZPos() {
      return this.zCoord;
   }

   public void setTransferCooldown(int var1) {
      this.transferCooldown = var1;
   }

   public boolean isCoolingDown() {
      return this.transferCooldown > 0;
   }
}
