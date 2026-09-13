package btw.entity.model;

import btw.entity.mechanical.source.WindMillEntity;
import btw.entity.mob.SheepEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class WindMillModel extends ModelBase {
   public ModelRenderer[] windMillComponents = new ModelRenderer[8];
   private static final int NUM_WIND_MILL_COMPONENTS = 8;
   private static final float LOCAL_PI = 3.141593F;
   private static final float BLADE_OFFSET_FROM_CENTER = 15.0F;
   private static final int BLADE_LENGTH = 84;
   private static final int BLADE_WIDTH = 16;
   private static final float SHAFT_OFFSET_FROM_CENTER = 2.5F;
   private static final int SHAFT_LENGTH = 97;
   private static final int SHAFT_WIDTH = 4;

   public WindMillModel() {
      for (int i = 0; i < 4; i++) {
         this.windMillComponents[i] = new ModelRenderer(this, 0, 0);
         this.windMillComponents[i].addBox(2.5F, -2.0F, -2.0F, 97, 4, 4);
         this.windMillComponents[i].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.windMillComponents[i].rotateAngleZ = 3.141593F * (i - 4) / 2.0F;
      }

      for (int i = 4; i < 8; i++) {
         this.windMillComponents[i] = new ModelRenderer(this, 0, 15);
         this.windMillComponents[i].addBox(15.0F, 1.75F, 1.0F, 84, 16, 1);
         this.windMillComponents[i].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.windMillComponents[i].rotateAngleX = -0.26179942F;
         this.windMillComponents[i].rotateAngleZ = 3.141593F * i / 2.0F;
      }
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
   }

   public void render(float f, float f1, float f2, float f3, float f4, float f5, WindMillEntity windMillEnt) {
      for (int i = 0; i < 4; i++) {
         this.windMillComponents[i].render(f5);
      }

      float fBrightness = 1.0F;

      for (int i = 4; i < 8; i++) {
         int iBladeColor = windMillEnt.getBladeColor(i - 4);
         GL11.glColor3f(
            fBrightness * SheepEntity.pathToEntity[iBladeColor][0],
            fBrightness * SheepEntity.pathToEntity[iBladeColor][1],
            fBrightness * SheepEntity.pathToEntity[iBladeColor][2]
         );
         this.windMillComponents[i].render(f5);
      }
   }
}
