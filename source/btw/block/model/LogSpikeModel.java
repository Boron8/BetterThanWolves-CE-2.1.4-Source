package btw.block.model;

import net.minecraft.src.AxisAlignedBB;

public class LogSpikeModel extends BlockModel {
   private static final float RIM_WIDTH = 0.0625F;
   private static final float LAYER_HEIGHT = 0.125F;
   private static final float FIRST_LAYER_HEIGHT = 0.1875F;
   private static final float LAYER_WIDTH_GAP = 0.0625F;
   private static final float SELECTION_HEIGHT = 0.1875F;
   private static final float SELECTION_WIDTH_GAP = 0.0625F;
   public static final AxisAlignedBB boxSelection = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);

   @Override
   protected void initModel() {
      this.addBox(0.0625, 0.0, 0.0625, 0.9375, 0.1875, 0.9375);

      for (int iTempLayer = 1; iTempLayer <= 6; iTempLayer++) {
         float fWidthGap = 0.0625F + 0.0625F * iTempLayer;
         float fHeightGap = 0.1875F + 0.125F * (iTempLayer - 1);
         this.addBox(fWidthGap, fHeightGap, fWidthGap, 1.0F - fWidthGap, fHeightGap + 0.125F, 1.0F - fWidthGap);
      }
   }
}
