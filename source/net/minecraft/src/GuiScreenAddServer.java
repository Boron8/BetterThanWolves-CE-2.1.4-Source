package net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiScreenAddServer extends GuiScreen {
   private GuiScreen parentGui;
   private GuiTextField serverAddress;
   private GuiTextField serverName;
   private ServerData newServerData;

   public GuiScreenAddServer(GuiScreen var1, ServerData var2) {
      this.parentGui = var1;
      this.newServerData = var2;
   }

   @Override
   public void updateScreen() {
      this.serverName.updateCursorCounter();
      this.serverAddress.updateCursorCounter();
   }

   @Override
   public void initGui() {
      StringTranslate var1 = StringTranslate.getInstance();
      Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("addServer.add")));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
      this.buttonList
         .add(
            new GuiButton(
               2,
               this.width / 2 - 100,
               142,
               var1.translateKey("addServer.hideAddress")
                  + ": "
                  + (this.newServerData.isHidingAddress() ? var1.translateKey("gui.yes") : var1.translateKey("gui.no"))
            )
         );
      this.serverName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 66, 200, 20);
      this.serverName.setFocused(true);
      this.serverName.setText(this.newServerData.serverName);
      this.serverAddress = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 106, 200, 20);
      this.serverAddress.setMaxStringLength(128);
      this.serverAddress.setText(this.newServerData.serverIP);
      ((GuiButton)this.buttonList.get(0)).enabled = this.serverAddress.getText().length() > 0
         && this.serverAddress.getText().split(":").length > 0
         && this.serverName.getText().length() > 0;
   }

   @Override
   public void onGuiClosed() {
      Keyboard.enableRepeatEvents(false);
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.enabled) {
         if (var1.id == 1) {
            this.parentGui.confirmClicked(false, 0);
         } else if (var1.id == 0) {
            this.newServerData.serverName = this.serverName.getText();
            this.newServerData.serverIP = this.serverAddress.getText();
            this.parentGui.confirmClicked(true, 0);
         } else if (var1.id == 2) {
            StringTranslate var2 = StringTranslate.getInstance();
            this.newServerData.setHideAddress(!this.newServerData.isHidingAddress());
            ((GuiButton)this.buttonList.get(2)).displayString = var2.translateKey("addServer.hideAddress")
               + ": "
               + (this.newServerData.isHidingAddress() ? var2.translateKey("gui.yes") : var2.translateKey("gui.no"));
         }
      }
   }

   @Override
   protected void keyTyped(char var1, int var2) {
      this.serverName.textboxKeyTyped(var1, var2);
      this.serverAddress.textboxKeyTyped(var1, var2);
      if (var1 == '\t') {
         if (this.serverName.isFocused()) {
            this.serverName.setFocused(false);
            this.serverAddress.setFocused(true);
         } else {
            this.serverName.setFocused(true);
            this.serverAddress.setFocused(false);
         }
      }

      if (var1 == '\r') {
         this.actionPerformed((GuiButton)this.buttonList.get(0));
      }

      ((GuiButton)this.buttonList.get(0)).enabled = this.serverAddress.getText().length() > 0
         && this.serverAddress.getText().split(":").length > 0
         && this.serverName.getText().length() > 0;
   }

   @Override
   protected void mouseClicked(int var1, int var2, int var3) {
      super.mouseClicked(var1, var2, var3);
      this.serverAddress.mouseClicked(var1, var2, var3);
      this.serverName.mouseClicked(var1, var2, var3);
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      StringTranslate var4 = StringTranslate.getInstance();
      this.e();
      this.a(this.fontRenderer, var4.translateKey("addServer.title"), this.width / 2, 17, 16777215);
      this.b(this.fontRenderer, var4.translateKey("addServer.enterName"), this.width / 2 - 100, 53, 10526880);
      this.b(this.fontRenderer, var4.translateKey("addServer.enterIp"), this.width / 2 - 100, 94, 10526880);
      this.serverName.drawTextBox();
      this.serverAddress.drawTextBox();
      super.drawScreen(var1, var2, var3);
   }
}
