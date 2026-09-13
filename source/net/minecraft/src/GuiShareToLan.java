package net.minecraft.src;

public class GuiShareToLan extends GuiScreen {
   private final GuiScreen parentScreen;
   private GuiButton buttonAllowCommandsToggle;
   private GuiButton buttonGameMode;
   private String gameMode = "survival";
   private boolean allowCommands = false;

   public GuiShareToLan(GuiScreen var1) {
      this.parentScreen = var1;
   }

   @Override
   public void initGui() {
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(101, this.width / 2 - 155, this.height - 28, 150, 20, StatCollector.translateToLocal("lanServer.start")));
      this.buttonList.add(new GuiButton(102, this.width / 2 + 5, this.height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
      this.buttonList.add(this.buttonGameMode = new GuiButton(104, this.width / 2 - 155, 100, 150, 20, StatCollector.translateToLocal("selectWorld.gameMode")));
      this.buttonList
         .add(
            this.buttonAllowCommandsToggle = new GuiButton(103, this.width / 2 + 5, 100, 150, 20, StatCollector.translateToLocal("selectWorld.allowCommands"))
         );
      this.func_74088_g();
   }

   private void func_74088_g() {
      StringTranslate var1 = StringTranslate.getInstance();
      this.buttonGameMode.displayString = var1.translateKey("selectWorld.gameMode") + " " + var1.translateKey("selectWorld.gameMode." + this.gameMode);
      this.buttonAllowCommandsToggle.displayString = var1.translateKey("selectWorld.allowCommands") + " ";
      if (this.allowCommands) {
         this.buttonAllowCommandsToggle.displayString = this.buttonAllowCommandsToggle.displayString + var1.translateKey("options.on");
      } else {
         this.buttonAllowCommandsToggle.displayString = this.buttonAllowCommandsToggle.displayString + var1.translateKey("options.off");
      }
   }

   @Override
   protected void actionPerformed(GuiButton var1) {
      if (var1.id == 102) {
         this.mc.displayGuiScreen(this.parentScreen);
      } else if (var1.id == 104) {
         if (this.gameMode.equals("survival")) {
            this.gameMode = "creative";
         } else if (this.gameMode.equals("creative")) {
            this.gameMode = "adventure";
         } else {
            this.gameMode = "survival";
         }

         this.func_74088_g();
      } else if (var1.id == 103) {
         this.allowCommands = !this.allowCommands;
         this.func_74088_g();
      } else if (var1.id == 101) {
         this.mc.displayGuiScreen(null);
         String var2 = this.mc.getIntegratedServer().shareToLAN(EnumGameType.getByName(this.gameMode), this.allowCommands);
         String var3 = "";
         if (var2 != null) {
            var3 = this.mc.thePlayer.a("commands.publish.started", new Object[]{var2});
         } else {
            var3 = this.mc.thePlayer.a("commands.publish.failed", new Object[0]);
         }

         this.mc.ingameGUI.getChatGUI().printChatMessage(var3);
      }
   }

   @Override
   public void drawScreen(int var1, int var2, float var3) {
      this.e();
      this.a(this.fontRenderer, StatCollector.translateToLocal("lanServer.title"), this.width / 2, 50, 16777215);
      this.a(this.fontRenderer, StatCollector.translateToLocal("lanServer.otherPlayers"), this.width / 2, 82, 16777215);
      super.drawScreen(var1, var2, var3);
   }
}
