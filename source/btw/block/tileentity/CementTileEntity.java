package btw.block.tileentity;

import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;

public class CementTileEntity extends TileEntity implements TileEntityDataPacketHandler {
   private int dryTime;
   private int spreadDist = 0;

   public CementTileEntity() {
      this.dryTime = 0;
   }

   @Override
   public void writeToNBT(NBTTagCompound nbttagcompound) {
      super.writeToNBT(nbttagcompound);
      nbttagcompound.setInteger("dryTime", this.dryTime);
      nbttagcompound.setInteger("spreadDist", this.spreadDist);
   }

   @Override
   public void readFromNBT(NBTTagCompound nbttagcompound) {
      super.readFromNBT(nbttagcompound);
      if (nbttagcompound.hasKey("dryTime")) {
         this.dryTime = nbttagcompound.getInteger("dryTime");
      } else {
         this.dryTime = 12;
      }

      if (nbttagcompound.hasKey("spreadDist")) {
         this.spreadDist = nbttagcompound.getInteger("spreadDist");
      } else {
         this.spreadDist = 16;
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound nbttagcompound = new NBTTagCompound();
      nbttagcompound.setShort("d", (short)this.dryTime);
      nbttagcompound.setShort("s", (short)this.spreadDist);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, nbttagcompound);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound nbttagcompound) {
      this.dryTime = nbttagcompound.getShort("d");
      this.spreadDist = nbttagcompound.getShort("s");
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   public int getDryTime() {
      return this.dryTime;
   }

   public void setDryTime(int iDryTime) {
      this.dryTime = iDryTime;
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }

   public int getSpreadDist() {
      return this.spreadDist;
   }

   public void setSpreadDist(int iSpreadDist) {
      this.spreadDist = iSpreadDist;
      this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
   }
}
