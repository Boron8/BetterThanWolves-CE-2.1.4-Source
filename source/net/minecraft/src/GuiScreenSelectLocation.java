package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiScreenSelectLocation {
   private final Minecraft field_104092_f;
   private int field_104093_g;
   private int field_104105_h;
   protected int field_104098_a;
   protected int field_104096_b;
   private int field_104106_i;
   private int field_104103_j;
   protected final int field_104097_c;
   private int field_104104_k;
   private int field_104101_l;
   protected int field_104094_d;
   protected int field_104095_e;
   private float field_104102_m = -2.0F;
   private float field_104099_n;
   private float field_104100_o;
   private int field_104111_p = -1;
   private long field_104110_q = 0L;
   private boolean field_104109_r = true;
   private boolean field_104108_s;
   private int field_104107_t;

   public GuiScreenSelectLocation(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
      this.field_104092_f = var1;
      this.field_104093_g = var2;
      this.field_104105_h = var3;
      this.field_104098_a = var4;
      this.field_104096_b = var5;
      this.field_104097_c = var6;
      this.field_104103_j = 0;
      this.field_104106_i = var2;
   }

   public void func_104084_a(int var1, int var2, int var3, int var4) {
      this.field_104093_g = var1;
      this.field_104105_h = var2;
      this.field_104098_a = var3;
      this.field_104096_b = var4;
      this.field_104103_j = 0;
      this.field_104106_i = var1;
   }

   protected abstract int getSize();

   protected abstract void elementClicked(int var1, boolean var2);

   protected abstract boolean isSelected(int var1);

   protected abstract boolean func_104086_b(int var1);

   protected int getContentHeight() {
      return this.getSize() * this.field_104097_c + this.field_104107_t;
   }

   protected abstract void drawBackground();

   protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

   protected void func_104088_a(int var1, int var2, Tessellator var3) {
   }

   protected void func_104089_a(int var1, int var2) {
   }

   protected void func_104087_b(int var1, int var2) {
   }

   private void func_104091_h() {
      int var1 = this.func_104085_d();
      if (var1 < 0) {
         var1 /= 2;
      }

      if (this.field_104100_o < 0.0F) {
         this.field_104100_o = 0.0F;
      }

      if (this.field_104100_o > var1) {
         this.field_104100_o = var1;
      }
   }

   public int func_104085_d() {
      return this.getContentHeight() - (this.field_104096_b - this.field_104098_a - 4);
   }

   public void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == this.field_104104_k) {
            this.field_104100_o = this.field_104100_o - this.field_104097_c * 2 / 3;
            this.field_104102_m = -2.0F;
            this.func_104091_h();
         } else if (var1.id == this.field_104101_l) {
            this.field_104100_o = this.field_104100_o + this.field_104097_c * 2 / 3;
            this.field_104102_m = -2.0F;
            this.func_104091_h();
         }
      }
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.field_104094_d = var1;
      this.field_104095_e = var2;
      this.drawBackground();
      int var4 = this.getSize();
      int var5 = this.func_104090_g();
      int var6 = var5 + 6;
      if (Mouse.isButtonDown(0)) {
         if (this.field_104102_m == -1.0F) {
            boolean var16 = true;
            if (var2 >= this.field_104098_a && var2 <= this.field_104096_b) {
               int var8 = this.field_104093_g / 2 - 110;
               int var9 = this.field_104093_g / 2 + 110;
               int var10 = var2 - this.field_104098_a - this.field_104107_t + (int)this.field_104100_o - 4;
               int var11 = var10 / this.field_104097_c;
               if (var1 >= var8 && var1 <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
                  boolean var12 = var11 == this.field_104111_p && Minecraft.getSystemTime() - this.field_104110_q < 250L;
                  this.elementClicked(var11, var12);
                  this.field_104111_p = var11;
                  this.field_104110_q = Minecraft.getSystemTime();
               } else if (var1 >= var8 && var1 <= var9 && var10 < 0) {
                  this.func_104089_a(var1 - var8, var2 - this.field_104098_a + (int)this.field_104100_o - 4);
                  var16 = false;
               }

               if (var1 >= var5 && var1 <= var6) {
                  this.field_104099_n = -1.0F;
                  int var23 = this.func_104085_d();
                  if (var23 < 1) {
                     var23 = 1;
                  }

                  int var13 = (int)(
                     (float)((this.field_104096_b - this.field_104098_a) * (this.field_104096_b - this.field_104098_a)) / this.getContentHeight()
                  );
                  if (var13 < 32) {
                     var13 = 32;
                  }

                  if (var13 > this.field_104096_b - this.field_104098_a - 8) {
                     var13 = this.field_104096_b - this.field_104098_a - 8;
                  }

                  this.field_104099_n = this.field_104099_n / ((float)(this.field_104096_b - this.field_104098_a - var13) / var23);
               } else {
                  this.field_104099_n = 1.0F;
               }

               if (var16) {
                  this.field_104102_m = var2;
               } else {
                  this.field_104102_m = -2.0F;
               }
            } else {
               this.field_104102_m = -2.0F;
            }
         } else if (this.field_104102_m >= 0.0F) {
            this.field_104100_o = this.field_104100_o - (var2 - this.field_104102_m) * this.field_104099_n;
            this.field_104102_m = var2;
         }
      } else {
         while (!this.field_104092_f.gameSettings.touchscreen && Mouse.next()) {
            int var7 = Mouse.getEventDWheel();
            if (var7 != 0) {
               if (var7 > 0) {
                  var7 = -1;
               } else if (var7 < 0) {
                  var7 = 1;
               }

               this.field_104100_o = this.field_104100_o + var7 * this.field_104097_c / 2;
            }
         }

         this.field_104102_m = -1.0F;
      }

      this.func_104091_h();
      GL11.glDisable(2896);
      GL11.glDisable(2912);
      Tessellator var17 = Tessellator.instance;
      this.field_104092_f.renderEngine.bindTexture("/gui/background.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float var18 = 32.0F;
      var17.startDrawingQuads();
      var17.setColorOpaque_I(2105376);
      var17.addVertexWithUV(
         this.field_104103_j, this.field_104096_b, 0.0, this.field_104103_j / var18, (this.field_104096_b + (int)this.field_104100_o) / var18
      );
      var17.addVertexWithUV(
         this.field_104106_i, this.field_104096_b, 0.0, this.field_104106_i / var18, (this.field_104096_b + (int)this.field_104100_o) / var18
      );
      var17.addVertexWithUV(
         this.field_104106_i, this.field_104098_a, 0.0, this.field_104106_i / var18, (this.field_104098_a + (int)this.field_104100_o) / var18
      );
      var17.addVertexWithUV(
         this.field_104103_j, this.field_104098_a, 0.0, this.field_104103_j / var18, (this.field_104098_a + (int)this.field_104100_o) / var18
      );
      var17.draw();
      int var19 = this.field_104093_g / 2 - 92 - 16;
      int var20 = this.field_104098_a + 4 - (int)this.field_104100_o;
      if (this.field_104108_s) {
         this.func_104088_a(var19, var20, var17);
      }

      for (int var21 = 0; var21 < var4; var21++) {
         int var24 = var20 + var21 * this.field_104097_c + this.field_104107_t;
         int var26 = this.field_104097_c - 4;
         if (var24 <= this.field_104096_b && var24 + var26 >= this.field_104098_a) {
            if (this.field_104109_r && this.func_104086_b(var21)) {
               int var14 = this.field_104093_g / 2 - 110;
               int var15 = this.field_104093_g / 2 + 110;
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glDisable(3553);
               var17.startDrawingQuads();
               var17.setColorOpaque_I(0);
               var17.addVertexWithUV(var14, var24 + var26 + 2, 0.0, 0.0, 1.0);
               var17.addVertexWithUV(var15, var24 + var26 + 2, 0.0, 1.0, 1.0);
               var17.addVertexWithUV(var15, var24 - 2, 0.0, 1.0, 0.0);
               var17.addVertexWithUV(var14, var24 - 2, 0.0, 0.0, 0.0);
               var17.draw();
               GL11.glEnable(3553);
            }

            if (this.field_104109_r && this.isSelected(var21)) {
               int var28 = this.field_104093_g / 2 - 110;
               int var30 = this.field_104093_g / 2 + 110;
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glDisable(3553);
               var17.startDrawingQuads();
               var17.setColorOpaque_I(8421504);
               var17.addVertexWithUV(var28, var24 + var26 + 2, 0.0, 0.0, 1.0);
               var17.addVertexWithUV(var30, var24 + var26 + 2, 0.0, 1.0, 1.0);
               var17.addVertexWithUV(var30, var24 - 2, 0.0, 1.0, 0.0);
               var17.addVertexWithUV(var28, var24 - 2, 0.0, 0.0, 0.0);
               var17.setColorOpaque_I(0);
               var17.addVertexWithUV(var28 + 1, var24 + var26 + 1, 0.0, 0.0, 1.0);
               var17.addVertexWithUV(var30 - 1, var24 + var26 + 1, 0.0, 1.0, 1.0);
               var17.addVertexWithUV(var30 - 1, var24 - 1, 0.0, 1.0, 0.0);
               var17.addVertexWithUV(var28 + 1, var24 - 1, 0.0, 0.0, 0.0);
               var17.draw();
               GL11.glEnable(3553);
            }

            this.drawSlot(var21, var19, var24, var26, var17);
         }
      }

      GL11.glDisable(2929);
      byte var22 = 4;
      this.func_104083_b(0, this.field_104098_a, 255, 255);
      this.func_104083_b(this.field_104096_b, this.field_104105_h, 255, 255);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      var17.startDrawingQuads();
      var17.setColorRGBA_I(0, 0);
      var17.addVertexWithUV(this.field_104103_j, this.field_104098_a + var22, 0.0, 0.0, 1.0);
      var17.addVertexWithUV(this.field_104106_i, this.field_104098_a + var22, 0.0, 1.0, 1.0);
      var17.setColorRGBA_I(0, 255);
      var17.addVertexWithUV(this.field_104106_i, this.field_104098_a, 0.0, 1.0, 0.0);
      var17.addVertexWithUV(this.field_104103_j, this.field_104098_a, 0.0, 0.0, 0.0);
      var17.draw();
      var17.startDrawingQuads();
      var17.setColorRGBA_I(0, 255);
      var17.addVertexWithUV(this.field_104103_j, this.field_104096_b, 0.0, 0.0, 1.0);
      var17.addVertexWithUV(this.field_104106_i, this.field_104096_b, 0.0, 1.0, 1.0);
      var17.setColorRGBA_I(0, 0);
      var17.addVertexWithUV(this.field_104106_i, this.field_104096_b - var22, 0.0, 1.0, 0.0);
      var17.addVertexWithUV(this.field_104103_j, this.field_104096_b - var22, 0.0, 0.0, 0.0);
      var17.draw();
      int var25 = this.func_104085_d();
      if (var25 > 0) {
         int var27 = (this.field_104096_b - this.field_104098_a) * (this.field_104096_b - this.field_104098_a) / this.getContentHeight();
         if (var27 < 32) {
            var27 = 32;
         }

         if (var27 > this.field_104096_b - this.field_104098_a - 8) {
            var27 = this.field_104096_b - this.field_104098_a - 8;
         }

         int var29 = (int)this.field_104100_o * (this.field_104096_b - this.field_104098_a - var27) / var25 + this.field_104098_a;
         if (var29 < this.field_104098_a) {
            var29 = this.field_104098_a;
         }

         var17.startDrawingQuads();
         var17.setColorRGBA_I(0, 255);
         var17.addVertexWithUV(var5, this.field_104096_b, 0.0, 0.0, 1.0);
         var17.addVertexWithUV(var6, this.field_104096_b, 0.0, 1.0, 1.0);
         var17.addVertexWithUV(var6, this.field_104098_a, 0.0, 1.0, 0.0);
         var17.addVertexWithUV(var5, this.field_104098_a, 0.0, 0.0, 0.0);
         var17.draw();
         var17.startDrawingQuads();
         var17.setColorRGBA_I(8421504, 255);
         var17.addVertexWithUV(var5, var29 + var27, 0.0, 0.0, 1.0);
         var17.addVertexWithUV(var6, var29 + var27, 0.0, 1.0, 1.0);
         var17.addVertexWithUV(var6, var29, 0.0, 1.0, 0.0);
         var17.addVertexWithUV(var5, var29, 0.0, 0.0, 0.0);
         var17.draw();
         var17.startDrawingQuads();
         var17.setColorRGBA_I(12632256, 255);
         var17.addVertexWithUV(var5, var29 + var27 - 1, 0.0, 0.0, 1.0);
         var17.addVertexWithUV(var6 - 1, var29 + var27 - 1, 0.0, 1.0, 1.0);
         var17.addVertexWithUV(var6 - 1, var29, 0.0, 1.0, 0.0);
         var17.addVertexWithUV(var5, var29, 0.0, 0.0, 0.0);
         var17.draw();
      }

      this.func_104087_b(var1, var2);
      GL11.glEnable(3553);
      GL11.glShadeModel(7424);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
   }

   protected int func_104090_g() {
      return this.field_104093_g / 2 + 124;
   }

   private void func_104083_b(int var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      this.field_104092_f.renderEngine.bindTexture("/gui/background.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float var6 = 32.0F;
      var5.startDrawingQuads();
      var5.setColorRGBA_I(4210752, var4);
      var5.addVertexWithUV(0.0, var2, 0.0, 0.0, var2 / var6);
      var5.addVertexWithUV(this.field_104093_g, var2, 0.0, this.field_104093_g / var6, var2 / var6);
      var5.setColorRGBA_I(4210752, var3);
      var5.addVertexWithUV(this.field_104093_g, var1, 0.0, this.field_104093_g / var6, var1 / var6);
      var5.addVertexWithUV(0.0, var1, 0.0, 0.0, var1 / var6);
      var5.draw();
   }
}
