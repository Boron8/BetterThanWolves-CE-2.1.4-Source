package net.minecraft.src;

public class RenderPig extends RenderLiving {
   public RenderPig(ModelBase var1, ModelBase var2, float var3) {
      super(var1, var3);
      this.a(var2);
   }

   protected int renderSaddledPig(EntityPig var1, int var2, float var3) {
      if (var2 == 0 && var1.getSaddled()) {
         this.a("/mob/saddle.png");
         return 1;
      } else {
         return -1;
      }
   }

   public void renderLivingPig(EntityPig var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }
}
