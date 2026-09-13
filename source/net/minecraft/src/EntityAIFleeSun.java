package net.minecraft.src;

import java.util.Random;

public class EntityAIFleeSun extends EntityAIBase {
   private EntityCreature theCreature;
   private double shelterX;
   private double shelterY;
   private double shelterZ;
   private float movementSpeed;
   private World theWorld;

   public EntityAIFleeSun(EntityCreature var1, float var2) {
      this.theCreature = var1;
      this.movementSpeed = var2;
      this.theWorld = var1.worldObj;
      this.a(1);
   }

   @Override
   public boolean shouldExecute() {
      if (!this.theWorld.isDaytime()) {
         return false;
      } else if (!this.theCreature.ae()) {
         return false;
      } else if (!this.theWorld
         .canBlockSeeTheSky(
            MathHelper.floor_double(this.theCreature.posX), (int)this.theCreature.boundingBox.minY, MathHelper.floor_double(this.theCreature.posZ)
         )) {
         return false;
      } else {
         Vec3 var1 = this.findPossibleShelter();
         if (var1 == null) {
            return false;
         } else {
            this.shelterX = var1.xCoord;
            this.shelterY = var1.yCoord;
            this.shelterZ = var1.zCoord;
            return true;
         }
      }
   }

   @Override
   public boolean continueExecuting() {
      return !this.theCreature.aC().noPath();
   }

   @Override
   public void startExecuting() {
      this.theCreature.aC().tryMoveToXYZ(this.shelterX, this.shelterY, this.shelterZ, this.movementSpeed);
   }

   private Vec3 findPossibleShelter() {
      Random var1 = this.theCreature.aE();

      for (int var2 = 0; var2 < 10; var2++) {
         int var3 = MathHelper.floor_double(this.theCreature.posX + var1.nextInt(20) - 10.0);
         int var4 = MathHelper.floor_double(this.theCreature.boundingBox.minY + var1.nextInt(6) - 3.0);
         int var5 = MathHelper.floor_double(this.theCreature.posZ + var1.nextInt(20) - 10.0);
         if (!this.theWorld.canBlockSeeTheSky(var3, var4, var5) && this.theCreature.getBlockPathWeight(var3, var4, var5) < 0.0F) {
            return this.theWorld.getWorldVec3Pool().getVecFromPool(var3, var4, var5);
         }
      }

      return null;
   }
}
