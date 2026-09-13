package net.minecraft.src;

public abstract class EntityMinecartContainer extends EntityMinecart implements IInventory {
   private ItemStack[] minecartContainerItems = new ItemStack[36];
   private boolean dropContentsWhenDead = true;

   public EntityMinecartContainer(World par1World) {
      super(par1World);
   }

   public EntityMinecartContainer(World par1World, double par2, double par4, double par6) {
      super(par1World, par2, par4, par6);
   }

   @Override
   public void killMinecart(DamageSource par1DamageSource) {
      super.killMinecart(par1DamageSource);

      for (int var2 = 0; var2 < this.j_(); var2++) {
         ItemStack var3 = this.getStackInSlot(var2);
         if (var3 != null) {
            while (var3.stackSize > 0) {
               int var7 = this.rand.nextInt(21) + 10;
               if (var7 > var3.stackSize) {
                  var7 = var3.stackSize;
               }

               var3.stackSize -= var7;
               EntityItem var8 = (EntityItem)EntityList.createEntityOfType(
                  EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(var3.itemID, var7, var3.getItemDamage())
               );
               float var9 = 0.08F;
               var8.motionX = (float)this.rand.nextGaussian() * var9;
               var8.motionY = (float)this.rand.nextGaussian() * var9 + 0.2F;
               var8.motionZ = (float)this.rand.nextGaussian() * var9;
               this.worldObj.spawnEntityInWorld(var8);
            }
         }
      }
   }

   @Override
   public ItemStack getStackInSlot(int par1) {
      return this.minecartContainerItems[par1];
   }

   @Override
   public ItemStack decrStackSize(int par1, int par2) {
      if (this.minecartContainerItems[par1] != null) {
         if (this.minecartContainerItems[par1].stackSize <= par2) {
            ItemStack var3 = this.minecartContainerItems[par1];
            this.minecartContainerItems[par1] = null;
            return var3;
         } else {
            ItemStack var3 = this.minecartContainerItems[par1].splitStack(par2);
            if (this.minecartContainerItems[par1].stackSize == 0) {
               this.minecartContainerItems[par1] = null;
            }

            return var3;
         }
      } else {
         return null;
      }
   }

   @Override
   public ItemStack getStackInSlotOnClosing(int par1) {
      if (this.minecartContainerItems[par1] != null) {
         ItemStack var2 = this.minecartContainerItems[par1];
         this.minecartContainerItems[par1] = null;
         return var2;
      } else {
         return null;
      }
   }

   @Override
   public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
      this.minecartContainerItems[par1] = par2ItemStack;
      if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit()) {
         par2ItemStack.stackSize = this.getInventoryStackLimit();
      }
   }

   @Override
   public void onInventoryChanged() {
   }

   @Override
   public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer) {
      return this.isDead ? false : par1EntityPlayer.e(this) <= 64.0;
   }

   @Override
   public void openChest() {
   }

   @Override
   public void closeChest() {
   }

   @Override
   public boolean isStackValidForSlot(int par1, ItemStack par2ItemStack) {
      return true;
   }

   @Override
   public String getInvName() {
      return this.c() ? this.t() : "container.minecart";
   }

   @Override
   public int getInventoryStackLimit() {
      return 64;
   }

   @Override
   public void travelToDimension(int par1) {
      this.dropContentsWhenDead = false;
      super.c(par1);
   }

   @Override
   public void setDead() {
      if (this.dropContentsWhenDead) {
         for (int var1 = 0; var1 < this.j_(); var1++) {
            ItemStack var2 = this.getStackInSlot(var1);
            if (var2 != null) {
               while (var2.stackSize > 0) {
                  int var6 = this.rand.nextInt(21) + 10;
                  if (var6 > var2.stackSize) {
                     var6 = var2.stackSize;
                  }

                  var2.stackSize -= var6;
                  EntityItem var7 = (EntityItem)EntityList.createEntityOfType(
                     EntityItem.class, this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(var2.itemID, var6, var2.getItemDamage())
                  );
                  if (var2.hasTagCompound()) {
                     var7.getEntityItem().setTagCompound((NBTTagCompound)var2.getTagCompound().copy());
                  }

                  float var8 = 0.08F;
                  var7.motionX = (float)this.rand.nextGaussian() * var8;
                  var7.motionY = (float)this.rand.nextGaussian() * var8 + 0.2F;
                  var7.motionZ = (float)this.rand.nextGaussian() * var8;
                  this.worldObj.spawnEntityInWorld(var7);
               }
            }
         }
      }

      super.setDead();
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      NBTTagList var2 = new NBTTagList();

      for (int var3 = 0; var3 < this.minecartContainerItems.length; var3++) {
         if (this.minecartContainerItems[var3] != null) {
            NBTTagCompound var4 = new NBTTagCompound();
            var4.setByte("Slot", (byte)var3);
            this.minecartContainerItems[var3].writeToNBT(var4);
            var2.appendTag(var4);
         }
      }

      par1NBTTagCompound.setTag("Items", var2);
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      NBTTagList var2 = par1NBTTagCompound.getTagList("Items");
      this.minecartContainerItems = new ItemStack[this.j_()];

      for (int var3 = 0; var3 < var2.tagCount(); var3++) {
         NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
         int var5 = var4.getByte("Slot") & 255;
         if (var5 >= 0 && var5 < this.minecartContainerItems.length) {
            this.minecartContainerItems[var5] = ItemStack.loadItemStackFromNBT(var4);
         }
      }
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      if (!this.worldObj.isRemote) {
         par1EntityPlayer.displayGUIChest(this);
      }

      return true;
   }

   @Override
   protected void applyDrag() {
      int var1 = 15 - Container.calcRedstoneFromInventory(this);
      float var2 = 0.98F + var1 * 0.001F;
      this.motionX *= var2;
      this.motionY *= 0.0;
      this.motionZ *= var2;
   }
}
