package btw.block.model;

public class CampfireModel extends BlockModel {
   private static final float LOG_THICKNESS = 0.125F;
   private static final float MINOR_BORDER_WIDTH = 0.0625F;
   private static final float MAJOR_BORDER_WIDTH = 0.0F;
   private static final float LAYER_WIDTH_OFFSET = 0.0625F;
   private static final float LAYER_LENGTH_OFFSET = 0.0625F;
   private static final int NUM_LAYERS = 4;
   public BlockModel modelInSnow;

   @Override
   protected void initModel() {
      this.modelInSnow = new BlockModel();

      for (int iTempLayer = 0; iTempLayer < 4; iTempLayer++) {
         float fLayerMultiplier = iTempLayer;
         float fMinY = fLayerMultiplier * 0.125F;
         float fMaxY = fMinY + 0.125F;
         float fMinorOffset = 0.0F;
         float fMajorOffset = 0.0F;
         if (iTempLayer > 1) {
            fMajorOffset = 0.0625F * (fLayerMultiplier - 1.0F);
         }

         fMinorOffset = 0.0625F * fLayerMultiplier;
         float fMinorAxisMin = 0.0625F + fMinorOffset;
         float fMinorAxisMax = fMinorAxisMin + 0.125F;
         float fMajorAxisMin = 0.0F + fMajorOffset;
         float fMajorAxisMax = 1.0F - fMajorAxisMin;
         if ((iTempLayer & 1) == 0) {
            this.addBox(fMajorAxisMin, fMinY, fMinorAxisMin, fMajorAxisMax, fMaxY, fMinorAxisMax);
            this.addBox(fMajorAxisMin, fMinY, 1.0F - fMinorAxisMax, fMajorAxisMax, fMaxY, 1.0F - fMinorAxisMin);
            if (iTempLayer != 0) {
               this.modelInSnow.addBox(fMajorAxisMin, fMinY, fMinorAxisMin, fMajorAxisMax, fMaxY, fMinorAxisMax);
               this.modelInSnow.addBox(fMajorAxisMin, fMinY, 1.0F - fMinorAxisMax, fMajorAxisMax, fMaxY, 1.0F - fMinorAxisMin);
            }
         } else {
            this.addBox(fMinorAxisMin, fMinY, fMajorAxisMin, fMinorAxisMax, fMaxY, fMajorAxisMax);
            this.addBox(1.0F - fMinorAxisMax, fMinY, fMajorAxisMin, 1.0F - fMinorAxisMin, fMaxY, fMajorAxisMax);
            this.modelInSnow.addBox(fMinorAxisMin, fMinY, fMajorAxisMin, fMinorAxisMax, fMaxY, fMajorAxisMax);
            this.modelInSnow.addBox(1.0F - fMinorAxisMax, fMinY, fMajorAxisMin, 1.0F - fMinorAxisMin, fMaxY, fMajorAxisMax);
         }
      }
   }
}
