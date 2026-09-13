package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderBat extends RenderLiving {
   private int renderedBatSize = ((ModelBat)this.mainModel).getBatSize();

   public RenderBat() {
      super(new ModelBat(), 0.25F);
   }

   public void func_82443_a(EntityBat var1, double var2, double var4, double var6, float var8, float var9) {
      int var10 = ((ModelBat)this.mainModel).getBatSize();
      if (var10 != this.renderedBatSize) {
         this.renderedBatSize = var10;
         this.mainModel = new ModelBat();
      }

      super.doRenderLiving(var1, var2, var4, var6, var8, var9);
   }

   protected void func_82442_a(EntityBat var1, float var2) {
      GL11.glScalef(0.35F, 0.35F, 0.35F);
   }

   protected void func_82445_a(EntityBat var1, double var2, double var4, double var6) {
      super.renderLivingAt(var1, var2, var4, var6);
   }

   protected void func_82444_a(EntityBat var1, float var2, float var3, float var4) {
      if (!var1.getIsBatHanging()) {
         GL11.glTranslatef(0.0F, MathHelper.cos(var2 * 0.3F) * 0.1F, 0.0F);
      } else {
         GL11.glTranslatef(0.0F, -0.1F, 0.0F);
      }

      super.rotateCorpse(var1, var2, var3, var4);
   }
}
