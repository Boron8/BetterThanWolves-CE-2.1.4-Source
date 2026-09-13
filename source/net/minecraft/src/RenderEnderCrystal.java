package net.minecraft.src;

import org.lwjgl.opengl.GL11;

public class RenderEnderCrystal extends Render {
   private int field_76996_a = -1;
   private ModelBase field_76995_b;

   public RenderEnderCrystal() {
      this.shadowSize = 0.5F;
   }

   public void doRenderEnderCrystal(EntityEnderCrystal var1, double var2, double var4, double var6, float var8, float var9) {
      if (this.field_76996_a != 1) {
         this.field_76995_b = new ModelEnderCrystal(0.0F, true);
         this.field_76996_a = 1;
      }

      float var10 = var1.innerRotation + var9;
      GL11.glPushMatrix();
      GL11.glTranslatef((float)var2, (float)var4, (float)var6);
      this.a("/mob/enderdragon/crystal.png");
      float var11 = MathHelper.sin(var10 * 0.2F) / 2.0F + 0.5F;
      var11 = var11 * var11 + var11;
      this.field_76995_b.render(var1, 0.0F, var10 * 3.0F, var11 * 0.2F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
   }
}
