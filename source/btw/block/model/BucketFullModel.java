package btw.block.model;

import btw.util.PrimitiveAABBWithBenefits;

public class BucketFullModel extends BucketModel {
   public static final double CONTENTS_HEIGHT = 0.34375;
   public static final double CONTENTS_VERTICAL_OFFSET = 0.125;
   public static final double CONTENTS_WIDTH = 0.375;
   public static final double CONTENTS_HALF_WIDTH = 0.1875;

   @Override
   protected void initModel() {
      super.initModel();
      PrimitiveAABBWithBenefits tempBox = new PrimitiveAABBWithBenefits(0.3125, 0.125, 0.3125, 0.6875, 0.46875, 0.6875, 4);
      this.addPrimitive(tempBox);
   }
}
