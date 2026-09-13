package net.minecraft.src;

import java.util.List;

public class EntityMinecartHopper extends EntityMinecartContainer implements Hopper {
   private boolean isBlocked = true;
   private int transferTicker = -1;

   public EntityMinecartHopper(World var1) {
      super(var1);
   }

   public EntityMinecartHopper(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   public int getMinecartType() {
      return 5;
   }

   @Override
   public Block getDefaultDisplayTile() {
      return Block.hopperBlock;
   }

   @Override
   public int getDefaultDisplayTileOffset() {
      return 1;
   }

   @Override
   public int getSizeInventory() {
      return 5;
   }

   @Override
   public boolean interact(EntityPlayer var1) {
      if (!this.worldObj.isRemote) {
         var1.displayGUIHopperMinecart(this);
      }

      return true;
   }

   @Override
   public void onActivatorRailPass(int var1, int var2, int var3, boolean var4) {
      boolean var5 = !var4;
      if (var5 != this.getBlocked()) {
         this.setBlocked(var5);
      }
   }

   public boolean getBlocked() {
      return this.isBlocked;
   }

   public void setBlocked(boolean var1) {
      this.isBlocked = var1;
   }

   @Override
   public World getWorldObj() {
      return this.worldObj;
   }

   @Override
   public double getXPos() {
      return this.posX;
   }

   @Override
   public double getYPos() {
      return this.posY;
   }

   @Override
   public double getZPos() {
      return this.posZ;
   }

   @Override
   public void onUpdate() {
      super.l_();
      if (!this.worldObj.isRemote && this.R() && this.getBlocked()) {
         this.transferTicker--;
         if (!this.canTransfer()) {
            this.setTransferTicker(0);
            if (this.func_96112_aD()) {
               this.setTransferTicker(4);
               this.k_();
            }
         }
      }
   }

   public boolean func_96112_aD() {
      if (TileEntityHopper.suckItemsIntoHopper(this)) {
         return true;
      } else {
         List var1 = this.worldObj.selectEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(0.25, 0.0, 0.25), IEntitySelector.selectAnything);
         if (var1.size() > 0) {
            TileEntityHopper.func_96114_a(this, (EntityItem)var1.get(0));
         }

         return false;
      }
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(var1);
      this.a(Block.hopperBlock.blockID, 1, 0.0F);
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setInteger("TransferCooldown", this.transferTicker);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.transferTicker = var1.getInteger("TransferCooldown");
   }

   public void setTransferTicker(int var1) {
      this.transferTicker = var1;
   }

   public boolean canTransfer() {
      return this.transferTicker > 0;
   }
}
