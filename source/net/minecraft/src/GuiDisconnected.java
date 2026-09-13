package net.minecraft.src;

import java.util.List;

public class GuiDisconnected extends GuiScreen {
   private String errorMessage;
   private String errorDetail;
   private Object[] field_74247_c;
   private List field_74245_d;
   private final GuiScreen field_98095_n;

   public GuiDisconnected(GuiScreen var1, String var2, String var3, Object... var4) {
      StringTranslate var5 = StringTranslate.getInstance();
      this.field_98095_n = var1;
      this.errorMessage = var5.translateKey(var2);
      this.errorDetail = var3;
      this.field_74247_c = var4;
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.toMenu")));
      if (this.field_74247_c != null) {
         this.field_74245_d = this.fontRenderer.listFormattedStringToWidth(var1.translateKeyFormat(this.errorDetail, this.field_74247_c), this.width - 50);
      } else {
         this.field_74245_d = this.fontRenderer.listFormattedStringToWidth(var1.translateKey(this.errorDetail), this.width - 50);
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(this.field_98095_n);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.a(this.fontRenderer, this.errorMessage, this.width / 2, this.height / 2 - 50, 11184810);
      int var4 = this.height / 2 - 30;
      if (this.field_74245_d != null) {
         for (String var6 : this.field_74245_d) {
            this.a(this.fontRenderer, var6, this.width / 2, var4, 16777215);
            var4 += this.fontRenderer.FONT_HEIGHT;
         }
      }

      super.drawScreen(var1, var2, var3);
   }
}
