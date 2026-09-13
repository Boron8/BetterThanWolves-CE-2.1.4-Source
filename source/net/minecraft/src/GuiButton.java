package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiButton extends Gui {
   protected int width = 200;
   protected int height = 20;
   public int xPosition;
   public int yPosition;
   public String displayString;
   public int id;
   public boolean enabled = true;
   public boolean drawButton = true;
   protected boolean field_82253_i;

   public GuiButton(int var1, int var2, int var3, String var4) {
      this(var1, var2, var3, 200, 20, var4);
   }

   public GuiButton(int var1, int var2, int var3, int var4, int var5, String var6) {
      this.id = var1;
      this.xPosition = var2;
      this.yPosition = var3;
      this.width = var4;
      this.height = var5;
      this.displayString = var6;
   }

   protected int getHoverState(boolean var1) {
      byte var2 = 1;
      if (!this.enabled) {
         var2 = 0;
      } else if (var1) {
         var2 = 2;
      }

      return var2;
   }

   public void drawButton(Minecraft var1, int var2, int var3) {
      if (this.drawButton) {
         FontRenderer var4 = var1.fontRenderer;
         var1.renderEngine.bindTexture("/gui/gui.png");
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         this.field_82253_i = var2 >= this.xPosition && var3 >= this.yPosition && var2 < this.xPosition + this.width && var3 < this.yPosition + this.height;
         int var5 = this.getHoverState(this.field_82253_i);
         this.b(this.xPosition, this.yPosition, 0, 46 + var5 * 20, this.width / 2, this.height);
         this.b(this.xPosition + this.width / 2, this.yPosition, 200 - this.width / 2, 46 + var5 * 20, this.width / 2, this.height);
         this.mouseDragged(var1, var2, var3);
         int var6 = 14737632;
         if (!this.enabled) {
            var6 = -6250336;
         } else if (this.field_82253_i) {
            var6 = 16777120;
         }

         this.a(var4, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, var6);
      }
   }

   protected void mouseDragged(Minecraft var1, int var2, int var3) {
   }

   public void mouseReleased(int var1, int var2) {
   }

   public boolean mousePressed(Minecraft var1, int var2, int var3) {
      return this.enabled
         && this.drawButton
         && var2 >= this.xPosition
         && var3 >= this.yPosition
         && var2 < this.xPosition + this.width
         && var3 < this.yPosition + this.height;
   }

   public boolean func_82252_a() {
      return this.field_82253_i;
   }

   public void func_82251_b(int var1, int var2) {
   }
}
