package btw.block.model;

import btw.util.PrimitiveAABBWithBenefits;
import net.minecraft.src.Vec3;

public class BucketModel extends BlockModel {
   public static final int ASSEMBLY_ID_BASE = 0;
   public static final int ASSEMBLY_ID_BODY = 1;
   public static final int ASSEMBLY_ID_RIM = 2;
   public static final int ASSEMBLY_ID_INTERIOR = 3;
   public static final int ASSEMBLY_ID_CONTENTS = 4;
   public static final double HEIGHT = 0.5;
   public static final double WIDTH = 0.5;
   public static final double HALF_WIDTH = 0.25;
   public static final double BASE_HEIGHT = 0.0625;
   public static final double BASE_WIDTH = 0.375;
   public static final double BASE_HALF_WIDTH = 0.1875;
   public static final double BODY_WIDTH = 0.4375;
   public static final double BODY_HALF_WIDTH = 0.21875;
   public static final double RIM_HEIGHT = 0.0625;
   public static final double INTERIOR_HEIGHT = 0.34375;
   public static final double INTERIOR_WIDTH = 0.375;
   public static final double INTERIOR_HALF_WIDTH = 0.1875;
   private static final double MIND_THE_GAP = 0.001;

   @Override
   protected void initModel() {
      BlockModel tempModel = new BlockModel(0);
      tempModel.addBox(0.3125, 0.0, 0.3125, 0.6875, 0.0625, 0.6875);
      this.addPrimitive(tempModel);
      tempModel = new BlockModel(1);
      tempModel.addBox(0.28125, 0.0625, 0.28125, 0.71875, 0.4375, 0.71875);
      this.addPrimitive(tempModel);
      PrimitiveAABBWithBenefits tempBox = new PrimitiveAABBWithBenefits(0.25, 0.4375, 0.25, 0.75, 0.5, 0.75, 2);
      this.addPrimitive(tempBox);
      tempBox = new PrimitiveAABBWithBenefits(0.6885, 0.5, 0.6885, 0.3115, 0.15625, 0.3115, 3);
      tempBox.setForceRenderWithColorMultiplier(true);
      this.addPrimitive(tempBox);
   }

   public static void offsetModelForFacing(BlockModel model, int iFacing) {
      if (iFacing != 1) {
         Vec3 offset = getOffsetForFacing(iFacing);
         model.translate(offset.xCoord, offset.yCoord, offset.zCoord);
      }
   }

   public static Vec3 getOffsetForFacing(int iFacing) {
      if (iFacing == 0) {
         return Vec3.createVectorHelper(0.0, -0.5, 0.0);
      } else {
         Vec3 offset = Vec3.createVectorHelper(0.0, -0.25, -0.5);
         offset.rotateAsVectorAroundJToFacing(iFacing);
         return offset;
      }
   }
}
