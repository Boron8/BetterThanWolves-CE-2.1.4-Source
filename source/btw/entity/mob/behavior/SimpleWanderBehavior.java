package btw.entity.mob.behavior;

import btw.util.RandomPositionGenerator;
import btw.world.util.BlockPos;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityCreature;

public class SimpleWanderBehavior extends EntityAIBase {
   private EntityCreature myEntity;
   private float moveSpeed;
   protected BlockPos destPos = new BlockPos();

   public SimpleWanderBehavior(EntityCreature entity, float fMoveSpeed) {
      this.myEntity = entity;
      this.moveSpeed = fMoveSpeed;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      return this.myEntity.aE().nextInt(120) == 0 && RandomPositionGenerator.findSimpleRandomTargetBlock(this.myEntity, 10, 7, this.destPos);
   }

   @Override
   public boolean continueExecuting() {
      return !this.myEntity.aC().noPath();
   }

   @Override
   public void startExecuting() {
      this.myEntity.aC().tryMoveToXYZ(this.destPos.x, this.destPos.y, this.destPos.z, this.moveSpeed);
   }
}
