package btw.client.render.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class SpiderWebRenderer extends Render {
   private static final int ICON_INDEX = 111;

   @Override
   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      this.a("/btwmodtex/fcSpiderSpit.png");
      Tessellator tessellator = Tessellator.instance;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      GL11.glEnable(32826);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      float fUMin = 0.0F;
      float fUMax = 1.0F;
      float fVMin = 0.0F;
      float fVMax = 1.0F;
      float fXOffset = 0.25F;
      float fYOffset = 0.25F;
      tessellator.addVertexWithUV(0.0F - fXOffset, 0.0F - fYOffset, 0.0, fUMin, fVMax);
      tessellator.addVertexWithUV(1.0F - fXOffset, 0.0F - fYOffset, 0.0, fUMax, fVMax);
      tessellator.addVertexWithUV(1.0F - fXOffset, 1.0F - fYOffset, 0.0, fUMax, fVMin);
      tessellator.addVertexWithUV(0.0F - fXOffset, 1.0F - fYOffset, 0.0, fUMin, fVMin);
      tessellator.draw();
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }
}
