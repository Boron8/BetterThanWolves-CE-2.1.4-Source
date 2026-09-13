package net.minecraft.src;

public class EntityAIArrowAttack extends EntityAIBase {
   private final EntityLiving entityHost;
   private final IRangedAttackMob rangedAttackEntityHost;
   private EntityLiving attackTarget;
   private int rangedAttackTime = -1;
   private float entityMoveSpeed;
   private int field_75318_f = 0;
   private int field_96561_g;
   private int maxRangedAttackTime;
   private float field_96562_i;
   private float field_82642_h;

   public EntityAIArrowAttack(IRangedAttackMob var1, float var2, int var3, float var4) {
      this(var1, var2, var3, var3, var4);
   }

   public EntityAIArrowAttack(IRangedAttackMob var1, float var2, int var3, int var4, float var5) {
      if (!(var1 instanceof EntityLiving)) {
         throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
      } else {
         this.rangedAttackEntityHost = var1;
         this.entityHost = (EntityLiving)var1;
         this.entityMoveSpeed = var2;
         this.field_96561_g = var3;
         this.maxRangedAttackTime = var4;
         this.field_96562_i = var5;
         this.field_82642_h = var5 * var5;
         this.a(3);
      }
   }

   @Override
   public boolean shouldExecute() {
      EntityLiving var1 = this.entityHost.getAttackTarget();
      if (var1 == null) {
         return false;
      } else {
         this.attackTarget = var1;
         return true;
      }
   }

   @Override
   public boolean continueExecuting() {
      return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
   }

   @Override
   public void resetTask() {
      this.attackTarget = null;
      this.field_75318_f = 0;
      this.rangedAttackTime = -1;
   }

   @Override
   public void updateTask() {
      double var1 = this.entityHost.e(this.attackTarget.posX, this.attackTarget.boundingBox.minY, this.attackTarget.posZ);
      boolean var3 = this.entityHost.getEntitySenses().canSee(this.attackTarget);
      if (var3) {
         this.field_75318_f++;
      } else {
         this.field_75318_f = 0;
      }

      if (!(var1 > this.field_82642_h) && this.field_75318_f >= 20) {
         this.entityHost.getNavigator().clearPathEntity();
      } else {
         this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
      }

      this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
      if (--this.rangedAttackTime == 0) {
         if (var1 > this.field_82642_h || !var3) {
            return;
         }

         float var4 = MathHelper.sqrt_double(var1) / this.field_96562_i;
         float var5 = var4;
         if (var4 < 0.1F) {
            var5 = 0.1F;
         }

         if (var5 > 1.0F) {
            var5 = 1.0F;
         }

         this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, var5);
         this.rangedAttackTime = MathHelper.floor_float(var4 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
      } else if (this.rangedAttackTime < 0) {
         float var6 = MathHelper.sqrt_double(var1) / this.field_96562_i;
         this.rangedAttackTime = MathHelper.floor_float(var6 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
      }
   }
}
