package btw.entity.mob;

import btw.block.tileentity.beacon.BeaconTileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Block;
import net.minecraft.src.DamageSource;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.Item;
import net.minecraft.src.Potion;
import net.minecraft.src.PotionEffect;
import net.minecraft.src.World;

public class JungleSpiderEntity extends SpiderEntity {
   public JungleSpiderEntity(World world) {
      super(world);
      this.texture = "/btwmodtex/fcSpiderJungle.png";
      this.a(0.7F, 0.5F);
   }

   @Override
   public int getMaxHealth() {
      return 10;
   }

   @Override
   public boolean attackEntityAsMob(Entity targetEntity) {
      if (super.m(targetEntity)) {
         if (targetEntity instanceof EntityLiving) {
            byte iPoisonDurationInSeconds = 0;
            if (this.worldObj.difficultySetting > 1) {
               if (this.worldObj.difficultySetting == 2) {
                  iPoisonDurationInSeconds = 3;
               } else if (this.worldObj.difficultySetting == 3) {
                  iPoisonDurationInSeconds = 7;
               }
            } else {
               iPoisonDurationInSeconds = 1;
            }

            if (iPoisonDurationInSeconds > 0) {
               ((EntityLiving)targetEntity).addPotionEffect(new PotionEffect(Potion.poison.id, iPoisonDurationInSeconds * 20, 0));
               int hungerDuration;
               if (this.worldObj.getDifficulty().shouldReduceJungleSpiderFoodPoisoning()) {
                  hungerDuration = 10;
               } else {
                  hungerDuration = 30;
               }

               ((EntityLiving)targetEntity).addPotionEffect(new PotionEffect(Potion.hunger.id, hungerDuration * 20, 0));
            }
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public void initCreature() {
   }

   @Override
   public boolean doesLightAffectAggessiveness() {
      return false;
   }

   @Override
   public boolean isAlwaysNeutral() {
      return !this.worldObj.getDifficulty().areJungleSpidersHostile();
   }

   @Override
   protected boolean isValidLightLevel() {
      return true;
   }

   @Override
   public boolean getCanSpawnHere() {
      if (this.worldObj.getAmbientBeaconEffectAtLocation(BeaconTileEntity.JUNGLE_SPIDER_REPELLENT.EFFECT_NAME, (int)this.posX, (int)this.posY, (int)this.posZ)
         > 0) {
         return false;
      } else {
         return (int)this.posY >= this.worldObj.provider.getAverageGroundLevel() - 5 ? super.bv() : false;
      }
   }

   @Override
   public String getEntityName() {
      return "Jungle Spider";
   }

   @Override
   protected boolean canSpawnOnBlock(int x, int y, int z) {
      Block block = Block.blocksList[this.worldObj.getBlockId(x, y, z)];
      return block != null && block.isLeafBlock(this.worldObj, x, y, z);
   }

   @Override
   public float getBlockPathWeight(int i, int j, int k) {
      return this.worldObj.getBlockId(i, j - 1, k) == Block.leaves.blockID ? 10.0F : super.a(i, j, k);
   }

   @Override
   protected float getSoundPitch() {
      return (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 0.7F;
   }

   @Override
   protected float getSoundVolume() {
      return 0.25F;
   }

   @Override
   public int getTalkInterval() {
      return 80 + this.rand.nextInt(240);
   }

   @Override
   public boolean doEyesGlow() {
      return false;
   }

   @Override
   public boolean attackEntityFrom(DamageSource damageSource, int iDamageAmount) {
      return damageSource == DamageSource.fall ? false : super.a(damageSource, iDamageAmount);
   }

   @Override
   protected void dropFewItems(boolean bKilledByPlayer, int iLootingModifier) {
      super.dropFewItems(bKilledByPlayer, iLootingModifier);
      if (this.rand.nextInt(16) - (iLootingModifier << 1) <= 0) {
         this.b(Item.fermentedSpiderEye.itemID, 1);
      }
   }

   @Override
   protected boolean dropsSpiderEyes() {
      return false;
   }

   @Override
   protected void checkForSpiderSkeletonMounting() {
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float spiderScaleAmount() {
      return 0.7F;
   }
}
