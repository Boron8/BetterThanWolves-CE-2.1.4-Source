package btw.block.tileentity;

import btw.item.util.ItemUtils;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;

public class PlacedToolTileEntity extends TileEntity implements TileEntityDataPacketHandler {
   private ItemStack toolStack = null;

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      NBTTagCompound storageTag = tag.getCompoundTag("fcToolStack");
      if (storageTag != null) {
         this.toolStack = ItemStack.loadItemStackFromNBT(storageTag);
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      if (this.toolStack != null) {
         NBTTagCompound storageTag = new NBTTagCompound();
         this.toolStack.writeToNBT(storageTag);
         tag.setCompoundTag("fcToolStack", storageTag);
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound tag = new NBTTagCompound();
      if (this.toolStack != null) {
         NBTTagCompound storageTag = new NBTTagCompound();
         this.toolStack.writeToNBT(storageTag);
         tag.setCompoundTag("x", storageTag);
      }

      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound tag) {
      NBTTagCompound storageTag = tag.getCompoundTag("x");
      if (storageTag != null) {
         this.toolStack = ItemStack.loadItemStackFromNBT(storageTag);
      }

      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   public void setToolStack(ItemStack stack) {
      if (stack != null) {
         this.toolStack = stack.copy();
      } else {
         this.toolStack = null;
      }
   }

   public ItemStack getToolStack() {
      return this.toolStack;
   }

   public void ejectContents() {
      if (this.toolStack != null) {
         ItemUtils.ejectStackWithRandomOffset(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.toolStack);
         this.toolStack = null;
      }
   }
}
