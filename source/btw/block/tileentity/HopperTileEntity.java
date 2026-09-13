package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.ArcaneVesselBlock;
import btw.block.blocks.HopperBlock;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import btw.world.util.WorldUtils;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.IInventory;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityChest;
import net.minecraft.src.World;

public class HopperTileEntity extends TileEntity implements IInventory, TileEntityDataPacketHandler {
   private static final int INVENTORY_SIZE = 19;
   private static final int STACK_SIZE_LIMIT = 64;
   private static final double MAX_PLAYER_INTERACTION_DIST = 64.0;
   private static final int STACK_SIZE_TO_EJECT = 8;
   private static final int TIME_TO_EJECT = 3;
   private static final int OVERLOAD_SOUL_COUNT = 8;
   private static final int CHANCE_OF_CHECKING_FOR_POSSESSION = 8;
   private static final int POSSESSION_COUNT_ON_OVERLOAD = 4;
   private static final int XP_INVENTORY_SPACE = 100;
   private static final int XP_EJECT_UNIT_SIZE = 20;
   private static final int XP_DELAY_BETWEEN_DROPS = 10;
   private ItemStack[] contents = new ItemStack[19];
   private int ejectCounter = 0;
   private int containedSoulCount = 0;
   private int containedXPCount = 0;
   private int hopperXPDropDelayCount = 10;
   public boolean outputBlocked = false;
   public int mechanicalPowerIndicator = 0;
   protected int filterItemID = 0;
   public short storageSlotsOccupied = 0;

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

      if (nbttagcompound.hasKey("grindCounter")) {
         this.ejectCounter = nbttagcompound.getInteger("grindCounter");
      }

      if (nbttagcompound.hasKey("iHoppeContainedSoulCount")) {
         this.containedSoulCount = nbttagcompound.getInteger("iHoppeContainedSoulCount");
      }

      if (nbttagcompound.hasKey("iHopperContainedXPCount")) {
         this.containedXPCount = nbttagcompound.getInteger("iHopperContainedXPCount");
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
      nbttagcompound.setInteger("grindCounter", this.ejectCounter);
      nbttagcompound.setInteger("iHoppeContainedSoulCount", this.containedSoulCount);
      nbttagcompound.setInteger("iHopperContainedXPCount", this.containedXPCount);
   }

   @Override
   public void updateEntity() {
      if (!this.worldObj.isRemote) {
         boolean bHopperOn = ((HopperBlock)BTWBlocks.hopper).isBlockOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
         if (bHopperOn) {
            this.mechanicalPowerIndicator = 1;
            this.attemptToEjectXPFromInv();
            if (!this.outputBlocked) {
               this.ejectCounter++;
               if (this.ejectCounter >= 3) {
                  this.attemptToEjectStackFromInv();
                  this.ejectCounter = 0;
               }
            } else {
               this.ejectCounter = 0;
            }
         } else {
            this.mechanicalPowerIndicator = 0;
            this.ejectCounter = 0;
            this.hopperXPDropDelayCount = 0;
         }

         if (this.containedSoulCount > 0 && this.filterItemID == Block.slowSand.blockID) {
            int iBlockBelowID = this.worldObj.getBlockId(this.xCoord, this.yCoord - 1, this.zCoord);
            int iBlockBelowMetaData = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord - 1, this.zCoord);
            if (bHopperOn && (iBlockBelowID != BTWBlocks.aestheticNonOpaque.blockID || iBlockBelowMetaData != 0)) {
               for (int i = 0; i < this.containedSoulCount; i++) {
                  if (this.worldObj.rand.nextInt(8) == 0) {
                     EntityCreature.attemptToPossessCreaturesAroundBlock(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 1, 16);
                  }
               }

               this.containedSoulCount = 0;
            }

            if (this.containedSoulCount >= 8) {
               if (bHopperOn && iBlockBelowID == BTWBlocks.aestheticNonOpaque.blockID && iBlockBelowMetaData == 0) {
                  this.worldObj.setBlockWithNotify(this.xCoord, this.yCoord - 1, this.zCoord, 0);
                  ItemStack newItemStack = new ItemStack(BTWItems.soulUrn.itemID, 1, 0);
                  ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord - 1, this.zCoord, newItemStack);
                  this.containedSoulCount = 0;
               } else {
                  this.hopperSoulOverload();
               }
            }
         }
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setInteger("f", this.filterItemID);
      nbttagcompound.setShort("s", this.storageSlotsOccupied);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbttagcompound);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound nbttagcompound) {
      this.filterItemID = nbttagcompound.getInteger("f");
      this.storageSlotsOccupied = nbttagcompound.getShort("s");
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   @Override
   public int getSizeInventory() {
      return 19;
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
   public String getInvName() {
      return "Hopper";
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
      if (this.worldObj != null) {
         this.outputBlocked = false;
         if (this.validateInventoryStateVariables()) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
         }

         int iOccupiedStacks = InventoryUtils.getNumOccupiedStacksInRange(this, 0, 17);
         ((HopperBlock)BTWBlocks.hopper).setHasFilter(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.filterItemID > 0);
      }
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

   public Item getCurrentFilterItem() {
      return Item.itemsList[this.filterItemID];
   }

   private boolean validateInventoryStateVariables() {
      boolean bStateChanged = false;
      int currentFilterID = this.getFilterIDBasedOnInventory();
      if (currentFilterID != this.filterItemID) {
         this.filterItemID = currentFilterID;
         bStateChanged = true;
      }

      short numSlotsOccupied = (short)InventoryUtils.getNumOccupiedStacksInRange(this, 0, 17);
      if (numSlotsOccupied != this.storageSlotsOccupied) {
         this.storageSlotsOccupied = numSlotsOccupied;
         bStateChanged = true;
      }

      return bStateChanged;
   }

   public int getFilterIDBasedOnInventory() {
      ItemStack filterStack = this.getStackInSlot(18);
      return filterStack != null && filterStack.stackSize > 0 ? filterStack.itemID : 0;
   }

   public Item getFilterItem() {
      ItemStack filterStack = this.getStackInSlot(18);
      return filterStack != null ? filterStack.getItem() : null;
   }

   public boolean canCurrentFilterProcessItem(ItemStack itemStack) {
      Item filterItem = this.getFilterItem();
      return filterItem != null ? filterItem.canItemPassIfFilter(itemStack) : true;
   }

   public boolean isEjecting() {
      return ((HopperBlock)BTWBlocks.hopper).isBlockOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }

   private void attemptToEjectStackFromInv() {
      int iStackIndex = InventoryUtils.getRandomOccupiedStackInRange(this, this.worldObj.rand, 0, 17);
      if (iStackIndex >= 0 && iStackIndex <= 17) {
         ItemStack invStack = this.getStackInSlot(iStackIndex);
         int iEjectStackSize;
         if (8 > invStack.stackSize) {
            iEjectStackSize = invStack.stackSize;
         } else {
            iEjectStackSize = 8;
         }

         ItemStack ejectStack = new ItemStack(invStack.itemID, iEjectStackSize, invStack.getItemDamage());
         InventoryUtils.copyEnchantments(ejectStack, invStack);
         int iTargetI = this.xCoord;
         int iTargetJ = this.yCoord - 1;
         int iTargetK = this.zCoord;
         boolean bEjectIntoWorld = false;
         if (this.worldObj.isAirBlock(iTargetI, iTargetJ, iTargetK)) {
            bEjectIntoWorld = true;
         } else if (WorldUtils.isReplaceableBlock(this.worldObj, iTargetI, iTargetJ, iTargetK)) {
            bEjectIntoWorld = true;
         } else {
            int iTargetBlockID = this.worldObj.getBlockId(iTargetI, iTargetJ, iTargetK);
            Block targetBlock = Block.blocksList[iTargetBlockID];
            if (targetBlock == null || !targetBlock.doesBlockHopperEject(this.worldObj, iTargetI, iTargetJ, iTargetK)) {
               bEjectIntoWorld = true;
            } else if (targetBlock.doesBlockHopperInsert(this.worldObj, iTargetI, iTargetJ, iTargetK)) {
               this.outputBlocked = true;
            } else {
               TileEntity targetTileEntity = this.worldObj.getBlockTileEntity(iTargetI, iTargetJ, iTargetK);
               int iNumItemsStored = 0;
               if (targetTileEntity != null && targetTileEntity instanceof IInventory) {
                  int iMinSlotToAddTo = 0;
                  int iMaxSlotToAddTo = ((IInventory)targetTileEntity).getSizeInventory() - 1;
                  boolean canProcessStack = true;
                  if (iTargetBlockID == Block.furnaceIdle.blockID || iTargetBlockID == Block.furnaceBurning.blockID) {
                     iMaxSlotToAddTo = 0;
                  } else if (iTargetBlockID == BTWBlocks.hopper.blockID) {
                     iMaxSlotToAddTo = 17;
                     int iTargetFilterID = ((HopperTileEntity)targetTileEntity).filterItemID;
                     if (iTargetFilterID > 0) {
                        canProcessStack = false;
                     }
                  }

                  if (!canProcessStack) {
                     this.outputBlocked = true;
                  } else {
                     boolean bFullStackDeposited;
                     if (iTargetBlockID != Block.chest.blockID && iTargetBlockID != BTWBlocks.chest.blockID) {
                        bFullStackDeposited = InventoryUtils.addItemStackToInventoryInSlotRange(
                           (IInventory)targetTileEntity, ejectStack, iMinSlotToAddTo, iMaxSlotToAddTo
                        );
                     } else {
                        bFullStackDeposited = InventoryUtils.addItemStackToChest((TileEntityChest)targetTileEntity, ejectStack);
                     }

                     if (!bFullStackDeposited) {
                        iNumItemsStored = iEjectStackSize - ejectStack.stackSize;
                     } else {
                        iNumItemsStored = iEjectStackSize;
                     }

                     if (iNumItemsStored > 0) {
                        this.decrStackSize(iStackIndex, iNumItemsStored);
                        this.worldObj.playAuxSFX(2231, this.xCoord, this.yCoord, this.zCoord, 0);
                     }
                  }
               } else {
                  this.outputBlocked = true;
               }
            }
         }

         if (bEjectIntoWorld) {
            List list = this.worldObj
               .getEntitiesWithinAABB(
                  EntityMinecart.class,
                  AxisAlignedBB.getAABBPool()
                     .getAABB(this.xCoord + 0.4F, this.yCoord - 0.5F, this.zCoord + 0.4F, this.xCoord + 0.6F, this.yCoord, this.zCoord + 0.6F)
               );
            if (list != null && list.size() > 0) {
               for (int listIndex = 0; listIndex < list.size(); listIndex++) {
                  EntityMinecart minecartEntity = (EntityMinecart)list.get(listIndex);
                  if (minecartEntity.getMinecartType() == 1
                     && minecartEntity.boundingBox
                        .intersectsWith(
                           AxisAlignedBB.getAABBPool()
                              .getAABB(this.xCoord, this.yCoord - 0.5F, this.zCoord, this.xCoord + 0.25F, this.yCoord, this.zCoord + 1.0F)
                        )
                     && minecartEntity.boundingBox
                        .intersectsWith(
                           AxisAlignedBB.getAABBPool()
                              .getAABB(this.xCoord + 0.75F, this.yCoord - 0.5F, this.zCoord, this.xCoord + 1.0F, this.yCoord, this.zCoord + 1.0F)
                        )
                     && minecartEntity.boundingBox
                        .intersectsWith(
                           AxisAlignedBB.getAABBPool()
                              .getAABB(this.xCoord, this.yCoord - 0.5F, this.zCoord, this.xCoord + 1.0F, this.yCoord, this.zCoord + 0.25F)
                        )
                     && minecartEntity.boundingBox
                        .intersectsWith(
                           AxisAlignedBB.getAABBPool()
                              .getAABB(this.xCoord, this.yCoord - 0.5F, this.zCoord + 0.75F, this.xCoord + 1.0F, this.yCoord, this.zCoord + 1.0F)
                        )) {
                     int iNumItemsStored = 0;
                     if (InventoryUtils.addItemStackToInventory((IInventory)minecartEntity, ejectStack)) {
                        iNumItemsStored = iEjectStackSize;
                     } else {
                        iNumItemsStored = iEjectStackSize - ejectStack.stackSize;
                     }

                     if (iNumItemsStored > 0) {
                        this.decrStackSize(iStackIndex, iNumItemsStored);
                        this.worldObj.playAuxSFX(2231, this.xCoord, this.yCoord, this.zCoord, 0);
                     }

                     bEjectIntoWorld = false;
                     break;
                  }
               }
            }
         }

         if (bEjectIntoWorld) {
            this.ejectStack(ejectStack);
            this.decrStackSize(iStackIndex, iEjectStackSize);
         }
      }
   }

   private void ejectStack(ItemStack stack) {
      float xOffset = this.worldObj.rand.nextFloat() * 0.1F + 0.45F;
      float yOffset = -0.35F;
      float zOffset = this.worldObj.rand.nextFloat() * 0.1F + 0.45F;
      EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(
         EntityItem.class, this.worldObj, this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset, stack
      );
      entityitem.motionX = 0.0;
      entityitem.motionY = -0.01F;
      entityitem.motionZ = 0.0;
      entityitem.delayBeforeCanPickup = 10;
      this.worldObj.spawnEntityInWorld(entityitem);
   }

   public void attemptToEjectXPFromInv() {
      boolean bShouldResetEjectCount = true;
      if (this.containedXPCount >= 20) {
         int iTargetI = this.xCoord;
         int iTargetJ = this.yCoord - 1;
         int iTargetK = this.zCoord;
         boolean bCanEjectIntoWorld = false;
         if (this.worldObj.isAirBlock(iTargetI, iTargetJ, iTargetK)) {
            bCanEjectIntoWorld = true;
         } else {
            int iTargetBlockID = this.worldObj.getBlockId(iTargetI, iTargetJ, iTargetK);
            if (iTargetBlockID == BTWBlocks.hopper.blockID) {
               bShouldResetEjectCount = this.attemptToEjectXPIntoHopper(iTargetI, iTargetJ, iTargetK);
            } else if (iTargetBlockID == BTWBlocks.arcaneVessel.blockID) {
               bShouldResetEjectCount = this.attemptToEjectXPIntoArcaneVessel(iTargetI, iTargetJ, iTargetK);
            } else if (WorldUtils.isReplaceableBlock(this.worldObj, iTargetI, iTargetJ, iTargetK)) {
               bCanEjectIntoWorld = true;
            } else {
               Block targetBlock = Block.blocksList[iTargetBlockID];
               if (!targetBlock.blockMaterial.isSolid()) {
                  bCanEjectIntoWorld = true;
               }
            }
         }

         if (bCanEjectIntoWorld) {
            if (this.hopperXPDropDelayCount <= 0) {
               this.ejectXPOrb(20);
               this.containedXPCount -= 20;
            } else {
               bShouldResetEjectCount = false;
            }
         }
      }

      if (bShouldResetEjectCount) {
         this.resetXPEjectCount();
      } else {
         this.hopperXPDropDelayCount--;
      }
   }

   private boolean attemptToEjectXPIntoHopper(int iTargetI, int iTargetJ, int iTargetK) {
      HopperBlock hopperBlock = (HopperBlock)BTWBlocks.hopper;
      HopperTileEntity targetTileEntity = (HopperTileEntity)this.worldObj.getBlockTileEntity(iTargetI, iTargetJ, iTargetK);
      if (targetTileEntity != null && this.filterItemID == Block.slowSand.blockID) {
         int iTargetSpaceRemaining = 100 - targetTileEntity.containedXPCount;
         if (iTargetSpaceRemaining > 0) {
            if (this.hopperXPDropDelayCount > 0) {
               return false;
            }

            int iXPEjected = 20;
            if (iTargetSpaceRemaining < iXPEjected) {
               iXPEjected = iTargetSpaceRemaining;
            }

            targetTileEntity.containedXPCount += iXPEjected;
            this.containedXPCount -= iXPEjected;
            this.worldObj.playAuxSFX(2232, this.xCoord, this.yCoord, this.zCoord, 0);
         }
      }

      return true;
   }

   private boolean attemptToEjectXPIntoArcaneVessel(int iTargetI, int iTargetJ, int iTargetK) {
      ArcaneVesselBlock vesselBlock = (ArcaneVesselBlock)BTWBlocks.arcaneVessel;
      ArcaneVesselTileEntity targetTileEntity = (ArcaneVesselTileEntity)this.worldObj.getBlockTileEntity(iTargetI, iTargetJ, iTargetK);
      if (targetTileEntity != null && !vesselBlock.getMechanicallyPoweredFlag(this.worldObj, iTargetI, iTargetJ, iTargetK)) {
         int iTargetSpaceRemaining = 1000 - targetTileEntity.getContainedTotalExperience();
         if (iTargetSpaceRemaining > 0) {
            if (this.hopperXPDropDelayCount > 0) {
               return false;
            }

            int iXPEjected = 20;
            if (iTargetSpaceRemaining < iXPEjected) {
               iXPEjected = iTargetSpaceRemaining;
            }

            targetTileEntity.setContainedRegularExperience(targetTileEntity.getContainedRegularExperience() + iXPEjected);
            this.containedXPCount -= iXPEjected;
            this.worldObj.playAuxSFX(2232, this.xCoord, this.yCoord, this.zCoord, 0);
         }
      }

      return true;
   }

   private void resetXPEjectCount() {
      this.hopperXPDropDelayCount = 10 + this.worldObj.rand.nextInt(3);
   }

   private void ejectXPOrb(int iXPValue) {
      double xOffset = this.worldObj.rand.nextDouble() * 0.1 + 0.45;
      double yOffset = -0.2;
      double zOffset = this.worldObj.rand.nextDouble() * 0.1 + 0.45;
      EntityXPOrb xpOrb = (EntityXPOrb)EntityList.createEntityOfType(
         EntityXPOrb.class, this.worldObj, this.xCoord + xOffset, this.yCoord + yOffset, this.zCoord + zOffset, iXPValue
      );
      xpOrb.motionX = 0.0;
      xpOrb.motionY = 0.0;
      xpOrb.motionZ = 0.0;
      this.worldObj.spawnEntityInWorld(xpOrb);
      this.worldObj.playAuxSFX(2230, this.xCoord, this.yCoord, this.zCoord, 0);
   }

   public void resetContainedSoulCount() {
      this.containedSoulCount = 0;
   }

   public void incrementContainedSoulCount(int iNumSouls) {
      this.containedSoulCount += iNumSouls;
   }

   private void hopperSoulOverload() {
      this.worldObj.playAuxSFX(2225, this.xCoord, this.yCoord, this.zCoord, 0);
      ((HopperBlock)BTWBlocks.hopper).breakHopper(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
      EntityCreature.attemptToPossessCreaturesAroundBlock(this.worldObj, this.xCoord, this.yCoord, this.zCoord, 4, 16);
   }

   public boolean attemptToSwallowXPOrb(World world, int i, int j, int k, EntityXPOrb entityXPOrb) {
      int iRemainingSpace = 100 - this.containedXPCount;
      if (iRemainingSpace > 0) {
         if (entityXPOrb.xpValue <= iRemainingSpace) {
            this.containedXPCount = this.containedXPCount + entityXPOrb.xpValue;
            entityXPOrb.w();
            return true;
         }

         entityXPOrb.xpValue -= iRemainingSpace;
         this.containedXPCount = 100;
      }

      return false;
   }
}
