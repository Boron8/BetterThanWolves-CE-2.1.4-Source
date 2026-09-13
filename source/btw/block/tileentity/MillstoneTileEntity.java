package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.MillstoneBlock;
import btw.crafting.manager.MillStoneCraftingManager;
import btw.inventory.util.InventoryUtils;
import btw.item.util.ItemUtils;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3;

public class MillstoneTileEntity extends TileEntity implements IInventory {
   public ItemStack stackMilling = null;
   public static final String UNLOCALIZED_NAME = "container.fcMillStone";
   private static final double MAX_PLAYER_INTERACTION_DIST_SQ = 64.0;
   private static final int TIME_TO_GRIND = 200;
   private boolean validateContentsOnUpdate;
   private boolean containsIngredientsToGrind;
   public int grindCounter;
   private static final int LEGACY_INVENTORY_SIZE = 3;
   private ItemStack[] legacyInventory = null;

   public MillstoneTileEntity() {
      this.grindCounter = 0;
      this.validateContentsOnUpdate = true;
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      if (tag.hasKey("Items")) {
         NBTTagList tagList = tag.getTagList("Items");
         this.legacyInventory = new ItemStack[3];

         for (int iTempTag = 0; iTempTag < tagList.tagCount(); iTempTag++) {
            NBTTagCompound tempSlotTag = (NBTTagCompound)tagList.tagAt(iTempTag);
            int iTempSlot = tempSlotTag.getByte("Slot") & 255;
            if (iTempSlot >= 0 && iTempSlot < this.legacyInventory.length) {
               this.legacyInventory[iTempSlot] = ItemStack.loadItemStackFromNBT(tempSlotTag);
            }
         }
      }

      NBTTagCompound millingTag = tag.getCompoundTag("stackMilling");
      if (millingTag != null) {
         this.stackMilling = ItemStack.loadItemStackFromNBT(millingTag);
      }

      if (tag.hasKey("grindCounter")) {
         this.grindCounter = tag.getInteger("grindCounter");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      if (!this.isLegacyInventoryEmpty()) {
         NBTTagList tagList = new NBTTagList();

         for (int iTempSlot = 0; iTempSlot < this.legacyInventory.length; iTempSlot++) {
            if (this.legacyInventory[iTempSlot] != null) {
               NBTTagCompound tempSlotTag = new NBTTagCompound();
               tempSlotTag.setByte("Slot", (byte)iTempSlot);
               this.legacyInventory[iTempSlot].writeToNBT(tempSlotTag);
               tagList.appendTag(tempSlotTag);
            }
         }

         tag.setTag("Items", tagList);
      }

      if (this.stackMilling != null) {
         NBTTagCompound millingTag = new NBTTagCompound();
         this.stackMilling.writeToNBT(millingTag);
         tag.setCompoundTag("stackMilling", millingTag);
      }

      tag.setInteger("grindCounter", this.grindCounter);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         int iBlockID = this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord);
         MillstoneBlock millStoneBlock = (MillstoneBlock)Block.blocksList[iBlockID];
         if (this.validateContentsOnUpdate) {
            this.validateContentsForGrinding(millStoneBlock);
         }

         if (this.containsIngredientsToGrind && millStoneBlock.getIsMechanicalOn(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
            this.grindCounter++;
            if (this.grindCounter >= 200) {
               this.grindContents(millStoneBlock);
               this.grindCounter = 0;
               this.validateContentsOnUpdate = true;
            }

            this.checkForNauseateNearbyPlayers(millStoneBlock);
         }
      }
   }

   @Override
   public int getSizeInventory() {
      return 1;
   }

   @Override
   public ItemStack getStackInSlot(int iSlot) {
      return iSlot == 0 ? this.stackMilling : null;
   }

   @Override
   public ItemStack decrStackSize(int iSlot, int iAmount) {
      return InventoryUtils.decreaseStackSize(this, iSlot, iAmount);
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int iSlot) {
      if (iSlot == 0 && this.stackMilling != null) {
         ItemStack itemstack = this.stackMilling;
         this.stackMilling = null;
         return itemstack;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int iSlot, ItemStack stack) {
      if (iSlot == 0) {
         this.stackMilling = stack;
         if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
         }

         this.onInventoryChanged();
      }
   }

   @Override
   public String getInvName() {
      return "container.fcMillStone";
   }

   @Override
   public int getInventoryStackLimit() {
      return 1;
   }

   @Override
   public void onInventoryChanged() {
      super.onInventoryChanged();
      if (this.worldObj != null && !this.worldObj.isRemote) {
         if (this.containsWholeCompanionCube()) {
            this.worldObj
               .playSoundEffect(
                  this.xCoord + 0.5F,
                  this.yCoord + 0.5F,
                  this.zCoord + 0.5F,
                  "mob.wolf.whine",
                  0.5F,
                  2.6F + (this.worldObj.rand.nextFloat() - this.worldObj.rand.nextFloat()) * 0.8F
               );
         }

         this.validateContentsOnUpdate = true;
      }
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer entityplayer) {
      return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this
         ? entityplayer.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0
         : false;
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
      return false;
   }

   public void ejectStackOnMilled(ItemStack stack) {
      int iFacing = 2 + this.worldObj.rand.nextInt(4);
      Vec3 ejectPos = Vec3.createVectorHelper(this.worldObj.rand.nextDouble() * 1.25 - 0.125, this.worldObj.rand.nextFloat() * 0.0625F + 0.4375F, -0.2F);
      ejectPos.rotateAsBlockPosAroundJToFacing(iFacing);
      EntityItem entity = (EntityItem)EntityList.createEntityOfType(
         EntityItem.class, this.worldObj, this.xCoord + ejectPos.xCoord, this.yCoord + ejectPos.yCoord, this.zCoord + ejectPos.zCoord, stack
      );
      Vec3 ejectVel = Vec3.createVectorHelper(
         this.worldObj.rand.nextGaussian() * 0.025, this.worldObj.rand.nextGaussian() * 0.025 + 0.1F, -0.06 + this.worldObj.rand.nextGaussian() * 0.04
      );
      ejectVel.rotateAsVectorAroundJToFacing(iFacing);
      entity.motionX = ejectVel.xCoord;
      entity.motionY = ejectVel.yCoord;
      entity.motionZ = ejectVel.zCoord;
      entity.delayBeforeCanPickup = 10;
      this.worldObj.spawnEntityInWorld(entity);
   }

   public int getGrindProgressScaled(int iScale) {
      return this.grindCounter * iScale / 200;
   }

   public boolean isGrinding() {
      return this.grindCounter > 0;
   }

   public boolean containsWholeCompanionCube() {
      return this.stackMilling != null && this.stackMilling.itemID == BTWBlocks.companionCube.blockID && this.stackMilling.getItemDamage() == 0;
   }

   private boolean grindContents(MillstoneBlock millStoneBlock) {
      if (this.stackMilling != null && MillStoneCraftingManager.getInstance().hasRecipeForSingleIngredient(this.stackMilling)) {
         List<ItemStack> outputList = MillStoneCraftingManager.getInstance().getCraftingResult(this.stackMilling);
         if (outputList != null) {
            if (this.stackMilling.itemID == BTWBlocks.companionCube.blockID && this.stackMilling.getItemDamage() == 0) {
               this.worldObj.playAuxSFX(2242, this.xCoord, this.yCoord, this.zCoord, 0);
            }

            for (int listIndex = 0; listIndex < outputList.size(); listIndex++) {
               ItemStack groundStack = outputList.get(listIndex).copy();
               if (groundStack != null) {
                  this.ejectStackOnMilled(groundStack);
               }
            }

            this.stackMilling = null;
            return true;
         }
      }

      return false;
   }

   private void validateContentsForGrinding(MillstoneBlock millStoneBlock) {
      int iOldGrindingType = millStoneBlock.getCurrentGrindingType(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
      int iNewGrindingType = 0;
      this.migrateLegacyInventory();
      if (this.stackMilling != null) {
         if (MillStoneCraftingManager.getInstance().hasRecipeForSingleIngredient(this.stackMilling)) {
            this.containsIngredientsToGrind = true;
            int iItemIndex = this.stackMilling.getItem().itemID;
            if (iItemIndex == BTWBlocks.companionCube.blockID && this.stackMilling.getItemDamage() == 0) {
               iNewGrindingType = 3;
            } else if (iItemIndex == Block.netherrack.blockID) {
               iNewGrindingType = 2;
            } else {
               iNewGrindingType = 1;
            }
         } else {
            iNewGrindingType = 4;
            this.grindCounter = 0;
            this.containsIngredientsToGrind = false;
         }
      } else {
         this.grindCounter = 0;
         this.containsIngredientsToGrind = false;
      }

      this.validateContentsOnUpdate = false;
      if (iOldGrindingType != iNewGrindingType) {
         millStoneBlock.setCurrentGrindingType(this.worldObj, this.xCoord, this.yCoord, this.zCoord, iNewGrindingType);
      }
   }

   private void checkForNauseateNearbyPlayers(MillstoneBlock block) {
      int iGrindType = block.getCurrentGrindingType(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
      if (iGrindType == 2 && this.worldObj.getTotalWorldTime() % 40L == 0L) {
         this.applyPotionEffectToPlayersInRange(Potion.confusion.getId(), 120, 0, 10.0);
      }
   }

   private void applyPotionEffectToPlayersInRange(int iEffectID, int iDuration, int iEffectLevel, double dRange) {
      for (EntityPlayer player : this.worldObj.playerEntities) {
         if (!player.isDead
            && !player.capabilities.isCreativeMode
            && Math.abs(this.xCoord - player.posX) <= dRange
            && Math.abs(this.yCoord - player.posY) <= dRange
            && Math.abs(this.zCoord - player.posZ) <= dRange) {
            player.d(new PotionEffect(iEffectID, iDuration, iEffectLevel, true));
         }
      }
   }

   private void migrateLegacyInventory() {
      if (this.stackMilling == null && this.legacyInventory != null) {
         for (int iTempSlot = 0; iTempSlot < this.legacyInventory.length; iTempSlot++) {
            if (this.legacyInventory[iTempSlot] != null) {
               ItemStack legacyStack = this.legacyInventory[iTempSlot];
               this.stackMilling = legacyStack.copy();
               this.stackMilling.stackSize = 1;
               this.legacyInventory[iTempSlot].stackSize--;
               if (legacyStack.stackSize <= 0) {
                  this.legacyInventory[iTempSlot] = null;
                  if (this.isLegacyInventoryEmpty()) {
                     this.legacyInventory = null;
                     break;
                  }
               }
            }
         }
      }
   }

   private boolean isLegacyInventoryEmpty() {
      if (this.legacyInventory != null) {
         for (int iTempSlot = 0; iTempSlot < this.legacyInventory.length; iTempSlot++) {
            if (this.legacyInventory[iTempSlot] != null && this.legacyInventory[iTempSlot].stackSize > 0) {
               return false;
            }
         }
      }

      return true;
   }

   public void ejectContents(int iFacing) {
      if (iFacing < 2) {
         iFacing = this.worldObj.rand.nextInt(4) + 2;
      }

      if (this.legacyInventory != null) {
         for (int iTempSlot = 0; iTempSlot < this.legacyInventory.length; iTempSlot++) {
            if (this.legacyInventory[iTempSlot] != null && this.legacyInventory[iTempSlot].stackSize > 0) {
               ItemUtils.ejectStackFromBlockTowardsFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.legacyInventory[iTempSlot], iFacing);
               this.legacyInventory[iTempSlot] = null;
            }
         }

         this.legacyInventory = null;
      }

      if (this.stackMilling != null) {
         ItemUtils.ejectStackFromBlockTowardsFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.stackMilling, iFacing);
         this.stackMilling = null;
         this.onInventoryChanged();
      }
   }

   public void attemptToAddSingleItemFromStack(ItemStack stack) {
      if (this.stackMilling == null) {
         this.stackMilling = stack.copy();
         this.stackMilling.stackSize = 1;
         stack.stackSize--;
         this.onInventoryChanged();
      }
   }
}
