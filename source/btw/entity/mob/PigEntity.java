package btw.entity.mob;

import btw.entity.LightningBoltEntity;
import btw.entity.mob.behavior.AnimalFleeBehavior;
import btw.entity.mob.behavior.GrazeBehavior;
import btw.entity.mob.behavior.MoveToGrazeBehavior;
import btw.entity.mob.behavior.MoveToLooseFoodBehavior;
import btw.entity.mob.behavior.MultiTemptBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIFollowParent;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.World;

public class PigEntity extends EntityPig {
   public PigEntity(World world) {
      super(world);
      this.tasks.removeAllTasks();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new AnimalFleeBehavior(this, 0.38F));
      this.tasks.addTask(2, this.n());
      this.tasks.addTask(3, new EntityAIMate(this, 0.25F));
      this.tasks.addTask(4, new MultiTemptBehavior(this, 0.3F));
      this.tasks.addTask(5, new GrazeBehavior(this));
      this.tasks.addTask(6, new MoveToLooseFoodBehavior(this, 0.25F));
      this.tasks.addTask(7, new MoveToGrazeBehavior(this, 0.25F));
      this.tasks.addTask(8, new EntityAIFollowParent(this, 0.28F));
      this.tasks.addTask(9, new SimpleWanderBehavior(this, 0.25F));
      this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(11, new EntityAILookIdle(this));
   }

   @Override
   public boolean isAIEnabled() {
      return !this.getWearingBreedingHarness();
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (!this.isStarving() && !this.hasHeadCrabbedSquid()) {
         int iNumDrops = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + iLootingModifier);
         if (this.isFamished()) {
            iNumDrops /= 2;
         }

         for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
            if (this.ae()) {
               if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
                  this.b(Item.porkCooked.itemID, 1);
               } else {
                  this.b(BTWItems.burnedMeat.itemID, 1);
               }
            } else {
               this.b(Item.porkRaw.itemID, 1);
            }
         }
      }

      if (this.m()) {
         this.b(Item.saddle.itemID, 1);
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource damageSource, int par2) {
      if (this.aq()) {
         return false;
      } else {
         Entity attackingEntity = damageSource.getEntity();
         if (attackingEntity != null && attackingEntity instanceof EntityPlayer) {
            EntityPlayer attackingPlayer = (EntityPlayer)attackingEntity;

            for (ZombiePigmanEntity tempPigman : this.worldObj.getEntitiesWithinAABB(ZombiePigmanEntity.class, this.boundingBox.expand(16.0, 8.0, 16.0))) {
               if (!tempPigman.isLivingDead) {
                  tempPigman.becomeAngryWhenPigAttacked(attackingPlayer);
               }
            }
         }

         return super.a(damageSource, par2);
      }
   }

   @Override
   public double getMountedYOffset() {
      return this.hasHeadCrabbedSquid() ? this.height * 1.2 : super.W();
   }

   @Override
   public boolean isBreedingItem(ItemStack itemStack) {
      return itemStack.itemID == BTWItems.chocolate.itemID;
   }

   @Override
   public boolean isAffectedByMovementModifiers() {
      return false;
   }

   @Override
   public boolean getCanCreatureTypeBePossessed() {
      return true;
   }

   @Override
   public void onFullPossession() {
      this.worldObj.playAuxSFX(2259, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
      this.w();
      ZombiePigmanEntity entityPigman = (ZombiePigmanEntity)EntityList.createEntityOfType(ZombiePigmanEntity.class, this.worldObj);
      entityPigman.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      entityPigman.renderYawOffset = this.renderYawOffset;
      entityPigman.setPersistent(true);
      entityPigman.h(true);
      this.worldObj.spawnEntityInWorld(entityPigman);
   }

   @Override
   public boolean isValidZombieSecondaryTarget(EntityZombie zombie) {
      return true;
   }

   public PigEntity spawnBabyAnimal(EntityAgeable parent) {
      return (PigEntity)EntityList.createEntityOfType(PigEntity.class, this.worldObj);
   }

   @Override
   public void onStruckByLightning(LightningBoltEntity bolt) {
      if (!this.worldObj.isRemote) {
         ZombiePigmanEntity pigman = (ZombiePigmanEntity)EntityList.createEntityOfType(ZombiePigmanEntity.class, this.worldObj);
         pigman.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
         this.worldObj.spawnEntityInWorld(pigman);
         this.w();
      }
   }

   @Override
   protected String getLivingSound() {
      return !this.isStarving() ? "mob.pig.say" : "mob.pig.death";
   }

   @Override
   public boolean isSubjectToHunger() {
      return true;
   }

   @Override
   public int getFoodValueMultiplier() {
      return 4;
   }

   @Override
   public boolean getDisruptsEarthOnGraze() {
      return true;
   }

   @Override
   public boolean canGrazeOnRoughVegetation() {
      return true;
   }

   @Override
   public int getGrazeDuration() {
      return 80;
   }

   @Override
   public int getItemFoodValue(ItemStack stack) {
      return stack.getItem().getPigFoodValue(stack.getItemDamage()) * this.getFoodValueMultiplier();
   }

   @Override
   public float getGrazeHeadRotationMagnitudeDivisor() {
      return 3.0F;
   }

   @Override
   public float getGrazeHeadRotationRateMultiplier() {
      return 50.225002F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      if (this.getWearingBreedingHarness()) {
         return "/btwmodtex/fc_mr_pig.png";
      } else {
         int iHungerLevel = this.getHungerLevel();
         if (iHungerLevel == 1) {
            return "/btwmodtex/fcPigFamished.png";
         } else {
            return iHungerLevel == 2 ? "/btwmodtex/fcPigStarving.png" : super.N();
         }
      }
   }
}
