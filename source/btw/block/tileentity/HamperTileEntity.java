package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.inventory.container.HamperContainer;
import btw.inventory.util.InventoryUtils;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.IInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.NBTTagList;

public class HamperTileEntity extends BasketTileEntity implements IInventory {
   public static final int INVENTORY_SIZE = 4;
   public static final String FC_HAMPER = "container.fcHamper";
   private static final int STACK_SIZE_LIMIT = 64;
   private static final double MAX_PLAYER_INTERACTION_DIST_SQ = 64.0;
   private static final int FULL_UPDATE_INTERVAL = 200;
   private ItemStack[] contents;
   private int numUsingPlayers;
   private int fullUpdateCounter = 0;

   public HamperTileEntity() {
      super(BTWBlocks.hamper);
      this.contents = new ItemStack[4];
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.a(tag);
      NBTTagList tagList = tag.getTagList("Items");
      this.contents = new ItemStack[this.getSizeInventory()];

      for (int iTempIndex = 0; iTempIndex < tagList.tagCount(); iTempIndex++) {
         NBTTagCompound tempTag = (NBTTagCompound)tagList.tagAt(iTempIndex);
         int tempSlot = tempTag.getByte("Slot") & 255;
         if (tempSlot >= 0 && tempSlot < this.contents.length) {
            this.contents[tempSlot] = ItemStack.loadItemStackFromNBT(tempTag);
         }
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.b(tag);
      NBTTagList tagList = new NBTTagList();

      for (int iTempIndex = 0; iTempIndex < this.contents.length; iTempIndex++) {
         if (this.contents[iTempIndex] != null) {
            NBTTagCompound tempTag = new NBTTagCompound();
            tempTag.setByte("Slot", (byte)iTempIndex);
            this.contents[iTempIndex].writeToNBT(tempTag);
            tagList.appendTag(tempTag);
         }
      }

      tag.setTag("Items", tagList);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         if (this.numUsingPlayers > 0 && !this.blockBasket.getIsOpen(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
            this.worldObj
               .playSoundEffect(
                  this.xCoord + 0.5,
                  this.yCoord + 0.5,
                  this.zCoord + 0.5,
                  "step.gravel",
                  0.25F + this.worldObj.rand.nextFloat() * 0.1F,
                  0.5F + this.worldObj.rand.nextFloat() * 0.1F
               );
            this.blockBasket.setIsOpen(this.worldObj, this.xCoord, this.yCoord, this.zCoord, true);
            if (this.closing) {
               this.closing = false;
               this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.q().blockID, 1, 0);
            }
         }

         this.fullUpdateCounter++;
         if ((this.fullUpdateCounter + this.xCoord + this.yCoord + this.zCoord) % 200 == 0 && this.numUsingPlayers != 0) {
            int iOldNumUsing = this.numUsingPlayers;
            this.numUsingPlayers = 0;

            for (EntityPlayer tempPlayer : this.worldObj
               .getEntitiesWithinAABB(
                  EntityPlayer.class,
                  AxisAlignedBB.getAABBPool()
                     .getAABB(
                        this.xCoord - 8.0F, this.yCoord - 8.0F, this.zCoord - 8.0F, this.xCoord + 1 + 8.0F, this.yCoord + 1 + 8.0F, this.zCoord + 1 + 8.0F
                     )
               )) {
               if (tempPlayer.openContainer instanceof HamperContainer && ((HamperContainer)tempPlayer.openContainer).containerInventory == this) {
                  this.numUsingPlayers++;
               }
            }
         }
      }
   }

   @Override
   public void ejectContents() {
      InventoryUtils.ejectInventoryContents(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this);
   }

   @Override
   public int getSizeInventory() {
      return 4;
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
   public ItemStack getStackInSlotOnClosing(int iSlot) {
      return null;
   }

   @Override
   public void setInventorySlotContents(int iSlot, ItemStack itemstack) {
      this.contents[iSlot] = itemstack;
      if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
         itemstack.stackSize = this.getInventoryStackLimit();
      }

      this.k_();
   }

   @Override
   public String getInvName() {
      return "container.fcHamper";
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer entityplayer) {
      return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this
         && entityplayer.e(this.xCoord + 0.5, this.yCoord + 0.5, this.zCoord + 0.5) <= 64.0;
   }

   @Override
   public void openChest() {
      if (this.numUsingPlayers < 0) {
         this.numUsingPlayers = 0;
      }

      this.numUsingPlayers++;
   }

   @Override
   public void closeChest() {
      this.numUsingPlayers--;
   }

   @Override
   public boolean isStackValidForSlot(int iSlot, ItemStack stack) {
      return true;
   }

   @Override
   public boolean isInvNameLocalized() {
      return false;
   }

   @Override
   public boolean shouldStartClosingServerSide() {
      return !this.worldObj.isRemote && this.numUsingPlayers <= 0;
   }
}
