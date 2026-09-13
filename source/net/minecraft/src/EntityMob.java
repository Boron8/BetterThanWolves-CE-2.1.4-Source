package net.minecraft.src;

public abstract class EntityMob extends EntityCreature implements IMob {
   public EntityMob(World par1World) {
      super(par1World);
      this.experienceValue = 5;
   }

   @Override
   public void onLivingUpdate() {
      this.entityMobOnLivingUpdate();
   }

   public void entityMobOnLivingUpdate() {
      this.br();
      float var1 = this.c(1.0F);
      if (var1 > 0.5F) {
         this.entityAge += 2;
      }

      super.c();
   }

   @Override
   public void onUpdate() {
      super.l_();
      if (!this.worldObj.isRemote && this.worldObj.difficultySetting == 0) {
         this.w();
      }
   }

   @Override
   protected Entity findPlayerToAttack() {
      EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0);
      return var1 != null && this.n(var1) ? var1 : null;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      return this.entityMobAttackEntityFrom(par1DamageSource, par2);
   }

   public boolean entityMobAttackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if (super.a(par1DamageSource, par2)) {
         Entity var3 = par1DamageSource.getEntity();
         if (this.riddenByEntity != var3 && this.ridingEntity != var3) {
            if (var3 != this && var3 != null) {
               this.entityToAttack = var3;
            }

            return true;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   protected void attackEntity(Entity par1Entity, float par2) {
      this.entityMobAttackEntity(par1Entity, par2);
   }

   protected void entityMobAttackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
         this.attackTime = 20;
         this.attackEntityAsMob(par1Entity);
      }
   }

   @Override
   public float getBlockPathWeight(int par1, int par2, int par3) {
      return 0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
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
      return this.isValidLightLevel() && super.getCanSpawnHere() && this.canSpawnOnBlockBelow();
   }

   public int getAttackStrength(Entity par1Entity) {
      return 2;
   }

   @Override
   public int getMeleeAttackStrength(Entity target) {
      return this.getAttackStrength(target);
   }

   protected boolean canSpawnOnBlockBelow() {
      int i = MathHelper.floor_double(this.posX);
      int j = (int)this.boundingBox.minY - 1;
      int k = MathHelper.floor_double(this.posZ);
      return this.canSpawnOnBlock(i, j, k);
   }

   protected boolean canSpawnOnBlock(int x, int y, int z) {
      Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];
      return block != null && !block.isLeafBlock(this.worldObj, x, y, z);
   }

   protected void checkForCatchFireInSun() {
      if (!this.worldObj.isRemote
         && this.worldObj.isDaytime()
         && !this.worldObj.isRainingAtPos((int)this.posX, (int)this.posY, (int)this.posZ)
         && !this.h_()
         && !this.inWater) {
         float fBrightness = this.c(1.0F);
         if (fBrightness > 0.5F
            && this.rand.nextFloat() * 30.0F < (fBrightness - 0.4F) * 2.0F
            && this.worldObj
               .canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY + this.e()), MathHelper.floor_double(this.posZ))) {
            int iBlockBelowID = this.worldObj
               .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.1F), MathHelper.floor_double(this.posZ));
            Block blockBelow = Block.blocksList[iBlockBelowID];
            if (blockBelow == null || blockBelow.blockMaterial != Material.water) {
               ItemStack headStack = this.p(4);
               if (headStack == null && !this.hasHeadCrabbedSquid()) {
                  this.d(8);
               }
            }
         }
      }
   }
}
