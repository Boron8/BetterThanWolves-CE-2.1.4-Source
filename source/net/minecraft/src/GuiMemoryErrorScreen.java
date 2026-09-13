package net.minecraft.src;

public class GuiMemoryErrorScreen extends GuiScreen {
   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonList.clear();
      this.buttonList.add(new GuiSmallButton(0, this.width / 2 - 155, this.height / 4 + 120 + 12, var1.translateKey("gui.toMenu")));
      this.buttonList.add(new GuiSmallButton(1, this.width / 2 - 155 + 160, this.height / 4 + 120 + 12, var1.translateKey("menu.quit")));
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 0) {
         this.mc.displayGuiScreen(new GuiMainMenu());
      } else if (var1.id == 1) {
         this.mc.shutdown();
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.a(this.fontRenderer, "Out of memory!", this.width / 2, this.height / 4 - 60 + 20, 16777215);
      this.b(this.fontRenderer, "Minecraft has run out of memory.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 0, 10526880);
      this.b(this.fontRenderer, "This could be caused by a bug in the game or by the", this.width / 2 - 140, this.height / 4 - 60 + 60 + 18, 10526880);
      this.b(this.fontRenderer, "Java Virtual Machine not being allocated enough", this.width / 2 - 140, this.height / 4 - 60 + 60 + 27, 10526880);
      this.b(this.fontRenderer, "memory. If you are playing in a web browser, try", this.width / 2 - 140, this.height / 4 - 60 + 60 + 36, 10526880);
      this.b(this.fontRenderer, "downloading the game and playing it offline.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 45, 10526880);
      this.b(this.fontRenderer, "To prevent level corruption, the current game has quit.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 63, 10526880);
      this.b(this.fontRenderer, "We've tried to free up enough memory to let you go back to", this.width / 2 - 140, this.height / 4 - 60 + 60 + 81, 10526880);
      this.b(
         this.fontRenderer, "the main menu and back to playing, but this may not have worked.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 90, 10526880
      );
      this.b(this.fontRenderer, "Please restart the game if you see this message again.", this.width / 2 - 140, this.height / 4 - 60 + 60 + 99, 10526880);
      super.drawScreen(var1, var2, var3);
   }
}
