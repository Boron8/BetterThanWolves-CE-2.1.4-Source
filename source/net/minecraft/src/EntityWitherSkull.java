package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class EntityWitherSkull extends EntityFireball {
   public EntityWitherSkull(World par1World) {
      super(par1World);
      this.a(0.3125F, 0.3125F);
   }

   public EntityWitherSkull(World par1World, EntityLiving par2EntityLiving, double par3, double par5, double par7) {
      super(par1World, par2EntityLiving, par3, par5, par7);
      this.a(0.3125F, 0.3125F);
   }

   @Override
   protected float getMotionFactor() {
      return this.isInvulnerable() ? 0.73F : super.getMotionFactor();
   }

   @Environment(EnvType.CLIENT)
   public EntityWitherSkull(World par1World, double par2, double par4, double par6, double par8, double par10, double par12) {
      super(par1World, par2, par4, par6, par8, par10, par12);
      this.a(0.3125F, 0.3125F);
   }

   @Override
   public boolean isBurning() {
      return false;
   }

   @Override
   public float func_82146_a(Explosion par1Explosion, World par2World, int par3, int par4, int par5, Block par6Block) {
      float var7 = super.a(par1Explosion, par2World, par3, par4, par5, par6Block);
      if (this.isInvulnerable() && par6Block != Block.bedrock && par6Block != Block.endPortal && par6Block != Block.endPortalFrame) {
         var7 = Math.min(0.8F, var7);
      }

      return var7;
   }

   @Override
   protected void onImpact(MovingObjectPosition par1MovingObjectPosition) {
      if (!this.worldObj.isRemote) {
         if (par1MovingObjectPosition.entityHit != null) {
            if (this.shootingEntity != null) {
               if (par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8)
                  && !par1MovingObjectPosition.entityHit.isEntityAlive()) {
                  this.shootingEntity.heal(5);
               }
            } else {
               par1MovingObjectPosition.entityHit.attackEntityFrom(DamageSource.magic, 5);
            }

            if (par1MovingObjectPosition.entityHit instanceof EntityLiving) {
               byte var2 = 0;
               if (this.worldObj.difficultySetting > 1) {
                  if (this.worldObj.difficultySetting == 2) {
                     var2 = 10;
                  } else if (this.worldObj.difficultySetting == 3) {
                     var2 = 40;
                  }
               }

               if (var2 > 0) {
                  ((EntityLiving)par1MovingObjectPosition.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * var2, 1));
               }
            }
         }

         this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
         this.w();
      }
   }

   @Override
   public boolean canBeCollidedWith() {
      return false;
   }

   @Override
   public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
      return false;
   }

   @Override
   protected void entityInit() {
      this.dataWatcher.addObject(10, (byte)0);
   }

   public boolean isInvulnerable() {
      return this.dataWatcher.getWatchableObjectByte(10) == 1;
   }

   public void setInvulnerable(boolean par1) {
      this.dataWatcher.updateObject(10, (byte)(par1 ? 1 : 0));
   }
}
