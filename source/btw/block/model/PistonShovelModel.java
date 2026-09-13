package btw.block.model;

import btw.util.PrimitiveQuad;
import net.minecraft.src.Vec3;

public class PistonShovelModel extends BlockModel {
   public static final float SIDE_THICKNESS = 0.0625F;
   public static final float SLOP_HEIGHT = 0.125F;
   public static final float BACK_SLOP_HEIGHT = 0.25F;
   public static final float SLOPE_COLLISION_HEIGHT = 0.015625F;
   public static final float SLOPE_MIDDLE_MAJOR_GAP = 0.375F;
   public static final double MIND_THE_GAP = 0.001;
   public BlockModel rayTraceModel;
   public BlockModel collisionModel;

   @Override
   protected void initModel() {
      this.rayTraceModel = new BlockModel();
      this.collisionModel = new BlockModel();
      this.addBox(0.0, 0.0, 0.5, 0.0625, 1.0, 1.0);
      this.addBox(0.0, 0.0, 0.0, 0.0625, 0.5, 0.5);
      this.rayTraceModel.addBox(0.0, 0.0, 0.5, 0.0625, 1.0, 1.0);
      this.rayTraceModel.addBox(0.0, 0.0, 0.0, 0.0625, 0.5, 0.5);
      this.addBox(0.9375, 0.0, 0.5, 1.0, 1.0, 1.0);
      this.addBox(0.9375, 0.0, 0.0, 1.0, 0.5, 0.5);
      this.rayTraceModel.addBox(0.9375, 0.0, 0.5, 1.0, 1.0, 1.0);
      this.rayTraceModel.addBox(0.9375, 0.0, 0.0, 1.0, 0.5, 0.5);
      this.addPrimitive(
         new PrimitiveQuad(
               Vec3.createVectorHelper(0.0625, 0.0, 0.0),
               Vec3.createVectorHelper(0.0625, 0.125, 0.625),
               Vec3.createVectorHelper(0.9375, 0.125, 0.625),
               Vec3.createVectorHelper(0.9375, 0.0, 0.0)
            )
            .setIconIndex(1)
            .setUVFractions(0.0625F, 0.0F, 0.9375F, 0.625F)
      );
      this.addPrimitive(
         new PrimitiveQuad(
               Vec3.createVectorHelper(0.9375, 0.0, 0.0),
               Vec3.createVectorHelper(0.9375, 0.0, 0.75),
               Vec3.createVectorHelper(0.0625, 0.0, 0.75),
               Vec3.createVectorHelper(0.0625, 0.0, 0.0)
            )
            .setIconIndex(2)
            .setUVFractions(0.0625F, 0.0F, 0.9375F, 0.75F)
      );
      this.rayTraceModel.addBox(0.0, 0.0, 0.0, 1.0, 0.015625, 0.75);
      this.addPrimitive(
         new PrimitiveQuad(
               Vec3.createVectorHelper(0.9375, 1.0, 1.0),
               Vec3.createVectorHelper(0.9375, 0.375, 0.875),
               Vec3.createVectorHelper(0.0625, 0.375, 0.875),
               Vec3.createVectorHelper(0.0625, 1.0, 1.0)
            )
            .setIconIndex(1)
            .setUVFractions(0.0625F, 0.0F, 0.9375F, 0.625F)
      );
      this.addPrimitive(
         new PrimitiveQuad(
               Vec3.createVectorHelper(0.0625, 1.0, 1.0),
               Vec3.createVectorHelper(0.0625, 0.25, 1.0),
               Vec3.createVectorHelper(0.9375, 0.25, 1.0),
               Vec3.createVectorHelper(0.9375, 1.0, 1.0)
            )
            .setIconIndex(2)
            .setUVFractions(0.0625F, 0.0F, 0.9375F, 0.75F)
      );
      this.rayTraceModel.addBox(0.0, 0.25, 0.984375, 1.0, 1.0, 1.0);
      this.addPrimitive(
         new PrimitiveQuad(
               Vec3.createVectorHelper(0.9375, 0.375, 0.875),
               Vec3.createVectorHelper(0.9375, 0.125, 0.625),
               Vec3.createVectorHelper(0.0625, 0.125, 0.625),
               Vec3.createVectorHelper(0.0625, 0.375, 0.875)
            )
            .setIconIndex(3)
            .setUVFractions(0.0625F, 0.0F, 0.9375F, 0.375F)
      );
      this.addPrimitive(
         new PrimitiveQuad(
               Vec3.createVectorHelper(0.0625, 0.25, 1.0),
               Vec3.createVectorHelper(0.0625, 0.0, 0.75),
               Vec3.createVectorHelper(0.9375, 0.0, 0.75),
               Vec3.createVectorHelper(0.9375, 0.25, 1.0)
            )
            .setIconIndex(3)
            .setUVFractions(0.0625F, 0.625F, 0.9375F, 1.0F)
      );
      this.rayTraceModel
         .addPrimitive(
            new PrimitiveQuad(
                  Vec3.createVectorHelper(0.0625, 0.25, 1.0),
                  Vec3.createVectorHelper(0.0625, 0.0, 0.75),
                  Vec3.createVectorHelper(0.9375, 0.0, 0.75),
                  Vec3.createVectorHelper(0.9375, 0.25, 1.0)
               )
               .setIconIndex(3)
               .setUVFractions(0.0625F, 0.625F, 0.9375F, 1.0F)
         );
      this.collisionModel.addBox(0.0, 0.0, 0.0, 1.0, 0.5, 1.0);
      this.collisionModel.addBox(0.0, 0.5, 0.5, 1.0, 1.0, 1.0);
   }
}
