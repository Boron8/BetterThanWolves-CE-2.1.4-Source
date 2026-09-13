package net.minecraft.src;

public class EntityMinecartFurnace extends EntityMinecart {
   private int fuel = 0;
   public double pushX;
   public double pushZ;

   public EntityMinecartFurnace(World var1) {
      super(var1);
   }

   public EntityMinecartFurnace(World var1, double var2, double var4, double var6) {
      super(var1, var2, var4, var6);
   }

   @Override
   public int getMinecartType() {
      return 2;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.fuel > 0) {
         this.fuel--;
      }

      if (this.fuel <= 0) {
         this.pushX = this.pushZ = 0.0;
      }

      this.setMinecartPowered(this.fuel > 0);
      if (this.isMinecartPowered() && this.rand.nextInt(4) == 0) {
         this.worldObj.spawnParticle("largesmoke", this.posX, this.posY + 0.8, this.posZ, 0.0, 0.0, 0.0);
      }
   }

   @Override
   public void killMinecart(DamageSource var1) {
      super.killMinecart(var1);
      if (!var1.isExplosion()) {
         this.a(new ItemStack(Block.furnaceIdle, 1), 0.0F);
      }
   }

   @Override
   protected void updateOnTrack(int var1, int var2, int var3, double var4, double var6, int var8, int var9) {
      super.updateOnTrack(var1, var2, var3, var4, var6, var8, var9);
      double var10 = this.pushX * this.pushX + this.pushZ * this.pushZ;
      if (var10 > 1.0E-4 && this.motionX * this.motionX + this.motionZ * this.motionZ > 0.001) {
         var10 = MathHelper.sqrt_double(var10);
         this.pushX /= var10;
         this.pushZ /= var10;
         if (this.pushX * this.motionX + this.pushZ * this.motionZ < 0.0) {
            this.pushX = 0.0;
            this.pushZ = 0.0;
         } else {
            this.pushX = this.motionX;
            this.pushZ = this.motionZ;
         }
      }
   }

   @Override
   protected void applyDrag() {
      double var1 = this.pushX * this.pushX + this.pushZ * this.pushZ;
      if (var1 > 1.0E-4) {
         var1 = MathHelper.sqrt_double(var1);
         this.pushX /= var1;
         this.pushZ /= var1;
         double var3 = 0.05;
         this.motionX *= 0.8F;
         this.motionY *= 0.0;
         this.motionZ *= 0.8F;
         this.motionX = this.motionX + this.pushX * var3;
         this.motionZ = this.motionZ + this.pushZ * var3;
      } else {
         this.motionX *= 0.98F;
         this.motionY *= 0.0;
         this.motionZ *= 0.98F;
      }

      super.applyDrag();
   }

   @Override
   public boolean interact(EntityPlayer var1) {
      ItemStack var2 = var1.inventory.getCurrentItem();
      if (var2 != null && var2.itemID == Item.coal.itemID) {
         if (--var2.stackSize == 0) {
            var1.inventory.setInventorySlotContents(var1.inventory.currentItem, null);
         }

         this.fuel += 3600;
      }

      this.pushX = this.posX - var1.posX;
      this.pushZ = this.posZ - var1.posZ;
      return true;
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
      super.writeEntityToNBT(var1);
      var1.setDouble("PushX", this.pushX);
      var1.setDouble("PushZ", this.pushZ);
      var1.setShort("Fuel", (short)this.fuel);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
      super.readEntityFromNBT(var1);
      this.pushX = var1.getDouble("PushX");
      this.pushZ = var1.getDouble("PushZ");
      this.fuel = var1.getShort("Fuel");
   }

   protected boolean isMinecartPowered() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   protected void setMinecartPowered(boolean var1) {
      if (var1) {
         this.dataWatcher.updateObject(16, (byte)(this.dataWatcher.getWatchableObjectByte(16) | 1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(this.dataWatcher.getWatchableObjectByte(16) & -2));
      }
   }

   @Override
   public Block getDefaultDisplayTile() {
      return Block.furnaceBurning;
   }

   @Override
   public int getDefaultDisplayTileData() {
      return 2;
   }
}
