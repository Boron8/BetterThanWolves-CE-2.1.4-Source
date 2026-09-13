package btw.block.model;

import net.minecraft.src.AxisAlignedBB;

public class AnvilModel extends BlockModel {
   private static final double BASE_WIDTH = 0.75;
   private static final double BASE_HALF_WIDTH = 0.375;
   private static final double BASE_HEIGHT = 0.25;
   private static final double BASE_TOP_WIDTH = 0.5;
   private static final double BASE_TOP_HALF_WIDTH = 0.25;
   private static final double BASE_TOP_DEPTH = 0.625;
   private static final double BASE_TOP_HALF_DEPTH = 0.3125;
   private static final double BASE_TOP_HEIGHT = 0.0625;
   private static final double SHAFT_WIDTH = 0.25;
   private static final double SHAFT_HALF_WIDTH = 0.125;
   private static final double SHAFT_DEPTH = 0.5;
   private static final double SHAFT_HALF_DEPTH = 0.25;
   private static final double SHAFT_HEIGHT = 0.3125;
   private static final double TOP_WIDTH = 0.625;
   private static final double TOP_HALF_WIDTH = 0.3125;
   private static final double TOP_DEPTH = 1.0;
   private static final double TOP_HALF_DEPTH = 0.5;
   private static final double TOP_HEIGHT = 0.375;
   public AxisAlignedBB boxSelection;

   @Override
   protected void initModel() {
      this.addBox(0.125, 0.0, 0.125, 0.875, 0.25, 0.875);
      double dRunningHeight = 0.25;
      this.addBox(0.25, dRunningHeight, 0.1875, 0.75, dRunningHeight + 0.0625, 0.8125);
      dRunningHeight += 0.0625;
      this.addBox(0.375, dRunningHeight, 0.25, 0.625, dRunningHeight + 0.3125, 0.75);
      dRunningHeight += 0.3125;
      this.addBox(0.1875, dRunningHeight, 0.0, 0.8125, dRunningHeight + 0.375, 1.0);
      this.boxSelection = new AxisAlignedBB(0.1875, 0.625, 0.0, 0.8125, 1.0, 1.0);
   }
}
