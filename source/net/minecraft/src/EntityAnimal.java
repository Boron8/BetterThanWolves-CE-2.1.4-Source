package net.minecraft.src;

import btw.entity.mob.SquidEntity;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public abstract class EntityAnimal extends EntityAgeable implements IAnimals {
   protected int breeding = 0;
   protected static final int HUNGER_LEVEL_DATA_WATCHER_ID = 21;
   protected static final int IN_LOVE_DATA_WATCHER_ID = 22;
   protected static final int WEARING_BREEDING_HARNESS_DATA_WATCHER_ID = 23;
   public static final int FULL_HUNGER_COUNT = 24000;
   public static final int LEVEL_UP_HUNGER_COUNT = 25500;
   public static final int MAX_HEALING_COUNT = 24000;
   public int hungerCountdown = 24000;
   public int healingCountdown = 24000;
   public static final int BASE_GRAZE_FOOD_VALUE = 200;
   public static final int m_iDelayBetweenEatLoose = 10;
   public static final int DELAY_BETWEEN_EAT_LOOSE_VARIANCE = 10;
   public int eatLooseCooldownCounter = 20;
   public int grazeProgressCounter = 0;

   public EntityAnimal(World par1World) {
      super(par1World);
   }

   @Override
   protected void updateAITick() {
      if (this.b() != 0) {
         this.resetInLove();
      }

      super.bp();
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.b() != 0) {
         this.resetInLove();
      }

      if (this.isInLove()) {
         this.setInLove(this.getInLove() - 1);
         String var1 = "heart";
         if (this.getInLove() % 10 == 0) {
            double var2 = this.rand.nextGaussian() * 0.02;
            double var4 = this.rand.nextGaussian() * 0.02;
            double var6 = this.rand.nextGaussian() * 0.02;
            this.worldObj
               .spawnParticle(
                  var1,
                  this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  this.posY + 0.5 + this.rand.nextFloat() * this.height,
                  this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  var2,
                  var4,
                  var6
               );
         }
      } else {
         this.breeding = 0;
      }
   }

   @Override
   protected void attackEntity(Entity par1Entity, float par2) {
      if (par1Entity instanceof EntityPlayer) {
         if (par2 < 3.0F) {
            double var3 = par1Entity.posX - this.posX;
            double var5 = par1Entity.posZ - this.posZ;
            this.rotationYaw = (float)(Math.atan2(var5, var3) * 180.0 / Math.PI) - 90.0F;
            this.hasAttacked = true;
         }

         EntityPlayer var7 = (EntityPlayer)par1Entity;
         if (var7.getCurrentEquippedItem() == null || !this.isBreedingItem(var7.getCurrentEquippedItem())) {
            this.entityToAttack = null;
         }
      } else if (par1Entity instanceof EntityAnimal) {
         EntityAnimal var8 = (EntityAnimal)par1Entity;
         if (this.b() > 0 && var8.b() < 0) {
            if (par2 < 2.5) {
               this.hasAttacked = true;
            }
         } else if (this.isInLove() && var8.isInLove()) {
            if (var8.entityToAttack == null) {
               var8.entityToAttack = this;
            }

            if (var8.entityToAttack == this && par2 < 3.5) {
               this.setInLove(this.getInLove() + 1);
               var8.setInLove(var8.getInLove() + 1);
               this.breeding++;
               if (this.breeding % 4 == 0) {
                  this.worldObj
                     .spawnParticle(
                        "heart",
                        this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                        this.posY + 0.5 + this.rand.nextFloat() * this.height,
                        this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                        0.0,
                        0.0,
                        0.0
                     );
               }

               if (this.breeding == 60) {
                  this.procreate((EntityAnimal)par1Entity);
               }
            } else {
               this.breeding = 0;
            }
         } else {
            this.breeding = 0;
            this.entityToAttack = null;
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         this.fleeingTick = 60;
         this.entityToAttack = null;
         this.resetInLove();
         this.panicNearbyAnimals(par1DamageSource);
         return super.a(par1DamageSource, par2);
      }
   }

   @Override
   protected Entity findPlayerToAttack() {
      if (this.fleeingTick > 0) {
         return null;
      } else {
         float var1 = 8.0F;
         if (this.isInLove()) {
            double dClosestAnimalDistanceSq = 0.0;
            EntityAnimal closestValidAnimal = null;
            List list = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand(var1, var1, var1));

            for (int i = 0; i < list.size(); i++) {
               EntityAnimal entityanimal = (EntityAnimal)list.get(i);
               if (entityanimal != this && entityanimal.isInLove()) {
                  double dDistanceSqToAnimal = this.e(entityanimal);
                  if (closestValidAnimal == null || dDistanceSqToAnimal < dClosestAnimalDistanceSq) {
                     dClosestAnimalDistanceSq = dDistanceSqToAnimal;
                     closestValidAnimal = entityanimal;
                  }
               }
            }

            return closestValidAnimal;
         } else {
            if (this.b() == 0) {
               List var2 = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.boundingBox.expand(var1, var1, var1));

               for (int var3 = 0; var3 < var2.size(); var3++) {
                  EntityPlayer var5 = (EntityPlayer)var2.get(var3);
                  if (var5.getCurrentEquippedItem() != null && this.isBreedingItem(var5.getCurrentEquippedItem())) {
                     return var5;
                  }
               }
            } else if (this.b() > 0) {
               List var2 = this.worldObj.getEntitiesWithinAABB(this.getClass(), this.boundingBox.expand(var1, var1, var1));

               for (int var3x = 0; var3x < var2.size(); var3x++) {
                  EntityAnimal var4 = (EntityAnimal)var2.get(var3x);
                  if (var4 != this && var4.b() < 0) {
                     return var4;
                  }
               }
            }

            return null;
         }
      }
   }

   @Override
   public boolean getCanSpawnHere() {
      int var1 = MathHelper.floor_double(this.posX);
      int var2 = MathHelper.floor_double(this.boundingBox.minY);
      int var3 = MathHelper.floor_double(this.posZ);
      return this.worldObj.getBlockId(var1, var2 - 1, var3) == Block.grass.blockID && this.worldObj.getFullBlockLightValue(var1, var2, var3) > 8 && super.bv();
   }

   @Override
   public int getTalkInterval() {
      return 120;
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }

   @Override
   protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
      return 1 + this.worldObj.rand.nextInt(3);
   }

   public boolean isBreedingItem(ItemStack par1ItemStack) {
      return par1ItemStack.itemID == Item.wheat.itemID;
   }

   public boolean isInLove() {
      return this.getInLove() > 0;
   }

   public void resetInLove() {
      this.setInLove(0);
   }

   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      return par1EntityAnimal == this ? false : (par1EntityAnimal.getClass() != this.getClass() ? false : this.isInLove() && par1EntityAnimal.isInLove());
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(21, new Byte((byte)0));
      this.dataWatcher.addObject(22, new Integer(0));
      this.dataWatcher.addObject(23, (byte)0);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      tag.setInteger("InLove", this.getInLove());
      tag.setByte("fcHungerLvl", (byte)this.getHungerLevel());
      tag.setBoolean("BreedingHarness", this.getWearingBreedingHarness());
      tag.setInteger("fcHungerCnt", this.hungerCountdown);
      tag.setInteger("fcHealCnt", this.healingCountdown);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      this.setInLove(tag.getInteger("InLove"));
      if (tag.hasKey("BreedingHarness")) {
         this.setWearingBreedingHarness(tag.getBoolean("BreedingHarness"));
      }

      if (tag.hasKey("fcHungerLvl")) {
         this.setHungerLevel(tag.getByte("fcHungerLvl"));
      }

      if (tag.hasKey("fcHungerCnt")) {
         this.hungerCountdown = tag.getInteger("fcHungerCnt");
      } else {
         this.resetHungerCountdown();
      }

      if (tag.hasKey("fcHealCnt")) {
         this.healingCountdown = tag.getInteger("fcHealCnt");
      } else {
         this.resetHealingCountdown();
      }
   }

   @Override
   protected void modSpecificOnLivingUpdate() {
      super.modSpecificOnLivingUpdate();
      if (!this.worldObj.isRemote) {
         if (this.R()) {
            this.checkForLooseFood();
            this.checkForIntersectingBreedingHarnesses();
            this.updateHealing();
            this.updateHungerState();
         }
      } else {
         if (this.grazeProgressCounter > 0) {
            this.grazeProgressCounter--;
         }

         if (this.fleeingTick > 0) {
            this.fleeingTick--;
         }
      }

      if (this.isInLove() && this.entityToAttack != null && this.entityToAttack instanceof EntityAnimal) {
         EntityAnimal entityanimal = (EntityAnimal)this.entityToAttack;
         if (!entityanimal.isInLove()) {
            this.entityToAttack = null;
         }
      }
   }

   @Override
   public void jump() {
      if (this.h_()) {
         this.motionY = 0.21;
         this.isAirBorne = true;
      } else {
         super.bl();
      }
   }

   @Override
   public void onDeath(DamageSource damageSource) {
      super.a(damageSource);
      if (!this.worldObj.isRemote && this.getWearingBreedingHarness()) {
         this.b(BTWItems.breedingHarness.itemID, 1);
      }
   }

   @Override
   protected void updateEntityActionState() {
      super.bq();
      if (this.getWearingBreedingHarness()) {
         this.moveStrafing = 0.0F;
         this.moveForward = 0.0F;
      }
   }

   @Override
   public void checkForScrollDrop() {
   }

   @Override
   protected float getSoundPitch() {
      float fPitch = super.aY();
      if (this.isPossessed()) {
         fPitch *= 0.6F;
      }

      return fPitch;
   }

   @Override
   public void setRevengeTarget(EntityLiving targetEntity) {
      this.breeding = targetEntity;
      if (this.breeding != null) {
         this.revengeTimer = 300;
      } else {
         this.revengeTimer = 0;
      }
   }

   @Override
   public float getBlockPathWeight(int i, int j, int k) {
      return !this.canGrazeOnBlock(i, j - 1, k) && !this.canGrazeOnBlock(i, j, k) ? this.worldObj.getNaturalLightBrightness(i, j, k) - 0.5F : 10.0F;
   }

   @Override
   public boolean isSecondaryTargetForSquid() {
      return true;
   }

   @Override
   public void onFlungBySquidTentacle(SquidEntity squid) {
      DamageSource squidSource = DamageSource.causeMobDamage(squid);
      this.attackEntityFrom(squidSource, 0);
   }

   @Override
   public void onHeadCrabbedBySquid(SquidEntity squid) {
      DamageSource squidSource = DamageSource.causeMobDamage(squid);
      this.attackEntityFrom(squidSource, 0);
   }

   @Override
   protected void attemptToPossessNearbyCreatureOnDeath() {
      this.attemptToPossessNearbyCreature(16.0, true);
   }

   @Override
   public float getSpeedModifier() {
      return super.bE() * this.getHungerSpeedModifier();
   }

   @Override
   public boolean canChildGrow() {
      return super.canChildGrow() && !this.isTooHungryToGrow();
   }

   @Override
   public boolean canLoveJuiceRegenerate() {
      return this.isFullyFed();
   }

   @Override
   public int getTicksForChildToGrow() {
      return 48000;
   }

   @Override
   public boolean interact(EntityPlayer player) {
      return this.entityAnimalInteract(player);
   }

   @Override
   public void initCreature() {
      this.initHungerWithVariance();
   }

   public int getInLove() {
      return this.dataWatcher.getWatchableObjectInt(22);
   }

   public void setInLove(int iInLove) {
      this.dataWatcher.updateObject(22, iInLove);
   }

   public boolean getWearingBreedingHarness() {
      return this.dataWatcher.getWatchableObjectByte(23) > 0;
   }

   public void setWearingBreedingHarness(boolean bWearingHarness) {
      Byte wearing = (byte)0;
      if (bWearingHarness) {
         wearing = (byte)1;
      }

      this.dataWatcher.updateObject(23, wearing);
   }

   public void checkForIntersectingBreedingHarnesses() {
      if (this.getWearingBreedingHarness()) {
         AxisAlignedBB tempBoundingBox = this.boundingBox.copy();
         tempBoundingBox.contract(0.1, 0.1, 0.1);
         List collisionList = this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, tempBoundingBox);
         if (!collisionList.isEmpty()) {
            for (int listIndex = 0; listIndex < collisionList.size(); listIndex++) {
               EntityAnimal entityAnimal = (EntityAnimal)collisionList.get(listIndex);
               if (entityAnimal != this && entityAnimal.getWearingBreedingHarness() && !entityAnimal.isLivingDead) {
                  this.attackEntityFrom(DamageSource.inWall, 1);
                  break;
               }
            }
         }
      }
   }

   public void panicNearbyAnimals(DamageSource damageSource) {
      Entity attackingEntity = damageSource.getEntity();
      if (attackingEntity != null && attackingEntity instanceof EntityLiving) {
         EntityLiving attackingEntityLiving = (EntityLiving)attackingEntity;

         for (EntityAnimal tempAnimal : this.worldObj.getEntitiesWithinAABB(EntityAnimal.class, this.boundingBox.expand(16.0, 8.0, 16.0))) {
            if (!tempAnimal.isLivingDead && tempAnimal != this && tempAnimal != attackingEntityLiving) {
               tempAnimal.onNearbyAnimalAttacked(this, attackingEntityLiving);
            }
         }
      }
   }

   public void onNearbyAnimalAttacked(EntityAnimal attackedAnimal, EntityLiving attackSource) {
      if (this.breeding == null) {
         this.breeding = attackSource;
         this.revengeTimer = 150;
      } else if (this.revengeTimer < 150) {
         this.revengeTimer = 150;
      }
   }

   public void onNearbyFireStartAttempt(EntityPlayer player) {
      this.onNearbyPlayerStartles(player);
   }

   public void onNearbyPlayerBlockAddOrRemove(EntityPlayer player) {
      this.onNearbyPlayerStartles(player);
   }

   protected void onNearbyPlayerStartles(EntityPlayer player) {
      if (this.breeding == null) {
         this.breeding = player;
         this.revengeTimer = 150;
      } else if (this.revengeTimer < 150) {
         this.revengeTimer = 150;
      }
   }

   protected void procreate(EntityAnimal targetMate) {
      double dChildX = this.posX;
      double dChildY = this.posY;
      double dChildZ = this.posZ;
      if (this.getWearingBreedingHarness()) {
         dChildX = (this.posX + targetMate.posX) / 2.0;
         dChildY = (this.posY + targetMate.posY) / 2.0;
         dChildZ = (this.posZ + targetMate.posZ) / 2.0;
      }

      this.giveBirthAtTargetLocation(targetMate, dChildX, dChildY, dChildZ);
      this.resetMatingStateOfBothParents(targetMate);
      this.spawnBirthHeartParticles();
      this.worldObj.playSoundAtEntity(this, this.bd(), this.ba(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      this.worldObj.playAuxSFX(2222, MathHelper.floor_double(dChildX), MathHelper.floor_double(dChildY), MathHelper.floor_double(dChildZ), 0);
   }

   public int getNestSize() {
      return 1;
   }

   protected void giveBirthAtTargetLocation(EntityAnimal targetMate, double dChildX, double dChildY, double dChildZ) {
      int nestSize = this.getNestSize();

      for (int nestTempCount = 0; nestTempCount < nestSize; nestTempCount++) {
         EntityAgeable childEntity = this.a(targetMate);
         if (childEntity != null) {
            childEntity.setGrowingAge(-this.getTicksForChildToGrow());
            childEntity.b(dChildX, dChildY, dChildZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(childEntity);
         }
      }
   }

   protected void resetMatingStateOfBothParents(EntityAnimal targetMate) {
      this.a(this.getTicksToRegenerateLoveJuice());
      targetMate.a(targetMate.getTicksToRegenerateLoveJuice());
      this.resetInLove();
      this.breeding = 0;
      this.entityToAttack = null;
      targetMate.resetInLove();
      targetMate.entityToAttack = null;
      targetMate.breeding = 0;
   }

   protected void spawnBirthHeartParticles() {
      for (int iTempCount = 0; iTempCount < 7; iTempCount++) {
         double dParticleVelX = this.rand.nextGaussian() * 0.02;
         double dParticleVelY = this.rand.nextGaussian() * 0.02;
         double dParticleVelZ = this.rand.nextGaussian() * 0.02;
         this.worldObj
            .spawnParticle(
               "heart",
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
               this.posY + 0.5 + this.rand.nextFloat() * this.height,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
               dParticleVelX,
               dParticleVelY,
               dParticleVelZ
            );
      }
   }

   public void initHungerWithVariance() {
      if (this.isSubjectToHunger()) {
         this.hungerCountdown = 24000 - this.rand.nextInt(this.getGrazeHungerGain());
      }
   }

   public int getHungerLevel() {
      return this.dataWatcher.getWatchableObjectByte(21);
   }

   public void setHungerLevel(int iHungerLevel) {
      this.dataWatcher.updateObject(21, (byte)iHungerLevel);
   }

   public boolean isFullyFed() {
      return this.getHungerLevel() == 0;
   }

   public boolean isFamished() {
      return this.getHungerLevel() == 1;
   }

   public boolean isStarving() {
      return this.getHungerLevel() >= 2;
   }

   public void onBecomeFamished() {
      this.setHungerLevel(1);
   }

   public void onBecomeStarving() {
      this.setHungerLevel(2);
   }

   public void onStarvingCountExpired() {
      if (this.worldObj.getDifficulty().canAnimalsStarve()) {
         this.attackEntityFrom(DamageSource.starve, 5);
      }
   }

   public boolean isSubjectToHunger() {
      return false;
   }

   public void updateHungerState() {
      if (this.isSubjectToHunger()) {
         if (!this.h_()) {
            this.hungerCountdown--;
         } else {
            this.hungerCountdown -= 2;
         }

         if (this.hungerCountdown <= 0) {
            if (!this.h_()) {
               if (this.isFullyFed()) {
                  this.onBecomeFamished();
               } else if (this.isFamished()) {
                  this.onBecomeStarving();
               } else {
                  this.onStarvingCountExpired();
               }

               this.resetHungerCountdown();
            } else {
               this.attackEntityFrom(DamageSource.starve, 1);
            }
         }
      }
   }

   public void resetHungerCountdown() {
      this.hungerCountdown = 24000;
   }

   public void addToHungerCount(int iAddedHunger) {
      this.hungerCountdown += iAddedHunger;
      if (this.hungerCountdown > 25500) {
         int iHungerLevel = this.getHungerLevel();
         if (iHungerLevel > 0) {
            this.hungerCountdown -= 24000;
            this.setHungerLevel(iHungerLevel - 1);
         }
      }
   }

   public int getGrazeHungerGain() {
      return 200 * this.getFoodValueMultiplier();
   }

   public int getFoodValueMultiplier() {
      return 2;
   }

   public void onGrazeBlock(int i, int j, int k) {
      this.addToHungerCount(this.getGrazeHungerGain());
   }

   public boolean shouldNotifyBlockOnGraze() {
      return true;
   }

   public void playGrazeFX(int i, int j, int k, int iBlockID) {
      this.worldObj.playAuxSFX(2001, i, j, k, iBlockID);
   }

   public int getGrazeDuration() {
      return 40;
   }

   public boolean isHungryEnoughToGraze() {
      return !this.isFullyFed() || this.hungerCountdown + this.getGrazeHungerGain() <= 24000;
   }

   public boolean isHungryEnoughToForceMoveToGraze() {
      return this.h_() || !this.isFullyFed() || this.hungerCountdown < 12000;
   }

   public boolean isTooHungryToGrow() {
      return !this.isFullyFed() || this.hungerCountdown < 18000;
   }

   public boolean isTooHungryToHeal() {
      return !this.isFullyFed() || this.hungerCountdown < 18000;
   }

   public boolean canGrazeMycelium() {
      return false;
   }

   public boolean getDisruptsEarthOnGraze() {
      return false;
   }

   public boolean canGrazeOnRoughVegetation() {
      return false;
   }

   public BlockPos getGrazeBlockForPos() {
      BlockPos targetPos = new BlockPos(MathHelper.floor_double(this.posX), (int)this.boundingBox.minY, MathHelper.floor_double(this.posZ));
      if (this.canGrazeOnBlock(targetPos.x, targetPos.y, targetPos.z)) {
         return targetPos;
      } else {
         targetPos.y--;
         return this.canGrazeOnBlock(targetPos.x, targetPos.y, targetPos.z) ? targetPos : null;
      }
   }

   public boolean shouldStayInPlaceToGraze() {
      return this.getGrazeBlockForPos() != null;
   }

   public boolean canGrazeOnBlock(int i, int j, int k) {
      Block block = Block.blocksList[this.worldObj.getBlockId(i, j, k)];
      return block != null ? block.canBeGrazedOn(this.worldObj, i, j, k, this) : false;
   }

   public float getHungerSpeedModifier() {
      if (this.isStarving()) {
         return 0.5F;
      } else {
         return this.isFamished() ? 0.75F : 1.0F;
      }
   }

   public boolean isTemptingItem(ItemStack stack) {
      return this.getItemFoodValue(stack) > 0 || this.isBreedingItem(stack) && this.isReadyToEatBreedingItem();
   }

   public boolean isEdibleItem(ItemStack stack) {
      return this.isBreedingItem(stack) || this.getItemFoodValue(stack) > 0;
   }

   public boolean isHungryEnoughToEatLooseFood() {
      return !this.isFullyFed() || this.hungerCountdown <= 24000;
   }

   public boolean isReadyToEatBreedingItem() {
      return this.isFullyFed() && this.b() == 0 && !this.isInLove();
   }

   public int getItemFoodValue(ItemStack stack) {
      return stack.getItem().getHerbivoreFoodValue(stack.getItemDamage()) * this.getFoodValueMultiplier();
   }

   public boolean attemptToEatItemForBreeding(ItemStack stack) {
      if (this.isBreedingItem(stack) && this.isReadyToEatBreedingItem()) {
         this.onEatBreedingItem();
         return true;
      } else {
         return false;
      }
   }

   public void onEatBreedingItem() {
      this.setInLove(600);
      this.entityToAttack = null;

      for (int iTempCount = 0; iTempCount < 7; iTempCount++) {
         this.worldObj
            .spawnParticle(
               "heart",
               this.posX + (this.rand.nextFloat() * this.width * 2.0F - this.width),
               this.posY + 0.5 + this.rand.nextFloat() * this.height,
               this.posZ + (this.rand.nextFloat() * this.width * 2.0F - this.width),
               this.rand.nextGaussian() * 0.02,
               this.rand.nextGaussian() * 0.02,
               this.rand.nextGaussian() * 0.02
            );
      }
   }

   public boolean attemptToEatItem(ItemStack stack) {
      int iFoodValue = this.getItemFoodValue(stack);
      if (!this.attemptToEatItemForBreeding(stack) && (iFoodValue <= 0 || !this.isHungryEnoughToEatLooseFood())) {
         return false;
      } else {
         this.addToHungerCount(iFoodValue);
         this.worldObj.setEntityState(this, (byte)10);
         this.worldObj.playAuxSFX(2283, MathHelper.floor_double(this.posX), (int)(this.posY + this.height), MathHelper.floor_double(this.posZ), 0);
         return true;
      }
   }

   public boolean attemptToBeHandFedItem(ItemStack stack) {
      return this.attemptToEatItem(stack);
   }

   public boolean attemptToEatLooseItem(ItemStack stack) {
      return this.attemptToEatItem(stack);
   }

   public boolean isReadyToEatLooseFood() {
      return this.isHungryEnoughToEatLooseFood() || this.isReadyToEatBreedingItem();
   }

   public boolean isReadyToEatLooseItem(ItemStack stack) {
      return this.getItemFoodValue(stack) > 0 && this.isHungryEnoughToEatLooseFood() || this.isBreedingItem(stack) && this.isReadyToEatBreedingItem();
   }

   public void checkForLooseFood() {
      if (this.eatLooseCooldownCounter > 0) {
         this.eatLooseCooldownCounter--;
      } else if (this.isReadyToEatLooseFood()) {
         List<EntityItem> entityList = this.worldObj
            .getEntitiesWithinAABB(
               EntityItem.class,
               AxisAlignedBB.getAABBPool()
                  .getAABB(
                     this.boundingBox.minX - 1.5,
                     this.boundingBox.minY - 1.0,
                     this.boundingBox.minZ - 1.5,
                     this.boundingBox.maxX + 1.5,
                     this.boundingBox.maxY + 1.0,
                     this.boundingBox.maxZ + 1.5
                  )
            );
         if (!entityList.isEmpty()) {
            for (EntityItem tempEntity : entityList) {
               if (tempEntity.delayBeforeCanPickup == 0 && tempEntity.R()) {
                  ItemStack tempStack = tempEntity.getEntityItem();
                  if (this.attemptToEatLooseItem(tempEntity.getEntityItem())) {
                     tempStack.stackSize--;
                     if (tempStack.stackSize <= 0) {
                        tempEntity.w();
                     } else {
                        tempEntity.delayBeforeCanPickup = 2;
                     }

                     this.eatLooseCooldownCounter = 10 + this.rand.nextInt(11);
                     break;
                  }
               }
            }
         }
      }
   }

   public boolean entityAnimalInteract(EntityPlayer player) {
      ItemStack heldItem = player.inventory.getCurrentItem();
      if (heldItem != null && this.isEdibleItem(heldItem)) {
         if (!this.worldObj.isRemote && this.attemptToBeHandFedItem(heldItem)) {
            heldItem.stackSize--;
            if (heldItem.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
         }

         return true;
      } else {
         return super.interact(player);
      }
   }

   public int getTicksToRegenerateLoveJuice() {
      return 24000;
   }

   public float getGrazeHeadVerticalOffset(float fPartialTick) {
      int iGrazeDuration = this.getGrazeDuration();
      if (this.getWearingBreedingHarness() || this.grazeProgressCounter <= 0) {
         return 0.0F;
      } else if (this.grazeProgressCounter >= 4 && this.grazeProgressCounter <= iGrazeDuration - 4) {
         return 1.0F;
      } else {
         return this.grazeProgressCounter < 4
            ? (this.grazeProgressCounter - fPartialTick) / 4.0F
            : -(this.grazeProgressCounter - iGrazeDuration - fPartialTick) / 4.0F;
      }
   }

   public float getGrazeHeadRotation(float fPartialTick) {
      int iGrazeDuration = this.getGrazeDuration();
      if (this.getWearingBreedingHarness() || this.grazeProgressCounter <= 0) {
         return this.rotationPitch / (180.0F / (float)Math.PI);
      } else if (this.grazeProgressCounter > 4 && this.grazeProgressCounter <= iGrazeDuration - 4) {
         float fProgress = (this.grazeProgressCounter - 4 - fPartialTick) / (iGrazeDuration - 8);
         return (float) Math.PI / this.getGrazeHeadRotationMagnitudeDivisor()
            + 0.2199115F * MathHelper.sin(fProgress * this.getGrazeHeadRotationRateMultiplier());
      } else {
         return (float) Math.PI / this.getGrazeHeadRotationMagnitudeDivisor();
      }
   }

   public float getGrazeHeadRotationMagnitudeDivisor() {
      return 5.0F;
   }

   public float getGrazeHeadRotationRateMultiplier() {
      return 28.7F;
   }

   public void updateHealing() {
      if (this.isSubjectToHunger() && !this.h_()) {
         if (this.isTooHungryToHeal()) {
            this.resetHealingCountdown();
         } else {
            this.healingCountdown--;
            if (this.healingCountdown <= 0) {
               this.j(1);
               this.resetHealingCountdown();
            }
         }
      }
   }

   public void resetHealingCountdown() {
      this.healingCountdown = 24000;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte bUpdateType) {
      if (bUpdateType == 10) {
         this.grazeProgressCounter = this.getGrazeDuration();
      } else {
         super.a(bUpdateType);
      }
   }
}
