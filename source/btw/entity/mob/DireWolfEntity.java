package btw.entity.mob;

import btw.entity.mob.behavior.DireWolfHowlBehavior;
import btw.entity.mob.behavior.SimpleWanderBehavior;
import btw.entity.mob.behavior.ZombieBreakBarricadeBehavior;
import btw.entity.mob.villager.VillagerEntity;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAIAttackOnCollide;
import net.minecraft.src.EntityAIFleeSun;
import net.minecraft.src.EntityAIHurtByTarget;
import net.minecraft.src.EntityAILeapAtTarget;
import net.minecraft.src.EntityAILookIdle;
import net.minecraft.src.EntityAINearestAttackableTarget;
import net.minecraft.src.EntityAIRestrictSun;
import net.minecraft.src.EntityAISwimming;
import net.minecraft.src.EntityAIWatchClosest;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumCreatureAttribute;
import net.minecraft.src.IAnimals;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class DireWolfEntity extends EntityCreature implements IAnimals {
   private static final float MOVE_SPEED_AGGRESSIVE = 0.45F;
   private static final float MOVE_SPEED_PASSIVE = 0.3F;
   public int howlingCountdown = 0;
   public int heardHowlCountdown = 0;

   public DireWolfEntity(World world) {
      super(world);
      this.texture = "/btwmodtex/fcWolfDire.png";
      this.a(0.9F, 1.2F);
      this.moveSpeed = 0.45F;
      this.aC().setBreakDoors(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(1, new ZombieBreakBarricadeBehavior(this));
      this.tasks.addTask(1, new EntityAILeapAtTarget(this, 0.4F));
      this.tasks.addTask(2, new EntityAIAttackOnCollide(this, this.moveSpeed, true));
      this.tasks.addTask(3, new EntityAIRestrictSun(this));
      this.tasks.addTask(4, new EntityAIFleeSun(this, this.moveSpeed));
      this.tasks.addTask(5, new DireWolfHowlBehavior(this));
      this.tasks.addTask(7, new SimpleWanderBehavior(this, 0.3F));
      this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(9, new EntityAILookIdle(this));
      this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true));
      this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 32.0F, 0, false));
      this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, VillagerEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, ChickenEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, CowEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, PigEntity.class, 16.0F, 0, false));
      this.targetTasks.addTask(4, new EntityAINearestAttackableTarget(this, SheepEntity.class, 16.0F, 0, false));
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public int getMaxHealth() {
      return 40;
   }

   @Override
   protected void entityInit() {
      super.entityInit();
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.wolf.step", 0.15F, 1.0F);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.writeEntityToNBT(par1NBTTagCompound);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.readEntityFromNBT(par1NBTTagCompound);
   }

   @Override
   protected boolean canDespawn() {
      return false;
   }

   @Override
   protected String getLivingSound() {
      return "mob.wolf.growl";
   }

   @Override
   protected String getHurtSound() {
      return "mob.wolf.growl";
   }

   @Override
   protected String getDeathSound() {
      return "mob.wolf.death";
   }

   @Override
   protected float getSoundVolume() {
      return 3.0F;
   }

   @Override
   protected float getSoundPitch() {
      return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.05F + 0.55F;
   }

   @Override
   protected int getDropItemId() {
      return !this.worldObj.isRemote ? Item.rottenFlesh.itemID : -1;
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingLevel) {
      super.a(bKilledByPlayer, iLootingLevel);
      this.b(BTWItems.rawLiver.itemID, 1);
   }

   @Override
   public void onLivingUpdate() {
      this.checkForLooseFood();
      if (this.worldObj.isRemote) {
         this.howlingCountdown = Math.max(0, this.howlingCountdown - 1);
      } else {
         this.heardHowlCountdown = Math.max(0, this.heardHowlCountdown - 1);
         if (this.worldObj.isDaytime()) {
            float fBrightness = this.c(1.0F);
            if (fBrightness > 0.5F
               && this.rand.nextFloat() * 30.0F < (fBrightness - 0.4F) * 2.0F
               && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ))) {
               this.d(8);
            }
         }
      }

      super.c();
   }

   @Override
   public void onUpdate() {
      super.l_();
   }

   @Override
   public void knockBack(Entity entity, int iDamageDone, double dMotionX, double dMotionY) {
   }

   @Override
   public float getEyeHeight() {
      return this.height * 0.8F;
   }

   @Override
   public int getMeleeAttackStrength(Entity target) {
      return 6;
   }

   @Override
   public float getBlockPathWeight(int par1, int par2, int par3) {
      return 0.5F - this.worldObj.getLightBrightness(par1, par2, par3);
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   public String getEntityName() {
      return "The Beast";
   }

   public float getTailRotation() {
      return 1.5393804F;
   }

   private void checkForLooseFood() {
      if (!this.worldObj.isRemote && !this.isLivingDead) {
         boolean bAte = false;

         for (EntityItem itemEntity : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(2.5, 1.0, 2.5))) {
            if (itemEntity.delayBeforeCanPickup == 0 && !itemEntity.isDead) {
               ItemStack itemStack = itemEntity.getEntityItem();
               Item item = itemStack.getItem();
               if (item.doZombiesConsume() || item.itemID == Item.chickenRaw.itemID) {
                  itemEntity.w();
                  bAte = true;
               }
            }
         }

         if (bAte) {
            this.worldObj.playAuxSFX(2226, MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 0);
         }
      }
   }

   public float getHeadRotationPointOffset(float par1) {
      if (this.howlingCountdown > 0) {
         float fTiltFraction = 1.0F;
         if (this.howlingCountdown < 5) {
            fTiltFraction = this.howlingCountdown / 5.0F;
         } else if (this.howlingCountdown > 70) {
            fTiltFraction = (81 - this.howlingCountdown) / 10.0F;
         }

         return fTiltFraction * -0.5F;
      } else {
         return 0.0F;
      }
   }

   public float getHeadRotation(float par1) {
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

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte bUpdateType) {
      if (bUpdateType == 10) {
         this.howlingCountdown = 80;
      } else {
         super.a(bUpdateType);
      }
   }
}
