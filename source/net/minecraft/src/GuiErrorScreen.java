package net.minecraft.src;

public class GuiErrorScreen extends GuiScreen {
   private String message1;
   private String message2;

   public GuiErrorScreen(String var1, String var2) {
      this.message1 = var1;
      this.message2 = var2;
   }

   @Override
   public void initGui() {
      super.initGui();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, StatCollector.translateToLocal("gui.cancel")));
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.a(0, 0, this.width, this.height, -12574688, -11530224);
      this.a(this.fontRenderer, this.message1, this.width / 2, 90, 16777215);
      this.a(this.fontRenderer, this.message2, this.width / 2, 110, 16777215);
      super.drawScreen(var1, var2, var3);
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      this.mc.displayGuiScreen(null);
   }
}
