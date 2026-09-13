package net.minecraft.src;

import java.util.Calendar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 0.25F, 20, 60, 15.0F);
   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 0.31F, false);

   public EntitySkeleton(World par1World) {
      super(par1World);
      this.texture = "/mob/skeleton.png";
      this.moveSpeed = 0.25F;
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIRestrictSun(this));
      this.tasks.addTask(3, new EntityAIFleeSun(this, this.moveSpeed));
      this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(6, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
      if (par1World != null && !par1World.isRemote) {
         this.setCombatTask();
      }
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(13, new Byte((byte)0));
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   protected String getLivingSound() {
      return "mob.skeleton.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.skeleton.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.skeleton.death";
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.skeleton.step", 0.15F, 1.0F);
   }

   @Override
   public boolean attackEntityAsMob(Entity par1Entity) {
      if (super.attackEntityAsMob(par1Entity)) {
         if (this.getSkeletonType() == 1 && par1Entity instanceof EntityLiving) {
            ((EntityLiving)par1Entity).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public int getAttackStrength(Entity par1Entity) {
      if (this.getSkeletonType() == 1) {
         ItemStack var2 = this.bG();
         int var3 = 4;
         if (var2 != null) {
            var3 += var2.getDamageVsEntity(this);
         }

         return var3;
      } else {
         return super.getAttackStrength(par1Entity);
      }
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   public void onLivingUpdate() {
      if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
         float var1 = this.c(1.0F);
         if (var1 > 0.5F
            && this.rand.nextFloat() * 30.0F < (var1 - 0.4F) * 2.0F
            && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))) {
            boolean var2 = true;
            ItemStack var3 = this.p(4);
            if (var3 != null) {
               if (var3.isItemStackDamageable()) {
                  var3.setItemDamage(var3.getItemDamageForDisplay() + this.rand.nextInt(2));
                  if (var3.getItemDamageForDisplay() >= var3.getMaxDamage()) {
                     this.a(var3);
                     this.setCurrentItemOrArmor(4, (ItemStack)null);
                  }
               }

               var2 = false;
            }

            if (var2) {
               this.d(8);
            }
         }
      }

      if (this.worldObj.isRemote && this.getSkeletonType() == 1) {
         this.a(0.72F, 2.34F);
      }

      super.onLivingUpdate();
   }

   @Override
   public void onDeath(DamageSource par1DamageSource) {
      super.a(par1DamageSource);
      if (par1DamageSource.getSourceOfDamage() instanceof EntityArrow && par1DamageSource.getEntity() instanceof EntityPlayer) {
         EntityPlayer var2 = (EntityPlayer)par1DamageSource.getEntity();
         double var3 = var2.posX - this.posX;
         double var5 = var2.posZ - this.posZ;
         if (var3 * var3 + var5 * var5 >= 2500.0) {
            var2.triggerAchievement(AchievementList.snipeSkeleton);
         }
      }
   }

   @Override
   protected int getDropItemId() {
      return Item.arrow.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      if (this.getSkeletonType() == 1) {
         int var3 = this.rand.nextInt(3 + par2) - 1;

         for (int var4 = 0; var4 < var3; var4++) {
            this.b(Item.coal.itemID, 1);
         }
      } else {
         int var3 = this.rand.nextInt(3 + par2);

         for (int var4 = 0; var4 < var3; var4++) {
            this.b(Item.arrow.itemID, 1);
         }
      }

      int var6 = this.rand.nextInt(3 + par2);

      for (int var8 = 0; var8 < var6; var8++) {
         this.b(Item.bone.itemID, 1);
      }
   }

   @Override
   protected void dropRareDrop(int par1) {
      if (this.getSkeletonType() == 1) {
         this.a(new ItemStack(Item.skull.itemID, 1, 1), 0.0F);
      }
   }

   @Override
   protected void addRandomArmor() {
      super.bH();
      this.setCurrentItemOrArmor(0, new ItemStack(Item.bow));
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.getSkeletonType() == 1 ? "/mob/skeleton_wither.png" : super.N();
   }

   @Override
   public void initCreature() {
      if (this.worldObj.provider instanceof WorldProviderHell && this.aE().nextInt(5) > 0) {
         this.tasks.addTask(4, this.aiAttackOnCollide);
         this.setSkeletonType(1);
         this.setCurrentItemOrArmor(0, new ItemStack(Item.swordStone));
      } else {
         this.tasks.addTask(4, this.aiArrowAttack);
         this.addRandomArmor();
         this.bI();
      }

      this.h(this.rand.nextFloat() < au[this.worldObj.difficultySetting]);
      if (this.p(4) == null) {
         Calendar var1 = this.worldObj.getCurrentDate();
         if (var1.get(2) + 1 == 10 && var1.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
            this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Block.pumpkinLantern : Block.pumpkin));
            this.equipmentDropChances[4] = 0.0F;
         }
      }
   }

   public void setCombatTask() {
      this.tasks.removeTask(this.aiAttackOnCollide);
      this.tasks.removeTask(this.aiArrowAttack);
      ItemStack var1 = this.bG();
      if (var1 != null && var1.itemID == Item.bow.itemID) {
         this.tasks.addTask(4, this.aiArrowAttack);
      } else {
         this.tasks.addTask(4, this.aiAttackOnCollide);
      }
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
      EntityArrow var3 = (EntityArrow)EntityList.createEntityOfType(
         EntityArrow.class, this.worldObj, this, par1EntityLiving, 1.6F, (float)(14 - this.worldObj.difficultySetting * 4)
      );
      int var4 = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.bG());
      int var5 = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.bG());
      var3.setDamage(par2 * 2.0F + this.rand.nextGaussian() * 0.25 + this.worldObj.difficultySetting * 0.11F);
      if (var4 > 0) {
         var3.setDamage(var3.getDamage() + var4 * 0.5 + 0.5);
      }

      if (var5 > 0) {
         var3.setKnockbackStrength(var5);
      }

      if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.bG()) > 0 || this.getSkeletonType() == 1) {
         var3.d(100);
      }

      this.a("random.bow", 1.0F, 1.0F / (this.aE().nextFloat() * 0.4F + 0.8F));
      this.worldObj.spawnEntityInWorld(var3);
   }

   public int getSkeletonType() {
      return this.dataWatcher.getWatchableObjectByte(13);
   }

   public void setSkeletonType(int par1) {
      this.dataWatcher.updateObject(13, (byte)par1);
      this.isImmuneToFire = par1 == 1;
      if (par1 == 1) {
         this.a(0.72F, 2.34F);
      } else {
         this.a(0.6F, 1.8F);
      }
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      if (par1NBTTagCompound.hasKey("SkeletonType")) {
         byte var2 = par1NBTTagCompound.getByte("SkeletonType");
         this.setSkeletonType(var2);
      }

      this.setCombatTask();
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      par1NBTTagCompound.setByte("SkeletonType", (byte)this.getSkeletonType());
   }

   @Override
   public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
      super.c(par1, par2ItemStack);
      if (!this.worldObj.isRemote && par1 == 0) {
         this.setCombatTask();
      }
   }
}
