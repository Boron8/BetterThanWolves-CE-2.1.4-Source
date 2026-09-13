package net.minecraft.src;

import java.util.List;

public class EntityAIFollowParent extends EntityAIBase {
   EntityAnimal childAnimal;
   EntityAnimal parentAnimal;
   float field_75347_c;
   private int field_75345_d;

   public EntityAIFollowParent(EntityAnimal par1EntityAnimal, float par2) {
      this.childAnimal = par1EntityAnimal;
      this.field_75347_c = par2;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.childAnimal.b() >= 0) {
         return false;
      } else {
         List var1 = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.boundingBox.expand(8.0, 4.0, 8.0));
         EntityAnimal var2 = null;
         double var3 = Double.MAX_VALUE;

         for (EntityAnimal var6 : var1) {
            if (var6.b() >= 0) {
               double var7 = this.childAnimal.e(var6);
               if (var7 <= var3) {
                  var3 = var7;
                  var2 = var6;
               }
            }
         }

         if (var2 == null) {
            return false;
         } else if (var3 < 9.0) {
            return false;
         } else {
            this.parentAnimal = var2;
            return true;
         }
      }
   }

   @Override
   public boolean continueExecuting() {
      if (!this.childAnimal.h_()) {
         return false;
      } else if (!this.parentAnimal.R()) {
         return false;
      } else {
         double var1 = this.childAnimal.e(this.parentAnimal);
         return var1 >= 9.0 && var1 <= 256.0;
      }
   }

   @Override
   public void startExecuting() {
      this.field_75345_d = 0;
   }

   @Override
   public void resetTask() {
      this.parentAnimal = null;
   }

   @Override
   public void updateTask() {
      if (--this.field_75345_d <= 0) {
         this.field_75345_d = 10;
         this.childAnimal.aC().tryMoveToEntityLiving(this.parentAnimal, this.field_75347_c);
      }
   }
}
