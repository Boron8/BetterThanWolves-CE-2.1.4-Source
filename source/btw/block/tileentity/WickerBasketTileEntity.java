package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.item.util.ItemUtils;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;

public class WickerBasketTileEntity extends BasketTileEntity implements TileEntityDataPacketHandler {
   private ItemStack storageStack = null;

   public WickerBasketTileEntity() {
      super(BTWBlocks.wickerBasket);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      this.updateVisualContentsState();
   }

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.a(tag);
      NBTTagCompound storageTag = tag.getCompoundTag("fcStorageStack");
      if (storageTag != null) {
         this.storageStack = ItemStack.loadItemStackFromNBT(storageTag);
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.b(tag);
      if (this.storageStack != null) {
         NBTTagCompound storageTag = new NBTTagCompound();
         this.storageStack.writeToNBT(storageTag);
         tag.setCompoundTag("fcStorageStack", storageTag);
      }
   }

   @Override
   public void ejectContents() {
      if (this.storageStack != null) {
         ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storageStack);
         this.storageStack = null;
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound tag = new NBTTagCompound();
      if (this.storageStack != null) {
         NBTTagCompound storageTag = new NBTTagCompound();
         this.storageStack.writeToNBT(storageTag);
         tag.setCompoundTag("x", storageTag);
      }

      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound tag) {
      NBTTagCompound storageTag = tag.getCompoundTag("x");
      if (storageTag != null) {
         this.storageStack = ItemStack.loadItemStackFromNBT(storageTag);
      }
   }

   @Override
   public boolean shouldStartClosingServerSide() {
      return !this.worldObj.isRemote && this.worldObj.getClosestPlayer(this.xCoord, this.yCoord, this.zCoord, 8.0) == null;
   }

   public void setStorageStack(ItemStack stack) {
      if (stack != null) {
         this.storageStack = stack.copy();
      } else {
         this.storageStack = null;
      }

      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public ItemStack getStorageStack() {
      return this.storageStack;
   }

   private void updateVisualContentsState() {
      if (!this.worldObj.isRemote) {
         boolean bHasContents = BTWBlocks.wickerBasket.getHasContents(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
         if (bHasContents != (this.storageStack != null)) {
            BTWBlocks.wickerBasket.setHasContents(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.storageStack != null);
         }
      }
   }
}
