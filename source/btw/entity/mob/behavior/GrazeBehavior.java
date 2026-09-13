package btw.entity.mob.behavior;

import btw.block.blocks.GroundCoverBlock;
import btw.world.util.BlockPos;
import net.minecraft.src.Block;
import net.minecraft.src.EntityAIBase;
import net.minecraft.src.EntityAnimal;

public class GrazeBehavior extends EntityAIBase {
   private EntityAnimal myAnimal;
   private int grazeCooldown = 0;

   public GrazeBehavior(EntityAnimal entity) {
      this.myAnimal = entity;
      this.a(7);
   }

   @Override
   public boolean shouldExecute() {
      if (this.grazeCooldown > 0) {
         this.grazeCooldown--;
         return false;
      } else {
         return this.myAnimal.isSubjectToHunger()
            ? this.myAnimal.isHungryEnoughToGraze() && this.myAnimal.getGrazeBlockForPos() != null
            : this.myAnimal.aE().nextInt(this.myAnimal.h_() ? 50 : 1000) == 0 && this.myAnimal.getGrazeBlockForPos() != null;
      }
   }

   @Override
   public void startExecuting() {
      this.grazeCooldown = 10;
      this.myAnimal.grazeProgressCounter = this.myAnimal.getGrazeDuration();
      this.myAnimal.worldObj.setEntityState(this.myAnimal, (byte)10);
      this.myAnimal.aC().clearPathEntity();
   }

   @Override
   public void resetTask() {
      this.myAnimal.grazeProgressCounter = 0;
   }

   @Override
   public boolean continueExecuting() {
      return this.myAnimal.grazeProgressCounter > 0;
   }

   @Override
   public void updateTask() {
      this.myAnimal.grazeProgressCounter = Math.max(0, this.myAnimal.grazeProgressCounter - 1);
      if (this.myAnimal.grazeProgressCounter == 4) {
         BlockPos targetPos = this.myAnimal.getGrazeBlockForPos();
         if (targetPos != null) {
            this.myAnimal.onGrazeBlock(targetPos.x, targetPos.y, targetPos.z);
            GroundCoverBlock.clearAnyGroundCoverRestingOnBlock(this.myAnimal.worldObj, targetPos.x, targetPos.y, targetPos.z);
            if (this.myAnimal.shouldNotifyBlockOnGraze()) {
               int iTargetBlockID = this.myAnimal.worldObj.getBlockId(targetPos.x, targetPos.y, targetPos.z);
               if (iTargetBlockID != 0) {
                  this.myAnimal.playGrazeFX(targetPos.x, targetPos.y, targetPos.z, iTargetBlockID);
                  Block.blocksList[iTargetBlockID].onGrazed(this.myAnimal.worldObj, targetPos.x, targetPos.y, targetPos.z, this.myAnimal);
               }
            }
         }
      }
   }
}
