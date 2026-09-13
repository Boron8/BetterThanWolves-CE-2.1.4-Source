package btw.block.tileentity;

import btw.block.blocks.BasketBlock;
import net.minecraft.src.TileEntity;

public abstract class BasketTileEntity extends TileEntity {
   protected static final float LID_OPEN_RATE = 0.1F;
   protected static final float LID_CLOSE_RATE = 0.2F;
   protected static final float MAX_KEEP_OPEN_RANGE = 8.0F;
   public float lidOpenRatio = 0.0F;
   public float prevLidOpenRatio = 0.0F;
   public boolean closing = false;
   BasketBlock blockBasket;

   public BasketTileEntity(BasketBlock blockBasket) {
      this.blockBasket = blockBasket;
   }

   @Override
   public void updateEntity() {
      super.updateEntity();
      this.updateOpenState();
   }

   @Override
   public boolean receiveClientEvent(int iEventType, int iEventParam) {
      if (iEventType == 1) {
         this.closing = iEventParam == 1;
         return true;
      } else {
         return super.receiveClientEvent(iEventType, iEventParam);
      }
   }

   public abstract void ejectContents();

   public void startClosingServerSide() {
      this.closing = true;
      this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord, this.q().blockID, 1, 1);
   }

   private void updateOpenState() {
      this.prevLidOpenRatio = this.lidOpenRatio;
      if (this.blockBasket.getIsOpen(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
         if (this.closing) {
            this.lidOpenRatio -= 0.2F;
            if (this.lidOpenRatio <= 0.0F) {
               this.lidOpenRatio = 0.0F;
               if (!this.worldObj.isRemote) {
                  this.blockBasket.setIsOpen(this.worldObj, this.xCoord, this.yCoord, this.zCoord, false);
                  this.onFinishedClosing();
               }
            }
         } else if (this.shouldStartClosingServerSide()) {
            this.startClosingServerSide();
         } else {
            this.lidOpenRatio += 0.1F;
            if (this.lidOpenRatio > 1.0F) {
               this.lidOpenRatio = 1.0F;
            }
         }
      } else {
         this.closing = false;
         this.lidOpenRatio = 0.0F;
      }
   }

   public abstract boolean shouldStartClosingServerSide();

   protected void onFinishedClosing() {
      this.worldObj
         .playSoundEffect(
            this.xCoord + 0.5,
            this.yCoord + 0.5,
            this.zCoord + 0.5,
            "step.gravel",
            0.1F + this.worldObj.rand.nextFloat() * 0.1F,
            1.0F + this.worldObj.rand.nextFloat() * 0.25F
         );
   }
}
