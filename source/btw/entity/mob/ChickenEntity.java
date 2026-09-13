package btw.entity.mob;

import btw.block.blocks.BlockDispenserBlock;
import btw.block.blocks.GroundCoverBlock;
import btw.block.tileentity.dispenser.BlockDispenserTileEntity;
import btw.entity.mob.behavior.GrazeBehavior;
import btw.entity.mob.behavior.MoveToGrazeBehavior;
import btw.entity.mob.behavior.MoveToLooseFoodBehavior;
import btw.entity.mob.behavior.MultiTemptBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.inventory.util.InventoryUtils;
import btw.item.BTWItems;
import btw.world.util.BlockPos;
import btw.world.util.WorldUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.EntityAIFollowParent;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAIMate;
import net.minecraft.src.EntityAIPanic;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityAgeable;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class ChickenEntity extends EntityChicken {
   protected long timeToLayEgg = 0L;

   public ChickenEntity(World world) {
      super(world);
      this.aC().setAvoidsWater(true);
      this.tasks.removeAllTasks();
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new EntityAIPanic(this, 0.38F));
      this.tasks.addTask(2, new EntityAIMate(this, 0.25F));
      this.tasks.addTask(3, new MultiTemptBehavior(this, 0.25F));
      this.tasks.addTask(4, new GrazeBehavior(this));
      this.tasks.addTask(5, new MoveToLooseFoodBehavior(this, 0.25F));
      this.tasks.addTask(6, new MoveToGrazeBehavior(this, 0.25F));
      this.tasks.addTask(7, new EntityAIFollowParent(this, 0.28F));
      this.tasks.addTask(8, new SimpleWanderBehavior(this, 0.25F));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
      this.tasks.addTask(10, new EntityAILookIdle(this));
      this.renderDistanceWeight = 2.0;
   }

   @Override
   public void onLivingUpdate() {
      this.timeUntilNextEgg = 6000;
      super.onLivingUpdate();
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.b(tag);
      tag.setLong("fcTimeToLayEgg", this.timeToLayEgg);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.a(tag);
      if (tag.hasKey("fcTimeToLayEgg")) {
         this.timeToLayEgg = tag.getLong("fcTimeToLayEgg");
      } else {
         this.timeToLayEgg = 0L;
      }
   }

   @Override
   protected void playStepSound(int iBlockI, int iBlockJ, int iBlockK, int iBlockID) {
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (!this.isStarving()) {
         int iNumDrops = this.rand.nextInt(3) + this.rand.nextInt(1 + iLootingModifier) + 1;
         if (this.isFamished()) {
            iNumDrops /= 2;
         }

         for (int iTempCount = 0; iTempCount < iNumDrops; iTempCount++) {
            this.b(Item.feather.itemID, 1);
         }

         if (this.isFullyFed() && !this.hasHeadCrabbedSquid()) {
            if (this.ae()) {
               if (this.worldObj.getDifficulty().shouldBurningMobsDropCookedMeat()) {
                  this.b(Item.chickenCooked.itemID, 1);
               } else {
                  this.b(BTWItems.burnedMeat.itemID, 1);
               }
            } else {
               this.b(Item.chickenRaw.itemID, 1);
            }
         }
      }
   }

   public ChickenEntity spawnBabyAnimal(EntityAgeable parent) {
      return (ChickenEntity)EntityList.createEntityOfType(ChickenEntity.class, this.worldObj);
   }

   @Override
   public boolean isReadyToEatBreedingItem() {
      return this.isFullyFed() && this.b() == 0 && this.timeToLayEgg == 0L;
   }

   @Override
   public void onEatBreedingItem() {
      long lCurrentTime = WorldUtils.getOverworldTimeServerOnly();
      this.timeToLayEgg = ((lCurrentTime + 12000L) / 24000L + 1L) * 24000L;
      this.timeToLayEgg = this.timeToLayEgg + (-1450 + this.rand.nextInt(600));
      this.worldObj.playSoundAtEntity(this, this.bd(), this.ba(), this.rand.nextFloat() * 0.2F + 1.5F);
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
      this.worldObj.playAuxSFX(2243, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
      int iFeatherCount = this.rand.nextInt(3) + 3;

      for (int iTempCount = 0; iTempCount < iFeatherCount; iTempCount++) {
         ItemStack itemStack = new ItemStack(Item.feather.itemID, 1, 0);
         double dFeatherX = this.posX + (this.worldObj.rand.nextDouble() - 0.5) * 2.0;
         double dFeatherY = this.posY + 0.5;
         double dFeatherZ = this.posZ + (this.worldObj.rand.nextDouble() - 0.5) * 2.0;
         EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(EntityItem.class, this.worldObj, dFeatherX, dFeatherY, dFeatherZ, itemStack);
         entityitem.motionX = (this.worldObj.rand.nextDouble() - 0.5) * 0.5;
         entityitem.motionY = 0.2 + this.worldObj.rand.nextDouble() * 0.3;
         entityitem.motionZ = (this.worldObj.rand.nextDouble() - 0.5) * 0.5;
         entityitem.delayBeforeCanPickup = 10;
         this.worldObj.spawnEntityInWorld(entityitem);
      }

      this.attemptToPossessNearbyCreatureOnDeath();
      this.w();
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 1.3F;
   }

   @Override
   public boolean isBreedingItem(ItemStack stack) {
      return stack.itemID == BTWItems.chickenFeed.itemID;
   }

   @Override
   protected String getLivingSound() {
      return !this.isStarving() ? "mob.chicken.say" : "mob.chicken.hurt";
   }

   @Override
   public boolean isSubjectToHunger() {
      return true;
   }

   @Override
   public int getFoodValueMultiplier() {
      return 1;
   }

   @Override
   public boolean shouldNotifyBlockOnGraze() {
      return this.rand.nextInt(8) == 0;
   }

   @Override
   public void playGrazeFX(int i, int j, int k, int iBlockID) {
   }

   @Override
   public BlockPos getGrazeBlockForPos() {
      BlockPos pos = super.getGrazeBlockForPos();
      return pos != null && GroundCoverBlock.isGroundCoverRestingOnBlock(this.worldObj, pos.x, pos.y, pos.z) ? null : pos;
   }

   @Override
   public int getGrazeDuration() {
      return 20;
   }

   @Override
   public boolean shouldStayInPlaceToGraze() {
      return this.rand.nextInt(10) != 0 ? super.shouldStayInPlaceToGraze() : false;
   }

   @Override
   public boolean isHungryEnoughToForceMoveToGraze() {
      return this.h_() || !this.isFullyFed() || this.hungerCountdown + this.getGrazeHungerGain() * 3 / 4 <= 24000;
   }

   @Override
   public int getItemFoodValue(ItemStack stack) {
      return stack.getItem().getChickenFoodValue(stack.getItemDamage()) * this.getFoodValueMultiplier();
   }

   @Override
   public void onBecomeFamished() {
      super.onBecomeFamished();
      this.timeToLayEgg = 0L;
   }

   @Override
   public void updateHungerState() {
      if (!this.h_()
         && this.isFullyFed()
         && this.timeToLayEgg > 0L
         && this.validateTimeToLayEgg()
         && WorldUtils.getOverworldTimeServerOnly() > this.timeToLayEgg) {
         this.a("mob.slime.attack", 1.0F, this.aY());
         this.a(this.bd(), this.ba(), (this.aY() + 0.25F) * (this.aY() + 0.25F));
         this.b(Item.egg.itemID, 1);
         this.timeToLayEgg = 0L;
      }

      super.updateHungerState();
   }

   private boolean validateTimeToLayEgg() {
      long lCurrentTime = WorldUtils.getOverworldTimeServerOnly();
      long lDeltaTime = this.timeToLayEgg - lCurrentTime;
      if (lDeltaTime > 48000L) {
         this.timeToLayEgg = 0L;
         return false;
      } else {
         return true;
      }
   }

   @Override
   public float getGrazeHeadRotationMagnitudeDivisor() {
      return 3.0F;
   }

   @Override
   public float getGrazeHeadRotationRateMultiplier() {
      return 14.35F;
   }

   @Override
   public boolean onBlockDispenserConsume(BlockDispenserBlock blockDispenser, BlockDispenserTileEntity tileEntity) {
      this.worldObj.playAuxSFX(2240, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, 0);
      this.w();
      InventoryUtils.addSingleItemToInventory(tileEntity, Item.egg.itemID, 0);
      return true;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      int iHungerLevel = this.getHungerLevel();
      if (iHungerLevel == 1) {
         return "/btwmodtex/fcChickenFamished.png";
      } else {
         return iHungerLevel == 2 ? "/btwmodtex/fcChickenStarving.png" : super.N();
      }
   }
}
