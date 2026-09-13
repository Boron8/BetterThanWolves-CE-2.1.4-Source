package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiScreenServerList extends GuiScreen {
   private final GuiScreen guiScreen;
   private final ServerData theServerData;
   private GuiTextField serverTextField;

   public GuiScreenServerList(GuiScreen var1, ServerData var2) {
      this.guiScreen = var1;
      this.theServerData = var2;
   }

   @Override
   public void updateScreen() {
      this.serverTextField.updateCursorCounter();
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("selectServer.select")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
      this.serverTextField = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
      this.serverTextField.setMaxStringLength(128);
      this.serverTextField.setFocused(true);
      this.serverTextField.setText(this.mc.gameSettings.lastServer);
      ((GuiButton)this.buttonList.get(0)).enabled = this.serverTextField.getText().length() > 0 && this.serverTextField.getText().split(":").length > 0;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
      this.mc.gameSettings.lastServer = this.serverTextField.getText();
      this.mc.gameSettings.saveOptions();
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.guiScreen.confirmClicked(false, 0);
         } else if (var1.id == 0) {
            this.theServerData.serverIP = this.serverTextField.getText();
            this.guiScreen.confirmClicked(true, 0);
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      if (this.serverTextField.textboxKeyTyped(var1, var2)) {
         ((GuiButton)this.buttonList.get(0)).enabled = this.serverTextField.getText().length() > 0 && this.serverTextField.getText().split(":").length > 0;
      } else if (var2 == 28) {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.serverTextField.mouseClicked(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("selectServer.direct"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
      this.b(this.fontRenderer, var4.translateKey("addServer.enterIp"), this.width / 2 - 100, 100, 10526880);
      this.serverTextField.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }
}
