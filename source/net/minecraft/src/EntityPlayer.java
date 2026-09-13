package net.minecraft.src;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import btw.BTWMod;
import btw.block.blocks.BedBlockBase;
import btw.block.tileentity.beacon.BeaconEffect;
import btw.block.tileentity.beacon.BeaconTileEntity;
import btw.entity.mob.CreeperEntity;
import btw.entity.mob.GhastEntity;
import btw.inventory.container.PlayerContainer;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.item.items.ArmorItemSteel;
import btw.util.status.PlayerStatusEffects;
import btw.util.status.StatusCategory;
import btw.util.status.StatusEffect;
import btw.world.util.difficulty.Difficulties;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;

public abstract class EntityPlayer extends EntityLiving implements ICommandSender {
   private static final float BED_SLEEP_SPEED_MODIFIER = 50.0F;
   public InventoryPlayer inventory = new InventoryPlayer(this);
   private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
   public Container inventoryContainer;
   public Container openContainer;
   public FoodStats foodStats = new FoodStats();
   protected int flyToggleTimer = 0;
   public byte field_71098_bD = 0;
   public float prevCameraYaw;
   public float cameraYaw;
   public String username;
   public int xpCooldown = 0;
   public double field_71091_bM;
   public double field_71096_bN;
   public double field_71097_bO;
   public double field_71094_bP;
   public double field_71095_bQ;
   public double field_71085_bR;
   protected boolean sleeping;
   public ChunkCoordinates playerLocation;
   private int sleepTimer;
   public float field_71079_bU;
   @Environment(EnvType.CLIENT)
   public float field_71082_cx;
   public float field_71089_bV;
   public int lastDeathLocationX;
   public int lastDeathLocationY;
   public int lastDeathLocationZ;
   public int lastDeathDimension;
   private ChunkCoordinates spawnChunk;
   private boolean spawnForced;
   private ChunkCoordinates startMinecartRidingCoordinate;
   public PlayerCapabilities capabilities = new PlayerCapabilities();
   public int experienceLevel;
   public int experienceTotal;
   public float experience;
   private ItemStack itemInUse;
   private int itemInUseCount;
   protected float speedOnGround = 0.1F;
   protected float speedInAir = 0.02F;
   private int field_82249_h = 0;
   public EntityFishHook fishEntity = null;
   public float timerSpeedModifier = 1.0F;
   private boolean usingSpecialKey = false;
   public ChunkCoordinates hardcoreSpawnChunk;
   protected long timeOfLastSpawnAssignment = 0L;
   public long timeOfLastDimensionSwitch = 0L;
   public long respawnAssignmentCooldownTimer = 0L;
   public int spawnDimension = 0;
   public int timesCraftedThisTick = 0;
   public int inGloomCounter = 0;
   public int airRecoveryCountdown = 0;
   public int ticksSinceEmoteSound = 0;
   protected float currentMiningSpeedModifier = 1.0F;
   public int deathCount = 0;
   public static final int GLOOM_COUNTER_BETWEEN_STATE_CHANGES = 1200;
   private static final int STRONGEST_MAGNETIC_POINT_FOR_LOCATION_I_DATA_WATCHER_ID = 22;
   private static final int STRONGEST_MAGNETIC_POINT_FOR_LOCATION_K_DATA_WATCHER_ID = 23;
   private static final int HAS_VALID_MAGNETIC_POINT_FOR_LOCATION_DATA_WATCHER_ID = 24;
   private static final int GLOOM_LEVEL_DATA_WATCHER_ID = 25;
   private static final int FAT_PENALTY_LEVEL_DATA_WATCHER_ID = 26;
   private static final int HUNGER_PENALTY_LEVEL_DATA_WATCHER_ID = 27;
   private static final int HEALTH_PENALTY_LEVEL_DATA_WATCHER_ID = 28;
   private static final int SPAWN_CHUNKS_VISUALIZATION_LOCATION_I_DATA_WATCHER_ID = 29;
   private static final int SPAWN_CHUNKS_VISUALIZATION_LOCATION_J_DATA_WATCHER_ID = 30;
   private static final int SPAWN_CHUNKS_VISUALIZATION_LOCATION_K_DATA_WATCHER_ID = 31;
   private static final int TICKS_BETWEEN_EMOTE_SOUNDS = 10;
   public static final float EXHAUSTION_JUMPING = 0.2F;
   public static final float EXHAUSTION_JUMPING_SPRINTING = 1.0F;
   @Environment(EnvType.CLIENT)
   private static final Map<String, String> uuids = new HashMap<>();

   public EntityPlayer(World par1World) {
      super(par1World);
      this.inventoryContainer = new PlayerContainer(this.inventory, !par1World.isRemote, this);
      this.openContainer = this.inventoryContainer;
      this.yOffset = 1.62F;
      ChunkCoordinates var2 = par1World.getSpawnPoint();
      this.b(var2.posX + 0.5, var2.posY + 1, var2.posZ + 0.5, 0.0F, 0.0F);
      this.entityType = "humanoid";
      this.field_70741_aB = 180.0F;
      this.fireResistance = 20;
      this.texture = "/mob/char.png";
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
      this.dataWatcher.addObject(16, (byte)0);
      this.dataWatcher.addObject(17, (byte)0);
      this.dataWatcher.addObject(18, 0);
      this.dataWatcher.addObject(24, new Byte((byte)0));
      this.dataWatcher.addObject(22, new Integer(0));
      this.dataWatcher.addObject(23, new Integer(0));
      this.dataWatcher.addObject(25, new Byte((byte)0));
      this.dataWatcher.addObject(26, new Byte((byte)0));
      this.dataWatcher.addObject(27, new Byte((byte)0));
      this.dataWatcher.addObject(28, new Byte((byte)0));
      this.dataWatcher.addObject(29, new Integer(0));
      this.dataWatcher.addObject(30, new Integer(0));
      this.dataWatcher.addObject(31, new Integer(0));
   }

   @Environment(EnvType.CLIENT)
   public ItemStack getItemInUse() {
      return this.itemInUse;
   }

   public int getItemInUseCount() {
      return this.itemInUseCount;
   }

   public boolean isUsingItem() {
      return this.itemInUse != null;
   }

   @Environment(EnvType.CLIENT)
   public int getItemInUseDuration() {
      return this.isUsingItem() ? this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount : 0;
   }

   public void stopUsingItem() {
      if (this.itemInUse != null) {
         this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
      }

      this.clearItemInUse();
   }

   public void clearItemInUse() {
      this.itemInUse = null;
      this.itemInUseCount = 0;
      if (!this.worldObj.isRemote) {
         this.e(false);
      }
   }

   @Override
   public boolean isBlocking() {
      return this.isUsingItem() && Item.itemsList[this.itemInUse.itemID].getItemUseAction(this.itemInUse) == EnumAction.block;
   }

   @Override
   public void onUpdate() {
      this.timesCraftedThisTick = 0;
      this.ticksSinceEmoteSound++;
      if (this.itemInUse != null) {
         ItemStack var1 = this.inventory.getCurrentItem();
         if (var1 == this.itemInUse
            || var1 != null
               && this.itemInUse.getItem().ignoreDamageWhenComparingDuringUse()
               && var1.itemID == this.itemInUse.itemID
               && ItemStack.areItemStackTagsEqual(this.itemInUse, var1)) {
            this.itemInUse = var1;
            if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0) {
               this.updateItemUse(var1, 5);
            }

            var1.getItem().updateUsingItem(var1, this.worldObj, this);
            if (--this.itemInUseCount == 0 && !this.worldObj.isRemote) {
               this.onItemUseFinish();
            }
         } else {
            this.clearItemInUse();
         }
      }

      if (this.xpCooldown > 0) {
         this.xpCooldown--;
      }

      if (this.isPlayerSleeping()) {
         this.sleepTimer++;
         if (this.sleepTimer > 100) {
            this.sleepTimer = 100;
            this.timerSpeedModifier = 50.0F;
         }

         if (!this.worldObj.isRemote) {
            if (!this.isInBed()) {
               this.wakeUpPlayer(true, true, false);
            } else if (this.worldObj.isDaytime()) {
               this.wakeUpPlayer(false, true, true);
            }
         }
      } else if (this.sleepTimer > 0) {
         this.sleepTimer++;
         if (this.sleepTimer >= 110) {
            this.sleepTimer = 0;
         }
      }

      super.onUpdate();
      if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
         this.closeScreen();
         this.openContainer = this.inventoryContainer;
      }

      if (this.ae() && this.capabilities.disableDamage) {
         this.A();
      }

      this.field_71091_bM = this.field_71094_bP;
      this.field_71096_bN = this.field_71095_bQ;
      this.field_71097_bO = this.field_71085_bR;
      double var9 = this.posX - this.field_71094_bP;
      double var3 = this.posY - this.field_71095_bQ;
      double var5 = this.posZ - this.field_71085_bR;
      double var7 = 10.0;
      if (var9 > var7) {
         this.field_71091_bM = this.field_71094_bP = this.posX;
      }

      if (var5 > var7) {
         this.field_71097_bO = this.field_71085_bR = this.posZ;
      }

      if (var3 > var7) {
         this.field_71096_bN = this.field_71095_bQ = this.posY;
      }

      if (var9 < -var7) {
         this.field_71091_bM = this.field_71094_bP = this.posX;
      }

      if (var5 < -var7) {
         this.field_71097_bO = this.field_71085_bR = this.posZ;
      }

      if (var3 < -var7) {
         this.field_71096_bN = this.field_71095_bQ = this.posY;
      }

      this.field_71094_bP += var9 * 0.25;
      this.field_71085_bR += var5 * 0.25;
      this.field_71095_bQ += var3 * 0.25;
      this.addStat(StatList.minutesPlayedStat, 1);
      if (this.ridingEntity == null) {
         this.startMinecartRidingCoordinate = null;
      }

      if (!this.worldObj.isRemote) {
         this.foodStats.onUpdate(this);
      }

      this.updateGloomState();
   }

   @Override
   public int getMaxInPortalTime() {
      return this.capabilities.disableDamage ? 0 : 80;
   }

   @Override
   public int getPortalCooldown() {
      return 10;
   }

   @Override
   public void playSound(String par1Str, float par2, float par3) {
      this.worldObj.playSoundToNearExcept(this, par1Str, par2, par3);
   }

   protected void updateItemUse(ItemStack par1ItemStack, int par2) {
      if (par1ItemStack.getItemUseAction() == EnumAction.drink) {
         this.playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
      }

      if (par1ItemStack.getItemUseAction() == EnumAction.eat) {
         for (int var3 = 0; var3 < par2; var3++) {
            Vec3 var4 = this.worldObj.getWorldVec3Pool().getVecFromPool((this.rand.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0);
            var4.rotateAroundX(-this.rotationPitch * (float) Math.PI / 180.0F);
            var4.rotateAroundY(-this.rotationYaw * (float) Math.PI / 180.0F);
            Vec3 var5 = this.worldObj.getWorldVec3Pool().getVecFromPool((this.rand.nextFloat() - 0.5) * 0.3, -this.rand.nextFloat() * 0.6 - 0.3, 0.6);
            var5.rotateAroundX(-this.rotationPitch * (float) Math.PI / 180.0F);
            var5.rotateAroundY(-this.rotationYaw * (float) Math.PI / 180.0F);
            var5 = var5.addVector(this.posX, this.posY + this.getEyeHeight(), this.posZ);
            this.worldObj
               .spawnParticle(
                  "iconcrack_" + par1ItemStack.getItem().itemID, var5.xCoord, var5.yCoord, var5.zCoord, var4.xCoord, var4.yCoord + 0.05, var4.zCoord
               );
         }

         this.playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
      }
   }

   protected void onItemUseFinish() {
      if (this.itemInUse != null) {
         this.updateItemUse(this.itemInUse, 16);
         int var1 = this.itemInUse.stackSize;
         ItemStack var2 = this.itemInUse.onFoodEaten(this.worldObj, this);
         if (var2 != this.itemInUse || var2 != null && var2.stackSize != var1) {
            this.inventory.mainInventory[this.inventory.currentItem] = var2;
            if (var2.stackSize == 0) {
               this.inventory.mainInventory[this.inventory.currentItem] = null;
            }
         }

         this.clearItemInUse();
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 9) {
         this.onItemUseFinish();
      } else {
         super.handleHealthUpdate(par1);
      }
   }

   @Override
   protected boolean isMovementBlocked() {
      return this.aX() <= 0 || this.isPlayerSleeping();
   }

   protected void closeScreen() {
      this.openContainer = this.inventoryContainer;
   }

   @Override
   public void mountEntity(Entity par1Entity) {
      if (this.ridingEntity == par1Entity) {
         this.unmountEntity(par1Entity);
         if (this.ridingEntity != null) {
            this.ridingEntity.riddenByEntity = null;
         }

         this.ridingEntity = null;
      } else {
         super.a(par1Entity);
      }
   }

   @Override
   public void updateRidden() {
      double var1 = this.posX;
      double var3 = this.posY;
      double var5 = this.posZ;
      float var7 = this.rotationYaw;
      float var8 = this.rotationPitch;
      super.updateRidden();
      this.prevCameraYaw = this.cameraYaw;
      this.cameraYaw = 0.0F;
      this.addMountedMovementStat(this.posX - var1, this.posY - var3, this.posZ - var5);
      if (this.ridingEntity instanceof EntityPig) {
         this.rotationPitch = var8;
         this.rotationYaw = var7;
         this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void preparePlayerToSpawn() {
      this.yOffset = 1.62F;
      this.a(0.6F, 1.8F);
      super.v();
      this.b(this.getMaxHealth());
      this.deathTime = 0;
   }

   @Override
   protected void updateEntityActionState() {
      this.br();
   }

   @Override
   public void onLivingUpdate() {
      if (this.flyToggleTimer > 0) {
         this.flyToggleTimer--;
      }

      if (this.worldObj.difficultySetting == 0 && this.aX() < this.getMaxHealth() && this.ticksExisted % 20 * 10 == 0) {
         this.j(1);
      }

      this.inventory.decrementAnimations();
      this.prevCameraYaw = this.cameraYaw;
      super.onLivingUpdate();
      this.landMovementFactor = this.capabilities.getWalkSpeed();
      this.jumpMovementFactor = this.speedInAir;
      this.jumpMovementFactor = this.jumpMovementFactor * this.getJumpingHorizontalMovementModifier();
      if (this.ah()) {
         this.landMovementFactor = (float)(this.landMovementFactor + this.capabilities.getWalkSpeed() * 0.3);
         this.jumpMovementFactor = (float)(this.jumpMovementFactor + this.speedInAir * 0.3);
      }

      float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
      float var2 = (float)Math.atan(-this.motionY * 0.2F) * 15.0F;
      if (var1 > 0.1F) {
         var1 = 0.1F;
      }

      if (!this.onGround || this.aX() <= 0) {
         var1 = 0.0F;
      }

      if (this.onGround || this.aX() <= 0) {
         var2 = 0.0F;
      }

      this.cameraYaw = this.cameraYaw + (var1 - this.cameraYaw) * 0.4F;
      this.cameraPitch = this.cameraPitch + (var2 - this.cameraPitch) * 0.8F;
      if (this.aX() > 0) {
         List var3 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.expand(1.0, 0.5, 1.0));
         if (var3 != null) {
            for (int var4 = 0; var4 < var3.size(); var4++) {
               Entity var5 = (Entity)var3.get(var4);
               if (!var5.isDead) {
                  this.collideWithPlayer(var5);
               }
            }
         }
      }
   }

   private void collideWithPlayer(Entity par1Entity) {
      par1Entity.onCollideWithPlayer(this);
   }

   public int getScore() {
      return this.dataWatcher.getWatchableObjectInt(18);
   }

   public void setScore(int par1) {
      this.dataWatcher.updateObject(18, par1);
   }

   public void addScore(int par1) {
      int var2 = this.getScore();
      this.dataWatcher.updateObject(18, var2 + par1);
   }

   @Override
   public void onDeath(DamageSource par1DamageSource) {
      super.onDeath(par1DamageSource);
      this.a(0.2F, 0.2F);
      this.b(this.posX, this.posY, this.posZ);
      this.motionY = 0.1F;
      if (this.username.equals("Notch")) {
         this.dropPlayerItemWithRandomChoice(new ItemStack(Item.appleRed, 1), true);
      }

      if (!this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
         this.inventory.dropAllItems();
      }

      if (par1DamageSource != null) {
         this.motionX = -MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * (float) Math.PI / 180.0F) * 0.1F;
         this.motionZ = -MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * (float) Math.PI / 180.0F) * 0.1F;
      } else {
         this.motionX = this.motionZ = 0.0;
      }

      this.yOffset = 0.1F;
      this.addStat(StatList.deathsStat, 1);
   }

   @Override
   public void addToPlayerScore(Entity par1Entity, int par2) {
      this.addScore(par2);
      Collection var3 = this.getWorldScoreboard().func_96520_a(ScoreObjectiveCriteria.field_96640_e);
      if (par1Entity instanceof EntityPlayer) {
         this.addStat(StatList.playerKillsStat, 1);
         var3.addAll(this.getWorldScoreboard().func_96520_a(ScoreObjectiveCriteria.field_96639_d));
      } else {
         this.addStat(StatList.mobKillsStat, 1);
      }

      for (ScoreObjective var5 : var3) {
         Score var6 = this.getWorldScoreboard().func_96529_a(this.getEntityName(), var5);
         var6.func_96648_a();
      }
   }

   public EntityItem dropOneItem(boolean par1) {
      return this.dropPlayerItemWithRandomChoice(
         this.inventory
            .decrStackSize(this.inventory.currentItem, par1 && this.inventory.getCurrentItem() != null ? this.inventory.getCurrentItem().stackSize : 1),
         false
      );
   }

   public EntityItem dropPlayerItem(ItemStack par1ItemStack) {
      return this.dropPlayerItemWithRandomChoice(par1ItemStack, false);
   }

   public EntityItem dropPlayerItemWithRandomChoice(ItemStack par1ItemStack, boolean par2) {
      if (par1ItemStack == null) {
         return null;
      } else {
         EntityItem var3 = (EntityItem)EntityList.createEntityOfType(
            EntityItem.class, this.worldObj, this.posX, this.posY - 0.3F + this.getEyeHeight(), this.posZ, par1ItemStack
         );
         var3.delayBeforeCanPickup = 40;
         float var4 = 0.1F;
         if (par2) {
            float var5 = this.rand.nextFloat() * 0.5F;
            float var6 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
            var3.motionX = -MathHelper.sin(var6) * var5;
            var3.motionZ = MathHelper.cos(var6) * var5;
            var3.motionY = 0.2F;
            var3.setEntityItemAsDroppedOnPlayerDeath(this);
         } else {
            var4 = 0.3F;
            var3.motionX = -MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var4;
            var3.motionZ = MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI) * var4;
            var3.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI) * var4 + 0.1F;
            var4 = 0.02F;
            float var5 = this.rand.nextFloat() * (float) Math.PI * 2.0F;
            var4 *= this.rand.nextFloat();
            var3.motionX = var3.motionX + Math.cos(var5) * var4;
            var3.motionY = var3.motionY + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F;
            var3.motionZ = var3.motionZ + Math.sin(var5) * var4;
         }

         this.joinEntityItemWithWorld(var3);
         this.addStat(StatList.dropStat, 1);
         return var3;
      }
   }

   protected void joinEntityItemWithWorld(EntityItem par1EntityItem) {
      this.worldObj.spawnEntityInWorld(par1EntityItem);
   }

   public float getCurrentPlayerStrVsBlock(Block par1Block, int i, int j, int k) {
      float var3x = this.inventory.getStrVsBlock(this.worldObj, par1Block, i, j, k);
      if (var3x > 1.0F) {
         int var4x = EnchantmentHelper.getEfficiencyModifier(this);
         ItemStack var5 = this.inventory.getCurrentItem();
         if (var4x > 0 && var5 != null) {
            float var6 = var4x * var4x + 1;
            if (!var5.canHarvestBlock(this.worldObj, par1Block, i, j, k) && var3x <= 1.0F) {
               var3x += var6 * 0.08F;
            } else {
               var3x += var6;
            }
         }
      }

      if (this.a(Potion.digSpeed)) {
         var3x *= 1.0F + (this.b(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
      }

      if (this.a(Potion.digSlowdown)) {
         var3x *= 1.0F - (this.b(Potion.digSlowdown).getAmplifier() + 1) * 0.2F;
      }

      if (this.a(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this)) {
         var3x /= 5.0F;
      }

      if (!this.onGround) {
         var3x /= 5.0F;
      }

      return var3x * this.getMiningSpeedModifier();
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
      NBTTagList var2 = par1NBTTagCompound.getTagList("Inventory");
      this.inventory.readFromNBT(var2);
      this.inventory.currentItem = par1NBTTagCompound.getInteger("SelectedItemSlot");
      this.sleeping = par1NBTTagCompound.getBoolean("Sleeping");
      this.sleepTimer = par1NBTTagCompound.getShort("SleepTimer");
      this.experience = par1NBTTagCompound.getFloat("XpP");
      this.experienceLevel = par1NBTTagCompound.getInteger("XpLevel");
      this.experienceTotal = par1NBTTagCompound.getInteger("XpTotal");
      this.setScore(par1NBTTagCompound.getInteger("Score"));
      if (this.sleeping) {
         this.playerLocation = new ChunkCoordinates(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ));
         this.wakeUpPlayer(true, true, false);
      }

      if (par1NBTTagCompound.hasKey("SpawnX") && par1NBTTagCompound.hasKey("SpawnY") && par1NBTTagCompound.hasKey("SpawnZ")) {
         this.spawnChunk = new ChunkCoordinates(
            par1NBTTagCompound.getInteger("SpawnX"), par1NBTTagCompound.getInteger("SpawnY"), par1NBTTagCompound.getInteger("SpawnZ")
         );
         this.spawnForced = par1NBTTagCompound.getBoolean("SpawnForced");
      }

      this.foodStats.readNBT(par1NBTTagCompound);
      this.capabilities.readCapabilitiesFromNBT(par1NBTTagCompound);
      if (par1NBTTagCompound.hasKey("EnderItems")) {
         NBTTagList var3 = par1NBTTagCompound.getTagList("EnderItems");
         this.theInventoryEnderChest.loadInventoryFromNBT(var3);
      }

      this.readModDataFromNBT(par1NBTTagCompound);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setTag("Inventory", this.inventory.writeToNBT(new NBTTagList()));
      par1NBTTagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
      par1NBTTagCompound.setBoolean("Sleeping", this.sleeping);
      par1NBTTagCompound.setShort("SleepTimer", (short)this.sleepTimer);
      par1NBTTagCompound.setFloat("XpP", this.experience);
      par1NBTTagCompound.setInteger("XpLevel", this.experienceLevel);
      par1NBTTagCompound.setInteger("XpTotal", this.experienceTotal);
      par1NBTTagCompound.setInteger("Score", this.getScore());
      if (this.spawnChunk != null) {
         par1NBTTagCompound.setInteger("SpawnX", this.spawnChunk.posX);
         par1NBTTagCompound.setInteger("SpawnY", this.spawnChunk.posY);
         par1NBTTagCompound.setInteger("SpawnZ", this.spawnChunk.posZ);
         par1NBTTagCompound.setBoolean("SpawnForced", this.spawnForced);
      }

      this.foodStats.writeNBT(par1NBTTagCompound);
      this.capabilities.writeCapabilitiesToNBT(par1NBTTagCompound);
      par1NBTTagCompound.setTag("EnderItems", this.theInventoryEnderChest.saveInventoryToNBT());
      this.writeModDataToNBT(par1NBTTagCompound);
   }

   public void displayGUIChest(IInventory par1IInventory) {
   }

   public void displayGUIHopper(TileEntityHopper par1TileEntityHopper) {
   }

   public void displayGUIHopperMinecart(EntityMinecartHopper par1EntityMinecartHopper) {
   }

   public void displayGUIEnchantment(int par1, int par2, int par3, String par4Str) {
   }

   public void displayGUIAnvil(int par1, int par2, int par3) {
   }

   public void displayGUIWorkbench(int par1, int par2, int par3) {
   }

   @Override
   public float getEyeHeight() {
      return 0.12F;
   }

   protected void resetHeight() {
      this.yOffset = 1.62F;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if (this.capabilities.disableDamage && !par1DamageSource.canHarmInCreative()) {
         return false;
      } else {
         this.entityAge = 0;
         if (this.aX() <= 0) {
            return false;
         } else {
            if (this.isPlayerSleeping() && !this.worldObj.isRemote) {
               this.wakeUpPlayer(true, true, false);
            }

            if (par1DamageSource.isDifficultyScaled()) {
               if (this.worldObj.difficultySetting == 0) {
                  par2 = 0;
               }

               if (this.worldObj.difficultySetting == 1) {
                  par2 = par2 / 2 + 1;
               }

               if (this.worldObj.difficultySetting == 3) {
                  par2 = par2 * 3 / 2;
               }
            }

            if (par2 == 0) {
               return false;
            } else {
               Entity var3 = par1DamageSource.getEntity();
               if (var3 instanceof EntityArrow && ((EntityArrow)var3).shootingEntity != null) {
                  var3 = ((EntityArrow)var3).shootingEntity;
               }

               if (var3 instanceof EntityLiving) {
                  this.alertWolves((EntityLiving)var3, false);
               }

               this.addStat(StatList.damageTakenStat, par2);
               if (!this.isDead && this.isCarryingBlastingOil()) {
                  this.detonateCarriedBlastingOil();
                  return false;
               } else {
                  return super.attackEntityFrom(par1DamageSource, par2);
               }
            }
         }
      }
   }

   public boolean func_96122_a(EntityPlayer par1EntityPlayer) {
      ScorePlayerTeam var2 = this.getTeam();
      ScorePlayerTeam var3 = par1EntityPlayer.getTeam();
      return var2 != var3 ? true : (var2 != null ? var2.func_96665_g() : true);
   }

   protected void alertWolves(EntityLiving par1EntityLiving, boolean par2) {
      par2 = true;
      if (!(par1EntityLiving instanceof CreeperEntity) && !(par1EntityLiving instanceof GhastEntity)) {
         if (par1EntityLiving instanceof EntityWolf) {
            EntityWolf var3 = (EntityWolf)par1EntityLiving;
            if (var3.m() && this.username.equals(var3.o())) {
               return;
            }
         }

         if (!(par1EntityLiving instanceof EntityPlayer) || this.func_96122_a((EntityPlayer)par1EntityLiving)) {
            for (EntityWolf var5 : this.worldObj
               .getEntitiesWithinAABB(
                  EntityWolf.class,
                  AxisAlignedBB.getAABBPool()
                     .getAABB(this.posX, this.posY, this.posZ, this.posX + 1.0, this.posY + 1.0, this.posZ + 1.0)
                     .expand(16.0, 4.0, 16.0)
               )) {
               if (var5.m() && var5.l() == null && this.username.equals(var5.o()) && (!par2 || !var5.n())) {
                  var5.k(false);
                  var5.b(par1EntityLiving);
               }
            }
         }
      }
   }

   @Override
   protected void damageArmor(int par1) {
      this.inventory.damageArmor(par1);
   }

   @Override
   public int getTotalArmorValue() {
      return this.inventory.getTotalArmorValue();
   }

   public float func_82243_bO() {
      int var1 = 0;

      for (ItemStack var5 : this.inventory.armorInventory) {
         if (var5 != null) {
            var1++;
         }
      }

      return (float)var1 / this.inventory.armorInventory.length;
   }

   @Override
   protected void damageEntity(DamageSource par1DamageSource, int par2) {
      if (!this.aq()) {
         if (!par1DamageSource.isUnblockable() && this.isBlocking()) {
            this.onBlockedDamage(par1DamageSource, par2);
            par2 = 1 + par2 >> 1;
         }

         par2 = this.b(par1DamageSource, par2);
         par2 = this.c(par1DamageSource, par2);
         this.addExhaustion(par1DamageSource.getHungerDamage());
         int var3 = this.aX();
         this.b(this.aX() - par2);
         this.field_94063_bt.func_94547_a(par1DamageSource, var3, par2);
      }
   }

   public void displayGUIFurnace(TileEntityFurnace par1TileEntityFurnace) {
   }

   public void displayGUIDispenser(TileEntityDispenser par1TileEntityDispenser) {
   }

   public void displayGUIEditSign(TileEntity par1TileEntity) {
   }

   public void displayGUIBrewingStand(TileEntityBrewingStand par1TileEntityBrewingStand) {
   }

   public void displayGUIBeacon(TileEntityBeacon par1TileEntityBeacon) {
   }

   public void displayGUIMerchant(IMerchant par1IMerchant, String par2Str) {
   }

   public void displayGUIBook(ItemStack par1ItemStack) {
   }

   public boolean interactWith(Entity par1Entity) {
      if (par1Entity.interact(this)) {
         return true;
      } else {
         ItemStack var2 = this.getCurrentEquippedItem();
         if (var2 != null && par1Entity instanceof EntityLiving) {
            if (this.capabilities.isCreativeMode) {
               var2 = var2.copy();
            }

            if (var2.interactWith((EntityLiving)par1Entity)) {
               if (var2.stackSize <= 0 && !this.capabilities.isCreativeMode) {
                  this.destroyCurrentEquippedItem();
               }

               return true;
            }
         }

         return false;
      }
   }

   public ItemStack getCurrentEquippedItem() {
      return this.inventory.getCurrentItem();
   }

   public void destroyCurrentEquippedItem() {
      this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
   }

   @Override
   public double getYOffset() {
      return this.yOffset - 0.5F;
   }

   public void attackTargetEntityWithCurrentItem(Entity par1Entity) {
      if (par1Entity.canAttackWithItem() && !par1Entity.func_85031_j(this)) {
         int var2 = this.inventory.getDamageVsEntity(par1Entity);
         if (this.a(Potion.damageBoost)) {
            var2 += 3 << this.b(Potion.damageBoost).getAmplifier();
         }

         if (this.a(Potion.weakness)) {
            var2 -= 2 << this.b(Potion.weakness).getAmplifier();
         }

         int var3 = 0;
         int var4 = 0;
         if (par1Entity instanceof EntityLiving) {
            var4 = EnchantmentHelper.getEnchantmentModifierLiving(this, (EntityLiving)par1Entity);
            var3 += EnchantmentHelper.getKnockbackModifier(this, (EntityLiving)par1Entity);
         }

         if (this.ah()) {
            var3++;
         }

         float fModifier = this.getMeleeDamageModifier();
         if (fModifier < 0.99F) {
            var2 = (int)(var2 * fModifier);
         }

         if (var2 <= 0 && var4 <= 0) {
            this.onZeroDamageAttack();
         } else {
            boolean var5 = this.fallDistance > 0.0F
               && !this.onGround
               && !this.isOnLadder()
               && !this.G()
               && !this.a(Potion.blindness)
               && this.ridingEntity == null
               && par1Entity instanceof EntityLiving;
            if (var5 && var2 > 0) {
               var2 += this.rand.nextInt(var2 / 2 + 2);
            }

            var2 += var4;
            boolean var6 = false;
            int var7 = EnchantmentHelper.getFireAspectModifier(this);
            if (par1Entity instanceof EntityLiving && var7 > 0 && !par1Entity.isBurning()) {
               var6 = true;
               par1Entity.setFire(1);
            }

            boolean var8 = par1Entity.attackEntityFrom(DamageSource.causePlayerDamage(this), var2);
            if (var8) {
               if (var3 > 0) {
                  par1Entity.addVelocity(
                     -MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * var3 * 0.5F,
                     0.1,
                     MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * var3 * 0.5F
                  );
                  this.motionX *= 0.6;
                  this.motionZ *= 0.6;
                  this.c(false);
               }

               if (var5) {
                  this.onCriticalHit(par1Entity);
               }

               if (var4 > 0) {
                  this.onEnchantmentCritical(par1Entity);
               }

               if (var2 >= 18) {
                  this.triggerAchievement(AchievementList.overkill);
               }

               this.l(par1Entity);
               if (par1Entity instanceof EntityLiving) {
                  EnchantmentThorns.func_92096_a(this, (EntityLiving)par1Entity, this.rand);
               }
            }

            ItemStack var9 = this.getCurrentEquippedItem();
            Object var10 = par1Entity;
            if (par1Entity instanceof EntityDragonPart) {
               IEntityMultiPart var11 = ((EntityDragonPart)par1Entity).entityDragonObj;
               if (var11 != null && var11 instanceof EntityLiving) {
                  var10 = (EntityLiving)var11;
               }
            }

            if (var9 != null && var10 instanceof EntityLiving) {
               var9.hitEntity((EntityLiving)var10, this);
               if (var9.stackSize <= 0) {
                  this.destroyCurrentEquippedItem();
               }
            }

            if (par1Entity instanceof EntityLiving) {
               if (par1Entity.isEntityAlive()) {
                  this.alertWolves((EntityLiving)par1Entity, true);
               }

               this.addStat(StatList.damageDealtStat, var2);
               if (var7 > 0 && var8) {
                  par1Entity.setFire(var7 * 4);
               } else if (var6) {
                  par1Entity.extinguish();
               }
            }

            this.addExhaustion(0.3F);
         }
      }
   }

   public void onCriticalHit(Entity par1Entity) {
   }

   public void onEnchantmentCritical(Entity par1Entity) {
   }

   @Environment(EnvType.CLIENT)
   public void respawnPlayer() {
   }

   @Override
   public void setDead() {
      super.w();
      this.inventoryContainer.onCraftGuiClosed(this);
      if (this.openContainer != null) {
         this.openContainer.onCraftGuiClosed(this);
      }
   }

   @Override
   public boolean isEntityInsideOpaqueBlock() {
      return !this.sleeping && super.S();
   }

   public boolean func_71066_bF() {
      return false;
   }

   public EnumStatus sleepInBedAt(int par1, int par2, int par3) {
      if (!this.worldObj.isRemote) {
         if (this.isPlayerSleeping() || !this.R()) {
            return EnumStatus.OTHER_PROBLEM;
         }

         if (!this.worldObj.provider.isSurfaceWorld()) {
            return EnumStatus.NOT_POSSIBLE_HERE;
         }

         if (this.worldObj.isDaytime()) {
            return EnumStatus.NOT_POSSIBLE_NOW;
         }

         if (Math.abs(this.posX - par1) > 3.0 || Math.abs(this.posY - par2) > 2.0 || Math.abs(this.posZ - par3) > 3.0) {
            return EnumStatus.TOO_FAR_AWAY;
         }
      }

      this.a(0.2F, 0.2F);
      this.yOffset = 0.2F;
      if (this.worldObj.blockExists(par1, par2, par3)) {
         int var9 = this.worldObj.getBlockMetadata(par1, par2, par3);
         int var5 = BlockBed.j(var9);
         float var10 = 0.5F;
         float var7 = 0.5F;
         switch (var5) {
            case 0:
               var7 = 0.9F;
               break;
            case 1:
               var10 = 0.1F;
               break;
            case 2:
               var7 = 0.1F;
               break;
            case 3:
               var10 = 0.9F;
         }

         this.func_71013_b(var5);
         this.b(par1 + var10, par2 + 0.9375F, par3 + var7);
      } else {
         this.b(par1 + 0.5F, par2 + 0.9375F, par3 + 0.5F);
      }

      this.sleeping = true;
      this.sleepTimer = 0;
      this.playerLocation = new ChunkCoordinates(par1, par2, par3);
      this.motionX = this.motionZ = this.motionY = 0.0;
      if (!this.worldObj.isRemote) {
         this.worldObj.updateAllPlayersSleepingFlag();
      }

      return EnumStatus.OK;
   }

   private void func_71013_b(int par1) {
      this.field_71079_bU = 0.0F;
      this.field_71089_bV = 0.0F;
      switch (par1) {
         case 0:
            this.field_71089_bV = -1.8F;
            break;
         case 1:
            this.field_71079_bU = 1.8F;
            break;
         case 2:
            this.field_71089_bV = 1.8F;
            break;
         case 3:
            this.field_71079_bU = -1.8F;
      }
   }

   public void wakeUpPlayer(boolean par1, boolean par2, boolean par3) {
      this.a(0.6F, 1.8F);
      this.resetHeight();
      ChunkCoordinates var4 = this.playerLocation;
      ChunkCoordinates var5 = this.playerLocation;
      if (var4 != null && Block.blocksList[this.worldObj.getBlockId(var4.posX, var4.posY, var4.posZ)] instanceof BedBlockBase) {
         BlockBed.setBedOccupied(this.worldObj, var4.posX, var4.posY, var4.posZ, false);
         var5 = BlockBed.getNearestEmptyChunkCoordinates(this.worldObj, var4.posX, var4.posY, var4.posZ, 0);
         if (var5 == null) {
            var5 = new ChunkCoordinates(var4.posX, var4.posY + 1, var4.posZ);
         }

         this.b(var5.posX + 0.5F, var5.posY + this.yOffset + 0.1F, var5.posZ + 0.5F);
      }

      this.sleeping = false;
      this.resetTimerSpeedModifier();
      if (!this.worldObj.isRemote && par2) {
         this.worldObj.updateAllPlayersSleepingFlag();
      }

      if (par1) {
         this.sleepTimer = 0;
      } else {
         this.sleepTimer = 100;
      }
   }

   private boolean isInBed() {
      return Block.blocksList[this.worldObj.getBlockId(this.playerLocation.posX, this.playerLocation.posY, this.playerLocation.posZ)] instanceof BedBlockBase;
   }

   public static ChunkCoordinates verifyRespawnCoordinates(World par0World, ChunkCoordinates par1ChunkCoordinates, boolean par2) {
      IChunkProvider var3 = par0World.getChunkProvider();
      var3.loadChunk(par1ChunkCoordinates.posX - 3 >> 4, par1ChunkCoordinates.posZ - 3 >> 4);
      var3.loadChunk(par1ChunkCoordinates.posX + 3 >> 4, par1ChunkCoordinates.posZ - 3 >> 4);
      var3.loadChunk(par1ChunkCoordinates.posX - 3 >> 4, par1ChunkCoordinates.posZ + 3 >> 4);
      var3.loadChunk(par1ChunkCoordinates.posX + 3 >> 4, par1ChunkCoordinates.posZ + 3 >> 4);
      if (par0World.getBlockId(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ) == Block.bed.blockID) {
         return BlockBed.getNearestEmptyChunkCoordinates(par0World, par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ, 0);
      } else {
         Material var4 = par0World.getBlockMaterial(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY, par1ChunkCoordinates.posZ);
         Material var5 = par0World.getBlockMaterial(par1ChunkCoordinates.posX, par1ChunkCoordinates.posY + 1, par1ChunkCoordinates.posZ);
         boolean var6 = !var4.isSolid() && !var4.isLiquid();
         boolean var7 = !var5.isSolid() && !var5.isLiquid();
         return par2 && var6 && var7 ? par1ChunkCoordinates : null;
      }
   }

   @Environment(EnvType.CLIENT)
   public float getBedOrientationInDegrees() {
      if (this.playerLocation != null) {
         int var1 = this.worldObj.getBlockMetadata(this.playerLocation.posX, this.playerLocation.posY, this.playerLocation.posZ);
         int var2 = BlockBed.j(var1);
         switch (var2) {
            case 0:
               return 90.0F;
            case 1:
               return 0.0F;
            case 2:
               return 270.0F;
            case 3:
               return 180.0F;
         }
      }

      return 0.0F;
   }

   @Override
   public boolean isPlayerSleeping() {
      return this.sleeping;
   }

   public boolean isPlayerFullyAsleep() {
      return this.sleeping && this.sleepTimer >= 100;
   }

   @Environment(EnvType.CLIENT)
   public int getSleepTimer() {
      return this.sleepTimer;
   }

   @Environment(EnvType.CLIENT)
   protected boolean getHideCape(int par1) {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1 << par1) != 0;
   }

   protected void setHideCape(int par1, boolean par2) {
      byte var3 = this.dataWatcher.getWatchableObjectByte(16);
      if (par2) {
         this.dataWatcher.updateObject(16, (byte)(var3 | 1 << par1));
      } else {
         this.dataWatcher.updateObject(16, (byte)(var3 & ~(1 << par1)));
      }
   }

   public void addChatMessage(String par1Str) {
   }

   public ChunkCoordinates getBedLocation() {
      return this.spawnChunk;
   }

   public boolean isSpawnForced() {
      return this.spawnForced;
   }

   public void setSpawnChunk(ChunkCoordinates par1ChunkCoordinates, boolean par2) {
      if (par1ChunkCoordinates != null) {
         this.spawnChunk = new ChunkCoordinates(par1ChunkCoordinates);
         this.spawnForced = par2;
      } else {
         this.spawnChunk = null;
         this.spawnForced = false;
      }

      this.spawnDimension = 0;
   }

   public void triggerAchievement(StatBase par1StatBase) {
      this.addStat(par1StatBase, 1);
   }

   public void addStat(StatBase par1StatBase, int par2) {
   }

   @Override
   public void jump() {
      super.jump();
      this.addStat(StatList.jumpStat, 1);
      this.addExhaustionForJump();
   }

   @Override
   public void moveEntityWithHeading(float par1, float par2) {
      double var3 = this.posX;
      double var5 = this.posY;
      double var7 = this.posZ;
      if (this.capabilities.isFlying && this.ridingEntity == null) {
         double var9 = this.motionY;
         float var11 = this.jumpMovementFactor;
         this.jumpMovementFactor = this.capabilities.getFlySpeed();
         super.moveEntityWithHeading(par1, par2);
         this.motionY = var9 * 0.6;
         this.jumpMovementFactor = var11;
      } else {
         super.moveEntityWithHeading(par1, par2);
      }

      this.addMovementStat(this.posX - var3, this.posY - var5, this.posZ - var7);
   }

   public void addMovementStat(double par1, double par3, double par5) {
      if (this.ridingEntity == null) {
         if (this.G() && par3 > 0.0 && this.canSwim()) {
            float exhaustionAmount = 0.025F;
            this.addExhaustion(exhaustionAmount * this.worldObj.getDifficulty().getHungerIntensiveActionCostMultiplier());
         }

         if (this.a(Material.water)) {
            int var7 = Math.round(MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5) * 100.0F);
            if (var7 > 0) {
               this.addStat(StatList.distanceDoveStat, var7);
               this.addExhaustionWithoutVisualFeedback(0.015F * var7 * 0.01F);
            }
         } else if (this.G()) {
            int var7 = Math.round(MathHelper.sqrt_double(par1 * par1 + par5 * par5) * 100.0F);
            if (var7 > 0) {
               this.addStat(StatList.distanceSwumStat, var7);
               this.addExhaustionWithoutVisualFeedback(0.015F * var7 * 0.01F);
            }
         } else if (this.isOnLadder()) {
            if (par3 > 0.0) {
               this.addStat(StatList.distanceClimbedStat, (int)Math.round(par3 * 100.0));
            }
         } else if (this.onGround) {
            int var7 = Math.round(MathHelper.sqrt_double(par1 * par1 + par5 * par5) * 100.0F);
            if (var7 > 0) {
               this.addStat(StatList.distanceWalkedStat, var7);
               if (this.ah()) {
                  float exhaustionAmount = 0.1F;
                  this.addExhaustion(exhaustionAmount * var7 * 0.01F * this.worldObj.getDifficulty().getHungerIntensiveActionCostMultiplier());
               } else {
                  this.addExhaustionWithoutVisualFeedback(0.01F * var7 * 0.01F);
               }
            }
         } else {
            int var7 = Math.round(MathHelper.sqrt_double(par1 * par1 + par5 * par5) * 100.0F);
            if (var7 > 25) {
               this.addStat(StatList.distanceFlownStat, var7);
            }
         }
      }
   }

   private void addMountedMovementStat(double par1, double par3, double par5) {
      if (this.ridingEntity != null) {
         int var7 = Math.round(MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5) * 100.0F);
         if (var7 > 0) {
            if (this.ridingEntity instanceof EntityMinecart) {
               this.addStat(StatList.distanceByMinecartStat, var7);
               if (this.startMinecartRidingCoordinate == null) {
                  this.startMinecartRidingCoordinate = new ChunkCoordinates(
                     MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)
                  );
               } else if (this.startMinecartRidingCoordinate
                     .getDistanceSquared(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))
                  >= 1000000.0) {
                  this.addStat(AchievementList.onARail, 1);
               }
            } else if (this.ridingEntity instanceof EntityBoat) {
               this.addStat(StatList.distanceByBoatStat, var7);
            } else if (this.ridingEntity instanceof EntityPig) {
               this.addStat(StatList.distanceByPigStat, var7);
            }
         }
      }
   }

   @Override
   protected void fall(float par1) {
      if (!this.capabilities.allowFlying) {
         if (par1 >= 2.0F) {
            this.addStat(StatList.distanceFallenStat, (int)Math.round(par1 * 100.0));
         }

         super.fall(par1);
      }
   }

   @Override
   public void onKillEntity(EntityLiving par1EntityLiving) {
      if (par1EntityLiving instanceof IMob) {
         this.triggerAchievement(AchievementList.killEnemy);
      }
   }

   @Override
   public void setInWeb() {
      if (!this.capabilities.isFlying) {
         super.al();
      }
   }

   @Environment(EnvType.CLIENT)
   @Override
   public Icon getItemIcon(ItemStack itemStack, int index) {
      Icon icon = super.getItemIcon(itemStack, index);
      if (itemStack.getItem().requiresMultipleRenderPasses()) {
         return itemStack.getItem().getIconFromDamageForRenderPass(itemStack.getItemDamage(), index);
      } else {
         return itemStack.getItem().getAnimationIcon(this) != null ? itemStack.getItem().getAnimationIcon(this) : icon;
      }
   }

   @Override
   public ItemStack getCurrentArmor(int par1) {
      return this.inventory.armorItemInSlot(par1);
   }

   @Override
   protected void addRandomArmor() {
   }

   @Override
   protected void func_82162_bC() {
   }

   public void addExperience(int par1) {
      this.addScore(par1);
      int var2 = Integer.MAX_VALUE - this.experienceTotal;
      if (par1 > var2) {
         par1 = var2;
      }

      this.experience = this.experience + (float)par1 / this.xpBarCap();

      for (this.experienceTotal += par1; this.experience >= 1.0F; this.experience = this.experience / this.xpBarCap()) {
         this.experience = (this.experience - 1.0F) * this.xpBarCap();
         this.addExperienceLevel(1);
      }
   }

   public void addExperienceLevel(int par1) {
      this.experienceLevel += par1;
      if (this.experienceLevel < 0) {
         this.experienceLevel = 0;
         this.experience = 0.0F;
         this.experienceTotal = 0;
      }

      if (par1 > 0 && this.experienceLevel % 5 == 0 && this.field_82249_h < this.ticksExisted - 100.0F) {
         float var2 = this.experienceLevel > 30 ? 1.0F : this.experienceLevel / 30.0F;
         this.worldObj.playSoundAtEntity(this, "random.levelup", var2 * 0.75F, 1.0F);
         this.field_82249_h = this.ticksExisted;
      }
   }

   public int xpBarCap() {
      return this.experienceLevel >= 30 ? 62 + (this.experienceLevel - 30) * 7 : (this.experienceLevel >= 15 ? 17 + (this.experienceLevel - 15) * 3 : 17);
   }

   public void addExhaustion(float par1) {
      if (!this.capabilities.disableDamage && !this.worldObj.isRemote) {
         par1 *= this.getArmorExhaustionModifier();
         this.foodStats.addExhaustion(par1);
      }
   }

   public FoodStats getFoodStats() {
      return this.foodStats;
   }

   public boolean canEat(boolean par1) {
      return this.a(Potion.hunger) ? false : (par1 || this.foodStats.needFood()) && !this.capabilities.disableDamage;
   }

   public boolean shouldHeal() {
      return this.aX() > 0 && this.aX() < this.getMaxHealth();
   }

   public void setItemInUse(ItemStack par1ItemStack, int par2) {
      if (par1ItemStack != this.itemInUse) {
         this.itemInUse = par1ItemStack;
         this.itemInUseCount = par2;
         if (!this.worldObj.isRemote) {
            this.e(true);
         }
      }
   }

   public boolean canCurrentToolHarvestBlock(int par1, int par2, int par3) {
      if (this.capabilities.allowEdit) {
         return true;
      } else {
         int var4 = this.worldObj.getBlockId(par1, par2, par3);
         if (var4 > 0) {
            Block var5 = Block.blocksList[var4];
            if (var5.blockMaterial.isAlwaysHarvested()) {
               return true;
            }

            if (this.getCurrentEquippedItem() != null) {
               ItemStack var6 = this.getCurrentEquippedItem();
               if (var6.canHarvestBlock(this.worldObj, var5, par1, par2, par3) || var6.getStrVsBlock(this.worldObj, var5, par1, par2, par3) > 1.0F) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   public boolean canPlayerEdit(int par1, int par2, int par3, int par4, ItemStack par5ItemStack) {
      if (!this.capabilities.isCreativeMode && !this.onGround && !this.inWater && !this.isOnLadder() && this.ridingEntity == null && !this.I()) {
         return BTWMod.allowPlaceWhileJumping || this.worldObj.getDifficulty().allowsPlacingBlocksInAir();
      } else {
         return this.capabilities.allowEdit ? true : (par5ItemStack != null ? par5ItemStack.func_82835_x() : false);
      }
   }

   @Override
   protected int getExperiencePoints(EntityPlayer par1EntityPlayer) {
      if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
         return 0;
      } else {
         int var2 = this.experienceLevel * 7;
         return var2 > 100 ? 100 : var2;
      }
   }

   @Override
   protected boolean isPlayer() {
      return true;
   }

   @Override
   public String getEntityName() {
      return this.username;
   }

   @Override
   public boolean func_94062_bN() {
      return super.func_94062_bN();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean func_94059_bO() {
      return true;
   }

   @Override
   public boolean canPickUpLoot() {
      return false;
   }

   public void clonePlayer(EntityPlayer par1EntityPlayer, boolean playerLeavingTheEnd) {
      if (playerLeavingTheEnd) {
         this.inventory.copyInventory(par1EntityPlayer.inventory);
         this.health = par1EntityPlayer.health;
         this.foodStats = par1EntityPlayer.foodStats;
         this.experienceLevel = par1EntityPlayer.experienceLevel;
         this.experienceTotal = par1EntityPlayer.experienceTotal;
         this.experience = par1EntityPlayer.experience;
         this.setScore(par1EntityPlayer.getScore());
         this.teleportDirection = par1EntityPlayer.teleportDirection;
      } else if (this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) {
         this.inventory.copyInventory(par1EntityPlayer.inventory);
         this.experienceLevel = par1EntityPlayer.experienceLevel;
         this.experienceTotal = par1EntityPlayer.experienceTotal;
         this.experience = par1EntityPlayer.experience;
         this.setScore(par1EntityPlayer.getScore());
      }

      this.deathCount = par1EntityPlayer.deathCount;
      this.theInventoryEnderChest = par1EntityPlayer.theInventoryEnderChest;
   }

   @Override
   protected boolean canTriggerWalking() {
      return !this.capabilities.isFlying;
   }

   public void sendPlayerAbilities() {
   }

   public void setGameType(EnumGameType par1EnumGameType) {
   }

   @Override
   public String getCommandSenderName() {
      return this.username;
   }

   public StringTranslate getTranslator() {
      return StringTranslate.getInstance();
   }

   @Override
   public String translateString(String par1Str, Object... par2ArrayOfObj) {
      return this.getTranslator().translateKeyFormat(par1Str, par2ArrayOfObj);
   }

   public InventoryEnderChest getInventoryEnderChest() {
      return this.theInventoryEnderChest;
   }

   @Override
   public ItemStack getCurrentItemOrArmor(int par1) {
      return par1 == 0 ? this.inventory.getCurrentItem() : this.inventory.armorInventory[par1 - 1];
   }

   @Override
   public ItemStack getHeldItem() {
      return this.inventory.getCurrentItem();
   }

   @Override
   public void setCurrentItemOrArmor(int par1, ItemStack par2ItemStack) {
      this.inventory.armorInventory[par1] = par2ItemStack;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public boolean func_98034_c(EntityPlayer par1EntityPlayer) {
      if (!this.ai()) {
         return false;
      } else {
         ScorePlayerTeam var2 = this.getTeam();
         return var2 == null || par1EntityPlayer == null || par1EntityPlayer.getTeam() != var2 || !var2.func_98297_h();
      }
   }

   @Override
   public ItemStack[] getLastActiveItems() {
      return this.inventory.armorInventory;
   }

   @Environment(EnvType.CLIENT)
   public boolean getHideCape() {
      return this.getHideCape(1);
   }

   @Override
   public boolean func_96092_aw() {
      return !this.capabilities.isFlying;
   }

   public Scoreboard getWorldScoreboard() {
      return this.worldObj.getScoreboard();
   }

   public ScorePlayerTeam getTeam() {
      return this.getWorldScoreboard().getPlayersTeam(this.username);
   }

   @Override
   public String getTranslatedEntityName() {
      return ScorePlayerTeam.func_96667_a(this.getTeam(), this.username);
   }

   @Override
   public boolean isUsingSpecialKey() {
      return this.usingSpecialKey;
   }

   public void setUsingSpecialKey(boolean usingSpecialKey) {
      this.usingSpecialKey = usingSpecialKey;
   }

   protected void readModDataFromNBT(NBTTagCompound tag) {
      if (tag.hasKey("fcTimeOfLastSpawnAssignment")) {
         this.timeOfLastSpawnAssignment = tag.getLong("fcTimeOfLastSpawnAssignment");
      }

      if (tag.hasKey("fcTimeOfLastDimensionSwitch")) {
         this.timeOfLastDimensionSwitch = tag.getLong("fcTimeOfLastDimensionSwitch");
      }

      if (tag.hasKey("fcHCSpawnX") && tag.hasKey("fcHCSpawnY") && tag.hasKey("fcHCSpawnZ")) {
         this.hardcoreSpawnChunk = new ChunkCoordinates(tag.getInteger("fcHCSpawnX"), tag.getInteger("fcHCSpawnY"), tag.getInteger("fcHCSpawnZ"));
      }

      if (tag.hasKey("fcSpawnDimension")) {
         this.spawnDimension = tag.getInteger("fcSpawnDimension");
      }

      if (tag.hasKey("fcGloomLevel")) {
         this.setGloomLevel(tag.getInteger("fcGloomLevel"));
      }

      if (tag.hasKey("fcGloomCounter")) {
         this.inGloomCounter = tag.getInteger("fcGloomCounter");
      }

      if (tag.hasKey("fcDeathCount")) {
         this.deathCount = tag.getInteger("fcDeathCount");
      }

      if (this.deathCount > 0) {
         this.lastDeathLocationX = tag.getInteger("fcLastDeathLocationX");
         this.lastDeathLocationY = tag.getInteger("fcLastDeathLocationY");
         this.lastDeathLocationZ = tag.getInteger("fcLastDeathLocationZ");
         this.lastDeathDimension = tag.getInteger("fcLastDeathDimension");
      }
   }

   protected void writeModDataToNBT(NBTTagCompound tag) {
      tag.setLong("fcTimeOfLastSpawnAssignment", this.timeOfLastSpawnAssignment);
      tag.setLong("fcTimeOfLastDimensionSwitch", this.timeOfLastDimensionSwitch);
      if (this.hardcoreSpawnChunk != null) {
         tag.setInteger("fcHCSpawnX", this.hardcoreSpawnChunk.posX);
         tag.setInteger("fcHCSpawnY", this.hardcoreSpawnChunk.posY);
         tag.setInteger("fcHCSpawnZ", this.hardcoreSpawnChunk.posZ);
      }

      tag.setInteger("fcSpawnDimension", this.spawnDimension);
      tag.setInteger("fcGloomLevel", this.getGloomLevel());
      tag.setInteger("fcGloomCounter", this.inGloomCounter);
      tag.setInteger("fcDeathCount", this.deathCount);
      if (this.deathCount > 0) {
         tag.setInteger("fcLastDeathLocationX", this.lastDeathLocationX);
         tag.setInteger("fcLastDeathLocationY", this.lastDeathLocationY);
         tag.setInteger("fcLastDeathLocationZ", this.lastDeathLocationZ);
         tag.setInteger("fcLastDeathDimension", this.lastDeathDimension);
      }
   }

   @Override
   protected int decreaseAirSupply(int iAirSupply) {
      this.airRecoveryCountdown = 20;
      int iEnchantmentLevel = EnchantmentHelper.getRespiration(this);
      if (iEnchantmentLevel > 0 && this.isWearingSoulforgedHelm()) {
         if (this.worldObj.getWorldTime() % 100L == 0L) {
            this.worldObj.playSoundAtEntity(this, "random.breath", 0.75F + this.rand.nextFloat() * 0.5F, 0.5F + this.rand.nextFloat() * 0.025F);
         }

         return this.rand.nextInt(iEnchantmentLevel * iEnchantmentLevel + 1) > 0 ? iAirSupply : iAirSupply - 1;
      } else {
         return super.decreaseAirSupply(iAirSupply);
      }
   }

   @Override
   protected void recoverAirSupply() {
      if (this.airRecoveryCountdown > 0) {
         this.airRecoveryCountdown--;
      } else {
         int iCurrentAir = this.ak();
         if (iCurrentAir < 300) {
            iCurrentAir += 10;
            if (iCurrentAir > 300) {
               iCurrentAir = 300;
            }

            this.g(iCurrentAir + 1);
         } else {
            this.g(300);
         }
      }
   }

   @Override
   public boolean isOnLadder() {
      return this.canJump() && super.isOnLadder();
   }

   @Override
   public boolean canJump() {
      for (StatusEffect effect : this.getAllActiveStatusEffects()) {
         if (effect.preventsJumping()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean canSwim() {
      return !this.isWeighted() && this.health > 4;
   }

   @Override
   protected int getWornArmorWeight() {
      int iWeight = 0;

      for (int iSlot = 0; iSlot < this.inventory.armorInventory.length; iSlot++) {
         ItemStack tempStack = this.inventory.armorInventory[iSlot];
         if (tempStack != null) {
            iWeight += tempStack.getItem().getWeightWhenWorn();
         }
      }

      return iWeight;
   }

   public float getArmorExhaustionModifier() {
      float fModifier = 1.0F;
      int iWeight = this.getWornArmorWeight();
      if (iWeight > 0) {
         fModifier += iWeight / 44.0F;
      }

      return fModifier;
   }

   public boolean isWearingFullSuitSoulforgedArmor() {
      for (int iSlot = 0; iSlot < this.inventory.armorInventory.length; iSlot++) {
         if (this.inventory.armorInventory[iSlot] == null || !(this.inventory.armorInventory[iSlot].getItem() instanceof ArmorItemSteel)) {
            return false;
         }
      }

      return true;
   }

   public boolean isWearingSoulforgedHelm() {
      return this.inventory.armorInventory[3] != null && this.inventory.armorInventory[3].getItem().itemID == BTWItems.plateHelmet.itemID;
   }

   public boolean isWearingSoulforgedBoots() {
      return this.inventory.armorInventory[0] != null && this.inventory.armorInventory[0].getItem().itemID == BTWItems.plateBoots.itemID;
   }

   public boolean isWearingEnderSpectacles() {
      return this.inventory.armorInventory[3] != null && this.inventory.armorInventory[3].getItem().itemID == BTWItems.enderSpectacles.itemID;
   }

   @Override
   protected void playStepSound(int i, int j, int k, int iBlockID) {
      float fHealthAndExhaustionModifier = this.getHealthAndExhaustionModifier();
      if (fHealthAndExhaustionModifier < 0.26F && this.worldObj.getDifficulty() != Difficulties.RELAXED) {
         float fGruntVolume = (1.0F - fHealthAndExhaustionModifier) * 0.75F;
         this.worldObj.playSoundAtEntity(this, "random.classic_hurt", 0.5F, 1.0F + this.rand.nextFloat() * 0.1F);
      }

      if (this.isWearingSoulforgedBoots()) {
         int iBlockAboveID = this.worldObj.getBlockId(i, j + 1, k);
         Block blockAbove = Block.blocksList[iBlockAboveID];
         if (blockAbove != null && blockAbove.isGroundCover()) {
            StepSound stepSound = blockAbove.getStepSound(this.worldObj, i, j, k);
            this.worldObj.playSoundAtEntity(this, stepSound.getStepSound(), stepSound.getStepVolume() * 0.3F, stepSound.getStepPitch() * 0.75F);
         } else if (!Block.blocksList[iBlockID].blockMaterial.isLiquid()) {
            StepSound stepSound = Block.blocksList[iBlockID].getStepSound(this.worldObj, i, j, k);
            this.worldObj.playSoundAtEntity(this, stepSound.getStepSound(), stepSound.getStepVolume() * 0.3F, stepSound.getStepPitch() * 0.5F);
         }
      } else {
         super.a(i, j, k, iBlockID);
      }
   }

   @Override
   protected float getSwimmingHorizontalModifier() {
      return this.getMovementSpeedModifierFromEffects();
   }

   @Override
   protected float getLandMovementModifier() {
      return this.getMovementSpeedModifierFromEffects();
   }

   @Override
   protected float getLadderVerticalMovementModifier() {
      float modifier = this.getMovementSpeedModifierFromEffects();
      if (this.isUsingItem()) {
         modifier *= 0.5F;
      }

      return modifier;
   }

   protected float getJumpingHorizontalMovementModifier() {
      return this.getMovementSpeedModifierFromEffects();
   }

   protected void setMiningSpeedModifier(float modifier) {
      if (modifier > 1.0F) {
         modifier = 1.0F;
      }

      this.currentMiningSpeedModifier = modifier;
   }

   protected float getMiningSpeedModifier() {
      return this.currentMiningSpeedModifier;
   }

   protected float updateMiningSpeedModifier() {
      this.currentMiningSpeedModifier = this.getMiningSpeedModifierFromEffects();
      return this.currentMiningSpeedModifier;
   }

   protected float getMeleeDamageModifier() {
      return this.getAttackDamageModifierFromEffects();
   }

   public float getBowPullStrengthModifier() {
      return this.getAttackDamageModifierFromEffects();
   }

   public boolean doesStatusPreventSprinting() {
      for (StatusEffect effect : this.getAllActiveStatusEffects()) {
         if (effect.preventsSprinting()) {
            return true;
         }
      }

      return false;
   }

   protected boolean isCarryingBlastingOil() {
      return this.inventory.hasItem(BTWItems.blastingOil.itemID);
   }

   protected void detonateCarriedBlastingOil() {
      if (!this.worldObj.isRemote) {
         int iHellfireCount = InventoryUtils.countItemsInInventory(this.inventory, BTWItems.hellfireDust.itemID, -1);
         float fExplosionSize = iHellfireCount * 10.0F / 64.0F;
         fExplosionSize += InventoryUtils.countItemsInInventory(this.inventory, Item.gunpowder.itemID, -1) * 10.0F / 64.0F;
         fExplosionSize += InventoryUtils.countItemsInInventory(this.inventory, BTWItems.blastingOil.itemID, -1) * 10.0F / 64.0F;
         int iTNTCount = InventoryUtils.countItemsInInventory(this.inventory, Block.tnt.blockID, -1);
         if (iTNTCount > 0) {
            if (fExplosionSize < 4.0F) {
               fExplosionSize = 4.0F;
            }

            fExplosionSize += InventoryUtils.countItemsInInventory(this.inventory, Block.tnt.blockID, -1);
         }

         if (fExplosionSize < 1.5F) {
            fExplosionSize = 1.5F;
         } else if (fExplosionSize > 10.0F) {
            fExplosionSize = 10.0F;
         }

         InventoryUtils.clearInventoryContents(this.inventory);
         this.health = 0;
         this.onDeath(DamageSource.generic);
         this.worldObj.createExplosion(null, this.posX, this.posY, this.posZ, fExplosionSize, true);
      }
   }

   @Override
   protected void dropHead() {
      EntityItem skullEntity = this.a(new ItemStack(Item.skull.itemID, 1, 3), 0.0F);
      if (skullEntity != null) {
         ItemStack stack = skullEntity.getEntityItem();
         NBTTagCompound tag = stack.getTagCompound();
         if (tag == null) {
            tag = new NBTTagCompound();
            stack.setTagCompound(tag);
         }

         tag.setString("SkullOwner", this.username);
      }
   }

   public boolean hasValidMagneticPointForLocation() {
      return this.dataWatcher.getWatchableObjectByte(24) > 0;
   }

   public int getStongestMagneticPointForLocationI() {
      return this.dataWatcher.getWatchableObjectInt(22);
   }

   public int getStongestMagneticPointForLocationK() {
      return this.dataWatcher.getWatchableObjectInt(23);
   }

   public void setHasValidMagneticPointForLocation(boolean bValid) {
      byte bValidByte = 0;
      if (bValid) {
         bValidByte = 1;
      }

      this.dataWatcher.updateObject(24, bValidByte);
   }

   public void setStrongestMagneticPointForLocationI(int iLocationI) {
      this.dataWatcher.updateObject(22, iLocationI);
   }

   public void setStrongestMagneticPointForLocationK(int iLocationK) {
      this.dataWatcher.updateObject(23, iLocationK);
   }

   public int getGloomLevel() {
      return this.dataWatcher.getWatchableObjectByte(25);
   }

   public void setGloomLevel(int iGloomLevel) {
      this.dataWatcher.updateObject(25, (byte)iGloomLevel);
   }

   public Optional<StatusEffect> getStatusForCategory(StatusCategory category) {
      Collection<StatusEffect> effects = PlayerStatusEffects.STATUS_EFFECT_LIST.get(category).values();
      if (effects.size() > 0) {
         StatusEffect maximumEffect = null;

         for (StatusEffect effect : effects) {
            if ((maximumEffect == null || effect.getLevel() > maximumEffect.getLevel()) && effect.test(this)) {
               maximumEffect = effect;
            }
         }

         if (maximumEffect != null) {
            return Optional.of(maximumEffect);
         }
      }

      return Optional.empty();
   }

   public ArrayList<StatusEffect> getAllActiveStatusEffects() {
      ArrayList<StatusEffect> activeEffects = new ArrayList<>();

      for (StatusCategory category : PlayerStatusEffects.STATUS_EFFECT_LIST.keySet()) {
         this.getStatusForCategory(category).ifPresent(activeEffects::add);
      }

      return activeEffects;
   }

   public float getMovementSpeedModifierFromEffects() {
      float baseModifier = 1.0F;
      float multiplier = 1.0F;

      for (StatusEffect effect : this.getAllActiveStatusEffects()) {
         if (effect.affectsMovement()) {
            if (effect.areEffectsMultiplicative()) {
               multiplier *= effect.getEffectivenessMultiplier();
            } else {
               baseModifier = Math.min(baseModifier, effect.getEffectivenessMultiplier());
            }
         }
      }

      return baseModifier * multiplier;
   }

   public float getMiningSpeedModifierFromEffects() {
      float baseModifier = 1.0F;
      float multiplier = 1.0F;

      for (StatusEffect effect : this.getAllActiveStatusEffects()) {
         if (effect.affectsMiningSpeed()) {
            if (effect.areEffectsMultiplicative()) {
               multiplier *= effect.getEffectivenessMultiplier();
            } else {
               baseModifier = Math.min(baseModifier, effect.getEffectivenessMultiplier());
            }
         }
      }

      return baseModifier * multiplier;
   }

   public float getAttackDamageModifierFromEffects() {
      float baseModifier = 1.0F;
      float multiplier = 1.0F;

      for (StatusEffect effect : this.getAllActiveStatusEffects()) {
         if (effect.affectsAttackDamage()) {
            if (effect.areEffectsMultiplicative()) {
               multiplier *= effect.getEffectivenessMultiplier();
            } else {
               baseModifier = Math.min(baseModifier, effect.getEffectivenessMultiplier());
            }
         }
      }

      return baseModifier * multiplier;
   }

   public int getSpawnChunksVisualizationLocationI() {
      return this.dataWatcher.getWatchableObjectInt(29);
   }

   public int getSpawnChunksVisualizationLocationJ() {
      return this.dataWatcher.getWatchableObjectInt(30);
   }

   public int getSpawnChunksVisualizationLocationK() {
      return this.dataWatcher.getWatchableObjectInt(31);
   }

   public void setSpawnChunksVisualization(int iLocationI, int iLocationJ, int iLocationK) {
      this.dataWatcher.updateObject(29, iLocationI);
      this.dataWatcher.updateObject(30, iLocationJ);
      this.dataWatcher.updateObject(31, iLocationK);
   }

   public boolean hasRespawnCoordinates() {
      return this.spawnChunk != null;
   }

   public int getValidatedRespawnCoordinates(World newWorld, ChunkCoordinates respawnLocation) {
      int returnValue = 0;
      int oldDimension = this.dimension;
      int newDimension = this.spawnDimension;
      IChunkProvider chunkProvider = newWorld.getChunkProvider();
      ChunkCoordinates validatedCoords = null;
      chunkProvider.loadChunk(this.spawnChunk.posX - 4 >> 4, this.spawnChunk.posZ - 4 >> 4);
      chunkProvider.loadChunk(this.spawnChunk.posX + 4 >> 4, this.spawnChunk.posZ - 4 >> 4);
      chunkProvider.loadChunk(this.spawnChunk.posX - 4 >> 4, this.spawnChunk.posZ + 4 >> 4);
      chunkProvider.loadChunk(this.spawnChunk.posX + 4 >> 4, this.spawnChunk.posZ + 4 >> 4);
      if (this.spawnForced) {
         Material targetMaterial = newWorld.getBlockMaterial(this.spawnChunk.posX, this.spawnChunk.posY, this.spawnChunk.posZ);
         Material aboveTargetMaterial = newWorld.getBlockMaterial(this.spawnChunk.posX, this.spawnChunk.posY + 1, this.spawnChunk.posZ);
         if (!targetMaterial.isSolid() && !targetMaterial.isLiquid()) {
            boolean var14 = true;
         } else {
            boolean var10000 = false;
         }

         if (!aboveTargetMaterial.isSolid() && !aboveTargetMaterial.isLiquid()) {
            boolean var16 = true;
         } else {
            boolean var15 = false;
         }

         if (this.isValidRespawnLocation(newWorld, this.spawnChunk.posX, this.spawnChunk.posY, this.spawnChunk.posZ)) {
            validatedCoords = this.spawnChunk;
         } else {
            returnValue = 1;
         }
      } else {
         EntityPlayer$BeaconRespawnValidationResult validatedResult = this.validateBoundRespawnBeacon(newWorld, oldDimension, newDimension);
         returnValue = validatedResult.beaconStatus.id;
         if (returnValue == 0) {
            respawnLocation.posX = validatedResult.coords.posX;
            respawnLocation.posY = validatedResult.coords.posY;
            respawnLocation.posZ = validatedResult.coords.posZ;
         }
      }

      return returnValue;
   }

   public EntityPlayer$BeaconRespawnValidationResult validateBoundRespawnBeacon(World world, int oldDimension, int newDimension) {
      EntityPlayer$BeaconRespawnValidationResult result = new EntityPlayer$BeaconRespawnValidationResult();
      result.beaconStatus = EntityPlayer$BeaconRespawnValidationResult.BeaconStatus.MISSING;
      BeaconTileEntity beaconEnt = null;
      if (this.spawnChunk != null) {
         for (int i = 0; i < MinecraftServer.getServer().worldServers.length; i++) {
            WorldServer tempServer = MinecraftServer.getServer().worldServers[i];
            if (tempServer.provider.dimensionId == newDimension
               && tempServer.a(this.spawnChunk.posX, this.spawnChunk.posY, this.spawnChunk.posZ) == Block.beacon.blockID) {
               beaconEnt = (BeaconTileEntity)tempServer.r(this.spawnChunk.posX, this.spawnChunk.posY, this.spawnChunk.posZ);
               break;
            }
         }
      }

      if (beaconEnt != null) {
         BeaconEffect beaconEffect = beaconEnt.getActiveEffect();
         if (beaconEffect == BeaconTileEntity.SPAWN_ANCHOR_EFFECT) {
            int beaconPowerLevel = beaconEnt.l();
            if (beaconPowerLevel > 0) {
               result.beaconStatus = EntityPlayer$BeaconRespawnValidationResult.BeaconStatus.OUT_OF_RANGE;
               if (beaconPowerLevel >= 4 || oldDimension == newDimension) {
                  boolean inRange = true;
                  if (beaconPowerLevel < 3) {
                     int maxRange = 160;
                     if (beaconPowerLevel == 2) {
                        maxRange = 2000;
                     }

                     int deltaX = Math.abs((int)this.posX - this.spawnChunk.posX);
                     if (deltaX > maxRange) {
                        inRange = false;
                     } else {
                        int deltaZ = Math.abs((int)this.posZ - this.spawnChunk.posZ);
                        if (deltaZ > maxRange) {
                           inRange = false;
                        }
                     }
                  }

                  if (inRange) {
                     result.coords = this.getRandomValidSpawnAroundBeaconLocation(
                        world, this.spawnChunk.posX, this.spawnChunk.posY, this.spawnChunk.posZ, beaconPowerLevel
                     );
                     if (result.coords != null) {
                        result.beaconStatus = EntityPlayer$BeaconRespawnValidationResult.BeaconStatus.VALID;
                        beaconEnt.playerRespawnedAtBeacon = true;
                     } else {
                        result.beaconStatus = EntityPlayer$BeaconRespawnValidationResult.BeaconStatus.OBSTRUCTED;
                     }
                  }
               }
            }
         }
      }

      return result;
   }

   private boolean isValidRespawnLocation(World world, int i, int j, int k) {
      Material targetMaterial = world.getBlockMaterial(i, j, k);
      Material aboveTargetMaterial = world.getBlockMaterial(i, j + 1, k);
      boolean bValidTarget = !targetMaterial.isSolid() && !targetMaterial.isLiquid();
      boolean bValidAboveTarget = !aboveTargetMaterial.isSolid() && !aboveTargetMaterial.isLiquid();
      return bValidTarget && bValidAboveTarget;
   }

   private ChunkCoordinates getRandomValidSpawnAroundBeaconLocation(World world, int i, int j, int k, int iBeaconLevel) {
      for (int iAttempt = 0; iAttempt < 20; iAttempt++) {
         int iDistance = this.rand.nextInt(iBeaconLevel) + 1;
         int iPrimaryOffset = this.rand.nextInt(2) * iDistance;
         if (this.rand.nextInt(2) == 0) {
            iPrimaryOffset = -iPrimaryOffset;
         }

         int iSecondaryOffset = this.rand.nextInt(iBeaconLevel * 2 + 1) - iBeaconLevel;
         int iXOffset = iPrimaryOffset;
         int iYOffset = -(iDistance - 1);
         int iZOffset = iSecondaryOffset;
         if (this.rand.nextInt(2) == 0) {
            iXOffset = iSecondaryOffset;
            iZOffset = iPrimaryOffset;
         }

         int iISpawn = i + iXOffset;
         int iJSpawn = j + iYOffset;
         int iKSpawn = k + iZOffset;
         if (world.doesBlockHaveSolidTopSurface(iISpawn, iJSpawn - 1, iKSpawn) && this.isValidRespawnLocation(world, iISpawn, iJSpawn, iKSpawn)) {
            return new ChunkCoordinates(iISpawn, iJSpawn, iKSpawn);
         }
      }

      return null;
   }

   public void setSpawnChunk(ChunkCoordinates coords, boolean bForced, int iDimension) {
      if (coords != null) {
         this.spawnChunk = new ChunkCoordinates(coords);
         this.spawnForced = bForced;
         this.spawnDimension = iDimension;
      } else {
         this.spawnChunk = null;
         this.spawnForced = false;
         this.spawnDimension = 0;
      }
   }

   public void addRawChatMessage(String message) {
   }

   boolean isCurrentToolEffectiveOnBlock(Block targetBlock, int i, int j, int k) {
      float var2x = 1.0F;
      ItemStack currentItemStack = this.inventory.mainInventory[this.inventory.currentItem];
      return currentItemStack != null ? currentItemStack.getItem().isEfficientVsBlock(currentItemStack, this.worldObj, targetBlock, i, j, k) : false;
   }

   public boolean canHarvestBlock(Block par1Block, int i, int j, int k) {
      return this.inventory.canHarvestBlock(this.worldObj, par1Block, i, j, k);
   }

   public boolean addStackToCurrentHeldStackIfEmpty(ItemStack stack) {
      if (this.getCurrentEquippedItem() == null) {
         this.inventory.setInventorySlotContents(this.inventory.currentItem, stack.copy());
         return true;
      } else {
         return false;
      }
   }

   protected void updateGloomState() {
   }

   protected void onBlockedDamage(DamageSource source, int iDamage) {
      ItemStack currentItemStack = this.inventory.mainInventory[this.inventory.currentItem];
      if (currentItemStack != null) {
         currentItemStack.damageItem(1, this);
      }
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.025;
   }

   public void addExhaustionWithoutVisualFeedback(float fAmount) {
      this.addExhaustion(fAmount);
   }

   public void addHarvestBlockExhaustion(int iBlockID, int iBlockI, int iBlockJ, int iBlockK, int iBlockMetadata) {
      float fExhaustionConsumed = 0.025F;
      ItemStack currentItemStack = this.inventory.mainInventory[this.inventory.currentItem];
      if (currentItemStack != null) {
         fExhaustionConsumed = currentItemStack.getItem().getExhaustionOnUsedToHarvestBlock(iBlockID, this.worldObj, iBlockI, iBlockJ, iBlockK, iBlockMetadata);
      }

      if (fExhaustionConsumed > 0.0F) {
         this.addExhaustion(fExhaustionConsumed);
      }
   }

   protected void onZeroDamageAttack() {
   }

   protected boolean isPlayerHoldingSail() {
      ItemStack currentItemStack = this.inventory.mainInventory[this.inventory.currentItem];
      return currentItemStack != null ? currentItemStack.itemID == BTWItems.windMillBlade.itemID : false;
   }

   @Override
   public boolean appliesConstantForceWhenRidingBoat() {
      return this.isPlayerHoldingSail();
   }

   @Override
   public double movementModifierWhenRidingBoat() {
      double dModifier = 0.35;
      if (this.isPlayerHoldingSail()) {
         dModifier = 1.0;
      }

      return dModifier;
   }

   @Override
   public void unmountEntity(Entity riddenEntity) {
      double dUnmountX = this.posX;
      double dUnmountY = this.posY;
      double dUnmountZ = this.posZ;
      if (riddenEntity != null) {
         dUnmountX = riddenEntity.posX;
         dUnmountY = riddenEntity.boundingBox.minY + riddenEntity.height;
         dUnmountZ = riddenEntity.posZ;
      }

      double dLookOffsetX = -MathHelper.cos((this.rotationYaw - 90.0F) * (float) Math.PI / 180.0F);
      double dLookOffsetZ = -MathHelper.sin((this.rotationYaw - 90.0F) * (float) Math.PI / 180.0F);
      int iMaxSuitability = 0;

      for (double dTempLookOffset = 2.0; dTempLookOffset > 0.1; dTempLookOffset -= 0.5) {
         double dTempXOffset = dLookOffsetX * dTempLookOffset;
         double dTempZOffset = dLookOffsetZ * dTempLookOffset;
         int iTempSuitability = this.getDismountLocationSuitability(dTempXOffset, dTempZOffset);
         if (iTempSuitability > iMaxSuitability) {
            dUnmountX = this.posX + dTempXOffset;
            dUnmountY = this.posY + 1.0;
            dUnmountZ = this.posZ + dTempZOffset;
            iMaxSuitability = iTempSuitability;
         }
      }

      if (iMaxSuitability <= 0) {
         for (double dTempXOffset = -1.5; dTempXOffset < 2.0; dTempXOffset++) {
            for (double dTempZOffset = -1.5; dTempZOffset < 2.0; dTempZOffset++) {
               int iTempSuitability = this.getDismountLocationSuitability(dTempXOffset, dTempZOffset);
               if (iTempSuitability > iMaxSuitability) {
                  dUnmountX = this.posX + dTempXOffset;
                  dUnmountY = this.posY + 1.0;
                  dUnmountZ = this.posZ + dTempZOffset;
                  iMaxSuitability = iTempSuitability;
               }
            }
         }
      }

      this.b(dUnmountX, dUnmountY, dUnmountZ, this.rotationYaw, this.rotationPitch);
   }

   private boolean isSolidBlockToDismountOn(int i, int j, int k) {
      return this.worldObj.doesBlockHaveSolidTopSurface(i, j, k) || this.worldObj.getBlockMaterial(i, j, k) == Material.ice;
   }

   private int getDismountLocationSuitability(double dPosOffsetX, double dPosOffsetZ) {
      int i = MathHelper.floor_double(this.posX + dPosOffsetX);
      int j = MathHelper.floor_double(this.posY);
      int k = MathHelper.floor_double(this.posZ + dPosOffsetZ);
      AxisAlignedBB dTempBoundingBox = this.boundingBox.getOffsetBoundingBox(dPosOffsetX, 1.0, dPosOffsetZ);
      if (this.worldObj.getCollidingBlockBounds(dTempBoundingBox).isEmpty()) {
         if (this.isSolidBlockToDismountOn(i, j, k)) {
            return 3;
         }

         if (this.isSolidBlockToDismountOn(i, j - 1, k)) {
            return 2;
         }

         if (this.worldObj.getBlockMaterial(i, j - 1, k) == Material.water) {
            return 1;
         }
      }

      return 0;
   }

   public void addExhaustionForJump() {
      float multiplier = this.worldObj.getDifficulty().getHungerIntensiveActionCostMultiplier();
      if (this.ah()) {
         this.addExhaustion(1.0F * multiplier);
      } else {
         this.addExhaustion(0.2F * multiplier);
      }
   }

   public void setItemInUseCount(int iCount) {
      this.itemInUseCount = iCount;
   }

   @Override
   public boolean getCanBeHeadCrabbed(boolean bSquidInWater) {
      return this.R() && !this.capabilities.disableDamage && this.riddenByEntity == null && this.ridingEntity == null;
   }

   @Override
   public boolean isValidOngoingAttackTargetForSquid() {
      return this.R();
   }

   @Override
   public boolean isImmuneToHeadCrabDamage() {
      return this.isWearingSoulforgedHelm();
   }

   public boolean isLocalPlayerAndHittingBlock() {
      return false;
   }

   @Override
   public void mountEntityRemote(Entity entityToMount) {
      if (this.ridingEntity != entityToMount) {
         super.a(entityToMount);
      }
   }

   public boolean canDrink() {
      return !this.a(Potion.hunger);
   }

   public void onCantConsume() {
      if (!this.worldObj.isRemote && this.ticksSinceEmoteSound >= 10) {
         this.worldObj.playAuxSFX(2285, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
         this.ticksSinceEmoteSound = 0;
      }
   }

   @Override
   protected double getCowKickMovementMultiplier() {
      return this.worldObj.getDifficulty().getCowKickStrengthMultiplier();
   }

   public static boolean installationIntegrityTestPlayer() {
      return true;
   }

   public static String fetchUuid(String userName) {
      if (uuids.containsKey(userName)) {
         return uuids.get(userName);
      } else {
         HttpURLConnection profileConn = null;
         String id = null;

         Object json;
         try {
            URL profileUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + userName);
            profileConn = (HttpURLConnection)profileUrl.openConnection();
            profileConn.setDoInput(true);
            profileConn.setDoOutput(false);
            profileConn.connect();
            if (profileConn.getResponseCode() / 100 != 4) {
               JsonRootNode jsonx = new JdomParser().parse(new InputStreamReader(profileConn.getInputStream()));
               String name = jsonx.b(new Object[]{"name"});
               id = jsonx.b(new Object[]{"id"});
               if (userName.equals(name)) {
                  uuids.put(userName, id);
               } else {
                  id = null;
               }

               return id;
            }

            json = null;
         } catch (Exception var9) {
            var9.printStackTrace();
            return id;
         } finally {
            if (profileConn != null) {
               profileConn.disconnect();
            }
         }

         return (String)json;
      }
   }

   public float getTimerSpeedModifier() {
      return this.timerSpeedModifier;
   }

   public void setTimerSpeedModifier(float timerSpeedModifier) {
      this.timerSpeedModifier = timerSpeedModifier;
   }

   public void resetTimerSpeedModifier() {
      this.timerSpeedModifier = 1.0F;
      if (!this.worldObj.isRemote) {
         MinecraftServer.getServer().sendTimerSpeedImmediately();
      } else {
         Minecraft.getMinecraft().getTimer().resetTimerSpeed();
      }
   }

   public long getTimeOfLastSpawnAssignment() {
      return this.timeOfLastSpawnAssignment;
   }

   public void setTimeOfLastSpawnAssignment(long timeOfLastSpawnAssignment) {
      this.timeOfLastSpawnAssignment = timeOfLastSpawnAssignment;
   }
}
