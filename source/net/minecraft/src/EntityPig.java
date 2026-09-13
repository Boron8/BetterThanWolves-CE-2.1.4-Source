package net.minecraft.src;

public class EntityPig extends EntityAnimal {
   private final EntityAIControlledByPlayer aiControlledByPlayer;

   public EntityPig(World par1World) {
      super(par1World);
      this.texture = "/mob/pig.png";
      this.a(0.9F, 0.9F);
      this.aC().setAvoidsWater(true);
      float var2 = 0.25F;
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
      this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.34F));
      this.tasks.addTask(3, new EntityAIMate(this, var2));
      this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrotOnAStick.itemID, false));
      this.tasks.addTask(4, new EntityAITempt(this, 0.3F, Item.carrot.itemID, false));
      this.tasks.addTask(5, new EntityAIFollowParent(this, 0.28F));
      this.tasks.addTask(6, new EntityAIWander(this, var2));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(8, new EntityAILookIdle(this));
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
   protected void updateAITasks() {
      super.bo();
   }

   @Override
   public boolean canBeSteered() {
      ItemStack var1 = ((EntityPlayer)this.riddenByEntity).getHeldItem();
      return var1 != null && var1.itemID == Item.carrotOnAStick.itemID;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("Saddle", this.getSaddled());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setSaddled(par1NBTTagCompound.getBoolean("Saddle"));
   }

   @Override
   protected String getLivingSound() {
      return "mob.pig.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.pig.say";
   }

   @Override
   protected String getDeathSound() {
      return "mob.pig.death";
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.pig.step", 0.15F, 1.0F);
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      if (super.interact(par1EntityPlayer)) {
         return true;
      } else if (!this.getSaddled() || this.worldObj.isRemote || this.riddenByEntity != null && this.riddenByEntity != par1EntityPlayer) {
         return false;
      } else {
         par1EntityPlayer.mountEntity(this);
         return true;
      }
   }

   @Override
   protected int getDropItemId() {
      return this.ae() ? Item.porkCooked.itemID : Item.porkRaw.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + par2);

      for (int var4 = 0; var4 < var3; var4++) {
         if (this.ae()) {
            this.b(Item.porkCooked.itemID, 1);
         } else {
            this.b(Item.porkRaw.itemID, 1);
         }
      }

      if (this.getSaddled()) {
         this.b(Item.saddle.itemID, 1);
      }
   }

   public boolean getSaddled() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setSaddled(boolean par1) {
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)1);
      } else {
         this.dataWatcher.updateObject(16, (byte)0);
      }
   }

   @Override
   public void onStruckByLightning(EntityLightningBolt par1EntityLightningBolt) {
      if (!this.worldObj.isRemote) {
         EntityPigZombie var2 = (EntityPigZombie)EntityList.createEntityOfType(EntityPigZombie.class, this.worldObj);
         var2.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         this.worldObj.spawnEntityInWorld(var2);
         this.w();
      }
   }

   @Override
   protected void fall(float par1) {
      super.a(par1);
      if (par1 > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
         ((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
      }
   }

   public EntityPig spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      return (EntityPig)EntityList.createEntityOfType(EntityPig.class, this.worldObj);
   }

   @Override
   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return par1ItemStack != null && par1ItemStack.itemID == Item.carrot.itemID;
   }

   public EntityAIControlledByPlayer getAIControlledByPlayer() {
      return this.aiControlledByPlayer;
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.spawnBabyAnimal(par1EntityAgeable);
   }
}
