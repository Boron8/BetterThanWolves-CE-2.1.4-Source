package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.CampfireBlock;
import btw.block.blocks.FireBlock;
import btw.crafting.manager.CampfireCraftingManager;
import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;

public class CampfireTileEntity extends TileEntity implements TileEntityDataPacketHandler {
   private static final int CAMPFIRE_BURN_TIME_MULTIPLIER = 8;
   private static final int TIME_TO_COOK = 4800;
   private static final int MAX_BURN_TIME = 6000;
   private static final int INITIAL_BURN_TIME = 3200;
   private static final int WARMUP_TIME = 200;
   private static final int REVERT_TO_SMALL_TIME = 400;
   private static final int BLAZE_TIME = 4800;
   private static final int SMOULDER_TIME = 6000;
   private static final int TIME_TO_BURN_FOOD = 2400;
   private static final float CHANCE_OF_FIRE_SPREAD = 0.05F;
   private static final float CHANCE_OF_GOING_OUT_FROM_RAIN = 0.01F;
   private ItemStack spitStack = null;
   private ItemStack cookStack = null;
   private int burnTimeCountdown = 0;
   private int burnTimeSinceLit = 0;
   private int cookCounter = 0;
   private int smoulderCounter = 0;
   private int cookBurningCounter = 0;

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      NBTTagCompound spitTag = tag.getCompoundTag("fcSpitStack");
      if (spitTag != null) {
         this.spitStack = ItemStack.loadItemStackFromNBT(spitTag);
      }

      NBTTagCompound cookTag = tag.getCompoundTag("fcCookStack");
      if (cookTag != null) {
         this.cookStack = ItemStack.loadItemStackFromNBT(cookTag);
      }

      if (tag.hasKey("fcBurnCounter")) {
         this.burnTimeCountdown = tag.getInteger("fcBurnCounter");
      }

      if (tag.hasKey("fcBurnTime")) {
         this.burnTimeSinceLit = tag.getInteger("fcBurnTime");
      }

      if (tag.hasKey("fcCookCounter")) {
         this.cookCounter = tag.getInteger("fcCookCounter");
      }

      if (tag.hasKey("fcSmoulderCounter")) {
         this.smoulderCounter = tag.getInteger("fcSmoulderCounter");
      }

      if (tag.hasKey("fcCookBurning")) {
         this.cookBurningCounter = tag.getInteger("fcCookBurning");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      if (this.spitStack != null) {
         NBTTagCompound spitTag = new NBTTagCompound();
         this.spitStack.writeToNBT(spitTag);
         tag.setCompoundTag("fcSpitStack", spitTag);
      }

      if (this.cookStack != null) {
         NBTTagCompound cookTag = new NBTTagCompound();
         this.cookStack.writeToNBT(cookTag);
         tag.setCompoundTag("fcCookStack", cookTag);
      }

      tag.setInteger("fcBurnCounter", this.burnTimeCountdown);
      tag.setInteger("fcBurnTime", this.burnTimeSinceLit);
      tag.setInteger("fcCookCounter", this.cookCounter);
      tag.setInteger("fcSmoulderCounter", this.smoulderCounter);
      tag.setInteger("fcCookBurning", this.cookBurningCounter);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         int iCurrentFireLevel = this.getCurrentFireLevel();
         if (iCurrentFireLevel > 0) {
            if (iCurrentFireLevel > 1 && this.worldObj.rand.nextFloat() <= 0.05F) {
               FireBlock.checkForFireSpreadFromLocation(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.worldObj.rand, 0);
            }

            this.burnTimeSinceLit++;
            if (this.burnTimeCountdown > 0) {
               this.burnTimeCountdown--;
               if (iCurrentFireLevel == 3) {
                  this.burnTimeCountdown--;
               }
            }

            iCurrentFireLevel = this.validateFireLevel();
            if (iCurrentFireLevel > 0) {
               this.updateCookState();
               if (this.worldObj.rand.nextFloat() <= 0.01F && this.isRainingOnCampfire()) {
                  this.extinguishFire(false);
               }
            }
         } else if (this.smoulderCounter > 0) {
            this.smoulderCounter--;
            if (this.smoulderCounter == 0 || this.worldObj.rand.nextFloat() <= 0.01F && this.isRainingOnCampfire()) {
               this.stopSmouldering();
            }
         }
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound tag = new NBTTagCompound();
      if (this.cookStack != null) {
         NBTTagCompound cookTag = new NBTTagCompound();
         this.cookStack.writeToNBT(cookTag);
         tag.setCompoundTag("x", cookTag);
      }

      if (this.spitStack != null) {
         NBTTagCompound spitTag = new NBTTagCompound();
         this.spitStack.writeToNBT(spitTag);
         tag.setCompoundTag("y", spitTag);
      }

      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound tag) {
      NBTTagCompound cookTag = tag.getCompoundTag("x");
      if (cookTag != null) {
         this.cookStack = ItemStack.loadItemStackFromNBT(cookTag);
      }

      NBTTagCompound spitTag = tag.getCompoundTag("y");
      if (spitTag != null) {
         this.spitStack = ItemStack.loadItemStackFromNBT(spitTag);
      }

      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   public void setSpitStack(ItemStack stack) {
      if (stack != null) {
         this.spitStack = stack.copy();
         this.spitStack.stackSize = 1;
      } else {
         this.spitStack = null;
      }

      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public ItemStack getSpitStack() {
      return this.spitStack;
   }

   public void setCookStack(ItemStack stack) {
      if (stack != null) {
         this.cookStack = stack.copy();
         this.cookStack.stackSize = 1;
      } else {
         this.cookStack = null;
         this.cookBurningCounter = 0;
      }

      this.cookCounter = 0;
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public ItemStack getCookStack() {
      return this.cookStack;
   }

   public void ejectContents() {
      if (this.spitStack != null) {
         ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.spitStack);
         this.spitStack = null;
      }

      if (this.cookStack != null) {
         ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.cookStack);
         this.cookStack = null;
      }
   }

   public void addBurnTime(int iBurnTime) {
      this.burnTimeCountdown += iBurnTime * 8 * 2;
      if (this.burnTimeCountdown > 6000) {
         this.burnTimeCountdown = 6000;
      }

      this.validateFireLevel();
   }

   public void onFirstLit() {
      this.burnTimeCountdown = 3200;
      this.burnTimeSinceLit = 0;
   }

   public int validateFireLevel() {
      int iCurrentFireLevel = this.getCurrentFireLevel();
      if (iCurrentFireLevel > 0) {
         if (this.burnTimeCountdown <= 0) {
            this.extinguishFire(true);
            return 0;
         }

         int iDesiredFireLevel = 2;
         if (this.burnTimeSinceLit < 200 || this.burnTimeCountdown < 400) {
            iDesiredFireLevel = 1;
         } else if (this.burnTimeCountdown > 4800) {
            iDesiredFireLevel = 3;
         }

         if (iDesiredFireLevel != iCurrentFireLevel) {
            this.changeFireLevel(iDesiredFireLevel);
            if (iDesiredFireLevel == 1 && iCurrentFireLevel == 2) {
               this.worldObj.playAuxSFX(2227, this.xCoord, this.yCoord, this.zCoord, 1);
            }

            return iDesiredFireLevel;
         }
      } else if (this.burnTimeCountdown > 0 && BTWBlocks.unlitCampfire.getFuelState(this.worldObj, this.xCoord, this.yCoord, this.zCoord) == 2) {
         this.relightSmouldering();
         return 1;
      }

      return iCurrentFireLevel;
   }

   private void extinguishFire(boolean bSmoulder) {
      if (bSmoulder) {
         this.smoulderCounter = 6000;
      } else {
         this.smoulderCounter = 0;
      }

      this.cookCounter = 0;
      this.cookBurningCounter = 0;
      CampfireBlock block = (CampfireBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      block.extinguishFire(this.worldObj, this.xCoord, this.yCoord, this.zCoord, bSmoulder);
   }

   private void changeFireLevel(int iNewLevel) {
      CampfireBlock block = (CampfireBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      block.changeFireLevel(
         this.worldObj, this.xCoord, this.yCoord, this.zCoord, iNewLevel, this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord)
      );
   }

   private int getCurrentFireLevel() {
      CampfireBlock block = (CampfireBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      return block.fireLevel;
   }

   private void updateCookState() {
      if (this.cookStack != null) {
         int iFireLevel = this.getCurrentFireLevel();
         if (iFireLevel >= 2) {
            ItemStack cookResult = CampfireCraftingManager.instance.getRecipeResult(this.cookStack.getItem().itemID);
            if (cookResult != null) {
               this.cookCounter++;
               if (this.cookCounter >= 4800) {
                  this.setCookStack(cookResult);
                  this.cookCounter = 0;
               }
            }

            if (iFireLevel >= 3 && this.cookStack.itemID != BTWItems.burnedMeat.itemID) {
               this.cookBurningCounter++;
               if (this.cookBurningCounter >= 2400) {
                  this.setCookStack(new ItemStack(BTWItems.burnedMeat));
                  this.cookCounter = 0;
                  this.cookBurningCounter = 0;
               }
            }
         }
      }
   }

   public boolean getIsCooking() {
      if (this.cookStack != null && this.getCurrentFireLevel() >= 2) {
         ItemStack cookResult = CampfireCraftingManager.instance.getRecipeResult(this.cookStack.getItem().itemID);
         if (cookResult != null) {
            return true;
         }
      }

      return false;
   }

   public boolean getIsFoodBurning() {
      return this.cookStack != null && this.getCurrentFireLevel() >= 3 && this.cookStack.itemID != BTWItems.burnedMeat.itemID;
   }

   public boolean isRainingOnCampfire() {
      CampfireBlock block = (CampfireBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      return block.isRainingOnCampfire(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }

   private void stopSmouldering() {
      this.smoulderCounter = 0;
      CampfireBlock block = (CampfireBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      block.stopSmouldering(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }

   private void relightSmouldering() {
      this.burnTimeSinceLit = 0;
      CampfireBlock block = (CampfireBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
      block.relightFire(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
   }
}
