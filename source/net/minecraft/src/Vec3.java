package net.minecraft.src;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class Vec3 {
   public static final Vec3Pool fakePool = new Vec3Pool(-1, -1);
   public final Vec3Pool myVec3LocalPool;
   public double xCoord;
   public double yCoord;
   public double zCoord;

   public static Vec3 createVectorHelper(double par0, double par2, double par4) {
      return new Vec3(fakePool, par0, par2, par4);
   }

   protected Vec3(Vec3Pool par1Vec3Pool, double par2, double par4, double par6) {
      if (par2 == -0.0) {
         par2 = 0.0;
      }

      if (par4 == -0.0) {
         par4 = 0.0;
      }

      if (par6 == -0.0) {
         par6 = 0.0;
      }

      this.xCoord = par2;
      this.yCoord = par4;
      this.zCoord = par6;
      this.myVec3LocalPool = par1Vec3Pool;
   }

   public Vec3 setComponents(double par1, double par3, double par5) {
      this.xCoord = par1;
      this.yCoord = par3;
      this.zCoord = par5;
      return this;
   }

   public Vec3 subtract(Vec3 par1Vec3) {
      return this.myVec3LocalPool.getVecFromPool(par1Vec3.xCoord - this.xCoord, par1Vec3.yCoord - this.yCoord, par1Vec3.zCoord - this.zCoord);
   }

   public Vec3 normalize() {
      double var1 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
      return var1 < 1.0E-4
         ? this.myVec3LocalPool.getVecFromPool(0.0, 0.0, 0.0)
         : this.myVec3LocalPool.getVecFromPool(this.xCoord / var1, this.yCoord / var1, this.zCoord / var1);
   }

   public double dotProduct(Vec3 par1Vec3) {
      return this.xCoord * par1Vec3.xCoord + this.yCoord * par1Vec3.yCoord + this.zCoord * par1Vec3.zCoord;
   }

   public Vec3 crossProduct(Vec3 par1Vec3) {
      return this.myVec3LocalPool
         .getVecFromPool(
            this.yCoord * par1Vec3.zCoord - this.zCoord * par1Vec3.yCoord,
            this.zCoord * par1Vec3.xCoord - this.xCoord * par1Vec3.zCoord,
            this.xCoord * par1Vec3.yCoord - this.yCoord * par1Vec3.xCoord
         );
   }

   public Vec3 addVector(double par1, double par3, double par5) {
      return this.myVec3LocalPool.getVecFromPool(this.xCoord + par1, this.yCoord + par3, this.zCoord + par5);
   }

   public double distanceTo(Vec3 par1Vec3) {
      double var2 = par1Vec3.xCoord - this.xCoord;
      double var4 = par1Vec3.yCoord - this.yCoord;
      double var6 = par1Vec3.zCoord - this.zCoord;
      return MathHelper.sqrt_double(var2 * var2 + var4 * var4 + var6 * var6);
   }

   public double squareDistanceTo(Vec3 par1Vec3) {
      double var2 = par1Vec3.xCoord - this.xCoord;
      double var4 = par1Vec3.yCoord - this.yCoord;
      double var6 = par1Vec3.zCoord - this.zCoord;
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   public double squareDistanceTo(double par1, double par3, double par5) {
      double var7 = par1 - this.xCoord;
      double var9 = par3 - this.yCoord;
      double var11 = par5 - this.zCoord;
      return var7 * var7 + var9 * var9 + var11 * var11;
   }

   public double lengthVector() {
      return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
   }

   public Vec3 getIntermediateWithXValue(Vec3 par1Vec3, double par2) {
      double var4 = par1Vec3.xCoord - this.xCoord;
      double var6 = par1Vec3.yCoord - this.yCoord;
      double var8 = par1Vec3.zCoord - this.zCoord;
      if (var4 * var4 < 1.0E-7F) {
         return null;
      } else {
         double var10 = (par2 - this.xCoord) / var4;
         return var10 >= 0.0 && var10 <= 1.0
            ? this.myVec3LocalPool.getVecFromPool(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10)
            : null;
      }
   }

   public Vec3 getIntermediateWithYValue(Vec3 par1Vec3, double par2) {
      double var4 = par1Vec3.xCoord - this.xCoord;
      double var6 = par1Vec3.yCoord - this.yCoord;
      double var8 = par1Vec3.zCoord - this.zCoord;
      if (var6 * var6 < 1.0E-7F) {
         return null;
      } else {
         double var10 = (par2 - this.yCoord) / var6;
         return var10 >= 0.0 && var10 <= 1.0
            ? this.myVec3LocalPool.getVecFromPool(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10)
            : null;
      }
   }

   public Vec3 getIntermediateWithZValue(Vec3 par1Vec3, double par2) {
      double var4 = par1Vec3.xCoord - this.xCoord;
      double var6 = par1Vec3.yCoord - this.yCoord;
      double var8 = par1Vec3.zCoord - this.zCoord;
      if (var8 * var8 < 1.0E-7F) {
         return null;
      } else {
         double var10 = (par2 - this.zCoord) / var8;
         return var10 >= 0.0 && var10 <= 1.0
            ? this.myVec3LocalPool.getVecFromPool(this.xCoord + var4 * var10, this.yCoord + var6 * var10, this.zCoord + var8 * var10)
            : null;
      }
   }

   @Override
   public String toString() {
      return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
   }

   public void rotateAroundX(float par1) {
      float var2 = MathHelper.cos(par1);
      float var3 = MathHelper.sin(par1);
      double var4 = this.xCoord;
      double var6 = this.yCoord * var2 + this.zCoord * var3;
      double var8 = this.zCoord * var2 - this.yCoord * var3;
      this.xCoord = var4;
      this.yCoord = var6;
      this.zCoord = var8;
   }

   public void rotateAroundY(float par1) {
      float var2 = MathHelper.cos(par1);
      float var3 = MathHelper.sin(par1);
      double var4 = this.xCoord * var2 + this.zCoord * var3;
      double var6 = this.yCoord;
      double var8 = this.zCoord * var2 - this.xCoord * var3;
      this.xCoord = var4;
      this.yCoord = var6;
      this.zCoord = var8;
   }

   @Environment(EnvType.CLIENT)
   public void rotateAroundZ(float par1) {
      float var2 = MathHelper.cos(par1);
      float var3 = MathHelper.sin(par1);
      double var4 = this.xCoord * var2 + this.yCoord * var3;
      double var6 = this.yCoord * var2 - this.xCoord * var3;
      double var8 = this.zCoord;
      this.xCoord = var4;
      this.yCoord = var6;
      this.zCoord = var8;
   }

   public static Vec3 createVectorHelper(Vec3 copyVector) {
      return new Vec3(fakePool, copyVector.xCoord, copyVector.yCoord, copyVector.zCoord);
   }

   public void rotateAsBlockPosAroundJToFacing(int iFacing) {
      if (iFacing > 2) {
         if (iFacing == 5) {
            double tempZ = this.xCoord;
            this.xCoord = 1.0 - this.zCoord;
            this.zCoord = tempZ;
         } else if (iFacing == 4) {
            double tempZ = 1.0 - this.xCoord;
            this.xCoord = this.zCoord;
            this.zCoord = tempZ;
         } else {
            this.xCoord = 1.0 - this.xCoord;
            this.zCoord = 1.0 - this.zCoord;
         }
      }
   }

   public void rotateAsVectorAroundJToFacing(int iFacing) {
      if (iFacing > 2) {
         if (iFacing == 5) {
            double tempZ = this.xCoord;
            this.xCoord = -this.zCoord;
            this.zCoord = tempZ;
         } else if (iFacing == 4) {
            double tempZ = -this.xCoord;
            this.xCoord = this.zCoord;
            this.zCoord = tempZ;
         } else {
            this.xCoord = -this.xCoord;
            this.zCoord = -this.zCoord;
         }
      }
   }

   public void tiltAsBlockPosToFacingAlongJ(int iFacing) {
      if (iFacing == 0) {
         this.yCoord = 1.0 - this.yCoord;
         this.xCoord = 1.0 - this.xCoord;
      } else if (iFacing == 2) {
         double tempZ = 1.0 - this.yCoord;
         this.yCoord = this.zCoord;
         this.zCoord = tempZ;
      } else if (iFacing == 3) {
         double tempZ = this.yCoord;
         this.yCoord = 1.0 - this.zCoord;
         this.zCoord = tempZ;
      } else if (iFacing == 4) {
         double tempY = this.xCoord;
         this.xCoord = 1.0 - this.yCoord;
         this.yCoord = tempY;
      } else if (iFacing == 5) {
         double tempY = 1.0 - this.xCoord;
         this.xCoord = this.yCoord;
         this.yCoord = tempY;
      }
   }

   public void tiltAsVectorToFacingAlongJ(int iFacing) {
      if (iFacing == 0) {
         this.yCoord = -this.yCoord;
         this.xCoord = -this.xCoord;
      } else if (iFacing == 2) {
         double tempZ = -this.yCoord;
         this.yCoord = this.zCoord;
         this.zCoord = tempZ;
      } else if (iFacing == 3) {
         double tempZ = this.yCoord;
         this.yCoord = -this.zCoord;
         this.zCoord = tempZ;
      } else if (iFacing == 4) {
         double tempY = this.xCoord;
         this.xCoord = -this.yCoord;
         this.yCoord = tempY;
      } else if (iFacing == 5) {
         double tempY = -this.xCoord;
         this.xCoord = this.yCoord;
         this.yCoord = tempY;
      }
   }

   public final Vec3 subtractFrom(Vec3 vec) {
      return this.subtract(vec);
   }

   public Vec3 addVector(Vec3 vec) {
      return this.myVec3LocalPool.getVecFromPool(this.xCoord + vec.xCoord, this.yCoord + vec.yCoord, this.zCoord + vec.zCoord);
   }

   public void scale(double dScale) {
      this.xCoord *= dScale;
      this.yCoord *= dScale;
      this.zCoord *= dScale;
   }

   public double distanceSquareFlat(Vec3 toVec) {
      double dDeltaX = toVec.xCoord - this.xCoord;
      double dDeltaZ = toVec.zCoord - this.zCoord;
      return dDeltaX * dDeltaX + dDeltaZ * dDeltaZ;
   }
}
