package btw.entity.mob;

import btw.block.BTWBlocks;
import btw.block.blocks.BlockDispenserBlock;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.entity.mob.behavior.MoveToLooseFoodBehavior;
import btw.entity.mob.behavior.MultiTemptBehavior;
import btw.entity.mob.behavior.PanicOnHeadCrabBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.entity.mob.behavior.SittingWolfHowlBehavior;
import btw.entity.mob.behavior.WildWolfTargetIfHungryBehavior;
import btw.entity.mob.behavior.WildWolfTargetIfStarvingBehavior;
import btw.entity.mob.behavior.WildWolfTargetIfStarvingOrHostileBehavior;
import btw.entity.mob.behavior.WolfHowlBehavior;
import btw.entity.mob.villager.VillagerEntity;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAIBeg;
import net.minecraft.src.EntityAIFollowOwner;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILeapAtTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAIOwnerHurtByTarget;
import net.minecraft.src.EntityAIOwnerHurtTarget;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class WolfEntity extends EntityWolf {
   private static final int IS_ENGAGED_IN_POSSESSION_ATTEMPT_DATA_WATCHER_ID = 26;
   private static final float MOVE_SPEED_AGGRESSIVE = 0.45F;
   private static final float MOVE_SPEED_PASSIVE = 0.3F;
   public int howlingCountdown = 0;
   public int heardHowlCountdown = 0;
   public int infectionCountdown = -1;
   private static final int MINIMUM_INFECTION_TIME = 12000;
   private static final int INFECTION_TIME_VARIANCE = 12000;
   private float possessionHeadRotation = 0.0F;
   private boolean isDoingHeadSpin = false;
   private boolean hasHeadSpunOnThisPossessionAttempt = false;
   private int possessionAttemptCountdown = 0;
   private static final int CHANCE_OF_POSSESSION_ATTEMPT = 12000;
   private static final int POSSESSION_ATTEMPT_TIME = 200;
   protected static final int HUNGER_COUNT_VARIANCE = 1200;

   public WolfEntity(World world) {
      super(world);
      this.moveSpeed = 0.45F;
      this.tasks.removeAllTasks();
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new PanicOnHeadCrabBehavior(this, 0.45F));
      this.tasks.addTask(3, this.aiSit);
      this.tasks.addTask(4, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(5, new EntityAIAttackOnCollide(this, 0.3F, true));
      this.tasks.addTask(6, new EntityAIFollowOwner(this, 0.3F, 10.0F, 2.0F));
      this.tasks.addTask(7, new EntityAIMate(this, 0.3F));
      this.tasks.addTask(7, new MultiTemptBehavior(this, 0.3F));
      this.tasks.addTask(8, new WolfHowlBehavior(this));
      this.tasks.addTask(8, new SittingWolfHowlBehavior(this));
      this.tasks.addTask(9, new MoveToLooseFoodBehavior(this, 0.3F));
      this.tasks.addTask(10, new SimpleWanderBehavior(this, 0.3F));
      this.tasks.addTask(11, new EntityAIBeg(this, 8.0F));
      this.tasks.addTask(12, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(13, new EntityAILookIdle(this));
      this.targetTasks.removeAllTasks();
      this.targetTasks.addTask(1, new EntityAIOwnerHurtByTarget(this));
      this.targetTasks.addTask(2, new EntityAIOwnerHurtTarget(this));
      this.targetTasks.addTask(3, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(4, new WildWolfTargetIfStarvingOrHostileBehavior(this, VillagerEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new WildWolfTargetIfStarvingOrHostileBehavior(this, EntityPlayer.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new WildWolfTargetIfHungryBehavior(this, ChickenEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new WildWolfTargetIfHungryBehavior(this, SheepEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new WildWolfTargetIfHungryBehavior(this, PigEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new WildWolfTargetIfStarvingBehavior(this, CowEntity.class, 16.0F, 0, false));
   }

   @Override
   public void setAttackTarget(EntityLiving target) {
      this.entityLivingSetAttackTarget(target);
   }

   @Override
   public void setRevengeTarget(EntityLiving target) {
      super.c(target);
      if (!this.m()) {
         if (target instanceof EntityPlayer) {
            this.setAngry(true);
         }

         this.setAttackTarget(target);
      }
   }

   @Override
   public void onKillEntity(EntityLiving entityKilled) {
      if (entityKilled instanceof EntityPlayer) {
         int breedableWolf = 0;
         int maxWolvesFed = 4;
         if (this.isReadyToEatBreedingItem()) {
            this.onEatBreedingItem();
            breedableWolf++;
         }

         List nearbyEntityList = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(16.0, 8.0, 16.0));
         Iterator nearbyEntityIterator = nearbyEntityList.iterator();

         while (nearbyEntityIterator.hasNext() && breedableWolf <= maxWolvesFed) {
            Entity nearbyEntity = (Entity)nearbyEntityIterator.next();
            if (nearbyEntity instanceof WolfEntity) {
               WolfEntity nearbyWolf = (WolfEntity)nearbyEntity;
               if (nearbyWolf.isReadyToEatBreedingItem()) {
                  nearbyWolf.onEatBreedingItem();
                  breedableWolf++;
               }
            }
         }
      }
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(26, new Byte((byte)0));
      this.resetHungerCountdown();
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.writeEntityToNBT(tag);
      tag.setInteger("fcInfection", this.infectionCountdown);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.readEntityFromNBT(tag);
      if (tag.hasKey("bIsFed")) {
         boolean bIsFed = tag.getBoolean("bIsFed");
         if (bIsFed) {
            this.setHungerLevel(0);
         } else {
            this.setHungerLevel(1);
         }
      }

      if (tag.hasKey("fcInfection")) {
         this.infectionCountdown = tag.getInteger("fcInfection");
      }
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }

   @Override
   protected String getLivingSound() {
      if (this.isWildAndHostile()) {
         return "mob.wolf.growl";
      } else if (this.rand.nextInt(3) != 0) {
         return "mob.wolf.bark";
      } else if (this.m() && (this.dataWatcher.getWatchableObjectInt(18) < 10 || !this.isFullyFed())) {
         return this.isStarving() ? "mob.wolf.growl" : "mob.wolf.whine";
      } else {
         return "mob.wolf.panting";
      }
   }

   @Override
   protected int getDropItemId() {
      if (!this.worldObj.isRemote) {
         if (!this.ae()) {
            return BTWItems.rawWolfChop.itemID;
         }

         if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
            this.b(BTWItems.cookedWolfChop.itemID, 1);
         } else {
            this.b(BTWItems.burnedMeat.itemID, 1);
         }
      }

      return -1;
   }

   @Override
   public void onLivingUpdate() {
      super.onLivingUpdate();
      if (this.worldObj.isRemote) {
         this.howlingCountdown = Math.max(0, this.howlingCountdown - 1);
      } else {
         this.heardHowlCountdown = Math.max(0, this.heardHowlCountdown - 1);
         if (this.infectionCountdown > 0) {
            this.infectionCountdown--;
            if (this.infectionCountdown <= 0) {
               this.transformToDire();
               return;
            }
         }
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource source, int iDamageAmount) {
      if (!this.aq() && !this.worldObj.isRemote && !this.m() && source.getEntity() instanceof EntityPlayer) {
         this.setAngry(true);
      }

      return super.attackEntityFrom(source, iDamageAmount);
   }

   @Override
   public int getMeleeAttackStrength(Entity target) {
      return 4;
   }

   @Override
   public boolean attackEntityAsMob(Entity target) {
      return this.meleeAttack(target);
   }

   @Override
   public boolean interact(EntityPlayer player) {
      ItemStack playerStack = player.inventory.getCurrentItem();
      if (playerStack != null) {
         if (playerStack.itemID == BTWItems.rawWolfChop.itemID || playerStack.itemID == BTWItems.cookedWolfChop.itemID) {
            this.worldObj.playSoundAtEntity(this, "mob.wolf.growl", this.ba(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.setAngry(true);
            this.j(false);
            this.a("");
            this.setAttackTarget(player);
            return true;
         }

         if (this.attemptUseStackOn(player, playerStack)) {
            if (!player.capabilities.isCreativeMode) {
               playerStack.stackSize--;
               if (playerStack.stackSize <= 0) {
                  player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
               }
            }

            return true;
         }
      }

      if (this.entityAnimalInteract(player)) {
         return true;
      } else {
         if (!this.worldObj.isRemote
            && this.m()
            && player.username.equalsIgnoreCase(this.o())
            && (playerStack == null || !this.isEdibleItem(playerStack) || !this.isBreedingItem(playerStack))) {
            this.aiSit.setSitting(!this.n());
            this.isJumping = false;
            this.a(null);
         }

         return false;
      }
   }

   @Override
   public boolean getCanBeHeadCrabbed(boolean bSquidInWater) {
      return !bSquidInWater && this.riddenByEntity == null && this.R() && !this.h_();
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 1.2;
   }

   @Override
   public boolean isBreedingItem(ItemStack stack) {
      return stack != null && (stack.itemID == BTWItems.rawMysteryMeat.itemID || stack.itemID == BTWItems.cookedMysteryMeat.itemID);
   }

   @Override
   public void setAngry(boolean bAngry) {
      super.setAngry(bAngry);
      if (bAngry) {
         this.k(false);
      }
   }

   @Override
   public boolean isReadyToEatLooseFood() {
      return !this.isFullyFed();
   }

   @Override
   public boolean isReadyToEatLooseItem(ItemStack stack) {
      Item tempItem = stack.getItem();
      return this.isBreedingItem(stack) && this.isReadyToEatBreedingItem()
         ? true
         : this.isReadyToEatLooseFood() && tempItem.isWolfFood() && (tempItem.itemID != Item.rottenFlesh.itemID || this.isStarving());
   }

   @Override
   public boolean attemptToEatLooseItem(ItemStack stack) {
      if (this.isReadyToEatLooseItem(stack)) {
         this.onEat(stack.getItem());
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean isEdibleItem(ItemStack stack) {
      return stack.getItem().isWolfFood();
   }

   @Override
   public boolean attemptToBeHandFedItem(ItemStack stack) {
      if (this.dataWatcher.getWatchableObjectInt(18) >= 20 && this.isFullyFed() && (!this.isBreedingItem(stack) || !this.isReadyToEatBreedingItem())) {
         return false;
      } else {
         this.onEat(stack.getItem());
         return true;
      }
   }

   @Override
   public void onNearbyPlayerBlockAddOrRemove(EntityPlayer player) {
   }

   @Override
   protected void onNearbyPlayerStartles(EntityPlayer player) {
      if (!this.m() && this.aJ() == null) {
         this.setAttackTarget(player);
      }
   }

   @Override
   public boolean getCanCreatureTypeBePossessed() {
      return true;
   }

   @Override
   protected void handlePossession() {
      super.handlePossession();
      if (this.isFullyPossessed()) {
         if (this.isEngagedInPossessionAttempt()) {
            if (!this.hasHeadSpunOnThisPossessionAttempt) {
               this.hasHeadSpunOnThisPossessionAttempt = true;
               this.isDoingHeadSpin = true;
               if (!this.worldObj.isRemote) {
                  this.a("portal.portal", 3.0F, this.rand.nextFloat() * 0.1F + 0.75F);
               }
            }

            if (!this.worldObj.isRemote) {
               this.possessionAttemptCountdown--;
               if (this.possessionAttemptCountdown <= 0) {
                  this.setEngagedInPossessionAttempt(false);
                  this.attemptToPossessNearbyCreature(16.0, false);
               }
            }
         } else {
            this.hasHeadSpunOnThisPossessionAttempt = false;
            if (!this.worldObj.isRemote && this.rand.nextInt(12000) == 0) {
               this.setEngagedInPossessionAttempt(true);
               this.possessionAttemptCountdown = 200;
            }
         }

         this.updateHeadSpin();
      }
   }

   @Override
   public void onNearbyAnimalAttacked(EntityAnimal attackedAnimal, EntityLiving attackSource) {
   }

   @Override
   public void notifyOfWolfHowl(Entity sourceEntity) {
      if (!this.isLivingDead) {
         double dDeltaX = this.posX - sourceEntity.posX;
         double dDeltaZ = this.posZ - sourceEntity.posZ;
         double dDistSq = dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
         if (dDistSq < 102400.0 && this != sourceEntity) {
            this.heardHowlCountdown = 95;
         }
      }
   }

   public WolfEntity spawnBabyAnimal(EntityAgeable parent) {
      return (WolfEntity)EntityList.createEntityOfType(WolfEntity.class, this.worldObj);
   }

   @Override
   public boolean isSubjectToHunger() {
      return !this.isPossessed();
   }

   @Override
   public void updateHungerState() {
      super.updateHungerState();
      this.updateShitState();
   }

   @Override
   public void onStarvingCountExpired() {
      super.onStarvingCountExpired();
      if (this.R() && !this.isFullyPossessed() && this.worldObj.getDifficulty().canAnimalsStarve()) {
         this.setAngry(true);
         this.j(false);
         this.a("");
      }
   }

   @Override
   public void resetHungerCountdown() {
      this.hungerCountdown = 24000 + (this.rand.nextInt(2400) - 1200);
   }

   @Override
   public float getHungerSpeedModifier() {
      return 1.0F;
   }

   @Override
   public boolean isTooHungryToGrow() {
      return !this.isFullyFed();
   }

   @Override
   public boolean isTooHungryToHeal() {
      return true;
   }

   @Override
   public int getItemFoodValue(ItemStack stack) {
      return 0;
   }

   @Override
   public boolean canMateWith(EntityAnimal par1EntityAnimal) {
      if (par1EntityAnimal == this) {
         return false;
      } else if (!(par1EntityAnimal instanceof EntityWolf)) {
         return false;
      } else {
         WolfEntity partner = (WolfEntity)par1EntityAnimal;
         return !partner.n() && this.r() && partner.r();
      }
   }

   public boolean isEngagedInPossessionAttempt() {
      return this.dataWatcher.getWatchableObjectByte(26) > 0;
   }

   public void setEngagedInPossessionAttempt(boolean bIsEngaged) {
      Byte tempByte = (byte)0;
      if (bIsEngaged) {
         tempByte = (byte)1;
      }

      this.dataWatcher.updateObject(26, tempByte);
   }

   private void updateShitState() {
      if (this.isFullyFed()) {
         int chanceOfShitting = 1;
         if (this.isDarkEnoughToAffectShitting()) {
            chanceOfShitting *= 2;
         }

         if (this.worldObj.rand.nextInt(24000) < chanceOfShitting && !this.isPossessed()) {
            this.attemptToShit();
         }
      }
   }

   private boolean attemptUseStackOn(EntityPlayer player, ItemStack playerStack) {
      if (this.m()) {
         if (playerStack.itemID == Item.dyePowder.itemID) {
            int iNewColor = BlockCloth.getBlockFromDye(playerStack.getItemDamage());
            if (iNewColor != this.bX()) {
               this.s(iNewColor);
               return true;
            }
         } else if (playerStack.itemID == BTWItems.dung.itemID) {
            int iNewColor = 12;
            if (iNewColor != this.bX()) {
               this.s(iNewColor);
               if (!this.worldObj.isRemote) {
                  this.worldObj.playAuxSFX(2266, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
               }

               return true;
            }
         }
      } else if (playerStack.itemID == Item.bone.itemID && !this.isWildAndHostile()) {
         if (!this.worldObj.isRemote) {
            if (this.rand.nextInt(3) == 0) {
               this.j(true);
               this.a(null);
               this.setAttackTarget(null);
               this.aiSit.setSitting(true);
               this.b(20);
               this.a(player.username);
               this.i(true);
               this.worldObj.setEntityState(this, (byte)7);
            } else {
               this.i(false);
               this.worldObj.setEntityState(this, (byte)6);
            }
         }

         return true;
      }

      return false;
   }

   private void onEat(Item food) {
      this.j(food.getWolfHealAmount());
      int iHungerLevel = this.getHungerLevel();
      if (iHungerLevel > 0) {
         this.setHungerLevel(iHungerLevel - 1);
      }

      this.resetHungerCountdown();
      if (!this.worldObj.isRemote) {
         this.worldObj.playAuxSFX(2284, MathHelper.floor_double(this.posX), (int)(this.posY + this.height), MathHelper.floor_double(this.posZ), 0);
      }

      if (food.itemID == BTWItems.rawMysteryMeat.itemID || food.itemID == BTWItems.cookedMysteryMeat.itemID) {
         this.onEatBreedingItem();
      }

      if (food.itemID == Item.rottenFlesh.itemID) {
         this.onRottenFleshEaten();
      } else if (food.itemID == BTWItems.chocolate.itemID) {
         this.onChocolateEaten();
      }
   }

   @Override
   public void onEatBreedingItem() {
      this.setAngry(false);
      super.onEatBreedingItem();
   }

   private void onChocolateEaten() {
      if (!this.worldObj.isRemote) {
         this.d(new PotionEffect(Potion.wither.id, 800, 0));
         this.worldObj.setEntityState(this, (byte)11);
      }
   }

   private void onRottenFleshEaten() {
      if (this.infectionCountdown < 0) {
         this.infectionCountdown = 12000 + this.rand.nextInt(12000);
      }
   }

   public boolean isDarkEnoughToAffectShitting() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ);
      int lightValue = this.worldObj.getBlockLightValue(i, j, k);
      return lightValue <= 5;
   }

   public boolean attemptToShit() {
      float poopVectorX = MathHelper.sin(this.rotationYawHead / 180.0F * (float) Math.PI);
      float poopVectorZ = -MathHelper.cos(this.rotationYawHead / 180.0F * (float) Math.PI);
      double shitPosX = this.posX + poopVectorX;
      double shitPosY = this.posY + 0.25;
      double shitPosZ = this.posZ + poopVectorZ;
      int shitPosI = MathHelper.floor_double(shitPosX);
      int shitPosJ = MathHelper.floor_double(shitPosY);
      int shitPosK = MathHelper.floor_double(shitPosZ);
      if (!this.isPathToBlockOpenToShitting(shitPosI, shitPosJ, shitPosK)) {
         return false;
      } else {
         EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(
            EntityItem.class, this.worldObj, shitPosX, shitPosY, shitPosZ, new ItemStack(BTWItems.dung)
         );
         float velocityFactor = 0.05F;
         entityitem.motionX = poopVectorX * 10.0F * velocityFactor;
         entityitem.motionZ = poopVectorZ * 10.0F * velocityFactor;
         entityitem.motionY = (float)this.worldObj.rand.nextGaussian() * velocityFactor + 0.2F;
         entityitem.delayBeforeCanPickup = 10;
         this.worldObj.spawnEntityInWorld(entityitem);
         this.worldObj.playSoundAtEntity(this, "random.explode", 0.2F, 1.25F);
         this.worldObj.playSoundAtEntity(this, "mob.wolf.growl", this.ba(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);

         for (int counter = 0; counter < 5; counter++) {
            double smokeX = this.posX + poopVectorX * 0.5F + this.worldObj.rand.nextDouble() * 0.25;
            double smokeY = this.posY + this.worldObj.rand.nextDouble() * 0.5 + 0.25;
            double smokeZ = this.posZ + poopVectorZ * 0.5F + this.worldObj.rand.nextDouble() * 0.25;
            this.worldObj.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
         }

         return true;
      }
   }

   private boolean isPathToBlockOpenToShitting(int i, int j, int k) {
      if (!this.isBlockOpenToShitting(i, j, k)) {
         return false;
      } else {
         int wolfI = MathHelper.floor_double(this.posX);
         int wolfK = MathHelper.floor_double(this.posZ);
         int deltaI = i - wolfI;
         int deltaK = k - wolfK;
         return deltaI == 0 || deltaK == 0 || this.isBlockOpenToShitting(wolfI, j, k) || this.isBlockOpenToShitting(i, j, wolfK);
      }
   }

   private boolean isBlockOpenToShitting(int i, int j, int k) {
      Block block = Block.blocksList[this.worldObj.getBlockId(i, j, k)];
      if (block != null
         && (
            block == Block.waterMoving
               || block == Block.waterStill
               || block == Block.lavaMoving
               || block == Block.lavaStill
               || block == Block.fire
               || block.blockMaterial.isReplaceable()
               || block == BTWBlocks.detectorLogic
               || block == BTWBlocks.glowingDetectorLogic
               || block == BTWBlocks.stokedFire
         )) {
         block = null;
      }

      return block == null;
   }

   private void transformToDire() {
      int iFXI = MathHelper.floor_double(this.posX);
      int iFXJ = MathHelper.floor_double(this.posY) + 1;
      int iFXK = MathHelper.floor_double(this.posZ);
      this.worldObj.func_82739_e(2257, iFXI, iFXJ, iFXK, 0);
      this.w();
      DireWolfEntity direWolf = new DireWolfEntity(this.worldObj);
      direWolf.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      direWolf.renderYawOffset = this.renderYawOffset;
      this.worldObj.spawnEntityInWorld(direWolf);
   }

   public boolean isWildAndHostile() {
      if (!this.m()) {
         if (this.isStarving() || this.bW()) {
            return true;
         }

         int iTimeOfDay = (int)(this.worldObj.worldInfo.getWorldTime() % 24000L);
         if (iTimeOfDay > 13500 && iTimeOfDay < 22500) {
            int iMoonPhase = this.worldObj.getMoonPhase();
            if (iMoonPhase == 0 && this.worldObj.worldInfo.getWorldTime() > 24000L) {
               return true;
            }
         }
      }

      return false;
   }

   public boolean isWildAndHungry() {
      return !this.m() && !this.isFullyFed();
   }

   public boolean isWildAndStarving() {
      return !this.m() && this.isStarving();
   }

   @Override
   public float getGrazeHeadVerticalOffset(float par1) {
      if (this.howlingCountdown > 0) {
         float fTiltFraction = 1.0F;
         if (this.howlingCountdown < 5) {
            fTiltFraction = this.howlingCountdown / 5.0F;
         } else if (this.howlingCountdown > 70) {
            fTiltFraction = (81 - this.howlingCountdown) / 10.0F;
         }

         return !this.n() ? fTiltFraction * -0.5F : fTiltFraction * -0.25F;
      } else {
         return 0.0F;
      }
   }

   @Override
   public float getGrazeHeadRotation(float par1) {
      if (this.howlingCountdown > 0) {
         float fTiltFraction = 1.0F;
         if (this.howlingCountdown < 5) {
            fTiltFraction = this.howlingCountdown / 5.0F;
         } else if (this.howlingCountdown > 70) {
            fTiltFraction = (81 - this.howlingCountdown) / 10.0F;
         }

         return fTiltFraction * (float) (-Math.PI / 5);
      } else {
         return this.rotationPitch / (180.0F / (float)Math.PI);
      }
   }

   public boolean areEyesGlowing() {
      return this.isDoingHeadSpin;
   }

   public float getPossessionHeadRotation() {
      return this.possessionHeadRotation * 2.0F * (float) Math.PI;
   }

   private void updateHeadSpin() {
      if (this.isDoingHeadSpin) {
         this.possessionHeadRotation += 0.008F;
         if (this.possessionHeadRotation >= 1.0F) {
            this.possessionHeadRotation = 0.0F;
            this.isDoingHeadSpin = false;
         }
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      if (this.m()) {
         return this.isStarving() ? "/btwmodtex/fcWolf_tame_starving.png" : "/mob/wolf_tame.png";
      } else if (this.bW()) {
         return "/mob/wolf_angry.png";
      } else {
         return !this.isStarving() && !this.hasAttackTarget() ? this.texture : "/btwmodtex/fcWolf_wild_starving.png";
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte bUpdateType) {
      if (bUpdateType == 10) {
         this.howlingCountdown = 80;
      } else if (bUpdateType == 11) {
         this.d(new PotionEffect(Potion.wither.id, 800, 0));
      } else {
         super.handleHealthUpdate(bUpdateType);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getTailRotation() {
      if (this.isWildAndHostile()) {
         return 1.5393804F;
      } else {
         return this.m() ? (0.55F - (20 - this.dataWatcher.getWatchableObjectInt(18)) * 0.02F) * (float) Math.PI : (float) (Math.PI / 5);
      }
   }

   @Override
   public boolean onBlockDispenserConsume(BlockDispenserBlock blockDispenser, BlockDispenserTileEntity tileEntity) {
      this.worldObj.playAuxSFX(2239, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      this.w();
      InventoryUtils.addSingleItemToInventory(tileEntity, BTWBlocks.companionCube.blockID, 0);

      for (int tempCount = 0; tempCount < 2; tempCount++) {
         blockDispenser.spitOutItem(this.worldObj, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, new ItemStack(Item.silk));
      }

      return true;
   }
}
