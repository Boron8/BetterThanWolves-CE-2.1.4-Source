package btw.entity.mob.behavior;

import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAnimal;
import net.minecraft.src.EntityItem;

public class MoveToLooseFoodBehavior extends EntityAIBase {
   protected EntityAnimal myAnimal;
   protected float moveSpeed;
   private EntityItem temptingItem = null;
   private static final int UPDATES_BETWEEN_CHECKS = 20;
   private int delayBetweenChecksCount = 20;
   private boolean failedToPath = false;

   public MoveToLooseFoodBehavior(EntityAnimal animal, float fMoveSpeed) {
      this.myAnimal = animal;
      this.moveSpeed = fMoveSpeed;
      this.a(3);
   }

   @Override
   public boolean shouldExecute() {
      boolean bReturnValue = false;
      if (this.delayBetweenChecksCount <= 0) {
         this.delayBetweenChecksCount = 20 + this.myAnimal.rand.nextInt(3) - 1;
         if (this.myAnimal.isReadyToEatLooseFood()) {
            List<EntityItem> entityList = this.myAnimal
               .worldObj
               .getEntitiesWithinAABB(
                  EntityItem.class,
                  AxisAlignedBB.getAABBPool()
                     .getAABB(
                        this.myAnimal.posX - 10.0,
                        this.myAnimal.posY - 7.0,
                        this.myAnimal.posZ - 10.0,
                        this.myAnimal.posX + 10.0,
                        this.myAnimal.posY + 7.0,
                        this.myAnimal.posZ + 10.0
                     )
               );
            if (!entityList.isEmpty()) {
               double dClosestDistSq = 0.0;
               this.temptingItem = null;

               for (EntityItem tempEntity : entityList) {
                  if (tempEntity.R() && this.myAnimal.isReadyToEatLooseItem(tempEntity.getEntityItem())) {
                     double dTempDistSq = this.myAnimal.e(tempEntity);
                     if (this.temptingItem == null || dTempDistSq < dClosestDistSq) {
                        this.temptingItem = tempEntity;
                        dClosestDistSq = dTempDistSq;
                        bReturnValue = true;
                     }
                  }
               }
            }
         }
      } else {
         this.delayBetweenChecksCount--;
      }

      return bReturnValue;
   }

   @Override
   public boolean continueExecuting() {
      return !this.failedToPath && this.temptingItem != null && this.temptingItem.R() && this.myAnimal.isReadyToEatLooseItem(this.temptingItem.getEntityItem());
   }

   @Override
   public void updateTask() {
      this.myAnimal.az().setLookPositionWithEntity(this.temptingItem, 30.0F, this.myAnimal.bs());
      if (this.isWithinEatBox()) {
         this.myAnimal.aC().clearPathEntity();
      } else if (!this.myAnimal.aC().tryMoveToEntity(this.temptingItem, this.moveSpeed)) {
         this.failedToPath = true;
      }
   }

   @Override
   public void resetTask() {
      this.temptingItem = null;
      this.failedToPath = false;
      this.myAnimal.aC().clearPathEntity();
   }

   public boolean isWithinEatBox() {
      AxisAlignedBB eatBox = AxisAlignedBB.getAABBPool()
         .getAABB(
            this.myAnimal.boundingBox.minX - 1.45F,
            this.myAnimal.boundingBox.minY - 0.95F,
            this.myAnimal.boundingBox.minZ - 1.45F,
            this.myAnimal.boundingBox.maxX + 1.45F,
            this.myAnimal.boundingBox.maxY + 0.95F,
            this.myAnimal.boundingBox.maxZ + 1.45F
         );
      return eatBox.intersectsWith(this.temptingItem.boundingBox);
   }
}
