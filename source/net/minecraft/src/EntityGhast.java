package net.minecraft.src;

public class EntityGhast extends EntityFlying implements IMob {
   public int courseChangeCooldown = 0;
   public double waypointX;
   public double waypointY;
   public double waypointZ;
   private Entity targetedEntity = null;
   private int aggroCooldown = 0;
   public int prevAttackCounter = 0;
   public int attackCounter = 0;
   private int explosionStrength = 1;

   public EntityGhast(World par1World) {
      super(par1World);
      this.texture = "/mob/ghast.png";
      this.a(4.0F, 4.0F);
      this.isImmuneToFire = true;
      this.experienceValue = 5;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if ("fireball".equals(par1DamageSource.getDamageType()) && par1DamageSource.getEntity() instanceof EntityPlayer) {
         super.a(par1DamageSource, 1000);
         ((EntityPlayer)par1DamageSource.getEntity()).triggerAchievement(AchievementList.ghast);
         return true;
      } else {
         return super.a(par1DamageSource, par2);
      }
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, (byte)0);
   }

   @Override
   public int getMaxHealth() {
      return 10;
   }

   @Override
   public void onUpdate() {
      super.l_();
      byte var1 = this.dataWatcher.getWatchableObjectByte(16);
      this.texture = var1 == 1 ? "/mob/ghast_fire.png" : "/mob/ghast.png";
   }

   @Override
   protected void updateEntityActionState() {
      if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
         this.w();
      }

      this.bn();
      this.prevAttackCounter = this.attackCounter;
      double var1 = this.waypointX - this.posX;
      double var3 = this.waypointY - this.posY;
      double var5 = this.waypointZ - this.posZ;
      double var7 = var1 * var1 + var3 * var3 + var5 * var5;
      if (var7 < 1.0 || var7 > 3600.0) {
         this.waypointX = this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
         this.waypointY = this.posY + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
         this.waypointZ = this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
      }

      if (this.courseChangeCooldown-- <= 0) {
         this.courseChangeCooldown = this.courseChangeCooldown + this.rand.nextInt(5) + 2;
         var7 = MathHelper.sqrt_double(var7);
         if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, var7)) {
            this.motionX += var1 / var7 * 0.1;
            this.motionY += var3 / var7 * 0.1;
            this.motionZ += var5 / var7 * 0.1;
         } else {
            this.waypointX = this.posX;
            this.waypointY = this.posY;
            this.waypointZ = this.posZ;
         }
      }

      if (this.targetedEntity != null && this.targetedEntity.isDead) {
         this.targetedEntity = null;
      }

      if (this.targetedEntity == null || this.aggroCooldown-- <= 0) {
         this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0);
         if (this.targetedEntity != null) {
            this.aggroCooldown = 20;
         }
      }

      double var9 = 64.0;
      if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < var9 * var9) {
         double var11 = this.targetedEntity.posX - this.posX;
         double var13 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F);
         double var15 = this.targetedEntity.posZ - this.posZ;
         this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(var11, var15)) * 180.0F / (float) Math.PI;
         if (this.n(this.targetedEntity)) {
            if (this.attackCounter == 10) {
               this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1007, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
            }

            this.attackCounter++;
            if (this.attackCounter == 20) {
               this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
               EntityLargeFireball var17 = (EntityLargeFireball)EntityList.createEntityOfType(
                  EntityLargeFireball.class, this.worldObj, this, var11, var13, var15
               );
               var17.field_92057_e = this.explosionStrength;
               double var18 = 4.0;
               Vec3 var20 = this.i(1.0F);
               var17.posX = this.posX + var20.xCoord * var18;
               var17.posY = this.posY + this.height / 2.0F + 0.5;
               var17.posZ = this.posZ + var20.zCoord * var18;
               this.worldObj.spawnEntityInWorld(var17);
               this.attackCounter = -40;
            }
         } else if (this.attackCounter > 0) {
            this.attackCounter--;
         }
      } else {
         this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float) Math.PI;
         if (this.attackCounter > 0) {
            this.attackCounter--;
         }
      }

      if (!this.worldObj.isRemote) {
         byte var21 = this.dataWatcher.getWatchableObjectByte(16);
         byte var12 = (byte)(this.attackCounter > 10 ? 1 : 0);
         if (var21 != var12) {
            this.dataWatcher.updateObject(16, var12);
         }
      }
   }

   private boolean isCourseTraversable(double par1, double par3, double par5, double par7) {
      double var9 = (this.waypointX - this.posX) / par7;
      double var11 = (this.waypointY - this.posY) / par7;
      double var13 = (this.waypointZ - this.posZ) / par7;
      AxisAlignedBB var15 = this.boundingBox.copy();

      for (int var16 = 1; var16 < par7; var16++) {
         var15.offset(var9, var11, var13);
         if (!this.worldObj.getCollidingBoundingBoxes(this, var15).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   protected String getLivingSound() {
      return "mob.ghast.moan";
   }

   @Override
   protected String getHurtSound() {
      return "mob.ghast.scream";
   }

   @Override
   protected String getDeathSound() {
      return "mob.ghast.death";
   }

   @Override
   protected int getDropItemId() {
      return Item.gunpowder.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.rand.nextInt(2) + this.rand.nextInt(1 + par2);

      for (int var4 = 0; var4 < var3; var4++) {
         this.b(Item.ghastTear.itemID, 1);
      }

      var3 = this.rand.nextInt(3) + this.rand.nextInt(1 + par2);

      for (int var6 = 0; var6 < var3; var6++) {
         this.b(Item.gunpowder.itemID, 1);
      }
   }

   @Override
   protected float getSoundVolume() {
      return 10.0F;
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.rand.nextInt(20) == 0 && super.bv() && this.worldObj.difficultySetting > 0;
   }

   @Override
   public int getMaxSpawnedInChunk() {
      return 1;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("ExplosionPower", this.explosionStrength);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      if (par1NBTTagCompound.hasKey("ExplosionPower")) {
         this.explosionStrength = par1NBTTagCompound.getInteger("ExplosionPower");
      }
   }
}
