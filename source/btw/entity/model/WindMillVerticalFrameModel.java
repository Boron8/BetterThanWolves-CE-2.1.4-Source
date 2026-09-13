package btw.entity.model;

import btw.entity.mechanical.source.VerticalWindMillEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

@Environment(EnvType.CLIENT)
public class WindMillVerticalFrameModel extends ModelBase {
   public ModelRenderer[] components = new ModelRenderer[32];
   private static final int NUM_BLADES = 8;
   private static final int NUM_COMPONENTS = 32;
   private static final float LOCAL_PI = 3.141593F;
   private static final float SPOKES_OFFSET_FROM_CENTER = 2.0F;
   private static final int SPOKES_LENGTH = 67;
   private static final float HALF_SPOKES_LENGTH = 33.5F;
   private static final int SPOKES_WIDTH = 2;
   private static final float HALF_SPOKES_WIDTH = 1.0F;
   private static final float SPOKES_VERTICAL_OFFSET = 51.9F;
   private static final float RIM_OFFSET_FROM_CENTER = 65.9F;
   private static final int RIM_SEGMENT_LENGTH = 52;
   private static final float HALF_RIM_SEGMENT_LENGTH = 26.0F;

   public WindMillVerticalFrameModel() {
      for (int iTempBlade = 0; iTempBlade < 8; iTempBlade++) {
         this.components[iTempBlade] = new ModelRenderer(this, 0, 0);
         this.components[iTempBlade].setTextureSize(16, 16);
         this.components[iTempBlade].addBox(2.0F, -52.9F, -1.0F, 67, 2, 2);
         this.components[iTempBlade].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.components[iTempBlade].rotateAngleY = 6.283186F * iTempBlade / 8.0F;
         this.components[iTempBlade + 8] = new ModelRenderer(this, 0, 0);
         this.components[iTempBlade + 8].setTextureSize(16, 16);
         this.components[iTempBlade + 8].addBox(2.0F, 50.9F, -1.0F, 67, 2, 2);
         this.components[iTempBlade + 8].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.components[iTempBlade + 8].rotateAngleY = 6.283186F * iTempBlade / 8.0F;
         this.components[iTempBlade + 16] = new ModelRenderer(this, 0, 0);
         this.components[iTempBlade + 16].setTextureSize(16, 16);
         this.components[iTempBlade + 16].addBox(64.9F, -52.9F, -26.0F, 2, 2, 52);
         this.components[iTempBlade + 16].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.components[iTempBlade + 16].rotateAngleY = 6.283186F * iTempBlade / 8.0F + 0.39269912F;
         this.components[iTempBlade + 24] = new ModelRenderer(this, 0, 0);
         this.components[iTempBlade + 24].setTextureSize(16, 16);
         this.components[iTempBlade + 24].addBox(64.9F, 50.9F, -26.0F, 2, 2, 52);
         this.components[iTempBlade + 24].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.components[iTempBlade + 24].rotateAngleY = 6.283186F * iTempBlade / 8.0F + 0.39269912F;
      }
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
   }

   public void render(float f, float f1, float f2, float f3, float f4, float fScale, VerticalWindMillEntity windMillEnt) {
      for (int i = 0; i < 32; i++) {
         this.components[i].render(fScale);
      }
   }
}
