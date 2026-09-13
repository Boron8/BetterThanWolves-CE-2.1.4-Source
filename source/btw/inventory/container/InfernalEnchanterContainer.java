package btw.inventory.container;

import btw.block.BTWBlocks;
import btw.inventory.inventories.InfernalEnchanterInventory;
import btw.item.BTWItems;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.Container;
import net.minecraft.src.Enchantment;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ICrafting;
import net.minecraft.src.IInventory;
import net.minecraft.src.InventoryPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Slot;
import net.minecraft.src.World;

public class InfernalEnchanterContainer extends Container {
   public IInventory tableInventory;
   private World localWorld;
   private int blockX;
   private int blockY;
   private int blockZ;
   private static final double MAX_INTERACTION_DISTANCE = 8.0;
   private static final double MAX_INTERACTION_DISTANCE_SQ = 64.0;
   private static final int SLOT_SCREEN_WIDTH = 18;
   private static final int SLOT_SCREEN_HEIGHT = 18;
   private static final int SCROLL_SLOT_SCREEN_POS_X = 17;
   private static final int SCROLL_SLOT_SCREEN_POS_Y = 37;
   private static final int ITEM_SLOT_SCREEN_POS_X = 17;
   private static final int ITEM_SLOT_SCREEN_POS_Y = 75;
   private static final int PLAYER_INVENTORY_SCREEN_POS_X = 8;
   private static final int PLAYER_INVENTORY_SCREEN_POS_Y = 129;
   private static final int PLAYER_HOTBAR_SCREEN_POS_Y = 187;
   private static final int HORIZONTAL_BOOK_SHELF_CHECK_DISTANCE = 8;
   private static final int VERTICAL_POSITIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
   private static final int VERTICAL_NEGATIVE_BOOK_SHELF_CHECK_DISTANCE = 8;
   public static final int MAX_ENCHANTMENT_POWER_LEVEL = 5;
   public int[] currentEnchantmentLevels;
   public int maxSurroundingBookshelfLevel;
   public int lastMaxSurroundingBookshelfLevel;
   public long nameSeed;
   Random rand;

   public InfernalEnchanterContainer(InventoryPlayer playerInventory, World world, int i, int j, int k) {
      this.localWorld = world;
      this.blockX = i;
      this.blockY = j;
      this.blockZ = k;
      this.rand = new Random();
      this.nameSeed = this.rand.nextLong();
      this.tableInventory = new InfernalEnchanterInventory(this, "fcInfernalEnchanterInv", 2);
      this.currentEnchantmentLevels = new int[5];
      this.resetEnchantingLevels();
      this.maxSurroundingBookshelfLevel = 0;
      this.lastMaxSurroundingBookshelfLevel = 0;
      this.a(new Slot(this.tableInventory, 0, 17, 37));
      this.a(new Slot(this.tableInventory, 1, 17, 75));

      for (int tempSlotY = 0; tempSlotY < 3; tempSlotY++) {
         for (int tempSlotX = 0; tempSlotX < 9; tempSlotX++) {
            this.a(new Slot(playerInventory, tempSlotX + tempSlotY * 9 + 9, 8 + tempSlotX * 18, 129 + tempSlotY * 18));
         }
      }

      for (int tempSlotX = 0; tempSlotX < 9; tempSlotX++) {
         this.a(new Slot(playerInventory, tempSlotX, 8 + tempSlotX * 18, 187));
      }

      if (world != null && !world.isRemote) {
         this.checkForSurroundingBookshelves();
      }
   }

   @Override
   public void onCraftMatrixChanged(IInventory inventory) {
      if (inventory == this.tableInventory) {
         this.nameSeed = this.rand.nextLong();
         this.resetEnchantingLevels();
         this.computeCurrentEnchantmentLevels();
         if (this.localWorld != null && !this.localWorld.isRemote) {
            this.detectAndSendChanges();
         }
      }
   }

   @Override
   public void onCraftGuiClosed(EntityPlayer player) {
      super.onCraftGuiClosed(player);
      if (this.localWorld != null && !this.localWorld.isRemote) {
         for (int i = 0; i < this.tableInventory.getSizeInventory(); i++) {
            ItemStack itemstack = this.tableInventory.getStackInSlot(i);
            if (itemstack != null) {
               player.dropPlayerItem(itemstack);
            }
         }
      }
   }

   @Override
   public boolean canInteractWith(EntityPlayer entityplayer) {
      if (this.localWorld != null && !this.localWorld.isRemote) {
         return this.localWorld.getBlockId(this.blockX, this.blockY, this.blockZ) != BTWBlocks.infernalEnchanter.blockID
            ? false
            : entityplayer.e(this.blockX + 0.5, this.blockY + 0.5, this.blockZ + 0.5) <= 64.0;
      } else {
         return true;
      }
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int iSlotIndex) {
      ItemStack clickedStack = null;
      Slot slot = (Slot)this.inventorySlots.get(iSlotIndex);
      if (slot != null && slot.getHasStack()) {
         ItemStack processedStack = slot.getStack();
         clickedStack = processedStack.copy();
         if (iSlotIndex <= 1) {
            if (!this.a(processedStack, 2, 38, true)) {
               return null;
            }
         } else if (processedStack.getItem().itemID == BTWItems.arcaneScroll.itemID) {
            if (!this.a(processedStack, 0, 1, false)) {
               return null;
            }
         } else if (this.getMaximumEnchantmentCost(processedStack) > 0) {
            if (!this.a(processedStack, 1, 2, false)) {
               return null;
            }
         } else if (iSlotIndex >= 2 && iSlotIndex < 29) {
            if (!this.a(processedStack, 29, 38, false)) {
               return null;
            }
         } else if (iSlotIndex >= 29 && iSlotIndex < 38 && !this.a(processedStack, 2, 29, false)) {
            return null;
         }

         if (processedStack.stackSize == 0) {
            slot.putStack(null);
         } else {
            slot.onSlotChanged();
         }

         if (processedStack.stackSize == clickedStack.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(player, processedStack);
         if (this.localWorld != null && !this.localWorld.isRemote) {
            this.detectAndSendChanges();
         }
      }

      return clickedStack;
   }

   private void checkForSurroundingBookshelves() {
      int iBookshelfCount = 0;

      for (int iTempI = this.blockX - 8; iTempI <= this.blockX + 8; iTempI++) {
         for (int iTempJ = this.blockY - 8; iTempJ <= this.blockY + 8; iTempJ++) {
            for (int iTempK = this.blockZ - 8; iTempK <= this.blockZ + 8; iTempK++) {
               if (this.isValidBookshelf(iTempI, iTempJ, iTempK)) {
                  iBookshelfCount++;
               }
            }
         }
      }

      this.maxSurroundingBookshelfLevel = iBookshelfCount;
   }

   private boolean isValidBookshelf(int i, int j, int k) {
      int iBlockID = this.localWorld.getBlockId(i, j, k);
      return iBlockID == Block.bookShelf.blockID
         && (
            this.localWorld.isAirBlock(i + 1, j, k)
               || this.localWorld.isAirBlock(i - 1, j, k)
               || this.localWorld.isAirBlock(i, j, k + 1)
               || this.localWorld.isAirBlock(i, j, k - 1)
         );
   }

   private void setCurrentEnchantingLevels(int iMaxPowerLevel, int iCostMultiplier, int iMaxBaseCostForItem) {
      this.resetEnchantingLevels();
      if (iMaxPowerLevel == 1) {
         this.currentEnchantmentLevels[0] = 30;
      } else if (iMaxPowerLevel == 2) {
         this.currentEnchantmentLevels[0] = 15;
         this.currentEnchantmentLevels[1] = 30;
      } else if (iMaxPowerLevel == 3) {
         this.currentEnchantmentLevels[0] = 10;
         this.currentEnchantmentLevels[1] = 20;
         this.currentEnchantmentLevels[2] = 30;
      } else if (iMaxPowerLevel == 4) {
         this.currentEnchantmentLevels[0] = 8;
         this.currentEnchantmentLevels[1] = 15;
         this.currentEnchantmentLevels[2] = 23;
         this.currentEnchantmentLevels[3] = 30;
      } else if (iMaxPowerLevel == 5) {
         this.currentEnchantmentLevels[0] = 6;
         this.currentEnchantmentLevels[1] = 12;
         this.currentEnchantmentLevels[2] = 18;
         this.currentEnchantmentLevels[3] = 24;
         this.currentEnchantmentLevels[4] = 30;
      }

      int iCostIncrement = (iCostMultiplier - 1) * 30;

      for (int iTemp = 0; iTemp < 5; iTemp++) {
         if (this.currentEnchantmentLevels[iTemp] > 0) {
            if (iMaxBaseCostForItem < this.currentEnchantmentLevels[iTemp]) {
               this.currentEnchantmentLevels[iTemp] = 0;
            } else {
               this.currentEnchantmentLevels[iTemp] = this.currentEnchantmentLevels[iTemp] + iCostIncrement;
            }
         }
      }
   }

   private void resetEnchantingLevels() {
      for (int iTemp = 0; iTemp < 5; iTemp++) {
         this.currentEnchantmentLevels[iTemp] = 0;
      }
   }

   private void computeCurrentEnchantmentLevels() {
      ItemStack scrollStack = this.tableInventory.getStackInSlot(0);
      if (scrollStack != null && scrollStack.getItem().itemID == BTWItems.arcaneScroll.itemID) {
         ItemStack itemToEnchantStack = this.tableInventory.getStackInSlot(1);
         if (itemToEnchantStack != null) {
            int iMaxEnchantmentCost = this.getMaximumEnchantmentCost(itemToEnchantStack);
            if (iMaxEnchantmentCost > 0) {
               int iEnchantmentIndex = scrollStack.getItemDamage();
               if (iEnchantmentIndex >= Enchantment.enchantmentsList.length || Enchantment.enchantmentsList[iEnchantmentIndex] == null) {
                  return;
               }

               if (this.isEnchantmentAppropriateForItem(iEnchantmentIndex, itemToEnchantStack)
                  && !this.doesEnchantmentConflictWithExistingOnes(iEnchantmentIndex, itemToEnchantStack)) {
                  int iMaxNumberOfItemEnchants = this.getMaximumNumberOfEnchantments(itemToEnchantStack);
                  int iCurrentNumberOfItemEnchants = 0;
                  NBTTagList enchantmentTagList = itemToEnchantStack.getEnchantmentTagList();
                  if (enchantmentTagList != null) {
                     iCurrentNumberOfItemEnchants = itemToEnchantStack.getEnchantmentTagList().tagCount();
                  }

                  if (iCurrentNumberOfItemEnchants < iMaxNumberOfItemEnchants) {
                     this.setCurrentEnchantingLevels(
                        this.getMaxEnchantmentPowerLevel(iEnchantmentIndex, itemToEnchantStack),
                        iCurrentNumberOfItemEnchants + 1,
                        this.getMaximumEnchantmentCost(itemToEnchantStack)
                     );
                  }
               }
            }
         }
      }
   }

   private int getMaximumEnchantmentCost(ItemStack itemStack) {
      return itemStack.getItem().getInfernalMaxEnchantmentCost();
   }

   private int getMaximumNumberOfEnchantments(ItemStack itemStack) {
      return itemStack.getItem().getInfernalMaxNumEnchants();
   }

   private boolean isEnchantmentAppropriateForItem(int iEnchantmentIndex, ItemStack itemStack) {
      return Enchantment.enchantmentsList[iEnchantmentIndex].canApply(itemStack);
   }

   private boolean doesEnchantmentConflictWithExistingOnes(int iEnchantmentIndex, ItemStack itemStack) {
      NBTTagList enchantmentTagList = itemStack.getEnchantmentTagList();
      if (enchantmentTagList != null) {
         int iCurrentNumberOfItemEnchants = itemStack.getEnchantmentTagList().tagCount();

         for (int iTemp = 0; iTemp < iCurrentNumberOfItemEnchants; iTemp++) {
            int iTempEnchantmentIndex = ((NBTTagCompound)enchantmentTagList.tagAt(iTemp)).getShort("id");
            if (iTempEnchantmentIndex == iEnchantmentIndex) {
               return true;
            }

            if (iEnchantmentIndex == Enchantment.silkTouch.effectId && iTempEnchantmentIndex == Enchantment.fortune.effectId
               || iEnchantmentIndex == Enchantment.fortune.effectId && iTempEnchantmentIndex == Enchantment.silkTouch.effectId) {
               return true;
            }
         }
      }

      return false;
   }

   private int getMaxEnchantmentPowerLevel(int iEnchantmentIndex, ItemStack itemStack) {
      return iEnchantmentIndex == Enchantment.respiration.effectId && itemStack.getItem().itemID == BTWItems.plateHelmet.itemID
         ? 5
         : Enchantment.enchantmentsList[iEnchantmentIndex].getMaxLevel();
   }

   @Override
   public boolean enchantItem(EntityPlayer player, int iButtonIndex) {
      if (this.localWorld != null && !this.localWorld.isRemote) {
         int iButtonEnchantmentLevel = this.currentEnchantmentLevels[iButtonIndex];
         if (iButtonEnchantmentLevel > 0) {
            boolean bPlayerCapable = iButtonEnchantmentLevel <= player.experienceLevel && iButtonEnchantmentLevel <= this.maxSurroundingBookshelfLevel;
            if (bPlayerCapable) {
               ItemStack scrollStack = this.tableInventory.getStackInSlot(0);
               if (scrollStack != null && scrollStack.getItem().itemID == BTWItems.arcaneScroll.itemID) {
                  ItemStack itemToEnchantStack = this.tableInventory.getStackInSlot(1);
                  if (itemToEnchantStack != null) {
                     int iEnchantmentIndex = scrollStack.getItemDamage();
                     if (iEnchantmentIndex < Enchantment.enchantmentsList.length && Enchantment.enchantmentsList[iEnchantmentIndex] != null) {
                        itemToEnchantStack.addEnchantment(Enchantment.enchantmentsList[iEnchantmentIndex], iButtonIndex + 1);
                        player.addExperienceLevel(-iButtonEnchantmentLevel);
                        this.tableInventory.decrStackSize(0, 1);
                        this.onCraftMatrixChanged(this.tableInventory);
                        this.localWorld.playSoundAtEntity(player, "ambient.weather.thunder", 1.0F, this.localWorld.rand.nextFloat() * 0.4F + 0.8F);
                        return true;
                     }

                     return false;
                  }
               }
            }
         }

         return false;
      } else {
         return true;
      }
   }

   @Override
   public void addCraftingToCrafters(ICrafting craftingInterface) {
      super.addCraftingToCrafters(craftingInterface);
      craftingInterface.sendProgressBarUpdate(this, 0, this.maxSurroundingBookshelfLevel);
   }

   @Override
   public void detectAndSendChanges() {
      super.detectAndSendChanges();

      for (ICrafting icrafting : this.crafters) {
         if (this.lastMaxSurroundingBookshelfLevel != this.maxSurroundingBookshelfLevel) {
            icrafting.sendProgressBarUpdate(this, 0, this.maxSurroundingBookshelfLevel);
         }
      }

      this.lastMaxSurroundingBookshelfLevel = this.maxSurroundingBookshelfLevel;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void updateProgressBar(int iVariableIndex, int iValue) {
      if (iVariableIndex == 0) {
         this.maxSurroundingBookshelfLevel = iValue;
      }
   }
}
