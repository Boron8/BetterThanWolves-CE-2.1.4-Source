package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiRenameWorld extends GuiScreen {
   private GuiScreen parentGuiScreen;
   private GuiTextField theGuiTextField;
   private final String worldName;

   public GuiRenameWorld(GuiScreen var1, String var2) {
      this.parentGuiScreen = var1;
      this.worldName = var2;
   }

   @Override
   public void updateScreen() {
      this.theGuiTextField.updateCursorCounter();
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("selectWorld.renameButton")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
      ISaveFormat var2 = this.mc.getSaveLoader();
      WorldInfo var3 = var2.getWorldInfo(this.worldName);
      String var4 = var3.getWorldName();
      this.theGuiTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 60, 200, 20);
      this.theGuiTextField.setFocused(true);
      this.theGuiTextField.setText(var4);
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.mc.displayGuiScreen(this.parentGuiScreen);
         } else if (var1.id == 0) {
            ISaveFormat var2 = this.mc.getSaveLoader();
            var2.renameWorld(this.worldName, this.theGuiTextField.getText().trim());
            this.mc.displayGuiScreen(this.parentGuiScreen);
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.theGuiTextField.textboxKeyTyped(var1, var2);
      ((GuiButton)this.buttonList.get(0)).enabled = this.theGuiTextField.getText().trim().length() > 0;
      if (var1 == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.theGuiTextField.mouseClicked(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("selectWorld.renameTitle"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
      this.b(this.fontRenderer, var4.translateKey("selectWorld.enterName"), this.width / 2 - 100, 47, 10526880);
      this.theGuiTextField.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }
}
