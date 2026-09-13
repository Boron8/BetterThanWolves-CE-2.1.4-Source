package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityWolf extends EntityTameable {
   private float field_70926_e;
   private float field_70924_f;
   private boolean isShaking;
   private boolean field_70928_h;
   private float timeWolfIsShaking;
   private float prevTimeWolfIsShaking;

   public EntityWolf(World par1World) {
      super(par1World);
      this.texture = "/mob/wolf.png";
      this.a(0.6F, 0.8F);
      this.moveSpeed = 0.3F;
      this.aC().setAvoidsWater(true);
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, this.aiSit);
      this.tasks.addTask(3, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(4, new EntityAIAttackOnCollide(this, this.moveSpeed, true));
      this.tasks.addTask(5, new EntityAIFollowOwner(this, this.moveSpeed, 10.0F, 2.0F));
      this.tasks.addTask(6, new EntityAIMate(this, this.moveSpeed));
      this.tasks.addTask(7, new EntityAIWander(this, this.moveSpeed));
      this.tasks.addTask(8, new EntityAIBeg(this, 8.0F));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(9, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
      this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
      this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(4, new EntityAITargetNonTamed(this, EntitySheep.class, 16.0F, 200, false));
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public void setAttackTarget(EntityLiving par1EntityLiving) {
      super.b(par1EntityLiving);
      if (par1EntityLiving instanceof EntityPlayer) {
         this.setAngry(true);
      }
   }

   @Override
   protected void updateAITick() {
      this.dataWatcher.updateObject(18, this.aX());
   }

   @Override
   public int getMaxHealth() {
      return this.m() ? 20 : 8;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(18, new Integer(this.aX()));
      this.dataWatcher.addObject(19, new Byte((byte)0));
      this.dataWatcher.addObject(20, new Byte((byte)BlockCloth.getBlockFromDye(1)));
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.wolf.step", 0.15F, 1.0F);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.m() ? "/mob/wolf_tame.png" : (this.isAngry() ? "/mob/wolf_angry.png" : super.N());
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("Angry", this.isAngry());
      par1NBTTagCompound.setByte("CollarColor", (byte)this.getCollarColor());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setAngry(par1NBTTagCompound.getBoolean("Angry"));
      if (par1NBTTagCompound.hasKey("CollarColor")) {
         this.setCollarColor(par1NBTTagCompound.getByte("CollarColor"));
      }
   }

   @Override
   protected boolean canDespawn() {
      return this.isAngry() && !this.m();
   }

   @Override
   protected String getLivingSound() {
      return this.isAngry()
         ? "mob.wolf.growl"
         : (this.rand.nextInt(3) == 0 ? (this.m() && this.dataWatcher.getWatchableObjectInt(18) < 10 ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
   }

   @Override
   protected String getHurtSound() {
      return "mob.wolf.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.wolf.death";
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F;
   }

   @Override
   protected int getDropItemId() {
      return -1;
   }

   @Override
   public void onLivingUpdate() {
      super.c();
      if (!this.worldObj.isRemote && this.isShaking && !this.field_70928_h && !this.k() && this.onGround) {
         this.field_70928_h = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
         this.worldObj.setEntityState(this, (byte)8);
      }
   }

   @Override
   public void onUpdate() {
      super.l_();
      this.field_70924_f = this.field_70926_e;
      if (this.func_70922_bv()) {
         this.field_70926_e = this.field_70926_e + (1.0F - this.field_70926_e) * 0.4F;
      } else {
         this.field_70926_e = this.field_70926_e + (0.0F - this.field_70926_e) * 0.4F;
      }

      if (this.func_70922_bv()) {
         this.numTicksToChaseTarget = 10;
      }

      if (this.F()) {
         this.isShaking = true;
         this.field_70928_h = false;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
      } else if ((this.isShaking || this.field_70928_h) && this.field_70928_h) {
         if (this.timeWolfIsShaking == 0.0F) {
            this.a("mob.wolf.shake", this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }

         this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
         this.timeWolfIsShaking += 0.05F;
         if (this.prevTimeWolfIsShaking >= 2.0F) {
            this.isShaking = false;
            this.field_70928_h = false;
            this.prevTimeWolfIsShaking = 0.0F;
            this.timeWolfIsShaking = 0.0F;
         }

         if (this.timeWolfIsShaking > 0.4F) {
            float var1 = (float)this.boundingBox.minY;
            int var2 = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * (float) Math.PI) * 7.0F);

            for (int var3 = 0; var3 < var2; var3++) {
               float var4 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
               float var5 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
               this.worldObj.spawnParticle("splash", this.posX + var4, var1 + 0.8F, this.posZ + var5, this.motionX, this.motionY, this.motionZ);
            }
         }
      }
   }

   @Environment(EnvType.CLIENT)
   public boolean getWolfShaking() {
      return this.isShaking;
   }

   @Environment(EnvType.CLIENT)
   public float getShadingWhileShaking(float par1) {
      return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1) / 2.0F * 0.25F;
   }

   @Environment(EnvType.CLIENT)
   public float getShakeAngle(float par1, float par2) {
      float var3 = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * par1 + par2) / 1.8F;
      if (var3 < 0.0F) {
         var3 = 0.0F;
      } else if (var3 > 1.0F) {
         var3 = 1.0F;
      }

      return MathHelper.sin(var3 * (float) Math.PI) * MathHelper.sin(var3 * (float) Math.PI * 11.0F) * 0.15F * (float) Math.PI;
   }

   @Environment(EnvType.CLIENT)
   public float getInterestedAngle(float par1) {
      return (this.field_70924_f + (this.field_70926_e - this.field_70924_f) * par1) * 0.15F * (float) Math.PI;
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.8F;
   }

   @Override
   public int getVerticalFaceSpeed() {
      return this.n() ? 20 : super.bs();
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         Entity var3 = par1DamageSource.getEntity();
         this.aiSit.setSitting(false);
         if (var3 != null && !(var3 instanceof EntityPlayer) && !(var3 instanceof EntityArrow)) {
            par2 = (par2 + 1) / 2;
         }

         return super.a(par1DamageSource, par2);
      }
   }

   @Override
   public boolean attackEntityAsMob(Entity par1Entity) {
      int var2 = this.m() ? 4 : 2;
      return par1Entity.attackEntityFrom(DamageSource.causeMobDamage(this), var2);
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.inventory.getCurrentItem();
      if (this.m()) {
         if (var2 != null) {
            if (Item.itemsList[var2.itemID] instanceof ItemFood) {
               ItemFood var3 = (ItemFood)Item.itemsList[var2.itemID];
               if (var3.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectInt(18) < 20) {
                  if (!par1EntityPlayer.capabilities.isCreativeMode) {
                     var2.stackSize--;
                  }

                  this.j(var3.getHealAmount());
                  if (var2.stackSize <= 0) {
                     par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                  }

                  return true;
               }
            } else if (var2.itemID == Item.dyePowder.itemID) {
               int var4 = BlockCloth.getBlockFromDye(var2.getItemDamage());
               if (var4 != this.getCollarColor()) {
                  this.setCollarColor(var4);
                  if (!par1EntityPlayer.capabilities.isCreativeMode && --var2.stackSize <= 0) {
                     par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
                  }

                  return true;
               }
            }
         }

         if (par1EntityPlayer.username.equalsIgnoreCase(this.o()) && !this.worldObj.isRemote && !this.isBreedingItem(var2)) {
            this.aiSit.setSitting(!this.n());
            this.isJumping = false;
            this.a((PathEntity)null);
         }
      } else if (var2 != null && var2.itemID == Item.bone.itemID && !this.isAngry()) {
         if (!par1EntityPlayer.capabilities.isCreativeMode) {
            var2.stackSize--;
         }

         if (var2.stackSize <= 0) {
            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
         }

         if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(3) == 0) {
               this.j(true);
               this.a((PathEntity)null);
               this.setAttackTarget((EntityLiving)null);
               this.aiSit.setSitting(true);
               this.b(20);
               this.a(par1EntityPlayer.username);
               this.i(true);
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

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 8) {
         this.field_70928_h = true;
         this.timeWolfIsShaking = 0.0F;
         this.prevTimeWolfIsShaking = 0.0F;
      } else {
         super.handleHealthUpdate(par1);
      }
   }

   @Environment(EnvType.CLIENT)
   public float getTailRotation() {
      return this.isAngry()
         ? 1.5393804F
         : (this.m() ? (0.55F - (20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * (float) Math.PI : (float) (Math.PI / 5));
   }

   @Override
   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return par1ItemStack == null
         ? false
         : (!(Item.itemsList[par1ItemStack.itemID] instanceof ItemFood) ? false : ((ItemFood)Item.itemsList[par1ItemStack.itemID]).isWolfsFavoriteMeat());
   }

   @Override
   public int getMaxSpawnedInChunk() {
      return 8;
   }

   public boolean isAngry() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 2) != 0;
   }

   public void setAngry(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         this.dataWatcher.updateObject(16, (byte)(var2 | 2));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var2 & -3));
      }
   }

   public int getCollarColor() {
      return this.dataWatcher.getWatchableObjectByte(20) & 15;
   }

   public void setCollarColor(int par1) {
      this.dataWatcher.updateObject(20, (byte)(par1 & 15));
   }

   public EntityWolf spawnBabyAnimal(EntityAgeable par1EntityAgeable) {
      EntityWolf var2 = (EntityWolf)EntityList.createEntityOfType(EntityWolf.class, this.worldObj);
      String var3 = this.o();
      if (var3 != null && var3.trim().length() > 0) {
         var2.a(var3);
         var2.j(true);
      }

      return var2;
   }

   public void func_70918_i(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(19);
      if (par1) {
         this.dataWatcher.updateObject(19, (byte)1);
      } else {
         this.dataWatcher.updateObject(19, (byte)0);
      }
   }

   @Override
   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      if (par1EntityAnimal == this) {
         return false;
      } else if (!this.m()) {
         return false;
      } else if (!(par1EntityAnimal instanceof EntityWolf)) {
         return false;
      } else {
         EntityWolf var2 = (EntityWolf)par1EntityAnimal;
         return !var2.m() ? false : (var2.n() ? false : this.r() && var2.r());
      }
   }

   public boolean func_70922_bv() {
      return this.dataWatcher.getWatchableObjectByte(19) == 1;
   }

   @Override
   public EntityAgeable createChild(EntityAgeable par1EntityAgeable) {
      return this.spawnBabyAnimal(par1EntityAgeable);
   }
}
