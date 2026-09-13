package btw.entity.mob.behavior;

import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityCreature;
import net.minecraft.src.RandomPositionGenerator;
import net.minecraft.src.Vec3;

public class PanicOnHeadCrabBehavior extends EntityAIBase {
   private EntityCreature owningEntity;
   private float moveSpeed;
   private double randPosX;
   private double randPosY;
   private double randPosZ;

   public PanicOnHeadCrabBehavior(EntityCreature entity, float fMoveSpeed) {
      this.owningEntity = entity;
      this.moveSpeed = fMoveSpeed;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (this.owningEntity.hasHeadCrabbedSquid()) {
         Vec3 randPos = RandomPositionGenerator.findRandomTarget(this.owningEntity, 5, 4);
         if (randPos != null) {
            this.randPosX = randPos.xCoord;
            this.randPosY = randPos.yCoord;
            this.randPosZ = randPos.zCoord;
            return true;
         }
      }

      return false;
   }

   @Override
   public void startExecuting() {
      this.owningEntity.aC().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.moveSpeed);
   }

   @Override
   public boolean continueExecuting() {
      return !this.owningEntity.aC().noPath() && this.owningEntity.hasHeadCrabbedSquid();
   }
}
