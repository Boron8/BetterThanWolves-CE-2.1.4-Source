package btw.block.model;

import net.minecraft.src.AxisAlignedBB;

public class MillstoneModel extends BlockModel {
   public static final double BASE_HEIGHT = 0.5625;
   public static final double MID_MIN_Y = 0.4375;
   public static final double MID_MAX_Y = 1.0;
   public static final double MID_WIDTH_GAP = 0.0625;
   public static final double TOP_MIN_Y = 0.6875;
   public static final double TOP_HEIGHT = 0.25;
   public AxisAlignedBB boxBase;
   public AxisAlignedBB boxSelection;

   @Override
   protected void initModel() {
      this.boxBase = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.5625, 1.0);
      this.addBox(0.0625, 0.4375, 0.0625, 0.9375, 1.0, 0.9375);
      this.addBox(0.0, 0.6875, 0.0, 1.0, 0.9375, 1.0);
      this.boxSelection = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 0.9375, 1.0);
   }
}
