package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.block.blocks.BedBlockBase;
import btw.block.tileentity.beacon.CompanionBeaconEffect;
import btw.item.BTWItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class FoodStats {
   private int foodLevel = 60;
   private float foodSaturationLevel = 0.0F;
   private float foodExhaustionLevel;
   private int foodTimer = 0;
   private int prevFoodLevel = 60;

   public void addStats(ItemFood par1ItemFood) {
      this.addStats(par1ItemFood.getHungerRestored(), par1ItemFood.getSaturationModifier());
   }

   public void readNBT(NBTTagCompound par1NBTTagCompound) {
      if (par1NBTTagCompound.hasKey("foodLevel")) {
         this.foodLevel = par1NBTTagCompound.getInteger("foodLevel");
         this.foodTimer = par1NBTTagCompound.getInteger("foodTickTimer");
         this.foodSaturationLevel = par1NBTTagCompound.getFloat("foodSaturationLevel");
         this.foodExhaustionLevel = par1NBTTagCompound.getFloat("foodExhaustionLevel");
         if (!par1NBTTagCompound.hasKey("fcFoodLevelAdjusted")) {
            this.foodLevel *= 3;
            this.foodSaturationLevel = 0.0F;
         }

         if (this.foodLevel > 60 || this.foodLevel < 0) {
            this.foodLevel = 60;
         }

         if (this.foodSaturationLevel > 20.0F || this.foodSaturationLevel < 0.0F) {
            this.foodSaturationLevel = 20.0F;
         }
      }
   }

   public void writeNBT(NBTTagCompound par1NBTTagCompound) {
      par1NBTTagCompound.setInteger("foodLevel", this.foodLevel);
      par1NBTTagCompound.setInteger("foodTickTimer", this.foodTimer);
      par1NBTTagCompound.setFloat("foodSaturationLevel", this.foodSaturationLevel);
      par1NBTTagCompound.setFloat("foodExhaustionLevel", this.foodExhaustionLevel);
      par1NBTTagCompound.setBoolean("fcFoodLevelAdjusted", true);
   }

   public int getFoodLevel() {
      return this.foodLevel;
   }

   @Environment(EnvType.CLIENT)
   public int getPrevFoodLevel() {
      return this.prevFoodLevel;
   }

   public boolean needFood() {
      return this.foodLevel < 60;
   }

   public void addExhaustion(float par1) {
      this.foodExhaustionLevel = Math.min(this.foodExhaustionLevel + par1, 40.0F);
   }

   public float getSaturationLevel() {
      return this.foodSaturationLevel;
   }

   public void setFoodLevel(int par1) {
      this.foodLevel = par1;
   }

   public void setFoodSaturationLevel(float par1) {
      this.foodSaturationLevel = par1;
   }

   public void addStats(int iFoodGain, float fFatMultiplier) {
      int iPreviousFoodLevel = this.foodLevel;
      this.foodLevel = Math.min(iFoodGain + this.foodLevel, 60);
      int iExcessFood = iFoodGain - (this.foodLevel - iPreviousFoodLevel);
      if (iExcessFood > 0) {
         this.foodSaturationLevel = Math.min(this.foodSaturationLevel + iExcessFood * fFatMultiplier / 3.0F, 20.0F);
      }
   }

   public void onUpdate(EntityPlayer player) {
      int difficulty = player.worldObj.difficultySetting;
      this.prevFoodLevel = this.foodLevel;
      if (difficulty <= 0) {
         this.foodExhaustionLevel = 0.0F;
      } else {
         while (this.foodLevel > 0 && this.foodExhaustionLevel >= 1.33F && !this.shouldBurnFatBeforeHunger()) {
            this.foodExhaustionLevel--;
            this.foodLevel = Math.max(this.foodLevel - 1, 0);
         }

         while (this.foodExhaustionLevel >= 0.5F && this.shouldBurnFatBeforeHunger()) {
            this.foodExhaustionLevel -= 0.5F;
            this.foodSaturationLevel = Math.max(this.foodSaturationLevel - 0.125F, 0.0F);
         }
      }

      if (CompanionBeaconEffect.companionStrength > 3 && player.worldObj.rand.nextInt(5000) == 0) {
         this.attemptToShit(player);
      }

      int healingHungerThreshold = 24;
      if (this.foodLevel > healingHungerThreshold && player.shouldHeal()) {
         this.foodTimer++;
         int foodTimerMax = (int)(400.0F * player.worldObj.getDifficulty().getHealthRegenDelayMultiplier());
         if (this.foodTimer >= foodTimerMax) {
            if (player.sleeping) {
               Block block = Block.blocksList[player.worldObj.getBlockId((int)player.posX - 1, (int)player.posY, (int)player.posZ)];
               if (block instanceof BedBlockBase) {
                  BedBlockBase bed = (BedBlockBase)block;
                  if (!bed.blocksHealing()) {
                     player.j(1);
                  }
               }
            } else {
               player.j(1);
            }

            this.foodTimer = 0;
         }
      } else if (this.foodLevel <= 0 && this.foodSaturationLevel <= 0.01F) {
         this.foodTimer++;
         if (this.foodTimer >= 80) {
            if (difficulty > 0) {
               player.attackEntityFrom(DamageSource.starve, 1);
            }

            this.foodTimer = 0;
         }

         this.foodExhaustionLevel = 0.0F;
      } else {
         this.foodTimer = 0;
      }
   }

   private boolean shouldBurnFatBeforeHunger() {
      return this.foodSaturationLevel > (this.foodLevel + 5) / 6 * 2.0F;
   }

   public boolean attemptToShit(EntityPlayer player) {
      float poopVectorX = MathHelper.sin(player.rotationYawHead / 180.0F * (float) Math.PI);
      float poopVectorZ = -MathHelper.cos(player.rotationYawHead / 180.0F * (float) Math.PI);
      double shitPosX = player.posX + poopVectorX;
      double shitPosY = player.posY + 0.25;
      double shitPosZ = player.posZ + poopVectorZ;
      int shitPosI = MathHelper.floor_double(shitPosX);
      int shitPosJ = MathHelper.floor_double(shitPosY);
      int shitPosK = MathHelper.floor_double(shitPosZ);
      if (!this.isPathToBlockOpenToShitting(shitPosI, shitPosJ, shitPosK, player)) {
         return false;
      } else {
         EntityItem entityitem = (EntityItem)EntityList.createEntityOfType(
            EntityItem.class, player.worldObj, shitPosX, shitPosY, shitPosZ, new ItemStack(BTWItems.dung)
         );
         float velocityFactor = 0.05F;
         entityitem.motionX = poopVectorX * 10.0F * velocityFactor;
         entityitem.motionZ = poopVectorZ * 10.0F * velocityFactor;
         entityitem.motionY = (float)player.worldObj.rand.nextGaussian() * velocityFactor + 0.2F;
         entityitem.delayBeforeCanPickup = 10;
         player.worldObj.spawnEntityInWorld(entityitem);
         player.worldObj.playSoundAtEntity(player, "random.explode", 0.2F, 1.25F);
         player.worldObj.playSoundAtEntity(player, "random.classic_hurt", player.ba(), (player.rand.nextFloat() - player.rand.nextFloat()) * 0.2F + 1.0F);

         for (int counter = 0; counter < 5; counter++) {
            double smokeX = player.posX + poopVectorX * 0.5F + player.worldObj.rand.nextDouble() * 0.25;
            double smokeY = player.posY + player.worldObj.rand.nextDouble() * 0.5 + 0.25;
            double smokeZ = player.posZ + poopVectorZ * 0.5F + player.worldObj.rand.nextDouble() * 0.25;
            player.worldObj.spawnParticle("smoke", smokeX, smokeY, smokeZ, 0.0, 0.0, 0.0);
         }

         return true;
      }
   }

   private boolean isPathToBlockOpenToShitting(int i, int j, int k, EntityPlayer player) {
      if (!this.isBlockOpenToShitting(i, j, k, player)) {
         return false;
      } else {
         int wolfI = MathHelper.floor_double(player.posX);
         int wolfK = MathHelper.floor_double(player.posZ);
         int deltaI = i - wolfI;
         int deltaK = k - wolfK;
         return deltaI == 0 || deltaK == 0 || this.isBlockOpenToShitting(wolfI, j, k, player) || this.isBlockOpenToShitting(i, j, wolfK, player);
      }
   }

   private boolean isBlockOpenToShitting(int i, int j, int k, EntityPlayer player) {
      Block block = Block.blocksList[player.worldObj.getBlockId(i, j, k)];
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
}
