package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityOcelot extends EntityTameable {
   private EntityAITempt aiTempt;

   public EntityOcelot(World par1World) {
      super(par1World);
      this.texture = "/mob/ozelot.png";
      this.a(0.6F, 0.8F);
      this.aC().setAvoidsWater(true);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(3, this.aiTempt = new EntityAITempt(this, 0.18F, Item.fishRaw.itemID, true));
      this.tasks.addTask(4, new EntityAIAvoidEntity(this, EntityPlayer.class, 16.0F, 0.23F, 0.4F));
      this.tasks.addTask(5, new EntityAIFollowOwner(this, 0.3F, 10.0F, 5.0F));
      this.tasks.addTask(6, new EntityAIOcelotSit(this, 0.4F));
      this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
      this.tasks.addTask(8, new EntityAIOcelotAttack(this));
      this.tasks.addTask(9, new EntityAIMate(this, 0.23F));
      this.tasks.addTask(10, new EntityAIWander(this, 0.23F));
      this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
      this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, 14.0F, 750, false));
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(18, (byte)0);
   }

   @Override
   public void updateAITick() {
      if (this.aA().isUpdating()) {
         float var1 = this.aA().getSpeed();
         if (var1 == 0.18F) {
            this.b(true);
            this.c(false);
         } else if (var1 == 0.4F) {
            this.b(false);
            this.c(true);
         } else {
            this.b(false);
            this.c(false);
         }
      } else {
         this.b(false);
         this.c(false);
      }
   }

   @Override
   protected boolean canDespawn() {
      return !this.m();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      switch (this.getTameSkin()) {
         case 0:
            return "/mob/ozelot.png";
         case 1:
            return "/mob/cat_black.png";
         case 2:
            return "/mob/cat_red.png";
         case 3:
            return "/mob/cat_siamese.png";
         default:
            return super.N();
      }
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public int getMaxHealth() {
      return 10;
   }

   @Override
   protected void fall(float par1) {
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("CatType", this.getTameSkin());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setTameSkin(par1NBTTagCompound.getInteger("CatType"));
   }

   @Override
   protected String getLivingSound() {
      return this.m() ? (this.r() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : "";
   }

   @Override
   protected String getHurtSound() {
      return "mob.cat.hitt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.cat.hitt";
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   protected int getDropItemId() {
      return Item.leather.itemID;
   }

   @Override
   public boolean attackEntityAsMob(Entity par1Entity) {
      return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), 3);
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         this.aiSit.setSitting(false);
         return super.a(par1DamageSource, par2);
      }
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
      if (this.m()) {
         if (par1EntityPlayer.username.equalsIgnoreCase(this.o()) && !this.worldObj.isRemote && !this.isBreedingItem(var2)) {
            this.aiSit.setSitting(!this.n());
         }
      } else if (this.aiTempt.func_75277_f() && var2 != null && var2.itemID == Item.fishRaw.itemID && par1EntityPlayer.e(this) < 9.0) {
         if (!par1EntityPlayer.capabilities.isCreativeMode) {
            var2.stackSize--;
         }

         if (var2.stackSize <= 0) {
            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
         }

         if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(3) == 0) {
               this.j(true);
               this.setTameSkin(1 + this.worldObj.rand.nextInt(3));
               this.a(par1EntityPlayer.username);
               this.i(true);
               this.aiSit.setSitting(true);
               this.worldObj.setEntityState(this, (byte)7);
            } else {
               this.i(false);
               this.worldObj.setEntityState(this, (byte)6);
            }
         }

         return true;
      }

      return super.a_(par1EntityPlayer);
   }

   public EntityOcelot spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      EntityOcelot var2 = (EntityOcelot)EntityList.createEntityOfType(EntityOcelot.class, this.worldObj);
      if (this.m()) {
         var2.a(this.o());
         var2.j(true);
         var2.setTameSkin(this.getTameSkin());
      }

      return var2;
   }

   @Override
   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return par1ItemStack != null && par1ItemStack.itemID == Item.fishRaw.itemID;
   }

   @Override
   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      if (par1EntityAnimal == this) {
         return false;
      } else if (!this.m()) {
         return false;
      } else if (!(par1EntityAnimal instanceof EntityOcelot)) {
         return false;
      } else {
         EntityOcelot var2 = (EntityOcelot)par1EntityAnimal;
         return !var2.m() ? false : this.r() && var2.r();
      }
   }

   public int getTameSkin() {
      return this.dataWatcher.getWatchableObjectByte(18);
   }

   public void setTameSkin(int par1) {
      this.dataWatcher.updateObject(18, (byte)par1);
   }

   @Override
   public boolean getCanSpawnHere() {
      if (this.worldObj.rand.nextInt(3) == 0) {
         return false;
      } else if (this.worldObj.checkNoEntityCollision(this.boundingBox)
         && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()
         && !this.worldObj.isAnyLiquid(this.boundingBox)) {
         int x = MathHelper.floor_double(this.posX);
         int y = MathHelper.floor_double(this.boundingBox.minY);
         int z = MathHelper.floor_double(this.posZ);
         if (y < 63) {
            return false;
         } else {
            int blockID = this.worldObj.getBlockId(x, y - 1, z);
            Block block = Block.blocksList[blockID];
            return block != null && block.isLeafBlock(this.worldObj, x, y - 1, z) && this.worldObj.getBlockLightValue(x, y, z) > 9;
         }
      } else {
         return false;
      }
   }

   @Override
   public String getEntityName() {
      return this.bQ() ? this.bP() : (this.m() ? "entity.Cat.name" : super.am());
   }

   @Override
   public void initCreature() {
      if (this.worldObj.rand.nextInt(7) == 0) {
         for (int var1 = 0; var1 < 2; var1++) {
            EntityOcelot var2 = (EntityOcelot)EntityList.createEntityOfType(EntityOcelot.class, this.worldObj);
            var2.b(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
            var2.a(-24000);
            this.worldObj.spawnEntityInWorld(var2);
         }
      }
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.spawnBabyAnimal(par1EntityAgeable);
   }
}
