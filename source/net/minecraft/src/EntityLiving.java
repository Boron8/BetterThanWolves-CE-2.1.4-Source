package net.minecraft.src;

import btw.block.tileentity.beacon.BeaconTileEntity;
import btw.entity.mob.CreeperEntity;
import btw.entity.mob.GhastEntity;
import btw.entity.mob.SquidEntity;
import btw.item.BTWItems;
import btw.util.CustomDamageSource;
import btw.world.util.WorldUtils;
import com.prupe.mcpatcher.mob.MobRandomizer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.MinecraftServer;

public abstract class EntityLiving extends Entity {
   private static final float[] enchantmentProbability = new float[]{0.05F, 0.05F, 0.05F, 0.05F};
   private static final float[] armorEnchantmentProbability = new float[]{0.05F, 0.05F, 0.05F, 0.05F};
   private static final float[] armorProbability = new float[]{0.0025F, 0.0025F, 0.0025F, 0.0025F};
   public static final float[] pickUpLootProability = new float[]{0.15F, 0.15F, 0.15F, 0.15F};
   public int maxHurtResistantTime = 20;
   public float field_70769_ao;
   public float field_70770_ap;
   public float renderYawOffset = 0.0F;
   public float prevRenderYawOffset = 0.0F;
   public float rotationYawHead = 0.0F;
   public float prevRotationYawHead = 0.0F;
   protected float field_70768_au;
   protected float field_70766_av;
   protected float field_70764_aw;
   protected float field_70763_ax;
   protected boolean field_70753_ay = true;
   protected String texture = "/mob/char.png";
   protected boolean field_70740_aA = true;
   protected float field_70741_aB = 0.0F;
   protected String entityType = null;
   protected float field_70743_aD = 1.0F;
   protected int scoreValue = 0;
   protected float field_70745_aF = 0.0F;
   public float landMovementFactor = 0.1F;
   public float jumpMovementFactor = 0.02F;
   public float prevSwingProgress;
   public float swingProgress;
   public int health = this.getMaxHealth();
   public int prevHealth;
   protected int carryoverDamage;
   public int livingSoundTime;
   public int hurtTime;
   public int maxHurtTime;
   public float attackedAtYaw = 0.0F;
   public int deathTime = 0;
   public int attackTime = 0;
   public float prevCameraPitch;
   public float cameraPitch;
   public boolean isLivingDead = false;
   protected int experienceValue;
   public int field_70731_aW = -1;
   public float field_70730_aX = (float)(Math.random() * 0.9F + 0.1F);
   public float prevLimbYaw;
   public float limbYaw;
   public float limbSwing;
   protected EntityPlayer attackingPlayer = null;
   protected int recentlyHit = 0;
   public EntityLiving entityLivingToAttack = null;
   public int revengeTimer = 0;
   private EntityLiving lastAttackingEntity = null;
   public int arrowHitTimer = 0;
   protected HashMap activePotionsMap = new HashMap();
   private boolean potionsNeedUpdate = true;
   private int field_70748_f;
   private EntityLookHelper lookHelper;
   private EntityMoveHelper moveHelper;
   private EntityJumpHelper jumpHelper;
   private EntityBodyHelper bodyHelper;
   private PathNavigate navigator;
   protected final EntityAITasks tasks;
   protected final EntityAITasks targetTasks;
   private EntityLiving attackTarget;
   protected static final int HAS_ATTACK_TARGET_DATA_WATCHER_ID = 11;
   private EntitySenses senses;
   private float AIMoveSpeed;
   private ChunkCoordinates homePosition = new ChunkCoordinates(0, 0, 0);
   private float maximumHomeDistance = -1.0F;
   private ItemStack[] equipment = new ItemStack[5];
   protected float[] equipmentDropChances = new float[5];
   private ItemStack[] previousEquipment = new ItemStack[5];
   public boolean isSwingInProgress = false;
   public int swingProgressInt = 0;
   private boolean canPickUpLoot = false;
   private boolean persistenceRequired = false;
   protected final CombatTracker field_94063_bt = new CombatTracker(this);
   protected int newPosRotationIncrements;
   protected double newPosX;
   protected double newPosY;
   protected double newPosZ;
   protected double newRotationYaw;
   protected double newRotationPitch;
   float field_70706_bo = 0.0F;
   protected int lastDamage = 0;
   protected int entityAge = 0;
   protected float moveStrafing;
   protected float moveForward;
   protected float randomYawVelocity;
   protected boolean isJumping = false;
   protected float defaultPitch = 0.0F;
   public float moveSpeed = 0.7F;
   private int jumpTicks = 0;
   private Entity currentTarget;
   protected int numTicksToChaseTarget = 0;
   private int recentlyOnChoppingBlockCountdown = 0;
   public static final int onChoppingBlockMaxCountdown = 40;

   public EntityLiving(World par1World) {
      super(par1World);
      this.preventEntitySpawning = true;
      this.tasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
      this.targetTasks = new EntityAITasks(par1World != null && par1World.theProfiler != null ? par1World.theProfiler : null);
      this.lookHelper = new EntityLookHelper(this);
      this.moveHelper = new EntityMoveHelper(this);
      this.jumpHelper = new EntityJumpHelper(this);
      this.bodyHelper = new EntityBodyHelper(this);
      this.navigator = new PathNavigate(this, par1World, this.func_96121_ay());
      this.senses = new EntitySenses(this);
      this.field_70770_ap = (float)(Math.random() + 1.0) * 0.01F;
      this.b(this.posX, this.posY, this.posZ);
      this.field_70769_ao = (float)Math.random() * 12398.0F;
      this.rotationYaw = (float)(Math.random() * Math.PI * 2.0);
      this.rotationYawHead = this.rotationYaw;

      for (int var2 = 0; var2 < this.equipmentDropChances.length; var2++) {
         this.equipmentDropChances[var2] = 0.085F;
      }

      this.stepHeight = 0.5F;
      this.livingSoundTime = -this.getTalkInterval();
   }

   protected int func_96121_ay() {
      return 16;
   }

   public EntityLookHelper getLookHelper() {
      return this.lookHelper;
   }

   public EntityMoveHelper getMoveHelper() {
      return this.moveHelper;
   }

   public EntityJumpHelper getJumpHelper() {
      return this.jumpHelper;
   }

   public PathNavigate getNavigator() {
      return this.navigator;
   }

   public EntitySenses getEntitySenses() {
      return this.senses;
   }

   public Random getRNG() {
      return this.rand;
   }

   public EntityLiving getAITarget() {
      return this.entityLivingToAttack;
   }

   public EntityLiving getLastAttackingEntity() {
      return this.lastAttackingEntity;
   }

   public void setLastAttackingEntity(Entity par1Entity) {
      if (par1Entity instanceof EntityLiving) {
         this.lastAttackingEntity = (EntityLiving)par1Entity;
      }
   }

   public int getAge() {
      return this.entityAge;
   }

   @Override
   public float getRotationYawHead() {
      return this.rotationYawHead;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setRotationYawHead(float par1) {
      this.rotationYawHead = par1;
   }

   public float getAIMoveSpeed() {
      return this.AIMoveSpeed;
   }

   public void setAIMoveSpeed(float par1) {
      this.AIMoveSpeed = par1;
      this.setMoveForward(par1);
   }

   public boolean attackEntityAsMob(Entity par1Entity) {
      return this.meleeAttack(par1Entity);
   }

   public EntityLiving getAttackTarget() {
      return this.attackTarget;
   }

   public boolean hasAttackTarget() {
      return this.dataWatcher.getWatchableObjectByte(11) == 1;
   }

   public void setAttackTarget(EntityLiving par1EntityLiving) {
      this.entityLivingSetAttackTarget(par1EntityLiving);
   }

   protected void entityLivingSetAttackTarget(EntityLiving par1EntityLiving) {
      this.attackTarget = par1EntityLiving;
      if (par1EntityLiving != null) {
         this.dataWatcher.updateObject(11, (byte)1);
      } else {
         this.dataWatcher.updateObject(11, (byte)0);
      }
   }

   public boolean canAttackClass(Class par1Class) {
      return CreeperEntity.class != par1Class && GhastEntity.class != par1Class;
   }

   public void eatGrassBonus() {
   }

   @Override
   protected void updateFallState(double par1, boolean par3) {
      if (!this.G()) {
         this.H();
      }

      if (par3 && this.fallDistance > 0.0F) {
         int var4 = MathHelper.floor_double(this.posX);
         int var5 = MathHelper.floor_double(this.posY - 0.2F - this.yOffset);
         int var6 = MathHelper.floor_double(this.posZ);
         int var7 = this.worldObj.getBlockId(var4, var5, var6);
         if (var7 == 0) {
            int var8 = this.worldObj.blockGetRenderType(var4, var5 - 1, var6);
            if (var8 == 11 || var8 == 32 || var8 == 21) {
               var7 = this.worldObj.getBlockId(var4, var5 - 1, var6);
            }
         }

         if (var7 > 0) {
            Block.blocksList[var7].onFallenUpon(this.worldObj, var4, var5, var6, this, this.fallDistance);
         }
      }

      super.updateFallState(par1, par3);
   }

   public boolean isWithinHomeDistanceCurrentPosition() {
      return this.isWithinHomeDistance(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
   }

   public boolean isWithinHomeDistance(int par1, int par2, int par3) {
      return this.maximumHomeDistance == -1.0F
         ? true
         : this.homePosition.getDistanceSquared(par1, par2, par3) < this.maximumHomeDistance * this.maximumHomeDistance;
   }

   public void setHomeArea(int par1, int par2, int par3, int par4) {
      this.homePosition.set(par1, par2, par3);
      this.maximumHomeDistance = par4;
   }

   public ChunkCoordinates getHomePosition() {
      return this.homePosition;
   }

   public float getMaximumHomeDistance() {
      return this.maximumHomeDistance;
   }

   public void detachHome() {
      this.maximumHomeDistance = -1.0F;
   }

   public boolean hasHome() {
      return this.maximumHomeDistance != -1.0F;
   }

   public void setRevengeTarget(EntityLiving par1EntityLiving) {
      this.entityLivingToAttack = par1EntityLiving;
      this.revengeTimer = this.entityLivingToAttack != null ? 100 : 0;
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(8, this.field_70748_f);
      this.dataWatcher.addObject(9, (byte)0);
      this.dataWatcher.addObject(10, (byte)0);
      this.dataWatcher.addObject(6, (byte)0);
      this.dataWatcher.addObject(5, "");
      this.dataWatcher.addObject(11, (byte)0);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      return this.texture;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public boolean canBePushed() {
      return !this.isDead;
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.85F;
   }

   public int getTalkInterval() {
      return 80;
   }

   public void playLivingSound() {
      String var1 = this.getLivingSound();
      if (var1 != null) {
         this.a(var1, this.getSoundVolume(), this.getSoundPitch());
      }
   }

   @Override
   public void onEntityUpdate() {
      this.prevSwingProgress = this.swingProgress;
      super.onEntityUpdate();
      this.worldObj.theProfiler.startSection("mobBaseTick");
      if (this.isEntityAlive() && this.rand.nextInt(1000) < this.livingSoundTime++) {
         this.livingSoundTime = -this.getTalkInterval();
         this.playLivingSound();
      }

      if (this.isEntityAlive() && this.S()) {
         this.attackEntityFrom(DamageSource.inWall, 1);
      }

      if (this.E() || this.worldObj.isRemote) {
         this.A();
      }

      boolean var1 = this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.disableDamage;
      if (this.isEntityAlive()
         && this.a(Material.water)
         && !this.canBreatheUnderwater()
         && !this.activePotionsMap.containsKey(Potion.waterBreathing.id)
         && !var1) {
         this.g(this.decreaseAirSupply(this.ak()));
         if (this.ak() == -20) {
            this.g(0);

            for (int var2 = 0; var2 < 8; var2++) {
               float var3 = this.rand.nextFloat() - this.rand.nextFloat();
               float var4 = this.rand.nextFloat() - this.rand.nextFloat();
               float var5 = this.rand.nextFloat() - this.rand.nextFloat();
               this.worldObj.spawnParticle("bubble", this.posX + var3, this.posY + var4, this.posZ + var5, this.motionX, this.motionY, this.motionZ);
            }

            this.attackEntityFrom(DamageSource.drown, 2);
         }

         this.A();
      } else {
         this.recoverAirSupply();
      }

      this.prevCameraPitch = this.cameraPitch;
      if (this.attackTime > 0) {
         this.attackTime--;
      }

      if (this.hurtTime > 0) {
         this.hurtTime--;
      }

      if (this.hurtResistantTime > 0) {
         this.hurtResistantTime--;
      }

      if (this.health <= 0) {
         this.onDeathUpdate();
      }

      if (this.recentlyHit > 0) {
         this.recentlyHit--;
      } else {
         this.attackingPlayer = null;
      }

      if (this.lastAttackingEntity != null && !this.lastAttackingEntity.isEntityAlive()) {
         this.lastAttackingEntity = null;
      }

      if (this.entityLivingToAttack != null) {
         if (!this.entityLivingToAttack.isEntityAlive()) {
            this.setRevengeTarget((EntityLiving)null);
         } else if (this.revengeTimer > 0) {
            this.revengeTimer--;
         } else {
            this.setRevengeTarget((EntityLiving)null);
         }
      }

      if (this.attackTarget != null && !this.attackTarget.isEntityAlive()) {
         this.setAttackTarget(null);
      }

      this.updatePotionEffects();
      this.field_70763_ax = this.field_70764_aw;
      this.prevRenderYawOffset = this.renderYawOffset;
      this.prevRotationYawHead = this.rotationYawHead;
      this.prevRotationYaw = this.rotationYaw;
      this.prevRotationPitch = this.rotationPitch;
      this.worldObj.theProfiler.endSection();
   }

   protected void onDeathUpdate() {
      this.deathTime++;
      if (this.deathTime == 20) {
         if (!this.worldObj.isRemote
            && (this.recentlyHit > 0 || this.isPlayer())
            && !this.isChild()
            && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
            int var1 = this.getExperiencePoints(this.attackingPlayer);

            while (var1 > 0) {
               int var2 = EntityXPOrb.getXPSplit(var1);
               var1 -= var2;
               this.worldObj.spawnEntityInWorld(EntityList.createEntityOfType(EntityXPOrb.class, this.worldObj, this.posX, this.posY, this.posZ, var2));
            }
         } else if (!this.worldObj.isRemote && !this.isChild()) {
            int iExperienceDropped = this.getExperiencePoints(this.attackingPlayer);
            if (iExperienceDropped > 0) {
               this.worldObj
                  .spawnEntityInWorld(
                     EntityList.createEntityOfType(
                        EntityXPOrb.class, this.worldObj, this.posX, this.posY, this.posZ, this.getExperiencePoints(this.attackingPlayer), true
                     )
                  );
            }
         }

         this.w();

         for (int var1 = 0; var1 < 20; var1++) {
            double var8 = this.rand.nextGaussian() * 0.02;
            double var4 = this.rand.nextGaussian() * 0.02;
            double var6 = this.rand.nextGaussian() * 0.02;
            this.worldObj
               .spawnParticle(
                  "explode",
                  this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  this.posY + this.rand.nextFloat() * this.height,
                  this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width,
                  var8,
                  var4,
                  var6
               );
         }
      }
   }

   protected int decreaseAirSupply(int par1) {
      int var2 = EnchantmentHelper.getRespiration(this);
      return var2 > 0 && this.rand.nextInt(var2 + 1) > 0 ? par1 : par1 - 1;
   }

   protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
      if (this.experienceValue > 0) {
         int var2 = this.experienceValue;
         ItemStack[] var3 = this.getLastActiveItems();

         for (int var4 = 0; var4 < var3.length; var4++) {
            if (var3[var4] != null && this.equipmentDropChances[var4] <= 1.0F) {
               var2 += 1 + this.rand.nextInt(3);
            }
         }

         return var2;
      } else {
         return this.experienceValue;
      }
   }

   protected boolean isPlayer() {
      return false;
   }

   public void spawnExplosionParticle() {
      for (int var1 = 0; var1 < 20; var1++) {
         double var2 = this.rand.nextGaussian() * 0.02;
         double var4 = this.rand.nextGaussian() * 0.02;
         double var6 = this.rand.nextGaussian() * 0.02;
         double var8 = 10.0;
         this.worldObj
            .spawnParticle(
               "explode",
               this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width - var2 * var8,
               this.posY + this.rand.nextFloat() * this.height - var4 * var8,
               this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width - var6 * var8,
               var2,
               var4,
               var6
            );
      }
   }

   @Override
   public void updateRidden() {
      super.updateRidden();
      this.field_70768_au = this.field_70766_av;
      this.field_70766_av = 0.0F;
      this.fallDistance = 0.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
      this.yOffset = 0.0F;
      this.newPosX = par1;
      this.newPosY = par3;
      this.newPosZ = par5;
      this.newRotationYaw = par7;
      this.newRotationPitch = par8;
      this.newPosRotationIncrements = par9;
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote) {
         for (int var1 = 0; var1 < 5; var1++) {
            ItemStack var2 = this.getCurrentItemOrArmor(var1);
            if (!ItemStack.areItemStacksEqual(var2, this.previousEquipment[var1])) {
               ((WorldServer)this.worldObj)
                  .getEntityTracker()
                  .sendPacketToAllPlayersTrackingEntity(this, new Packet5PlayerInventory(this.entityId, var1, var2));
               this.previousEquipment[var1] = var2 == null ? null : var2.copy();
            }
         }

         int var121 = this.getArrowCountInEntity();
         if (var121 > 0) {
            if (this.arrowHitTimer <= 0) {
               this.arrowHitTimer = 20 * (30 - var121);
            }

            this.arrowHitTimer--;
            if (this.arrowHitTimer <= 0) {
               this.setArrowCountInEntity(var121 - 1);
            }
         }
      }

      this.onLivingUpdate();
      double var12 = this.posX - this.prevPosX;
      double var3 = this.posZ - this.prevPosZ;
      float var5 = (float)(var12 * var12 + var3 * var3);
      float var6 = this.renderYawOffset;
      float var7 = 0.0F;
      this.field_70768_au = this.field_70766_av;
      float var8 = 0.0F;
      if (var5 > 0.0025000002F) {
         var8 = 1.0F;
         var7 = (float)Math.sqrt(var5) * 3.0F;
         var6 = (float)Math.atan2(var3, var12) * 180.0F / (float) Math.PI - 90.0F;
      }

      if (this.swingProgress > 0.0F) {
         var6 = this.rotationYaw;
      }

      if (!this.onGround) {
         var8 = 0.0F;
      }

      this.field_70766_av = this.field_70766_av + (var8 - this.field_70766_av) * 0.3F;
      this.worldObj.theProfiler.startSection("headTurn");
      if (this.isAIEnabled()) {
         this.bodyHelper.func_75664_a();
      } else {
         float var9 = MathHelper.wrapAngleTo180_float(var6 - this.renderYawOffset);
         this.renderYawOffset += var9 * 0.3F;
         float var10 = MathHelper.wrapAngleTo180_float(this.rotationYaw - this.renderYawOffset);
         boolean var11 = var10 < -90.0F || var10 >= 90.0F;
         if (var10 < -75.0F) {
            var10 = -75.0F;
         }

         if (var10 >= 75.0F) {
            var10 = 75.0F;
         }

         this.renderYawOffset = this.rotationYaw - var10;
         if (var10 * var10 > 2500.0F) {
            this.renderYawOffset += var10 * 0.2F;
         }

         if (var11) {
            var7 *= -1.0F;
         }
      }

      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("rangeChecks");

      while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
         this.prevRotationYaw -= 360.0F;
      }

      while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
         this.prevRotationYaw += 360.0F;
      }

      while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F) {
         this.prevRenderYawOffset -= 360.0F;
      }

      while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F) {
         this.prevRenderYawOffset += 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
         this.prevRotationPitch -= 360.0F;
      }

      while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
         this.prevRotationPitch += 360.0F;
      }

      while (this.rotationYawHead - this.prevRotationYawHead < -180.0F) {
         this.prevRotationYawHead -= 360.0F;
      }

      while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F) {
         this.prevRotationYawHead += 360.0F;
      }

      this.worldObj.theProfiler.endSection();
      this.field_70764_aw += var7;
   }

   public void heal(int par1) {
      if (this.health > 0) {
         this.setEntityHealth(this.getHealth() + par1);
         if (this.health > this.getMaxHealth()) {
            this.setEntityHealth(this.getMaxHealth());
         }

         this.hurtResistantTime = this.maxHurtResistantTime / 2;
      }
   }

   public abstract int getMaxHealth();

   public int getHealth() {
      return this.health;
   }

   public void setEntityHealth(int par1) {
      this.health = par1;
      if (par1 > this.getMaxHealth()) {
         par1 = this.getMaxHealth();
      }
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if (this.worldObj.isRemote) {
         return false;
      } else {
         this.entityAge = 0;
         if (this.health <= 0) {
            return false;
         } else if (par1DamageSource.isFireDamage() && this.isPotionActive(Potion.fireResistance)) {
            return false;
         } else {
            if (par1DamageSource == CustomDamageSource.damageSourceChoppingBlock) {
               this.recentlyOnChoppingBlockCountdown = 40;
            }

            if ((par1DamageSource == DamageSource.anvil || par1DamageSource == DamageSource.fallingBlock) && this.getCurrentItemOrArmor(4) != null) {
               this.getCurrentItemOrArmor(4).damageItem(par2 * 4 + this.rand.nextInt(par2 * 2), this);
               par2 = (int)(par2 * 0.75F);
            }

            this.limbYaw = 1.5F;
            boolean var3 = true;
            if (this.hurtResistantTime > this.maxHurtResistantTime / 2.0F) {
               if (par2 <= this.lastDamage) {
                  return false;
               }

               this.damageEntity(par1DamageSource, par2 - this.lastDamage);
               this.lastDamage = par2;
               var3 = false;
            } else {
               this.lastDamage = par2;
               this.prevHealth = this.health;
               this.hurtResistantTime = this.maxHurtResistantTime;
               this.damageEntity(par1DamageSource, par2);
               this.hurtTime = this.maxHurtTime = 10;
            }

            this.attackedAtYaw = 0.0F;
            Entity var4 = par1DamageSource.getEntity();
            if (var4 != null) {
               if (var4 instanceof EntityLiving) {
                  this.setRevengeTarget((EntityLiving)var4);
               }

               if (var4 instanceof EntityPlayer) {
                  this.recentlyHit = 100;
                  this.attackingPlayer = (EntityPlayer)var4;
               } else if (var4 instanceof EntityWolf) {
                  EntityWolf var5 = (EntityWolf)var4;
                  if (var5.m()) {
                     this.recentlyHit = 100;
                     this.attackingPlayer = null;
                  }
               }
            }

            if (var3) {
               this.worldObj.setEntityState(this, (byte)2);
               if (par1DamageSource != DamageSource.drown) {
                  this.J();
               }

               if (var4 != null) {
                  double var9 = var4.posX - this.posX;

                  double var7;
                  for (var7 = var4.posZ - this.posZ; var9 * var9 + var7 * var7 < 1.0E-4; var7 = (Math.random() - Math.random()) * 0.01) {
                     var9 = (Math.random() - Math.random()) * 0.01;
                  }

                  this.attackedAtYaw = (float)(Math.atan2(var7, var9) * 180.0 / Math.PI) - this.rotationYaw;
                  if (par2 > 1 || var4.isSprinting()) {
                     this.knockBack(var4, par2, var9, var7);
                  }
               } else {
                  this.attackedAtYaw = (int)(Math.random() * 2.0) * 180;
               }
            }

            if (this.health <= 0) {
               if (var3) {
                  this.a(this.getDeathSound(), this.getSoundVolume(), this.getSoundPitch());
               }

               this.onDeath(par1DamageSource);
            } else if (var3) {
               this.a(this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
            }

            return true;
         }
      }
   }

   protected float getSoundPitch() {
      return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void performHurtAnimation() {
      this.hurtTime = this.maxHurtTime = 10;
      this.attackedAtYaw = 0.0F;
   }

   public int getTotalArmorValue() {
      int var1 = 0;

      for (ItemStack var5 : this.getLastActiveItems()) {
         if (var5 != null && var5.getItem() instanceof ItemArmor) {
            int var6 = ((ItemArmor)var5.getItem()).damageReduceAmount;
            var1 += var6;
         }
      }

      return var1;
   }

   protected void damageArmor(int par1) {
   }

   protected int applyArmorCalculations(DamageSource par1DamageSource, int par2) {
      if (!par1DamageSource.isUnblockable()) {
         int var3 = 25 - this.getTotalArmorValue();
         int var4 = par2 * var3 + this.carryoverDamage;
         this.damageArmor(par2);
         par2 = var4 / 25;
         this.carryoverDamage = var4 % 25;
      }

      return par2;
   }

   protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2) {
      if (this.isPotionActive(Potion.resistance)) {
         int var3 = (this.getActivePotionEffect(Potion.resistance).getAmplifier() + 1) * 5;
         int var4 = 25 - var3;
         int var5 = par2 * var4 + this.carryoverDamage;
         par2 = var5 / 25;
         this.carryoverDamage = var5 % 25;
      }

      if (par2 <= 0) {
         return 0;
      } else {
         int var3 = EnchantmentHelper.getEnchantmentModifierDamage(this.getLastActiveItems(), par1DamageSource);
         if (var3 > 20) {
            var3 = 20;
         }

         if (var3 > 0 && var3 <= 20) {
            int var4 = 25 - var3;
            int var5 = par2 * var4 + this.carryoverDamage;
            par2 = var5 / 25;
            this.carryoverDamage = var5 % 25;
         }

         return par2;
      }
   }

   protected void damageEntity(DamageSource par1DamageSource, int par2) {
      if (!this.aq()) {
         par2 = this.applyArmorCalculations(par1DamageSource, par2);
         par2 = this.applyPotionDamageCalculations(par1DamageSource, par2);
         int var3 = this.getHealth();
         this.health -= par2;
         this.field_94063_bt.func_94547_a(par1DamageSource, var3, par2);
      }
   }

   protected float getSoundVolume() {
      return 1.0F;
   }

   protected String getLivingSound() {
      return null;
   }

   protected String getHurtSound() {
      return "damage.hit";
   }

   protected String getDeathSound() {
      return "damage.hit";
   }

   public void knockBack(Entity par1Entity, int par2, double par3, double par5) {
      this.isAirBorne = true;
      float var7 = MathHelper.sqrt_double(par3 * par3 + par5 * par5);
      float var8 = this.knockbackMagnitude();
      this.motionX /= 2.0;
      this.motionY /= 2.0;
      this.motionZ /= 2.0;
      this.motionX -= par3 / var7 * var8;
      this.motionY += var8;
      this.motionZ -= par5 / var7 * var8;
      if (this.motionY > 0.4F) {
         this.motionY = 0.4F;
      }
   }

   protected void dropRareDrop(int par1) {
   }

   protected void dropFewItems(boolean par1, int par2) {
      this.entityLivingDropFewItems(par1, par2);
   }

   protected void entityLivingDropFewItems(boolean par1, int par2) {
      int var3 = this.getDropItemId();
      if (var3 > 0) {
         int var4 = this.rand.nextInt(3);
         if (par2 > 0) {
            var4 += this.rand.nextInt(par2 + 1);
         }

         for (int var5 = 0; var5 < var4; var5++) {
            this.b(var3, 1);
         }
      }
   }

   protected int getDropItemId() {
      return 0;
   }

   @Override
   protected void fall(float par1) {
      this.entityLivingFall(par1);
   }

   protected void entityLivingFall(float par1) {
      super.fall(par1);
      int var2 = MathHelper.ceiling_float_int(par1 - 3.0F);
      if (var2 > 0) {
         if (var2 > 4) {
            this.a("damage.fallbig", 1.0F, 1.0F);
         } else {
            this.a("damage.fallsmall", 1.0F, 1.0F);
         }

         this.attackEntityFrom(DamageSource.fall, var2);
         int var3 = this.worldObj
            .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.2F - this.yOffset), MathHelper.floor_double(this.posZ));
         if (var3 > 0) {
            StepSound var4 = Block.blocksList[var3].getStepSound(this.worldObj, (int)this.posX, (int)this.posY, (int)this.posZ);
            this.a(var4.getStepSound(), var4.getStepVolume() * 0.5F, var4.getStepPitch() * 0.75F);
         }
      }
   }

   public void moveEntityWithHeading(float par1, float par2) {
      if (!this.G() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
         if (!this.I() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying) {
            float var3 = 0.91F;
            if (this.onGround) {
               var3 = this.getDefaultSlipperinessOnGround();
               int var4 = this.worldObj
                  .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY - 0.25), MathHelper.floor_double(this.posZ));
               if (var4 > 0) {
                  var3 = this.getSlipperinessRelativeToBlock(var4);
               }
            }

            float var8 = 0.16277136F / (var3 * var3 * var3);
            float var5;
            if (this.onGround) {
               if (this.isAIEnabled()) {
                  var5 = this.getAIMoveSpeed();
               } else {
                  var5 = this.landMovementFactor;
               }

               var5 *= var8;
            } else {
               var5 = this.jumpMovementFactor;
            }

            this.a(par1, par2, var5);
            var3 = 0.91F;
            if (this.onGround) {
               var3 = this.getDefaultSlipperinessOnGround();
               int var6 = this.worldObj
                  .getBlockId(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.boundingBox.minY - 0.25), MathHelper.floor_double(this.posZ));
               if (var6 > 0) {
                  var3 = this.getSlipperinessRelativeToBlock(var6);
               }
            }

            if (this.isOnLadder()) {
               float var10 = 0.15F;
               if (this.motionX < -var10) {
                  this.motionX = -var10;
               }

               if (this.motionX > var10) {
                  this.motionX = var10;
               }

               if (this.motionZ < -var10) {
                  this.motionZ = -var10;
               }

               if (this.motionZ > var10) {
                  this.motionZ = var10;
               }

               this.fallDistance = 0.0F;
               if (this.motionY < -0.15) {
                  this.motionY = -0.15;
               }

               boolean var7 = this.ag() && this instanceof EntityPlayer;
               if (var7 && this.motionY < 0.0) {
                  this.motionY = 0.0;
               }

               float fModifier = this.getLadderVerticalMovementModifier();
               this.motionY *= fModifier;
            }

            this.d(this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && this.isOnLadder()) {
               this.motionY = 0.2;
            }

            if (this.worldObj.isRemote
               && (
                  !this.worldObj.blockExists((int)this.posX, 0, (int)this.posZ)
                     || !this.worldObj.getChunkFromBlockCoords((int)this.posX, (int)this.posZ).isChunkLoaded
               )) {
               if (this.posY > 0.0) {
                  this.motionY = -0.1;
               } else {
                  this.motionY = 0.0;
               }
            } else {
               this.motionY -= 0.08;
            }

            this.motionY *= 0.98F;
            this.motionX *= var3;
            this.motionZ *= var3;
         } else {
            double var9 = this.posY;
            this.a(par1, par2, 0.02F);
            this.d(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
            this.motionY -= 0.02;
            if (this.isCollidedHorizontally && this.c(this.motionX, this.motionY + 0.6F - this.posY + var9, this.motionZ)) {
               this.motionY = 0.3F;
            }
         }
      } else {
         double var9 = this.posY;
         this.a(par1, par2, this.isAIEnabled() ? 0.04F : 0.02F);
         this.d(this.motionX, this.motionY, this.motionZ);
         this.motionX *= 0.8F;
         this.motionY *= 0.8F;
         this.motionZ *= 0.8F;
         this.motionY -= 0.02;
         float fModifier = this.getSwimmingHorizontalModifier();
         this.motionX *= fModifier;
         this.motionZ *= fModifier;
         if (this.isCollidedHorizontally && this.c(this.motionX, this.motionY + 0.6F - this.posY + var9, this.motionZ)) {
            this.motionY = 0.3F;
         }
      }

      this.prevLimbYaw = this.limbYaw;
      double var9x = this.posX - this.prevPosX;
      double var12 = this.posZ - this.prevPosZ;
      float var11 = MathHelper.sqrt_double(var9x * var9x + var12 * var12) * 4.0F;
      if (var11 > 1.0F) {
         var11 = 1.0F;
      }

      this.limbYaw = this.limbYaw + (var11 - this.limbYaw) * 0.4F;
      this.limbSwing = this.limbSwing + this.limbYaw;
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      if (!MinecraftServer.getIsServer()) {
         MobRandomizer.ExtraInfo.writeToNBT(this, par1NBTTagCompound);
      }

      if (this.health < -32768) {
         this.health = -32768;
      }

      par1NBTTagCompound.setShort("Health", (short)this.health);
      par1NBTTagCompound.setShort("HurtTime", (short)this.hurtTime);
      par1NBTTagCompound.setShort("DeathTime", (short)this.deathTime);
      par1NBTTagCompound.setShort("AttackTime", (short)this.attackTime);
      par1NBTTagCompound.setBoolean("CanPickUpLoot", this.canPickUpLoot());
      par1NBTTagCompound.setBoolean("PersistenceRequired", this.persistenceRequired);
      NBTTagList var2 = new NBTTagList();

      for (int var3 = 0; var3 < this.equipment.length; var3++) {
         NBTTagCompound var4 = new NBTTagCompound();
         if (this.equipment[var3] != null) {
            this.equipment[var3].writeToNBT(var4);
         }

         var2.appendTag(var4);
      }

      par1NBTTagCompound.setTag("Equipment", var2);
      if (!this.activePotionsMap.isEmpty()) {
         NBTTagList var6 = new NBTTagList();

         for (PotionEffect var5 : this.activePotionsMap.values()) {
            var6.appendTag(var5.writeCustomPotionEffectToNBT(new NBTTagCompound()));
         }

         par1NBTTagCompound.setTag("ActiveEffects", var6);
      }

      NBTTagList var6 = new NBTTagList();

      for (int var8 = 0; var8 < this.equipmentDropChances.length; var8++) {
         var6.appendTag(new NBTTagFloat(var8 + "", this.equipmentDropChances[var8]));
      }

      par1NBTTagCompound.setTag("DropChances", var6);
      par1NBTTagCompound.setString("CustomName", this.func_94057_bL());
      par1NBTTagCompound.setBoolean("CustomNameVisible", this.func_94062_bN());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      if (!MinecraftServer.getIsServer()) {
         MobRandomizer.ExtraInfo.readFromNBT(this, par1NBTTagCompound);
      }

      this.health = par1NBTTagCompound.getShort("Health");
      if (!par1NBTTagCompound.hasKey("Health")) {
         this.health = this.getMaxHealth();
      }

      this.hurtTime = par1NBTTagCompound.getShort("HurtTime");
      this.deathTime = par1NBTTagCompound.getShort("DeathTime");
      this.attackTime = par1NBTTagCompound.getShort("AttackTime");
      this.setCanPickUpLoot(par1NBTTagCompound.getBoolean("CanPickUpLoot"));
      this.persistenceRequired = par1NBTTagCompound.getBoolean("PersistenceRequired");
      if (par1NBTTagCompound.hasKey("CustomName") && par1NBTTagCompound.getString("CustomName").length() > 0) {
         this.func_94058_c(par1NBTTagCompound.getString("CustomName"));
      }

      this.func_94061_f(par1NBTTagCompound.getBoolean("CustomNameVisible"));
      if (par1NBTTagCompound.hasKey("Equipment")) {
         NBTTagList var2 = par1NBTTagCompound.getTagList("Equipment");

         for (int var3 = 0; var3 < this.equipment.length; var3++) {
            this.equipment[var3] = ItemStack.loadItemStackFromNBT((NBTTagCompound)var2.tagAt(var3));
         }
      }

      if (par1NBTTagCompound.hasKey("ActiveEffects")) {
         NBTTagList var2 = par1NBTTagCompound.getTagList("ActiveEffects");

         for (int var3 = 0; var3 < var2.tagCount(); var3++) {
            NBTTagCompound var4 = (NBTTagCompound)var2.tagAt(var3);
            PotionEffect var5 = PotionEffect.readCustomPotionEffectFromNBT(var4);
            this.activePotionsMap.put(var5.getPotionID(), var5);
         }
      }

      if (par1NBTTagCompound.hasKey("DropChances")) {
         NBTTagList var2 = par1NBTTagCompound.getTagList("DropChances");

         for (int var3 = 0; var3 < var2.tagCount(); var3++) {
            this.equipmentDropChances[var3] = ((NBTTagFloat)var2.tagAt(var3)).data;
         }
      }
   }

   @Override
   public boolean isEntityAlive() {
      return !this.isDead && this.health > 0;
   }

   public boolean canBreatheUnderwater() {
      return false;
   }

   public void setMoveForward(float par1) {
      this.moveForward = par1;
   }

   public void setJumping(boolean par1) {
      this.isJumping = par1;
   }

   public void onLivingUpdate() {
      this.entityLivingOnLivingUpdate();
   }

   protected void entityLivingOnLivingUpdate() {
      if (this.jumpTicks > 0) {
         this.jumpTicks--;
      }

      if (this.newPosRotationIncrements > 0) {
         double var1 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
         double var3 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
         double var5 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
         double var7 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
         this.rotationYaw = (float)(this.rotationYaw + var7 / this.newPosRotationIncrements);
         this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
         this.newPosRotationIncrements--;
         this.b(var1, var3, var5);
         this.b(this.rotationYaw, this.rotationPitch);
      } else if (!this.isClientWorld()) {
         this.motionX *= 0.98;
         this.motionY *= 0.98;
         this.motionZ *= 0.98;
      }

      if (Math.abs(this.motionX) < 0.005) {
         this.motionX = 0.0;
      }

      if (Math.abs(this.motionY) < 0.005) {
         this.motionY = 0.0;
      }

      if (Math.abs(this.motionZ) < 0.005) {
         this.motionZ = 0.0;
      }

      this.worldObj.theProfiler.startSection("ai");
      if (this.isMovementBlocked()) {
         this.isJumping = false;
         this.moveStrafing = 0.0F;
         this.moveForward = 0.0F;
         this.randomYawVelocity = 0.0F;
      } else if (this.isClientWorld()) {
         if (this.isAIEnabled()) {
            this.worldObj.theProfiler.startSection("newAi");
            this.updateAITasks();
            this.worldObj.theProfiler.endSection();
         } else {
            this.worldObj.theProfiler.startSection("oldAi");
            this.updateEntityActionState();
            this.worldObj.theProfiler.endSection();
            this.rotationYawHead = this.rotationYaw;
         }
      }

      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("jump");
      if (this.isJumping) {
         if ((this.G() || this.I()) && this.canSwim()) {
            this.motionY += 0.04F;
         } else if (this.canJump() && (this.onGround || this.canJumpMidWater()) && this.jumpTicks == 0) {
            this.jump();
            this.jumpTicks = 10;
         }
      } else {
         this.jumpTicks = 0;
      }

      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("travel");
      this.moveStrafing *= 0.98F;
      this.moveForward *= 0.98F;
      this.randomYawVelocity *= 0.9F;
      float var11 = this.landMovementFactor;
      this.landMovementFactor = this.landMovementFactor * this.getSpeedModifier();
      this.moveEntityWithHeading(this.moveStrafing, this.moveForward);
      this.landMovementFactor = var11;
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("push");
      if (!this.worldObj.isRemote) {
         this.func_85033_bc();
      }

      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("looting");
      if (!this.worldObj.isRemote && this.canPickUpLoot() && !this.isLivingDead && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
         for (EntityItem var4 : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(1.0, 0.0, 1.0))) {
            if (!var4.isDead && var4.getEntityItem() != null) {
               ItemStack var13 = var4.getEntityItem();
               int var6 = getArmorPosition(var13);
               if (var6 > -1) {
                  boolean var14 = true;
                  ItemStack var8 = this.getCurrentItemOrArmor(var6);
                  if (var8 != null) {
                     if (var6 == 0) {
                        if (var13.getItem() instanceof ItemSword && !(var8.getItem() instanceof ItemSword)) {
                           var14 = true;
                        } else if (var13.getItem() instanceof ItemSword && var8.getItem() instanceof ItemSword) {
                           ItemSword var9 = (ItemSword)var13.getItem();
                           ItemSword var10 = (ItemSword)var8.getItem();
                           if (var9.func_82803_g() == var10.func_82803_g()) {
                              var14 = var13.getItemDamage() > var8.getItemDamage() || var13.hasTagCompound() && !var8.hasTagCompound();
                           } else {
                              var14 = var9.func_82803_g() > var10.func_82803_g();
                           }
                        } else {
                           var14 = false;
                        }
                     } else if (var13.getItem() instanceof ItemArmor && !(var8.getItem() instanceof ItemArmor)) {
                        var14 = true;
                     } else if (var13.getItem() instanceof ItemArmor && var8.getItem() instanceof ItemArmor) {
                        ItemArmor var15 = (ItemArmor)var13.getItem();
                        ItemArmor var16 = (ItemArmor)var8.getItem();
                        if (var15.damageReduceAmount == var16.damageReduceAmount) {
                           var14 = var13.getItemDamage() > var8.getItemDamage() || var13.hasTagCompound() && !var8.hasTagCompound();
                        } else {
                           var14 = var15.damageReduceAmount > var16.damageReduceAmount;
                        }
                     } else {
                        var14 = false;
                     }
                  }

                  if (var14) {
                     if (var8 != null && this.rand.nextFloat() - 0.1F < this.equipmentDropChances[var6]) {
                        this.a(var8, 0.0F);
                     }

                     this.setCurrentItemOrArmor(var6, var13);
                     this.equipmentDropChances[var6] = 2.0F;
                     this.persistenceRequired = true;
                     this.onItemPickup(var4, 1);
                     var4.w();
                  }
               }
            }
         }
      }

      this.worldObj.theProfiler.endSection();
      this.modSpecificOnLivingUpdate();
   }

   protected void func_85033_bc() {
      List var1 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(0.2F, 0.0, 0.2F));
      if (var1 != null && !var1.isEmpty()) {
         for (int var2 = 0; var2 < var1.size(); var2++) {
            Entity var3 = (Entity)var1.get(var2);
            if (var3.canBePushed()) {
               this.collideWithEntity(var3);
            }
         }
      }
   }

   protected void collideWithEntity(Entity par1Entity) {
      par1Entity.applyEntityCollision(this);
   }

   protected boolean isAIEnabled() {
      return false;
   }

   protected boolean isClientWorld() {
      return !this.worldObj.isRemote;
   }

   protected boolean isMovementBlocked() {
      return this.health <= 0;
   }

   public boolean isBlocking() {
      return false;
   }

   public void jump() {
      this.motionY = 0.42F;
      if (this.isPotionActive(Potion.jump)) {
         this.motionY = this.motionY + (this.getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F;
      }

      if (this.ah() && !this.G()) {
         float var1 = this.rotationYaw * (float) (Math.PI / 180.0);
         this.motionX = this.motionX - MathHelper.sin(var1) * 0.2F;
         this.motionZ = this.motionZ + MathHelper.cos(var1) * 0.2F;
      }

      this.isAirBorne = true;
   }

   protected boolean canDespawn() {
      return true;
   }

   protected void updateAITasks() {
      this.entityLivingUpdateAITasks();
   }

   protected void entityLivingUpdateAITasks() {
      this.entityAge++;
      this.worldObj.theProfiler.startSection("checkDespawn");
      this.despawnEntity();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("sensing");
      this.senses.clearSensingCache();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("targetSelector");
      this.targetTasks.onUpdateTasks();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("goalSelector");
      this.tasks.onUpdateTasks();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("navigation");
      this.navigator.onUpdateNavigation();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("mob tick");
      this.updateAITick();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.startSection("controls");
      this.worldObj.theProfiler.startSection("move");
      this.moveHelper.onUpdateMoveHelper();
      this.worldObj.theProfiler.endStartSection("look");
      this.lookHelper.onUpdateLook();
      this.worldObj.theProfiler.endStartSection("jump");
      this.jumpHelper.doJump();
      this.worldObj.theProfiler.endSection();
      this.worldObj.theProfiler.endSection();
   }

   protected void updateAITick() {
   }

   protected void updateEntityActionState() {
      this.entityAge++;
      this.despawnEntity();
      this.moveStrafing = 0.0F;
      this.moveForward = 0.0F;
      float var1 = 8.0F;
      if (this.rand.nextFloat() < 0.02F) {
         EntityPlayer var2 = this.worldObj.getClosestPlayerToEntity(this, var1);
         if (var2 != null) {
            this.currentTarget = var2;
            this.numTicksToChaseTarget = 10 + this.rand.nextInt(20);
         } else {
            this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
         }
      }

      if (this.currentTarget != null) {
         this.faceEntity(this.currentTarget, 10.0F, this.getVerticalFaceSpeed());
         if (this.numTicksToChaseTarget-- <= 0 || this.currentTarget.isDead || this.currentTarget.getDistanceSqToEntity(this) > var1 * var1) {
            this.currentTarget = null;
         }
      } else {
         if (this.rand.nextFloat() < 0.05F) {
            this.randomYawVelocity = (this.rand.nextFloat() - 0.5F) * 20.0F;
         }

         this.rotationYaw = this.rotationYaw + this.randomYawVelocity;
         this.rotationPitch = this.defaultPitch;
      }

      boolean var4 = this.G();
      boolean var3 = this.I();
      if (var4 || var3) {
         this.isJumping = this.rand.nextFloat() < 0.8F;
      }
   }

   protected void updateArmSwingProgress() {
      int var1 = this.getArmSwingAnimationEnd();
      if (this.isSwingInProgress) {
         this.swingProgressInt++;
         if (this.swingProgressInt >= var1) {
            this.swingProgressInt = 0;
            this.isSwingInProgress = false;
         }
      } else {
         this.swingProgressInt = 0;
      }

      this.swingProgress = (float)this.swingProgressInt / var1;
   }

   public int getVerticalFaceSpeed() {
      return 40;
   }

   public void faceEntity(Entity par1Entity, float par2, float par3) {
      double var4 = par1Entity.posX - this.posX;
      double var8 = par1Entity.posZ - this.posZ;
      double var6;
      if (par1Entity instanceof EntityLiving) {
         EntityLiving var10 = (EntityLiving)par1Entity;
         var6 = var10.posY + var10.getEyeHeight() - (this.posY + this.getEyeHeight());
      } else {
         var6 = (par1Entity.boundingBox.minY + par1Entity.boundingBox.maxY) / 2.0 - (this.posY + this.getEyeHeight());
      }

      double var14 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
      float var12 = (float)(Math.atan2(var8, var4) * 180.0 / Math.PI) - 90.0F;
      float var13 = (float)(-(Math.atan2(var6, var14) * 180.0 / Math.PI));
      this.rotationPitch = this.updateRotation(this.rotationPitch, var13, par3);
      this.rotationYaw = this.updateRotation(this.rotationYaw, var12, par2);
   }

   private float updateRotation(float par1, float par2, float par3) {
      float var4 = MathHelper.wrapAngleTo180_float(par2 - par1);
      if (var4 > par3) {
         var4 = par3;
      }

      if (var4 < -par3) {
         var4 = -par3;
      }

      return par1 + var4;
   }

   public boolean getCanSpawnHere() {
      return this.worldObj.checkNoEntityCollision(this.boundingBox)
         && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()
         && !this.worldObj.isAnyLiquid(this.boundingBox);
   }

   @Override
   protected void kill() {
      this.attackEntityFrom(DamageSource.outOfWorld, 4);
   }

   @Environment(EnvType.CLIENT)
   public float getSwingProgress(float par1) {
      float var2 = this.swingProgress - this.prevSwingProgress;
      if (var2 < 0.0F) {
         var2++;
      }

      return this.prevSwingProgress + var2 * par1;
   }

   @Environment(EnvType.CLIENT)
   public Vec3 getPosition(float par1) {
      if (par1 == 1.0F) {
         return this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
      } else {
         double var2 = this.prevPosX + (this.posX - this.prevPosX) * par1;
         double var4 = this.prevPosY + (this.posY - this.prevPosY) * par1;
         double var6 = this.prevPosZ + (this.posZ - this.prevPosZ) * par1;
         return this.worldObj.getWorldVec3Pool().getVecFromPool(var2, var4, var6);
      }
   }

   @Override
   public Vec3 getLookVec() {
      return this.getLook(1.0F);
   }

   public Vec3 getLook(float par1) {
      if (par1 == 1.0F) {
         float var2 = MathHelper.cos(-this.rotationYaw * (float) (Math.PI / 180.0) - (float) Math.PI);
         float var3 = MathHelper.sin(-this.rotationYaw * (float) (Math.PI / 180.0) - (float) Math.PI);
         float var4 = -MathHelper.cos(-this.rotationPitch * (float) (Math.PI / 180.0));
         float var5 = MathHelper.sin(-this.rotationPitch * (float) (Math.PI / 180.0));
         return this.worldObj.getWorldVec3Pool().getVecFromPool(var3 * var4, var5, var2 * var4);
      } else {
         float var2 = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * par1;
         float var3 = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * par1;
         float var4 = MathHelper.cos(-var3 * (float) (Math.PI / 180.0) - (float) Math.PI);
         float var5 = MathHelper.sin(-var3 * (float) (Math.PI / 180.0) - (float) Math.PI);
         float var6 = -MathHelper.cos(-var2 * (float) (Math.PI / 180.0));
         float var7 = MathHelper.sin(-var2 * (float) (Math.PI / 180.0));
         return this.worldObj.getWorldVec3Pool().getVecFromPool(var5 * var6, var7, var4 * var6);
      }
   }

   @Environment(EnvType.CLIENT)
   public float getRenderSizeModifier() {
      return 1.0F;
   }

   @Environment(EnvType.CLIENT)
   public MovingObjectPosition rayTrace(double par1, float par3) {
      Vec3 var4 = this.getPosition(par3);
      Vec3 var5 = this.getLook(par3);
      Vec3 var6 = var4.addVector(var5.xCoord * par1, var5.yCoord * par1, var5.zCoord * par1);
      return this.worldObj.rayTraceBlocks(var4, var6);
   }

   public int getMaxSpawnedInChunk() {
      return 4;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 2) {
         this.limbYaw = 1.5F;
         this.hurtResistantTime = this.maxHurtResistantTime;
         this.hurtTime = this.maxHurtTime = 10;
         this.attackedAtYaw = 0.0F;
         this.a(this.getHurtSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.attackEntityFrom(DamageSource.generic, 0);
      } else if (par1 == 3) {
         this.a(this.getDeathSound(), this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
         this.health = 0;
         this.onDeath(DamageSource.generic);
      } else {
         super.handleHealthUpdate(par1);
      }
   }

   public boolean isPlayerSleeping() {
      return false;
   }

   @Environment(EnvType.CLIENT)
   public Icon getItemIcon(ItemStack par1ItemStack, int par2) {
      return par1ItemStack.getIconIndex();
   }

   protected void updatePotionEffects() {
      Iterator var1 = this.activePotionsMap.keySet().iterator();

      while (var1.hasNext()) {
         Integer var2 = (Integer)var1.next();
         PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);

         try {
            if (!var3.onUpdate(this)) {
               if (!this.worldObj.isRemote) {
                  var1.remove();
                  this.onFinishedPotionEffect(var3);
               }
            } else if (var3.getDuration() % 600 == 0) {
               this.onChangedPotionEffect(var3);
            }
         } catch (Throwable var11) {
            CrashReport var5 = CrashReport.makeCrashReport(var11, "Ticking mob effect instance");
            CrashReportCategory var6 = var5.makeCategory("Mob effect being ticked");
            var6.addCrashSectionCallable("Effect Name", new CallableEffectName(this, var3));
            var6.addCrashSectionCallable("Effect ID", new CallableEffectID(this, var3));
            var6.addCrashSectionCallable("Effect Duration", new CallableEffectDuration(this, var3));
            var6.addCrashSectionCallable("Effect Amplifier", new CallableEffectAmplifier(this, var3));
            var6.addCrashSectionCallable("Effect is Splash", new CallableEffectIsSplash(this, var3));
            var6.addCrashSectionCallable("Effect is Ambient", new CallableEffectIsAmbient(this, var3));
            throw new ReportedException(var5);
         }
      }

      if (this.potionsNeedUpdate) {
         if (!this.worldObj.isRemote) {
            if (this.activePotionsMap.isEmpty()) {
               this.dataWatcher.updateObject(9, (byte)0);
               this.dataWatcher.updateObject(8, 0);
               this.d(false);
            } else {
               int var12 = PotionHelper.calcPotionLiquidColor(this.activePotionsMap.values());
               this.dataWatcher.updateObject(9, (byte)(PotionHelper.func_82817_b(this.activePotionsMap.values()) ? 1 : 0));
               this.dataWatcher.updateObject(8, var12);
               this.d(this.isPotionActive(Potion.invisibility.id));
            }
         }

         this.potionsNeedUpdate = false;
      }

      int var12 = this.dataWatcher.getWatchableObjectInt(8);
      boolean var13 = this.dataWatcher.getWatchableObjectByte(9) > 0;
      if (var12 > 0) {
         if (this.hasOnlyAmbientPotionEffects(this.activePotionsMap.values())) {
            return;
         }

         boolean var4 = false;
         if (!this.ai()) {
            var4 = this.rand.nextBoolean();
         } else {
            var4 = this.rand.nextInt(15) == 0;
         }

         if (var13) {
            var4 &= this.rand.nextInt(5) == 0;
         }

         if (var4 && var12 > 0) {
            double var14 = (var12 >> 16 & 0xFF) / 255.0;
            double var7 = (var12 >> 8 & 0xFF) / 255.0;
            double var9 = (var12 >> 0 & 0xFF) / 255.0;
            this.worldObj
               .spawnParticle(
                  var13 ? "mobSpellAmbient" : "mobSpell",
                  this.posX + (this.rand.nextDouble() - 0.5) * this.width,
                  this.posY + this.rand.nextDouble() * this.height - this.yOffset,
                  this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
                  var14,
                  var7,
                  var9
               );
         }
      }
   }

   public void clearActivePotions() {
      Iterator var1 = this.activePotionsMap.keySet().iterator();

      while (var1.hasNext()) {
         Integer var2 = (Integer)var1.next();
         PotionEffect var3 = (PotionEffect)this.activePotionsMap.get(var2);
         if (!this.worldObj.isRemote) {
            var1.remove();
            this.onFinishedPotionEffect(var3);
         }
      }
   }

   public Collection getActivePotionEffects() {
      return this.activePotionsMap.values();
   }

   public boolean isPotionActive(int par1) {
      return this.activePotionsMap.containsKey(par1);
   }

   public boolean isPotionActive(Potion par1Potion) {
      return this.activePotionsMap.containsKey(par1Potion.id);
   }

   public PotionEffect getActivePotionEffect(Potion par1Potion) {
      return (PotionEffect)this.activePotionsMap.get(par1Potion.id);
   }

   public void addPotionEffect(PotionEffect par1PotionEffect) {
      if (this.isPotionApplicable(par1PotionEffect)) {
         if (this.activePotionsMap.containsKey(par1PotionEffect.getPotionID())) {
            ((PotionEffect)this.activePotionsMap.get(par1PotionEffect.getPotionID())).combine(par1PotionEffect);
            this.onChangedPotionEffect((PotionEffect)this.activePotionsMap.get(par1PotionEffect.getPotionID()));
         } else {
            this.activePotionsMap.put(par1PotionEffect.getPotionID(), par1PotionEffect);
            this.onNewPotionEffect(par1PotionEffect);
         }
      }
   }

   public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
      return true;
   }

   public boolean isEntityUndead() {
      return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
   }

   @Environment(EnvType.CLIENT)
   public void removePotionEffectClient(int par1) {
      this.activePotionsMap.remove(par1);
   }

   public void removePotionEffect(int par1) {
      PotionEffect var2 = (PotionEffect)this.activePotionsMap.remove(par1);
      if (var2 != null) {
         this.onFinishedPotionEffect(var2);
      }
   }

   protected void onNewPotionEffect(PotionEffect par1PotionEffect) {
      this.potionsNeedUpdate = true;
   }

   protected void onChangedPotionEffect(PotionEffect par1PotionEffect) {
      this.potionsNeedUpdate = true;
   }

   protected void onFinishedPotionEffect(PotionEffect par1PotionEffect) {
      this.potionsNeedUpdate = true;
   }

   public void setPositionAndUpdate(double par1, double par3, double par5) {
      this.b(par1, par3, par5, this.rotationYaw, this.rotationPitch);
   }

   public boolean isChild() {
      return false;
   }

   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEFINED;
   }

   public void renderBrokenItemStack(ItemStack par1ItemStack) {
      this.a("random.break", 0.8F, 0.8F + this.worldObj.rand.nextFloat() * 0.4F);

      for (int var2 = 0; var2 < 5; var2++) {
         Vec3 var3 = this.worldObj.getWorldVec3Pool().getVecFromPool((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
         var3.rotateAroundX(-this.rotationPitch * (float) Math.PI / 180.0F);
         var3.rotateAroundY(-this.rotationYaw * (float) Math.PI / 180.0F);
         Vec3 var4 = this.worldObj.getWorldVec3Pool().getVecFromPool((this.rand.nextFloat() - 0.5) * 0.3, -this.rand.nextFloat() * 0.6 - 0.3, 0.6);
         var4.rotateAroundX(-this.rotationPitch * (float) Math.PI / 180.0F);
         var4.rotateAroundY(-this.rotationYaw * (float) Math.PI / 180.0F);
         var4 = var4.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
         this.worldObj
            .spawnParticle("iconcrack_" + par1ItemStack.getItem().itemID, var4.xCoord, var4.yCoord, var4.zCoord, var3.xCoord, var3.yCoord + 0.05, var3.zCoord);
      }
   }

   @Override
   public int func_82143_as() {
      if (this.getAttackTarget() == null) {
         return 3;
      } else {
         int var1 = (int)(this.health - this.getMaxHealth() * 0.33F);
         var1 -= (3 - this.worldObj.difficultySetting) * 4;
         if (var1 < 0) {
            var1 = 0;
         }

         return var1 + 3;
      }
   }

   public ItemStack getHeldItem() {
      return this.equipment[0];
   }

   public ItemStack getCurrentItemOrArmor(int par1) {
      return this.equipment[par1];
   }

   public ItemStack getCurrentArmor(int par1) {
      return this.equipment[par1 + 1];
   }

   @Override
   public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
      this.equipment[par1] = par2ItemStack;
   }

   @Override
   public ItemStack[] getLastActiveItems() {
      return this.equipment;
   }

   protected void dropEquipment(boolean par1, int par2) {
      for (int var3 = 0; var3 < this.getLastActiveItems().length; var3++) {
         ItemStack var4 = this.getCurrentItemOrArmor(var3);
         boolean var5 = this.equipmentDropChances[var3] > 1.0F;
         if (var4 != null && (par1 || var5) && this.rand.nextFloat() - par2 * 0.01F < this.equipmentDropChances[var3]) {
            if (!var5 && var4.isItemStackDamageable()) {
               int var6 = Math.max(var4.getMaxDamage() - 25, 1);
               var6 = Math.max((int)(var4.getMaxDamage() * 0.95F), 1);
               int var7 = var4.getMaxDamage() - this.rand.nextInt(this.rand.nextInt(var6) + 1);
               if (var7 > var6) {
                  var7 = var6;
               }

               if (var7 < 1) {
                  var7 = 1;
               }

               var4.setItemDamage(var7);
            }

            this.a(var4, 0.0F);
         }
      }
   }

   protected void addRandomArmor() {
      this.entityLivingAddRandomArmor();
   }

   protected void entityLivingAddRandomArmor() {
      if (this.rand.nextFloat() < armorProbability[this.worldObj.difficultySetting]) {
         int var1 = this.rand.nextInt(2);
         float var2 = this.worldObj.difficultySetting == 3 ? 0.1F : 0.25F;
         var2 = 0.1F;
         if (this.rand.nextFloat() < 0.095F) {
            var1++;
         }

         if (this.rand.nextFloat() < 0.095F) {
            var1++;
         }

         if (this.rand.nextFloat() < 0.095F) {
            var1++;
         }

         for (int var3 = 3; var3 >= 0; var3--) {
            ItemStack var4 = this.getCurrentArmor(var3);
            if (var3 < 3 && this.rand.nextFloat() < var2) {
               break;
            }

            if (var4 == null) {
               Item var5 = getArmorItemForSlot(var3 + 1, var1);
               if (var5 != null) {
                  this.setCurrentItemOrArmor(var3 + 1, new ItemStack(var5));
                  this.equipmentDropChances[var3 + 1] = 0.75F;
               }
            }
         }
      }
   }

   public void onItemPickup(Entity par1Entity, int par2) {
      if (!par1Entity.isDead && !this.worldObj.isRemote) {
         EntityTracker var3 = ((WorldServer)this.worldObj).getEntityTracker();
         if (par1Entity instanceof EntityItem) {
            var3.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, this.entityId));
         }

         if (par1Entity instanceof EntityArrow) {
            var3.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, this.entityId));
         }

         if (par1Entity instanceof EntityXPOrb) {
            var3.sendPacketToAllPlayersTrackingEntity(par1Entity, new Packet22Collect(par1Entity.entityId, this.entityId));
         }
      }
   }

   public static int getArmorPosition(ItemStack par0ItemStack) {
      if (par0ItemStack.itemID == Block.pumpkin.blockID || par0ItemStack.itemID == Item.skull.itemID) {
         return 4;
      } else {
         if (par0ItemStack.getItem() instanceof ItemArmor) {
            switch (((ItemArmor)par0ItemStack.getItem()).armorType) {
               case 0:
                  return 4;
               case 1:
                  return 3;
               case 2:
                  return 2;
               case 3:
                  return 1;
            }
         }

         return 0;
      }
   }

   protected void func_82162_bC() {
      if (this.getHeldItem() != null && this.rand.nextFloat() < enchantmentProbability[this.worldObj.difficultySetting]) {
         EnchantmentHelper.addRandomEnchantment(this.rand, this.getHeldItem(), 7 * this.rand.nextInt(6));
      }

      for (int var1 = 0; var1 < 4; var1++) {
         ItemStack var2 = this.getCurrentArmor(var1);
         if (var2 != null && this.rand.nextFloat() < armorEnchantmentProbability[this.worldObj.difficultySetting]) {
            EnchantmentHelper.addRandomEnchantment(this.rand, var2, 7 * this.rand.nextInt(6));
         }
      }
   }

   public void initCreature() {
   }

   private int getArmSwingAnimationEnd() {
      return this.isPotionActive(Potion.digSpeed)
         ? 6 - (1 + this.getActivePotionEffect(Potion.digSpeed).getAmplifier()) * 1
         : (this.isPotionActive(Potion.digSlowdown) ? 6 + (1 + this.getActivePotionEffect(Potion.digSlowdown).getAmplifier()) * 2 : 6);
   }

   public void swingItem() {
      if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0) {
         this.swingProgressInt = -1;
         this.isSwingInProgress = true;
         if (this.worldObj instanceof WorldServer) {
            ((WorldServer)this.worldObj).getEntityTracker().sendPacketToAllPlayersTrackingEntity(this, new Packet18Animation(this, 1));
         }
      }
   }

   public boolean canBeSteered() {
      return false;
   }

   public final int getArrowCountInEntity() {
      return this.dataWatcher.getWatchableObjectByte(10);
   }

   public final void setArrowCountInEntity(int par1) {
      this.dataWatcher.updateObject(10, (byte)par1);
   }

   public EntityLiving func_94060_bK() {
      return (EntityLiving)(this.field_94063_bt.func_94550_c() != null
         ? this.field_94063_bt.func_94550_c()
         : (this.attackingPlayer != null ? this.attackingPlayer : (this.entityLivingToAttack != null ? this.entityLivingToAttack : null)));
   }

   @Override
   public String getEntityName() {
      return this.func_94056_bM() ? this.func_94057_bL() : super.getEntityName();
   }

   public void func_94058_c(String par1Str) {
      this.dataWatcher.updateObject(5, par1Str);
   }

   public String func_94057_bL() {
      return this.dataWatcher.getWatchableObjectString(5);
   }

   public boolean func_94056_bM() {
      return this.dataWatcher.getWatchableObjectString(5).length() > 0;
   }

   public void func_94061_f(boolean par1) {
      this.dataWatcher.updateObject(6, (byte)(par1 ? 1 : 0));
   }

   public boolean func_94062_bN() {
      return this.dataWatcher.getWatchableObjectByte(6) == 1;
   }

   @Environment(EnvType.CLIENT)
   public boolean func_94059_bO() {
      return this.func_94062_bN();
   }

   public void func_96120_a(int par1, float par2) {
      this.equipmentDropChances[par1] = par2;
   }

   public boolean canPickUpLoot() {
      return this.canPickUpLoot;
   }

   public void setCanPickUpLoot(boolean par1) {
      this.canPickUpLoot = par1;
   }

   public boolean getIsPersistent() {
      return this.persistenceRequired;
   }

   public boolean isOnLadder() {
      int i = MathHelper.floor_double(this.posX);
      int j = MathHelper.floor_double(this.boundingBox.minY);
      int k = MathHelper.floor_double(this.posZ);
      Block block = Block.blocksList[this.worldObj.getBlockId(i, j, k)];
      return block != null && block.isBlockClimbable(this.worldObj, i, j, k);
   }

   protected void modSpecificOnLivingUpdate() {
      if (this.recentlyOnChoppingBlockCountdown > 0) {
         this.recentlyOnChoppingBlockCountdown--;
      }
   }

   public float getSpeedModifier() {
      float fMoveSpeed = 1.0F;
      if (this.isPotionActive(Potion.moveSlowdown)) {
         fMoveSpeed *= 1.0F - 0.15F * (this.getActivePotionEffect(Potion.moveSlowdown).getAmplifier() + 1);
      } else if (this.isPotionActive(Potion.moveSpeed)) {
         fMoveSpeed *= 1.0F + 0.15F * (this.getActivePotionEffect(Potion.moveSpeed).getAmplifier() + 1);
      }

      if (this.onGround && this.isAffectedByMovementModifiers()) {
         int iGroundI = MathHelper.floor_double(this.posX);
         int iGroundJ = MathHelper.floor_double(this.posY - 0.03 - this.yOffset);
         int iGroundK = MathHelper.floor_double(this.posZ);
         if (WorldUtils.isGroundCoverOnBlock(this.worldObj, iGroundI, iGroundJ, iGroundK)) {
            fMoveSpeed *= 0.8F;
         }

         Block blockOn = Block.blocksList[this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK)];
         if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iGroundI, iGroundJ, iGroundK) == null) {
            float fHalfWidth = this.width / 2.0F;
            int iCenterGroundI = iGroundI;
            iGroundI = MathHelper.floor_double(this.posX + fHalfWidth);
            blockOn = Block.blocksList[this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK)];
            if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iGroundI, iGroundJ, iGroundK) == null) {
               iGroundI = MathHelper.floor_double(this.posX - fHalfWidth);
               blockOn = Block.blocksList[this.worldObj.getBlockId(iGroundI, iGroundJ, iGroundK)];
               if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iGroundI, iGroundJ, iGroundK) == null) {
                  iGroundI = iCenterGroundI;
                  iGroundK = MathHelper.floor_double(this.posZ + fHalfWidth);
                  blockOn = Block.blocksList[this.worldObj.getBlockId(iCenterGroundI, iGroundJ, iGroundK)];
                  if (blockOn == null || blockOn.getCollisionBoundingBoxFromPool(this.worldObj, iCenterGroundI, iGroundJ, iGroundK) == null) {
                     iGroundK = MathHelper.floor_double(this.posZ - fHalfWidth);
                     blockOn = Block.blocksList[this.worldObj.getBlockId(iCenterGroundI, iGroundJ, iGroundK)];
                  }
               }
            }
         }

         if (blockOn != null) {
            fMoveSpeed *= blockOn.getMovementModifier(this.worldObj, iGroundI, iGroundJ, iGroundK);
         }

         fMoveSpeed *= this.getLandMovementModifier();
      }

      if (fMoveSpeed < 0.0F) {
         fMoveSpeed = 0.0F;
      }

      return fMoveSpeed;
   }

   protected float getHealthAndExhaustionModifier() {
      return 1.0F;
   }

   protected float getSwimmingHorizontalModifier() {
      return this.getHealthAndExhaustionModifier();
   }

   protected float getLandMovementModifier() {
      return this.getHealthAndExhaustionModifier();
   }

   protected float getLadderVerticalMovementModifier() {
      return this.getHealthAndExhaustionModifier();
   }

   public static Item getArmorItemForSlot(int par0, int par1) {
      switch (par0) {
         case 1:
            if (par1 == 4) {
               return Item.bootsChain;
            }

            return Item.bootsIron;
         case 2:
            if (par1 == 4) {
               return Item.legsChain;
            }

            return Item.legsIron;
         case 3:
            if (par1 == 4) {
               return Item.plateChain;
            }

            return Item.plateIron;
         case 4:
            if (par1 == 4) {
               return Item.helmetChain;
            }

            return Item.helmetIron;
         default:
            return null;
      }
   }

   public void onDeath(DamageSource source) {
      this.entityLivingOnDeath(source);
   }

   public void entityLivingOnDeath(DamageSource source) {
      EntityLiving killCreditedEntity = this.func_94060_bK();
      if (this.scoreValue >= 0 && killCreditedEntity != null) {
         killCreditedEntity.c(this, this.scoreValue);
      }

      Entity sourceEntity = source.getEntity();
      if (sourceEntity != null) {
         sourceEntity.onKillEntity(this);
      }

      this.isLivingDead = true;
      if (!this.worldObj.isRemote && !this.isChild() && this.worldObj.getGameRules().getGameRuleBooleanValue("doMobLoot")) {
         int iLootingModifier = this.getAmbientLootingModifier();
         if (sourceEntity instanceof EntityPlayer) {
            int iPlayerLootingModifier = EnchantmentHelper.getLootingModifier((EntityLiving)sourceEntity);
            if (iPlayerLootingModifier > iLootingModifier) {
               iLootingModifier = iPlayerLootingModifier;
            }
         }

         this.dropFewItems(true, iLootingModifier);
         this.dropEquipment(true, iLootingModifier);
         this.checkForRareDrop(source, iLootingModifier);
         this.checkForHeadDrop(source, iLootingModifier);
         this.checkForScrollDrop();
      }

      this.worldObj.setEntityState(this, (byte)3);
   }

   protected void checkForRareDrop(DamageSource source, int iLootingModifier) {
      int iChance = this.rand.nextInt(800) - iLootingModifier * 4;
      if (iChance < 5) {
         this.dropRareDrop(iChance <= 0 ? 1 : 0);
      }
   }

   public void checkForScrollDrop() {
   }

   protected void checkForHeadDrop(DamageSource source, int iLootingModifier) {
      Entity sourceEntity = source.getEntity();
      int iHeadChance = this.rand.nextInt(200);
      if (sourceEntity instanceof EntityPlayer) {
         if (((EntityPlayer)sourceEntity).getHeldItem() != null && ((EntityPlayer)sourceEntity).getHeldItem().getItem().itemID == BTWItems.battleaxe.itemID) {
            iHeadChance >>= 2;
         }
      } else if (source == CustomDamageSource.damageSourceChoppingBlock || this.recentlyOnChoppingBlockCountdown > 0) {
         iHeadChance >>= 2;
      }

      iHeadChance -= iLootingModifier;
      if (iHeadChance < 5) {
         this.dropHead();
      }
   }

   protected int getAmbientLootingModifier() {
      int iLocI = MathHelper.floor_double(this.posX);
      int iLocJ = MathHelper.floor_double(this.posY);
      int iLocK = MathHelper.floor_double(this.posZ);
      return this.worldObj.getAmbientBeaconEffectAtLocation(BeaconTileEntity.LOOTING_EFFECT.EFFECT_NAME, iLocI, iLocJ, iLocK);
   }

   protected void dropHead() {
   }

   public boolean canJump() {
      return true;
   }

   public boolean canSwim() {
      return !this.isWeighted();
   }

   public boolean isWeighted() {
      int iWeight = this.getWornArmorWeight();
      return iWeight >= 10 ? true : this.isWeightedByHeadCrab();
   }

   protected boolean isWeightedByHeadCrab() {
      return this.hasHeadCrabbedSquid();
   }

   @Override
   public void onFlungBySquidTentacle(SquidEntity squid) {
      this.a(this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
   }

   @Override
   public void onHeadCrabbedBySquid(SquidEntity squid) {
      this.a(this.getHurtSound(), this.getSoundVolume(), this.getSoundPitch());
   }

   protected int getWornArmorWeight() {
      int iWeight = 0;

      for (int iTempSlot = 0; iTempSlot < 4; iTempSlot++) {
         ItemStack tempStack = this.getCurrentArmor(iTempSlot);
         if (tempStack != null) {
            iWeight += tempStack.getItem().getWeightWhenWorn();
         }
      }

      return iWeight;
   }

   private boolean hasOnlyAmbientPotionEffects(Collection effectCollection) {
      if (effectCollection != null && !effectCollection.isEmpty()) {
         for (PotionEffect tempEffect : effectCollection) {
            if (!tempEffect.getIsAmbient()) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void spawnerInitCreature() {
      this.initCreature();
   }

   public void preInitCreature() {
   }

   public boolean canEntityBeSeen(Entity entity) {
      return this.worldObj
            .rayTraceBlocks_do_do(
               this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY + this.getEyeHeight(), this.posZ),
               this.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY + entity.getEyeHeight(), entity.posZ),
               false,
               true
            )
         == null;
   }

   public boolean canEntityCenterOfMassBeSeen(Entity entity) {
      return this.worldObj
            .rayTraceBlocks_do_do(
               this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY + this.getEyeHeight(), this.posZ),
               this.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX, entity.posY + entity.height / 2.0F, entity.posZ),
               false,
               true
            )
         == null;
   }

   protected void recoverAirSupply() {
      this.g(300);
   }

   public void setPersistent(boolean bPersistant) {
      this.persistenceRequired = bPersistant;
   }

   @Override
   public boolean doesEntityApplyToSpawnCap() {
      return !this.getIsPersistent();
   }

   @Override
   public void outOfUpdateRangeUpdate() {
      this.despawnEntity();
   }

   public float getDefaultSlipperinessOnGround() {
      return 0.54600006F;
   }

   public float getSlipperinessRelativeToBlock(int iBlockID) {
      return Block.blocksList[iBlockID].slipperiness * 0.91F;
   }

   public boolean canJumpMidWater() {
      return false;
   }

   public void onClimbWhileSwimming() {
   }

   protected void despawnEntity() {
      if (!this.persistenceRequired && this.canDespawn()) {
         int iChunkX = MathHelper.floor_double(this.posX / 16.0);
         int iChunkZ = MathHelper.floor_double(this.posZ / 16.0);
         if (!this.worldObj.isChunkActive(iChunkX, iChunkZ)) {
            this.w();
         } else {
            EntityPlayer closestPlayer = this.worldObj.getClosestPlayerToEntity(this, this.minDistFromPlayerForDespawn());
            if (closestPlayer != null) {
               this.entityAge = 0;
            } else if (this.entityAge > 600 && this.rand.nextInt(800) == 0) {
               this.w();
            }
         }
      } else {
         this.entityAge = 0;
      }
   }

   protected double minDistFromPlayerForDespawn() {
      return 32.0;
   }

   protected boolean isInsideSpawnAreaAroundPlayer(EntityPlayer player) {
      return this.isInsideSpawnAreaAroundChunk(MathHelper.floor_double(player.posX / 16.0), MathHelper.floor_double(player.posZ / 16.0));
   }

   protected boolean isInsideSpawnAreaAroundOriginalSpawn() {
      return this.worldObj.provider.dimensionId == 0
         && this.isInsideSpawnAreaAroundChunk(this.worldObj.worldInfo.getSpawnX() >> 4, this.worldObj.worldInfo.getSpawnZ() >> 4);
   }

   protected boolean isInsideSpawnAreaAroundChunk(int iChunkX, int iChunkZ) {
      int iValidRange = this.worldObj.getMobSpawnRangeInChunks();
      int iEntityChunkX = MathHelper.floor_double(this.posX / 16.0);
      int iDeltaX = iChunkX - iEntityChunkX;
      if (iDeltaX >= -iValidRange && iDeltaX <= iValidRange) {
         int iEntityChunkZ = MathHelper.floor_double(this.posZ / 16.0);
         int iDeltaZ = iChunkZ - iEntityChunkZ;
         if (iDeltaZ >= -iValidRange && iDeltaZ <= iValidRange) {
            return true;
         }
      }

      return false;
   }

   @Override
   public boolean attractsLightning() {
      return true;
   }

   public int getMeleeAttackStrength(Entity target) {
      return 2;
   }

   public boolean meleeAttack(Entity target) {
      this.setLastAttackingEntity(target);
      int iStrength = this.getMeleeAttackStrength(target);
      if (this.isPotionActive(Potion.damageBoost)) {
         iStrength += 3 << this.getActivePotionEffect(Potion.damageBoost).getAmplifier();
      }

      if (this.isPotionActive(Potion.weakness)) {
         iStrength -= 2 << this.getActivePotionEffect(Potion.weakness).getAmplifier();
      }

      int iKnockback = 0;
      if (target instanceof EntityLiving) {
         iStrength += EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLiving)target);
         iKnockback += EnchantmentHelper.getKnockbackModifier(this, (EntityLiving)target);
      }

      boolean bAttackSuccess = target.attackEntityFrom(DamageSource.causeMobDamage(this), iStrength);
      if (bAttackSuccess) {
         if (iKnockback > 0) {
            target.addVelocity(
               -MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * iKnockback * 0.5F,
               0.1,
               MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * iKnockback * 0.5F
            );
            this.motionX *= 0.6;
            this.motionZ *= 0.6;
         }

         int iFireModifier = EnchantmentHelper.getFireAspectModifier(this);
         if (iFireModifier > 0) {
            target.setFire(iFireModifier * 4);
         } else if (this.ae() && this.rand.nextFloat() < 0.6F) {
            target.setFire(4);
         }

         if (target instanceof EntityLiving) {
            EnchantmentThorns.func_92096_a(this, (EntityLiving)target, this.rand);
         }
      }

      return bAttackSuccess;
   }

   public float knockbackMagnitude() {
      return 0.4F;
   }

   public static boolean installationIntegrityTest() {
      return true;
   }

   @Environment(EnvType.CLIENT)
   public MovingObjectPosition mouseOverCustomRayTrace(double dDistance, float fInterpolationFactor) {
      Vec3 posVector = this.getPosition(fInterpolationFactor);
      Vec3 lookVector = this.getLook(fInterpolationFactor);
      Vec3 targetVector = posVector.addVector(lookVector.xCoord * dDistance, lookVector.yCoord * dDistance, lookVector.zCoord * dDistance);
      return this.worldObj.mouseOverRayTrace(posVector, targetVector);
   }
}
