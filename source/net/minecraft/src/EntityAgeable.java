package net.minecraft.src;

public abstract class EntityAgeable extends EntityCreature {
   private float adultWidth = -1.0F;
   private float adultHeight;

   public EntityAgeable(World par1World) {
      super(par1World);
   }

   public abstract EntityAgeable createChild(EntityAgeable var1);

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      return this.entityAgeableInteract(par1EntityPlayer);
   }

   public boolean entityAgeableInteract(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
      if (var2 != null && var2.itemID == Item.monsterPlacer.itemID && !this.worldObj.isRemote) {
         Class var3 = EntityList.getClassFromID(var2.getItemDamage());
         if (var3 != null && var3.isAssignableFrom(this.getClass())) {
            EntityAgeable var4 = this.createChild(this);
            if (var4 != null) {
               var4.setGrowingAge(-this.getTicksForChildToGrow());
               var4.b(this.posX, this.posY, this.posZ, 0.0F, 0.0F);
               this.worldObj.spawnEntityInWorld(var4);
               if (var2.hasDisplayName()) {
                  var4.c(var2.getDisplayName());
               }

               if (!par1EntityPlayer.capabilities.isCreativeMode) {
                  var2.stackSize--;
                  if (var2.stackSize <= 0) {
                     par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                  }
               }
            }
         }
      }

      return super.a_(par1EntityPlayer);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(12, new Integer(0));
   }

   public int getGrowingAge() {
      return this.dataWatcher.getWatchableObjectInt(12);
   }

   public void setGrowingAge(int par1) {
      this.dataWatcher.updateObject(12, par1);
      this.adjustSizeForAge(this.isChild());
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Age", this.getGrowingAge());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setGrowingAge(par1NBTTagCompound.getInteger("Age"));
   }

   @Override
   public void onLivingUpdate() {
      super.c();
      if (this.worldObj.isRemote) {
         this.adjustSizeForAge(this.isChild());
      } else {
         int var1 = this.getGrowingAge();
         if (var1 < 0) {
            if (!this.canChildGrow()) {
               return;
            }

            if (++var1 == 0) {
               AxisAlignedBB adultBounds = AxisAlignedBB.getAABBPool()
                  .getAABB(
                     this.boundingBox.minX,
                     this.boundingBox.minY,
                     this.boundingBox.minZ,
                     this.boundingBox.minX + this.adultWidth,
                     this.boundingBox.minY + this.adultHeight,
                     this.boundingBox.minZ + this.adultWidth
                  );
               if (!this.worldObj.getCollidingBoundingBoxes(this, adultBounds).isEmpty()) {
                  var1 = -20;
               }
            }

            this.setGrowingAge(var1);
         } else if (var1 > 0) {
            if (!this.canLoveJuiceRegenerate()) {
               return;
            }

            this.setGrowingAge(--var1);
         }
      }
   }

   @Override
   public boolean isChild() {
      return this.getGrowingAge() < 0;
   }

   @Override
   protected final void setSize(float fWidth, float fHeight) {
      boolean bSizeAlreadyInitialized = this.adultWidth > 0.0F;
      this.adultWidth = fWidth;
      this.adultHeight = fHeight;
      if (!bSizeAlreadyInitialized) {
         this.adjustedSizeToScale(1.0F);
      }
   }

   private void adjustedSizeToScale(float fScale) {
      super.a(this.adultWidth * fScale, this.adultHeight * fScale);
   }

   public void adjustSizeForAge(boolean bIsChild) {
      this.adjustedSizeToScale(bIsChild ? 0.5F : 1.0F);
   }

   public boolean canChildGrow() {
      return this.worldObj.provider.dimensionId != 1;
   }

   public boolean canLoveJuiceRegenerate() {
      return true;
   }

   public int getTicksForChildToGrow() {
      return 24000;
   }
}
