package btw.inventory.util;

import btw.block.BTWBlocks;
import btw.world.util.BlockPos;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;

public class InventoryUtils {
   public static final int IGNORE_METADATA = 32767;

   public static void clearInventoryContents(IInventory inventory) {
      for (int iSlot = 0; iSlot < inventory.getSizeInventory(); iSlot++) {
         ItemStack itemstack = inventory.getStackInSlot(iSlot);
         if (itemstack != null) {
            inventory.setInventorySlotContents(iSlot, null);
         }
      }
   }

   public static void ejectInventoryContents(World world, int i, int j, int k, IInventory inventory) {
      if (inventory != null) {
         for (int l = 0; l < inventory.getSizeInventory(); l++) {
            ItemStack itemstack = inventory.getStackInSlot(l);
            if (itemstack != null) {
               float f = world.rand.nextFloat() * 0.7F + 0.15F;
               float f1 = world.rand.nextFloat() * 0.7F + 0.15F;
               float f2 = world.rand.nextFloat() * 0.7F + 0.15F;

               while (itemstack.stackSize > 0) {
                  int i1 = world.rand.nextInt(21) + 10;
                  if (i1 > itemstack.stackSize) {
                     i1 = itemstack.stackSize;
                  }

                  itemstack.stackSize -= i1;
                  EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(
                     EntityItem.class,
                     world,
                     (double)(i + f),
                     (double)(j + f1),
                     (double)(k + f2),
                     new ItemStack(itemstack.itemID, i1, itemstack.getItemDamage())
                  );
                  float f3 = 0.05F;
                  entityitem.motionX = (float)world.rand.nextGaussian() * f3;
                  entityitem.motionY = (float)world.rand.nextGaussian() * f3 + 0.2F;
                  entityitem.motionZ = (float)world.rand.nextGaussian() * f3;
                  copyEnchantments(entityitem.getEntityItem(), itemstack);
                  world.spawnEntityInWorld(entityitem);
               }
            }
         }
      }
   }

   public static void copyEnchantments(ItemStack destStack, ItemStack sourceStack) {
      if (sourceStack.hasTagCompound()) {
         destStack.setTagCompound((NBTTagCompound)sourceStack.getTagCompound().copy());
      }
   }

   public static ItemStack decreaseStackSize(IInventory inventory, int iSlot, int iAmount) {
      if (inventory.getStackInSlot(iSlot) != null) {
         if (inventory.getStackInSlot(iSlot).stackSize <= iAmount) {
            ItemStack itemstack = inventory.getStackInSlot(iSlot);
            inventory.setInventorySlotContents(iSlot, null);
            return itemstack;
         } else {
            ItemStack splitStack = inventory.getStackInSlot(iSlot).splitStack(iAmount);
            if (inventory.getStackInSlot(iSlot).stackSize == 0) {
               inventory.setInventorySlotContents(iSlot, null);
            } else {
               inventory.onInventoryChanged();
            }

            return splitStack;
         }
      } else {
         return null;
      }
   }

   public static int getNumOccupiedStacks(IInventory inventory) {
      return getNumOccupiedStacksInRange(inventory, 0, inventory.getSizeInventory() - 1);
   }

   public static int getNumOccupiedStacksInRange(IInventory inventory, int iMinSlot, int iMaxSlot) {
      int iCount = 0;

      for (int iTempSlot = iMinSlot; iTempSlot <= iMaxSlot; iTempSlot++) {
         if (inventory.getStackInSlot(iTempSlot) != null) {
            iCount++;
         }
      }

      return iCount;
   }

   public static int getRandomOccupiedStackInRange(IInventory inventory, Random rand, int iMinSlot, int iMaxSlot) {
      int iNumStacks = getNumOccupiedStacksInRange(inventory, iMinSlot, iMaxSlot);
      if (iNumStacks > 0) {
         int iRandomStackNum = rand.nextInt(iNumStacks) + 1;
         int iStackCount = 0;

         for (int iTempSlot = iMinSlot; iTempSlot <= iMaxSlot; iTempSlot++) {
            if (inventory.getStackInSlot(iTempSlot) != null) {
               if (++iStackCount >= iRandomStackNum) {
                  return iTempSlot;
               }
            }
         }
      }

      return -1;
   }

   public static int getFirstOccupiedStack(IInventory inventory) {
      for (int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++) {
         if (inventory.getStackInSlot(iTempSlot) != null) {
            return iTempSlot;
         }
      }

      return -1;
   }

   public static int getFirstOccupiedStackOfItem(IInventory inventory, int iItemID) {
      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem().itemID == iItemID) {
            return i;
         }
      }

      return -1;
   }

   public static ArrayList<Integer> getAllOccupiedStacksOfItem(IInventory inventory, int itemID) {
      ArrayList<Integer> slots = new ArrayList<>();

      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).getItem().itemID == itemID) {
            slots.add(i);
         }
      }

      return slots;
   }

   public static int getFirstOccupiedStackNotOfItem(IInventory inventory, int iNotItemID) {
      for (int iTempSlot = 0; iTempSlot < inventory.getSizeInventory(); iTempSlot++) {
         if (inventory.getStackInSlot(iTempSlot) != null && inventory.getStackInSlot(iTempSlot).getItem().itemID != iNotItemID) {
            return iTempSlot;
         }
      }

      return -1;
   }

   public static int getFirstEmptyStack(IInventory inventory) {
      return getFirstEmptyStackInSlotRange(inventory, 0, inventory.getSizeInventory() - 1);
   }

   public static int getFirstEmptyStackInSlotRange(IInventory inventory, int iMinSlotIndex, int iMaxSlotIndex) {
      for (int iTempSlot = iMinSlotIndex; iTempSlot <= iMaxSlotIndex; iTempSlot++) {
         if (inventory.getStackInSlot(iTempSlot) == null) {
            return iTempSlot;
         }
      }

      return -1;
   }

   public static int countItemsInInventory(IInventory inventory, int iItemID, int iItemDamage) {
      return countItemsInInventory(inventory, iItemID, iItemDamage, false);
   }

   public static int countItemsInInventory(IInventory inventory, int iItemID, int iItemDamage, boolean bMetaDataExclusive) {
      int itemCount = 0;

      for (int i = 0; i < inventory.getSizeInventory(); i++) {
         ItemStack tempStack = inventory.getStackInSlot(i);
         if (tempStack != null
            && tempStack.getItem().itemID == iItemID
            && (
               iItemDamage == 32767
                  || !bMetaDataExclusive && tempStack.getItemDamage() == iItemDamage
                  || bMetaDataExclusive && tempStack.getItemDamage() != iItemDamage
            )) {
            itemCount += inventory.getStackInSlot(i).stackSize;
         }
      }

      return itemCount;
   }

   public static boolean consumeItemsInInventory(IInventory inventory, int iShiftedItemIndex, int iItemDamage, int iItemCount) {
      return consumeItemsInInventory(inventory, iShiftedItemIndex, iItemDamage, iItemCount, false);
   }

   public static boolean consumeItemsInInventory(IInventory inventory, int iShiftedItemIndex, int iItemDamage, int iItemCount, boolean bMetaDataExclusive) {
      for (int iSlot = 0; iSlot < inventory.getSizeInventory(); iSlot++) {
         ItemStack tempItemStack = inventory.getStackInSlot(iSlot);
         if (tempItemStack != null) {
            Item tempItem = tempItemStack.getItem();
            if (tempItem.itemID == iShiftedItemIndex
               && (
                  iItemDamage == 32767
                     || !bMetaDataExclusive && tempItemStack.getItemDamage() == iItemDamage
                     || bMetaDataExclusive && tempItemStack.getItemDamage() != iItemDamage
               )) {
               if (tempItemStack.stackSize >= iItemCount) {
                  decreaseStackSize(inventory, iSlot, iItemCount);
                  return true;
               }

               iItemCount -= tempItemStack.stackSize;
               inventory.setInventorySlotContents(iSlot, null);
            }
         }
      }

      return false;
   }

   public static boolean addSingleItemToInventory(IInventory inventory, int iItemShiftedIndex, int itemDamage) {
      ItemStack itemStack = new ItemStack(iItemShiftedIndex, 1, itemDamage);
      return addItemStackToInventory(inventory, itemStack);
   }

   public static boolean addItemStackToInventory(IInventory inventory, ItemStack stack) {
      return addItemStackToInventoryInSlotRange(inventory, stack, 0, inventory.getSizeInventory() - 1);
   }

   public static boolean addItemStackToInventoryInSlotRange(IInventory inventory, ItemStack itemstack, int iMinSlotIndex, int iMaxSlotIndex) {
      return !itemstack.isItemDamaged() && attemptToMergeWithExistingStacksInSlotRange(inventory, itemstack, iMinSlotIndex, iMaxSlotIndex)
         ? true
         : attemptToPlaceInEmptySlotInSlotRange(inventory, itemstack, iMinSlotIndex, iMaxSlotIndex);
   }

   public static int getMaxNumberOfItemsForTransferInRange(IInventory inventory, ItemStack itemStack, int countLimit, int minSlotIndex, int maxSlotIndex) {
      int totalSlotCount = maxSlotIndex - minSlotIndex + 1;
      int maximumEmptySlotCount = totalSlotCount - getNumOccupiedStacksInRange(inventory, minSlotIndex, maxSlotIndex);
      int maxItemsForEmptySlots = maximumEmptySlotCount * itemStack.getMaxStackSize();
      if (maxItemsForEmptySlots >= countLimit) {
         return countLimit;
      } else {
         int itemCountToMerge = countLimit - maxItemsForEmptySlots;
         ArrayList<Integer> slotsAvailableToMerge = getAllOccupiedStacksOfItem(inventory, itemStack.itemID);
         int availableItems = 0;

         for (int slotIndex : slotsAvailableToMerge) {
            ItemStack slotStack = inventory.getStackInSlot(slotIndex);
            availableItems += slotStack.getMaxStackSize() - slotStack.stackSize;
         }

         return availableItems >= itemCountToMerge ? countLimit : countLimit - (itemCountToMerge - availableItems);
      }
   }

   public static boolean addItemStackToChest(TileEntityChest chest, ItemStack itemstack) {
      World world = chest.worldObj;

      for (int iTempFacing = 2; iTempFacing <= 5; iTempFacing++) {
         BlockPos secondChestPos = new BlockPos(chest.xCoord, chest.yCoord, chest.zCoord);
         secondChestPos.addFacingAsOffset(iTempFacing);
         int iSecondBlockID = world.getBlockId(secondChestPos.x, secondChestPos.y, secondChestPos.z);
         if (iSecondBlockID == Block.chest.blockID || iSecondBlockID == BTWBlocks.chest.blockID) {
            TileEntityChest secondChest = (TileEntityChest)world.getBlockTileEntity(secondChestPos.x, secondChestPos.y, secondChestPos.z);
            if (secondChest != null) {
               if (chest.xCoord >= secondChest.xCoord && chest.zCoord >= secondChest.zCoord) {
                  return addItemStackToDoubleInventory(secondChest, chest, itemstack);
               }

               return addItemStackToDoubleInventory(chest, secondChest, itemstack);
            }
         }
      }

      return addItemStackToInventory(chest, itemstack);
   }

   private static boolean canStacksMerge(ItemStack sourceStack, ItemStack destStack, int iInventoryStackSizeLimit) {
      return destStack != null
         && destStack.itemID == sourceStack.itemID
         && destStack.isStackable()
         && destStack.stackSize < destStack.getMaxStackSize()
         && destStack.stackSize < iInventoryStackSizeLimit
         && (!destStack.getHasSubtypes() || destStack.getItemDamage() == sourceStack.getItemDamage())
         && ItemStack.areItemStackTagsEqual(destStack, sourceStack);
   }

   private static int findSlotToMergeItemStackInSlotRange(IInventory inventory, ItemStack itemStack, int iMinSlotIndex, int iMaxSlotIndex) {
      int iInventoryStackLimit = inventory.getInventoryStackLimit();

      for (int i = iMinSlotIndex; i <= iMaxSlotIndex; i++) {
         ItemStack tempStack = inventory.getStackInSlot(i);
         if (canStacksMerge(itemStack, tempStack, iInventoryStackLimit)) {
            return i;
         }
      }

      return -1;
   }

   private static boolean attemptToMergeWithExistingStacks(IInventory inventory, ItemStack stack) {
      return attemptToMergeWithExistingStacksInSlotRange(inventory, stack, 0, inventory.getSizeInventory() - 1);
   }

   private static boolean attemptToMergeWithExistingStacksInSlotRange(IInventory inventory, ItemStack stack, int iMinSlotIndex, int iMaxSlotIndex) {
      for (int iMergeSlot = findSlotToMergeItemStackInSlotRange(inventory, stack, iMinSlotIndex, iMaxSlotIndex);
         iMergeSlot >= 0;
         iMergeSlot = findSlotToMergeItemStackInSlotRange(inventory, stack, iMinSlotIndex, iMaxSlotIndex)
      ) {
         int iNumItemsToStore = stack.stackSize;
         ItemStack tempStack = inventory.getStackInSlot(iMergeSlot);
         if (iNumItemsToStore > tempStack.getMaxStackSize() - tempStack.stackSize) {
            iNumItemsToStore = tempStack.getMaxStackSize() - tempStack.stackSize;
         }

         if (iNumItemsToStore > inventory.getInventoryStackLimit() - tempStack.stackSize) {
            iNumItemsToStore = inventory.getInventoryStackLimit() - tempStack.stackSize;
         }

         if (iNumItemsToStore == 0) {
            return false;
         }

         stack.stackSize -= iNumItemsToStore;
         tempStack.stackSize += iNumItemsToStore;
         inventory.setInventorySlotContents(iMergeSlot, tempStack);
         if (stack.stackSize <= 0) {
            return true;
         }
      }

      return false;
   }

   private static boolean attemptToPlaceInEmptySlot(IInventory inventory, ItemStack stack) {
      return attemptToPlaceInEmptySlotInSlotRange(inventory, stack, 0, inventory.getSizeInventory() - 1);
   }

   private static boolean attemptToPlaceInEmptySlotInSlotRange(IInventory inventory, ItemStack stack, int iMinSlotIndex, int iMaxSlotIndex) {
      int iItemID = stack.itemID;
      int iItemDamage = stack.getItemDamage();

      for (int iEmptySlot = getFirstEmptyStackInSlotRange(inventory, iMinSlotIndex, iMaxSlotIndex);
         iEmptySlot >= 0;
         iEmptySlot = getFirstEmptyStackInSlotRange(inventory, iMinSlotIndex, iMaxSlotIndex)
      ) {
         int iNumItemsToStore = stack.stackSize;
         if (iNumItemsToStore > inventory.getInventoryStackLimit()) {
            iNumItemsToStore = inventory.getInventoryStackLimit();
         }

         ItemStack newStack = new ItemStack(iItemID, iNumItemsToStore, iItemDamage);
         copyEnchantments(newStack, stack);
         inventory.setInventorySlotContents(iEmptySlot, newStack);
         stack.stackSize -= iNumItemsToStore;
         if (stack.stackSize <= 0) {
            return true;
         }
      }

      return false;
   }

   private static boolean addItemStackToDoubleInventory(IInventory primaryInventory, IInventory secondaryInventory, ItemStack stack) {
      if (!stack.isItemDamaged()) {
         if (attemptToMergeWithExistingStacks(primaryInventory, stack)) {
            return true;
         }

         if (attemptToMergeWithExistingStacks(secondaryInventory, stack)) {
            return true;
         }
      }

      return attemptToPlaceInEmptySlot(primaryInventory, stack) ? true : attemptToPlaceInEmptySlot(secondaryInventory, stack);
   }
}
