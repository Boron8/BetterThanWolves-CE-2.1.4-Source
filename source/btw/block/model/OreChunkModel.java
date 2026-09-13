package btw.block.model;

public class OreChunkModel extends BlockModel {
   protected static final double BASE_WIDTH = 0.1875;
   protected static final double BASE_HALF_WIDTH = 0.09375;
   protected static final double BASE_HEIGHT = 0.03125;
   protected static final double CENTER_WIDTH = 0.25;
   protected static final double CENTER_HALF_WIDTH = 0.125;
   protected static final double CENTER_HEIGHT = 0.1875;
   protected static final double TOP_WIDTH = 0.125;
   protected static final double TOP_HALF_WIDTH = 0.0625;
   protected static final double TOP_HEIGHT = 0.03125;
   protected static final double TOP_VERTICAL_OFFSET = 0.21875;
   protected static final double TOP_X_OFFSET = 0.0;
   protected static final double TOP_Z_OFFSET = 0.03125;
   protected static final double FRINGE_X_WIDTH = 0.3125;
   protected static final double FRINGE_X_HALF_WIDTH = 0.15625;
   protected static final double FRINGE_X_DEPTH = 0.1875;
   protected static final double FRINGE_X_HALF_DEPTH = 0.09375;
   protected static final double FRINGE_X_HEIGHT = 0.09375;
   protected static final double FRINGE_X_VERTICAL_OFFSET = 0.0625;
   protected static final double FRINGE_Z_WIDTH = 0.1875;
   protected static final double FRINGE_Z_HALF_WIDTH = 0.09375;
   protected static final double FRINGE_Z_DEPTH = 0.3125;
   protected static final double FRINGE_Z_HALF_DEPTH = 0.15625;
   protected static final double FRINGE_Z_HEIGHT = 0.09375;
   protected static final double FRINGE_Z_VERTICAL_OFFSET = 0.09375;
   public static final double BOUNDING_BOX_WIDTH = 0.25;
   public static final double BOUNDING_BOX_HALF_WIDTH = 0.125;
   public static final double BOUNDING_BOX_HEIGHT = 0.1875;
   public static final double BOUNDING_BOX_VERTICAL_OFFSET = 0.03125;

   @Override
   protected void initModel() {
      super.initModel();
      this.addBox(0.40625, 0.0, 0.40625, 0.59375, 0.03125, 0.59375);
      this.addBox(0.375, 0.03125, 0.375, 0.625, 0.21875, 0.625);
      this.addBox(0.4375, 0.21875, 0.46875, 0.5625, 0.25, 0.59375);
      this.addBox(0.34375, 0.0625, 0.40625, 0.65625, 0.15625, 0.59375);
      this.addBox(0.40625, 0.09375, 0.34375, 0.59375, 0.1875, 0.65625);
   }
}
