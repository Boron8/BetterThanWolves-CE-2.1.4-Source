package net.minecraft.src;

import btw.block.BTWBlocks;
import btw.entity.WitherSkullEntity;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
   private float[] field_82220_d = new float[2];
   private float[] field_82221_e = new float[2];
   private float[] field_82217_f = new float[2];
   private float[] field_82218_g = new float[2];
   private int[] field_82223_h = new int[2];
   private int[] field_82224_i = new int[2];
   private int field_82222_j;
   private static final IEntitySelector attackEntitySelector = new EntityWitherAttackFilter();

   public EntityWither(World par1World) {
      super(par1World);
      this.b(this.getMaxHealth());
      this.texture = "/mob/wither.png";
      this.a(0.9F, 4.0F);
      this.isImmuneToFire = true;
      this.moveSpeed = 0.6F;
      this.aC().setCanSwim(true);
      this.tasks.addTask(0, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIArrowAttack(this, this.moveSpeed, 40, 20.0F));
      this.tasks.addTask(5, new EntityAIWander(this, this.moveSpeed));
      this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(7, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 30.0F, 0, false, false, attackEntitySelector));
      this.experienceValue = 50;
   }

   @Override
   protected void entityInit() {
      super.a();
      this.dataWatcher.addObject(16, new Integer(100));
      this.dataWatcher.addObject(17, new Integer(0));
      this.dataWatcher.addObject(18, new Integer(0));
      this.dataWatcher.addObject(19, new Integer(0));
      this.dataWatcher.addObject(20, new Integer(0));
   }

   @Override
   public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound) {
      super.b(par1NBTTagCompound);
      par1NBTTagCompound.setInteger("Invul", this.func_82212_n());
   }

   @Override
   public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound) {
      super.a(par1NBTTagCompound);
      this.func_82215_s(par1NBTTagCompound.getInteger("Invul"));
      this.dataWatcher.updateObject(16, this.health);
   }

   @Environment(EnvType.CLIENT)
   @Override
   public float getShadowSize() {
      return this.height / 8.0F;
   }

   @Override
   protected String getLivingSound() {
      return "mob.wither.idle";
   }

   @Override
   protected String getHurtSound() {
      return "mob.wither.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.wither.death";
   }

   @Environment(EnvType.CLIENT)
   @Override
   public String getTexture() {
      int var1 = this.func_82212_n();
      return var1 <= 0 || var1 <= 80 && var1 / 5 % 2 == 1 ? "/mob/wither.png" : "/mob/wither_invul.png";
   }

   @Override
   public void onLivingUpdate() {
      if (!this.worldObj.isRemote) {
         this.dataWatcher.updateObject(16, this.health);
      }

      this.motionY *= 0.6F;
      if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0) {
         Entity var1 = this.worldObj.getEntityByID(this.getWatchedTargetId(0));
         if (var1 != null) {
            if (this.posY < var1.posY || !this.isArmored() && this.posY < var1.posY + 5.0) {
               if (this.motionY < 0.0) {
                  this.motionY = 0.0;
               }

               this.motionY = this.motionY + (0.5 - this.motionY) * 0.6F;
            }

            double var2 = var1.posX - this.posX;
            double var4 = var1.posZ - this.posZ;
            double var6 = var2 * var2 + var4 * var4;
            if (var6 > 9.0) {
               double var8 = MathHelper.sqrt_double(var6);
               this.motionX = this.motionX + (var2 / var8 * 0.5 - this.motionX) * 0.6F;
               this.motionZ = this.motionZ + (var4 / var8 * 0.5 - this.motionZ) * 0.6F;
            }
         }
      }

      if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05F) {
         this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * (180.0F / (float)Math.PI) - 90.0F;
      }

      super.onLivingUpdate();

      for (int var20 = 0; var20 < 2; var20++) {
         this.field_82218_g[var20] = this.field_82221_e[var20];
         this.field_82217_f[var20] = this.field_82220_d[var20];
      }

      for (int var24 = 0; var24 < 2; var24++) {
         int var21 = this.getWatchedTargetId(var24 + 1);
         Entity var3 = null;
         if (var21 > 0) {
            var3 = this.worldObj.getEntityByID(var21);
         }

         if (var3 != null) {
            double var4 = this.func_82214_u(var24 + 1);
            double var6 = this.func_82208_v(var24 + 1);
            double var8 = this.func_82213_w(var24 + 1);
            double var10 = var3.posX - var4;
            double var12 = var3.posY + var3.getEyeHeight() - var6;
            double var14 = var3.posZ - var8;
            double var16 = MathHelper.sqrt_double(var10 * var10 + var14 * var14);
            float var18 = (float)(Math.atan2(var14, var10) * 180.0 / Math.PI) - 90.0F;
            float var19 = (float)(-(Math.atan2(var12, var16) * 180.0 / Math.PI));
            this.field_82220_d[var24] = this.func_82204_b(this.field_82220_d[var24], var19, 40.0F);
            this.field_82221_e[var24] = this.func_82204_b(this.field_82221_e[var24], var18, 10.0F);
         } else {
            this.field_82221_e[var24] = this.func_82204_b(this.field_82221_e[var24], this.renderYawOffset, 10.0F);
         }
      }

      boolean var22 = this.isArmored();

      for (int var21x = 0; var21x < 3; var21x++) {
         double var23 = this.func_82214_u(var21x);
         double var5 = this.func_82208_v(var21x);
         double var7 = this.func_82213_w(var21x);
         this.worldObj
            .spawnParticle(
               "smoke", var23 + this.rand.nextGaussian() * 0.3F, var5 + this.rand.nextGaussian() * 0.3F, var7 + this.rand.nextGaussian() * 0.3F, 0.0, 0.0, 0.0
            );
         if (var22 && this.worldObj.rand.nextInt(4) == 0) {
            this.worldObj
               .spawnParticle(
                  "mobSpell",
                  var23 + this.rand.nextGaussian() * 0.3F,
                  var5 + this.rand.nextGaussian() * 0.3F,
                  var7 + this.rand.nextGaussian() * 0.3F,
                  0.7F,
                  0.7F,
                  0.5
               );
         }
      }

      if (this.func_82212_n() > 0) {
         for (int var27 = 0; var27 < 3; var27++) {
            this.worldObj
               .spawnParticle(
                  "mobSpell",
                  this.posX + this.rand.nextGaussian() * 1.0,
                  this.posY + this.rand.nextFloat() * 3.3F,
                  this.posZ + this.rand.nextGaussian() * 1.0,
                  0.7F,
                  0.7F,
                  0.9F
               );
         }
      }
   }

   @Override
   protected void updateAITasks() {
      if (this.func_82212_n() > 0) {
         int var1 = this.func_82212_n() - 1;
         if (var1 <= 0) {
            this.worldObj
               .newExplosion(this, this.posX, this.posY + this.e(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
            this.worldObj.func_82739_e(1013, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
         }

         this.func_82215_s(var1);
         if (this.ticksExisted % 10 == 0) {
            this.j(10);
         }
      } else {
         super.bo();

         for (int var1x = 1; var1x < 3; var1x++) {
            if (this.ticksExisted >= this.field_82223_h[var1x - 1]) {
               this.field_82223_h[var1x - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
               int var10001 = var1x - 1;
               int var10003 = this.field_82224_i[var1x - 1];
               this.field_82224_i[var10001] = this.field_82224_i[var1x - 1] + 1;
               if (var10003 > 15) {
                  float var2 = 10.0F;
                  float var3 = 5.0F;
                  double var4 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - var2, this.posX + var2);
                  double var6 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - var3, this.posY + var3);
                  double var8 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - var2, this.posZ + var2);
                  this.func_82209_a(var1x + 1, var4, var6, var8, true);
                  this.field_82224_i[var1x - 1] = 0;
               }

               int var12 = this.getWatchedTargetId(var1x);
               if (var12 > 0) {
                  Entity var14 = this.worldObj.getEntityByID(var12);
                  if (var14 != null && var14.isEntityAlive() && this.e(var14) <= 900.0 && this.n(var14)) {
                     this.func_82216_a(var1x + 1, (EntityLiving)var14);
                     this.field_82223_h[var1x - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
                     this.field_82224_i[var1x - 1] = 0;
                  } else {
                     this.func_82211_c(var1x, 0);
                  }
               } else {
                  List var13 = this.worldObj.selectEntitiesWithinAABB(EntityLiving.class, this.boundingBox.expand(20.0, 8.0, 20.0), attackEntitySelector);

                  for (int var16 = 0; var16 < 10 && !var13.isEmpty(); var16++) {
                     EntityLiving var5 = (EntityLiving)var13.get(this.rand.nextInt(var13.size()));
                     if (var5 != this && var5.isEntityAlive() && this.n(var5)) {
                        if (var5 instanceof EntityPlayer) {
                           if (!((EntityPlayer)var5).capabilities.disableDamage) {
                              this.func_82211_c(var1x, var5.entityId);
                           }
                        } else {
                           this.func_82211_c(var1x, var5.entityId);
                        }
                        break;
                     }

                     var13.remove(var5);
                  }
               }
            }
         }

         if (this.aJ() != null) {
            this.func_82211_c(0, this.aJ().entityId);
         } else {
            this.func_82211_c(0, 0);
         }

         if (this.field_82222_j > 0) {
            this.field_82222_j--;
            if (this.field_82222_j == 0 && this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing")) {
               int var14 = MathHelper.floor_double(this.posY);
               int var12 = MathHelper.floor_double(this.posX);
               int var15 = MathHelper.floor_double(this.posZ);
               boolean var18 = false;

               for (int var17 = -1; var17 <= 1; var17++) {
                  for (int var19 = -1; var19 <= 1; var19++) {
                     for (int var7 = 0; var7 <= 3; var7++) {
                        int var20 = var12 + var17;
                        int var9 = var14 + var7;
                        int var10 = var15 + var19;
                        int var11 = this.worldObj.getBlockId(var20, var9, var10);
                        if (var11 > 0
                           && var11 != Block.bedrock.blockID
                           && var11 != Block.endPortal.blockID
                           && var11 != Block.endPortalFrame.blockID
                           && var11 != BTWBlocks.soulforgedSteelBlock.blockID) {
                           var18 = this.worldObj.destroyBlock(var20, var9, var10, true) || var18;
                        }
                     }
                  }
               }

               if (var18) {
                  this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
               }
            }
         }

         if (this.ticksExisted % 20 == 0) {
            this.j(1);
         }
      }
   }

   public void func_82206_m() {
      this.func_82215_s(220);
      this.b(this.getMaxHealth() / 3);
   }

   @Override
   public void setInWeb() {
   }

   @Override
   public int getTotalArmorValue() {
      return 4;
   }

   private double func_82214_u(int par1) {
      if (par1 <= 0) {
         return this.posX;
      } else {
         float var2 = (this.renderYawOffset + 180 * (par1 - 1)) / 180.0F * (float) Math.PI;
         float var3 = MathHelper.cos(var2);
         return this.posX + var3 * 1.3;
      }
   }

   private double func_82208_v(int par1) {
      return par1 <= 0 ? this.posY + 3.0 : this.posY + 2.2;
   }

   private double func_82213_w(int par1) {
      if (par1 <= 0) {
         return this.posZ;
      } else {
         float var2 = (this.renderYawOffset + 180 * (par1 - 1)) / 180.0F * (float) Math.PI;
         float var3 = MathHelper.sin(var2);
         return this.posZ + var3 * 1.3;
      }
   }

   private float func_82204_b(float par1, float par2, float par3) {
      float var4 = MathHelper.wrapAngleTo180_float(par2 - par1);
      if (var4 > par3) {
         var4 = par3;
      }

      if (var4 < -par3) {
         var4 = -par3;
      }

      return par1 + var4;
   }

   private void func_82216_a(int par1, EntityLiving par2EntityLiving) {
      this.func_82209_a(
         par1,
         par2EntityLiving.posX,
         par2EntityLiving.posY + par2EntityLiving.getEyeHeight() * 0.5,
         par2EntityLiving.posZ,
         par1 == 0 && this.rand.nextFloat() < 0.001F
      );
   }

   private void func_82209_a(int par1, double par2, double par4, double par6, boolean par8) {
      this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
      double var9 = this.func_82214_u(par1);
      double var11 = this.func_82208_v(par1);
      double var13 = this.func_82213_w(par1);
      double var15 = par2 - var9;
      double var17 = par4 - var11;
      double var19 = par6 - var13;
      WitherSkullEntity var21 = (WitherSkullEntity)EntityList.createEntityOfType(WitherSkullEntity.class, this.worldObj, this, var15, var17, var19);
      if (par8) {
         var21.a(true);
      }

      var21.posY = var11;
      var21.posX = var9;
      var21.posZ = var13;
      this.worldObj.spawnEntityInWorld(var21);
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
      this.func_82216_a(0, par1EntityLiving);
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      if (this.aq()) {
         return false;
      } else if (par1DamageSource == DamageSource.drown) {
         return false;
      } else if (this.func_82212_n() > 0) {
         return false;
      } else {
         if (this.isArmored()) {
            Entity var3 = par1DamageSource.getSourceOfDamage();
            if (var3 instanceof EntityArrow) {
               return false;
            }
         }

         Entity var3 = par1DamageSource.getEntity();
         if (var3 != null
            && !(var3 instanceof EntityPlayer)
            && var3 instanceof EntityLiving
            && ((EntityLiving)var3).getCreatureAttribute() == this.getCreatureAttribute()) {
            return false;
         } else {
            if (this.field_82222_j <= 0) {
               this.field_82222_j = 20;
            }

            for (int var4 = 0; var4 < this.field_82224_i.length; var4++) {
               this.field_82224_i[var4] = this.field_82224_i[var4] + 3;
            }

            return super.attackEntityFrom(par1DamageSource, par2);
         }
      }
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      this.b(Item.netherStar.itemID, 1);
   }

   @Override
   protected void despawnEntity() {
      this.entityAge = 0;
   }

   @Environment(EnvType.CLIENT)
   @Override
   public int getBrightnessForRender(float par1) {
      return 15728880;
   }

   @Override
   public boolean canBeCollidedWith() {
      return !this.isDead;
   }

   @Override
   public int getBossHealth() {
      return this.dataWatcher.getWatchableObjectInt(16);
   }

   @Override
   protected void fall(float par1) {
   }

   @Override
   public void addPotionEffect(PotionEffect par1PotionEffect) {
   }

   @Override
   protected boolean isAIEnabled() {
      return true;
   }

   @Override
   public int getMaxHealth() {
      return 300;
   }

   @Environment(EnvType.CLIENT)
   public float func_82207_a(int par1) {
      return this.field_82221_e[par1];
   }

   @Environment(EnvType.CLIENT)
   public float func_82210_r(int par1) {
      return this.field_82220_d[par1];
   }

   public int func_82212_n() {
      return this.dataWatcher.getWatchableObjectInt(20);
   }

   public void func_82215_s(int par1) {
      this.dataWatcher.updateObject(20, par1);
   }

   public int getWatchedTargetId(int par1) {
      return this.dataWatcher.getWatchableObjectInt(17 + par1);
   }

   public void func_82211_c(int par1, int par2) {
      this.dataWatcher.updateObject(17 + par1, par2);
   }

   public boolean isArmored() {
      return this.getBossHealth() <= this.getMaxHealth() / 2;
   }

   @Override
   public EnumCreatureAttribute getCreatureAttribute() {
      return EnumCreatureAttribute.UNDEAD;
   }

   @Override
   public void mountEntity(Entity par1Entity) {
      this.ridingEntity = null;
   }
}
