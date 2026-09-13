package btw.block.model;

public class OvenModel extends BlockModel {
   public static final float BASE_HEIGHT = 0.375F;
   public static final float SIDE_WIDTH = 0.25F;
   public static final float HALF_SIDE_WIDTH = 0.125F;
   public static final float TOP_THICKNESS = 0.25F;
   public static final float HALF_TOP_THICKNESS = 0.125F;
   public static final float MIND_THE_GAP = 0.001F;

   @Override
   protected void initModel() {
      this.addBox(0.751F, 0.751F, 0.75, 0.249F, 0.374F, 0.0);
   }
}
