package btw.entity.mob.behavior;

import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;

public class MultiTemptBehavior extends EntityAIBase {
   protected EntityAnimal myAnimal;
   protected float moveSpeed;
   private EntityPlayer temptingPlayer;
   private int delayBetweenCounter = 0;
   private boolean doesAnimalNormallyAvoidWater;

   public MultiTemptBehavior(EntityAnimal animal, float fMoveSpeed) {
      this.myAnimal = animal;
      this.moveSpeed = fMoveSpeed;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      if (this.delayBetweenCounter <= 0) {
         this.temptingPlayer = this.myAnimal.worldObj.getClosestPlayerToEntity(this.myAnimal, 10.0);
         if (this.temptingPlayer != null) {
            ItemStack itemstack = this.temptingPlayer.getCurrentEquippedItem();
            if (itemstack != null) {
               return this.myAnimal.isTemptingItem(itemstack);
            }
         }
      } else {
         this.delayBetweenCounter--;
      }

      return false;
   }

   @Override
   public void startExecuting() {
      this.doesAnimalNormallyAvoidWater = this.myAnimal.aC().getAvoidsWater();
      this.myAnimal.aC().setAvoidsWater(false);
   }

   @Override
   public void resetTask() {
      this.temptingPlayer = null;
      this.myAnimal.aC().clearPathEntity();
      this.delayBetweenCounter = 33;
      this.myAnimal.aC().setAvoidsWater(this.doesAnimalNormallyAvoidWater);
   }

   @Override
   public void updateTask() {
      this.myAnimal.az().setLookPositionWithEntity(this.temptingPlayer, 30.0F, this.myAnimal.bs());
      if (this.myAnimal.e(this.temptingPlayer) < 6.25) {
         this.myAnimal.aC().clearPathEntity();
      } else {
         this.myAnimal.aC().tryMoveToEntityLiving(this.temptingPlayer, this.moveSpeed);
      }
   }
}
