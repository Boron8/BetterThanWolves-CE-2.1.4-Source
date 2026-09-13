package btw.entity.model;

import btw.entity.mechanical.source.VerticalWindMillEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

@Environment(EnvType.CLIENT)
public class WindMillVerticalShaftsModel extends ModelBase {
   public ModelRenderer[] components = new ModelRenderer[8];
   private static final int NUM_BLADES = 8;
   private static final int NUM_COMPONENTS = 8;
   private static final float LOCAL_PI = 3.141593F;
   private static final float SHAFT_OFFSET_FROM_CENTER = 70.4F;
   private static final int SHAFT_LENGTH = 108;
   private static final float SHAFT_HALF_LENGTH = 54.0F;
   private static final int SHAFT_WIDTH = 4;
   private static final float HALF_SHAFT_WIDTH = 2.0F;

   public WindMillVerticalShaftsModel() {
      for (int iTempBlade = 0; iTempBlade < 8; iTempBlade++) {
         this.components[iTempBlade] = new ModelRenderer(this, 0, 0);
         this.components[iTempBlade].setTextureSize(16, 16);
         this.components[iTempBlade].addBox(68.4F, -54.0F, -2.0F, 4, 108, 4);
         this.components[iTempBlade].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.components[iTempBlade].rotateAngleY = 6.283186F * iTempBlade / 8.0F;
      }
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
   }

   public void render(float f, float f1, float f2, float f3, float f4, float fScale, VerticalWindMillEntity windMillEnt) {
      for (int i = 0; i < 8; i++) {
         this.components[i].render(fScale);
      }
   }
}
