package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderOcelot extends RenderLiving {
   public RenderOcelot(ModelBase var1, float var2) {
      super(var1, var2);
   }

   public void renderLivingOcelot(EntityOcelot var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }

   protected void preRenderOcelot(EntityOcelot var1, float var2) {
      super.preRenderCallback(var1, var2);
      if (var1.m()) {
         GL11.glScalef(0.8F, 0.8F, 0.8F);
      }
   }
}
