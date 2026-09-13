package net.minecraft.src;

public class EntityDragonPart extends Entity {
   public final IEntityMultiPart entityDragonObj;
   public final String name;

   public EntityDragonPart(IEntityMultiPart var1, String var2, float var3, float var4) {
      super(var1.func_82194_d());
      this.a(var3, var4);
      this.entityDragonObj = var1;
      this.name = var2;
   }

   @Override
   protected void entityInit() {
   }

   @Override
   protected void readEntityFromNBT(NBTTagCompound var1) {
   }

   @Override
   protected void writeEntityToNBT(NBTTagCompound var1) {
   }

   @Override
   public boolean canBeCollidedWith() {
      return true;
   }

   @Override
   public boolean attackEntityFrom(DamageSource var1, int var2) {
      return this.aq() ? false : this.entityDragonObj.attackEntityFromPart(this, var1, var2);
   }

   @Override
   public boolean isEntityEqual(Entity var1) {
      return this == var1 || this.entityDragonObj == var1;
   }
}
