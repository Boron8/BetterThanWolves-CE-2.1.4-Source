package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class InventoryPlayer implements IInventory {
   public ItemStack[] mainInventory = new ItemStack[36];
   public ItemStack[] armorInventory = new ItemStack[4];
   public int currentItem = 0;
   @Environment(EnvType.CLIENT)
   private ItemStack currentItemStack;
   public EntityPlayer player;
   private ItemStack itemStack;
   public boolean inventoryChanged = false;

   public InventoryPlayer(EntityPlayer par1EntityPlayer) {
      this.player = par1EntityPlayer;
   }

   public ItemStack getCurrentItem() {
      return this.currentItem < 9 && this.currentItem >= 0 ? this.mainInventory[this.currentItem] : null;
   }

   public static int getHotbarSize() {
      return 9;
   }

   private int getInventorySlotContainItem(int par1) {
      for (int var2 = 0; var2 < this.mainInventory.length; var2++) {
         if (this.mainInventory[var2] != null && this.mainInventory[var2].itemID == par1) {
            return var2;
         }
      }

      return -1;
   }

   @Environment(EnvType.CLIENT)
   private int getInventorySlotContainItemAndDamage(int par1, int par2) {
      for (int var3 = 0; var3 < this.mainInventory.length; var3++) {
         if (this.mainInventory[var3] != null && this.mainInventory[var3].itemID == par1 && this.mainInventory[var3].getItemDamage() == par2) {
            return var3;
         }
      }

      return -1;
   }

   private int storeItemStack(ItemStack par1ItemStack) {
      for (int var2 = 0; var2 < this.mainInventory.length; var2++) {
         if (this.mainInventory[var2] != null
            && this.mainInventory[var2].itemID == par1ItemStack.itemID
            && this.mainInventory[var2].isStackable()
            && this.mainInventory[var2].stackSize < this.mainInventory[var2].getMaxStackSize()
            && this.mainInventory[var2].stackSize < this.getInventoryStackLimit()
            && (!this.mainInventory[var2].getHasSubtypes() || this.mainInventory[var2].getItemDamage() == par1ItemStack.getItemDamage())
            && ItemStack.areItemStackTagsEqual(this.mainInventory[var2], par1ItemStack)) {
            return var2;
         }
      }

      return -1;
   }

   public int getFirstEmptyStack() {
      for (int var1 = 0; var1 < this.mainInventory.length; var1++) {
         if (this.mainInventory[var1] == null) {
            return var1;
         }
      }

      return -1;
   }

   @Environment(EnvType.CLIENT)
   public void setCurrentItem(int par1, int par2, boolean par3, boolean par4) {
      boolean var5 = true;
      this.currentItemStack = this.getCurrentItem();
      int var7;
      if (par3) {
         var7 = this.getInventorySlotContainItemAndDamage(par1, par2);
      } else {
         var7 = this.getInventorySlotContainItem(par1);
      }

      if (var7 >= 0 && var7 < 9) {
         this.currentItem = var7;
      } else if (par4 && par1 > 0) {
         int var6 = this.getFirstEmptyStack();
         if (var6 >= 0 && var6 < 9) {
            this.currentItem = var6;
         }

         this.func_70439_a(Item.itemsList[par1], par2);
      }
   }

   @Environment(EnvType.CLIENT)
   public void changeCurrentItem(int par1) {
      if (par1 > 0) {
         par1 = 1;
      }

      if (par1 < 0) {
         par1 = -1;
      }

      this.currentItem -= par1;

      while (this.currentItem < 0) {
         this.currentItem += 9;
      }

      while (this.currentItem >= 9) {
         this.currentItem -= 9;
      }
   }

   public int clearInventory(int par1, int par2) {
      int var3 = 0;

      for (int var4 = 0; var4 < this.mainInventory.length; var4++) {
         ItemStack var5 = this.mainInventory[var4];
         if (var5 != null && (par1 <= -1 || var5.itemID == par1) && (par2 <= -1 || var5.getItemDamage() == par2)) {
            var3 += var5.stackSize;
            this.mainInventory[var4] = null;
         }
      }

      for (int var6 = 0; var6 < this.armorInventory.length; var6++) {
         ItemStack var5 = this.armorInventory[var6];
         if (var5 != null && (par1 <= -1 || var5.itemID == par1) && (par2 <= -1 || var5.getItemDamage() == par2)) {
            var3 += var5.stackSize;
            this.armorInventory[var6] = null;
         }
      }

      return var3;
   }

   @Environment(EnvType.CLIENT)
   public void func_70439_a(Item par1Item, int par2) {
      if (par1Item != null) {
         int var3 = this.getInventorySlotContainItemAndDamage(par1Item.itemID, par2);
         if (var3 >= 0) {
            this.mainInventory[var3] = this.mainInventory[this.currentItem];
         }

         if (this.currentItemStack != null
            && this.currentItemStack.isItemEnchantable()
            && this.getInventorySlotContainItemAndDamage(this.currentItemStack.itemID, this.currentItemStack.getItemDamageForDisplay()) == this.currentItem) {
            return;
         }

         this.mainInventory[this.currentItem] = new ItemStack(Item.itemsList[par1Item.itemID], 1, par2);
      }
   }

   private int storePartialItemStack(ItemStack par1ItemStack) {
      int var2 = par1ItemStack.itemID;
      int var3 = par1ItemStack.stackSize;
      if (par1ItemStack.getMaxStackSize() == 1) {
         int var4 = this.getFirstEmptyStack();
         if (var4 < 0) {
            return var3;
         } else {
            if (this.mainInventory[var4] == null) {
               this.mainInventory[var4] = ItemStack.copyItemStack(par1ItemStack);
            }

            return 0;
         }
      } else {
         int var4 = this.storeItemStack(par1ItemStack);
         if (var4 < 0) {
            var4 = this.getFirstEmptyStack();
         }

         if (var4 < 0) {
            return var3;
         } else {
            if (this.mainInventory[var4] == null) {
               this.mainInventory[var4] = new ItemStack(var2, 0, par1ItemStack.getItemDamage());
               if (par1ItemStack.hasTagCompound()) {
                  this.mainInventory[var4].setTagCompound((NBTTagCompound)par1ItemStack.getTagCompound().copy());
               }
            }

            int var5 = var3;
            if (var3 > this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize) {
               var5 = this.mainInventory[var4].getMaxStackSize() - this.mainInventory[var4].stackSize;
            }

            if (var5 > this.getInventoryStackLimit() - this.mainInventory[var4].stackSize) {
               var5 = this.getInventoryStackLimit() - this.mainInventory[var4].stackSize;
            }

            if (var5 == 0) {
               return var3;
            } else {
               var3 -= var5;
               this.mainInventory[var4].stackSize += var5;
               this.mainInventory[var4].animationsToGo = 5;
               return var3;
            }
         }
      }
   }

   public void decrementAnimations() {
      for (int var1 = 0; var1 < this.mainInventory.length; var1++) {
         if (this.mainInventory[var1] != null) {
            this.mainInventory[var1].updateAnimation(this.player.worldObj, this.player, var1, this.currentItem == var1);
         }
      }
   }

   public boolean consumeInventoryItem(int par1) {
      int var2 = this.getInventorySlotContainItem(par1);
      if (var2 < 0) {
         return false;
      } else {
         if (--this.mainInventory[var2].stackSize <= 0) {
            this.mainInventory[var2] = null;
         }

         return true;
      }
   }

   public boolean hasItem(int par1) {
      int var2 = this.getInventorySlotContainItem(par1);
      return var2 >= 0;
   }

   public boolean addItemStackToInventory(ItemStack par1ItemStack) {
      if (par1ItemStack == null) {
         return false;
      } else {
         try {
            if (par1ItemStack.isItemDamaged()) {
               int var2 = this.getFirstEmptyStack();
               if (var2 >= 0) {
                  this.mainInventory[var2] = ItemStack.copyItemStack(par1ItemStack);
                  this.mainInventory[var2].animationsToGo = 5;
                  par1ItemStack.stackSize = 0;
                  return true;
               } else if (this.player.capabilities.isCreativeMode) {
                  par1ItemStack.stackSize = 0;
                  return true;
               } else {
                  return false;
               }
            } else {
               int var2;
               do {
                  var2 = par1ItemStack.stackSize;
                  par1ItemStack.stackSize = this.storePartialItemStack(par1ItemStack);
               } while (par1ItemStack.stackSize > 0 && par1ItemStack.stackSize < var2);

               if (par1ItemStack.stackSize == var2 && this.player.capabilities.isCreativeMode) {
                  par1ItemStack.stackSize = 0;
                  return true;
               } else {
                  return par1ItemStack.stackSize < var2;
               }
            }
         } catch (Throwable var51) {
            CrashReport var3 = CrashReport.makeCrashReport(var51, "Adding item to inventory");
            CrashReportCategory var4 = var3.makeCategory("Item being added");
            var4.addCrashSection("Item ID", par1ItemStack.itemID);
            var4.addCrashSection("Item data", par1ItemStack.getItemDamage());
            var4.addCrashSectionCallable("Item name", new CallableItemName(this, par1ItemStack));
            throw new ReportedException(var3);
         }
      }
   }

   @Override
   public ItemStack decrStackSize(int par1, int par2) {
      ItemStack[] var3 = this.mainInventory;
      if (par1 >= this.mainInventory.length) {
         var3 = this.armorInventory;
         par1 -= this.mainInventory.length;
      }

      if (var3[par1] != null) {
         if (var3[par1].stackSize <= par2) {
            ItemStack var4 = var3[par1];
            var3[par1] = null;
            return var4;
         } else {
            ItemStack var4 = var3[par1].splitStack(par2);
            if (var3[par1].stackSize == 0) {
               var3[par1] = null;
            }

            return var4;
         }
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      ItemStack[] var2 = this.mainInventory;
      if (par1 >= this.mainInventory.length) {
         var2 = this.armorInventory;
         par1 -= this.mainInventory.length;
      }

      if (var2[par1] != null) {
         ItemStack var3 = var2[par1];
         var2[par1] = null;
         return var3;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      ItemStack[] var3 = this.mainInventory;
      if (par1 >= var3.length) {
         par1 -= var3.length;
         var3 = this.armorInventory;
      }

      var3[par1] = par2ItemStack;
   }

   public float getStrVsBlock(World world, Block par1Block, int i, int j, int k) {
      float var2x = 1.0F;
      if (this.mainInventory[this.currentItem] != null) {
         var2x *= this.mainInventory[this.currentItem].getStrVsBlock(world, par1Block, i, j, k);
      }

      return var2x;
   }

   public NBTTagList writeToNBT(NBTTagList par1NBTTagList) {
      for (int var2 = 0; var2 < this.mainInventory.length; var2++) {
         if (this.mainInventory[var2] != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            var3.setByte("Slot", (byte)var2);
            this.mainInventory[var2].writeToNBT(var3);
            par1NBTTagList.appendTag(var3);
         }
      }

      for (int var4 = 0; var4 < this.armorInventory.length; var4++) {
         if (this.armorInventory[var4] != null) {
            NBTTagCompound var3 = new NBTTagCompound();
            var3.setByte("Slot", (byte)(var4 + 100));
            this.armorInventory[var4].writeToNBT(var3);
            par1NBTTagList.appendTag(var3);
         }
      }

      return par1NBTTagList;
   }

   public void readFromNBT(NBTTagList par1NBTTagList) {
      this.mainInventory = new ItemStack[36];
      this.armorInventory = new ItemStack[4];

      for (int var2 = 0; var2 < par1NBTTagList.tagCount(); var2++) {
         NBTTagCompound var3 = (NBTTagCompound)par1NBTTagList.tagAt(var2);
         int var4 = var3.getByte("Slot") & 255;
         ItemStack var5 = ItemStack.loadItemStackFromNBT(var3);
         if (var5 != null) {
            if (var4 >= 0 && var4 < this.mainInventory.length) {
               this.mainInventory[var4] = var5;
            }

            if (var4 >= 100 && var4 < this.armorInventory.length + 100) {
               this.armorInventory[var4 - 100] = var5;
            }
         }
      }
   }

   @Override
   public int getSizeInventory() {
      return this.mainInventory.length + 4;
   }

   @Override
   public ItemStack getStackInSlot(int par1) {
      ItemStack[] var2 = this.mainInventory;
      if (par1 >= var2.length) {
         par1 -= var2.length;
         var2 = this.armorInventory;
      }

      return var2[par1];
   }

   @Override
   public String getInvName() {
      return "container.inventory";
   }

   @Override
   public boolean isInvNameLocalized() {
      return false;
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   public int getDamageVsEntity(Entity par1Entity) {
      ItemStack var2 = this.getStackInSlot(this.currentItem);
      return var2 != null ? var2.getDamageVsEntity(par1Entity) : 1;
   }

   public boolean canHarvestBlock(World world, Block par1Block, int i, int j, int k) {
      if (par1Block.blockMaterial.isToolNotRequired()) {
         return true;
      } else {
         ItemStack var2x = this.getStackInSlot(this.currentItem);
         return var2x != null ? var2x.canHarvestBlock(world, par1Block, i, j, k) : false;
      }
   }

   public ItemStack armorItemInSlot(int par1) {
      return this.armorInventory[par1];
   }

   public int getTotalArmorValue() {
      int var1 = 0;

      for (int var2 = 0; var2 < this.armorInventory.length; var2++) {
         if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
            int var3 = ((ItemArmor)this.armorInventory[var2].getItem()).damageReduceAmount;
            var1 += var3;
         }
      }

      return var1;
   }

   public void damageArmor(int par1) {
      par1 /= 4;
      if (par1 < 1) {
         par1 = 1;
      }

      for (int var2 = 0; var2 < this.armorInventory.length; var2++) {
         if (this.armorInventory[var2] != null && this.armorInventory[var2].getItem() instanceof ItemArmor) {
            this.armorInventory[var2].damageItem(par1, this.player);
            if (this.armorInventory[var2].stackSize == 0) {
               this.armorInventory[var2] = null;
            }
         }
      }
   }

   public void dropAllItems() {
      for (int var1 = 0; var1 < this.mainInventory.length; var1++) {
         if (this.mainInventory[var1] != null) {
            this.player.dropPlayerItemWithRandomChoice(this.mainInventory[var1], true);
            this.mainInventory[var1] = null;
         }
      }

      for (int var2 = 0; var2 < this.armorInventory.length; var2++) {
         if (this.armorInventory[var2] != null) {
            this.player.dropPlayerItemWithRandomChoice(this.armorInventory[var2], true);
            this.armorInventory[var2] = null;
         }
      }
   }

   @Override
   public void onInventoryChanged() {
      this.inventoryChanged = true;
   }

   public void setItemStack(ItemStack par1ItemStack) {
      this.itemStack = par1ItemStack;
   }

   public ItemStack getItemStack() {
      return this.itemStack;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.player.isDead ? false : par1EntityPlayer.e(this.player) <= 64.0;
   }

   public boolean hasItemStack(ItemStack par1ItemStack) {
      for (int var2 = 0; var2 < this.armorInventory.length; var2++) {
         if (this.armorInventory[var2] != null && this.armorInventory[var2].isItemEqual(par1ItemStack)) {
            return true;
         }
      }

      for (int var3 = 0; var3 < this.mainInventory.length; var3++) {
         if (this.mainInventory[var3] != null && this.mainInventory[var3].isItemEqual(par1ItemStack)) {
            return true;
         }
      }

      return false;
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
      return true;
   }

   public void copyInventory(InventoryPlayer par1InventoryPlayer) {
      for (int var2 = 0; var2 < this.mainInventory.length; var2++) {
         this.mainInventory[var2] = ItemStack.copyItemStack(par1InventoryPlayer.mainInventory[var2]);
      }

      for (int var3 = 0; var3 < this.armorInventory.length; var3++) {
         this.armorInventory[var3] = ItemStack.copyItemStack(par1InventoryPlayer.armorInventory[var3]);
      }

      this.currentItem = par1InventoryPlayer.currentItem;
   }
}
