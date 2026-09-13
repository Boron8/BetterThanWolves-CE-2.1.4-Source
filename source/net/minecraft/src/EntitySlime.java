package net.minecraft.src;

public class EntitySlime extends EntityLiving implements IMob {
   private static final float[] spawnChances = new float[]{1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
   public float field_70813_a;
   public float field_70811_b;
   public float field_70812_c;
   private int slimeJumpDelay = 0;

   public EntitySlime(World par1World) {
      super(par1World);
      this.texture = "/mob/slime.png";
      int var2 = 1 << this.rand.nextInt(3);
      this.yOffset = 0.0F;
      this.slimeJumpDelay = this.rand.nextInt(20) + 10;
      this.setSlimeSize(var2);
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, new Byte((byte)1));
   }

   protected void setSlimeSize(int par1) {
      this.dataWatcher.updateObject(16, new Byte((byte)par1));
      this.a(0.6F * par1, 0.6F * par1);
      this.b(this.posX, this.posY, this.posZ);
      this.b(this.getMaxHealth());
      this.experienceValue = par1;
   }

   @Override
   public int getMaxHealth() {
      int var1 = this.getSlimeSize();
      return var1 * var1;
   }

   public int getSlimeSize() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Size", this.getSlimeSize() - 1);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      this.setSlimeSize(par1NBTTagCompound.getInteger("Size") + 1);
   }

   protected String getSlimeParticle() {
      return "slime";
   }

   protected String getJumpSound() {
      return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
   }

   @Override
   public void onUpdate() {
      if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0 && this.getSlimeSize() > 0) {
         this.isDead = true;
      }

      this.field_70811_b = this.field_70811_b + (this.field_70813_a - this.field_70811_b) * 0.5F;
      this.field_70812_c = this.field_70811_b;
      boolean var1 = this.onGround;
      super.onUpdate();
      if (this.onGround && !var1) {
         int var2 = this.getSlimeSize();

         for (int var3 = 0; var3 < var2 * 8; var3++) {
            float var4 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
            float var5 = this.rand.nextFloat() * 0.5F + 0.5F;
            float var6 = MathHelper.sin(var4) * var2 * 0.5F * var5;
            float var7 = MathHelper.cos(var4) * var2 * 0.5F * var5;
            this.worldObj.spawnParticle(this.getSlimeParticle(), this.posX + var6, this.boundingBox.minY, this.posZ + var7, 0.0, 0.0, 0.0);
         }

         if (this.makesSoundOnLand()) {
            this.a(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
         }

         this.field_70813_a = -0.5F;
      } else if (!this.onGround && var1) {
         this.field_70813_a = 1.0F;
      }

      this.func_70808_l();
      if (this.worldObj.isRemote) {
         int var2 = this.getSlimeSize();
         this.a(0.6F * var2, 0.6F * var2);
      }
   }

   @Override
   protected void updateEntityActionState() {
      this.bn();
      EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0);
      if (var1 != null) {
         this.a(var1, 10.0F, 20.0F);
      }

      if (this.onGround && this.slimeJumpDelay-- <= 0) {
         this.slimeJumpDelay = this.getJumpDelay();
         if (var1 != null) {
            this.slimeJumpDelay /= 3;
         }

         this.isJumping = true;
         if (this.makesSoundOnJump()) {
            this.a(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
         }

         this.moveStrafing = 1.0F - this.rand.nextFloat() * 2.0F;
         this.moveForward = 1 * this.getSlimeSize();
      } else {
         this.isJumping = false;
         if (this.onGround) {
            this.moveStrafing = this.moveForward = 0.0F;
         }
      }
   }

   protected void func_70808_l() {
      this.field_70813_a *= 0.6F;
   }

   protected int getJumpDelay() {
      return this.rand.nextInt(20) + 10;
   }

   protected EntitySlime createInstance() {
      return (EntitySlime)EntityList.createEntityOfType(EntitySlime.class, this.worldObj);
   }

   @Override
   public void setDead() {
      int var1 = this.getSlimeSize();
      if (!this.worldObj.isRemote && var1 > 1 && this.aX() <= 0) {
         int var2 = 2 + this.rand.nextInt(3);

         for (int var3 = 0; var3 < var2; var3++) {
            float var4 = (var3 % 2 - 0.5F) * var1 / 40.0F;
            float var5 = (var3 / 2 - 0.5F) * var1 / 40.0F;
            EntitySlime var6 = this.createInstance();
            var6.setSlimeSize(var1 / 2);
            var6.b(this.posX + var4, this.posY + 0.5, this.posZ + var5, this.rand.nextFloat() * 360.0F, 0.0F);
            this.worldObj.spawnEntityInWorld(var6);
         }
      }

      super.w();
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
      if (this.canDamagePlayer()) {
         int var2 = this.getSlimeSize();
         if (this.n(par1EntityPlayer)
            && this.e(par1EntityPlayer) < 0.6 * var2 * 0.6 * var2
            && par1EntityPlayer.attackEntityFrom(DamageSource.causeMobDamage(this), this.getAttackStrength())) {
            this.a("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         }
      }
   }

   protected boolean canDamagePlayer() {
      return this.getSlimeSize() > 1;
   }

   protected int getAttackStrength() {
      return this.getSlimeSize();
   }

   @Override
   protected String getHurtSound() {
      return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
   }

   @Override
   protected String getDeathSound() {
      return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
   }

   @Override
   protected int getDropItemId() {
      return this.getSlimeSize() == 1 ? Item.slimeBall.itemID : 0;
   }

   protected boolean isValidLightLevel() {
      int x = MathHelper.floor_double(this.posX);
      int y = MathHelper.floor_double(this.boundingBox.minY);
      int z = MathHelper.floor_double(this.posZ);
      if (this.worldObj.getSavedLightValue(EnumSkyBlock.Sky, x, y, z) > this.rand.nextInt(32)) {
         return false;
      } else {
         int blockLightValue = this.worldObj.getBlockLightValueNoSky(x, y, z);
         if (blockLightValue > 0) {
            return false;
         } else {
            int naturalLightValue = this.worldObj.getBlockNaturalLightValue(x, y, z);
            if (this.worldObj.isThundering()) {
               naturalLightValue = Math.min(naturalLightValue, 5);
            }

            return naturalLightValue <= this.rand.nextInt(8);
         }
      }
   }

   @Override
   public boolean getCanSpawnHere() {
      Chunk var1 = this.worldObj.getChunkFromBlockCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
      if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
         return false;
      } else {
         if (this.getSlimeSize() == 1 || this.worldObj.difficultySetting > 0) {
            BiomeGenBase var2 = this.worldObj.getBiomeGenForCoords(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posZ));
            if (var2.canSlimesSpawnOnSurface()
               && this.posY > 50.0
               && this.posY < 70.0
               && this.rand.nextFloat() < 0.5F
               && this.rand.nextFloat() < spawnChances[this.worldObj.getMoonPhase()]
               && this.isValidLightLevel()) {
               return super.getCanSpawnHere();
            }

            if (this.rand.nextInt(10) == 0 && var1.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
               return super.getCanSpawnHere();
            }
         }

         return false;
      }
   }

   @Override
   protected float getSoundVolume() {
      return 0.4F * this.getSlimeSize();
   }

   @Override
   public int getVerticalFaceSpeed() {
      return 0;
   }

   protected boolean makesSoundOnJump() {
      return this.getSlimeSize() > 0;
   }

   protected boolean makesSoundOnLand() {
      return this.getSlimeSize() > 2;
   }
}
