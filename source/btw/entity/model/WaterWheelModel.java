package btw.entity.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.ModelBase;
import net.minecraft.src.ModelRenderer;

@Environment(EnvType.CLIENT)
public class WaterWheelModel extends ModelBase {
   public ModelRenderer[] waterWheelComponents = new ModelRenderer[16];
   private static final int NUM_WATER_WHEEL_COMPONENTS = 16;
   private static final float LOCAL_PI = 3.141593F;
   private static final float STRUT_DIST_FROM_CENT = 30.0F;

   public WaterWheelModel() {
      for (int i = 0; i < 8; i++) {
         this.waterWheelComponents[i] = new ModelRenderer(this, 0, 0);
         this.waterWheelComponents[i].addBox(2.5F, -1.0F, -7.0F, 36, 2, 14);
         this.waterWheelComponents[i].setRotationPoint(0.0F, 0.0F, 0.0F);
         this.waterWheelComponents[i].rotateAngleZ = 3.141593F * i / 4.0F;
      }

      for (int i = 0; i < 8; i++) {
         this.waterWheelComponents[i + 8] = new ModelRenderer(this, 0, 0);
         this.waterWheelComponents[i + 8].addBox(0.0F, -1.0F, -6.0F, 23, 2, 12);
         float fRotationAngle = 0.78539824F * i;
         this.waterWheelComponents[i + 8].setRotationPoint(30.0F * MathHelper.cos(fRotationAngle), 30.0F * MathHelper.sin(fRotationAngle), 0.0F);
         this.waterWheelComponents[i + 8].rotateAngleZ = 1.9634956F + 0.78539824F * i;
      }
   }

   @Override
   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      for (int i = 0; i < 16; i++) {
         this.waterWheelComponents[i].render(f5);
      }
   }

   @Override
   public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
   }
}
