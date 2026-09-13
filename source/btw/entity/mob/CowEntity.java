package btw.entity.mob;

import btw.entity.mob.behavior.AnimalFleeBehavior;
import btw.entity.mob.behavior.GrazeBehavior;
import btw.entity.mob.behavior.MoveToGrazeBehavior;
import btw.entity.mob.behavior.MoveToLooseFoodBehavior;
import btw.entity.mob.behavior.MultiTemptBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.item.BTWItems;
import btw.world.util.WorldUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIFollowParent;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;
import net.minecraft.src.WorldServer;

public class CowEntity extends EntityCow {
   protected static final int GOT_MILK_DATA_WATCHER_ID = 26;
   private static final int FULL_MILK_ACCUMULATION_COUNT = 24000;
   private static final int KICK_ATTACK_TICKS_TO_COOLDOWN = 40;
   private static final double KICK_ATTACK_RANGE = 1.75;
   public static final int KICK_ATTACK_DURATION = 20;
   public static final double KICK_ATTACK_TIP_COLLISION_WIDTH = 2.75;
   public static final double KICK_ATTACK_TIP_COLLISION_HALF_WIDTH = 1.375;
   public static final double KICK_ATTACK_TIP_COLLISION_HEIGHT = 2.0;
   public static final double KICK_ATTACK_TIP_COLLISION_HALF_HEIGHT = 1.0;
   private int milkAccumulationCount = 0;
   private int kickAttackCooldownTimer = 40;
   public int kickAttackInProgressCounter = -1;
   public int kickAttackLegUsed = 0;

   public CowEntity(World world) {
      super(world);
      this.tasks.removeAllTasks();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new AnimalFleeBehavior(this, 0.38F));
      this.tasks.addTask(2, new EntityAIMate(this, 0.2F));
      this.tasks.addTask(3, new MultiTemptBehavior(this, 0.25F));
      this.tasks.addTask(4, new GrazeBehavior(this));
      this.tasks.addTask(5, new MoveToLooseFoodBehavior(this, 0.2F));
      this.tasks.addTask(6, new MoveToGrazeBehavior(this, 0.2F));
      this.tasks.addTask(7, new EntityAIFollowParent(this, 0.25F));
      this.tasks.addTask(8, new SimpleWanderBehavior(this, 0.25F));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(10, new EntityAILookIdle(this));
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(26, new Byte((byte)0));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      par1NBTTagCompound.setBoolean("fcGotMilk", this.gotMilk());
      par1NBTTagCompound.setInteger("fcMilkCount", this.milkAccumulationCount);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      if (par1NBTTagCompound.hasKey("fcGotMilk")) {
         this.setGotMilk(par1NBTTagCompound.getBoolean("fcGotMilk"));
      }

      if (par1NBTTagCompound.hasKey("fcMilkCount")) {
         this.milkAccumulationCount = par1NBTTagCompound.getInteger("fcMilkCount");
      }
   }

   @Override
   public boolean isAIEnabled() {
      return !this.getWearingBreedingHarness();
   }

   @Override
   public int getMaxHealth() {
      return 15;
   }

   @Override
   public void onLivingUpdate() {
      this.updateKickAttack();
      super.c();
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (!this.isStarving()) {
         int iNumDrops = this.rand.nextInt(3) + this.rand.nextInt(1 + iLootingModifier) + 1;
         if (this.isFamished()) {
            iNumDrops /= 2;
         }

         for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
            this.b(Item.leather.itemID, 1);
         }

         if (!this.hasHeadCrabbedSquid()) {
            iNumDrops = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + iLootingModifier);
            if (this.isFamished()) {
               iNumDrops /= 2;
            }

            for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
               if (this.ae()) {
                  if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
                     this.b(Item.beefCooked.itemID, 1);
                  } else {
                     this.b(BTWItems.burnedMeat.itemID, 1);
                  }
               } else {
                  this.b(Item.beefRaw.itemID, 1);
               }
            }
         }
      }
   }

   @Override
   public boolean isBreedingItem(ItemStack stack) {
      return stack.itemID == Item.cake.itemID;
   }

   @Override
   public boolean interact(EntityPlayer player) {
      ItemStack stack = player.inventory.getCurrentItem();
      if (stack != null && stack.itemID == Item.bucketEmpty.itemID) {
         if (this.gotMilk()) {
            stack.stackSize--;
            if (stack.stackSize <= 0) {
               player.inventory.setInventorySlotContents(player.inventory.currentItem, new ItemStack(Item.bucketMilk));
            } else if (!player.inventory.addItemStackToInventory(new ItemStack(Item.bucketMilk))) {
               player.dropPlayerItem(new ItemStack(Item.bucketMilk.itemID, 1, 0));
            }

            this.a(DamageSource.generic, 0);
            if (!this.worldObj.isRemote) {
               this.setGotMilk(false);
               this.worldObj.playAuxSFX(2254, MathHelper.floor_double(this.posX), (int)this.posY, MathHelper.floor_double(this.posZ), 0);
            }
         } else if (this.worldObj.getDifficulty().canMilkingStartleCows()) {
            this.a(DamageSource.causePlayerDamage(player), 0);
         }

         return true;
      } else {
         return this.entityAnimalInteract(player);
      }
   }

   @Override
   public void onGrazeBlock(int i, int j, int k) {
      super.onGrazeBlock(i, j, k);
      if (!this.getWearingBreedingHarness()) {
         this.checkForGrazeSideEffects(i, j, k);
      }
   }

   @Override
   public boolean isSubjectToHunger() {
      return true;
   }

   @Override
   public void onBecomeFamished() {
      super.onBecomeFamished();
      if (this.gotMilk()) {
         this.setGotMilk(false);
      }

      this.milkAccumulationCount = 0;
   }

   @Override
   public boolean canGrazeMycelium() {
      return true;
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 1.2;
   }

   @Override
   public boolean getCanCreatureTypeBePossessed() {
      return true;
   }

   @Override
   protected void giveBirthAtTargetLocation(EntityAnimal targetMate, double dChildX, double dChildY, double dChildZ) {
      if ((this.isFullyPossessed() || targetMate.isFullyPossessed()) && this.rand.nextInt(8) != 0) {
         if (this.worldObj.provider.dimensionId != 1 && this.worldObj.rand.nextInt(2) == 0) {
            this.birthMutant(targetMate, dChildX, dChildY, dChildZ);
         } else {
            this.stillBirth(targetMate, dChildX, dChildY, dChildZ);
         }
      } else {
         super.giveBirthAtTargetLocation(targetMate, dChildX, dChildY, dChildZ);
      }
   }

   @Override
   public void initCreature() {
      this.initHungerWithVariance();
      if (!this.h_()) {
         this.milkAccumulationCount = this.worldObj.rand.nextInt(30001);
         if (this.milkAccumulationCount >= 24000) {
            this.milkAccumulationCount = 0;
            this.setGotMilk(true);
         }
      }
   }

   @Override
   public boolean isValidZombieSecondaryTarget(EntityZombie zombie) {
      return true;
   }

   public CowEntity spawnBabyAnimal(EntityAgeable parent) {
      return (CowEntity)EntityList.createEntityOfType(CowEntity.class, this.worldObj);
   }

   @Override
   protected String getLivingSound() {
      return !this.isStarving() ? "mob.cow.say" : "mob.cow.hurt";
   }

   @Override
   public void updateHungerState() {
      if (!this.gotMilk() && this.isFullyFed() && !this.h_() && !this.getWearingBreedingHarness()) {
         this.hungerCountdown--;
         this.milkAccumulationCount++;
         if (this.milkAccumulationCount >= 24000) {
            this.setGotMilk(true);
            this.milkAccumulationCount = 0;
            this.worldObj.playAuxSFX(2253, MathHelper.floor_double(this.posX), (int)this.posY + 1, MathHelper.floor_double(this.posZ), 0);
         }
      }

      super.updateHungerState();
   }

   @Override
   public float knockbackMagnitude() {
      return 0.3F;
   }

   public void checkForGrazeSideEffects(int i, int j, int k) {
      int iTargetBlockID = this.worldObj.getBlockId(i, j, k);
      if (iTargetBlockID == Block.mycelium.blockID) {
         this.convertToMooshroom();
      }
   }

   public void convertToMooshroom() {
      int iFXI = MathHelper.floor_double(this.posX);
      int iFXJ = MathHelper.floor_double(this.posY) + 1;
      int iFXK = MathHelper.floor_double(this.posZ);
      int iExtendedFXData = 0;
      if (this.h_()) {
         iExtendedFXData = 1;
      }

      this.worldObj.playAuxSFX(2255, iFXI, iFXJ, iFXK, iExtendedFXData);
      this.w();
      CowEntity entityMooshroom = (CowEntity)EntityList.createEntityOfType(EntityMooshroom.class, this.worldObj);
      entityMooshroom.b(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
      entityMooshroom.b(this.aX());
      entityMooshroom.renderYawOffset = this.renderYawOffset;
      entityMooshroom.a(this.b());
      this.worldObj.spawnEntityInWorld(entityMooshroom);
   }

   public boolean gotMilk() {
      byte bGotMilk = this.dataWatcher.getWatchableObjectByte(26);
      return bGotMilk != 0;
   }

   protected void setGotMilk(boolean bGotMilk) {
      byte byteValue = 0;
      if (bGotMilk) {
         byteValue = 1;
      }

      this.dataWatcher.updateObject(26, byteValue);
   }

   private void updateKickAttack() {
      if (this.kickAttackInProgressCounter >= 0) {
         this.kickAttackInProgressCounter++;
         if (this.kickAttackInProgressCounter >= 20) {
            this.kickAttackInProgressCounter = -1;
         }
      } else if (!this.worldObj.isRemote) {
         this.kickAttackCooldownTimer--;
         if (this.R() && !this.h_() && !this.getWearingBreedingHarness() && this.kickAttackCooldownTimer <= 0 && (this.ae() || this.aF() != null)) {
            Vec3 kickCenter = this.computeKickAttackCenter();
            AxisAlignedBB tipBox = AxisAlignedBB.getAABBPool()
               .getAABB(
                  kickCenter.xCoord - 1.375,
                  kickCenter.yCoord - 1.0,
                  kickCenter.zCoord - 1.375,
                  kickCenter.xCoord + 1.375,
                  kickCenter.yCoord + 1.0,
                  kickCenter.zCoord + 1.375
               );
            List potentialCollisionList = this.worldObj.getEntitiesWithinAABB(EntityLiving.class, tipBox);
            if (!potentialCollisionList.isEmpty()) {
               boolean bAttackLaunched = false;
               Vec3 lineOfSightOrigin = Vec3.createVectorHelper(this.posX, this.posY + this.height / 2.0F, this.posZ);

               for (EntityLiving tempEntity : potentialCollisionList) {
                  if (!(tempEntity instanceof CowEntity)
                     && tempEntity.isEntityAlive()
                     && tempEntity.ridingEntity != this
                     && this.canEntityBeSeenForAttackToCenterOfMass(tempEntity, lineOfSightOrigin)) {
                     bAttackLaunched = true;
                     this.kickAttackHitTarget(tempEntity);
                  }
               }

               if (bAttackLaunched) {
                  this.launchKickAttack();
               }
            }
         }
      }
   }

   public boolean canEntityBeSeenForAttackToCenterOfMass(Entity entity, Vec3 attackOrigin) {
      return this.worldObj
            .rayTraceBlocks_do_do(
               attackOrigin, this.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY + entity.height / 2.0F, entity.posZ), false, true
            )
         == null;
   }

   public Vec3 computeKickAttackCenter() {
      float fAttackAngle = MathHelper.wrapAngleTo180_float(this.rotationYaw + 180.0F);
      double dPosX = -MathHelper.sin(fAttackAngle / 180.0F * (float) Math.PI) * 1.75;
      double dPosY = this.height / 2.0F;
      double dPosZ = MathHelper.cos(fAttackAngle / 180.0F * (float) Math.PI) * 1.75;
      dPosX += this.posX;
      dPosY += this.posY;
      dPosZ += this.posZ;
      return Vec3.createVectorHelper(dPosX, dPosY, dPosZ);
   }

   private void launchKickAttack() {
      this.kickAttackInProgressCounter = 0;
      this.kickAttackCooldownTimer = 40;
      this.transmitKickAttackToClients();
   }

   private void transmitKickAttackToClients() {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      DataOutputStream dataStream = new DataOutputStream(byteStream);

      try {
         dataStream.writeInt(this.entityId);
         dataStream.writeByte(2);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      Packet250CustomPayload packet = new Packet250CustomPayload("BTW|EV", byteStream.toByteArray());
      WorldUtils.sendPacketToAllPlayersTrackingEntity((WorldServer)this.worldObj, this, packet);
   }

   public void onClientNotifiedOfKickAttack() {
      this.kickAttackInProgressCounter = 0;
      this.kickAttackLegUsed = this.rand.nextInt(2);
      this.worldObj.playSound(this.posX, this.posY, this.posZ, "random.bow", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.5F);
   }

   private void kickAttackHitTarget(Entity hitEntity) {
      DamageSource cowSource = DamageSource.causeMobDamage(this);
      int kickDamage = 7;
      if (hitEntity instanceof EntityPlayer) {
         kickDamage = (int)(kickDamage * this.worldObj.getDifficulty().getCowKickStrengthMultiplier());
      }

      if (hitEntity.attackEntityFrom(cowSource, kickDamage)) {
         if (this.ae() && this.rand.nextFloat() < 0.6F) {
            hitEntity.setFire(4);
         }

         hitEntity.onKickedByCow(this);
      }
   }

   private boolean birthMutant(EntityAnimal targetMate, double dChildX, double dChildY, double dChildZ) {
      int iRandomFactor = this.rand.nextInt(20);
      if (iRandomFactor == 0) {
         CaveSpiderEntity childEntity = (CaveSpiderEntity)EntityList.createEntityOfType(CaveSpiderEntity.class, this.worldObj);
         if (childEntity != null) {
            childEntity.b(dChildX, dChildY, dChildZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(childEntity);
         }
      } else if (iRandomFactor < 4) {
         for (int iTempCount = 0; iTempCount < 10; iTempCount++) {
            BatEntity childEntity = (BatEntity)EntityList.createEntityOfType(BatEntity.class, this.worldObj);
            if (childEntity != null) {
               childEntity.b(dChildX, dChildY, dChildZ, this.rotationYaw, this.rotationPitch);
               this.worldObj.spawnEntityInWorld(childEntity);
            }
         }
      } else if (iRandomFactor < 7) {
         for (int iTempCountx = 0; iTempCountx < 5; iTempCountx++) {
            EntitySilverfish childEntity = (EntitySilverfish)EntityList.createEntityOfType(EntitySilverfish.class, this.worldObj);
            if (childEntity != null) {
               childEntity.b(dChildX, dChildY, dChildZ, this.rotationYaw, this.rotationPitch);
               this.worldObj.spawnEntityInWorld(childEntity);
            }
         }
      } else {
         SquidEntity childEntity = (SquidEntity)EntityList.createEntityOfType(SquidEntity.class, this.worldObj);
         if (childEntity != null) {
            childEntity.b(dChildX, dChildY, dChildZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(childEntity);
         }
      }

      return true;
   }

   protected void stillBirth(EntityAnimal targetMate, double dChildX, double dChildY, double dChildZ) {
      EntityAgeable childEntity = this.a(targetMate);
      if (childEntity != null) {
         childEntity.setGrowingAge(-childEntity.getTicksForChildToGrow());
         childEntity.b(dChildX, dChildY, dChildZ, this.rotationYaw, this.rotationPitch);
         this.worldObj.spawnEntityInWorld(childEntity);
         childEntity.a(DamageSource.generic, 20);
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      if (this.getWearingBreedingHarness()) {
         return "/btwmodtex/fc_mr_cow.png";
      } else {
         int iHungerLevel = this.getHungerLevel();
         if (iHungerLevel == 1) {
            return "/btwmodtex/fcCowFamished.png";
         } else {
            return iHungerLevel == 2 ? "/btwmodtex/fcCowStarving.png" : super.N();
         }
      }
   }
}
