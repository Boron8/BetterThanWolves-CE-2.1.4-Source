package btw.block.tileentity;

import btw.block.BTWBlocks;
import btw.block.blocks.UnfiredBrickBlock;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class UnfiredBrickTileEntity extends TileEntity implements TileEntityDataPacketHandler {
   private static final int TIME_TO_COOK = 12000;
   private static final int RAIN_COOK_DECAY = 10;
   private int cookCounter = 0;
   private boolean isCooking = false;

   @Override
   public void readFromNBT(NBTTagCompound tag) {
      super.readFromNBT(tag);
      if (tag.hasKey("fcCookCounter")) {
         this.cookCounter = tag.getInteger("fcCookCounter");
      }
   }

   @Override
   public void writeToNBT(NBTTagCompound tag) {
      super.writeToNBT(tag);
      tag.setInteger("fcCookCounter", this.cookCounter);
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      if (!this.worldObj.isRemote) {
         this.updateCooking();
      } else if (this.isCooking && this.worldObj.rand.nextInt(20) == 0) {
         double xPos = this.xCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F;
         double yPos = this.yCoord + 0.5F + this.worldObj.rand.nextFloat() * 0.25F;
         double zPos = this.zCoord + 0.25F + this.worldObj.rand.nextFloat() * 0.5F;
         this.worldObj.spawnParticle("fcwhitesmoke", xPos, yPos, zPos, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public Packet getDescriptionPacket() {
      NBTTagCompound tag = new NBTTagCompound();
      tag.setBoolean("x", this.isCooking);
      return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 1, tag);
   }

   @Override
   public void readNBTFromPacket(NBTTagCompound tag) {
      this.isCooking = tag.getBoolean("x");
      this.worldObj.markBlockRangeForRenderUpdate(this.xCoord, this.yCoord, this.zCoord, this.xCoord, this.yCoord, this.zCoord);
   }

   public void updateCooking() {
      int iBlockMaxNaturalLight = this.worldObj.getBlockNaturalLightValueMaximum(this.xCoord, this.yCoord, this.zCoord);
      int iBlockCurrentNaturalLight = iBlockMaxNaturalLight - this.worldObj.skylightSubtracted;
      boolean bNewCooking = iBlockCurrentNaturalLight >= 15;
      int iBlockAboveID = this.worldObj.getBlockId(this.xCoord, this.yCoord + 1, this.zCoord);
      Block blockAbove = Block.blocksList[iBlockAboveID];
      if (blockAbove != null && blockAbove.isGroundCover()) {
         bNewCooking = false;
      }

      if (bNewCooking != this.isCooking) {
         this.isCooking = bNewCooking;
         this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
      }

      UnfiredBrickBlock brickBlock = BTWBlocks.placedUnfiredBrick;
      if (this.isCooking) {
         this.cookCounter++;
         if (this.cookCounter >= 12000) {
            brickBlock.onFinishedCooking(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
            return;
         }
      } else if (this.isRainingOnBrick(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
         this.cookCounter -= 10;
         if (this.cookCounter < 0) {
            this.cookCounter = 0;
         }
      }

      int iDisplayedCookLevel = brickBlock.getCookLevel(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
      int iCurrentCookLevel = this.computeCookLevel();
      if (iDisplayedCookLevel != iCurrentCookLevel) {
         brickBlock.setCookLevel(this.worldObj, this.xCoord, this.yCoord, this.zCoord, iCurrentCookLevel);
      }
   }

   public boolean isRainingOnBrick(World world, int i, int j, int k) {
      return world.isRaining() && world.isRainingAtPos(i, j, k);
   }

   private int computeCookLevel() {
      if (this.cookCounter > 0) {
         int iCookLevel = (int)(this.cookCounter / 12000.0F * 7.0F) + 1;
         return MathHelper.clamp_int(iCookLevel, 0, 7);
      } else {
         return 0;
      }
   }
}
