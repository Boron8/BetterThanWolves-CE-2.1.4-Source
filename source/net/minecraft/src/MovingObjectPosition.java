package net.minecraft.src;

public class MovingObjectPosition {
   public EnumMovingObjectType typeOfHit;
   public int blockX;
   public int blockY;
   public int blockZ;
   public int sideHit;
   public Vec3 hitVec;
   public Entity entityHit;

   public MovingObjectPosition(int var1, int var2, int var3, int var4, Vec3 var5) {
      this.typeOfHit = EnumMovingObjectType.TILE;
      this.blockX = var1;
      this.blockY = var2;
      this.blockZ = var3;
      this.sideHit = var4;
      this.hitVec = var5.myVec3LocalPool.getVecFromPool(var5.xCoord, var5.yCoord, var5.zCoord);
   }

   public MovingObjectPosition(Entity var1) {
      this.typeOfHit = EnumMovingObjectType.ENTITY;
      this.entityHit = var1;
      this.hitVec = var1.worldObj.getWorldVec3Pool().getVecFromPool(var1.posX, var1.posY, var1.posZ);
   }
}
