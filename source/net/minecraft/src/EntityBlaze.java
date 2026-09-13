package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityBlaze extends EntityMob {
   private float heightOffset = 0.5F;
   private int heightOffsetUpdateTime;
   private int field_70846_g;

   public EntityBlaze(World par1World) {
      super(par1World);
      this.texture = "/mob/fire.png";
      this.isImmuneToFire = true;
      this.experienceValue = 10;
   }

   @Override
   public int getMaxHealth() {
      return 20;
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   @Override
   protected String getLivingSound() {
      return "mob.blaze.breathe";
   }

   @Override
   protected String getHurtSound() {
      return "mob.blaze.hit";
   }

   @Override
   protected String getDeathSound() {
      return "mob.blaze.death";
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
   public void onLivingUpdate() {
      if (!this.worldObj.isRemote) {
         if (this.F()) {
            this.a(DamageSource.drown, 1);
         }

         this.heightOffsetUpdateTime--;
         if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
         }

         if (this.l() != null && this.l().posY + this.l().getEyeHeight() > this.posY + this.e() + this.heightOffset) {
            this.motionY = this.motionY + (0.3F - this.motionY) * 0.3F;
         }
      }

      if (this.rand.nextInt(24) == 0) {
         this.worldObj
            .playSoundEffect(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F);
      }

      if (!this.onGround && this.motionY < 0.0) {
         this.motionY *= 0.6;
      }

      for (int var1 = 0; var1 < 2; var1++) {
         this.worldObj
            .spawnParticle(
               "largesmoke",
               this.posX + (this.rand.nextDouble() - 0.5) * this.width,
               this.posY + this.rand.nextDouble() * this.height,
               this.posZ + (this.rand.nextDouble() - 0.5) * this.width,
               0.0,
               0.0,
               0.0
            );
      }

      super.onLivingUpdate();
   }

   @Override
   protected void attackEntity(Entity par1Entity, float par2) {
      if (this.attackTime <= 0 && par2 < 2.0F && par1Entity.boundingBox.maxY > this.boundingBox.minY && par1Entity.boundingBox.minY < this.boundingBox.maxY) {
         this.attackTime = 20;
         this.m(par1Entity);
      } else if (par2 < 30.0F) {
         double var3 = par1Entity.posX - this.posX;
         double var5 = par1Entity.boundingBox.minY + par1Entity.height / 2.0F - (this.posY + this.height / 2.0F);
         double var7 = par1Entity.posZ - this.posZ;
         if (this.attackTime == 0) {
            this.field_70846_g++;
            if (this.field_70846_g == 1) {
               this.attackTime = 60;
               this.func_70844_e(true);
            } else if (this.field_70846_g <= 4) {
               this.attackTime = 6;
            } else {
               this.attackTime = 100;
               this.field_70846_g = 0;
               this.func_70844_e(false);
            }

            if (this.field_70846_g > 1) {
               float var9 = MathHelper.sqrt_float(par2) * 0.5F;
               this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

               for (int var10 = 0; var10 < 1; var10++) {
                  EntitySmallFireball var11 = (EntitySmallFireball)EntityList.createEntityOfType(
                     EntitySmallFireball.class, this.worldObj, this, var3 + this.rand.nextGaussian() * var9, var5, var7 + this.rand.nextGaussian() * var9
                  );
                  var11.posY = this.posY + this.height / 2.0F + 0.5;
                  this.worldObj.spawnEntityInWorld(var11);
               }
            }
         }

         this.rotationYaw = (float)(Math.atan2(var7, var3) * 180.0 / Math.PI) - 90.0F;
         this.hasAttacked = true;
      }
   }

   @Override
   protected void fall(float par1) {
   }

   @Override
   protected int getDropItemId() {
      return Item.blazeRod.itemID;
   }

   @Override
   public boolean isBurning() {
      return this.func_70845_n();
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      if (par1) {
         int var3 = this.rand.nextInt(2 + par2);

         for (int var4 = 0; var4 < var3; var4++) {
            this.b(Item.blazeRod.itemID, 1);
         }
      }
   }

   public boolean func_70845_n() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void func_70844_e(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 = (byte)(var2 & -2);
      }

      this.dataWatcher.updateObject(16, var2);
   }

   @Override
   protected boolean isValidLightLevel() {
      return true;
   }

   @Override
   public int getAttackStrength(Entity par1Entity) {
      return 6;
   }
}
