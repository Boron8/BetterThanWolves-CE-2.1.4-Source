package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntitySpider extends EntityMob {
   public EntitySpider(World par1World) {
      super(par1World);
      this.texture = "/mob/spider.png";
      this.a(1.4F, 0.9F);
      this.moveSpeed = 0.8F;
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, new Byte((byte)0));
   }

   @Override
   public void onUpdate() {
      super.onUpdate();
      if (!this.worldObj.isRemote) {
         this.setBesideClimbableBlock(this.isCollidedHorizontally);
      }
   }

   @Override
   public int getMaxHealth() {
      return 16;
   }

   @Override
   public double getMountedYOffset() {
      return this.height * 0.75 - 0.5;
   }

   @Override
   protected Entity findPlayerToAttack() {
      float var1 = this.c(1.0F);
      if (var1 < 0.5F) {
         double var2 = 16.0;
         return this.worldObj.getClosestVulnerablePlayerToEntity(this, var2);
      } else {
         return null;
      }
   }

   @Override
   protected String getLivingSound() {
      return "mob.spider.say";
   }

   @Override
   protected String getHurtSound() {
      return "mob.spider.say";
   }

   @Override
   protected String getDeathSound() {
      return "mob.spider.death";
   }

   @Override
   protected void playStepSound(int par1, int par2, int par3, int par4) {
      this.a("mob.spider.step", 0.15F, 1.0F);
   }

   @Override
   protected void attackEntity(Entity par1Entity, float par2) {
      float var3 = this.c(1.0F);
      if (var3 > 0.5F && this.rand.nextInt(100) == 0) {
         this.entityToAttack = null;
      } else if (!(par2 > 2.0F) || !(par2 < 6.0F) || this.rand.nextInt(10) != 0) {
         super.attackEntity(par1Entity, par2);
      } else if (this.onGround) {
         double var4 = par1Entity.posX - this.posX;
         double var6 = par1Entity.posZ - this.posZ;
         float var8 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
         this.motionX = var4 / var8 * 0.5 * 0.8F + this.motionX * 0.2F;
         this.motionZ = var6 / var8 * 0.5 * 0.8F + this.motionZ * 0.2F;
         this.motionY = 0.4F;
      }
   }

   @Override
   protected int getDropItemId() {
      return Item.silk.itemID;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      super.a(par1, par2);
      if (par1 && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + par2) > 0)) {
         this.b(Item.spiderEye.itemID, 1);
      }
   }

   @Override
   public boolean isOnLadder() {
      return this.isBesideClimbableBlock();
   }

   @Override
   public void setInWeb() {
   }

   @Environment(EnvType.CLIENT)
   public float spiderScaleAmount() {
      return 1.0F;
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.ARTHROPOD;
   }

   @Override
   public boolean isPotionApplicable(PotionEffect par1PotionEffect) {
      return par1PotionEffect.getPotionID() == Potion.poison.id ? false : super.e(par1PotionEffect);
   }

   public boolean isBesideClimbableBlock() {
      return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
   }

   public void setBesideClimbableBlock(boolean par1) {
      byte var2 = this.dataWatcher.getWatchableObjectByte(16);
      if (par1) {
         var2 = (byte)(var2 | 1);
      } else {
         var2 = (byte)(var2 & -2);
      }

      this.dataWatcher.updateObject(16, var2);
   }

   @Override
   public void initCreature() {
      if (this.worldObj.rand.nextInt(100) == 0) {
         EntitySkeleton var1 = (EntitySkeleton)EntityList.createEntityOfType(EntitySkeleton.class, this.worldObj);
         var1.b(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
         var1.initCreature();
         this.worldObj.spawnEntityInWorld(var1);
         var1.a(this);
      }
   }
}
