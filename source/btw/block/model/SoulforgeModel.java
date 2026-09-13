package btw.block.model;

public class SoulforgeModel extends BlockModel {
   public static final float ANVIL_BASE_HEIGHT = 0.125F;
   public static final float ANVIL_BASE_WIDTH = 0.5F;
   public static final float ANVIL_HALF_BASE_WIDTH = 0.25F;
   public static final float ANVIL_SHAFT_HEIGHT = 0.4375F;
   public static final float ANVIL_SHAFT_WIDTH = 0.25F;
   public static final float ANVIL_HALF_SHAFT_WIDTH = 0.125F;
   public static final float ANVIL_TOP_HEIGHT = 0.4375F;
   public static final float ANVIL_TOP_WIDTH = 0.375F;
   public static final float ANVIL_TOP_HALF_WIDTH = 0.1875F;

   @Override
   protected void initModel() {
      this.addBox(0.25, 0.0, 0.0, 0.75, 0.125, 1.0);
      this.addBox(0.375, 0.125, 0.375, 0.625, 0.5625, 0.625);
      this.addBox(0.3125, 0.5625, 0.3125, 0.6875, 1.0, 0.6875);
      this.addBox(0.3125, 0.75, 0.25, 0.6875, 1.0, 0.3125);
      this.addBox(0.3125, 0.9375, 0.0, 0.6875, 1.0, 0.25);
      this.addBox(0.3125, 0.75, 0.6875, 0.6875, 1.0, 0.75);
      this.addBox(0.375, 0.8125, 0.75, 0.625, 1.0, 0.875);
      this.addBox(0.4375, 0.875, 0.875, 0.5625, 1.0, 1.0);
   }
}
