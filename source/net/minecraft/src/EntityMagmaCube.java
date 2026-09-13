package net.minecraft.src;

import btw.entity.mob.SlimeEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityMagmaCube extends SlimeEntity {
   public EntityMagmaCube(World par1World) {
      super(par1World);
      this.texture = "/mob/lava.png";
      this.isImmuneToFire = true;
      this.landMovementFactor = 0.5F;
   }

   @Override
   public boolean getCanSpawnHere() {
      return this.worldObj.difficultySetting > 0
         && this.worldObj.checkNoEntityCollision(this.boundingBox)
         && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty()
         && !this.worldObj.isAnyLiquid(this.boundingBox);
   }

   @Override
   public int getTotalArmorValue() {
      return this.p() * 3;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   @Override
   public float getBrightness(float par1) {
      return 1.0F;
   }

   @Override
   protected String getSlimeParticle() {
      return "flame";
   }

   @Override
   protected EntitySlime createInstance() {
      return (EntitySlime)EntityList.createEntityOfType(EntityMagmaCube.class, this.worldObj);
   }

   @Override
   protected int getDropItemId() {
      return Item.magmaCream.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.getDropItemId();
      if (var3 > 0 && this.p() > 1) {
         int var4 = this.rand.nextInt(4) - 2;
         if (par2 > 0) {
            var4 += this.rand.nextInt(par2 + 1);
         }

         for (int var5 = 0; var5 < var4; var5++) {
            this.b(var3, 1);
         }
      }
   }

   @Override
   public boolean isBurning() {
      return false;
   }

   @Override
   protected int getJumpDelay() {
      return super.j() * 4;
   }

   @Override
   protected void func_70808_l() {
      this.field_70813_a *= 0.9F;
   }

   @Override
   public void jump() {
      this.motionY = 0.42F + this.p() * 0.1F;
      this.isAirBorne = true;
   }

   @Override
   protected void fall(float par1) {
   }

   @Override
   protected boolean canDamagePlayer() {
      return true;
   }

   @Override
   protected int getAttackStrength() {
      return super.m() + 2;
   }

   @Override
   protected String getHurtSound() {
      return "mob.slime." + (this.p() > 1 ? "big" : "small");
   }

   @Override
   protected String getDeathSound() {
      return "mob.slime." + (this.p() > 1 ? "big" : "small");
   }

   @Override
   protected String getJumpSound() {
      return this.p() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
   }

   @Override
   public boolean handleLavaMovement() {
      return false;
   }

   @Override
   protected boolean makesSoundOnLand() {
      return true;
   }
}
