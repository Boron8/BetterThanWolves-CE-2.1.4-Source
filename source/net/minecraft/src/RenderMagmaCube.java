package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class RenderMagmaCube extends RenderLiving {
   private int field_77120_a = ((ModelMagmaCube)this.mainModel).func_78107_a();

   public RenderMagmaCube() {
      super(new ModelMagmaCube(), 0.25F);
   }

   public void renderMagmaCube(EntityMagmaCube var1, double var2, double var4, double var6, float var8, float var9) {
      int var10 = ((ModelMagmaCube)this.mainModel).func_78107_a();
      if (var10 != this.field_77120_a) {
         this.field_77120_a = var10;
         this.mainModel = new ModelMagmaCube();
         Minecraft.getMinecraft().getLogAgent().logInfo("Loaded new lava slime model");
      }

      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }

   protected void scaleMagmaCube(EntityMagmaCube var1, float var2) {
      int var3 = var1.p();
      float var4 = (var1.field_70812_c + (var1.field_70811_b - var1.field_70812_c) * var2) / (var3 * 0.5F + 1.0F);
      float var5 = 1.0F / (var4 + 1.0F);
      float var6 = var3;
      GL11.glScalef(var5 * var6, 1.0F / var5 * var6, var5 * var6);
   }
}
