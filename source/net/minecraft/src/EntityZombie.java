package net.minecraft.src;

import btw.item.BTWItems;
import java.util.Calendar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityZombie extends EntityMob {
   private int conversionTime = 0;

   public EntityZombie(World par1World) {
      super(par1World);
      this.texture = "/mob/zombie.png";
      this.moveSpeed = 0.23F;
      this.aC().setBreakDoors(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIBreakDoor(this));
      this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, this.moveSpeed, false));
      this.tasks.addTask(3, new EntityAIAttackOnCollide(this, EntityVillager.class, this.moveSpeed, true));
      this.tasks.addTask(4, new EntityAIMoveTwardsRestriction(this, this.moveSpeed));
      this.tasks.addTask(5, new EntityAIMoveThroughVillage(this, this.moveSpeed, false));
      this.tasks.addTask(6, new EntityAIWander(this, this.moveSpeed));
      this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityVillager.class, 16.0F, 0, false));
   }

   @Override
   protected int func_96121_ay() {
      return 40;
   }

   @Override
   public float getSpeedModifier() {
      return super.bE() * (this.isChild() ? 1.5F : 1.0F);
   }

   @Override
   protected void entityInit() {
      super.a();
      this.u().addObject(12, (byte)0);
      this.u().addObject(13, (byte)0);
      this.u().addObject(14, (byte)0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.isVillager() ? "/mob/zombie_villager.png" : "/mob/zombie.png";
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   public int getTotalArmorValue() {
      int var1 = super.aZ() + 2;
      if (var1 > 20) {
         var1 = 20;
      }

      return var1;
   }

   @Override
   protected boolean isAIEnabled() {
      return true;
   }

   @Override
   public boolean isChild() {
      return this.u().getWatchableObjectByte(12) == 1;
   }

   public void setChild(boolean par1) {
      this.u().updateObject(12, (byte)1);
   }

   public boolean isVillager() {
      return this.u().getWatchableObjectByte(13) == 1;
   }

   public void setVillager(boolean par1) {
      this.u().updateObject(13, (byte)(par1 ? 1 : 0));
   }

   @Override
   public void onLivingUpdate() {
      if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !this.isChild()) {
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
                     this.c(4, (ItemStack)null);
                  }
               }

               var2 = false;
            }

            if (var2) {
               this.d(8);
            }
         }
      }

      super.onLivingUpdate();
   }

   @Override
   public void onUpdate() {
      if (!this.worldObj.isRemote && this.isConverting()) {
         int var1 = this.getConversionTimeBoost();
         this.conversionTime -= var1;
         if (this.conversionTime <= 0) {
            this.convertToVillager();
         }
      }

      super.onUpdate();
   }

   @Override
   public boolean attackEntityAsMob(Entity par1Entity) {
      boolean var2 = super.attackEntityAsMob(par1Entity);
      if (var2 && this.bG() == null && this.ae() && this.rand.nextFloat() < this.worldObj.difficultySetting * 0.3F) {
         par1Entity.setFire(2 * this.worldObj.difficultySetting);
      }

      return var2;
   }

   @Override
   public int getAttackStrength(Entity par1Entity) {
      ItemStack var2 = this.bG();
      float var3 = (float)(this.getMaxHealth() - this.aX()) / this.getMaxHealth();
      int var4 = 3 + MathHelper.floor_float(var3 * 4.0F);
      if (var2 != null) {
         var4 += var2.getDamageVsEntity(this);
      }

      return var4;
   }

   @Override
   protected String getLivingSound() {
      return "mob.zombie.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.zombie.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.zombie.death";
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.zombie.step", 0.15F, 1.0F);
   }

   @Override
   protected int getDropItemId() {
      return this.isVillager() ? BTWItems.rawMysteryMeat.itemID : Item.rottenFlesh.itemID;
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   protected void dropRareDrop(int par1) {
      switch (this.rand.nextInt(3)) {
         case 0:
            this.b(Item.ingotIron.itemID, 1);
            break;
         case 1:
            this.b(Item.carrot.itemID, 1);
            break;
         case 2:
            this.b(Item.potato.itemID, 1);
      }
   }

   @Override
   protected void addRandomArmor() {
      super.bH();
      if (this.rand.nextFloat() < (this.worldObj.difficultySetting == 3 ? 0.05F : 0.01F)) {
         int var1 = this.rand.nextInt(3);
         if (var1 == 0) {
            this.c(0, new ItemStack(Item.swordIron));
         } else {
            this.c(0, new ItemStack(Item.shovelIron));
         }
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      if (this.isChild()) {
         par1NBTTagCompound.setBoolean("IsBaby", true);
      }

      if (this.isVillager()) {
         par1NBTTagCompound.setBoolean("IsVillager", true);
      }

      par1NBTTagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      if (par1NBTTagCompound.getBoolean("IsBaby")) {
         this.setChild(true);
      }

      if (par1NBTTagCompound.getBoolean("IsVillager")) {
         this.setVillager(true);
      }

      if (par1NBTTagCompound.hasKey("ConversionTime") && par1NBTTagCompound.getInteger("ConversionTime") > -1) {
         this.startConversion(par1NBTTagCompound.getInteger("ConversionTime"));
      }
   }

   @Override
   public void onKillEntity(EntityLiving par1EntityLiving) {
      super.a(par1EntityLiving);
      if (this.worldObj.difficultySetting >= 2 && par1EntityLiving instanceof EntityVillager) {
         if (this.worldObj.difficultySetting == 2 && this.rand.nextBoolean()) {
            return;
         }

         EntityZombie var2 = (EntityZombie)EntityList.createEntityOfType(EntityZombie.class, this.worldObj);
         var2.k(par1EntityLiving);
         this.worldObj.removeEntity(par1EntityLiving);
         var2.initCreature();
         var2.setVillager(true);
         if (par1EntityLiving.isChild()) {
            var2.setChild(true);
         }

         this.worldObj.spawnEntityInWorld(var2);
         this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1016, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      }
   }

   @Override
   public void initCreature() {
      this.h(this.rand.nextFloat() < au[this.worldObj.difficultySetting]);
      if (this.worldObj.rand.nextFloat() < 0.05F) {
         this.setVillager(true);
      }

      this.addRandomArmor();
      this.bI();
      if (this.p(4) == null) {
         Calendar var1 = this.worldObj.getCurrentDate();
         if (var1.get(2) + 1 == 10 && var1.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
            this.c(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Block.pumpkinLantern : Block.pumpkin));
            this.equipmentDropChances[4] = 0.0F;
         }
      }
   }

   @Override
   public boolean interact(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.getCurrentEquippedItem();
      if (var2 != null && var2.getItem() == Item.appleGold && var2.getItemDamage() == 0 && this.isVillager() && this.a(Potion.weakness)) {
         if (!par1EntityPlayer.capabilities.isCreativeMode) {
            var2.stackSize--;
         }

         if (var2.stackSize <= 0) {
            par1EntityPlayer.inventory.setInventorySlotContents(par1EntityPlayer.inventory.currentItem, (ItemStack)null);
         }

         if (!this.worldObj.isRemote) {
            this.startConversion(this.rand.nextInt(2401) + 3600);
         }

         return true;
      } else {
         return false;
      }
   }

   protected void startConversion(int par1) {
      this.conversionTime = par1;
      this.u().updateObject(14, (byte)1);
      this.o(Potion.weakness.id);
      this.d(new PotionEffect(Potion.damageBoost.id, par1, Math.min(this.worldObj.difficultySetting - 1, 0)));
      this.worldObj.setEntityState(this, (byte)16);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 16) {
         this.worldObj
            .playSound(
               this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false
            );
      } else {
         super.a(par1);
      }
   }

   public boolean isConverting() {
      return this.u().getWatchableObjectByte(14) == 1;
   }

   protected void convertToVillager() {
      EntityVillager var1 = (EntityVillager)EntityList.createEntityOfType(EntityVillager.class, this.worldObj);
      var1.k(this);
      var1.initCreature();
      var1.func_82187_q();
      if (this.isChild()) {
         var1.a(-24000);
      }

      this.worldObj.removeEntity(this);
      this.worldObj.spawnEntityInWorld(var1);
      var1.d(new PotionEffect(Potion.confusion.id, 200, 0));
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
   }

   protected int getConversionTimeBoost() {
      int var1 = 1;
      if (this.rand.nextFloat() < 0.01F) {
         int var2 = 0;

         for (int var3 = (int)this.posX - 4; var3 < (int)this.posX + 4 && var2 < 14; var3++) {
            for (int var4 = (int)this.posY - 4; var4 < (int)this.posY + 4 && var2 < 14; var4++) {
               for (int var5 = (int)this.posZ - 4; var5 < (int)this.posZ + 4 && var2 < 14; var5++) {
                  int var6 = this.worldObj.getBlockId(var3, var4, var5);
                  if (var6 == Block.fenceIron.blockID || var6 == Block.bed.blockID) {
                     if (this.rand.nextFloat() < 0.3F) {
                        var1++;
                     }

                     var2++;
                  }
               }
            }
         }
      }

      return var1;
   }
}
