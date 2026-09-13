package net.minecraft.src;

import btw.BTWMod;
import btw.item.BTWItems;
import net.minecraft.server.MinecraftServer;

public class EntityItem extends Entity {
   public int age;
   public int delayBeforeCanPickup;
   private int health;
   public float hoverStart;
   private long absoluteItemDespawnTime = 0L;
   private String droppingPlayerName = "";
   private int droppingPlayerDeathsOnDrop = -1;

   public EntityItem(World par1World, double par2, double par4, double par6) {
      super(par1World);
      this.age = 0;
      this.health = 5;
      this.hoverStart = (float)(Math.random() * Math.PI * 2.0);
      this.a(0.25F, 0.25F);
      this.yOffset = this.height / 2.0F;
      this.b(par2, par4, par6);
      this.rotationYaw = (float)(Math.random() * 360.0);
      this.motionX = (float)(Math.random() * 0.2F - 0.1F);
      this.motionY = 0.2F;
      this.motionZ = (float)(Math.random() * 0.2F - 0.1F);
   }

   public EntityItem(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
      this(par1World, par2, par4, par6);
      this.setEntityItemStack(par8ItemStack);
   }

   @Override
   protected boolean canTriggerWalking() {
      return false;
   }

   public EntityItem(World par1World) {
      super(par1World);
      this.age = 0;
      this.health = 5;
      this.hoverStart = (float)(Math.random() * Math.PI * 2.0);
      this.a(0.25F, 0.25F);
      this.yOffset = this.height / 2.0F;
   }

   @Override
   protected void entityInit() {
      this.u().addObjectByDataType(10, 5);
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (this.delayBeforeCanPickup > 0) {
         this.delayBeforeCanPickup--;
      }

      this.prevPosX = this.posX;
      this.prevPosY = this.posY;
      this.prevPosZ = this.posZ;
      this.motionY -= 0.04F;
      this.updateHardcoreBuoy();
      if (!this.worldObj.isRemote) {
         this.pushOutOfBlocks(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0, this.posZ);
      }

      this.d(this.motionX, this.motionY, this.motionZ);
      boolean var1 = (int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ;
      if (var1 || this.ticksExisted % 25 == 0) {
         if (this.worldObj.getBlockMaterial(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
            == Material.lava) {
            this.motionY = 0.2F;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F;
            this.a("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
         }

         if (!this.worldObj.isRemote) {
            this.searchForOtherItemsNearby();
         }
      }

      float var2 = 0.98F;
      if (this.onGround) {
         var2 = 0.58800006F;
         int var3 = this.worldObj
            .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY) - 1, MathHelper.floor_double(this.posZ));
         if (var3 > 0) {
            var2 = Block.blocksList[var3].slipperiness * 0.98F;
         }
      }

      this.motionX *= var2;
      this.motionY *= 0.98F;
      this.motionZ *= var2;
      if (this.onGround) {
         this.motionY *= -0.5;
      }

      this.age++;
      this.checkForItemDespawn();
   }

   private void searchForOtherItemsNearby() {
      for (EntityItem var2 : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(0.5, 0.0, 0.5))) {
         this.combineItems(var2);
      }
   }

   public boolean combineItems(EntityItem par1EntityItem) {
      if (par1EntityItem == this) {
         return false;
      } else if (par1EntityItem.R() && this.R()) {
         ItemStack var2 = this.getEntityItem();
         ItemStack var3 = par1EntityItem.getEntityItem();
         if (var3.getItem() != var2.getItem()) {
            return false;
         } else if (var3.hasTagCompound() ^ var2.hasTagCompound()) {
            return false;
         } else if (var3.hasTagCompound() && !var3.getTagCompound().equals(var2.getTagCompound())) {
            return false;
         } else if (var3.getItem().getHasSubtypes() && var3.getItemDamage() != var2.getItemDamage()) {
            return false;
         } else if (var3.getItem().getHasSubtypes() || var3.getItemDamage() <= 0 && var2.getItemDamage() <= 0) {
            if (var3.stackSize < var2.stackSize) {
               return par1EntityItem.combineItems(this);
            } else if (var3.stackSize + var2.stackSize > var3.getMaxStackSize()) {
               return false;
            } else {
               var3.stackSize = var3.stackSize + var2.stackSize;
               par1EntityItem.delayBeforeCanPickup = Math.max(par1EntityItem.delayBeforeCanPickup, this.delayBeforeCanPickup);
               par1EntityItem.age = Math.min(par1EntityItem.age, this.age);
               par1EntityItem.setEntityItemStack(var3);
               this.w();
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public void setAgeToCreativeDespawnTime() {
      this.age = 4800;
   }

   @Override
   public boolean handleWaterMovement() {
      return this.worldObj.handleMaterialAcceleration(this.boundingBox, Material.water, this);
   }

   @Override
   public void dealFireDamage(int par1) {
      this.attackEntityFrom(DamageSource.inFire, par1);
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if (this.getEntityItem() != null && this.getEntityItem().itemID == Item.netherStar.itemID && par1DamageSource.isExplosion()) {
         return false;
      } else {
         this.J();
         if (!this.worldObj.isRemote && !this.isDead && this.getEntityItem().getItem().itemID == BTWItems.blastingOil.itemID) {
            this.detonateBlastingOil();
            return false;
         } else {
            this.health -= par2;
            if (this.health <= 0) {
               this.w();
            }

            return false;
         }
      }
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setShort("Health", (byte)this.health);
      par1NBTTagCompound.setShort("Age", (short)this.age);
      if (this.getEntityItem() != null) {
         par1NBTTagCompound.setCompoundTag("Item", this.getEntityItem().writeToNBT(new NBTTagCompound()));
      }

      par1NBTTagCompound.setLong("fcDespawnTime", this.absoluteItemDespawnTime);
      par1NBTTagCompound.setString("fcDroppingPlayerName", this.droppingPlayerName);
      par1NBTTagCompound.setInteger("fcDroppingPlayerDeathsOnDrop", this.droppingPlayerDeathsOnDrop);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      this.health = par1NBTTagCompound.getShort("Health") & 255;
      this.age = par1NBTTagCompound.getShort("Age");
      NBTTagCompound var2 = par1NBTTagCompound.getCompoundTag("Item");
      this.setEntityItemStack(ItemStack.loadItemStackFromNBT(var2));
      if (par1NBTTagCompound.hasKey("fcDespawnTime")) {
         this.absoluteItemDespawnTime = par1NBTTagCompound.getLong("fcDespawnTime");
      }

      if (par1NBTTagCompound.hasKey("fcDroppingPlayerName")) {
         this.droppingPlayerName = par1NBTTagCompound.getString("fcDroppingPlayerName");
      }

      if (par1NBTTagCompound.hasKey("fcDroppingPlayerDeathsOnDrop")) {
         this.droppingPlayerDeathsOnDrop = par1NBTTagCompound.getInteger("fcDroppingPlayerDeathsOnDrop");
      }

      if (this.getEntityItem() == null) {
         this.w();
      }
   }

   @Override
   public void onCollideWithPlayer(EntityPlayer par1EntityPlayer) {
      if (!this.worldObj.isRemote) {
         ItemStack var2 = this.getEntityItem();
         int var3 = var2.stackSize;
         if (this.delayBeforeCanPickup == 0 && par1EntityPlayer.inventory.addItemStackToInventory(var2)) {
            if (var2.itemID == Block.wood.blockID) {
               par1EntityPlayer.triggerAchievement(AchievementList.mineWood);
            }

            if (var2.itemID == Item.leather.itemID) {
               par1EntityPlayer.triggerAchievement(AchievementList.killCow);
            }

            if (var2.itemID == Item.diamond.itemID) {
               par1EntityPlayer.triggerAchievement(AchievementList.diamonds);
            }

            if (var2.itemID == Item.blazeRod.itemID) {
               par1EntityPlayer.triggerAchievement(AchievementList.blazeRod);
            }

            this.a("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
            par1EntityPlayer.a(this, var3);
            if (var2.stackSize <= 0) {
               this.w();
            }
         }
      }
   }

   @Override
   public String getEntityName() {
      return StatCollector.translateToLocal("item." + this.getEntityItem().getItemName());
   }

   @Override
   public boolean canAttackWithItem() {
      return false;
   }

   @Override
   public void travelToDimension(int par1) {
      super.travelToDimension(par1);
      if (!this.worldObj.isRemote) {
         this.searchForOtherItemsNearby();
      }
   }

   public ItemStack getEntityItem() {
      ItemStack var1 = this.u().getWatchableObjectItemStack(10);
      if (var1 == null) {
         if (this.worldObj != null) {
            this.worldObj.getWorldLogAgent().logSevere("Item entity " + this.entityId + " has no item?!");
         }

         return new ItemStack(Block.stone);
      } else {
         return var1;
      }
   }

   public void setEntityItemStack(ItemStack par1ItemStack) {
      this.u().updateObject(10, par1ItemStack);
      this.u().setObjectWatched(10);
   }

   private void updateHardcoreBuoy() {
      if (BTWMod.isHardcoreBuoyEnabled(this.worldObj)) {
         int numDepthChecks = 10;
         double d = 0.0;
         double dBoundingYOffset = 0.1;

         for (int j = 0; j < numDepthChecks; j++) {
            double d2 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (j + 0) * 0.375 + dBoundingYOffset;
            double d8 = this.boundingBox.minY + (this.boundingBox.maxY - this.boundingBox.minY) * (j + 1) * 0.375 + dBoundingYOffset;
            AxisAlignedBB axisalignedbb = AxisAlignedBB.getAABBPool()
               .getAABB(this.boundingBox.minX, d2, this.boundingBox.minZ, this.boundingBox.maxX, d8, this.boundingBox.maxZ);
            if (!this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
               break;
            }

            d += 1.0 / numDepthChecks;
         }

         if (d > 0.001) {
            if (!this.isInUndertow()) {
               float fBuoyancyShifted = this.getEntityItem().getItem().getBuoyancy(this.getEntityItem().getItemDamage()) + 1.0F;
               this.motionY += 0.04 * fBuoyancyShifted * d;
            }

            this.motionX *= 0.9F;
            this.motionY *= 0.9F;
            this.motionZ *= 0.9F;
         }
      }
   }

   @Override
   protected void doBlockCollisions() {
      int i = MathHelper.floor_double(this.boundingBox.minX + 0.001);
      int j = MathHelper.floor_double(this.boundingBox.minY - 0.01);
      int k = MathHelper.floor_double(this.boundingBox.minZ + 0.001);
      int l = MathHelper.floor_double(this.boundingBox.maxX - 0.001);
      int i1 = MathHelper.floor_double(this.boundingBox.maxY - 0.001);
      int j1 = MathHelper.floor_double(this.boundingBox.maxZ - 0.001);
      if (this.worldObj.checkChunksExist(i, j, k, l, i1, j1)) {
         for (int k1 = i; k1 <= l; k1++) {
            for (int l1 = j; l1 <= i1; l1++) {
               for (int i2 = k; i2 <= j1; i2++) {
                  int j2 = this.worldObj.getBlockId(k1, l1, i2);
                  if (j2 > 0) {
                     Block.blocksList[j2].onEntityCollidedWithBlock(this.worldObj, k1, l1, i2, this);
                  }
               }
            }
         }
      }
   }

   private boolean isInUndertow() {
      int minI = MathHelper.floor_double(this.boundingBox.minX);
      int maxI = MathHelper.floor_double(this.boundingBox.maxX + 1.0);
      int minJ = MathHelper.floor_double(this.boundingBox.minY);
      int maxJ = MathHelper.floor_double(this.boundingBox.maxY + 1.0);
      int minK = MathHelper.floor_double(this.boundingBox.minZ);
      int maxK = MathHelper.floor_double(this.boundingBox.maxZ + 1.0);

      for (int i = minI; i < maxI; i++) {
         for (int j = minJ; j < maxJ; j++) {
            for (int k = minK; k < maxK; k++) {
               if (this.doesBlockHaveUndertow(i, j, k)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean doesBlockHaveUndertow(int i, int j, int k) {
      int iBlockID = this.worldObj.getBlockId(i, j, k);
      if (iBlockID == Block.waterMoving.blockID || iBlockID == Block.waterStill.blockID) {
         int iFluidHeight = this.worldObj.getBlockMetadata(i, j, k);
         if (iFluidHeight >= 8) {
            return true;
         }

         iBlockID = this.worldObj.getBlockId(i, j - 1, k);
         if (iBlockID == Block.waterMoving.blockID || iBlockID == Block.waterStill.blockID) {
            iFluidHeight = this.worldObj.getBlockMetadata(i, j - 1, k);
            if (iFluidHeight >= 8) {
               return true;
            }
         }

         iBlockID = this.worldObj.getBlockId(i, j + 1, k);
         if (iBlockID == Block.waterMoving.blockID || iBlockID == Block.waterStill.blockID) {
            iFluidHeight = this.worldObj.getBlockMetadata(i, j + 1, k);
            if (iFluidHeight >= 8) {
               return true;
            }
         }
      }

      return false;
   }

   @Override
   protected void fall(float fFallDistance) {
      super.fall(fFallDistance);
      if (!this.worldObj.isRemote && this.getEntityItem().getItem().itemID == BTWItems.blastingOil.itemID && fFallDistance > 3.0F) {
         this.detonateBlastingOil();
      }
   }

   private void detonateBlastingOil() {
      int iStackSize = this.getEntityItem().stackSize;
      this.health = 0;
      this.w();
      if (iStackSize > 0) {
         float fExplosionSize = 1.5F + (iStackSize - 1) * 2.5F / 63.0F;
         this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, fExplosionSize, true);
      }
   }

   @Override
   protected boolean pushOutOfBlocks(double par1, double par3, double par5) {
      int var7 = MathHelper.floor_double(par1);
      int var8 = MathHelper.floor_double(par3);
      int var9 = MathHelper.floor_double(par5);
      double var10 = par1 - var7;
      double var12 = par3 - var8;
      double var14 = par5 - var9;
      if (this.worldObj.isBlockNormalCube(var7, var8, var9)) {
         boolean var16 = !this.worldObj.isBlockNormalCube(var7 - 1, var8, var9);
         boolean var17 = !this.worldObj.isBlockNormalCube(var7 + 1, var8, var9);
         boolean var18 = !this.worldObj.isBlockNormalCube(var7, var8 - 1, var9);
         boolean var19 = !this.worldObj.isBlockNormalCube(var7, var8 + 1, var9);
         boolean var20 = !this.worldObj.isBlockNormalCube(var7, var8, var9 - 1);
         boolean var21 = !this.worldObj.isBlockNormalCube(var7, var8, var9 + 1);
         byte var22 = -1;
         double var23 = 9999.0;
         if (var16 && var10 < var23) {
            var23 = var10;
            var22 = 0;
         }

         if (var17 && 1.0 - var10 < var23) {
            var23 = 1.0 - var10;
            var22 = 1;
         }

         if (var18 && var12 < var23) {
            var23 = var12;
            var22 = 2;
         }

         if (var19 && 1.0 - var12 < var23) {
            var23 = 1.0 - var12;
            var22 = 3;
         }

         if (var20 && var14 < var23) {
            var23 = var14;
            var22 = 4;
         }

         if (var21 && 1.0 - var14 < var23) {
            var23 = 1.0 - var14;
            var22 = 5;
         }

         float var25 = this.rand.nextFloat() * 0.2F + 0.1F;
         if (var22 == 0) {
            this.motionX = -var25;
         }

         if (var22 == 1) {
            this.motionX = var25;
         }

         if (var22 == 2) {
            this.motionY = -var25;
         }

         if (var22 == 3) {
            this.motionY = var25;
         }

         if (var22 == 4) {
            this.motionZ = -var25;
         }

         if (var22 == 5) {
            this.motionZ = var25;
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean isItemEntity() {
      return true;
   }

   @Override
   public boolean canEntityTriggerTripwire() {
      return false;
   }

   private void checkForItemDespawn() {
      if (!this.worldObj.isRemote) {
         if (this.droppingPlayerDeathsOnDrop > -1) {
            if (this.age >= 6000) {
               this.age = 0;
            }

            int deathsBeforeDisappear = this.worldObj.getDifficulty().getDeathCountBeforeItemDestruction();
            if (MinecraftServer.getServer().worldServers[0].H() >= this.absoluteItemDespawnTime && deathsBeforeDisappear >= 0) {
               EntityPlayer player = MinecraftServer.getServer().worldServers[0].a(this.droppingPlayerName);
               if (player == null) {
                  NBTTagCompound nbt = ((SaveHandler)MinecraftServer.getServer().worldServers[0].L()).getPlayerData(this.droppingPlayerName);
                  if (nbt == null) {
                     System.out.println("NBT tag not found checking dropped item owner, player was deleted??");
                     this.w();
                  } else if (nbt.hasKey("fcDeathCount") && nbt.getInteger("fcDeathCount") > this.droppingPlayerDeathsOnDrop + deathsBeforeDisappear) {
                     this.w();
                  }
               } else if (player.deathCount > this.droppingPlayerDeathsOnDrop + deathsBeforeDisappear) {
                  this.w();
               }
            }
         } else if (this.absoluteItemDespawnTime > 0L) {
            long lOverworldTime = MinecraftServer.getServer().worldServers[0].H();
            if (lOverworldTime >= this.absoluteItemDespawnTime) {
               this.w();
            }
         } else if (this.age >= 6000) {
            this.w();
         }
      }
   }

   public void setEntityItemAsDroppedOnPlayerDeath(EntityPlayer player) {
      if (!this.worldObj.isRemote) {
         long overworldTime = MinecraftServer.getServer().worldServers[0].H();
         long deltaSinceLastRespawn = overworldTime - player.timeOfLastSpawnAssignment;
         long maxGraceTime = 10800L;
         if (deltaSinceLastRespawn < maxGraceTime) {
            maxGraceTime -= deltaSinceLastRespawn;
         }

         this.absoluteItemDespawnTime = overworldTime + maxGraceTime;
         this.droppingPlayerName = player.username;
         this.droppingPlayerDeathsOnDrop = player.deathCount;
      }
   }

   public static boolean installationIntegrityTestEntityItem() {
      return true;
   }
}
