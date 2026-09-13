package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.CookingVesselBlock;
import btw.crafting.manager.BulkCraftingManager;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.util.MiscUtils;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;

public abstract class CookingVesselTileEntity extends TileEntity implements IInventory, TileEntityDataPacketHandler {
   protected static final int INVENTORY_SIZE = 27;
   private static final int STACK_SIZE_LIMIT = 64;
   private static final int PRIMARY_FIRE_FACTOR = 5;
   private static final int SECONDARY_FIRE_FACTOR = 3;
   protected static final int STACK_SIZE_TO_DROP_WHEN_TIPPED = 8;
   private static final double MAX_PLAYER_INTERACTION_DIST = 64.0;
   private static final int STOKED_TICKS_TO_COOLDOWN = 20;
   private static final int TIME_TO_COOK = 4350;
   protected ItemStack[] contents = new ItemStack[27];
   protected int cookCounter = 0;
   protected int stokedCooldownCounter = 0;
   protected boolean containsValidIngredientsForState = false;
   private boolean forceValidateOnUpdate = true;
   protected int fireUnderType;
   public int scaledCookCounter = 0;
   public int forceFacing;
   public short storageSlotsOccupied;

   public CookingVesselTileEntity() {
      this.fireUnderType = 0;
      this.forceFacing = -1;
      this.storageSlotsOccupied = 0;
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      NBTTagList nbttaglist = nbttagcompound.getTagList("Items");
      this.contents = new ItemStack[this.getSizeInventory()];

      for (int i = 0; i < nbttaglist.tagCount(); i++) {
         NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
         int j = nbttagcompound1.getByte("Slot") & 255;
         if (j >= 0 && j < this.contents.length) {
            this.contents[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
         }
      }

      if (nbttagcompound.hasKey("m_iFireUnderType")) {
         this.fireUnderType = nbttagcompound.getInteger("m_iFireUnderType");
      } else {
         this.fireUnderType = -1;
      }

      if (nbttagcompound.hasKey("m_iFacing")) {
         this.forceFacing = nbttagcompound.getInteger("m_iFacing");
      }

      this.validateInventoryStateVariables();
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      NBTTagList nbttaglist = new NBTTagList();

      for (int i = 0; i < this.contents.length; i++) {
         if (this.contents[i] != null) {
            NBTTagCompound nbttagcompound1 = new NBTTagCompound();
            nbttagcompound1.setByte("Slot", (byte)i);
            this.contents[i].writeToNBT(nbttagcompound1);
            nbttaglist.appendTag(nbttagcompound1);
         }
      }

      nbttagcompound.setTag("Items", nbttaglist);
      nbttagcompound.setBoolean("m_bContainsValidIngrediantsForState", this.containsValidIngredientsForState);
      nbttagcompound.setInteger("m_iFireUnderType", this.fireUnderType);
   }

   @Override
   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         int iBlockID = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord);
         Block block = Block.blocksList[iBlockID];
         if (block != null && block instanceof CookingVesselBlock) {
            CookingVesselBlock cookingBlock = (CookingVesselBlock)block;
            if (this.forceFacing >= 0) {
               cookingBlock.setTiltFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.forceFacing);
               this.forceFacing = -1;
            }

            if (this.fireUnderType == -1) {
               this.validateFireUnderType();
            }

            if (!cookingBlock.getMechanicallyPoweredFlag(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
               if (this.fireUnderType > 0) {
                  if (this.forceValidateOnUpdate) {
                     this.validateContentsForState();
                     this.forceValidateOnUpdate = false;
                  }

                  if (this.fireUnderType == 2) {
                     if (this.stokedCooldownCounter <= 0) {
                        this.cookCounter = 0;
                     }

                     this.stokedCooldownCounter = 20;
                     this.performStokedFireUpdate(this.getCurrentFireFactor());
                  } else if (this.stokedCooldownCounter > 0) {
                     this.stokedCooldownCounter--;
                     if (this.stokedCooldownCounter <= 0) {
                        this.cookCounter = 0;
                     }
                  } else {
                     this.performNormalFireUpdate(this.getCurrentFireFactor());
                  }
               } else {
                  this.cookCounter = 0;
               }
            } else {
               this.cookCounter = 0;
               int iTiltFacing = cookingBlock.getTiltFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
               this.attemptToEjectStackFromInv(iTiltFacing);
            }

            this.scaledCookCounter = this.cookCounter * 1000 / 4350;
         }
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setShort("s", this.storageSlotsOccupied);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbttagcompound);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound nbttagcompound) {
      this.storageSlotsOccupied = nbttagcompound.getShort("s");
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   @Override
   public int getSizeInventory() {
      return 27;
   }

   @Override
   public ItemStack getStackInSlot(int iSlot) {
      return this.contents[iSlot];
   }

   @Override
   public ItemStack decrStackSize(int iSlot, int iAmount) {
      return InventoryUtils.decreaseStackSize(this, iSlot, iAmount);
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.contents[par1] != null) {
         ItemStack itemstack = this.contents[par1];
         this.contents[par1] = null;
         return itemstack;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int iSlot, ItemStack itemstack) {
      this.contents[iSlot] = itemstack;
      if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
         itemstack.stackSize = this.getInventoryStackLimit();
      }

      this.onInventoryChanged();
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
      this.forceValidateOnUpdate = true;
      if (this.worldObj != null && this.validateInventoryStateVariables()) {
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
      if (this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) != this) {
         return false;
      } else {
         int iBlockID = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord);
         Block block = Block.blocksList[iBlockID];
         if (!(block instanceof CookingVesselBlock)) {
            return false;
         } else {
            return ((CookingVesselBlock)block).isOpenSideBlocked(this.worldObj, this.xCoord, this.yCoord, this.zCoord)
               ? false
               : entityPlayer.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
         }
      }
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   public abstract void validateContentsForState();

   protected abstract BulkCraftingManager getCraftingManager(int var1);

   public int getCurrentFireFactor() {
      int iFireFactor = 0;
      if (this.fireUnderType > 0) {
         iFireFactor = 5;
         if (this.fireUnderType == 1) {
            int tempY = this.yCoord - 1;

            for (int tempX = this.xCoord - 1; tempX <= this.xCoord + 1; tempX++) {
               for (int tempZ = this.zCoord - 1; tempZ <= this.zCoord + 1; tempZ++) {
                  if (tempX != this.xCoord || tempZ != this.zCoord) {
                     int iTempBlockID = this.worldObj.getBlockId(tempX, tempY, tempZ);
                     if (iTempBlockID == Block.fire.blockID
                        || iTempBlockID == BTWBlocks.mediumCampfire.blockID
                        || iTempBlockID == BTWBlocks.largeCampfire.blockID) {
                        iFireFactor += 3;
                     }
                  }
               }
            }
         } else {
            int tempY = this.yCoord - 1;

            for (int tempX = this.xCoord - 1; tempX <= this.xCoord + 1; tempX++) {
               for (int tempZx = this.zCoord - 1; tempZx <= this.zCoord + 1; tempZx++) {
                  if ((tempX != this.xCoord || tempZx != this.zCoord) && this.worldObj.getBlockId(tempX, tempY, tempZx) == BTWBlocks.stokedFire.blockID) {
                     iFireFactor += 3;
                  }
               }
            }
         }
      }

      return iFireFactor;
   }

   public void validateFireUnderType() {
      if (this.worldObj != null) {
         int iNewType = 0;
         int iBlockUnderID = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);
         if (iBlockUnderID == Block.fire.blockID || iBlockUnderID == BTWBlocks.mediumCampfire.blockID || iBlockUnderID == BTWBlocks.largeCampfire.blockID) {
            iNewType = 1;
         } else if (iBlockUnderID == BTWBlocks.stokedFire.blockID) {
            iNewType = 2;
         }

         if (iNewType != this.fireUnderType) {
            this.fireUnderType = iNewType;
            this.validateContentsForState();
         }
      }
   }

   private void performNormalFireUpdate(int iFireFactor) {
      if (this.containsValidIngredientsForState) {
         this.cookCounter += iFireFactor;
         if (this.cookCounter >= 4350) {
            this.attemptToCookNormal();
            this.cookCounter = 0;
         }
      } else {
         this.cookCounter = 0;
      }
   }

   private void performStokedFireUpdate(int iFireFactor) {
      if (this.containsValidIngredientsForState) {
         this.cookCounter += iFireFactor;
         if (this.cookCounter >= 4350) {
            if (this.doesContainExplosives()) {
               this.blowUp();
            } else {
               this.attemptToCookStoked();
            }

            this.cookCounter = 0;
         }
      } else {
         this.cookCounter = 0;
      }
   }

   protected boolean attemptToCookNormal() {
      return this.attemptToCookWithManager(this.getCraftingManager(1));
   }

   protected boolean attemptToCookStoked() {
      return this.attemptToCookWithManager(this.getCraftingManager(2));
   }

   private boolean attemptToCookWithManager(BulkCraftingManager manager) {
      if (manager != null && manager.getCraftingResult(this) != null) {
         List<ItemStack> outputList = manager.consumeIngredientsAndReturnResult(this);

         assert outputList != null && outputList.size() > 0;

         for (int listIndex = 0; listIndex < outputList.size(); listIndex++) {
            ItemStack cookedStack = outputList.get(listIndex).copy();
            if (cookedStack != null && !InventoryUtils.addItemStackToInventory(this, cookedStack)) {
               ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord + 1, this.zCoord, cookedStack);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public int getCookProgressScaled(int iScale) {
      return this.scaledCookCounter * iScale / 1000;
   }

   public boolean isCooking() {
      return this.scaledCookCounter > 0;
   }

   protected boolean doesContainExplosives() {
      return InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.hellfireDust.itemID) >= 0
         || InventoryUtils.getFirstOccupiedStackOfItem(this, Block.tnt.blockID) >= 0
         || InventoryUtils.getFirstOccupiedStackOfItem(this, Item.gunpowder.itemID) >= 0
         || InventoryUtils.getFirstOccupiedStackOfItem(this, BTWItems.blastingOil.itemID) >= 0;
   }

   private void blowUp() {
      int iHellfireCount = InventoryUtils.countItemsInInventory(this, BTWItems.hellfireDust.itemID, -1);
      float fExplosionSize = iHellfireCount * 10.0F / 64.0F;
      fExplosionSize += InventoryUtils.countItemsInInventory(this, Item.gunpowder.itemID, -1) * 10.0F / 64.0F;
      fExplosionSize += InventoryUtils.countItemsInInventory(this, BTWItems.blastingOil.itemID, -1) * 10.0F / 64.0F;
      int iTNTCount = InventoryUtils.countItemsInInventory(this, Block.tnt.blockID, -1);
      if (iTNTCount > 0) {
         if (fExplosionSize < 4.0F) {
            fExplosionSize = 4.0F;
         }

         fExplosionSize += InventoryUtils.countItemsInInventory(this, Block.tnt.blockID, -1);
      }

      if (fExplosionSize < 2.0F) {
         fExplosionSize = 2.0F;
      } else if (fExplosionSize > 10.0F) {
         fExplosionSize = 10.0F;
      }

      InventoryUtils.clearInventoryContents(this);
      this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord, this.zCoord, 0);
      this.worldObj.createExplosion(null, this.xCoord, this.yCoord, this.zCoord, fExplosionSize, true);
   }

   private void attemptToEjectStackFromInv(int iTiltFacing) {
      int iStackIndex = InventoryUtils.getFirstOccupiedStackNotOfItem(this, Item.brick.itemID);
      if (iStackIndex >= 0 && iStackIndex < this.getSizeInventory()) {
         ItemStack invStack = this.getStackInSlot(iStackIndex);
         int iEjectStackSize;
         if (8 > invStack.stackSize) {
            iEjectStackSize = invStack.stackSize;
         } else {
            iEjectStackSize = 8;
         }

         ItemStack ejectStack = new ItemStack(invStack.itemID, iEjectStackSize, invStack.getItemDamage());
         InventoryUtils.copyEnchantments(ejectStack, invStack);
         BlockPos targetPos = new BlockPos(this.xCoord, this.yCoord, this.zCoord);
         targetPos.addFacingAsOffset(iTiltFacing);
         boolean bEjectIntoWorld = false;
         if (this.worldObj.isAirBlock(targetPos.x, targetPos.y, targetPos.z)) {
            bEjectIntoWorld = true;
         } else if (WorldUtils.isReplaceableBlock(this.worldObj, targetPos.x, targetPos.y, targetPos.z)) {
            bEjectIntoWorld = true;
         } else {
            int iTargetBlockID = this.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
            Block targetBlock = Block.blocksList[iTargetBlockID];
            if (!targetBlock.blockMaterial.isSolid()) {
               bEjectIntoWorld = true;
            }
         }

         if (bEjectIntoWorld) {
            this.ejectStack(ejectStack, iTiltFacing);
            this.decrStackSize(iStackIndex, iEjectStackSize);
         }
      }
   }

   private void ejectStack(ItemStack stack, int iFacing) {
      Vec3 itemPos = MiscUtils.convertBlockFacingToVector(iFacing);
      itemPos.xCoord *= 0.5;
      itemPos.yCoord *= 0.5;
      itemPos.zCoord *= 0.5;
      itemPos.xCoord = itemPos.xCoord + (this.xCoord + 0.5F);
      itemPos.yCoord = itemPos.yCoord + (this.yCoord + 0.25F);
      itemPos.zCoord = itemPos.zCoord + (this.zCoord + 0.5F);
      EntityItem entityItem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, this.worldObj, itemPos.xCoord, itemPos.yCoord, itemPos.zCoord, stack);
      Vec3 itemVel = MiscUtils.convertBlockFacingToVector(iFacing);
      itemVel.xCoord *= 0.1F;
      itemVel.yCoord *= 0.1F;
      itemVel.zCoord *= 0.1F;
      entityItem.motionX = itemVel.xCoord;
      entityItem.motionY = itemVel.yCoord;
      entityItem.motionZ = itemVel.zCoord;
      entityItem.delayBeforeCanPickup = 10;
      this.worldObj.spawnEntityInWorld(entityItem);
   }

   private boolean validateInventoryStateVariables() {
      boolean bStateChanged = false;
      short currentSlotsOccupied = (short)InventoryUtils.getNumOccupiedStacks(this);
      if (currentSlotsOccupied != this.storageSlotsOccupied) {
         this.storageSlotsOccupied = currentSlotsOccupied;
         bStateChanged = true;
      }

      return bStateChanged;
   }
}
