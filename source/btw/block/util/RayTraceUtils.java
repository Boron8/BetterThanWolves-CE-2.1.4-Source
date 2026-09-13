package btw.block.util;

import btw.util.MathUtils;
import btw.util.PrimitiveQuad;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Block;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class RayTraceUtils {
   private World world;
   private int blockX;
   private int blockY;
   private int blockZ;
   private Vec3 startRay;
   private Vec3 endRay;
   List<MovingObjectPosition> intersectionPointList = new ArrayList<>();

   public RayTraceUtils(World world, int i, int j, int k, Vec3 startRay, Vec3 endRay) {
      this.world = world;
      this.blockX = i;
      this.blockY = j;
      this.blockZ = k;
      this.startRay = Vec3.createVectorHelper(startRay.xCoord, startRay.yCoord, startRay.zCoord);
      this.endRay = Vec3.createVectorHelper(endRay.xCoord, endRay.yCoord, endRay.zCoord);
   }

   public void addBoxWithLocalCoordsToIntersectionList(AxisAlignedBB box) {
      this.addBoxWithLocalCoordsToIntersectionList(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
   }

   public void addBoxWithLocalCoordsToIntersectionList(double fMinX, double fMinY, double fMinZ, double fMaxX, double fMaxY, double fMaxZ) {
      Vec3 startRay = this.startRay.addVector(-this.blockX, -this.blockY, -this.blockZ);
      Vec3 endRay = this.endRay.addVector(-this.blockX, -this.blockY, -this.blockZ);
      Vec3 boxMin = Vec3.createVectorHelper(fMinX, fMinY, fMinZ);
      Vec3 boxMax = Vec3.createVectorHelper(fMaxX, fMaxY, fMaxZ);
      Vec3 vec3 = startRay.getIntermediateWithXValue(endRay, boxMin.xCoord);
      Vec3 vec3_1 = startRay.getIntermediateWithXValue(endRay, boxMax.xCoord);
      Vec3 vec3_2 = startRay.getIntermediateWithYValue(endRay, boxMin.yCoord);
      Vec3 vec3_3 = startRay.getIntermediateWithYValue(endRay, boxMax.yCoord);
      Vec3 vec3_4 = startRay.getIntermediateWithZValue(endRay, boxMin.zCoord);
      Vec3 vec3_5 = startRay.getIntermediateWithZValue(endRay, boxMax.zCoord);
      if (!this.isVecInsideYZBounds(vec3, boxMin, boxMax)) {
         vec3 = null;
      }

      if (!this.isVecInsideYZBounds(vec3_1, boxMin, boxMax)) {
         vec3_1 = null;
      }

      if (!this.isVecInsideXZBounds(vec3_2, boxMin, boxMax)) {
         vec3_2 = null;
      }

      if (!this.isVecInsideXZBounds(vec3_3, boxMin, boxMax)) {
         vec3_3 = null;
      }

      if (!this.isVecInsideXYBounds(vec3_4, boxMin, boxMax)) {
         vec3_4 = null;
      }

      if (!this.isVecInsideXYBounds(vec3_5, boxMin, boxMax)) {
         vec3_5 = null;
      }

      Vec3 vec3_6 = null;
      if (vec3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3;
      }

      if (vec3_1 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_1) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_1;
      }

      if (vec3_2 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_2) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_2;
      }

      if (vec3_3 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_3) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_3;
      }

      if (vec3_4 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_4) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_4;
      }

      if (vec3_5 != null && (vec3_6 == null || startRay.squareDistanceTo(vec3_5) < startRay.squareDistanceTo(vec3_6))) {
         vec3_6 = vec3_5;
      }

      if (vec3_6 != null) {
         byte byte0 = -1;
         if (vec3_6 == vec3) {
            byte0 = 4;
         }

         if (vec3_6 == vec3_1) {
            byte0 = 5;
         }

         if (vec3_6 == vec3_2) {
            byte0 = 0;
         }

         if (vec3_6 == vec3_3) {
            byte0 = 1;
         }

         if (vec3_6 == vec3_4) {
            byte0 = 2;
         }

         if (vec3_6 == vec3_5) {
            byte0 = 3;
         }

         this.intersectionPointList
            .add(new MovingObjectPosition(this.blockX, this.blockY, this.blockZ, byte0, vec3_6.addVector(this.blockX, this.blockY, this.blockZ)));
      }
   }

   public void addQuadWithLocalCoordsToIntersectionList(PrimitiveQuad quad, Vec3 pointOnQuad) {
      Vec3 lineDir = this.endRay.subtractFrom(this.startRay);
      Vec3 pointOnLine = this.startRay.addVector(-this.blockX, -this.blockY, -this.blockZ);
      Vec3 planeNormal = quad.computeNormal();
      double lDotn = planeNormal.dotProduct(lineDir);
      if (lDotn != 0.0) {
         Vec3 p0Minusl0 = pointOnLine.subtractFrom(pointOnQuad);
         double dNumeratorDot = planeNormal.dotProduct(p0Minusl0);
         if (dNumeratorDot != 0.0) {
            double dLineScalar = dNumeratorDot / lDotn;
            Vec3 intersection = Vec3.createVectorHelper(dLineScalar * lineDir.xCoord, dLineScalar * lineDir.yCoord, dLineScalar * lineDir.zCoord)
               .addVector(pointOnLine);
            if (quad.isPointOnPlaneWithinBounds(intersection)) {
               int iFacingClicked = this.convertVectorToApproximateBlockFacing(planeNormal);
               if (lDotn < 0.0) {
                  iFacingClicked = Block.getOppositeFacing(iFacingClicked);
               }

               this.intersectionPointList
                  .add(
                     new MovingObjectPosition(
                        this.blockX, this.blockY, this.blockZ, iFacingClicked, intersection.addVector(this.blockX, this.blockY, this.blockZ)
                     )
                  );
            }
         }
      }
   }

   public int convertVectorToApproximateBlockFacing(Vec3 vec) {
      double dAbsX = MathUtils.absDouble(vec.xCoord);
      double dAbsY = MathUtils.absDouble(vec.yCoord);
      double dAbsZ = MathUtils.absDouble(vec.zCoord);
      if (dAbsX >= dAbsZ && dAbsX >= dAbsY) {
         return vec.xCoord < 0.0 ? 4 : 5;
      } else if (dAbsZ >= dAbsY) {
         return vec.zCoord < 0.0 ? 2 : 3;
      } else {
         return vec.yCoord < 0.0 ? 0 : 1;
      }
   }

   public MovingObjectPosition getFirstIntersection() {
      MovingObjectPosition firstIntersect = null;
      Iterator iterator = this.intersectionPointList.iterator();
      double dMaxDistance = 0.0;

      while (iterator.hasNext()) {
         MovingObjectPosition tempPos = (MovingObjectPosition)iterator.next();
         double dCurrentIntersectDistance = tempPos.hitVec.squareDistanceTo(this.endRay);
         if (dCurrentIntersectDistance > dMaxDistance) {
            firstIntersect = tempPos;
            dMaxDistance = dCurrentIntersectDistance;
         }
      }

      return firstIntersect;
   }

   private boolean isVecInsideYZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max) {
      return par1Vec3 == null
         ? false
         : par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
   }

   private boolean isVecInsideXZBounds(Vec3 par1Vec3, Vec3 min, Vec3 max) {
      return par1Vec3 == null
         ? false
         : par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.zCoord >= min.zCoord && par1Vec3.zCoord <= max.zCoord;
   }

   private boolean isVecInsideXYBounds(Vec3 par1Vec3, Vec3 min, Vec3 max) {
      return par1Vec3 == null
         ? false
         : par1Vec3.xCoord >= min.xCoord && par1Vec3.xCoord <= max.xCoord && par1Vec3.yCoord >= min.yCoord && par1Vec3.yCoord <= max.yCoord;
   }
}
