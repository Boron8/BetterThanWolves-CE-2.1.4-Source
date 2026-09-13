package net.minecraft.src;

public class RenderSilverfish extends RenderLiving {
   public RenderSilverfish() {
      super(new ModelSilverfish(), 0.3F);
   }

   protected float getSilverfishDeathRotation(EntitySilverfish var1) {
      return 180.0F;
   }

   public void renderSilverfish(EntitySilverfish var1, double var2, double var4, double var6, float var8, float var9) {
      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }

   protected int shouldSilverfishRenderPass(EntitySilverfish var1, int var2, float var3) {
      return -1;
   }
}
