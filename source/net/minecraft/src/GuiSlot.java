package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public abstract class GuiSlot {
   private final Minecraft mc;
   private int width;
   private int height;
   protected int top;
   protected int bottom;
   private int right;
   private int left;
   protected final int slotHeight;
   private int scrollUpButtonID;
   private int scrollDownButtonID;
   protected int mouseX;
   protected int mouseY;
   private float initialClickY = -2.0F;
   private float scrollMultiplier;
   private float amountScrolled;
   private int selectedElement = -1;
   private long lastClicked = 0L;
   private boolean showSelectionBox = true;
   private boolean field_77243_s;
   private int field_77242_t;

   public GuiSlot(Minecraft var1, int var2, int var3, int var4, int var5, int var6) {
      this.mc = var1;
      this.width = var2;
      this.height = var3;
      this.top = var4;
      this.bottom = var5;
      this.slotHeight = var6;
      this.left = 0;
      this.right = var2;
   }

   public void func_77207_a(int var1, int var2, int var3, int var4) {
      this.width = var1;
      this.height = var2;
      this.top = var3;
      this.bottom = var4;
      this.left = 0;
      this.right = var1;
   }

   public void setShowSelectionBox(boolean var1) {
      this.showSelectionBox = var1;
   }

   protected void func_77223_a(boolean var1, int var2) {
      this.field_77243_s = var1;
      this.field_77242_t = var2;
      if (!var1) {
         this.field_77242_t = 0;
      }
   }

   protected abstract int getSize();

   protected abstract void elementClicked(int var1, boolean var2);

   protected abstract boolean isSelected(int var1);

   protected int getContentHeight() {
      return this.getSize() * this.slotHeight + this.field_77242_t;
   }

   protected abstract void drawBackground();

   protected abstract void drawSlot(int var1, int var2, int var3, int var4, Tessellator var5);

   protected void func_77222_a(int var1, int var2, Tessellator var3) {
   }

   protected void func_77224_a(int var1, int var2) {
   }

   protected void func_77215_b(int var1, int var2) {
   }

   public int func_77210_c(int var1, int var2) {
      int var3 = this.width / 2 - 110;
      int var4 = this.width / 2 + 110;
      int var5 = var2 - this.top - this.field_77242_t + (int)this.amountScrolled - 4;
      int var6 = var5 / this.slotHeight;
      return var1 >= var3 && var1 <= var4 && var6 >= 0 && var5 >= 0 && var6 < this.getSize() ? var6 : -1;
   }

   public void registerScrollButtons(List var1, int var2, int var3) {
      this.scrollUpButtonID = var2;
      this.scrollDownButtonID = var3;
   }

   private void bindAmountScrolled() {
      int var1 = this.func_77209_d();
      if (var1 < 0) {
         var1 /= 2;
      }

      if (this.amountScrolled < 0.0F) {
         this.amountScrolled = 0.0F;
      }

      if (this.amountScrolled > var1) {
         this.amountScrolled = var1;
      }
   }

   public int func_77209_d() {
      return this.getContentHeight() - (this.bottom - this.top - 4);
   }

   public void func_77208_b(int var1) {
      this.amountScrolled += var1;
      this.bindAmountScrolled();
      this.initialClickY = -2.0F;
   }

   public void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == this.scrollUpButtonID) {
            this.amountScrolled = this.amountScrolled - this.slotHeight * 2 / 3;
            this.initialClickY = -2.0F;
            this.bindAmountScrolled();
         } else if (var1.id == this.scrollDownButtonID) {
            this.amountScrolled = this.amountScrolled + this.slotHeight * 2 / 3;
            this.initialClickY = -2.0F;
            this.bindAmountScrolled();
         }
      }
   }

   public void drawScreen(int var1, int var2, float var3) {
      this.mouseX = var1;
      this.mouseY = var2;
      this.drawBackground();
      int var4 = this.getSize();
      int var5 = this.getScrollBarX();
      int var6 = var5 + 6;
      if (Mouse.isButtonDown(0)) {
         if (this.initialClickY == -1.0F) {
            boolean var16 = true;
            if (var2 >= this.top && var2 <= this.bottom) {
               int var8 = this.width / 2 - 110;
               int var9 = this.width / 2 + 110;
               int var10 = var2 - this.top - this.field_77242_t + (int)this.amountScrolled - 4;
               int var11 = var10 / this.slotHeight;
               if (var1 >= var8 && var1 <= var9 && var11 >= 0 && var10 >= 0 && var11 < var4) {
                  boolean var12 = var11 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                  this.elementClicked(var11, var12);
                  this.selectedElement = var11;
                  this.lastClicked = Minecraft.getSystemTime();
               } else if (var1 >= var8 && var1 <= var9 && var10 < 0) {
                  this.func_77224_a(var1 - var8, var2 - this.top + (int)this.amountScrolled - 4);
                  var16 = false;
               }

               if (var1 >= var5 && var1 <= var6) {
                  this.scrollMultiplier = -1.0F;
                  int var23 = this.func_77209_d();
                  if (var23 < 1) {
                     var23 = 1;
                  }

                  int var13 = (int)((float)((this.bottom - this.top) * (this.bottom - this.top)) / this.getContentHeight());
                  if (var13 < 32) {
                     var13 = 32;
                  }

                  if (var13 > this.bottom - this.top - 8) {
                     var13 = this.bottom - this.top - 8;
                  }

                  this.scrollMultiplier = this.scrollMultiplier / ((float)(this.bottom - this.top - var13) / var23);
               } else {
                  this.scrollMultiplier = 1.0F;
               }

               if (var16) {
                  this.initialClickY = var2;
               } else {
                  this.initialClickY = -2.0F;
               }
            } else {
               this.initialClickY = -2.0F;
            }
         } else if (this.initialClickY >= 0.0F) {
            this.amountScrolled = this.amountScrolled - (var2 - this.initialClickY) * this.scrollMultiplier;
            this.initialClickY = var2;
         }
      } else {
         while (!this.mc.gameSettings.touchscreen && Mouse.next()) {
            int var7 = Mouse.getEventDWheel();
            if (var7 != 0) {
               if (var7 > 0) {
                  var7 = -1;
               } else if (var7 < 0) {
                  var7 = 1;
               }

               this.amountScrolled = this.amountScrolled + var7 * this.slotHeight / 2;
            }
         }

         this.initialClickY = -1.0F;
      }

      this.bindAmountScrolled();
      GL11.glDisable(2896);
      GL11.glDisable(2912);
      Tessellator var17 = Tessellator.instance;
      this.mc.renderEngine.bindTexture("/gui/background.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float var18 = 32.0F;
      var17.startDrawingQuads();
      var17.setColorOpaque_I(2105376);
      var17.addVertexWithUV(this.left, this.bottom, 0.0, this.left / var18, (this.bottom + (int)this.amountScrolled) / var18);
      var17.addVertexWithUV(this.right, this.bottom, 0.0, this.right / var18, (this.bottom + (int)this.amountScrolled) / var18);
      var17.addVertexWithUV(this.right, this.top, 0.0, this.right / var18, (this.top + (int)this.amountScrolled) / var18);
      var17.addVertexWithUV(this.left, this.top, 0.0, this.left / var18, (this.top + (int)this.amountScrolled) / var18);
      var17.draw();
      int var19 = this.width / 2 - 92 - 16;
      int var20 = this.top + 4 - (int)this.amountScrolled;
      if (this.field_77243_s) {
         this.func_77222_a(var19, var20, var17);
      }

      for (int var21 = 0; var21 < var4; var21++) {
         int var24 = var20 + var21 * this.slotHeight + this.field_77242_t;
         int var26 = this.slotHeight - 4;
         if (var24 <= this.bottom && var24 + var26 >= this.top) {
            if (this.showSelectionBox && this.isSelected(var21)) {
               int var14 = this.width / 2 - 110;
               int var15 = this.width / 2 + 110;
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glDisable(3553);
               var17.startDrawingQuads();
               var17.setColorOpaque_I(8421504);
               var17.addVertexWithUV(var14, var24 + var26 + 2, 0.0, 0.0, 1.0);
               var17.addVertexWithUV(var15, var24 + var26 + 2, 0.0, 1.0, 1.0);
               var17.addVertexWithUV(var15, var24 - 2, 0.0, 1.0, 0.0);
               var17.addVertexWithUV(var14, var24 - 2, 0.0, 0.0, 0.0);
               var17.setColorOpaque_I(0);
               var17.addVertexWithUV(var14 + 1, var24 + var26 + 1, 0.0, 0.0, 1.0);
               var17.addVertexWithUV(var15 - 1, var24 + var26 + 1, 0.0, 1.0, 1.0);
               var17.addVertexWithUV(var15 - 1, var24 - 1, 0.0, 1.0, 0.0);
               var17.addVertexWithUV(var14 + 1, var24 - 1, 0.0, 0.0, 0.0);
               var17.draw();
               GL11.glEnable(3553);
            }

            this.drawSlot(var21, var19, var24, var26, var17);
         }
      }

      GL11.glDisable(2929);
      byte var22 = 4;
      this.overlayBackground(0, this.top, 255, 255);
      this.overlayBackground(this.bottom, this.height, 255, 255);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      GL11.glDisable(3008);
      GL11.glShadeModel(7425);
      GL11.glDisable(3553);
      var17.startDrawingQuads();
      var17.setColorRGBA_I(0, 0);
      var17.addVertexWithUV(this.left, this.top + var22, 0.0, 0.0, 1.0);
      var17.addVertexWithUV(this.right, this.top + var22, 0.0, 1.0, 1.0);
      var17.setColorRGBA_I(0, 255);
      var17.addVertexWithUV(this.right, this.top, 0.0, 1.0, 0.0);
      var17.addVertexWithUV(this.left, this.top, 0.0, 0.0, 0.0);
      var17.draw();
      var17.startDrawingQuads();
      var17.setColorRGBA_I(0, 255);
      var17.addVertexWithUV(this.left, this.bottom, 0.0, 0.0, 1.0);
      var17.addVertexWithUV(this.right, this.bottom, 0.0, 1.0, 1.0);
      var17.setColorRGBA_I(0, 0);
      var17.addVertexWithUV(this.right, this.bottom - var22, 0.0, 1.0, 0.0);
      var17.addVertexWithUV(this.left, this.bottom - var22, 0.0, 0.0, 0.0);
      var17.draw();
      int var25 = this.func_77209_d();
      if (var25 > 0) {
         int var27 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
         if (var27 < 32) {
            var27 = 32;
         }

         if (var27 > this.bottom - this.top - 8) {
            var27 = this.bottom - this.top - 8;
         }

         int var28 = (int)this.amountScrolled * (this.bottom - this.top - var27) / var25 + this.top;
         if (var28 < this.top) {
            var28 = this.top;
         }

         var17.startDrawingQuads();
         var17.setColorRGBA_I(0, 255);
         var17.addVertexWithUV(var5, this.bottom, 0.0, 0.0, 1.0);
         var17.addVertexWithUV(var6, this.bottom, 0.0, 1.0, 1.0);
         var17.addVertexWithUV(var6, this.top, 0.0, 1.0, 0.0);
         var17.addVertexWithUV(var5, this.top, 0.0, 0.0, 0.0);
         var17.draw();
         var17.startDrawingQuads();
         var17.setColorRGBA_I(8421504, 255);
         var17.addVertexWithUV(var5, var28 + var27, 0.0, 0.0, 1.0);
         var17.addVertexWithUV(var6, var28 + var27, 0.0, 1.0, 1.0);
         var17.addVertexWithUV(var6, var28, 0.0, 1.0, 0.0);
         var17.addVertexWithUV(var5, var28, 0.0, 0.0, 0.0);
         var17.draw();
         var17.startDrawingQuads();
         var17.setColorRGBA_I(12632256, 255);
         var17.addVertexWithUV(var5, var28 + var27 - 1, 0.0, 0.0, 1.0);
         var17.addVertexWithUV(var6 - 1, var28 + var27 - 1, 0.0, 1.0, 1.0);
         var17.addVertexWithUV(var6 - 1, var28, 0.0, 1.0, 0.0);
         var17.addVertexWithUV(var5, var28, 0.0, 0.0, 0.0);
         var17.draw();
      }

      this.func_77215_b(var1, var2);
      GL11.glEnable(3553);
      GL11.glShadeModel(7424);
      GL11.glEnable(3008);
      GL11.glDisable(3042);
   }

   protected int getScrollBarX() {
      return this.width / 2 + 124;
   }

   private void overlayBackground(int var1, int var2, int var3, int var4) {
      Tessellator var5 = Tessellator.instance;
      this.mc.renderEngine.bindTexture("/gui/background.png");
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      float var6 = 32.0F;
      var5.startDrawingQuads();
      var5.setColorRGBA_I(4210752, var4);
      var5.addVertexWithUV(0.0, var2, 0.0, 0.0, var2 / var6);
      var5.addVertexWithUV(this.width, var2, 0.0, this.width / var6, var2 / var6);
      var5.setColorRGBA_I(4210752, var3);
      var5.addVertexWithUV(this.width, var1, 0.0, this.width / var6, var1 / var6);
      var5.addVertexWithUV(0.0, var1, 0.0, 0.0, var1 / var6);
      var5.draw();
   }
}
