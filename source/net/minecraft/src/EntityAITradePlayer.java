package net.minecraft.src;

public class EntityAITradePlayer extends EntityAIBase {
   private EntityVillager villager;

   public EntityAITradePlayer(EntityVillager var1) {
      this.villager = var1;
      this.a(5);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.villager.R()) {
         return false;
      } else if (this.villager.G()) {
         return false;
      } else if (!this.villager.onGround) {
         return false;
      } else if (this.villager.velocityChanged) {
         return false;
      } else {
         EntityPlayer var1 = this.villager.getCustomer();
         if (var1 == null) {
            return false;
         } else {
            return this.villager.e(var1) > 16.0 ? false : var1.openContainer instanceof Container;
         }
      }
   }

   @Override
   public void startExecuting() {
      this.villager.aC().clearPathEntity();
   }

   @Override
   public void resetTask() {
      this.villager.setCustomer(null);
   }
}
