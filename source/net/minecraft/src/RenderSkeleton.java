package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderSkeleton extends RenderBiped {
   public RenderSkeleton() {
      super(new ModelSkeleton(), 0.5F);
   }

   protected void func_82438_a(EntitySkeleton var1, float var2) {
      if (var1.getSkeletonType() == 1) {
         GL11.glScalef(1.2F, 1.2F, 1.2F);
      }
   }

   @Override
   protected void func_82422_c() {
      GL11.glTranslatef(0.09375F, 0.1875F, 0.0F);
   }
}
