package net.minecraft.src;

public class EntityEnderman extends EntityMob {
   private static boolean[] carriableBlocks = new boolean[256];
   private int teleportDelay = 0;
   private int field_70826_g = 0;
   private boolean field_104003_g;

   public EntityEnderman(World par1World) {
      super(par1World);
      this.texture = "/mob/enderman.png";
      this.moveSpeed = 0.2F;
      this.a(0.6F, 2.9F);
      this.stepHeight = 1.0F;
   }

   @Override
   public int getMaxHealth() {
      return 40;
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, new Byte((byte)0));
      this.dataWatcher.addObject(17, new Byte((byte)0));
      this.dataWatcher.addObject(18, new Byte((byte)0));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      par1NBTTagCompound.setShort("carried", (short)this.getCarried());
      par1NBTTagCompound.setShort("carriedData", (short)this.getCarryingData());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      this.setCarried(par1NBTTagCompound.getShort("carried"));
      this.setCarryingData(par1NBTTagCompound.getShort("carriedData"));
   }

   @Override
   protected Entity findPlayerToAttack() {
      EntityPlayer var1 = this.worldObj.getClosestVulnerablePlayerToEntity(this, 64.0);
      if (var1 != null) {
         if (this.shouldAttackPlayer(var1)) {
            this.field_104003_g = true;
            if (this.field_70826_g == 0) {
               this.worldObj.playSoundAtEntity(var1, "mob.endermen.stare", 1.0F, 1.0F);
            }

            if (this.field_70826_g++ == 5) {
               this.field_70826_g = 0;
               this.setScreaming(true);
               return var1;
            }
         } else {
            this.field_70826_g = 0;
         }
      }

      return null;
   }

   private boolean shouldAttackPlayer(EntityPlayer par1EntityPlayer) {
      ItemStack var2 = par1EntityPlayer.inventory.armorInventory[3];
      if (var2 != null && var2.itemID == Block.pumpkin.blockID) {
         return false;
      } else {
         Vec3 var3 = par1EntityPlayer.i(1.0F).normalize();
         Vec3 var4 = this.worldObj
            .getWorldVec3Pool()
            .getVecFromPool(
               this.posX - par1EntityPlayer.posX,
               this.boundingBox.minY + this.height / 2.0F - (par1EntityPlayer.posY + par1EntityPlayer.getEyeHeight()),
               this.posZ - par1EntityPlayer.posZ
            );
         double var5 = var4.lengthVector();
         var4 = var4.normalize();
         double var7 = var3.dotProduct(var4);
         return var7 > 1.0 - 0.025 / var5 ? par1EntityPlayer.n(this) : false;
      }
   }

   @Override
   public void onLivingUpdate() {
      if (this.F()) {
         this.attackEntityFrom(DamageSource.drown, 1);
      }

      this.moveSpeed = this.entityToAttack != null ? 6.5F : 0.3F;
      if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
         if (this.getCarried() == 0) {
            if (this.rand.nextInt(20) == 0) {
               int var1 = MathHelper.floor_double(this.posX - 2.0 + this.rand.nextDouble() * 4.0);
               int var2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 3.0);
               int var3 = MathHelper.floor_double(this.posZ - 2.0 + this.rand.nextDouble() * 4.0);
               int var4 = this.worldObj.getBlockId(var1, var2, var3);
               if (carriableBlocks[var4]) {
                  this.setCarried(this.worldObj.getBlockId(var1, var2, var3));
                  this.setCarryingData(this.worldObj.getBlockMetadata(var1, var2, var3));
                  this.worldObj.setBlock(var1, var2, var3, 0);
               }
            }
         } else if (this.rand.nextInt(2000) == 0) {
            int var1 = MathHelper.floor_double(this.posX - 1.0 + this.rand.nextDouble() * 2.0);
            int var2 = MathHelper.floor_double(this.posY + this.rand.nextDouble() * 2.0);
            int var3 = MathHelper.floor_double(this.posZ - 1.0 + this.rand.nextDouble() * 2.0);
            int var4 = this.worldObj.getBlockId(var1, var2, var3);
            int var5 = this.worldObj.getBlockId(var1, var2 - 1, var3);
            if (var4 == 0 && var5 > 0 && Block.blocksList[var5].renderAsNormalBlock()) {
               this.worldObj.setBlock(var1, var2, var3, this.getCarried(), this.getCarryingData(), 3);
               this.setCarried(0);
            }
         }
      }

      for (int var1 = 0; var1 < 2; var1++) {
         this.worldObj
            .spawnParticle(
               "portal",
               this.posX + (this.rand.nextDouble() - 0.5) * this.width,
               this.posY + this.rand.nextDouble() * this.height - 0.25,
               this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
               (this.rand.nextDouble() - 0.5) * 2.0,
               -this.rand.nextDouble(),
               (this.rand.nextDouble() - 0.5) * 2.0
            );
      }

      if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
         float var6 = this.c(1.0F);
         if (var6 > 0.5F
            && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
            && this.rand.nextFloat() * 30.0F < (var6 - 0.4F) * 2.0F) {
            this.entityToAttack = null;
            this.setScreaming(false);
            this.field_104003_g = false;
            this.teleportRandomly();
         }
      }

      if (this.F() || this.ae()) {
         this.entityToAttack = null;
         this.setScreaming(false);
         this.field_104003_g = false;
         this.teleportRandomly();
      }

      if (this.isScreaming() && !this.field_104003_g && this.rand.nextInt(100) == 0) {
         this.setScreaming(false);
      }

      this.isJumping = false;
      if (this.entityToAttack != null) {
         this.a(this.entityToAttack, 100.0F, 100.0F);
      }

      if (!this.worldObj.isRemote && this.R()) {
         if (this.entityToAttack != null) {
            if (this.entityToAttack instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer)this.entityToAttack)) {
               this.moveStrafing = this.moveForward = 0.0F;
               this.moveSpeed = 0.0F;
               if (this.entityToAttack.getDistanceSqToEntity(this) < 16.0) {
                  this.teleportRandomly();
               }

               this.teleportDelay = 0;
            } else if (this.entityToAttack.getDistanceSqToEntity(this) > 256.0 && this.teleportDelay++ >= 30 && this.teleportToEntity(this.entityToAttack)) {
               this.teleportDelay = 0;
            }
         } else {
            this.setScreaming(false);
            this.teleportDelay = 0;
         }
      }

      super.onLivingUpdate();
   }

   protected boolean teleportRandomly() {
      double var1 = this.posX + (this.rand.nextDouble() - 0.5) * 64.0;
      double var3 = this.posY + (this.rand.nextInt(64) - 32);
      double var5 = this.posZ + (this.rand.nextDouble() - 0.5) * 64.0;
      return this.teleportTo(var1, var3, var5);
   }

   protected boolean teleportToEntity(Entity par1Entity) {
      Vec3 var2 = this.worldObj
         .getWorldVec3Pool()
         .getVecFromPool(
            this.posX - par1Entity.posX, this.boundingBox.minY + this.height / 2.0F - par1Entity.posY + par1Entity.getEyeHeight(), this.posZ - par1Entity.posZ
         );
      var2 = var2.normalize();
      double var3 = 16.0;
      double var5 = this.posX + (this.rand.nextDouble() - 0.5) * 8.0 - var2.xCoord * var3;
      double var7 = this.posY + (this.rand.nextInt(16) - 8) - var2.yCoord * var3;
      double var9 = this.posZ + (this.rand.nextDouble() - 0.5) * 8.0 - var2.zCoord * var3;
      return this.teleportTo(var5, var7, var9);
   }

   protected boolean teleportTo(double par1, double par3, double par5) {
      double var7 = this.posX;
      double var9 = this.posY;
      double var11 = this.posZ;
      this.posX = par1;
      this.posY = par3;
      this.posZ = par5;
      boolean var13 = false;
      int var14 = MathHelper.floor_double(this.posX);
      int var15 = MathHelper.floor_double(this.posY);
      int var16 = MathHelper.floor_double(this.posZ);
      if (this.worldObj.blockExists(var14, var15, var16)) {
         boolean var17 = false;

         while (!var17 && var15 > 0) {
            int var18 = this.worldObj.getBlockId(var14, var15 - 1, var16);
            if (var18 != 0 && Block.blocksList[var18].blockMaterial.blocksMovement()) {
               var17 = true;
            } else {
               this.posY--;
               var15--;
            }
         }

         if (var17) {
            this.b(this.posX, this.posY, this.posZ);
            if (this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox)) {
               Block blockBelow = Block.blocksList[this.worldObj.getBlockId(var14, var15 - 1, var16)];
               if (blockBelow != null && blockBelow.canMobsSpawnOn(this.worldObj, var14, var15 - 1, var16)) {
                  var13 = true;
               }
            }
         }
      }

      if (!var13) {
         this.b(var7, var9, var11);
         return false;
      } else {
         short var30 = 128;

         for (int var18 = 0; var18 < var30; var18++) {
            double var19 = var18 / (var30 - 1.0);
            float var21 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float var22 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            float var23 = (this.rand.nextFloat() - 0.5F) * 0.2F;
            double var24 = var7 + (this.posX - var7) * var19 + (this.rand.nextDouble() - 0.5) * this.width * 2.0;
            double var26 = var9 + (this.posY - var9) * var19 + this.rand.nextDouble() * this.height;
            double var28 = var11 + (this.posZ - var11) * var19 + (this.rand.nextDouble() - 0.5) * this.width * 2.0;
            this.worldObj.spawnParticle("portal", var24, var26, var28, var21, var22, var23);
         }

         this.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
         this.a("mob.endermen.portal", 1.0F, 1.0F);
         return true;
      }
   }

   @Override
   protected String getLivingSound() {
      return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
   }

   @Override
   protected String getHurtSound() {
      return "mob.endermen.hit";
   }

   @Override
   protected String getDeathSound() {
      return "mob.endermen.death";
   }

   @Override
   protected int getDropItemId() {
      return Item.enderPearl.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.getDropItemId();
      if (var3 > 0) {
         int var4 = this.rand.nextInt(2 + par2);

         for (int var5 = 0; var5 < var4; var5++) {
            this.b(var3, 1);
         }
      }
   }

   public void setCarried(int par1) {
      this.dataWatcher.updateObject(16, (byte)(par1 & 0xFF));
   }

   public int getCarried() {
      return this.dataWatcher.getWatchableObjectByte(16);
   }

   public void setCarryingData(int par1) {
      this.dataWatcher.updateObject(17, (byte)(par1 & 0xFF));
   }

   public int getCarryingData() {
      return this.dataWatcher.getWatchableObjectByte(17);
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         this.setScreaming(true);
         if (par1DamageSource instanceof EntityDamageSource && par1DamageSource.getEntity() instanceof EntityPlayer) {
            this.field_104003_g = true;
         }

         if (par1DamageSource instanceof EntityDamageSourceIndirect) {
            this.field_104003_g = false;

            for (int var3 = 0; var3 < 64; var3++) {
               if (this.teleportRandomly()) {
                  return true;
               }
            }

            return false;
         } else {
            return super.attackEntityFrom(par1DamageSource, par2);
         }
      }
   }

   public boolean isScreaming() {
      return this.dataWatcher.getWatchableObjectByte(18) > 0;
   }

   public void setScreaming(boolean par1) {
      this.dataWatcher.updateObject(18, (byte)(par1 ? 1 : 0));
   }

   @Override
   public int getAttackStrength(Entity par1Entity) {
      return 7;
   }

   static {
      carriableBlocks[Block.grass.blockID] = true;
      carriableBlocks[Block.dirt.blockID] = true;
      carriableBlocks[Block.sand.blockID] = true;
      carriableBlocks[Block.gravel.blockID] = true;
      carriableBlocks[Block.plantYellow.blockID] = true;
      carriableBlocks[Block.plantRed.blockID] = true;
      carriableBlocks[Block.mushroomBrown.blockID] = true;
      carriableBlocks[Block.mushroomRed.blockID] = true;
      carriableBlocks[Block.tnt.blockID] = true;
      carriableBlocks[Block.cactus.blockID] = true;
      carriableBlocks[Block.blockClay.blockID] = true;
      carriableBlocks[Block.pumpkin.blockID] = true;
      carriableBlocks[Block.melon.blockID] = true;
      carriableBlocks[Block.mycelium.blockID] = true;
   }
}
