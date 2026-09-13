package btw.client.render.entity;

import btw.entity.DynamiteEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.src.Entity;
import net.minecraft.src.Icon;
import net.minecraft.src.Item;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class DynamiteRenderer extends Render {
   @Override
   public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
      Tessellator tessellator = Tessellator.instance;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      GL11.glEnable(32826);
      GL11.glScalef(0.5F, 0.5F, 0.5F);
      this.a("/gui/items.png");
      DynamiteEntity entityDynamite = (DynamiteEntity)entity;
      Icon itemIcon = Item.itemsList[entityDynamite.itemShiftedIndex].itemIcon;
      double f2 = itemIcon.getMinU();
      double f3 = itemIcon.getMaxU();
      double f4 = itemIcon.getMinV();
      double f5 = itemIcon.getMaxV();
      float f6 = 1.0F;
      float f7 = 0.5F;
      float f8 = 0.25F;
      GL11.glRotatef(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
      GL11.glRotatef(-this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
      tessellator.startDrawingQuads();
      tessellator.setNormal(0.0F, 1.0F, 0.0F);
      tessellator.addVertexWithUV(0.0F - f7, 0.0F - f8, 0.0, f2, f5);
      tessellator.addVertexWithUV(f6 - f7, 0.0F - f8, 0.0, f3, f5);
      tessellator.addVertexWithUV(f6 - f7, 1.0F - f8, 0.0, f3, f4);
      tessellator.addVertexWithUV(0.0F - f7, 1.0F - f8, 0.0, f2, f4);
      tessellator.draw();
      GL11.glDisable(32826);
      GL11.glPopMatrix();
   }
}
