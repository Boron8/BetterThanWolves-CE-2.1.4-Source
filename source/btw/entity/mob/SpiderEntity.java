package btw.entity.mob;

import btw.entity.SpiderWebEntity;
import btw.item.BTWItems;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityList;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MathHelper;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class SpiderEntity extends EntitySpider {
   private static final double SPIDER_ATTACK_RANGE = 16.0;
   private static final int TIME_BETWEEN_WEBS = 24000;
   protected int timeToNextWeb = 0;

   public SpiderEntity(World world) {
      super(world);
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound tag) {
      super.b(tag);
      tag.setInteger("timeToWeb", this.timeToNextWeb);
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound tag) {
      super.a(tag);
      if (tag.hasKey("timeToWeb")) {
         this.timeToNextWeb = tag.getInteger("timeToWeb");
      }
   }

   @Override
   public void initCreature() {
      if (this.worldObj.rand.nextInt(100) == 0) {
         SkeletonEntity jockey = (SkeletonEntity)EntityList.createEntityOfType(SkeletonEntity.class, this.worldObj);
         jockey.b(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
         jockey.initCreature();
         this.worldObj.spawnEntityInWorld(jockey);
         jockey.a(this);
      }
   }

   @Override
   public void spawnerInitCreature() {
      this.timeToNextWeb = 24000;
   }

   @Override
   protected Entity findPlayerToAttack() {
      Entity targetEntity = null;
      if ((!this.doesLightAffectAggessiveness() || this.c(1.0F) < 0.5F) && !this.isAlwaysNeutral()) {
         targetEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 16.0);
      }

      if (targetEntity == null) {
         List chickenList = this.worldObj.getEntitiesWithinAABB(ChickenEntity.class, this.boundingBox.expand(16.0, 4.0, 16.0));
         Iterator chickenIterator = chickenList.iterator();
         double dClosestChickenDistSq = 257.0;

         while (chickenIterator.hasNext()) {
            ChickenEntity chicken = (ChickenEntity)chickenIterator.next();
            if (!chicken.isLivingDead) {
               double dDeltaX = this.posX - chicken.posX;
               double dDeltaY = this.posY - chicken.posY;
               double dDeltaZ = this.posZ - chicken.posZ;
               double dDistSq = dDeltaX * dDeltaX + dDeltaY * dDeltaY + dDeltaZ * dDeltaZ;
               if (dDistSq < dClosestChickenDistSq) {
                  targetEntity = chicken;
                  dClosestChickenDistSq = dDistSq;
               }
            }
         }
      }

      return targetEntity;
   }

   @Override
   public void setRevengeTarget(EntityLiving target) {
      this.entityLivingToAttack = target;
      if (this.entityLivingToAttack != null) {
         this.revengeTimer = 200;
      } else {
         this.revengeTimer = 0;
      }
   }

   @Override
   protected boolean shouldContinueAttacking(float fDistanceToTarget) {
      return this.revengeTimer > 0
         || !this.doesLightAffectAggessiveness()
         || this.rand.nextInt(600) != 0
         || !(this.c(1.0F) > 0.5F)
         || !(this.entityToAttack instanceof EntityPlayer)
         || this.n(this.entityToAttack);
   }

   @Override
   protected void attackEntity(Entity targetEntity, float fDistanceToTarget) {
      if (fDistanceToTarget < 2.0F) {
         if (targetEntity instanceof EntityAnimal) {
            if (this.attackTime <= 0) {
               this.attackTime = 20;
               this.m(targetEntity);
            }
         } else {
            this.entityMobAttackEntity(targetEntity, fDistanceToTarget);
         }
      } else if (fDistanceToTarget < 6.0F) {
         if (this.onGround && this.rand.nextInt(10) == 0) {
            double var4 = targetEntity.posX - this.posX;
            double var6 = targetEntity.posZ - this.posZ;
            float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
            this.motionX = var4 / var8 * 0.5 * 0.8F + this.motionX * 0.2F;
            this.motionZ = var6 / var8 * 0.5 * 0.8F + this.motionZ * 0.2F;
            this.motionY = 0.4F;
         }
      } else if (fDistanceToTarget < 10.0F
         && this.hasWeb()
         && !this.isEntityInWeb(targetEntity)
         && this.rand.nextInt(10) == 0
         && !(targetEntity instanceof SpiderEntity)) {
         this.spitWeb(targetEntity);
      }
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      if (this.hasWeb()) {
         this.entityLivingDropFewItems(bKilledByPlayer, iLootingModifier);
      }

      if (this.dropsSpiderEyes() && this.rand.nextInt(8) - (iLootingModifier << 1) <= 0) {
         this.b(Item.spiderEye.itemID, 1);
      }
   }

   @Override
   public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
      return true;
   }

   @Override
   public void onLivingUpdate() {
      this.checkForLooseFood();
      this.checkForSpiderSkeletonMounting();
      if (this.timeToNextWeb > 0) {
         this.timeToNextWeb--;
      }

      super.c();
   }

   @Override
   public void checkForScrollDrop() {
      if (this.rand.nextInt(1000) == 0) {
         ItemStack itemstack = new ItemStack(BTWItems.arcaneScroll, 1, Enchantment.baneOfArthropods.effectId);
         this.a(itemstack, 0.0F);
      }
   }

   @Override
   public boolean isAffectedByMovementModifiers() {
      return false;
   }

   protected boolean dropsSpiderEyes() {
      return true;
   }

   public boolean hasWeb() {
      return this.timeToNextWeb <= 0;
   }

   public boolean isEntityInWeb(Entity targetEntity) {
      return this.worldObj.isMaterialInBB(targetEntity.boundingBox, Material.web);
   }

   private void spitWeb(Entity targetEntity) {
      if (!this.worldObj.isRemote) {
         this.worldObj.spawnEntityInWorld(EntityList.createEntityOfType(SpiderWebEntity.class, this.worldObj, this, targetEntity));
         this.timeToNextWeb = 24000;
      }
   }

   private void checkForLooseFood() {
      if (!this.worldObj.isRemote && !this.isLivingDead) {
         boolean bAte = false;

         for (EntityItem itemEntity : this.worldObj.getEntitiesWithinAABB(EntityItem.class, this.boundingBox.expand(2.5, 1.0, 2.5))) {
            if (itemEntity.delayBeforeCanPickup == 0 && itemEntity.R()) {
               ItemStack itemStack = itemEntity.getEntityItem();
               Item item = itemStack.getItem();
               if (item.itemID == Item.chickenRaw.itemID || item.itemID == BTWItems.rawMysteryMeat.itemID) {
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

   public boolean doesLightAffectAggessiveness() {
      return true;
   }

   public boolean isAlwaysNeutral() {
      return false;
   }

   public boolean doEyesGlow() {
      return true;
   }

   protected void checkForSpiderSkeletonMounting() {
      if (!this.worldObj.isRemote && this.R() && this.riddenByEntity == null) {
         for (SkeletonEntity tempSkeleton : this.worldObj.getEntitiesWithinAABB(SkeletonEntity.class, this.getSpiderJockeyCollisionBoxFromPool())) {
            if (tempSkeleton != this.entityToAttack && tempSkeleton.entityToAttack != this && tempSkeleton.ridingEntity == null) {
               tempSkeleton.a(this);
            }
         }
      }
   }

   private AxisAlignedBB getSpiderJockeyCollisionBoxFromPool() {
      double dWidthOffset = this.width / 16.0F;
      return AxisAlignedBB.getAABBPool()
         .getAABB(
            this.boundingBox.minX + dWidthOffset,
            this.boundingBox.maxY,
            this.boundingBox.minZ + dWidthOffset,
            this.boundingBox.maxX - dWidthOffset,
            this.boundingBox.maxY + 0.1F,
            this.boundingBox.maxZ - dWidthOffset
         );
   }
}
