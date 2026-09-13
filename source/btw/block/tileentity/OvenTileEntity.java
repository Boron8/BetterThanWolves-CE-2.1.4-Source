package btw.block.tileentity;

import btw.block.blocks.FireBlock;
import btw.block.blocks.FurnaceBlock;
import btw.item.util.ItemUtils;
import btw.world.util.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntityFurnace;

public class OvenTileEntity extends TileEntityFurnace implements TileEntityDataPacketHandler {
   private static final float CHANCE_OF_FIRE_SPREAD = 0.01F;
   private boolean lightOnNextUpdate = false;
   private ItemStack cookStack = null;
   private int unlitFuelBurnTime = 0;
   private int visualFuelLevel = 0;
   private final int brickBurnTimeMultiplier = 4;
   private final int cookTimeMultiplier = 4;
   private final int maxFuelBurnTime = 14200;
   private final int visualFuelLevelIncrement = 1600;
   private final int visualSputterFuelLevel = 400;

   @Override
   public void updateEntity() {
      boolean bWasBurning = this.furnaceBurnTime > 0;
      boolean bInventoryChanged = false;
      if (this.furnaceBurnTime > 0) {
         this.furnaceBurnTime--;
      }

      if (!this.worldObj.isRemote) {
         if (bWasBurning || this.lightOnNextUpdate) {
            this.furnaceBurnTime = this.furnaceBurnTime + this.unlitFuelBurnTime;
            this.unlitFuelBurnTime = 0;
            this.lightOnNextUpdate = false;
         }

         if (this.j() && this.u()) {
            this.furnaceCookTime++;
            if (this.furnaceCookTime >= this.getCookTimeForCurrentItem()) {
               this.furnaceCookTime = 0;
               this.l();
               bInventoryChanged = true;
            }
         } else {
            this.furnaceCookTime = 0;
         }

         if (this.j() && this.worldObj.rand.nextFloat() <= 0.01F) {
            BlockPos frontPos = new BlockPos(this.xCoord, this.yCoord, this.zCoord);
            int iFacing = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord) & 7;
            frontPos.addFacingAsOffset(iFacing);
            FireBlock.checkForFireSpreadAndDestructionToOneBlockLocation(this.worldObj, frontPos.x, frontPos.y, frontPos.z);
         }

         FurnaceBlock furnaceBlock = (FurnaceBlock)Block.blocksList[this.worldObj.getBlockId(this.xCoord, this.yCoord, this.zCoord)];
         if (bWasBurning != this.j()) {
            bInventoryChanged = true;
            furnaceBlock.updateFurnaceBlockState(this.furnaceBurnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord, false);
         }

         this.updateCookStack();
         this.updateVisualFuelLevel();
      }

      if (bInventoryChanged) {
         this.k_();
      }
   }

   @Override
   public String getInvName() {
      return "container.fcFurnaceBrick";
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      if (tag.hasKey("fcUnlitFuel")) {
         this.unlitFuelBurnTime = tag.getInteger("fcUnlitFuel");
      }

      if (tag.hasKey("fcVisualFuel")) {
         this.visualFuelLevel = tag.getByte("fcVisualFuel");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      tag.setInteger("fcUnlitFuel", this.unlitFuelBurnTime);
      tag.setByte("fcVisualFuel", (byte)this.visualFuelLevel);
   }

   @Override
   public int getItemBurnTime(ItemStack stack) {
      return super.getItemBurnTime(stack) * 4;
   }

   @Override
   protected int getCookTimeForCurrentItem() {
      return super.getCookTimeForCurrentItem() * 4;
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound tag = new NBTTagCompound();
      if (this.cookStack != null) {
         NBTTagCompound cookTag = new NBTTagCompound();
         this.cookStack.writeToNBT(cookTag);
         tag.setCompoundTag("x", cookTag);
      }

      tag.setByte("y", (byte)this.visualFuelLevel);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound tag) {
      NBTTagCompound cookTag = tag.getCompoundTag("x");
      if (cookTag != null) {
         this.cookStack = ItemStack.loadItemStackFromNBT(cookTag);
      }

      this.visualFuelLevel = tag.getByte("y");
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   public boolean attemptToLight() {
      if (this.unlitFuelBurnTime > 0) {
         this.lightOnNextUpdate = true;
         return true;
      } else {
         return false;
      }
   }

   public boolean hasValidFuel() {
      return this.unlitFuelBurnTime > 0;
   }

   private void updateCookStack() {
      ItemStack newCookStack = this.furnaceItemStacks[0];
      if (newCookStack == null) {
         newCookStack = this.furnaceItemStacks[2];
         if (newCookStack == null) {
            newCookStack = this.furnaceItemStacks[1];
         }
      }

      if (!ItemStack.areItemStacksEqual(newCookStack, this.cookStack)) {
         this.setCookStack(newCookStack);
      }
   }

   public void setCookStack(ItemStack stack) {
      if (stack != null) {
         this.cookStack = stack.copy();
      } else {
         this.cookStack = null;
      }

      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public ItemStack getCookStack() {
      return this.cookStack;
   }

   public void givePlayerCookStack(EntityPlayer player, int iFacing) {
      if (!this.worldObj.isRemote) {
         this.ejectAllNotCookStacksToFacing(player, iFacing);
      }

      ItemUtils.givePlayerStackOrEjectFromTowardsFacing(player, this.cookStack, this.xCoord, this.yCoord, this.zCoord, iFacing);
      this.furnaceItemStacks[0] = null;
      this.furnaceItemStacks[1] = null;
      this.furnaceItemStacks[2] = null;
      this.setCookStack(null);
   }

   private void ejectAllNotCookStacksToFacing(EntityPlayer player, int iFacing) {
      if (this.furnaceItemStacks[0] != null && !ItemStack.areItemStacksEqual(this.furnaceItemStacks[0], this.cookStack)) {
         ItemUtils.ejectStackFromBlockTowardsFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.furnaceItemStacks[0], iFacing);
         this.furnaceItemStacks[0] = null;
      }

      if (this.furnaceItemStacks[1] != null && !ItemStack.areItemStacksEqual(this.furnaceItemStacks[1], this.cookStack)) {
         ItemUtils.ejectStackFromBlockTowardsFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.furnaceItemStacks[1], iFacing);
         this.furnaceItemStacks[1] = null;
      }

      if (this.furnaceItemStacks[2] != null && !ItemStack.areItemStacksEqual(this.furnaceItemStacks[2], this.cookStack)) {
         ItemUtils.ejectStackFromBlockTowardsFacing(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.furnaceItemStacks[2], iFacing);
         this.furnaceItemStacks[2] = null;
      }

      this.k_();
   }

   public void addCookStack(ItemStack stack) {
      this.furnaceItemStacks[0] = stack;
      this.k_();
   }

   public int attemptToAddFuel(ItemStack stack) {
      int iTotalBurnTime = this.unlitFuelBurnTime + this.furnaceBurnTime;
      int iDeltaBurnTime = 14200 - iTotalBurnTime;
      int iNumItemsBurned = 0;
      if (iDeltaBurnTime > 0) {
         iNumItemsBurned = iDeltaBurnTime / this.getItemBurnTime(stack);
         if (iNumItemsBurned == 0 && this.getVisualFuelLevel() <= 2) {
            iNumItemsBurned = 1;
         }

         if (iNumItemsBurned > 0) {
            if (iNumItemsBurned > stack.stackSize) {
               iNumItemsBurned = stack.stackSize;
            }

            this.unlitFuelBurnTime = this.unlitFuelBurnTime + this.getItemBurnTime(stack) * iNumItemsBurned;
            this.k_();
         }
      }

      return iNumItemsBurned;
   }

   private void updateVisualFuelLevel() {
      int iTotalBurnTime = this.unlitFuelBurnTime + this.furnaceBurnTime;
      int iNewFuelLevel = 0;
      if (iTotalBurnTime > 0) {
         if (iTotalBurnTime < 400) {
            iNewFuelLevel = 1;
         } else {
            iNewFuelLevel = iTotalBurnTime / 1600 + 2;
         }
      }

      this.setVisualFuelLevel(iNewFuelLevel);
   }

   public int getVisualFuelLevel() {
      return this.visualFuelLevel;
   }

   public void setVisualFuelLevel(int iLevel) {
      if (this.visualFuelLevel != iLevel) {
         this.visualFuelLevel = iLevel;
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }
   }
}
