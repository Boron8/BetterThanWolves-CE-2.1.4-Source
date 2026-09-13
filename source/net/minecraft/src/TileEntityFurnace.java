package net.minecraft.src;

import btw.block.blocks.FurnaceBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class TileEntityFurnace extends TileEntity implements ISidedInventory {
   private static final int[] field_102010_d = new int[]{0};
   private static final int[] field_102011_e = new int[]{2, 1};
   private static final int[] field_102009_f = new int[]{1};
   protected ItemStack[] furnaceItemStacks = new ItemStack[3];
   public int furnaceBurnTime = 0;
   public int currentItemBurnTime = 0;
   public int furnaceCookTime = 0;
   private String field_94130_e;
   public static final int DEFAULT_COOK_TIME = 400;
   public static final int BASE_BURN_TIME_MULTIPLIER = 2;

   @Override
   public int getSizeInventory() {
      return this.furnaceItemStacks.length;
   }

   @Override
   public ItemStack getStackInSlot(int par1) {
      return this.furnaceItemStacks[par1];
   }

   @Override
   public ItemStack decrStackSize(int par1, int par2) {
      if (this.furnaceItemStacks[par1] != null) {
         if (this.furnaceItemStacks[par1].stackSize <= par2) {
            ItemStack var3 = this.furnaceItemStacks[par1];
            this.furnaceItemStacks[par1] = null;
            return var3;
         } else {
            ItemStack var3 = this.furnaceItemStacks[par1].splitStack(par2);
            if (this.furnaceItemStacks[par1].stackSize == 0) {
               this.furnaceItemStacks[par1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.furnaceItemStacks[par1] != null) {
         ItemStack var2 = this.furnaceItemStacks[par1];
         this.furnaceItemStacks[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.furnaceItemStacks[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }
   }

   @Override
   public String getInvName() {
      return this.isInvNameLocalized() ? this.field_94130_e : "container.furnace";
   }

   @Override
   public boolean isInvNameLocalized() {
      return this.field_94130_e != null && this.field_94130_e.length() > 0;
   }

   public void func_94129_a(String par1Str) {
      this.field_94130_e = par1Str;
   }

   @Override
   public void readFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readFromNBT(par1NBTTagCompound);
      NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
      this.furnaceItemStacks = new ItemStack[this.getSizeInventory()];

      for (int var3 = 0; var3 < var2.tagCount(); var3++) {
         NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
         byte var5 = var4.getByte("Slot");
         if (var5 >= 0 && var5 < this.furnaceItemStacks.length) {
            this.furnaceItemStacks[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }

      this.furnaceBurnTime = par1NBTTagCompound.getShort("BurnTime");
      this.furnaceCookTime = par1NBTTagCompound.getShort("CookTime");
      this.currentItemBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
      if (par1NBTTagCompound.hasKey("CustomName")) {
         this.field_94130_e = par1NBTTagCompound.getString("CustomName");
      }

      if (par1NBTTagCompound.hasKey("fcBurnTimeEx")) {
         this.furnaceBurnTime = par1NBTTagCompound.getInteger("fcBurnTimeEx");
         this.furnaceCookTime = par1NBTTagCompound.getInteger("fcCookTimeEx");
         if (par1NBTTagCompound.hasKey("fcItemBurnTimeEx")) {
            this.currentItemBurnTime = par1NBTTagCompound.getInteger("fcItemBurnTimeEx");
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setShort("BurnTime", (short)this.furnaceBurnTime);
      par1NBTTagCompound.setShort("CookTime", (short)this.furnaceCookTime);
      NBTTagList var2 = new NBTTagList();

      for (int var3 = 0; var3 < this.furnaceItemStacks.length; var3++) {
         if (this.furnaceItemStacks[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.furnaceItemStacks[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      par1NBTTagCompound.setTag("Items", var2);
      if (this.isInvNameLocalized()) {
         par1NBTTagCompound.setString("CustomName", this.field_94130_e);
      }

      par1NBTTagCompound.setInteger("fcBurnTimeEx", this.furnaceBurnTime);
      par1NBTTagCompound.setInteger("fcCookTimeEx", this.furnaceCookTime);
      par1NBTTagCompound.setInteger("fcItemBurnTimeEx", this.currentItemBurnTime);
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Environment(EnvType.CLIENT)
   public int getCookProgressScaled(int par1) {
      int iCookProgress = this.furnaceCookTime * par1 / this.getCookTimeForCurrentItem();
      if (iCookProgress == 0 && this.isBurning() && this.canSmelt()) {
         iCookProgress = 1;
      }

      return iCookProgress;
   }

   @Environment(EnvType.CLIENT)
   public int getBurnTimeRemainingScaled(int par1) {
      if (this.currentItemBurnTime == 0) {
         this.currentItemBurnTime = 200;
      }

      return this.furnaceBurnTime * par1 / this.currentItemBurnTime;
   }

   public boolean isBurning() {
      return this.furnaceBurnTime > 0;
   }

   @Override
   public void updateEntity() {
      boolean var1 = this.furnaceBurnTime > 0;
      boolean var2 = false;
      if (this.furnaceBurnTime > 0) {
         this.furnaceBurnTime--;
      }

      if (!this.worldObj.isRemote) {
         if (this.furnaceBurnTime == 0) {
            this.currentItemBurnTime = this.furnaceBurnTime = this.getItemBurnTime(this.furnaceItemStacks[1]);
            if (this.furnaceBurnTime > 0) {
               var2 = true;
               if (this.furnaceItemStacks[1] != null) {
                  this.furnaceItemStacks[1].stackSize--;
                  if (this.furnaceItemStacks[1].stackSize == 0) {
                     Item var3 = this.furnaceItemStacks[1].getItem().getContainerItem();
                     this.furnaceItemStacks[1] = var3 != null ? new ItemStack(var3) : null;
                  }
               }
            }
         }

         if (this.isBurning() && this.canSmelt()) {
            this.furnaceCookTime++;
            if (this.furnaceCookTime >= this.getCookTimeForCurrentItem()) {
               this.furnaceCookTime = 0;
               this.smeltItem();
               var2 = true;
            }
         } else {
            this.furnaceCookTime = 0;
         }

         boolean bHasVisibleContents = this.furnaceItemStacks[0] != null || this.furnaceItemStacks[2] != null;
         FurnaceBlock furnaceBlock = (FurnaceBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
         if (var1 != this.furnaceBurnTime > 0) {
            var2 = true;
            furnaceBlock.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord, bHasVisibleContents);
         } else {
            boolean bPreviousContentsState = (this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 8) != 0;
            if (bPreviousContentsState != bHasVisibleContents) {
               furnaceBlock.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord, bHasVisibleContents);
            }
         }
      }

      if (var2) {
         this.k_();
      }
   }

   protected boolean canSmelt() {
      if (this.furnaceItemStacks[0] == null) {
         return false;
      } else {
         ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0].getItem().itemID);
         if (var1 == null) {
            return false;
         } else if (this.furnaceItemStacks[2] == null) {
            return true;
         } else if (!this.furnaceItemStacks[2].isItemEqual(var1)) {
            return false;
         } else {
            int iOutputStackSizeIfCooked = this.furnaceItemStacks[2].stackSize + var1.stackSize;
            return iOutputStackSizeIfCooked <= this.getInventoryStackLimit() && iOutputStackSizeIfCooked <= this.furnaceItemStacks[2].getMaxStackSize()
               ? true
               : iOutputStackSizeIfCooked <= var1.getMaxStackSize();
         }
      }
   }

   public void smeltItem() {
      if (this.canSmelt()) {
         ItemStack var1 = FurnaceRecipes.smelting().getSmeltingResult(this.furnaceItemStacks[0].getItem().itemID);
         if (this.furnaceItemStacks[2] == null) {
            this.furnaceItemStacks[2] = var1.copy();
         } else if (this.furnaceItemStacks[2].itemID == var1.itemID) {
            this.furnaceItemStacks[2].stackSize = this.furnaceItemStacks[2].stackSize + var1.stackSize;
         }

         this.furnaceItemStacks[0].stackSize--;
         if (this.furnaceItemStacks[0].stackSize <= 0) {
            this.furnaceItemStacks[0] = null;
         }
      }
   }

   public static boolean isItemFuel(ItemStack par0ItemStack) {
      return par0ItemStack.getItem().getFurnaceBurnTime(par0ItemStack.getItemDamage()) > 0;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this
         ? false
         : par1EntityPlayer.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
      return par1 == 2 ? false : (par1 == 1 ? isItemFuel(par2ItemStack) : true);
   }

   @Override
   public int[] getAccessibleSlotsFromSide(int par1) {
      return par1 == 0 ? field_102011_e : (par1 == 1 ? field_102010_d : field_102009_f);
   }

   @Override
   public boolean canInsertItem(int par1, ItemStack par2ItemStack, int par3) {
      return this.isStackValidForSlot(par1, par2ItemStack);
   }

   @Override
   public boolean canExtractItem(int par1, ItemStack par2ItemStack, int par3) {
      return par3 != 0 || par1 != 1 || par2ItemStack.itemID == Item.bucketEmpty.itemID;
   }

   protected int getCookTimeForCurrentItem() {
      int iCookTimeShift = 0;
      if (this.furnaceItemStacks[0] != null) {
         iCookTimeShift = FurnaceRecipes.smelting().getCookTimeBinaryShift(this.furnaceItemStacks[0].getItem().itemID);
      }

      return 400 << iCookTimeShift;
   }

   public int getItemBurnTime(ItemStack stack) {
      return stack != null ? stack.getItem().getFurnaceBurnTime(stack.getItemDamage()) * 2 : 0;
   }
}
