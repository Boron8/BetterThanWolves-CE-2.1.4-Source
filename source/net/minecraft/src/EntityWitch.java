package net.minecraft.src;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityWitch extends EntityMob implements IRangedAttackMob {
   private static final int[] witchDrops = new int[]{
      Item.lightStoneDust.itemID,
      Item.sugar.itemID,
      Item.redstone.itemID,
      Item.spiderEye.itemID,
      Item.glassBottle.itemID,
      Item.gunpowder.itemID,
      Item.stick.itemID,
      Item.stick.itemID
   };
   private int witchAttackTimer = 0;

   public EntityWitch(World par1World) {
      super(par1World);
      this.texture = "/mob/villager/witch.png";
      this.moveSpeed = 0.25F;
      this.tasks.addTask(1, new EntityAISwimming(this));
      this.tasks.addTask(2, new EntityAIArrowAttack(this, this.moveSpeed, 60, 10.0F));
      this.tasks.addTask(2, new EntityAIWander(this, this.moveSpeed));
      this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
      this.tasks.addTask(3, new EntityAILookIdle(this));
      this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
      this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 16.0F, 0, true));
   }

   @Override
   protected void entityInit() {
      super.a();
      this.u().addObject(21, (byte)0);
   }

   @Override
   protected String getLivingSound() {
      return "mob.witch.idle";
   }

   @Override
   protected String getHurtSound() {
      return "mob.witch.hurt";
   }

   @Override
   protected String getDeathSound() {
      return "mob.witch.death";
   }

   public void setAggressive(boolean par1) {
      this.u().updateObject(21, (byte)(par1 ? 1 : 0));
   }

   public boolean getAggressive() {
      return this.u().getWatchableObjectByte(21) == 1;
   }

   @Override
   public int getMaxHealth() {
      return 26;
   }

   @Override
   public boolean isAIEnabled() {
      return true;
   }

   @Override
   public void onLivingUpdate() {
      if (!this.worldObj.isRemote) {
         if (this.getAggressive()) {
            if (this.witchAttackTimer-- <= 0) {
               this.setAggressive(false);
               ItemStack var1 = this.bG();
               this.c(0, (ItemStack)null);
               if (var1 != null && var1.itemID == Item.potion.itemID) {
                  List var2 = Item.potion.getEffects(var1);
                  if (var2 != null) {
                     for (PotionEffect var4 : var2) {
                        this.d(new PotionEffect(var4));
                     }
                  }
               }
            }
         } else {
            short var5 = -1;
            if (this.rand.nextFloat() < 0.15F && this.ae() && !this.a(Potion.fireResistance)) {
               var5 = 16307;
            } else if (this.rand.nextFloat() < 0.05F && this.health < this.getMaxHealth()) {
               var5 = 16341;
            } else if (this.rand.nextFloat() < 0.25F && this.aJ() != null && !this.a(Potion.moveSpeed) && this.aJ().e(this) > 121.0) {
               var5 = 16274;
            } else if (this.rand.nextFloat() < 0.25F && this.aJ() != null && !this.a(Potion.moveSpeed) && this.aJ().e(this) > 121.0) {
               var5 = 16274;
            }

            if (var5 > -1) {
               this.c(0, new ItemStack(Item.potion, 1, var5));
               this.witchAttackTimer = this.bG().getMaxItemUseDuration();
               this.setAggressive(true);
            }
         }

         if (this.rand.nextFloat() < 7.5E-4F) {
            this.worldObj.setEntityState(this, (byte)15);
         }
      }

      super.onLivingUpdate();
   }

   @Environment(EnvType.CLIENT)
   @Override
   public void handleHealthUpdate(byte par1) {
      if (par1 == 15) {
         for (int var2 = 0; var2 < this.rand.nextInt(35) + 10; var2++) {
            this.worldObj
               .spawnParticle(
                  "witchMagic",
                  this.posX + this.rand.nextGaussian() * 0.13F,
                  this.boundingBox.maxY + 0.5 + this.rand.nextGaussian() * 0.13F,
                  this.posZ + this.rand.nextGaussian() * 0.13F,
                  0.0,
                  0.0,
                  0.0
               );
         }
      } else {
         super.a(par1);
      }
   }

   @Override
   protected int applyPotionDamageCalculations(DamageSource par1DamageSource, int par2) {
      par2 = super.c(par1DamageSource, par2);
      if (par1DamageSource.getEntity() == this) {
         par2 = 0;
      }

      if (par1DamageSource.isMagicDamage()) {
         par2 = (int)(par2 * 0.15);
      }

      return par2;
   }

   @Override
   public float getSpeedModifier() {
      float var1 = super.bE();
      if (this.getAggressive()) {
         var1 *= 0.75F;
      }

      return var1;
   }

   @Override
   protected void dropFewItems(boolean par1, int par2) {
      int var3 = this.rand.nextInt(3) + 1;

      for (int var4 = 0; var4 < var3; var4++) {
         int var5 = this.rand.nextInt(3);
         int var6 = witchDrops[this.rand.nextInt(witchDrops.length)];
         if (par2 > 0) {
            var5 += this.rand.nextInt(par2 + 1);
         }

         for (int var7 = 0; var7 < var5; var7++) {
            this.b(var6, 1);
         }
      }
   }

   @Override
   public void attackEntityWithRangedAttack(EntityLiving par1EntityLiving, float par2) {
      if (!this.getAggressive()) {
         EntityPotion var3 = (EntityPotion)EntityList.createEntityOfType(EntityPotion.class, this.worldObj, this, 32732);
         var3.rotationPitch -= -20.0F;
         double var4 = par1EntityLiving.posX + par1EntityLiving.motionX - this.posX;
         double var6 = par1EntityLiving.posY + par1EntityLiving.getEyeHeight() - 1.1F - this.posY;
         double var8 = par1EntityLiving.posZ + par1EntityLiving.motionZ - this.posZ;
         float var10 = MathHelper.sqrt_double(var4 * var4 + var8 * var8);
         if (var10 >= 8.0F && !par1EntityLiving.isPotionActive(Potion.moveSlowdown)) {
            var3.setPotionDamage(32698);
         } else if (par1EntityLiving.getHealth() >= 8 && !par1EntityLiving.isPotionActive(Potion.poison)) {
            var3.setPotionDamage(32660);
         } else if (var10 <= 3.0F && !par1EntityLiving.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F) {
            var3.setPotionDamage(32696);
         }

         var3.c(var4, var6 + var10 * 0.2F, var8, 0.75F, 8.0F);
         this.worldObj.spawnEntityInWorld(var3);
      }
   }
}
