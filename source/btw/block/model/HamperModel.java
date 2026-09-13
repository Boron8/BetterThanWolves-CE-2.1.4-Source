package btw.block.model;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Vec3;

public class HamperModel extends BlockModel {
   public static final double BASE_HEIGHT = 0.8125;
   public static final double BASE_WIDTH = 0.875;
   public static final double BASE_DEPTH = 0.75;
   public static final double BASE_BOTTOM_HEIGHT = 0.125;
   public static final double BASE_BOTTOM_WIDTH_GAP = 0.0625;
   public static final double BASE_HALF_WIDTH = 0.4375;
   public static final double BASE_HALF_DEPTH = 0.375;
   public static final double LID_HEIGHT = 0.1875;
   public static final double LID_WIDTH = 1.0;
   public static final double LID_DEPTH = 0.875;
   public static final double LID_HALF_WIDTH = 0.5;
   public static final double LID_HALF_DEPTH = 0.4375;
   private static final float LID_LAYER_HEIGHT = 0.0625F;
   private static final float LID_LAYER_WIDTH_GAP = 0.0625F;
   private static final double LID_MIN_X = 0.0;
   private static final double LID_MAX_X = 1.0;
   private static final double LID_MIN_Z = 0.0625;
   private static final double LID_MAX_Z = 0.9375;
   private static final int NUM_LID_LAYERS = 3;
   private static final Vec3 lidRotationPoint = Vec3.createVectorHelper(0.5, 0.8125, 0.875);
   public BlockModel lid;
   public AxisAlignedBB selectionBox;
   public AxisAlignedBB selectionBoxOpen;

   @Override
   protected void initModel() {
      this.addBox(0.0625, 0.125, 0.125, 0.9375, 0.8125, 0.875);
      this.addBox(0.125, 0.0, 0.1875, 0.875, 0.125, 0.8125);
      this.lid = new BlockModel();

      for (double iTempLayer = 0.0; iTempLayer < 3.0; iTempLayer++) {
         double WidthOffset = iTempLayer * 0.0625;
         double HeighOffset = iTempLayer * 0.0625;
         this.lid.addBox(0.0 + WidthOffset, 0.8125 + HeighOffset, 0.0625 + WidthOffset, 1.0 - WidthOffset, 0.875 + HeighOffset, 0.9375 - WidthOffset);
      }

      this.selectionBox = new AxisAlignedBB(0.0625, 0.125, 0.125, 0.9375, 0.9375, 0.875);
      this.selectionBoxOpen = new AxisAlignedBB(0.0625, 0.125, 0.125, 0.9375, 0.8125, 0.875);
   }

   public Vec3 getLidRotationPoint() {
      return lidRotationPoint;
   }
}
