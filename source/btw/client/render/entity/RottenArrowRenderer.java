package btw.client.render.entity;

import btw.entity.RottenArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RottenArrowRenderer extends Render {
   @Override
   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.renderArrow((RottenArrowEntity)entity, d, d1, d2, f, f1);
   }

   public void renderArrow(RottenArrowEntity entityarrow, double d, double d1, double d2, float f, float f1) {
      if (entityarrow.prevRotationYaw != 0.0F || entityarrow.prevRotationPitch != 0.0F) {
         this.a("/btwmodtex/fcrotten.png");
         GL11.glPushMatrix();
         GL11.glTranslatef((float)d, (float)d1, (float)d2);
         GL11.glRotatef(entityarrow.prevRotationYaw + (entityarrow.rotationYaw - entityarrow.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
         GL11.glRotatef(entityarrow.prevRotationPitch + (entityarrow.rotationPitch - entityarrow.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
         Tessellator tessellator = Tessellator.instance;
         int i = 0;
         float f2 = 0.0F;
         float f3 = 0.5F;
         float f4 = (0 + i * 10) / 32.0F;
         float f5 = (5 + i * 10) / 32.0F;
         float f6 = 0.0F;
         float f7 = 0.15625F;
         float f8 = (5 + i * 10) / 32.0F;
         float f9 = (10 + i * 10) / 32.0F;
         float f10 = 0.05625F;
         GL11.glEnable(32826);
         float f11 = entityarrow.arrowShake - f1;
         if (f11 > 0.0F) {
            float f12 = -MathHelper.sin(f11 * 3.0F) * f11;
            GL11.glRotatef(f12, 0.0F, 0.0F, 1.0F);
         }

         GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
         GL11.glScalef(f10, f10, f10);
         GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
         GL11.glNormal3f(f10, 0.0F, 0.0F);
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV(-7.0, -2.0, -2.0, f6, f8);
         tessellator.addVertexWithUV(-7.0, -2.0, 2.0, f7, f8);
         tessellator.addVertexWithUV(-7.0, 2.0, 2.0, f7, f9);
         tessellator.addVertexWithUV(-7.0, 2.0, -2.0, f6, f9);
         tessellator.draw();
         GL11.glNormal3f(-f10, 0.0F, 0.0F);
         tessellator.startDrawingQuads();
         tessellator.addVertexWithUV(-7.0, 2.0, -2.0, f6, f8);
         tessellator.addVertexWithUV(-7.0, 2.0, 2.0, f7, f8);
         tessellator.addVertexWithUV(-7.0, -2.0, 2.0, f7, f9);
         tessellator.addVertexWithUV(-7.0, -2.0, -2.0, f6, f9);
         tessellator.draw();

         for (int j = 0; j < 4; j++) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0, -2.0, 0.0, f2, f4);
            tessellator.addVertexWithUV(8.0, -2.0, 0.0, f3, f4);
            tessellator.addVertexWithUV(8.0, 2.0, 0.0, f3, f5);
            tessellator.addVertexWithUV(-8.0, 2.0, 0.0, f2, f5);
            tessellator.draw();
         }

         GL11.glDisable(32826);
         GL11.glPopMatrix();
      }
   }
}
