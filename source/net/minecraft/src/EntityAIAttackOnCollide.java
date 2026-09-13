package net.minecraft.src;

public class EntityAIAttackOnCollide extends EntityAIBase {
   public World worldObj;
   public EntityLiving attacker;
   public EntityLiving entityTarget;
   public int attackTick = 0;
   public float field_75440_e;
   public boolean field_75437_f;
   public PathEntity entityPathEntity;
   public Class classTarget;
   private int field_75445_i;

   public EntityAIAttackOnCollide(EntityLiving par1EntityLiving, Class par2Class, float par3, boolean par4) {
      this(par1EntityLiving, par3, par4);
      this.classTarget = par2Class;
   }

   public EntityAIAttackOnCollide(EntityLiving par1EntityLiving, float par2, boolean par3) {
      this.attacker = par1EntityLiving;
      this.worldObj = par1EntityLiving.worldObj;
      this.field_75440_e = par2;
      this.field_75437_f = par3;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      EntityLiving var1 = this.attacker.getAttackTarget();
      if (var1 == null) {
         return false;
      } else if (this.classTarget != null && !this.classTarget.isAssignableFrom(var1.getClass())) {
         return false;
      } else {
         this.entityTarget = var1;
         this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(this.entityTarget);
         return this.entityPathEntity != null;
      }
   }

   @Override
   public boolean continueExecuting() {
      EntityLiving var1 = this.attacker.getAttackTarget();
      return var1 == null
         ? false
         : (
            !this.entityTarget.isEntityAlive()
               ? false
               : (
                  !this.field_75437_f
                     ? !this.attacker.getNavigator().noPath()
                     : this.attacker
                        .isWithinHomeDistance(
                           MathHelper.floor_double(this.entityTarget.posX),
                           MathHelper.floor_double(this.entityTarget.posY),
                           MathHelper.floor_double(this.entityTarget.posZ)
                        )
               )
         );
   }

   @Override
   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.entityPathEntity, this.field_75440_e);
      this.field_75445_i = 0;
   }

   @Override
   public void resetTask() {
      if (this.attacker.getAttackTarget() == this.entityTarget) {
         this.attacker.setAttackTarget(null);
      }

      this.entityTarget = null;
      this.attacker.getNavigator().clearPathEntity();
   }

   @Override
   public void updateTask() {
      this.attacker.getLookHelper().setLookPositionWithEntity(this.entityTarget, 30.0F, 30.0F);
      if ((this.field_75437_f || this.attacker.getEntitySenses().canSee(this.entityTarget)) && --this.field_75445_i <= 0) {
         this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);
         this.attacker.getNavigator().tryMoveToEntityLiving(this.entityTarget, this.field_75440_e);
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      double dCombinedWidth = this.attacker.width + this.entityTarget.width;
      double var1 = dCombinedWidth * dCombinedWidth;
      if (this.entityTarget != this.attacker.riddenByEntity) {
         if (this.attacker.e(this.entityTarget.posX, this.entityTarget.boundingBox.minY, this.entityTarget.posZ) <= var1 && this.attackTick <= 0) {
            this.attackTick = 20;
            if (this.attacker.getHeldItem() != null) {
               this.attacker.swingItem();
            }

            this.attacker.attackEntityAsMob(this.entityTarget);
         }
      }
   }
}
