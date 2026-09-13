package btw.entity.model;

import btw.entity.mechanical.source.VerticalWindMillEntity;
import btw.entity.mob.SheepEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class WindMillVerticalSailsModel extends ModelBase {
   public ModelRenderer[] components = new ModelRenderer[8];
   private static final int NUM_BLADES = 8;
   private static final int NUM_COMPONENTS = 8;
   private static final float LOCAL_PI = 3.141593F;
   private static final float SAIL_OFFSET_FROM_CENTER = 70.9F;
   private static final int SAIL_LENGTH = 100;
   private static final float HALF_SAIL_LENGTH = 50.0F;
   private static final int SAIL_WIDTH = 20;
   private static final float HALF_SAIL_WIDTH = 10.0F;
   private static final int SAIL_THICKNESS = 1;
   private static final float HALF_SAIL_THICKNESS = 0.5F;

   public WindMillVerticalSailsModel() {
      for (int iTempBlade = 0; iTempBlade < 8; iTempBlade++) {
         this.components[iTempBlade] = new ModelRenderer(this, 0, 0);
         this.components[iTempBlade].setTextureSize(16, 16);
         this.components[iTempBlade].addBox(70.4F, -50.0F, -20.0F, 1, 100, 20);
         this.components[iTempBlade].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.components[iTempBlade].rotateAngleY = 6.283186F * iTempBlade / 8.0F;
      }
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
   }

   public void render(float f, float f1, float f2, float f3, float f4, float fScale, VerticalWindMillEntity windMillEnt) {
      float fBrightness = 1.0F;

      for (int i = 0; i < 8; i++) {
         int iBladeColor = windMillEnt.getBladeColor(i);
         GL11.glColor3f(
            fBrightness * SheepEntity.pathToEntity[iBladeColor][0],
            fBrightness * SheepEntity.pathToEntity[iBladeColor][1],
            fBrightness * SheepEntity.pathToEntity[iBladeColor][2]
         );
         this.components[i].render(fScale);
      }
   }
}
