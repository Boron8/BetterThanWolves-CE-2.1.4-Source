package net.minecraft.src;

public class EntityAITargetNonTamed extends EntityAINearestAttackableTarget {
   private EntityTameable theTameable;

   public EntityAITargetNonTamed(EntityTameable var1, Class var2, float var3, int var4, boolean var5) {
      super(var1, var2, var3, var4, var5);
      this.theTameable = var1;
   }

   @Override
   public boolean shouldExecute() {
      return this.theTameable.isTamed() ? false : super.shouldExecute();
   }
}
