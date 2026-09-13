package btw.entity.mob.behavior;

import btw.util.RandomPositionGenerator;
import btw.world.util.BlockPos;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAnimal;

public class MoveToGrazeBehavior extends EntityAIBase {
   private EntityAnimal myAnimal;
   private float moveSpeed;
   protected BlockPos destPos = new BlockPos();

   public MoveToGrazeBehavior(EntityAnimal entity, float fMoveSpeed) {
      this.myAnimal = entity;
      this.moveSpeed = fMoveSpeed;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      return this.myAnimal.isSubjectToHunger() && this.myAnimal.isHungryEnoughToForceMoveToGraze()
         ? !this.myAnimal.shouldStayInPlaceToGraze() && RandomPositionGenerator.findSimpleRandomTargetBlock(this.myAnimal, 10, 7, this.destPos)
         : false;
   }

   @Override
   public boolean continueExecuting() {
      return !this.myAnimal.aC().noPath();
   }

   @Override
   public void startExecuting() {
      this.myAnimal.aC().tryMoveToXYZ(this.destPos.x, this.destPos.y, this.destPos.z, this.moveSpeed);
   }
}
