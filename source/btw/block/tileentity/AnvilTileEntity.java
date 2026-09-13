package btw.block.tileentity;

import btw.item.BTWItems;
import btw.item.util.ItemUtils;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public class AnvilTileEntity extends TileEntity {
   private int mouldContentsBitField = 0;

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("m_iMouldContentsBitField", this.mouldContentsBitField);
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      if (nbttagcompound.hasKey("m_iMouldContentsBitField")) {
         this.mouldContentsBitField = nbttagcompound.getInteger("m_iMouldContentsBitField");
      } else {
         this.mouldContentsBitField = 0;
      }
   }

   public void clearMouldContents() {
      this.mouldContentsBitField = 0;
   }

   public boolean doesSlotContainMould(int iSlotX, int iSlotY) {
      int iSlotNum = iSlotX + iSlotY * 4;
      return this.doesSlotContainMould(iSlotNum);
   }

   public boolean doesSlotContainMould(int iSlotNum) {
      int iBitMask = 1 << iSlotNum;
      return (this.mouldContentsBitField & iBitMask) > 0;
   }

   public void setSlotContainsMould(int iSlotNum) {
      int iBitMask = 1 << iSlotNum;
      this.mouldContentsBitField |= iBitMask;
   }

   public void setSlotContainsMould(int iSlotX, int iSlotY) {
      int iBitMask = 1 << iSlotX + iSlotY * 4;
      this.mouldContentsBitField |= iBitMask;
   }

   public void ejectMoulds() {
      int iMouldCount = 0;

      for (int iTemp = 0; iTemp < 16; iTemp++) {
         if (this.doesSlotContainMould(iTemp)) {
            ItemUtils.ejectSingleItemWithRandomOffset(this.worldObj, this.xCoord, this.yCoord, this.zCoord, BTWItems.mould.itemID, 0);
         }
      }

      this.clearMouldContents();
   }
}
